package id.halalvestor.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import id.halalvestor.model.AppUser;
import id.halalvestor.model.QuestionParameter;

public class PreferenceManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Context _context;

    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Halalvestor";

    public PreferenceManager(Context context) {
        this._context = context;
        preferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String HAS_UNREAD_MESSAGE = "unread_message";
    private static final String SURVEY_ANSWERS = "survey_answers";
    private static final String APP_USER = "app_user";
    private static final String LAST_LOGIN_USER = "last_login_user";
    private static final String LAST_ACTIVE = "last_active2";
    private static final String FINGERPRINT_ENABLED = "fingerprint_enabled";
    private static final String BAREKSA_LOGGED_IN = "bareksa_logged_in";
    private static final String ENCRYPTION_KEY = "encryption_key";
    private static final String INSURANCE_PORTFOLIO = "insurance_portfolio";

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return preferences.getString(KEY_NOTIFICATIONS, null);
    }

    public void setNotification(String notification) {
        editor.putString(KEY_NOTIFICATIONS, notification);
        editor.commit();
    }

    public void setUnreadMessage(Boolean param){
        editor.putBoolean(HAS_UNREAD_MESSAGE, param);
        editor.commit();
    }

    public Boolean hasUnreadMessage(){
        try {
            return preferences.getBoolean(HAS_UNREAD_MESSAGE, Boolean.FALSE);
        }catch(Exception ex){
            return null;
        }
    }

    private static final String TAG = PreferenceManager.class.toString();

    public void putSurveyAnswers(Integer key, List<QuestionParameter> value){
        Map<Integer, List<QuestionParameter>> map = getSurveyAnswers();
        map.put(key,value);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
        String json = gson.toJson(map);
        editor.putString(SURVEY_ANSWERS, json);
        editor.commit();
    }

    public void setSurveyAnswers(Map<Integer, List<QuestionParameter>> map){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
        String json = gson.toJson(map);
        editor.putString(SURVEY_ANSWERS, json);
        editor.commit();
    }

    public List<QuestionParameter> getSurveyAnswers(Integer question){
        Map<Integer, List<QuestionParameter>> map = getSurveyAnswers();
        return map.get(question);
    }

    public Map<Integer, List<QuestionParameter>> getSurveyAnswers(){
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
            String json = preferences.getString(SURVEY_ANSWERS, "");
            if (json.equals("")){
                return new TreeMap<Integer, List<QuestionParameter>>();
            }
            return gson.fromJson(json, new TypeToken<Map<Integer, List<QuestionParameter>>>() {}.getType());
        }catch(Exception ex){
            return null;
        }
    }

    public void setAppUser(AppUser appUser){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
        String json = gson.toJson(appUser);
        editor.putString(APP_USER, json);
        editor.commit();
    }

    public AppUser getAppUser(){
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").enableComplexMapKeySerialization().create();
            String json = preferences.getString(APP_USER, "");
            return gson.fromJson(json, AppUser.class);
        }catch(Exception ex){
            return null;
        }
    }

    public void updateLastActive(String uid, int flag){
        Map<String, Long> map = getLastActiveMap();
        switch (flag){
            case 0:
                map.put(uid, 0l);
                break;
            case 1:
                map.put(uid, System.currentTimeMillis());
                break;
        }
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String json = gson.toJson(map);
        editor.putString(LAST_ACTIVE, json);
        editor.commit();
    }

    public long getLastActive(String uid){
        Map<String, Long> map = getLastActiveMap();
        long value = System.currentTimeMillis();
        if (map.containsKey(uid)){
            value = map.get(uid);
        }
        return value;
    }

    private Map<String, Long> getLastActiveMap(){
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String json = preferences.getString(LAST_ACTIVE, "");
            if (json.equals("")){
                return new TreeMap<String, Long>();
            }
            return gson.fromJson(json, new TypeToken<Map<String, Long>>() {}.getType());
        }catch(Exception ex){
            return null;
        }
    }

    public boolean isFingerprintEnabled(String uid){
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String json = preferences.getString(FINGERPRINT_ENABLED, "");
            if (!json.equals("")) {
                Map<String,Boolean> map = gson.fromJson(json, new TypeToken<Map<String, Boolean>>() {
                }.getType());
                if (map.containsKey(uid)){
                    return map.get(uid);
                }
            }
        }catch(Exception ex){
        }
        return false;
    }


    public void setEnabledFingerprint(String uid, boolean isEnabled){
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String json = preferences.getString(FINGERPRINT_ENABLED, "");
            Map<String,Boolean> map = new TreeMap<>();
            if (!json.equals("")) {
                map = gson.fromJson(json, new TypeToken<Map<String, Boolean>>() {}.getType());
            }
            map.put(uid,isEnabled);
            editor.putString(FINGERPRINT_ENABLED, gson.toJson(map));
            editor.commit();
        }catch(Exception ex){
        }
    }

    public void setBareksaLoggedIn(String uid, boolean isEnabled){
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String json = preferences.getString(BAREKSA_LOGGED_IN, "");
            Map<String,Boolean> map = new TreeMap<>();
            if (!json.equals("")) {
                map = gson.fromJson(json, new TypeToken<Map<String, Boolean>>() {}.getType());
            }
            map.put(uid,isEnabled);
            editor.putString(BAREKSA_LOGGED_IN, gson.toJson(map));
            editor.commit();
        }catch(Exception ex){
        }
    }

    public boolean isBareksaLoggedIn(String uid){
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String json = preferences.getString(BAREKSA_LOGGED_IN, "");
            if (!json.equals("")) {
                Map<String,Boolean> map = gson.fromJson(json, new TypeToken<Map<String, Boolean>>() {
                }.getType());
                if (map.containsKey(uid)){
                    return map.get(uid);
                }
            }
        }catch(Exception ex){
        }
        return false;
    }

    public void setLastLoginUser(String uid){
        editor.putString(LAST_LOGIN_USER, uid);
        editor.commit();
    }

    public void setEncryptionKey(String key){
        editor.putString(ENCRYPTION_KEY, key);
        editor.commit();
    }

    public String getLastLoginUser(){
        return preferences.getString(LAST_LOGIN_USER, "");
    }

    public String getEncryptionKey(){
        return preferences.getString(ENCRYPTION_KEY, "");
    }
}
