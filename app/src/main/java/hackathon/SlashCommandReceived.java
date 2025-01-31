package hackathon;

//Imports
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

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
}

