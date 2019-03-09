package no.carlhen.kioskbot.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBoxView {
    public String title;
    public String message;
    public String optionTrue;
    public String optionFalse;
    private boolean answer;

    public AlertBoxView(String title, String message, String optionTrue, String optionFalse){
        this.title = title;
        this.message = message;
        this.optionTrue = optionTrue;
        this.optionFalse = optionFalse;
        this.answer = false;
    }

    public boolean display() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMaxWidth(400);

        Label label = new Label();
        label.setWrapText(true);
        label.setText(message);

        window.setMinHeight(160 + label.getHeight());

        Button buttonTrue = new Button(optionTrue);
        buttonTrue.setMinSize(MainScene.buttonMinSize[0], MainScene.buttonMinSize[1]);

        Button buttonFalse = new Button(optionFalse);
        buttonFalse.setMinSize(MainScene.buttonMinSize[0], MainScene.buttonMinSize[1]);

        HBox buttonsHBox = new HBox(12);
        buttonsHBox.getChildren().addAll(buttonTrue, buttonFalse);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setPadding(new Insets(24,0,0,0));

        VBox layout = new VBox();
        layout.setPadding(new Insets(8));
        layout.getChildren().addAll(label, buttonsHBox);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        boolean answer = false;

        buttonTrue.setOnAction(event -> {
            this.answer = true;
            window.close();
        });
        buttonFalse.setOnAction(event -> {
            this.answer = false;
            window.close();
        });

        window.showAndWait();
        return this.answer;
    }

}