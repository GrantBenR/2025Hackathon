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

            //Name
            if(scnr.hasNextLine()){
                newArtist.setName(scnr.nextLine());
            }else{
                break;
            }
            int Counter = 3; //3 for 3 insults to add to the list after each artist
            
            while(Counter > 0){
                newArtist.insultsList.add(scnr.nextLine());
                Counter--;
            }

            //Add the artist to the list
            artistsList.add(newArtist);

            //Reset values for the next loop around
            newArtist = null;
            insultList = null;
        }

        System.out.println(artistsList);
    }
}
