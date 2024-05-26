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
    private List<FoodEntry> objects2;
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
        this.quantifier = quantifier;
        this.summarizers = summarizers;
        this.objects1 = objects1;
        this.weights = weights;
    }


    public List<OneSummary> generateAllOneSummaries() {
        List<OneSummary> allOneSummaries = new ArrayList<>();
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);

        for (List<Label> currentSummarizers : summarizerCombinations) {
            List<String> sentences = generateSentences(qualifiers, currentSummarizers);
            for (String sentence : sentences) {
                Measures measures1;
                if (qualifiers == null || qualifiers.isEmpty()) {
                    measures1 = new Measures(weights, quantifier, currentSummarizers, objects1);
                } else {
                    measures1 = new Measures(weights, quantifier, qualifiers, currentSummarizers, objects1);
                }
                allOneSummaries.add(new OneSummary(sentence, measures1));
            }
        }

        return allOneSummaries;
    }

    public List<String> generateSentences(List<Label> qualifiers, List<Label> summarizers) {
        List<String> sentences = new ArrayList<>();
        List<List<Label>> qualifierCombinations;
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        if (qualifiers != null) {
            qualifierCombinations = getAllCombinations(qualifiers);
            for (List<Label> qualifierCombination : qualifierCombinations) {
                for (List<Label> summarizerCombination : summarizerCombinations) {
                    StringBuilder sentence = new StringBuilder();
                    if (qualifierCombination.isEmpty()) {
                        sentence.append(quantifier.toString()).append(" products have ");
                    } else {
                        sentence.append(quantifier.toString()).append(" products that are/have ");
                        for (int i = 0; i < qualifierCombination.size(); i++) {
                            if (i > 0) {
                                sentence.append(" and ");
                            }
                            sentence.append(qualifierCombination.get(i).toString());
                        }
                        sentence.append(" are/have ");
                    }
                    for (int i = 0; i < summarizerCombination.size(); i++) {
                        if (i > 0) {
                            sentence.append(" and ");
                        }
                        sentence.append(summarizerCombination.get(i).toString());
                    }
                    sentences.add(sentence.toString());
                }
            }
        } else {
            for (List<Label> summarizerCombination : summarizerCombinations) {
                StringBuilder sentence = new StringBuilder();
                sentence.append(quantifier.toString()).append(" products are/have ");
                for (int i = 0; i < summarizerCombination.size(); i++) {
                    if (i > 0) {
                        sentence.append(" and ");
                    }
                    sentence.append(summarizerCombination.get(i).toString());
                }
                sentences.add(sentence.toString());
            }
        }

        return sentences;
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
