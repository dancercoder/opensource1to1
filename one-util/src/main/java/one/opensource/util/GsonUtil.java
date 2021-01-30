package one.opensource.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(GsonUtil.class);

    public static String toJsonString(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static String toCamelJsonString(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson  = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = gson.fromJson(gsonString, cls);
        return t;
    }

    public static <T> T jsonToCamelBean(String gsonString, Class<T> cls){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson  = gsonBuilder.create();
        T t = gson.fromJson(gsonString, cls);
        return t;
    }

    public static  <T> List<T> parseList(String json, Class<T> clazz){
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz}, null);
        Gson gson = new Gson();
        return (List<T>) gson.fromJson(json, listType);
    }

    public static  <K,V> Map<K,V> parseMap(String json, Class<K> keyClazz, Class<V> valueClazz){
        Type mapType = new ParameterizedTypeImpl(Map.class, new Class[]{keyClazz, valueClazz}, null);
        Gson gson = new Gson();
        return (Map<K, V>) gson.fromJson(json, mapType);
    }

    /**
     * 复杂类型定义
     */
    public static class ParameterizedTypeImpl implements ParameterizedType {

        private final Type rawType; //原始类型，
        private final Type[] actualTypeArguments; //泛型变量类型
        private final Type   ownerType; //所有者类型

        public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType){
            this.rawType = rawType;
            this.actualTypeArguments = actualTypeArguments;
            this.ownerType = ownerType;
        }

        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        public Type getOwnerType() {
            return ownerType;
        }

        public Type getRawType() {
            return rawType;
        }
    }
}
