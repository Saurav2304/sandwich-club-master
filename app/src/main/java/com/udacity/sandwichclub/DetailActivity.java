package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ImageView im;
    private TextView or_lb;
    private TextView or;
    private TextView ak_lb;
    private TextView ak;
    private TextView ingre;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        im = findViewById(R.id.image_iv);
        or_lb = findViewById(R.id.origin_label);
        or = findViewById(R.id.origin_tv);
        ak_lb = findViewById(R.id.also_known_label);
        ak = findViewById(R.id.also_known_tv);
        ingre = findViewById(R.id.ingredients_tv);
        desc = findViewById(R.id.description_tv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        setTitle(sandwich.getMainName());
        populateUI(sandwich);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(im);
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            or_lb.setVisibility(View.GONE);
            or.setVisibility(View.GONE);
        } else {
            or.setText(sandwich.getPlaceOfOrigin());
        }
        if (sandwich.getAlsoKnownAs().isEmpty()) {
            ak_lb.setVisibility(View.GONE);
            ak.setVisibility(View.GONE);
        } else {
            List<String> a = sandwich.getAlsoKnownAs();
            String a_str = TextUtils.join(",", a);
            ak.setText(a_str);
        }
        desc.setText(sandwich.getDescription());
        List<String> i = sandwich.getIngredients();
        String i_str = TextUtils.join(",", i);
        ingre.setText(i_str);
    }

}
