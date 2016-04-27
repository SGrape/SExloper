package com.sgrape.sexloper.utils;

import android.os.Handler;
import android.os.Message;

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
    }

    public static Su newInstance() {
        final Su su = new Su();
        synchronized (su.lock) {
            Thread work = new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println("started");
                        su.p = Runtime.getRuntime().exec("su");
                        su.bw = new BufferedWriter(new OutputStreamWriter(su.p.getOutputStream()));
//                    su.err = new SuListener(su.handler, su.p.getInputStream(), SuListener.ERROR);
                        su.exe = new SuListener(su.handler, su.p.getInputStream(), SuListener.OUTPUT);
//                    su.err.start();
                        su.exe.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            };
            work.start();
            try {
                work.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return su;
    }


    public void exec(String commond) {
        synchronized (lock) {
            try {
                System.out.println(commond);
                bw.write(commond + " \n");
                bw.newLine();
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
