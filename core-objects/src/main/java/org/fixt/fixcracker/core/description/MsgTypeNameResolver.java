package org.fixt.fixcracker.core.description;

import quickfix.field.MsgType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class MsgTypeNameResolver {
    private static final Map<String, String> map = new HashMap<>();

    static {
        Field[] fields = MsgType.class.getDeclaredFields();
        for (Field f : fields) {
            if (!Modifier.isStatic(f.getModifiers()) || !f.getType().equals(String.class)) continue;
            try {
                map.put((String) f.get(null), f.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDescription(String msgType) {
        return map.get(msgType);
    }
}
