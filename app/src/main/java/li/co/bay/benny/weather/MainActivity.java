package li.co.bay.benny.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String edit=editText.getText().toString();

                Downloader d= new Downloader();
                d.execute("http://api.openweathermap.org/data/2.5/weather?q="+edit+"&appid=5a50565e4e371c28faa56d856e4e7e98&units=metric");

            }
        });
    }
        public class Downloader extends AsyncTask<String ,Void, String>
        {
            @Override
            protected String doInBackground(String... params) {

                BufferedReader input = null;
                HttpURLConnection connection = null;
                StringBuilder response = new StringBuilder();
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line="";
                    while ((line=input.readLine())!=null){
                        response.append(line+"\n");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    if (input!=null){
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
                return response.toString();

            }


            @Override
            protected void onPostExecute(String jsonText) {

                Log.d("string", jsonText);
                Gson gson = new Gson();

                Jasonhelper jSonResponse= gson.fromJson(jsonText, Jasonhelper.class);
                textView.setText(jSonResponse.weather.get(0).description);
                String image= jSonResponse.weather.get(0).icon;
                

                Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+image+".png").into(imageView);


            }
        }


    }

