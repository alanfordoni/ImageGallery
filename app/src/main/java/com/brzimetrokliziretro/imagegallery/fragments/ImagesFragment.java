package com.brzimetrokliziretro.imagegallery.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.brzimetrokliziretro.imagegallery.MainActivity;
import com.brzimetrokliziretro.imagegallery.OnImageItemClickListener;
import com.brzimetrokliziretro.imagegallery.R;
import com.brzimetrokliziretro.imagegallery.adapters.ImagesAdapter;
import com.brzimetrokliziretro.imagegallery.models.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImagesFragment extends Fragment {
    public static final String EXTRA_ALBUM_ID = "album_id";
    private static final String BASE_URL = "jsonplaceholder.typicode.com";
    private static final String TAG = "TEST.imagesfrag";

    private TextView empty_state;
    private ProgressBar pro_bar;
    private JsonArrayRequest jsonRequest;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private ImagesAdapter adapter;

    private boolean connection;
    private List<Image> imagesList;
    private int albumNumber;


    public ImagesFragment() {
    }

    public static ImagesFragment newInstance(int albumId) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_ALBUM_ID, albumId);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        albumNumber = getArguments().getInt(EXTRA_ALBUM_ID, 1);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        empty_state = (TextView) view.findViewById(R.id.tv_empty_state);
        pro_bar = (ProgressBar) view.findViewById(R.id.loading_spinner);
        requestQueue = Volley.newRequestQueue(view.getContext());

        checkConnection(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        String url = urlBuilder();

        if (imagesList == null) {
            imagesList = new ArrayList<>();
        } else {
            imagesList.clear();
        }

        adapter = new ImagesAdapter(imagesList);

        jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pro_bar.setVisibility(View.GONE);

                JSONArray imagesArray = response;

                for (int i = 0; i < imagesArray.length(); i++) {
                    JSONObject image = imagesArray.optJSONObject(i);

                    String name = image.optString("title");
                    String url = image.optString("url");
                    String image_url = image.optString("thumbnailUrl");
                    Image imageObject = new Image(image_url, name, url);
                    imagesList.add(imageObject);
                }

                if (imagesList.size() < 1) {
                    empty_state.setText(R.string.no_data);
                }
                populateRV();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onError problem: " + error.getMessage());
            }
        });

        if (connection) {
            requestQueue.add(jsonRequest);
        }

        adapter.setOnImageItemClickListener(new OnImageItemClickListener() {
            @Override
            public void onImageItemClick(String url) {
                ((MainActivity) getActivity()).openLink(url);
            }
        });
    }

    private String urlBuilder() {
        String albumId = String.valueOf(albumNumber);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(BASE_URL)
                .appendPath("photos")
                .appendQueryParameter("albumId", albumId);

        return builder.toString();
    }

    private void checkConnection(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo n_info = connMgr.getActiveNetworkInfo();
        if (n_info != null && n_info.isConnected()) {
            connection = true;
        } else {
            pro_bar.setVisibility(View.GONE);
            empty_state.setText(R.string.no_connection);
        }
    }

    private void populateRV() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

}
