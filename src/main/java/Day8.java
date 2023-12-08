import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Day8 {

    static class Node {
        String left;
        String right;

        public Node(String left, String right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "left='" + left + '\'' +
                    ", right='" + right + '\'' +
                    '}';
        }
    }

    static class Navigation {
        String direction;
        Map<String, Node> navigationMap;

        public Navigation(String direction) {
            this.direction = direction;
            this.navigationMap = new HashMap<>();
        }

        @Override
        public String toString() {
            return "Navigation{" +
                    "direction='" + direction + '\'' +
                    ", navigationMap=" + navigationMap +
                    '}';
        }
    }

    public static void main(String[] args) {
        Navigation navigation = parseNavigation();
        long count = findPathGhosts(navigation);
        System.out.println("The result is " + count);
    }

    private static long findPathGhosts(Navigation navigation) {
        String target = "Z";
        List<Long> keys = navigation.navigationMap.keySet().stream()
                .filter(key -> key.matches(".*A$"))
                .map(e -> findPath(navigation, e, isLastCharacterNotEqualToTarget()))
                .toList();
        return leastCommonMultiple(keys);
    }

    private static long greatestCommonDivisor(long number1, long number2) {
        return 0 == number2 ? number1 : greatestCommonDivisor(number2, number1 % number2);
    }

    private static long leastCommonMultiple(long number1, long number2) {
        return (number1 * number2) / greatestCommonDivisor(number1, number2);
    }
    public static long leastCommonMultiple(List<Long> numbers) {
        return numbers.stream()
                .reduce(1L, Day8::leastCommonMultiple);
    }

    private static long findPath(Navigation navigation, String key, Predicate<String> predicate) {
        return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(
                                new KeyIteratorThroughNavigation(navigation,key, predicate),
                                Spliterator.IMMUTABLE), false)
                .count();
    }

    private static Predicate<String> isLastCharacterNotEqualToTarget() {
        return key -> !key.matches(".*Z$");
    }

    private static class KeyIteratorThroughNavigation implements Iterator<String> {
        private final Predicate<String> predicate;
        private int counter;
        private String currentKey;
        private Navigation navigation;

        public KeyIteratorThroughNavigation(Navigation navigation, String startingKey, Predicate<String> predicate) {
            this.counter = 0;
            this.currentKey = startingKey;
            this.predicate = predicate;
            this.navigation = navigation;
        }

        @Override
        public boolean hasNext() {
            return predicate.test(currentKey);
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node currentNodeElement = navigation.navigationMap.get(currentKey);
            Character currentInstruction = currentInstruction(navigation.direction, counter);

            if(currentInstruction == 'R') {
                currentKey = currentNodeElement.right;
            } else {
                currentKey = currentNodeElement.left;
            }
            counter++;
            return currentKey;
        }
        private Character currentInstruction(String navigation, int i) {
            return navigation.charAt(i % navigation.length());
        }
    }

    private static Navigation parseNavigation() {
        try(var inputStream = Day8.class.getResourceAsStream("day_8.txt")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String directions = bufferedReader.readLine();
            Navigation navigation = new Navigation(directions.trim());
            bufferedReader.readLine();

            String line;
            while((line = bufferedReader.readLine()) != null) {
                Pattern pattern = Pattern.compile("([^=]+)\\s*=\\s*\\(([^,]+),\\s*([^)]+)\\)");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String string1 = matcher.group(1).trim();
                    String string2 = matcher.group(2).trim();
                    String string3 = matcher.group(3).trim();

                    navigation.navigationMap.put(string1, new Node(string2, string3));
                }
            }
            return navigation;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
