package com.vefuture.big_bottle.web.vefuture.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author wangb
 * @date 2025/3/15
 * @description 从AI返回的信息模型
 */
@Data
public class RetinfoBigBottle {
    //是否可用
    private Boolean retinfoIsAvaild;
    //小票打印时间
    @TableField("retinfo_receipt_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date retinfoReceiptTime;
    //饮料列表
    private ArrayList<RetinfoDrink> drinkList = new ArrayList<>();
}
