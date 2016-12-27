package inf.reutlingenuniversity.de.trainbetter.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by EL on 20.12.2016.
 */

@ParseClassName("Image")
public class Image extends ParseObject {

    public static final String DESCRIPTION = "description";
    public static final String IMAGE_FILE = "imageFile";
    public static final String EXERCISE_POINTER = "exercisePointer";
    public static final String ORDER = "order";
    public static final String IS_TITLE_IMAGE = "isTitleImage";

    public String getDescription() {
        return this.getString(Image.DESCRIPTION);
    }

    public void  setDescription(String description) {
        this.put(Image.DESCRIPTION, description);
    }

    public ParseFile getImageFile() {
        return this.getParseFile(Image.IMAGE_FILE);
    }

    public void setImageFile(ParseFile imageFile) {
        this.put(Image.IMAGE_FILE, imageFile);
    }

    public Exercise getExercise() {
        return (Exercise) this.getParseObject(Image.EXERCISE_POINTER);
    }

    public void setExercise(Exercise exercise) {
        this.put(Image.EXERCISE_POINTER, exercise);
    }

    public int getOrder() {
        return this.getInt(Image.ORDER);
    }

    public void setOrder(int order) {
        this.put(Image.ORDER, order);
    }

    public boolean isTitleImage() {
        return this.getBoolean(Image.IS_TITLE_IMAGE);
    }

    public void setIsTitleImage(boolean order) {
        this.put(Image.IS_TITLE_IMAGE, order);
    }

}
