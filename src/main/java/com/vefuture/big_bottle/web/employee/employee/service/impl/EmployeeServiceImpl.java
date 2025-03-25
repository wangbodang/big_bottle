package com.vefuture.big_bottle.web.employee.employee.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.web.employee.employee.entity.Employee;
import com.vefuture.big_bottle.web.employee.employee.entity.EmployeeQuery;
import com.vefuture.big_bottle.web.employee.employee.mapper.EmployeeMapper;
import com.vefuture.big_bottle.web.employee.employee.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试表employee 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-03-12
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Override
    public ApiResponse<IPage<Employee>> getEmpPageData(Integer pageNo, Integer pageSize, EmployeeQuery empParam) {
        Page<Employee> pageParam = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if(StrUtil.isNotBlank(empParam.getNameFuzzy())){
            queryWrapper.like(Employee::getName, empParam.getNameFuzzy());
        }
        if(StrUtil.isNotBlank(empParam.getEmailFuzzy())){
            queryWrapper.like(Employee::getEmail, empParam.getEmailFuzzy());
        }
        queryWrapper.orderByAsc(Employee::getId);
        Page<Employee> employeePage = baseMapper.selectPage(pageParam, queryWrapper);
        return ApiResponse.success(employeePage);
    }
}
