package com.manna.parsing2.Manna;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manna.parsing2.Model.MannaData;
import com.manna.parsing2.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class NewMannaFragment extends Fragment {

    private TextView _mannaTextView;
    RequestQueue queue;
    private FloatingActionButton shareButton;
    String allString="";
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
        shareButton=v.findViewById(R.id.button_share);

        queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        getManna();

        // Inflate the layout for this fragment
        return v;
    }

    public void getManna() {
        String url = "http://3.137.156.185:9179/api/v1/today-manna/2021-01-02";

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("@@@@"+response);
                        try {
                            //MannaData mannaData = new MannaData();

                            String date=response.getString("date");
                            //mannaData.setDate(date);

                            String verse= response.getString("verse");
                            //mannaData.setVerse(verse);

                            JSONArray contentArray = response.getJSONArray("contents");
                            StringBuilder contents= new StringBuilder();
                            for(int i=0;i<contentArray.length();i++){
                                String content = contentArray.getString(i);
                                contents.append(content).append("\n\n");
                            }
                            //mannaData.setContent(contents.toString());

                            allString+=date+"\n\n"+verse+"\n\n"+contents.toString();

                            _mannaTextView.setText(allString);

                            //공유 버튼 리스너
                            shareButton.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clipData = ClipData.newPlainText("TEXT", allString);
                                    clipboardManager.setPrimaryClip(clipData);

                                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_TEXT, allString);
                                    Intent chooser = Intent.createChooser(intent, "공유하기");
                                    startActivity(chooser);
                                }
                            });

                            shareButton.show();

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