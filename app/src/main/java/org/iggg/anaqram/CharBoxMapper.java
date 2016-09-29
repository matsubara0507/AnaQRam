package org.iggg.anaqram;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class CharBoxMapper {
    private Button clickedButton = null;

    private Button[] buttons;
    private HashMap<Button,CharBox> charBoxes;

    CharBoxMapper(Context context, CharBox[] cbs, View.OnClickListener listener) {
        buttons = new Button[cbs.length];
        for (int i = 0; i < cbs.length; i++) {
            Button button = new Button(context);
            buttons[i] = button;
        }

        charBoxes = new HashMap<>(buttons.length);
        for (int i = 0; i < buttons.length; i++)
            charBoxes.put(buttons[i], cbs[i]);

        for (Button button : buttons) {
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(150,150);
            button.setLayoutParams(buttonLayoutParams);
            button.setTextSize(30);
            button.setOnClickListener(listener);
        }
    }

    Button[] getButtons() {
        return buttons;
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

    private CharBox[] getCurrentCharBoxes() {
        CharBox[] current = new CharBox[buttons.length];
        for (int i = 0; i < buttons.length; i++)
            current[i] = charBoxes.get(buttons[i]);
        return current;
    }

    void shuffle() {
        List<Integer> indexes = new ArrayList<>(buttons.length);
        for (int i = 0; i < buttons.length; i++)
            indexes.add(i);
        Collections.shuffle(indexes);

        CharBox[] current = getCurrentCharBoxes();
        for(int i = 0; i < buttons.length; i++)
            charBoxes.put(buttons[i], current[indexes.get(i)]);
    }

    String getCurrentString() {
        String str = "";
        for (Button button : buttons)
            str += button.getText();
        return str;
    }
}
