package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.PProjectMapper;
import com.ruoyi.system.mapper.PDocumentMapper;
import com.ruoyi.system.mapper.PDocumentVersionMapper;
import com.ruoyi.system.domain.PProject;
import com.ruoyi.system.domain.PDocument;
import com.ruoyi.system.service.IPProjectService;

@Service
public class PProjectServiceImpl implements IPProjectService 
{
    @Autowired
    private PProjectMapper pProjectMapper;
    
    @Autowired
    private PDocumentMapper pDocumentMapper;
    
    @Autowired
    private PDocumentVersionMapper pDocumentVersionMapper;

    @Override
    public PProject selectPProjectById(Long id)
    {
        return pProjectMapper.selectPProjectById(id);
    }

    @Override
    public List<PProject> selectPProjectList(PProject pProject)
    {
        return pProjectMapper.selectPProjectList(pProject);
    }

    @Override
    public int insertPProject(PProject pProject)
    {
        return pProjectMapper.insertPProject(pProject);
    }

    @Override
    public int updatePProject(PProject pProject)
    {
        return pProjectMapper.updatePProject(pProject);
    }

    @Override
    @Transactional
    public int deletePProjectByIds(Long[] ids)
    {
        for (Long id : ids) {
            deleteProjectRelatedData(id);
        }
        return pProjectMapper.deletePProjectByIds(ids);
    }

    @Override
    @Transactional
    public int deletePProjectById(Long id)
    {
        deleteProjectRelatedData(id);
        return pProjectMapper.deletePProjectById(id);
    }
    
    private void deleteProjectRelatedData(Long projectId) {
        PDocument docQuery = new PDocument();
        docQuery.setProjectId(projectId);
        List<PDocument> docs = pDocumentMapper.selectPDocumentList(docQuery);
        
        for (PDocument doc : docs) {
            pDocumentVersionMapper.deletePDocumentVersionByDocumentId(doc.getId());
        }
        
        pDocumentMapper.deletePDocumentByProjectId(projectId);
    }
}
