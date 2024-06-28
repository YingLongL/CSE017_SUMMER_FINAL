/***********************************
 * Course: Lehigh CSE017-SU2024
 * Assignment: Final (v01)
 * Name: Yinglong Lin
 * UID: yile22
 * **********************************/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeMap;

/** GIVEN: STUDENTS ARE TO COMPLETE INDICATED SECTIONS */
public class Country implements Comparable<Country>
{
    /** id used for country */
    private String code;
    /** country name, e.g. Angora */
    private String name;
    /** area in ?? units */
    private double area;
    /** list of country population, a pair of year and population count */
    private DoublyLinkedList< Pair<Integer,Integer> > population;
    /** the product weight, in grams */
    private java.util.Map<Integer,Double> emissions;

    protected Country( String code, String name, double area){
        this.code = code;
        this.name = name;
        this.area = area;
        population = new DoublyLinkedList<>();
        emissions = new java.util.HashMap<>();
    }

    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}

    public String getName() {return this.name;}
    public void setName(String n) {this.name = n;}

    public double getArea() {return this.area;}
    public void setArea(double a) {this.area = a;}

    public void addPopulation(int year, int count){
        population.add(new Pair<>(year, count));
    }
    public int getPopulation(int year){
        int first = 0;
        int last = population.size()-1;
        while (last >= first){
            int mid = (first + last) / 2;
            Pair<Integer,Integer> p = population.get(mid);
            if(year == p.getFirst() ){
                return p.getSecond() ;
            }
            else if(year > p.getFirst() ){
                first = mid + 1;
            }
            else{
                last = mid - 1;
            }

        }
        return 0;
    }
    
    public void addEmission(int year, double emission){
        emissions.put(year, emission);
    }
    public Double getEmission(int year){
        return emissions.get(year);
    }

    @Override
    public String toString(){
        return String.format("%-10s\t%-32s\t%-10.2f", code, name, area);
    }

    /** line is a comment if it begins with # */
    public static boolean isCommentedLine( String line ){
        if( line.trim().startsWith("#") ) // line is a comment because it begins with #
            return true;
        return false;
    }

    /** 
     * format: code,year,emission,population  
     * example: AFG,2000,1047127.94,19542986
     * input file: country_info.csv
     * allows lines to be commented by preceeding with a # sybmol
     * */
    public static java.util.Map<String,Country> readCountryDetails(String filename, Country[] countries){
        TreeMap<String,Country> tree = new TreeMap<>(); // maps String country code to Country object
        for( Country c : countries )
            tree.put( c.getCode(), c);

        try(Scanner read = new Scanner(new File(filename), "UTF-8")){
            while(read.hasNextLine()){
                String line = read.nextLine();
                if( isCommentedLine(line) ) 
                    continue; // skip the line
                String[] attributes = line.split(",");
                String code = attributes[0];
                
                if( tree.containsKey( code ) ){
                    Country c = tree.get(code);
                    c.addEmission(Integer.parseInt(attributes[1]), Double.parseDouble(attributes[2]));
                    c.addPopulation(Integer.parseInt(attributes[1]), Integer.parseInt(attributes[3]));
                    for(int i=2001; i<=2021;i++){
                        line = read.nextLine();
                        attributes = line.split(",");
                        c.addEmission(Integer.parseInt(attributes[1]), Double.parseDouble(attributes[2]));
                        c.addPopulation(Integer.parseInt(attributes[1]), Integer.parseInt(attributes[3]));
                    }
                }else{
                    System.err.println( "Unknown country found in emissions file: " + line );
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return tree;
    }

    @Override
    public int compareTo(Country other) {
        if (other == null) {
            return 1; // Consider this Country greater than null
        }
        return this.code.compareToIgnoreCase(other.code);
    }
    /** 
     * format: code,name,area    
     * example: AFG,Afghanistan,652230
     * input file: countries.csv
     * allows lines to be commented by preceeding with a # sybmol
     * */
    public static Country fromString(String countryLine, java.util.List<String> countryCodes) throws CountryFmtException {
        if (isCommentedLine(countryLine)) {
            return null;
        }
        String[] attributes = countryLine.split(",");
        try {
            if (attributes.length != 3) {
                throw new IllegalArgumentException("Invalid number of attributes");
            }
            String code = attributes[0].trim();
            if (!countryCodes.contains(code)) {
                throw new CountryFmtException("Country code " + code + " is unknown and therefore rejected.", countryLine);
            }
            String name = attributes[1];
            double area = Double.parseDouble(attributes[2]);
            return new Country(code, name, area);
        } catch (Exception e) {
            String message = "Problem parsing country line: " + countryLine;
            throw new CountryFmtException(message, countryLine, e);
        }
    }
}