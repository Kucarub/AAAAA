package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PDocumentVersion;

public interface IPDocumentVersionService
{
    public PDocumentVersion selectPDocumentVersionById(Long id);
    public List<PDocumentVersion> selectPDocumentVersionList(PDocumentVersion pDocumentVersion);
    public int insertPDocumentVersion(PDocumentVersion pDocumentVersion);
    public int updatePDocumentVersion(PDocumentVersion pDocumentVersion);
    public int deletePDocumentVersionByIds(Long[] ids);
    public int deletePDocumentVersionById(Long id);
}
