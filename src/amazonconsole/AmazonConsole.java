/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazonconsole;

import Networking.AWSClient;
import Networking.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
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
        AWSClient awsClient = new AWSClient();
        String url = "https://70r7hyxz72.execute-api.eu-west-1.amazonaws.com/development/tasks";
        awsClient.SetTarget(new URL(url));
        
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        String username;
        String password;
        while(!loggedIn){
            System.out.println("Please login");
            System.out.println("Username:");
            username = scanner.next();
            System.out.println("Password:");
            password = scanner.next();
            try{
                loggedIn = awsClient.login(username, password);
            }
            catch(WebApplicationException e){
                System.out.println("Unable to login, check your connection and spelling");
                e.printStackTrace();
            }
            while(loggedIn){
                System.out.println("Please select an action:");
                System.out.println("1: Create new task");
                System.out.println("2: Get existing task");
                //System.out.println("3: Update existing user");
                //System.out.println("4: Delete ");

                String selection = scanner.next();
                switch(selection){
                    case "1":{
                        JSONObject newTask = new JSONObject();
                        String title, description, street;
                        float price;
                        int zipaddress, urgent, supplies, ETC;
                        System.out.println("Create new task:");
                        boolean created = false;
                        while(!created){
                            try{
                                System.out.println("Title:");
                                title = scanner.next();
                                System.out.println("Description:");
                                scanner.next();
                                description = scanner.nextLine();
                                System.out.println("Price:");
                                price = scanner.nextFloat();
                                System.out.println("ECT:");
                                ETC = scanner.nextInt();
                                System.out.println("Supplies:");
                                supplies = scanner.nextInt();
                                System.out.println("Urgent:");
                                urgent = scanner.nextInt();
                                System.out.println("Street:");
                                street = scanner.next();  
                                System.out.println("ZipAddress:");
                                zipaddress = scanner.nextInt();
                                try {
                                    newTask.put("title", title);
                                    newTask.put("description", description);
                                    newTask.put("price", price);
                                    newTask.put("ETC", ETC);
                                    newTask.put("supplies", supplies);
                                    newTask.put("urgent", urgent);
                                    newTask.put("street", street);
                                    newTask.put("zipaddress", zipaddress);
                                    System.out.println(newTask.toString());
                                    awsClient.POST(newTask.toString());
                                } catch (JSONException ex) {
                                    System.out.println("Creation failed!");
                                    Logger.getLogger(AmazonConsole.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                created = true;
                            }
                            catch(InputMismatchException e){
                                System.out.println("Wrong input, please try again");
                                e.printStackTrace();
                            }
                        }
                    break;
                    }/*
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
                    }*/
                    case "2":{
                    String ID;
                    System.out.println("Select ID");
                    ID = scanner.next();
                    try {
                        awsClient.SetTarget(new URL(url + "/" + ID + "/"));
                        String res = awsClient.GET();
                        System.out.println(res);
                        JSONObject response = new JSONObject(res);
                        
                        System.out.println("title: " + response.getString("title"));
                        System.out.println("description: " + response.getString("description"));
                        System.out.println("price: " + response.getDouble("price"));
                        System.out.println("ETC: " + response.getInt("ETC"));
                        System.out.println("supplies: " + response.getBoolean("supplies"));
                        System.out.println("urgent: " + response.getBoolean("urgent"));
                        System.out.println("views: " + response.getInt("views"));
                        System.out.println("street: " + response.getString("street"));
                        System.out.println("zipaddress: " + response.getInt("zipaddress"));
                    } catch (JSONException ex) {
                        Logger.getLogger(AmazonConsole.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally{
                    awsClient.SetTarget(new URL(url));
                    }
                    break;
                    }
                    default:{
                        System.out.println("Please select either 1, 2 or 3.");
                    }
                }
            }
        }
    }
}