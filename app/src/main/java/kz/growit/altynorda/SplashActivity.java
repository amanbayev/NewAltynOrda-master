package kz.growit.altynorda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONObject;


public class SplashActivity extends Activity {

    ProgressView progressView;
    String urlGetCities = "http://altynorda.kz/api/citiesapi/getcities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressView = (ProgressView) findViewById(R.id.progressSplash);
        progressView.start();

        CitiesRequest();
    }

    public void CitiesRequest() {
        JsonArrayRequest jsonArrayRequestSplash = new JsonArrayRequest(
                Request.Method.GET,
                urlGetCities,
                new JSONObject(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressView.stop();
                        progressView.setVisibility(View.GONE);
                        String citiesToBundle = response.toString();

                        Intent goToMainActivity = new Intent(SplashActivity.this, MainActivity.class);
                        goToMainActivity.putExtra("citiesToBundle", citiesToBundle);
                        goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToMainActivity);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressView.stop();
                        progressView.setVisibility(View.GONE);
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequestSplash, "Cities Request");
    }
}