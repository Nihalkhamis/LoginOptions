package com.login.loginoptions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

     LoginButton loginbutton;
     CallbackManager callbackManager;
    CircleImageView profile_pic;
    TextView name,emailaddress,userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        profile_pic = findViewById(R.id.profile_pic);
        name = findViewById(R.id.name);
        emailaddress = findViewById(R.id.emailaddress);
        userid = findViewById(R.id.userid);

        loginbutton = findViewById(R.id.loginbutton);
        callbackManager = CallbackManager.Factory.create();



        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        loginbutton.setPermissions(Arrays.asList("email","public_profile"));

        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                 name.setText("Login success \n"+loginResult.getAccessToken().getUserId()+"\n"
//                 +loginResult.getAccessToken().getToken());

                 GraphRequest graphRequest = new GraphRequest().newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                     @Override
                     public void onCompleted(JSONObject object, GraphResponse response) {

                           DisplayUserInfo(object);
                     }

                 });
                Bundle parameters = new Bundle();
                parameters.putString("fields","first_name,last_name,email,id");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

                profile_pic.setImageResource(0);
                name.setText(" ");
                emailaddress.setText(" ");
                userid.setText(" ");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });



    }

    public void DisplayUserInfo(JSONObject object){
            String first_name, last_name, email, id,image_url;
            first_name="";
            last_name="";
            email="";
            id="";
            image_url="";
        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
            id = object.getString("id");
            image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";
            Log.d("TTT", "DisplayUserInfo: "+id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        name.setText(first_name+" "+last_name);
        emailaddress.setText(email);
        userid.setText(id);
        Picasso.get().load(image_url).into(profile_pic);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
   // AccessToken accessToken;

//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//               if (currentAccessToken == null){
//                     name.setText("");
//                     emailaddress.setText("");
//                     profile_pic.setImageResource(0);
//                    Toast.makeText(MainActivity.this, "User logged out", Toast.LENGTH_LONG).show();
//               }
//               else
//                   // accessToken = AccessToken.getCurrentAccessToken();
//
//            loaduserprofile(currentAccessToken);
//        }
//    };

//    private void loaduserprofile(AccessToken newAccessToken){
//        //graphRequest is an api to read and write data on facebook
//        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                try {
//                     first_name = object.getString("first_name");
//                     last_name = object.getString("last_name");
//                     email = object.getString("email");
//                     id = object.getString("id");
//                     image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";
//
//                    RequestOptions requestOptions = new RequestOptions();
//                    requestOptions.dontAnimate();
//
//                    Glide.with(MainActivity.this).load(image_url).into(profile_pic);
//                    name.setText(first_name+" "+last_name);
//                    emailaddress.setText(email);
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.getString("fields","first_name,last_name,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }
}
