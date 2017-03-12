/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 *
 * @author Hjorthen
 */
public class URL{
	private String hostname;
	private String path;
	public Map<String, String> parameters;
	
	public String getHostname() {
		return hostname;
	}
        public String getFullPath() {
            return hostname + path + "?" + getCanonicalQuery(); 
        }
	public String getPath() {
		return path;
	}

	public URL(String url)
	{
		hostname = parseHostname(url);
		path = parsePath(url);
		parameters = parseParameters(url);
		
	}
	
	Map<String, String> parseParameters(String url)
	{
		Map<String, String> ret = new HashMap<String, String>();
		int start = url.indexOf("?");
		if(start != -1)
		{
			String split = url.substring(start+1, url.length());
			String[] splits = split.split("&");
			for (int i = 0; i < splits.length; i++) {
				String[] kval = splits[i].split("=");
				ret.put(kval[0], kval[1]);
			}
		}
		return ret;
	}
	
	public void addParameter(String key, String value)
	{
		parameters.put(key, value);
	}
	
	private String parsePath(String url)
	{
		int start = url.indexOf("//");
		String result;
		if(start != -1){
			start = url.indexOf('/', start+2);
		}
		else
		{
			start = url.indexOf('/');
		}
		int end = url.indexOf('?');
		if(end != -1){
			result = url.substring(start, end);
		}
		else
		{
			result = url.substring(start, url.length());
		}
		return result;
	}
	private String parseHostname(String url)
	{
		int start = url.indexOf("//") + 1;
		int end = url.indexOf("/", start+1);
		String result;
		if(start==0 && end ==-1)
		{
			result = url;
		}
		else if(start==0)
		{
			result = url.substring(0, end);
		}
		else if(end==-1)
		{
			result = url.substring(start + 1, url.length());
		}
		else
		{
			result = url.substring(start + 1, end);
		}
		return result;
	}
        
        public String getCanonicalQuery()
	{
		Set<String> keys = parameters.keySet();
		String[] keyArray = new String[keys.size()];
		keyArray = keys.toArray(keyArray);
		Arrays.sort(keyArray);
		
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < keyArray.length; i++) {
			String key = keyArray[i];
                        if(i==0)
                        {
                            res.append(key + "=" + parameters.get(key));
                        }
                        else
                        {
                            res.append("&" + key + "=" + parameters.get(key));
                        }
		}
		//res.delete(res.length()-1, res.length()); //Removes the trailing &
		return res.toString();
	}
	@Override
	public String toString() {
		String root = "Hostname: " + hostname + "\npath: " + path;
		Iterator itr = parameters.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String, String> value = (Map.Entry<String, String>)itr.next();
			root += "\n" + value.getKey() + " " + value.getValue();
		}
		return root;
	}
}
