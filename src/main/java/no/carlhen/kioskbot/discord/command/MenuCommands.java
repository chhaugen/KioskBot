package no.carlhen.kioskbot.discord.command;

import no.carlhen.kioskbot.Item;
import no.carlhen.kioskbot.Menu;
import org.javacord.api.event.message.MessageCreateEvent;

public class MenuCommands {
    public static void menu(MessageCreateEvent event){
        Menu currMenu = Menu.GetCurrentMenu();
        if (currMenu == null){
            event.getChannel().sendMessage("Beklager, ingen meny er valgt i kiosken. Venligst ta kontakt med dem om dette.");
            return;
        }
        String menuDisplay = "--------------Meny " + currMenu.name + "--------------";
        menuDisplay += "\n" + currMenu.description + "\n";

        for (Item item : currMenu.items){
            if (item.enabled && item.getEnabled()){
                menuDisplay += "\n__         " + item.name + "__";
                menuDisplay += "\n**ID**: " + item.id;
                menuDisplay += "\n\n" + item.description;
                menuDisplay += "\n\nPris: " + item.price + "kr\n";
            }
        }

        event.getChannel().sendMessage(menuDisplay);
    }
}
