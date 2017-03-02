package com.gmsky.projectx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    /*
    * res/values/strings.xml de oluşturulan StringArraylerin idlerinin tutulduğu dizilerdir.
    * e_array dizisi ingilizce cümleler dizilerini tutmaktadır.
    * t_array dizisi Türkçe cümleler dizilerini tutmaktadır. */
    public int e_array[] = {R.array.english_1, R.array.english_2, R.array.english_3, R.array.english_4,
            R.array.english_5, R.array.english_6, R.array.english_7, R.array.english_8, R.array.english_9, R.array.english_10,
            R.array.english_11, R.array.english_12, R.array.english_13, R.array.english_14, R.array.english_15, R.array.english_16,
            R.array.english_17, R.array.english_18, R.array.english_19, R.array.english_20, R.array.english_21, R.array.english_22,
            R.array.english_23, R.array.english_24, R.array.english_25, R.array.english_26, R.array.english_27, R.array.english_28,
            R.array.english_29, R.array.english_30, R.array.english_31, R.array.english_32, R.array.english_33, R.array.english_34,
            R.array.english_35, R.array.english_36, R.array.english_37, R.array.english_38, R.array.english_39, R.array.english_40,
            R.array.english_41, R.array.english_42, R.array.english_43, R.array.english_44, R.array.english_45, R.array.english_46,
            R.array.english_47, R.array.english_48, R.array.english_49, R.array.english_50, R.array.english_51, R.array.english_52,
            R.array.english_53, R.array.english_54, R.array.english_55, R.array.english_56, R.array.english_57, R.array.english_58,
            R.array.english_59, R.array.english_60, R.array.english_61, R.array.english_62, R.array.english_63, R.array.english_64,
            R.array.english_65, R.array.english_66, R.array.english_67, R.array.english_68, R.array.english_69, R.array.english_70,
            R.array.english_71, R.array.english_72, R.array.english_73, R.array.english_74, R.array.english_75};
    int t_array[] = {R.array.turkish_1, R.array.turkish_2, R.array.turkish_3, R.array.turkish_4,
            R.array.turkish_5, R.array.turkish_6, R.array.turkish_7, R.array.turkish_8, R.array.turkish_9, R.array.turkish_10,
            R.array.turkish_11, R.array.turkish_12, R.array.turkish_13, R.array.turkish_14, R.array.turkish_15, R.array.turkish_16,
            R.array.turkish_17, R.array.turkish_18, R.array.turkish_19, R.array.turkish_20, R.array.turkish_21, R.array.turkish_22,
            R.array.turkish_23, R.array.turkish_24, R.array.turkish_25, R.array.turkish_26, R.array.turkish_27, R.array.turkish_28,
            R.array.turkish_29, R.array.turkish_30, R.array.turkish_31, R.array.turkish_32, R.array.turkish_33, R.array.turkish_34,
            R.array.turkish_35, R.array.turkish_36, R.array.turkish_37, R.array.turkish_38, R.array.turkish_39, R.array.turkish_40,
            R.array.turkish_41, R.array.turkish_42, R.array.turkish_43, R.array.turkish_44, R.array.turkish_45, R.array.turkish_46,
            R.array.turkish_47, R.array.turkish_48, R.array.turkish_49, R.array.turkish_50, R.array.turkish_51, R.array.turkish_52,
            R.array.turkish_53, R.array.turkish_54, R.array.turkish_55, R.array.turkish_56, R.array.turkish_57, R.array.turkish_58,
            R.array.turkish_59, R.array.turkish_60, R.array.turkish_61, R.array.turkish_62, R.array.turkish_63, R.array.turkish_64,
            R.array.turkish_65, R.array.turkish_66, R.array.turkish_67, R.array.turkish_68, R.array.turkish_69, R.array.turkish_70,
            R.array.turkish_71, R.array.turkish_72, R.array.turkish_73, R.array.turkish_74, R.array.turkish_75};

    /*
    * titles String dizisinde strings.xml de tanımlı Paragraf başlıkları tutulmaktadır*/
    String[] titles;
    /*İngilizce paragrafların tutulması için kullanılacaktır.*/
    String[] e_paragraphs;
    /*Türkçe paragrafların tutulması için kullanılacaktır.*/
    String[] t_paragraphs;

    //Projede İngilizce ve Türkçe olarak hazırlanmış meitnlerin gösterileceği ListView nesnesinin referansıdır.
    ListView text_list;
    //Özelleştirilmiş ListView kullanılacağı için hazırlanan bağlantı sınıfının referansıdır.
    ListViewAdapter adapter;

    //Cümlelerden oluşturulan paragrafları tutan diziler tutulmaktadır.
    public CustomArrayList engParagraphList, trParagraphList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Proje kullanılırken internet bağlantısı zkullanımı zorunlu olduğu için proje açılışında internet */
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setTitle("Bağlantı Kontrolü");

        Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (!isOnline()) {
            TextView tv = (TextView) dialog.findViewById(R.id.my_word);
            tv.setText("Uygulamayı tam işlevsellikle kullanabilmeniz için internet bağlantınızı aktifleştiriniz!");
            dialog.show();
        }

        //bu arraylistlere string.xmlden çektiğimiz paragrafları ekleyeceğiz
        engParagraphList = new CustomArrayList();
        trParagraphList = new CustomArrayList();
        titles = getResources().getStringArray(R.array.titles);

        ParagraphClass[] pClass = new ParagraphClass[titles.length];

        /* string resource lar sırayla makeParagraph metoduna gönderiliyor*/
        for (int i = 0; i < titles.length; i++) {
            makeParagraph(e_array[i], t_array[i]);
        }

        //makeParagraph metodunda hazırlanan paragraf listeleri ParagraphClass sınıfı tütünden oluşturulan
        //diziye akleniyor.
        for (int i = 0; i < titles.length; i++) {
            pClass[i] = new ParagraphClass(titles[i], engParagraphList.get(i), trParagraphList.get(i));
        }

        text_list = (ListView) findViewById(R.id.lst_paragraphs);
        text_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new ListViewAdapter(this, R.layout.list_row, pClass);
        text_list.setAdapter(adapter);
    }

    /**
     * Aşağıdaki metotta her bir String Array alınıp paragraf haline getirilip paragraf listesine ekleniyor.
     * string.xmlden alınan ingilizce ve türkçe cümlelerden paragraf oluşturup bu paragrafları ArrayListe ekliyor
     * */
    public void makeParagraph(int e_array, int t_array) {
        //en üstte tanımlanan stringArrayler sıra ile gelip bu Arraylerin her bir itemını e_paragraphs
        // ve t_paragraphs Arraylerine atıyor.
        e_paragraphs = getResources().getStringArray(e_array);
        t_paragraphs = getResources().getStringArray(t_array);

        //paragraph arrayin itemlerını alıp paragraf haline getirip Stringe atmak için kullanılan değişkenler.
        String engParagraph = "  ";
        String trParagraph = "  ";
        int s_length = e_paragraphs.length;
        for (int i = 0; i < s_length; i++) {
            //Listviewde görüntülemek için metota her gelen resource u paragraf yapıyor
            engParagraph = engParagraph + " " + e_paragraphs[i];
            trParagraph = trParagraph + " " + t_paragraphs[i];
        }
        //for içinde oluşturulan paragrafları ListViewAdaptere göndermek için ArrayListe ekliyor.
        engParagraphList.add(engParagraph);
        trParagraphList.add(trParagraph);
    }

    /*Android işletim sisteminden internet bağlantı bilgilerini alarak bağlantı olup olmadığı
    bilgisini programa veren metot.*/
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}




    /*aşağıdaki string dizileri ise ArrayListti string diziye dönüştürüp bunları tutacağız.
    sonrasında bu string dizilerini ListViewAdaptere göndereceğiz.*/
    /*String[] engParagraphArray;
    String[] trParagraphArray;*/


        /*ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);

        dialog.setContentView(listViewItems);
        dialog.show();*/
//text_list.setAdapter(adapter);
        /*Log.e("ERRRRR", String.valueOf(pClass.length));
        Log.e("ERRRRR", String.valueOf(titles.length));
        Log.e("ERRRRR", String.valueOf(engParagraphList.size()));
        Log.e("ERRRRR", String.valueOf(trParagraphList.size()));*/

        /* makeParagraphta liste haline getirilen paragraflar String dizi haline getiriliyor*/
        /*engParagraphArray = new String[engParagraphList.size()];
        engParagraphArray = engParagraphList.toArray(engParagraphArray);

        trParagraphArray = new String[trParagraphList.size()];
        trParagraphArray = trParagraphList.toArray(trParagraphArray);*/

        /*text_list e titles, engParagraphArray, trParagraphArray gönderiliyor bu arraylari listViewde gösteriyor*/
