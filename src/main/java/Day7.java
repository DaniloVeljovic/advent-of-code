import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day7 {

    enum Type {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    static class Hand implements Comparable<Hand> {
        int bid;
        String originalCards;
        Type type;

        public Hand(int bid, String originalCards) {
            this.bid = bid;
            this.originalCards = originalCards;
            this.type = evaluateType();
        }

        int resolveIndex(Character card) {
            if ('A' == card) {
                return 12;
            } else if ('K' == card) {
                return 11;
            } else if ('Q' == card) {
                return 10;
            } else if ('J' == card) {
                return 9;
            } else if ('T' == card) {
                return 8;
            } return (Character.getNumericValue(card) - 2);
        }

        Type evaluateType() {
            int[] cards = new int[13];
            for(Character card : originalCards.toCharArray()) {
                cards[resolveIndex(card)]++;
            }

            int pairCount = 0;

            int jokerCount = cards[9];

            boolean hasFourSameCards = false;
            boolean hastThreeSameCards = false;

            for (int i = 0; i < 13; i++) {
                if(i == 9) continue;
                if (cards[i] == 5) {
                    return Type.FIVE_OF_A_KIND;
                }
                if (cards[i] == 4) {
                    hasFourSameCards = true;
                }
                if (cards[i] == 3) {
                    hastThreeSameCards = true;
                }
                if (cards[i] == 2) {
                    pairCount++;
                }
            }

            if(jokerCount == 5) {
                return Type.FIVE_OF_A_KIND;
            }

            if(hasFourSameCards) {
                if(jokerCount == 1) {
                    return Type.FIVE_OF_A_KIND;
                } else {
                    return Type.FOUR_OF_A_KIND;
                }
            }

            if(hastThreeSameCards) {
                if(jokerCount == 2) {
                    return Type.FIVE_OF_A_KIND;
                }
                if(jokerCount == 1) {
                    return Type.FOUR_OF_A_KIND;
                }
                if(pairCount == 1) {
                    return Type.FULL_HOUSE;
                }
                else return Type.THREE_OF_A_KIND;
            }

            if (pairCount == 0) {
                if(jokerCount == 4) {
                    return Type.FIVE_OF_A_KIND;
                } else if(jokerCount == 3) {
                    return Type.FOUR_OF_A_KIND;
                } else if (jokerCount == 2) {
                    return Type.THREE_OF_A_KIND;
                } else if(jokerCount == 1) {
                    return Type.ONE_PAIR;
                }
                return Type.HIGH_CARD;
            }
            if (pairCount == 1) {
                if(jokerCount == 3) {
                    return Type.FIVE_OF_A_KIND;
                } else if (jokerCount == 2) {
                    return Type.FOUR_OF_A_KIND;
                } else if (jokerCount == 1) {
                    return Type.THREE_OF_A_KIND;
                }
                return Type.ONE_PAIR;
            } else {
                if (jokerCount == 1) {
                    return Type.FULL_HOUSE;
                }
                return Type.TWO_PAIR;
            }
        }

        @Override
        public int compareTo(Hand other) {
            int typeComparison = this.evaluateType().compareTo(other.evaluateType());
            if (typeComparison != 0) {
                return typeComparison;
            }

            for (int i = 0; i <= 4; i++) {
                int thisCardIndex = this.originalCards.charAt(i) == 'J' ? -1 : resolveIndex(this.originalCards.charAt(i));
                int otherCardIndex = other.originalCards.charAt(i) == 'J' ? -1 : resolveIndex(other.originalCards.charAt(i));;
                if (thisCardIndex > otherCardIndex) {
                    return -1;
                } else if (thisCardIndex < otherCardIndex) {
                    return 1;
                }
            }

            throw new RuntimeException();
        }

        @Override
        public String toString() {
            return "Hand{" +
                    "bid=" + bid +
                    ", originalCards='" + originalCards + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<Hand> hands = parseHands();
        Collections.sort(hands);

        for(Hand hand : hands) {
            System.out.println(hand);
        }

        int factor = hands.size();
        int sum = 0;

        for(Hand hand : hands) {
            sum += hand.bid * factor--;
        }

        System.out.println("The result is " + sum);
    }

    private static List<Hand> parseHands() {
        List<Hand> hands = new ArrayList<>();
        try(InputStream inputStream = Day7.class.getResourceAsStream("day_7.txt")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                String cards = line.substring(0, line.indexOf(" "));
                String bid = line.substring(line.indexOf(" ") + 1);
                Hand hand = new Hand(Integer.parseInt(bid), cards);
                hands.add(hand);
            }
            return hands;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
