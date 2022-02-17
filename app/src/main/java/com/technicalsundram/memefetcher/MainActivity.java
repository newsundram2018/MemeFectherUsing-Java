package com.technicalsundram.memefetcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String currentImageUrl;
    ImageView imageView;
    Context context;
    ProgressBar progressBar;
    String url = "https://meme-api.herokuapp.com/gimme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressbar);
        GetData();
    }

    public void  nextMeme(View view){
        GetData();
    }

    public void shareMeme(View view){
        Intent intent = new Intent(Intent.ACTION_SEND) ;
        intent.putExtra(Intent.EXTRA_TEXT,"Checkout this cool meme");
        intent.setType("text/plane");
        Intent chooser = Intent.createChooser(intent,"Share this meme using For loop");
        startActivity(intent);
    }



    private void GetData() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentImageUrl = response.getString("url");
//                    Toast.makeText(MainActivity.this,"User Id"+response.getInt("ups")+"\n" +
//                            "Id"+response.getString("url")+"\n"+
//                            "Title"+response.getString("title"),Toast.LENGTH_SHORT).show();
//                    Log.d("myapp","The Response is "+response.getString("title"));

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });

        Glide.with(this).load(currentImageUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Image Failed",Toast.LENGTH_LONG);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Image Ready",Toast.LENGTH_LONG);
                return false;
            }
        }).into(imageView);

        requestQueue.add(jsonObjectRequest);
    }

}