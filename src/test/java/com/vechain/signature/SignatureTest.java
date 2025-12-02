package com.vechain.signature;

import org.junit.jupiter.api.Test;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import org.web3j.crypto.Hash;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
/**
 * @author wangb
 * @date 2025/6/11
 * @description TODO: 类描述
 */
public class SignatureTest {
    private static final String PRIVATE_KEY = "0xf4d978f76050407a11cf8eb485cdf39f31d62a0d5ac3245388797348b50b4f11";
    private static final String ETH_SIGNED_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n32";

    /**
     * 签名工具测试
     */
    @Test
    public void testSign() throws Exception {
        String walletAddress = "0x1234567890abcdef1234567890abcdef12345678";
        BigInteger sentToken = new BigInteger("850000000000000000"); // 0.85 token * 10^18
        String imgUrl = "https://example.com/image/receipt123.jpg";
        BigInteger dePlastic = new BigInteger("123");  // 比如 123 克

        String signature = signClaimData(walletAddress, sentToken, imgUrl, dePlastic);

        System.out.println("签名结果：" + signature);
    }

    public static String signClaimData(String walletAddress, BigInteger sentToken, String imgUrl, BigInteger dePlastic) throws Exception {
        // 1️⃣ 打包数据，模拟 abi.encodePacked
        byte[] addressBytes = Numeric.hexStringToByteArray(walletAddress);
        addressBytes = padAddress(addressBytes); // Ethereum 地址固定 20 字节，pad 到 32 字节

        byte[] sentTokenBytes = ByteBuffer.allocate(32).put(new byte[12]).put(sentToken.toByteArray()).array();

        byte[] imgUrlBytes = imgUrl.getBytes(StandardCharsets.UTF_8);
        byte[] imgUrlHash = Hash.sha3(imgUrlBytes);  // Solidity 里 string 是 dynamic 类型 → encodePacked 时通常先做 hash

        byte[] dePlasticBytes = ByteBuffer.allocate(32).put(new byte[12]).put(dePlastic.toByteArray()).array();

        // 拼接打包
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(addressBytes);
        outputStream.write(sentTokenBytes);
        outputStream.write(imgUrlHash);   // 用 hash，不直接拼 raw 字符串
        outputStream.write(dePlasticBytes);

        byte[] packedMessage = outputStream.toByteArray();

        // 2️⃣ Keccak256 哈希
        byte[] messageHash = Hash.sha3(packedMessage);

        // 3️⃣ Ethereum Signed Message 包裹
        ByteArrayOutputStream prefixStream = new ByteArrayOutputStream();
        prefixStream.write(ETH_SIGNED_MESSAGE_PREFIX.getBytes(StandardCharsets.UTF_8));
        prefixStream.write(messageHash);

        byte[] ethSignedHash = Hash.sha3(prefixStream.toByteArray());

        // 4️⃣ 签名
        BigInteger privateKeyBigInt = Numeric.toBigInt(PRIVATE_KEY);
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKeyBigInt);
        Sign.SignatureData signatureData = Sign.signMessage(ethSignedHash, ecKeyPair, false);

        // 5️⃣ 拼接 r + s + v → 最终签名
        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        byte[] v = signatureData.getV();

        ByteArrayOutputStream sigStream = new ByteArrayOutputStream();
        sigStream.write(r);
        sigStream.write(s);
        sigStream.write(v);

        byte[] signatureBytes = sigStream.toByteArray();
        String signatureHex = Numeric.toHexString(signatureBytes);

        return signatureHex;
    }

    private static byte[] padAddress(byte[] addressBytes) {
        byte[] padded = new byte[32];
        System.arraycopy(addressBytes, 0, padded, 12, 20);
        return padded;
    }
}
