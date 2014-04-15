package com.e10dokup.shikajika;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

public class ImageAsyncRequest extends AsyncTask<Uri.Builder, Void, String> {
    private static final String TAG = Face.class.getSimpleName();

    private Context mContext;
    private Bitmap mImage;

    private int smile,doya,trouble;


    public ImageAsyncRequest(Context context, Bitmap image) {

        // 呼び出し元のアクティビティ
        mContext = context;

        mImage = image;
    }

    // このメソッドは必ずオーバーライドする必要があるよ
    // ここが非同期で処理される部分みたいたぶん。
    @Override
    protected String doInBackground(Uri.Builder... builder) {
        ImageRequest request = new ImageRequest(mContext);
        return request.postImage(mImage);


    }


    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        smile = 0;
        doya = 0;
        trouble = 0;

        try{
            parseXml(result);
        }catch(Exception e){
            Log.d(TAG,e.toString());

        }



    }

    public void parseXml(String result)
            throws IOException, XmlPullParserException
    {

        XmlPullParser parser = Xml.newPullParser();
        try {

            Log.d("aaaaaaaaaaaaaaa","ここ入った");
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "smilelevel".equals(parser.getName())) {
                    smile = Integer.parseInt(parser.nextText());
                    Log.d("aaaaaaaaaaaaaaa","ここ入った");
                }
                if (eventType == XmlPullParser.START_TAG && "doyalevel".equals(parser.getName())) {
                    doya = Integer.parseInt(parser.nextText());
                }
                if (eventType == XmlPullParser.START_TAG && "troublelevel".equals(parser.getName())) {
                    trouble = Integer.parseInt(parser.nextText());
                }
            }
        } catch (Exception e) {
            Log.v("ERROR", "on parseXML msg:" + e.getMessage());
        }

        Face face = new Face(smile,doya,trouble);
        face.setType();

        //face.sendMoveTrue();
    }
}