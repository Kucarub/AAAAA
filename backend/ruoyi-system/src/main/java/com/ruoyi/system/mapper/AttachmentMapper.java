package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Attachment;

public interface AttachmentMapper 
{
    public Attachment selectAttachmentById(Long id);
    public List<Attachment> selectAttachmentList(Attachment attachment);
    public int insertAttachment(Attachment attachment);
    public int updateAttachment(Attachment attachment);
    public int deleteAttachmentById(Long id);
    public int deleteAttachmentByIds(Long[] ids);
}
