package com.qczl.websocket.exception;

import com.qczl.websocket.response.Result;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常管理
 *
 */
@Component
public class CustomSimpleMappingExceptionResolver implements HandlerExceptionResolver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        logger.error(ExceptionUtils.getStackTrace(e));
        Map<String, Object> model = new HashMap<>();
        model.put("responseCode",Result.RESPONSE_CODE_HAS_ERROR);
        model.put("responseMsg", Result.RESPONSE_MSG_HAS_ERROR);
        return new ModelAndView(jsonView, model);
    }
}