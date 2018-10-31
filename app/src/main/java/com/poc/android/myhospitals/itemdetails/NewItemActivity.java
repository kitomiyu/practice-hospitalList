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
    public static final String EXTRA_REPLY_ID = "com.poc.android.myhospitals.REPLY.ID";

    // Bundle Key
    private static final String ITEM_ID = "0";
    private static final String ITEM_NAME = "1";
    public static final String ITEM_URL = "2";
    public static final String ITEM_INFO = "3";

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
                    String item = mEditItemView.getText().toString();
                    String itemName = mEditItemViewUrl.getText().toString();
                    // Put the new item in the extras for the reply Intent.
                    replyIntent.putExtra(EXTRA_REPLY, item);
                    replyIntent.putExtra(EXTRA_REPLY_URL, itemName);

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
