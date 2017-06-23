package com.qczl.websocket.util;

import java.util.Arrays;

public class ValidUtil {
    /** 有效的图片后缀 **/
    private static final String[] IMG_SUFFIXS = new String[] { "jpeg", "jpg", "gif", "png", "bmp", "JPEG", "JPG", "GIF", "PNG", "BMP" };

    /**
     * 图片格式校验
     * @param fileName
     * @return
     */
    public static boolean validPicFormatOK(String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()); // 获取文件后缀
        if (!Arrays.asList(IMG_SUFFIXS).contains(suffix.toLowerCase())) {
            return false;
        }
        return true;
    }
}
