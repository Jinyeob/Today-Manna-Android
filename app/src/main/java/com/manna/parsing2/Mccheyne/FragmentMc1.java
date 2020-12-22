package com.manna.parsing2.Mccheyne;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manna.parsing2.Model.Mccheyne;
import com.manna.parsing2.R;
import java.util.List;

import static com.manna.parsing2.activity.MainActivity.AllList;
import static com.manna.parsing2.activity.MainActivity.mcString;

public class FragmentMc1 extends Fragment {

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        titleTextView = getView().findViewById(R.id.title);
//
//        int length=AllList.get(0).size();
//        for(int i=0;i<length;i++){
//            AllList.get(0).get(i).getText();
//        }
//        List<Mccheyne> thisList=AllList.get(0);
//        for(Mccheyne node : thisList) {
//            allString += (node.getTitle() + node.getPoint() + "\n");
//            allString += (node.getText() + "\n\n");
//        }
//        titleTextView.setText(allString);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mc1, container, false);
        TextView titleTextView = v.findViewById(R.id.title);

        titleTextView.setText(mcString[0]);

        return v;
    }
}