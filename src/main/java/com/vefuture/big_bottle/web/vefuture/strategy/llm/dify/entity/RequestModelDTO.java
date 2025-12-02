package com.vefuture.big_bottle.web.vefuture.strategy.llm.dify.entity;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/4/26
 * @description TODO: 类描述
 * {
 *   "inputs": {
 *     "img_url": {
 *       "transfer_method": "remote_url",
 *       "url": "https://victor-oss.oss-cn-shanghai.aliyuncs.com/uPic/CleanShot%202025-03-15%20at%2021.14.01@2x.png",
 *       "type": "image"
 *     }
 *   },
 *   "response_mode": "blocking",
 *   "user": "big_bottle"
 * }
 */
@Data
public class RequestModelDTO {
    private Inputs inputs;
    private String response_mode = "blocking";
    private String user = "big_bottle";

    @Data
    public static class Inputs {
        private ImgUrl image_url;
    }

    @Data
    public static class ImgUrl {
        private String transfer_method = "remote_url";
        private String url;
        private String type = "image";
    }
}
