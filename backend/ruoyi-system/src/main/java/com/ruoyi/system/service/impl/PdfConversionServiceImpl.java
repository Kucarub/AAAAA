package com.ruoyi.system.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
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
                String pdfRelativePath = FileUtils.stripPrefix(conversion.getPdfStorePath());
                String localPath = RuoYiConfig.getProfile() + File.separator + pdfRelativePath;
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
            String pdfRelativePath = FileUtils.stripPrefix(conversion.getPdfStorePath());
            String localPath = RuoYiConfig.getProfile() + File.separator + pdfRelativePath;
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
            throw new RuntimeException("不支持的文件格式，仅支持doc、docx、xls、xlsx格式");
        }

        // 去掉路径前缀（如 /profile），只保留相对路径
        String relativePath = FileUtils.stripPrefix(attachment.getStorePath());
        String sourcePath = RuoYiConfig.getProfile() + File.separator + relativePath;
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new RuntimeException("源文件不存在: " + sourceFile.getAbsolutePath());
        }

        String pdfFilename = StringUtils.substringBeforeLast(originalName, ".") + ".pdf";
        String pdfStorePath = attachment.getStorePath().replace(StringUtils.substringAfterLast(attachment.getStorePath(), "."), "pdf");
        String pdfRelativePath = FileUtils.stripPrefix(pdfStorePath);
        String pdfPath = RuoYiConfig.getProfile() + File.separator + pdfRelativePath;

        if (originalName.toLowerCase().endsWith(".pdf")) {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(pdfPath);
            Files.copy(source, target);
        } else {
            try (InputStream inputStream = new FileInputStream(sourceFile);
                 OutputStream outputStream = new FileOutputStream(pdfPath)) {
                IConverter converter = LocalConverter.builder().build();
                DocumentType docType;
                if (originalName.toLowerCase().endsWith(".doc")) {
                    docType = DocumentType.DOC;
                } else if (originalName.toLowerCase().endsWith(".docx")) {
                    docType = DocumentType.DOCX;
                } else if (originalName.toLowerCase().endsWith(".xls")) {
                    docType = DocumentType.XLS;
                } else {
                    docType = DocumentType.XLSX;
                }
                converter.convert(inputStream).as(docType)
                    .to(outputStream).as(DocumentType.PDF).execute();
                converter.shutDown();
            }
        }

        File pdfFile = new File(pdfPath);

        PdfConversion conversion = new PdfConversion();
        conversion.setAttachmentId(attachmentId);
        conversion.setOriginalFilename(originalName);
        conversion.setPdfFilename(pdfFilename);
        conversion.setPdfStorePath(pdfStorePath);
        conversion.setPdfFileSize(pdfFile.length());
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
            String pdfRelativePath = FileUtils.stripPrefix(conversion.getPdfStorePath());
            String pdfPath = RuoYiConfig.getProfile() + File.separator + pdfRelativePath;
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
        return suffix.endsWith(".doc") || suffix.endsWith(".docx")
            || suffix.endsWith(".xls") || suffix.endsWith(".xlsx") || suffix.endsWith(".pdf");
    }
}
