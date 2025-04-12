package com.tool.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author wangb
 * @date 2025/3/30
 * @description TODO: 类描述
 */
@Slf4j
public class StringTest {

    //0x204002c2bD2FCE451c7806870E54C6C7509691E3
    @Test
    public void testStrLower(){
        String walletAddress = "0X204002C2BD2FCE451C7806870E54C6C7509691E3";
        System.out.println(walletAddress.toLowerCase());

    }
}
