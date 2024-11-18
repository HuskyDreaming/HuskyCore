package com.huskydreaming.huskycore.utilities;

import java.util.regex.Pattern;

public class NumberUtil {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String string) {
        return pattern.matcher(string).matches();
    }
}