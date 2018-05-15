package com.jairus.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jairus.R;
import com.jairus.Utils.PermissionUtil;
import com.jairus.Utils.camera.CameraUtil;
import com.jairus.Utils.camera.CropBean;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int REQUEST_CODE_ALBUM = 101;//相册回调
    private final int REQUEST_CODE_CAMER = 102;//相机回调
    private final int REQUEST_CODE_CROP = 103;//裁剪回调

    //存放头像的File路径
    public static File imageFile = new File(Environment.getExternalStorageDirectory(),
            "head_image.jpg");

    private ImageView ivHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivHead = (ImageView) findViewById(R.id.iv_head);

        findViewById(R.id.btn).setOnClickListener(this);
    }

    /**
     * 修改头像
     */
    private void changeImgae() {
        PermissionUtil.requestEach(this, new PermissionUtil.OnPermissionListener() {
            @Override
            public void onSucceed() {
                //授权成功后打开弹窗
                showDialog();
            }

            @Override
            public void onFailed(boolean showAgain) {

            }
        }, PermissionUtil.STORAGE, PermissionUtil.CAMERA);
    }

    /**
     * 显示选择相册和相机的弹窗
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请选择图片获取方式");
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CameraUtil.openAlbum(MainActivity.this, REQUEST_CODE_ALBUM);
            }
        });
        builder.setNeutralButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CameraUtil.openCamera(MainActivity.this, imageFile, REQUEST_CODE_CAMER);
            }
        });
        builder.create().show();
    }

    /**
     * 裁剪图片
     * @param data
     */
    private void startCrop(Uri data) {
        CropBean albumCropBean = new CropBean();
        albumCropBean.inputUri = data;
        albumCropBean.outputX = 300;
        albumCropBean.outputY = 300;
        albumCropBean.caculateAspect();
        albumCropBean.isReturnData = false;
        albumCropBean.isReturnData = true;
        //裁剪后输出的图片文件
        albumCropBean.outputUri = Uri.fromFile(imageFile);
        //跳转裁剪
        CameraUtil.openCrop(this, albumCropBean, REQUEST_CODE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_ALBUM://相册
                startCrop(data.getData());
                break;
            case REQUEST_CODE_CAMER://相机
                startCrop(Uri.fromFile(imageFile));
                break;
            case REQUEST_CODE_CROP://裁剪完成
                Bitmap cropBitmap = BitmapFactory.decodeFile(imageFile.getPath());
                ivHead.setImageBitmap(cropBitmap);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                changeImgae();
                break;
        }
    }
}
