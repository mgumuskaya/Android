package com.gmsky.projectx;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gmsky on 08/01/2017.
 */

public class ListViewAdapter extends BaseAdapter {

    public static final String INFO = "INFO=>";
    public static final String ERR = "ERROR=>";

    TextView tvWord, tvTranslate;
    Button btnOK;
    Dialog dialog;
    String[] titles;

    String[] e_paragraphs;
    String[] t_paragraphs;

    public CustomArrayList engList;
    public CustomArrayList trList;

    //aşağıdaki arraylistler _ karakterine före ayrılmış kelimelerle dolduruluyor ve
    //listviewadaptere gönderiliyor burada da listview dolduruluyor.
    ArrayList<String> wOfSentence = new ArrayList<>();
    ArrayList<String> wordType = new ArrayList<>();

    //renklendirilmiş cümle için kullanılacak.
    ArrayList<Spannable> spannedText = new ArrayList<>();

    //aşağıdaki arraylist postagger metodundan dönen sümleyi boşluğa göre ayrılmış halde tutuyor.
    ArrayList<String> stringList = new ArrayList<>();


    //Adapterden gelenler
    Context context;
    int layoutResourceId;
    ParagraphClass[] pClasses = null;

    public ListViewAdapter(Context context_, int layoutResourceId_, ParagraphClass[] pClasses_) {
        this.context = context_;
        this.layoutResourceId = layoutResourceId_;
        this.pClasses = pClasses_;
    }

    @Override
    public int getCount() {
        return pClasses.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            vHolder = new ViewHolder();

            vHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            vHolder.tvEngParagraph = (TextView) convertView.findViewById(R.id.tv_english);
            vHolder.tvTrParagraph = (TextView) convertView.findViewById(R.id.tv_turkish);

            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        ParagraphClass paragraphClass = pClasses[position];

        if (paragraphClass != null) {
            vHolder.tvTitle.setText(paragraphClass.getTitle());
            vHolder.tvEngParagraph.setText(paragraphClass.getS_english());
            vHolder.tvTrParagraph.setText(paragraphClass.getS_turkish());
        }

        vHolder.tvEngParagraph.setCustomSelectionActionModeCallback(new callback(vHolder.tvEngParagraph));
        vHolder.tvTrParagraph.setCustomSelectionActionModeCallback(new callback(vHolder.tvTrParagraph));
        return convertView;
    }

    public class callback implements ActionMode.Callback {
        private TextView mText;

        public callback(TextView text) {
            this.mText = text;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Seçim");
            mode.getMenuInflater().inflate(R.menu.selection_menu, menu);
            return true;
        }

        @SuppressWarnings("ResourceType")
        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            menu.clear();
            menu.add(R.menu.selection_menu, R.id.translate, 0, R.string.s_translate);
            menu.add(R.menu.selection_menu, R.id.tag_it, 1, R.string.s_tag_it);
            menu.add(R.menu.selection_menu, R.id.translate_google, 2, R.string.s_translate_g);
            menu.add(R.menu.selection_menu, R.id.close, 3, R.string.s_close);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
            //Çeviri sonuçlarını göstermek için açılan diaolg
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom_alert_dialog);
            dialog.setTitle("Çeviri");

            btnOK = (Button) dialog.findViewById(R.id.btn_ok);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //makeParagraph metodunu çalıştırmak için bir liste boyutu gerekiyor o nedenle kullanılıyor.
            titles = context.getResources().getStringArray(R.array.titles);

            tvWord = (TextView) dialog.findViewById(R.id.my_word);
            tvTranslate = (TextView) dialog.findViewById(R.id.my_translate);

            int start = mText.getSelectionStart();
            int end = mText.getSelectionEnd();

            engList = new CustomArrayList();
            trList = new CustomArrayList();

            MainActivity ma = new MainActivity();
            for (int i = 0; i < titles.length; i++) {
                fillList(ma.e_array[i], ma.t_array[i]);
            }

            Spannable wordtoSpan = (Spannable) mText.getText();
            switch (item.getItemId()) {
                case R.id.translate:
                    wordtoSpan.setSpan(new BackgroundColorSpan(Color.YELLOW), start
                            , end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    String sentence = wordtoSpan.subSequence(start, end).toString();

                    tvWord.setText(findMarkedSentence(sentence));
                    tvTranslate.setText(translateSentence(sentence));
                    dialog.show();

                    Log.i(INFO, wordtoSpan.subSequence(start, end).toString());
                    wordtoSpan.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    actionMode.finish();
                    return true;

                case R.id.translate_google:

                    if (!isOnline()) {
                        tvWord.setText("Uygulamayı tam işlevsellikle kullanabilmeniz için internet bağlantınızı aktifleştiriniz!");
                        dialog.show();
                    } else {
                        wordtoSpan.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        String word = wordtoSpan.subSequence(start, end).toString();
                        new TranslateAsyncTask(new TranslateCallback() {
                            @Override
                            public void onSuccess(String result) {
                                tvTranslate.setText(result);
                            }
                        }).execute(word);
                        tvWord.setText(word);
                        dialog.show();
                    }
                    actionMode.finish();
                    wordtoSpan.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.i(INFO, wordtoSpan.subSequence(start, end).toString());
                    return true;

                /* Bu fonksiyonu da diğeri ana listenin hazırlanması gibi sınıf kullanarak yap öyle dene bakalım sonuna ekleyecek mi*/
                case R.id.tag_it:
                    if (!isOnline()) {
                        tvWord.setText("Uygulamayı tam işlevsellikle kullanabilmeniz için internet bağlantınızı aktifleştiriniz!");
                        dialog.show();
                    } else {
                        //wordtoSpan.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end,
                        //Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        final String pos_sentence = findMarkedSentence(wordtoSpan.subSequence(start, end).toString());

                        /*text_list = (ListView) findViewById(R.id.lst_paragraphs);
                        text_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                        adapter = new ListViewAdapter(this, R.layout.list_row, pClass);
                        text_list.setAdapter(adapter);*/

                        new POSAsyncTask(new POSCallback() {
                            @Override
                            public void onSuccess(String result) {

                                if (stringList.size() > 0) {
                                    Log.e("NULLCONT", "stringList null değil");
                                    stringList.removeAll(stringList);
                                    wOfSentence.removeAll(wOfSentence);
                                    wordType.removeAll(wordType);
                                    spannedText.removeAll(spannedText);
                                } else {
                                    Log.e("NULLCONT", "stringList null");
                                }

                                //result değişkeninin tuttuğu NLP uygulanmış cümleyi getiriyor  //result aşağıdaki metoda gönderilerek iki arrayliste ayrılıyor
                                splitText(result);

                                //aşağıdaki kod parçası kelimeyi renklendiriyor
                                for (int i = 0; i < wOfSentence.size(); i++) {
                                    spannedText.add(setSpanToWord(wOfSentence.get(i), wordType.get(i)));
                                }
                                //renklendirilmiş kelimelerden cümle oluşturuyor.
                                final SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
                                for (int i = 0; i < spannedText.size(); i++) {
                                    ssBuilder.append(spannedText.get(i));
                                    ssBuilder.append(" ");
                                }

                                //mText.setText(findMarkedSentence(ssBuilder), TextView.BufferType.SPANNABLE);
                                //new BackgroundColorSpan(context.getResources().getColor(replaceColor(/*burada pos_sentence in her kelimesini değiştireceğiz*/)))
                                /* Textview nesnesi döndüren bir metot yazsam
                                * mtext.getTexti bu metoda göndersem */

                                /*String text2 = text + CepVizyon.getPhoneCode() + "\n\n"
                                 + getText(R.string.currentversion) + CepVizyon.getLicenseText();
                                 Spannable spannable = new SpannableString(text2);
                                 spannable.setSpan(new ForegroundColorSpan(Color.WHITE), text.length(), (text + CepVizyon.getPhoneCode()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                 myTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                                mText.setText(mText.getText().toString().replace(pos_sentence,"ddddddddddddddddddddddddddddddd"));*/
                                //mText.setText(ssBuilder, TextView.BufferType.SPANNABLE);

                                /*int x=pos_sentence.indexOf(pos_sentence.charAt(0));
                                int y=pos_sentence.length();
                                Spannable sentenceSpan= (Spannable) mText.getText();
                                sentenceSpan.setSpan(new BackgroundColorSpan(Color.CYAN),x,y,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                Log.e("POSINDEX", String.valueOf(x)+"    "+String.valueOf(y));*/

                                final CustomDialogForPOSTagger customDialog = new CustomDialogForPOSTagger(context, ssBuilder);
                                customDialog.show();
                            }
                        }).execute(pos_sentence);
                    }
                    wordtoSpan.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    actionMode.finish();
                    Log.i(INFO, wordtoSpan.subSequence(start, end).toString());
                    return true;

                case R.id.close:
                    wordtoSpan.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    actionMode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
        }

        public void fillList(int e_array, int t_array) {
            e_paragraphs = context.getResources().getStringArray(e_array);
            t_paragraphs = context.getResources().getStringArray(t_array);
            int s_length = e_paragraphs.length;
            for (int i = 0; i < s_length; i++) {
                //String.xmlden gelen cümleleri arama yapabilmek için Arrayliste ekliyor
                engList.add(e_paragraphs[i]);
                trList.add(t_paragraphs[i]);
            }
        }

        /*Bu metot translate sentence için*/
        public String translateSentence(String sentencePart) {
            int index;
            String translatedSentence = "";
            if (engList.contains(sentencePart) == true) {
                index = engList.indexOf(sentencePart);
                translatedSentence = trList.get(index);
            } else if (trList.contains(sentencePart) == true) {
                index = trList.indexOf(sentencePart);
                translatedSentence = engList.get(index);
            }
            return translatedSentence;
        }

        public String findMarkedSentence(String sentencePart) {
            int index;
            String translatedSentence = "";
            if (engList.contains(sentencePart) == true) {
                index = engList.indexOf(sentencePart);
                translatedSentence = engList.get(index);
            } else if (trList.contains(sentencePart) == true) {
                index = trList.indexOf(sentencePart);
                translatedSentence = trList.get(index);
            }
            return translatedSentence;
        }


        //recolor words according to wordType
        public Spannable setSpanToWord(String wOfSentence, String wordType) {
            Spannable wordSpan = new SpannableString(wOfSentence);
            wordSpan.setSpan(new BackgroundColorSpan(context.getResources().getColor(replaceColor(wordType))), 0, wOfSentence.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordSpan;
        }

        //Buradan sonrası POS tagger için
        public void splitText(String Str) {
            for (String retval : Str.split(" ")) {
                for (String subRet : retval.split("_")) {
                    stringList.add(subRet);
                }
            }

            for (int i = 0; i < stringList.size(); i++) {
                if (i % 2 == 0) {
                    wOfSentence.add(stringList.get(i));
                } else if (i % 2 == 1) {
                    wordType.add(replacePOS(stringList.get(i)));
                }
            }
        }

        //set colour to word according to wordType
        public int replaceColor(String wordType) {
            int color;
            switch (wordType) {
                case ("Coordinating conjunction"):
                    color = R.color.cc;
                    break;
                case ("Cardinal number"):
                    color = R.color.cd;
                    break;
                case ("Determiner"):
                    color = R.color.dt;
                    break;
                case ("Existential there"):
                    color = R.color.ex;
                    break;
                case ("Foreign word"):
                    color = R.color.fw;
                    break;
                case ("Preposition or subordinating conjunction"):
                    color = R.color.in;
                    break;
                case ("Adjective"):
                    color = R.color.jj;
                    break;
                case ("Adjective, comparative"):
                    color = R.color.jjr;
                    break;
                case ("Adjective, superlative"):
                    color = R.color.jjs;
                    break;
                case ("List item marker"):
                    color = R.color.ls;
                    break;
                case ("Modal"):
                    color = R.color.md;
                    break;
                case ("Noun, singular or mass"):
                    color = R.color.nn;
                    break;
                case ("Noun, plural"):
                    color = R.color.nns;
                    break;
                case ("Proper noun, singular"):
                    color = R.color.nnp;
                    break;
                case ("Proper noun, plural"):
                    color = R.color.nnps;
                    break;
                case ("Predeterminer"):
                    color = R.color.pdt;
                    break;
                case ("Possessive ending"):
                    color = R.color.pos;
                    break;
                case ("Personal pronoun"):
                    color = R.color.prp;
                    break;
                case ("Possessive pronoun"):
                    color = R.color.prpp;
                    break;
                case ("Adverb"):
                    color = R.color.rb;
                    break;
                case ("Adverb, comparative"):
                    color = R.color.rbr;
                    break;
                case ("Adverb, superlative"):
                    color = R.color.rbs;
                    break;
                case ("Particle"):
                    color = R.color.rp;
                    break;
                case ("Symbol"):
                    color = R.color.sym;
                    break;
                case ("to"):
                    color = R.color.to;
                    break;
                case ("Interjection"):
                    color = R.color.uh;
                    break;
                case ("Verb, base form"):
                    color = R.color.vb;
                    break;
                case ("Verb, past tense"):
                    color = R.color.vbd;
                    break;
                case ("Verb, gerund or present participle"):
                    color = R.color.vbg;
                    break;
                case ("Verb, past participle"):
                    color = R.color.vbn;
                    break;
                case ("Verb, non-3rd person singular present"):
                    color = R.color.vbp;
                    break;
                case ("Verb, 3rd person singular present"):
                    color = R.color.vbz;
                    break;
                case ("Wh-determiner"):
                    color = R.color.wdt;
                    break;
                case ("Wh-pronoun"):
                    color = R.color.wp;
                    break;
                case ("Possessive wh-pronoun"):
                    color = R.color.wpp;
                    break;
                case ("Wh-adverb"):
                    color = R.color.wrb;
                    break;
                case ("."):
                    color = R.color.mrk;
                    break;
                case (","):
                    color = R.color.mrk;
                    break;
                case ("?"):
                    color = R.color.mrk;
                    break;
                case ("!"):
                    color = R.color.mrk;
                    break;
                case (":"):
                    color = R.color.mrk;
                    break;
                case (";"):
                    color = R.color.mrk;
                    break;
                case ("'"):
                    color = R.color.mrk;
                    break;
                case ("\""):
                    color = R.color.mrk;
                    break;
                case ("``"):
                    color = R.color.mrk;
                    break;
                case ("´´"):
                    color = R.color.mrk;
                    break;
                default:
                    color = R.color.trp;
                    break;
            }
            return color;
        }

        //replace PENN TREEBANK codes to word type
        public String replacePOS(String word) {
            String replacedText = "";
            switch (word) {
                case ("CC"):
                    replacedText = "Coordinating conjunction";
                    break;
                case ("CD"):
                    replacedText = "Cardinal number";
                    break;
                case ("DT"):
                    replacedText = "Determiner";
                    break;
                case ("EX"):
                    replacedText = "Existential there";
                    break;
                case ("FW"):
                    replacedText = "Foreign word";
                    break;
                case ("IN"):
                    replacedText = "Preposition or subordinating conjunction";
                    break;
                case ("JJ"):
                    replacedText = "Adjective";
                    break;
                case ("JJR"):
                    replacedText = "Adjective, comparative";
                    break;
                case ("JJS"):
                    replacedText = "Adjective, superlative";
                    break;
                case ("LS"):
                    replacedText = "List item marker";
                    break;
                case ("MD"):
                    replacedText = "Modal";
                    break;
                case ("NN"):
                    replacedText = "Noun, singular or mass";
                    break;
                case ("NNS"):
                    replacedText = "Noun, plural";
                    break;
                case ("NNP"):
                    replacedText = "Proper noun, singular";
                    break;
                case ("NNPS"):
                    replacedText = "Proper noun, plural";
                    break;
                case ("PDT"):
                    replacedText = "Predeterminer";
                    break;
                case ("POS"):
                    replacedText = "Possessive ending";
                    break;
                case ("PRP"):
                    replacedText = "Personal pronoun";
                    break;
                case ("PRP$"):
                    replacedText = "Possessive pronoun";
                    break;
                case ("RB"):
                    replacedText = "Adverb";
                    break;
                case ("RBR"):
                    replacedText = "Adverb, comparative";
                    break;
                case ("RBS"):
                    replacedText = "Adverb, superlative";
                    break;
                case ("RP"):
                    replacedText = "Particle";
                    break;
                case ("SYM"):
                    replacedText = "Symbol";
                    break;
                case ("TO"):
                    replacedText = "to";
                    break;
                case ("UH"):
                    replacedText = "Interjection";
                    break;
                case ("VB"):
                    replacedText = "Verb, base form";
                    break;
                case ("VBD"):
                    replacedText = "Verb, past tense";
                    break;
                case ("VBG"):
                    replacedText = "Verb, gerund or present participle";
                    break;
                case ("VBN"):
                    replacedText = "Verb, past participle";
                    break;
                case ("VBP"):
                    replacedText = "Verb, non-3rd person singular present";
                    break;
                case ("VBZ"):
                    replacedText = "Verb, 3rd person singular present";
                    break;
                case ("WDT"):
                    replacedText = "Wh-determiner";
                    break;
                case ("WP"):
                    replacedText = "Wh-pronoun";
                    break;
                case ("WP$"):
                    replacedText = "Possessive wh-pronoun";
                    break;
                case ("WRB"):
                    replacedText = "Wh-adverb";
                    break;
                case ("."):
                    replacedText = ".";
                    break;
                case (","):
                    replacedText = ",";
                    break;
                case ("?"):
                    replacedText = "?";
                    break;
                case ("!"):
                    replacedText = "!";
                    break;
                case (":"):
                    replacedText = ":";
                    break;
                case (";"):
                    replacedText = ";";
                    break;
                case ("'"):
                    replacedText = "'";
                    break;
                case ("\""):
                    replacedText = "\"";
                    break;
                case ("``"):
                    replacedText = "``";
                    break;
                case ("´´"):
                    replacedText = "´´";
                    break;
                default:
                    replacedText = word;
                    break;
            }
            return replacedText;
        }

        //Internet connection control method
        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }
    }

    static class ViewHolder {
        TextView tvTitle, tvEngParagraph, tvTrParagraph;
    }
}


        /*eski usul her kaydırmada yenilenen listview içeriği

        TextView tvTitle, tvEngParagraph, tvTrParagraph;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.list_row, parent, false);

        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvEngParagraph = (TextView) itemView.findViewById(R.id.tv_english);
        tvTrParagraph = (TextView) itemView.findViewById(R.id.tv_turkish);

        tvTitle.setText(titles[position]);
        tvEngParagraph.setText(e_paragraphs.get(position));
        tvTrParagraph.setText(t_paragraphs.get(position));
        */





                                /*Custom AlertDialog Oluşturuluyor
                                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Part of Speech");
                                builder.setCancelable(true);
                                builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                //custom alert dialoga listviewAdapterde doldurulan liste gönderiliyor
                                builder.setAdapter(listViewAdapterDialog, null);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                */
//splittext metodunda doldurulan arraylistler listViewAdapter a gönderiliyor burada listeleniyor.

                                /*aşağıdaki kod parçası WordType sınıfına listviewde kullanılacak olan veri listesini gönderiyor

                                WordType[] wordTypes = new WordType[wOfSentence.size()];
                                for (int i = 0; i < titles.length; i++) {
                                    wordTypes[i] = new WordType(wOfSentence.get(i), wordType.get(i));
                                }*/






                                /*final TextView tv = new TextView(context);
                                tv.setText(ssBuilder);*/

                                /*listViewAdapterDialog = new ListViewAdapterDialog(context, R.layout.custom_list_view, wordTypes);
                                ListView listViewItems = new ListView(context);
                                listViewItems.setAdapter(listViewAdapterDialog);*/

                                /*AlertDialog alertDialogStores = new AlertDialog.Builder(context)
                                        .setView(listViewItems)
                                        .setTitle("Parts-of-Speech")
                                        .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .show();*/