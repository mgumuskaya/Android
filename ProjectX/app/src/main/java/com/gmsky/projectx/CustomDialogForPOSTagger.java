package com.gmsky.projectx;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by gmsky on 22/01/2017.
 */


/*
* Part-of-Speech uygulanan cümlenin renklendirilmiş halde alerte basılması için özelleştirilmiş Dialog sınıfıdır.*/
public class CustomDialogForPOSTagger extends Dialog {

    private SpannableStringBuilder ssBuilder;
    TextView tv_spanned_sentence;
    Button btn_close;

    public CustomDialogForPOSTagger(Context context, SpannableStringBuilder ssBuilder_) {
        super(context);
        this.ssBuilder = ssBuilder_;
    }

    /*
    * Dialogun gösterilmesi için zorunlu olan OnCreate metodu ile Dialog görüntüsü
    * Farklı bir Layout kullanılarak özelleştirilmiştir.*/
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos_dialog_layout);

        setTitle("Parts-of-Speech");
        tv_spanned_sentence = (TextView) findViewById(R.id.spanned_sentence);
        tv_spanned_sentence.setText(ssBuilder);
        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
