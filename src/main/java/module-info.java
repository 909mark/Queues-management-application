module com.example.queue_management {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.queue_management to javafx.fxml;
    exports com.example.queue_management;
}