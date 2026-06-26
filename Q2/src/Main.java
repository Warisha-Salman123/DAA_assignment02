import java.util.*;

class BoyerMoore {

    static int comparisons = 0;
    static List<Integer> matchedIndices = new ArrayList<>();

    // Prefix Function (π)
    static int[] computePrefixFunction(String pattern) {

        int m = pattern.length();
        int[] pi = new int[m];

        int k = 0;

        for (int q = 1; q < m; q++) {

            while (k > 0 &&
                    pattern.charAt(k) != pattern.charAt(q)) {

                k = pi[k - 1];
            }

            if (pattern.charAt(k) == pattern.charAt(q))
                k++;

            pi[q] = k;
        }

        return pi;
    }

    // Last Occurrence Function (λ)
    static int[] computeLastOccurrenceFunction(String pattern) {

        int[] lambda = new int[256];

        Arrays.fill(lambda, -1);

        for (int i = 0; i < pattern.length(); i++) {
            lambda[pattern.charAt(i)] = i;
        }

        return lambda;
    }

    // Good Suffix Function (γ)
    static int[] computeGoodSuffixFunction(String pattern) {

        int m = pattern.length();

        int[] gamma = new int[m + 1];

        int[] pi = computePrefixFunction(pattern);

        String reversed =
                new StringBuilder(pattern).reverse().toString();

        int[] piReverse =
                computePrefixFunction(reversed);

        for (int j = 0; j <= m; j++) {
            gamma[j] = m - pi[m - 1];
        }

        for (int l = 1; l <= m; l++) {

            int j = m - piReverse[l - 1];

            if (j <= m &&
                    gamma[j] > l - piReverse[l - 1]) {

                gamma[j] = l - piReverse[l - 1];
            }
        }

        return gamma;
    }

    // Boyer Moore Matcher
    static void boyerMooreMatcher(String text,
                                  String pattern) {

        int n = text.length();
        int m = pattern.length();

        int[] lambda =
                computeLastOccurrenceFunction(pattern);

        int[] gamma =
                computeGoodSuffixFunction(pattern);

        int shift = 0;

        while (shift <= n - m) {

            System.out.println("\n-----------------------------------");
            System.out.println("Shift = " + shift);

            for (int i = 0; i < shift; i++)
                System.out.print(" ");

            System.out.println(pattern);

            int j = m - 1;

            while (j >= 0) {

                comparisons++;

                char patternChar = pattern.charAt(j);
                char textChar = text.charAt(shift + j);

                System.out.println(
                        "Comparing P[" + j + "] = '"
                                + patternChar
                                + "' with T[" + (shift + j)
                                + "] = '"
                                + textChar + "'"
                );

                if (patternChar == textChar) {

                    System.out.println("Match");

                    j--;
                }
                else {

                    System.out.println("Mismatch");

                    break;
                }
            }

            if (j < 0) {

                matchedIndices.add(shift);

                System.out.println(
                        "\n>>> Pattern Found at Index "
                                + shift
                );

                System.out.println(
                        "Good Suffix Shift = "
                                + gamma[0]
                );

                shift += gamma[0];
            }
            else {

                char mismatched =
                        text.charAt(shift + j);

                int badCharacterShift =
                        j - lambda[mismatched];

                int goodSuffixShift =
                        gamma[j + 1];

                int actualShift =
                        Math.max(goodSuffixShift,
                                badCharacterShift);

                if (actualShift < 1)
                    actualShift = 1;

                System.out.println(
                        "Bad Character Shift = "
                                + badCharacterShift
                );

                System.out.println(
                        "Good Suffix Shift = "
                                + goodSuffixShift
                );

                System.out.println(
                        "Actual Shift = "
                                + actualShift
                );

                shift += actualShift;
            }
        }
    }

    public static void main(String[] args) {

        String text =
                "Insertion sort typically has a smaller constant factor than merge sort";

        String pattern = "sort";

        System.out.println("TEXT:");
        System.out.println(text);

        System.out.println("\nPATTERN:");
        System.out.println(pattern);

        System.out.println(
                "\n===== BOYER MOORE MATCHING ====="
        );

        boyerMooreMatcher(text, pattern);

        System.out.println(
                "\n==================================="
        );

        System.out.println(
                "Total Matches Found = "
                        + matchedIndices.size()
        );

        System.out.println(
                "Matched Indices = "
                        + matchedIndices
        );

        System.out.println(
                "Total Comparisons Made = "
                        + comparisons
        );
    }
}