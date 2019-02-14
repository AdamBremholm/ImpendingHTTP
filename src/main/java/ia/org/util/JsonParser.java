package ia.org.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonParser {


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
            System.out.println(object.toString());
        }

        System.out.println(list.toString());
        return list;
    }

}