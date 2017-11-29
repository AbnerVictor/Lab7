package com.example.abnervictor.lab7;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by abnervictor on 2017/11/28.
 */

public class FileEditActivity extends AppCompatActivity {

    private TextInputLayout filename_outer;
    private TextInputLayout filecontent_outer;
    private Button save_btn;
    private Button load_btn;
    private Button clear_btn;
    private Button delete_btn;
    private Button allfiles_btn;
    private FileHelper fileHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);
        findView();
        setBtnListener();//按键监听器
        setEdittextListener();
    }
    private void findView(){
        filename_outer = findViewById(R.id.filename_outer);
        filecontent_outer = findViewById(R.id.filecontent_outer);
        save_btn = findViewById(R.id.save_btn);
        load_btn = findViewById(R.id.load_btn);
        clear_btn = findViewById(R.id.clear_btn);
        delete_btn = findViewById(R.id.delete_btn);
        allfiles_btn = findViewById(R.id.allfiles_btn);
        fileHelper = new FileHelper();
    }

    private void setBtnListener(){
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Filename = filename_outer.getEditText().getText().toString();
                String Filecontent = filecontent_outer.getEditText().getText().toString();
                if (checkContent()){
                    if (fileHelper.writeStringWithName(FileEditActivity.this,Filename,Filecontent)){
                        Toast.makeText(FileEditActivity.this,"保存成功！",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(FileEditActivity.this,"保存失败！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        load_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Filename = filename_outer.getEditText().getText().toString();
                if (!(Filename.length() > 0)){
                    Toast.makeText(FileEditActivity.this,"文件名不能为空",Toast.LENGTH_SHORT).show();
                    filename_outer.setErrorEnabled(true);
                    filename_outer.setError("文件名不能为空");
                }
                else {
                    String content = fileHelper.readStringWithName(FileEditActivity.this,Filename);
                    if (content!=null){
                        filecontent_outer.getEditText().setText(content);
                        Toast.makeText(FileEditActivity.this,"读取成功！",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(FileEditActivity.this,"文件不存在！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename_outer.getEditText().setText("");
                filecontent_outer.getEditText().setText("");
                filename_outer.setErrorEnabled(false);
                filecontent_outer.setErrorEnabled(false);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Filename = filename_outer.getEditText().getText().toString();
                if (!(Filename.length() > 0)){
                    Toast.makeText(FileEditActivity.this,"文件名不能为空",Toast.LENGTH_SHORT).show();
                    filename_outer.setErrorEnabled(true);
                    filename_outer.setError("文件名不能为空");
                }
                else {
                    if (fileHelper.deleteFileWithName(FileEditActivity.this,Filename)){
                        Toast.makeText(FileEditActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(FileEditActivity.this,"文件不存在或删除失败！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        allfiles_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] filelist = getApplicationContext().fileList();
                StringBuilder list = new StringBuilder();
                for  (int i = 2; i < filelist.length; i++){
                    if (list.length()>0) list.append(", ");
                    list.append(filelist[i]);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(FileEditActivity.this);
                builder.setTitle("文件目录如下：");
                builder.setMessage(list);
                builder.setPositiveButton("确定", null);
                builder.show();
            }
        });
    }

    private void setEdittextListener(){
        filename_outer.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filename_outer.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        filecontent_outer.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filecontent_outer.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean checkContent(){
        String Filename = filename_outer.getEditText().getText().toString();
        String Filecontent = filecontent_outer.getEditText().getText().toString();
        if (!(Filename.length() > 0)){
            Toast.makeText(FileEditActivity.this,"文件名不能为空",Toast.LENGTH_SHORT).show();
            filename_outer.setErrorEnabled(true);
            filename_outer.setError("文件名不能为空");
            return false;
        }
        else if(!(Filecontent.length() > 0)){
            Toast.makeText(FileEditActivity.this,"文件内容不能为空",Toast.LENGTH_SHORT).show();
            filecontent_outer.setErrorEnabled(true);
            filecontent_outer.setError("文件内容不能为空");
            return false;
        }
        return true;
    }

}
