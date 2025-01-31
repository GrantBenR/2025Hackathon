package hackathon;

import java.util.*;

public class Artist {

    //Variables
    private String id;
    private String href;

    private String type = "artist";
    private String name;

    private ArrayList<String> insultsList;
    //Artist Constructor
    public Artist(String id, String href, String type, String name, ArrayList<String> insultsList){
        
        //Initialization
        this.id = id;
        this.href = href;
        this.type = type;
        this.name = name;

        this.insultsList = insultsList;

    }
    

}
