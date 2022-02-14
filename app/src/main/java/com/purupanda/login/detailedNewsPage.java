package com.purupanda.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.purupanda.login.databinding.ActivityDetailedNewsPageBinding;
import com.squareup.picasso.Picasso;

public class detailedNewsPage extends AppCompatActivity {

    private String newsTitle,newsImage,newsUrl,description,source;
    private ActivityDetailedNewsPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedNewsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.newsTitle = getIntent().getStringExtra("newsTitle");
        newsImage = getIntent().getStringExtra("newsImage");
        newsUrl = getIntent().getStringExtra("newsUrl");
        description = getIntent().getStringExtra("description");
        source = getIntent().getStringExtra("source");
        binding.detailedNewsTitle.setText(newsTitle);
        binding.detailedNewsDescription.setText(description);
        binding.detailedNewsSource.setText(source);
        Picasso.get().load(newsImage).into(binding.detailedNewsImage);
        binding.readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(newsUrl);
                Intent in = new Intent(Intent.ACTION_VIEW,webpage);
//                in.setData(Uri.parse(newsUrl));
                startActivity(in);
//                Intent chooser = Intent.createChooser(in,null);
//                startActivity(chooser);
            }
        });
    }
}