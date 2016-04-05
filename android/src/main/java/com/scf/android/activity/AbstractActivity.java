package com.scf.android.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.scf.android.utils.Tuple;
import com.scf.client.exception.SCFServerException;

public abstract class AbstractActivity extends AppCompatActivity {

    abstract class SCFAsyncTask<R> extends AsyncTask<Object, Object, Tuple<R, Exception>> {

        boolean crashed = false;

        @Override
        protected Tuple<R, Exception> doInBackground(Object... params) {
            crashed = false;
            try {
                R result = inBackground();
                return new Tuple<>(result, null);
            } catch (Exception e) {
                crashed = true;
                return new Tuple<>(null, e);
            }
        }

        @Override
        protected void onPostExecute(Tuple<R, Exception> value) {
            if (!crashed) {
                onSuccess(value.getResult());
            } else {
                onFailure(value.getException());
            }
            crashed = false;
        }

        protected void onFailure(Exception e) {
            if (e instanceof SCFServerException) {
                showErrorAlert((SCFServerException) e);
            } else {
                throw new RuntimeException(e);
            }
        }

        abstract void onSuccess(R value);

        abstract R inBackground();
    }

    private void showErrorAlert(SCFServerException value) {
        String title = "";
        String message = "";
        if (value.getErrorDTO() != null) {
            title = "Error";
            message = value.getErrorDTO().getMessage();
        } else {
            title = "Error";
            message = value.getHttpMessage();
        }
        new AlertDialog.Builder(AbstractActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
