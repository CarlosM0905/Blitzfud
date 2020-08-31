package com.blitzfud.models.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.blitzfud.R;

public class ConfirmDialog {

    private final Dialog dialog;

    public ConfirmDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public void show(){
        dialog.show();
    }

    public static class Builder {

        private Dialog dialogBuilder;
        private final Context context;
        private ImageView imgModal;
        private TextView txtTitle;
        private TextView txtContent;
        private AppCompatButton btnCancel;
        private AppCompatButton btnConfirm;

        public Builder(Context context) {
            this.context = context;
            configdialog();
        }

        private void configdialog() {
            dialogBuilder = new Dialog(context);
            dialogBuilder.setContentView(R.layout.dialog_confirm_action);

            initView();
            setDefaultCancelListener();
        }

        private void initView() {
            imgModal = dialogBuilder.findViewById(R.id.imgModal);
            txtTitle = dialogBuilder.findViewById(R.id.txtTitle);
            txtContent = dialogBuilder.findViewById(R.id.txtContent);
            btnCancel = dialogBuilder.findViewById(R.id.btnCancel);
            btnConfirm = dialogBuilder.findViewById(R.id.btnConfirm);
        }

        private void setDefaultCancelListener(){
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });
        }

        public Builder setImage(int imageResource){
            imgModal.setImageResource(imageResource);
            return this;
        }

        public Builder setTitle(String title){
            txtTitle.setText(title);
            return this;
        }

        public Builder setContent(String content){
            txtContent.setText(content);
            return this;
        }

        public Builder setOnConfirmAction(String confirmText, final OnConfirmDialog onConfirmDialog){
            btnConfirm.setText(confirmText);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    onConfirmDialog.onClickListener();
                }
            });

            return this;
        }

        public Builder setOnCancelAction(String cancelText, final OnConfirmDialog onConfirmDialog){
            btnCancel.setText(cancelText);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    onConfirmDialog.onClickListener();
                }
            });

            return this;
        }

        public ConfirmDialog build(){
            return new ConfirmDialog(dialogBuilder);
        }

    }

    public interface OnConfirmDialog{
        void onClickListener();
    }

}
