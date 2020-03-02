package id.halalvestor.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.halalvestor.App;
import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.activity.credential.LoginActivity;


public class BaseFragment extends Fragment {

    public void onCreate(Bundle b){
        super.onCreate(b);
        /**FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        long lastActive = App.getInstance().getPreferenceManager().getLastActive(firebaseUser.getUid());

        if ((System.currentTimeMillis()-lastActive)> Constants.ACTIVE_TIME_MS){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            getActivity().finish();
        }else {
            App.getInstance().getPreferenceManager().updateLastActive(firebaseUser.getUid(), 1);
        }*/
    }
}
