module com.example.sistema {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires json.simple;
    requires javafx.graphics;

    opens com.example.sistema to javafx.fxml;
    opens com.example.sistema.controllers to javafx.fxml;

    exports com.example.sistema;
    exports com.example.sistema.controllers;
}
