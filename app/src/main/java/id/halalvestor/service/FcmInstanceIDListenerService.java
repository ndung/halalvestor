package id.halalvestor.service;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.util.Helper;
import id.halalvestor.util.HttpClientRequest;

public class FcmInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = FcmInstanceIDListenerService.class.toString();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.

        //Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        //Intent intent = new Intent(this, RegistrationIntentService.class);
        //startService(intent);
        try {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            final Map<String, String> params = new HashMap<String, String>();

            params.put("uid", firebaseUser.getUid());

            params.put("email", firebaseUser.getEmail());
            params.put("name", firebaseUser.getDisplayName());
            params.put("photo_url", firebaseUser.getPhotoUrl().toString());
            params.put("fcm_token", refreshedToken);
            params.put("providers", firebaseUser.getProviders().toString());

            StringRequest strReq = new StringRequest(Request.Method.POST, Constants.API_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "response: " + response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Error: " + error.getMessage() + ", code: " + networkResponse);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return Helper.translateParameters(params, false);
                }

                @Override
                public Map<String, String> getHeaders() {
                    return HttpClientRequest.getHeaders("update_user");
                }
            };

            // disabling retry policy so that it won't make
            // multiple http calls
            int socketTimeout = 0;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            strReq.setRetryPolicy(policy);

            //Adding request to request queue
            App.getInstance().addToRequestQueue(strReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
