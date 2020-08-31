package com.blitzfud.views.pages.order;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.providers.PurchasesProvider;
import com.blitzfud.controllers.restapi.services.PurchasesService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.databinding.ActivityHistoryOrdersBinding;
import com.blitzfud.models.order.Order;
import com.blitzfud.models.responseAPI.PurchasesSet;
import com.blitzfud.views.adapters.order.OrderAdapter;
import com.blitzfud.views.adapters.order.PurchaseOrdersAdapter;
import com.google.gson.JsonSyntaxException;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.SDF_DATE;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.SDF_DATE_PURCHASE;

public class HistoryOrdersActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean loadedFromAPI = false;

    private ActivityHistoryOrdersBinding binding;
    private Realm realm;
    private PurchasesSet purchasesSet;

    private final Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePickerDialogFrom;
    private DatePickerDialog datePickerDialogTo;

    private String dateFrom;
    private String dateTo;

    private boolean realoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryOrdersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initConfig();
        initDatePicker();
        loadData();
        bindListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.layoutFrom:
                datePickerDialogFrom.show();
                break;
            case R.id.layoutTo:
                datePickerDialogTo.show();
                break;
            default: break;
        }
    }

    private void initConfig() {
        realm = Realm.getDefaultInstance();
        binding.txtFrom.setText(SDF_DATE.format(new Date()));
        binding.txtTo.setText(SDF_DATE.format(new Date()));
        dateFrom = SDF_DATE_PURCHASE.format(new Date());
        dateTo = SDF_DATE_PURCHASE.format(new Date());
    }

    private void initDatePicker() {
        datePickerDialogFrom = new DatePickerDialog(HistoryOrdersActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fromDay = String.format("%02d", dayOfMonth);
                        String fromMonth = String.format("%02d", month + 1);
                        String fromYear = String.valueOf(year);
                        binding.txtFrom.setText(fromDay + "/" + fromMonth + "/" + fromYear);
                        dateFrom = fromMonth + "-" + fromDay + "-" + fromYear;

                        if (!realoading) reloadData();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialogTo = new DatePickerDialog(HistoryOrdersActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String toDay = String.format("%02d", dayOfMonth);
                        String toMonth = String.format("%02d", month + 1);
                        String toYear = String.valueOf(year);
                        binding.txtTo.setText(toDay + "/" + toMonth + "/" + toYear);
                        dateTo = toMonth + "-" + toDay + "-" + toYear;
                        if (!realoading) reloadData();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void loadData() {
        if (!loadedFromAPI) {
            PurchasesService.getToday().enqueue(new Callback<PurchasesSet>() {
                @Override
                public void onResponse(Call<PurchasesSet> call, Response<PurchasesSet> response) {
                    if (response.isSuccessful()) {
                        PurchasesProvider.save(realm, response.body());
                        purchasesSet = PurchasesProvider.getPurchases(realm);
                        loadedFromAPI = true;

                        showPurchases();
                    } else {
                        try {
                            BlitzfudUtils.showError(HistoryOrdersActivity.this, response.errorBody());
                        } catch (JsonSyntaxException ex) {
                            loadLocalPurchases();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PurchasesSet> call, Throwable t) {
                    loadLocalPurchases();
                }
            });
        } else {
            purchasesSet = PurchasesProvider.getPurchases(realm);
            showPurchases();
        }
    }

    private void showPurchases() {
        if (binding == null) return;

        binding.pantallaLoading.setVisibility(View.GONE);

         final PurchaseOrdersAdapter adapter = new PurchaseOrdersAdapter(HistoryOrdersActivity.this, purchasesSet.getPurchaseOrders(), new OrderAdapter.OnOrderClickListener() {
            @Override
            public void onItemClick(String createdAtDate, Order order, int position) {
                final Intent intent = new Intent(HistoryOrdersActivity.this, DetailPendingOrderActivity.class);
                intent.putExtra("orderId", order.get_id());
                intent.putExtra("createdAtDate", createdAtDate);
                startActivity(intent);
            }
        });
        binding.recyclerPurchase.setAdapter(adapter);

        binding.pantallaPrincipal.setVisibility(View.VISIBLE);

        if (purchasesSet.getPurchaseOrders().isEmpty()) {
            binding.layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerPurchase.setVisibility(View.VISIBLE);
        }
    }

    private void loadLocalPurchases() {
        purchasesSet = PurchasesProvider.getPurchases(realm);
        if (purchasesSet != null) {
            showPurchases();
            BlitzfudUtils.showFailureSnackbar(binding.pantallaPrincipal);
        } else {
            BlitzfudUtils.showFailure(this);
        }
    }

    private void reloadData() {
        realoading = true;
        binding.recyclerPurchase.setVisibility(View.GONE);
        binding.layoutEmpty.setVisibility(View.GONE);
        binding.layoutLoading.setVisibility(View.VISIBLE);

        PurchasesService.get(dateFrom, dateTo.equals(dateFrom)? null: dateTo).enqueue(new Callback<PurchasesSet>() {
            @Override
            public void onResponse(Call<PurchasesSet> call, Response<PurchasesSet> response) {
                if (response.isSuccessful()) {
                    PurchasesProvider.save(realm, response.body());
                    purchasesSet = PurchasesProvider.getPurchases(realm);

                    binding.layoutLoading.setVisibility(View.GONE);
                    if (purchasesSet.getPurchaseOrders().isEmpty()) {
                        binding.layoutEmpty.setVisibility(View.VISIBLE);
                    } else {
                        binding.recyclerPurchase.setVisibility(View.VISIBLE);
                    }
                    realoading = false;
                } else {
                    BlitzfudUtils.showErrorWithCatch(HistoryOrdersActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PurchasesSet> call, Throwable t) {
                BlitzfudUtils.showFailure(HistoryOrdersActivity.this);
            }
        });
    }

    private void bindListeners() {
        binding.imgBack.setOnClickListener(this);
        binding.layoutFrom.setOnClickListener(this);
        binding.layoutTo.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
        binding = null;
    }
}
