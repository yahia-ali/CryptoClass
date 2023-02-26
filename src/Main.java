package encryptdecrypt;

import java.io.*;
import java.util.Scanner;

abstract class BasicCrypto {
    protected String originalText;
    protected String clearText;
    protected String cipherText;
    protected String key;

    public BasicCrypto(String text, String key) {
        this.originalText = text;
        this.key = key;
    }

    public abstract String encrypt();
    public abstract String decrypt();

    public String getCipherText() {
        return cipherText;
    }

    public String getClearText() {
        return clearText;
    }
}

class CaesarUnicode extends BasicCrypto {
     public CaesarUnicode(String text, String key) {
         super(text, key);
     }
    @Override
    public String encrypt() {
        clearText = originalText;
        char[] clearTextArray = clearText.toCharArray();
        int keyAsNumber = Integer.parseInt(key);
        for(int i = 0; i < clearTextArray.length; i++) {
            clearTextArray[i] += keyAsNumber;
        }
        cipherText = new String(clearTextArray);
        return cipherText;
    }

    @Override
    public String decrypt() {
        cipherText = originalText;
        char[] cipherTextArray = cipherText.toCharArray();
        int keyAsNumber = Integer.parseInt(key);
        for(int i = 0; i < cipherTextArray.length; i++) {
            cipherTextArray[i] -= keyAsNumber;
        }
        clearText = new String(cipherTextArray);
        return clearText;
    }
}

class CaesarShift extends BasicCrypto {
    public CaesarShift(String text, String key) {
        super(text, key);
    }

    @Override
    public String encrypt() {
        clearText = originalText;
        char[] clearTextArray = clearText.toCharArray();
        int keyAsNumber = Integer.parseInt(key);
        int encryptedChar;
        for(int i = 0; i < clearTextArray.length; i++) {
            if (!Character.isUpperCase(clearTextArray[i]) && !Character.isLowerCase(clearTextArray[i])) {
                continue;
            }
            encryptedChar = clearTextArray[i] + keyAsNumber;
            if (Character.isUpperCase(clearTextArray[i])) {
                clearTextArray[i] = encryptedChar > 'Z' ?
                        (char) (encryptedChar - 'Z' - 1 + 'A') : (char) encryptedChar;
            } else {
                clearTextArray[i] = encryptedChar > 'z' ?
                        (char) (encryptedChar - 'z' - 1 + 'a') : (char) encryptedChar;
            }
        }
        cipherText = new String(clearTextArray);
        return cipherText;
    }

    @Override
    public String decrypt() {
        clearText = originalText;
        char[] cipherTextArray = clearText.toCharArray();
        int keyAsNumber = Integer.parseInt(key);
        int decryptedChar;
        for(int i = 0; i < cipherTextArray.length; i++) {
            if (!Character.isUpperCase(cipherTextArray[i]) && !Character.isLowerCase(cipherTextArray[i])) {
                continue;
            }
            decryptedChar = cipherTextArray[i] - keyAsNumber;
            if (Character.isUpperCase(cipherTextArray[i])) {
                cipherTextArray[i] = decryptedChar < 'A' ?
                        (char) (decryptedChar - 'A' + 1 + 'Z') : (char) decryptedChar;
            } else {
                cipherTextArray[i] = decryptedChar < 'a' ?
                        (char) (decryptedChar - 'a' + 1 + 'z') : (char) decryptedChar;
            }
        }
        clearText = new String(cipherTextArray);
        return clearText;
    }

}

public class Main {
    public static void main(String[] args) {
        /*
        String clearText = "we found a treasure!";
        BasicCrypto encryptor = new BasicCrypto(clearText);
        encryptor.encrypt("reverseAlphabet", "anyKey");
        */
        String operation = "enc";
        String text = "";
        String userKey = "0";
        String inputFileName = "";
        String outputFileName = "";
        String algorithm = "shift";
        boolean dataSwitch = false;
        boolean inSwitch = false;
        boolean outSwitch = false;
        boolean algorithmSwitch = false;

        /*
        Scanner readInput = new Scanner(System.in);
        operation = readInput.nextLine();
        text = readInput.nextLine();
        userKey = readInput.nextLine();
        */
        int parameterIndex = 0;
        while (parameterIndex < args.length) {
            switch (args[parameterIndex++]) {
                case "-mode" :
                    operation = args[parameterIndex++];
                    break;
                case "-key" :
                    userKey = args[parameterIndex++];
                    break;
                case "-data" :
                    text = args[parameterIndex++];
                    dataSwitch = true;
                    break;
                case "-in" :
                    inputFileName = args[parameterIndex++];
                    inSwitch = true;
                    break;
                case "-out" :
                    outputFileName = args[parameterIndex++];
                    outSwitch = true;
                    break;
                case "-alg" :
                    algorithm = args[parameterIndex++];
                    algorithmSwitch = true;
                    break;

                default :
                    System.out.println("Bad switch!");
                    break;
            }
        }
        if (!dataSwitch && inSwitch) {
            File inputFile = new File(inputFileName);
            try (Scanner readInputFile = new Scanner(inputFile)) {
                text = readInputFile.nextLine();
            } catch (IOException e) {
                System.out.println("Error! Read Error!");
                return;
            }
        }

        BasicCrypto cryptObject = null;

        switch (algorithm) {
            case "shift" :
                cryptObject = new CaesarShift(text, userKey);
                break;
            case "unicode" :
                cryptObject = new CaesarUnicode(text, userKey);
                break;
            default :
                System.out.println("Unknown algorithm!");
                break;
        }


        if (operation.equals("enc")) {
            cryptObject.encrypt();
            if (!outSwitch) {
                System.out.println(cryptObject.getCipherText());
            } else {
                File outFile = new File(outputFileName);
                try (FileWriter writeOutputFile = new FileWriter(outFile)) {
                    writeOutputFile.write(cryptObject.getCipherText());
                } catch (IOException e) {
                    System.out.println("Error! Write Error!");
                }
            }
        } else if (operation.equals("dec")) {
            cryptObject.decrypt();
            if (!outSwitch) {
                System.out.println(cryptObject.getClearText());
            } else {
                File outFile = new File(outputFileName);
                try (FileWriter writeOutputFile = new FileWriter(outFile)) {
                    writeOutputFile.write(cryptObject.getClearText());
                } catch (IOException e) {
                    System.out.println("Error! Write Error!");
                }
            }
        } else {
            System.out.println("Error! Bad operation from input!");
        }
    }
}