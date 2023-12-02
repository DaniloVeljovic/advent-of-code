import com.sun.source.tree.NewArrayTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {

    static class Bags {
        int numOfRed;
        int numOfBlue;
        int numOfGreen;

        public Bags(int numOfBlue, int numOfRed, int numOfGreen) {
            this.numOfBlue = numOfBlue;
            this.numOfGreen = numOfGreen;
            this.numOfRed = numOfRed;
        }
    }

    static class Game {
        int id;
        List<Bags> bags;

        Game(Integer id) {
            this.id = id;
            bags = new ArrayList<>();
        }

        void addBag(int numOfBlue, int numOfRed, int numOfGreen) {
            this.bags.add(new Bags(numOfBlue, numOfRed,numOfGreen));
        }
    }

    public static void main(String[] args) {
        int sumOfPossible = findSumOfPossibleGames();
        System.out.println("The result is " + sumOfPossible);
    }

    private static int findSumOfPossibleGames() {
        int sum = 0;
        InputStream fileIs = Day2.class.getResourceAsStream("./day_2.txt");
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileIs))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Game game = parseToGame(line.trim());
                Bags minimumBag = isPossible(game);
                sum += minimumBag.numOfBlue * minimumBag.numOfGreen * minimumBag.numOfRed;
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }

        return sum;
    }

    private static Bags isPossible(Game game) {
        Bags minimumBag = new Bags(0, 0, 0);

        for (Bags bag : game.bags) {
            minimumBag.numOfGreen = Math.max(minimumBag.numOfGreen,bag.numOfGreen);
            minimumBag.numOfBlue = Math.max(minimumBag.numOfBlue,bag.numOfBlue);
            minimumBag.numOfRed = Math.max(minimumBag.numOfRed,bag.numOfRed);
        }

        return minimumBag;
    }

    private static Game parseToGame(String line) {
        String gameId = line.substring(5, line.indexOf(":"));
        Game game = new Game(Integer.valueOf(gameId));

        String[] bags = line.substring(line.indexOf(":") + 1).trim().split(";");
        for(String bag : bags) {
            bag = bag.trim();
            int currentBlue = 0, currentRed = 0, currentGreen = 0;
            for (String s : bag.split(",")) {
                s = s.trim();
                if(s.contains("blue")) {
                    currentBlue = Integer.parseInt(s.substring(0, s.indexOf(" ")));
                }
                if(s.contains("red")) {
                    currentRed = Integer.parseInt(s.substring(0, s.indexOf(" ")));
                }
                if(s.contains("green")) {
                    currentGreen = Integer.parseInt(s.substring(0, s.indexOf(" ")));
                }
            }
            game.addBag(currentBlue, currentRed, currentGreen);
        }
        return game;
    }
}
