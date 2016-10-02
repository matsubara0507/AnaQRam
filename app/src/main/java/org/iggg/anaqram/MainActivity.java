package org.iggg.anaqram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {

    private TextView barcodeInfo;
    private CharBoxMapper charBoxMapper;
    private GameManager gameManager;
    private BarcodeDetectorManager barcodeDetectorManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateSetting();

        /* 下部のメニューボタンを描画 */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /* 中央部のカメラを描画 */
        SurfaceView cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeDetectorManager = new BarcodeDetectorManager(cameraView, this);

        barcodeInfo = (TextView) findViewById(R.id.code_info);

        barcodeDetectorManager.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() { }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if (barcode.size() != 0) {
                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            String qrText = barcode.valueAt(0).displayValue;
                            barcodeInfo.setText(gameManager.displayChar(qrText));
                            charBoxMapper.updateChar();
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getString("answer","").equals(gameManager.getAnswer()))
            updateSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barcodeDetectorManager.release();
    }

    void updateSetting() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gameManager = new GameManager(prefs.getString("answer",""));

        /* 上部のボタン文字列を描画 */
        LinearLayout buttonArea = (LinearLayout) findViewById(R.id.buttonArea);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            charBoxMapper.swapCharBox((Button) v);
            String msg = gameManager.accept(charBoxMapper.getCurrentString());
            if (msg != null)
                toastMake(msg, 0, 0);

            }
        };

        charBoxMapper = new CharBoxMapper(this, gameManager.getCharBoxes(), listener);
        charBoxMapper.shuffle();
        charBoxMapper.updateChar();

        buttonArea.removeAllViews();
        for (Button button : charBoxMapper.getButtons())
            buttonArea.addView(button);
    }

    private void toastMake(String message, int x, int y) {
        TextView text = new TextView(this);
        text.setText(message);
        text.setTextSize(50);
        text.setTextColor(Color.WHITE);
        text.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, x, y);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(text);

        toast.show();
    }
}