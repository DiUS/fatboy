package au.com.dius.fatboy.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    public static <T> T random(List<T> values) {
        return values.get(Math.abs(new Random().nextInt(values.size())));
    }

    @SafeVarargs
    public static <T> T random(T... values) {
        return values[Math.abs(new Random().nextInt(values.length))];
    }
}
