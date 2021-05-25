package com.github.victoralvess.newz.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.victoralvess.newz.R;
import com.github.victoralvess.newz.adapters.helpers.NewZApiClient;
import com.github.victoralvess.newz.domain.CheckData;
import com.github.victoralvess.newz.domain.entities.News;
import com.github.victoralvess.newz.domain.port.NewsService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public final List<News> news;
    private final Context context;
    private String uid = "testuser";

    public NewsAdapter(List<News> news, Context context) {
        this.news = news;
        this.context = context;
        uid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        News news = this.news.get(i);

        NewsService service = NewZApiClient.getInstance().getRetrofit().create(NewsService.class);

        service.isBookmarked(uid, new CheckData(news.getUrl())).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                news.bookmarked = response.isSuccessful();

                if (news.bookmarked) {
                    viewHolder.getBookmarkButton().setVisibility(View.INVISIBLE);
                    viewHolder.getUnbookmarkButton().setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getBookmarkButton().setVisibility(View.VISIBLE);
                    viewHolder.getUnbookmarkButton().setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                news.bookmarked = false;

                viewHolder.getBookmarkButton().setVisibility(View.INVISIBLE);
                viewHolder.getUnbookmarkButton().setVisibility(View.INVISIBLE);
            }
        });

        if (!(news.getPictureUrl() == null || news.getPictureUrl().isEmpty())) {
            Picasso.get().load(news.getPictureUrl()).into(viewHolder.getImageView());
        }
        viewHolder.getTitleTextView().setText(news.getTitle());
        viewHolder.getSnippetTextView().setText(news.getSnippet());
        viewHolder.getCreditsTextView().setText(news.getSource().getName());

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(news.getPublishedAt());
        } catch (ParseException e) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(news.getPublishedAt());
            } catch (ParseException e2) {

            }
        }

        if (date == null) {
            viewHolder.getPublishedAtTextView().setText("Data inválida");
        } else {
            viewHolder.getPublishedAtTextView().setText(
                    new SimpleDateFormat("dd/MM/yyyy' às 'HH:mm")
                            .format(date)
            );
        }

        viewHolder.getBookmarkButton().setOnClickListener(view -> {
            service.createBookmark(uid, news).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.isSuccessful()) {
                        viewHolder.getBookmarkButton().setVisibility(View.INVISIBLE);
                        viewHolder.getUnbookmarkButton().setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
        });

        viewHolder.getUnbookmarkButton().setOnClickListener(view -> {
            service.deleteBookmark(uid, new CheckData(news.getUrl())).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        viewHolder.getBookmarkButton().setVisibility(View.VISIBLE);
                        viewHolder.getUnbookmarkButton().setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        });

        viewHolder.getOpenButton().setOnClickListener(view -> {
            Uri newsPage = Uri.parse(news.getUrl());

            Intent intent = new Intent(Intent.ACTION_VIEW, newsPage);
            if (intent.resolveActivity(NewsAdapter.this.context.getPackageManager()) != null) {
                NewsAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (news == null) return 0;
        return news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView titleTextView;
        private final TextView snippetTextView;
        private final TextView creditsTextView;
        private final TextView publishedAtTextView;
        private final Button bookmarkButton;
        private final Button unbookmarkButton;
        private final Button openButton;


        public ViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.imageView);
            titleTextView = view.findViewById(R.id.titleTextView);
            snippetTextView = view.findViewById(R.id.snippetTextView);
            creditsTextView = view.findViewById(R.id.creditsTextView);
            publishedAtTextView = view.findViewById(R.id.published_at_text_view);
            bookmarkButton = view.findViewById(R.id.bookmark_button);
            unbookmarkButton = view.findViewById(R.id.unbookmark_button);
            openButton = view.findViewById(R.id.open_button);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getSnippetTextView() {
            return snippetTextView;
        }

        public TextView getCreditsTextView() {
            return creditsTextView;
        }

        public TextView getPublishedAtTextView() {
            return publishedAtTextView;
        }

        public Button getBookmarkButton() {
            return bookmarkButton;
        }

        public Button getUnbookmarkButton() {
            return unbookmarkButton;
        }

        public Button getOpenButton() {
            return openButton;
        }
    }
}
