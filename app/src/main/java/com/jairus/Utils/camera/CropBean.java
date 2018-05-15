/*
 * File:  CropBean.java
 * Copyright (c) $2018 3N CTO Co.,Ltd. All rights reserved
 * Date:  18-5-14 下午6:05
 * Version:  V1.0
 *
 */

package com.jairus.Utils.camera;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;

/**
 * 裁剪图片对象
 * 作者： JairusTse
 * 日期： 18/5/15 17:41
 */
public class CropBean {

    //要裁剪的图片Uri
    public Uri inputUri;

    //发送裁剪信号
    public String crop;
    //X方向上的比例
    public int aspectX;
    //Y方向上的比例
    public int aspectY;
    //裁剪区的宽
    public int outputX;
    //裁剪区的高
    public int outputY;
    //是否保留比例
    public boolean scale;
    //是否将数据保存在Bitmap中返回
    public boolean isReturnData;
    //相应的Bitmap数据
    public Parcelable returnData;
    //圆形裁剪区域
    public String circleCrop;
    //输出图片的uri，如果不以Bitmap形式返回，需要返回这个uri
    public Uri outputUri;
    //图片输出格式，默认JPEG
    public String outputFormat = Bitmap.CompressFormat.JPEG.toString();
    //是否取消人脸识别
    public boolean noFaceDetection;

    /**
     * 根据宽高计算裁剪比例
     */
    public void caculateAspect() {

        scale = true;

        if (outputX == outputY) {
            aspectX = 1;
            aspectY = 1;
            return;
        }
        float proportion = (float) outputX / (float) outputY;

        aspectX = (int) (proportion * 100);
        aspectY = 100;
    }
}
