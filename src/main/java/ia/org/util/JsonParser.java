package ia.org.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class JsonParser {


    //Splits fileRequested url at ?, returns second half
    public static String urlToJson(String inParam) {
        String strArray[] = inParam.split("[?]", 2);

        return strArray[1];
    }

    //Turns fileRequested url format to json format.
    public static String stringToJsonFormat(String inParam) {
        inParam = inParam.replaceAll("=", "\":\"");
        inParam = inParam.replaceAll("&", "\",\"");

        return "{\"" + inParam + "\"}";
    }

    public static JSONObject makeJsonObject(String key, String value) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);

        return obj;
    }

    public static JSONArray makeJsonArray(JSONObject... jsonObjects) {
        JSONArray list = new JSONArray();

        for (JSONObject object: jsonObjects) {
            list.add(object);
        }

        return list;
    }

    void makeHtmlJsonConvertion(String url){

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Title Of the page");
        sb.append("</title>");
        sb.append("</head>");
        sb.append("<body> <b>" + stringToJsonFormat(urlToJson(url)) + "</b>");
        sb.append("</body>");
        sb.append("</html>");
        FileWriter fstream = null;
        try {
            fstream = new FileWriter("MyHtml.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter out = new BufferedWriter(fstream);
        try {
            out.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}