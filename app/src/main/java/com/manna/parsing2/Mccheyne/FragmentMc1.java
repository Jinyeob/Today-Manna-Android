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

public class FragmentMc1 extends Fragment {

    private TextView titleTextView;
    String allString="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mc1, container, false);

        titleTextView=v.findViewById(R.id.title);

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
//        FragmentMc1.JsoupAsyncTask_mcchain1 jsoupAsyncTask_mcchain1 = new FragmentMc1.JsoupAsyncTask_mcchain1();
//        jsoupAsyncTask_mcchain1.execute();

        return v;
    }
//
//    @SuppressLint("StaticFieldLeak")
//    private class JsoupAsyncTask_mcchain1 extends AsyncTask<Void, Void, Void> {
//
//        private ProgressDialog progressDialog;
//
//        List<Mccheyne> mList;
//        String allString="";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //프로세스 다이얼로그
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("불러오는 중...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//
//                Document doc = Jsoup.connect("http://www.bible4u.pe.kr/zbxe/read")
//                        .get();
//                mList = GetData(doc);
//
//                for(Mccheyne node : mList){
//                    allString+= (node.getTitle() + node.getPoint() + "\n");
//                    allString+=(node.getText()+"\n\n");
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            titleTextView.setText(allString);
//            progressDialog.dismiss();
//
//        }
//    }
}