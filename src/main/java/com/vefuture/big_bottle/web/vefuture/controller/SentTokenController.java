package com.vefuture.big_bottle.web.vefuture.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.common.domain.ApiResponse;
import com.vefuture.big_bottle.common.enums.ResultCode;
import com.vefuture.big_bottle.web.vefuture.entity.BlackList;
import com.vefuture.big_bottle.web.vefuture.entity.SentToken;
import com.vefuture.big_bottle.web.vefuture.entity.qo.BlackListQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.qo.SentTokenQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.SentTokenVO;
import com.vefuture.big_bottle.web.vefuture.service.SentTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangchao
 * @since 2025-06-11
 */
@RestController
@RequestMapping("/manage/sentToken")
@Slf4j
public class SentTokenController {

    @Autowired
    private SentTokenService sentTokenService;

    /**
     * 查询代币发送情况列表
     * @return
     */
    @RequestMapping(value = "/sentTokenList", method = RequestMethod.POST)
    public ApiResponse<Page<SentTokenVO>> getSentTokenList(HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       @RequestBody SentTokenQueryDTO dto){
        log.info("查询代币发送情况列表:page dto:[{}]", dto);
        Page<SentToken> page = new Page<>(dto.getCurrent(), dto.getSize());

        Page<SentTokenVO> sentTokenVOPage = sentTokenService.getSentTokenList(request, page, dto);
        return ApiResponse.success(sentTokenVOPage);
    }

    /**
     * 上传CSV文件
     * @param csvFile
     * @return
     */
    @PostMapping("/upload")
    public ApiResponse<String> uploadCsv(HttpServletRequest request, @RequestParam("file") MultipartFile csvFile) {
        if (csvFile.isEmpty()) {
            return ApiResponse.error(400, "上传的文件为空！");
        }
        sentTokenService.processSentTokenCsv(request, csvFile);
        return ApiResponse.success("CSV 文件上传并处理成功！");
    }
}
