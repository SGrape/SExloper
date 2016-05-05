package com.sgrape.sexloper.utils;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;

/**
 * Created by root on 16-4-22.
 */
public class ExloperThread {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg.obj);
        }
    };

    private ExloperThread() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            new SuListener(handler, p.getInputStream(), SuListener.OUTPUT).start();
            new SuListener(handler, p.getErrorStream(), SuListener.ERROR).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ExloperThread getInstance() {
        return Instance.EXLOPER_THREAD;
    }

    static interface Instance {
        public static final ExloperThread EXLOPER_THREAD = new ExloperThread();
    }
}
