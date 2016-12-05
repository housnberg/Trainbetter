package inf.reutlingenuniversity.de.trainbetter.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import inf.reutlingenuniversity.de.trainbetter.R;

/**
 * Created by EL on 05.12.2016.
 */

public class ComponentHelper {

    public static Snackbar createSnackbar(Context context, View view, int message, Status status) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        View snackBarView = snackbar.getView();

        if (Status.ERROR == status) {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.red_A400_loud));
            snackbar.setActionTextColor(context.getResources().getColor(R.color.colorPrimaryText));
        } else if (Status.INFO == status) {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryText));
            snackbar.setActionTextColor(context.getResources().getColor(R.color.red_A400_loud));
        } else {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.green_A400_loud));
            snackbar.setActionTextColor(context.getResources().getColor(R.color.red_A400_loud));
        }

        return snackbar;
    }

    public static Snackbar createSnackbar(Context context, View view, String message, Status status) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        View snackBarView = snackbar.getView();

        if (Status.ERROR == status) {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.red_A400_loud));
            snackbar.setActionTextColor(context.getResources().getColor(R.color.colorPrimaryText));
        } else if (Status.INFO == status) {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryText));
            snackbar.setActionTextColor(context.getResources().getColor(R.color.red_A400_loud));
        } else {
            snackBarView.setBackgroundColor(context.getResources().getColor(R.color.green_A400_loud));
            snackbar.setActionTextColor(context.getResources().getColor(R.color.red_A400_loud));
        }

        return snackbar;
    }

    public static Snackbar createSnackbar(Context context, View view, int message) {
        return createSnackbar(context, view, message, Status.SUCCESS);
    }

    public static Snackbar createActionSnackbar(Context context, View view, int message, Status status, int actionMessage, View.OnClickListener onClickListener) {
        Snackbar snackbar =  createSnackbar(context, view, message, status);

        snackbar.setAction(actionMessage, onClickListener);

        return snackbar;
    }

    public static Snackbar createActionSnackbar(Context context, View view, int message, int actionMessage, View.OnClickListener onClickListener) {
        return createActionSnackbar(context, view, message, Status.SUCCESS, actionMessage, onClickListener);
    }

}
