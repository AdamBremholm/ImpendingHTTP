package ia.util;

public class JsonParser {

    public static String paramJson(String inParam) {
        inParam = inParam.replaceAll("=", "\":\"");
        inParam = inParam.replaceAll("&", "\",\"");
        return "{\"" + inParam + "\"}";
    }
}