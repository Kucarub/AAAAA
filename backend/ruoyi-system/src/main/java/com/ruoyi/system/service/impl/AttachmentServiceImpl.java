package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AttachmentMapper;
import com.ruoyi.system.domain.Attachment;
import com.ruoyi.system.service.IAttachmentService;

@Service
public class AttachmentServiceImpl implements IAttachmentService 
{
    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public Attachment selectAttachmentById(Long id)
    {
        return attachmentMapper.selectAttachmentById(id);
    }

    @Override
    public List<Attachment> selectAttachmentList(Attachment attachment)
    {
        return attachmentMapper.selectAttachmentList(attachment);
    }

    @Override
    public int insertAttachment(Attachment attachment)
    {
        attachment.setCreatedTime(DateUtils.getNowDate());
        attachment.setUploadTime(DateUtils.getNowDate());
        return attachmentMapper.insertAttachment(attachment);
    }

    @Override
    public int updateAttachment(Attachment attachment)
    {
        attachment.setUpdatedTime(DateUtils.getNowDate());
        return attachmentMapper.updateAttachment(attachment);
    }

    @Override
    public int deleteAttachmentByIds(Long[] ids)
    {
        return attachmentMapper.deleteAttachmentByIds(ids);
    }

    @Override
    public int deleteAttachmentById(Long id)
    {
        return attachmentMapper.deleteAttachmentById(id);
    }
}
