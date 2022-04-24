/* E/17/207 - Marasinghe MAPG */

public class VitalPrinter {
    /**
     * Prints the vital details sent from a vital monitor to stdout
     * @param details
     *
     * this class is used to let the threads access the instance of the class
     * synchronously
     */
    public void printVitalDetails(String details) {
        System.out.println(details);
    }
}
