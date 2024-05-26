package com.example.ksr_2;

import com.example.ksr_2.functions.Gaussian;
import com.example.ksr_2.functions.Trapezoidal;
import com.example.ksr_2.functions.Triangular;

import java.util.ArrayList;
import java.util.List;

public class LabelFactory {


    public static LinguisticVariable createLinguisticVariable(String name) {
        switch (name) {
            case "carbohydrates":
                List<Double> numbersCarb = new ArrayList<>(){{
                    for (double i = 0.00; i <= 300.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label lowCarb = new Label(new FuzzySet(new Trapezoidal(0, 0, 50, 90), new ClassicSet(numbersCarb)), "low level of carbohydrates", "carbohydrates");
                Label moderateCarb = new Label(new FuzzySet(new Trapezoidal(50, 90, 140, 170), new ClassicSet(numbersCarb)), "moderate level of carbohydrates", "carbohydrates");
                Label highCarb = new Label(new FuzzySet(new Trapezoidal(140, 170, 300, 300), new ClassicSet(numbersCarb)), "high level of carbohydrates", "carbohydrates");
                List<Label> labelsCarb = new ArrayList<>() {{
                    add(lowCarb); add(moderateCarb); add(highCarb);
                }};
                return new LinguisticVariable(labelsCarb, new ClassicSet(numbersCarb), "carbohydrates");
            case "cholesterol":
                List<Double> numbersChol = new ArrayList<>(){{
                    for (double i = 0.00; i <= 350.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label lowChol = new Label(new FuzzySet(new Trapezoidal(0, 0, 100, 160), new ClassicSet(numbersChol)), "low level of cholesterol", "cholesterol");
                Label moderateChol = new Label(new FuzzySet(new Trapezoidal(100, 160, 220, 250), new ClassicSet(numbersChol)), "optimal level of cholesterol", "cholesterol");
                Label highChol = new Label(new FuzzySet(new Trapezoidal(220, 250, 350, 350), new ClassicSet(numbersChol)), "high level of cholesterol", "cholesterol");
                List<Label> labelsChol = new ArrayList<>() {{
                    add(lowChol); add(moderateChol); add(highChol);
                }};
                return new LinguisticVariable(labelsChol, new ClassicSet(numbersChol), "cholesterol");
            case "energy":
                List<Double> numbersEnergy = new ArrayList<>(){{
                    for (double i = 0.00; i <= 1300.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label lowEnergy = new Label(new FuzzySet(new Trapezoidal(0, 0, 150, 350), new ClassicSet(numbersEnergy)), "low level of energy", "energy");
                Label moderateEnergy = new Label(new FuzzySet(new Trapezoidal(150, 300, 400, 650), new ClassicSet(numbersEnergy)), "moderate level of energy", "energy");
                Label energeticEnergy = new Label(new FuzzySet(new Trapezoidal(400, 650, 850, 1000), new ClassicSet(numbersEnergy)), "energetic", "energy");
                Label highEnergy = new Label(new FuzzySet(new Trapezoidal(850, 1000, 1300, 1300), new ClassicSet(numbersEnergy)), "high level of energy", "energy");
                List<Label> labelsEnergy = new ArrayList<>() {{
                    add(lowEnergy); add(energeticEnergy); add(moderateEnergy); add(highEnergy);
                }};
                return new LinguisticVariable(labelsEnergy, new ClassicSet(numbersEnergy), "energy");
            case "fiber":
                List<Double> numbersFiber = new ArrayList<>(){{
                    for (double i = 0.00; i <= 100.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label deficiencyFiber = new Label(new FuzzySet(new Triangular(0, 0, 30), new ClassicSet(numbersFiber)), "deficiency of fiber", "fiber");
                Label optimalFiber = new Label(new FuzzySet(new Triangular(15, 40, 70), new ClassicSet(numbersFiber)), "optimal fiber", "fiber");
                Label excessFiber = new Label(new FuzzySet(new Triangular(45, 100, 100), new ClassicSet(numbersFiber)), "excess of fiber", "fiber");
                List<Label> labelsFiber = new ArrayList<>() {{
                    add(deficiencyFiber); add(optimalFiber); add(excessFiber);
                }};
                return new LinguisticVariable(labelsFiber, new ClassicSet(numbersFiber), "fiber");
            case "protein":
                List<Double> numbersProtein = new ArrayList<>(){{
                    for (double i = 0.00; i <= 100.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label lowProtein = new Label(new FuzzySet(new Trapezoidal(0, 0, 7, 15), new ClassicSet(numbersProtein)), "low-protein", "protein");
                Label moderateProtein = new Label(new FuzzySet(new Trapezoidal(7, 15, 30, 45), new ClassicSet(numbersProtein)), "moderate-protein", "protein");
                Label highProtein = new Label(new FuzzySet(new Trapezoidal(30, 45, 100, 100), new ClassicSet(numbersProtein)), "high-protein", "protein");
                List<Label> labelsProtein = new ArrayList<>() {{
                    add(lowProtein); add(moderateProtein); add(highProtein);
                }};
                return new LinguisticVariable(labelsProtein, new ClassicSet(numbersProtein), "protein");
            case "sugar":
                List<Double> numbersSugar = new ArrayList<>(){{
                    for (double i = 0.00; i <= 100.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label lowSugar = new Label(new FuzzySet(new Gaussian(0, 10), new ClassicSet(numbersSugar)), "low level of sugar", "sugar");
                Label moderateSugar = new Label(new FuzzySet(new Gaussian(40, 30), new ClassicSet(numbersSugar)), "normal level of sugar", "sugar");
                Label highSugar = new Label(new FuzzySet(new Gaussian(100, 10), new ClassicSet(numbersSugar)), "excessive level of sugar", "sugar");
                List<Label> labelsSugar = new ArrayList<>() {{
                    add(lowSugar); add(moderateSugar); add(highSugar);
                }};
                return new LinguisticVariable(labelsSugar, new ClassicSet(numbersSugar), "sugar");
            case "fat":
                List<Double> numbersFat = new ArrayList<>(){{
                    for (double i = 0.00; i <= 100.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label freeFat = new Label(new FuzzySet(new Trapezoidal(0, 0, 2, 5), new ClassicSet(numbersFat)), "fat free", "fat");
                Label lowFat = new Label(new FuzzySet(new Trapezoidal(2, 6, 15, 20), new ClassicSet(numbersFat)), "low-fat", "fat");
                Label moderateFat = new Label(new FuzzySet(new Trapezoidal(15, 21, 40, 50), new ClassicSet(numbersFat)), "moderatly fatty", "fat");
                Label fatty = new Label(new FuzzySet(new Trapezoidal(40, 51, 70, 80), new ClassicSet(numbersFat)), "fatty", "fat");
                Label oily = new Label(new FuzzySet(new Trapezoidal(70, 81, 100, 100), new ClassicSet(numbersFat)), "oily", "fat");
                List<Label> labelsFat = new ArrayList<>() {{
                    add(freeFat); add(lowFat); add(moderateFat); add(fatty); add(oily);
                }};
                return new LinguisticVariable(labelsFat, new ClassicSet(numbersFat), "fat");
            case "magnesium":
                List<Double> numbersMagnesium = new ArrayList<>(){{
                    for (double i = 0.00; i <= 2300.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label deficiencyMagnesium = new Label(new FuzzySet(new Trapezoidal(0, 0, 30, 100), new ClassicSet(numbersMagnesium)), "deficiency of magnesium", "magnesium");
                Label lowMagnesium = new Label(new FuzzySet(new Trapezoidal(30, 100, 150, 300), new ClassicSet(numbersMagnesium)), "low level of magnesium", "magnesium");
                Label optimalMagnesium = new Label(new FuzzySet(new Trapezoidal(150, 300, 500, 600), new ClassicSet(numbersMagnesium)), "optimal level of magnesium", "magnesium");
                Label highMagnesium = new Label(new FuzzySet(new Trapezoidal(500, 600, 1000, 1200), new ClassicSet(numbersMagnesium)), "high level of magnesium", "magnesium");
                Label dangerouslyHighMagnesium = new Label(new FuzzySet(new Trapezoidal(1000, 1200, 2300, 2300), new ClassicSet(numbersMagnesium)), "dangerously high level of magnesium", "magnesium");
                List<Label> labelsMagnesium = new ArrayList<>() {{
                    add(deficiencyMagnesium); add(lowMagnesium); add(optimalMagnesium); add(highMagnesium); add(dangerouslyHighMagnesium);
                }};
                return new LinguisticVariable(labelsMagnesium, new ClassicSet(numbersMagnesium), "magnesium");
            case "vitamin c":
                List<Double> numbersC = new ArrayList<>(){{
                    for (double i = 0.00; i <= 4500.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label deficiencyC = new Label(new FuzzySet(new Trapezoidal(0, 0, 20, 40), new ClassicSet(numbersC)), "deficiency level of vitamin C", "vitamin C");
                Label lowC = new Label(new FuzzySet(new Trapezoidal(20, 40, 60, 80), new ClassicSet(numbersC)), "low level of vitamin C", "vitamin C");
                Label optimalC = new Label(new FuzzySet(new Trapezoidal(60, 80, 250, 500), new ClassicSet(numbersC)), "optimal level of vitamin C", "vitamin C");
                Label highC = new Label(new FuzzySet(new Trapezoidal(250, 500, 4500, 4500), new ClassicSet(numbersC)), "high level of vitamin C", "vitamin C");
                List<Label> labelsC = new ArrayList<>() {{
                    add(deficiencyC); add(lowC); add(optimalC); add(highC);
                }};
                return new LinguisticVariable(labelsC, new ClassicSet(numbersC), "vitamin C");
            case "vitamin b6":
                List<Double> numbersB6 = new ArrayList<>(){{
                    for (double i = 0.00; i <= 10.00; i += 0.01) {
                        add(i);
                    }
                }};
                Label deficiencyB6 = new Label(new FuzzySet(new Trapezoidal(0, 0, 0.5, 1), new ClassicSet(numbersB6)), "deficiency level of vitamin B6", "vitamin B6");
                Label optimalB6 = new Label(new FuzzySet(new Trapezoidal(0.5, 1, 1.25, 1.5), new ClassicSet(numbersB6)), "optimal level of vitamin B6", "vitamin B6");
                Label highB6 = new Label(new FuzzySet(new Trapezoidal(1.25, 1.5, 2, 2.5), new ClassicSet(numbersB6)), "high level of vitamin B6", "vitamin B6");
                Label excessiveB6 = new Label(new FuzzySet(new Trapezoidal(2, 2.5, 10, 10), new ClassicSet(numbersB6)), "excessive level of vitamin B6", "vitamin B6");
                List<Label> labelsB6 = new ArrayList<>() {{
                    add(deficiencyB6); add(optimalB6); add(highB6); add(excessiveB6);
                }};
                return new LinguisticVariable(labelsB6, new ClassicSet(numbersB6), "vitamin B6");
            default:
                return null;
        }
    }

    public static Quantifier createQuantifier(String name, boolean isAbsolute) {
        if (isAbsolute) {
            List<Double> numbersAbsolute = new ArrayList<>() {{
                for (double i = 0.00; i <= 15000.00; i += 0.01) {
                    add(i);
                }
            }};
            switch (name){
                case "less than 3000":
                    return new Quantifier(new FuzzySet(new Trapezoidal(0, 3000, 3000, 3000),new ClassicSet(numbersAbsolute)), "Less than 3000", true);
                case "around 5000":
                    return new Quantifier(new FuzzySet(new Trapezoidal(2000, 4000, 6000, 9000),new ClassicSet(numbersAbsolute)), "Around 5000", true);
                case "around 10000":
                    return new Quantifier(new FuzzySet(new Trapezoidal(6000, 9000, 11000, 13000),new ClassicSet(numbersAbsolute)), "Around 10000", true);
                case "over 12000":
                    return new Quantifier(new FuzzySet(new Trapezoidal(12000, 12000, 12000, 15000),new ClassicSet(numbersAbsolute)), "Over 12000", true);
                default:
                    return null;
            }
        } else {
            List<Double> numbersRel = new ArrayList<>() {{
                for (double i = 0.00; i <= 1.00; i += 0.01) {
                    add(i);
                }
            }};
            switch (name){
                case "almost none":
                    return new Quantifier(new FuzzySet(new Trapezoidal(0, 0, 0.1, 0.2),new ClassicSet(numbersRel)), "almost none", false);
                case "minority":
                    return new Quantifier(new FuzzySet(new Trapezoidal(0.1, 0.2, 0.3, 0.4),new ClassicSet(numbersRel)), "minority", false);
                case "about half":
                    return new Quantifier(new FuzzySet(new Trapezoidal(0.3, 0.4, 0.6, 0.7),new ClassicSet(numbersRel)), "about half", false);
                case "majority":
                    return new Quantifier(new FuzzySet(new Trapezoidal(0.6, 0.7, 0.8, 0.9),new ClassicSet(numbersRel)), "majority", false);
                case "almost all":
                    return new Quantifier(new FuzzySet(new Trapezoidal(0.8, 0.9, 1, 1),new ClassicSet(numbersRel)), "almost all", false);
                default:
                    return null;
            }
        }
    }
}
