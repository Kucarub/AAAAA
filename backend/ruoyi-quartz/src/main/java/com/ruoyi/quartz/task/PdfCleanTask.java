package com.ruoyi.quartz.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.PdfConversion;
import com.ruoyi.system.mapper.PdfConversionMapper;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * PDF清理定时任务
 *
 * @author ruoyi
 * @date 2026-06-02
 */
@Component("pdfCleanTask")
public class PdfCleanTask
{
    private static final Logger log = LoggerFactory.getLogger(PdfCleanTask.class);

    @Autowired
    private PdfConversionMapper pdfConversionMapper;

    /**
     * 清理过期的PDF文件（超过1个月未访问）
     */
    public void cleanExpiredPdf()
    {
        log.info("开始清理过期PDF文件，时间：{}", DateUtils.getTime());
        
        try {
            // 查询所有过期的记录
            List<PdfConversion> expiredList = pdfConversionMapper.selectExpiredRecords();
            
            if (expiredList != null && !expiredList.isEmpty()) {
                log.info("发现{}条过期PDF记录，开始清理...", expiredList.size());
                
                for (PdfConversion conversion : expiredList) {
                    try {
                        // 删除文件
                        if (conversion.getPdfStorePath() != null) {
                            String localPath = RuoYiConfig.getProfile() + conversion.getPdfStorePath();
                            boolean deleted = FileUtils.deleteFile(localPath);
                            log.info("删除PDF文件：{}，结果：{}", localPath, deleted);
                        }
                        
                        // 删除数据库记录
                        pdfConversionMapper.deletePdfConversionById(conversion.getId());
                    } catch (Exception e) {
                        log.error("删除PDF记录失败，ID：{}", conversion.getId(), e);
                    }
                }
                
                log.info("过期PDF文件清理完成！");
            } else {
                log.info("没有发现过期的PDF记录");
            }
        } catch (Exception e) {
            log.error("清理过期PDF文件失败", e);
        }
    }
}
