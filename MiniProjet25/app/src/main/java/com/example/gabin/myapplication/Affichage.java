package com.example.gabin.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class Affichage extends Activity {
    private ImageView imge;
    private TextView date, titre,resume;
    private RatingBar note;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage);

        imge=(ImageView) findViewById(R.id.img);
        date=(TextView) findViewById(R.id.date);
        titre=(TextView) findViewById(R.id.titre);
        resume=(TextView) findViewById(R.id.resume);
        note=(RatingBar) findViewById(R.id.note);

        Intent intent = getIntent();
        String stJson = intent.getStringExtra("obj");

        try {
            JSONObject film = new JSONObject(stJson);
            System.out.println(film);
            String poster = film.getString("poster_path");
            String poster2 = poster.substring(1);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w500")
                    .appendPath(poster2);

            String myUrl = builder.build().toString();

            System.out.println(myUrl);

            Picasso.with(this)
                    .load(myUrl)
                    .resize(500, 681)
                    .centerCrop()
                    .into(imge);

            titre.setText(film.getString("title"));
            date.setText(film.getString("release_date"));
            resume.setText(film.getString("overview"));
            note.setRating(Float.parseFloat(film.getString("vote_average"))/10*4);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //https://image.tmdb.org/t/p/w500/5pAGnkFYSsFJ99ZxDIYnhQbQFXs.jpg




    }
}