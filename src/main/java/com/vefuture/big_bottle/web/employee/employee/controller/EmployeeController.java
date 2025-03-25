package com.vefuture.big_bottle.web.employee.employee.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.domain.RetResponse;
import com.vefuture.big_bottle.common.exception.BusinessException;
import com.vefuture.big_bottle.web.employee.employee.entity.Employee;
import com.vefuture.big_bottle.web.employee.employee.entity.EmployeeQuery;
import com.vefuture.big_bottle.web.employee.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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

        return employeeService.list();
    }

    //请求分页数据
    @RequestMapping(value = "/List", method = RequestMethod.POST)
    public ApiResponse<IPage<Employee>> getEmpPageData(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "5") Integer size,
                                                       EmployeeQuery empParam){
        log.info("---> 请求参数:page-{}, size-{}, emp-{}", (Object) page, size, empParam);
        return employeeService.getEmpPageData(page, size, empParam);
    }

    @RequestMapping(value = "/addRandom", method = RequestMethod.POST)
    public ApiResponse getEmpPageData(@RequestParam(defaultValue = "15") Integer size){
        List<Employee> empList = new ArrayList<>();
        for(int i=0; i<size; i++){
            Employee temp = new Employee();
            temp.setName(RandomUtil.randomString(10));
            temp.setAge(RandomUtil.randomInt(10, 100));
            temp.setGender(RandomUtil.randomString("01", 1));
            temp.setSalary(RandomUtil.randomBigDecimal(new BigDecimal(100000)));
            temp.setHiredate(RandomUtil.randomDate(new Date(), DateField.DAY_OF_YEAR, -110, 300));



            empList.add(temp);
        }
         employeeService.saveBatch(empList, 10);
        return ApiResponse.success();
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
