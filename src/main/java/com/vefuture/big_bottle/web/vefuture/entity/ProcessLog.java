package com.vefuture.big_bottle.web.vefuture.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangchao
 * @since 2025-04-18
 */
@Getter
@Setter
@ToString
@TableName("b_vef_process_log")
public class ProcessLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键, 序列自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("ip_address")
    private String ipAddress;

    /**
     * 流程ID，全流程使用
     */
    @TableField("process_id")
    private String processId;

    /**
     * 钱包地址
     */
    @TableField("wallet_address")
    private String walletAddress;

    /**
     * 图片URL
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * AI模型类型
     */
    @TableField("ai_type")
    private String aiType;

    /**
     * AI流程 开始时间
     */
    @TableField("ai_process_start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private LocalDateTime aiProcessStartTime;

    /**
     * AI流程结束时间
     */
    @TableField("ai_process_end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private LocalDateTime aiProcessEndTime;

    /**
     * AI模型返回信息
     */
    @TableField("ai_return_msg")
    private String aiReturnMsg;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标志, Boolean类型
     */
    @TableField("is_delete")
    private Boolean isDelete;

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
    private LocalDateTime createTime;

    /**
     * 修改人ID
     */
    @TableField("update_id")
    private Long updateId;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private LocalDateTime updateTime;
}
