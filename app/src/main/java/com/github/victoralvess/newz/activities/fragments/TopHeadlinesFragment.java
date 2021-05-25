package com.github.victoralvess.newz.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.victoralvess.newz.R;
import com.github.victoralvess.newz.activities.adapters.NewsAdapter;
import com.github.victoralvess.newz.activities.helpers.InMemoryState;
import com.github.victoralvess.newz.adapters.NewsApiRepository;
import com.github.victoralvess.newz.domain.entities.News;
import com.github.victoralvess.newz.domain.port.NewsCallback;
import com.github.victoralvess.newz.usecases.SearchNewsUseCase;

import java.util.ArrayList;
import java.util.List;

public class TopHeadlinesFragment extends Fragment {
    public final String TOP_HEADLINES = "top_headlines";

    private SearchNewsUseCase searchNewsUseCase;

    private RecyclerView rv;
    private final NewsCallback newsCallback = news -> {
        NewsAdapter adapter = new NewsAdapter(news, TopHeadlinesFragment.this.getContext());
        rv.setAdapter(adapter);

        Bundle bundle = new Bundle();
        bundle.putSerializable(TOP_HEADLINES, (ArrayList) adapter.news);
        InMemoryState.getInstance().setState(R.id.topHeadlinesFragment, bundle);

        for (News n : news) {
            Log.i("NEWS_API", n.getTitle() + " " + n.getUrl());
        }

        if (news.isEmpty()) {
            Log.i("NEWS_API", "[EMPTY]");
        }
    };

    private List<News> news = new ArrayList<>();

    public TopHeadlinesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        news = new ArrayList<>();

        if (savedInstanceState != null) {
            news = (ArrayList<News>) savedInstanceState.getSerializable(TOP_HEADLINES);
        } else {
            Bundle bundle = InMemoryState.getInstance().getState(R.id.topHeadlinesFragment);
            if (bundle != null) {
                news = (ArrayList<News>) bundle.getSerializable(TOP_HEADLINES);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_headlines, container, false);
        searchNewsUseCase = new SearchNewsUseCase(new NewsApiRepository());

        rv = view.findViewById(R.id.top_news_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setAdapter(new NewsAdapter(news, TopHeadlinesFragment.this.getContext()));

        searchNewsUseCase.findByCountry("br", newsCallback);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TOP_HEADLINES, (ArrayList) ((NewsAdapter) rv.getAdapter()).news);
    }
}