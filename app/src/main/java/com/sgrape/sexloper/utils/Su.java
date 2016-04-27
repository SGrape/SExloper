package com.sgrape.sexloper.utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;

/**
 * Created by root on 16-4-22.
 */
public class Su {
    private Process p;
    private Object lock = new Object();
    private BufferedWriter bw;
    private SuListener err;
    private SuListener exe;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg.obj);
        }
    };
    private LinkedList<String> commonds = new LinkedList<>();

    private Su() {
        synchronized (lock) {
            try {
                System.out.println("started");
                p = Runtime.getRuntime().exec("su");
                bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//                        err = new SuListener(handler, p.getInputStream(), SuListener.ERROR);
                exe = new SuListener(handler, p.getInputStream(), SuListener.OUTPUT);
//                        err.start();
                exe.start();
                System.out.println("started");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    public static Su newInstance() {
        return new Su();
    }


    public void exec(String commond) {
        synchronized (lock) {
            try {
                bw.write((commond + " \n"));
                bw.flush();
                commonds.addLast(commond);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void quit() {
        exec("exit");
        commonds.clear();
        if (err != null) err.quit();
        exe.quit();
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
