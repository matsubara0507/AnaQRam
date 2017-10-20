package org.iggg.anaqram

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.SparseArray

import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

class PhotoActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val myQRCode = BitmapFactory.decodeStream(
                    assets.open("myqrcode.jpg")
            )

            val barcodeDetector = BarcodeDetector.Builder(this)
                    .setBarcodeFormats(Barcode.QR_CODE)
                    .build()

            val myFrame = Frame.Builder()
                    .setBitmap(myQRCode)
                    .build()

            val barcodes = barcodeDetector.detect(myFrame)

            // Check if at least one barcode was detected
            if (barcodes.size() != 0) {

                // Print the QR code's message
                Log.d("My QR Code's Data",
                        barcodes.valueAt(0).displayValue
                )
            }
        } catch (e: Exception) {
            Log.e("ERROR", e.message)
        }

    }
}