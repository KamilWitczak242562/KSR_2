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
        return 0;
    }

    @Override
    public double getCardinalNumber(double x) {
        return 0;
    }

    @Override
    public double getSupport(double x) {
        return 0;
    }
}
