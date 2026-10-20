package lab1;

public class RomanianCaesarCipherWithPermutation {
    private final RomanianCaesarCipher cipher = new RomanianCaesarCipher();

    public String buildPermutedAlphabet(String keyword) {
        return RomanianAlphabet.buildPermuted(keyword);
    }

    public String encrypt(String message, int key, String keyword) {
        return transform(message, key, keyword);
    }

    public String decrypt(String message, int key, String keyword) {
        return transform(message, -key, keyword);
    }

    private String transform(String message, int key, String keyword) {
        return cipher.transform(message, key, buildPermutedAlphabet(keyword));
    }
}
