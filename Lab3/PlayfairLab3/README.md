# Laboratory Work 3 — Playfair Cipher (Variant 11)

Java console application supporting all 31 Romanian letters: `AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ`.

## Run

```sh
javac -encoding UTF-8 *.java
java -Dfile.encoding=UTF-8 Main
```

On Windows Command Prompt, first run `chcp 65001` and use a font that supports Romanian letters. In IntelliJ IDEA, set File Encoding and Project Encoding to UTF-8; preferably run in its terminal.

The cipher uses a 4×8 matrix with `#` as one extra cell. This is an adaptation of the laboratory's generic Playfair matrix procedure to preserve all 31 letters. Ciphertexts can contain `#`; it is accepted when decrypting, but never in plaintext or keys. Whitespace is removed before processing. Keys require at least 7 letters, excluding spaces. Romanian legacy cedilla characters `Ş/Ţ` are normalized to `Ș/Ț`.

Duplicate letters in a digraph are separated with `X`, or `Q` if the letter is `X`. An odd final letter is padded in the same manner. Decryption outputs the raw prepared text; filler removal and word spacing are manual, as requested in the lab.

Files: Main.java, RomanianAlphabet.java, PlayfairMatrix.java, PlayfairCipher.java.
