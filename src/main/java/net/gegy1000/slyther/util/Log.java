package net.gegy1000.slyther.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Supplier;

public final class Log {
    private Log() {}

    private static final Logger LOGGER = LogManager.getLogger("Slyther");

    public static void catching(Throwable t) {
        LOGGER.catching(t);
    }

    private static String getCallerInfo() {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement stack = stacks[3];
        return String.format("[%s:%s(%s)]", stack.getFileName(), stack.getLineNumber(), stack.getMethodName());
    }
    
    public static void debug(String message, Object... params) {
        Object[] ps = new Object[params.length + 1];
        ps[0] = getCallerInfo();
        for (int i = 1; i < ps.length; i++) {
            ps[i] = params[i - 1];
        }
        LOGGER.debug("{} " + message, ps);
    }

    public static void error(String message, Object... params) {
        Object[] ps = new Object[params.length + 1];
        ps[0] = getCallerInfo();
        for (int i = 1; i < ps.length; i++) {
            ps[i] = params[i - 1];
        }
        LOGGER.error("{} " + message, ps);
    }

    public static void info(String message, Object... params) {
        Object[] ps = new Object[params.length + 1];
        ps[0] = getCallerInfo();
        for (int i = 1; i < ps.length; i++) {
            ps[i] = params[i - 1];
        }
        LOGGER.info("{} " + message, ps);
    }

    public static void warn(String message, Object... params) {
        Object[] ps = new Object[params.length + 1];
        ps[0] = getCallerInfo();
        for (int i = 1; i < ps.length; i++) {
            ps[i] = params[i - 1];
        }
        LOGGER.warn("{} " + message, ps);
    }

    public static void warn(String message, Supplier<?> supplier, Object... params) {
        Supplier<?>[] paramSuppliers = new Supplier<?>[params.length + 1];
        paramSuppliers[0] = supplier;
        for (int i = 0; i < params.length;) {
            Object obj = params[i];
            paramSuppliers[++i] = () -> obj;
        }

        Object[] ps = new Object[paramSuppliers.length + 1];
        ps[0] = getCallerInfo();
        for (int i = 1; i < ps.length; i++) {
            ps[i] = paramSuppliers[i - 1];
        }
        LOGGER.warn("{} " + message, ps);
    }

    public static String bytes(byte[] arr) {
        StringBuilder str = new StringBuilder();
        for (int i = 0, end = arr.length - 1, to = Math.min(end, 39);; i++) {
            String h = Integer.toHexString(arr[i] & 0xFF);
            if (h.length() == 1) {
                str.append('0');
            }
            str.append(h);
            if (i == to) {
                if (to < end) {
                    str.append("... ").append(arr.length - to + 1).append(" more");
                }
                return str.toString();
            }
            if (i % 2 == 1) {
                str.append(' ');
            }
        }
    }
}
