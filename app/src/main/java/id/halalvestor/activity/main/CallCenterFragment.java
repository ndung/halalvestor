package id.halalvestor.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.halalvestor.R;
import id.halalvestor.activity.ChatActivity;

public class CallCenterFragment extends BaseFragment {

    @BindView(R.id.tv_hai)
    TextView tvHai;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_center_fragment, container, false);
        ButterKnife.bind(this, view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvHai.setText("Hai, "+user.getDisplayName()+" !");
        return view;
    }

    @OnClick({R.id.iv_chat, R.id.tv_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_chat:
            case R.id.tv_chat:
                startActivity(new Intent(getActivity(), ChatActivity.class));
                break;
        }
    }
}
