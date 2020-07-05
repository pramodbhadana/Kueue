package com.example.kueue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTrackSuccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTrackSuccessFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mTrack;
    private String mArtist;

    public AddTrackSuccessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mTrack Parameter 1.
     * @param mArtist Parameter 2.
     * @return A new instance of fragment AddTrackSuccessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTrackSuccessFragment newInstance(String mTrack, String mArtist) {
        AddTrackSuccessFragment fragment = new AddTrackSuccessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mTrack);
        args.putString(ARG_PARAM2, mArtist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrack = getArguments().getString(ARG_PARAM1);
            mArtist = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_track_success, container, false);
        TextView trackTextView = view.findViewById(R.id.textView_track);
        TextView artistTextView = view.findViewById(R.id.textView_artist);

        trackTextView.setText(mTrack);
        artistTextView.setText(mArtist);

        return view;
    }
}