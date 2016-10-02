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
