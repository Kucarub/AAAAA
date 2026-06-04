package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.PProject;

public interface PProjectMapper 
{
    public PProject selectPProjectById(Long id);
    public List<PProject> selectPProjectList(PProject pProject);
    public int insertPProject(PProject pProject);
    public int updatePProject(PProject pProject);
    public int deletePProjectById(Long id);
    public int deletePProjectByIds(Long[] ids);
}
