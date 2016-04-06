package com.scf.android;

import android.app.Application;
import android.content.SharedPreferences;
import com.scf.client.SCFClient;
import com.scf.client.config.ConfigurationFactory;
import com.scf.shared.dto.TokenDTO;

public class SCFApplication extends Application {

    public static final String MY_PREFS_NAME = "SCFPregerences";
    private static final String TOKEN = "token";

    private SCFClient scfClient;

    /**
     * Saving token to SharedPreferences
     * @param tokenDTO
     */
    public void saveToken(TokenDTO tokenDTO) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(TOKEN, tokenDTO.getToken());
        editor.apply();
    }

    /**
     * Retrieve SCFClient instance.
     * User only this method for getting SCFClient object
     * @return
     */
    public SCFClient getSCFClient() {
        if (scfClient == null) {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String token = prefs.getString(TOKEN, null);
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(token);
            scfClient = new SCFClient(ConfigurationFactory.createConfiguration(), tokenDTO);
        }
        return scfClient;
    }

    /**
     * Check is user has logged in application
     * @return
     */
    public boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString(TOKEN, null);
        return token != null;
    }

    /**
     * Logout from application
     */
    public void logOut() {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(TOKEN, null);
        editor.apply();
    }
}
