package com.josfloy.healthlife.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Jos on 2019/7/14 0014.
 */
public class BaseFragment extends Fragment {
    public LayoutInflater layoutInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater = LayoutInflater.from(getContext());
    }
}
