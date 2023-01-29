package com.example.util;

import java.text.DecimalFormat;

public class ToDecimalUtil {
    public static String toStr(Double sum){
        DecimalFormat decimalFormat = new DecimalFormat();

        if (sum == null) {
            return decimalFormat.format(0.0);
        }

        return decimalFormat.format(sum);
    }
}
