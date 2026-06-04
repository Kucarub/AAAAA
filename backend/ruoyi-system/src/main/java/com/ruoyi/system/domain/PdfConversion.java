package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class PdfConversion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "源附件ID")
    private Long attachmentId;

    @Excel(name = "源文件名")
    private String originalFilename;

    @Excel(name = "PDF文件名")
    private String pdfFilename;

    @Excel(name = "PDF存储路径")
    private String pdfStorePath;

    @Excel(name = "PDF文件大小", readConverterExp = "字节")
    private Long pdfFileSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastAccessTime;

    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
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

    public void setAttachmentId(Long attachmentId) 
    {
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId() 
    {
        return attachmentId;
    }

    public void setOriginalFilename(String originalFilename) 
    {
        this.originalFilename = originalFilename;
    }

    public String getOriginalFilename() 
    {
        return originalFilename;
    }

    public void setPdfFilename(String pdfFilename) 
    {
        this.pdfFilename = pdfFilename;
    }

    public String getPdfFilename() 
    {
        return pdfFilename;
    }

    public void setPdfStorePath(String pdfStorePath) 
    {
        this.pdfStorePath = pdfStorePath;
    }

    public String getPdfStorePath() 
    {
        return pdfStorePath;
    }

    public void setPdfFileSize(Long pdfFileSize) 
    {
        this.pdfFileSize = pdfFileSize;
    }

    public Long getPdfFileSize() 
    {
        return pdfFileSize;
    }

    public void setLastAccessTime(Date lastAccessTime) 
    {
        this.lastAccessTime = lastAccessTime;
    }

    public Date getLastAccessTime() 
    {
        return lastAccessTime;
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
            .append("attachmentId", getAttachmentId())
            .append("originalFilename", getOriginalFilename())
            .append("pdfFilename", getPdfFilename())
            .append("pdfStorePath", getPdfStorePath())
            .append("pdfFileSize", getPdfFileSize())
            .append("lastAccessTime", getLastAccessTime())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
