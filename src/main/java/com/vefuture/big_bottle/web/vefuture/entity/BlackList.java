package com.vefuture.big_bottle.web.vefuture.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * vefuture 黑名单列表
 * </p>
 *
 * @author wangchao
 * @since 2025-05-05
 */
@Getter
@Setter
@ToString
@TableName("b_vef_black_list")
public class BlackList implements Serializable {

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
     * 黑名单类型 0-默认值目前无意义 1-本系统自己拦黑 3-外部导入 5-存疑
     */
    @TableField("black_type")
    private Integer blackType;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @TableLogic
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
    private Date createTime;

    /**
     * 修改人ID
     */
    @TableField("update_id")
    private Long updateId;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
