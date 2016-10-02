package org.iggg.anaqram;

import android.support.annotation.Nullable;

class GameManager {
    private String answer;
    private boolean clear = false;
    private CharBox[] charBoxes;

    GameManager(String ans) {
        answer = ans;
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
            return "くりあ～～ ヽ(^◇^*)/";
        }
        return null;
    }
}
