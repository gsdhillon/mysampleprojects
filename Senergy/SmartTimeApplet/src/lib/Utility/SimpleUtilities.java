/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.Utility;

import java.util.Random;

/**
 *
 * @author pradnya
 */
public class SimpleUtilities {

    public static String getPasswordText(char pwd[]) {
        String password = "";
        for (int i = 0; i < pwd.length; i++) {
            password = password + pwd[i];
        }
        return password;
    }

    public static String getSplittedString(String str, int positionToGet) {
        String split[] = str.split(" ");
        return split[positionToGet];
    }

    public static int generateRandomNumber() {
        Random rand = new Random();
        int num = rand.nextInt(99);
        int numNoRange = rand.nextInt();
        return numNoRange;
    }

    public static String[] fillReaderAccessCombo() {
        String strAccesslevel[] = {"Select AccessLevel", ""
            + "AccessLevel 1", ""
            + "AccessLevel 2", ""
            + "AccessLevel 3", ""
            + "AccessLevel 4", ""
            + "AccessLevel 5", ""
            + "AccessLevel 6", ""
            + "AccessLevel 7", ""
            + "AccessLevel 8", ""
            + "AccessLevel 9", ""
            + "AccessLevel 10", ""
            + "AccessLevel 11", ""
            + "AccessLevel 12", ""
            + "AccessLevel 13", ""
            + "AccessLevel 14", ""
            + "AccessLevel 15", ""
            + "AccessLevel 16", ""
            + "AccessLevel 17", ""
            + "AccessLevel 18", ""
            + "AccessLevel 19", ""
            + "AccessLevel 20", ""
            + "AccessLevel 21", ""
            + "AccessLevel 22", ""
            + "AccessLevel 23", ""
            + "AccessLevel 24", ""
            + "AccessLevel 25", ""
            + "AccessLevel 26", ""
            + "AccessLevel 27", ""
            + "AccessLevel 28", ""
            + "AccessLevel 29", ""
            + "AccessLevel 30", ""
            + "AccessLevel 31", ""
            + "AccessLevel 32", ""
            + "AccessLevel 33", ""
            + "AccessLevel 34", ""
            + "AccessLevel 35", ""
            + "AccessLevel 36", ""
            + "AccessLevel 37", ""
            + "AccessLevel 38", ""
            + "AccessLevel 39", ""
            + "AccessLevel 40", ""
            + "AccessLevel 41", ""
            + "AccessLevel 42", ""
            + "AccessLevel 43", ""
            + "AccessLevel 44", ""
            + "AccessLevel 45", ""
            + "AccessLevel 46", ""
            + "AccessLevel 47", ""
            + "AccessLevel 48", ""
            + "AccessLevel 49", ""
            + "AccessLevel 50"};
        return strAccesslevel;
    }
}
