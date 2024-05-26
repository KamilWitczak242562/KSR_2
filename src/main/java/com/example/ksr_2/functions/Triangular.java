package com.example.ksr_2.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Triangular implements MembershipFunction {
    private double a;
    private double b;
    private double c;

    @Override
    public double getMembership(double x) {
        if (x <= a || x >= c) {
            return 0.0;
        } else if (x > a && x < b) {
            return (x - a) / (b - a);
        } else if (x == b) {
            return 1.0;
        } else if (x > b && x < c) {
            return (c - x) / (c - b);
        } else {
            return 0.0;
        }
    }

    @Override
    public double getCardinalNumber() {
        return 0.5 * (c - a);
    }

    @Override
    public double getSupport() {
       return c - a;
    }
}
