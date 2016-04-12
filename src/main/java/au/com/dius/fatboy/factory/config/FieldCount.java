package au.com.dius.fatboy.factory.config;

public class FieldCount extends FactoryConfig {
    private FieldCount(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Min must be less than max");
        }

        this.max = max;
        this.min = min;
    }

    private FieldCount(int amount) {
        this.min = this.max = amount;
    }

    public int fieldCount() {
        return min + (Math.round((float) Math.random() * (max - min)));
    }

    public static FieldCount random(int min, int max) {
        return new FieldCount(min, max);
    }

    public static FieldCount constant(int amount) {
        return new FieldCount(amount);
    }

    private int min;
    private int max;
}
