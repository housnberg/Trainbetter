package inf.reutlingenuniversity.de.trainbetter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.model.Exercise;
import inf.reutlingenuniversity.de.trainbetter.model.Image;
import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.model.WorkoutExercise;
import inf.reutlingenuniversity.de.trainbetter.services.CountdownService;
import inf.reutlingenuniversity.de.trainbetter.utils.TextToSpeechController;

/**
 * Created by EL on 06.01.2017.
 */

public class CountdownActivity extends LoggedInActivity {

    public static final int REQUEST_CODE = 55;
    private static final String EMPTY_TEXT = "";

    private BroadcastReceiver tickReceiver;
    private BroadcastReceiver finishReceiver;
    private CountdownService countdownService;
    private TextToSpeechController textToSpeechController;

    private Toolbar toolbar;
    private TextView currentRoundTextView;
    private TextView countdownTextView;
    private TextView nextExerciseNameTextView;
    private TextView nextExerciseRepsTextView;
    private TextView nextExerciseSetsTextView;
    private TextView nextExerciseDescriptionTextView;
    private TextView pauseTextView;
    private CircularImageView nextExerciseTitleImage;
    private Resources resources;

    private List<WorkoutExercise> workoutExercises;
    private Exercise nextExercise;
    private Workout workout;

    private boolean isBound = false;
    private int timeInSeconds = 11;
    private int currentRound = 0;
    private int currentExercise = 0;
    private int currentSet = 0;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountdownService.LocalBinder binder = (CountdownService.LocalBinder) service;
            countdownService = binder.getService();
            countdownService.countdownTimer(timeInSeconds * CountdownService.SECONDS_TO_MILLISECONDS_FACTOR);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            countdownService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        textToSpeechController = new TextToSpeechController(getApplicationContext());

        currentRoundTextView = (TextView) findViewById(R.id.current_round);
        countdownTextView = (TextView) findViewById(R.id.time_text_view);
        nextExerciseNameTextView = (TextView) findViewById(R.id.exercise_name);
        nextExerciseRepsTextView = (TextView) findViewById(R.id.exercise_repetitions);
        nextExerciseDescriptionTextView = (TextView) findViewById(R.id.exercise_description);
        nextExerciseSetsTextView = (TextView) findViewById(R.id.exercise_sets);
        nextExerciseTitleImage = (CircularImageView) findViewById(R.id.title_image);
        pauseTextView = (TextView) findViewById(R.id.pause);
        resources = getResources();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            if (extras.getInt("requestCode") == WorkoutDetailsActivity.REQUEST_CODE) {

            }
            workout = ParseObject.createWithoutData(Workout.class, extras.getString("id"));
            try {
                workout.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            initToolbar(workout.getName());
            workout.findWorkoutExercises(new FindCallback<WorkoutExercise>() {

                @Override
                public void done(List<WorkoutExercise> resultSet, ParseException e) {
                    workoutExercises = resultSet;
                    loadNextExercise();
                }

            });

            if (!isBound) {
                tickReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String text = countdownTextView.getText().toString();

                        int currentTimeInSeconds = timeInSeconds;
                        if(!text.equals(EMPTY_TEXT)){
                            currentTimeInSeconds = Integer.valueOf(text);
                        }
                        currentTimeInSeconds--;
                        //TODO KONFIGURIERBAR MACHEN
                        if ((currentTimeInSeconds % 10 == 0 && currentTimeInSeconds > 0) || (currentTimeInSeconds <= 10 && currentTimeInSeconds > 0)) {
                            textToSpeechController.speak(String.valueOf(currentTimeInSeconds));
                        } else if (currentTimeInSeconds == 0) {
                        }

                        countdownTextView.setText(Integer.toString(currentTimeInSeconds));
                    }
                };

                finishReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        textToSpeechController.speak("Go");
                        countdownTextView.setText(EMPTY_TEXT);
                        Intent workoutRunIntent = new Intent(getApplicationContext(), WorkoutRunActivity.class);
                        workoutRunIntent.putExtra("id", workoutExercises.get(currentExercise).getObjectId());
                        if (currentRound == workout.getRounds() - 1
                                && currentExercise == workoutExercises.size() - 1
                                && currentSet < workoutExercises.get(currentExercise).getSets() -1) {
                            startActivity(workoutRunIntent);
                            textToSpeechController.shutDown();
                        } else {
                            startActivityForResult(workoutRunIntent, REQUEST_CODE);
                        }
                    }
                };
            }
        }
    }

    private void loadNextExercise() {
        nextExercise = ParseObject.createWithoutData(Exercise.class, workoutExercises.get(currentExercise).getExercise().getObjectId());
        try {
            nextExercise.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        nextExerciseNameTextView.setText(nextExercise.getName());
        nextExerciseDescriptionTextView.setText(nextExercise.getDescription());
        currentRoundTextView.setText((currentRound + 1) + "/" + workout.getRounds());

        String repetitionsText = workoutExercises.get(currentExercise).getRepetitions() + " ";
        repetitionsText += workoutExercises.get(currentExercise).isRepeatable() ? this.getResources().getString(R.string.repetitions_short) : this.getResources().getString(R.string.seconds_short);
        nextExerciseRepsTextView.setText(repetitionsText);

        String setsText = workoutExercises.get(currentExercise).getSets() <= 1 ? "1 " + this.getResources().getString(R.string.set) : workoutExercises.get(currentExercise).getSets() + " " + this.getResources().getString(R.string.sets);
        nextExerciseSetsTextView.setText(setsText);

        nextExercise.findTitleImage(new FindCallback<Image>() {

            @Override
            public void done(List<Image> resultSet, ParseException e) {
                if (!resultSet.isEmpty() && e == null) {
                    ParseFile titleImage = resultSet.get(0).getImageFile();

                    if (titleImage != null) {
                        titleImage.getDataInBackground(new GetDataCallback() {

                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    Bitmap titleImageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    nextExerciseTitleImage.setImageBitmap(titleImageBitmap);
                                }
                            }

                        });
                    }
                }
            }
        });

        Intent countdownServiceIntent = new Intent(this, CountdownService.class);
        isBound = bindService(countdownServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initToolbar(String workoutName) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(workoutName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(tickReceiver, new IntentFilter(CountdownService.CTS_TICK));
        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, new IntentFilter(CountdownService.CTS_FINISH));
    }

    @Override
    protected void onStop(){
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(tickReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(finishReceiver);
        if(isBound){
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (resultCode == RESULT_OK) {

        String pauseText = resources.getString(R.string.pause) + ". ";
        if (currentExercise == workoutExercises.size() - 1) {
            currentRound++;
            currentExercise = 0;
            timeInSeconds = workout.getPauseBetweenRounds();
            pauseText += String.format(resources.getString(R.string.get_ready_for), resources.getString(R.string.round).toLowerCase());
            pauseTextView.setText("Get ready for next round!");
        } else {
            if (currentSet < workoutExercises.get(currentExercise).getSets() - 1) {
                timeInSeconds = workout.getPauseBetweenSets();
                currentSet++;
                pauseText += String.format(resources.getString(R.string.get_ready_for), resources.getString(R.string.set).toLowerCase());
            } else {
                currentSet = 0;
                currentExercise++;
                timeInSeconds = workout.getPauseBetweenExercises();
                pauseText += String.format(resources.getString(R.string.get_ready_for), resources.getString(R.string.exercise).toLowerCase());
            }
        }
        pauseTextView.setText(pauseText);
        timeInSeconds++;
        loadNextExercise();
    }
}
