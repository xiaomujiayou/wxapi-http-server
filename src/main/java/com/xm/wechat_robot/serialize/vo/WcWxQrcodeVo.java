package com.xm.wechat_robot.serialize.vo;

import lombok.Data;

import java.util.UUID;

@Data
public class WcWxQrcodeVo {
    /**
     * 二维码数据
     * @mock http://weixin.qq.com/x/Q7esxVuGloULTn7uPg_W
     */
    private String qrCode;

    /**
     * 微信客户端ID
     * @mock 11932
     */
    private String clientId;

    /**
     * 生成时间
     * @mock 2020-8-30 12:12:12
     */
    private java.util.Date createTime;
    
}
