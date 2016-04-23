package com.sgrape.sexloper.utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 16-4-22.
 */
public class Su {
    private ReentrantLock lock;
    private Process p;
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
        su.lock = new ReentrantLock();
        su.lock.lock();
        new Thread() {
            @Override
            public void run() {
                try {
                    su.p = Runtime.getRuntime().exec("su");
                    su.bw = new BufferedWriter(new OutputStreamWriter(su.p.getOutputStream()));
                    su.err = new SuListener(su.handler, su.p.getInputStream(), SuListener.ERROR);
                    su.exe = new SuListener(su.handler, su.p.getInputStream(), SuListener.OUTPUT);
                    su.lock.unlock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return su;
    }


    public synchronized void exec(String commond) {
        try {
            bw.write(commond);
            bw.newLine();
            bw.flush();
            commonds.addLast(commond);
        } catch (IOException e) {
            e.printStackTrace();
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
