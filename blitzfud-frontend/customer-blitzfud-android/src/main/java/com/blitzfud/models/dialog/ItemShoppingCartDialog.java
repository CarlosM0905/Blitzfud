package com.blitzfud.models.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.blitzfud.R;
import com.blitzfud.controllers.localDB.providers.ShoppingCartDBProvider;
import com.blitzfud.controllers.restapi.services.ShoppingCartService;
import com.blitzfud.controllers.utilities.BlitzfudUtils;
import com.blitzfud.models.market.Market;
import com.blitzfud.models.market.Product;
import com.blitzfud.models.responseAPI.ResponseAPI;
import com.blitzfud.models.responseAPI.ShoppingCartSet;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.blitzfud.controllers.utilities.BlitzfudConstants.ADD_ITEM_SHOPPING_CART;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.REMOVE_ITEM_SHOPPING_CART;
import static com.blitzfud.controllers.utilities.BlitzfudConstants.UPDATE_ITEM_SHOPPING_CART;

public class ItemShoppingCartDialog {
    private Context context;
    private ShoppingCartSet shoppingCartSet;
    private Product product;
    private Market market;
    private OnConfirmClickListener onConfirmClickListener;

    private int quantity = 1;

    public ItemShoppingCartDialog(Context context, OnConfirmClickListener onConfirmClickListener) {
        this.context = context;
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public ItemShoppingCartDialog(Context context, Product product, OnConfirmClickListener onConfirmClickListener) {
        this.context = context;
        this.product = product;
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public void setShoppingCartSet(ShoppingCartSet shoppingCartSet) {
        this.shoppingCartSet = shoppingCartSet;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public void addItem(final Realm realm, final View view, final Dialog dialog, final int quantity, final Product product, final Market market) {
        dialog.show();
        ShoppingCartService.addItem(product.get_id(), quantity).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                onResponseBody(realm, view, dialog, response, ADD_ITEM_SHOPPING_CART, quantity, product, market);
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                onFailureBody(dialog);
            }
        });
    }

    public void updateItem(final Realm realm, final View view, final Dialog dialog, final int quantity, final Product product, final Market market) {
        dialog.show();
        ShoppingCartService.updateItem(product.get_id(), quantity).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                onResponseBody(realm, view, dialog, response, UPDATE_ITEM_SHOPPING_CART, quantity, product, market);
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                onFailureBody(dialog);
            }
        });
    }

    public void removeItem(final Realm realm, final View view, final Dialog dialog, final Product product, final Market market) {
        dialog.show();
        ShoppingCartService.removeItem(product.get_id()).enqueue(new Callback<ResponseAPI>() {
            @Override
            public void onResponse(Call<ResponseAPI> call, Response<ResponseAPI> response) {
                onResponseBody(realm, view, dialog, response, REMOVE_ITEM_SHOPPING_CART, 0, product, market);
            }

            @Override
            public void onFailure(Call<ResponseAPI> call, Throwable t) {
                onFailureBody(dialog);
            }
        });
    }

    private void onResponseBody(final Realm realm, final View view, final Dialog dialog,
                                Response<ResponseAPI> response, int type, int quantity,
                                Product product, Market market) {
        switch (type) {
            case ADD_ITEM_SHOPPING_CART:
                ShoppingCartDBProvider.addItem(realm, shoppingCartSet, quantity, product, market);
                break;
            case UPDATE_ITEM_SHOPPING_CART:
                ShoppingCartDBProvider.updateItem(realm, shoppingCartSet, quantity, product, market);
                break;
            case REMOVE_ITEM_SHOPPING_CART:
                ShoppingCartDBProvider.removeItem(realm, shoppingCartSet, product, market);
                break;
            default: return;
        }

        dialog.dismiss();

        if (response.isSuccessful()) {
            BlitzfudUtils.showSnackbar(view, response.body().getMessage());
        } else {
            BlitzfudUtils.showErrorWithCatch(context, response.errorBody());
        }
    }

    private void onFailureBody(final Dialog dialog) {
        dialog.dismiss();
        BlitzfudUtils.showFailure(context);
    }

    public static class Builder {
        private ItemShoppingCartDialog itemShoppingCartDialog;
        private Dialog dialog;
        private ImageView imgClose;
        private TextView txtInformationProduct;
        private TextView txtTotal;
        private ImageView lblMinus;
        private ImageView lblPlus;
        private TextView txtQuantity;
        private AppCompatButton btnAccept;
        private int initialQuantity;
        private int quantity = 1;
        private boolean alreadyAdded;

        public Builder(Context context, OnConfirmClickListener onConfirmClickListener) {
            itemShoppingCartDialog = new ItemShoppingCartDialog(context, onConfirmClickListener);
            configDialog();
        }

        private void configDialog() {
            dialog = new Dialog(itemShoppingCartDialog.context);
            dialog.setContentView(R.layout.dialog_item_shopping_cart);

            imgClose = dialog.findViewById(R.id.imgClose);
            txtInformationProduct = dialog.findViewById(R.id.txtInformationProduct);
            txtTotal = dialog.findViewById(R.id.txtTotal);
            lblMinus = dialog.findViewById(R.id.lblMinus);
            lblPlus = dialog.findViewById(R.id.lblPlus);
            txtQuantity = dialog.findViewById(R.id.txtQuantity);
            btnAccept = dialog.findViewById(R.id.btnAccept);

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!alreadyAdded) {
                        if (quantity != 0) {
                            dialog.dismiss();
                            itemShoppingCartDialog.onConfirmClickListener.onAddItem(quantity,
                                    itemShoppingCartDialog.product, itemShoppingCartDialog.market);
                        }
                    } else {
                        if (quantity != 0) {
                            if (quantity != initialQuantity) {
                                dialog.dismiss();
                                itemShoppingCartDialog.onConfirmClickListener.onUpdateItem(quantity,
                                        itemShoppingCartDialog.product, itemShoppingCartDialog.market);
                            }
                        } else {
                            dialog.dismiss();
                            itemShoppingCartDialog.onConfirmClickListener.onRemoveItem(
                                    itemShoppingCartDialog.product, itemShoppingCartDialog.market);
                        }
                    }
                }
            });
        }

        public Builder setShoppingCartSet(ShoppingCartSet shoppingCartSet) {
            itemShoppingCartDialog.setShoppingCartSet(shoppingCartSet);

            return this;
        }

        public Builder setProduct(Product product) {
            itemShoppingCartDialog.setProduct(product);

            return this;
        }

        public Builder setMarket(Market market) {
            itemShoppingCartDialog.setMarket(market);

            return this;
        }

        private void updateView() {
            double total = quantity * itemShoppingCartDialog.product.getPrice();
            txtQuantity.setText(String.valueOf(quantity));
            txtTotal.setText(String.format("S/ %.2f", total));

            if (alreadyAdded) {
                if (quantity == 0) {
                    btnAccept.setText("Eliminar del carrito");
                } else {
                    btnAccept.setText("Actualizar");
                }
            }
        }

        public ItemShoppingCartDialog build() {
            if (itemShoppingCartDialog.product == null || itemShoppingCartDialog.market == null ||
                    itemShoppingCartDialog.shoppingCartSet == null) return null;

            quantity = itemShoppingCartDialog.shoppingCartSet.getQuantity(
                    itemShoppingCartDialog.product.get_id(), itemShoppingCartDialog.market.get_id());
            initialQuantity = quantity;
            alreadyAdded = (quantity != 0);

            if (quantity == 0) quantity = 1;

            lblMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity > 0) {
                        quantity--;
                        updateView();
                    }
                }
            });

            lblPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity < itemShoppingCartDialog.product.getMaxQuantityPerOrder()) {
                        quantity++;
                        updateView();
                    }
                }
            });

            txtQuantity.setText(String.valueOf(quantity));
            txtInformationProduct.setText(itemShoppingCartDialog.product.getName() + " " +
                    itemShoppingCartDialog.product.getInformation());
            txtTotal.setText(String.format("S/ %.2f",
                    itemShoppingCartDialog.product.getPrice() * quantity));
            btnAccept.setText((alreadyAdded)?"Actualizar": "Agregar al carrito");

            dialog.show();

            return itemShoppingCartDialog;
        }
    }

    public interface OnConfirmClickListener {
        void onAddItem(int quantity, Product product, Market market);

        void onUpdateItem(int quantity, Product product, Market market);

        void onRemoveItem(Product product, Market market);
    }
}
