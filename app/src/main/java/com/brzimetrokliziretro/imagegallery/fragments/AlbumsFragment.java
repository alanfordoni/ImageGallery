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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.brzimetrokliziretro.imagegallery.MainActivity;
import com.brzimetrokliziretro.imagegallery.OnItemClickListener;
import com.brzimetrokliziretro.imagegallery.R;
import com.brzimetrokliziretro.imagegallery.adapters.AlbumsAdapter;
import com.brzimetrokliziretro.imagegallery.models.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumsFragment extends Fragment {

    private static final String BASE_URL = "jsonplaceholder.typicode.com";
    private static final String TAG = "TEST.albumsfrag";

    private Context context;
    private TextView empty_state;
    private ProgressBar pro_bar;
    private JsonArrayRequest jsonRequest;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;

    private boolean connection;
    private List<Album> albumsList;

    public AlbumsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        context = view.getContext();
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

        if (albumsList == null) {
            albumsList = new ArrayList<>();
        } else {
            albumsList.clear();
        }

        adapter = new AlbumsAdapter(albumsList);
        jsonRequest = new JsonArrayRequest(Request.Method.GET, url
                , null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "got response");
                pro_bar.setVisibility(View.GONE);

                JSONArray albumsArray = response;

                for (int i = 0; i < albumsArray.length(); i++) {
                    JSONObject album = albumsArray.optJSONObject(i);

                    int userId = album.optInt("userId");
                    int id = album.optInt("id");
                    String name = album.optString("title");
                    Album albumObject = new Album(name, userId, id);
                    albumsList.add(albumObject);
                }
                if (albumsList.size() < 1) {
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
            Log.d(TAG, "volley request started");
        }

        adapter.setOnItemClickListner(new OnItemClickListener() {
            @Override
            public void onItemClick(int albumId) {
                ((MainActivity)getActivity()).replaceImageFragment(albumId);
            }
        });

    }

    private String urlBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(BASE_URL)
                .appendPath("albums");

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}
