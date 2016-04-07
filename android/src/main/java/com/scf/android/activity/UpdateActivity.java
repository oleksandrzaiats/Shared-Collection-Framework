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

public class UpdateActivity extends AbstractActivity  {
    private EditText mNameView;
    private EditText mPathView;
    private SCFClient scfClient;

    private View mProgressView;
    private View mUpdateFormView;

    private ArtifactDTO artifact;

    private static final int FILE_SELECT_CODE = 0;

    public static final String ARTIFACT_ID_EXTRA = "artifact_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mNameView = (EditText) findViewById(R.id.update_artifact_name);
        mPathView = (EditText) findViewById(R.id.update_artifact_path);

        Button mCreateArtifact = (Button) findViewById(R.id.update_button);
        mCreateArtifact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateArtifact();
            }
        });

        Button mChooseArtifact = (Button) findViewById(R.id.choose_updated_button);
        mChooseArtifact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseArtifact();
            }
        });

        SCFApplication application = (SCFApplication) UpdateActivity.this.getApplication();
        scfClient = application.getSCFClient();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Long value = extras.getLong(ARTIFACT_ID_EXTRA);
            getArtifact(value);
        }

        mUpdateFormView = findViewById(R.id.create_artifact_form);
        mProgressView = findViewById(R.id.update_progress);
    }

    private void getArtifact(final long id) {
        showProgress(true);
        SCFAsyncTask<ArtifactDTO> scfAsyncTask = new SCFAsyncTask<ArtifactDTO>() {
            @Override
            void onSuccess(ArtifactDTO value) {
                //Toast.makeText(UpdateActivity.this, "Artifact is updated", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

            @Override
            protected void onFailure(Exception value) {
                super.onFailure(value);
                Toast.makeText(UpdateActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

            @Override
            ArtifactDTO inBackground() {
                artifact = scfClient.getArtifact(id);
                mNameView.setText(artifact.getName());
                return artifact;
            }
        };
        scfAsyncTask.execute();
    }

    private void chooseArtifact() {
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

    private void updateArtifact() {
        boolean cancel = false;
        View focusView = null;

        final String name = mNameView.getText().toString();
        final String path = mPathView.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            SCFAsyncTask<ArtifactDTO> scfAsyncTask = new SCFAsyncTask<ArtifactDTO>() {
                @Override
                void onSuccess(ArtifactDTO value) {
                    Toast.makeText(UpdateActivity.this, "Artifact is updated", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }

                @Override
                protected void onFailure(Exception value) {
                    super.onFailure(value);
                    Toast.makeText(UpdateActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }

                @Override
                ArtifactDTO inBackground() {
                    ArtifactDTO updatedArtifact = scfClient.getArtifact(artifact.getId());
                    updatedArtifact.setName(name);

                    if (path != null && !path.equals("")) {
                        File file = new File(path);
                        return scfClient.updateArtifactFile(updatedArtifact, file);
                    }

                    return scfClient.updateArtifact(updatedArtifact);
                }
            };
            scfAsyncTask.execute();

        }
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mUpdateFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mUpdateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
