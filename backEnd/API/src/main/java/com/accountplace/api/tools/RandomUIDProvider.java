package com.accountplace.api.tools;

import java.util.Random;

/**
 * Utility class to generate random 10-digit numbers.
 * This class provides methods to generate random numbers within a specific range.
 */
public class RandomUIDProvider {

    private static final Random RANDOM = new Random();
    private static final long MIN_TEN_DIGIT_NUMBER = 1000000000L; // 10-digit number starting from 1000000000
    private static final long MAX_TEN_DIGIT_NUMBER = 9999999999L; // 10-digit number ending at 9999999999

    /**
     * Generates a random 10-digit number.
     * This method generates a random number within the range of 1000000000 and 9999999999 (inclusive).
     * The number is generated using the {@link Random} class.
     *
     * @return a randomly generated 10-digit number.
     */
    public static Long generateRandom10DigitNumber() {
        // Generate a random number between MIN_TEN_DIGIT_NUMBER and MAX_TEN_DIGIT_NUMBER
        return MIN_TEN_DIGIT_NUMBER + (long) (RANDOM.nextDouble() * (MAX_TEN_DIGIT_NUMBER - MIN_TEN_DIGIT_NUMBER + 1));
    }

    /**
     * Main method for demonstrating the random number generation.
     * This method calls {@link #generateRandom10DigitNumber()} and prints the generated number.
     *
     * @param args command line arguments (not used in this case).
     */
    public static void main(String[] args) {
        // Example usage
        long random10DigitNumber = generateRandom10DigitNumber();
        System.out.println("Random 10-digit number: " + random10DigitNumber);
    }
}
