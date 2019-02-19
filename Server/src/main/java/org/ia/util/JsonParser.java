package org.ia.util;

import org.bson.json.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {


    //Splits fileRequested url at ?, returns second half
    public static String urlToString(String inParam) {
        String firstHalf = "";
        String secondHalf = "";
        if (inParam.contains("?")) {
            String strArray[] = inParam.split("[?]", 2);
            firstHalf = strArray[0];
            secondHalf = strArray[1];
        }

        if (secondHalf.length() > 1) {
            return secondHalf;
        } else return firstHalf;

    }

    public static String urlToJson(String inParam) {
        return stringToJsonFormat(urlToString(inParam)).toString();
    }

    public static JSONObject stringToJson (String inParam) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse("{\"" + inParam + "\"}");
            System.out.println(obj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }


    //Turns fileRequested url format to json format.
    public static JSONObject stringToJsonFormat(String inParam) {
        inParam = inParam.replaceAll("=", "\":\"");
        inParam = inParam.replaceAll("&", "\",\"");

        JSONObject obj = stringToJson(inParam);

        return obj;
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

    public static String makeHtmlJsonConvertion(String url){

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Json data!");
        sb.append("</title>");
        sb.append("</head>");
        sb.append("<body> <b>" + urlToJson(url) + "</b>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

}