package com.scf.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.scf.android.R;
import com.scf.android.SCFApplication;
import com.scf.client.SCFClient;
import com.scf.shared.dto.ArtifactDTO;

import java.io.File;
import java.net.URISyntaxException;

public class CreateActivity extends AbstractActivity {

    private EditText mNameView;
    private EditText mPathView;
    private SCFClient scfClient;

    private View mProgressView;
    private View mCreateFormView;

    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mNameView = (EditText) findViewById(R.id.artifact_name);
        mPathView = (EditText) findViewById(R.id.artifact_path);

        Button mUploadFile = (Button) findViewById(R.id.choose_button);
        mUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        Button mCreateArtifact = (Button) findViewById(R.id.create_button);
        mCreateArtifact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createArtifact();
            }
        });

        SCFApplication application = (SCFApplication) CreateActivity.this.getApplication();
        scfClient = application.getSCFClient();

        mCreateFormView = findViewById(R.id.create_artifact_form);
        mProgressView = findViewById(R.id.create_progress);
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void createArtifact() {
        boolean cancel = false;
        View focusView = null;

        final String name = mNameView.getText().toString();
        final String path = mPathView.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(path)) {
            mPathView.setError(getString(R.string.error_field_required));
            focusView = mPathView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            SCFAsyncTask<ArtifactDTO> scfAsyncTask = new SCFAsyncTask<ArtifactDTO>() {
                @Override
                void onSuccess(ArtifactDTO value) {
                    Toast.makeText(CreateActivity.this, "Artifact is created", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }

                @Override
                protected void onFailure(Exception value) {
                    super.onFailure(value);
                    Toast.makeText(CreateActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }

                @Override
                ArtifactDTO inBackground() {
                    File file = new File(path);
                    return scfClient.createArtifact(name, file);
                }
            };
            scfAsyncTask.execute();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        String path = getPath(this, uri);
                        mPathView.setText(path);
                        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "oops",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mCreateFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
