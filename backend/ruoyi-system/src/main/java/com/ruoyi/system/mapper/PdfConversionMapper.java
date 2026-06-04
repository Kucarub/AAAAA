package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PdfConversion;

public interface PdfConversionMapper
{
    public PdfConversion selectPdfConversionById(Long id);
    public PdfConversion selectPdfConversionByAttachmentId(Long attachmentId);
    public List<PdfConversion> selectPdfConversionList(PdfConversion pdfConversion);
    public List<PdfConversion> selectExpiredRecords();
    public int insertPdfConversion(PdfConversion pdfConversion);
    public int updatePdfConversion(PdfConversion pdfConversion);
    public int updateLastAccessTime(Long id);
    public int deletePdfConversionById(Long id);
    public int deletePdfConversionByIds(Long[] ids);
}
