package md.utm.des;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        System.out.println("=== Laboratory Work 4: DES, Task 2.11 ===");
        System.out.println("Determine the encrypted block from L16 and R16.\n");

        while (true) {
            System.out.println("1. Enter L16 and R16 manually");
            System.out.println("2. Generate random L16 and R16");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = SCANNER.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Goodbye!");
                return;
            }
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Invalid option.\n");
                continue;
            }

            String left = choice.equals("1") ? readBits("L16") : randomBits(32);
            String right = choice.equals("1") ? readBits("R16") : randomBits(32);
            showCalculation(left, right);
        }
    }

    private static String readBits(String name) {
        while (true) {
            System.out.print("Enter " + name + " (exactly 32 bits): ");
            String input = SCANNER.nextLine().trim();
            if (input.matches("[01]{32}")) {
                return input;
            }
            System.out.println("Invalid input: enter exactly 32 characters containing only 0 and 1.");
        }
    }

    private static String randomBits(int count) {
        StringBuilder bits = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            bits.append(RANDOM.nextBoolean() ? '1' : '0');
        }
        return bits.toString();
    }

    private static void showCalculation(String left, String right) {
        System.out.println("\n--- Input (32 bits each) ---");
        System.out.println("L16 = " + left);
        System.out.println("R16 = " + right);

        // DES swaps the halves before applying the final permutation.
        String combined = right + left;
        System.out.println("\n--- Step 1: Swap and concatenate ---");
        System.out.println("R16 || L16 = " + combined);
        System.out.println("Length: " + combined.length() + " bits");

        System.out.println("\n--- Step 2: Standard IP^-1 table ---");
        for (int i = 0; i < DesFinalPermutation.IP_INVERSE.length; i++) {
            System.out.printf("%3d", DesFinalPermutation.IP_INVERSE[i]);
            if ((i + 1) % 8 == 0) System.out.println();
        }

        System.out.println("\n--- Step 3: Bit-by-bit permutation ---");
        String encryptedBits = DesFinalPermutation.permute(combined);
        for (int i = 0; i < DesFinalPermutation.IP_INVERSE.length; i++) {
            int inputPosition = DesFinalPermutation.IP_INVERSE[i];
            System.out.printf("Output bit %2d <- Input bit %2d = %c%n",
                    i + 1, inputPosition, combined.charAt(inputPosition - 1));
        }

        System.out.println("\n--- Step 4: Encrypted block ---");
        System.out.println("Binary (64 bits): " + encryptedBits);
        System.out.println("Binary grouped:   " + group(encryptedBits, 8));
        System.out.println("HEX (16 digits):  " + DesFinalPermutation.toHex(encryptedBits));
        System.out.println("\n==============================================\n");
    }

    private static String group(String bits, int groupSize) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bits.length(); i += groupSize) {
            if (i > 0) result.append(' ');
            result.append(bits, i, i + groupSize);
        }
        return result.toString();
    }
}
