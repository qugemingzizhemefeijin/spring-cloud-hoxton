package com.atguigu.springcloud.test.tale;

import com.atguigu.springcloud.test.tale.util.TaleHelper;
import com.atguigu.springcloud.test.tale.enums.Units;

public final class TaleUnitConversion {

    private TaleUnitConversion() {
        throw new AssertionError("No Instances.");
    }

    /**
     * 将长度转换为请求的单位。<br>
     * 有效单位：英里、海里、英寸、码、米、米、公里、厘米、英尺
     *
     * @param length     距离
     * @param originUnit 原始单位，默认值为 KILOMETERS
     * @param finalUnit  转换单位，默认为值 KILOMETERS
     * @return 返回转换后的距离
     */
    public static double convertLength(double length, Units originUnit, Units finalUnit) {
        if (length == 0) {
            return 0;
        }
        if (originUnit == null) {
            originUnit = Units.KILOMETERS;
        }
        if (finalUnit == null) {
            finalUnit = Units.KILOMETERS;
        }
        if (originUnit == finalUnit) {
            return length;
        }

        return TaleHelper.radiansToLength(TaleHelper.lengthToRadians(length, originUnit), finalUnit);
    }

}
