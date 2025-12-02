package com.vechain;

import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson.JSON;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.BodyEntity;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.ImageContent;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.ParameterEntity;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author wangb
 * @date 2025/12/2
 * @description TODO: 类描述
 */
@Slf4j
public class DifyClientTest {
    private final OkHttpClient client = new OkHttpClient();

    String difyUrl = "https://api.dify.ai/v1/workflows/run";
    //String difyUrl = "http://18.176.28.203/v1/workflow/run";
    //String workflow_id = "7480478548415840263"; //我的
    //String workflow_id = "7480934802444992567"; //八个大大的
    // String workflow_id = "7482017874254037010"; //八个大大的更详细的模型
    String workflow_id = "9c93452d-40b1-4588-8792-41e9694d777d"; //八个大大的更详细的模型
    //String workflow_id = "7484704441888489480"; //八个大大的更详细的模型
    //String workflow_id = "7485935567689302034"; //八个大大的更详细的模型

    //String token = "Bearer pat_2cG2zJt8n9T1uqNDyVJ1lUS0h3R09NetFeWHDsPoLHDPgZW94u0DKN0kgIdc48Lx"; //我的
    String token = "Bearer app-H6MqvyfJ5cNJYYR5twKWvxN7";  //八个大大的

    @Test
    public void test01() throws IOException {

        //封装请求参数
        //返回true的数据

        //一瓶饮料
        String img_url = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/xxxx.png";
        //两瓶饮料
        //String img_url = "https://victor-oss.oss-cn-shanghai.aliyuncs.com/uPic/CleanShot 2025-03-15 at 21.14.01@2x.png"

        ImageContent image_url = new ImageContent();
        image_url.setUrl(img_url);

        ParameterEntity parameterEntity = new ParameterEntity();
        parameterEntity.setImgUrl(img_url);
        parameterEntity.setImage_url(image_url);

        BodyEntity bodyEntity = new BodyEntity();
        bodyEntity.setWorkflow_id(workflow_id);
        bodyEntity.setInputs(parameterEntity);
        bodyEntity.setUser("4f2fa996-74db-4107-bc38-199611e76f5b");
        bodyEntity.setInputs(parameterEntity);
        Object bodyEntityJson = JSON.toJSON(bodyEntity);
        // 3. 使用 Gson（或 Jackson 等）将实体转换为 JSON 字符串
        String jsonString = JSON.toJSONString(bodyEntity);
        log.info("----> 请求参数为:{}", jsonString);
        // 4. 将 JSON 字符串封装为 RequestBody
        RequestBody body = RequestBody.create(jsonString, MediaType.get("application/json; charset=utf-8"));


        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Request request = new Request.Builder()
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .url(difyUrl)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        stopWatch.stop();
        log.info("---> 调用 Dify 模型耗时:{}秒", stopWatch.prettyPrint(TimeUnit.SECONDS));
        log.info("---> 返回值为:{}", response.body().string());
    }
}
