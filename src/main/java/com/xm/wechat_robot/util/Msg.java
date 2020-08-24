package com.xm.wechat_robot.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Msg<T> implements Serializable {

    /**
     * 状态码
     *
     * @mock 200
     */
    private Integer code;

    /**
     * 提示消息
     *
     * @mock sucess
     */
    private String msg;

    /**
     * 消息结构体
     */
    private T data;

    public Msg() {
    }

    public Msg(MsgEnum msgEnum, T data) {
        this.code = msgEnum.getCode();
        this.msg = msgEnum.getMsg();
        this.data = data;
    }

    public Msg(MsgEnum msgEnum, String errorMsg) {
        this.code = msgEnum.getCode();
        this.msg = errorMsg == null ? msgEnum.getMsg() : errorMsg;
        this.data = null;
    }

    public Msg(MsgEnum msgEnum) {
        this.code = msgEnum.getCode();
        this.msg = msgEnum.getMsg();
        this.data = null;
    }
}
