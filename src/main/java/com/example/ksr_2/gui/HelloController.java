package com.example.ksr_2.gui;

import com.example.ksr_2.Label;
import com.example.ksr_2.*;
import com.example.ksr_2.functions.Gaussian;
import com.example.ksr_2.functions.MembershipFunction;
import com.example.ksr_2.functions.Trapezoidal;
import com.example.ksr_2.functions.Triangular;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;

public class HelloController implements Initializable {
    @FXML
    private ListView<String> listViewQ, listViewW, listViewS;

    @FXML
    private TextField t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11;
    @FXML
    private TextField nameQ, bQ, aQ, cQ, dQ, bS, aS, cS, dS, nameS, startS, endS, nameSLing;

    @FXML
    private LineChart<Number, Number> chartS, chartQ;

    @FXML
    private MenuItem carbohydrates, cholesterol, energy, fiber, protein, sugar, fat, magnesium, vitaminC, vitaminB6;

    @FXML
    private TabPane tabPaneOne, tabPaneFun;

    @FXML
    private ListView<String> view;

    private ObservableList<String> summariesList, summariesTwoList;
    private List<Summary> summaries;
    private List<SummaryTwo> summariesTwo;
    private List<SummaryTwoSecond> summariesTwoSecond;

    private List<Quantifier> quantifiersNew;
    private List<Label> summariesNew;

    private boolean isAbs, gauss, trap, trian, gaussS, trapS, trianS;

    private String selectedLingVariable;
    private List<Double> selectedUniverse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeListViewQ();
        initializeListViewW();
        initializeListViewS();
        initializeMenuItems();
    }

    private void initializeMenuItems() {
        carbohydrates.setOnAction(event -> updateLingVariable("carbohydrates", generateUniverse(0, 300)));
        cholesterol.setOnAction(event -> updateLingVariable("cholesterol", generateUniverse(0, 350)));
        energy.setOnAction(event -> updateLingVariable("energy", generateUniverse(0, 1300)));
        fiber.setOnAction(event -> updateLingVariable("fiber", generateUniverse(0, 100)));
        protein.setOnAction(event -> updateLingVariable("protein", generateUniverse(0, 100)));
        sugar.setOnAction(event -> updateLingVariable("sugar", generateUniverse(0, 100)));
        fat.setOnAction(event -> updateLingVariable("fat", generateUniverse(0, 100)));
        magnesium.setOnAction(event -> updateLingVariable("magnesium", generateUniverse(0, 2300)));
        vitaminC.setOnAction(event -> updateLingVariable("vitamin C", generateUniverse(0, 4500)));
        vitaminB6.setOnAction(event -> updateLingVariable("vitamin B6", generateUniverse(0, 10)));
    }

    private void updateLingVariable(String variable, List<Double> universe) {
        selectedLingVariable = variable;
        selectedUniverse = universe;
    }

    private List<Double> generateUniverse(double start, double end) {
        List<Double> universe = new ArrayList<>();
        for (double i = start; i <= end; i += 1) {
            universe.add(i);
        }
        return universe;
    }

    private void initializeListViewQ() {
        listViewQ.setItems(FXCollections.observableArrayList(
                "Almost none", "Minority", "About half", "Majority", "Almost all",
                "Less than 3000", "Around 5000", "Around 10000", "Over 12000"
        ));
        listViewQ.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initializeListViewW() {
        listViewW.setItems(FXCollections.observableArrayList(
                "low level of carbohydrates", "moderate level of carbohydrates", "high level of carbohydrates",
                "low level of cholesterol", "optimal level of cholesterol", "high level of cholesterol",
                "low level of energy", "moderate level of energy", "energetic", "high level of energy",
                "deficiency of fiber", "optimal fiber", "excess of fiber",
                "low-protein", "moderate-protein", "high-protein",
                "low level of sugar", "normal level of sugar", "excessive level of sugar",
                "fat free", "low-fat", "moderatly fatty", "fatty", "oily",
                "deficiency of magnesium", "low level of magnesium", "optimal level of magnesium", "high level of magnesium", "dangerously high level of magnesium",
                "deficiency level of vitamin C", "low level of vitamin C", "optimal level of vitamin C", "high level of vitamin C",
                "deficiency level of vitamin B6", "optimal level of vitamin B6", "high level of vitamin B6", "excessive level of vitamin B6",
                "User's"
        ));
        listViewW.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initializeListViewS() {
        listViewS.setItems(FXCollections.observableArrayList(
                "low level of carbohydrates", "moderate level of carbohydrates", "high level of carbohydrates",
                "low level of cholesterol", "optimal level of cholesterol", "high level of cholesterol",
                "low level of energy", "moderate level of energy", "energetic", "high level of energy",
                "deficiency of fiber", "optimal fiber", "excess of fiber",
                "low-protein", "moderate-protein", "high-protein",
                "low level of sugar", "normal level of sugar", "excessive level of sugar",
                "fat free", "low-fat", "moderatly fatty", "fatty", "oily",
                "deficiency of magnesium", "low level of magnesium", "optimal level of magnesium", "high level of magnesium", "dangerously high level of magnesium",
                "deficiency level of vitamin C", "low level of vitamin C", "optimal level of vitamin C", "high level of vitamin C",
                "deficiency level of vitamin B6", "optimal level of vitamin B6", "high level of vitamin B6", "excessive level of vitamin B6",
                "User's"
        ));
        listViewS.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void chooseGenerate() {
        tabPaneOne.setVisible(true);
        tabPaneFun.setVisible(false);
    }

    @FXML
    public void chooseCreate() {
        tabPaneOne.setVisible(false);
        tabPaneFun.setVisible(true);
    }

    private void drawMembershipFunctionChart(LineChart<Number, Number> chart, MembershipFunction function, List<Double> universe) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (double x : universe) {
            series.getData().add(new XYChart.Data<>(x, function.getMembership(x)));
        }
        chart.getData().clear();
        chart.getData().add(series);
    }

    @FXML
    public void createS() {
        MembershipFunction membershipFunction = getSelectedMembershipFunction(trapS, gaussS, trianS, aS, bS, cS, dS);
        if (membershipFunction == null) return;

        ClassicSet classicSet = new ClassicSet(selectedUniverse);
        FuzzySet fuzzySet = new FuzzySet(membershipFunction, classicSet);
        Label summarizer = new Label(fuzzySet, nameS.getText(), selectedLingVariable);
        summariesNew = new ArrayList<>();
        summariesNew.add(summarizer);

        listViewS.getItems().add(nameS.getText());

        listViewW.getItems().add(nameS.getText());

        drawMembershipFunctionChart(chartS, membershipFunction, selectedUniverse);
    }

    @FXML
    public void chooseGaussS() {
        setVisibilityForMembershipFunctionParams(true, true, false, false, "stdev", "mean");
        gaussS = true;
        trapS = trianS = false;
    }

    @FXML
    public void chooseTrapS() {
        setVisibilityForMembershipFunctionParams(true, true, true, true, "a", "b", "c", "d");
        trapS = true;
        gaussS = trianS = false;
    }

    @FXML
    public void chooseTrianS() {
        setVisibilityForMembershipFunctionParams(true, true, true, false, "a", "b", "c");
        trianS = true;
        gaussS = trapS = false;
    }

    private void setVisibilityForMembershipFunctionParams(boolean aVisible, boolean bVisible, boolean cVisible, boolean dVisible, String... prompts) {
        aS.setVisible(aVisible);
        bS.setVisible(bVisible);
        cS.setVisible(cVisible);
        dS.setVisible(dVisible);
        if (prompts.length > 0) aS.setPromptText(prompts[0]);
        if (prompts.length > 1) bS.setPromptText(prompts[1]);
        if (prompts.length > 2) cS.setPromptText(prompts[2]);
        if (prompts.length > 3) dS.setPromptText(prompts[3]);
    }

    @FXML
    public void createQ() {
        MembershipFunction membershipFunction = getSelectedMembershipFunction(trap, gauss, trian, aQ, bQ, cQ, dQ);
        if (membershipFunction == null) return;

        List<Double> universe = isAbs ? generateUniverse(0, 15261) : generateUniverse(0, 1);
        ClassicSet classicSet = new ClassicSet(universe);
        FuzzySet fuzzySet = new FuzzySet(membershipFunction, classicSet);
        Quantifier quantifier = new Quantifier(fuzzySet, nameQ.getText(), isAbs);
        quantifiersNew = new ArrayList<>();
        quantifiersNew.add(quantifier);

        listViewQ.getItems().add(nameQ.getText());
        drawMembershipFunctionChart(chartQ, membershipFunction, universe);
    }

    @FXML
    public void chooseGauss() {
        gauss = true;
        trap = false;
        trian = false;
        setFunctionParametersVisibility(true, true, false, false);
        aQ.setPromptText("stdev");
        bQ.setPromptText("mean");
    }

    @FXML
    public void chooseTrap() {
        trap = true;
        gauss = false;
        trian = false;
        setFunctionParametersVisibility(true, true, true, true);
        aQ.setPromptText("a");
        bQ.setPromptText("b");
        cQ.setPromptText("c");
        dQ.setPromptText("d");
    }

    @FXML
    public void chooseTrian() {
        trian = true;
        gauss = false;
        trap = false;
        setFunctionParametersVisibility(true, true, true, false);
        aQ.setPromptText("a");
        bQ.setPromptText("b");
        cQ.setPromptText("c");
    }

    private void setFunctionParametersVisibility(boolean aVisible, boolean bVisible, boolean cVisible, boolean dVisible) {
        aQ.setVisible(aVisible);
        bQ.setVisible(bVisible);
        cQ.setVisible(cVisible);
        dQ.setVisible(dVisible);
    }

    @FXML
    public void chooseAbs() {
        isAbs = true;
    }

    @FXML
    public void chooseRel() {
        isAbs = false;
    }

    private MembershipFunction getSelectedMembershipFunction(boolean trap, boolean gauss, boolean trian, TextField a, TextField b, TextField c, TextField d) {
        if (gauss) {
            return new Gaussian(Double.parseDouble(b.getText()), Double.parseDouble(a.getText()));
        }
        if (trap) {
            return new Trapezoidal(Double.parseDouble(a.getText()), Double.parseDouble(b.getText()),
                    Double.parseDouble(c.getText()), Double.parseDouble(d.getText()));
        }
        if (trian) {
            return new Triangular(Double.parseDouble(a.getText()), Double.parseDouble(b.getText()), Double.parseDouble(c.getText()));
        }
        return null;
    }

    @FXML
    private void generateTwo() {
        List<CheckedItem> checkedItemsQ = getCheckedItems(listViewQ);
        List<Quantifier> checkedNewQs = new ArrayList<>();
        if (quantifiersNew != null) {
            for (Quantifier quantifier : quantifiersNew) {
                for (CheckedItem checkedItem : checkedItemsQ) {
                    if (quantifier.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewQs.add(quantifier);
                        checkedItemsQ.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsW = getCheckedItems(listViewW);
        List<Label> checkedNewWs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label : summariesNew) {
                for (CheckedItem checkedItem : checkedItemsW) {
                    if (label.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewWs.add(label);
                        checkedItemsW.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsS = getCheckedItems(listViewS);
        List<Label> checkedNewSs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label : summariesNew) {
                for (CheckedItem checkedItem : checkedItemsS) {
                    if (label.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewSs.add(label);
                        checkedItemsS.remove(checkedItem);
                        break;
                    }
                }
            }
        }

        List<FoodEntry> foodEntries = loadFoodEntries();
        List<List<FoodEntry>> split = splitFoodEntry(foodEntries);

        List<Label> ws = new ArrayList<>();
        List<Label> ss = new ArrayList<>();

        List<String> uniqueW = checkedItemsW.stream()
                .map(CheckedItem::getCategory).distinct().collect(Collectors.toList());

        List<String> uniqueS = checkedItemsS.stream()
                .map(CheckedItem::getCategory).distinct().collect(Collectors.toList());

        for (String name : uniqueW) {
            ws.addAll(LabelFactory.createLinguisticVariable(name.toLowerCase()).getLabels());
        }

        for (String name : uniqueS) {
            ss.addAll(LabelFactory.createLinguisticVariable(name.toLowerCase()).getLabels());
        }

        Set<String> checkedItemNamesW = checkedItemsW.stream()
                .map(CheckedItem::getItem)
                .collect(Collectors.toSet());

        Set<String> checkedItemNamesS = checkedItemsS.stream()
                .map(CheckedItem::getItem)
                .collect(Collectors.toSet());

        ws.removeIf(label -> !checkedItemNamesW.contains(label.getLabelName()));

        ss.removeIf(label -> !checkedItemNamesS.contains(label.getLabelName()));

        ss.addAll(checkedNewSs);
        ws.addAll(checkedNewWs);

        summariesTwo = new ArrayList<>();
        summariesTwoSecond = new ArrayList<>();
        for (CheckedItem checkedItem : checkedItemsQ) {
            Quantifier quantifier = LabelFactory.createQuantifier(checkedItem.getItem().toLowerCase(), checkedItem.getCategory().equals("Absolutne"));
            if (ws.isEmpty()) {
                summariesTwo.add(new SummaryTwo(quantifier, ss, split.get(0), split.get(1)));
                summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ss, split.get(1), split.get(0)));
            } else {
                summariesTwo.add(new SummaryTwo(quantifier, ws, ss, split.get(0), split.get(1)));
                summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ws, ss, split.get(1), split.get(0)));
            }
        }
        for (Quantifier quantifier : checkedNewQs) {
            if (ws.isEmpty()) {
                summariesTwo.add(new SummaryTwo(quantifier, ss, split.get(0), split.get(1)));
                summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ss, split.get(1), split.get(0)));
            } else {
                summariesTwo.add(new SummaryTwo(quantifier, ws, ss, split.get(0), split.get(1)));
                summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ws, ss, split.get(1), split.get(0)));
            }
        }
        generateResultsTwo();
    }


    @FXML
    private void generateOne() {
        List<CheckedItem> checkedItemsQ = getCheckedItems(listViewQ);
        List<Quantifier> checkedNewQs = new ArrayList<>();
        if (quantifiersNew != null) {
            for (Quantifier quantifier : quantifiersNew) {
                for (CheckedItem checkedItem : checkedItemsQ) {
                    if (quantifier.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewQs.add(quantifier);
                        checkedItemsQ.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsW = getCheckedItems(listViewW);
        List<Label> checkedNewWs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label : summariesNew) {
                for (CheckedItem checkedItem : checkedItemsW) {
                    if (label.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewWs.add(label);
                        checkedItemsW.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsS = getCheckedItems(listViewS);
        List<Label> checkedNewSs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label : summariesNew) {
                for (CheckedItem checkedItem : checkedItemsS) {
                    if (label.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewSs.add(label);
                        checkedItemsS.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<Double> weights;

        List<FoodEntry> foodEntries = loadFoodEntries();

        if (t1.getText().isEmpty()) {
            weights = Arrays.asList(0.5, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05);
        } else {
            weights = Arrays.asList(
                    Double.valueOf(t1.getText()), Double.valueOf(t2.getText()), Double.valueOf(t3.getText()),
                    Double.valueOf(t4.getText()), Double.valueOf(t5.getText()), Double.valueOf(t6.getText()),
                    Double.valueOf(t7.getText()), Double.valueOf(t8.getText()), Double.valueOf(t9.getText()),
                    Double.valueOf(t10.getText()), Double.valueOf(t11.getText())
            );
        }

        List<Label> ws = new ArrayList<>();
        List<Label> ss = new ArrayList<>();

        List<String> uniqueW = checkedItemsW.stream()
                .map(CheckedItem::getCategory).distinct().collect(Collectors.toList());

        List<String> uniqueS = checkedItemsS.stream()
                .map(CheckedItem::getCategory).distinct().collect(Collectors.toList());

        for (String name : uniqueW) {
            ws.addAll(LabelFactory.createLinguisticVariable(name.toLowerCase()).getLabels());
        }

        for (String name : uniqueS) {
            ss.addAll(LabelFactory.createLinguisticVariable(name.toLowerCase()).getLabels());
        }

        Set<String> checkedItemNamesW = checkedItemsW.stream()
                .map(CheckedItem::getItem)
                .collect(Collectors.toSet());

        Set<String> checkedItemNamesS = checkedItemsS.stream()
                .map(CheckedItem::getItem)
                .collect(Collectors.toSet());

        ws.removeIf(label -> !checkedItemNamesW.contains(label.getLabelName()));

        ss.removeIf(label -> !checkedItemNamesS.contains(label.getLabelName()));

        ws.addAll(checkedNewWs);
        ss.addAll(checkedNewSs);

        summaries = new ArrayList<>();
        for (CheckedItem checkedItem : checkedItemsQ) {
            Quantifier quantifier = LabelFactory.createQuantifier(checkedItem.getItem().toLowerCase(), checkedItem.getCategory().equals("Absolutne"));
            if (ws.isEmpty()) {
                summaries.add(new Summary(quantifier, ss, foodEntries, weights));
            } else {
                summaries.add(new Summary(quantifier, ws, ss, foodEntries, weights));
            }
        }
        for (Quantifier quantifier : checkedNewQs) {
            if (ws.isEmpty()) {
                summaries.add(new Summary(quantifier, ss, foodEntries, weights));
            } else {
                summaries.add(new Summary(quantifier, ws, ss, foodEntries, weights));
            }
        }
        generateResults();
    }


    private List<FoodEntry> loadFoodEntries() {
        List<FoodEntry> foodEntries = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/com/example/ksr_2/filtered_data.csv"))) {
            String[] line;
            reader.readNext();

            long idCounter = 1;
            while ((line = reader.readNext()) != null) {
                FoodEntry entry = new FoodEntry(
                        idCounter++,
                        Double.parseDouble(line[1]),
                        line[2],
                        Double.parseDouble(line[3]),
                        Double.parseDouble(line[4]),
                        Double.parseDouble(line[5]),
                        Double.parseDouble(line[6]),
                        Double.parseDouble(line[7]),
                        Double.parseDouble(line[8]),
                        Double.parseDouble(line[9]),
                        Double.parseDouble(line[10]),
                        Double.parseDouble(line[11]),
                        Double.parseDouble(line[12])
                );
                foodEntries.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foodEntries;
    }

    private List<List<FoodEntry>> splitFoodEntry(List<FoodEntry> foodEntries) {
        List<List<FoodEntry>> meatNotMeat = new ArrayList<>();
        List<FoodEntry> meat = new ArrayList<>();
        List<FoodEntry> notMeat = new ArrayList<>();
        List<String> meats = Arrays.asList("meat", "beef", "chicken", "pork", "lamb", "turkey", "duck", "goose", "venison", "rabbit");

        for (FoodEntry foodEntry : foodEntries) {
            boolean isMeat = false;
            for (String meatType : meats) {
                if (foodEntry.getIngredients().toLowerCase().contains(meatType)) {
                    isMeat = true;
                    break;
                }
            }

            if (isMeat) {
                meat.add(foodEntry);
            } else {
                notMeat.add(foodEntry);
            }
        }
        meatNotMeat.add(meat);
        meatNotMeat.add(notMeat);
        return meatNotMeat;
    }

    public void generateResultsTwo() {
        List<TwoSummary> twoSummaries = new ArrayList<>();
        for (SummaryTwo summaryTwo : summariesTwo) {
            List<TwoSummary> generatedSummaries = summaryTwo.generateAllDualSummaries();
            twoSummaries.addAll(generatedSummaries);
        }

        for (SummaryTwoSecond summaryTwoSecond : summariesTwoSecond) {
            List<TwoSummary> generatedSummaries = summaryTwoSecond.generateAllDualSummaries();
            twoSummaries.addAll(generatedSummaries);
        }

        twoSummaries.sort(Comparator.comparingDouble(TwoSummary::getQuality).reversed());

        for (TwoSummary twoSummary : twoSummaries) {
            twoSummary.saveToCsv();
        }

        summariesTwoList = FXCollections.observableArrayList(
                twoSummaries.stream().map(TwoSummary::toString).collect(Collectors.toList())
        );
        for (String oneSummary : summariesTwoList) {
            System.out.println(oneSummary);
        }
        view.setItems(summariesTwoList);
    }


    public void generateResults() {
        List<OneSummary> oneSummaries = new ArrayList<>();
        for (Summary summary : summaries) {
            List<OneSummary> generatedSummaries = summary.generateAllOneSummaries();
            oneSummaries.addAll(generatedSummaries);
        }
        oneSummaries.sort(Comparator.comparingDouble(OneSummary::getQuality).reversed());

        for (OneSummary oneSummary : oneSummaries) {
            oneSummary.saveToCsv();
        }

        summariesList = FXCollections.observableArrayList(
                oneSummaries.stream().map(OneSummary::toString).collect(Collectors.toList())
        );
        for (String oneSummary : summariesList) {
            System.out.println(oneSummary);
        }
        view.setItems(summariesList);
    }


    public List<CheckedItem> getCheckedItems(ListView<String> listView) {
        List<CheckedItem> checkedItems = new ArrayList<>();
        ObservableList<String> selectedItems = listView.getSelectionModel().getSelectedItems();

        for (String item : selectedItems) {
            String category = determineCategory(item);
            checkedItems.add(new CheckedItem(category, item));
        }

        return checkedItems;
    }

    private String determineCategory(String itemName) {
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("Almost none", "Relatywne");
        categoryMap.put("Minority", "Relatywne");
        categoryMap.put("About half", "Relatywne");
        categoryMap.put("Majority", "Relatywne");
        categoryMap.put("Almost all", "Relatywne");
        categoryMap.put("Less than 3000", "Absolutne");
        categoryMap.put("Around 5000", "Absolutne");
        categoryMap.put("Around 10000", "Absolutne");
        categoryMap.put("Over 12000", "Absolutne");
        categoryMap.put("low level of carbohydrates", "Carbohydrates");
        categoryMap.put("moderate level of carbohydrates", "Carbohydrates");
        categoryMap.put("high level of carbohydrates", "Carbohydrates");
        categoryMap.put("low level of cholesterol", "Cholesterol");
        categoryMap.put("optimal level of cholesterol", "Cholesterol");
        categoryMap.put("high level of cholesterol", "Cholesterol");
        categoryMap.put("low level of energy", "Energy");
        categoryMap.put("moderate level of energy", "Energy");
        categoryMap.put("energetic", "Energy");
        categoryMap.put("high level of energy", "Energy");
        categoryMap.put("deficiency of fiber", "Fiber");
        categoryMap.put("optimal fiber", "Fiber");
        categoryMap.put("excess of fiber", "Fiber");
        categoryMap.put("low-protein", "Protein");
        categoryMap.put("moderate-protein", "Protein");
        categoryMap.put("high-protein", "Protein");
        categoryMap.put("low level of sugar", "Sugar");
        categoryMap.put("normal level of sugar", "Sugar");
        categoryMap.put("excessive level of sugar", "Sugar");
        categoryMap.put("fat free", "Fat");
        categoryMap.put("low-fat", "Fat");
        categoryMap.put("moderatly fatty", "Fat");
        categoryMap.put("fatty", "Fat");
        categoryMap.put("oily", "Fat");
        categoryMap.put("deficiency of magnesium", "Magnesium");
        categoryMap.put("low level of magnesium", "Magnesium");
        categoryMap.put("optimal level of magnesium", "Magnesium");
        categoryMap.put("high level of magnesium", "Magnesium");
        categoryMap.put("dangerously high level of magnesium", "Magnesium");
        categoryMap.put("deficiency level of vitamin C", "Vitamin C");
        categoryMap.put("low level of vitamin C", "Vitamin C");
        categoryMap.put("optimal level of vitamin C", "Vitamin C");
        categoryMap.put("high level of vitamin C", "Vitamin C");
        categoryMap.put("deficiency level of vitamin B6", "Vitamin B6");
        categoryMap.put("optimal level of vitamin B6", "Vitamin B6");
        categoryMap.put("high level of vitamin B6", "Vitamin B6");
        categoryMap.put("excessive level of vitamin B6", "Vitamin B6");
        categoryMap.put("User's", "User's");

        return categoryMap.getOrDefault(itemName, "Unknown");
    }


}
