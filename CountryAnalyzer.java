/***********************************
 * Course: Lehigh CSE017-SU2024
 * Assignment: Final (v01)
 * Name: Yinglong Lin
 * UID: yile22
 * **********************************/

/** GIVEN: STUDENTS ARE TO COMPLETE INDICATED SECTION */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class CountryAnalyzer{
    /** maximum number of Countries to read from file */
    public static final int MAX_LINES_FILE = 300; // TODO: "refactor" so this is not needed.
    /** random number generator to use; starts with a fixed seed */
    public static final java.util.Random RANDOM = new Random(0x017L);
    /** scanner to read from System.in (keyboard) */
    public static final Scanner KBD = new Scanner(System.in);

    /** prints top level menu items for user to select from */
    public static void printMenu(){
        System.out.println("Select an Operation:");
        System.out.println("0. (re)Read countries");
        System.out.println("1. View top 10 countries");
        System.out.println("2. Search by country name");
        System.out.println("3. Sort by country natural ordering (id)");
        System.out.println("4. Sort by preferences submenu");
        System.out.println("5. Sort by preferences: Conservers");
        System.out.println("6. Sort by preferences: Consumers");
        System.out.println("8. Shuffle/randomize countries");
        System.out.println("9. Exit"); 
    }

    /** accepts an optional argument of file to read from */
    public static void main(String[] args){
        String fname_codes = args.length > 0 ? args[0] : "codes.txt";
        String fname_countries = args.length > 1 ? args[1] : "countries.csv";
        String fname_country_info = args.length > 2 ? args[2] : "country_info.csv";
        System.out.printf("Using files:%n codes:%s%n countries:%s%n country_info:%s%n", fname_codes, fname_countries, fname_country_info);

        java.util.List<String> codes = readCodes(fname_codes);
        System.out.printf("Read in %d country codes.%n", codes.size() );
        Country[] countries = readCountries(fname_countries, codes);
        System.out.printf("Read in %d countries.%n", countries.length );
        java.util.Map<String,Country> countryDetails = Country.readCountryDetails(fname_country_info, countries);
        System.out.printf("Have details on %d countries.%n", countryDetails.size());

        int userChoice = -1;
        do{
            printMenu();
            try{
                userChoice = KBD.nextInt(); KBD.nextLine();
                switch(userChoice){
                    case 0: //0. (re)read countries
                        System.out.println("Noop.");
                        break;
                    case 1: //1. View countries top 10
                        CountrySort.printCountriesTopTen(countries);
                        break;
                    case 2: // 2. Search by Country name
                        System.out.println("Noop.");
                        break;
                    case 3: // 3. Sort by Country natural ordering (code)
                        Arrays.sort(countries); 
                        break;
                    case 4: // Sort by preferences submenu
                        CountrySort.doMenu(KBD, countries);
                        break;
                    case 5: // Sort by preferences: Conservers
                        CountrySort.sortConservers(countries);
                        break;
                    case 6: // Sort by preferences: Consumers
                        CountrySort.sortConsumers(countries);
                        break;
                    case 8: // Shuffle up the array
                        shuffleFisherYates(countries);
                        break;
                    case 9: // 9. Exit
                        System.out.println("Quitting, goodbye.");
                        break;
                    default: 
                        System.err.println("Invalid option");
                }
                if( userChoice != 9 ) 
                    CountrySort.printCountriesTopTen(countries);
            } catch(InputMismatchException e){
                System.err.println("Not an int");
                KBD.nextLine(); // clear out rest of input line
            }
        } while (userChoice != 9);
    }

    /**
     * implements https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
     * The algorithm produces an unbiased permutation: every permutation is equally likely. 
     * The modern version of the algorithm is efficient: it takes time proportional to the 
     * number of items being shuffled and shuffles them in place.
     * @param list
     */
    public static final void shuffleFisherYates(Object[] list){
        for(int i=list.length-1; i>0; i--)
            swap(list, i, RANDOM.nextInt(i)); // nextInt is exclusive, dont swap with self
    }

    /** swaps elements i and j in list */
    private static final void swap(Object[] list, int i, int j){
        Object tmp = list[i];
        list[i] = list[j];
        list[j] = tmp;
    }

    /** 
     * format: code
     * example: AFG
     * input file: codes.txt
     * allows lines to be commented by preceeding with a # sybmol
     * */
    public static java.util.List<String> readCodes(String filename){
        java.util.List<String> codes = new ArrayList<>();
        try(Scanner read = new Scanner(new File(filename), "UTF-8")){
            while(read.hasNextLine()){
                String nextLine = read.nextLine();
                if( Country.isCommentedLine(nextLine) ){
                    System.out.println( "Skipping: " + nextLine );
                    continue; //skip it
                }
                codes.add(nextLine);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return codes;
    }

    // --------------------------------------------------------------
    // TODO: you are to implement or modify the methods below
    //   You may replace the entirety of the body of each method, 
    //   but may not change the method signature (ie modifiers, 
    //   return type method name, number/types/order of arguments)
    // --------------------------------------------------------------

    // --------------------------------------------------------------
    // TODO: you are to implement the methods below
    //   +readCountries(String filename, java.util.List<String> countryCodes)
    // --------------------------------------------------------------


    /** 
     * returns a list of Countries read from filename up to MAX_LINES_FILE entries
     * returned list has no null elements and has exactly as many Countries as in file. 
     * 
     * format: code,name,area    
     * example: AFG,Afghanistan,652230
     * input file: countries.csv
     * allows lines to be commented by preceeding with a # sybmol
     * 
     * TODO:
     *   1. change this to use an ArrayList; use toArray() at the end to return an array
     *      note: delete/remove the static class variable MAX_LINES_FILE, as well
     *   2. make this method catch any CountryFmtException thrown by fromString, print the offending
     *      line to System.err, AND continue parsing the file
     * */
    public static Country[] readCountries(String filename, java.util.List<String> countryCodes) {
        ArrayList<Country> countries = new ArrayList<>();
        
        try (Scanner read = new Scanner(new File(filename), "UTF-8")) {
            while (read.hasNextLine()) {
                String line = read.nextLine();
                try {
                    Country newCountry = Country.fromString(line, countryCodes);
                    if (newCountry != null) {
                        countries.add(newCountry);
                    }
                } catch (CountryFmtException e) {
                    System.err.println("Warning: " + e.getMessage());
                    System.err.println("Skipping malformed country line: " + e.getBadString());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found, no data to work on.");
            System.exit(-1);
        }
    
        return countries.toArray(new Country[0]);
    }
}