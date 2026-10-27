import java.text.Normalizer;
import java.util.Locale;

public final class RomanianAlphabet {
    public static final String LETTERS = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";
    public static final char EXTRA_SYMBOL = '#';

    private RomanianAlphabet() { }

    public static String normalize(String input) {
        String value = Normalizer.normalize(input, Normalizer.Form.NFC)
                .toUpperCase(Locale.ROOT);
        return value.replace('Ş', 'Ș').replace('Ţ', 'Ț');
    }

    public static boolean isValid(String input, boolean allowExtraSymbol) {
        String value = normalize(input);
        for (int i = 0; i < value.length(); i++) {
            char letter = value.charAt(i);
            if (Character.isWhitespace(letter)) {
                continue;
            }
            if (LETTERS.indexOf(letter) < 0 && !(allowExtraSymbol && letter == EXTRA_SYMBOL)) {
                return false;
            }
        }
        return true;
    }

    public static String removeSpaces(String input) {
        return normalize(input).replaceAll("\\s+", "");
    }
}
