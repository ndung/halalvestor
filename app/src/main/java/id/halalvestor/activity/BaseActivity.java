package id.halalvestor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.credential.LoginActivity;
import id.halalvestor.model.ChatMessage;
import id.halalvestor.ui.ImagePicker;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;
import id.halalvestor.util.ImageCompressor;
import id.halalvestor.util.SingleUploadBroadcastReceiver;

public abstract class BaseActivity extends AppCompatActivity implements SingleUploadBroadcastReceiver.Delegate {

    private static final String TAG = BaseActivity.class.toString();

    //protected FirebaseUser firebaseUser;
    //protected FirebaseAuth firebaseAuth;

    protected ImageCompressor imageCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.imageCompressor = new ImageCompressor(this);

        /**if (!(this instanceof SplashActivity) && FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (this instanceof Unauthorizable) {
                App.getInstance().getPreferenceManager().updateLastActive(FirebaseAuth.getInstance().getCurrentUser().getUid(), 0);
            } else {
                long lastActive = App.getInstance().getPreferenceManager().getLastActive(FirebaseAuth.getInstance().getCurrentUser().getUid());

                Log.d(TAG, "lastActive firebaseUser:" + lastActive);

                Log.d(TAG, "active firebaseUser:" + (System.currentTimeMillis() - lastActive));

                if ((System.currentTimeMillis() - lastActive) > Constants.ACTIVE_TIME_MS) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    finish();
                } else {
                    App.getInstance().getPreferenceManager().updateLastActive(FirebaseAuth.getInstance().getCurrentUser().getUid(), 1);
                }
            }
        }*/
    }

    /**
     * Making notification bar transparent
     */
    protected void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void showNotificationMessage(View view, String notificationMessage) {
        Snackbar snackbar = Snackbar.make(view, notificationMessage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showErrorMessage(View view, String errorMessage) {
        Snackbar snackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public static final int PICK_IMAGE_REQUEST = 1;

    public void showFileChooser(int request) {
        Log.d(TAG, "WRITE_EXTERNAL_STORAGE:"+ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
        Log.d(TAG, "READ_EXTERNAL_STORAGE:"+ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE));
        Log.d(TAG, "bool:"+(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED));
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    },
                    PICK_IMAGE_REQUEST);
        } else {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, Integer.parseInt("999" + request));
        }
    }

    private Uri filePath;

    protected String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    protected final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    private SingleUploadBroadcastReceiver.Delegate delegate;

    public void setDelegate(SingleUploadBroadcastReceiver.Delegate delegate) {
        this.delegate = delegate;
    }

    public void uploadMultipart(int requestCode) {
        try {
            String path = absolutePath;//imageCompressor.compressImageFromFilePath(getPath(filePath));
            String uploadId = UUID.randomUUID().toString();
            if (delegate == null) {
                uploadReceiver.setDelegate(this);
            } else {
                uploadReceiver.setDelegate(delegate);
            }
            uploadReceiver.setRequestCode(requestCode);
            uploadReceiver.setUploadID(uploadId);
            new MultipartUploadRequest(this, uploadId, Constants.FILE_UPLOAD_URL)
                    // starting from 3.1+, you can also use content:// URI string instead of absolute file
                    .addParameter("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()) //Adding text parameter to the request
                    .addFileToUpload(path, "chatImage")
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        } catch (Exception exc) {
            Log.e(TAG, "exception:", exc);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode > 9990 && requestCode < 9999) {// && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Object[] obj = ImagePicker.getImageFromResult(this, resultCode, data);
            filePath = (Uri) obj[0];
            absolutePath = (String) obj[2];
            Log.d(TAG, "filePath:" + filePath);
            uploadMultipart(requestCode);
        }
    }

    String absolutePath;

    private static final int MY_PERMISSION = 1;

    protected void attach(int requestCode) {

        showFileChooser(requestCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showFileChooser(requestCode);
            } else {
                Toast.makeText(this, getResources().getString(R.string.storage_permission_not_granted), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */
    protected void sendMessage(final String txt) throws AuthFailureError {

        final ChatMessage message = new ChatMessage();
        message.setMessage(txt);

        showProgressDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Constants.CHAT_SENDER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {

                        JSONObject commentObj = obj.getJSONObject("message");

                        String commentId = commentObj.getString("id");
                        String commentText = commentObj.getString("message");
                        String createdAt = commentObj.getString("created");
                        String user = commentObj.getString("user");
                        Boolean self = commentObj.getBoolean("self");
                        Boolean read = commentObj.getBoolean("read");

                        message.setId(commentId);
                        message.setMessage(commentText);
                        message.setCreatedAt(createdAt);
                        message.setUser(user);
                        message.setSelf(self);
                        message.setRead(read);

                        handleSentChat(message, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                        handleSentChat(message, false);
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
                Log.e(TAG, "Error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG).show();
                handleSentChat(message, false);
                hideProgressDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                params.put("message", txt);
                params.put("topicId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                params.put("self", "1");
                params.put("type", "chat");
                return params;
            }
        };

        // disabling retry policy so that it won't make
        // multiple http calls
        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);


        Log.d("url", strReq.getUrl() + "/" + new String(strReq.getBody()));

        //Adding request to request queue
        App.getInstance().addToRequestQueue(strReq);
    }

    public void handleSentChat(ChatMessage message, boolean sent) {

        if (!(this instanceof ChatActivity)) {
            Intent intent = new Intent();
            if (!sent) {
                intent.putExtra("message", message.getMessage());
            }
            intent.setClass(this, ChatActivity.class);
            startActivity(intent);
        }
    }

    public void connectServerApi(final String requestHeader, final Map<String, String> params) {
        if (HttpClientRequest.isNetworkAvailable(this)) {
            new AsyncTask<String, Integer, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    showProgressDialog();
                }

                @Override
                protected void onProgressUpdate(Integer... progress) {
                    mProgressDialog.setProgress(progress[0]);
                }

                @Override
                protected void onPostExecute(String result) {
                    hideProgressDialog();
                    try {
                        if (result != null && !result.isEmpty()) {
                            onSuccessfullApiConnect(requestHeader, result);
                        } else {
                            onFailedApiConnect(requestHeader, getResources().getString(R.string.system_malfunction));
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "ex", ex);
                    }
                }

                @Override
                protected String doInBackground(String... strings) {
                    String url = strings[0];
                    if (params != null && params.size() > 0) {
                        url = url + "?" + Helper.translateParamsToQuery(params);
                    }
                    Log.d("debug", "fetch: " + url);
                    String response = HttpClientRequest.execute(url, strings[1]); // getting XML

                    return response;
                }
            }.execute(Constants.API_URL, requestHeader);
        } else {
            onFailedApiConnect(requestHeader, getResources().getString(R.string.internet_connection_error));
        }
    }

    public void onSuccessfullApiConnect(String requestHeader, String result) {

    }

    public void onFailedApiConnect(String requestHeader, String error) {

    }

    @Override
    public void onProgress(int progress) {
        showProgressDialog();
    }

    @Override
    public void onError(Exception exception) {
        hideProgressDialog();
    }

    @Override
    public void onCompleted(int serverResponseCode, int requestCode, String serverResponseBody) {
        hideProgressDialog();
        if (serverResponseCode == 200) {
            try {
                sendMessage(serverResponseBody);
            } catch (Exception ex) {
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Error occurred! Http Status Code: " + serverResponseCode)
                    .show();
        }
    }

    @Override
    public void onCancelled() {
        hideProgressDialog();
    }
}
