package lab6;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Skeleton for the password cracking task
 * @author Fabian
 *
 */
public class Crack {

    /** The name used for the Web service */
    public static String name;

    /** List of accounts */
    public static List<Integer> accounts;

    /** Loads a file as a list of strings, one per line */
    public static List<String> load(File f) {
        try {
            return (java.nio.file.Files.readAllLines(f.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to crack the accounts with the password, returns TRUE if it worked
     */
    public static boolean crack(String password) {
        if (Collections.binarySearch(accounts, password.hashCode()) >= 0) {
            try (Scanner s = new Scanner(
                    new URL("https://julienromero.com/ATHENS/submit?name=" + name + "&password=" + password)
                            .openStream())) {
                if (s.nextLine().equals("True")) {
                    accounts.remove(new Integer(password.hashCode()));
                    System.out.println("Password: " + password);
                    return (true);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (false);
    }

    /**
     * Takes as arguments (1) a file with account numbers and (2) your name (as
     * given on our Web page). Prints the passwords one per line (in any order).
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Uncomment for your convenience, comment it out again before
        // submitting
        // args=new String[]{"./src/lab4solution/accounts/elvis.txt", "elvis"};
        args = new String[]{"./ktang.txt", "ktang", "./pw.txt", "./pw2.txt", "./worldcities.txt"};
        accounts = load(new File(args[0])).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        name = args[1];
        Collections.sort(accounts);

        // Smartness goes here

        // Popular passwords (e.g., downloaded from the Web)
        List<String> passwd  = load(new File(args[2])).stream().collect(Collectors.toList());
        // Dictionary attack with the 10000 most common English words (in lowercase)
        List<String> passwd2 = load(new File(args[3])).stream().collect(Collectors.toList());
        passwd.addAll(passwd2);

        // The city where Elvis' parents met (try all cities)
        FileReader fr = new FileReader(args[4]);
        BufferedReader br = new BufferedReader(fr);

        String str = null;
        List<String> worldCities = new ArrayList<>();
        try{
            while( (str = br.readLine()) != null ){
                str = str.split(",")[2];
                crack(str);
                worldCities.add(str);
                // System.out.println(str);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        for(String pw : passwd){
            crack(pw);
            // The dictionary attack, randomly replacing letters with the corresponding numbers or symbols
            crack(pw.replace("a", "@"));
            crack(pw.replace("l", "1"));
            crack(pw.replace("o","0"));
            crack(pw.replace("e", "3"));
        }

        // Diceware passwords built from the 10000 most common English words
        // (in lowercase with hyphens in between) up to length 2
        for(String pw1 : passwd){
            for(String pw2: passwd){
                crack(pw1+"-"+pw2);
            }
        }

        // Brute force with up to 10 digits
        for(int j = 0; j < 10; j ++) {
            for (long i = 0; i <= 999999999; i++) {
                crack(j+ "" + i);
            }
        }

        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQTSTUVWXYZ1@3";
        // Brute force with up to 5 letters
        for(int i1 = 0; i1 < chars.length(); i1 ++ ){
            crack(""+chars.charAt(i1));
            for(int i2 = 0; i2 < chars.length(); i2 ++ ){
                crack(chars.charAt(i1)+""+chars.charAt(i2));
                for(int i3 = 0; i3 < chars.length(); i3 ++ ) {
                    crack(""+chars.charAt(i1)+""+chars.charAt(i2) +
                            ""+chars.charAt(i3));
                    for (int i4 = 0; i4 < chars.length(); i4++) {
                        crack(""+chars.charAt(i1)+""+chars.charAt(i2) +
                                ""+chars.charAt(i3) + chars.charAt(i4));
                        for (int i5 = 0; i5 < chars.length(); i5++) {
                            crack(""+chars.charAt(i1)+""+chars.charAt(i2) +
                                    ""+chars.charAt(i3) + chars.charAt(i4) +
                                    ""+chars.charAt(i5));
                        }
                    }
                }
            }
        }

    }

}