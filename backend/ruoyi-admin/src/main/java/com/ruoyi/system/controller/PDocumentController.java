package com.ruoyi.system.controller;

import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.ruoyi.system.domain.PDocument;
import com.ruoyi.system.domain.PDocumentVersion;
import com.ruoyi.system.domain.Attachment;
import com.ruoyi.system.service.IPDocumentService;
import com.ruoyi.system.service.IPDocumentVersionService;
import com.ruoyi.system.service.IAttachmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 资料Controller
 *
 * @author ruoyi
 * @date 2026-04-27
 */
@RestController
@RequestMapping("/system/document")
public class PDocumentController extends BaseController
{
    @Autowired
    private IPDocumentService pDocumentService;

    @Autowired
    private IPDocumentVersionService pDocumentVersionService;

    @Autowired
    private IAttachmentService attachmentService;

    /**
     * 查询资料列表
     */
    @PreAuthorize("@ss.hasPermi('mamage:document:list')")
    @GetMapping("/list")
    public TableDataInfo list(PDocument pDocument) {
        startPage();
        List<PDocument> list;
        if (pDocument.getProjectId() != null) {
            list = pDocumentService.selectLatestDocumentList(pDocument);
        } else {
            list = pDocumentService.selectAllLatestDocumentList(pDocument);
        }
        return getDataTable(list);
    }

    /**
     * 导出资料列表
     */
    @PreAuthorize("@ss.hasPermi('mamage:document:export')")
    @Log(title = "资料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PDocument pDocument)
    {
        List<PDocument> list = pDocumentService.selectPDocumentList(pDocument);
        ExcelUtil<PDocument> util = new ExcelUtil<PDocument>(PDocument.class);
        util.exportExcel(response, list, "资料数据");
    }

    /**
     * 获取资料详细信息
     */
    @PreAuthorize("@ss.hasPermi('mamage:document:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pDocumentService.selectPDocumentById(id));
    }

    /**
     * 新增资料
     */
    @PreAuthorize("@ss.hasPermi('mamage:document:add')")
    @Log(title = "资料", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(@RequestBody PDocument pDocument)
    {
        // 检查资料名称和版本号是否重复
        int count = pDocumentService.checkDuplicateNameAndVersion(pDocument);
        if (count > 0) {
            return error("资料名称和版本号已存在，不能重复添加");
        }

        Date now = new java.util.Date();
        pDocument.setCreatedBy(getUserId());
        pDocument.setCreatedTime(now);

        // 插入资料
        int result = pDocumentService.insertPDocument(pDocument);
        if (result > 0) {
            // 保存版本记录（使用现有表结构）
            PDocumentVersion version = new PDocumentVersion();
            version.setDocumentId(pDocument.getId());
            version.setVersionNumber(pDocument.getVersion());
            version.setRemark(pDocument.getRemark());
            version.setIsCurrent(1);
            version.setUploadBy(getUserId());
            version.setUploadTime(now);

            // 如果有附件ID，关联附件信息
            if (pDocument.getAttachmentId() != null) {
                Attachment attachment = attachmentService.selectAttachmentById(pDocument.getAttachmentId());
                if (attachment != null) {
                    version.setAttachmentId(attachment.getId());
                    version.setFileName(attachment.getOriginalName());
                    version.setFilePath(attachment.getStorePath());
                    version.setFileSize(attachment.getFileSize());
                    version.setFileType(attachment.getFileType());
                }
            } else {
                // 暂时使用默认值填充必填字段
                version.setFileName("placeholder");
                version.setFilePath("placeholder");
            }

            pDocumentVersionService.insertPDocumentVersion(version);
        }

        return toAjax(result);
    }

    /**
     * 修改资料
     */
    @PreAuthorize("@ss.hasPermi('mamage:document:edit')")
    @Log(title = "资料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PDocument pDocument)
    {
        return toAjax(pDocumentService.updatePDocument(pDocument));
    }

    /**
     * 删除资料
     */
    @Log(title = "资料", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pDocumentService.deletePDocumentByIds(ids));
    }

    /**
     * 查询资料的所有版本
     */
    @PreAuthorize("@ss.hasPermi('mamage:document:list')")
    @GetMapping("/versions/{documentId}")
    public AjaxResult getVersions(@PathVariable Long documentId)
    {
        PDocumentVersion versionQuery = new PDocumentVersion();
        versionQuery.setDocumentId(documentId);
        List<PDocumentVersion> versions = pDocumentVersionService.selectPDocumentVersionList(versionQuery);
        return success(versions);
    }

    /**
     * 根据资料名称和版本号查询资料详情
     */
    @PreAuthorize("@ss.hasPermi('mamage:document:list')")
    @GetMapping("/detail")
    public AjaxResult getDetailByNameAndVersion(String name, String version)
    {
        PDocument docQuery = new PDocument();
        docQuery.setName(name);
        List<PDocument> docs = pDocumentService.selectPDocumentList(docQuery);
        if (docs.isEmpty()) {
            return error("资料不存在");
        }
        // 获取该资料的所有版本
        PDocumentVersion versionQuery = new PDocumentVersion();
        versionQuery.setDocumentId(docs.get(0).getId());
        versionQuery.setVersionNumber(version);
        List<PDocumentVersion> versions = pDocumentVersionService.selectPDocumentVersionList(versionQuery);
        if (versions.isEmpty()) {
            return error("版本不存在");
        }
        PDocumentVersion docVersion = versions.get(0);

        PDocument result = new PDocument();
        result.setId(docs.get(0).getId());
        result.setName(docs.get(0).getName());
        result.setDescription(docs.get(0).getDescription());
        result.setRemark(docVersion.getRemark());
        result.setVersion(docVersion.getVersionNumber());
        result.setCreatedBy(docs.get(0).getCreatedBy());
        result.setCreatedTime(docs.get(0).getCreatedTime());

        // 获取附件信息
        if (docVersion.getAttachmentId() != null) {
            Attachment attachment = attachmentService.selectAttachmentById(docVersion.getAttachmentId());
            if (attachment != null) {
                result.setFilePath(attachment.getStorePath());
                result.setFileName(attachment.getOriginalName());
            }
        }
        return success(result);
    }
}
