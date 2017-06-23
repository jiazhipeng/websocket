package com.qczl.websocket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传工具类
 */
public class ImageUploadUtils {

    protected static final Logger log = LoggerFactory.getLogger(ImageUploadUtils.class);

    public final static String DIR_NAME = "talk_img";

    public static String uploadImgToTmp(String contextPath, MultipartFile file) {

        /**加入日期为后续定时任务删除图片*/
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String yyyyMMdd = sdf.format(date);

        String filePath = contextPath + DIR_NAME + File.separator + yyyyMMdd;

        String srcFileName = file.getOriginalFilename();
        String type = srcFileName.substring(srcFileName.lastIndexOf(".") + 1);

        String fileName = UUID.randomUUID().toString() + "." + type;
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File destFile = new File(destDir, fileName);
        log.info(destFile.getPath());
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("图片上传出现错误:{}" , e);
        }
        //file url
        return destFile.getPath();
    }

}
