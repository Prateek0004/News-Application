package com.example.newsapplicaton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//bd87e69fdd2f4e68bebf3d48a5cf7b6e

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;

    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRV=findViewById(R.id.idRVNews);
        categoryRV=findViewById(R.id.idRVCategories);
        loadingPB=findViewById(R.id.idPBLoading);
        articlesArrayList=new ArrayList<>();
        categoryRVModelArrayList=new ArrayList<>();
        newsRVAdapter =new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModelArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }

    private void getCategories(){
        categoryRVModelArrayList.add(new CategoryRVModel("All","https://media.istockphoto.com/id/1477858506/photo/news-online-in-phone-reading-newspaper-from-website-digital-publication-and-magazine-mockup.webp?b=1&s=170667a&w=0&k=20&c=G9EGbIPr1D59b_dSdrFYKXt5pC402U_NAWFefHjk-BM="));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology","https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dGVjaG5vbG9neXxlbnwwfHwwfHx8MA%3D%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science","https://media.istockphoto.com/id/1399246824/photo/digital-eye-wave-lines-stock-background.webp?b=1&s=170667a&w=0&k=20&c=k7LI06N_W3AKrcB17bN4wF-Dc29FZUmrg9Sicvxsg6E="));
        categoryRVModelArrayList.add(new CategoryRVModel("General","https://media.istockphoto.com/id/1414065914/photo/teen-redhead-girl-wearing-headphones-using-smartphone-in-big-city.webp?b=1&s=170667a&w=0&k=20&c=bFlKEQGm-l626BZ0GbK24V1jpj8Xb8WNZHStCeXHf_g="));
        categoryRVModelArrayList.add(new CategoryRVModel("Business","https://media.istockphoto.com/id/180823791/photo/businees-team-having-a-meeting.webp?b=1&s=170667a&w=0&k=20&c=-XKNUM1YXHnJ8QSHRzU1KF4vTJXYpyaCoRdLQmtlefs=\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment","https://media.istockphoto.com/id/1163326338/photo/young-couple-dancing-at-music-festival.webp?b=1&s=170667a&w=0&k=20&c=mGNg4tI7LyZXw00T88aXQYpIYqCdP9P5oh-d9qvveTw=\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health","https://images.unsplash.com/photo-1506126613408-eca07ce68773?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8aGVhbHRofGVufDB8fDB8fHww&auto=format&fit=crop&w=500&q=60\n"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports","https://media.istockphoto.com/id/509616398/photo/male-pole-vaulting-athlete.webp?b=1&s=170667a&w=0&k=20&c=pmpSYF1KOghjisQ4_c25WZ8nQT7qRZ0dWh3pD7VQbmM="));
        categoryRVAdapter.notifyDataSetChanged();

    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apikey=bd87e69fdd2f4e68bebf3d48a5cf7b6e";
        String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=published&language=en&apikey=bd87e69fdd2f4e68bebf3d48a5cf7b6e";
        String BASE_URL= "https://newsapi.org/";
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI= retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if (category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {

                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles();
                for (int i=0;i<articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Fail to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();

        getNews(category);

    }
}