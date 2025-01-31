package hackathon;

//Imports
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashCommandReceived extends ListenerAdapter{

    public static String prefix = Bot.prefix;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(event.getMember().getUser().isBot()) return;

        if(event.getName().equalsIgnoreCase("ping")){
                event.reply("Pong!").setEphemeral(false).queue();
            }
        }
}
