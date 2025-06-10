package com.vefuture.big_bottle.web.vefuture.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vefuture.big_bottle.web.vefuture.entity.SentToken;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vefuture.big_bottle.web.vefuture.entity.qo.SentTokenQueryDTO;
import com.vefuture.big_bottle.web.vefuture.entity.vo.SentTokenVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangchao
 * @since 2025-06-11
 */
public interface SentTokenService extends IService<SentToken> {

    Page<SentTokenVO> getSentTokenList(HttpServletRequest request, Page<SentToken> page, SentTokenQueryDTO dto);

    void processSentTokenCsv(HttpServletRequest request, MultipartFile csvFile);
}
