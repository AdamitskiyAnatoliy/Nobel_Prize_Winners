package com.adamitskiy.anatoliy.nobel_prize_winners;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(" Nobel Prize Countries");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*
            Network Verification
         */
        if (!Network.checkNetwork(this)) {
            Toast.makeText(this, "Please Reconnect Network", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
            Network + No Storage = Download
         */
        if (Network.checkNetwork(this)) {
            if (DataStorage.getCountries(this) == null) {
                new DownloadCountry().execute("http://api.nobelprize.org/v1/country.json");
            }
        }
    }

    /*
        Main Activities Fragment
     */
    public static class MainActivityFragment extends ListFragment {

        ArrayList<Country> countryList = new ArrayList<Country>();

        public MainActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onResume() {
            super.onResume();

            /*
                1. Broadcast Receiver Registration
                2. Loading list from previous store (if exists)
             */
            IntentFilter intentFilter = new IntentFilter(String.valueOf(R.string.COUNTRY_SAVE_COMPLETE));
            getActivity().registerReceiver(listReceiver, intentFilter);

            if (DataStorage.getCountries(getActivity()) != null) {
                countryList = DataStorage.getCountries(getActivity());
                setListAdapter(new CountryListAdapter(getActivity(), countryList));
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            getActivity().unregisterReceiver(listReceiver);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);

            /*
                Opening new activity and passing country as intent.
             */
            Intent intent = new Intent(getActivity(), LaureateActivity.class);
            intent.putExtra("Country", countryList.get(position));
            startActivity(intent);
        }

        BroadcastReceiver listReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                /*
                    Receiving confirmation of first download.
                 */
                if (intent.getAction().equals(String.valueOf(R.string.COUNTRY_SAVE_COMPLETE))) {
                    countryList = DataStorage.getCountries(getActivity());
                    setListAdapter(new CountryListAdapter(getActivity(), countryList));
                }

            }
        };
    }

    /*
        Country Download
     */
    public class DownloadCountry extends AsyncTask<String,Void,ArrayList<Country>> {

        @Override
        protected ArrayList<Country> doInBackground(String... params) {

            String data = null;
            ArrayList<Country> countryList = new ArrayList<Country>();

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                data = IOUtils.toString(is);
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (data != null) {
                try {
                    JSONObject mainObject = new JSONObject(data);
                    JSONArray countryArray = mainObject.getJSONArray("countries");

                    for (int i = 0; i < countryArray.length(); i++) {

                        JSONObject country = countryArray.getJSONObject(i);
                        String code = "", name = "";

                        if (country.has("code")) {
                            code = country.getString("code");
                        }
                        if (country.has("name")) {
                            name = country.getString("name");

                            if (name.equals("Southern Rhodesia")) {
                                code = "ZW";
                            }
                        }

                        countryList.add(new Country(code, name));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return countryList;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> countries) {
            super.onPostExecute(countries);

            /*
                Saving Data and Sending Completion Intent
             */
            DataStorage.saveCountries(getApplicationContext(), countries);

            Intent saveComplete = new Intent(String.valueOf(R.string.COUNTRY_SAVE_COMPLETE));
            sendBroadcast(saveComplete);

        }

    }

}
