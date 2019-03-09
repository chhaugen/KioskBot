package no.carlhen.kioskbot.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import no.carlhen.kioskbot.Order;
import no.carlhen.kioskbot.Main;

import java.util.Date;

public class MainScene {

    public static TableView<Order> orderTableView = new TableView<>(FXCollections.observableArrayList(Order.GetOrdersList()));
    public static final double buttonMinSize[] = {120, 40};

    public static Scene scene(){

        // Left side of the scene

        VBox leftButtonVBox = new VBox(16);
        leftButtonVBox.setPadding(new Insets(0,8,0,0));

        Button buttonMenus = new Button("Menyer");
        buttonMenus.setMinSize(buttonMinSize[0], buttonMinSize[1]);
        buttonMenus.setOnAction((e) -> EditMenuListView.display());

        Separator separator1 = new Separator(Orientation.HORIZONTAL);

        Button buttonDeleteTableRow = new Button("Slett ordre");
        buttonDeleteTableRow.setMinSize(buttonMinSize[0], buttonMinSize[1]);

        Separator separator2 = new Separator(Orientation.HORIZONTAL);

        Label labelStatusCheckBoxes = new Label("Endre status:");
        CheckBox checkBoxOrderSeen = new CheckBox("Ordre er sett");
        checkBoxOrderSeen.setDisable(true);
        CheckBox checkBoxOrderReady = new CheckBox("Ordre er klar");
        checkBoxOrderReady.setDisable(true);
        CheckBox checkBoxOrderDelivered = new CheckBox("Ordre er levert");
        checkBoxOrderDelivered.setDisable(true);

        leftButtonVBox.getChildren().addAll(
                buttonMenus,
                separator1,
                buttonDeleteTableRow,
                separator2,
                labelStatusCheckBoxes,
                checkBoxOrderSeen,
                checkBoxOrderReady,
                checkBoxOrderDelivered
                );
        //==============================================================================

        // Right side of the scene

        TableColumn<Order, Date> dateCreatedCol = new TableColumn<>("Tid oppretett");
        dateCreatedCol.setCellValueFactory(new PropertyValueFactory<>("timeStampMade"));

        TableColumn<Order, String> discordUsernameCol = new TableColumn<>("Brukernavn");
        discordUsernameCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        TableColumn<Order, String> itemNameCol = new TableColumn<>("Artikkel");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<Order, String> commentCol = new TableColumn<>("Kommentar");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

        TableColumn<Order, Integer> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(cloumn ->
            new TableCell<Order, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean emty){
                    if (!emty || item != null){
                        //super.updateItem(item, emty);
                        String[] StateParsed = Order.OrderStateParse(item);
                        setText(StateParsed[0]);
                        setGraphic(null);
                        TableRow currentRow = getTableRow();
                        if (!(currentRow == null))
                            currentRow.setStyle(StateParsed[1]);
                    }
                    else
                        setGraphic(null);
                }
            }
        );

        TableColumn<Order, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        orderTableView.getColumns().addAll(dateCreatedCol, discordUsernameCol, itemNameCol, commentCol, statusCol);
        orderTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        HBox.setHgrow(orderTableView, Priority.ALWAYS);

        HBox layout = new HBox();
        layout.setPadding(new Insets(8));
        layout.getChildren().addAll(leftButtonVBox, orderTableView);
        //==============================================================================


        // Actions

        buttonDeleteTableRow.setOnAction(event -> {
            Order itemToDelete = orderTableView.getSelectionModel().getSelectedItem();
            if (itemToDelete == null){
                Main.logger.warn("No order selected");
                return;
            }
            orderTableView.getItems().removeIf(menu -> menu.isSame(itemToDelete));
            orderTableView.refresh();
            Order.DeleteOrder(itemToDelete);
        });

        orderTableView.setOnMouseClicked((event -> {
            if(MouseButton.PRIMARY.equals(event.getButton())) {
                try {
                    Integer selectionStatus = orderTableView.getSelectionModel().getSelectedItem().getStatus();
                    checkBoxOrderSeen.setDisable(true);
                    checkBoxOrderSeen.setSelected(false);
                    checkBoxOrderReady.setDisable(true);
                    checkBoxOrderReady.setSelected(false);
                    checkBoxOrderDelivered.setDisable(true);
                    checkBoxOrderDelivered.setSelected(false);

                    switch (selectionStatus) {
                        case Order.ORDER_CREATED:
                            checkBoxOrderSeen.setDisable(false);
                            break;
                        case Order.ORDER_SEEN:
                            checkBoxOrderReady.setDisable(false);
                            break;
                        case Order.ORDER_READY:
                            checkBoxOrderDelivered.setDisable(false);
                            break;
                    }
                    switch (selectionStatus) {
                        case Order.ORDER_DELIVERED:
                            checkBoxOrderDelivered.setSelected(true);
                        case Order.ORDER_READY:
                            checkBoxOrderReady.setSelected(true);
                        case Order.ORDER_SEEN:
                            checkBoxOrderSeen.setSelected(true);
                    }
                }
                catch (Exception e){
                    Main.logger.warn("User did not click on a row");
                }
            }
        }));

        checkBoxOrderSeen.setOnAction(event -> {
            Order order = orderTableView.getSelectionModel().getSelectedItem();
            order.setStatus(Order.ORDER_SEEN);
            Order.UpdateOrder(order);
            checkBoxOrderSeen.setDisable(true);
            checkBoxOrderReady.setDisable(false);
            orderTableView.refresh();
        });

        checkBoxOrderReady.setOnAction(event -> {
            Order order = orderTableView.getSelectionModel().getSelectedItem();
            order.setStatus(Order.ORDER_READY);
            Order.UpdateOrder(order);
            checkBoxOrderReady.setDisable(true);
            checkBoxOrderDelivered.setDisable(false);
            orderTableView.refresh();
        });

        checkBoxOrderDelivered.setOnAction(event -> {
            Order order = orderTableView.getSelectionModel().getSelectedItem();
            order.setStatus(Order.ORDER_DELIVERED);
            Order.UpdateOrder(order);
            checkBoxOrderDelivered.setDisable(true);
            orderTableView.refresh();
        });

        return new Scene(layout, 1000,600);
    }

}
