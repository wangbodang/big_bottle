package com.vefuture.big_bottle.web.system.entity.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangb
 * @date 2025/5/20
 * @description TODO: 类描述
 */
@Data
public class SysConfigQueryDTO {
    private int current;
    private int size;

    @NotBlank(message = "配置键不能为空")
    private String configKey;
    @NotBlank(message = "配置值不能为空")
    private String configValue;
}
