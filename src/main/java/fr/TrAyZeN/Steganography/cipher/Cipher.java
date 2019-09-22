package fr.TrAyZeN.Steganography.cipher;

import java.util.ArrayList;

public class Cipher {
    public static String cipher(String message, String cipherMethod, ArrayList<String> parameters) {
        switch (cipherMethod) {
            case "caesar":
                return caesar(message, Integer.parseInt(parameters.get(0)));
            case "rot13":
                return rot13(message);
            default:
                System.out.println("Cipher failed : unknown cipher method");
                System.exit(1);
                break;
        }

        return "";
    }

    public static String caesar(String message, int n) {
        StringBuilder cipheredMessageBuilder = new StringBuilder();
        n %= 26;

        for (int i = 0; i < message.length(); i++) {
            if (isUpperCase(message.charAt(i))) {
                cipheredMessageBuilder.append((char) (Math.floorMod(message.charAt(i) - 'A' + n, 26) + 'A'));
            } else if (isLowerCase(message.charAt(i))) {
                cipheredMessageBuilder.append((char) (Math.floorMod(message.charAt(i) - 'a' + n, 26) + 'a'));
            } else {
                cipheredMessageBuilder.append(message.charAt(i));
            }
        }

        return cipheredMessageBuilder.toString();
    }

    private static boolean isUpperCase(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private static boolean isLowerCase(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static String rot13(String message) {
        return caesar(message, 13);
    }
}
