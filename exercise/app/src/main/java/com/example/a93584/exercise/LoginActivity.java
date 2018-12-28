
package com.example.a93584.exercise;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import com.example.a93584.exercise.MainActivity;
import com.example.a93584.exercise.R;

import com.example.a93584.exercise.MD5Utils;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back, tv_register, tv_find_psw;
    private Button btn_login;
    private String userName, psw, spPsw;
    private EditText et_user_name, et_psw;
    private Tencent mTencent;
    private String APP_ID =
                "1108059120";
    private IUiListener loginListener;
    private String SCOPE = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void initQqLogin() {
        mTencent = Tencent.createInstance(APP_ID, this);
        //创建QQ登录回调接口
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                //登录成功后回调该方法
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError uiError) {
                //登录失败后回调该方法
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                Log.e("LoginError:", uiError.toString());
            }

            @Override
            public void onCancel() {
                //取消登录后回调该方法
                Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void init() {

        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back = findViewById(R.id.tv_back);

        tv_register = findViewById(R.id.tv_register);
        tv_find_psw = findViewById(R.id.tv_find_psw);
        btn_login = findViewById(R.id.btn_login);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        tv_find_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initQqLogin();
                mTencent.login(LoginActivity.this, "all", loginListener);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = et_user_name.getText().toString().trim();
                psw = et_psw.getText().toString().trim();
                String md5Psw = MD5Utils.md5(psw);
                spPsw = readPsw(userName);
                try {
                    InputStream is = new FileInputStream("E:/iodemo/ch01.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "please input username", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginActivity.this, "please input passwords", Toast.LENGTH_SHORT).show();
                    return;
                } else if (md5Psw.equals(spPsw)) {
                    Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                    saveLoginStatus(true, userName);
                    Intent data = new Intent();
                    data.putExtra("isLogin", true);
                    setResult(RESULT_OK, data);
                    LoginActivity.this.finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    return;
                } else if ((spPsw != null && !TextUtils.isEmpty(spPsw) && !md5Psw.equals(spPsw))) {
                    Toast.makeText(LoginActivity.this, "username and passwords not consistent", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(LoginActivity.this, "username not existed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String readPsw(String userName) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(userName, "");
    }

    private void saveLoginStatus(boolean status, String userName) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserName", userName);
        editor.commit();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            if (resultCode == -1) {
                Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
                Tencent.handleResultData(data, loginListener);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String userName = data.getStringExtra("userName");
            if (!TextUtils.isEmpty(userName)) {
                et_user_name.setText(userName);
                et_user_name.setSelection(userName.length());
            }
        }
    }
}
