package ia.org.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonParser {


    //Splits fileRequested url at ?, returns second half
    public static String urlToString(String inParam) {
        String strArray[] = inParam.split("[?]", 2);

        return strArray[1];
    }

    public static String urlToJson(String inParam) {
        return stringToJsonFormat(urlToString(inParam));
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

    public static String makeHtmlJsonConvertion(String url){

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>Title Of the page");
        sb.append("</title>");
        sb.append("</head>");
        sb.append("<body> <b>" + stringToJsonFormat(urlToString(url)) + "</b>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

}