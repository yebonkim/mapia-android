package com.example.earl.sns_api;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.drm.DrmManagerClient;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class MainActivity extends FragmentActivity {

    CallbackManager callbackManager;
    private LoginButton loginBtn;

    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    public static final String APIURL = "https://api.instagram.com/v1";
    public static String CALLBACKURL = "https://github.com/bdh1011/realmapia.git";
    public static final String client_id = "2c7a7e80b13b423dbe879edc621421cb";
    public static final String client_secret = "956af1ddc25444d592ab4266c27cb2a3";
    public static final String ConsumerKey = "";
    public static final String ConsumerSecret = "";
    public static final String oauth_verifier = "";
    public static String insta_request_token="";

    private Twitter twitter;
    private AccessToken acToken;
    private RequestToken rqToken;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class AuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(CALLBACKURL)) {
                String parts[] = url.split("=");
                String request_token = parts[1];
                insta_request_token = request_token;
//                Log.d("iurl",insta_request_token);
                return true;
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.earl.sns_api",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch(PackageManager.NameNotFoundException e) {

        } catch(NoSuchAlgorithmException e){

        }

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        loginBtn = (LoginButton) findViewById(R.id.facebook);
        loginBtn.setReadPermissions("user_friends");

        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
        //facebook

        Button insta = (Button) findViewById(R.id.instagram);
        insta.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String authURLString = AUTHURL + "?client_id=" + client_id + "&redirect_uri=" + CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships";

                WebView webView = new WebView(getApplicationContext());
                webView.setVerticalScrollBarEnabled(false);
                webView.setHorizontalScrollBarEnabled(false);
                webView.setWebViewClient(new AuthWebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(authURLString);

                String tokenURLString = TOKENURL;// + "?client_id=" + client_id + "&client_secret=" + client_secret + "&redirect_uri=" + CALLBACKURL + "&grant_type=authorization_code";
                try {
                    URL url = new URL(tokenURLString);
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("POST");
                    httpsURLConnection.setDoInput(true);
                    httpsURLConnection.setDoOutput(true);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                    outputStreamWriter.write("client_id=" + client_id +
                            "&client_secret=" + client_secret +
                            "&grant_type=authorization_code" +
                            "&redirect_uri=" + CALLBACKURL +
                            "&code=" + insta_request_token);
                    outputStreamWriter.flush();
                    String response = streamToString(httpsURLConnection.getInputStream());
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                    String accessTokenString = jsonObject.getString("access_token");
                    String id = jsonObject.getJSONObject("user").getString("id");
                    String username = jsonObject.getJSONObject("user").getString("username");
                    Log.d("name",insta_request_token+username);
                } catch (Exception e) {
                    Log.d("name",insta_request_token+"failed");
                    e.printStackTrace();
                }

                setContentView(webView);
            }
        });

        Button twit = (Button) findViewById(R.id.twitter);

        twit.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                twitter = new TwitterFactory().getInstance();
                twitter.setOAuthConsumer(ConsumerKey, ConsumerSecret);

                rqToken = null;

                try{
                    rqToken = twitter.getOAuthRequestToken();
                }
                catch(TwitterException e){
                    e.printStackTrace();
                }

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(rqToken.getAuthenticationURL())));

                try {
                    acToken = twitter.getOAuthAccessToken(rqToken, oauth_verifier);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static String streamToString(InputStream is)
    {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
