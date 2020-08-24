package com.xm.wechat_robot.serialize.form;

import lombok.Data;

@Data
public class PageForm extends BaseForm {

    /**
     * 当前页
     */
    private Integer pageNum = 1;

    /**
     * 页面大小
     */
    private Integer pageSize = 20;
}
