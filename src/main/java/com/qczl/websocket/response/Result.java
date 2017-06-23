package com.qczl.websocket.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class Result {
    // 服务器返回成功
    public static final int RESPONSE_CODE_SCUESS = 1000;

    // 有错误
    public static final int RESPONSE_CODE_HAS_ERROR = 1001;

    public static final String RESPONSE_MSG_HAS_ERROR = "系统异常";

    /**
     * 服务器返回的json数据响应码
     */
    private int responseCode;

    /**
     * 响应信息
     */
    private String responseMsg;

    private Object data;

    public Result() {
    }

    public Result(int responseCode, String responseMsg, Object data) {
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.data = data;
    }

    /**
     * 存在错误
     * @param msg 错误信息
     * @return
     */
    public static Result error(String msg) {
        Result result = new Result(Result.RESPONSE_CODE_HAS_ERROR, msg, null);
        return result;
    }

    /**
     * 存在错误
     * @param msg
     * @param errorCode
     * @return
     */
    public static Result error(String msg, int errorCode) {
        Result result = new Result(errorCode, msg, null);
        return result;
    }

    /**
     * 常用的成功请求
     * @param data
     * @return
     */
    public static Result success(Object data) {
        Result result = new Result(Result.RESPONSE_CODE_SCUESS, "", data);
        return result;
    }

    /**
     * 常用的成功请求
     *
     * @param data
     * @return
     */
    public static Result success(String msg, Object data) {
        Result result = new Result(Result.RESPONSE_CODE_SCUESS, msg, data);
        return result;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return this.responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }
}
