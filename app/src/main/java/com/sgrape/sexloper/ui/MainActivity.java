package com.sgrape.sexloper.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.sgrape.sexloper.R;
import com.sgrape.sexloper.utils.Exloper;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Exloper exloperThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exloperThread = Exloper.getInstance();
        et = (EditText) findViewById(R.id.et);
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER){
                    exloperThread.exec(et.getText().toString().trim());
                    et.setText("");
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exloperThread.exec(et.getText().toString());
                et.setText("");
            }
        });
    }
}
