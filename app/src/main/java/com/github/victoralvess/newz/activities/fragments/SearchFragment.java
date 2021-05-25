package com.github.victoralvess.newz.activities.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.victoralvess.newz.R;
import com.github.victoralvess.newz.activities.MainActivity;
import com.github.victoralvess.newz.activities.adapters.NewsAdapter;
import com.github.victoralvess.newz.activities.helpers.InMemoryState;
import com.github.victoralvess.newz.adapters.NewsApiRepository;
import com.github.victoralvess.newz.adapters.SharedPreferencesUserRepository;
import com.github.victoralvess.newz.domain.entities.News;
import com.github.victoralvess.newz.domain.port.NewsCallback;
import com.github.victoralvess.newz.usecases.ManageUserPreferenceUseCase;
import com.github.victoralvess.newz.usecases.SearchNewsUseCase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public final String NEWS = "news";

    private SearchNewsUseCase searchNewsUseCase;
    private ManageUserPreferenceUseCase manageUserPreferenceUseCase;

    private RecyclerView rv;
    private final NewsCallback newsCallback = news -> {
        NewsAdapter adapter = new NewsAdapter(news, SearchFragment.this.getContext());
        rv.setAdapter(adapter);

        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS, (ArrayList) adapter.news);
        InMemoryState.getInstance().setState(R.id.searchFragment, bundle);

        for (News n : news) {
            Log.i("NEWS_API", n.getTitle() + " " + n.getUrl());
        }

        if (news.isEmpty()) {
            Log.i("NEWS_API", "[EMPTY]");
        }
    };

    private TextView tvSearch;
    private TextView tvCurrentTerm;
    private Button savePreferenceButton;
    private FloatingActionButton cancelButton;

    private List<News> news = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        news = new ArrayList<>();

        if (savedInstanceState != null) {
            news = (ArrayList<News>) savedInstanceState.getSerializable(NEWS);
        } else {
            Bundle bundle = InMemoryState.getInstance().getState(R.id.searchFragment);
            if (bundle != null) {
                news = (ArrayList<News>) bundle.getSerializable(NEWS);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchNewsUseCase = new SearchNewsUseCase(new NewsApiRepository());

        manageUserPreferenceUseCase = new ManageUserPreferenceUseCase(
                new SharedPreferencesUserRepository(
                        this.getActivity().getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE),
                        getString(R.string.subject_key)
                )
        );

        rv = view.findViewById(R.id.newsRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager( this.getContext()));
        rv.setAdapter(new NewsAdapter(news, SearchFragment.this.getContext()));

        tvCurrentTerm = view.findViewById(R.id.current_term_text_view);
        setCurrentTerm();

        tvSearch = view.findViewById(R.id.search);
        tvSearch.setOnEditorActionListener((tv, id, event) -> {
            boolean handled = false;

            if (id == EditorInfo.IME_ACTION_SEARCH) {
                searchNewsUseCase.findByQuery(tv.getText().toString(), newsCallback);
                handled = true;
            }

            return handled;
        });

        tvSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Editable text = ((EditText) v).getText();
                if (text.length() > 0) {
                    text.replace(0, 1, text.subSequence(0, 1), 0, 1);
                    ((EditText) v).selectAll();
                }
            }
        });

        cancelButton = view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> {
            manageUserPreferenceUseCase.setSubject(null);
            SearchFragment.this.setCurrentTerm();
            savePreferenceButton.setVisibility(View.VISIBLE);
            cancelButton.hide();
        });

        savePreferenceButton = view.findViewById(R.id.button);
        savePreferenceButton.setOnClickListener(v -> {
            String q = tvSearch.getText().toString().trim();
            if (q.isEmpty())
                q = null;

            manageUserPreferenceUseCase.setSubject(q);
            SearchFragment.this.setCurrentTerm();
            if (q == null) {
                savePreferenceButton.setVisibility(View.VISIBLE);
                cancelButton.hide();
            } else {
                searchNewsUseCase.findByQuery(manageUserPreferenceUseCase.getSubject(), newsCallback);

                savePreferenceButton.setVisibility(View.GONE);
                cancelButton.show();
            }
        });

        if (manageUserPreferenceUseCase.getSubject() != null) {
            tvSearch.setText(manageUserPreferenceUseCase.getSubject());
            searchNewsUseCase.findByQuery(manageUserPreferenceUseCase.getSubject(), newsCallback);

            savePreferenceButton.setVisibility(View.GONE);
            cancelButton.show();
        } else {
            searchNewsUseCase.findByCountry("br", newsCallback);

            savePreferenceButton.setVisibility(View.VISIBLE);
            cancelButton.hide();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(NEWS, (ArrayList) ((NewsAdapter) rv.getAdapter()).news);
    }

    private void setCurrentTerm() {
        tvCurrentTerm.setText(manageUserPreferenceUseCase.getSubject() == null ? "" : "Termo atual: " + manageUserPreferenceUseCase.getSubject());
    }
}