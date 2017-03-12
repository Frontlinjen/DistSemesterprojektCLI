/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazonconsole;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;    
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Thomas
 */
public class AmazonConsole {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RestCommandsInterface commands = new RestCommands();
        Client client = ClientBuilder.newClient();
        String url = null;
        JSONObject newObject = new JSONObject();
        /*
        Build the new account
        */
        /* TEST
        System.out.println(commands.get(url, client));
        System.out.println(commands.post(url, client, newObject));
        System.out.println(commands.put(url, client, newObject));
        System.out.println(commands.delete(url, client));
        */
        
        System.out.println("Please select an action:");
        System.out.println("1: Create new user");
        System.out.println("2: Change existing user");
        System.out.println("3: Delete user");
        
        Scanner scanner = new Scanner(System.in);
        boolean selectionChosen = true;
        while(selectionChosen){
            System.out.println("Please select an action:");
            System.out.println("1: Create new user");
            System.out.println("2: Change existing user");
            System.out.println("3: Delete user");
            String selection = scanner.next();
            switch(selection){
                case "1":{
                    JSONObject newAccount = new JSONObject();
                    String name, email, phone, aboutMe;
                    boolean sex = true;
                    System.out.println("Create new user:");
                    System.out.println("Name:");
                    name = scanner.nextLine();
                    System.out.println("Email:");
                    email = scanner.nextLine();
                    System.out.println("Phone:");
                    phone = scanner.nextLine();
                    System.out.println("Sex:");
                    if(scanner.nextLine().equals("male")){
                        sex = true;
                    }
                    else if(scanner.nextLine().equals("female")){
                        sex = false;
                    }
                    System.out.println("About me:");
                    aboutMe = scanner.nextLine();
                    
                try {
                    newAccount.put("name", name);
                    newAccount.put("email", email);
                    newAccount.put("phone", phone);
                    newAccount.put("sex", sex);
                    newAccount.put("aboutMe", aboutMe);
                    selectionChosen = false;
                    System.out.println("User created!");
                    
                    commands.post(url, client, newAccount);
                } catch (JSONException ex) {
                    Logger.getLogger(AmazonConsole.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Creation failed!");
                }
                break;
                }
                case "2":{
                    JSONObject newAccount = new JSONObject();
                    String name, email, phone, aboutMe;
                    boolean sex = true;
                    System.out.println("Change existing user:");
                    System.out.println("Name:");
                    name = scanner.nextLine();
                    System.out.println("Email:");
                    email = scanner.nextLine();
                    System.out.println("Phone:");
                    phone = scanner.nextLine();
                    System.out.println("Sex:");
                    if(scanner.nextLine().equals("male")){
                        sex = true;
                    }
                    else if(scanner.nextLine().equals("female")){
                        sex = false;
                    }
                    System.out.println("About me:");
                    aboutMe = scanner.nextLine();
                    
                try {
                    newAccount.put("name", name);
                    newAccount.put("email", email);
                    newAccount.put("phone", phone);
                    newAccount.put("sex", sex);
                    newAccount.put("aboutMe", aboutMe);
                    selectionChosen = false;
                    System.out.println("User changed!");
                    
                    commands.put(url, client, newAccount);
                } catch (JSONException ex) {
                    Logger.getLogger(AmazonConsole.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Change failed!");
                }
                break;
                }
                case "3":{
                    commands.delete(url, client);
                    selectionChosen = false;
                break;
                }
                case "4":{
                    JSONObject response = commands.get(url, client);
                try {
                    System.out.println("UserID: " + response.getString("userID"));
                    System.out.println("Name: " + response.getString("name"));
                    System.out.println("Email: " + response.getString("email"));
                    System.out.println("Phone: " + response.getString("phone"));
                    if(scanner.nextLine().equals("male")){
                        System.out.println("Sex: male");
                    }
                    else if(scanner.nextLine().equals("female")){
                        System.out.println("Sex: female");
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(AmazonConsole.class.getName()).log(Level.SEVERE, null, ex);
                }
                    selectionChosen = false;
                break;
                }
                default:{
                    System.out.println("Please select either 1, 2 or 3.");
                }
            }
        }
    }
}