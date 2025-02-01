package hackathon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
  public static void main( String args[] ) {
      Connection c = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Opened database successfully");
      //CREATE MAIN TABLE CODE (No longer necessary)

      // String query = "CREATE TABLE AuthObjects (AuthObjectsId INTEGER NOT NULL UNIQUE, DiscordUserId TEXT NOT NULL UNIQUE, AuthenticationToken TEXT NOT NULL, RefreshToken TEXT NOT NULL, DateCreated TEXT NOT NULL, PRIMARY KEY (AuthObjectsId AUTOINCREMENT));";
      // try (Statement stmt = c.createStatement()) {
      //    stmt.executeUpdate(query);
      //  } 
      //  catch (SQLException e) {
      //    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      //    System.exit(0);
      //  }
   }
   public static String CheckUserStatusInDatabase(String userId)
   {
      Connection c = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Opened database successfully");
      String query = "SELECT * FROM AuthObjects WHERE DiscordUserId == " + userId + ";";
      try (Statement stmt = c.createStatement()) 
      {
         String DiscordUserId = "";
         String AuthenticationToken = "";
         String RefreshToken = "";
         String DateCreated = "";
         ResultSet rs = stmt.executeQuery(query);
         while (rs.next()) {
         DiscordUserId = rs.getString("DiscordUserId");
         AuthenticationToken = rs.getString("AuthenticationToken");
         RefreshToken = rs.getString("RefreshToken");
         DateCreated = rs.getString("DateCreated");
         System.out.println("DiscordUserId: " + DiscordUserId + " AuthenticationToken: " + AuthenticationToken + " RefreshToken: " + RefreshToken + " Date Created: " + DateCreated);
         }
         return "";
      } 
      catch (SQLException e) 
      {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      return null;
   }
   public static void InsertNewUserIntoDatabase(String DiscordUserId, String AuthenticationToken, String RefreshToken, String DateCreated)
   {
      Connection c = null;
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Opened database successfully");
      String query = "INSERT INTO AuthObjects (DiscordUserId, AuthenticationToken, RefreshToken, DateCreated) VALUES (" + DiscordUserId + ", " + AuthenticationToken + ", " + RefreshToken + ", " + DateCreated + ");";
      try (Statement stmt = c.createStatement()) {
         stmt.executeUpdate(query);
       } 
       catch (SQLException e) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
       }
   }
}
