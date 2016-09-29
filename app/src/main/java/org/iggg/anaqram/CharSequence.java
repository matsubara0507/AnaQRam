package org.iggg.anaqram;

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
    private CharBox[] values;

    CharSequence(String str) {
        values = new CharBox[str.length()];
        int i = 0;
        for (char c: str.toCharArray())
            values[i++] = new CharBox(c);
    }

    int length() {
        return values.length;
    }

    String at(int index) {
        return values[index].toString();
    }

    void setFlag(int index) {
        values[index].setFlag();
    }

    CharBox[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        String output = "";
        for (CharBox value: values)
            output += value;
        return output;
    }
}
