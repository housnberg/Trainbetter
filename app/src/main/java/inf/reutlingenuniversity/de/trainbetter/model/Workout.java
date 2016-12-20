package inf.reutlingenuniversity.de.trainbetter.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by EL on 19.12.2016.
 */

@ParseClassName("Workout")
public class Workout extends ParseObject{

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String ROUNDS = "rounds";
    private static final String PAUSE_BETWEEN_EXERCISES = "pauseBetweenExercises";
    private static final String PAUSE_BETWEEN_ROUNDS = "pauseBetweenRounds";
    private static final String USER_POINTER = "userPointer";
    private static final String TITLE_IMAGE = "titleImage";

    public String getName() {
        return this.getString(Workout.NAME);
    }

    public void setName(String name) {
        this.put(Workout.NAME, name);
    }

    public String getDescription() {
        return this.getString(Workout.DESCRIPTION);
    }

    public void setDescription(String description) {
        this.put(Workout.DESCRIPTION, description);
    }

    public int getRounds() {
        return this.getInt(Workout.ROUNDS);
    }

    public void setRounds(int rounds) {
        this.put(Workout.ROUNDS, rounds);
    }

    public int getPauseBetweenExercises() {
        return this.getInt(Workout.PAUSE_BETWEEN_EXERCISES);
    }

    public void setPauseBetweenExercises(int pauseBetweenExercises) {
        this.put(Workout.PAUSE_BETWEEN_EXERCISES, pauseBetweenExercises);
    }

    public int getPauseBetweenRounds() {
        return this.getInt(Workout.PAUSE_BETWEEN_ROUNDS);
    }

    public void setPauseBetweenRounds(int pauseBetweenRounds) {
        this.put(Workout.PAUSE_BETWEEN_ROUNDS, pauseBetweenRounds);
    }

    public ParseUser getUserPointer() {
        return this.getParseUser(Workout.USER_POINTER);
    }

    public void setUserPointer(ParseUser userPointer) {
        this.put(Workout.USER_POINTER, userPointer);
    }

    public ParseFile getTitleImage() {
        return this.getParseFile(Workout.TITLE_IMAGE);
    }

    public void setTitleImage(ParseFile titleImage) {
        this.put(Workout.TITLE_IMAGE, titleImage);
    }
 }
