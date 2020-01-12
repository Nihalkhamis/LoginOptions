package com.login.loginoptions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginbutton;
    private CallbackManager callbackManager;
    String first_name,last_name,email,id,image_url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loginbutton = findViewById(R.id.loginbutton);
        callbackManager = CallbackManager.Factory.create();

        loginbutton.setReadPermissions(Arrays.asList("email","public_profile"));

        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                  Intent intent = new Intent(MainActivity.this,InfoActivity.class);
                  intent.putExtra("First_name",first_name);
                  intent.putExtra("Last_name",last_name);
                  intent.putExtra("Email",email);
                  intent.putExtra("ID",id);
                  intent.putExtra("Image_url",image_url);
                  startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
               if (currentAccessToken == null){
                     first_name="";
                     last_name="";
                     email="";
                     id="";
                     image_url="";
                    Toast.makeText(MainActivity.this, "User logged out", Toast.LENGTH_LONG).show();
               }
               else
                   loaduserprofile(currentAccessToken);
        }
    };

    private void loaduserprofile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                     first_name = object.getString("first_name");
                     last_name = object.getString("last_name");
                     email = object.getString("email");
                     id = object.getString("id");
                     image_url = "https://graph.facebook.com/"+id+"/pictures?type=normal";



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.getString("fields","first_name,last_name,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
