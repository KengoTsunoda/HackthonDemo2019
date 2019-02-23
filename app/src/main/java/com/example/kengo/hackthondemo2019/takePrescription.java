package com.example.kengo.hackthondemo2019;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class takePrescription extends AppCompatActivity {

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_prescription);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // カメラアプリとの連携からの戻りでかつ撮影成功の場合
        if (requestCode == 200 && resultCode == RESULT_OK){
            // 撮影された画像のビットマップデータを取得
            //Bitmap bitmap = data.getParcelableExtra("data");
            // 画像を表示するImageViewを取得
            ImageView ivCamera = findViewById(R.id.ivCamera);
            // 撮影された画像をImageViewに設定
            //ivCamera.setImageBitmap(bitmap);
            // フィールドの画像URIをImageViewに設定
            ivCamera.setImageURI(imageUri);
        }
    }
    /**
     * 画像部分がタップされた時の処理メソッド
     */
    public void onCameraImageClick(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // WRITE_EXTERNAL_STORAGEの許可を求めるダイアログを表示
            // その際、リクエストコードに2000を設定
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, 2000);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date(System.currentTimeMillis());
        String nowStr = dateFormat.format(now);
        // ストレージに格納する画像のファイル名を生成。ファイル名の一位を確保するためにタイムスタンプの値を利用。
        String fileName = "UseCameraActivityPicture_" + nowStr + ".jpg";

        ContentValues contentValues = new ContentValues();
        // 画像のファイル名を設定
        contentValues.put(MediaStore.Images.Media.TITLE, fileName);
        // 画像ファイルの種類を設定
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // ContentResolverを使ってURIオブジェクトを生成
        ContentResolver contentResolver = getContentResolver();
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        // カメラのIntentオブジェクトを生成
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Extra情報としてimageUriを設定
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // アクティビティを起動
        startActivityForResult(intent, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requsetCode, String[] permissions, int[] grantResults){
        // WRITE_EXTERNAL_STORAGEに対するパーミッションダイアログかつ許可を選択したなら…
        if (requsetCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // もう一度カメラアプリを起動
            ImageView ivCamera = findViewById(R.id.ivCamera);
            onCameraImageClick(ivCamera);
        }
    }
}
