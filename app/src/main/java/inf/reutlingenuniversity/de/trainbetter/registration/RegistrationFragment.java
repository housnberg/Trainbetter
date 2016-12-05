package inf.reutlingenuniversity.de.trainbetter.registration;

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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import inf.reutlingenuniversity.de.trainbetter.R;
import inf.reutlingenuniversity.de.trainbetter.StartActivity;
import inf.reutlingenuniversity.de.trainbetter.utils.ComponentHelper;
import inf.reutlingenuniversity.de.trainbetter.utils.Validation;

/**
 * @author Andreas Mueller
 */
public class RegistrationFragment extends Fragment {

    public static final int STATUS_CODE = 10;

    private UserRegistrationTask registrationTask = null;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;

    private View progressView;

    public RegistrationFragment() {
        //Default Constructor Needed
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        usernameEditText = (EditText) rootView.findViewById(R.id.username);
        emailEditText = (EditText) rootView.findViewById(R.id.email);

        passwordEditText = (EditText) rootView.findViewById(R.id.password);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) rootView.findViewById(R.id.button_register);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        progressView = rootView.findViewById(R.id.registration_progress);

        return rootView;
    }

    private void attemptRegistration() {
        if (registrationTask != null) {
            return;
        }

        usernameEditText.setError(null);
        passwordEditText.setError(null);
        emailEditText.setError(null);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !Validation.isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError(getString(R.string.error_field_required));
            focusView = usernameEditText;
            cancel = true;
        } else if (!Validation.isEmailValid(username)) {
            usernameEditText.setError(getString(R.string.error_invalid_email));
            focusView = usernameEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            registrationTask = new UserRegistrationTask(username, password);
            registrationTask.execute((Void) null);
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

    private class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;

        UserRegistrationTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        ((StartActivity) getActivity()).startMainActivity(RegistrationFragment.STATUS_CODE);
                    } else {
                        Log.i("PARSE", "USER ALREADY EXISTS");
                        ComponentHelper.createSnackbar(getActivity(), ((StartActivity) getActivity()).getContentWrapper(), R.string.error_user_exists, inf.reutlingenuniversity.de.trainbetter.utils.Status.ERROR).show();
                    }
                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            registrationTask = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            registrationTask = null;
            showProgress(false);
        }
    }


}
