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

        while(true){
            ArrayList<String> insultList = new ArrayList<>();
            Artist newArtist = new Artist("", "", "", "", insultList);

            if(scnr.hasNextLine()){
                newArtist.setName(scnr.nextLine());
            }
        }
    }
}
