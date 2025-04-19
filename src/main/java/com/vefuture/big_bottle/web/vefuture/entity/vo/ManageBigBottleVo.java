package com.vefuture.big_bottle.web.vefuture.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author wangb
 * @date 2025/4/19
 * @description TODO: 类描述
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageBigBottleVo {

    private String processId;
    private String walletAddress;
    private String imgUrl;
    private Boolean retinfoIsAvaild;
    private Boolean isTimeThreshold;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private Date retinfoReceiptTime;

    private Integer drinkCount;
    private Integer drinkCapacity;
    private Integer drinkAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //接收前端传过来的数据时
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private Date createTime;
}
