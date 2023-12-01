import javax.xml.stream.events.Characters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 {

    public static Map<String, Character> map = Map.of(
            "one", '1',
            "two", '2',
            "three", '3',
            "four", '4',
            "five", '5',
            "six", '6',
            "seven", '7',
            "eight", '8',
            "nine", '9'
    );

    public static void main(String[] args) {
        int result = trebuchet();
        System.out.println("The result is: " + result);

    }

    public static int trebuchet() {
        final List<Integer> values = new ArrayList<>();
        InputStream inputStream = Day1.class.getResourceAsStream("/day_1.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                int value = retrieveNumber(line.trim());
                values.add(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return values.stream().reduce(0, Integer::sum);
    }

    private static int retrieveNumber(final String line) {
        int left = 0, right = line.length();
        Character leftChar = null, rightChar = null;

        while(left <= right) {

            if(leftChar == null) {
                if(Character.isDigit(line.charAt(left))) {
                    leftChar = line.charAt(left);
                } else {
                    int i = 3;
                    while(i < 6 && left + i < line.length()) {
                        Character maybeDigit = extractDigitIfPossible(line.substring(left, left + i));
                        if(maybeDigit != null) {
                            leftChar = maybeDigit;
                            break;
                        }
                        else
                            i++;
                    }
                }
                if(leftChar == null) left++;
            }

            if(rightChar == null) {
                if(Character.isDigit(line.charAt(right - 1))) {
                    rightChar = line.charAt(right - 1);
                } else {
                    int i = 3;
                    while(i < 6 && right - i > 0) {
                        Character maybeDigit = extractDigitIfPossible(line.substring(right - i, right));
                        if(maybeDigit != null) {
                            rightChar = maybeDigit;
                            break;
                        }
                        else
                            i++;
                    }
                }
                if(rightChar == null) right--;
            }

            if(rightChar != null && leftChar != null) {
                StringBuilder stringBuilder = new StringBuilder();
                String meanResult = stringBuilder.append(leftChar).append(rightChar).toString();
                System.out.println(meanResult);
                return Integer.parseInt(meanResult);
            }
        }

        return 0;
    }

    private static Character extractDigitIfPossible(String line) {
        Character character = null;
            if(map.containsKey(line)) {
                return map.get(line);
        }
        return character;
    }
}
