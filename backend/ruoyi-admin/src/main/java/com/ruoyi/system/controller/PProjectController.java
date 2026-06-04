package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.PProject;
import com.ruoyi.system.service.IPProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目Controller
 *
 * @author ruoyi
 * @date 2026-04-21
 */
@RestController
@RequestMapping("/system/project")
public class PProjectController extends BaseController
{

    @Autowired
    private IPProjectService pProjectService;

    /**
     * 查询项目列表
     */
    @PreAuthorize("@ss.hasPermi('mamage:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(PProject pProject)
    {
        startPage();
        List<PProject> list = pProjectService.selectPProjectList(pProject);
        return getDataTable(list);
    }

    /**
     * 导出项目列表
     */
    @PreAuthorize("@ss.hasPermi('mamage:project:export')")
    @Log(title = "项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PProject pProject)
    {
        List<PProject> list = pProjectService.selectPProjectList(pProject);
        ExcelUtil<PProject> util = new ExcelUtil<PProject>(PProject.class);
        util.exportExcel(response, list, "项目数据");
    }

    /**
     * 获取项目详细信息
     */
    @PreAuthorize("@ss.hasPermi('mamage:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pProjectService.selectPProjectById(id));
    }

    /**
     * 新增项目
     */
    @PreAuthorize("@ss.hasPermi('mamage:project:add')")
    @Log(title = "项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PProject pProject)
    {
        return toAjax(pProjectService.insertPProject(pProject));
    }

    /**
     * 修改项目
     */
    @PreAuthorize("@ss.hasPermi('mamage:project:edit')")
    @Log(title = "项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PProject pProject)
    {
        return toAjax(pProjectService.updatePProject(pProject));
    }

    /**
     * 删除项目
     */
    @PreAuthorize("@ss.hasPermi('mamage:project:remove')")
    @Log(title = "项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pProjectService.deletePProjectByIds(ids));
    }
}
