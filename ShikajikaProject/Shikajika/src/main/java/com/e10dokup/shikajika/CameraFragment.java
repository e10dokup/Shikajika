package com.e10dokup.shikajika;

import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

public class CameraFragment extends Fragment implements TextureView.SurfaceTextureListener {
    private static final String TAG = CameraFragment.class.getSimpleName();
    private final CameraFragment self = this;

    private Camera mCamera;
    private TextureView mTextureView;

    private TakePictureTask mTakePictureTask;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTextureView = (TextureView)view.findViewById(R.id.camera_preview);
        mTextureView.setSurfaceTextureListener(this);



        return view;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height){
        mCamera = Camera.open(1);

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(previewSize.width, previewSize.height, Gravity.CENTER));


        try{
            mCamera.setPreviewTexture(surface);
        }catch(IOException t){

        }

        mCamera.startPreview();

        mTakePictureTask = new TakePictureTask(12000,true,mCamera,getActivity());

        if(mTakePictureTask!=null){
            mTakePictureTask.execute();
        }

        mTextureView.setRotation(90.0f);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height){

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface){
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onPause() {
        super.onPause();
        mTakePictureTask.cancel();
    }
}