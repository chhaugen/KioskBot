package no.carlhen.kioskbot.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import no.carlhen.kioskbot.Main;
import no.carlhen.kioskbot.Menu;

import java.util.ArrayList;

public class EditMenuListView {

    public static final String title = "Menyer";

    public static void display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(280);
        //window.setResizable(false);

        // Left side of the window

        VBox buttonVBox = new VBox(16);
        buttonVBox.setPadding(new Insets(0,0,0,8));
        final double buttonMinSize[] = {120, 40};

        Button buttonNewMenu = new Button("Ny meny");
        buttonNewMenu.setMinSize(buttonMinSize[0], buttonMinSize[1]);

        Button buttonEditMenu = new Button("Editer meny");
        buttonEditMenu.setMinSize(buttonMinSize[0], buttonMinSize[1]);

        Button buttonDuplicateMenu = new Button("Dupliser meny");
        buttonDuplicateMenu.setMinSize(buttonMinSize[0], buttonMinSize[1]);

        Button buttonSetDefaultMenu = new Button("Sett som\nnåverende meny");
        buttonSetDefaultMenu.setMinSize(buttonMinSize[0], buttonMinSize[1]);

        Button buttonDeleteMenu = new Button("Slett meny");
        buttonDeleteMenu.setStyle("-fx-base: rgba(255, 78, 50, 0.89);");
        buttonDeleteMenu.setMinSize(buttonMinSize[0], buttonMinSize[1]);

        buttonVBox.getChildren().addAll(buttonNewMenu, buttonEditMenu, buttonDuplicateMenu, buttonSetDefaultMenu, buttonDeleteMenu);
        //==============================================================================

        // Right side of the window

        ListView<Menu> listViewMenus = new ListView<>();
        listViewMenus.getItems().addAll(Menu.GetMenusList());
        listViewMenus.setEditable(true);
        HBox.setHgrow(listViewMenus, Priority.ALWAYS);
        //==============================================================================

        // Buttons and ListView actions

        buttonEditMenu.setOnAction(event -> {
            Menu menu = listViewMenus.getSelectionModel().getSelectedItem();
            int selectedInt = listViewMenus.getSelectionModel().getSelectedIndex();
            if (menu == null){
                Main.logger.warn("No menu selected");
                return;
            }
            Menu newMenu = new EditMenuView(menu).display();
            Menu.UpdateMenu(newMenu);
            listViewMenus.refresh();
        });

        buttonNewMenu.setOnAction(event -> {
            Menu newMenu = new EditMenuView(new Menu("","",new ArrayList<>())).display();
            Menu.AddNewMenu(newMenu);
            listViewMenus.getItems().add(newMenu);
        });

        buttonDuplicateMenu.setOnAction(event -> {
            Menu menu = listViewMenus.getSelectionModel().getSelectedItem();
            if (menu == null){
                Main.logger.warn("No menu selected");
                return;
            }
            Menu duplicateMenu = new Menu(menu.name, menu.description, menu.items);
            Menu.AddNewMenu(duplicateMenu);
            listViewMenus.getItems().add(duplicateMenu);
        });

        buttonSetDefaultMenu.setOnAction(event -> {
            Menu menu = listViewMenus.getSelectionModel().getSelectedItem();
            if (menu == null){
                Main.logger.warn("No menu selected");
                return;
            }
            if (new AlertBoxView(
                    "Sette meny " + menu.name + " som nåverende meny.",
                    "Er du sikker på at du ønsker å sette meny " + menu.name + " som nåverende meny?",
                    "Ja", "Nei")
                    .display()){
                Menu.SetCurrentMenu(menu);
            }
        });


        buttonDeleteMenu.setOnAction(event -> {
            Menu itemToDelete = listViewMenus.getSelectionModel().getSelectedItem();
            if (itemToDelete == null){
                Main.logger.warn("No menu selected");
                return;
            }
            listViewMenus.getItems().removeIf(menu -> menu.isSame(itemToDelete));
            Menu.DeleteMenu(itemToDelete);
        });
        //==============================================================================

        // Display

        HBox layout = new HBox();
        layout.setPadding(new Insets(8));
        layout.getChildren().addAll(listViewMenus, buttonVBox);


        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
