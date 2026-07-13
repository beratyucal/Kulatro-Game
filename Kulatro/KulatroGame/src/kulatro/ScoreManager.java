package kulatro;

import kulatro.alchemy.Catalyst;
import java.util.ArrayList;
import java.util.List;
import kulatro.alchemy.ElementalFusion;
import kulatro.element.ElectronBond;
import kulatro.quantum.QuantumEntanglement;
import kulatro.quantum.Superposition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreManager {

    // Main entry point - calculates score based on hand and active special card
    public int scoreCards(List<Card> cards, SpecialCard specialCard) {
        if (cards == null || cards.isEmpty()) return 0;

        List<NumericCard> numericCards = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof NumericCard) {
                numericCards.add((NumericCard) card);
            }
        }
        if (numericCards.isEmpty()) return 0;

        // Elemental Fusion treats a matching type as four of a kind for scoring.
        if (specialCard instanceof ElementalFusion
                && ((ElementalFusion) specialCard).isActive()) {
            Map<String, List<NumericCard>> typeMap = splitCardsByType(numericCards);
            if (containsPair(typeMap)) {
                return sumEffectiveValues(numericCards, specialCard) * 10;
            }
        }

        // Apply Electron Bond transformation before scoring
        if (specialCard instanceof ElectronBond
                && ((ElectronBond) specialCard).isActive()) {
            numericCards = makeElectronBondedCards(numericCards, (ElectronBond) specialCard);
        }

        // Superposition - play scoring patterns and take the better result
        if (specialCard instanceof Superposition
                && ((Superposition) specialCard).isActive()) {
            return scoreSuperpositionChoice(numericCards, specialCard);
        }

        return scoreBestAvailablePattern(numericCards, specialCard);
    }

    // Calculates the Superposition result from the available scoring patterns.
    private int scoreSuperpositionChoice(List<NumericCard> cards, SpecialCard specialCard) {
        return findBestSuperpositionScore(cards, specialCard);
    }

    // Calculates the best score Superposition can use for numeric cards.
    private int findBestSuperpositionScore(List<NumericCard> cards,
                                                       SpecialCard specialCard) {
        int bestScore = sumEffectiveValues(cards, specialCard);

        // Four of a kind scoring pattern
        if (areAllSameType(cards)) {
            int fourOfAKindScore = sumEffectiveValues(cards, specialCard) * 10;
            bestScore = Math.max(bestScore, fourOfAKindScore);
        }

        // One of each scoring pattern
        if (areAllDifferentTypes(cards)) {
            int oneOfEachScore = sumEffectiveValues(cards, specialCard) * 5;
            bestScore = Math.max(bestScore, oneOfEachScore);
        }

        // Pair scoring pattern
        Map<String, List<NumericCard>> typeMap = splitCardsByType(cards);
        if (containsPair(typeMap)) {
            int pairScore = scoreBestPairOnly(typeMap, specialCard);
            bestScore = Math.max(bestScore, pairScore);
        }

        return bestScore;
    }

    // Normal pattern detection and scoring
    private int scoreBestAvailablePattern(List<NumericCard> cards, SpecialCard specialCard) {
        Map<String, List<NumericCard>> typeMap = splitCardsByType(cards);
        int totalCards = cards.size();
        int totalTypes = typeMap.size();

        // Four of a kind - all 4 cards same type: sum x10
        if (totalCards == 4 && totalTypes == 1) {
            return sumEffectiveValues(cards, specialCard) * 10;
        }
        // One of each type - all 4 different types: sum x5
        else if (totalCards == 4 && totalTypes == 4) {
            return sumEffectiveValues(cards, specialCard) * 5;
        }
        // Pair or other - check pairs: pair sum x2, rest normal
        else {
            return scoreBestPairOnly(typeMap, specialCard);
        }
    }

    // Four of a kind pattern: sum x10 (only valid if all 4 are same type)
    private int scoreFourMatchingTypes(List<NumericCard> cards, SpecialCard specialCard) {
        Map<String, List<NumericCard>> typeMap = splitCardsByType(cards);
        if (cards.size() == 4 && typeMap.size() == 1) {
            return sumEffectiveValues(cards, specialCard) * 10;
        }
        return 0;
    }

    // One of each type pattern: sum x5 (only valid if 4 different types)
    private int scoreOneOfEachType(List<NumericCard> cards, SpecialCard specialCard) {
        Map<String, List<NumericCard>> typeMap = splitCardsByType(cards);
        if (cards.size() == 4 && typeMap.size() == 4) {
            return sumEffectiveValues(cards, specialCard) * 5;
        }
        return 0;
    }

    // Pair scoring: pairs get x2 multiplier (x3 with Catalyst), singles are normal
    private int scoreBestPairOnly(Map<String, List<NumericCard>> typeMap,
                                   SpecialCard specialCard) {
        int pairMultiplier = 2;

        // Catalyst increases pair multiplier from x2 to x3
        if (specialCard instanceof Catalyst
                && ((Catalyst) specialCard).isActive()) {
            pairMultiplier = 3;
        }

        List<NumericCard> allCards = new ArrayList<>();
        for (List<NumericCard> group : typeMap.values()) {
            allCards.addAll(group);
        }

        int totalScore = sumEffectiveValues(allCards, specialCard);
        int bestScore = totalScore;

        for (List<NumericCard> group : typeMap.values()) {
            if (group.size() < 2) {
                continue;
            }

            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    int pairSum = effectiveCardValue(group.get(i), specialCard)
                            + effectiveCardValue(group.get(j), specialCard);
                    int score = pairSum * pairMultiplier + (totalScore - pairSum);
                    bestScore = Math.max(bestScore, score);
                }
            }
        }

        return bestScore;
    }

    // Electron Bond - changes one bonded card's type to match the other
    private List<NumericCard> makeElectronBondedCards(List<NumericCard> cards,
                                                 ElectronBond bond) {
        String type1 = bond.getBondedType1();
        String type2 = bond.getBondedType2();
        if (type1 == null || type2 == null) return cards;

        List<NumericCard> result = new ArrayList<>(cards);
        NumericCard bondCard2 = null;

        for (NumericCard nc : result) {
            if (nc.getType().equals(type2) && bondCard2 == null) {
                bondCard2 = nc;
            }
        }

        // Change type2 card's type to type1 so they form a pair
        if (bondCard2 != null) {
            result.remove(bondCard2);
            result.add(new NumericCard(type1, bondCard2.getValue()));
        }
        return result;
    }

    // Sum values with Noble Gas (x2 locked) and Quantum Entanglement (x3) effects
    private int sumEffectiveValues(List<NumericCard> cards, SpecialCard specialCard) {
        int sum = 0;
        for (NumericCard card : cards) {
            sum += effectiveCardValue(card, specialCard);
        }
        return sum;
    }

    // Returns a card value after active scoring effects are applied.
    private int effectiveCardValue(NumericCard card, SpecialCard specialCard) {
        int value = card.getValue();

        // Noble Gas - locked card value counts twice
        if (card.isLocked()) {
            value *= 2;
        }

        // Quantum Entanglement - selected card value counts triple
        if (specialCard instanceof QuantumEntanglement) {
            QuantumEntanglement qe = (QuantumEntanglement) specialCard;
            if (qe.isActive() && qe.getEntangledCard() != null
                    && qe.getEntangledCard().equals(card)) {
                value *= 3;
            }
        }

        return value;
    }

    // Simple sum with only Noble Gas effect
    private int sumRawValues(List<NumericCard> cards) {
        int sum = 0;
        for (NumericCard card : cards) {
            int value = card.getValue();
            if (card.isLocked()) value *= 2;
            sum += value;
        }
        return sum;
    }

    // Group cards by type
    private Map<String, List<NumericCard>> splitCardsByType(List<NumericCard> cards) {
        Map<String, List<NumericCard>> typeMap = new HashMap<>();
        for (NumericCard card : cards) {
            typeMap.putIfAbsent(card.getType(), new ArrayList<>());
            typeMap.get(card.getType()).add(card);
        }
        return typeMap;
    }

    // Calculate total accumulated score from all rounds
    public int sumRoundScores(int[] roundScores) {
        int total = 0;
        for (int score : roundScores) total += score;
        return total;
    }

    // Calculate average score from array
    public double averageScore(int[] scores) {
        if (scores.length == 0) return 0;
        double total = 0;
        for (int score : scores) total += score;
        return total / scores.length;
    }

    // Determine if player won based on averages
    public boolean isAverageEnoughToWin(double playerAverage, double thresholdAverage) {
        return playerAverage >= thresholdAverage;
    }
 // Public method for Superposition preview
    public int previewSuperpositionScore(List<Card> cards) {
        List<NumericCard> numericCards = new ArrayList<>();
        for (Card card : cards) {
            if (card instanceof NumericCard) {
                numericCards.add((NumericCard) card);
            }
        }
        if (numericCards.isEmpty()) return 0;

        return findBestSuperpositionScore(numericCards, null);
    }

    // Checks whether at least one pair exists.
    private boolean containsPair(Map<String, List<NumericCard>> typeMap) {
        for (List<NumericCard> group : typeMap.values()) {
            if (group.size() >= 2) {
                return true;
            }
        }
        return false;
    }
    // Checks whether the hand is four of a kind.
    private boolean areAllSameType(List<NumericCard> cards) {
        if (cards.size() < 4) {
            return false;
        }

        String firstType = cards.get(0).getType();

        for (NumericCard card : cards) {
            if (!card.getType().equals(firstType)) {
                return false;
            }
        }

        return true;
    }
    // Checks whether the hand contains one of each type.
    private boolean areAllDifferentTypes(List<NumericCard> cards) {
        if (cards.size() < 4) {
            return false;
        }

        List<String> types = new ArrayList<>();

        for (NumericCard card : cards) {
            if (types.contains(card.getType())) {
                return false;
            }

            types.add(card.getType());
        }

        return true;
    }
}
