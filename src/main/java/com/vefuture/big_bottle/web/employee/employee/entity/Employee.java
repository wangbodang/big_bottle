package com.vefuture.big_bottle.web.employee.employee.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 测试表employee
 * </p>
 *
 * @author wangchao
 * @since 2025-03-12
 */
@Getter
@Setter
@ToString
@TableName("employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("age")
    private Short age;

    @TableField("gender")
    private String gender;

    @TableField("hiredate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private Date hiredate;

    @TableField("salary")
    private BigDecimal salary;

    @TableField("create_id")
    private Long createId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("update_id")
    private Long updateId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField("email")
    private String email;
}
