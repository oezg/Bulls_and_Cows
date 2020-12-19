package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        int len = askLength();
        if (len > 0) {
            int range = askRange();
            if (range > 0) {
                if (len > range) {
                    System.out.printf("Error: it's not possible to generate " +
                            "a code with a length of %d with %d unique " +
                            "symbols.", len, range);
                } else {
                    String secret = secretNumberGenerator(len, range);
                    System.out.println("Okay, let's start a game!");
                    int turn = 1;
                    int[] cowsBulls;
                    do {
                        System.out.printf("Turn %d: ", turn++);
                        cowsBulls = grader(scanner.next(), secret);
                        System.out.println(gradeMessage(cowsBulls));
                    } while (cowsBulls[1] < len);
                    System.out.println("Congratulations! You guessed the secret code.");
                }
            } else {
                System.out.println("Error: the range of possible symbols" +
                        " in the code must be a positive integer.");
            }
        } else {
            System.out.println("Error: the length of the secret code " +
                    "must be a positive integer.");
        }
    }

    static String gradeMessage(int[] a) {
        StringBuilder s = new StringBuilder();
        if (a[0] == 0 && a[1] == 0) {
            s.append("None");
        } else {
            s.append("Grade: ");
            if (a[1] > 0) {
                s.append(a[1] + " bull");
                if (a[1] > 1) {
                    s.append("s");
                }
                if (a[0] > 0) {
                    s.append(" and " + a[0] + " cow");
                    if (a[0] > 1) {
                        s.append("s");
                    }
                }
            } else {
                s.append(a[0] + " cow");
                if (a[0] > 1) {
                    s.append("s");
                }
            }
        }
        return s.toString();
    }

    static int askRange() {
        int range;
        String input;
        final int MAX_LENGTH = 36;
        System.out.println("Input the number of possible symbols in the code:");
        input = scanner.nextLine();
        try {
            range = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            range = 0;
            System.out.printf("Error: \"%s\" isn't a valid number.%n", input);
        }
        if (range > MAX_LENGTH) {
            range = 0;
            System.out.println("Error: maximum number of possible" +
                    "symbols in the code is 36 (0-9, a-z).");
        }
        return range;
    }

    static int askLength() {
        int len;
        final int MAX_LENGTH = 36;
        String input;
        System.out.println("Please, enter the secret code's length:");
        input = scanner.nextLine();
        try {
            len = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            len = 0;
            System.out.printf("Error: \"%s\" isn't a valid number.%n", input);
        }
        if (len > MAX_LENGTH) {
            len = 0;
            System.out.println("Error: maximum number of possible " +
                    "symbols in the code is 36 (0-9, a-z).");
        }
        return len;
    }

    static String secretNumberGenerator(int len, int range) {
        StringBuilder code = new StringBuilder();
        StringBuilder stars = new StringBuilder();
        StringBuilder limit = new StringBuilder();
        Random random = new Random();
        int maxDigit = 0;
        int[] digits = new int[len];
        for (int i = 0; i < len; i++) {
            stars.append('*');
            boolean exists;
            do {
                exists = false;
                digits[i] = random.nextInt(range);
                for (int j = 0; j < i; j++) {
                    if (digits[j] == digits[i]) {
                        exists = true;
                    }
                }
            } while (exists);
            if (digits[i] > maxDigit) {
                maxDigit = digits[i];
            }
            if (digits[i] > 9) {
                code.append((char)('a' + digits[i] - 10));
            } else {
                code.append(digits[i]);
            }
        }
        if (range < 10) {
            limit.append(range);
        } else {
            limit.append("9, a-");
            char end = (char)('a' + range - 11);
            limit.append(end);
        }
        System.out.printf("The secret is prepared: %s (0-%s).%n", stars, limit);
        return code.toString();
    }

    static int[] grader(String guess, String secret) {
        char[] secretChars = secret.toCharArray();
        int[] cNb = new int[] {0, 0};
        char[] guessChars = guess.toCharArray();
        for (char c : guessChars) {
            for (char d : secretChars) {
                if (c == d) {
                    cNb[0]++;
                }
            }
        }
        for (int i = 0; i < guessChars.length; i++) {
            if (guessChars[i] == secretChars[i]) {
                cNb[1]++;
                cNb[0]--;
            }
        }
        return cNb;
    }
}
