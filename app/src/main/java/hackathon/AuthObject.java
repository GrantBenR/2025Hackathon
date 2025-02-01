package hackathon;

public class AuthObject 
{
    public String AuthObjectId;
    public String DiscordUserId;
    public String AuthenticationToken;
    public String RefreshToken;
    public String DateCreated;
    public AuthObject(String authObjectId, String discordUserId, String authenticationToken, String refreshToken, String dateCreated)
    {
        this.AuthObjectId = authObjectId;
        this.DiscordUserId = discordUserId;
        this.AuthenticationToken = authenticationToken;
        this.RefreshToken = refreshToken;
        this.DateCreated = dateCreated;
    }
}
