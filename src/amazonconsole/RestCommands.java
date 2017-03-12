/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazonconsole;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Thomas
 */
public class RestCommands implements RestCommandsInterface{
    @Override
    public JSONObject get(String url, Client client){
        JSONObject result = null;
        try{
            String answer = client.target(url).request(MediaType.APPLICATION_JSON).get(String.class);
            System.out.println(answer);
            try {
                result = new JSONObject(answer);
            } catch (JSONException ex) {
                Logger.getLogger(AmazonConsole.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(WebApplicationException ex){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return result;
    }
    @Override
    public String post(String url, Client client, JSONObject json){
        String status = null;
        try{
            String answer = client.target(url).request(MediaType.APPLICATION_JSON).post(Entity.json(json), String.class);
            System.out.println(answer);
            status = answer;
        }
        catch(WebApplicationException ex){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return status;
    }
    @Override
    public String put(String url, Client client, JSONObject json){
        String status = null;
        try{
            String answer = client.target(url).request(MediaType.APPLICATION_JSON).put(Entity.json(json), String.class);
            System.out.println(answer);
            status = answer;
        }
        catch(WebApplicationException ex){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return status;
    }
    @Override
    public String delete(String url, Client client){
        String status = null;
        try{
            String answer = client.target(url).request(MediaType.APPLICATION_JSON).delete(String.class);
            System.out.println(answer);
            status = answer;
        }
        catch(WebApplicationException ex){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return status;
    }
    
}
