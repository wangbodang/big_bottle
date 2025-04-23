package com.vefuture.big_bottle.web.vefuture.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.vefuture.entity.qo.ReqBigBottleQo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.service.IManageBiBottleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/19
 * @description 管理饮料列表Service
 */
@Slf4j
@Service
public class ManageBiBottleServiceImpl implements IManageBiBottleService {

    @Autowired
    private BVefutureBigBottleMapper bigBottleMapper;

    @Override
    public Page<ManageBigBottleVo> getBigBottleList(HttpServletRequest request, Page<ManageBigBottleVo> page, ReqBigBottleQo qo) {

        List<ManageBigBottleVo> manageBigBottleVoList = bigBottleMapper.getManageBigBottleList(page, qo);
        //设置图像名字
        manageBigBottleVoList.forEach(manageBigBottleVo -> {
            String imgUrl = manageBigBottleVo.getImgUrl();
            manageBigBottleVo.setImgName(imgUrl.substring(imgUrl.lastIndexOf("/")+1));
        });

        page.setRecords(manageBigBottleVoList);
        Long total = bigBottleMapper.getManageBigBottleCount(qo);
        page.setTotal(total);
        return page;
    }
}
