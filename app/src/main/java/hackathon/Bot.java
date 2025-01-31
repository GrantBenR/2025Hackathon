package hackathon;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

    public static void main(String[] args) {
        
        //Grab the bot token from the .env file so it doesn't get pushed to github
        Dotenv dotenv = Dotenv.load();
        String BOT_TOKEN = dotenv.get("BOT_TOKEN");

        //Var for command prefix
        String prefix = "/";

        //Set up the JDA API with the bot
        JDA api = JDABuilder.createDefault(BOT_TOKEN).build();

        //Space to register listeners

        //Space for slash commands

    }
}
