package no.carlhen.kioskbot.discord.command;

import org.javacord.api.event.message.MessageCreateEvent;

public class HelpCommands {

    public static void help(MessageCreateEvent event){
        final HelpEntry[] helpEntries = {
                new HelpEntry(
                        "hjelp",
                        "Viser denne hjelp teksten.",
                        "hjelp"),
                new HelpEntry("meny",
                        "Viser det du kan bestille.",
                        "meny"),
                new HelpEntry("bestill",
                        "Ved å bruke 'bestill', kan du legge in ordre for en vare. Du har også muligheten til å legge ved en kommentar til bestillingen.",
                        "bestill [id] <kommentar>"),
                new HelpEntry("bestillinger",
                        "Ved å bruke 'bestillinger', kan du se hva du har bestilt og bestillingens status.",
                        "bestillinger")
        };
        event.getChannel().sendMessage(GenerateHelpMenu(helpEntries));
    }

    public static class HelpEntry{
        public String command;
        public String descrition;
        public String example;
        public HelpEntry(String command, String descrition, String example){
            this.command = command;
            this.descrition = descrition;
            this.example = example;
        }
    }

    public static String GenerateHelpMenu(HelpEntry[] helpEntrys){
        String helpMenu = "---------Liste over kommandoer---------";
        for (HelpEntry helpEntry : helpEntrys){
            helpMenu += "\n**" + helpEntry.command + ":**";
            helpMenu += "\n" + helpEntry.descrition + "";
            helpMenu += "\nEksempel: ```" + helpEntry.example + "```";
        }
        //helpMenu += "```";
        return helpMenu;
    }
}
