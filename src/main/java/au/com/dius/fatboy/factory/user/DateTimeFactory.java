package au.com.dius.fatboy.factory.user;

import au.com.dius.fatboy.factory.impl.AbstractClassFactory;
import org.joda.time.DateTime;

import java.lang.reflect.Field;

public class DateTimeFactory extends AbstractClassFactory<DateTime> {
    @Override
    public boolean supports(Class clazz) {
        return clazz == DateTime.class;
    }

    @Override
    public DateTime create(Field field) {
        return new DateTime();
    }

}
