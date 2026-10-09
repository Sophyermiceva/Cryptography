package lab1;

public class RomanianCaesarCipher extends CaesarCipher{
    private static final String ALPHABET = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";

    @Override
    protected String encryptInternal(String message, int key) {
        return transform(message, key);
    }

    @Override
    protected String decryptInternal(String message, int key) {
        return transform(message, -key);
    }

    private String transform(String message, int shift) {
        StringBuilder result = new StringBuilder(message.length());
        for (int i = 0; i < message.length(); i++) {
            char character = normalize(message.charAt(i));
            int index = ALPHABET.indexOf(character);
            if (index < 0) {
                throw new IllegalArgumentException(
                        "Invalid character: '" + message.charAt(i)
                                + "'. Only Romanian alphabet letters and spaces are allowed.");
            }
            int shiftedIndex = Math.floorMod(index + shift, ALPHABET.length());
            result.append(ALPHABET.charAt(shiftedIndex));
        }
        return result.toString();
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
