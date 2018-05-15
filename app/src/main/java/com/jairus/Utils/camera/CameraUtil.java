/*
 * File:  CameraUtil.java
 * Copyright (c) $2018 3N CTO Co.,Ltd. All rights reserved
 * Date:  18-5-14 下午6:04
 * Version:  V1.0
 *
 */

package com.jairus.Utils.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.jairus.AppApplication;
import com.jairus.BuildConfig;

import java.io.File;
import java.util.List;

/**
 * 相机相册工具类
 * 作者： JairusTse
 * 日期： 18/5/14 18:03
 */
public class CameraUtil {

    /**
     * 启动相机
     *
     * @param activity
     * @param file 拍照后保存的相片文件
     * @param requestCode 请求码
     */
    public static void openCamera(Activity activity, File file, int requestCode) {
        if (!isHaveCame(MediaStore.ACTION_IMAGE_CAPTURE)) {
            Toast.makeText(activity, "该手机没有安装相机", Toast.LENGTH_SHORT).show();
            return;
        }
        //指定相机意图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //如果TargetSdkVersion >= 24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri photoUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + "" +
                    ".fileprovider", file);
            //申请权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //设置相片保存的地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        } else {
            //设置相片保存的地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动相册
     *
     * @param activity
     * @param requestCode 请求码
     */
    public static void openAlbum(Activity activity, int requestCode) {
        //指定相册意图
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 设置文件类型
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 启动裁剪
     *
     * @param activity
     * @param cropBean
     * @param requestCode
     */
    public static void openCrop(Activity activity, CropBean cropBean, int requestCode) {
        if (cropBean == null) {
            Toast.makeText(activity, "cropBean不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cropBean.inputUri == null) {
            Toast.makeText(activity, "裁剪图片inputUri不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        //配置一系列裁剪参数
        intent.putExtra("outputX", cropBean.outputX);
        intent.putExtra("outputY", cropBean.outputY);
        intent.putExtra("scale", cropBean.scale);
        intent.putExtra("aspectX", cropBean.aspectX);
        intent.putExtra("aspectY", cropBean.aspectY);
        intent.putExtra("outputFormat", cropBean.outputFormat);
        intent.putExtra("return-data", cropBean.isReturnData);

        //如果TargetSdkVersion >= 24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //申请权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //将数据Uri转化成FileProvider的Uri
            File dataFile = new File(UriUtil.getPath(cropBean.inputUri));
            cropBean.inputUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID +
                    ".fileprovider", dataFile);
        }
        //设置要裁剪的图片Uri
        intent.setDataAndType(cropBean.inputUri, "image/*");
        // 如果不需要返回Btimap，则必须指定图片保存的Uri
        if (!cropBean.isReturnData) {
            if (cropBean.outputUri == null) {
                Toast.makeText(activity, "请指定保存裁剪图片地址：outputUri", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropBean.outputUri);

        activity.startActivityForResult(intent, requestCode);

    }

    /**
     * 判断意图是否存在
     */
    public static boolean isHaveCame(String intentName) {
        PackageManager packageManager = AppApplication.mContext.getPackageManager();
        Intent intent = new Intent(intentName);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager
                .MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
