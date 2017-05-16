package com.careyun.voiceassistant.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Huangmq on 2017/4/18.
 */

public class readTxtFile {
    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param
     */
    private Context mContext;
    public readTxtFile(Context mContext){
        this.mContext = mContext;
    }
    /**
     * 从assets中读取txt
     */
    public ArrayList<String> readFromAssets() {
        ArrayList<String> text = new ArrayList<String>();
        try {
            InputStream is = mContext.getAssets().open("pattern.txt");
            text = readTextFromSDcard(is);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }
    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    private  ArrayList<String>  readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is,"GBK");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        ArrayList<String> readRes = new ArrayList<String>();

        while ((str = bufferedReader.readLine()) != null) {
            readRes.add(str);
            buffer.append(str);
            buffer.append("\n");
        }
        return readRes;
    }

}
