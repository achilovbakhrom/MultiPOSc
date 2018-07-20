package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.cameraview.CameraView;
import com.jim.multipos.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by developer on 14.09.2017.
 */

public class PhotoPickDialog extends Dialog {
    private View dialogView;
    private ImageView ivContentPhoto;
    private FrameLayout flCamera;
    private FrameLayout flCancel;
    private FrameLayout flGallery;
    private FrameLayout flRemove;
    private ImageView ivCamera;
    private ImageView ivGallery;
    private ImageView ivRemove;
    private RelativeLayout llCamera;
    private LinearLayout llGallery;
    private LinearLayout llRemove;
    private TextView tvCamera;
    private TextView tvGallery;
    private TextView tvRemove;
    private Context context;
    private OnButtonsClickListner onButtonsClickListner;
    private CameraView cameraView;

    public interface OnButtonsClickListner {
        public void onCameraShot(Uri uri);

        public void onGallery();

        public void onRemove();
    }

    public PhotoPickDialog(@NonNull Context context, OnButtonsClickListner onButtonsClickListner) {
        super(context);
        this.context = context;
        this.onButtonsClickListner = onButtonsClickListner;
        dialogView = getLayoutInflater().inflate(R.layout.photo_pick_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        ivContentPhoto = (ImageView) dialogView.findViewById(R.id.ivContentPhoto);
        flCamera = (FrameLayout) dialogView.findViewById(R.id.flCamera);
        flGallery = (FrameLayout) dialogView.findViewById(R.id.flGallery);
        flRemove = (FrameLayout) dialogView.findViewById(R.id.flRemove);
        flCancel = (FrameLayout) dialogView.findViewById(R.id.flCancel);
        ivCamera = (ImageView) dialogView.findViewById(R.id.ivCamera);
        ivGallery = (ImageView) dialogView.findViewById(R.id.ivGallery);
        ivRemove = (ImageView) dialogView.findViewById(R.id.ivRemove);
        llCamera = (RelativeLayout) dialogView.findViewById(R.id.llCamera);
        llGallery = (LinearLayout) dialogView.findViewById(R.id.llGallery);
        llRemove = (LinearLayout) dialogView.findViewById(R.id.llRemove);
        tvCamera = (TextView) dialogView.findViewById(R.id.tvCamera);
        tvGallery = (TextView) dialogView.findViewById(R.id.tvGallery);
        tvRemove = (TextView) dialogView.findViewById(R.id.tvRemove);
        cameraView = (CameraView) dialogView.findViewById(R.id.camera);
        cameraView.addCallback(mCallback);
        tvCamera.setText(R.string.camera);
        dialogView.findViewById(R.id.cancel_btn).setOnClickListener(view -> {
            dismiss();
        });
        flCancel.setVisibility(View.VISIBLE);

        flCancel.setOnClickListener(view -> {
            if (withContentOpened) {
                openExist = true;
                ivContentPhoto.setVisibility(View.VISIBLE);
                flRemove.setVisibility(View.VISIBLE);
                flCancel.setVisibility(View.GONE);
                flGallery.setVisibility(View.VISIBLE);
                cameraView.setVisibility(View.GONE);
            } else dismiss();

        });
        flCamera.setOnClickListener(view -> {
            if (openExist) {
                openExist = false;
                ivContentPhoto.setVisibility(View.GONE);
                flRemove.setVisibility(View.GONE);
                flGallery.setVisibility(View.GONE);
                flCancel.setVisibility(View.VISIBLE);
                cameraView.setVisibility(View.VISIBLE);
                tvCamera.setText(R.string.shot);
            } else {
                cameraView.takePicture();

            }
        });
        flGallery.setOnClickListener(view -> {
            onButtonsClickListner.onGallery();
            dismiss();
        });
        flRemove.setOnClickListener(view -> {
            onButtonsClickListner.onRemove();
            dismiss();
        });
    }

    @Override
    public void onDetachedFromWindow() {
        cameraView.stop();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        cameraView.start();
    }

    boolean openExist = false;

    public PhotoPickDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void showDialog(Uri uri) {
        openExist = true;
        ivContentPhoto.setVisibility(View.VISIBLE);
        flRemove.setVisibility(View.VISIBLE);
        cameraView.setVisibility(View.GONE);
        flCancel.setVisibility(View.GONE);
        withContentOpened = true;
        GlideApp.with(context).load(uri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).centerCrop().into(ivContentPhoto);
        getWindow().setLayout((int) convertDpToPixel(360), RelativeLayout.LayoutParams.WRAP_CONTENT);

        show();
    }

    public void showDialog() {
        openExist = false;
        ivContentPhoto.setVisibility(View.GONE);
        flRemove.setVisibility(View.GONE);
        cameraView.setVisibility(View.VISIBLE);
        withContentOpened = false;

        tvCamera.setText("Shot");
        getWindow().setLayout((int) convertDpToPixel(360), RelativeLayout.LayoutParams.WRAP_CONTENT);

        show();
    }

    boolean withContentOpened = false;

    protected PhotoPickDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d("cameratest", "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d("cameratest", "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d("cameratest", "onPictureTaken " + data.length);
            File file = new File(getAlbumStorageDir("multipos").getAbsolutePath());
            OutputStream os = null;
            if (!file.exists()) {
                file.mkdirs();
            }
            File file1 = new File(file.getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");

            Log.d("cameratest", file.getAbsolutePath());
            try {
                os = new FileOutputStream(file1.getAbsolutePath());
                os.write(data);
                os.close();
                onButtonsClickListner.onCameraShot(Uri.fromFile(file1));
                dismiss();

            } catch (IOException e) {
                Log.w("cameratest", "Cannot write to " + file, e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        // Ignore
                    }
                }
            }


        }

    };

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("cameratest", "Directory not created");
        }
        return file;
    }

}
