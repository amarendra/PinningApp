package app.test.amar.pinningapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.test.amar.pinningapp.R;

import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
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
                byte[] secret = null;

                TrustManager tm[] = { new PubKeyManager() };
                assert (null != tm);

                SSLContext sslContext = SSLContext.getInstance("TLS");
                assert (null != sslContext);
                sslContext.init(null, tm, null);

                String urlStr = Constants.URL_RANDOM;
                URL url = new URL(urlStr);

                assert (null != url);

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                assert (null != connection);

                connection.setSSLSocketFactory(sslContext.getSocketFactory());
                InputStreamReader instream = new InputStreamReader(connection.getInputStream());
                assert (null != instream);

                StreamTokenizer tokenizer = new StreamTokenizer(instream);
                assert (null != tokenizer);

                secret = new byte[16];
                assert (null != secret);

                int idx = 0, token;
                while (idx < secret.length) {
                    token = tokenizer.nextToken();
                    if (token == StreamTokenizer.TT_EOF)
                        break;
                    if (token != StreamTokenizer.TT_NUMBER)
                        continue;

                    secret[idx++] = (byte) tokenizer.nval;
                }

                // Prepare return value
                response = getString(secret);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.d(TAG, "Response fetches is: " + response);

            return response;
        }

        private String getString(byte[] response) {
            assert (null != response);

            StringBuilder sb = new StringBuilder(response.length * 3 + 1);
            assert (null != sb);

            for (int i = 0; i < response.length; i++) {
                sb.append(String.format("%02X ", response[i]));
                response[i] = 0;
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response == null) {
                response = "Response is " + response;
            }

            Log.d(TAG, response);
            showResponse(response);
        }
    }
}
