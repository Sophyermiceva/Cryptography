package md.utm.des;

public final class DesFinalPermutation {
    // Standard DES inverse initial permutation (IP^-1).
    public static final int[] IP_INVERSE = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private DesFinalPermutation() {
    }

    public static String permute(String bits) {
        if (bits == null || !bits.matches("[01]{64}")) {
            throw new IllegalArgumentException("Input must contain exactly 64 binary digits.");
        }
        StringBuilder result = new StringBuilder(64);
        for (int position : IP_INVERSE) {
            result.append(bits.charAt(position - 1));
        }
        return result.toString();
    }

    public static String toHex(String bits) {
        StringBuilder hex = new StringBuilder(bits.length() / 4);
        for (int i = 0; i < bits.length(); i += 4) {
            int value = Integer.parseInt(bits.substring(i, i + 4), 2);
            hex.append(Character.toUpperCase(Character.forDigit(value, 16)));
        }
        return hex.toString();
    }
}
