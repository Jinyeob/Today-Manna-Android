package com.manna.parsing2.Mccheyne;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manna.parsing2.Model.Mccheyne;
import com.manna.parsing2.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import static com.manna.parsing2.Mccheyne.MccheyneFragment.AllList;
import static com.manna.parsing2.Mccheyne.MccheyneFragment.GetData;

public class FragmentMc4 extends Fragment {

    private TextView titleTextView;
    String allString="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mc4, container, false);

        titleTextView=v.findViewById(R.id.title);

//        FragmentMc4.JsoupAsyncTask_mcchain4 jsoupAsyncTask_mcchain4 = new FragmentMc4.JsoupAsyncTask_mcchain4();
//        jsoupAsyncTask_mcchain4.execute();

        int length=AllList.get(0).size();
        for(int i=0;i<length;i++){
            AllList.get(0).get(i).getText();
        }
        List<Mccheyne> thisList=AllList.get(0);
        for(Mccheyne node : thisList){
            allString+= (node.getTitle() + node.getPoint() + "\n");
            allString+=(node.getText()+"\n\n");
        }
        titleTextView.setText(allString);
        // Inflate the layout for this fragment
        return v;
    }

    @SuppressLint("StaticFieldLeak")
    private class JsoupAsyncTask_mcchain4 extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        List<Mccheyne> mList;
        String allString="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect("http://bible4u.pe.kr/zbxe/?mid=open_read&ver=korean_krv&b_num=4")
                        .get();
                List<Mccheyne> mList = GetData(doc);

                for(Mccheyne node : mList){
                    allString+= (node.getTitle() + node.getPoint() + "\n");
                    allString+=(node.getText()+"\n\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            titleTextView.setText(allString);
        }
    }
}