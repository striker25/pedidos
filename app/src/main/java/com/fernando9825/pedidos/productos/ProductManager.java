package com.fernando9825.pedidos.productos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fernando9825.pedidos.SQLite.SQLiteProducts;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductManager extends AppCompatActivity {

    // list of products
    public List<Product> productList;
    private Context context;

    // constructor
    public ProductManager(Context context) {
        this.context = context;
    }


    // get products
    public void loadProductsToLocalDB() {
        /*
         * Crear  un String Request
         * El tipo de peticion es GET definido como primer parametro
         * La URL  es definida como primer parametro
         * Entonces tenemos  un Response Listener y un Error Listener
         * En el response listener obtenemos el  JSON como un String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Product.URL_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            productList = new ArrayList<>();
                            List<Product> products;
                            Gson gson = new Gson();

                            //convertir  el string a json array object
                            JSONArray array = new JSONArray(response);
                            products = Arrays.asList(gson.fromJson(response, Product[].class));
                            productList = products;

                            SQLiteProducts sqLiteProducts = new SQLiteProducts(context, SQLiteProducts.PRODUCTS, null, 1);
                            //establece el metod para hacer que podamos escribir sobre la bd creada
                            SQLiteDatabase bd = sqLiteProducts.getWritableDatabase();

                            ContentValues registro = new ContentValues();

                            for (int i = 0; i < products.size(); i++) {
                                registro.put("descripcion", products.get(i).getDescripcion());
                                registro.put("barcode", products.get(i).getBarcode());
                                registro.put("precio", products.get(i).getPrecio());
                                registro.put("image", products.get(i).getImage());
                                bd.insert(SQLiteProducts.PRODUCTS, null, registro);

                                registro.clear();
                            }

                            bd.close();

                            //crear el  adaptador y asignarlo al  recyclerview
                            //ProductsAdapter adapter = new ProductsAdapter(MainActivity.this, SQLiteClients.getLocalProducts());
                            //recyclerView.setAdapter(adapter);

                            Toast.makeText(context, "Se han descargado los datos del servidor", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "No se ha podido conectar ", Toast.LENGTH_SHORT).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(context).add(stringRequest);


        /*RequestTickle mRequestTickle = VolleyTickle.newRequestTickle(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Product.URL_PRODUCT,
                null, null);
        mRequestTickle.add(stringRequest);
        NetworkResponse response = mRequestTickle.start();

        if (response.statusCode == 200) {
            String data = VolleyTickle.parseResponse(response);


            productList = new ArrayList<>();
            List<Product> products;
            Gson gson = new Gson();

            products = Arrays.asList(gson.fromJson(data, Product[].class));
            productList = products;

            SQLiteProducts sqLiteProducts = new SQLiteProducts(context, SQLiteProducts.PRODUCTS, null, 1);
            //establece el metod para hacer que podamos escribir sobre la bd creada
            SQLiteDatabase bd = sqLiteProducts.getWritableDatabase();

            ContentValues registro = new ContentValues();

            for (int i = 0; i < products.size(); i++) {
                registro.put("descripcion", products.get(i).getDescripcion());
                registro.put("barcode", products.get(i).getBarcode());
                registro.put("precio", products.get(i).getPrecio());
                registro.put("image", products.get(i).getImage());
                bd.insert(SQLiteProducts.PRODUCTS, null, registro);

                registro.clear();
            }

            bd.close();

            //crear el  adaptador y asignarlo al  recyclerview
            //ProductsAdapter adapter = new ProductsAdapter(MainActivity.this, SQLiteClients.getLocalProducts());
            //recyclerView.setAdapter(adapter);

            Toast.makeText(context, "Se han descargado los datos del servidor", Toast.LENGTH_LONG).show();

        } else {
            productList = null;
        }*/


    }


}