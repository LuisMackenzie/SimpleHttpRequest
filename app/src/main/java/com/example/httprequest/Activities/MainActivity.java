package com.example.httprequest.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.httprequest.API.API;
import com.example.httprequest.Models.City;
import com.example.httprequest.R;
import com.example.httprequest.API.WeatherService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText EditTextsearch;
    private TextView textViewcity;
    private TextView textViewDescription;
    private TextView textViewTemp;
    private ImageView img;
    private Button btn;

    private WeatherService service;
    private Call<City> cityCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instanciamos todos los objetos
        setUI();
        // Aquii creamos la instancia al servicio
        service = API.getApi().create(WeatherService.class);
        btn.setOnClickListener(this);




    }

    private void setUI() {
        EditTextsearch = findViewById(R.id.editTextSearch);
        textViewcity = findViewById(R.id.textViewCity);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewTemp = findViewById(R.id.textViewTemperature);
        img = findViewById(R.id.imageViewIcon);
        btn = findViewById(R.id.buttonSearch);
    }

    @Override
    public void onClick(View view) {
        // extraemos lo que hay en el campo de texto edittextsearch
        String city = EditTextsearch.getText().toString();
        if (city != "") {
            // Preparamos las request para hacer las llamadas
            cityCall = service.getCity(city, API.APIKEY, "metric", "es");

            // Aqui gestionamos la respuesta de la solicitud a la clase city
            cityCall.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                    // le llega algo a la clase city y lo guardamos en el objeto local city
                    City city = response.body();
                    setResult(city);
                }
                @Override
                public void onFailure(Call<City> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Ha habido algun tipo de error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setResult(City city) {
        textViewcity.setText(city.getName() + ", " + city.getCountry());
        textViewDescription.setText(city.getDescription());
        textViewTemp.setText(city.getTemperature() + "ºC");
        Picasso.get().load(API.BASE_ICONS + city.getIcon() + API.EXTENSION_ICONS).into(img);
    }

}