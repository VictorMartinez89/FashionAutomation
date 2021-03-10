package com.enterprise.fashion.web.utils;

import com.enterprise.fashion.web.config.MyDriver;
import java.util.logging.*;
import java.io.IOException;


import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class MLogger {

    public MLogger(){}
    public static Logger logger = Logger.getLogger(MLogger.class.getName());
    private static final String LOGGER_PATH ="src/test/java/com/enterprise/fashion/web/logs/logs.txt";

    public static void initializeFile(){
        try {
            FileHandler handler = new FileHandler(LOGGER_PATH);
            logger.addHandler(handler);
        }catch (Exception ex){

        }
        MLogger.print(0, "MLogger was Initialized");
    }

    public static void print(int level, String msg){
        System.out.println("");
        setLOGGER(level,msg);
    }

    public static void setLOGGER(int level, String message) {
        switch (level){
            case 0 : logger.log(Level.OFF, message); break;
            case 1 : logger.log(Level.FINE,message); break;
            case 2 : logger.info(message); break;
            case 3 : logger.warning(message); break;
            case 4 : logger.log(Level.SEVERE,message); break;
            default: logger.log(Level.INFO, message); break;
        }
    }

}
