package com.vefuture.big_bottle.web.vefuture.strategy.deplast;

/**
 * @author wangb
 * @date 2025/4/24
 * @description TODO: 类描述
 */


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 计算节省塑料量 Δ(x) 的工具类。
 * x 单位：mL（必须 ≥ 300）
 * 返回值：g
 */
public final class PlasticSaving {

    private PlasticSaving() {}          // 工具类，无需实例化

    /* ------------- 公共精度与常量 ---------------- */

    private static final MathContext MC = new MathContext(15, RoundingMode.HALF_UP);

    // w(x) 的系数（BigDecimal 支持科学计数法）
    private static final BigDecimal A = new BigDecimal("5.48412698E-8",  MC);   // +5.48…×10⁻⁸
    private static final BigDecimal B = new BigDecimal("-1.60507937E-4", MC);   // −1.60…×10⁻⁴
    private static final BigDecimal C = new BigDecimal("0.16157381",     MC);
    private static final BigDecimal D = new BigDecimal("-13.8071429",    MC);

    // k = 21.7 / 300
    private static final BigDecimal K = new BigDecimal("21.7")
            .divide(new BigDecimal("300"), MC);

    /* ------------- 计算入口 ---------------------- */

    /**
     * Δ(x)：用 1 个 x mL 瓶比若干 300 mL 瓶少用的塑料量（单位 g）
     */
    public static BigDecimal delta(BigDecimal x) {
        if (x.compareTo(new BigDecimal("300")) < 0) {
            throw new IllegalArgumentException("x must be ≥ 300 mL");
        }

        BigDecimal x2 = x.pow(2, MC);
        BigDecimal x3 = x.pow(3, MC);

        // w(x) = a·x³ + b·x² + c·x + d
        BigDecimal w = A.multiply(x3, MC)
                .add(B.multiply(x2, MC), MC)
                .add(C.multiply(x,  MC), MC)
                .add(D, MC);

        // 基准消耗 (21.7 / 300)·x
        BigDecimal baseline = K.multiply(x, MC);

        return baseline.subtract(w, MC);  // Δ(x)
    }

    /**
     * 便捷的 double 重载；精度要求高时仍应使用 BigDecimal 版本。
     */
    public static double delta(double x) {
        return delta(BigDecimal.valueOf(x)).doubleValue();
    }

    /* ------------- DEMO ------------------------- */

    public static void main(String[] args) {
        System.out.println("Δ(600)  = " + delta(600));   // ≈ 6.2 g
        System.out.println("Δ(1000) = " + delta(1000));  // ≈ 30.233… g
        System.out.println("Δ(1500) = " + delta(1500));  // ≈ 56 g
    }
}
