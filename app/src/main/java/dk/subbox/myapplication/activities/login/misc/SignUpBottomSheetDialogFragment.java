package dk.subbox.myapplication.activities.login.misc;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.subbox.myapplication.R;

public class SignUpBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public SignUpBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottomsheet_sign_up, container, false);
    }

}
