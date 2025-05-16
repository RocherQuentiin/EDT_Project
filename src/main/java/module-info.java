module fr.isep.edt_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens fr.isep.edt_project to javafx.fxml;
    exports fr.isep.edt_project;
}