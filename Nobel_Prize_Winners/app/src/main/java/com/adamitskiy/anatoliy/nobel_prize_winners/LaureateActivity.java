package com.adamitskiy.anatoliy.nobel_prize_winners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Anatoliy on 6/19/15.
 */
public class LaureateActivity extends AppCompatActivity {

    Country country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laureate);

        country = (Country) getIntent().getSerializableExtra("Country");

        setTitle(country.getCountryName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentDetail, fragmentLaureate())
                .commit();
    }

    public Fragment fragmentLaureate() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("Country", country);

        LaureateFragment frag = new LaureateFragment();
        frag.setArguments(bundle);

        return frag;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Network.checkNetwork(this)) {
            if (DataStorage.getLaureates(this, country.getCountryCode()) == null) {
                new DownloadLaureate().execute("http://api.nobelprize.org/v1/laureate" +
                        ".json?bornCountryCode=" + country.getCountryCode());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    /*
        Fragment for Laureates of Selected Country
     */
    public static class LaureateFragment extends Fragment {

        ArrayList<Laureate> laureateList;
        Country fragCountry;

        ListView laureateListView;
        Button retryButton;

        public LaureateFragment () {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_laureate, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            laureateListView = (ListView) getView().findViewById(R.id.laureateListView);
            retryButton = (Button) getView().findViewById(R.id.tapToRetryButton);
            fragCountry = (Country) getArguments().getSerializable("Country");

            /*
                Doubling checking for a previous store
             */
            if (DataStorage.getLaureates(getActivity(), fragCountry.getCountryName()) != null) {

                laureateList = DataStorage.getLaureates(getActivity(), fragCountry.getCountryName());
                laureateListView.setAdapter(new LaureateListAdapter(getActivity(), laureateList));
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            IntentFilter intentFilter = new IntentFilter(String.valueOf(R.string.LAUREATE_SAVE_COMPLETE));
            getActivity().registerReceiver(listReceiver, intentFilter);
        }

        @Override
        public void onPause() {
            super.onPause();
            getActivity().unregisterReceiver(listReceiver);
        }

        BroadcastReceiver listReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                /*
                    Receiving Confirmation of Successful Download
                 */
                if (intent.getAction().equals(String.valueOf(R.string.LAUREATE_SAVE_COMPLETE))) {
                    laureateList = DataStorage.getLaureates(getActivity(), fragCountry.getCountryName());
                    if (laureateList.size() != 0) {
                        laureateListView.setAdapter(new LaureateListAdapter(getActivity(), laureateList));
                    } else {
                        laureateListView.setVisibility(View.GONE);
                        retryButton.setVisibility(View.VISIBLE);
                    }
                }

            }
        };
    }

    /*
        Laureate Download
     */
    public class DownloadLaureate extends AsyncTask<String,Void,ArrayList<Laureate>> {

        @Override
        protected ArrayList<Laureate> doInBackground(String... params) {

            String data = null;
            ArrayList<Laureate> laureateList = new ArrayList<Laureate>();

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
                    JSONArray laureateArray = mainObject.getJSONArray("laureates");

                    for (int i = 0; i < laureateArray.length(); i++) {

                        JSONObject lr = laureateArray.getJSONObject(i);
                        JSONArray prizeArray = lr.getJSONArray("prizes");

                        ArrayList<Prize> prizes = new ArrayList<Prize>();

                        for (int j = 0; j < prizeArray.length(); j++) {
                            JSONObject prize = prizeArray.getJSONObject(j);

                            String year = "", category = "", share = "", motivation = "";

                            if (prize.has("year")) {
                                year = prize.getString("year");
                            }
                            if (prize.has("category")) {
                                category = prize.getString("category");
                            }
                            if (prize.has("share")) {
                                share = prize.getString("share");
                            }
                            if (prize.has("motivation")) {
                                motivation = prize.getString("motivation");
                            }

                            prizes.add(new Prize(year, category,
                                    share, motivation));
                        }

                        String firstName = "", lastName = "", born = "", died = "", bornCountry = "",
                                bornCountryCode = "", bornCity = "";

                        if (lr.has("firstname")) {
                            firstName = lr.getString("firstname");
                        }
                        if (lr.has("surname")) {
                            lastName = lr.getString("surname");
                        }
                        if (lr.has("born")) {
                            born = lr.getString("born");
                        }
                        if (lr.has("died")) {
                            died = lr.getString("died");
                        }
                        if (lr.has("bornCountry")) {
                            bornCountry = lr.getString("bornCountry");
                        }
                        if (lr.has("bornCountryCode")) {
                            bornCountryCode = lr.getString("bornCountryCode");
                        }
                        if (lr.has("bornCity")) {
                            bornCity = lr.getString("bornCity");
                        }

                        laureateList.add(new Laureate(firstName, lastName,
                                born, died, bornCountry, bornCountryCode, bornCity, prizes));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return laureateList;
        }

        @Override
        protected void onPostExecute(ArrayList<Laureate> laureates) {
            super.onPostExecute(laureates);

            DataStorage.saveLaureates(getApplicationContext(), laureates, country.getCountryName());

            Intent saveComplete = new Intent(String.valueOf(R.string.LAUREATE_SAVE_COMPLETE));
            sendBroadcast(saveComplete);

        }

    }

}
