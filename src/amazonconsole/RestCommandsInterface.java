/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazonconsole;

import javax.ws.rs.client.Client;
import org.json.JSONObject;

/**
 *
 * @author Thomas
 */
public interface RestCommandsInterface {
    public JSONObject get(String url, Client client);
    public String post(String url, Client client, JSONObject object);
    public String put(String url, Client client, JSONObject object);
    public String delete(String url, Client client);
}
