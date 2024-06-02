package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FoodEntry {
    private long id;
    private double servingSize;
    private String ingredients;
    private double carbohydrate;
    private double cholesterol;
    private double energy;
    private double fiber;
    private double protein;
    private double sugar;
    private double fat;
    private double magnesium;
    private double vitaminC;
    private double vitaminB6;

    public double getValueByName(String variableName) {
        switch (variableName) {
            case "servingSize":
                return servingSize;
            case "carbohydrates":
                return carbohydrate;
            case "cholesterol":
                return cholesterol;
            case "energy":
                return energy;
            case "fiber":
                return fiber;
            case "protein":
                return protein;
            case "sugar":
                return sugar;
            case "fat":
                return fat;
            case "magnesium":
                return magnesium;
            case "vitamin c":
                return vitaminC;
            case "vitamin b6":
                return vitaminB6;
            default:
                throw new IllegalArgumentException("Invalid variable name: " + variableName);
        }
    }
}
