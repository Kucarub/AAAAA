package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Attachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "原始文件名")
    private String originalName;

    @Excel(name = "存储文件名")
    private String storeName;

    @Excel(name = "存储路径")
    private String storePath;

    @Excel(name = "完整访问URL")
    private String fullUrl;

    @Excel(name = "文件大小", readConverterExp = "字=节")
    private Long fileSize;

    @Excel(name = "文件MIME类型")
    private String fileType;

    @Excel(name = "文件后缀")
    private String suffix;

    @Excel(name = "上传人ID")
    private Long uploadBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOriginalName(String originalName) 
    {
        this.originalName = originalName;
    }

    public String getOriginalName() 
    {
        return originalName;
    }
    public void setStoreName(String storeName) 
    {
        this.storeName = storeName;
    }

    public String getStoreName() 
    {
        return storeName;
    }
    public void setStorePath(String storePath) 
    {
        this.storePath = storePath;
    }

    public String getStorePath() 
    {
        return storePath;
    }
    public void setFullUrl(String fullUrl) 
    {
        this.fullUrl = fullUrl;
    }

    public String getFullUrl() 
    {
        return fullUrl;
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
    public void setSuffix(String suffix) 
    {
        this.suffix = suffix;
    }

    public String getSuffix() 
    {
        return suffix;
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
    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }
    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("originalName", getOriginalName())
            .append("storeName", getStoreName())
            .append("storePath", getStorePath())
            .append("fullUrl", getFullUrl())
            .append("fileSize", getFileSize())
            .append("fileType", getFileType())
            .append("suffix", getSuffix())
            .append("uploadBy", getUploadBy())
            .append("uploadTime", getUploadTime())
            .append("remark", getRemark())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
