package inf.reutlingenuniversity.de.trainbetter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.loginout.LoginFragment;
import inf.reutlingenuniversity.de.trainbetter.model.Exercise;
import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.model.WorkoutExercise;
import inf.reutlingenuniversity.de.trainbetter.registration.RegistrationFragment;
import inf.reutlingenuniversity.de.trainbetter.utils.ComponentHelper;
import inf.reutlingenuniversity.de.trainbetter.utils.Status;
import inf.reutlingenuniversity.de.trainbetter.workout.SpacesItemDecoration;
import inf.reutlingenuniversity.de.trainbetter.workout.WorkoutsOverviewAdapter;

/**
 * Created by EL on 05.12.2016.
 */
public class WorkoutOverviewActivity extends LoggedInActivity {

    private View contentWrapper;
    private Toolbar toolbar;

    private List<Workout> workouts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_overview);

        contentWrapper = findViewById(R.id.content_wrapper);

        Resources resources = getResources();
        int from = getIntent().getExtras().getInt(StartActivity.FROM);
        if (from == LoginFragment.STATUS_CODE) {
            ComponentHelper.createSnackbar(this, contentWrapper, String.format(resources.getString(R.string.success_loggedin_signedup), resources.getString(R.string.success_loggedin)), Status.SUCCESS).show();
        } else if (from == RegistrationFragment.STATUS_CODE) {
            ComponentHelper.createSnackbar(this, contentWrapper, String.format(resources.getString(R.string.success_loggedin_signedup), resources.getString(R.string.success_signedup)), Status.SUCCESS).show();
        }

        initToolbar();

        ParseQuery<Workout> workoutQuery = ParseQuery.getQuery(Workout.class);
        workoutQuery.whereEqualTo(Workout.USER_POINTER, ParseUser.getCurrentUser());
        workoutQuery.findInBackground(new FindCallback<Workout>() {
            @Override
            public void done(List<Workout> resultSet, ParseException e) {
                if (e == null) {
                    workouts = resultSet;
                    //ParseObject.pinAllInBackground(workouts);
                    workouts.get(0).findWorkoutExercises(new FindCallback<WorkoutExercise>() {
                        @Override
                        public void done(List<WorkoutExercise> objects, ParseException e) {
                            for (WorkoutExercise object : objects) {
                                Log.i("parse", object.getOrder() + "");
                                object.getExercise().fetchIfNeededInBackground(new GetCallback<Exercise>() {
                                    @Override
                                    public void done(Exercise object, ParseException e) {
                                        Log.i("parse", object.getName());
                                    }
                                });
                            }
                        }
                    });
                    setUpRecyclerView();
                }
            }
        });
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycer_view);

        WorkoutsOverviewAdapter woAdapter = new WorkoutsOverviewAdapter(this, workouts, new OnParseObjectClickListener() {
            @Override
            public void onParseObjectClick(ParseObject workout) {
                startWorkoutDetailsActivity((Workout) workout);
            }
        });

        RecyclerView.ItemDecoration spacesItemDecoration = new SpacesItemDecoration(16);
        recyclerView.addItemDecoration(spacesItemDecoration);

        recyclerView.setAdapter(woAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    private void startWorkoutDetailsActivity(Workout workout) {
        Intent workoutDetailIntent = new Intent(getApplicationContext(), WorkoutDetailsActivity.class);
        workoutDetailIntent.putExtra("id", workout.getObjectId());
        startActivity(workoutDetailIntent);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
