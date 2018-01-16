package cn.truistic.enmicromsg.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonHelper {
  
  
    public static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())  
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())  
                .registerTypeAdapter(String.class, new StringAdapter())  
                .create();  
    }  
  
    public static class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override  
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {  
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为int类型,如果后台返回""或者null,则返回0  
                    return 0;  
                }  
            } catch (Exception ignore) {  
            }  
            try {  
                return json.getAsInt();  
            } catch (NumberFormatException e) {  
                throw new JsonSyntaxException(e);
            }  
        }  
  
        @Override  
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }  
    }  
  
    public static class StringAdapter implements JsonDeserializer<String> {  
  
        @Override  
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {  
            try {  
                return json.getAsString();  
            } catch (Exception e) {  
                return null;  
            }  
        }  
    }  
  
}