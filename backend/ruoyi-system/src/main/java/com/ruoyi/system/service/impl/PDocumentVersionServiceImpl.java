package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PDocumentVersionMapper;
import com.ruoyi.system.domain.PDocumentVersion;
import com.ruoyi.system.service.IPDocumentVersionService;

@Service
public class PDocumentVersionServiceImpl implements IPDocumentVersionService
{
    @Autowired
    private PDocumentVersionMapper pDocumentVersionMapper;

    @Override
    public PDocumentVersion selectPDocumentVersionById(Long id)
    {
        return pDocumentVersionMapper.selectPDocumentVersionById(id);
    }

    @Override
    public List<PDocumentVersion> selectPDocumentVersionList(PDocumentVersion pDocumentVersion)
    {
        return pDocumentVersionMapper.selectPDocumentVersionList(pDocumentVersion);
    }

    @Override
    public int insertPDocumentVersion(PDocumentVersion pDocumentVersion)
    {
        return pDocumentVersionMapper.insertPDocumentVersion(pDocumentVersion);
    }

    @Override
    public int updatePDocumentVersion(PDocumentVersion pDocumentVersion)
    {
        return pDocumentVersionMapper.updatePDocumentVersion(pDocumentVersion);
    }

    @Override
    public int deletePDocumentVersionByIds(Long[] ids)
    {
        return pDocumentVersionMapper.deletePDocumentVersionByIds(ids);
    }

    @Override
    public int deletePDocumentVersionById(Long id)
    {
        return pDocumentVersionMapper.deletePDocumentVersionById(id);
    }
}
