package inf.reutlingenuniversity.de.trainbetter.model;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by EL on 19.12.2016.
 */

@ParseClassName("Workout")
public class Workout extends ParseObject {
    //ToDo: Currently, there is no possibility to make an ParseObject implementing Parcelabel.
    //ToDo: Since Parcelabel is more performant than Serialization, you should refactor all ParseObjects when ParceObject is combinable with Parcelabel.

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ROUNDS = "rounds";
    public static final String PAUSE_BETWEEN_EXERCISES = "pauseBetweenExercises";
    public static final String PAUSE_BETWEEN_ROUNDS = "pauseBetweenRounds";
    public static final String PAUSE_BETWEEN_SETS = "pauseBetweenSets";
    public static final String USER_POINTER = "userPointer";
    public static final String TITLE_IMAGE = "titleImage";

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

    public int getPauseBetweenSets() {
        return this.getInt(Workout.PAUSE_BETWEEN_SETS);
    }

    public void setPauseBetweenSets(int pauseBetweenSets) {
        this.put(Workout.PAUSE_BETWEEN_SETS, pauseBetweenSets);
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

    /**
     * Finds all {@link WorkoutExercise} Objects which are belong to an {@link Workout} asynchronously.
     * @param findCallback The callback to handle of type {@link FindCallback}.
     * @see WorkoutExercise
     * @see FindCallback
     */
    public void findWorkoutExercises(FindCallback<WorkoutExercise> findCallback) {
        ParseQuery<WorkoutExercise> query = ParseQuery.getQuery(WorkoutExercise.class);
        query.whereEqualTo(WorkoutExercise.WORKOUT_POINTER, this);
        query.orderByAscending(WorkoutExercise.ORDER);
        query.findInBackground(findCallback);
    }

 }
