package lab1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RomanianCaesarCipher cipher = new RomanianCaesarCipher();
        RomanianCaesarCipherWithPermutation twoKeyCipher = new RomanianCaesarCipherWithPermutation();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String operation;
                while (true) {
                    System.out.print("Operation (e/d = normal, e2/d2 = two-key, x = exit): ");
                    operation = scanner.nextLine().trim().toLowerCase();
                    if (operation.equals("x")) {
                        System.out.println("Goodbye.");
                        return;
                    }
                    if (operation.equals("e") || operation.equals("d")
                            || operation.equals("e2") || operation.equals("d2")) break;
                    System.out.println("Invalid operation. Choose e, d, e2, d2, or x to exit.");
                }

                String keyword = null;
                if (operation.equals("e2") || operation.equals("d2")) {
                    while (true) {
                        System.out.print("Key 2 keyword (at least 7 Romanian letters): ");
                        keyword = scanner.nextLine();
                        try {
                            System.out.println("Permuted alphabet: "
                                    + twoKeyCipher.buildPermutedAlphabet(keyword));
                            break;
                        } catch (IllegalArgumentException exception) {
                            System.out.println(exception.getMessage());
                        }
                    }
                }

                int key;
                while (true) {
                    System.out.print("Key (1-30): ");
                    try {
                        key = Integer.parseInt(scanner.nextLine().trim());
                        if (key < 1 || key > 30) throw new NumberFormatException();
                        break;
                    } catch (NumberFormatException exception) {
                        System.out.println("Invalid key. Enter an integer between 1 and 30.");
                    }
                }

                while (true) {
                    System.out.print("Message: ");
                    String message = scanner.nextLine();
                    try {
                        String result;
                        if (operation.equals("e")) result = cipher.encrypt(message, key);
                        else if (operation.equals("d")) result = cipher.decrypt(message, key);
                        else if (operation.equals("e2")) result = twoKeyCipher.encrypt(message, key, keyword);
                        else result = twoKeyCipher.decrypt(message, key, keyword);
                        System.out.println("Result: " + result);
                        break;
                    } catch (IllegalArgumentException exception) {
                        System.out.println(exception.getMessage());
                    }
                }
            }
        }

    }
}
