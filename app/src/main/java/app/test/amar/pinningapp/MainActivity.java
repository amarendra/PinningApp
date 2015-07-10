package app.test.amar.pinningapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.test.amar.pinningapp.R;

public class MainActivity extends Activity {

    private TextView viewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewResponse = (TextView) findViewById(R.id.response_text);

        SslConnectionAsync connectAsync = new SslConnectionAsync();
        connectAsync.execute();
    }

    protected void showResponse(String response) {
        if(response == null) {
            viewResponse.setTextColor(getResources().getColor(R.color.red));
        } else {
            viewResponse.setTextColor(getResources().getColor(R.color.green));
        }
        viewResponse.setText(response);
    }

    class SslConnectionAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            try {
                //do something
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response == null) {
                response = "Response is " + response;
            }
            showResponse(response);
        }
    }
}
