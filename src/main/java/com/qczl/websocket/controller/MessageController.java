package com.qczl.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.qczl.websocket.response.Result;
import com.qczl.websocket.service.TalkService;
import com.qczl.websocket.util.ImageUploadUtils;
import com.qczl.websocket.entity.Message;
import com.qczl.websocket.util.ValidUtil;
import com.qczl.websocket.websocket.TalkWebSocketHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{

    @Autowired
    private TalkWebSocketHandler handler;

    @Autowired
    private TalkService talkService;

    /**
     * @param picFile 图片文件
     * @return
     */
    @RequestMapping(value = "/pic/upload")
    public Result upload(MultipartFile picFile, Message message, HttpServletRequest request, HttpSession session) {

        String realPath = session.getServletContext().getRealPath("/");
        String url = null;
        String error = null;
        if (picFile != null) {

            // 校验文件后缀的合法性
            String fileName = picFile.getOriginalFilename();
            if (!ValidUtil.validPicFormatOK(fileName)) {
                error = "'图片格式只能是jepg,gif,jpg,png,bmp'";
                return Result.error(error);
            }

            // 校验提交数据
            String validResult = this.dataValid(message);
            if (StringUtils.isNotEmpty(validResult)) {
                return Result.error(validResult, Result.RESPONSE_CODE_HAS_ERROR);
            }

            url = ImageUploadUtils.uploadImgToTmp(realPath,picFile);

        }

        if (url == null) {
            return Result.error("图片上传失败", Result.RESPONSE_CODE_HAS_ERROR);
        }

        //写入消息
        message.setText(url);

        //推送消息
        message.setMessageType(Message.MessageTypeEnum.MESSAGETYPE_PICTURE.getValue());
        handler.sendMessageToRoom(message.getRoomId(), JSON.toJSONString(message));
        //同时将消息加入到redis
        talkService.messageAddRedis(message);

        //返回信息
        Map<String, String> result = new HashMap<>();
        result.put("imgUrl", url);
        return Result.success(result);
    }


    @RequestMapping("/getHistoryMessage")
    public Result getHistoryMessage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  String roomId, Long score) {

        if (StringUtils.isBlank(roomId)){
           return Result.error("房间号不能为空");
        }

        if (score == null){
            return Result.error("加入直播间时间不能为空");
        }

        List<Message> data = talkService.getMessage(roomId,pageNo,pageSize,score);

        logger.info("message data is {}",data);

        return Result.success(data);
    }

    @RequestMapping("test")
    public Object test(){
        int a = 1/0;
        return null;
    }

}
