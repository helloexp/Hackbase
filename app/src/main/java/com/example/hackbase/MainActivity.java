package com.example.hackbase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpRequest;

public class MainActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private Button reset;
    private Button login;
    private String TAG=MainActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_username=findViewById(R.id.username);
        et_password=findViewById(R.id.password);

        reset=findViewById(R.id.reset);
        login=findViewById(R.id.login);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setText("");
                et_password.setText("");

                et_username.requestFocus();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=et_username.getText().toString();
                String password=et_password.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    Log.d("Main","用户名或密码不可为空");
                    Toast.makeText(MainActivity.this,"用户名或密码不可为空",Toast.LENGTH_SHORT).show();

                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Main","username:"+username);
                        Log.d("Main","password:"+password);
                        boolean flag= login(username,password);

                        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                        Looper.prepare();
                        if(flag){
                            Toast.makeText(MainActivity.this,"登录成功，正在跳转...",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();

                        }
                        Looper.loop();

                    }
                }).start();
            }
        });

    }

    private boolean login(String username, String password) {
        Log.d("login","进入login 方法");
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("password",password);

        // 此处url 是服务端地址，注意修改ip 地址及端口
        String url="http://192.168.2.127:8080/login";
        String result=HttpRequest.post(url).body(jsonObject.toJSONString()).execute().body().toString();
        Log.d(TAG, "login:  "+result);
        if(result.contains("ok")){
            return true;
        }else{
            return false;
        }
    }
}