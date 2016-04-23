package com.sgrape.sexloper.utils;


import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by root on 16-4-22.
 */
public class SuListener extends Thread {
    protected final Handler handler;
    protected BufferedReader br;
    protected boolean beQuit;
    public static final int ERROR = 0;
    public static final int OUTPUT = 1;
    protected int type;

    public SuListener(Handler handler, InputStream is, int type) {
        this.handler = handler;
        br = new BufferedReader(new InputStreamReader(is));
        this.type = type;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = br.readLine()) != null || !beQuit) {
                if (beQuit) break;
                synchronized (handler) {
                    handler.obtainMessage(ERROR).sendToTarget();
                    System.out.println(line);
                }
            }
            br.close();
            interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        beQuit = true;
        if (!isInterrupted())
            interrupt();
    }

}
