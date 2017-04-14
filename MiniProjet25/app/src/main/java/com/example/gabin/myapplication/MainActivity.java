package com.example.gabin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    private String apiKey = "62fdbfea0c904d776ea664cba5232a94";
    private Spinner spinner;
    private EditText editText;
    private ListView list;
    private ArrayList<String> mStrings;
    private ArrayAdapter<String> aa2;
    private JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.nbResult);

        ArrayList<Integer> NbChoix = new ArrayList<>(5);
        NbChoix.add(1);
        NbChoix.add(5);
        NbChoix.add(10);
        NbChoix.add(15);
        NbChoix.add(20);

        ArrayAdapter<Integer> arrayAdapter =
                new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1, NbChoix);

        spinner.setAdapter(arrayAdapter);

        list = (ListView) findViewById(R.id.list);

        mStrings = new ArrayList<>();
        aa2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mStrings);
        list.setAdapter(aa2);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(MainActivity.this, Affichage.class);
                //param : data.getJSONObject(position);
                try {
                    intent.putExtra("obj", data.getJSONObject(position).toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    erreur("changement fenetre");
                }
            }
        });
    }

    public void action(View view) {
        //https://api.themoviedb.org/3/search/movie?api_key=62fdbfea0c904d776ea664cba5232a94&language=fr-FR&page=1&include_adult=false&query=Logan

        editText = (EditText) findViewById(R.id.recherche);
        String src = editText.getText().toString();
        editText.setEnabled(false);
        editText.setEnabled(true);

        if(!src.isEmpty()){
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("search")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", apiKey)
                    .appendQueryParameter("language", "fr-FR")
                    .appendQueryParameter("page", "1")
                    .appendQueryParameter("include_adult", "false")
                    .appendQueryParameter("query", src);
            String myUrl = builder.build().toString();

            System.out.println(myUrl);

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            while(!mStrings.isEmpty()){
                                mStrings.remove(0);
                            }
                            try {
                                JSONObject json = new JSONObject(response);
                                data = json.getJSONArray("results");

                                int i =0;
                                while (i<Integer.parseInt(spinner.getSelectedItem().toString()) && data.getJSONObject(i)!=null){
                                    JSONObject film = data.getJSONObject(i);
                                    System.out.println(film);
                                    String titre = film.getString("title");
                                    mStrings.add(titre);
                                    i++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                            aa2.notifyDataSetChanged();


                            erreur("ok");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    erreur("Volley error");
                }
            });

            queue.add(stringRequest);



        }else{
            erreur("qu'es que vous voulez rechercher ?");
        }
    }

    public void erreur(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}