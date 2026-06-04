package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PDocument;

public interface PDocumentMapper
{
    public PDocument selectPDocumentById(Long id);
    public List<PDocument> selectPDocumentList(PDocument pDocument);
    public int insertPDocument(PDocument pDocument);
    public int updatePDocument(PDocument pDocument);
    public int deletePDocumentById(Long id);
    public int deletePDocumentByIds(Long[] ids);
    public int checkDuplicateNameAndVersion(PDocument pDocument);
    public List<PDocument> selectLatestDocumentList(PDocument pDocument);
    public int deletePDocumentByProjectId(Long projectId);
    public List<PDocument> selectAllLatestDocumentList(PDocument pDocument);
}
