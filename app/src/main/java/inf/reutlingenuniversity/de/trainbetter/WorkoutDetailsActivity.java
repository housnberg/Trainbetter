package inf.reutlingenuniversity.de.trainbetter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.model.WorkoutExercise;
import inf.reutlingenuniversity.de.trainbetter.utils.ComponentHelper;
import inf.reutlingenuniversity.de.trainbetter.workout.WorkoutDetailAdapter;

/**
 * Created by EL on 22.12.2016.
 */

public class WorkoutDetailsActivity extends LoggedInActivity {

    public static final int REQUEST_CODE = 12;

    private Toolbar toolbar;
    private ImageView titleImageView;
    private TextView workoutDescTextView;
    private TextView roundsTextView;
    private TextView pauseBetweenExercisesTextView;
    private TextView pauseBetweenRoundsTextView;
    private TextView pauseBetweenSetsTextView;
    private RelativeLayout pauseBetweenSetsWrapper;

    private FloatingActionButton floatingActionButton;

    private Workout workout;
    private List<WorkoutExercise> workoutExercises;
    private static String secondsShort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        secondsShort = getResources().getString(R.string.seconds_short);

        setContentView(R.layout.activity_workout_details);
        titleImageView = (ImageView) findViewById(R.id.title_image);
        workoutDescTextView = (TextView) findViewById(R.id.workout_description);
        roundsTextView = (TextView) findViewById(R.id.workout_rounds);
        pauseBetweenExercisesTextView = (TextView) findViewById(R.id.workout_pause_between_exercises);
        pauseBetweenRoundsTextView = (TextView) findViewById(R.id.workout_pause_between_rounds);
        pauseBetweenSetsTextView = (TextView) findViewById(R.id.workout_pause_between_sets);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        pauseBetweenSetsWrapper = (RelativeLayout) findViewById(R.id.workout_pause_between_sets_wrapper);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            workout = ParseObject.createWithoutData(Workout.class, extras.getString("id"));
            try {
                workout.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        roundsTextView.setText(String.valueOf(workout.getRounds()));
        pauseBetweenExercisesTextView.setText(workout.getPauseBetweenExercises() + " " + secondsShort);
        pauseBetweenRoundsTextView.setText(workout.getPauseBetweenRounds() + " " + secondsShort);
        pauseBetweenSetsTextView.setText(workout.getPauseBetweenSets() + " " + secondsShort);
        workoutDescTextView.setText(workout.getDescription());

        workout.findWorkoutExercises(new FindCallback<WorkoutExercise>() {

            @Override
            public void done(List<WorkoutExercise> resultSet, ParseException e) {
                workoutExercises = resultSet;
                if (!workoutExercises.isEmpty()) {
                    workoutExercises.get(0).getWorkoutExercisesWithSetsGreaterThan(new FindCallback<WorkoutExercise>() {

                        @Override
                        public void done(List<WorkoutExercise> objects, ParseException e) {
                            if (objects.isEmpty()) {
                                pauseBetweenSetsWrapper.setVisibility(View.GONE);
                            }
                        }

                    }, 1);
                    setupRecyclerView();
                } else {
                    //TODO Snackbar no Exercises assigned
                }
            }
        });

        initToolbar();
        setTitleImage();

    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycer_view);

        WorkoutDetailAdapter wdAdapter = new WorkoutDetailAdapter(this, workoutExercises, new OnParseObjectClickListener() {
            @Override
            public void onParseObjectClick(ParseObject workout) {
                //startWorkoutDetailsActivity((Workout) workout);
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(wdAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setTitleImage() {
        ParseFile titleImage = workout.getTitleImage();

        if (titleImage != null) {
            titleImage.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap titleImageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        titleImageView.setImageBitmap(titleImageBitmap);
                    }
                }

            });
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(workout.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void startWorkout(View view) {
        Intent countdownIntent = new Intent(this, CountdownActivity.class);
        countdownIntent.putExtra("requestCode", REQUEST_CODE);
        countdownIntent.putExtra("id", workout.getObjectId());
        startActivity(countdownIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        Intent workoutRunIntent = new Intent(this, WorkoutRunActivity.class);
        workoutRunIntent.putExtra("id", workout.getObjectId());
        startActivity(workoutRunIntent);
    }
}
