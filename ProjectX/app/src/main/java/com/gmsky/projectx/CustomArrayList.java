package com.gmsky.projectx;

import java.util.ArrayList;

/**
 * Created by gmsky on 08/01/2017.
 */

/*
* ArrayListin bazı metotlarının özelleştirilmesi için yazılmış sınıftır.
* Proje de kullanılan özelleştirilmiş metotlar contains ve indexOf metotlarıdır.*/
public class CustomArrayList extends ArrayList<String> {
    private static final long serialVersionUID = 2178228925760279677L;

    /*Aşağıdaki metot ArrayList içerisindeki her itemın belli bir parçası kullanılarak arama yapılması
    * durumunda kullanılmaktadır.
    * Projede, bir cümlenin içerisinde işaretlenen bir ya da birkaç sözcüğün
    * cümleler listesi içerisinde yer alıp almadığının kontrol edilmesi için kullanılmıştır.*/
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /*
    * ArrayListte yer alan itemlardan birisi arandığında hangi indexte yer aldığının bulunmasını sağlar
    * ve aranan item arraylistte yer alıyorsa bu itemın index numarasını geri döndürür.*/
    @Override
    public int indexOf(Object o) {
        int size = this.size();
        if (o == null) {
            for (int i = 0; i < size ; i++) {
                if (this.get(i) == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size ; i++) {
                if (this.get(i).contains(String.valueOf(o))) {
                    return i;
                }
            }
        }
        return -1;
    }
}
