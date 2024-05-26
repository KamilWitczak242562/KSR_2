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
        this.weights = weights;
        this.quantifier = quantifier;
        this.summarizers = summarizers;
        this.objects1 = objects1;
        calculateMeasures();
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

    public double calcT1() {
        if (getQualifiers() == null) {
            List<Double> getMin = new ArrayList<>();
            double sum = 0;
            if (getQuantifier().isAbsolute()) {
                for (FoodEntry foodEntry : getObjects1()) {
                    for (Label label : getSummarizers()) {
                        getMin.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                    }
                    sum += Collections.min(getMin);
                    getMin.removeAll(getMin);
                }
                System.out.println("Sum dla q abs " + sum);
                return getQuantifier().getMembership(sum);
            } else {
                for (FoodEntry foodEntry : getObjects1()) {
                    for (Label label : getSummarizers()) {
                        getMin.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                    }
                    sum += Collections.min(getMin);
                    getMin.removeAll(getMin);
                }
                System.out.println("sum dla q rel " +  sum / getObjects1().size());
                return getQuantifier().getMembership(sum / getObjects1().size());
            }
        } else {
            List<Double> getMin = new ArrayList<>();
            List<Double> getMinQ = new ArrayList<>();
            List<Double> valuesForWMinS = new ArrayList<>();
            double sumForS;
            double sumForW;
            for (FoodEntry foodEntry : getObjects1()) {
                sumForS = 0;
                sumForW = 0;
                for (Label label : getSummarizers()) {
                    getMin.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                sumForS += Collections.min(getMin);
                for (Label label : getQualifiers()) {
                    getMinQ.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                sumForW += Collections.min(getMin);
                if (sumForS < sumForW) {
                    valuesForWMinS.add(sumForS);
                } else {
                    valuesForWMinS.add(sumForW);
                }
                getMin.removeAll(getMin);
                getMinQ.removeAll(getMinQ);
            }
            sumForW = 0;
            for (FoodEntry foodEntry : getObjects1()) {
                for (Label label : getQualifiers()) {
                    getMin.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                sumForW += Collections.min(getMin);
                getMin.removeAll(getMin);
            }
            double sumWandS = 0;
            for (double value : valuesForWMinS) {
                sumWandS += value;
            }
            sumWandS = sumWandS / getObjects1().size();
            sumForW = sumForW / getObjects1().size();
            return getQuantifier().getMembership(sumWandS / sumForW);
        }
    }

    public double calcT2() {
        double p = 1.0;
        List<Double> values = new ArrayList<>();
        for (Label label : getSummarizers()) {
            for (FoodEntry foodEntry : getObjects1()) {
                values.add(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase()));
            }
            p *= label.getFuzzySet().degreeOfFuzziness(values);
            values.removeAll(values);
        }
        return 1 - Math.pow(p, 1.0 / getSummarizers().size());
    }

    public double calcT3() {
        int t = 0;
        int h = 0;
        double membershipQ = 0.0;
        List<Double> listQ = new ArrayList<>();
        for (FoodEntry foodEntry : getObjects1()) {
            if (getQualifiers() != null) {
                for (Label label : getQualifiers()) {
                    listQ.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                membershipQ = Collections.min(listQ);
                listQ.removeAll(listQ);
            }
            if (membershipQ > 0.0) {
                h++;
                for (Label label : getSummarizers()) {
                    listQ.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                membershipQ = Collections.min(listQ);
                listQ.removeAll(listQ);
                if (membershipQ > 0.0) {
                    t++;
                }
            }
        }
        if (getQualifiers() != null) {
            if (h == 0 || t == 0) {
                return 0.0;
            } else {
                return (double) t / h;
            }
        } else {
            for (FoodEntry foodEntry : getObjects1()) {
                for (Label label : getSummarizers()) {
                    listQ.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                membershipQ = Collections.min(listQ);
                listQ.removeAll(listQ);
                if (membershipQ > 0.0) {
                    t++;
                }
            }
            return (double) t / getObjects1().size();
        }
    }

    public double calcT4() {
        double p = 1.0;
        for (Label label : getSummarizers()) {
            double r = 0.0;
            for (FoodEntry foodEntry : getObjects1()) {
                if (label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())) > 0.0) {
                    r++;
                }
            }
            p *= r / getObjects1().size();
        }
        return Math.abs(p - this.T3degreeOfCovering);
    }

    public double calcT5() {
        return 2 * Math.pow(0.5, getSummarizers().size());
    }

    public double calcT6() {
        if (getQualifiers() != null) {
            List<Double> getMinQ = new ArrayList<>();
            List<Double> getMinS = new ArrayList<>();
            List<Double> bigQ = new ArrayList<>();
            List<Double> bigS = new ArrayList<>();
            for (FoodEntry foodEntry : getObjects1()) {
                for (Label label : getQualifiers()) {
                    getMinQ.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                for (Label label : getSummarizers()) {
                    getMinS.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                bigS.add(Collections.min(getMinS));
                bigQ.add(Collections.min(getMinQ));
                getMinQ.removeAll(getMinQ);
                getMinS.removeAll(getMinS);
            }
            List<Double> output = new ArrayList<>();
            int i = 0;
            for (double value : bigS) {
                if (value < bigQ.get(i)) {
                    output.add(value);
                } else {
                    output.add(bigS.get(i));
                }
            }
            List<Double> suppQ = new ArrayList<>();
            for (double value : output) {
                if (getQuantifier().getMembership(value) > 0.0) {
                    suppQ.add(value);
                }
            }
            if (getQuantifier().isAbsolute()) {
                return 1 - (double) suppQ.size() / getObjects1().size();
            } else {
                return 1 - getQuantifier().getFuzzySet().getMembershipFunction().getSupport();
            }
        } else {
            List<Double> getMinS = new ArrayList<>();
            List<Double> bigS = new ArrayList<>();
            for (FoodEntry foodEntry : getObjects1()) {
                for (Label label : getSummarizers()) {
                    getMinS.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                bigS.add(Collections.min(getMinS));
                getMinS.removeAll(getMinS);
            }
            List<Double> suppQ = new ArrayList<>();
            for (double value : bigS) {
                if (getQuantifier().getMembership(value) > 0.0) {
                    suppQ.add(value);
                }
            }
            if (getQuantifier().isAbsolute()) {
                return 1 - (double) suppQ.size() / getObjects1().size();
            } else {
                return 1 - getQuantifier().getFuzzySet().getMembershipFunction().getSupport();
            }
        }
    }

    public double calcT7() {
        if (getQualifiers() != null) {
            List<Double> getMinQ = new ArrayList<>();
            List<Double> getMinS = new ArrayList<>();
            List<Double> bigQ = new ArrayList<>();
            List<Double> bigS = new ArrayList<>();
            for (FoodEntry foodEntry : getObjects1()) {
                for (Label label : getQualifiers()) {
                    getMinQ.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                for (Label label : getSummarizers()) {
                    getMinS.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                bigS.add(Collections.min(getMinS));
                bigQ.add(Collections.min(getMinQ));
                getMinQ.removeAll(getMinQ);
                getMinS.removeAll(getMinS);
            }
            List<Double> output = new ArrayList<>();
            int i = 0;
            for (double value : bigS) {
                if (value < bigQ.get(i)) {
                    output.add(value);
                } else {
                    output.add(bigS.get(i));
                }
            }
            double sum = 0.0;
            for (double value : output) {
                if (getQuantifier().getMembership(value) > 0.0) {
                    sum += getQuantifier().getMembership(value);
                }
            }
            if (getQuantifier().isAbsolute()) {
                return 1 - sum / getObjects1().size();
            } else {
                return 1 - getQuantifier().getFuzzySet().getMembershipFunction().getCardinalNumber();
            }
        } else {
            List<Double> getMinS = new ArrayList<>();
            List<Double> bigS = new ArrayList<>();
            for (FoodEntry foodEntry : getObjects1()) {
                for (Label label : getSummarizers()) {
                    getMinS.add(label.getMembership(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase())));
                }
                bigS.add(Collections.min(getMinS));
                getMinS.removeAll(getMinS);
            }
            double sum = 0.0;
            for (double value : bigS) {
                if (getQuantifier().getMembership(value) > 0.0) {
                    sum += getQuantifier().getMembership(value);
                }
            }
            if (getQuantifier().isAbsolute()) {
                return 1 - sum / getObjects1().size();
            } else {
                return 1 - getQuantifier().getFuzzySet().getMembershipFunction().getCardinalNumber();
            }
        }
    }

    public double calcT8() {
        double p = 1.0;
        double card = 0.0;
        for (Label label : getSummarizers()) {
            card = label.getFuzzySet().getMembershipFunction().getCardinalNumber();
            p *= card / label.getFuzzySet().getUniverse().universe();
        }
        return 1 - Math.pow(p, (double) 1 / getSummarizers().size());
    }

    public double calcT9() {
        if (getQualifiers() != null) {
            double p = 1.0;
            List<Double> values = new ArrayList<>();
            for (Label label : getQualifiers()) {
                for (FoodEntry foodEntry : getObjects1()) {
                    values.add(foodEntry.getValueByName(label.getLinguisticVariable().toLowerCase()));
                }
                p *= label.getFuzzySet().degreeOfFuzziness(values);
                values.removeAll(values);
            }
            return 1 - Math.pow(p, 1.0 / getSummarizers().size());
        } else {
            return 0.0;
        }
    }

    public double calcT10() {
        if (getQualifiers() != null) {
            double p = 1.0;
            double card = 0.0;
            for (Label label : getSummarizers()) {
                card = label.getFuzzySet().getMembershipFunction().getCardinalNumber();
                p *= card / label.getFuzzySet().getUniverse().universe();
            }
            return 1 - Math.pow(p, (double) 1 / getSummarizers().size());
        } else {
            return 0.0;
        }
    }

    public double calcT11() {
        if (getQualifiers() != null) {
            return 2 * Math.pow(0.5, getQualifiers().size());
        } else {
            return 0.0;
        }
    }

    public double calcQuality() {
        double q = 0.0;
        double sum = 0.0;
        if (getQualifiers() == null) {
            List<Double> measures = new ArrayList<>() {{
                add(T1degreeOfTruth); add(T2degreeOfImprecision); add(T3degreeOfCovering); add(T4degreeOfAppropriateness);
                add(T5lengthOfSummary); add(T6degreeOfQuantifierImprecision); add(T7degreeOfQuantifierCardinality);
                add(T8degreeOfSummarizerCardinality);
            }};
            for (int i = 0; i < measures.size(); i++) {
                q += weights.get(i) * measures.get(i);
                sum += weights.get(i);
            }
        } else {
            List<Double> measures = new ArrayList<>() {{
                add(T1degreeOfTruth); add(T2degreeOfImprecision); add(T3degreeOfCovering); add(T4degreeOfAppropriateness);
                add(T5lengthOfSummary); add(T6degreeOfQuantifierImprecision); add(T7degreeOfQuantifierCardinality);
                add(T8degreeOfSummarizerCardinality); add(T9degreeOfQualifierImprecision); add(T10degreeOfQualifierCardinality);
                add(T11lengthOfQualifier);
            }};
            for (int i = 0; i < measures.size(); i++) {
                q += weights.get(i) * measures.get(i);
                sum += weights.get(i);
            }
        }
        return q / sum;
    }

    @Override
    public String toString() {
        return T1degreeOfTruth + ", " + T2degreeOfImprecision + ", " + T3degreeOfCovering + ", " +
                T4degreeOfAppropriateness + ", " + T5lengthOfSummary + ", " + T6degreeOfQuantifierImprecision + ", " +
                T7degreeOfQuantifierCardinality + ", " + T8degreeOfSummarizerCardinality + ", " + T9degreeOfQualifierImprecision +
                ", " + T10degreeOfQualifierCardinality + ", " + T11lengthOfQualifier + ", " + qualityOfSummary;
    }
}
