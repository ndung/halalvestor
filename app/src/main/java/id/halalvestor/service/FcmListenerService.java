package id.halalvestor.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.activity.ChatActivity;
import id.halalvestor.activity.SplashActivity;
import id.halalvestor.model.ChatMessage;

public class FcmListenerService extends FirebaseMessagingService {

    private static final String TAG = FcmListenerService.class.toString();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        try {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            String from = remoteMessage.getFrom();
            Map bundle = remoteMessage.getData();

            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Message: " + remoteMessage);

            String title = (String) bundle.get("title");
            Boolean isBackground = Boolean.valueOf((String)bundle.get("is_background"));
            String flag = (String)bundle.get("flag");
            String data = (String)bundle.get("data");
            Log.d(TAG, "From: " + from);
            Log.d(TAG, "title: " + title);
            Log.d(TAG, "isBackground: " + isBackground);
            Log.d(TAG, "flag: " + flag);
            Log.d(TAG, "data: " + data);

            if (!isBackground) {
                if (flag.equals("1")) {
                    JSONObject mObj = new JSONObject(data);
                    String chatRoomId = mObj.getString("topicId");

                    ChatMessage message = new ChatMessage();
                    message.setId(mObj.getString("id"));
                    message.setMessage(mObj.getString("message"));
                    message.setCreatedAt(mObj.getString("created"));
                    message.setUser(mObj.getString("user"));
                    message.setSelf(mObj.getBoolean("self"));
                    message.setRead(mObj.getBoolean("read"));

                    // skip the message if the message belongs to same user as
                    // the user would be having the same message when he was sending
                    // but it might differs in your scenario
                    if (message.isSelf()) {
                        Log.e(TAG, "Skipping the push message as it belongs to same user");
                        return;
                    }

                    Intent chatNotification = new Intent(Constants.CHAT_NOTIFICATION);
                    chatNotification.putExtra("message", message);
                    chatNotification.putExtra("chat_room_id", chatRoomId);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(chatNotification);

                    App.getInstance().getNotificationUtil().playNotificationSound();

                    if (App.getInstance().getNotificationUtil().isAppIsInBackground()) {
                        // app is in background. show the message in notification try
                        Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
                        resultIntent.putExtra("chat_room_id", chatRoomId);
                        if (firebaseUser == null) {
                            resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                        }
                        showNotificationMessage(title, message.getUser() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                    }
                }
                else if (flag.equals("2")) {
                    Intent readNotification = new Intent(Constants.READ_NOTIFICATION);
                    readNotification.putExtra("message_id", data);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(readNotification);
                }
                else if (flag.equals("3")) {
                    Log.d(TAG, "send typing notif");
                    Intent chatNotification = new Intent(Constants.TYPING_NOTIFICATION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(chatNotification);
                }
            } else {
                String message = (String) bundle.get("message");
                String activity = (String) bundle.get("activity");
                String timestamp = (String) bundle.get("timestamp");
                String image = (String) bundle.get("image");
                Intent resultIntent = new Intent(getApplicationContext(), Class.forName(activity));
                if (firebaseUser == null) {
                    resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                }
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                App.getInstance().getNotificationUtil().showNotificationMessage(title, message, timestamp, resultIntent, image);
            }
        }catch(Exception ex){
            Log.e(TAG, "error exception", ex);
        }
    }

    private void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        App.getInstance().getNotificationUtil().showNotificationMessage(title, message, timeStamp, intent);
    }
}
