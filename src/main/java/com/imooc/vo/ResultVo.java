package com.imooc.vo;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @Description http请求返回的对象
 * @Author GuanHuizhen
 * @Date 2018/8/1
 */
@Data
public class ResultVo <T>{

    private Integer code;

    private String msg;

    private T data;

    private ResultVo(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultVo<T> success(T t) {
       return new ResultVo<T>(0,"SUCCESS",t);
    }

    public static <T> ResultVo<T> success() {
        return new ResultVo<T>(0,"SUCCESS",null);
    }

    public static <T> ResultVo<T> failure(String msg) {
        return new ResultVo<T>(500,msg,null);
    }


}
