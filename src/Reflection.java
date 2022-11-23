import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/** This class contains the basic methods of the reflection project **/

public class Reflection {

    public int valueOfN;

    public Reflection(int N) { valueOfN = N; }

    Functionalities functionalities = new Functionalities();

    //Declared Fields
    public List<String> havingMostDeclaredFields(List<String> providedClasses) throws ClassNotFoundException {

        //"unsortedMap" is a map that will save the name of the classes and the sum of their declared fields
        Map<String,Integer> unsortedMap = new HashMap<>();
        for (String className : providedClasses) {
            Class<?> c = Class.forName(className);
            Field[] fields = c.getDeclaredFields();
            int sumOfFields = fields.length;
            unsortedMap.put(className,sumOfFields);
        }

        //returns the first N classes with the most declared fields
        return functionalities.sortAndLimit(unsortedMap,valueOfN);
    }

    //Declared and Inherited Fields
    public List<String> havingMostFields(List<String> providedClasses) throws ClassNotFoundException {

        //"unsortedMap" is a map that will save the name of the classes and the sum of their fields (declared & inherited)
        Map<String,Integer> unsortedMap = new HashMap<>();
        for (String className : providedClasses) {
            Class<?> c = Class.forName(className);

            //"declaredFields" are the fields that are declared in the specific class (public,protected,private)
            Field[] declaredFields = c.getDeclaredFields();
            List<Field> declaredFieldsList = Arrays.asList(declaredFields);

            //"otherFields" are all the public fields up the entire class hierarchy
            Field[] otherFields= c.getFields();
            List<Field> otherFieldsList = Arrays.asList(otherFields);

            //below we find all the superclasses' declared fields
            //because the getFields() method only returns the public fields, and we also want the protected ones
            List<Class<?>> superTypes = functionalities.findSuperTypes(c);

            //"inheritedFieldsList" is a list with all the fields inherited from the superclasses
            List<Field> inheritedFieldsList = new LinkedList<>();
            for (Class<?> sClass : superTypes) {
                Field[] superClassFields = sClass.getDeclaredFields();
                List<Field> superClassFieldsList = new LinkedList<>(Arrays.asList(superClassFields));
                inheritedFieldsList.addAll(superClassFieldsList);
            }

            inheritedFieldsList.addAll(otherFieldsList);

            //"private" fields are not inherited, so we must remove them from the list
            List<Field> fieldsToRemove = new ArrayList<>();
            for (Field field : inheritedFieldsList) {
                String modifier = Modifier.toString(field.getModifiers());
                if (modifier.contains("private")) {
                    fieldsToRemove.add(field);
                }
            }

            inheritedFieldsList.removeAll(fieldsToRemove);

            //convert lists to set in order to remove duplicates
            Set<Field> allFields = new LinkedHashSet<>(declaredFieldsList);
            allFields.addAll(inheritedFieldsList);

            int sumOfFields = allFields.size();
            unsortedMap.put(className,sumOfFields);
        }

        //returns the first N classes with the most fields
        return functionalities.sortAndLimit(unsortedMap, valueOfN);
    }

    //Declared Methods
    public List<String> havingMostDeclaredMethods(List<String> providedClasses) throws ClassNotFoundException {

        //"unsortedMap" is a map that will save the name of the classes and the sum of their declared methods
        Map<String,Integer> unsortedMap = new HashMap<>();
        for (String className : providedClasses) {
            Class<?> c = Class.forName(className);
            Method[] methods = c.getDeclaredMethods();
            int sumOfMethods = methods.length;
            unsortedMap.put(className,sumOfMethods);
        }

        //returns the first N classes with the most declared methods
        return functionalities.sortAndLimit(unsortedMap, valueOfN);
    }

    //Methods (declared and inherited)
    public List<String> havingMostMethods(List<String> providedClasses) throws ClassNotFoundException {

        //"unsortedMap" is a map that will save the name of the classes and the sum of their methods (declared & inherited)
        Map<String,Integer> unsortedMap = new HashMap<>();
        for (String className : providedClasses) {
            Class<?> c = Class.forName(className);

            //"declaredMethods" are the methods that are declared in the specific class (public,protected,private)
            Method[] declaredMethods = c.getDeclaredMethods();
            List<Method> declaredMethodsList = Arrays.asList(declaredMethods);

            //below we find all the superclasses' declared methods
            //because the getMethods() method only returns the public methods, and we also want the protected ones
            List<Class<?>> superClasses = functionalities.findSuperTypes(c);

            //"inheritedMethodsList" is a list with all the methods inherited from the superclasses
            List<Method> inheritedMethodsList = new LinkedList<>();
            for (Class<?> sClass : superClasses) {
                Method[] superClassMethods = sClass.getDeclaredMethods();
                List<Method> superClassMethodsList = new LinkedList<>(Arrays.asList(superClassMethods));
                inheritedMethodsList.addAll(superClassMethodsList);
            }

            /* 1. "private" methods are not inherited,
             2. if a type is not an interface, the abstract methods are already declared in the (parent) class (declared as @Override)
             so we must remove them from the list */

            List<Method> methodsToRemove = new ArrayList<>();
            for (Method method : inheritedMethodsList) {
                String modifier = Modifier.toString(method.getModifiers());
                if (modifier.contains("private") || (!c.isInterface() && Modifier.isAbstract(method.getModifiers()))) {
                    methodsToRemove.add(method);
                }
            }

            inheritedMethodsList.removeAll(methodsToRemove);

            //convert lists to set in order to remove duplicates
            Set<Method> set = new LinkedHashSet<>(declaredMethodsList);
            set.addAll(inheritedMethodsList);

            int sumOfMethods = set.size();
            unsortedMap.put(className, sumOfMethods);
        }

        //returns the first N classes with the most methods
        return functionalities.sortAndLimit(unsortedMap, valueOfN);

    }

    //Superclasses(up to java.lang.Object) and interfaces
    public List<String> havingMostSuperTypes(List<String> providedClasses) throws ClassNotFoundException {

        // "unsortedMap" is a map that will save the name of the classes and the sum of their supertypes
        Map<String,Integer> unsortedMap = new HashMap<>();
        for (String className : providedClasses) {
            Class<?> c = Class.forName(className);
            ArrayList<Class<?>> superClasses = functionalities.findSuperTypes(c);
            int sumOfSuperTypes = superClasses.size();
            unsortedMap.put(className,sumOfSuperTypes );
        }

        //returns the first N classes with the most supertypes
        return functionalities.sortAndLimit(unsortedMap, valueOfN);
    }

    /* To find the subtypes of a class, we first find the supertypes of all the classes, and save them in a map.
    superTypes = {nameOfClass, superTypes []}
    Then based on the superTypes map we will create a subTypes map, which will save the name of the class and
    their subTypes. (We look for the class' name in the values of the superTypes map)
    subTypes = {nameOfClass, subTypes []} */
    public List<String> havingMostSubTypes(List<String> providedClasses) throws ClassNotFoundException {

        Map<String,List<String>> superTypes = new HashMap<>();

        //we find all the supertypes of the provided classes (interfaces & supertypes)
        for (String className : providedClasses) {
            Class<?> c = Class.forName(className);
            ArrayList<Class<?>> allClasses = functionalities.findSuperTypes(c);
            Class<?>[] classArray = allClasses.toArray(new Class[0]);

            //"classStrings" list contains the names of the class' superTypes as Strings
            List<String> classStrings = new ArrayList<>();
            for (Class<?> m: classArray) {
                String ClassStr = functionalities.mapClassToString(m);
                classStrings.add(ClassStr);
            }
            superTypes.put(className, classStrings);
        }

        Map<String, Integer> subTypes = new HashMap<>();

        //"outsideEntry" will be used to iterate the map of the "superTypes"
        for (Map.Entry<String, List<String>> outsideEntry : superTypes.entrySet()) {
            String nameOfClass = outsideEntry.getKey();
            int subClasses = 0;

            //"insideEntry" will be used to iterate again the map of the "superTypes"
            for (Map.Entry<String, List<String>> insideEntry : superTypes.entrySet()) {
                List<String> classes = insideEntry.getValue();
                for (String sClass : classes) {
                    //if the class name is found in the value of "superTypes" as superType of a class
                    if (nameOfClass.equals(sClass)) {
                        subClasses++;
                    }
                }
            }

            subTypes.put(nameOfClass, subClasses);
        }

        //returns the first N classes with the most subtypes
        return functionalities.sortAndLimit(subTypes, valueOfN);
    }

}
