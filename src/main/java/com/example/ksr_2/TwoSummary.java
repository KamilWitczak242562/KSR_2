package com.example.ksr_2;

import com.opencsv.CSVWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
public class TwoSummary {
    private String summary;
    private double quality;

    @Override
    public String toString() {
        return summary + "[" + (double) Math.round(quality * 100) / 100 + "]";
    }

    public void saveToCsv() {
        File file = new File("two_summary.csv");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            CSVWriter writer = new CSVWriter(fileWriter);
//            String[] header = {"Sentence", "T"};
//            writer.writeNext(header);

            String[] data1 = {summary, Double.toString(quality)};
            writer.writeNext(data1);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
