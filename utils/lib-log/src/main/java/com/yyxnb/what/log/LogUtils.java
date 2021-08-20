package com.yyxnb.what.log;

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
        throw new RuntimeException();
    }

    /**
     * 配置项
     *
     * @param iLog 自定义 {@link ILog}
     * @return {@link LogConfig}
     */
    public static LogConfig init(@NonNull ILog iLog) {
        LOGGER_PRINTER = iLog;
        return LOGGER_PRINTER.init();
    }

    /**
     * 配置项
     *
     * @return {@link LogConfig}
     */
    public static LogConfig init() {
        return init(new LoggerImpl());
    }

    /**
     * 返回最后的Log
     *
     * @return 结果
     */
    public static String getLastLog() {
        return LOGGER_PRINTER.getLastLog();
    }

    /**
     * Log.DEBUG
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    public static void d(String message, Object... args) {
        LOGGER_PRINTER.d(message, args);
    }

    /**
     * Log.ERROR
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    public static void e(String message, Object... args) {
        LOGGER_PRINTER.e(null, message, args);
    }

    /**
     * Log.ERROR
     *
     * @param throwable throwable
     * @param message   内容
     * @param args      String.format(message,args)
     */
    public static void e(Throwable throwable, String message, Object... args) {
        LOGGER_PRINTER.e(throwable, message, args);
    }

    /**
     * Log.INFO
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    public static void i(String message, Object... args) {
        LOGGER_PRINTER.i(message, args);
    }

    /**
     * Log.VERBOSE
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    public static void v(String message, Object... args) {
        LOGGER_PRINTER.v(message, args);
    }

    /**
     * Log.WARN
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    public static void w(String message, Object... args) {
        LOGGER_PRINTER.w(message, args);
    }

    /**
     * Log.ASSERT
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    public static void a(String message, Object... args) {
        LOGGER_PRINTER.a(message, args);
    }

    /**
     * 打印Json
     *
     * @param json Json数据
     */
    public static void json(String json) {
        LOGGER_PRINTER.json(json);
    }

    /**
     * 打印xml
     *
     * @param xml xml数据
     */
    public static void xml(String xml) {
        LOGGER_PRINTER.xml(xml);
    }

    /**
     * 打印map
     *
     * @param map map数据
     */
    public static void map(Map<?, ?> map) {
        LOGGER_PRINTER.map(map);
    }

    /**
     * 打印list
     *
     * @param list list数据
     */
    public static void list(List<?> list) {
        LOGGER_PRINTER.list(list);
    }

}