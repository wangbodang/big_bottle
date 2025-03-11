package com.vefuture.big_bottle.web.employee.employee.controller;

import com.vefuture.big_bottle.common.domain.RetResponse;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.employee.employee.entity.Employee;
import com.vefuture.big_bottle.web.employee.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 测试表employee 前端控制器
 * </p>
 *
 * @author wangchao
 * @since 2025-03-12
 */
@Slf4j
@RestController
@RequestMapping("/employee/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @RequestMapping("/getById")
    public Employee getById(Integer id){
        log.info("---> 请求ID:{}", id);
        return employeeService.getById(id);
    }

    @RequestMapping(value = "/saveEmp", method = RequestMethod.POST)
    public RetResponse saveEmp(Employee employee){
        RetResponse retResponse = new RetResponse(1, "插入成功", employee);
        try {
            boolean save = employeeService.save(employee);

        } catch (Exception e) {
            log.error("===> 异常消息为:{}", e.getMessage());
            throw new BusinessException("插入异常:"+e.getMessage(), e);
        }
        return retResponse;
    }
}
