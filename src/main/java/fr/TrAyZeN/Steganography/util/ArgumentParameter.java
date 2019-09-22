package fr.TrAyZeN.Steganography.util;

import java.util.ArrayList;

public class ArgumentParameter {
    public String value;
    public ArrayList<String> options;

    public ArgumentParameter(String value, ArrayList<String> options) {
        this.value = value;
        this.options = options;
    }

    public ArgumentParameter(String value) {
        this(value, new ArrayList<>());
    }

    public ArgumentParameter() {
        this(new String(), new ArrayList<>());
    }
}
