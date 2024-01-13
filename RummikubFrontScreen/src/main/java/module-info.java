module com.example.rummikubfrontscreen {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    //requires deeplearning4j.core;

    opens com.example.rummikubfrontscreen to javafx.fxml;
    exports com.example.rummikubfrontscreen;
}