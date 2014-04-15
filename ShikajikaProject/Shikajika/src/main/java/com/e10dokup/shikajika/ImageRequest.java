package com.e10dokup.shikajika;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageRequest {
    private static final String TAG = ImageRequest.class.getSimpleName();
    private final ImageRequest self = this;

    Context mContext;


    public ImageRequest(Context context){
        mContext = context;
    }

    public String postImage(Bitmap image){
        HttpURLConnection http = null;  // HTTP通信
        OutputStream out = null;   // HTTPリクエスト送信用ストリーム
        InputStream in = null;    // HTTPレスポンス取得用ストリーム
        BufferedReader reader = null;  // レスポンスデータ出力用バッファ
        String result = null;

        try{

            // URL指定
            URL url = new URL(mContext.getString(R.string.api_url));

            // HttpURLConnectionインスタンス作成
            http = (HttpURLConnection)url.openConnection();

            // POST設定
            http.setRequestMethod("POST");

            // HTTPヘッダの「Content-Type」を「application/octet-stream」に設定
            http.setRequestProperty("Content-Type","application/octet-stream");

            // URL 接続を使用して入出力を行う
            http.setDoInput(true);
            http.setDoOutput(true);

            // キャッシュは使用しない
            http.setUseCaches(false);

            // 接続
            http.connect();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

            //Bodyに入力データを入れる
            byte[] readBuf = new byte[1024];
            out = new BufferedOutputStream(http.getOutputStream());
            in = new BufferedInputStream(bs);
            while ((in.read(readBuf)) >= 0) {
                out.write(readBuf);
            }
            out.flush();

            // レスポンスを取得
            Log.d(TAG, Integer.toString(http.getResponseCode()));
            in = new BufferedInputStream(http.getInputStream());
            reader = new BufferedReader(new InputStreamReader(in));
            result = reader.readLine();



        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
                if(http != null) {
                    http.disconnect();
                }
            } catch(Exception e) {
            }
        }
        return result;
    }
}