package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PDocumentVersion;

public interface PDocumentVersionMapper
{
    public PDocumentVersion selectPDocumentVersionById(Long id);
    public List<PDocumentVersion> selectPDocumentVersionList(PDocumentVersion pDocumentVersion);
    public int insertPDocumentVersion(PDocumentVersion pDocumentVersion);
    public int updatePDocumentVersion(PDocumentVersion pDocumentVersion);
    public int deletePDocumentVersionById(Long id);
    public int deletePDocumentVersionByIds(Long[] ids);
    public int deletePDocumentVersionByDocumentId(Long documentId);
}
