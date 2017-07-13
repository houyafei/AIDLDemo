package com.example.app3;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button btn1,btn2,btn3 ,btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        btn4 = (Button) findViewById(R.id.button);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                Intent intent1 = new Intent();
                intent1.setAction("com.hyf.a1");
                intent1.addCategory("com.hyf.b1") ;
                //intent1.setDataAndType(Uri.parse("file://as"),"txttxt/pp");
                ComponentName cn0 = intent1.resolveActivity(getPackageManager());
                if (cn0!=null && cn0.getClassName()!=null){
                    startActivity(intent1);
                }else{
                    Toast.makeText(MainActivity.this, "不匹配", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.button2:
                Intent intent2 = new Intent();
                intent2.setAction("com.hyf.a2");
                intent2.setDataAndType(Uri.parse("file://as"),"txttxt/pp");
                ComponentName cn = intent2.resolveActivity(getPackageManager());
                if (cn!=null && cn.getClassName()!=null){
                    startActivity(intent2);
                }else{
                    Toast.makeText(MainActivity.this, "不匹配", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.button3 :
                Intent intent3 = new Intent();
                intent3.setDataAndType(Uri.parse("file://as"),"txttxt/pp");
                ComponentName cn1 = intent3.resolveActivity(getPackageManager());
                if (cn1!=null && cn1.getClassName()!=null){
                    startActivity(intent3);
                }else{
                    Toast.makeText(MainActivity.this, "不匹配", Toast.LENGTH_SHORT).show();
                }

                Log.e(TAG,"cn1==null---"+(cn1==null));
                break;
            case R.id.button:
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                break;
        }
    }
}
