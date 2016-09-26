package org.iggg.anaqram;

import java.util.ArrayList;
import java.util.List;

class CharBox {
    private char value;
    private boolean flag;
    private final static char defaultValue = '？';

    CharBox(char c) {
        value = c;
        flag = false;
    }

    void setFlag() {
        flag = true;
    }

    @Override
    public String toString() {
        return String.valueOf(flag ? value : defaultValue);
    }
}

class CharSequence {
    private List<CharBox> values;

    CharSequence(String str) {
        values = new ArrayList<>(str.length());
        for (char c: str.toCharArray())
            values.add(new CharBox(c));
    }

    int length() {
        return values.size();
    }

    String at(int index) {
        return values.get(index).toString();
    }

    void setFlag(int index) {
        values.get(index).setFlag();
    }

    @Override
    public String toString() {
        String output = "";
        for (CharBox value: values)
            output += value;
        return output;
    }
}
