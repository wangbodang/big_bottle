package com.vefuture.big_bottle.web.vefuture.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.vefuture.entity.BVefutureBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BigBottleQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.ManageBigBottleVo;
import com.vefuture.big_bottle.web.vefuture.service.IManageBiBottleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author wangb
 * @date 2025/4/19
 * @description 管理饮料列表Controller
 */
@Slf4j
@RestController
@RequestMapping("/manage")
public class ManageBigBottleController {

    @Autowired
    private IManageBiBottleService manageBiBottleService;

    /**
     * 获取饮料列表
     * @return
     */
    @RequestMapping(value = "/bigbottlelist", method = RequestMethod.POST)
    public ApiResponse<Page<ManageBigBottleVo>> getBigBottleList(HttpServletRequest request,
                                           HttpServletResponse response,
                                                                 @RequestBody BigBottleQueryDTO dto){
        log.info("---> 查询管理BigBottle列表:page DTO:[{}]", dto);
        Page<ManageBigBottleVo> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<ManageBigBottleVo> manageBigBottleVoList = manageBiBottleService.getBigBottleList(request, page, dto);

        return ApiResponse.success(manageBigBottleVoList);
    }
    /**
     * 获取黑名单列表
     * @return
     */
    @RequestMapping(value = "/blacklist", method = RequestMethod.POST)
    public ApiResponse<Page<BlackList>> getBlackList(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     @RequestBody BlackListQueryDTO dto){
        log.info("---> 查询管理黑名单列表:page dto:[{}]", dto);
        Page<BlackList> page = new Page<>(dto.getCurrent(), dto.getSize());

        Page<BlackList> blackList = manageBiBottleService.getBlackList(request, page, dto);
        return ApiResponse.success(blackList);
    }
    /*
     * 根据IDs获取小票信息
     */
    @PostMapping("/receipt/infoByIds")
    public ApiResponse<List<BVefutureBigBottle>> getReceiptsByIds(@RequestBody Map<String, List<String>> body) {
        List<String> ids = body.get("ids");
        List<BVefutureBigBottle> receipts = manageBiBottleService.getDetailsByIds(ids);
        return ApiResponse.success(receipts);
    }
    //作废小票
    @PostMapping("/receipt/invalidate")
    public ApiResponse<String> invalidateReceipts(@RequestBody Map<String, List<Object>> body) {
        List<Object> rawIds = body.get("ids");
        if (rawIds == null || rawIds.isEmpty()) {
            return ApiResponse.error(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(), "参数 ids 不能为空");
        }
        log.info("---> 传入的参数为:{}", rawIds);
        try {
            manageBiBottleService.invalidateReceiptsByIds(rawIds);
            return ApiResponse.success("小票已成功作废");
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.INVALIDATE_RECEIPT_FAILE.getCode(), ResultCode.INVALIDATE_RECEIPT_FAILE.getMessage()+e.getMessage());
        }
    }

    //拉黑地址
    @PostMapping("/wallet/blacklist")
    public ApiResponse<String> addWalletToBlacklist(@RequestBody BlackListQueryDTO dto) {
        String walletAddress = dto.getWalletAddress();
        Integer blackType = dto.getBlackType();
        if (walletAddress == null || walletAddress.trim().isEmpty() || blackType == null) {
            return ApiResponse.error(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(),"参数错误，钱包地址不能为空");
        }
        log.info("--->>> 拉黑参数:{}-{}", walletAddress, blackType);
        // 逻辑处理：加入黑名单
        manageBiBottleService.addWalletAddressToBlacklist(walletAddress, blackType);

        return ApiResponse.success("地址已加入黑名单");
    }

    //导出
    @PostMapping("/bigbottlelist/export")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestBody BigBottleQueryDTO dto){
        log.info("===> 导出的请求参数为:{}", dto);
        try {
            manageBiBottleService.exportCsv(request, response, dto);
        } catch (Exception e) {
            log.error("CSV导出失败：{}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    /*@PostMapping("/blacklist")
    public ApiResponse<String> debug(@RequestBody Map<String, Object> map) {
        log.info("🚀 参数 map = {}", map);
        return ApiResponse.success("ok");
    }*/
}
