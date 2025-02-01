package hackathon;

import java.util.*;
import java.io.*;

/**
 * The goal of ArtistAssembler is to take all the information put into a seperate text document and create an array list of all the artists with it
 */
public class ArtistAssembler {

    public static void main(String[] args) throws Exception{
        //Variables
        String fileName = "InsultInfo.txt";

        FileInputStream fInStream = new FileInputStream(fileName);
        Scanner scnr = new Scanner(fInStream);

        ArrayList<Artist> artistsList = new ArrayList<>();
        
        while (scnr.hasNextLine()) {
            String name = "";

            // Skip any leading blank lines
            while (scnr.hasNextLine() && scnr.nextLine().trim().isEmpty()) {
                // Do nothing, just skip the blank line
            }
            
            // Read name
            if(scnr.hasNextLine()){
                name = scnr.nextLine();
            }

            // Read the 3 artists into an ArrayList
            ArrayList<String> insults = new ArrayList<>();
            for (int i = 0; i < 3 && scnr.hasNextLine(); i++) {
                insults.add(scnr.nextLine().trim());
            }

            // Create and print MyObject instance
            Artist newArtist = new Artist("", "", "", name, insults);
            System.out.println(newArtist.toString());

            // Skip any blank lines (separators between objects)
            while (scnr.hasNextLine() && scnr.nextLine().trim().isEmpty()) {
                // Do nothing (just skip the blank line)
            }
        }
    }
}
