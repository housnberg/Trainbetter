package inf.reutlingenuniversity.de.trainbetter.workout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.R;
import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.utils.Helper;

/**
 * Created by EL on 19.12.2016.
 */
public class WorkoutsOverviewAdapter extends RecyclerView.Adapter<WorkoutsOverviewAdapter.ViewHolder> {

    private List<Workout> workouts;
    private Context context;

    public WorkoutsOverviewAdapter(Context context, List<Workout> workouts) {
        this.workouts = workouts;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public WorkoutsOverviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View workoutOverviewView = inflater.inflate(R.layout.item_workout, parent, false);

        ViewHolder viewHolder = new ViewHolder(workoutOverviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WorkoutsOverviewAdapter.ViewHolder viewHolder, int position) {
        Workout workout = workouts.get(position);

        TextView workoutNameTextView = viewHolder.workoutNameTextView;
        TextView workoutDescriptionTextView = viewHolder.workoutDescriptionTextView;
        final ImageView titleImageView = viewHolder.titleImageView;
        final RelativeLayout workoutWrapper = viewHolder.workoutWrapper;

        workoutNameTextView.setText(workout.getName());
        workoutDescriptionTextView.setText(workout.getDescription());
        ParseFile titleImage = workout.getTitleImage();

        if (titleImage != null) {
            titleImage.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap titleImageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        titleImageView.setImageBitmap(titleImageBitmap);
                        Palette.from(titleImageBitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch swatch = Helper.getProfileSwatch(palette);
                                if (swatch != null) {
                                    workoutWrapper.setBackgroundColor(swatch.getRgb());
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView workoutNameTextView;
        public TextView workoutDescriptionTextView;
        public ImageView titleImageView;
        public RelativeLayout workoutWrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            workoutNameTextView = (TextView) itemView.findViewById(R.id.workout_name);
            workoutDescriptionTextView = (TextView) itemView.findViewById(R.id.workout_description);
            titleImageView = (ImageView) itemView.findViewById(R.id.title_image);
            workoutWrapper = (RelativeLayout) itemView.findViewById(R.id.workout_wrapper);
        }
    }
}
