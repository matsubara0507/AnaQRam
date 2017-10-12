package org.iggg.anaqram

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.SurfaceView
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode

class MainActivity : AppCompatActivity() {
    data internal class Model(val gameManager: GameManager, val charBoxMapper: CharBoxMapper)

    private var model: Model? = null
    private var barcodeDetectorManager: BarcodeDetectorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)

        val startButton = findViewById(R.id.start_button) as Button
        val barcodeInfo = findViewById(R.id.code_info) as TextView

        startButton.setOnClickListener {
            model!!.let {
                if (it.gameManager.isRunning) {
                    it.gameManager.reset()
                    it.charBoxMapper.shuffle()
                    it.charBoxMapper.updateChar()
                    barcodeInfo.text = getString(R.string.please_start)
                    startButton.text = getString(R.string.start)
                } else {
                    it.gameManager.start()
                    barcodeInfo.text = getString(R.string.please_scan)
                    startButton.text = getString(R.string.reset)
                }
            }
        }

        updateSetting()

        /* 中央部のカメラを描画 */
        val cameraView = findViewById(R.id.camera_view) as SurfaceView
        barcodeDetectorManager = BarcodeDetectorManager(cameraView, this)
        barcodeDetectorManager!!.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcode = detections.detectedItems
                model!!.let {
                    if (barcode.size() != 0 && it.gameManager.isRunning) {
                        barcodeInfo.post {
                            val qrText = barcode.valueAt(0).displayValue
                            barcodeInfo.text = it.gameManager.displayChar(qrText)
                            it.charBoxMapper.updateChar()
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        model!!.let {
            if (prefs.getString("answer", "") != it.gameManager.answer) {
                it.gameManager.reset()
                updateSetting()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeDetectorManager?.release()
    }

    private fun updateSetting() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val timerText = findViewById(R.id.timer) as TextView

        val gameManager = GameManager(prefs.getString("answer", ""), timerText)

        /* 上部のボタン文字列を描画 */
        val buttonArea = findViewById(R.id.buttonArea) as LinearLayout

        val charBoxMapper = CharBoxMapper(this, gameManager.charBoxes, { str ->
            gameManager.accept(str)?.let { toastMake(it, 0, 0) }
        })
        charBoxMapper.shuffle()
        charBoxMapper.updateChar()

        buttonArea.removeAllViews()
        charBoxMapper.buttons.forEach { button -> buttonArea.addView(button) }

        model = Model(gameManager, charBoxMapper)
    }

    private fun toastMake(message: String?, x: Int, y: Int) {
        val text = TextView(this)
        text.text = message
        text.textSize = 50f
        text.setTextColor(Color.WHITE)
        text.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        val toast = Toast(this)
        toast.setGravity(Gravity.CENTER, x, y)
        toast.duration = Toast.LENGTH_LONG
        toast.view = text

        toast.show()
    }
}