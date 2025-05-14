package com.vefuture.big_bottle.web.vefuture.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author wangb
 * @date 2025/4/19
 * @description TODO: 类描述
 */
public interface IManageBiBottleService {
    Page<ManageBigBottleVo> getBigBottleList(HttpServletRequest request, Page<ManageBigBottleVo> page, BigBottleQueryDTO qo);

    Page<BlackList> getBlackList(HttpServletRequest request, Page<BlackList> page, BlackListQueryDTO dto);

    List<BVefutureBigBottle> getDetailsByIds(List<String> ids);


    void invalidateReceiptsByIds(List<Object> ids);

    void addWalletAddressToBlacklist(String walletAddress, Integer type);


    void exportCsv(HttpServletRequest request, HttpServletResponse response, BigBottleQueryDTO dto);
}
