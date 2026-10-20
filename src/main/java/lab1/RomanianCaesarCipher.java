package lab1;

public class RomanianCaesarCipher {
    public String encrypt(String message, int key) {
        RomanianAlphabet.validateKey(key);
        return transform(message, key, RomanianAlphabet.VALUE);
    }

    public String decrypt(String message, int key) {
        RomanianAlphabet.validateKey(key);
        return transform(message, -key, RomanianAlphabet.VALUE);
    }

    String transform(String message, int key, String alphabet) {
        String text = RomanianAlphabet.removeSpaces(message);
        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char original = text.charAt(i);
            char letter = RomanianAlphabet.normalize(original);
            int index = alphabet.indexOf(letter);
            RomanianAlphabet.validateLetter(letter, original);
            result.append(alphabet.charAt(
                    Math.floorMod(index + key, alphabet.length())));
        }
        return result.toString();
    }
}
