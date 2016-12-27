package inf.reutlingenuniversity.de.trainbetter.model;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by EL on 20.12.2016.
 */

@ParseClassName("WorkoutExercise")
public class WorkoutExercise extends ParseObject {

    public static final String ORDER = "order";
    public static final String IS_REPEATABLE = "isRepeatable";
    public static final String REPETITIONS = "repetitions";
    public static final String EXERCISE_POINTER = "exercisePointer";
    public static final String WORKOUT_POINTER = "workoutPointer";
    public static final String SETS = "sets";

    public int getOrder() {
        return this.getInt(WorkoutExercise.ORDER);
    }

    public void setOrder(int order) {
        this.put(WorkoutExercise.ORDER, order);
    }

    public boolean isRepeatable() {
        return this.getBoolean(WorkoutExercise.IS_REPEATABLE);
    }

    public void setRepeatable(boolean isRepeatable) {
        this.put(WorkoutExercise.IS_REPEATABLE, isRepeatable);
    }

    /**
     * This method returns the amount of repetions if {@link #isRepeatable()} returns true.
     * Otherwise the method returns the amount of seconds.
     * @return amount of repetitions or seconds
     * @see #isRepeatable()
     */
    public int getRepetitions() {
        return this.getInt(WorkoutExercise.REPETITIONS);
    }

    public void setRepetitions(int repetitions) {
        this.put(WorkoutExercise.REPETITIONS, repetitions);
    }

    public Exercise getExercise() {
        return (Exercise) this.getParseObject(WorkoutExercise.EXERCISE_POINTER);
    }

    public void setExercise(Exercise exercise) {
        this.put(WorkoutExercise.EXERCISE_POINTER, exercise);
    }

    public Workout getWorkout() {
        return (Workout) this.getParseObject(WorkoutExercise.WORKOUT_POINTER);
    }

    public void setWorkout(Workout workout) {
        this.put(WorkoutExercise.WORKOUT_POINTER, workout);
    }

    public int getSets() {
        return this.getInt(WorkoutExercise.SETS);
    }

    public void setSets(int sets) {
        this.put(WorkoutExercise.SETS, sets);
    }

    public void getWorkoutExercisesWithSetsGreaterThan(FindCallback<WorkoutExercise> findCallback, int greaterThan) {
        ParseQuery<WorkoutExercise> query = ParseQuery.getQuery(WorkoutExercise.class);
        query.whereEqualTo(WorkoutExercise.WORKOUT_POINTER, getWorkout());
        query.whereGreaterThan(WorkoutExercise.SETS, greaterThan);
        query.findInBackground(findCallback);
    }
}
