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
        viewResponse = (TextView) findViewById(R.id.result_text);

        SslConnectionAsync connectAsync = new SslConnectionAsync();
    }

    protected void showResponse(String response) {
        if(response == null) {
            viewResponse.setTextColor(getResources().getColor(R.color.red));
        } else {
            viewResponse.setTextColor(getResources().getColor(R.color.green));
        }
    }

    class SslConnectionAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                //do something
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
