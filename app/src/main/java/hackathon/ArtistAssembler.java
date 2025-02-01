package hackathon;

import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

/**
 * The goal of ArtistAssembler is to take all the information put into a seperate text document and create an array list of all the artists with it
 */
public class ArtistAssembler {

    public static void main(String[] args) throws Exception
    {

    }
    public static ArrayList<Artist> CreateInsultsArray() throws FileNotFoundException {
        String absolutePath = Paths.get("").toAbsolutePath().getParent().toString();
        String fileName = absolutePath + "/InsultInfo.txt";
        FileInputStream fInStream = new FileInputStream(fileName);
        Scanner scnr = new Scanner(fInStream);

        ArrayList<Artist> artistsList = new ArrayList<>();

        while (scnr.hasNextLine()) {
            String name = "";

            // Read name
            if(scnr.hasNextLine()){
                name = scnr.nextLine();
            }

            // Read the 3 artists into an ArrayList
            ArrayList<String> insults = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                insults.add(scnr.nextLine());
            }

            // Create and print MyObject instance
            Artist newArtist = new Artist("", "", "", name, insults);
            artistsList.add(newArtist);

            // Skip any blank lines (separators between objects)
            // while (scnr.hasNextLine() && scnr.nextLine().trim().isEmpty()) {
            //     // Do nothing (just skip the blank line)
            // }
        }
        return artistsList;
    }
}
