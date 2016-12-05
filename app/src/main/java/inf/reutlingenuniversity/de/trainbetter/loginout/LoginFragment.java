package inf.reutlingenuniversity.de.trainbetter.loginout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import inf.reutlingenuniversity.de.trainbetter.R;
import inf.reutlingenuniversity.de.trainbetter.StartActivity;
import inf.reutlingenuniversity.de.trainbetter.utils.ComponentHelper;
import inf.reutlingenuniversity.de.trainbetter.utils.Status;
import inf.reutlingenuniversity.de.trainbetter.utils.Validation;

/**
 * @author Eugen Ljavin
 */

public class LoginFragment extends Fragment {

    public static final int STATUS_CODE = 11;

    private UserLoginTask loginTask = null;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private View progressView;

    public  LoginFragment() {
        //Default Constructor Needed
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        if (ParseUser.getCurrentUser() != null) {
            ((StartActivity) getActivity()).startMainActivity(LoginFragment.STATUS_CODE);
        }

        usernameEditText = (EditText) rootView.findViewById(R.id.username);

        passwordEditText = (EditText) rootView.findViewById(R.id.password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) rootView.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        progressView = rootView.findViewById(R.id.registration_progress);

        return rootView;
    }

    private void attemptLogin() {
        if (loginTask != null) {
            return;
        }

        usernameEditText.setError(null);
        passwordEditText.setError(null);

        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !Validation.isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            usernameEditText.setError(getString(R.string.error_field_required));
            focusView = usernameEditText;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            usernameEditText.setError(getString(R.string.error_invalid_email));
            focusView = usernameEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            loginTask = new UserLoginTask(email, password);
            loginTask.execute((Void) null);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ParseUser.logInInBackground(mUsername, mPassword, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        ((StartActivity) getActivity()).startMainActivity(LoginFragment.STATUS_CODE);
                    } else {
                        Log.i("PARSE", "USER NOT EXISTS");
                        ComponentHelper.createSnackbar(getActivity(), ((StartActivity) getActivity()).getContentWrapper(), R.string.error_user_not_exists, inf.reutlingenuniversity.de.trainbetter.utils.Status.ERROR).show();
                    }
                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            loginTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            loginTask = null;
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


}
