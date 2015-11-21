package muccw.euanmcmen.landmarksapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Euan on 21/11/2015.
 */
public class AboutDialogFactory
{
    public static void ShowAlertDialog(Context appContext, String message, String title, boolean useADIcon)
    {
        //This method displays an alert dialog box.

        //Build the alert dialog box with passed parameters.
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(appContext);
        aboutDialog.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });
        aboutDialog.setTitle(title);

        //Use an icon depending on the isAboutDialog flag.
        if (useADIcon)
            aboutDialog.setIcon(R.drawable.ic_action_about);
        else
            aboutDialog.setIcon(R.drawable.ic_action_settings);

        //Display the dialog box.
        aboutDialog.show();
    }
}
