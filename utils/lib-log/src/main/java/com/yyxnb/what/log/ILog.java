package com.yyxnb.what.log;

import java.util.List;
import java.util.Map;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/14
 * 描    述：打印信息接口
 * ================================================
 */
public interface ILog {

    /**
     * 配置项
     *
     * @return {@link LogConfig}
     */
    LogConfig init();

    /**
     * 返回最后的Log
     *
     * @return 结果
     */
    String getLastLog();

    /**
     * Log.DEBUG
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    void d(String message, Object... args);

    /**
     * Log.ERROR
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    void e(String message, Object... args);

    /**
     * Log.ERROR
     *
     * @param throwable throwable
     * @param message   内容
     * @param args      String.format(message,args)
     */
    void e(Throwable throwable, String message, Object... args);

    /**
     * Log.WARN
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    void w(String message, Object... args);

    /**
     * Log.INFO
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    void i(String message, Object... args);

    /**
     * Log.VERBOSE
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    void v(String message, Object... args);

    /**
     * Log.ASSERT
     *
     * @param message 内容
     * @param args    String.format(message,args)
     */
    void a(String message, Object... args);

    /**
     * 打印Json
     *
     * @param json Json数据
     */
    void json(String json);

    /**
     * 打印xml
     *
     * @param xml xml数据
     */
    void xml(String xml);

    /**
     * 打印map
     *
     * @param map map数据
     */
    void map(Map<?, ?> map);

    /**
     * 打印list
     *
     * @param list list数据
     */
    void list(List<?> list);

    /**
     * log监听
     */
    interface ILogPrintListener {
        /**
         * 日志输出
         *
         * @param priority 日志优先级
         * @param content  日志内容
         */
        void log(int priority, String content);
    }
}