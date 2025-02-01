package hackathon;

//Imports
import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyListeners extends ListenerAdapter {
    public static Integer counter = 0;
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
       }
       else if(content.equals("!authenticate"))
       {
            MessageChannel channel = event.getChannel();
            String DiscordUserId = event.getAuthor().getId();
            Boolean isUserInDatabase = DatabaseConnection.CheckUserStatusInDatabase(DiscordUserId);
            System.out.println(isUserInDatabase);
            if (!isUserInDatabase)
            {
                channel.sendMessage("New Authentication Request in Progress").queue();
                SpotifyAuth spotifyAuth = new SpotifyAuth();
                String url = spotifyAuth.redirectToAuthCodeFlow(DiscordUserId);
                channel.sendMessage(url).queue();
            }
            else
            {
                counter++;
                System.out.println("Counter: " + counter);
                channel.sendMessage("User " + event.getAuthor().getName() + " Already Authenticated").queue();
                SpotifyAuth spotifyAuth = new SpotifyAuth();
                String url = spotifyAuth.redirectToAuthCodeFlow(DiscordUserId);
                channel.sendMessage(url).queue();
            }

            //Implement here
       }
       else if(content.equals("!mytoptracks"))
       {
            MessageChannel channel = event.getChannel();
            String DiscordUserId = event.getAuthor().getId();
            Boolean isUserInDatabase = DatabaseConnection.CheckUserStatusInDatabase(DiscordUserId);
            System.out.println(isUserInDatabase);
            if (!isUserInDatabase)
            {
                channel.sendMessage("User " + event.getAuthor().getName() + " is not yet authenticated. Type '!authenticate' to get started").queue();
            }
            else
            {
                AuthObject authObject = DatabaseConnection.GetUserData(DiscordUserId);
                String authenticationToken = authObject.AuthenticationToken;
                SpotifyAuth spotifyAuth = new SpotifyAuth();
                ArrayList<SpotifyTrack> topTracks = spotifyAuth.getCurrentUserTopTracks(authenticationToken);
                int trackRank = 0;
                for (int i = 0; i < topTracks.size(); i++)
                {
                    int rank = i + 1;
                    //channel.sendMessage(topTracks.get(i).toString()).queue();
                    StringBuilder trackMessage = new StringBuilder(". " + topTracks.get(i).getTrackName() + " by ");
                    ArrayList<SpotifyArtist> artists = topTracks.get(i).getArtists();
                    ArrayList<String> insults = new ArrayList<String>();
                    for (int j = 0; j < artists.size(); j++)
                    {
                        String artistName = artists.get(j).getName();
                        if (j == artists.size() - 1)
                        {
                            trackMessage.append(" ").append(artistName);
                        }
                        else
                        {
                            trackMessage.append(" ").append(artistName).append(", ");
                        }
                        try
                        {
                            ArrayList<Artist> artistsWithInsults = ArtistAssembler.CreateInsultsArray();
                            for (int k = 0; k < artistsWithInsults.size(); k++)
                            {
                                if (artistName == artistsWithInsults.get(k).getName())
                                {
                                    insults.addAll(artistsWithInsults.get(k).insultsList);
                                }
                            }
                        }
                        catch (FileNotFoundException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }

                    channel.sendMessage(trackMessage).queue();
                    channel.sendMessage(topTracks.get(i).getAlbum().getAlbumName()).queue();
                    if (insults.size() == 0)
                    {
                        channel.sendMessage("Wow, I don't have any insults for this artist . . . yet").queue();
                    }
                    else
                    {
                        for (int j = 0; j < insults.size(); j++)
                        {
                            channel.sendMessage(insults.get(j)).queue();
                        }
                    }
                    
                }
        
            }   
        }
    }
}
