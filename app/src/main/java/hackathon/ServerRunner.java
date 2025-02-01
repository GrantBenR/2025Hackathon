package hackathon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerRunner
{
    public ServerRunner()
    {
        HttpServer server = null;
        try
        {
            server = HttpServer.create(new InetSocketAddress(8888), 0);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        server.createContext("/callback", new SpotifyAuthHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    static class SpotifyAuthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "You can close this window";
            String query = t.getRequestURI().getQuery();
            String shortQuery = query.substring(5,227);
            SpotifyAuth.getAccessToken(shortQuery);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(shortQuery.getBytes());
            os.close();
        }
    }

}
