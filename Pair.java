/***********************************
 * Course: Lehigh CSE017-SU2024
 * Assignment: Final (v01)
 * Name: Yinglong Lin
 * UID: yile22
 * **********************************/

public class Pair<E1, E2> {
    private E1 first;
    private E2 second;

    /** No-arg constructor initializes data members to null */
    public Pair() {
        first = null;
        second = null;
    }

    /** Two-arg */
    public Pair(E1 f, E2 s) {
        // first = (E1)f.clone(); // deep copy?
        first = f; // shallow copy
        second = s;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(")
                .append(first)
                .append(", ")
                .append(second)
                .append(")");
        // return "("+first + "," + second+")";
        return sb.toString();
    }

    // setters and getters
    public E1 getFirst() {
        return first;
    }

    public E2 getSecond() {
        return second;
    }

    public void setFirst(E1 f) {
        first = f;
    }

    public void setSecond(E2 s) {
        second = s;
    }

    /**
     * Returns true iff the first and second of the two pairs are .equals
     * Note: For pairs of strings, the comparison IS case sensitive
     */
    @Override
    public boolean equals(Object obj) { // throws ClassCastException{
        if (obj instanceof Pair) {
            Pair p = (Pair) obj; // downcast
            // Pair<E1, E2> p = (Pair<E1, E2>) obj; //downcast
            return this.first.equals(p.first) && this.second.equals(p.second);
        }
        return false;
    }

    public static void main(String[] args) {
        Pair pairNoArgNoGeneric = new Pair();
        Pair<String, String> pairNoArg = new Pair<>();
        System.out.println("No arg no generic pair: " + pairNoArgNoGeneric);
        System.out.println("No arg pair (of strings): " + pairNoArg);

        Pair<String, String> pairTwoArg = new Pair<>("one", "two");
        System.out.println("Two arg pair (of strings): " + pairTwoArg);

        // test setters
        pairTwoArg.setFirst("red");
        pairTwoArg.setSecond("blue");
        System.out.println("Two arg pair (of strings): " + pairTwoArg);

        // test equals
        Double doub = 3.14;
        System.out.println("pairTwoArg.equals(doub):" + pairTwoArg.equals(doub));
        Pair<String, String> newPairStrings = new Pair<>("red", "BLUE");
        System.out.println("pairTwoArg.equals(newPairStrings):" + pairTwoArg.equals(newPairStrings));

        // try pair with arraylist
        java.util.ArrayList<Pair<Integer, Double>> al = new java.util.ArrayList<>();
        Pair<Integer, Double> sillyPair = new Pair<>(1, 1.0);
        al.add(sillyPair);
        al.add(new Pair<Integer, Double>(2, 2.0));
        // al.add( "silly"); // will not work
        System.out.println("sillyPair: " + sillyPair);
        System.out.println("al: " + al);
    }
}
