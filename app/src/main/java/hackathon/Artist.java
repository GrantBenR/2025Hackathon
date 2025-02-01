package hackathon;

import java.util.*;

public class Artist {

    //Variables
    private String id;
    private String href;

    private String type = "artist";
    private String name;

    public ArrayList<String> insultsList;

    //Artist Constructor
    public Artist(String id, String href, String type, String name, ArrayList<String> insultsList){
        
        //Initialization
        this.id = id;
        this.href = href;
        this.type = type;
        this.name = name;

        this.insultsList = insultsList;

    }

    @Override
    public String toString(){
        return this.name + ": " + this.insultsList.get(1);
    }
    
    //Getter and setters
        //Setters
    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHREF(String href){
        this.href = href;
    }

    public void setType(String type){
        this.type = type;
    }

        //Getters
    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getHREF(){
        return this.href;
    }

    public String getType(){
        return this.type;
    }
}
