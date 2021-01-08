package com.manna.parsing2.Manna;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manna.parsing2.Model.MannaData;
import com.manna.parsing2.R;

import static android.content.Context.CLIPBOARD_SERVICE;

/***
 * Create by Jinyeob
 */

public class NewMannaFragment extends Fragment {

    private TextView _mannaTextView;
    private FloatingActionButton shareButton;

    private RequestQueue queue;
    private String allString = "";

    public NewMannaFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manna, container, false);

        _mannaTextView = v.findViewById(R.id.textView);
        shareButton = v.findViewById(R.id.button_share);

        queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        getManna();

        return v;
    }

    private void getManna() {
        final String url = "http://3.138.184.130:9179/api/v1/today-manna/";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("@@@@" + response);
                        try {
                            MannaData mannaData = new MannaData();

                            String date = response.getString("date");
                            mannaData.setDate(date);

                            String verse = response.getString("verse");
                            mannaData.setVerse(verse);

                            JSONArray contentArray = response.getJSONArray("contents");
                            StringBuilder contents = new StringBuilder();
                            for (int i = 0; i < contentArray.length(); i++) {
                                String content = contentArray.getString(i);
                                contents.append(content).append("\n\n");
                            }
                            mannaData.setContent(contents.toString());

                            allString += date + "\n\n" + verse + "\n\n" + contents.toString();

                            _mannaTextView.setText(allString);

                            //공유 버튼 리스너
                            shareButton.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clipData = ClipData.newPlainText("TEXT", allString);
                                    clipboardManager.setPrimaryClip(clipData);

                                    Intent intent = new Intent(Intent.ACTION_SEND);
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
                Log.e("ErrorTAG", error.toString());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}