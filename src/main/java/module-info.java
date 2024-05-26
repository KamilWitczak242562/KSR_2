module com.example.ksr_2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.example.ksr_2 to javafx.fxml;
    exports com.example.ksr_2;
    exports com.example.ksr_2.gui;
    opens com.example.ksr_2.gui to javafx.fxml;
}