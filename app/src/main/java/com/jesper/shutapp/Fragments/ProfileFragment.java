package com.jesper.shutapp.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jesper.shutapp.Activities.MainSettings;
import com.jesper.shutapp.Activities.Settings;
import com.jesper.shutapp.R;
import com.jesper.shutapp.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Toolbar mToolbar;
    private ImageView profileImg;
    private TextView usernameText;
    private TextView bioText;


    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        init(view);
        setCurrentUser();



        return view;
    }

    private void init (View view){
        setHasOptionsMenu(true);
        mToolbar = view.findViewById(R.id.include_toolbar_xml);
        profileImg = view.findViewById(R.id.profile_image);
        usernameText = view.findViewById(R.id.user_name_profile_text);
        bioText = view.findViewById(R.id.bio_profile_text);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getActivity(), MainSettings.class);
        startActivity(intent);

        switch (item.getItemId()) {
            case R.id.settings:
                getActivity().startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Method for setting up the current user
    private void setCurrentUser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (isVisible()) {
                    User user = dataSnapshot.getValue(User.class);
                    usernameText.setText(user.getName());
                    bioText.setText(user.getBio());
                    Glide.with(getActivity()).load(user.getProfile_picture()).into(profileImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
