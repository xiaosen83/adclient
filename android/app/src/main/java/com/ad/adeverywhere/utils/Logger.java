package com.ad.adeverywhere.utils;

import android.util.Log;

/**
 * Created by cwang on 7/18/16.
 */

public class Logger {
    private static Logger ourInstance = new Logger();

    public static Logger getInstance() {
        return ourInstance;
    }

    private Logger() {
    }

    static final byte FATAL = 0;
    static final byte ERROR = 1;
    static final byte WARNING = 2;
    static final byte INFORMATION = 3;
    static final byte DEBUG = 4;
    static final byte MAX_LEVEL = 4;

    static final String[] levelNames = { "FATAL", "ERROR", "WARNING", "INFO", "DEBUG" };

    String UI_LOG_FILE_NAME = "UI.log";
    String UI_LOG_ROTATE_FILE_NAME = "UI.log.0";
    String UI_LOG_EMAILFILE_NAME = "AppLog.txt";
    String MC_LOG_FILE_NAME = "MobileConnect.log";
    String AV_LOG_EMAILFILE_NAME = "AvPluginLog.txt";
    String MC_PCAP_FILE_NAME = "MobileConnect.log.cap";
    String NX_LOG_FILE_NAME = "NxPlugin.log";
    String NX_LOG_ROTATE_FILE_NAME = "NxPlugin.log.0";
    String NX_LOG_EMAILFILE_NAME = "NxPluginLog.txt";
    String NX_PACKET_TRACE_FILE_NAME = "NxPacketTrace.pcap";
    String NX_PACKET_TRACE_EMAILFILE_NAME = "NxPacketTrace.pcap";

    int MAX_LOG_LEVEL = DEBUG;


    private byte mRequiredLogLevel = INFORMATION;

    private synchronized void log(int level, String className, Throwable ex) {
        if (level <= MAX_LOG_LEVEL) {
            ex.printStackTrace();

            switch (level) {
                case DEBUG:
                    Log.d(className, ex.toString(), ex);
                    break;
                case INFORMATION:
                    Log.i(className, ex.toString(), ex);
                    break;
                case WARNING:
                    Log.w(className, ex.toString(), ex);
                    break;
                case ERROR:
                case FATAL:
                    Log.e(className, ex.toString(), ex);
                    break;
            }
        }
    }

    private synchronized void log(int level, String className, String msg) {
        if (level <= mRequiredLogLevel) {
            // Log rotation check
            //logRotate();


            switch (level) {
                case DEBUG:
                    Log.d(className, msg);
                    break;
                case INFORMATION:
                    Log.i(className, msg);
                    break;
                case WARNING:
                    Log.w(className, msg);
                    break;
                case ERROR:
                case FATAL:
                    Log.e(className, msg);
                    break;
            }
        }
    }

    public synchronized void logError(String className, String msg) {
        log(ERROR, className, msg);
    }

    public synchronized void logError(String className, Throwable ex) {
        log(ERROR, className, ex);
    }

    public synchronized void logError(String className, String msg, Throwable ex) {
        logError(className, msg);
        logError(className, ex);
    }

    public synchronized void logWarn(String className, String msg) {
        log(WARNING, className, msg);
    }

    public synchronized void logFatal(String className, String msg) {
        log(FATAL, className, msg);
    }

    public synchronized void logFatal(String className, Throwable ex) {
        log(FATAL, className, ex);
    }

    public synchronized void logDebug(String className, String msg) {
        log(DEBUG, className, msg);
    }

    public synchronized void logDebug(String className, Throwable ex) {
        log(DEBUG, className, ex);
    }

    public synchronized void logDebug(String className, String msg, Throwable ex) {
        logDebug(className, msg);
        logDebug(className, ex);
    }

    public synchronized void logInfo(String className, String msg) {
        log(INFORMATION, className, msg);
    }

}
