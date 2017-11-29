package com.example.abnervictor.lab7;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by abnervictor on 2017/11/29.
 */

public class FileHelper {

    public FileHelper(){}

    public boolean writeStringWithName(Context context, String Filename, String Filecontent){
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(Filename,Context.MODE_PRIVATE);
            fileOutputStream.write(Filecontent.getBytes("UTF-8"));//指定编码
            fileOutputStream.close();
            Log.v("writeStringWithName:","successfully save file "+ Filename);
            return true;
        }catch (Exception e){
            Log.e("writeStringWithName:","can't save file "+ Filename);
            return false;
        }
    }

    public String readStringWithName(Context context, String Filename){
        try {
            FileInputStream fileInputStream = context.openFileInput(Filename);
            byte[] content = new byte[fileInputStream.available()];
            fileInputStream.read(content);
            String Content = new String(content,"UTF-8");//从指定编码将byte数组转回字符串
            fileInputStream.close();
            if (!Content.isEmpty()) return Content;
            else return null;
        }catch (Exception e){
            Log.e("readStringWithName:","can't read file "+ Filename);
            return null;
        }
    }

    public boolean deleteFileWithName(Context context, String Filename){
        try{
            return context.deleteFile(Filename);
        }catch (Exception e){
            Log.e("deleteFileWithName:","can't delete file "+ Filename);
            return false;
        }
    }

    public boolean deleteAllFiles(Context context){
        try {
            String[] filelist = context.fileList();
            for (int i = 2; i < filelist.length; i++){
                deleteFileWithName(context,filelist[i]);
            }
            return true;
        }catch (Exception e){
            Log.e("deleteAllFiles:","delete failed!");
            return false;
        }
    }

}
