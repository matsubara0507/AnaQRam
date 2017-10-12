package org.iggg.anaqram


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

import java.io.IOException

internal class BarcodeDetectorManager(private val cameraView: SurfaceView, private val activity: Activity) {
    private val barcodeDetector: BarcodeDetector
    private val cameraSource: CameraSource

    init {
        barcodeDetector = BarcodeDetector.Builder(activity)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()

        cameraSource = CameraSource.Builder(activity, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build()

        val REQUEST_CODE = 1 // カメラの権限を得るときに利用する

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE)
                        return
                    }
                    cameraSource.start(cameraView.holder)
                } catch (ie: IOException) {
                    Log.e("CAMERA SOURCE", ie.message)
                } catch (e: Exception) {
                    // ???
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
    }

    fun setProcessor(processor: Detector.Processor<Barcode>) {
        barcodeDetector.setProcessor(processor)
    }

    fun release() {
        cameraSource.release()
        barcodeDetector.release()
    }

}
