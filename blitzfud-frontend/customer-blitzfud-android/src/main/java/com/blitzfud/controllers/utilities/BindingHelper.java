package com.blitzfud.controllers.utilities;

public class BindingHelper {
//    private ActivityVerifyPhoneBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityVerifyPhoneBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
//
//    }




//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }




//    if(response.isSuccessful()){
//
//
//    }else{
//        try {
//            ResponseAPI error = new Gson().fromJson(response.errorBody().string(), ResponseAPI.class);
//            new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText("¡ERROR!")
//                    .setContentText(error.getMessage())
//                    .show();
//        } catch (IOException e) {
//            Toast.makeText(SignInActivity.this, "Error al leer datos", Toast.LENGTH_LONG).show();
//        }
//    }



//                        new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
//                            .setTitleText("Error")
//                            .setContentText("No se pudo establecer conexión con el servidor")
//                            .show();




//    private AlertDialog dialog;
//    dialog = new SpotsDialog.Builder().setContext(this).setMessage("Espere un momento...").build();




//                new SweetAlertDialog(ExecuteOneOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("¿Estás seguro?")
//                        .setConfirmText("Sí")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//        @Override
//        public void onClick(SweetAlertDialog sDialog) {
//            sDialog.dismissWithAnimation();
//            confirm();
//        }
//    })
//            .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
//        @Override
//        public void onClick(SweetAlertDialog sDialog) {
//            sDialog.dismissWithAnimation();
//        }
//    })
//            .show();



//            new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {
//
//            Intent intent;
//
//            if(MyPreference.firstTime){
//                intent = new Intent(SplashActivity.this, GuideActivity.class);
//            }else{
//                if(!AuthService.existsSession()){
//                    intent = new Intent(SplashActivity.this, SignInActivity.class);
//                }else{
//                    intent = new Intent(SplashActivity.this, MainActivity.class);
//                }
//            }
//
//            startActivity(intent);
//            finish();
//        }
//    }, 2000);

}
