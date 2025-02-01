package hackathon;

import java.sql.*;

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
      boolean isUserInDatabase = CheckUserStatusInDatabase("1234557799650");
      System.out.println(isUserInDatabase);
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
   public static Boolean CheckUserStatusInDatabase(String userId)
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
      String query = "SELECT * FROM AuthObjects WHERE DiscordUserId == ?;";
      try (PreparedStatement stmt = c.prepareStatement( query ))
      {
         String DiscordUserId = "";
         String AuthenticationToken = "";
         String RefreshToken = "";
         String DateCreated = "";
         stmt.setString(1, userId);
         ResultSet rs = stmt.executeQuery(query);
         while (rs.next()) 
         {
            DiscordUserId = rs.getString("DiscordUserId");
            AuthenticationToken = rs.getString("AuthenticationToken");
            RefreshToken = rs.getString("RefreshToken");
            DateCreated = rs.getString("DateCreated");
            System.out.println("DiscordUserId: " + DiscordUserId + " AuthenticationToken: " + AuthenticationToken + " RefreshToken: " + RefreshToken + " Date Created: " + DateCreated);
         }
          return (DiscordUserId != null && DiscordUserId != "");
      } 
      catch (SQLException e) 
      {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      return false;
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
      String query = "INSERT INTO AuthObjects (DiscordUserId, AuthenticationToken, RefreshToken, DateCreated) VALUES (?, ?, ?, ?);";
      try (PreparedStatement pstmt = c.prepareStatement( query ))
      {
          pstmt.setString( 1, DiscordUserId);
          pstmt.setString( 2, AuthenticationToken);
          pstmt.setString( 3, RefreshToken);
          pstmt.setString( 4, DateCreated);
          pstmt.executeUpdate(query);
      } 
      catch (SQLException e) 
      {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
   }
   public static AuthObject GetUserData(String userId)
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
      String query = "SELECT * FROM AuthObjects WHERE DiscordUserId == ?;";
      try (PreparedStatement pstmt = c.prepareStatement( query ))
      {

         String AuthObjectsId = "";
         String DiscordUserId = "";
         String AuthenticationToken = "";
         String RefreshToken = "";
         String DateCreated = "";
         pstmt.setString( 1, userId);
         ResultSet rs = pstmt.executeQuery(query);
         while (rs.next()) 
         {
            AuthObjectsId = rs.getString("AuthObjectsId");
            DiscordUserId = rs.getString("DiscordUserId");
            AuthenticationToken = rs.getString("AuthenticationToken");
            RefreshToken = rs.getString("RefreshToken");
            DateCreated = rs.getString("DateCreated");
            System.out.println("DiscordUserId: " + DiscordUserId + " AuthenticationToken: " + AuthenticationToken + " RefreshToken: " + RefreshToken + " Date Created: " + DateCreated);
         }
         AuthObject authObject = new AuthObject(AuthObjectsId, DiscordUserId, AuthenticationToken, RefreshToken, DateCreated);
         return authObject;
      } 
      catch (SQLException e) 
      {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      return null;
   }
   public static Boolean UpdateUserData(String DiscordUserId, String AuthorizationToken, String RefreshToken, String DateCreated) throws SQLException {
       Connection c = null;
       try {
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:test.db");
       } catch ( Exception e ) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
       }
       System.out.println("Opened database successfully");
       String query = "UPDATE AuthObjects SET AuthorizationToken = ?, RefreshToken = ?, DateCreated = ? WHERE DiscordUserId == ?;";
       PreparedStatement pstmt = c.prepareStatement( query );
       pstmt.setString( 1, AuthorizationToken);
       pstmt.setString( 2, RefreshToken);
       pstmt.setString( 3, DateCreated);
       pstmt.setString( 4, DiscordUserId);
       pstmt.executeUpdate(query);
       return true;
   }
}
