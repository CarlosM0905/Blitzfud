package com.blitzfud.controllers.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blitzfud.R;
import com.blitzfud.models.ResponseAPI;
import com.google.gson.Gson;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;

public class BlitzfudUtils {

    private static final Gson gson = new Gson();

    public static AlertDialog initLoading(final Context context){
        return new SpotsDialog.Builder().setContext(context).setMessage("Espere un momento").build();
    }

    public static void showSuccessMessage(final Context context, final String message){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("¡BIEN!")
                .setContentText(message)
                .show();
    }

    public static void initToolbar(AppCompatActivity appCompatActivity, String title, boolean homeButton){
        Toolbar toolbar = appCompatActivity.findViewById(R.id.toolbar);

        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setTitle(title);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(homeButton);
    }

    public static void showError(Context context, ResponseBody responseBody){
        try {
            ResponseAPI error = gson.fromJson(responseBody.string(), ResponseAPI.class);
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("¡ERROR!")
                    .setContentText(error.getMessage())
                    .show();
        } catch (IOException e) {
            Toast.makeText(context, "Error al leer datos", Toast.LENGTH_LONG).show();
        }
    }

    public static void showFailure(Context context){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("No se pudo establecer conexión con el servidor")
                .show();
    }

    public static RecyclerView.LayoutManager getLayoutManager(Context context, int spanCount){
        int display_mode = context.getResources().getConfiguration().orientation;

        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            return new LinearLayoutManager(context);
        } else {
            return new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    public static RecyclerView.LayoutManager getStaggeredGrid(Context context, int spanCountPortrait, int spanCountLandscape){
        int display_mode = context.getResources().getConfiguration().orientation;

        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            return new StaggeredGridLayoutManager(spanCountPortrait, StaggeredGridLayoutManager.VERTICAL);
        } else {
            return new StaggeredGridLayoutManager(spanCountLandscape, StaggeredGridLayoutManager.VERTICAL);
        }
    }

}
