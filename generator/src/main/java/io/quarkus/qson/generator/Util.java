package io.quarkus.qson.generator;

import io.quarkus.qson.util.Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Util {
    /**
     * Generate a classname from a pure generic type.  For example List<Foo> to what
     * a generated class would be for that  List_Foo
     *
     * @param type
     * @return
     */
    public static String generatedClassName(Type type) {
        return generatedClassName("io.quarkus.qson.generated", type);
    }

    /**
     * Generate a classname from a pure generic type.  For example List<Foo> to what
     * a generated class would be for that  List_Foo
     *
     * @param type
     * @return
     */
    public static String generatedClassName(String packageName, Type type) {
        String name = type.getTypeName()
                .replace(" ", "")
                .replace(',', '$')
                .replace("<", "_")
                .replace(">", "")
                .replace("java.util.", "")
                .replace("java.lang.", "")
                .replace('.', '_');
        return packageName + "." + name;
    }

    /**
     * Checks to see if clz is a user object or contains one (i.e. List<UserObject>)
     * If it does, it adds the class and generic type to the referenceMap
     *
     * @param referenceMap
     * @param clz
     * @param genericType
     */
    public static void addReference(Map<Type, Class> referenceMap, Class clz, Type genericType) {
        if (clz.isPrimitive()) return;
        if (clz.equals(String.class)
                || clz.equals(Integer.class)
                || clz.equals(Short.class)
                || clz.equals(Long.class)
                || clz.equals(Byte.class)
                || clz.equals(Boolean.class)
                || clz.equals(Double.class)
                || clz.equals(Float.class)
                || clz.equals(Character.class)) {
            return;
        }
        if (Map.class.isAssignableFrom(clz)
                || List.class.isAssignableFrom(clz)
                || Set.class.isAssignableFrom(clz)) {
            if (genericType != null && genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)genericType;
                if (Map.class.isAssignableFrom(clz)) {
                    addReference(referenceMap, Types.getRawType(pt.getActualTypeArguments()[1]), pt.getActualTypeArguments()[1]);
                } else {
                    addReference(referenceMap, Types.getRawType(pt.getActualTypeArguments()[0]), pt.getActualTypeArguments()[0]);
                }

            }
        } else {
            referenceMap.put(genericType, clz);
        }
    }
}
