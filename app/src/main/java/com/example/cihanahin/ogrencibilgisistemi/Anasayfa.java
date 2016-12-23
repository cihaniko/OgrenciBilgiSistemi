package com.example.cihanahin.ogrencibilgisistemi;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Anasayfa extends AppCompatActivity {

    EditText edNo;
    EditText edAdSoy;
    EditText edVizeNot;
    Button btnEkle;
    Button btnSil;
    Button btnDuzenle;
    Button btnGoruntule;
    Button btnListe;
    Button btnShow;
    ImageView imageView;
    SQLiteDatabase db; //
    public Anasayfa() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        this.edNo = (EditText) findViewById(R.id.ed_No);
        this.edAdSoy = (EditText) findViewById(R.id.ed_Name);
        this.edVizeNot = (EditText) findViewById(R.id.ed_VizeNot);
        this.btnEkle = (Button) findViewById(R.id.btn_Ekle);
        this.btnSil = (Button) findViewById(R.id.btn_Sil);
        this.btnDuzenle = (Button) findViewById(R.id.btn_Duzenle);
        this.btnGoruntule = (Button) findViewById(R.id.btn_goruntule);
        this.btnListe = (Button) findViewById(R.id.btn_Liste);
        this.imageView=(ImageView)findViewById(R.id.imageView2);

        //OgrenciDB Adında veritabanına bağlantı sağlar. Eğer Böyle bir veritabanı yoksa olusturur..
        this.db = this.openOrCreateDatabase("OgrenciDB", 0, (SQLiteDatabase.CursorFactory) null);
        this.db.execSQL("CREATE TABLE IF NOT EXISTS ogrenci(ogrno VARCHAR,isim VARCHAR,vizenot VARCHAR);");

        btnDuzenle.setVisibility(View.INVISIBLE);


        Cursor c;

        this.btnEkle.setOnClickListener(new View.OnClickListener() {
            //Ekle butonuna basıldığında doldurulması gereken degerler kontrol edilir
            @Override
            public void onClick(View v) {
                if (edNo.getText().toString().trim().length() == 0 || edAdSoy.getText().toString().trim().length() == 0 || edVizeNot.getText().toString().trim().length() == 0) {
                    showMessage("Hata", "Tüm Değerleri Doldurmanız Gerekmektedir.");
                    return;
                }
                Cursor c=db.rawQuery("SELECT * FROM ogrenci", (String[]) null);

                if(c.moveToFirst()){
                    //tüm yerler doluysa vize notunun 0 ile 100 arasında olup olmadıgı kontrol edilir.
                    Integer deger=Integer.parseInt(edVizeNot.getText().toString());
                    if(deger >100 || deger <0)
                    {
                        showMessage("Hata", "\"Vize Notu 0-100 Arasında Olmalıdır");
                        return;
                    }

                    // vize notu 0-100 arasında ise editviewlerdeki degerler veri tabanına kaydedilir
                    db.execSQL("INSERT INTO ogrenci VALUES(\'" + edNo.getText() + "\',\'" + edAdSoy.getText() + "\',\'" + edVizeNot.getText() + "\');");
                    showMessage("Başarılı", "Öğrenci Eklendi");
                    clearText();
                }

            }
        });
        this.btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edNo.getText().toString().trim().length() == 0) {
                    showMessage("Hata", "Lütfen Silmek İstediğiniz Öğrencinin Numarasını Giriniz.");
                    return;
                }
                //silme işlemi için önce veritabanında tutulan degerler bulunur
                Cursor c = db.rawQuery("SELECT * FROM ogrenci WHERE ogrno=\'" + edNo.getText() + "\'", (String[]) null);
                if (c.moveToFirst()) {
                    //o değerler veritabanında sıfırlanır.
                    db.execSQL("DELETE FROM ogrenci WHERE ogrno=\'" + edNo.getText() + "\'");
                    showMessage("Başarılı", "Öğrencinin Verileri Silinmiştir");
                } else {
                    showMessage("Hata !", "Öğrenci Numarası Bulunamamıştır..");
                }

                clearText();
            }
        });
        this.btnDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edNo.getText().toString().trim().length() == 0) {
                    showMessage("Hata !", "Lütfen Düzenlemek istediğiniz Numarayı Giriniz..");
                    return;
                }
                btnListe.setVisibility(View.VISIBLE);
                Cursor c = db.rawQuery("SELECT * FROM ogrenci WHERE ogrno=\'" + edNo.getText() + "\'", (String[]) null);
                if (c.moveToFirst()) {
                    Integer deger=Integer.parseInt(edVizeNot.getText().toString());
                    if(deger >100 || deger <0 )
                    {
                        showMessage("Hata", "Vize Notu 0-100 Arasında Olmalıdır");
                        return;
                    }
                    if (edAdSoy.getText().toString().trim().length() == 0) {
                        showMessage("Hata !", "Tüm Değerleri Doldurmanız Gerekmektedir..");
                        return;
                    }
                    //set sorgusu veritabanındakı verılerı guncelleme ıslemı yapmayı sağlar
                    db.execSQL("UPDATE ogrenci SET isim=\'" + edAdSoy.getText() + "\',vizenot=\'" + edVizeNot.getText() + "\' WHERE ogrno=\'" + edNo.getText() + "\'");
                    showMessage("Başarılı ", "Düzenleme Yapılmıştır.");
                }
                else {
                    showMessage("Hata !", "Bu numarada bir öğrenci bulunamamıştır..");
                }

                clearText();
                btnDuzenle.setVisibility(View.INVISIBLE);

            }
        });
        this.btnGoruntule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edNo.getText().toString().trim().length() == 0) {
                    showMessage("Hata !", "Lütfen Öğrenci Numarasını Giriniz..");
                    return;
                }
                //db de ogrno ya aıt degerlerı bulup Edittext'e yazar..
                Cursor c = db.rawQuery("SELECT * FROM ogrenci WHERE ogrno=\'" + edNo.getText() + "\'", (String[]) null);
                if (c.moveToFirst()) {
                    btnListe.setVisibility(View.INVISIBLE);
                    btnDuzenle.setVisibility(View.VISIBLE);

                    edAdSoy.setText(c.getString(1));

                    edVizeNot.setText(c.getString(2));
                } else {
                    showMessage("Hata !", "Bu numarada bir öğrenci bulunamamıştır..");
                    clearText();
                }


            }
        });

        this.btnListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.rawQuery("SELECT * FROM ogrenci", (String[]) null);
                if (c.getCount() == 0) {
                    showMessage("Hata", "Kayıtlı Öğrenci Yoktur");
                    return;
                }
                //buffer komutuyla listeleme işlemi için veri getirilir.
                StringBuffer buffer = new StringBuffer();

                while (c.moveToNext()) {
                    buffer.append("Öğrenci NO: " + c.getString(0) + "\n");
                    buffer.append("Ad Soyad: " + c.getString(1) + "\n");
                    buffer.append("Vize Notu: " + c.getString(2) + "\n\n");
                }

                showMessage("Öğrencilerin Detaylı Listesi", buffer.toString());

            }
        });
        }

    public void showMessage(String title, String message) {
        //hata mesajları ıcın cagırılan fonksıyon
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    };



    public void clearText() {
        this.edNo.setText("");
        this.edAdSoy.setText("");
        this.edVizeNot.setText("");
        this.edNo.requestFocus();
    };




}


