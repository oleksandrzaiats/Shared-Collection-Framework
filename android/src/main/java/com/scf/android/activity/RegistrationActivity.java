package com.scf.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.scf.android.R;
import com.scf.client.AuthClient;
import com.scf.client.config.Configuration;
import com.scf.client.config.ConfigurationFactory;
import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;

/**
 * Created by olefir on 2016-04-06.
 */
public class RegistrationActivity extends AbstractActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private AuthClient authClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEmailView = (EditText) findViewById(R.id.login_registration);
        mPasswordView = (EditText) findViewById(R.id.password_registration);
        mNameView = (EditText) findViewById(R.id.name_registration);

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        Configuration configuration = ConfigurationFactory.createConfiguration();
        authClient = new AuthClient(configuration);
    }

    private void attemptRegistration() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mNameView.setError(null);

        // Store values at the time of the login attempt.
        final String login = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String name = mNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid login address.
        if (TextUtils.isEmpty(login)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(login)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            SCFAsyncTask<UserDTO> scfAsyncTask = new SCFAsyncTask<UserDTO>() {
                @Override
                void onSuccess(UserDTO value) {
                    Toast.makeText(RegistrationActivity.this, "User " + value.getName() + " is created", Toast.LENGTH_SHORT).show();
                    Intent registration = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(registration);
                }

                @Override
                protected void onFailure(Exception value) {
                    super.onFailure(value);
                }

                @Override
                UserDTO inBackground() {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setPassword(password);
                    userDTO.setLogin(login);
                    userDTO.setName(name);
                    return authClient.register(userDTO);
                }

            };
            scfAsyncTask.execute();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isEmailValid(String login) {
        //TODO: Replace this with your own logic
        return login.length() > 4;
    }


}
