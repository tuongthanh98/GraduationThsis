package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gradutionthsis.MainActivity;
import com.example.gradutionthsis.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthCredential phoneAuthCredential;

    private EditText edtInput, edtInput2, edtInput3, edtInput4, edtInput5, edtInput6;
    private Button btnNext;
    private ProgressBar progressBar;

    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        String mobile = getIntent().getStringExtra("mobile");

        TextView txtMobile = findViewById(R.id.textMobile);
        txtMobile.setText(String.format("+84-%s", getIntent().getStringExtra("mobile")));

        mAuth = FirebaseAuth.getInstance();

        edtInput = findViewById(R.id.inputCode);
        edtInput2 = findViewById(R.id.inputCode2);
        edtInput3 = findViewById(R.id.inputCode3);
        edtInput4 = findViewById(R.id.inputCode4);
        edtInput5 = findViewById(R.id.inputCode5);
        edtInput6 = findViewById(R.id.inputCode6);

        setupOTPInputs();

        progressBar = findViewById(R.id.progressBar);
        btnNext = findViewById(R.id.buttonNext);

        verificationId = getIntent().getStringExtra("verificationId");

        btnNext.setOnClickListener(view -> {
            if (valid()) {
                String code = edtInput.getText().toString().trim() + edtInput2.getText().toString().trim()
                        + edtInput3.getText().toString().trim()
                        + edtInput4.getText().toString().trim()
                        + edtInput5.getText().toString().trim()
                        + edtInput6.getText().toString().trim();
                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                    phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });

        findViewById(R.id.textResendOTP).setOnClickListener(view -> VerifyOTPActivity.this.resendVerificationCode(mobile, mResendToken));
    }

    /*
     * @author: Nguyễn Thanh Tường
     * date: 04/4/2021 : 18h03p
     * */
    // [START setup_otp_inpunts]
    private void setupOTPInputs() {
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    edtInput2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    edtInput3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtInput3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    edtInput4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtInput4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    edtInput5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtInput5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    edtInput6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    // [END setup_otp_inpunts]

    /*
     * @author: Nguyễn Thanh Tường
     * date: 05/4/2021 : 8h49p
     * */
    // [START valid]
    private boolean valid() {
        if (edtInput.getText().toString().trim().isEmpty() || edtInput2.getText().toString().trim().isEmpty()
                || edtInput3.getText().toString().trim().isEmpty()
                || edtInput4.getText().toString().trim().isEmpty()
                || edtInput5.getText().toString().trim().isEmpty()
                || edtInput6.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter full valid code!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // [END valid]

    /*
     * @author: Nguyễn Thanh Tường
     * date: 05/4/2021 : 8h57p
     * */
    // [START resend_verification]
    private void resendVerificationCode(String mobile, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84" + mobile)             // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS)  // Timeout and unit
                .setActivity(VerifyOTPActivity.this)       // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationId = newVerificationId;
                        mResendToken = forceResendingToken;
                        Toast.makeText(VerifyOTPActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .setForceResendingToken(token)  // ForceResendingToken from callbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }
    // [END resend_verification]


    /*
     * @author: Nguyễn Thanh Tường
     * date: 05/4/2021 : 9h09p
     * */
    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, task -> {
            progressBar.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            if (task.isSuccessful()) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(VerifyOTPActivity.this, "The verification code entered was invalid!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // [END sign_in_with_phone]
}