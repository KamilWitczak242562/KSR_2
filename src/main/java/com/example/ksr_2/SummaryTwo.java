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
        this.quantifier = quantifier;
        this.summarizers = summarizers;
        this.objects1 = objects1;
        this.objects2 = objects2;
    }

    public List<TwoSummary> generateAllDualSummaries() {
        List<TwoSummary> allDualSummaries = new ArrayList<>();

        if (qualifiers != null) {
            allDualSummaries.addAll(generateSecondForm(this.qualifiers, this.summarizers));
            allDualSummaries.addAll(generateThirdForm(this.qualifiers, this.summarizers));
        } else {
            allDualSummaries.addAll(generateFirstForm(this.summarizers));
            allDualSummaries.addAll(generateForthForm(this.summarizers));
        }
        return allDualSummaries;
    }

    public List<TwoSummary> generateThirdForm(List<Label> qualifiers, List<Label> summarizers) {
        List<List<Label>> qualifierCombinations = getAllCombinations(qualifiers);
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        List<TwoSummary> twoSummaries = new ArrayList<>();
        for (List<Label> qualifierCombination : qualifierCombinations) {
            for (List<Label> summarizerCombination : summarizerCombinations) {
                double quality = 0.0;
                StringBuilder sentence = new StringBuilder();
                sentence.append(quantifier.toString());
                if (qualifierCombination.isEmpty()) {
                    sentence.append(" meat free products compared to meat products are/have ");
                } else {
                    sentence.append(" meat free products that are/have ");
                    for (int i = 0; i < qualifierCombination.size(); i++) {
                        if (i > 0) {
                            sentence.append(" and ");
                        }
                        sentence.append(qualifierCombination.get(i).toString());
                    }
                    sentence.append(" compared to meat products are/have ");
                }
                for (int i = 0; i < summarizerCombination.size(); i++) {
                    if (i > 0) {
                        sentence.append(" and ");
                    }
                    sentence.append(summarizerCombination.get(i).toString());
                }

                double sumTwo = 0.0;

                List<Double> min = new ArrayList<>();
                List<Double> minS1 = new ArrayList<>();
                List<Double> minW = new ArrayList<>();
                double sumWS = 0.0;
                for (FoodEntry foodEntry : objects2) {
                    for (Label label : summarizerCombination) {
                        min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable())));
                    }
                    sumTwo += Collections.min(min);
                    min.removeAll(min);
                }

                for (FoodEntry foodEntry : objects1) {
                    for (Label label : summarizerCombination) {
                        min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable())));
                    }
                    minS1.add(Collections.min(min));
                    min.removeAll(min);
                }

                for (FoodEntry foodEntry: objects1) {
                    for (Label label: qualifierCombination) {
                        min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable())));
                    }
                    minW.add(Collections.min(min));
                    min.removeAll(min);
                }

                for (int i = 0; i < minW.size(); i++) {
                    sumWS += Math.min(minS1.get(i), minW.get(i));
                }

                quality = (sumWS/objects1.size()) / (sumWS/objects1.size() + sumTwo/objects2.size());
                double tq = quantifier.getMembership(quality);
                twoSummaries.add(new TwoSummary(sentence.toString(), tq));
            }
        }
        return twoSummaries;
    }

    public List<TwoSummary> generateSecondForm(List<Label> qualifiers, List<Label> summarizers) {
        List<List<Label>> qualifierCombinations = getAllCombinations(qualifiers);
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        List<TwoSummary> twoSummaries = new ArrayList<>();
        for (List<Label> qualifierCombination : qualifierCombinations) {
            for (List<Label> summarizerCombination : summarizerCombinations) {
                double quality = 0.0;
                StringBuilder sentence = new StringBuilder();
                sentence.append(quantifier.toString());
                if (qualifierCombination.isEmpty()) {
                    sentence.append(" meat free products compared to meat products are/have ");
                } else {
                    sentence.append(" meat free products compared to meat products that are/have ");
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

                double sumOne = 0.0;

                List<Double> min = new ArrayList<>();
                List<Double> minS2 = new ArrayList<>();
                List<Double> minW = new ArrayList<>();
                double sumWS = 0.0;
                for (FoodEntry foodEntry : objects1) {
                    for (Label label : summarizerCombination) {
                        min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable())));
                    }
                    sumOne += Collections.min(min);
                    min.removeAll(min);
                }

                for (FoodEntry foodEntry : objects2) {
                    for (Label label : summarizerCombination) {
                        min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable())));
                    }
                    minS2.add(Collections.min(min));
                    min.removeAll(min);
                }

                for (FoodEntry foodEntry: objects2) {
                    for (Label label: qualifierCombination) {
                        min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable())));
                    }
                    minW.add(Collections.min(min));
                    min.removeAll(min);
                }

                for (int i = 0; i < minW.size(); i++) {
                    sumWS += Math.min(minS2.get(i), minW.get(i));
                }

                quality = (sumOne/objects1.size()) / ((sumOne/objects1.size()) + (sumWS/objects2.size()));
                double tq = quantifier.getMembership(quality);
                twoSummaries.add(new TwoSummary(sentence.toString(), tq));
            }
        }
        return twoSummaries;
    }

    public List<TwoSummary> generateFirstForm(List<Label> summarizers) {
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        List<TwoSummary> sentences = new ArrayList<>();
        for (List<Label> summarizerCombination : summarizerCombinations) {
            StringBuilder sentence = new StringBuilder();
            sentence.append(quantifier.toString()).append(" meat free products compared to meat products are/have ");
            for (int i = 0; i < summarizerCombination.size(); i++) {
                if (i > 0) {
                    sentence.append(" and ");
                }
                sentence.append(summarizerCombination.get(i).toString());
            }
            double quality = 0.0;
            double sumOne = 0.0;
            double sumTwo = 0.0;
            List<Double> min = new ArrayList<>();
            for (FoodEntry foodEntry : objects1) {
                for (Label label : summarizerCombination) {
                    min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                sumOne += Collections.min(min);
                min.removeAll(min);
            }

            for (FoodEntry foodEntry : objects2) {
                for (Label label : summarizerCombination) {
                    min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                sumTwo += Collections.min(min);
                min.removeAll(min);
            }
            sumOne = sumOne / objects1.size();
            sumTwo = sumTwo / objects2.size();
            quality = sumOne / (sumOne + sumTwo);
            double tq = quantifier.getMembership(quality);
            sentences.add(new TwoSummary(sentence.toString(), tq));
        }
        return sentences;
    }

    public List<TwoSummary> generateForthForm(List<Label> summarizers) {
        List<List<Label>> summarizerCombinations = getAllCombinations(summarizers);
        List<TwoSummary> sentences = new ArrayList<>();
        for (List<Label> summarizerCombination : summarizerCombinations) {
            StringBuilder sentence = new StringBuilder();
            sentence.append("More meat free products compared to meat products are/have ");
            for (int i = 0; i < summarizerCombination.size(); i++) {
                if (i > 0) {
                    sentence.append(" and ");
                }
                sentence.append(summarizerCombination.get(i).toString());
            }
            double quality = 0.0;
            List<Double> min = new ArrayList<>();
            List<Double> minS1 = new ArrayList<>();
            List<Double> minS2 = new ArrayList<>();
            for (FoodEntry foodEntry : objects1) {
                for (Label label : summarizerCombination) {
                    min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                minS1.add(Collections.min(min));
                min.removeAll(min);
            }

            for (FoodEntry foodEntry : objects2) {
                for (Label label : summarizerCombination) {
                    min.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                minS2.add(Collections.min(min));
                min.removeAll(min);
            }

            List<Double> minS2andS1 = new ArrayList<>();
            for (int i = 0; i < minS1.size(); i++) {
                minS2andS1.add(Math.min(minS1.get(i), minS2.get(i)));
            }
            double sumIm = 0.0;
            double sumB = 0.0;

            for (double value : minS2andS1) {
                sumIm += value;
            }

            for (double value : minS2) {
                sumB += value;
            }

            quality = 1 - sumIm / sumB;
            sentences.add(new TwoSummary(sentence.toString(), quality));
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