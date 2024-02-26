module ad.frontendapiud6borja {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;


    opens ad.frontendapiud6borja to javafx.fxml;
    exports ad.frontendapiud6borja;
}