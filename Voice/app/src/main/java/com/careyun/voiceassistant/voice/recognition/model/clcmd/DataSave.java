package com.careyun.voiceassistant.voice.recognition.model.clcmd;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Huangmq on 2017/3/31.
 */

public class DataSave {

    public String mAudioDirPath;
    public String mTextDirPath;
    private static final String SAMPLE_DIR_NAME = "AAAData";
    private static final String AUDIO = "audio";
    private static final String TEXT = "text";
    private final String TMPLE_AUDIO_FILE = "temp.pcm";
    public static Context mContext;
    private final static String FILE_NAME = ".txt"; // 设置文件的名称

    private static class SingletonHolder {
        private static DataSave instance = new DataSave();
    }

    public static DataSave getInstance() {
        return SingletonHolder.instance;
    }

    public DataSave() {
        initPath();
    }

/*
    private void daveFileToSD(String filename,String filecotent){

    }
*/

    public void audioSave(byte[] bytes, String filename) {
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        File file = new File(mAudioDirPath + filename);
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
            Log.e("DataSave", "语音数据保存成功");
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }


    public void Save(String data, String filename) throws IOException {
        BufferedWriter outPut = null;
        outPut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mTextDirPath + filename, true)));
        outPut.write(data + "\n");  //将String字符串以字节流的形式写入到输出流中
        outPut.close();         //关闭输出流

    }

    public void DeleteFile(String filename) {
        File file = new File(mTextDirPath + filename);
        if (file.exists()) {
            file.delete();
        }
    }

    private void initPath() {
        if (mAudioDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME + "/" + AUDIO + "/";
            makeDir(mAudioDirPath);
        }
        if (mTextDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mTextDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME + "/" + TEXT + "/";
            makeDir(mTextDirPath);
        }
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void copyFromAssetsToSdcard(boolean isCover, String text) {
        String source = mAudioDirPath + TMPLE_AUDIO_FILE;
        String dest = mAudioDirPath + text + ".pcm";
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
//                is = mContext.getResources().getAssets().open(source);
                is = new FileInputStream(source);
//                is=mContext.openFileInput(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
