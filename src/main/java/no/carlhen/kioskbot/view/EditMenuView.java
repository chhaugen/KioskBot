package no.carlhen.kioskbot.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import no.carlhen.kioskbot.Menu;
import no.carlhen.kioskbot.Item;
import no.carlhen.kioskbot.Main;

import java.util.ArrayList;

public class EditMenuView {
    private no.carlhen.kioskbot.Menu menu;

    public EditMenuView(no.carlhen.kioskbot.Menu menu){
        this.menu = menu;
    }

    private TableView<Item> itemsTableView = new TableView<>();

    public Menu display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Meny editor - " + this.menu.name);
        window.setMinWidth(500);
        window.setMinHeight(280);
        //window.setResizable(false);

        Label labelNameInput = new Label("Navn:");
        TextField nameInput = new TextField(this.menu.name);

        Label labelDescriptionInput = new Label("Beskrivelse:");
        TextField descriptionInput = new TextField(menu.description);

        Label labelItemsTableView = new Label("Artikler:");


        // Create columns

        TableColumn<Item, String> itemIdCol = new TableColumn<>("ID");
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Item, String> itemNameCol = new TableColumn<>("Navn");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Item, String> itemDescriptionCol = new TableColumn<>("Beskrivelse");
        itemDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Item, Number> itemPriceCol = new TableColumn<>("Pris (kr)");
        itemPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Item, Boolean> itemEnabledCol = new TableColumn<>("Aktivert");
        itemEnabledCol.setCellValueFactory(new PropertyValueFactory<>("enabled"));

        // Table edit code

        itemIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemIdCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setId(event.getNewValue());
        });

        itemNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemNameCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
        });

        itemDescriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemDescriptionCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setDescription(event.getNewValue());
        });

        itemPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        itemPriceCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPrice(event.getNewValue().doubleValue());
        });

        itemEnabledCol.setCellFactory(param -> {
            CheckBox checkBox = new CheckBox("");
            TableCell<Item, Boolean> cell = new TableCell<Item, Boolean>() {

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty){
                        setGraphic(null);
                    }
                    else {
                        checkBox.setSelected(item);
                        setGraphic(checkBox);
                    }
                }
            };
            checkBox.selectedProperty().addListener((observable, wasSelected, isSelected) -> {
                try {
                    ((Item)cell.getTableRow().getItem()).setEnabled(isSelected);
                }
                catch (Exception e){

                }
            });
            cell.setContentDisplay(ContentDisplay.CENTER);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        itemEnabledCol.setOnEditCommit(event -> event.getRowValue().setEnabled(event.getNewValue()));

        // Table init

        ObservableList<Item> itemsTableViewData = FXCollections.observableArrayList(this.menu.items);
        itemsTableView.setItems(itemsTableViewData);
        itemsTableView.getColumns().addAll(itemIdCol, itemNameCol, itemDescriptionCol, itemPriceCol, itemEnabledCol);
        itemsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        itemsTableView.setEditable(true);

        // Table input

        TextField itemIdInput = new TextField();
        itemIdInput.setPromptText("ID");
        itemIdInput.setMinWidth(80);

        TextField itemNameInput = new TextField();
        itemNameInput.setPromptText("Navn");
        itemNameInput.setMinWidth(100);

        TextField itemDescriptionInput = new TextField();
        itemDescriptionInput.setPromptText("Beskrivelse");

        TextField itemPriceInput = new TextField();
        itemPriceInput.setPromptText("Kr");
        itemPriceInput.setMaxWidth(65);

        CheckBox itemEnabledInput = new CheckBox();
        itemEnabledInput.setSelected(true);

        Button itemSubmitInput = new Button("Legg til");

        Button itemDeleteSelectedRow = new Button("Slett valgte felt");

        HBox itemInputsHBox = new HBox(10);
        itemInputsHBox.setPadding(new Insets(10));
        itemInputsHBox.setAlignment(Pos.CENTER);
        itemInputsHBox.getChildren().addAll(itemIdInput, itemNameInput, itemDescriptionInput, itemPriceInput, itemEnabledInput, itemSubmitInput, itemDeleteSelectedRow);

        // Save and Cancel buttons

        Button saveButton = new Button("Lagre");
        saveButton.setMinSize(MainScene.buttonMinSize[0],MainScene.buttonMinSize[1]);
        Button cancelButton = new Button("Avbryt");
        cancelButton.setMinSize(MainScene.buttonMinSize[0],MainScene.buttonMinSize[1]);

        HBox buttonHBox = new HBox(10);
        buttonHBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonHBox.getChildren().addAll(saveButton, cancelButton);

        // Actions

        itemSubmitInput.setOnAction(event -> {
            if(itemIdInput.getText().equals("") || itemNameInput.getText().equals("") || itemDescriptionInput.getText().equals("") || itemPriceInput.getText().equals(""))
                return;
            try{
            itemsTableView.getItems().add(new Item(itemIdInput.getText(), itemNameInput.getText(), itemDescriptionInput.getText(),Double.parseDouble(itemPriceInput.getText()), itemEnabledInput.isSelected()));
            itemIdInput.clear();
            itemNameInput.clear();
            itemDescriptionInput.clear();
            itemPriceInput.clear();
            itemEnabledInput.setSelected(true);
            }
            catch (Exception e){
                Main.logger.warn("Unable to convert text to double. Probably mistyped");
            }
        });

        itemDeleteSelectedRow.setOnAction(event -> {
            ObservableList<Item> productSelected, allProducts;
            allProducts = itemsTableView.getItems();
            productSelected = itemsTableView.getSelectionModel().getSelectedItems();

            productSelected.forEach(allProducts::remove);
        });

        saveButton.setOnAction(event -> {
            menu.name = nameInput.getText();
            menu.description = descriptionInput.getText();
            if(menu.id.equals("") || menu.name.equals("") || menu.description.equals(""))
                return;
            menu.items = new ArrayList<>();
            itemsTableView.getItems().iterator().forEachRemaining(item -> menu.items.add(item));
            window.close();
        });

        cancelButton.setOnAction(event -> window.close());

        // Window init
        VBox layout = new VBox();
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(labelNameInput, nameInput, labelDescriptionInput, descriptionInput, labelItemsTableView, itemsTableView, itemInputsHBox, buttonHBox);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return this.menu;
    }
}
