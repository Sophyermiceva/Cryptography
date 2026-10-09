package lab1;

public class RomanianCaesarCipherWithPermutation {
    private static final String ALPHABET = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";

    public String buildPermutedAlphabet(String keyword) {
        String normalized = normalizeKeyword(keyword);
        StringBuilder result = new StringBuilder(ALPHABET.length());
        for (int i = 0; i < normalized.length(); i++) {
            char letter = normalized.charAt(i);
            if (result.indexOf(String.valueOf(letter)) < 0) result.append(letter);
        }
        for (int i = 0; i < ALPHABET.length(); i++) {
            char letter = ALPHABET.charAt(i);
            if (result.indexOf(String.valueOf(letter)) < 0) result.append(letter);
        }
        return result.toString();
    }

    public String encrypt(String message, int key, String keyword) {
        return transform(message, key, keyword);
    }

    public String decrypt(String message, int key, String keyword) {
        return transform(message, -key, keyword);
    }

    private String transform(String message, int shift, String keyword) {
        validateKey(keyFromShift(shift));
        String alphabet = buildPermutedAlphabet(keyword);
        if (message == null) throw new IllegalArgumentException("Text cannot be null.");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char original = message.charAt(i);
            if (Character.isWhitespace(original)) continue;
            char letter = normalize(original);
            int index = alphabet.indexOf(letter);
            if (index < 0) {
                throw new IllegalArgumentException("Invalid character: '" + original
                        + "'. Only Romanian alphabet letters and spaces are allowed.");
            }
            result.append(alphabet.charAt(Math.floorMod(index + shift, alphabet.length())));
        }
        return result.toString();
    }

    private int keyFromShift(int shift) {
        int key = Math.abs(shift);
        return key == 0 ? 31 : key;
    }

    private void validateKey(int key) {
        if (key < 1 || key > 30) {
            throw new IllegalArgumentException("Key 1 must be an integer between 1 and 30.");
        }
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) throw new IllegalArgumentException("Keyword cannot be null.");
        String normalized = keyword.toUpperCase();
        if (normalized.length() < 7) {
            throw new IllegalArgumentException("Key 2 must contain at least 7 Romanian alphabet letters.");
        }
        for (int i = 0; i < normalized.length(); i++) {
            char original = normalized.charAt(i);
            char letter = normalize(original);
            if (ALPHABET.indexOf(letter) < 0) {
                throw new IllegalArgumentException("Invalid keyword character: '" + original
                        + "'. Key 2 may contain only Romanian alphabet letters and must have at least 7 characters.");
            }
            normalized = normalized.substring(0, i) + letter + normalized.substring(i + 1);
        }
        return normalized;
    }

    private char normalize(char character) {
        char upper = Character.toUpperCase(character);
        return switch (upper) {
            case 'Ş' -> 'Ș';
            case 'Ţ' -> 'Ț';
            default -> upper;
        };
    }
}
