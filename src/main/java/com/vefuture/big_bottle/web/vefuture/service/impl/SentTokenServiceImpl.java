package com.vefuture.big_bottle.web.vefuture.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.SentToken;
import com.vefuture.big_bottle.web.vefuture.entity.qo.SentTokenQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.SentTokenVO;
import com.vefuture.big_bottle.web.vefuture.mapper.SentTokenMapper;
import com.vefuture.big_bottle.web.vefuture.service.SentTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2025-06-11
 */
@Slf4j
@Service
public class SentTokenServiceImpl extends ServiceImpl<SentTokenMapper, SentToken> implements SentTokenService {

    @Override
    public Page<SentTokenVO> getSentTokenList(HttpServletRequest request, Page<SentToken> page, SentTokenQueryDTO dto) {
        String walletAddress = StrUtil.isBlank(dto.getWalletAddress()) ? "" : dto.getWalletAddress().toLowerCase().trim();

        log.info("查询条件为:{}", walletAddress);
        LambdaQueryWrapper<SentToken> queryWrapper = Wrappers.<SentToken>lambdaQuery()
                // 当 walletAddress 非空时才加 like
                .like(StrUtil.isNotBlank(walletAddress), SentToken::getWalletAddress, walletAddress)
                // 当 blackTypes 列表不为 null 且不空时，才加 in 条件
                /*.in(blackTypes != null && !blackTypes.isEmpty(),
                        BlackList::getBlackType,
                        blackTypes)*/
                // 固定按 createTime 降序
                .orderByAsc(SentToken::getId);
        Page<SentToken> sentTokenPage = this.page(page, queryWrapper);
        // 手动转换为 VO（可以用 MapStruct 或简单 copy）
        Page<SentTokenVO> voPage = new Page<>();
        voPage.setCurrent(sentTokenPage.getCurrent());
        voPage.setSize(sentTokenPage.getSize());
        voPage.setTotal(sentTokenPage.getTotal());

        List<SentTokenVO> voRecords = sentTokenPage.getRecords().stream()
                .map(entity -> {
                    SentTokenVO vo = new SentTokenVO();
                    BeanUtils.copyProperties(entity, vo); // 你也可以手写映射逻辑
                    //设置图片名字
                    vo.setImgName(entity.getImgUrl().substring(entity.getImgUrl().lastIndexOf("/") + 1));
                    return vo;
                })
                .collect(Collectors.toList());
        voPage.setRecords(voRecords);
        return voPage;
    }

    /**
     * todo 如何保证不上传同一个事务的数据两次
     * @param request
     * @param csvFile
     */
    @Override
    public void processSentTokenCsv(HttpServletRequest request, MultipartFile csvFile) {
        List<SentToken> tokenList = new ArrayList<>();
        int batchSize = 500;
        OffsetDateTime now = OffsetDateTime.now();
        try (CSVReader reader = new CSVReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> allRows = reader.readAll();
            for (String[] row : allRows) {
                //System.out.println(Arrays.toString(row));
                if("address".equalsIgnoreCase(row[0])){
                    continue;
                }

                String walletAddress = row[0].trim().toLowerCase();
                String sentTokenStr = row[1];
                String imgUrl = row[2];
                String transactionUrl = row[3];
                String transactionValue = row[3].substring(row[3].lastIndexOf("/")+1).trim().toLowerCase();

                SentToken tempSentToken = new SentToken();
                tempSentToken.setWalletAddress(walletAddress);
                tempSentToken.setTransactionValue(transactionValue);
                tempSentToken.setSentToken(new BigDecimal(sentTokenStr));
                tempSentToken.setImgUrl(imgUrl);
                tempSentToken.setTransactionUrl(transactionUrl);
                tempSentToken.setCreateTime(now);

                tokenList.add(tempSentToken);
                // 每 batchSize 条数据批量写入一次
                if (tokenList.size() >= batchSize) {
                    this.getBaseMapper().batchInsert(tokenList);
                    tokenList.clear();
                }
            }

            // 处理最后不足 batchSize 的数据
            if (!tokenList.isEmpty()) {
                this.getBaseMapper().batchInsert(tokenList);
            }
        } catch (IOException | CsvException e) {
            log.error("处理CSV文件异常", e);
            throw new RuntimeException(e);
        }
    }
}
