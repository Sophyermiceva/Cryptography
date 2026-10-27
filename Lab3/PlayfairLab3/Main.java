import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println("=== PLAYFAIR CIPHER - ROMANIAN ALPHABET ===");

        while (true) {
            System.out.println("\n1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Goodbye!");
                break;
            }
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Invalid option. Please enter 0, 1 or 2.");
                continue;
            }

            String key = readKey(scanner);
            PlayfairCipher cipher = new PlayfairCipher(key);
            cipher.printMatrix();

            boolean encrypt = choice.equals("1");
            String message = readMessage(scanner, encrypt);

            if (encrypt) {
                System.out.println("\nPrepared pairs: " + groupPairs(cipher.prepareText(message)));
                System.out.println("Encrypted text: " + cipher.encrypt(message));
            } else {
                System.out.println("\nDecrypted text: " + cipher.decrypt(message));
                System.out.println("Note: Remove inserted filler letters and restore spaces manually.");
            }
        }
    }

    private static String readKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter key (at least 7 Romanian letters): ");
            String key = scanner.nextLine();
            String cleaned = RomanianAlphabet.removeSpaces(key);

            if (!RomanianAlphabet.isValid(key, false)) {
                System.out.println("Invalid key. Use only Romanian letters: " + RomanianAlphabet.LETTERS);
            } else if (cleaned.length() < 7) {
                System.out.println("Key must contain at least 7 letters.");
            } else {
                return key;
            }
        }
    }

    private static String readMessage(Scanner scanner, boolean encrypt) {
        while (true) {
            System.out.print(encrypt ? "Enter plaintext: " : "Enter ciphertext: ");
            String message = scanner.nextLine();
            String cleaned = RomanianAlphabet.removeSpaces(message);

            if (cleaned.isEmpty()) {
                System.out.println("Message cannot be empty.");
            } else if (!RomanianAlphabet.isValid(message, !encrypt)) {
                System.out.println("Invalid characters. Use Romanian letters"
                        + (encrypt ? "." : " or #.")
                        + " Allowed letters: " + RomanianAlphabet.LETTERS);
            } else if (!encrypt && cleaned.length() % 2 != 0) {
                System.out.println("Ciphertext must contain an even number of characters.");
            } else {
                return message;
            }
        }
    }

    private static String groupPairs(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(text, i, i + 2);
        }
        return result.toString();
    }
}
