package fr.TrAyZeN.Steganography;

import fr.TrAyZeN.Steganography.cipher.Cipher;
import fr.TrAyZeN.Steganography.cipher.Decipher;

public class Steganography {

    public static void main(String[] args) {
        Arguments arguments = new Arguments(args);

        if (arguments.contains("help")) {
            help(arguments);
        } else if (arguments.valueOf("command").equals("encode")) {
            encode(arguments);
        } else if (arguments.valueOf("command").equals("decode")) {
            decode(arguments);
        }
    }

    private static void help(Arguments arguments) {
        if (arguments.valueOf("help").equals("cipher")) {
            printCipherHelp();
        } else {
            printHelp();
        }
    }

    private static void encode(Arguments arguments) {
        Image image = new Image(arguments.valueOf("input"));

        String message = arguments.valueOf("message");
        if (arguments.contains("cipher")) {
            message = Cipher.cipher(message, arguments.valueOf("cipher"), arguments.optionsOf("cipher"));
        }
        System.out.println(message);
        image.encode(message, Short.parseShort(arguments.valueOf("nb")));

        image.save(arguments.valueOf("output"));
    }

    private static void decode(Arguments arguments) {
        Image image = new Image(arguments.valueOf("input"));

        String message = image.decode(Short.parseShort(arguments.valueOf("nb")));
        if (arguments.contains("cipher")) {
            message = Decipher.decipher(message, arguments.valueOf("cipher"), arguments.optionsOf("cipher"));
        }

        System.out.println("message " + message);
    }

    private static void printHelp() {
        System.out.println("Usage: steg [command] [arguments]\n"
                         + "\n"
                         + "Commands:\n"
                         + "encode                             Hide a given message in a given image.\n"
                         + "decode                             Get the hidden message in a given image.\n"
                         + "\n"
                         + "Encoding mandatory arguments:\n"
                         + "-m, --message <message>            The message to hide in the image.\n"
                         + "-i, --input <path>                 The path of the input image.\n"
                         + "-o, --output <path>                The path of the resulting image.\n"
                         + "Encoding optional arguments:\n"
                         + "-c, --cipher <method> <parameters> Cipher to apply to the message. Use 'steg -h cipher' to get more information on available cipher.\n"
                         + "\n"
                         + "Decoding mandatory arguments:\n"
                         + "-i, --input <path>                 The path of the input image.\n"
                         + "Encoding optional arguments:\n"
                         + "-c, --cipher <method> <parameters> Decipher the message. Use 'steg -h cipher' to get more information on available cipher.\n"
                         + "\n"
                         + "Other options:\n"
                         + "-h, --help                         Show this help message.");
    }

    private static void printCipherHelp() {
        System.out.println("Cipher argument usage: -c <method> <parameters>"
                         + "Cipher methods:"
                         + "caesar <shift>      with shift an integer"
                         + "rot13");
    }

}
