package lab1;

public abstract class CaesarCipher {

    private String process(String text){
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null.");
        }
        return text.replaceAll("\\s+", "");
    }

    public final String encrypt(String message, int key){
        validateKey(key);
        return encryptInternal(process(message), key);
    }

    public final String decrypt(String message, int key){
        validateKey(key);
        return decryptInternal(process(message), key);
    }

    private void validateKey(int key) {
        if (key < 1 || key > 30) {
            throw new IllegalArgumentException("Key must be an integer between 1 and 30.");
        }
    }

    protected abstract String encryptInternal(String message, int key);
    protected abstract String decryptInternal(String message, int key);


}
