package com.example.runiq;

import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link question_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class question_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private View fragmentQuestionLayout;
    private TextView textView;

    public question_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment question_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static question_fragment newInstance(String param1, String param2) {
        question_fragment fragment = new question_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentQuestionLayout = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //fragmentQuestionLayout = inflater.inflate(R.layout.question_fragment, container, false);
        //binding = FragmentFirstBinding.inflate(inflater, container, false);
        //showCountTextView = fragmentQuestionLayout.findViewById(R.id.putti);
        return fragmentQuestionLayout;
        //return inflater.inflate(R.layout.fragment_question_fragment, container, false);
    }
}