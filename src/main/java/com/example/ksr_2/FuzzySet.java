package com.example.ksr_2;

import com.example.ksr_2.functions.MembershipFunction;
import com.example.ksr_2.functions.Trapezoidal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FuzzySet{
    private MembershipFunction membershipFunction;
    private ClassicSet universe;

    public List<Double> getSupport(List<Double> values) {
        List<Double> support = new ArrayList<>();
        for (double element : values) {
            if (membershipFunction.getMembership(element) > 0.0) {
                support.add(element);
            }
        }
        return support;
    }

    public double getMembership(double value) {
        return membershipFunction.getMembership(value);
    }

    public double degreeOfFuzziness(List<Double> values) {
        return (double) getSupport(values).size() / values.size();
    }

    public double getCardinalNumber(List<Double> values) {
        double sum = 0.0;
        for (double value: values) {
            sum += membershipFunction.getMembership(value);
        }
        return sum;
    }

    public ClassicSet getAlphaCut(double cut) {
        ClassicSet alphaCutSet = new ClassicSet(new ArrayList<>());
        for (double element : universe.getSet()) {
            if (membershipFunction.getMembership(element) >= cut) {
                alphaCutSet.add(element);
            }
        }
        return alphaCutSet;
    }

    public boolean isConvex() {
        for (double x1 : universe.getSet()) {
            for (double x2 : universe.getSet()) {
                if (x1 < x2) {
                    double x = 0.5 * (x1 + x2);
                    if (membershipFunction.getMembership(x) < Math.min(membershipFunction.getMembership(x1), membershipFunction.getMembership(x2))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public FuzzySet getComplement() {
        MembershipFunction complementFunction = new MembershipFunction() {
            @Override
            public double getMembership(double x) {
                return 1.0 - membershipFunction.getMembership(x);
            }

            @Override
            public double getCardinalNumber() {
                double cardinalNumber = 0.0;
                for (double element : universe.getSet()) {
                    cardinalNumber += 1.0 - membershipFunction.getMembership(element);
                }
                return cardinalNumber;
            }

            @Override
            public double getSupport() {
                return membershipFunction.getSupport();
            }
        };

        return new FuzzySet(complementFunction, universe);
    }
}
