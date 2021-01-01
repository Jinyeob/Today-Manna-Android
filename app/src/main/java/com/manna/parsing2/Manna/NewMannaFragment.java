package com.manna.parsing2.Manna;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.Cache;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manna.parsing2.Model.MannaData;
import com.manna.parsing2.R;

public class NewMannaFragment extends Fragment {

    private TextView _mannaTextView;
    RequestQueue queue;

    public NewMannaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manna, container, false);

        _mannaTextView = v.findViewById(R.id.textView);

        queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        getManna();

        // Inflate the layout for this fragment
        return v;
    }

    public void getManna() {
        String url = "http://3.137.156.185:3000/api/v1/today-manna";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("@@@@"+response);
                        try {
                            MannaData mannaData = new MannaData();
                            String verse= response.getString("verse");
                            mannaData.setVerse(verse);

                            JSONArray contentArray = response.getJSONArray("contents");
                            StringBuilder contents= new StringBuilder();
                            for(int i=0;i<contentArray.length();i++){
                                String content = contentArray.getString(i);
                                contents.append(content).append("\n\n");
                            }
                            mannaData.setContent(contents.toString());

                            _mannaTextView.setText(verse+"\n\n"+contents.toString()+"\n\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorTAG",error.toString());
                System.out.println(error);

            }
        });

        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}