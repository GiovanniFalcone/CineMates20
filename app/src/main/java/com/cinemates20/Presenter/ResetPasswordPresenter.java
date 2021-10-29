package com.cinemates20.Presenter;

import com.cinemates20.R;
import com.cinemates20.View.ResetPasswordActivity;
import com.cinemates20.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordPresenter {

    private final ResetPasswordActivity resetPasswordActivity;

    public ResetPasswordPresenter(ResetPasswordActivity resetPasswordActivity) {
        this.resetPasswordActivity = resetPasswordActivity;
    }

    /**
     * If the email inserted by the user exists in Firebase then send an email to reset the password,
     * error otherwise
     */
    public void onClickPasswordForgot() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = resetPasswordActivity.getEmail();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Utils.showDialog(resetPasswordActivity.getActivityContext(), "Email sent", resetPasswordActivity.getString(R.string.instruction_mail));
                    } else {
                        Utils.showErrorDialog(resetPasswordActivity.getActivityContext(), "Something went wrong ...", resetPasswordActivity.getString(R.string.something_wrong));
                    }
                });
    }
}
