# CameraUtil
Android 打开相机、相册和裁剪图片。
解决了Android 7.0 或以上 `android.os.FileUriExposedException` 的异常。

## 使用
#### 1、复制 res/xml 目录下的 file_path.xml 文件到自己的项目中
#### 2、在 Manifest.xml 文件中添加
	
```
<!--权限-->
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!--直接复制就可以-->
<application
	...
	<provider
	    android:name="android.support.v4.content.FileProvider"
	    android:authorities="${applicationId}.fileprovider"
	    android:exported="false"
	    android:grantUriPermissions="true">
	
	    <meta-data
	        android:name="android.support.FILE_PROVIDER_PATHS"
	        android:resource="@xml/file_paths" />
	</provider>
</application>

```

#### 3、权限控制（这里用的是RxPermissions，可以换成自己的）

```
compile 'com.tbruyelle.rxpermissions:rxpermissions:0.9.4@aar'
compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
compile 'io.reactivex.rxjava2:rxjava:2.1.5'
compile 'com.jakewharton.rxbinding2:rxbinding:2.0.0'
compile 'com.hwangjr.rxbus:rxbus:1.0.6'

```

#### 4、代码调用
- 请求授权

```
PermissionUtil.requestEach(this, new PermissionUtil.OnPermissionListener() {
    @Override
    public void onSucceed() {
        //授权成功
    }
	
    @Override
    public void onFailed(boolean showAgain) {
	
    }
}, PermissionUtil.STORAGE, PermissionUtil.CAMERA);
	
```
	
- 打开相机

```
CameraUtil.openCamera(Activity activity, File file, int requestCode)
	
```
	
- 打开相册

```
CameraUtil.openAlbum(Activity activity, int requestCode)
	
```
	
- 裁剪图片

```
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
	
```
