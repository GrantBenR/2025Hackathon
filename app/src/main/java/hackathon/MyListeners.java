package hackathon;

//Imports
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.channel.middleman.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MyListeners extends ListenerAdapter {
    
    public static void main(String[] arguments) throws LoginException, InterruptedException
    {
        //JDA api = JDABuilder.createDefault(arguments[0]).addEventListeners(new PingPongBot()).build().awaitReady();
    
    
    }

    //Runs the command in dm's just because
    public void onMessageReceived(MessageReceivedEvent event)
    {
       //Ignore messages from other bots
       if(event.getAuthor().isBot()) return;

       //Variables
       Message message = event.getMessage();
       String content = message.getContentRaw();

       //Functionality
       if(content.equals("!ping")){
           MessageChannel channel = event.getChannel(); 
           channel.sendMessage("Pong!").queue(); //.queue() is important to use after sendMessage()
       }else if(content.equals("!authenticate")){
            MessageChannel channel = event.getChannel();
            String DiscordUserId = event.getAuthor().getId();
            Boolean isUserInDatabase = DatabaseConnection.CheckUserStatusInDatabase(DiscordUserId);
            if (!isUserInDatabase)
            {
                channel.sendMessage("New Authentication Request in Progress").queue();
                SpotifyAuth spotifyAuth = new SpotifyAuth();
                String url = spotifyAuth.redirectToAuthCodeFlow();
                channel.sendMessage(url).queue();
            }
            else
            {
                channel.sendMessage("User " + event.getAuthor().getName() + " Already Authenticated");
            }

            //Implement here

       }
    }
    
}
