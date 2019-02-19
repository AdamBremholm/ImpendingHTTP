package org.ia.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {

    //*JsonParser.
    // sliceUrl: Takes url that contains a ?, removes everything before the ?.
    // formatSlicedUrl: Takes sliced url, replaces = and & with relevant Json symbols.
    // urlToFormattedString: Simple sliceUrl + formatSlicedUrl. Use with any raw url to get string back.
    // formattedUrlToJson: Takes url with relevant symbols, returns as Json object. Used to read BODY json.
    // */

    //Splits fileRequested url at ?, returns second half
    public static String sliceUrl(String url) {
        String firstHalf = "";
        String secondHalf = "";
        if (url.contains("?")) {
            String strArray[] = url.split("[?]", 2);
            firstHalf = strArray[0];
            secondHalf = strArray[1];
        }

        if (secondHalf.length() > 1) {
            return secondHalf;
        } else return firstHalf;

    }

    public static JSONObject formatSlicedUrl(String slicedUrl) {
        slicedUrl = slicedUrl.replaceAll("=", "\":\"");
        slicedUrl = slicedUrl.replaceAll("&", "\",\"");

        JSONObject obj = formattedUrlToJson(slicedUrl);

        return obj;
    }

    public static JSONObject formattedUrlToJson(String formattedString) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse("{\"" + formattedString + "\"}");
            System.out.println(obj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String urlToFormattedString(String url) {
        return formatSlicedUrl(sliceUrl(url)).toString();
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

}