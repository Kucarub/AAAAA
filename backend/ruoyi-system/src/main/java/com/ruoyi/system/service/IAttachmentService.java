package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Attachment;

public interface IAttachmentService 
{
    public Attachment selectAttachmentById(Long id);
    public List<Attachment> selectAttachmentList(Attachment attachment);
    public int insertAttachment(Attachment attachment);
    public int updateAttachment(Attachment attachment);
    public int deleteAttachmentByIds(Long[] ids);
    public int deleteAttachmentById(Long id);
}
