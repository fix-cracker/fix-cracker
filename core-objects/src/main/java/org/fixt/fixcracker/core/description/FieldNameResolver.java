package org.fixt.fixcracker.core.description;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Field;

import java.lang.reflect.Modifier;
import java.util.*;

public class FieldNameResolver {
    private static final Logger LOG = LoggerFactory.getLogger(FieldNameResolver.class);
    private static final Map<Integer, String> map;

    static {
        Map<Integer, String> fmap = new HashMap<>();
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(Collections.singletonList(ClasspathHelper.forClass(Field.class))));
        Set<Class<? extends Field>> subTypes = new HashSet<>();
        addSubTypes(reflections, subTypes, Field.class);
        for (Class clazz : subTypes) {
            java.lang.reflect.Field f;
            try {
                f = clazz.getDeclaredField("FIELD");
                if (!Modifier.isStatic(f.getModifiers()) || !f.getType().equals(int.class)) continue;
            } catch (NoSuchFieldException e) {
                continue;
            }
            try {
                fmap.put((Integer) f.get(null), clazz.getSimpleName());
            } catch (IllegalAccessException e) {
                LOG.error("error", e);
            }
        }
        map = fmap;
    }

    private static void addSubTypes(Reflections reflections, Set<Class<? extends Field>> set, Class clazz) {
        Set<Class> subTypes = reflections.getSubTypesOf(clazz);
        if (subTypes.isEmpty()) {
            set.add(clazz);
            return;
        }
        for (Class subType : subTypes) {
            addSubTypes(reflections, set, subType);
        }
    }

    public static String getDescription(Integer tag) {
        return map.get(tag);
    }
}
