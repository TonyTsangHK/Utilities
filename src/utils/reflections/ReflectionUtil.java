package utils.reflections;

import utils.data.ImmutableMap;
import utils.data.MapChainer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-01-17
 * Time: 11:52
 */
public class ReflectionUtil {
    private static Map<Class, Class> compatibleClassMap = new ImmutableMap(
            new MapChainer<Class, Class>()
                    .put(byte.class, Byte.class).put(Byte.class, byte.class)
                    .put(boolean.class, Boolean.class).put(Boolean.class, boolean.class)
                    .put(short.class, Short.class).put(Short.class, short.class)
                    .put(int.class, Integer.class).put(Integer.class, int.class)
                    .put(long.class, Long.class).put(Long.class, long.class)
                    .put(float.class, Float.class).put(Float.class, float.class)
                    .put(double.class, Double.class).put(Double.class, double.class)
                    .put(char.class, Character.class).put(Character.class, char.class)
                    .getMap()
    );

    private ReflectionUtil() {};

    public static boolean isCompatibleType(Class clz1, Class clz2) {
        return compatibleClassMap.get(clz1) == clz2;
    }

    public static Method searchForMethod(Class clz, String methodName, Class ... paramClasses) {
        Method[] methods = clz.getMethods();

        Method m = null;

        for (Method method : methods) {
            Class[] acceptedParameters = method.getParameterTypes();

            if (methodName.equals(method.getName()) && paramClasses.length == acceptedParameters.length) {
                boolean parameterMatch = true;
                for (int i = 0; i < acceptedParameters.length; i++) {
                    Class paramClass = paramClasses[i], acceptedParamClass = acceptedParameters[i];

                    if (
                            paramClass != acceptedParamClass && !acceptedParamClass.isAssignableFrom(paramClass) &&
                                    !isCompatibleType(paramClass, acceptedParamClass)
                            ) {
                        parameterMatch = false;
                        break;
                    }
                }

                if (parameterMatch) {
                    m = method;
                    break;
                }
            }
        }

        return m;
    }
}
