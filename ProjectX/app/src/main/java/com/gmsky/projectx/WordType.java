package com.gmsky.projectx;

/**
 * Created by gmsky on 20/01/2017.
 */

/**
 * Kelime türlerinin alert dialogda liste halinde gösterilmesi için kullanılmaktaydı.
 * Sözcüklerin renklendirilip alert dialogda textView içerisinde gösterilmesi sonrasında bu sınıfın kullanım sonlanmıştır.
 * Referans olarak kullanıldığı yer ListViewAdapterDialog tur.*/
public class WordType {
    String word, w_type;

    public WordType(String word_, String w_type_) {
        super();
        this.word = word_;
        this.w_type = w_type_;
    }

    public String getWord() {
        return word;
    }

    public String getW_type() {
        return w_type;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setW_type(String w_type) {
        this.w_type= w_type;
    }
}
