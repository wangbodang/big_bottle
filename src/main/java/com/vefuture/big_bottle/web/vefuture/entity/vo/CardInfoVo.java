package com.vefuture.big_bottle.web.vefuture.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wangb
 * @date 2025/3/28
 * @description 首页卡返回饮料名称和数量
 */
@Data
public class CardInfoVo {
   /* private String drinkName;
    private Integer drinkCapacity;
    private Integer drinkAmout;
    //小票上传时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo") //返回到前端时默认格式
    private Date receiptUploadTime;*/
    //返回到前端 饮料列表
    List<DrinkInfo> drinkInfos;
    //周积分
    private Integer weekPoints;


}
