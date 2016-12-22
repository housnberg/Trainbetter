package inf.reutlingenuniversity.de.trainbetter.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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

    public int getOrder() {
        return this.getInt(WorkoutExercise.ORDER);
    }

    public void setOrder(int order) {
        this.put(WorkoutExercise.ORDER, order);
    }

    public boolean getIsRepeatable() {
        return this.getBoolean(WorkoutExercise.IS_REPEATABLE);
    }

    public void setIsRepeatable(boolean isRepeatable) {
        this.put(WorkoutExercise.IS_REPEATABLE, isRepeatable);
    }

    /**
     * This method returns the amount of repetions if {@link #getIsRepeatable()} returns true.
     * Otherwise the method returns the amount of seconds.
     * @return amount of repetitions or seconds
     * @see #getIsRepeatable()
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

}
