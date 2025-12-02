package com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity;

import lombok.Data;

@Data
public class BodyEntity {
    // todo 此处改为配置
    //String workflow_id = "7480478548415840263"; //我的
    //String workflow_id = "7480934802444992567"; //八个大大的
    String workflow_id; //八个大大的更详细的模型
    String user = "4f2fa996-74db-4107-bc38-199611e76f5b";
    String response_mode = "blocking";

    ParameterEntity inputs;
}
