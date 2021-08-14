package com.yyxnb.oh.log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/07/19
 * 描    述：日志打印输出
 * ================================================
 */
public class LogUtils {

    private static ILog LOGGER_PRINTER = null;

    private LogUtils() {
    }

    public static LogConfig init(@NonNull ILog iLog) {
        LOGGER_PRINTER = iLog;
        return LOGGER_PRINTER.init();
    }

    public static LogConfig init() {
        return init(new LoggerImpl());
    }

    public static String getLastLog() {
        return LOGGER_PRINTER.getLastLog();
    }

    public static void d(String message, Object... args) {
        LOGGER_PRINTER.d(message, args);
    }

    public static void e(String message, Object... args) {
        LOGGER_PRINTER.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        LOGGER_PRINTER.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        LOGGER_PRINTER.i(message, args);
    }

    public static void v(String message, Object... args) {
        LOGGER_PRINTER.v(message, args);
    }

    public static void w(String message, Object... args) {
        LOGGER_PRINTER.w(message, args);
    }

    public static void a(String message, Object... args) {
        LOGGER_PRINTER.a(message, args);
    }

    public static void json(String json) {
        LOGGER_PRINTER.json(json);
    }

    public static void xml(String xml) {
        LOGGER_PRINTER.xml(xml);
    }

    public static void map(Map<?, ?> map) {
        LOGGER_PRINTER.map(map);
    }

    public static void list(List<?> list) {
        LOGGER_PRINTER.list(list);
    }

}