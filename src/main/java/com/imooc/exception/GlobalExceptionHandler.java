package com.imooc.exception;

import com.imooc.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description 全局异常处理
 * @Author GuanHuizhen
 * @Date 2018/8/7
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResultVo<String> handler(HttpServletRequest request, Exception e) {
        if(e instanceof GlobalException) {
            GlobalException ge = (GlobalException)e;
            log.info(ge.getMessage(),ge);
            return ResultVo.failure(ge.getMessage());
        } else if(e instanceof BindException){
            BindException be= (BindException)e;
            List<ObjectError> objectErrorList = be.getAllErrors();
            ObjectError error = objectErrorList.get(0);
            log.info(error.getDefaultMessage(),be);
            return ResultVo.failure(error.getDefaultMessage());
        } else {
            log.info(e.getMessage(),e);
            return ResultVo.failure(e.getMessage());
        }
    }


}
