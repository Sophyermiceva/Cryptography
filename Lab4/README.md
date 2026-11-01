# Laboratory Work 4 — DES, Task 2.11

A minimal Java console application that calculates the final DES ciphertext block from the known 32-bit halves `L16` and `R16`.

## Requirements
- JDK 17 or newer
- No external libraries, Maven, or Gradle

## Run (Windows PowerShell, from project root)
```powershell
javac -d out src/main/java/md/utm/des/*.java
java -cp out md.utm.des.Main
```

## Run (Linux/macOS, from project root)
```bash
javac -d out src/main/java/md/utm/des/*.java
java -cp out md.utm.des.Main
```

## Procedure
1. Enter 32 bits for `L16` and `R16`, or generate both randomly.
2. Concatenate the halves in the DES final order: `R16 || L16`.
3. Apply the standard inverse initial permutation, `IP^-1`.
4. Display the 64-bit ciphertext in binary and 16-digit hexadecimal form.

The app prints the complete permutation table and all 64 individual bit mappings. Input is validated.

## Test vector
Standard DES test vector (`0123456789ABCDEF` encrypted using key `133457799BBCDFF1`):
- L16 = `01000011010000100011001000110100`
- R16 = `00001010010011001101100110010101`
- Expected ciphertext (HEX): `85E813540F0AB405`

Additional simple self-check:
- L16 = `00000000000000000000000000000000`
- R16 = `00000000000000000000000000000000`
- Expected ciphertext (HEX): `0000000000000000`

> DES is obsolete for securing real data. This program is for the university lab only.
