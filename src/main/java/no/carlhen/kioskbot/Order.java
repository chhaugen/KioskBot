package no.carlhen.kioskbot;

import com.google.gson.reflect.TypeToken;
import no.carlhen.kioskbot.discord.Controller;
import no.carlhen.kioskbot.discord.command.OrderCommands;
import no.carlhen.kioskbot.view.MainScene;

import java.util.Date;
import java.util.List;

public class Order extends GenericModel<Order>{
    public Item item;
    public String userID;
    public String comment;
    public Date timeStampMade;
    public Integer status;


    public Order(Item item, String userID, String comment){
        this.id = RandomString.GenerateUniqueId(GetOrdersList());
        this.item = item;
        this.userID = userID;
        this.comment = comment;
        this.timeStampMade = new Date();
        this.status = ORDER_CREATED;
    }

    @Override
    public boolean isSame(Order order){
        return this.id.equals(order.id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getUserID() {
        return getDiscordUserName();
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimeStampMade() {
        return timeStampMade;
    }

    public void setTimeStampMade(Date timeStampMade) {
        this.timeStampMade = timeStampMade;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static final int ORDER_CREATED = 10;
    public static final int ORDER_SEEN = 20;
    public static final int ORDER_READY = 30;
    public static final int ORDER_DELIVERED = 40;
    public static final int ORDER_FLAGEDFORDELETION = 1;

    public static String[] OrderStateParse(Integer status){
        String[] returnArray = new String[2];
        if (status == null){
            returnArray[0] = "Ordre status utilgjengelig";
            returnArray[1] = "-fx-base: crimson";
            return returnArray;
        }
        switch (status){
            case ORDER_CREATED:
                returnArray[0] = "Ny ordre";
                returnArray[1] = "-fx-base: lightgreen";
                break;
            case ORDER_SEEN:
                returnArray[0] = "Ordre er sett";
                returnArray[1] = "-fx-base: deepskyblue";
                break;
            case ORDER_READY:
                returnArray[0] = "Ordre er klar";
                returnArray[1] = "-fx-base: blueviolet";
                break;
            case ORDER_DELIVERED:
                returnArray[0] = "Ordre er ferdig";
                returnArray[1] = "-fx-base: lightslategray";
                break;
            default:
                returnArray[0] = "Ordre status utilgjengelig";
                returnArray[1] = "-fx-base: crimson";
                break;
        }
        return returnArray;
    }

    public String getDiscordUserName(){
        String discordName = "";
        try {
            discordName = Controller.api.getUserById(this.userID).get().getDiscriminatedName();
        }
        catch (Exception e){
            Main.logger.error("Cannot fetch Discord username from id " + this.id);
            return null;
        }
        if (this.userID.equals("140194060341215232")){
            return discordName + " (Bot skaperen)";
        }
        else {
            return discordName;
        }
    }

    private static String fileName = "orders.json";
    private static TypeToken<List<Order>> TypeToken = new TypeToken<List<Order>>(){};

    private static List<Order> orders = GetOrdersList();

    @SuppressWarnings("unchecked")
    public static List<Order> GetOrdersList(){
        if (orders == null)
            return Storage.GetList(fileName, TypeToken);
        else
            return orders;
    }

    public static void AddNewOrder(Order order){
        orders.add(order);
        MainScene.orderTableView.getItems().add(order);
        MainScene.orderTableView.sort();
        Storage.SaveList(orders, fileName);
        OrderCommands.sendOrderUpdate(order);
        return;
    }

    public static void DeleteOrder(Order order){
        for (Order currOrder : orders)
            if (order.isSame(currOrder)){
                orders.remove(order);
                Storage.SaveList(orders, fileName);
                order.status = ORDER_FLAGEDFORDELETION;
                OrderCommands.sendOrderUpdate(order);
                return;
            }
    }

    public static void UpdateOrder(Order order){
        orders = Storage.UpdateList(orders, order, fileName);
        OrderCommands.sendOrderUpdate(order);
    }
}
