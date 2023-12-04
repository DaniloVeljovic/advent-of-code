import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {

    static class Card {
        int id;
        List<Integer> winningNumbers;
        List<Integer> obtainedNumbers;

        Card(int id) {
            this.id = id;
            this.winningNumbers = new ArrayList<>();
            this.obtainedNumbers = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "Card{" +
                    "id=" + id +
                    ", winningNumbers=" + winningNumbers +
                    ", obtainedNumbers=" + obtainedNumbers +
                    '}';
        }
    }

    public static void main(String[] args) {
        int result = countCards();
        System.out.println("The result is: " + result);
    }

    private static int countCards() {
        List<Card> cards = readInput();

        int sum = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for(Card card : cards) {
            int matches = countMatches(card);
            //Part 1
/*            if(matches != 0) {
                sum += 1 << (matches - 1);
            }*/

            //1. calculate the current sum
            int totalCount = 0;
            if(map.containsKey(card.id)) {
                totalCount = map.get(card.id);
            }

            sum += totalCount + 1;
            //2. update the next sum
            for(int j = 0; j < totalCount + 1; j++) {
                for(int i = card.id + 1; i <= card.id + matches; i++) {
                    map.put(i, map.getOrDefault(i, 0) + 1);
                }
            }
        }

        return sum;
    }

    private static int countMatches(Card card) {
        int matches = 0;
        for(int i = 0; i < card.obtainedNumbers.size(); i++) {
            Integer obtainedNumber = card.obtainedNumbers.get(i);
            for(int j = 0; j < card.winningNumbers.size(); j++) {
                Integer winningNumber = card.winningNumbers.get(j);
                if(Objects.equals(obtainedNumber, winningNumber)) {
                    matches++;
                    break;
                }
            }
        }
        return matches;
    }

    private static List<Card> readInput() {
        try(InputStream fileIs = Day4.class.getResourceAsStream("day_4.txt")) {
            List<Card> cards = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileIs));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String id = line.substring(line.indexOf(" "), line.indexOf(":")).trim();
                Card card = new Card(Integer.parseInt(id));
                String winningNumbers = line.substring(line.indexOf(":") + 1, line.indexOf("|")).trim();
                Pattern p = Pattern.compile("[0-9]+");
                Matcher m = p.matcher(winningNumbers);
                while (m.find()) {
                    int n = Integer.parseInt(m.group());
                    card.winningNumbers.add(n);
                }

                String obtainedNumbers = line.substring(line.indexOf("|") + 1).trim();
                Matcher m1 = p.matcher(obtainedNumbers);
                while (m1.find()) {
                    int n = Integer.parseInt(m1.group());
                    card.obtainedNumbers.add(n);
                }
                cards.add(card);

            }
            System.out.println(cards);
            return cards;
        }
        catch (IOException exception) {
            throw new RuntimeException();
        }
    }


}
