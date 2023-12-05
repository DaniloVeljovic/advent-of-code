import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Day5 {

    static class Range {
        Long start;
        Long end;
        Long range;

        public Range(Long start, Long end, Long range) {
            this.start = start;
            this.end = end;
            this.range = range;
        }

        @Override
        public String toString() {
            return "Range{" +
                    "start=" + start +
                    ", end=" + end +
                    ", range=" + range +
                    '}';
        }
    }

    static class Almanac {
        List<Range> seeds;

        List<Range> seedToSoil;
        List<Range> soilToFertilizer;
        List<Range> fertilizerToWater;
        List<Range> waterToLight;
        List<Range> lightToTemperature;
        List<Range> temperatureToHumidity;
        List<Range> humidityToLocation;
    }

    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException {
        Almanac almanac = parseAlmanac("src/main/resources/day_5.txt");
        Long result  = findClosestLocationParallel(almanac);
        System.out.println("The result " + result);
    }

    private static long findClosestLocationParallel(Almanac almanac) throws InterruptedException, ExecutionException {
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        List<Callable<Long>> tasks = new ArrayList<>();
        for (Range seed : almanac.seeds) {

            tasks.add(() -> {
                System.out.println("Started range");
                long start = System.currentTimeMillis();
                long closestLocation = Long.MAX_VALUE;
                for (long i = seed.start; i <= seed.end; i++) {
                    closestLocation = Math.min(closestLocation, findLocation(almanac, i));
                }
                System.out.println("Finished range after " + (System.currentTimeMillis() - start));
                return closestLocation;
            });
        }

        List<Future<Long>> futures = executorService.invokeAll(tasks);

        executorService.shutdown();

        long overallClosestLocation = Long.MAX_VALUE;

        for (Future<Long> future : futures) {
            overallClosestLocation = Math.min(overallClosestLocation, future.get());
        }

        return overallClosestLocation;
    }

    private static Long findClosestLocation(Almanac almanac) throws FileNotFoundException {
        Long closestLocation = Long.MAX_VALUE;

        for(Range seed : almanac.seeds) {
            for(long i = seed.start; i <= seed.end; i++) {
                closestLocation = Math.min(closestLocation, findLocation(almanac, i));
            }
        }

        return closestLocation;
    }

    private static long findLocation(Almanac almanac, Long seed) {
        long soil = findValue(almanac.seedToSoil, seed);

        long fertilizer = findValue(almanac.soilToFertilizer, soil);

        long water = findValue(almanac.fertilizerToWater, fertilizer);

        long light = findValue(almanac.waterToLight, water);

        long temp = findValue(almanac.lightToTemperature, light);

        long humidity = findValue(almanac.temperatureToHumidity, temp);

        return findValue(almanac.humidityToLocation, humidity);
    }

    private static long findValue(List<Range> ranges, Long input) {
        for(Range range : ranges) {
            if(range.start <= input && input <= range.start + range.range) {
                return range.end + input - range.start;
            }
        }
        return input;
    }

    public static Almanac parseAlmanac(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        Almanac almanac = new Almanac();
        almanac.seeds = parseSeeds(scanner);
        scanner.nextLine();
        almanac.seedToSoil = parseRanges(scanner);
        almanac.soilToFertilizer = parseRanges(scanner);
        almanac.fertilizerToWater = parseRanges(scanner);
        almanac.waterToLight = parseRanges(scanner);
        almanac.lightToTemperature = parseRanges(scanner);
        almanac.temperatureToHumidity = parseRanges(scanner);
        almanac.humidityToLocation = parseRanges(scanner);

        scanner.close();
        return almanac;
    }

    private static List<Range> parseSeeds(Scanner scanner) {
        String line = scanner.nextLine().trim();
        if (!line.startsWith("seeds:")) {
            throw new IllegalArgumentException("Expected 'seeds:' line");
        }

        List<Long> seeds = parseLine(line.substring("seeds:".length()).trim());
        List<Range> ranges = new ArrayList<>();

        for(int i = 0; i <= seeds.size() - 2; i += 2) {
            ranges.add(new Range(seeds.get(i), seeds.get(i) + seeds.get(i + 1) - 1, 0l));
        }

        return ranges;
    }

    private static List<Long> parseLine(String line) {
        String[] parts = line.split("\\s+");
        List<Long> result = new ArrayList<>();
        for (String part : parts) {
            result.add(Long.parseLong(part));
        }
        return result;
    }

    private static List<Range> parseRanges(Scanner scanner) {
        List<Range> ranges = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                break;
            }

            if(!Character.isDigit(line.charAt(0))) {
                continue;
            }

            List<Long> values = parseLine(line);
            ranges.add(new Range(values.get(1), values.get(0), values.get(2)));
        }
        return ranges;
    }
}
