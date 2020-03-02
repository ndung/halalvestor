package id.halalvestor.activity.credential;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import id.halalvestor.App;
import id.halalvestor.R;
import id.halalvestor.activity.MainActivity;
import id.halalvestor.activity.BaseActivity;
import id.halalvestor.activity.WelcomeActivity;
import id.halalvestor.model.LoginWrapper;

public abstract class AuthenticationActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = AuthenticationActivity.class.toString();

    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    protected AuthCredential authCredential;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        // Other app specific specialization
        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                onAuthenticationFailed(exception.getLocalizedMessage());
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_WITH_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private static final int RC_SIGN_IN_WITH_GOOGLE = 9001;
    private static final int RC_SIGN_IN_WITH_FB = 64206;

    protected void authenticationWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    protected void authenticationWithGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_WITH_GOOGLE);
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        }else{
            if (result.getStatus().getStatusMessage()!=null) {
                onAuthenticationFailed(result.getStatus().getStatusMessage());
            }else{
                onAuthenticationFailed(result.getStatus().toString());
            }
        }
    }

    protected void revokeAccess() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "revokeAccess Google status : "+status.getStatusMessage());
                    }
                });
    }

    protected void signIn(String email, String password) {

        showProgressDialog();

        authCredential = EmailAuthProvider.getCredential(email, password);
        // [START sign_in_with_email]
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                authenticationSuccess();
                            }else{
                                onEmailNotVerified(getResources().getString(R.string.authentication_email_not_verified));
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            onAuthenticationFailed(task.getException().getLocalizedMessage());
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    public void onEmailNotVerified(String errorMessage) {};

    protected void signOut() {
        FirebaseAuth.getInstance().signOut();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        //facebook sign out
        LoginManager.getInstance().logOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.d(TAG, "googleSignOut result status : "+status.getStatusMessage());
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void authenticationSuccess(){
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseMessaging.getInstance().subscribeToTopic("global");
        String lastLoginUser = App.getInstance().getPreferenceManager().getLastLoginUser();
        if (!lastLoginUser.equalsIgnoreCase("") &&
                !lastLoginUser.equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(lastLoginUser);
        }
        App.getInstance().getPreferenceManager().setLastLoginUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        authUser();
    }

    protected void onAuthenticationSuccess(LoginWrapper param){
        App.getInstance().getPreferenceManager().updateLastActive(FirebaseAuth.getInstance().getCurrentUser().getUid(), 1);
        Intent intent = new Intent(this, MainActivity.class);
        if (param.isNewUser() || param.getSurveyAnswers()==null || param.getSurveyAnswers().isEmpty()) {
            intent = new Intent(this, WelcomeActivity.class);
            if (!FirebaseAuth.getInstance().getCurrentUser().getProviders().toString().contains("password") && authCredential != null){
                intent = new Intent(this, SetPasswordActivity.class);
                intent.putExtra("credential", authCredential);
                intent.putExtra("data", param);
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        this.finish();
    }

    protected void onAuthenticationFailed(String errorMessage){
        showErrorMessage(findViewById(android.R.id.content), errorMessage);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog();

        authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            authenticationSuccess();
                        } else {
                            onAuthenticationFailed(task.getException().getLocalizedMessage());
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog();

        authCredential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            authenticationSuccess();
                        } else {
                            onAuthenticationFailed(task.getException().getLocalizedMessage());
                        }
                        hideProgressDialog();
                    }
                });
    }

    protected void authUser() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        String name = "";
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
            name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        params.put("name", name);
        String photoUrl = "";
        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
            photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        }
        params.put("photo_url", photoUrl);
        params.put("fcm_token", FirebaseInstanceId.getInstance().getToken());
        params.put("providers", FirebaseAuth.getInstance().getCurrentUser().getProviders().toString());

        connectServerApi("update_user", params);
    }

    @Override
    public void onSuccessfullApiConnect(String requestHeader, String result) {
        super.onSuccessfullApiConnect(requestHeader, result);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        LoginWrapper wrapper = gson.fromJson(result, LoginWrapper.class);
        if (wrapper.getSurveyAnswers()!=null) {
            App.getInstance().getPreferenceManager().setSurveyAnswers(wrapper.getSurveyAnswers());
        }
        if (wrapper.getAppUser()!=null){
            App.getInstance().getPreferenceManager().setAppUser(wrapper.getAppUser());
        }
        onAuthenticationSuccess(wrapper);
    }

    @Override
    public void onFailedApiConnect(String requestHeader, String error) {
        super.onFailedApiConnect(requestHeader, error);

        onAuthenticationFailed(error);
    }
}
