package com.vefuture.big_bottle.web.vefuture.entity.vo;

import lombok.Data;

/**
 * @author wangb
 * @date 2025/5/17
 * @description TODO: 类描述
 */
@Data
public class StatisticsResultDTO {

    /*
        const wallet_address_count = ref('0')
        const total_image_count = ref('0')
        const passed_address_count = ref('0')
        const passed_receipt_count = ref('0')
        const unpassed_address_count = ref('0')
        const unpassed_receipt_count = ref('0')
    */
    private Integer walletAddressCount = 0;
    private Integer totalImageCount = 0;
    private Integer passedAddressCount = 0;
    private Integer passedReceiptCount = 0;
    private Integer unpassedAddressCount = 0;
    private Integer unpassedReceiptCount = 0;
}
