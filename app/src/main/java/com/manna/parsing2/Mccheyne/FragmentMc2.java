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
import java.util.Objects;

import static com.manna.parsing2.activity.MainActivity.AllList;
import static com.manna.parsing2.activity.MainActivity.mcString;

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