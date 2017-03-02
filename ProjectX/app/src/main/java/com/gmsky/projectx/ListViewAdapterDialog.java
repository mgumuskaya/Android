package com.gmsky.projectx;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gmsky on 15/01/2017.
 */

/**
 * Kelime türlerinin alert dialogda liste halinde gösterilmesi için kullanılmaktaydı.
 * Sözcüklerin renklendirilip alert dialogda textView içerisinde gösterilmesi sonrasında bu sınıfın kullanım sonlanmıştır.*/
public class ListViewAdapterDialog extends BaseAdapter {

    //LayoutInflater inflater;

    Context context;
    int layoutResourceId;
    WordType[] wordTypes = null;
    /*ArrayList<String> wordOfSentence;
    ArrayList<String> wordType;*/

    public ListViewAdapterDialog(Context context_, int layoutResourceId_, WordType[] wordTypes_) {      //ArrayList<String> wordOfSentence_, ArrayList<String> wordType_){
        this.context = context_;
        this.layoutResourceId = layoutResourceId_;
        this.wordTypes = wordTypes_;
        /*this.wordOfSentence=wordOfSentence_;
        this.wordType=wordType_;*/
    }

    @Override
    public int getCount() {
        return wordTypes.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        WordViewHolder viewHolder;
        if (view == null) {
            //Log.e("NULLVIEW","view nesnesi boş");
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);

            viewHolder = new WordViewHolder();

            viewHolder.tvWOS = (TextView) view.findViewById(R.id.tv_wos);
            viewHolder.tvWT = (TextView) view.findViewById(R.id.tv_wt);

            view.setTag(viewHolder);
        } else {
            //Log.e("NULLVIEW","view nesnesi boş değillllllll");
            viewHolder = (WordViewHolder) view.getTag();
        }
        WordType wordType = wordTypes[i];
        if (wordType != null) {
            viewHolder.tvWOS.setText(wordType.getWord());
            viewHolder.tvWT.setText(wordType.getW_type());
        }
        return view;
    }
    static class WordViewHolder {
        TextView tvWOS, tvWT;
    }
}



        /*TextView tvWOS,tvWT;

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.custom_list_view, parent, false);

        tvWOS=(TextView)itemView.findViewById(R.id.tv_wos);
        tvWT=(TextView)itemView.findViewById(R.id.tv_wt);

        tvWOS.setText(wordOfSentence.get(i));
        tvWT.setText(wordType.get(i));
        return itemView;*/