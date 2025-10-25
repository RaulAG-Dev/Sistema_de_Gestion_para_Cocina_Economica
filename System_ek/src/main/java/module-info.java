module com.example.sistema {

    requires javafx.controls;
    requires javafx.fxml;


    requires javafx.web;
    requires org.controlsfx.controls;
    requires json.simple;

    exports com.example;
    opens com.example.controllers to javafx.fxml;
    opens com.example to javafx.fxml;
}