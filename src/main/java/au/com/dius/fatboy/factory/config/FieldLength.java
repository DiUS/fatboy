package au.com.dius.fatboy.factory.config;

public class FieldLength extends FactoryHint {
    private final long min;
    private final long max;

    private FieldLength(long min, long max) {
        if (min >= max) {
            throw new IllegalArgumentException("Min must be less than max");
        }

        this.max = max;
        this.min = min;
    }

    private FieldLength(long amount) {
        this.min = this.max = amount;
    }

    public int getLength() {
        return (int)(min + (Math.round((float) Math.random() * (max - min))));
    }

    public long getLongLength() {
        return min + (Math.round((double) Math.random() * (max - min)));
    }

    public static FieldLength random(long min, long max) {
        return new FieldLength(min, max);
    }

    public static FieldLength constant(long amount) {
        return new FieldLength(amount);
    }
}
