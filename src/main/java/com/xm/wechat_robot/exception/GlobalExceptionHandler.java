package com.xm.wechat_robot.exception;

import com.xm.wechat_robot.util.MsgEnum;
import com.xm.wechat_robot.util.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobleException.class)
    @ResponseBody
    public Object exceptionHandler(GlobleException e){
        e.printStackTrace();
        return R.error(e.getMsgEnum(),e.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e){
        e.printStackTrace();
        return R.error(MsgEnum.UNKNOWN_ERROR);
    }
}
