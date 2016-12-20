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
import com.parse.ParseObject;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.R;
import inf.reutlingenuniversity.de.trainbetter.model.Workout;

/**
 * Created by EL on 19.12.2016.
 */

public class WorkoutsOverviewAdapter extends RecyclerView.Adapter<WorkoutsOverviewAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Workout> workouts;
    // Store the context for easy access
    private Context context;

    // Pass in the contact array into the constructor
    public WorkoutsOverviewAdapter(Context context, List<Workout> workouts) {
        this.workouts = workouts;
        this.context = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public WorkoutsOverviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_workout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(WorkoutsOverviewAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Workout workout = workouts.get(position);

        // Set item views based on your views and data model
        TextView workoutNameTextView = viewHolder.workoutNameTextView;
        TextView workoutDescriptionTextView = viewHolder.workoutDescriptionTextView;
        final ImageView titleImageView = viewHolder.titleImageView;
        final RelativeLayout workoutWrapper = viewHolder.workoutWrapper;

        workoutNameTextView.setText(workout.getName());
        workoutDescriptionTextView.setText(workout.getDescription());
        ParseFile titleImage = (ParseFile) workout.getTitleImage();

        if (titleImage != null) {
            titleImage.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap titleImageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        titleImageView.setImageBitmap(titleImageBitmap);
                        Palette.from(titleImageBitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch swatch = palette.getVibrantSwatch();
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

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return workouts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView workoutNameTextView;
        public TextView workoutDescriptionTextView;
        public ImageView titleImageView;
        public RelativeLayout workoutWrapper;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            workoutNameTextView = (TextView) itemView.findViewById(R.id.workout_name);
            workoutDescriptionTextView = (TextView) itemView.findViewById(R.id.workout_description);
            titleImageView = (ImageView) itemView.findViewById(R.id.title_image);
            workoutWrapper = (RelativeLayout) itemView.findViewById(R.id.workout_wrapper);
        }
    }
}
