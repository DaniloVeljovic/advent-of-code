import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

    static class Race {
        long time;
        long distance;

        public Race(long time, long distance) {
            this.time = time;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "Race {" +
                    "time=" + time +
                    ", distance=" + distance +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<Race> races = parseRaces();
        long result = 1;
        for (Race race : races) {
            long waysToWin = countWaysToWin(race);
            System.out.println(waysToWin);
            if(waysToWin > 0) {
                result *= waysToWin;
            }
        }

        System.out.println("Result is " + result);

    }

    private static long countWaysToWin(Race race) {
        long waysToWin = 0;
        for(long i = 0; i < race.time; i++) {
            long distance = calculateDistance(race.time, i);

            if(race.distance < distance) {
                waysToWin++;
            }
        }
        return waysToWin;
    }

    private static long calculateDistance(long totalTime, long holdTime) {
        return holdTime * (totalTime - holdTime);
    }

    private static List<Race> parseRaces() {
        List<Race> races = new ArrayList<>();

        try(InputStream inputStream = Day6.class.getResourceAsStream("day_6.txt")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String time = bufferedReader.readLine();
            Long times = parseLine(time.substring("Time:".length()).trim());

            String distance = bufferedReader.readLine();
            Long distances = parseLine(distance.substring("Distance:".length()).trim());

            races.add(new Race(times, distances));

            System.out.println(races);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return races;
    }

    private static Long parseLine(String line) {
        String[] parts = line.split("\\s+");
        StringBuilder stringBuilder = new StringBuilder();
        for (String part : parts) {
            stringBuilder.append(Long.parseLong(part));
        }
        return Long.parseLong(stringBuilder.toString());
    }
}
