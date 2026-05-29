module org.example.digitallibraryproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.digitallibraryproject to javafx.fxml;
    exports org.example.digitallibraryproject;
}