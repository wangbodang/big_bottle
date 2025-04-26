package com.vefuture.big_bottle.web.vefuture.strategy.llm.dify.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.domain.RetinfoBigBottle;
import lombok.Data;

import java.util.List;

/**
 * @author wangb
 * @date 2025/4/26
 * @description TODO: 类描述
 */
@JsonIgnoreProperties(ignoreUnknown = true)//忽略未知字段
@Data
public class ResponseModelDTO {
    private String task_id;
    private String workflow_run_id;
    private DataContent data;

    @Data
    public static class DataContent {
        private String id;
        private String workflow_id;
        private String status;
        //private Outputs outputs;
        private RetinfoBigBottle outputs;
        private Object error; // 如果 error 是null或者以后是对象，可以用Object，灵活
        private Double elapsed_time;
        private Integer total_tokens;
        private Integer total_steps;
        private Long created_at;
        private Long finished_at;
    }

    /*
    @Data
    public static class Outputs {
        private List<Drink> drinkList;
        private Boolean retinfoIsAvaild;
        private String retinfoReceiptTime;
        private Boolean timeThreshold;
        private String workflow_run_id;
    }

    @Data
    public static class Drink {
        private String retinfoDrinkName;
        private Integer retinfoDrinkCapacity;
        private Integer retinfoDrinkAmount;
    }
    */
}
