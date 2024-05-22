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
@NoArgsConstructor
public class FuzzySet<T> {
    private MembershipFunction membershipFunction;
    private ClassicSet<T> universe;

    public List<T> getSupport() {
        return null;
    }

    public double getMembership(double value) {
        return membershipFunction.getMembership(value);
    }

    public double degreeOfFuzziness() {
        return 0.0;
    }

    public boolean isConvex() {
        return false;
    }

    public double getCardinalNumber() {
        return 0.0;
    }

    public ClassicSet<T> getAlphaCut(double cut) {
        return this.universe;
    }

    public FuzzySet<T> getComplement() {
        return new FuzzySet<>();
    }
}
