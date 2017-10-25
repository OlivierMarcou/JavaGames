package com.oodrive.omnikles.cryptodoc.utils;

import com.oodrive.omnikles.cryptodoc.pojo.Configuration;

public class Logs {

    public static void sp(int chanel, String line){
        switch (chanel){
            case 2:
                System.err.println(line);
                break;
            case 1:
                if(Configuration.debug)
                    System.out.println(line);
                break;
            default:
                System.out.println(line);
                break;
        }
    }

    public static void sp(Object line){
        sp(0, String.valueOf(line));
    }

    public static void sp(int chanel , Object line){
        sp(chanel, String.valueOf(line));
    }

    public static void sp(String line){
        sp(0, line);
    }

    public static void spDump(int chanel, String[] lines){
        for(String line: lines)
            Logs.sp(chanel, line);
    }

    public static void spDump(int chanel, Object[] lines){
        for(Object line: lines)
            Logs.sp(chanel, line);
    }

    public static void spDump(String[] lines){
        for(String line: lines)
            Logs.sp(line);
    }

    public static void spDump(Object[] lines){
        for(Object line: lines)
            Logs.sp(line);
    }
}
