import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Laboratory 2: interactive frequency analysis of English monoalphabetic ciphers. */
public class FrequencyAnalysis {
    private static final String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final double[] ENGLISH = {
        8.17, 1.49, 2.78, 4.25, 12.70, 2.23, 2.01, 6.09, 6.97,
        0.15, 0.77, 4.03, 2.41, 6.75, 7.51, 1.93, 0.09, 5.99,
        6.33, 9.06, 2.76, 0.98, 2.36, 0.15, 1.97, 0.07
    };
    private final String ciphertext;
    private final char[] key = new char[26]; // ciphertext letter -> plaintext letter
    private final List<String> history = new ArrayList<>();
    private final List<char[]> undo = new ArrayList<>();

    private FrequencyAnalysis(String ciphertext) {
        this.ciphertext = ciphertext.toUpperCase(Locale.ROOT);
        Arrays.fill(key, '\0');
    }

    private static boolean letter(char c) { return c >= 'A' && c <= 'Z'; }

    private String preview() {
        StringBuilder sb = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            if (letter(c) && key[c - 'A'] != '\0') sb.append(Character.toLowerCase(key[c - 'A']));
            else sb.append(c);
        }
        return sb.toString();
    }

    private void frequencies() {
        int[] count = new int[26];
        for (char c : ciphertext.toCharArray()) if (letter(c)) count[c - 'A']++;
        int total = Arrays.stream(count).sum();
        System.out.println("\nLETTER FREQUENCIES (" + total + " encrypted letters)");
        System.out.printf("%-8s %9s %10s %12s%n", "Cipher", "Count", "Cipher %", "English %*");
        Integer[] indices = new Integer[26];
        for (int i = 0; i < 26; i++) indices[i] = i;
        Arrays.sort(indices, Comparator.comparingInt((Integer i) -> count[i]).reversed());
        Integer[] englishIndices = indices.clone();
        Arrays.sort(englishIndices, Comparator.comparingDouble((Integer i) -> ENGLISH[i]).reversed());
        for (int rank = 0; rank < 26; rank++) {
            int i = indices[rank];
            System.out.printf(Locale.US, "%-8c %9d %9.2f%% %9c %5.2f%%%n",
                ABC.charAt(i), count[i], total == 0 ? 0 : 100.0 * count[i] / total,
                ABC.charAt(englishIndices[rank]), ENGLISH[englishIndices[rank]]);
        }
        System.out.println("*English column is a same-rank GUESS, not an established substitution.");
    }

    private void commonWords(int length, int limit) {
        Map<String, Integer> counts = new HashMap<>();
        Matcher matcher = Pattern.compile("[A-Z]+").matcher(ciphertext);
        while (matcher.find()) {
            String word = matcher.group();
            if (word.length() == length) counts.merge(word, 1, Integer::sum);
        }
        System.out.println("\nMost common " + length + "-letter words:");
        counts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()))
            .limit(limit)
            .forEach(e -> System.out.printf("%-12s %d%n", e.getKey(), e.getValue()));
    }

    private void showKey() {
        StringBuilder encrypted = new StringBuilder();
        StringBuilder decrypted = new StringBuilder();
        int found = 0;
        for (int i = 0; i < 26; i++) {
            encrypted.append(ABC.charAt(i)).append(' ');
            decrypted.append(key[i] == '\0' ? '?' : key[i]).append(' ');
            if (key[i] != '\0') found++;
        }
        System.out.println("Cipher: " + encrypted);
        System.out.println("Plain:  " + decrypted);
        System.out.println("Recovered: " + found + "/26");
        StringBuilder encryption = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            char p = ABC.charAt(i);
            char c = '?';
            for (int j = 0; j < 26; j++) if (key[j] == p) c = ABC.charAt(j);
            encryption.append(c).append(' ');
        }
        System.out.println("Encryption key (plain A..Z -> cipher):");
        System.out.println("Plain:  A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");
        System.out.println("Cipher: " + encryption);
    }

    private void set(char cipher, char plain) {
        if (!letter(cipher) || !letter(plain)) {
            System.out.println("Use English letters A-Z only."); return;
        }
        int idx = cipher - 'A';
        for (int i = 0; i < 26; i++) {
            if (i != idx && key[i] == plain) {
                System.out.printf("Conflict: %c is already mapped to %c. Remove that mapping first.%n",
                    ABC.charAt(i), plain);
                return;
            }
        }
        if (key[idx] == plain) { System.out.println("Already mapped."); return; }
        undo.add(key.clone());
        key[idx] = plain;
        history.add("SET " + cipher + " -> " + plain);
        System.out.printf("Mapped %c -> %c%n", cipher, Character.toLowerCase(plain));
        System.out.println(preview());
    }

    private void remove(char cipher) {
        if (!letter(cipher) || key[cipher - 'A'] == '\0') {
            System.out.println("This cipher letter has no mapping."); return;
        }
        undo.add(key.clone());
        key[cipher - 'A'] = '\0';
        history.add("REMOVE " + cipher);
        System.out.println(preview());
    }

    private void undo() {
        if (undo.isEmpty()) { System.out.println("Nothing to undo."); return; }
        char[] last = undo.remove(undo.size() - 1);
        System.arraycopy(last, 0, key, 0, 26);
        history.add("UNDO");
        System.out.println(preview());
    }

    private void reset() {
        undo.add(key.clone());
        Arrays.fill(key, '\0');
        history.add("RESET");
        System.out.println(preview());
    }

    private void save(String filename) {
        StringBuilder sb = new StringBuilder("LAB 2 - INTERACTIVE FREQUENCY ANALYSIS\n\n");
        sb.append("ORIGINAL CIPHERTEXT:\n").append(ciphertext).append("\n\n");
        sb.append("HISTORY:\n");
        for (int i = 0; i < history.size(); i++) sb.append(i + 1).append(". ").append(history.get(i)).append('\n');
        sb.append("\nCURRENT DECRYPTION:\n").append(preview()).append("\n\n");
        sb.append("DECRYPTION KEY (CIPHER -> PLAIN):\n");
        for (int i = 0; i < 26; i++) sb.append(ABC.charAt(i)).append(" -> ").append(key[i] == '\0' ? '?' : key[i]).append('\n');
        try { Files.writeString(Path.of(filename), sb.toString(), StandardCharsets.UTF_8);
            System.out.println("Saved to " + filename);
        } catch (IOException e) { System.out.println("Cannot save: " + e.getMessage()); }
    }

    private void run() {
        frequencies();
        commonWords(1, 10);
        commonWords(2, 12);
        commonWords(3, 12);
        System.out.println("\nCommands: set V E | remove V | undo | reset | show | freq | words | key | save [file] | help | exit");
        System.out.println("Known plaintext letters = lowercase; unknown ciphertext letters = UPPERCASE.");
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("\n> ");
                if (!scanner.hasNextLine()) break;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] args = line.toUpperCase(Locale.ROOT).split("\\s+");
                String cmd = args[0];
                if (cmd.equals("EXIT") || cmd.equals("QUIT")) break;
                switch (cmd) {
                    case "SET" -> {
                        if (args.length == 3 && args[1].length() == 1 && args[2].length() == 1)
                            set(args[1].charAt(0), args[2].charAt(0));
                        else System.out.println("Usage: set V E");
                    }
                    case "REMOVE" -> {
                        if (args.length == 2 && args[1].length() == 1) remove(args[1].charAt(0));
                        else System.out.println("Usage: remove V");
                    }
                    case "UNDO" -> undo();
                    case "RESET" -> reset();
                    case "SHOW" -> System.out.println(preview());
                    case "FREQ" -> frequencies();
                    case "WORDS" -> { commonWords(1, 10); commonWords(2, 12); commonWords(3, 12); }
                    case "KEY" -> showKey();
                    case "SAVE" -> save(line.split("\\s+", 2).length > 1 ? line.split("\\s+", 2)[1] : "analysis.txt");
                    case "HELP" -> System.out.println("set C P: ciphertext C -> plaintext P\nremove C: erase mapping\nundo: restore previous mapping state\nreset: clear mappings\nshow: preview\nfreq: sorted letter frequencies\nwords: frequent short words\nkey: display mapping\nsave [file]: save progress\nexit: quit");
                    default -> System.out.println("Unknown command. Type help.");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Path file = Path.of(args.length > 0 ? args[0] : "variant11.txt");
        if (!Files.exists(file)) {
            System.err.println("Ciphertext file not found: " + file.toAbsolutePath());
            System.err.println("Create variant11.txt in the current directory, or pass its path as an argument.");
            return;
        }
        String text = Files.readString(file, StandardCharsets.UTF_8);
        if (text.isBlank()) { System.err.println("Ciphertext file is empty."); return; }
        new FrequencyAnalysis(text).run();
    }
}
