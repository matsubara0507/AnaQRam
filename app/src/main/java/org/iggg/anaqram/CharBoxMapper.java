package org.iggg.anaqram;

import android.graphics.Color;
import android.widget.Button;

import java.util.HashMap;

class CharBoxMapper {
    private Button clickedButton = null;

    private Button[] buttons;
    private HashMap<Button,CharBox> charBoxes;

    CharBoxMapper(Button[] bs, CharBox[] cbs) {
        buttons = bs;
        charBoxes = new HashMap<>(bs.length);
        for (int i = 0; i < bs.length; i++)
            charBoxes.put(bs[i], cbs[i]);
    }

    void swapCharBox(Button button) {
        if (clickedButton == null) {
            button.setTextColor(Color.RED);
            clickedButton = button;
        }
        else {
            CharBox temp = charBoxes.get(button);
            charBoxes.put(button, charBoxes.get(clickedButton));
            charBoxes.put(clickedButton, temp);
            clickedButton.setTextColor(Color.BLACK);
            updateChar();
            clickedButton = null;
        }
    }

    void updateChar() {
        for (Button button : buttons)
            button.setText(charBoxes.get(button).toString());
    }

    String getCurrentString() {
        String str = "";
        for (Button button : buttons)
            str += button.getText();
        return str;
    }
}
