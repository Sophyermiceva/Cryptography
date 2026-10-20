package lab1;

final class RomanianAlphabet {
    static final String VALUE = "A\u0102\u00C2BCDEFGHI\u00CEJKLMNOPQRS\u0218T\u021AUVWXYZ";

    private RomanianAlphabet() {
    }

    static String removeSpaces(String text) {
        if (text == null) throw new IllegalArgumentException("Text cannot be null.");
        return text.replace(" ", "");
    }

    static char normalize(char character) {
        char upper = Character.toUpperCase(character);
        return switch (upper) {
            //Romanian letters
            case '\u015E' -> '\u0218';
            case '\u0162' -> '\u021A';
            default -> upper;
        };
    }

    static void validateKey(int key) {
        if (key < 1 || key > VALUE.length() - 1)
            throw new IllegalArgumentException("Key must be an integer between 1 and 30.");
    }

    static void validateLetter(char letter, char original) {
        if (VALUE.indexOf(letter) < 0)
            throw new IllegalArgumentException("Invalid character: '" + original
                    + "'. Only Romanian alphabet letters and spaces are allowed.");
    }

    static String buildPermuted(String keyword) {
        if (keyword == null) throw new IllegalArgumentException("Keyword cannot be null.");
        if (keyword.length() < 7)
            throw new IllegalArgumentException("Key 2 must contain at least 7 Romanian alphabet letters.");
        StringBuilder result = new StringBuilder(VALUE.length());
        for (int i = 0; i < keyword.length(); i++) {
            char original = keyword.charAt(i);
            char letter = normalize(original);
            if (VALUE.indexOf(letter) < 0)
                throw new IllegalArgumentException("Invalid keyword character: '" + original + "'. Key 2 may contain only Romanian alphabet letters.");
            if (result.indexOf(String.valueOf(letter)) < 0) result.append(letter);
        }
        for (int i = 0; i < VALUE.length(); i++) {
            char letter = VALUE.charAt(i);
            if (result.indexOf(String.valueOf(letter)) < 0) result.append(letter);
        }
        return result.toString();
    }
}
