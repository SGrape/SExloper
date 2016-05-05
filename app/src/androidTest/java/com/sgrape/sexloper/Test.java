package com.sgrape.sexloper;

import android.test.AndroidTestCase;

import com.sgrape.sexloper.utils.ExloperThread;

import java.io.File;

/**
 * Created by root on 16-4-22.
 */
public class Test extends AndroidTestCase {
    public void test() {
        File file = new File("/");
        System.out.println(file.list());
    }
}
