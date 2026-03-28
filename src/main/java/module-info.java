module ie.setu.jewelleryca {
    requires javafx.controls;
    requires javafx.fxml;

    exports ie.setu.jewelleryca;
    exports ie.setu.jewelleryca.controllers;
    exports ie.setu.jewelleryca.pages;

    opens ie.setu.jewelleryca to javafx.fxml;
    opens ie.setu.jewelleryca.controllers to javafx.fxml;
    opens ie.setu.jewelleryca.pages to javafx.fxml;
    opens ie.setu.jewelleryca.models to javafx.base;
    opens ie.setu.jewelleryca.services to javafx.base;
}
