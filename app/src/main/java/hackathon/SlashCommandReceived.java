package hackathon;

//Imports
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;

import java.util.*;
import java.io.*;

/**
 * Class used to track slash commands used by the discord bot
 * @author Nathan Fairweather
 */
public class SlashCommandReceived extends ListenerAdapter{

    //Pull the command prefix from  bot class
    public static String prefix = Bot.prefix;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(event.getMember().getUser().isBot()) return;
        //Command for recent songs (Spotify)
        if(event.getName().equalsIgnoreCase("recentsongs")){
            ArrayList<String> SongTitles = new ArrayList<>();
            event.reply("Your most recent listened to songs were: \n1: \n2: \n3: \n4: \n5: ").setEphemeral(false).queue();
        }
        else if(event.getName().equalsIgnoreCase("recentartists")){ //Command for recent artists (spotify)
            ArrayList<String> ArtistNames = new ArrayList<>();
            event.reply("Your most recent listened to songs were:\n1: \n2: \n3: \n4: \n5: ").setEphemeral(false).queue();
        }
        else if(event.getName().equalsIgnoreCase("ping")){ //Command for ping
            event.reply("Pong!").setEphemeral(false).queue();
        }
    }

    /* 
    // Guild Command - Updates hopefully instantly - max 100
    @Override
    public void onGuildReady(GuildReadyEvent event) {

        //List for commands
        List<CommandData> commandData = new ArrayList<>();

        //Add commands here for synchronization
        commandData.add(Commands.slash("Recent_Songs", "Testing this crap"));
        commandData.add(Commands.slash("Recent_Artists", "This better work"));

        //UPDATE THE WHOLE LIST PLEASE
        event.getGuild().updateCommands().addCommands(commandData).queue();

    }
        */
}

