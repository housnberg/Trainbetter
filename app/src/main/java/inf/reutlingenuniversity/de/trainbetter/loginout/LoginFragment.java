package inf.reutlingenuniversity.de.trainbetter.loginout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import inf.reutlingenuniversity.de.trainbetter.R;

/**
 * Created by EL on 04.12.2016.
 */

public class LoginFragment extends Fragment {

    public  LoginFragment() {
        //
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_login, container, false);
        return drawer;
    }

}
