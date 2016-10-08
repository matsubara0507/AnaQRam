package org.iggg.anaqram;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

class GameManager {
    private String answer;
    private boolean clear = false;
    private CharBox[] charBoxes;

    private boolean running = false;
    private Timer timer;
    private Handler handler = new Handler();
    private long count = 0;
    private TextView timerText;

    private final String initTime = "00:00";

    GameManager(String ans, TextView tt) {
        answer = ans;
        timerText = tt;
        timerText.setText(initTime);
        charBoxes = new CharBox[ans.length()];
        int i = 0;
        for (char c: ans.toCharArray())
            charBoxes[i++] = new CharBox(c);
    }

    String getAnswer(){
        return answer;
    }

    // コッチの charBoxes の中身を書き換えられたくないので
    // コピーしたものを渡している
    CharBox[] getCharBoxes() {
        CharBox[] temp = new CharBox[charBoxes.length];
        for(int i = 0; i < charBoxes.length; i++)
            temp[i] = charBoxes[i];
        return temp;
    }

    String displayChar(String qrText) {
        try {
            // 剰余を取って文字数未満の数字が出ても大丈夫にしている
            int index = Integer.valueOf(qrText) % charBoxes.length;
            charBoxes[index].setFlag();
            return "「" + charBoxes[index] + "」をみつけた！v(≧∇≦)v";
        } catch (NumberFormatException e) {
            // qrText が数字以外の場合
            return "ちがうQRコードだよ！(*￣∀￣)\"b\" ﾁｯﾁｯﾁｯ";
        }
    }

    @Nullable
    String accept(String solution) {
        if (solution.equals(answer) && !clear) {
            clear = true;
            if (timer != null)
                timer.cancel();
            return "くりあ～～ ヽ(^◇^*)/";
        }
        return null;
    }

    boolean isRunning() {
        return running;
    }

    void start() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        count = 0;
        timerText.setText(initTime);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        count++;
                        long mm = count * 100 / 1000 / 60;
                        long ss = count * 100 / 1000 % 60;
                        timerText.setText(String.format(Locale.US, "%1$02d:%2$02d", mm, ss));
                    }
                });
            }
        }, 0, 100);
        running = true;
    }

    void reset() {
        if (timer != null)
            timer.cancel();
        timer = null;
        timerText.setText(initTime);

        for (CharBox cb : charBoxes)
            cb.resetFlag();

        running = false;
    }
}
