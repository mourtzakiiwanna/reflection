import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** This class contains the basic functionalities regarding the file management
(classes reading from input file, file creation and results printing on the file) **/

public class InputOutputFile {

    //read the classes from the input file
    public List<String> readClassesFromInputFile(String inputFileName) throws FileNotFoundException {

        List<String> providedClasses = new ArrayList<>();
        Scanner scanner = new Scanner(new File(inputFileName));
        while (scanner.hasNextLine()) {
            providedClasses.add(scanner.nextLine());
        }
        scanner.close();
        return providedClasses;
    }

    //creates a file with a name given from the user
    public void createOutputFile(String fileName, List<String> declaredFields, List<String> fields,
                                 List<String> declaredMethods, List<String> methods, List<String> subTypes,
                                 List<String> superTypes) throws IOException {

        PrintWriter file = new PrintWriter(fileName);

        file.println("1a: " + String.join(",", declaredFields));
        file.println("1b: " + String.join(",", fields));
        file.println("2a: " + String.join(",", declaredMethods));
        file.println("2b: " + String.join(",", methods));
        file.println("3: " + String.join(",", subTypes));
        file.println("4: " + String.join(",", superTypes));

        file.close();
    }

}
