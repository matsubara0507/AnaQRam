package org.iggg.anaqram;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

class BarcodeDetectorManager {
    private Activity activity;
    private SurfaceView cameraView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    BarcodeDetectorManager(SurfaceView cv, Activity callingActivity) {
        cameraView = cv;
        activity = callingActivity;
        barcodeDetector =
                new BarcodeDetector.Builder(activity)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(activity, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        final int REQUEST_CODE = 1; // カメラの権限を得るときに利用する

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{
                                Manifest.permission.CAMERA
                        }, REQUEST_CODE);
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                } catch (Exception e) {
                    // ???
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    void setProcessor(Detector.Processor<Barcode> processor) {
        barcodeDetector.setProcessor(processor);
    }

    void release() {
        cameraSource.release();
        barcodeDetector.release();
    }

}
