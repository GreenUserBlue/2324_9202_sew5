package x_hp_vierte_code_stuff;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

import static java.lang.System.err;
import static java.lang.System.out;

public class DeetMain {
    public static void main(String... args) {
        try {
            Class<?> c = Class.forName("Deet");
            Object t = c.newInstance();

            Method method = c.getDeclaredMethod("testBar");
//                Type[] pType = method.getGenericParameterTypes();
//
//              Locale.class.isAssignableFrom(pType[0].getClass());

                out.format("invoking %s()%n", method.getName());
                try {
                    method.setAccessible(true);
                    Object o = method.invoke(t);
                    out.format("%s() returned %b%n", method, (Boolean) o);

                    // Handle any exceptions thrown by method to be invoked.
                } catch (InvocationTargetException x) {
                    Throwable cause = x.getCause();
                    err.format("invocation of %s failed: %s%n",
                            method.getName(), cause.getMessage());
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
        // production code should handle these exceptions more gracefully
    }
}
