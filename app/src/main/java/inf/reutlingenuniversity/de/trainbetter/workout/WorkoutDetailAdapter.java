package inf.reutlingenuniversity.de.trainbetter.workout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.OnParseObjectClickListener;
import inf.reutlingenuniversity.de.trainbetter.R;
import inf.reutlingenuniversity.de.trainbetter.model.Exercise;
import inf.reutlingenuniversity.de.trainbetter.model.Image;
import inf.reutlingenuniversity.de.trainbetter.model.WorkoutExercise;

/**
 * Created by EL on 24.12.2016.
 */

public class WorkoutDetailAdapter extends RecyclerView.Adapter<WorkoutDetailAdapter.ViewHolder> {

    private final List<WorkoutExercise> workoutExercises;
    private final Context context;
    private final OnParseObjectClickListener listener;

    public WorkoutDetailAdapter(Context context, List<WorkoutExercise> workoutExercises, OnParseObjectClickListener listener) {
        this.workoutExercises = workoutExercises;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public WorkoutDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View workoutDetailView = inflater.inflate(R.layout.item_exercise, parent, false);
        WorkoutDetailAdapter.ViewHolder viewHolder = new WorkoutDetailAdapter.ViewHolder(workoutDetailView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WorkoutDetailAdapter.ViewHolder viewHolder, int position) {
        WorkoutExercise workoutExercise = workoutExercises.get(position);

        Exercise exercise = workoutExercise.getExercise();
        try {
            exercise.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.exerciseNameTextView.setText(exercise.getName());
        viewHolder.exerciseDescriptionTextView.setText(exercise.getDescription());

        String repetitionsText = workoutExercise.getRepetitions() + " ";
        repetitionsText += workoutExercise.isRepeatable() ? context.getResources().getString(R.string.repetitions_short) : context.getResources().getString(R.string.seconds_short);
        viewHolder.repsTextView.setText(repetitionsText);

        String setsText = workoutExercise.getSets() <= 1 ? "1 " + context.getResources().getString(R.string.set) : workoutExercise.getSets() + " " + context.getResources().getString(R.string.sets);
        viewHolder.setsTextView.setText(setsText);

        exercise.findTitleImage(new FindCallback<Image>() {

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
                                    viewHolder.titleImageView.setImageBitmap(titleImageBitmap);
                                }
                            }

                        });
                    }
                }
            }
        });

        viewHolder.bind(workoutExercise, listener);
    }

    @Override
    public int getItemCount() {
        return workoutExercises != null ? workoutExercises.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView exerciseNameTextView;
        public TextView exerciseDescriptionTextView;
        public TextView repsTextView;
        public TextView setsTextView;
        public CircularImageView titleImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseNameTextView = (TextView) itemView.findViewById(R.id.exercise_name);
            exerciseDescriptionTextView = (TextView) itemView.findViewById(R.id.exercise_description);
            repsTextView = (TextView) itemView.findViewById(R.id.exercise_repetitions);
            setsTextView = (TextView) itemView.findViewById(R.id.exercise_sets);
            titleImageView = (CircularImageView) itemView.findViewById(R.id.title_image);
        }

        public void bind(final WorkoutExercise exercise, final OnParseObjectClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onParseObjectClick(exercise);
                }
            });
        }

    }
}
