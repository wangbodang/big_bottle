package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vefuture.big_bottle.common.config.BigBottleProperties;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.common.util.BbDateTimeUtils;
import com.vefuture.big_bottle.common.util.OkHttpUtil;
import com.vefuture.big_bottle.common.vechain.BodyEntity;
import com.vefuture.big_bottle.common.vechain.ParameterEntity;
import com.vefuture.big_bottle.web.vefuture.entity.*;
import com.vefuture.big_bottle.web.vefuture.entity.qo.ReqBigBottleQo;
import com.vefuture.big_bottle.web.vefuture.entity.llm_ret.RetinfoBigBottle;
import com.vefuture.big_bottle.web.vefuture.entity.llm_ret.RetinfoDrink;
import com.vefuture.big_bottle.web.vefuture.entity.llm_ret.RetinfoLLMJson;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CardInfoVo;
import com.vefuture.big_bottle.web.vefuture.entity.vo.CountLimitVo;
import com.vefuture.big_bottle.web.vefuture.mapper.BVefutureBigBottleMapper;
import com.vefuture.big_bottle.web.vefuture.service.BVefutureBigBottleService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * <p>
 * vefuture bigbottle 基本信息表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-03-15
 */
@Slf4j
@Service
public class BVefutureBigBottleServiceImpl extends ServiceImpl<BVefutureBigBottleMapper, BVefutureBigBottle> implements BVefutureBigBottleService {

    @Autowired
    private BigBottleProperties bigBottleProperties;
    @Autowired
    private ExecutorService threadPoolExecutor;
    @Value("${bigbottle.counttimes.max:13}")
    private Integer countMax;

    //改为配置
    //String cozeUrl = "https://api.coze.com/v1/workflow/run";
    //String workflow_id = "7480478548415840263"; //我的
    //String workflow_id = "7480934802444992567"; //八个大大的
    //String workflow_id = "7482017874254037010"; //八个大大的更详细的模型
    //改为配置
    //String token = "Bearer pat_2cG2zJt8n9T1uqNDyVJ1lUS0h3R09NetFeWHDsPoLHDPgZW94u0DKN0kgIdc48Lx"; //我的
    //String token = "Bearer pat_lUnXXnZwd75NmjB5GIXtZAo6uR5rEhDziooiD2AxF9d12HFqTIFSV1uWKSRK8Bdd";  //八个大大的
    //从单例class获取
    //private final OkHttpClient client = new OkHttpClient();
    private final OkHttpClient client = OkHttpUtil.getClient();

    /**
     *
     * 显示的是最后一次上传的小票内容和本周的积分
     - icon 展示  状态
     - 右下角时间显示后端数据库记录的时间
     - 饮料名称（UI 做最大字符串的截断处理或多行设计）
     - 饮料容积，以 ml 作为单位
     - 饮料数量
     - 积分 = {
                 1, 如果 数量 < 1000
                 5, 如果 1000 ≤ 数量 ≤ 2000
                 7, 如果 数量 > 2000
             }
     算出本周内的积分
     找出最后一次上传小票的时间

     失败：
     - icon 展示 false状态
     - 提示文字 “Your receipt doesn't meet the requirements”
     - 时间显示后端数据库记录的时间

     * @param  qo 包含钱包地址
     * @return  返回值说明
     */
    @Override
    public ApiResponse<CardInfoVo> getCardInfoByWalletAddress(ReqBigBottleQo qo) {
        //钱包地址
        String walletAddress = qo.getWalletAddress();
        if(StrUtil.isBlank(walletAddress)){
            log.info("---> 缺失参数 walletAddress不能为空");
            return ApiResponse.error(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(), ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }
        walletAddress = walletAddress.toLowerCase();

        List<BVefutureBigBottle> bigBottles = getbVefutureBigBottles(walletAddress);

        if(CollectionUtil.isEmpty(bigBottles)){
            log.info("---> 钱包地址[{}]本周内没有合适的小票", walletAddress);
            return ApiResponse.error(ResultCode.RECEIPT_ERR_UNMEET.getCode(), ResultCode.RECEIPT_ERR_UNMEET.getMessage());
        }
        BVefutureBigBottle bigBottleLast = bigBottles.get(0);

        CardInfoVo cardInfoVo = new CardInfoVo();
        cardInfoVo.setDrinkName(bigBottleLast.getRetinfoDrinkName());
        cardInfoVo.setDrinkAmout(bigBottleLast.getRetinfoDrinkAmout());
        cardInfoVo.setDrinkCapacity(bigBottleLast.getRetinfoDrinkCapacity());
        //最后一张小传的上传时间 - 记入数据库的时间
        cardInfoVo.setReceiptUploadTime(bigBottleLast.getCreateTime());

        //设定积分
        cardInfoVo.setPoints(getPointsByReceipts(new ArrayList<>(Arrays.asList(bigBottleLast))));
        //cardInfoVo.setPoints(getPointsByReceipt(bigBottleLast));

        ApiResponse<CardInfoVo> success = ApiResponse.success(cardInfoVo);
        return success;
    }

    //获取本周内小票列表
    private List<BVefutureBigBottle> getbVefutureBigBottles(String walletAddress) {
        //当前本地时间
        LocalDateTime now = LocalDateTime.now();
        // 算出本周内的积分
        // 找出最后一次上传小票的时间
        LambdaQueryWrapper<BVefutureBigBottle> queryWrapper = new LambdaQueryWrapper<>();
        //钱包地址，返回信息是可用的，是否超期可用
        queryWrapper.eq(BVefutureBigBottle::getWalletAddress, walletAddress);
        //此时先把所有的数据都查出来
        //queryWrapper.eq(BVefutureBigBottle::getRetinfoIsAvaild, true);
        //queryWrapper.eq(BVefutureBigBottle::getIsTimeThreshold, true);

        //限制插入时间为本周的开始
        Date currentTime = BbDateTimeUtils.localDateTimeToDate(now);
        DateTime beginOfWeek = DateUtil.beginOfWeek(BbDateTimeUtils.localDateTimeToDate(now));
        queryWrapper.ge(BVefutureBigBottle::getCreateTime, beginOfWeek);
        queryWrapper.le(BVefutureBigBottle::getCreateTime, currentTime);

        //按id排序
        queryWrapper.orderByDesc(BVefutureBigBottle::getId);

        List<BVefutureBigBottle> bigBottles = baseMapper.selectList(queryWrapper);
        return bigBottles;
    }

    /**
     * 根据钱包信息获取当周积分
     * @param qo
     * @return
     */
    @Override
    public ApiResponse<CardInfoVo> getWeekPointsByWalletAddress(ReqBigBottleQo qo) {

        CardInfoVo cardInfoVo = new CardInfoVo();
        //钱包地址和图片地址
        String walletAddress = qo.getWalletAddress();
        if(StrUtil.isBlank(walletAddress)){
            log.info("---> 缺失参数 walletAddress不能为空");
            return ApiResponse.error(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(), ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }
        walletAddress = walletAddress.toLowerCase();
        //当前本地时间
        List<BVefutureBigBottle> currentWeekBigBottles = getbVefutureBigBottles(walletAddress);
        if(CollectionUtil.isEmpty(currentWeekBigBottles)){
            log.info("---> 该地址:[{}]本周没有积分", walletAddress);
            cardInfoVo.setWeekPoints(0);
            return ApiResponse.success(cardInfoVo);
        }
        Integer currWeekPoints = getPointsByReceipts(currentWeekBigBottles);
        cardInfoVo.setWeekPoints(currWeekPoints);
        return ApiResponse.success(cardInfoVo);
    }

    /**
     * 根据用户的钱包确定当天上传的次数是否到达最大的限制
     * @param qo
     * @return
     */
    @Override
    public ApiResponse<CountLimitVo> getCountLimit(ReqBigBottleQo qo) {
        CountLimitVo countLimitVo = new CountLimitVo();
        countLimitVo.setCountMax(countMax);
        //钱包地址和图片地址
        String walletAddress = qo.getWalletAddress();
        if(StrUtil.isBlank(walletAddress)){
            log.info("---> 缺失参数 walletAddress不能为空");
            return ApiResponse.error(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(), ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }

        walletAddress = walletAddress.toLowerCase();
        //查询出当天上传的次数
        Integer currentCount = getCountByWalletAddress(walletAddress);
        countLimitVo.setCountCurrent(currentCount);
        return ApiResponse.success(countLimitVo);
    }

    //查询出当前用户当天上传的小票张数
    private Integer getCountByWalletAddress(String walletAddress) {
        //当前本地时间
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<BVefutureBigBottle> queryWrapper = new LambdaQueryWrapper<>();
        //钱包地址，返回信息是可用的，是否超期可用
        queryWrapper.eq(BVefutureBigBottle::getWalletAddress, walletAddress);
        //限制插入时间为本周的开始
        Date currentTime = BbDateTimeUtils.localDateTimeToDate(now);
        DateTime beginOfWeek = DateUtil.beginOfDay(currentTime);
        queryWrapper.ge(BVefutureBigBottle::getCreateTime, beginOfWeek);
        queryWrapper.le(BVefutureBigBottle::getCreateTime, currentTime);
        //按id排序
        queryWrapper.orderByDesc(BVefutureBigBottle::getId);
        List<BVefutureBigBottle> bigBottles = baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isEmpty(bigBottles)) {
            return 0;
        }
        int count = bigBottles.stream()
                .collect(Collectors.toMap(
                        BVefutureBigBottle::getImgUrl,   // 去重 key（比如 name）
                        p -> p,            // 保留的值（这里是整个对象）
                        (existing, replacement) -> existing // 保留重复时的哪个（保留第一个）
                ))
                .size();  // 最后 map 的 size 就是去重后的 count
        return count;
    }

    /*
    * 如果两个都是true， 其他信息都有 但是饮料容积拿不到的时候 积分用第一条规则
    */
    private Integer getPoints(BVefutureBigBottle bigBottle) {
        Integer capacity = bigBottle.getRetinfoDrinkCapacity();
        if(ObjectUtil.isEmpty(capacity) && ObjectUtil.isNotEmpty(bigBottle.getRetinfoDrinkName()) && ObjectUtil.isNotEmpty(bigBottle.getRetinfoDrinkAmout())){
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 1;
        }
        if(capacity < 1000)
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 1;
        if(capacity <= 2000)
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 5;
        if(capacity > 2000)
            return bigBottle.getRetinfoDrinkAmout() * (Integer) 7;
        return 0;
    }


    /**
     * 根据饮料信息返回积分
     * - 积分 = {
     *                  1, 如果 数量 < 1000
     *                  5, 如果 1000 ≤ 数量 ≤ 2000
     *                  7, 如果 数量 > 2000
     *              }
     * 
     * @param  
     * @return  返回值说明
     */
    
    private Integer getPointsByReceipts(List<BVefutureBigBottle> bigBottles) {
        Integer sumPoint = bigBottles.stream()
                .mapToInt(this::getPoints)
                .sum();
        return sumPoint;
    }

    /**
     *
     * @param  reqBigBottleQo
     * @return  返回值说明
     */
    @Override
    public ApiResponse processReceipt(ReqBigBottleQo reqBigBottleQo) {

        //钱包地址和图片地址
        String walletAddress = reqBigBottleQo.getWalletAddress().toLowerCase();
        String imgUrl = reqBigBottleQo.getImgUrl();
        if(StrUtil.isBlank(walletAddress) || StrUtil.isBlank(imgUrl)){
            log.info("---> 缺失参数 walletAddress imgUrl都不能为空");
            return ApiResponse.error(ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getCode(), ResultCode.RECEIPT_ERR_PARAMETER_NOT_COMPLETE.getMessage());
        }
        //构造请求参数
        ParameterEntity parameterEntity = new ParameterEntity();
        parameterEntity.setImg_url(imgUrl);
        BodyEntity bodyEntity = new BodyEntity();
        bodyEntity.setWorkflow_id(bigBottleProperties.getCoze_workflow_id());
        bodyEntity.setParameters(parameterEntity);

        // 3. 使用 Gson（或 Jackson 等）将实体转换为 JSON 字符串
        String jsonString = JSON.toJSONString(bodyEntity);
        log.info("---> 请求参数为:{}", jsonString);
        // 4. 将 JSON 字符串封装为 RequestBody
        RequestBody body = RequestBody.create(jsonString, MediaType.get("application/json; charset=utf-8"));

        try {
            Request request = new Request.Builder()
                    .header("Authorization", bigBottleProperties.getCoze_token())
                    .header("Content-Type", "application/json")
                    .url(bigBottleProperties.getCoze_url())
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            String retJsonString = response.body().string();
            log.info("---> 返回值为:{}", retJsonString);

            RetinfoLLMJson retinfoLLMJson = JSON.parseObject(retJsonString, RetinfoLLMJson.class);
            String contentStr = retinfoLLMJson.getData();
            log.info("---> 返回的票据信息为:[{}]", contentStr);

            RetinfoBigBottle bigBottle = JSON.parseObject(contentStr, RetinfoBigBottle.class);

            if(!bigBottle.getRetinfoIsAvaild()){
                log.info("---> 该票据信息不完整");
                return ApiResponse.error(ResultCode.RECEIPT_ERR_UNAVAILABLE.getCode(), ResultCode.RECEIPT_ERR_UNAVAILABLE.getMessage());
            }

            //todo 存到数据库
            //以当前时间作为插入时间
            LocalDateTime currentTime = LocalDateTime.now();
            saveToDb(walletAddress, imgUrl, bigBottle, currentTime);
        } catch (IOException e) {
            //throw new BusinessException(430, "业务异常:"+e.getMessage(), e);
            e.printStackTrace();
            log.error("===> 业务异常:{}", e.getMessage());
            return ApiResponse.error(ResultCode.INTERNAL_ERROR.getCode(), ResultCode.INTERNAL_ERROR.getMessage());
        }
        return ApiResponse.success();
    }

    //存储到
    private void saveToDb(String walletAddress, String imgUrl, RetinfoBigBottle retinfoBigBottle, LocalDateTime currentTime) {

        ArrayList<RetinfoDrink> drinkList = retinfoBigBottle.getDrinkList();
        drinkList.forEach(drink -> {
            BVefutureBigBottle bigBottle = new BVefutureBigBottle();
            //公共信息
            bigBottle.setWalletAddress(walletAddress.toLowerCase());
            bigBottle.setImgUrl(imgUrl);
            bigBottle.setRetinfoIsAvaild(retinfoBigBottle.getRetinfoIsAvaild());
            bigBottle.setRetinfoReceiptTime(retinfoBigBottle.getRetinfoReceiptTime());
            bigBottle.setIsTimeThreshold(retinfoBigBottle.getTimeThreshold());
            //饮料信息
            bigBottle.setRetinfoDrinkName(drink.getRetinfoDrinkName());
            bigBottle.setRetinfoDrinkCapacity(drink.getRetinfoDrinkCapacity());
            bigBottle.setRetinfoDrinkAmout(drink.getRetinfoDrinkAmout());

            //一张小票用统一一个插入时间便于后期统计
            bigBottle.setCreateTime(BbDateTimeUtils.localDateTimeToDate(currentTime));
            this.save(bigBottle);
        });
    }
}
