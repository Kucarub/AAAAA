package com.ruoyi.system.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Attachment;
import com.ruoyi.system.domain.PdfConversion;
import com.ruoyi.system.mapper.PdfConversionMapper;
import com.ruoyi.system.mapper.AttachmentMapper;
import com.ruoyi.system.service.IPdfConversionService;

@Service
public class PdfConversionServiceImpl implements IPdfConversionService
{
    @Autowired
    private PdfConversionMapper pdfConversionMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public PdfConversion selectPdfConversionById(Long id)
    {
        return pdfConversionMapper.selectPdfConversionById(id);
    }

    @Override
    public PdfConversion selectPdfConversionByAttachmentId(Long attachmentId)
    {
        return pdfConversionMapper.selectPdfConversionByAttachmentId(attachmentId);
    }

    @Override
    public List<PdfConversion> selectPdfConversionList(PdfConversion pdfConversion)
    {
        return pdfConversionMapper.selectPdfConversionList(pdfConversion);
    }

    @Override
    public int insertPdfConversion(PdfConversion pdfConversion)
    {
        pdfConversion.setCreatedTime(DateUtils.getNowDate());
        return pdfConversionMapper.insertPdfConversion(pdfConversion);
    }

    @Override
    public int updatePdfConversion(PdfConversion pdfConversion)
    {
        pdfConversion.setUpdatedTime(DateUtils.getNowDate());
        return pdfConversionMapper.updatePdfConversion(pdfConversion);
    }

    @Override
    public int updateLastAccessTime(Long id)
    {
        return pdfConversionMapper.updateLastAccessTime(id);
    }

    @Override
    public int deletePdfConversionByIds(Long[] ids)
    {
        for (Long id : ids) {
            PdfConversion conversion = pdfConversionMapper.selectPdfConversionById(id);
            if (conversion != null && StringUtils.isNotEmpty(conversion.getPdfStorePath())) {
                String localPath = RuoYiConfig.getProfile() + conversion.getPdfStorePath();
                FileUtils.deleteFile(localPath);
            }
        }
        return pdfConversionMapper.deletePdfConversionByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePdfConversionById(Long id)
    {
        PdfConversion conversion = pdfConversionMapper.selectPdfConversionById(id);
        if (conversion != null && StringUtils.isNotEmpty(conversion.getPdfStorePath())) {
            String localPath = RuoYiConfig.getProfile() + conversion.getPdfStorePath();
            FileUtils.deleteFile(localPath);
        }
        return pdfConversionMapper.deletePdfConversionById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PdfConversion convertToPdf(Long attachmentId) throws Exception
    {
        Attachment attachment = attachmentMapper.selectAttachmentById(attachmentId);
        if (attachment == null) {
            throw new RuntimeException("附件不存在");
        }

        String originalName = attachment.getOriginalName();
        if (!isSupportedFormat(originalName)) {
            throw new RuntimeException("不支持的文件格式，仅支持doc、docx格式");
        }

        String sourcePath = RuoYiConfig.getProfile() + attachment.getStorePath();
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new RuntimeException("源文件不存在");
        }

        PdfConversion conversion = new PdfConversion();
        conversion.setAttachmentId(attachmentId);
        conversion.setOriginalFilename(originalName);
        conversion.setPdfFilename(originalName);
        conversion.setPdfStorePath(attachment.getStorePath());
        conversion.setPdfFileSize(sourceFile.length());
        conversion.setLastAccessTime(DateUtils.getNowDate());
        conversion.setCreatedTime(DateUtils.getNowDate());

        pdfConversionMapper.insertPdfConversion(conversion);

        return conversion;
    }

    @Override
    public String getOrConvertToPdf(Long attachmentId) throws Exception
    {
        PdfConversion conversion = pdfConversionMapper.selectPdfConversionByAttachmentId(attachmentId);
        
        if (conversion != null) {
            String pdfPath = RuoYiConfig.getProfile() + conversion.getPdfStorePath();
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                pdfConversionMapper.updateLastAccessTime(conversion.getId());
                return conversion.getPdfStorePath();
            }
        }

        conversion = convertToPdf(attachmentId);
        return conversion.getPdfStorePath();
    }

    private boolean isSupportedFormat(String filename)
    {
        if (StringUtils.isEmpty(filename)) {
            return false;
        }
        String suffix = filename.toLowerCase();
        return suffix.endsWith(".doc") || suffix.endsWith(".docx") || suffix.endsWith(".pdf");
    }
}
