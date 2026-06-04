package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PProject;

public interface IPProjectService 
{
    public PProject selectPProjectById(Long id);
    public List<PProject> selectPProjectList(PProject pProject);
    public int insertPProject(PProject pProject);
    public int updatePProject(PProject pProject);
    public int deletePProjectByIds(Long[] ids);
    public int deletePProjectById(Long id);
}
