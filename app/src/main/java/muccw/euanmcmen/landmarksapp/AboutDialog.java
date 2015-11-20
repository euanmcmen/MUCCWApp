package muccw.euanmcmen.landmarksapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AboutDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(getActivity());
        aboutDialog.setMessage("This app displays the landmarks of various Scottish cities.\r\nThese can be displayed as a list or on a map.");
        aboutDialog.setTitle("About");
        aboutDialog.setIcon(R.drawable.ic_action_about);
        return aboutDialog.create();
    }
}
