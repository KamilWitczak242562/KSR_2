package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SummaryTwo {
    private Quantifier quantifier;
    private List<Label> qualifiers;
    private List<Label> summarizers;
    private List<FoodEntry> objects1;
    private List<FoodEntry> objects2;
    private List<OneSummary> oneSummaries;

    public SummaryTwo(Quantifier quantifier, List<Label> qualifiers, List<Label> summarizers, List<FoodEntry> objects1, List<FoodEntry> objects2) {
        this.quantifier = quantifier;
        this.qualifiers = qualifiers;
        this.summarizers = summarizers;
        this.objects1 = objects1;
        this.objects2 = objects2;
    }

    public SummaryTwo(Quantifier quantifier, List<Label> summarizers, List<FoodEntry> objects1, List<FoodEntry> objects2) {
        this(quantifier, null, summarizers, objects1, objects2);
    }

    public List<TwoSummary> generateAllDualSummaries() {
        List<TwoSummary> allDualSummaries = new ArrayList<>();

        if (qualifiers != null) {
            allDualSummaries.addAll(generateThirdForm());
            allDualSummaries.addAll(generateSecondForm());
        } else {
            allDualSummaries.addAll(generateFirstForm());
            allDualSummaries.addAll(generateFourthForm());
        }

        return allDualSummaries;
    }

    private List<TwoSummary> generateForm(List<Label> summarizers, boolean isFirstForm, boolean isThirdForm) {
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        List<TwoSummary> twoSummaries = new ArrayList<>();

        for (List<Label> summarizerCombination : summarizerCombinations) {
            StringBuilder sentence = new StringBuilder();
            double quality = 0.0;

            if (isFirstForm) {
                sentence.append(quantifier.toString()).append(" meat free products compared to meat products are/have ");
            } else {
                sentence.append("More meat free products compared to meat products are/have ");
            }

            for (int i = 0; i < summarizerCombination.size(); i++) {
                if (i > 0) {
                    sentence.append(" and ");
                }
                sentence.append(summarizerCombination.get(i).toString());
            }

            if (isFirstForm) {
                quality = calculateQuality(summarizerCombination, objects1, objects2);
            } else {
                quality = calculateQuality(summarizerCombination, objects2, objects1);
            }

            double tq = quantifier.getMembership(quality);
            twoSummaries.add(new TwoSummary(sentence.toString(), tq));
        }

        return twoSummaries;
    }

    private List<TwoSummary> generateFirstForm() {
        return generateForm(summarizers, true, false);
    }

    private List<TwoSummary> generateFourthForm() {
        return generateForm(summarizers, false, false);
    }

    private List<TwoSummary> generateSecondForm() {
        return generateQualifiedForm(qualifiers, summarizers, false);
    }

    private List<TwoSummary> generateThirdForm() {
        return generateQualifiedForm(qualifiers, summarizers, true);
    }

    private List<TwoSummary> generateQualifiedForm(List<Label> qualifiers, List<Label> summarizers, boolean isThirdForm) {
        List<List<Label>> qualifierCombinations = getAllCombinations(qualifiers);
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        List<TwoSummary> twoSummaries = new ArrayList<>();

        for (List<Label> qualifierCombination : qualifierCombinations) {
            for (List<Label> summarizerCombination : summarizerCombinations) {
                StringBuilder sentence = new StringBuilder();
                double quality = 0.0;

                sentence.append(quantifier.toString()).append(" meat free products");
                if (isThirdForm) {
                    sentence.append(" that are/have ");
                } else {
                    sentence.append(" compared to meat products that are/have ");
                }

                for (int i = 0; i < qualifierCombination.size(); i++) {
                    if (i > 0) {
                        sentence.append(" and ");
                    }
                    sentence.append(qualifierCombination.get(i).toString());
                }

                sentence.append(" are/have ");
                for (int i = 0; i < summarizerCombination.size(); i++) {
                    if (i > 0) {
                        sentence.append(" and ");
                    }
                    sentence.append(summarizerCombination.get(i).toString());
                }

                if (isThirdForm) {
                    quality = calculateQualifiedQuality(qualifierCombination, summarizerCombination, objects1, objects2);
                } else {
                    quality = calculateQualifiedQuality(qualifierCombination, summarizerCombination, objects2, objects1);
                }

                double tq = quantifier.getMembership(quality);
                twoSummaries.add(new TwoSummary(sentence.toString(), tq));
            }
        }

        return twoSummaries;
    }

    private double calculateQuality(List<Label> summarizerCombination, List<FoodEntry> group1, List<FoodEntry> group2) {
        double sumOne = calculateSum(summarizerCombination, group1);
        double sumTwo = calculateSum(summarizerCombination, group2);

        sumOne = sumOne / group1.size();
        sumTwo = sumTwo / group2.size();
        return sumOne / (sumOne + sumTwo);
    }

    private double calculateQualifiedQuality(List<Label> qualifierCombination, List<Label> summarizerCombination, List<FoodEntry> group1, List<FoodEntry> group2) {
        double sumOne = calculateSum(summarizerCombination, group1);
        double sumQualified = calculateQualifiedSum(qualifierCombination, summarizerCombination, group1);
        double sumTwo = calculateSum(summarizerCombination, group2);

        return (sumOne / group1.size()) / ((sumOne / group1.size()) + (sumQualified / group2.size()));
    }

    private double calculateSum(List<Label> combination, List<FoodEntry> group) {
        double sum = 0.0;
        List<Double> min = new ArrayList<>();
        for (FoodEntry foodEntry : group) {
            for (Label label : combination) {
                min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
            }
            sum += Collections.min(min);
            min.clear();
        }
        return sum;
    }

    private double calculateQualifiedSum(List<Label> qualifierCombination, List<Label> summarizerCombination, List<FoodEntry> group) {
        double sum = 0.0;
        List<Double> minQualifiers = new ArrayList<>();
        List<Double> minSummarizers = new ArrayList<>();

        for (FoodEntry foodEntry : group) {
            for (Label qualifier : qualifierCombination) {
                minQualifiers.add(qualifier.getMembership(foodEntry.getValueByName(qualifier.getLinguisticVariable().toLowerCase())));
            }
            for (Label summarizer : summarizerCombination) {
                minSummarizers.add(summarizer.getMembership(foodEntry.getValueByName(summarizer.getLinguisticVariable().toLowerCase())));
            }
            sum += Math.min(Collections.min(minQualifiers), Collections.min(minSummarizers));
            minQualifiers.clear();
            minSummarizers.clear();
        }

        return sum;
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
