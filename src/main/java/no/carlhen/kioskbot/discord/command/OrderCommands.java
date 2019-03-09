package no.carlhen.kioskbot.discord.command;
import no.carlhen.kioskbot.Item;
import no.carlhen.kioskbot.Menu;
import no.carlhen.kioskbot.Order;
import no.carlhen.kioskbot.discord.Controller;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;

public class OrderCommands {

    public static void Order(MessageCreateEvent event, String[] wordArray) {
        String comment = String.join(" ", Arrays.copyOfRange(wordArray, 2, wordArray.length));
        Menu currMenu = Menu.GetCurrentMenu();
        Item item = currMenu.getItemByID(wordArray[1]);
        if (item == null || !item.getEnabled()) {
            event.getChannel().sendMessage("Jeg er ganske sikker på at " + wordArray[1] + " ikke er på menyen :yum:");
        } else {
            Order incomingOrder = new Order(item, event.getMessage().getAuthor().getIdAsString(), comment);

            Order.AddNewOrder(incomingOrder);
        }
    }

    public static void ListOrders(MessageCreateEvent event){
        String returnMessage = "--------------Dine Ordre--------------";

        for (Order currOrder : Order.GetOrdersList()){
            if (currOrder.userID.equals((event.getMessageAuthor().getIdAsString())) && !currOrder.status.equals(Order.ORDER_DELIVERED)){
                returnMessage += "\n\n**[Ordre ID: **`" + currOrder.id + "`**]** Din ordre for " + currOrder.item.name + ".\n";
                switch (currOrder.status){
                    case Order.ORDER_CREATED:
                        returnMessage += "**[Status]:** Mottat.";
                        break;
                    case Order.ORDER_SEEN:
                        returnMessage += "**[Status]:** Sett og påbegynt.";
                        break;
                    case Order.ORDER_READY:
                        returnMessage += "**[Status]:** Klar og kan hentes.";
                        break;
                }
            }
        }
        event.getChannel().sendMessage(returnMessage);
    }

    public static void sendOrderUpdate(Order order){
        String returnMessage = "";
        switch (order.status){
            case Order.ORDER_CREATED:
                returnMessage = "**[Ordre ID: " + order.id + "]** Din ordre for " + order.item.name + " er mottat";
                break;
            case Order.ORDER_SEEN:
                returnMessage = "**[Ordre ID: " + order.id + "]** Din ordre for " + order.item.name + " er nå sett og påbegynt.";
                break;
            case Order.ORDER_READY:
                returnMessage = "**[Ordre ID: " + order.id + "]** Din ordre for " + order.item.name + " er nå klar og kan hentes.";
                break;
            case Order.ORDER_DELIVERED:
                returnMessage = "**[Ordre ID: " + order.id + "]** Din ordre for " + order.item.name + " er nå utlevert.";
                break;
        }
        try {
            Controller.api.getUserById(order.userID).get().getPrivateChannel().get().sendMessage(returnMessage);
        }
        catch (Exception e){

        }
    }
}
