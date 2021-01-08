package com.manna.parsing2.Mccheyne.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manna.parsing2.R;

import static com.manna.parsing2.activity.MainActivity.mcString;

/***
 * Create by Jinyeob
 */
public class FragmentMc2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mc2, container, false);

        TextView titleTextView = v.findViewById(R.id.title);
        titleTextView.setText(mcString[1]);

        return v;
    }
}