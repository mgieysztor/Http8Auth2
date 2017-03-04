package com.sdacademy.gieysztor.michal.http8auth2;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

import org.fuckboilerplate.rx_social_connect.Response;
import org.fuckboilerplate.rx_social_connect.RxSocialConnect;
import org.fuckboilerplate.rx_social_connect.internal.persistence.OAuth2AccessToken;

import java.io.IOException;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    final String apiKey = "6b38486d828453a8091b";
    final String apiSecret = "9b8da96f7973c61ff6cf68f6be7b4907800a29fa";
    final String secretState = "secret" + new Random().nextInt(999999);
    TextView mResultTextView;

    Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTextView = (TextView) findViewById(R.id.result_text);
        mLoginButton = (Button) findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void gitHubConnect() {

    }


    private void logIn() {
        final String apiKey = "6b38486d828453a8091b";
        final String apiSecret = "9b8da96f7973c61ff6cf68f6be7b4907800a29fa";
        final String secretState = "secret" + new Random().nextInt(999999);

        OAuth20Service service = new ServiceBuilder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .state(secretState)
                .callback("http://localhost:8080")
                .build(GitHubApi.instance());

        Log.i("TEST", "pre connect");

        RxSocialConnect.with(this, service)
                .subscribe(new Observer<Response<MainActivity, OAuth2AccessToken>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Response<MainActivity, OAuth2AccessToken> tokenResponse) {
                        Log.i("TEST", tokenResponse.toString());

                        new GetUserDataTask(tokenResponse.token().getAccessToken()).execute();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("TEST", e);
                    }

                    @Override
                    public void onComplete() { }
                });
    }




private class GetUserDataTask extends AsyncTask<Void, Void, String> {

    String token;

    public GetUserDataTask(String token) {
        this.token = token;
    }

    @Override
    protected String doInBackground(Void... params) {
        Request.Builder builder = new Request.Builder();
        builder.url("https://api.github.com/user?access_token=" + token);
        builder.get();

        Request request = builder.build();
        OkHttpClient client = new OkHttpClient();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Błąd sieci";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mResultTextView.setText(s);
    }
}

}
