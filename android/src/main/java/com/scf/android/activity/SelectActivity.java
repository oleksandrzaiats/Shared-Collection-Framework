package com.scf.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.scf.android.R;
import com.scf.android.SCFApplication;
import com.scf.client.SCFClient;
import com.scf.shared.dto.ArtifactDTO;

import java.io.File;

public class SelectActivity extends AbstractActivity {

    private TextView mArtifactName;
    private SCFClient scfClient;
    private View mProgressView;
    private View mSelectView;
    private ArtifactDTO artifact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mArtifactName = (TextView) findViewById(R.id.select_name);

        Button mCreateArtifact = (Button) findViewById(R.id.select_button);
        mCreateArtifact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadArtifact();
            }
        });

        SCFApplication application = (SCFApplication) SelectActivity.this.getApplication();
        scfClient = application.getSCFClient();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Long value = extras.getLong("artifact_id");
            artifact = scfClient.getArtifact(value);
            mArtifactName.setText(artifact.getName());
        }

        mProgressView = findViewById(R.id.select_form);
        mProgressView = findViewById(R.id.select_progress);
    }

    private void downloadArtifact() {
        boolean cancel = false;

        showProgress(true);
        SCFAsyncTask<File> scfAsyncTask = new SCFAsyncTask<File>() {
            @Override
            void onSuccess(File value) {
                Toast.makeText(SelectActivity.this, "Artifact is downloaded", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

            @Override
            protected void onFailure(Exception value) {
                super.onFailure(value);
                Toast.makeText(SelectActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }

            @Override
            File inBackground() {
                File directory = new File(Environment.getExternalStorageDirectory() + "//files//");
                return scfClient.downloadArtifactFile(artifact, directory);
            }
        };
        scfAsyncTask.execute();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSelectView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSelectView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSelectView.setVisibility(show ? View.GONE : View.VISIBLE);
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
