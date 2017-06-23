package com.qczl.websocket.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
public class BaseController {
    /**
     * 校验器
     **/
    protected static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 数据校验
     *
     * @param obj
     * @return
     */
    protected <T> String dataValid(T obj) {
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        StringBuilder errorMsg = new StringBuilder();
        if ((null != set) && (0 != set.size())) {
            for (ConstraintViolation<T> violation : set) {
                errorMsg.append("," + violation.getMessage());
            }
        }
        String customerMsg = this.customerValid(obj); // 自定义校验器
        if (StringUtils.isNotEmpty(customerMsg)) {
            errorMsg.append("," + customerMsg);
        }
        if (StringUtils.isNotEmpty(errorMsg.toString())) {
            return errorMsg.substring(1);
        }
        return null;
    }

    /**
     * 自定义校验
     *
     * @return
     */
    protected <T> String customerValid(T obj) {

        return null;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
