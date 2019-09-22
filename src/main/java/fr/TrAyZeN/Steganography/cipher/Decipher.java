package fr.TrAyZeN.Steganography.cipher;

import java.util.ArrayList;

public class Decipher {
    public static String decipher(String message, String cipherMethod, ArrayList<String> parameters) {
        switch (cipherMethod) {
            case "caesar":
                return caesar(message, Integer.parseInt(parameters.get(0)));
            case "rot13":
                return rot13(message);
            default:
                System.out.println("Decipher failed : unknown cipher method");
                System.exit(1);
                break;
        }

        return "";
    }

    public static String caesar(String cipheredMessage, int n) {
        StringBuilder messageBuilder = new StringBuilder();
        n %= 26;

        for (int i = 0; i < cipheredMessage.length(); i++) {
            if (isUpperCase(cipheredMessage.charAt(i))) {
                messageBuilder.append((char) (Math.floorMod(cipheredMessage.charAt(i) - 'A' - n, 26) + 'A'));
            } else if (isLowerCase(cipheredMessage.charAt(i))) {
                messageBuilder.append((char) (Math.floorMod(cipheredMessage.charAt(i) - 'a' - n, 26) + 'a'));
            } else {
                messageBuilder.append(cipheredMessage.charAt(i));
            }
        }

        return messageBuilder.toString();
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
