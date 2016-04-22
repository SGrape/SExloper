package com.sgrape.sexloper.utils;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

/**
 * Created by root on 16-4-22.
 */
public class Su extends Thread {
    private Process p;
    private BufferedWriter bw;
    private boolean bequit;
    private SuListener err;
    private SuListener exe;
    private Handler handler;
    private LinkedList<String> commonds = new LinkedList<>();

    private Su() {
    }

    public static Su newInstance(Handler handler) {
        Su su = new Su();
        su.handler = handler;
        su.start();
        return su;
    }

    @Override
    public void run() {
        try {
            p = Runtime.getRuntime().exec("su");
            bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            err = new SuListener(handler, p.getInputStream(), SuListener.ERROR);
            exe = new SuListener(handler, p.getInputStream(), SuListener.OUTPUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exec(String commond) {
        try {
            synchronized (bw) {
                bw.write(commond);
                bw.newLine();
                bw.flush();
                commonds.addLast(commond);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        exec("exit");
        commonds.clear();
        err.quit();
        exe.quit();
    }
}
