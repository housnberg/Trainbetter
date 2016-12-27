package inf.reutlingenuniversity.de.trainbetter.model;

import android.provider.ContactsContract;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by EL on 20.12.2016.
 */

@ParseClassName("Exercise")
public class Exercise extends ParseObject {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";

    public String getName() {
        return this.getString(Exercise.NAME);
    }

    public void setName(String name) {
        this.put(Exercise.NAME, name);
    }

    public String getDescription() {
        return this.getString(Exercise.DESCRIPTION);
    }

    public void setDescription(String description) {
        this.put(Exercise.DESCRIPTION, description);
    }

    /**
     * Finds all {@link Image} objects which are belong to an {@link Exercise} asynchronously.
     * @param findCallback The callback to handle of type {@link FindCallback}.
     * @see Image
     * @see FindCallback
     */
    public void findCorrespondingImages(FindCallback<Image> findCallback) {
        ParseQuery<Image> query = ParseQuery.getQuery(Image.class);
        query.whereEqualTo(Image.EXERCISE_POINTER, this);
        query.orderByAscending(Image.ORDER);
        query.findInBackground(findCallback);
    }

    /**
     * Finds the title {@link Image} object which belong to an {@link Exercise} asynchronously.
     * @param findCallback The callback to handle of type {@link FindCallback}.
     * @see Image
     * @see FindCallback
     */
    public void findTitleImage(FindCallback<Image> findCallback) {
        ParseQuery<Image> query = ParseQuery.getQuery(Image.class);
        query.whereEqualTo(Image.EXERCISE_POINTER, this);
        query.whereEqualTo(Image.IS_TITLE_IMAGE, true);
        query.findInBackground(findCallback);
    }

}
