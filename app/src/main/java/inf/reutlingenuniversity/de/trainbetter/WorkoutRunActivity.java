package inf.reutlingenuniversity.de.trainbetter;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.model.Exercise;
import inf.reutlingenuniversity.de.trainbetter.model.Image;
import inf.reutlingenuniversity.de.trainbetter.model.WorkoutExercise;
import inf.reutlingenuniversity.de.trainbetter.services.CountdownService;
import inf.reutlingenuniversity.de.trainbetter.utils.ComponentHelper;
import inf.reutlingenuniversity.de.trainbetter.utils.TextToSpeechController;
import inf.reutlingenuniversity.de.trainbetter.workout.ExerciseImageAdapter;

/**
 * Created by EL on 08.01.2017.
 */

public class WorkoutRunActivity extends LoggedInActivity implements View.OnClickListener {

    private TextToSpeechController textToSpeechController;
    private BroadcastReceiver tickReceiver;
    private BroadcastReceiver finishReceiver;
    private CountdownService countdownService;
    private boolean isBound;

    private Toolbar toolbar;
    private TextView timeTextView;
    private TextView exerciseRepetitionsTextView;
    private ViewPager exerciseImagesViewPager;
    private PagerAdapter exerciseImagesPageAdapter;
    private MenuItem navigateBefore;
    private MenuItem navigateNext;
    private WorkoutExercise workoutExercise;
    private Exercise exercise;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_run);

        exerciseRepetitionsTextView = (TextView) findViewById(R.id.exercise_repetitions);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            workoutExercise = ParseObject.createWithoutData(WorkoutExercise.class, extras.getString("id"));
            try {
                workoutExercise.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            exercise = workoutExercise.getExercise();
            try {
                exercise.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        initToolbar(exercise.getName());

        String exerciseRepetitionsText = workoutExercise.getRepetitions() + " ";
        exerciseRepetitionsText += workoutExercise.isRepeatable() ? this.getResources().getString(R.string.repetitions_short) : this.getResources().getString(R.string.seconds_short);
        exerciseRepetitionsTextView.setText(exerciseRepetitionsText);

        exercise.findCorrespondingImages(new FindCallback<Image>() {
            @Override
            public void done(List<Image> resultSet, ParseException e) {
                setupPager(resultSet);
            }
        });

        if (getCallingActivity() == null) {
            //This Activity was called by startActivity
            Log.i("CALLED FROM", "This Activity was called by startActivity");
        } else {
            Log.i("CALLED FROM", "This Activity was called by startActivityForResult");
        }
        /*
        timeTextView = (TextView) findViewById(R.id.time_text_view);

        textToSpeechController = new TextToSpeechController(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            workout = ParseObject.createWithoutData(Workout.class, extras.getString("id"));
            try {
                workout.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        workout.findWorkoutExercises(new FindCallback<WorkoutExercise>() {

            @Override
            public void done(List<WorkoutExercise> resultSet, ParseException e) {
                workoutExercises = resultSet;
                if (!workoutExercises.isEmpty()) {
                    workoutExercises.get(0).getWorkoutExercisesWithSetsGreaterThan(new FindCallback<WorkoutExercise>() {

                        @Override
                        public void done(List<WorkoutExercise> objects, ParseException e) {
                            if (objects.isEmpty()) {
                                //pauseBetweenSetsWrapper.setVisibility(View.GONE);
                            }
                        }

                    }, 1);

                } else {
                    //TODO Snackbar no Exercises assigned
                }
            }
        });

        tickReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String text = timeTextView.getText().toString();
                int currTime = reps / 1000;
                if(!text.equals("")){
                    currTime = Integer.valueOf(text);
                }
                currTime--;
                //TODO KONFIGURIERBAR MACHEN
                if(!exercisesRepeatable.get(exercises.get(currExercise).getId())) {
                    if ((currTime % 10 == 0 && currTime > 0) || (currTime <= 10 && currTime > 0)) {
                        ttsc.speak(String.valueOf(currTime));
                    } else if (currTime == 0) {
                        changeButton(true);
                        ttsc.speak("Stop");
                    }
                }

                timeTextView.setText(Integer.toString(currTime));
                repsSecTextView.setText(R.string.unitSeconds);
            }
        };

        finishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //timeTextView.setText("0");
            }
        };
        */
    }

    private void initToolbar(String exerciseName) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(exerciseName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setupPager(List<Image> images) {
        exerciseImagesPageAdapter = new ExerciseImageAdapter(getSupportFragmentManager(), images);
        exerciseImagesViewPager = (ViewPager) findViewById(R.id.pager);
        exerciseImagesViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    ComponentHelper.setMenuIconEnabled(navigateBefore, false);
                    ComponentHelper.setMenuIconEnabled(navigateNext, true);
                } else if (position == exerciseImagesPageAdapter.getCount() - 1) {
                    ComponentHelper.setMenuIconEnabled(navigateNext, false);
                    ComponentHelper.setMenuIconEnabled(navigateBefore, true);
                } else {
                    ComponentHelper.setMenuIconEnabled(navigateBefore, true);
                    ComponentHelper.setMenuIconEnabled(navigateNext, true);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //Do nothing because we don't need to override this method
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Do nothing because we don't need to override this method
            }
        });
        exerciseImagesViewPager.setAdapter(exerciseImagesPageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_navigate_images, menu);
        navigateBefore = menu.findItem(R.id.action_navigate_before);
        navigateNext = menu.findItem(R.id.action_navigate_next);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        int currentItem = exerciseImagesViewPager.getCurrentItem();
        if (itemId == R.id.action_navigate_before) {
            ComponentHelper.setMenuIconEnabled(navigateNext, true);
            exerciseImagesViewPager.setCurrentItem(currentItem - 1);
            return true;
        } else if (itemId == R.id.action_navigate_next) {
            ComponentHelper.setMenuIconEnabled(navigateBefore, true);
            exerciseImagesViewPager.setCurrentItem(currentItem + 1);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (exerciseImagesViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            exerciseImagesViewPager.setCurrentItem(exerciseImagesViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    public void click(View view) {
        finish();
    }
}
