package inf.reutlingenuniversity.de.trainbetter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.logging.Logger;

import inf.reutlingenuniversity.de.trainbetter.loginout.LoginFragment;
import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.registration.RegistrationFragment;
import inf.reutlingenuniversity.de.trainbetter.utils.ComponentHelper;
import inf.reutlingenuniversity.de.trainbetter.utils.Status;
import inf.reutlingenuniversity.de.trainbetter.workout.SpacesItemDecoration;
import inf.reutlingenuniversity.de.trainbetter.workout.WorkoutsOverviewAdapter;

/**
 * Created by EL on 05.12.2016.
 */
public class MainActivity extends AppCompatActivity {

    private View contentWrapper;
    private Toolbar toolbar;

    private List<Workout> workouts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        contentWrapper = findViewById(R.id.content_wrapper);

        Resources resources = getResources();
        int from = getIntent().getExtras().getInt(StartActivity.FROM);
        if (from == LoginFragment.STATUS_CODE) {
            ComponentHelper.createSnackbar(this, contentWrapper, String.format(resources.getString(R.string.success_loggedin_signedup), resources.getString(R.string.success_loggedin)), Status.SUCCESS).show();
        } else if (from == RegistrationFragment.STATUS_CODE) {
            ComponentHelper.createSnackbar(this, contentWrapper, String.format(resources.getString(R.string.success_loggedin_signedup), resources.getString(R.string.success_signedup)), Status.SUCCESS).show();
        }

        initToolbar();

        ParseQuery<Workout> gameQuery = ParseQuery.getQuery(Workout.class);
        gameQuery.whereEqualTo("userPointer", ParseUser.getCurrentUser());
        gameQuery.findInBackground(new FindCallback<Workout>() {
            @Override
            public void done(List<Workout> resultSet, ParseException e) {
                if (e == null) {
                    workouts = resultSet;
                    setUpRecyclerView();
                }
            }
        });

    }

    private void setUpRecyclerView() {
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        WorkoutsOverviewAdapter woAdapter = new WorkoutsOverviewAdapter(this, workouts);


        RecyclerView.ItemDecoration spacesItemDecoration = new SpacesItemDecoration(16);
        rvContacts.addItemDecoration(spacesItemDecoration);

        rvContacts.setAdapter(woAdapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {
            ParseUser.logOutInBackground();
            logOut();
            return true;
        } else if (itemId == R.id.action_settings) {
            //Settings
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        Intent logOutIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(logOutIntent);
    }
}
