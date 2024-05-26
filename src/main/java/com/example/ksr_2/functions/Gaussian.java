package com.example.ksr_2.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Gaussian implements MembershipFunction {
    private double mean;
    private double stdev;

    @Override
    public double getMembership(double x) {
        return Math.exp(-Math.pow((x - mean), 2) / (2 * Math.pow(stdev, 2)));
    }

    @Override
    public double getCardinalNumber() {
        return Math.sqrt(2 * Math.PI) * stdev;
    }

    @Override
    public double getSupport(double x) {
        if (getMembership(x) > 0.01) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
