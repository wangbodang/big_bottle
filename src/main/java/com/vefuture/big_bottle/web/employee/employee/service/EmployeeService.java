package com.vefuture.big_bottle.web.employee.employee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.web.employee.employee.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vefuture.big_bottle.web.employee.employee.entity.EmployeeQuery;

/**
 * <p>
 * 测试表employee 服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-03-12
 */
public interface EmployeeService extends IService<Employee> {

    ApiResponse<IPage<Employee>> getEmpPageData(Integer pageNo, Integer pageSize, EmployeeQuery empParam);
}
