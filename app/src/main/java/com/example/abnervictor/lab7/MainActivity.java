package com.example.abnervictor.lab7;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private View new_password_layout;
    private View enter_password_layout;
    private TextInputLayout new_pw_outer;
    private TextInputLayout confirm_pw_outer;
    private TextInputLayout enter_pw_outer;
    private Button ok_btn;
    private Button clr_btn;
    private boolean newPasswordIsNeeded;
    private int errorCnt;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        sharedPreferences = this.getSharedPreferences("com.abnervictor.lab7",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("firstlaunch",true)){
            Toast.makeText(MainActivity.this,"初次启动，需要设置密码",Toast.LENGTH_SHORT).show();
            newPasswordIsNeeded = true;
        }//检测是否初次启动
        else{
            Toast.makeText(MainActivity.this,"请输入密码以进入文件管理器",Toast.LENGTH_SHORT).show();
            newPasswordIsNeeded = false;
        }
        switchPwInterface();
        setButtonListener();
    }
    private void findView(){
        new_password_layout = findViewById(R.id.new_password_layout);
        enter_password_layout = findViewById(R.id.enter_password_layout);
        ok_btn = findViewById(R.id.ok_btn);
        clr_btn = findViewById(R.id.clr_btn);
        new_pw_outer = findViewById(R.id.new_pw_outer);
        confirm_pw_outer = findViewById(R.id.confirm_pw_outer);
        enter_pw_outer = findViewById(R.id.enter_pw_outer);

        errorCnt = 0;
    }
    private void switchPwInterface(){
        if (newPasswordIsNeeded){
            new_password_layout.setVisibility(View.VISIBLE);
            enter_password_layout.setVisibility(View.GONE);
        }
        else{
            new_password_layout.setVisibility(View.GONE);
            enter_password_layout.setVisibility(View.VISIBLE);
        }
        new_pw_outer.setErrorEnabled(false);
        confirm_pw_outer.setErrorEnabled(false);
        enter_pw_outer.setErrorEnabled(false);

        new_pw_outer.getEditText().setText("");
        confirm_pw_outer.getEditText().setText("");
        enter_pw_outer.getEditText().setText("");

        new_pw_outer.clearFocus();
        confirm_pw_outer.clearFocus();
        enter_pw_outer.clearFocus();
    }

    private void setButtonListener(){
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPasswordIsNeeded){
                    if (checkPassword()) {
                        Toast.makeText(MainActivity.this,"密码已保存",Toast.LENGTH_SHORT).show();
                        setPassword();//密码格式检查通过，保存密码
                        newPasswordIsNeeded = false;
                        switchPwInterface();
                    }
                }
                else{
                    boolean pass = checkPassword();
                    if (pass){
                        Toast.makeText(MainActivity.this,"密码正确",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,FileEditActivity.class);
                        startActivity(intent);
                        finish();
                    }//密码正确，进入文件管理
                    else if (errorCnt >= 5){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("密码错误次数太多！");
                        builder.setMessage("检测到您已经输入了5次错误密码，是否重设密码？（注意：重设密码将会清空文件）");
                        builder.setNegativeButton("取消",null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("firstlaunch",true);
                                editor.apply();
                                newPasswordIsNeeded = true;
                                switchPwInterface();
                                //清空用户文件
                                //清空用户文件
                                FileHelper fileHelper = new FileHelper();
                                fileHelper.deleteAllFiles(MainActivity.this);
                                //
                            }
                        });
                        builder.show();
                        errorCnt = 0;
                    }//输入密码错误5次，提示重设密码
                }
            }
        });
        clr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_pw_outer.setErrorEnabled(false);
                confirm_pw_outer.setErrorEnabled(false);
                enter_pw_outer.setErrorEnabled(false);

                new_pw_outer.getEditText().setText("");
                confirm_pw_outer.getEditText().setText("");
                enter_pw_outer.getEditText().setText("");

                new_pw_outer.clearFocus();
                confirm_pw_outer.clearFocus();
                enter_pw_outer.clearFocus();
            }
        });
    }

    private boolean checkPassword(){
        boolean passCheck = true;
        if(newPasswordIsNeeded){
            String new_pw = new_pw_outer.getEditText().getText().toString();
            String confirm_pw = confirm_pw_outer.getEditText().getText().toString();
            if (new_pw.length() == 0){
                new_pw_outer.setErrorEnabled(true);
                new_pw_outer.setError("密码不能为空");
                new_pw_outer.requestFocus();
                passCheck = false;
            }
            else{
                new_pw_outer.setErrorEnabled(false);
                new_pw_outer.clearFocus();
                if(confirm_pw.length() == 0){
                    confirm_pw_outer.setErrorEnabled(true);
                    confirm_pw_outer.setError("请再输入一次密码以验证");
                    confirm_pw_outer.requestFocus();
                    passCheck = false;
                }
                else if (!new_pw.equals(confirm_pw)){
                    confirm_pw_outer.setErrorEnabled(true);
                    confirm_pw_outer.setError("密码不一致，请检查输入");
                    confirm_pw_outer.requestFocus();
                    passCheck = false;
                }
                else {
                    confirm_pw_outer.setErrorEnabled(false);
                }
            }
        }//检查是否输入了密码，其次检查输入的密码是否一致
        else {
            String password = enter_pw_outer.getEditText().getText().toString();
            if (password.length() == 0){
                enter_pw_outer.setErrorEnabled(true);
                enter_pw_outer.setError("密码不为空");
                passCheck = false;
            }
            else {
                String CorrectPassword = sharedPreferences.getString("password","PwdNotSet");
                if (password.equals(CorrectPassword)){
                    enter_pw_outer.setErrorEnabled(false);
                    errorCnt = 0;
                }//密码正确
                else {
                    enter_pw_outer.setErrorEnabled(true);
                    enter_pw_outer.setError("密码错误，请检查输入");
                    errorCnt++;
                    passCheck = false;
                }//密码错误
            }
        }//检查密码是否正确
        return passCheck;
    }

    private void setPassword(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password",new_pw_outer.getEditText().getText().toString());
        editor.putBoolean("firstlaunch",false);
        editor.apply();
    }

}
