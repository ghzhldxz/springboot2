package com.imooc.exception;

import com.imooc.enums.ResultEnum;

/**
 * @Description 全局异常
 * @Author GuanHuizhen
 * @Date 2018/8/7
 */
public class GlobalException extends RuntimeException{

    private static final long serialVersionUID = -5620392878253389942L;

    private ResultEnum resultEnum;

    public GlobalException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.resultEnum = resultEnum;

    }
}
