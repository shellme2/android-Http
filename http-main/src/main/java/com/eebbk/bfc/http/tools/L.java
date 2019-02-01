package com.eebbk.bfc.http.tools;

import com.eebbk.bfc.bfclog.BfcLog;
import com.eebbk.bfc.http.config.BfcHttpConfigure;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * log print class
 */

public class L {
    public static String DEFAULT_TAG = ">>>BFC_HTTP>>>";

    private static final boolean LOGGER = BfcHttpConfigure.debugMode();

    private static final BfcLog bfcLog;

    static {
        bfcLog = new BfcLog.Builder()
                .tag(DEFAULT_TAG)
                .showLog(true)
                .methodOffset(1)
                .methodCount(0)
                .build();
    }

    private L() {
    }

    public static void v(String tag, String msg) {
        if (LOGGER) {
            bfcLog.tag(tag).v(printfClassLineStr(msg));
        }
    }

    public static void d(String tag, String msg) {
        if (LOGGER) {
            bfcLog.tag(tag).d(printfClassLineStr(msg));
        }
    }

    public static void i(String tag, String msg) {
        if (LOGGER) {
            bfcLog.tag(tag).i(printfClassLineStr(msg));
        }
    }

    public static void w(String tag, String msg) {
        if (LOGGER) {
            bfcLog.tag(tag).w(printfClassLineStr(msg));
        }
    }

    public static void e(String tag, String msg) {
        if (LOGGER) {
            bfcLog.tag(tag).e(printfClassLineStr(msg));
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOGGER) {
            msg += "\n";
            msg += formatStackTrace(tr);
            bfcLog.tag(tag).e(printfClassLineStr(msg));
        }
    }

    public static void v(String msg) {
        if (LOGGER) {
            bfcLog.v(printfClassLineStr(msg));
        }
    }

    public static void d(String msg) {
        if (LOGGER) {
            bfcLog.d(printfClassLineStr(msg));
        }
    }

    public static void i(String msg) {
        if (LOGGER) {
            bfcLog.i(printfClassLineStr(msg));
        }
    }

    public static void w(String msg) {
        if (LOGGER) {
            bfcLog.w(printfClassLineStr(msg));
        }
    }

    public static void e(String msg) {
        if (LOGGER) {
            bfcLog.e(printfClassLineStr(msg));
        }
    }

    public static void e(Throwable tr) {
        if (LOGGER) {
            bfcLog.e(printfClassLineStr(formatStackTrace(tr)));
        }
    }


    public static void e(String msg, Throwable tr) {
        if (LOGGER) {
            msg += "\n";
            msg += formatStackTrace(tr);
            bfcLog.e(printfClassLineStr(msg));
        }
    }

    public static void e(Exception e, String msg) {
        bfcLog.e(msg);
    }

    /**
     * print class name method name and line number
     */
    private static String printfClassLineStr(String str) {
        StringBuilder strBuffer = new StringBuilder();
//        StackTraceElement[] mStackTrace = new Throwable().getStackTrace();

        strBuffer.append(str);
//        strBuffer.append("            ,File:(").append(mStackTrace[2].getFileName());
//        strBuffer.append(":").append(mStackTrace[2].getLineNumber()).append(")");
//        strBuffer.append(",Method:").append(mStackTrace[2].getMethodName());

        return strBuffer.toString();
    }

    private static String formatStackTrace(Throwable throwable) {
        if (throwable == null) return "";
        String rtn = throwable.getStackTrace().toString();
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            writer.flush();
            rtn = writer.toString();
            printWriter.close();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rtn;
    }
}
