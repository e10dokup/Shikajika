package com.e10dokup.shikajika;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;

public class TakePictureTask extends AbstractPeriodTask {
    private static final String TAG = TakePictureTask.class.getSimpleName();
    private final TakePictureTask self = this;

    Camera mCamera;
    Context mContext;

    public TakePictureTask(long period, boolean isDaemon, Camera camera, Context context) {
        super(period, isDaemon);
        mCamera = camera;
        mContext = context;
    }



    @Override
    protected void invokersMethod() {
        mCamera.takePicture(mShutterListener, null, mPictureCallback);

    }

    private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback() {
        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };


    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bmp;
            Bitmap bitmap = null;


            if (data != null) {
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                int width = bmp.getWidth ();
                int height = bmp.getHeight ();
                Matrix matrix = new Matrix();
                matrix.postRotate (270);

                bitmap = Bitmap.createBitmap (bmp, 0, 0, width, height, matrix, true);
            }


            Uri.Builder builder = new Uri.Builder();
            ImageAsyncRequest image = new ImageAsyncRequest(mContext, bitmap);
            image.execute(builder);

            mCamera.startPreview();



        }
    };

}