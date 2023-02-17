module com.whackamole.wam20 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.whackamole.wam20 to javafx.fxml;
    exports com.whackamole.wam20;
}