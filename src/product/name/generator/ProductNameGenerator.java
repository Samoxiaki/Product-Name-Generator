/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.name.generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 *
 * @author Samoxiaki - Samoxiaki@yahoo.com
 */
public class ProductNameGenerator {

    private final static char[] numbers = "0123456789".toCharArray();
    private final static char[] upperCase = "QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
    private final static char[] lowerCase = "qwertyuiopasdfghjklzxcvbnm".toCharArray();

    private final static int defSamples = 25;
    private final static String defFormat = "NNcxxxxn";
    private final static String defMask = "aa-aaaaa";

    private final static Random RNG = new Random();

    private static int samples;
    private static String format;
    private static String mask;

    public static void main(String[] args) {
        if (parseArgs(args)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < samples; i++) {
                String generated = genName(format, mask);
                sb.append(generated);
                sb.append("\n");
                System.out.println(generated);
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("generated.txt")));
                bos.write(sb.toString().getBytes());
                bos.close();
                System.out.println("[+] Saved as generated.txt");
            }
            catch (Exception e) {
                System.out.println("[!] Unable to save as generated.txt");
            }

        }
    }

    public static boolean parseArgs(String[] args) { // false if critical error, true if avoidable
        if (args.length > 0) {
            if (args[0].equals("--help") || args[0].equals("-h")) { // prints help and exits. 
                printUsage();
                return false;
            }

            if (args.length > 3) {  // Too many arguments
                System.out.println("[!] Too many arguments.");
                return false;
            }

            try { // Assign samples
                samples = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException NFE) {
                System.out.println("[SAMPLES] Unknown value: " + args[0]);
                return false;
            }

            if (args.length > 1) { // Format
                String arg = args[1];
                for (int i = 0; i < arg.length(); i++) {
                    if (arg.charAt(i) != 'N' && arg.charAt(i) != 'n' && arg.charAt(i) != 'x' && arg.charAt(i) != 'c') {
                        System.out.println("[FORMAT] Invalid character: " + arg.charAt(i));
                        printUsage();
                        return false;
                    }
                }
                format = arg;
            }
            else {
                format = defFormat;
            }

            if (args.length > 2) { // Mask
                if (args[2].length() != format.length()) {
                    System.out.println("[MASK] FORMAT and MASK differ in length");
                    printUsage();
                    return false;
                }
                mask = args[2];
            }
            else {
                mask = defMask;
            }

            return true;

        }
        else {
            samples = defSamples;
            format = defFormat;
            mask = defMask;
            return true;
        }

    }

    public static void printUsage() {
        System.out.println("Usage:\n");
        System.out.println("java -jar Product-Name-Generator.jar [SAMPLES] [FORMAT] [MASK]\n");
        System.out.println("    SAMPLES:    Number of samples to generate.");
        System.out.println("                Default [" + defSamples + "]");
        System.out.println("    FORMAT:     N: uppercase, n: lowercase, x: number, c: custom");
        System.out.println("                Default [" + defFormat + "]");
        System.out.println("    MASK:       Set custom chars where is a 'c' placed in FORMAT");
        System.out.println("                Default [" + defMask + "]");
    }

    public static String genName(String format, String mask) {
        int len = format.length();
        if (mask.length() != len) {
            return null;
        }

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            if (format.charAt(i) == 'N') {
                sb.append(upperCase[RNG.nextInt(upperCase.length)]);
            }
            if (format.charAt(i) == 'n') {
                sb.append(lowerCase[RNG.nextInt(lowerCase.length)]);
            }
            if (format.charAt(i) == 'x') {
                sb.append(numbers[RNG.nextInt(numbers.length)]);
            }
            if (format.charAt(i) == 'c') {
                sb.append(mask.charAt(i));
            }
        }

        return sb.toString();
    }
}
