package com.vechain;

import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson.JSON;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.BodyEntity;
import com.vefuture.big_bottle.web.vefuture.strategy.llm.coze.entity.ParameterEntity;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 测试Coze模型调用判断图片
 */
@Slf4j
//@SpringBootTest(classes = BigBottleApplication.class)
//@RunWith(SpringRunner.class)
public class CozeClientTest {
    private final OkHttpClient client = new OkHttpClient();

    String cozeUrl = "https://api.coze.com/v1/workflow/run";
    //String workflow_id = "7480478548415840263"; //我的
    //String workflow_id = "7480934802444992567"; //八个大大的
    String workflow_id = "7482017874254037010"; //八个大大的更详细的模型
    //String workflow_id = "7484704441888489480"; //八个大大的更详细的模型
    //String workflow_id = "7485935567689302034"; //八个大大的更详细的模型

    //String token = "Bearer pat_2cG2zJt8n9T1uqNDyVJ1lUS0h3R09NetFeWHDsPoLHDPgZW94u0DKN0kgIdc48Lx"; //我的
    String token = "Bearer pat_lUnXXnZwd75NmjB5GIXtZAo6uR5rEhDziooiD2AxF9d12HFqTIFSV1uWKSRK8Bdd";  //八个大大的

    @Test
    public void test01() throws IOException {


        //封装请求参数
        //返回true的数据
        //String img_url = "https://victor-oss.oss-cn-shanghai.aliyuncs.com/uPic/CleanShot%202025-03-09%20at%2023.41.50@2x.png";
        //String img_url = "https://victor-oss.oss-cn-shanghai.aliyuncs.com/uPic/HQ6Vx4.png";
        //String img_url = "https://storage.cloud.google.com/bigbootle/bill01.png";
        //String img_url = "https://ffc5c0c271d12a4e94bfc7f38e1230d8a5a7212bb11c1690bfa76a9-apidata.googleusercontent.com/download/storage/v1/b/bigbootle/o/bill01.png?jk=AVyuY3huZ9qjhGNCIjrj9sQQbp2ESc0XJxPznVpMWWGssekG2p7iRkwQc8Bb8-w7tLTg9EDrdLy-pUwO1nfnOiSKumOA4lOVHbFBXJMZ1_AfDn_fSHq5XNRp2lmeawkbKjqfmbhiwLFd9NYsPzqTAmmIUP_diMlJV0TYu2uMFQnE4RHibq0DhmqIX40Lvwxy2b8-kOox-v7kWEMLY0lpviBRq4aF68m3_fpgA0MbgdKFQnCzE0iKgYxLsFizfsWD7qR03Cu2Kkem4gb7iQYhfWA9uUFPM8bscgNRh0QwisQbiSBaYw650caQjncPo-Ezp1TflPh9_OhV496u-TH9Xr7ulq8eB2APr8ihExilIH-pEsNTuF9K1fUPPkXKJjvp8MrtFkQzKgYCg9YGPL67npRaP9DrpmHMCYY5PZQ6IdJkVEphYFGARuI9bRqYHwPKDl5OQ68w-Fccz_vEQutqBR3xvy7iRfsybPR2kRH8_BZC02djrpSFG3Ep8F_HksjKrBBzj_GfyW6a-KMt7VY769ukXpFtUA8LM2T4RU_XnUq7OjPAMiuYIKqnsVHKipMkCc7CqIzF_3OSzqgiWtmvUXxyN57BNcTO_MBvl_FbjVxW4TjnKKPpSnPnFMXXgTz4D4_hs4zDlgIBPXrV3ByGs4qE7QhVp2kWiojoQTW6CKf0h3-MjmJClWpljO56Gouk26FQdJ9k5csbCivruJJWinHrwo36o6hvOY72hdMX7Hmj8hbET46zN6ADuFUSRY0DSplne9WSetxGF3jkrTw9Kq9cPBRKDAO4yaBDVDt1tVwxrxD0dYXP3FoK4FEg3rLSn_DgPooxBXYefOqIw12Rm0jwVCvQYmY8PAL-kA8FXF-PVZqIuVMaes9R5W6OPtxtDPoFVvjC8w8Qtkdxo6gYHN0SSMAlxhGOKds8Vt5GxxE7PBcTYVec2lnW_jQLC285Oo5_iqh-BAP7sVzzBHWkIE4rq58DNkOCgTm99R4oLjUnDPM7hq4gp1UuP9z3msEv55VWdXtyJCt6Rac9ZFK3wo4u3Ef2d6HiKvvUHjztcsFHyuP-YCkvoI265dYB-eRQnsYKcIEkOIaIdp9khEuuAayg6udDHcQC6pV8ymhq&isca=1";
        //String img_url = "https://storage.cloud.google.com/bigbootle/bill01.png";

        //Amazon云图片
        //String img_url = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/bill3.jpg";
        //String img_url = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/demo.png";
        //String img_url = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/_MG_1445_3k.jpg";

        //一瓶饮料
        String img_url = "https://bvefuturebigbottle.s3.ap-southeast-2.amazonaws.com/uploads/xxxx.png";
        //两瓶饮料
        //String img_url = "https://victor-oss.oss-cn-shanghai.aliyuncs.com/uPic/CleanShot 2025-03-15 at 21.14.01@2x.png"


        ParameterEntity parameterEntity = new ParameterEntity();
        parameterEntity.setImg_url(img_url);
        BodyEntity bodyEntity = new BodyEntity();
        bodyEntity.setWorkflow_id(workflow_id);
        bodyEntity.setParameters(parameterEntity);
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
                .url(cozeUrl)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        stopWatch.stop();
        log.info("---> 调用Coze模型耗时:{}秒", stopWatch.prettyPrint(TimeUnit.SECONDS));
        log.info("---> 返回值为:{}", response.body().string());

    }
}
