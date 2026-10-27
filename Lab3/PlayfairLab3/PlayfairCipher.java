public class PlayfairCipher {
    private final PlayfairMatrix matrix;

    public PlayfairCipher(String key) {
        matrix = new PlayfairMatrix(key);
    }

    public void printMatrix() {
        matrix.print();
    }

    public String encrypt(String text) {
        return transform(prepareText(text), true);
    }

    public String decrypt(String text) {
        String cleaned = RomanianAlphabet.removeSpaces(text);
        if (cleaned.length() % 2 != 0) {
            throw new IllegalArgumentException("Ciphertext must contain an even number of characters.");
        }
        return transform(cleaned, false);
    }

    public String prepareText(String text) {
        String cleaned = RomanianAlphabet.removeSpaces(text);
        StringBuilder prepared = new StringBuilder();
        int index = 0;

        while (index < cleaned.length()) {
            char first = cleaned.charAt(index++);
            char second;

            if (index == cleaned.length()) {
                second = fillerFor(first);
            } else if (first == cleaned.charAt(index)) {
                second = fillerFor(first);
            } else {
                second = cleaned.charAt(index++);
            }
            prepared.append(first).append(second);
        }
        return prepared.toString();
    }

    private char fillerFor(char letter) {
        return letter == 'X' ? 'Q' : 'X';
    }

    private String transform(String text, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        int shift = encrypt ? 1 : -1;

        for (int i = 0; i < text.length(); i += 2) {
            int[] first = matrix.find(text.charAt(i));
            int[] second = matrix.find(text.charAt(i + 1));

            if (first[0] == second[0]) {
                result.append(matrix.at(first[0], first[1] + shift));
                result.append(matrix.at(second[0], second[1] + shift));
            } else if (first[1] == second[1]) {
                result.append(matrix.at(first[0] + shift, first[1]));
                result.append(matrix.at(second[0] + shift, second[1]));
            } else {
                result.append(matrix.at(first[0], second[1]));
                result.append(matrix.at(second[0], first[1]));
            }
        }
        return result.toString();
    }
}
