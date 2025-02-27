package hackathon;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class AllRunner
{
    public static void main(String[] args) {
        System.out.println("Starting Server Runner");
        ServerRunner serverRunner = new ServerRunner();
        Dotenv dotenv = Dotenv.load();
        String BOT_TOKEN = dotenv.get("BOT_TOKEN");

        //Var for command prefix
        String prefix = "/";

        //Set up the JDA API with the bot
        JDA api = JDABuilder.createDefault(BOT_TOKEN).build();

        //Space to register listeners
        api.addEventListener(new MyListeners());
        api.addEventListener(new SlashCommandReceived());

        //Space for slash commands
        api.upsertCommand("recentsongs", "Displays your most recent listened to Spotify songs!").queue();;
        api.upsertCommand("recentartists", "Displays your most recent listened to Spotify artists!").queue();;
        api.upsertCommand("ping", "Pings the bot which responds with \"Pong!\"").queue();
    }
}
