package com.thlink.webBot.ai.google.REST;

import com.thlink.webBot.ai.google.REST.JSON.ReqAnsw;
import com.thlink.webBot.ai.google.REST.JSON.ReqAnsw2;
import com.thlink.webBot.sender.Evolution.findContacts.FindContatsResponseBody;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import lombok.Data;
import org.apache.log4j.Logger;

@Data
public class RestHelper 
{
    private final String USER_AGENT = "PostmanRuntime/7.39.0";
    public static final String ENCODING = "UTF-8";
    
    private static final Logger logger = Logger.getLogger(RestHelper.class);
    private Integer timeoutMs;
    
    public ReqAnsw sendPost2 (String url, String pBody) 
    {
      //logger.info("sendPost2");
        
        ReqAnsw ans = new ReqAnsw();
        try
        {
            URI uri = new URI(url);
            URL obj = uri.toURL();

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
          //con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json; charset=" + ENCODING);
            con.setRequestProperty("Accept", "*/*");
          //con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            con.setRequestProperty("Connection", "keep-alive");
            con.setUseCaches (false);
            con.setDoInput(true);
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream()))
            {
                wr.write(pBody.getBytes(ENCODING));
              //wr.writeBytes(pBody);
                wr.flush();
            }
            ans.setResponseCode(con.getResponseCode());
            if (ans.getResponseCode() == 200)
            {
                StringBuffer response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), ENCODING)))
                {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                  //System.out.println(response.toString());
                    ans.setResponse(response.toString());
                    return ans;
                }
            }
            else
            {
                StringBuffer response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), ENCODING)))
                {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
              //System.out.println(response.toString());
                ans.setResponse(response.toString());
                return ans;
            }
        } catch (Exception e)
        {
            logger.error("sendPost2 error " + e.toString());
            ans.setResponse(e.toString());
            ans.setResponseCode(-1);
            return ans;
        }
    }
    
    public ReqAnsw sendPost3 (String url, String pMethod, String pBody, List<String> lstHeaders, List<String> lstValues)
    {
        ReqAnsw ans = new ReqAnsw();
        try
        {
            URI uri = new URI(url);
            URL obj = uri.toURL();
            
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod(pMethod);
            for(int i = 0; i < lstHeaders.size(); i++)
            {
                con.setRequestProperty(lstHeaders.get(i), lstValues.get(i));
            }
            con.setUseCaches (false);
            con.setDoInput(true);
            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream()))
            {
                wr.write(pBody.getBytes(ENCODING));
              //wr.writeBytes(pBody);
                wr.flush();
            }
            ans.setResponseCode(con.getResponseCode());
            if (ans.getResponseCode() == 200 || ans.getResponseCode() == 201)
            {
                StringBuffer response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), ENCODING)))
                {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                  //System.out.println(response.toString());
                    ans.setResponse(response.toString());
                    return ans;
                }
            }
            else
            {
                StringBuffer response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), ENCODING)))
                {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
              //System.out.println(response.toString());
                ans.setResponse(response.toString());
                return ans;
            }
        } catch (Exception e)
        {
            System.out.println("sendPost3 error " + e.toString());
            ans.setResponse(e.toString());
            return ans;
        }
    }
    
    public ReqAnsw sendPost4 (String url, String pMethod, String pBody, HashMap<String, String> pHds)
    {
      //logger.info("sendPost4 (to=5)");
        ReqAnsw ans = new ReqAnsw();
        try
        {
            URI uri = new URI(url);
            URL obj = uri.toURL();

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod(pMethod);
            if (timeoutMs != null)
            {
                con.setConnectTimeout(timeoutMs);
                con.setReadTimeout(timeoutMs);
            }
            for (Entry<String, String> entry : pHds.entrySet())
            {
              //logger.info("Header: " + entry.getKey() + " = " + entry.getValue());
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
            con.setUseCaches (false);
            con.setDoInput(true);
            if (pBody != null && !pBody.isEmpty())
            {
                con.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(con.getOutputStream()))
                {
                    wr.write(pBody.getBytes(ENCODING));
                  //wr.writeBytes(pBody);
                    wr.flush();
                }
            }
            ans.setResponseCode(con.getResponseCode());
            if (ans.getResponseCode() == 200 || ans.getResponseCode() == 201)
            {
                StringBuffer response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), ENCODING)))
                {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                  //System.out.println(response.toString());
                    ans.setResponse(response.toString());
                    return ans;
                }
            }
            else
            {
                StringBuffer response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), ENCODING)))
                {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
              //System.out.println(response.toString());
                ans.setResponse(response.toString());
                return ans;
            }
        } catch (Exception e)
        {
            logger.error("sendPost4 '" + url + "' ERROR: " + e.toString());
            ans.setResponseCode(-1);
            ans.setResponse(e.toString());
            return ans;
        }
    }
    
    public ReqAnsw2 sendPostResponseZip (String url, String pMethod, String pBody, HashMap<String, String> pHds)
    {
        ReqAnsw2 ans = new ReqAnsw2();
        try
        {
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod(pMethod);
            for (Entry<String, String> entry : pHds.entrySet())
            {
              //System.out.println("Header: " + entry.getKey() + " = " + entry.getV+ alue());
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
            con.setUseCaches (false);
            con.setDoInput(true);
            if (pBody != null && !pBody.isEmpty())
            {
                con.setDoOutput(true);
                try (DataOutputStream wr = new DataOutputStream(con.getOutputStream()))
                {
                    wr.write(pBody.getBytes(ENCODING));
                  //wr.writeBytes(pBody);
                    wr.flush();
                }
            }
            ans.setResponseCode(con.getResponseCode());
            if (ans.getResponseCode() == 200 || ans.getResponseCode() == 201)
            {
                StringBuilder response;
                InputStream inputStream;
                
                if ("gzip".equalsIgnoreCase(con.getContentEncoding())) 
                    inputStream = new GZIPInputStream(con.getInputStream());
                else
                    inputStream = con.getInputStream();

                try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, ENCODING)))
                {
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine).append("\n");
                    }
                  //System.out.println(response.toString().substring(0, 1000));
                    String[] lines = response.toString().split("\"id\":");
                    List<FindContatsResponseBody> list = new ArrayList<>();
                    for (int i = 0; i < lines.length; i++)
                    {
                      //System.out.println(lines[i]);
                        if (i > 0)
                        {
                            String redo = "{\"id\":".concat(lines[i].replace(",{", ""));
                            if (redo.endsWith("]\n"))
                                redo = redo.substring(0, redo.length()-2);
                            FindContatsResponseBody c = FindContatsResponseBody.jsonToObj(redo);
                            if (c != null)
                                list.add(c);
                        }
                    }
                    ans.setListContacts(list);
                    return ans;
                }
            }
            else
            {
                StringBuffer response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), ENCODING)))
                {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
              //System.out.println(response.toString());
                ans.setErroMessage(response.toString());
                return ans;
            }
        } catch (Exception e)
        {
            System.out.println("sendPostResponseZip error " + e.toString());
            ans.setErroMessage(e.toString());
            return ans;
        }
    }
}
