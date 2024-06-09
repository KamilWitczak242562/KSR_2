package com.example.ksr_2;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Measures {
    private double T1degreeOfTruth;
    private double T2degreeOfImprecision;
    private double T3degreeOfCovering;
    private double T4degreeOfAppropriateness;
    private double T5lengthOfSummary;
    private double T6degreeOfQuantifierImprecision;
    private double T7degreeOfQuantifierCardinality;
    private double T8degreeOfSummarizerCardinality;
    private double T9degreeOfQualifierImprecision;
    private double T10degreeOfQualifierCardinality;
    private double T11lengthOfQualifier;
    private double qualityOfSummary;
    private List<Double> weights;
    private Quantifier quantifier;
    private List<Label> qualifiers;
    private List<Label> summarizers;
    private List<FoodEntry> objects1;

    public Measures(List<Double> weights, Quantifier quantifier, List<Label> qualifiers, List<Label> summarizers, List<FoodEntry> objects1) {
        this.weights = weights;
        this.quantifier = quantifier;
        this.qualifiers = qualifiers;
        this.summarizers = summarizers;
        this.objects1 = objects1;
        calculateMeasures();
    }

    public Measures(List<Double> weights, Quantifier quantifier, List<Label> summarizers, List<FoodEntry> objects1) {
        this(weights, quantifier, null, summarizers, objects1);
    }

    public void calculateMeasures() {
        this.T1degreeOfTruth = calcT1();
        this.T2degreeOfImprecision = calcT2();
        this.T3degreeOfCovering = calcT3();
        this.T4degreeOfAppropriateness = calcT4();
        this.T5lengthOfSummary = calcT5();
        this.T6degreeOfQuantifierImprecision = calcT6();
        this.T7degreeOfQuantifierCardinality = calcT7();
        this.T8degreeOfSummarizerCardinality = calcT8();
        this.T9degreeOfQualifierImprecision = calcT9();
        this.T10degreeOfQualifierCardinality = calcT10();
        this.T11lengthOfQualifier = calcT11();
        this.qualityOfSummary = calcQuality();
    }

    private double calcT1() {
        double t1 = 0.0;
        List<Double> minValues = new ArrayList<>();
        double sum = 0;

        for (FoodEntry foodEntry : objects1) {
            minValues.clear();
            for (Label label : summarizers) {
                minValues.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
            }
            sum += Collections.min(minValues);
        }

        if (qualifiers == null) {
            t1 = quantifier.isAbsolute() ? quantifier.getMembership(sum) : quantifier.getMembership(sum / objects1.size());
        } else {
            double sumForW = calculateSumForQualifiers(minValues, sum);
            t1 = quantifier.getMembership(sum / sumForW);
        }

        return roundToTwoDecimalPlaces(t1);
    }

    private double calculateSumForQualifiers(List<Double> minValues, double sum) {
        List<Double> valuesForWMinS = new ArrayList<>();
        double sumForW = 0;

        for (FoodEntry foodEntry : objects1) {
            minValues.clear();
            for (Label label : qualifiers) {
                minValues.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
            }
            double minQ = Collections.min(minValues);

            if (sum < minQ) {
                valuesForWMinS.add(sum);
            } else {
                valuesForWMinS.add(minQ);
            }
            sumForW += minQ;
        }

        double sumWandS = valuesForWMinS.stream().mapToDouble(Double::doubleValue).sum() / objects1.size();
        return sumWandS / (sumForW / objects1.size());
    }

    private double calcT2() {
        double p = 1.0;
        List<Double> values = new ArrayList<>();

        for (Label label : summarizers) {
            values.clear();
            for (FoodEntry foodEntry : objects1) {
                values.add(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase()));
            }
            p *= label.getFuzzySet().degreeOfFuzziness(values);
        }

        return roundToTwoDecimalPlaces(1 - Math.pow(p, 1.0 / summarizers.size()));
    }

    private double calcT3() {
        int t = 0;
        int h = 0;
        List<Double> minValues = new ArrayList<>();

        if (qualifiers != null) {
            for (FoodEntry foodEntry : objects1) {
                double membershipQ = getMinMembership(minValues, foodEntry, qualifiers);
                if (membershipQ > 0.0) h++;

                double membershipS = getMinMembership(minValues, foodEntry, summarizers);
                if (Math.min(membershipS, membershipQ) > 0.0) t++;
            }
            return roundToTwoDecimalPlaces(h == 0 || t == 0 ? 0.0 : (double) t / h);
        } else {
            for (FoodEntry foodEntry : objects1) {
                if (getMinMembership(minValues, foodEntry, summarizers) > 0.0) t++;
            }
            return roundToTwoDecimalPlaces((double) t / objects1.size());
        }
    }

    private double getMinMembership(List<Double> minValues, FoodEntry foodEntry, List<Label> labels) {
        minValues.clear();
        for (Label label : labels) {
            minValues.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
        }
        return Collections.min(minValues);
    }

    private double calcT4() {
        double p = 1.0;
        for (Label label : summarizers) {
            double r = 0.0;
            for (FoodEntry foodEntry : objects1) {
                r += label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase()));
            }
            p *= r / objects1.size();
        }
        return roundToTwoDecimalPlaces(Math.abs(p - T3degreeOfCovering));
    }

    private double calcT5() {
        return roundToTwoDecimalPlaces(2 * Math.pow(0.5, summarizers.size()));
    }

    private double calcT6() {
        double support = quantifier.getFuzzySet().getMembershipFunction().getSupport();
        double t6 = quantifier.isAbsolute() ? 1 - support / objects1.size() : 1 - support;
        return roundToTwoDecimalPlaces(t6);
    }

    private double calcT7() {
        double cardinalNumber = quantifier.getFuzzySet().getMembershipFunction().getCardinalNumber();
        double t7 = quantifier.isAbsolute() ? 1 - cardinalNumber / objects1.size() : 1 - cardinalNumber;
        return roundToTwoDecimalPlaces(t7);
    }

    private double calcT8() {
        double p = 1.0;
        for (Label label : summarizers) {
            double card = 0.0;
            for (FoodEntry foodEntry : objects1) {
                card += label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase()));
            }
            p *= card / objects1.size();
        }
        return roundToTwoDecimalPlaces(1 - Math.pow(p, 1.0 / summarizers.size()));
    }

    private double calcT9() {
        if (qualifiers == null) return 1.0;

        double p = 1.0;
        List<Double> values = new ArrayList<>();
        for (Label label : qualifiers) {
            values.clear();
            for (FoodEntry foodEntry : objects1) {
                values.add(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase()));
            }
            p *= label.getFuzzySet().degreeOfFuzziness(values);
        }

        return roundToTwoDecimalPlaces(1 - Math.pow(p, 1.0 / qualifiers.size()));
    }

    private double calcT10() {
        if (qualifiers == null) return 1.0;

        double p = 1.0;
        for (Label label : qualifiers) {
            double card = 0.0;
            for (FoodEntry foodEntry : objects1) {
                card += label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase()));
            }
            p *= card / objects1.size();
        }

        return roundToTwoDecimalPlaces(1 - Math.pow(p, 1.0 / qualifiers.size()));
    }

    private double calcT11() {
        return roundToTwoDecimalPlaces(qualifiers == null ? 1.0 : 2 * Math.pow(0.5, qualifiers.size()));
    }

    private double calcQuality() {
        double q = 0.0;
        double sum = 0.0;

        List<Double> measures = new ArrayList<>();
        measures.add(T1degreeOfTruth);
        measures.add(T2degreeOfImprecision);
        measures.add(T3degreeOfCovering);
        measures.add(T4degreeOfAppropriateness);
        measures.add(T5lengthOfSummary);
        measures.add(T6degreeOfQuantifierImprecision);
        measures.add(T7degreeOfQuantifierCardinality);
        measures.add(T8degreeOfSummarizerCardinality);

        if (qualifiers != null) {
            measures.add(T9degreeOfQualifierImprecision);
            measures.add(T10degreeOfQualifierCardinality);
            measures.add(T11lengthOfQualifier);
        }

        for (int i = 0; i < measures.size(); i++) {
            q += weights.get(i) * measures.get(i);
            sum += weights.get(i);
        }

        return roundToTwoDecimalPlaces(q / sum);
    }

    private double roundToTwoDecimalPlaces(double value) {
        return (double) Math.round(value * 100) / 100;
    }

    @Override
    public String toString() {
        if (qualifiers != null) {
            return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s",
                    T1degreeOfTruth, T2degreeOfImprecision, T3degreeOfCovering, T4degreeOfAppropriateness,
                    T5lengthOfSummary, T6degreeOfQuantifierImprecision, T7degreeOfQuantifierCardinality,
                    T8degreeOfSummarizerCardinality, T9degreeOfQualifierImprecision,
                    T10degreeOfQualifierCardinality, T11lengthOfQualifier, qualityOfSummary);
        } else {
            return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s",
                    T1degreeOfTruth, T2degreeOfImprecision, T3degreeOfCovering, T4degreeOfAppropriateness,
                    T5lengthOfSummary, T6degreeOfQuantifierImprecision, T7degreeOfQuantifierCardinality,
                    T8degreeOfSummarizerCardinality, qualityOfSummary);
        }
    }
}
