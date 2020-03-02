package id.halalvestor.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.adapter.ChatThreadAdapter;
import id.halalvestor.model.ChatMessage;
import id.halalvestor.util.Helper;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.toString();

    private BroadcastReceiver chatBroadcastReceiver;
    private RecyclerView recyclerView;
    private ChatThreadAdapter mAdapter;
    private ArrayList<ChatMessage> messageArrayList;
    private EditText inputMessage;

    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_is_typing)
    TextView tvIsTyping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_layout);

        ButterKnife.bind(this);

        App.getInstance().getPreferenceManager().setNotification("");
        App.getInstance().getPreferenceManager().setUnreadMessage(false);

        inputMessage = findViewById(R.id.message);
        recyclerView = findViewById(R.id.recycler_view);

        String txt = getIntent().getStringExtra("message");
        inputMessage.setText(txt);

        messageArrayList = new ArrayList<>();
        mAdapter = new ChatThreadAdapter(this, messageArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchChatThread();

        chatBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "intent:"+intent);

                if (intent.getAction().equals(Constants.CHAT_NOTIFICATION)) {
                    handlePushNotification(intent);
                } else if (intent.getAction().equals(Constants.READ_NOTIFICATION)) {
                    handleReadNotification(intent);
                } else if (intent.getAction().equals(Constants.TYPING_NOTIFICATION)) {
                    handleTypingNotification(intent);
                }
            }
        };

        tvTitle.setText("BANTUAN"); //Nama Expert
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void sendMessage() {
        Helper.hideKeyboard(this);
        try {
            if (inputMessage.getText() != null && !inputMessage.getText().toString().isEmpty()) {
                sendMessage(inputMessage.getText().toString());
            } else {
                showErrorMessage(inputMessage, getResources().getString(R.string.chat_text_empty));
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
    }

    private void handlePushNotification(Intent intent) {
        ChatMessage message = (ChatMessage) intent.getSerializableExtra("message");

        if (message != null) {
            if (!message.isSelf()){
                tvIsTyping.setVisibility(View.GONE);
            }
            messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
    }

    private void handleReadNotification(Intent intent) {
        String msgId = intent.getStringExtra("message_id");

        for (int i = 0; i < messageArrayList.size(); i++) {
            if (messageArrayList.get(i).getId().equals(msgId)) {
                messageArrayList.get(i).setRead(true);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void handleTypingNotification(Intent intent) {
        tvIsTyping.setVisibility(View.VISIBLE);
    }

    public void onResume(){
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(chatBroadcastReceiver, new IntentFilter(Constants.CHAT_NOTIFICATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(chatBroadcastReceiver, new IntentFilter(Constants.READ_NOTIFICATION));
        LocalBroadcastManager.getInstance(this).registerReceiver(chatBroadcastReceiver, new IntentFilter(Constants.TYPING_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatBroadcastReceiver);
    }

    /**
     * Fetching all the messages of a single chat room
     */
    private void fetchChatThread() {
        showProgressDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.CHAT_THREAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        JSONArray commentsObj = obj.getJSONArray("messages");

                        if (commentsObj.length()>0) {
                            for (int i = 0; i < commentsObj.length(); i++) {
                                JSONObject commentObj = (JSONObject) commentsObj.get(i);

                                String commentId = commentObj.getString("id");
                                String commentText = commentObj.getString("message");
                                String createdAt = commentObj.getString("created");
                                String user = commentObj.getString("user");
                                Boolean self = commentObj.getBoolean("self");
                                Boolean read = commentObj.getBoolean("read");

                                ChatMessage message = new ChatMessage();
                                message.setId(commentId);
                                message.setMessage(commentText);
                                message.setCreatedAt(createdAt);
                                message.setUser(user);
                                message.setSelf(self);
                                message.setRead(read);

                                messageArrayList.add(message);
                            }
                        }else{
                            ChatMessage message = new ChatMessage();
                            message.setId("0");
                            message.setMessage("Hai, "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+". Ada yang bisa kami bantu?");
                            message.setCreatedAt(sdf.format(new Date()));
                            message.setUser("Financial Advisor");
                            message.setSelf(false);
                            message.setRead(false);

                            messageArrayList.add(message);
                        }

                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount() > 1) {
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "response: " + response);
                    Log.e(TAG, "json parsing error: " + e);
                    Toast.makeText(getApplicationContext(), "parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show();
                finish();
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("topicId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                return params;
            }
        };
        //Adding request to request queue
        App.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void handleSentChat(ChatMessage message, boolean sent) {
        if (sent) {
            messageArrayList.add(message);
            inputMessage.setText("");
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                // scrolling to bottom of the recycler view
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        } else {
            inputMessage.setText(message.getMessage());
        }
    }

    @OnClick({R.id.iv_finish, R.id.btn_send, R.id.btn_attach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.btn_send:
                sendMessage();
                break;
            case R.id.btn_attach:
                attach(1);
                break;
        }
    }
}
