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
import javafx.scene.chart.Chart;
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
    private TreeView<String> Q;
    @FXML
    private TreeView<String> W;
    @FXML
    private TreeView<String> S;

    @FXML
    private TextField t1;

    @FXML
    private TextField t2;

    @FXML
    private TextField t3;

    @FXML
    private TextField t4;

    @FXML
    private TextField t5;

    @FXML
    private TextField t6;

    @FXML
    private TextField t7;

    @FXML
    private TextField t8;

    @FXML
    private TextField t9;

    @FXML
    private TextField t10;

    @FXML
    private TextField t11;

    @FXML
    private ListView<String> view;

    @FXML
    private TabPane tabPaneOne;

    @FXML
    private TabPane tabPaneFun;

    @FXML
    private TextField nameQ;

    @FXML
    private TextField startQ;

    @FXML
    private TextField endQ;

    @FXML
    private TextField bQ;

    @FXML
    private TextField aQ;

    @FXML
    private TextField cQ;

    @FXML
    private TextField dQ;

    @FXML
    private TextField bS;

    @FXML
    private TextField aS;

    @FXML
    private TextField cS;

    @FXML
    private TextField dS;

    @FXML
    private TextField nameS;

    @FXML
    private TextField startS;

    @FXML
    private TextField endS;

    @FXML
    private TextField nameSLing;

    @FXML
    private LineChart chartS;

    @FXML
    private LineChart chartQ;

    private ObservableList<String> summariesList;
    private ObservableList<String> summariesTwoList;

    private List<Summary> summaries;
    private List<SummaryTwo> summariesTwo;
    private List<SummaryTwoSecond> summariesTwoSecond;

    private List<Quantifier> quantifiersNew;
    private List<Label> summariesNew;

    private boolean isAbs;
    private boolean gauss;
    private boolean trap;
    private boolean trian;

    private boolean gaussS;
    private boolean trapS;
    private boolean trianS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTreeViewQ();
        initializeTreeViewW();
        initializeTreeViewS();
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
        List<Double> newUniverse = new ArrayList<>();
        for (double i = universe.get(0); i < universe.get(universe.size()-1); i++) {
            newUniverse.add(i);
        }
        for (double x : newUniverse) {
            series.getData().add(new XYChart.Data<>(x, function.getMembership(x)));
        }
        chart.getData().clear();
        chart.getData().add(series);
    }

    @FXML
    public void createS() {
        MembershipFunction membershipFunction = null;
        summariesNew = new ArrayList<>();
        List<Double> universe = new ArrayList<>(){{
            for (double i = Double.parseDouble(startS.getText()); i <= Double.parseDouble(endS.getText()); i += 0.01) {
                add(i);
            }
        }};
        Label summarizer;
        ClassicSet classicSet = new ClassicSet(universe);
        FuzzySet fuzzySet;
        if (gaussS) {
            membershipFunction = new Gaussian(Double.parseDouble(bS.getText()), Double.parseDouble(aS.getText()));
        }
        if (trapS) {
            membershipFunction = new Trapezoidal(Double.parseDouble(aS.getText()), Double.parseDouble(bS.getText()),
                    Double.parseDouble(cS.getText()), Double.parseDouble(dS.getText()));
        }
        if (trianS) {
            membershipFunction = new Triangular(Double.parseDouble(aS.getText()), Double.parseDouble(bS.getText()), Double.parseDouble(cS.getText()));
        }
        fuzzySet= new FuzzySet(membershipFunction, classicSet);
        summarizer = new Label(fuzzySet, nameS.getText(), nameSLing.getText());
        summariesNew.add(summarizer);

        TreeItem<String> newSItem = createCheckBoxTreeItem(nameS.getText());
        TreeItem<String> absoluteNode = findTreeItem(S.getRoot(), "User's");
        if (absoluteNode != null) {
            absoluteNode.getChildren().add(newSItem);
        }
        TreeItem<String> absoluteNodeW = findTreeItem(W.getRoot(), "User's");
        if (absoluteNodeW != null) {
            absoluteNodeW.getChildren().add(newSItem);
        }

        drawMembershipFunctionChart(chartS, membershipFunction, universe);
    }

    @FXML
    public void chooseGaussS() {
        gaussS = true;
        trapS = false;
        trianS = false;
        aS.setVisible(true);
        bS.setVisible(true);
        aS.setPromptText("stdev");
        bS.setPromptText("mean");
        cS.setVisible(false);
        dS.setVisible(false);
    }

    @FXML
    public void chooseTrapS() {
        trapS = true;
        gaussS = false;
        trianS = false;
        aS.setVisible(true);
        bS.setVisible(true);
        cS.setVisible(true);
        dS.setVisible(true);
        aS.setPromptText("a");
        bS.setPromptText("b");
        cS.setPromptText("c");
        dS.setPromptText("d");
    }

    @FXML
    public void chooseTrianS() {
        trapS = false;
        gaussS = false;
        trianS = true;
        aS.setVisible(true);
        bS.setVisible(true);
        cS.setVisible(true);
        dS.setVisible(false);
        aS.setPromptText("a");
        bS.setPromptText("b");
        cS.setPromptText("c");
    }

    @FXML
    public void createQ() {
        MembershipFunction membershipFunction = null;
        quantifiersNew = new ArrayList<>();
        List<Double> universe = new ArrayList<>(){{
            for (double i = Double.parseDouble(startQ.getText()); i <= Double.parseDouble(endQ.getText()); i += 0.01) {
                add(i);
            }
        }};
        Quantifier quantifier;
        ClassicSet classicSet = new ClassicSet(universe);
        FuzzySet fuzzySet;
        if (gauss) {
            membershipFunction = new Gaussian(Double.parseDouble(bQ.getText()), Double.parseDouble(aQ.getText()));
        }
        if (trap) {
            membershipFunction = new Trapezoidal(Double.parseDouble(aQ.getText()), Double.parseDouble(bQ.getText()),
                    Double.parseDouble(cQ.getText()), Double.parseDouble(dQ.getText()));
        }
        if (trian) {
            membershipFunction = new Triangular(Double.parseDouble(aQ.getText()), Double.parseDouble(bQ.getText()), Double.parseDouble(cQ.getText()));
        }
        fuzzySet= new FuzzySet(membershipFunction, classicSet);
        quantifier = new Quantifier(fuzzySet, nameQ.getText(), isAbs);
        quantifiersNew.add(quantifier);

        TreeItem<String> newQuantifierItem = createCheckBoxTreeItem(nameQ.getText());
        if (isAbs) {
            TreeItem<String> absoluteNode = findTreeItem(Q.getRoot(), "Absolutne");
            if (absoluteNode != null) {
                absoluteNode.getChildren().add(newQuantifierItem);
            }
        } else {
            TreeItem<String> relativeNode = findTreeItem(Q.getRoot(), "Relatywne");
            if (relativeNode != null) {
                relativeNode.getChildren().add(newQuantifierItem);
            }
        }

        drawMembershipFunctionChart(chartQ, membershipFunction, universe);
    }

    private TreeItem<String> findTreeItem(TreeItem<String> root, String value) {
        if (root.getValue().equals(value)) {
            return root;
        }
        for (TreeItem<String> child : root.getChildren()) {
            TreeItem<String> result = findTreeItem(child, value);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @FXML
    public void chooseGauss() {
        gauss = true;
        trap = false;
        trian = false;
        aQ.setVisible(true);
        bQ.setVisible(true);
        aQ.setPromptText("stdev");
        bQ.setPromptText("mean");
        cQ.setVisible(false);
        dQ.setVisible(false);
    }

    @FXML
    public void chooseTrap() {
        trap = true;
        gauss = false;
        trian = false;
        aQ.setVisible(true);
        bQ.setVisible(true);
        cQ.setVisible(true);
        dQ.setVisible(true);
        aQ.setPromptText("a");
        bQ.setPromptText("b");
        cQ.setPromptText("c");
        dQ.setPromptText("d");
    }

    @FXML
    public void chooseTrian() {
        trap = false;
        gauss = false;
        trian = true;
        aQ.setVisible(true);
        bQ.setVisible(true);
        cQ.setVisible(true);
        dQ.setVisible(false);
        aQ.setPromptText("a");
        bQ.setPromptText("b");
        cQ.setPromptText("c");
    }

    @FXML
    public void chooseAbs() {
        isAbs = true;
    }

    @FXML
    public void chooseRel() {
        isAbs = false;
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
        for (FoodEntry foodEntry : foodEntries) {
            if (foodEntry.getIngredients().toLowerCase().contains("meat") || foodEntry.getIngredients().toLowerCase().contains("beef") ||
                    foodEntry.getIngredients().toLowerCase().contains("chicken") || foodEntry.getIngredients().toLowerCase().contains("pork")) {
                meat.add(foodEntry);
            } else {
                notMeat.add(foodEntry);
            }
        }
        meatNotMeat.add(meat);
        meatNotMeat.add(notMeat);
        return meatNotMeat;
    }

    @FXML
    private void generateTwo() {
        List<CheckedItem> checkedItemsQ = getCheckedItems(Q);
        List<Quantifier> checkedNewQs = new ArrayList<>();
        if (quantifiersNew != null) {
            for (Quantifier quantifier: quantifiersNew) {
                for (CheckedItem checkedItem: checkedItemsQ) {
                    if (quantifier.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewQs.add(quantifier);
                        checkedItemsQ.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsW = getCheckedItems(W);
        List<Label> checkedNewWs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label: summariesNew) {
                for (CheckedItem checkedItem: checkedItemsW) {
                    if (label.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewWs.add(label);
                        checkedItemsW.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsS = getCheckedItems(S);
        List<Label> checkedNewSs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label: summariesNew) {
                for (CheckedItem checkedItem: checkedItemsS) {
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
                .map(CheckedItem::getCategory).distinct().toList();

        List<String> uniqueS = checkedItemsS.stream()
                .map(CheckedItem::getCategory).distinct().toList();

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
            if (checkedItem.getCategory().equals("Absolutne")) {
                Quantifier quantifier = LabelFactory.createQuantifier(checkedItem.getItem().toLowerCase(), true);
                if (ws.isEmpty()) {
                    summariesTwo.add(new SummaryTwo(quantifier, ss, split.get(0), split.get(1)));
                    summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ss, split.get(1), split.get(0)));
                } else {
                    summariesTwo.add(new SummaryTwo(quantifier, ws, ss, split.get(0), split.get(1)));
                    summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ws, ss, split.get(1), split.get(0)));
                }
            } else {
                Quantifier quantifier = LabelFactory.createQuantifier(checkedItem.getItem().toLowerCase(), false);
                if (ws.isEmpty()) {
                    summariesTwo.add(new SummaryTwo(quantifier, ss, split.get(0), split.get(1)));
                    summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ss, split.get(1), split.get(0)));
                } else {
                    summariesTwo.add(new SummaryTwo(quantifier, ws, ss, split.get(0), split.get(1)));
                    summariesTwoSecond.add(new SummaryTwoSecond(quantifier, ws, ss, split.get(1), split.get(0)));
                }
            }
        }
        for (Quantifier quantifier: checkedNewQs) {
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

    @FXML
    private void generateOne() {
        List<CheckedItem> checkedItemsQ = getCheckedItems(Q);
        List<Quantifier> checkedNewQs = new ArrayList<>();
        if (quantifiersNew != null) {
            for (Quantifier quantifier: quantifiersNew) {
                for (CheckedItem checkedItem: checkedItemsQ) {
                    if (quantifier.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewQs.add(quantifier);
                        checkedItemsQ.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsW = getCheckedItems(W);
        List<Label> checkedNewWs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label: summariesNew) {
                for (CheckedItem checkedItem: checkedItemsW) {
                    if (label.getLabelName().equals(checkedItem.getItem())) {
                        checkedNewWs.add(label);
                        checkedItemsW.remove(checkedItem);
                        break;
                    }
                }
            }
        }
        List<CheckedItem> checkedItemsS = getCheckedItems(S);
        List<Label> checkedNewSs = new ArrayList<>();
        if (summariesNew != null) {
            for (Label label: summariesNew) {
                for (CheckedItem checkedItem: checkedItemsS) {
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
            weights = new ArrayList<>() {{
                add(0.5);
                add(0.05);
                add(0.05);
                add(0.05);
                add(0.05);
                add(0.05);
                add(0.05);
                add(0.05);
                add(0.05);
                add(0.05);
                add(0.05);
            }};
        } else {
            weights = new ArrayList<>() {{
                add(Double.valueOf(t1.getText()));
                add(Double.valueOf(t2.getText()));
                add(Double.valueOf(t3.getText()));
                add(Double.valueOf(t4.getText()));
                add(Double.valueOf(t5.getText()));
                add(Double.valueOf(t6.getText()));
                add(Double.valueOf(t7.getText()));
                add(Double.valueOf(t8.getText()));
                add(Double.valueOf(t9.getText()));
                add(Double.valueOf(t10.getText()));
                add(Double.valueOf(t11.getText()));
            }};
        }

        List<Label> ws = new ArrayList<>();
        List<Label> ss = new ArrayList<>();

        List<String> uniqueW = checkedItemsW.stream()
                .map(CheckedItem::getCategory).distinct().toList();

        List<String> uniqueS = checkedItemsS.stream()
                .map(CheckedItem::getCategory).distinct().toList();

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
            if (checkedItem.getCategory().equals("Absolutne")) {
                Quantifier quantifier = LabelFactory.createQuantifier(checkedItem.getItem().toLowerCase(), true);
                if (ws.isEmpty()) {
                    summaries.add(new Summary(quantifier, ss, foodEntries, weights));
                } else {
                    summaries.add(new Summary(quantifier, ws, ss, foodEntries, weights));
                }
            } else {
                Quantifier quantifier = LabelFactory.createQuantifier(checkedItem.getItem().toLowerCase(), false);
                if (ws.isEmpty()) {
                    summaries.add(new Summary(quantifier, ss, foodEntries, weights));
                } else {
                    summaries.add(new Summary(quantifier, ws, ss, foodEntries, weights));
                }
            }
        }
        for (Quantifier quantifier: checkedNewQs) {
            if (ws.isEmpty()) {
                summaries.add(new Summary(quantifier, ss, foodEntries, weights));
            } else {
                summaries.add(new Summary(quantifier, ws, ss, foodEntries, weights));
            }
        }
        generateResults();
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

    public List<CheckedItem> getCheckedItems(TreeView<String> treeView) {
        List<CheckedItem> checkedItems = new ArrayList<>();
        TreeItem<String> root = treeView.getRoot();
        if (root != null) {
            collectCheckedItems(root, checkedItems, null);
        }
        return checkedItems;
    }

    private void collectCheckedItems(TreeItem<String> treeItem, List<CheckedItem> checkedItems, String parentCategory) {
        if (treeItem.getGraphic() instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) treeItem.getGraphic();
            if (checkBox.isSelected() && parentCategory != null) {
                checkedItems.add(new CheckedItem(parentCategory, treeItem.getValue()));
            }
        }

        String currentCategory = parentCategory;

        if (parentCategory == null) {
            if (treeItem.getValue().equals("Relatywne") || treeItem.getValue().equals("Absolutne")) {
                currentCategory = treeItem.getValue();
            } else if (treeItem.getParent() != null && treeItem.getParent().getValue().equals("Kwalifikatory")) {
                currentCategory = treeItem.getValue();
            }
        }

        for (TreeItem<String> child : treeItem.getChildren()) {
            collectCheckedItems(child, checkedItems, currentCategory);
        }
    }


    private void initializeTreeViewQ() {
        TreeItem<String> rootItemQ = new TreeItem<>("Kwantyfikatory");
        rootItemQ.setExpanded(true);

        TreeItem<String> rootItemRel = new TreeItem<>("Relatywne");
        TreeItem<String> rootItemAbs = new TreeItem<>("Absolutne");
        rootItemRel.setExpanded(true);
        rootItemAbs.setExpanded(true);

        TreeItem<String> category1Rel = createCheckBoxTreeItem("Almost none");
        TreeItem<String> category2Rel = createCheckBoxTreeItem("Minority");
        TreeItem<String> category3Rel = createCheckBoxTreeItem("About half");
        TreeItem<String> category4Rel = createCheckBoxTreeItem("Majority");
        TreeItem<String> category5Rel = createCheckBoxTreeItem("Almost all");

        rootItemRel.getChildren().addAll(category1Rel, category2Rel, category3Rel, category4Rel, category5Rel);

        TreeItem<String> category1Abs = createCheckBoxTreeItem("Less than 3000");
        TreeItem<String> category2Abs = createCheckBoxTreeItem("Around 5000");
        TreeItem<String> category3Abs = createCheckBoxTreeItem("Around 10000");
        TreeItem<String> category4Abs = createCheckBoxTreeItem("Over 12000");

        rootItemAbs.getChildren().addAll(category1Abs, category2Abs, category3Abs, category4Abs);

        rootItemQ.getChildren().addAll(rootItemRel, rootItemAbs);

        Q.setRoot(rootItemQ);
        Q.setCellFactory(new Callback<TreeView<String>, javafx.scene.control.TreeCell<String>>() {
            @Override
            public javafx.scene.control.TreeCell<String> call(TreeView<String> param) {
                return new CheckBoxTreeCell();
            }
        });
    }

    private void initializeTreeViewW() {
        TreeItem<String> rootItemW = new TreeItem<>("Kwalifikatory");
        rootItemW.setExpanded(true);

        TreeItem<String> rootItem1 = new TreeItem<>("Carbohydrates");
        TreeItem<String> rootItem2 = new TreeItem<>("Cholesterol");
        TreeItem<String> rootItem3 = new TreeItem<>("Energy");
        TreeItem<String> rootItem4 = new TreeItem<>("Fiber");
        TreeItem<String> rootItem5 = new TreeItem<>("Protein");
        TreeItem<String> rootItem6 = new TreeItem<>("Sugar");
        TreeItem<String> rootItem7 = new TreeItem<>("Fat");
        TreeItem<String> rootItem8 = new TreeItem<>("Magnesium");
        TreeItem<String> rootItem9 = new TreeItem<>("Vitamin C");
        TreeItem<String> rootItem10 = new TreeItem<>("Vitamin B6");
        TreeItem<String> rootItem11 = new TreeItem<>("User's");
        rootItem1.setExpanded(true);
        rootItem2.setExpanded(true);
        rootItem3.setExpanded(true);
        rootItem4.setExpanded(true);
        rootItem5.setExpanded(true);
        rootItem6.setExpanded(true);
        rootItem7.setExpanded(true);
        rootItem8.setExpanded(true);
        rootItem9.setExpanded(true);
        rootItem10.setExpanded(true);
        rootItem11.setExpanded(true);

        rootItem1.getChildren().addAll(
                createCheckBoxTreeItem("low level of carbohydrates"),
                createCheckBoxTreeItem("moderate level of carbohydrates"),
                createCheckBoxTreeItem("high level of carbohydrates")
        );

        rootItem2.getChildren().addAll(
                createCheckBoxTreeItem("low level of cholesterol"),
                createCheckBoxTreeItem("optimal level of cholesterol"),
                createCheckBoxTreeItem("high level of cholesterol")
        );

        rootItem3.getChildren().addAll(
                createCheckBoxTreeItem("low level of energy"),
                createCheckBoxTreeItem("moderate level of energy"),
                createCheckBoxTreeItem("energetic"),
                createCheckBoxTreeItem("high level of energy")
        );

        rootItem4.getChildren().addAll(
                createCheckBoxTreeItem("deficiency of fiber"),
                createCheckBoxTreeItem("optimal fiber"),
                createCheckBoxTreeItem("excess of fiber")
        );

        rootItem5.getChildren().addAll(
                createCheckBoxTreeItem("low-protein"),
                createCheckBoxTreeItem("moderate-protein"),
                createCheckBoxTreeItem("high-protein")
        );

        rootItem6.getChildren().addAll(
                createCheckBoxTreeItem("low level of sugar"),
                createCheckBoxTreeItem("normal level of sugar"),
                createCheckBoxTreeItem("excessive level of sugar")
        );

        rootItem7.getChildren().addAll(
                createCheckBoxTreeItem("fat free"),
                createCheckBoxTreeItem("low-fat"),
                createCheckBoxTreeItem("moderatly fatty"),
                createCheckBoxTreeItem("fatty"),
                createCheckBoxTreeItem("oily")
        );

        rootItem8.getChildren().addAll(
                createCheckBoxTreeItem("deficiency of magnesium"),
                createCheckBoxTreeItem("low level of magnesium"),
                createCheckBoxTreeItem("optimal level of magnesium"),
                createCheckBoxTreeItem("high level of magnesium"),
                createCheckBoxTreeItem("dangerously high level of magnesium")
        );

        rootItem9.getChildren().addAll(
                createCheckBoxTreeItem("deficiency level of vitamin C"),
                createCheckBoxTreeItem("low level of vitamin C"),
                createCheckBoxTreeItem("optimal level of vitamin C"),
                createCheckBoxTreeItem("high level of vitamin C")
        );

        rootItem10.getChildren().addAll(
                createCheckBoxTreeItem("deficiency level of vitamin B6"),
                createCheckBoxTreeItem("optimal level of vitamin B6"),
                createCheckBoxTreeItem("high level of vitamin B6"),
                createCheckBoxTreeItem("excessive level of vitamin B6")
        );

        rootItemW.getChildren().addAll(
                rootItem1,
                rootItem2,
                rootItem3,
                rootItem4,
                rootItem5,
                rootItem6,
                rootItem7,
                rootItem8,
                rootItem9,
                rootItem10,
                rootItem11
        );

        W.setRoot(rootItemW);
        W.setCellFactory(new Callback<TreeView<String>, javafx.scene.control.TreeCell<String>>() {
            @Override
            public javafx.scene.control.TreeCell<String> call(TreeView<String> param) {
                return new CheckBoxTreeCell();
            }
        });
    }

    private void initializeTreeViewS() {
        TreeItem<String> rootItemS = new TreeItem<>("Kwalifikatory");
        rootItemS.setExpanded(true);

        TreeItem<String> rootItem1 = new TreeItem<>("Carbohydrates");
        TreeItem<String> rootItem2 = new TreeItem<>("Cholesterol");
        TreeItem<String> rootItem3 = new TreeItem<>("Energy");
        TreeItem<String> rootItem4 = new TreeItem<>("Fiber");
        TreeItem<String> rootItem5 = new TreeItem<>("Protein");
        TreeItem<String> rootItem6 = new TreeItem<>("Sugar");
        TreeItem<String> rootItem7 = new TreeItem<>("Fat");
        TreeItem<String> rootItem8 = new TreeItem<>("Magnesium");
        TreeItem<String> rootItem9 = new TreeItem<>("Vitamin C");
        TreeItem<String> rootItem10 = new TreeItem<>("Vitamin B6");
        TreeItem<String> rootItem11 = new TreeItem<>("User's");
        rootItem1.setExpanded(true);
        rootItem2.setExpanded(true);
        rootItem3.setExpanded(true);
        rootItem4.setExpanded(true);
        rootItem5.setExpanded(true);
        rootItem6.setExpanded(true);
        rootItem7.setExpanded(true);
        rootItem8.setExpanded(true);
        rootItem9.setExpanded(true);
        rootItem10.setExpanded(true);
        rootItem11.setExpanded(true);

        rootItem1.getChildren().addAll(
                createCheckBoxTreeItem("low level of carbohydrates"),
                createCheckBoxTreeItem("moderate level of carbohydrates"),
                createCheckBoxTreeItem("high level of carbohydrates")
        );

        rootItem2.getChildren().addAll(
                createCheckBoxTreeItem("low level of cholesterol"),
                createCheckBoxTreeItem("optimal level of cholesterol"),
                createCheckBoxTreeItem("high level of cholesterol")
        );

        rootItem3.getChildren().addAll(
                createCheckBoxTreeItem("low level of energy"),
                createCheckBoxTreeItem("moderate level of energy"),
                createCheckBoxTreeItem("energetic"),
                createCheckBoxTreeItem("high level of energy")
        );

        rootItem4.getChildren().addAll(
                createCheckBoxTreeItem("deficiency of fiber"),
                createCheckBoxTreeItem("optimal fiber"),
                createCheckBoxTreeItem("excess of fiber")
        );

        rootItem5.getChildren().addAll(
                createCheckBoxTreeItem("low-protein"),
                createCheckBoxTreeItem("moderate-protein"),
                createCheckBoxTreeItem("high-protein")
        );

        rootItem6.getChildren().addAll(
                createCheckBoxTreeItem("low level of sugar"),
                createCheckBoxTreeItem("normal level of sugar"),
                createCheckBoxTreeItem("excessive level of sugar")
        );

        rootItem7.getChildren().addAll(
                createCheckBoxTreeItem("fat free"),
                createCheckBoxTreeItem("low-fat"),
                createCheckBoxTreeItem("moderatly fatty"),
                createCheckBoxTreeItem("fatty"),
                createCheckBoxTreeItem("oily")
        );

        rootItem8.getChildren().addAll(
                createCheckBoxTreeItem("deficiency of magnesium"),
                createCheckBoxTreeItem("low level of magnesium"),
                createCheckBoxTreeItem("optimal level of magnesium"),
                createCheckBoxTreeItem("high level of magnesium"),
                createCheckBoxTreeItem("dangerously high level of magnesium")
        );

        rootItem9.getChildren().addAll(
                createCheckBoxTreeItem("deficiency level of vitamin C"),
                createCheckBoxTreeItem("low level of vitamin C"),
                createCheckBoxTreeItem("optimal level of vitamin C"),
                createCheckBoxTreeItem("high level of vitamin C")
        );

        rootItem10.getChildren().addAll(
                createCheckBoxTreeItem("deficiency level of vitamin B6"),
                createCheckBoxTreeItem("optimal level of vitamin B6"),
                createCheckBoxTreeItem("high level of vitamin B6"),
                createCheckBoxTreeItem("excessive level of vitamin B6")
        );

        rootItemS.getChildren().addAll(
                rootItem1,
                rootItem2,
                rootItem3,
                rootItem4,
                rootItem5,
                rootItem6,
                rootItem7,
                rootItem8,
                rootItem9,
                rootItem10,
                rootItem11
        );

        S.setRoot(rootItemS);
        S.setCellFactory(new Callback<TreeView<String>, javafx.scene.control.TreeCell<String>>() {
            @Override
            public javafx.scene.control.TreeCell<String> call(TreeView<String> param) {
                return new CheckBoxTreeCell();
            }
        });
    }

    private TreeItem<String> createCheckBoxTreeItem(String itemName) {
        TreeItem<String> item = new TreeItem<>(itemName);
        item.setGraphic(new CheckBox());
        return item;
    }

    private class CheckBoxTreeCell extends javafx.scene.control.TreeCell<String> {
        private final CheckBox checkBox;

        public CheckBoxTreeCell() {
            this.checkBox = new CheckBox();
            checkBox.setDisable(false);
            checkBox.setAllowIndeterminate(false);

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (getTreeItem() != null && getTreeItem().getGraphic() instanceof CheckBox) {
                    ((CheckBox) getTreeItem().getGraphic()).setSelected(newValue);
                }
            });
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item);
                if (getTreeItem() != null && getTreeItem().getGraphic() instanceof CheckBox) {
                    CheckBox existingCheckBox = (CheckBox) getTreeItem().getGraphic();
                    checkBox.setSelected(existingCheckBox.isSelected());
                    setGraphic(checkBox);
                } else {
                    setGraphic(checkBox);
                }
            }
        }
    }
}
