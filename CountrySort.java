/***********************************
 * Course: Lehigh CSE017-SU2024
 * Assignment: Final (v01)
 * Name: Yinglong Lin
 * UID: yile22
 * **********************************/

import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.PriorityQueue;


public class CountrySort{
    public static void doMenu(Scanner kbd, Country[] countries){
        if(countries == null || countries.length == 0){
            System.err.println("Cannot do sorts on a null or empty list.");
            return;
        }
        int userChoice = -1;
        do{
            System.out.printf("%nSelect an Operation%n");
            System.out.println("0. Shuffle Countries");
            System.out.println("1. Sort by country name");
            System.out.println("2. Sort by country natural ordering (code low to high)");
            System.out.println("3. Sort by area (low to high)");
            System.out.println("4. Sort by population (high to low)");
            System.out.println("5. Sort by emissions (high to low)");
            System.out.println("7. Sort by emissions to population ratio (low to high)");
            System.out.println("9. Exit submenu"); 
            try{
                userChoice = kbd.nextInt(); kbd.nextLine();
                switch(userChoice){
                    case 0:
                        CountryAnalyzer.shuffleFisherYates(countries);
                        break;
                    case 1: //1. Sort by country name
                        Arrays.sort(countries, ORDER_NAME_LH);
                        break;
                    case 2: // 2. Sort by country natural ordering (code, low to high)
                        Arrays.sort(countries); 
                        break;
                    case 3: // Sort by area (low to high)
                        // Arrays.sort(countries, ORDER_NOT_IMPLEMENTED);
                        Arrays.sort(countries, ORDER_AREA_LH);
                        break;
                    case 4: // Sort by population (high to low)
                        Arrays.sort(countries, ORDER_POPULATION_LH.reversed());
                        break;
                    case 5: // Sort by emissions (high to low)
                        Arrays.sort(countries, ORDER_EMISSIONS_LH.reversed());
                        break;
                    case 6: // Sort by emissions to population ratio (low to high)
                        Arrays.sort(countries, ORDER_RATIO_EMISSIONS_POPULATION_LH);
                        break; 
                    case 9: // 9. Exit
                        System.out.println("Exiting submenu");
                        break;
                    default: 
                        System.err.println("Invalid option");
                }
                if(userChoice != 9) 
                    CountrySort.printCountriesTopTen(countries);
            } catch(InputMismatchException e){
                System.err.println("Not an int");
                kbd.nextLine(); // clear out rest of input line
            }
        } while (userChoice != 9);        
    }

    /** prints the top 10 */
    public static void printCountriesTopTen( Country[] countries ){
        for( int i=0; i<countries.length && i<10; i++){
            System.out.println( countries[i] );
        }
    }

    public static final Comparator<Country> ORDER_NOT_IMPLEMENTED = new Comparator<Country>() {
        public int compare(Country c1, Country c2){
            throw new UnsupportedOperationException("Requested sort not implemented!");
        }
    };   

    public static final Comparator<Country> ORDER_CODE_LH = new Comparator<Country>() {
        public int compare(Country c1, Country c2){
            return c1.getCode().compareTo(c2.getCode());
        }
    }; 

    public static final Comparator<Country> ORDER_NAME_LH = new Comparator<Country>() {
        public int compare(Country c1, Country c2){
            return c1.getName().compareTo(c2.getName());
        }
    }; 

    static class CountryPopulationComparator implements Comparator<Country> {
        private int comparisonYear;
        public CountryPopulationComparator( int year ){
            this.comparisonYear = year;
        }

        public int compare(Country c1, Country c2){
            return Double.compare(c1.getPopulation(comparisonYear), c2.getPopulation(comparisonYear));
        }
    }

    /** Comparator to order by the population of the country for the specified year */
    public static final Comparator<Country> ORDER_POPULATION_LH = new CountryPopulationComparator(2021);     

    // -------------------------------------------
    // TODO: you are to define or refine the below
    // -------------------------------------------

    public static final Comparator<Country> ORDER_AREA_LH = new Comparator<Country>() {
        public int compare(Country c1, Country c2) {
            return Double.compare(c1.getArea(), c2.getArea());
        }
    };
    
    public static final Comparator<Country> ORDER_EMISSIONS_LH = new Comparator<Country>() {
        public int compare(Country c1, Country c2) {
            Double e1 = c1.getEmission(2021); // Using 2021 as the latest year
            Double e2 = c2.getEmission(2021);
            if (e1 == null) e1 = 0.0;
            if (e2 == null) e2 = 0.0;
            return Double.compare(e1, e2);
        }
    };

    public static final Comparator<Country> ORDER_RATIO_EMISSIONS_POPULATION_LH = new Comparator<Country>() {
        public int compare(Country c1, Country c2) {
            int year = 2021; // Hard-coded year as specified
            Double e1 = c1.getEmission(year);
            Double e2 = c2.getEmission(year);
            int p1 = c1.getPopulation(year);
            int p2 = c2.getPopulation(year);
            
            if (e1 == null) e1 = 0.0;
            if (e2 == null) e2 = 0.0;
            
            double ratio1 = p1 != 0 ? e1 / p1 : Double.MAX_VALUE;
            double ratio2 = p2 != 0 ? e2 / p2 : Double.MAX_VALUE;
            
            return Double.compare(ratio1, ratio2);
        }
    };

    /** uses heap to sort list by the ratio of emissions to population, low to high */
    /**
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     * 
     * Justification:
     * Time:
     * - Creating the heap: O(n)
     * - Adding n elements to heap: n * O(log n) = O(n log n)
     * - Polling n elements from heap: n * O(log n) = O(n log n)
     * Total time: O(n) + O(n log n) + O(n log n) = O(n log n)
     * 
     * Space:
     * - PriorityQueue uses O(n) extra space
     */
    public static void sortConservers(Country[] list) {
        PriorityQueue<Country> maxHeap = new PriorityQueue<>(
            list.length,
            ORDER_RATIO_EMISSIONS_POPULATION_LH.reversed()
        );

        for (Country country : list) {
            maxHeap.offer(country);
        }

        for (int i = list.length - 1; i >= 0; i--) {
            list[i] = maxHeap.poll();
        }

        System.out.println("SORTED BY EMISSIONS TO POPULATION RATIO (LH).");
        printCountriesTopTen(list);
    }

    /** sorts list by ascending area, then by emissions high to low (exploit stability of merge sort) */
    public static void sortConsumers(Country[] list) {
        // First, sort by area (low to high)
        Arrays.sort(list, ORDER_AREA_LH);
        System.out.println("SORTED BY AREA (LH).");
        printCountriesTopTen(list);

        // Then, sort by emissions (high to low)
        Arrays.sort(list, ORDER_EMISSIONS_LH.reversed());
        System.out.println("SORTED BY EMISSIONS (HL).");
        printCountriesTopTen(list);
    }
}