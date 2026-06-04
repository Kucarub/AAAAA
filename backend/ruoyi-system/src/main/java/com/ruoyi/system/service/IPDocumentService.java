package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PDocument;

public interface IPDocumentService
{
    public PDocument selectPDocumentById(Long id);
    public List<PDocument> selectPDocumentList(PDocument pDocument);
    public int insertPDocument(PDocument pDocument);
    public int updatePDocument(PDocument pDocument);
    public int deletePDocumentByIds(Long[] ids);
    public int deletePDocumentById(Long id);
    public int checkDuplicateNameAndVersion(PDocument pDocument);
    public List<PDocument> selectLatestDocumentList(PDocument pDocument);
    public List<PDocument> selectAllLatestDocumentList(PDocument pDocument);
}
