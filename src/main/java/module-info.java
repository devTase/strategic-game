module org.hsh.games.aoe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens org.hsh.games.aoe.visual.modal to javafx.fxml;
    exports org.hsh.games.aoe.visual.modal;
}