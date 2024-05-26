package com.example.ksr_2.functions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Trapezoidal implements MembershipFunction {
    private double a;
    private double b;
    private double c;
    private double d;

    @Override
    public double getMembership(double x) {
        if (x <= a || x >= d) {
            return 0.0;
        } else if (x > a && x < b) {
            return (x - a) / (b - a);
        } else if (x >= b && x <= c) {
            return 1.0;
        } else if (x > c && x < d) {
            return (d - x) / (d - c);
        } else {
            return 0.0;
        }
    }

    @Override
    public double getCardinalNumber() {
        return 0.5 * ((b - a) + (d - c));
    }

    @Override
    public double getSupport(double x) {
        if (x > a && x < d) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
