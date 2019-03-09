package no.carlhen.kioskbot.discord;

import no.carlhen.kioskbot.discord.command.HelpCommands;
import no.carlhen.kioskbot.discord.command.MenuCommands;
import no.carlhen.kioskbot.discord.command.OrderCommands;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Controller {
    public static DiscordApi api;

    public static void init(String token){
        api = new DiscordApiBuilder().setToken(token).login().join();

        api.addMessageCreateListener(messageCreateEvent -> {
            if (messageCreateEvent.isPrivateMessage() && !messageCreateEvent.getMessageAuthor().isYourself()){
                String[] wordArray = messageCreateEvent.getMessageContent().split(" ");
                String command = wordArray[0].toLowerCase();
                switch (command){
                    case "hjelp":
                        HelpCommands.help(messageCreateEvent);
                        break;
                    case "meny":
                        MenuCommands.menu(messageCreateEvent);
                        break;
                    case "bestill":
                        OrderCommands.Order(messageCreateEvent, wordArray);
                        break;
                    case "bestillinger":
                        OrderCommands.ListOrders(messageCreateEvent);
                        break;
                    default:
                        messageCreateEvent.getChannel().sendMessage("Jeg skjønte ikke helt hva du skreiv. :stuck_out_tongue:\nSkriv `hjelp`, hvis du lurer på noe.");
                }
            }
        });

    }
}
