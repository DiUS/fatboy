package au.com.dius.fatboy.utils;

import java.util.Random;

public class RandomUtils {
    @SafeVarargs
    public static <T> T random(T... values) {
        return values[Math.abs(new Random().nextInt(values.length))];
    }
}
