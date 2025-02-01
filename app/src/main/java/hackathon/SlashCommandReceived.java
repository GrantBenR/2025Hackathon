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
        if(event.getName().equalsIgnoreCase("RecentSongs")){
            event.reply("Your most recent listened to songs were:\n1: \n2: \n3: \n4: \n5: ").setEphemeral(false).queue();
        }

        //Command for recent artists (spotify)
        if(event.getName().equalsIgnoreCase("RecentArtists")){
            event.reply("Your most recent listened to songs were:\n1: \n2: \n3: \n4: \n5: ").setEphemeral(false).queue();
        }
    }

    // Global Command - Updates in about an hour - max 100
    @Override
    public void onReady(ReadyEvent event){
        
        List<CommandData> commandData = new ArrayList<>();
        
        commandData.add(Commands.slash("RecentArtists", "Imports a list of the most recent artists"));
        commandData.add(Commands.slash("RecentSongs", "Imports a list of the most recent songs"));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}

