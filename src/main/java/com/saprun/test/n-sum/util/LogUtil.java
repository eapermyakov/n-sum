/*
 * Copyright (C) JSC SAPRUN, 2019. All Rights Reserved.
 * http://www.saprun.com
 */

package com.saprun.test.magnit.util;

/**
 *
 * @author Permyakov, Evgeny
 * @email evgeniy.permyakov@saprun.com
 * @created 18.10.2019 15:20:35
 */
public class LogUtil {

    public static void info(String m) {
        info(-1, m);
    }

    public static void info(long time, String m) {
        if(Util.isEmpty(m)) return;

        if(time >= 0) m += ". Time (ms): " + time;
        System.out.println(m);
    }

    public static void info(String pattern, Object... args) {
        if(Util.isEmpty(pattern)) return;

        info(-1, pattern, args);
    }

    public static void info(long time, String pattern, Object... args) {
        if(Util.isEmpty(pattern)) return;

        info(time, String.format(pattern, args));
    }

    public static void error(String m) {
        error(m, null);
    }

    public static void error(Throwable ex) {
        error(null, ex);
    }

    public static void error(String m, Throwable ex) {
        if(Util.isNotEmpty(m)) {
            System.err.println("Error: " + m);
        }
        if(ex != null) {
            ex.printStackTrace(System.err);
        }
    }
}
