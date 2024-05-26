package com.example.ksr_2.gui;

import com.example.ksr_2.Label;
import com.example.ksr_2.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

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
    private Button buttonGenerate;

    @FXML
    private ListView<String> view;

    private ObservableList<String> summariesList;

    private List<Summary> summaries;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTreeViewQ();
        initializeTreeViewW();
        initializeTreeViewS();
        buttonGenerate.setOnAction(event -> generate());
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
                        Double.parseDouble(line[2]),
                        Double.parseDouble(line[3]),
                        Double.parseDouble(line[4]),
                        Double.parseDouble(line[5]),
                        Double.parseDouble(line[6]),
                        Double.parseDouble(line[7]),
                        Double.parseDouble(line[8]),
                        Double.parseDouble(line[9]),
                        Double.parseDouble(line[10]),
                        Double.parseDouble(line[11])
                );
                foodEntries.add(entry);
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        return foodEntries;
    }

    private void generate() {
        List<CheckedItem> checkedItemsQ = getCheckedItems(Q);
        List<CheckedItem> checkedItemsW = getCheckedItems(W);
        List<CheckedItem> checkedItemsS = getCheckedItems(S);
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
        generateResults();
    }

    public void generateResults() {
        List<OneSummary> oneSummaries = new ArrayList<>();
        for (Summary summary : summaries) {
            List<OneSummary> generatedSummaries = summary.generateAllOneSummaries();
            oneSummaries.addAll(generatedSummaries);
        }

        oneSummaries.sort(Comparator.comparingDouble(OneSummary::getQuality).reversed());

        summariesList = FXCollections.observableArrayList(
                oneSummaries.stream().map(OneSummary::toString).collect(Collectors.toList())
        );
        for (String oneSummary: summariesList) {
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
                rootItem10
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
                rootItem10
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
