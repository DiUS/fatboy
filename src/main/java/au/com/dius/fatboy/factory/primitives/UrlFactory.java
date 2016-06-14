package au.com.dius.fatboy.factory.primitives;

import au.com.dius.fatboy.FatBoy;
import au.com.dius.fatboy.factory.impl.AbstractClassFactory;
import au.com.dius.fatboy.utils.RandomUtils;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlFactory extends AbstractClassFactory<URL> {

    @Override
    public boolean supports(Class clazz) {
        return clazz == URL.class;
    }

    @Override
    public URL create(Field field, Class<?> clazz) {
        try {
            return new URL("https://" + FatBoy.FAKER.internet().url());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
