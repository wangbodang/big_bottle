package com.vefuture.big_bottle.web.vefuture.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * vefuture bigbottle 基本信息表
 * </p>
 *
 * @author wangchao
 * @since 2025-03-15
 */
@Getter
@Setter
@ToString
@TableName("b_vefuture_big_bottle")
public class BVefutureBigBottle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键长整型自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 钱包地址
     */
    @TableField("wallet_address")
    private String walletAddress;

    /**
     * 图片地址
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 判定结果是否可用
     */
    @TableField("retinfo_is_availd")
    private Boolean retinfoIsAvaild;

    /**
     * 布尔值，用来检验小票时间和上传到系统时间的差值（比如我们规定小票时间是本周内的，就需要做这个判断）
     */
    @TableField("is_time_threshold")
    private Boolean isTimeThreshold;

    /**
     * 饮料名称
     */
    @TableField("retinfo_drink_name")
    private String retinfoDrinkName;

    /**
     * 饮料容量
     */
    @TableField("retinfo_drink_capacity")
    private Integer retinfoDrinkCapacity;

    /**
     * 饮料数量
     */
    @TableField("retinfo_drink_amout")
    private Integer retinfoDrinkAmout;

    /**
     * 小票打印时间
     */
    @TableField("retinfo_receipt_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private Date retinfoReceiptTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableField("is_delete")
    private String isDelete;

    /**
     * 创建人ID
     */
    @TableField("create_id")
    private Long createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private Date createTime;

    /**
     * 修改人ID
     */
    @TableField("update_id")
    private Long updateId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private Date updateTime;
}
