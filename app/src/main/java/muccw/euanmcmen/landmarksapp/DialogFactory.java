package muccw.euanmcmen.landmarksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class DialogFactory
{
    //This method displays an alert dialog box.
    public static void ShowAlertDialog(Context appContext, String message, String title)
    {
        //Build the alert dialog box with passed parameters.
        AlertDialog.Builder dialog = new AlertDialog.Builder(appContext);
        dialog.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });
        dialog.setTitle(title);

        //Set about icon
        dialog.setIcon(R.drawable.ic_action_about);

        //Display the dialog box.
        dialog.show();
    }

    //This method displays a settings dialog box.
    public static void ShowPreferencesDialog(Context appContext, String message, String title)
    {
        //Build the alert dialog box with passed parameters.
        AlertDialog.Builder dialog = new AlertDialog.Builder(appContext);
        dialog.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });
        dialog.setTitle(title);

        //Use an icon depending on the isAboutDialog flag.
        dialog.setIcon(R.drawable.ic_action_settings);

        //Display the dialog box.
        dialog.show();
    }

    //This method displays an confirmation dialog box.
    public static void ShowDeleteConfirmationDialog(Context appContext, String message, String title, DialogInterface.OnClickListener clickEvent)
    {
        //Build the alert dialog box with passed parameters, and use the click event listener created in calling class.
        AlertDialog.Builder dialog = new AlertDialog.Builder(appContext);
        dialog.setMessage(message).setPositiveButton("Yes", clickEvent).setNegativeButton("No", clickEvent);
        dialog.setTitle(title);

        //Use an icon depending on the isAboutDialog flag.
        dialog.setIcon(R.drawable.ic_action_warning);

        //Display the dialog box.
        dialog.show();
    }
}
