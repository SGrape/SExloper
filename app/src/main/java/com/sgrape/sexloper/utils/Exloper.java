package com.sgrape.sexloper.utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by root on 16-4-22.
 */
public class Exloper {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg.obj);
        }
    };
    private BufferedWriter bw;

    private Exloper() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            new SuListener(handler, p.getInputStream(), SuListener.OUTPUT).start();
            new SuListener(handler, p.getErrorStream(), SuListener.ERROR).start();
            bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            bw.write("cd / \n");
            bw.flush();
            bw.write("ls \n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exec(String commond) {
        try {
            bw.write(commond + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Exloper getInstance() {
        return Instance.EXLOPER_THREAD;
    }

    static interface Instance {
        public static final Exloper EXLOPER_THREAD = new Exloper();
    }
}
