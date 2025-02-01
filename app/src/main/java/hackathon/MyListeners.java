package hackathon;

//Imports
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
                                String trimmedArtistName = artistName.replace("\"", "").trim();
                                // System.out.println(trimmedArtistName);
                                // System.out.println(artistsWithInsults.get(k).getName());
                                if (trimmedArtistName.equals(artistsWithInsults.get(k).getName()))
                                {
                                    System.out.println("match found for " + trimmedArtistName);
                                    insults.addAll(artistsWithInsults.get(k).insultsList);
                                }
                            }
                        }
                        catch (FileNotFoundException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }

                    // channel.sendMessage(trackMessage + "\n" + ).queue();
                    // channel.sendMessage(topTracks.get(i).getAlbum().getAlbumName()).queue();
                    String insultMessage = "";
                    if (insults.size() == 0)
                    {
                        insultMessage = "Wow, I don't have any insults for this artist . . . yet";
                    }
                    else
                    {
                        Random random = new Random();
                        System.out.println(insults.size());
                        int randomInsultIndex = random.nextInt(0, insults.size() - 1);
                        insultMessage = insults.get(randomInsultIndex);
                    }
                    channel.sendMessage(trackMessage + "\nFeatured on: " + topTracks.get(i).getAlbum().getAlbumName() + "\n" + insultMessage + "\n------------------\n").queue();
                }
        
            }   
        }
    }
}
