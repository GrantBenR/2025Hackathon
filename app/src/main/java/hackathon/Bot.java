package hackathon;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

    public static String prefix;
    public static void main(String[] args) throws Exception {
        
        //Grab the bot token from the .env file so it doesn't get pushed to github
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
        api.upsertCommand("recentsongs", "Displays your most recent listened to Spotify songs!");
        api.upsertCommand("recentartists", "Displays your most recent listened to Spotify artists!");
        api.upsertCommand("ping", "Pings the bot which responds with \"Pong!\"");
    }
}
