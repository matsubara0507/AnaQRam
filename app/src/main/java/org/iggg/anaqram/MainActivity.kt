package org.iggg.anaqram

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import android.view.Gravity
import android.view.SurfaceView
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode

class MainActivity : AppCompatActivity() {

    private var barcodeInfo: TextView? = null
    private var charBoxMapper: CharBoxMapper? = null
    private var gameManager: GameManager? = null
    private var barcodeDetectorManager: BarcodeDetectorManager? = null

    private var startButton: Button? = null
    private var timerText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        startButton = findViewById(R.id.start_button) as Button
        timerText = findViewById(R.id.timer) as TextView

        startButton!!.setOnClickListener {
            if (gameManager!!.isRunning) {
                gameManager!!.reset()
                charBoxMapper!!.shuffle()
                charBoxMapper!!.updateChar()
                barcodeInfo!!.text = getString(R.string.please_start)
                startButton!!.text = getString(R.string.start)
            } else {
                gameManager!!.start()
                barcodeInfo!!.text = getString(R.string.please_scan)
                startButton!!.text = getString(R.string.reset)
            }
        }

        updateSetting()

        /* 中央部のカメラを描画 */
        val cameraView = findViewById(R.id.camera_view) as SurfaceView
        barcodeDetectorManager = BarcodeDetectorManager(cameraView, this)

        barcodeInfo = findViewById(R.id.code_info) as TextView

        barcodeDetectorManager!!.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcode = detections.detectedItems
                if (barcode.size() != 0 && gameManager!!.isRunning) {
                    barcodeInfo!!.post {
                        val qrText = barcode.valueAt(0).displayValue
                        barcodeInfo!!.text = gameManager!!.displayChar(qrText)
                        charBoxMapper!!.updateChar()

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
        if (prefs.getString("answer", "") != gameManager!!.answer) {
            gameManager!!.reset()
            updateSetting()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeDetectorManager!!.release()
    }

    internal fun updateSetting() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        gameManager = GameManager(prefs.getString("answer", ""), timerText!!)

        /* 上部のボタン文字列を描画 */
        val buttonArea = findViewById(R.id.buttonArea) as LinearLayout

        val listener = View.OnClickListener { v ->
            charBoxMapper!!.swapCharBox(v as Button)
            val msg = gameManager!!.accept(charBoxMapper!!.currentString)
            if (msg != null)
                toastMake(msg, 0, 0)
        }

        charBoxMapper = CharBoxMapper(this, gameManager!!.getCharBoxes(), listener)
        charBoxMapper!!.shuffle()
        charBoxMapper!!.updateChar()

        buttonArea.removeAllViews()
        for (button in charBoxMapper!!.buttons)
            buttonArea.addView(button)
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