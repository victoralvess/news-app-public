package com.github.victoralvess.newz.activities.fragments;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.victoralvess.newz.R;
import com.github.victoralvess.newz.activities.adapters.NewsAdapter;
import com.github.victoralvess.newz.activities.helpers.InMemoryState;
import com.github.victoralvess.newz.adapters.helpers.NewZApiClient;
import com.github.victoralvess.newz.domain.entities.News;
import com.github.victoralvess.newz.domain.port.NewsService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarksFragment extends Fragment {
    public final String BOOKMARKS = "bookmarks";

    private RecyclerView rv;

    private List<News> news = new ArrayList<>();

    private String uid = "testuser";

    public BookmarksFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = Settings.Secure.getString(this.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        news = new ArrayList<>();

        if (savedInstanceState != null) {
            news = (ArrayList<News>) savedInstanceState.getSerializable(BOOKMARKS);
        } else {
            Bundle bundle = InMemoryState.getInstance().getState(R.id.bookmarksFragment);
            if (bundle != null) {
                news = (ArrayList<News>) bundle.getSerializable(BOOKMARKS);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        rv = view.findViewById(R.id.bookmarks_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(new NewsAdapter(news, BookmarksFragment.this.getContext()));

        NewsService service = NewZApiClient.getInstance().getRetrofit().create(NewsService.class);
        service.listBookmarks(uid).enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    List<News> news = response.body();
                    for (News n : news) {
                        n.bookmarked = true;
                    }
                    rv.setAdapter(new NewsAdapter(news, BookmarksFragment.this.getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {

            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BOOKMARKS, (ArrayList) ((NewsAdapter) rv.getAdapter()).news);
    }
}