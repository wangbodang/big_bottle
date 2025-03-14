package com.vefuture.big_bottle.web.employee.employee.controller;

import com.vefuture.big_bottle.common.domain.RetResponse;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.employee.employee.entity.Employee;
import com.vefuture.big_bottle.web.employee.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Employee> getEmpList(){
        log.info("---> 请求调用demo数据");
        List<Map<String, Object>> resultList = new ArrayList<>();

        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", "10001");
        user1.put("name", "wangbodang");
        user1.put("email", "ttt@gmail.com");
        resultList.add(user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("id", "10002");
        user2.put("name", "xieyingdeng");
        user2.put("email", "bbb@gmail.com");
        resultList.add(user2);

        Map<String, Object> user3 = new HashMap<>();
        user3.put("id", "10003");
        user3.put("name", "qiguoyuan");
        user3.put("email", "qqq@gmail.com");
        resultList.add(user3);

        return employeeService.list();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public RetResponse saveEmployee(@RequestBody Employee employee){
        log.info("---> 新加入的employee信息为:{}", employee);
        RetResponse retResponse = new RetResponse(1, "插入成功", employee);
        try {
            boolean save = employeeService.save(employee);
            log.info("---> 插入成功:{}", employee);
        } catch (Exception e) {
            log.error("===> 异常消息为:{}", e.getMessage());
            throw new BusinessException("插入异常:"+e.getMessage(), e);
        }
        return retResponse;
    }
}
