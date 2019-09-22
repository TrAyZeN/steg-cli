package fr.TrAyZeN.Steganography.util;

import java.util.concurrent.LinkedBlockingDeque;

public class BitQueue extends LinkedBlockingDeque<Boolean> {
    private short SIZEOF_CHAR_IN_BITS = 4 * 8;

    public void addString(String s) {
        for (char c : s.toCharArray()) {
            this.addCharacter(c);
        }

        this.addNullTerminator();
    }

    public void addCharacter(char c) {
        for (int i = this.SIZEOF_CHAR_IN_BITS - 1; i >= 0; i--) {
            this.addIntAsBool((c >> i) & 1);
        }
    }

    public void addIntAsBool(int n) {
        this.add(n != 0);
    }

    private void addNullTerminator() {
        this.addCharacter('\0');
    }

    public int removeFirstAsInt() {
        return this.removeFirst() ? 1 : 0;
    }

    public String asString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.size() / this.SIZEOF_CHAR_IN_BITS; i++) {
            char c = 0;
            for (int b = 0; b < this.SIZEOF_CHAR_IN_BITS; b++) {
                c <<= 1;
                c |= this.removeFirstAsInt();
            }

            if (c == '\0') {
                break;
            }

            sb.append(c);
        }

        return sb.toString();
    }

}
