package com.george.cordova.plugin;

import android.util.Log;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import com.george.ILogger;

public class CordovaLogger implements ILogger {

    /**********************
     * Internal properties
     **********************/
    private boolean enabled = false;
    private CordovaInterface cordova;
    private CordovaWebView webView;
    private String logTag;

    /*******************
     * Constructors
     *******************/
    public CordovaLogger(CordovaInterface cordova, CordovaWebView webView, String logTag) {
        initialize(cordova, webView, logTag);
    }

    public CordovaLogger(CordovaInterface cordova, CordovaWebView webView, String logTag, boolean enabled) {
        initialize(cordova, webView, logTag);
        setEnabled(enabled);
    }

    /*******************
     * Public API
     *******************/
    @Override
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    @Override
    public boolean getEnabled(){
        return this.enabled;
    }

    @Override
    public void error(String msg) {
        Log.e(logTag, msg);
        logToCordova(msg, "error");
    }

    @Override
    public void warn(String msg) {
        Log.w(logTag, msg);
        logToCordova(msg, "warn");
    }

    @Override
    public void info(String msg) {
        Log.i(logTag, msg);
        logToCordova(msg, "info");
    }

    @Override
    public void debug(String msg) {
        Log.d(logTag, msg);
        logToCordova(msg, "log");
    }

    @Override
    public void verbose(String msg) {
        Log.v(logTag, msg);
        logToCordova(msg, "debug");
    }

    /*******************
     * Internal methods
     *******************/
    private void initialize(CordovaInterface cordova, CordovaWebView webView, String logTag){
        this.cordova = cordova;
        this.webView = webView;
        this.logTag = logTag;
    }

    private void logToCordova(String msg, String logLevel){
        if(enabled){
            executeGlobalJavascript("console."+logLevel+"(\""+logTag+": "+escapeDoubleQuotes(msg)+"\")");
        }
    }

    private String escapeDoubleQuotes(String string){
        String escapedString = string.replace("\"", "\\\"");
        escapedString = escapedString.replace("%22", "\\%22");
        return escapedString;
    }

    private void executeGlobalJavascript(final String jsString){
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + jsString);
            }
        });
    }
}
