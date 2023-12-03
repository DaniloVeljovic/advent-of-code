import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3Part2 {

    static class Number {
        Integer number;
        Position numberPosition;
        Position starPosition;

        public Number(Integer number, Position numberPosition) {
            this.number = number;
            this.numberPosition = numberPosition;
        }

        @Override
        public String toString() {
            return "Number{" +
                    "number=" + number +
                    ", numberPosition=" + numberPosition +
                    ", starPosition=" + starPosition +
                    '}';
        }
    }

    static class Position {
        Integer row;
        List<Integer> cols;

        Position(int row) {
            this.row = row;
            this.cols = new ArrayList<>();
        }

        void addCols(int col) {
            this.cols.add(col);
        }

        @Override
        public String toString() {
            return "Position {" +
                    "row=" + row +
                    ", cols=" + cols +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return row.equals(position.row) && cols.equals(position.cols);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, cols);
        }
    }

    public static void main(String[] args) {
        int sum = countParts();
        System.out.println("The result is: " + sum);
    }

    private static int countParts() {
        List<String> matrix = getMatrix();
        List<Number> numbers = extractNumbersAndPositions(matrix);
        List<Number> filteredNumbers = numbers.stream().filter(number -> getValidElements(matrix, number)).toList();
        //System.out.println(filteredNumbers);
        Map<Position, List<Integer>> map = new HashMap<>();
        for(Number number : filteredNumbers) {
            if(!map.containsKey(number.starPosition)) {
                map.put(number.starPosition, new ArrayList<>());

            }
            map.get(number.starPosition).add(number.number);
        }
        int result = 0;
        for (Map.Entry<Position, List<Integer>> entry : map.entrySet()) {
            if(entry.getValue().size() == 2) {
                result += entry.getValue().stream().reduce(1, (a1, b1) -> a1 * b1);
            }
        }
        return result;
    }

    private static boolean getValidElements(List<String> matrix, Number number) {
        boolean hasSymbol = false;
        Integer row = number.numberPosition.row;
        Integer firstCol = number.numberPosition.cols.get(0);
        Integer lastCol = number.numberPosition.cols.get(1);

        for(int i = firstCol; i < lastCol; i++) {
            hasSymbol |= hasNonDotSymbolNearby(number, matrix, row, i);
        }

        return hasSymbol;
    }


    private static boolean hasNonDotSymbolNearby(Number number, List<String> lines, int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < lines.size() && j >= 0 && j < lines.get(i).length() &&
                        lines.get(i).charAt(j) == '*' && !(i == row && j == col)) {
                    number.starPosition = new Position(i);
                    number.starPosition.addCols(j);
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Number> extractNumbersAndPositions(List<String> matrix) {
        List<Number> numbers = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            String row = matrix.get(i);
            for(int j = 0; j < row.length(); j++) {
                if(Character.isDigit(row.charAt(j))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int p = j;
                    while(p < row.length() && Character.isDigit(row.charAt(p))) {
                        stringBuilder.append(row.charAt(p));
                        p++;
                    }
                    int lastIndex = p;
                    String number = stringBuilder.toString();
                    Position position = new Position(i);
                    position.addCols(j);
                    position.addCols(lastIndex);
                    numbers.add(new Number(Integer.parseInt(number), position));
                    j = p;
                }
            }
        }
        return numbers;
    }

    private static List<String> getMatrix() {
        List<String> matrix = new ArrayList<>();
        try(InputStream fileIs = Day3Part2.class.getResourceAsStream("day_3.txt")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fileIs));
            String line;
            while((line = br.readLine()) != null) {
                matrix.add(line);
            }

            return matrix;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
