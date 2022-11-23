import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        try {

            //to be sure that the user will provide input & output file & the value of N
            if (args != null && args.length == 3) {
                String inputFile = args[0];
                String outputFile = args[1];
                int valueOfN = Integer.parseInt(args[2]);


                InputOutputFile file = new InputOutputFile();
                Reflection reflection = new Reflection(valueOfN);

                //"providedClasses" is a list with all the classes found in the input file
                List<String> providedClasses = file.readClassesFromInputFile(inputFile);

                if (providedClasses.size() != 0) {

                    //"aboutX" is a list with the first N classes with the most X elements (e.g. fields)

                    List<String> aboutDeclaredFields = reflection.havingMostDeclaredFields(providedClasses);

                    List<String> aboutFields = reflection.havingMostFields(providedClasses);

                    List<String> aboutDeclaredMethods = reflection.havingMostDeclaredMethods(providedClasses);

                    List<String> aboutMethods = reflection.havingMostMethods(providedClasses);

                    List<String> aboutSubTypes = reflection.havingMostSubTypes(providedClasses);

                    List<String> aboutSuperTypes = reflection.havingMostSuperTypes(providedClasses);

                    //prints the above info in the output file, which name was given from the user
                    file.createOutputFile
                            (outputFile,aboutDeclaredFields,aboutFields,aboutDeclaredMethods, aboutMethods,
                                    aboutSubTypes,aboutSuperTypes);

                } else {
                    System.out.println("No classes found in the input file.");
                }

            } else {
                System.out.println("Please provide the required info in this order : Input file - Output file - Value of N");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("The class declared in the input file was not found in the classpath.");
        }

    }


}

