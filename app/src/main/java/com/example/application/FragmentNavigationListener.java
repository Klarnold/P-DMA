package com.example.application;

import androidx.fragment.app.Fragment;

public interface FragmentNavigationListener {
    void navigateToFragment(Fragment fragment, boolean addToBackStack);
}
