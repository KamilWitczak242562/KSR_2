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
public class OneSummary {
    private String summary;
    private Measures measures;
    private double quality;

    public OneSummary(String summary, Measures measures) {
        this.summary = summary;
        this.measures = measures;
        this.quality = measures.getT1degreeOfTruth();
    }

    @Override
    public String toString() {
        return summary + "[" + measures + "]";
    }

    public void saveToCsv() {
        File file = new File("one_summary.csv");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            CSVWriter writer = new CSVWriter(fileWriter);
//            String[] header = {"Sentence", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9", "t10", "t11", "T"};
//            writer.writeNext(header);

            String[] data1 = {summary, Double.toString(measures.getT1degreeOfTruth()), Double.toString(measures.getT2degreeOfImprecision()),
                    Double.toString(measures.getT3degreeOfCovering()), Double.toString(measures.getT4degreeOfAppropriateness()),
                    Double.toString(measures.getT5lengthOfSummary()), Double.toString(measures.getT6degreeOfQuantifierImprecision()),
                    Double.toString(measures.getT7degreeOfQuantifierCardinality()), Double.toString(measures.getT8degreeOfSummarizerCardinality()),
                    Double.toString(measures.getT9degreeOfQualifierImprecision()), Double.toString(measures.getT10degreeOfQualifierCardinality()),
                    Double.toString(measures.getT11lengthOfQualifier()), Double.toString(measures.getQualityOfSummary())};
            writer.writeNext(data1);
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
