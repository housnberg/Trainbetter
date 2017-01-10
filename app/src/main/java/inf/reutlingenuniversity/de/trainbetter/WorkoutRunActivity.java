package inf.reutlingenuniversity.de.trainbetter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.model.Exercise;
import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.model.WorkoutExercise;
import inf.reutlingenuniversity.de.trainbetter.services.CountdownService;
import inf.reutlingenuniversity.de.trainbetter.utils.TextToSpeechController;

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

    private WorkoutExercise workoutExercise;
    private Exercise exercise;

    /*
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownService.LocalBinder binder = (CountdownService.LocalBinder) service;
            countdownService = binder.getService();
            countdownService.countdownTimer((reps + 1) * 1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            countdownService = null;
        }

    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_run);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        toolbar.setTitle(exercise.getName());

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

    /*
    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver((tickReceiver),
                new IntentFilter(CountdownService.CTS_TICK)
        );
        LocalBroadcastManager.getInstance(this).registerReceiver((finishReceiver),
                new IntentFilter(CountdownService.CTS_FINISH)
        );

        long key = exercises.get(currExercise).getId();
        if(!exercisesRepeatable.get(key)){
            Intent intent = new Intent(this, CountdownTimerService.class);
            if(!isBound)
                isBound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(tickReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver);
        textToSpeechController.shutDown();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){


        if(currRound < workout.getRounds()){
            if(currExercise < (exercises.size()-1)){
                currExercise++;
            }else{
                currRound++;
                currExercise = 0;
            }

            setImage();
            setReps();

            long key = exercises.get(currExercise).getId();
            if(!exercisesRepeatable.get(key)){
                changeButton(false);
                button.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                button.setBackground(getResources().getDrawable(android.R.color.transparent));
                countdownTimerService.countdownTimer((reps + 1) * 1000);
            }
        } else {
            time = System.currentTimeMillis() - time;
            Intent intentFinish = new Intent(this, FinishActivity.class);
            intentFinish.putExtra("workoutTime", time);
            intentFinish.putExtra("data", workout);
            startActivity(intentFinish);
        }
        exerciseName.setText(exercises.get(currExercise).getName());


    }
    */

    @Override
    public void onClick(View view) {
        finish();
    }

    public void click(View view) {
        finish();
    }
}
