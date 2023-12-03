import com.sun.source.doctree.SeeTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day3 {

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
        Map<Position, Integer> map = extractNumbersAndPositions(matrix);
        return getValidElements(map).stream().reduce(0, Integer::sum);
    }

    private static List<Integer> getValidElements(Map<Position, Integer> map) {
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Position, Integer> entry : map.entrySet()) {
            boolean hasSymbol = false;
            Integer row = entry.getKey().row;
            Integer firstCol = entry.getKey().cols.get(0);
            Integer lastCol = entry.getKey().cols.get(1);

            for(int i = firstCol; i < lastCol; i++) {
                hasSymbol |= hasNonDotSymbolNearby(getMatrix(), row, i);
            }

            if(hasSymbol) {
                result.add(entry.getValue());
            }
        }

        return result;
    }


    private static boolean hasNonDotSymbolNearby(List<String> lines, int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < lines.size() && j >= 0 && j < lines.get(i).length() &&
                        lines.get(i).charAt(j) == '.' && !Character.isDigit(lines.get(i).charAt(j)) && !(i == row && j == col)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Map<Position, Integer> extractNumbersAndPositions(List<String> matrix) {
        Map<Position, Integer> numbers = new LinkedHashMap<>();
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
                    numbers.put(position, Integer.parseInt(number));
                    j = p;
                }
            }
        }
        return numbers;
    }

    private static List<String> getMatrix() {
        List<String> matrix = new ArrayList<>();
        try(InputStream fileIs = Day3.class.getResourceAsStream("day_3.txt")) {
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
