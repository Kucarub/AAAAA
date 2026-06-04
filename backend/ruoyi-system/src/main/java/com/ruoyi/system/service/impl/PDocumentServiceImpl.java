package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PDocumentMapper;
import com.ruoyi.system.domain.PDocument;
import com.ruoyi.system.service.IPDocumentService;

@Service
public class PDocumentServiceImpl implements IPDocumentService
{
    @Autowired
    private PDocumentMapper pDocumentMapper;

    @Override
    public PDocument selectPDocumentById(Long id)
    {
        return pDocumentMapper.selectPDocumentById(id);
    }

    @Override
    public List<PDocument> selectPDocumentList(PDocument pDocument)
    {
        return pDocumentMapper.selectPDocumentList(pDocument);
    }

    @Override
    public int insertPDocument(PDocument pDocument)
    {
        return pDocumentMapper.insertPDocument(pDocument);
    }

    @Override
    public int updatePDocument(PDocument pDocument)
    {
        return pDocumentMapper.updatePDocument(pDocument);
    }

    @Override
    public int deletePDocumentByIds(Long[] ids)
    {
        return pDocumentMapper.deletePDocumentByIds(ids);
    }

    @Override
    public int deletePDocumentById(Long id)
    {
        return pDocumentMapper.deletePDocumentById(id);
    }

    @Override
    public int checkDuplicateNameAndVersion(PDocument pDocument)
    {
        return pDocumentMapper.checkDuplicateNameAndVersion(pDocument);
    }

    @Override
    public List<PDocument> selectLatestDocumentList(PDocument pDocument)
    {
        return pDocumentMapper.selectLatestDocumentList(pDocument);
    }

    @Override
    public List<PDocument> selectAllLatestDocumentList(PDocument pDocument)
    {
        return pDocumentMapper.selectAllLatestDocumentList(pDocument);
    }
}
