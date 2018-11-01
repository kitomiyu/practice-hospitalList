package com.poc.android.myhospitals.itemdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.poc.android.myhospitals.R;

public class NewItemActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.poc.android.myhospitals.REPLY";
    public static final String EXTRA_REPLY_URL = "com.poc.android.myhospitals.REPLY.URL";
    public static final String EXTRA_REPLY_ACCOUNT = "com.poc.android.myhospitals.REPLY.ACCOUNT";
    public static final String EXTRA_REPLY_PASSWORD = "com.poc.android.myhospitals.REPLY.PW";
    public static final String EXTRA_REPLY_ID = "com.poc.android.myhospitals.REPLY.ID";

    // Bundle Key
    private static final String ITEM_ID = "0";
    private static final String ITEM_NAME = "1";
    public static final String ITEM_URL = "2";
    private static final String ITEM_ACCOUNT = "3";
    private static final String ITEM_PW = "4";
    public static final String ITEM_INFO = "5";

    private TextInputEditText mEditItemView;
    private TextInputEditText mEditItemViewUrl;
    private TextInputEditText mEditItemViewAccount;
    private TextInputEditText mEditItemViewPw;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        mEditItemView = findViewById(R.id.edit_item);
        mEditItemViewUrl = findViewById(R.id.edit_item_url);
        mEditItemViewAccount = findViewById(R.id.edit_item_account);
        mEditItemViewPw = findViewById(R.id.edit_item_pw);
        mButton = findViewById(R.id.button_save);

        final Bundle bundle = getIntent().getBundleExtra(ITEM_INFO);

        if (bundle != null) {
            mEditItemView.setText(bundle.getString(ITEM_NAME));
            mEditItemViewUrl.setText(bundle.getString(ITEM_URL));
            mEditItemViewAccount.setText(bundle.getString(ITEM_ACCOUNT));
            mEditItemViewPw.setText(bundle.getString(ITEM_PW));
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent for the reply.
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditItemView.getText()) | TextUtils.isEmpty(mEditItemViewUrl.getText())) {
                    // No word was entered, set the result accordingly.
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    // Get the new item that the user entered.
                    String itemName = mEditItemView.getText().toString();
                    String itemUrl = mEditItemViewUrl.getText().toString();
                    String itemAccount = mEditItemViewAccount.getText().toString();
                    String itemPassword = mEditItemViewPw.getText().toString();
                    // Put the new item in the extras for the reply Intent.
                    replyIntent.putExtra(EXTRA_REPLY, itemName);
                    replyIntent.putExtra(EXTRA_REPLY_URL, itemUrl);
                    replyIntent.putExtra(EXTRA_REPLY_ACCOUNT, itemAccount);
                    replyIntent.putExtra(EXTRA_REPLY_PASSWORD, itemPassword);

                    // in case if we need to update data, we should identify the id to find the target item in the list
                    if (bundle != null) {
                        replyIntent.putExtra(EXTRA_REPLY_ID, bundle.getInt(ITEM_ID));
                    }

                    // Set the result status to indicate success.
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
