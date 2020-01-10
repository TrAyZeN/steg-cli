package fr.TrAyZeN.Steganography;

import fr.TrAyZeN.Steganography.util.ArgumentParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Arguments {
    private HashMap<String, ArgumentParameter> arguments;

    public Arguments(String[] args) {
        this.setDefaultValues();
        this.parse(args);
        this.validateArguments();
    }

    private void setDefaultValues() {
        this.arguments = new HashMap<>();
        this.arguments.put("nb", new ArgumentParameter("1"));
    }

    // big ugly function with code duplication
    private void parse(String[] args) {
        List<String> argsList = Arrays.asList(args);

        for (int i = 0; i < argsList.size(); i++) {
            switch (argsList.get(i)) {
                case "-h": case "--help":
                    if (++i < argsList.size()) {
                        arguments.put("help", new ArgumentParameter(argsList.get(i)));
                    } else {
                        arguments.put("help", new ArgumentParameter("help"));
                    }
                    break;
                case "encode": case "decode":
                    arguments.put("command", new ArgumentParameter(argsList.get(i)));
                    break;
                case "-m": case "--message":
                    if (++i < argsList.size()) {
                        arguments.put("message", new ArgumentParameter(argsList.get(i)));
                    } else {
                        System.out.println("Missing argument for '-m'/'--message'");
                        System.exit(1);
                    }
                    break;
                case "-i": case "--input":
                    if (++i < argsList.size()) {
                        arguments.put("input", new ArgumentParameter(argsList.get(i)));
                    } else {
                        System.out.println("Missing argument for '-i'/'--input'");
                        System.exit(1);
                    }
                    break;
                case "-o": case "--output":
                    if (++i < argsList.size()) {
                        arguments.put("output", new ArgumentParameter(argsList.get(i)));
                    } else {
                        System.out.println("Missing argument for '-o'/'--output'");
                        System.exit(1);
                    }
                    break;
                case "-c": case "--cipher":
                    if (++i < argsList.size()) {
                        String cipherMethod = argsList.get(i);
                        ArrayList<String> parameters = new ArrayList<>();

                        switch (cipherMethod) {
                            case "caesar":
                                if (++i < argsList.size()) {
                                    String shift = argsList.get(i);
                                    try {
                                        Integer.parseInt(shift);
                                    } catch (NumberFormatException e) {
                                        System.out.println("Cipher parameter error : '" + shift + "' is not an integer. Use 'steg-cli -h cipher' for more information.");
                                        System.exit(1);
                                    } finally {
                                        parameters.add(shift);
                                        arguments.put("cipher", new ArgumentParameter(cipherMethod, parameters));
                                    }
                                } else {
                                    System.out.println("Missing parameter for caesar cipher. Use 'steg-cli -h cipher' for more information.");
                                    System.exit(1);
                                }
                                break;
                            case "rot13":
                                arguments.put("cipher", new ArgumentParameter(cipherMethod));
                                break;
                            default:
                                System.out.println("Unknown cipher method '" + cipherMethod + "' use 'steg-cli -h cipher' for more information.");
                                System.exit(1);
                                break;
                        }
                    } else {
                        System.out.println("Missing argument for '-c'/'--cipher'");
                        System.exit(1);
                    }
                    break;
                case "--force":
                    arguments.put("force", new ArgumentParameter("true"));
                    break;
                default:
                    if (argsList.get(i).startsWith("-")) {
                        System.out.println("Unknown argument '" + argsList.get(i) + "' use 'steg-cli -h' for more information.");
                        System.exit(1);
                    }
                    break;
            }
        }
    }

    private void validateArguments() {
        if (this.contains("command")) {
            if (!this.contains("input")) {
                System.out.println("Missing input argument.");
                System.exit(1);
            } else {
                this.validateInputFile();
            }

            if (this.valueOf("command").equals("encode")) {
                if (!this.contains("message")) {
                    System.out.println("Missing message argument.");
                    System.exit(1);
                }

                if (!this.contains("output")) {
                    System.out.println("Missing output argument.");
                    System.exit(1);
                } else {
                    this.validateOutputFolder();
                }
            }
        } else if (!this.contains("help")) {
            System.out.println("Unknown command provided, use 'steg-cli -h' for more information on how to use this program.");
            System.exit(1);
        }
    }

    private void validateInputFile() {
        File tempFile = new File(this.valueOf("input"));

        if (!tempFile.exists()) {
            System.out.println("Input file '" + this.valueOf("input") + "' does not exists.");
            System.exit(1);
        }
    }

    private void validateOutputFolder() {
        String parts[] = this.valueOf("output").replace("\\", "/").split("/");
        StringBuilder folderBuilder = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            folderBuilder.append(parts[i] + "/");
        }

        File tempFolder = new File(folderBuilder.toString());
        if (!tempFolder.exists()) {
            System.out.println("Output folder '" + folderBuilder.toString() + "' does not exists.");
            System.exit(1);
        }
    }

    public String valueOf(String key) {
        return this.arguments.get(key).value;
    }

    public ArrayList<String> optionsOf(String key) {
        return this.arguments.get(key).options;
    }

    public boolean contains(String key) {
        return this.arguments.containsKey(key);
    }
}
