import java.util.*;
import java.util.stream.Collectors;

/** This class contains some supportive functionalities (utilities)
 (sort and limit a map based on value of N, find the supertypes of a class, return the name of a Class Object) **/

public class Functionalities {

    //sorts the map (className, sumOfX) based on the sumOfX, find the first N elements and returns only the keys (classNames)
    public List<String> sortAndLimit(Map<String,Integer> unsortedMap, int valueOfN) {

        //copy all data into LinkedHashMap in order to sort the values
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        unsortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        //return only the names of the classes (keys) of the sorted map
        ArrayList<String> sorted = new ArrayList<>(sortedMap.keySet());

        //find the sublist of the classes based on the value of N
        return sorted.stream().limit(valueOfN).collect(Collectors.toList());

    }

    //finds the supertypes of a Class
    public ArrayList<Class<?>> findSuperTypes(Class<?> c)  {

        ArrayList<Class<?>> allClasses;
        Class<?>[] interfaces = c.getInterfaces();
        List<Class<?>> interfacesOfClass = Arrays.asList(interfaces);

        //to identify all superclasses of a class, we'll call getSuperclass iteratively until it returns null (java.lang.Object)
        Class<?> superClass = c.getSuperclass();

        List<Class<?>> superClasses = new LinkedList<>();
        while (superClass != null) {
            superClasses.add(superClass);
            c = superClass;
            superClass = c.getSuperclass();
        }
        
        //find the interfaces of all the superclasses
        List<Class<?>> interfacesOfSuperclasses = new LinkedList<>();
        for (Class<?> sClass : superClasses) {
            List<Class<?>> superclassInterfaces = List.of(sClass.getInterfaces());
            interfacesOfSuperclasses.addAll(superclassInterfaces);
        }

        //merge the interfaces of the class and the interfaces of the superclasses
        Set<Class<?>> interfaceSet = new LinkedHashSet<>(interfacesOfSuperclasses);
        interfaceSet.addAll(interfacesOfClass);

        //find the supertypes of all the interfaces
        Set<Class<?>> allInterfaces = new LinkedHashSet<>();
        for (Class<?> interFace : interfaceSet) {
            Class<?>[] interfacesOfInterface = interFace.getInterfaces();
            List<Class<?>> interfacesOfInterfaceList = Arrays.asList(interfacesOfInterface);
            allInterfaces.addAll(interfacesOfInterfaceList);
        }

        Set<Class<?>> allSuperTypes = new LinkedHashSet<>(interfaceSet);
        allSuperTypes.addAll(allInterfaces);
        allSuperTypes.addAll(superClasses);

        allClasses = new ArrayList<>(allSuperTypes);
        return allClasses;

    }

    //returns the name of a Class Object as a String
    public String mapClassToString(Class<?> m) {
        return m.getName();
    }

}
