package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PdfConversion;

public interface IPdfConversionService
{
    public PdfConversion selectPdfConversionById(Long id);
    public PdfConversion selectPdfConversionByAttachmentId(Long attachmentId);
    public List<PdfConversion> selectPdfConversionList(PdfConversion pdfConversion);
    public int insertPdfConversion(PdfConversion pdfConversion);
    public int updatePdfConversion(PdfConversion pdfConversion);
    public int updateLastAccessTime(Long id);
    public int deletePdfConversionByIds(Long[] ids);
    public int deletePdfConversionById(Long id);
    public PdfConversion convertToPdf(Long attachmentId) throws Exception;
    public String getOrConvertToPdf(Long attachmentId) throws Exception;
}
