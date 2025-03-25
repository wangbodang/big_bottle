package com.vefuture.big_bottle.web.employee.employee.entity;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/3/23
 * @description TODO: 类描述
 */
@Data
public class EmployeeQuery {

    private String nameFuzzy;
    private String emailFuzzy;
}
