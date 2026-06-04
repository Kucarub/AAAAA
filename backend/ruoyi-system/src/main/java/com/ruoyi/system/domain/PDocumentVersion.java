package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class PDocumentVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "所属资料ID")
    private Long documentId;

    @Excel(name = "附件ID")
    private Long attachmentId;

    @Excel(name = "版本号")
    private String versionNumber;

    @Excel(name = "附件原始文件名")
    private String fileName;

    @Excel(name = "附件存储路径")
    private String filePath;

    @Excel(name = "文件大小")
    private Long fileSize;

    @Excel(name = "文件类型")
    private String fileType;

    @Excel(name = "版本备注")
    private String remark;

    @Excel(name = "是否为当前版本")
    private Integer isCurrent;

    @Excel(name = "上传人ID")
    private Long uploadBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDocumentId(Long documentId) 
    {
        this.documentId = documentId;
    }

    public Long getDocumentId() 
    {
        return documentId;
    }

    public void setAttachmentId(Long attachmentId) 
    {
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId() 
    {
        return attachmentId;
    }

    public void setVersionNumber(String versionNumber) 
    {
        this.versionNumber = versionNumber;
    }

    public String getVersionNumber() 
    {
        return versionNumber;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setRemark(String remark) 
    {
        this.remark = remark;
    }

    public String getRemark() 
    {
        return remark;
    }

    public void setIsCurrent(Integer isCurrent) 
    {
        this.isCurrent = isCurrent;
    }

    public Integer getIsCurrent() 
    {
        return isCurrent;
    }

    public void setUploadBy(Long uploadBy) 
    {
        this.uploadBy = uploadBy;
    }

    public Long getUploadBy() 
    {
        return uploadBy;
    }

    public void setUploadTime(Date uploadTime) 
    {
        this.uploadTime = uploadTime;
    }

    public Date getUploadTime() 
    {
        return uploadTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("documentId", getDocumentId())
            .append("attachmentId", getAttachmentId())
            .append("versionNumber", getVersionNumber())
            .append("fileName", getFileName())
            .append("filePath", getFilePath())
            .append("fileSize", getFileSize())
            .append("fileType", getFileType())
            .append("remark", getRemark())
            .append("isCurrent", getIsCurrent())
            .append("uploadBy", getUploadBy())
            .append("uploadTime", getUploadTime())
            .toString();
    }
}
