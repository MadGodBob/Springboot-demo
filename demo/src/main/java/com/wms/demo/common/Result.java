package com.wms.demo.common;

import lombok.Data;

@Data
public class Result {
    private static Integer SUCCESS_CODE = 200;
    private static Integer ERROR_CODE = 400;

    private Integer code;
    private String msg;
    private Long total;
    private Object data;

    // 成功
    public static Result success(){
        return setResult(SUCCESS_CODE, "success", 0L, null);
    }
    // 成功(带data)
    public static Result success(Object data){
        return setResult(SUCCESS_CODE, "success", 0L, data);
    }
    // 成功(带data和total)
    public static Result success(Object data, Long total){
        return setResult(SUCCESS_CODE, "success", total, data);
    }

    // 失败
    public static Result error(Object data){
        return setResult(ERROR_CODE, "error", 0L, data);
    }

    // 设置方法
    public static Result setResult(Integer code, String msg, Long total, Object data){
        Result res = new Result();
        res.setCode(code);
        res.setMsg(msg);
        res.setTotal(total);
        res.setData(data);
        return res;
    }
}
