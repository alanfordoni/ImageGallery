package com.brzimetrokliziretro.imagegallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.brzimetrokliziretro.imagegallery.fragments.AlbumsFragment;
import com.brzimetrokliziretro.imagegallery.fragments.ImagesFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.isEmpty()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (ft.isEmpty()) {
                ft.add(R.id.frag_container, new AlbumsFragment(), "albums")
                        .commit();
            }
        }
    }

    public void replaceImageFragment(int albumId){
        ImagesFragment fragment = ImagesFragment.newInstance(albumId);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, fragment
                , "images").commit();
    }

    public void openLink(String link){
        Uri url = Uri.parse(link);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
        startActivity(browserIntent);
    }

    @Override
    public void onBackPressed() {

        if((getSupportFragmentManager().findFragmentByTag("images") instanceof ImagesFragment))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container
                    , new AlbumsFragment(), "albums").commit();
        }else {
            super.onBackPressed();
        }
    }
}
