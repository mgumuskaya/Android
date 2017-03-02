package com.gmsky.projectx;

/**
 * Created by gmsky on 07/01/2017.
 */

/*
* Bu sınıf StringArray olarak hazırlanmış cümlelerin ana ekranda paragraf halinde listelenmesi için kullanılmaktadır.
* Paragraf oluşturduktan sonra doğrudan ListViewe paragraf listesi olarak gönderdikten sonra burada
* görüntüleme yapılabilirdi; fakat bu uygulamanın işlevselliğini bozuyordu.
* Bu sınıf kullanılarak hem bellek optimizasyonu sağlandı. Hem de listviewde dialog pencereleri açıldıktan
* sonra son paragrafın cümlelere ayrılarak Listenin her bir satırında tek tek cümlelerin listelenmesi engellendi.*/
public class ParagraphClass {
    String title, s_english, s_turkish;

    public ParagraphClass(String title_, String s_english_, String s_turkish_) {
        super();
        this.title = title_;
        this.s_english = s_english_;
        this.s_turkish = s_turkish_;
    }

    public String getTitle() {
        return title;
    }

    public String getS_english() {
        return s_english;
    }

    public String getS_turkish() {
        return s_turkish;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setS_english(String s_english) {
        this.s_english = s_english;
    }

    public void setS_turkish(String s_turkish) {
        this.s_turkish = s_turkish;
    }
}
