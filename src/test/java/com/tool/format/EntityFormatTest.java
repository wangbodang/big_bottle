package com.tool.format;

import com.alibaba.fastjson.JSON;
import com.vefuture.big_bottle.web.vefuture.entity.RetinfoBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.RetinfoDrink;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @author wangb
 * @date 2025/3/15
 * @description TODO: 类描述
 */
@Slf4j
public class EntityFormatTest {

    /**
     * 规定返回数据格式
     */
    @Test
    public void testRetJson(){
        RetinfoBigBottle bigBottle = new RetinfoBigBottle();

        bigBottle.setRetinfoReceiptTime(new Date());
        bigBottle.setRetinfoIsAvaild(true);

        RetinfoDrink drink1 = new RetinfoDrink("Asahi Beer", 1500, 12);
        RetinfoDrink drink2 = new RetinfoDrink("Santory Beer", 2000, 3);
        bigBottle.getDrinkList().add(drink1);
        bigBottle.getDrinkList().add(drink2);

        String jsonString = JSON.toJSONString(bigBottle);
        log.info("---> 返回数据为:[{}]", jsonString);
    }


    @Test
    public void testParseJson(){
        String jsonStr = "{\n" +
                "  \"retinfoIsAvaild\": true,\n" +
                "  \"retinfoReceiptTime\": \"2025-03-14 11:30:12\",\n" +
                "  \"drinkList\": [\n" +
                "    {\n" +
                "      \"retinfoDrinkAmout\": 12,\n" +
                "      \"retinfoDrinkCapacity\": 1500,\n" +
                "      \"retinfoDrinkName\": \"Asahi Beer\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"retinfoDrinkAmout\": 3,\n" +
                "      \"retinfoDrinkCapacity\": 2000,\n" +
                "      \"retinfoDrinkName\": \"Santory Beer\"\n" +
                "    }\n" +
                "  ]  \n" +
                "}";
        RetinfoBigBottle retinfoBigBottles = JSON.parseObject(jsonStr, RetinfoBigBottle.class);
        log.info("---> 解析的结果:{}", retinfoBigBottles);
    }

}
