package com.nehori.get_recommendation;

public class Here {
    public static String at () {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
        String where = ste.getClassName() + " " + ste.getMethodName() + " " + ste.getLineNumber() + " ";
        return where;
    }
}
