package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.PdfConversion;
import com.ruoyi.system.service.IPdfConversionService;

/**
 * PDF转换Controller
 *
 * @author ruoyi
 * @date 2026-06-02
 */
@RestController
@RequestMapping("/system/pdf")
public class PdfConversionController extends BaseController
{
    @Autowired
    private IPdfConversionService pdfConversionService;

    /**
     * 查询PDF转换列表
     */
    @PreAuthorize("@ss.hasPermi('system:pdf:list')")
    @GetMapping("/list")
    public TableDataInfo list(PdfConversion pdfConversion)
    {
        startPage();
        List<PdfConversion> list = pdfConversionService.selectPdfConversionList(pdfConversion);
        return getDataTable(list);
    }

    /**
     * 导出PDF转换列表
     */
    @PreAuthorize("@ss.hasPermi('system:pdf:export')")
    @Log(title = "PDF转换", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, PdfConversion pdfConversion)
    {
        List<PdfConversion> list = pdfConversionService.selectPdfConversionList(pdfConversion);
        ExcelUtil<PdfConversion> util = new ExcelUtil<PdfConversion>(PdfConversion.class);
        util.exportExcel(response, list, "PDF转换数据");
    }

    /**
     * 获取PDF转换详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:pdf:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pdfConversionService.selectPdfConversionById(id));
    }

    /**
     * 获取或转换附件为PDF并返回路径
     */
    @GetMapping("/convert/{attachmentId}")
    public AjaxResult convert(@PathVariable("attachmentId") Long attachmentId)
    {
        try {
            String pdfPath = pdfConversionService.getOrConvertToPdf(attachmentId);
            return success(pdfPath);
        } catch (Exception e) {
            return error("转换失败：" + e.getMessage());
        }
    }

    /**
     * 下载转换后的PDF
     */
    @GetMapping("/download/{attachmentId}")
    public void downloadPdf(HttpServletResponse response, @PathVariable("attachmentId") Long attachmentId)
    {
        try {
            String pdfPath = pdfConversionService.getOrConvertToPdf(attachmentId);
            String localPath = RuoYiConfig.getProfile() + pdfPath;
            PdfConversion conversion = pdfConversionService.selectPdfConversionByAttachmentId(attachmentId);
            String filename = conversion != null ? conversion.getPdfFilename() : "document.pdf";
            response.setContentType("application/pdf");
            FileUtils.setAttachmentResponseHeader(response, filename);
            FileUtils.writeBytes(localPath, response.getOutputStream());
        } catch (Exception e) {
            logger.error("下载PDF失败", e);
        }
    }

    /**
     * 预览文件（直接返回文件流）
     */
    @GetMapping("/preview/{attachmentId}")
    public void previewPdf(HttpServletResponse response, @PathVariable("attachmentId") Long attachmentId)
    {
        try {
            String pdfPath = pdfConversionService.getOrConvertToPdf(attachmentId);
            String localPath = RuoYiConfig.getProfile() + pdfPath;
            PdfConversion conversion = pdfConversionService.selectPdfConversionByAttachmentId(attachmentId);
            String filename = conversion != null ? conversion.getPdfFilename() : "document";
            
            // 根据文件扩展名设置正确的 Content-Type
            String contentType = getContentType(filename);
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline; filename=" + FileUtils.percentEncode(filename));
            FileUtils.writeBytes(localPath, response.getOutputStream());
        } catch (Exception e) {
            logger.error("预览文件失败", e);
        }
    }

    /**
     * 根据文件名获取 Content-Type
     */
    private String getContentType(String filename)
    {
        if (filename == null) {
            return "application/octet-stream";
        }
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lowerFilename.endsWith(".doc")) {
            return "application/msword";
        } else if (lowerFilename.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (lowerFilename.endsWith(".xls")) {
            return "application/vnd.ms-excel";
        } else if (lowerFilename.endsWith(".xlsx")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else if (lowerFilename.endsWith(".ppt")) {
            return "application/vnd.ms-powerpoint";
        } else if (lowerFilename.endsWith(".pptx")) {
            return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        } else if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFilename.endsWith(".png")) {
            return "image/png";
        } else if (lowerFilename.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFilename.endsWith(".txt")) {
            return "text/plain";
        }
        return "application/octet-stream";
    }
}
