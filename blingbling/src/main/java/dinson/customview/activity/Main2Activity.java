package dinson.customview.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dinson.customview.R;

import static dinson.customview.kotlin.LogExtentionKt.logv;

public class Main2Activity extends AppCompatActivity {
    /* mDataPath 是字库在手机上的存储位置 */
    private String mDataPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tessdata/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getStorageAccessPermission(); // 获取权限
        File parentfile = new File(mDataPath);
        if (!parentfile.exists()) { // 确保路径存在
            parentfile.mkdir();
        }
        copyFiles(); // 复制字库到手机
        String lang = "eng"; // 使用简体中文 + 英文检测
        TessBaseAPI mTess = new TessBaseAPI();
        mTess.init(Environment.getExternalStorageDirectory().getAbsolutePath(), lang); // 初始化，第一个参数为 mDataPath 的父目录

        Long starttime = System.currentTimeMillis(); // 检测开始时间
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable._001_bg_input_cursor_gold); // 获取测试图片

        Thread thread = new Thread() {
            @Override
            public void run() {
                mTess.setImage(bitmap);
                String OCRresult = mTess.getUTF8Text(); // 拿到字符串结果
                Long endtime = System.currentTimeMillis(); // 检测结束时间
                logv(() -> "识别结果：" + OCRresult);
                logv(() -> (endtime - starttime) + " ms");
            }
        };
        thread.start();


    }

    private void copyFiles() {
        String[] datafilepaths = new String[]{mDataPath + "/chi_sim.traineddata", mDataPath + "/eng.traineddata"}; // 拷两个字库过去
        for (String datafilepath : datafilepaths) {
            copyFile(datafilepath);
        }
    }

    private void copyFile(String datafilepath) {
        try {
            String filepath = datafilepath;
            String[] filesegment = filepath.split(File.separator);
            String filename = filesegment[(filesegment.length - 1)]; // 获取字库文件名
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open(filename); // 打开字库文件
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();
            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getStorageAccessPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请先授权！", Toast.LENGTH_LONG).show();
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
