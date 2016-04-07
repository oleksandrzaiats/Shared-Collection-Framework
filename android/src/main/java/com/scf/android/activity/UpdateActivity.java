package com.scf.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
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

public class UpdateActivity extends AbstractActivity  {
    private EditText mNameView;
    private SCFClient scfClient;

    private View mProgressView;
    private View mUpdateFormView;

    private ArtifactDTO artifact;

    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mNameView = (EditText) findViewById(R.id.update_artifact_name);

        Button mCreateArtifact = (Button) findViewById(R.id.update_button);
        mCreateArtifact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateArtifact();
            }
        });

        SCFApplication application = (SCFApplication) UpdateActivity.this.getApplication();
        scfClient = application.getSCFClient();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Long value = extras.getLong("artifact_id");
            artifact = scfClient.getArtifact(value);
            mNameView.setText(artifact.getName());
        }

        mUpdateFormView = findViewById(R.id.create_artifact_form);
        mProgressView = findViewById(R.id.update_progress);
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

    private void updateArtifact() {
        boolean cancel = false;
        View focusView = null;

        final String name = mNameView.getText().toString();

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
