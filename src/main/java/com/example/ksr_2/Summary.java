package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Summary {
    private Quantifier quantifier;
    private List<Label> qualifiers;
    private List<Label> summarizers;
    private List<FoodEntry> objects1;
    private List<OneSummary> oneSummaries;
    private List<Double> weights;

    public Summary(Quantifier quantifier, List<Label> qualifiers, List<Label> summarizers, List<FoodEntry> objects1, List<Double> weights) {
        this.quantifier = quantifier;
        this.qualifiers = qualifiers;
        this.summarizers = summarizers;
        this.objects1 = objects1;
        this.weights = weights;
    }

    public Summary(Quantifier quantifier, List<Label> summarizers, List<FoodEntry> objects1, List<Double> weights) {
        this(quantifier, null, summarizers, objects1, weights);
    }

    public List<OneSummary> generateAllOneSummaries() {
        List<OneSummary> allOneSummaries = new ArrayList<>();
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        List<List<Label>> qualifierCombinations = qualifiers != null ? getAllCombinations(qualifiers) : null;

        for (List<Label> currentSummarizers : summarizerCombinations) {
            if (qualifierCombinations != null) {
                for (List<Label> currentQualifiers : qualifierCombinations) {
                    String sentence = generateSentence(currentQualifiers, currentSummarizers);
                    Measures measures = new Measures(weights, quantifier, currentQualifiers, currentSummarizers, objects1);
                    allOneSummaries.add(new OneSummary(sentence, measures));
                }
            } else {
                String sentence = generateSentence(null, currentSummarizers);
                Measures measures = new Measures(weights, quantifier, currentSummarizers, objects1);
                allOneSummaries.add(new OneSummary(sentence, measures));
            }
        }

        return allOneSummaries;
    }

    public String generateSentence(List<Label> qualifiers, List<Label> summarizers) {
        StringBuilder sentence = new StringBuilder();
        if (qualifiers == null || qualifiers.isEmpty()) {
            sentence.append(quantifier).append(" products are/have ");
        } else {
            sentence.append(quantifier).append(" products that are/have ");
            for (int i = 0; i < qualifiers.size(); i++) {
                if (i > 0) {
                    sentence.append(" and ");
                }
                sentence.append(qualifiers.get(i));
            }
            sentence.append(" are/have ");
        }
        for (int i = 0; i < summarizers.size(); i++) {
            if (i > 0) {
                sentence.append(" and ");
            }
            sentence.append(summarizers.get(i));
        }
        return sentence.toString();
    }

    private List<List<Label>> getAllCombinations(List<Label> items) {
        List<List<Label>> combinations = new ArrayList<>();
        int n = items.size();
        int numberOfCombinations = 1 << n;

        for (int i = 1; i < numberOfCombinations; i++) {
            List<Label> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(items.get(j));
                }
            }
            combinations.add(combination);
        }

        return combinations;
    }
}
