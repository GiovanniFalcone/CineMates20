package com.cinemates20.View;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cinemates20.Presenter.ResetPasswordPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.google.android.material.textfield.TextInputLayout;


public class ResetPasswordActivity extends AppCompatActivity {

    private TextView back;
    private Button forgot;
    private EditText editTextEmail;
    private ResetPasswordPresenter resetPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordPresenter = new ResetPasswordPresenter(this);
        TextInputLayout textInputLayoutPasswordForgot = findViewById(R.id.emailResetPassword);
        editTextEmail = findViewById(R.id.textEmailReset);
        forgot = findViewById(R.id.buttonForgot);
        back = findViewById(R.id.textBack);

        back.setOnClickListener(view -> finish());

        editTextEmail.addTextChangedListener(textWatcher);
    }

    private void eventListener() {
        forgot.setOnClickListener(v -> {
            resetPasswordPresenter.onClickPasswordForgot(editTextEmail.getText().toString().trim());
        });
    }

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String mail = editTextEmail.getText().toString().trim();

            if(!Utils.checkIfFieldIsEmpty(mail)){
                forgot.setEnabled(true);
                eventListener();
            }else{
                forgot.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public Context getActivityContext(){
        return this;
    }
}