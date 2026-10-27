public class PlayfairMatrix {
    private static final int ROWS = 4;
    private static final int COLS = 8;
    private final char[][] letters = new char[ROWS][COLS];

    public PlayfairMatrix(String key) {
        String allCharacters = RomanianAlphabet.removeSpaces(key)
                + RomanianAlphabet.LETTERS + RomanianAlphabet.EXTRA_SYMBOL;
        StringBuilder unique = new StringBuilder();

        for (int i = 0; i < allCharacters.length(); i++) {
            char letter = allCharacters.charAt(i);
            if (unique.indexOf(String.valueOf(letter)) == -1) {
                unique.append(letter);
            }
        }

        for (int i = 0; i < ROWS * COLS; i++) {
            letters[i / COLS][i % COLS] = unique.charAt(i);
        }
    }

    public int[] find(char letter) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (letters[row][col] == letter) {
                    return new int[]{row, col};
                }
            }
        }
        throw new IllegalArgumentException("Character not found in matrix: " + letter);
    }

    public char at(int row, int col) {
        return letters[Math.floorMod(row, ROWS)][Math.floorMod(col, COLS)];
    }

    public void print() {
        System.out.println("\nPlayfair matrix (4 x 8):");
        System.out.println("+-------------------------------+");
        for (char[] row : letters) {
            System.out.print("| ");
            for (char letter : row) {
                System.out.print(letter + "   ");
            }
            System.out.println("|");
        }
        System.out.println("+-------------------------------+");
    }
}
