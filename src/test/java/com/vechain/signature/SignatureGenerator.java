package com.vechain.signature;

import org.junit.jupiter.api.Test;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author wangb
 * @date 2025/6/11
 * @description TODO: 类描述
 */
public class SignatureGenerator {

    @Test
    public void test(){
        // 你的 trustedSigner 对应私钥（Hardhat node 打印出的私钥） -> 不要加 0x 前缀
        String privateKeyHex = "ac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80";

        // 你要签的数据：
        String walletAddress = "0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266";
        BigInteger sentToken = BigInteger.valueOf(100);
        String imgUrl = "https://example.com/test.jpg";
        BigInteger dePlastic = BigInteger.valueOf(10);

        // 1️⃣ 计算 imgUrlHash (keccak256)
        byte[] imgUrlHash = Hash.sha3(imgUrl.getBytes(StandardCharsets.UTF_8));

        // 2️⃣ abi.encodePacked(walletAddress, sentToken, imgUrlHash, dePlastic)
        byte[] packed = concat(
                Numeric.hexStringToByteArray(walletAddress),
                toBytesPadded(sentToken, 32),
                imgUrlHash,
                toBytesPadded(dePlastic, 32)
        );

        // 3️⃣ keccak256(packed)
        byte[] hash = Hash.sha3(packed);

        // 4️⃣ toEthSignedMessageHash
        byte[] ethSignedMessageHash = Hash.sha3(
                concat(("\u0019Ethereum Signed Message:\n32").getBytes(StandardCharsets.UTF_8), hash)
        );

        // 5️⃣ 用私钥签名
        BigInteger privateKey = new BigInteger(privateKeyHex, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKey);

        Sign.SignatureData signature = Sign.signMessage(ethSignedMessageHash, ecKeyPair, false);

        // 6️⃣ 输出 signature
        String r = Numeric.toHexString(signature.getR());
        String s = Numeric.toHexString(signature.getS());
        String v = Numeric.toHexString(signature.getV());

        System.out.println("Signature R: " + r);
        System.out.println("Signature S: " + s);
        System.out.println("Signature V: " + v);

        // 拼成完整 signature
        byte[] sig = concat(signature.getR(), signature.getS(), signature.getV());
        System.out.println("Full Signature: " + Numeric.toHexString(sig));
    }
    // 工具函数：concat
    public static byte[] concat(byte[]... arrays) {
        int totalLength = Arrays.stream(arrays).mapToInt(a -> a.length).sum();
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }
        return result;
    }

    // 工具函数：BigInteger -> bytes32
    public static byte[] toBytesPadded(BigInteger value, int length) {
        byte[] result = new byte[length];
        byte[] bytes = value.toByteArray();

        int bytesLength = bytes.length;
        int srcPos = 0;

        if (bytes[0] == 0) {
            bytesLength -= 1;
            srcPos = 1;
        }

        if (bytesLength > length) {
            throw new RuntimeException("Input is too large to fit in byte array of size " + length);
        }

        int destPos = length - bytesLength;
        System.arraycopy(bytes, srcPos, result, destPos, bytesLength);
        return result;
    }
}
