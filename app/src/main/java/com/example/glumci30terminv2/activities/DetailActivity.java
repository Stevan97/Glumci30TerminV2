package com.example.glumci30terminv2.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glumci30terminv2.R;
import com.example.glumci30terminv2.adapters.DrawerAdapter;
import com.example.glumci30terminv2.adapters.FilmoviAdapter;
import com.example.glumci30terminv2.db.DatabaseHelper;
import com.example.glumci30terminv2.db.model.Filmovi;
import com.example.glumci30terminv2.db.model.Glumac;
import com.example.glumci30terminv2.dialogs.AboutDialog;
import com.example.glumci30terminv2.model.NavigationItems;
import com.example.glumci30terminv2.tools.InputOcena;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String NOTIFY_MESSAGE = "notify";
    private static final String TOAST_MESSAGE = "toast";

    private int position = 1;
    private DatabaseHelper databaseHelper = null;

    private Glumac glumac = null;
    private Filmovi filmovi = null;

    private ForeignCollection<Filmovi> filmoviForeignCollection = null;
    private List<Filmovi> filmoviList = null;
    private ListView listViewDetail = null;
    private FilmoviAdapter filmoviAdapter = null;
    private Intent intentPosition = null;
    private int idPosition = 0;

    private Spannable message1 = null;
    private Spannable message2 = null;
    private Spannable message3 = null;
    private Toast toast = null;
    private View toastView = null;
    private TextView textToast = null;

    private Button cancel = null;
    private Button confirm = null;

    private boolean showMessage = false;
    private boolean showNotify = false;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItems> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        navigationDrawer();

        prikaziDetalje();

    }


    /**
     * Prikaz detalja Glumca i filmove u kojima ja glumio
     */
    @SuppressLint("SetTextI18n")
    private void prikaziDetalje() {
        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            glumac = getDatabaseHelper().getGlumac().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView naziv = findViewById(R.id.detail_naziv_glumca);
        message1 = new SpannableString("Naziv Glumca: ");
        message2 = new SpannableString(glumac.getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView biografija = findViewById(R.id.detail_biografija_glumca);
        message1 = new SpannableString("Biografija Glumca: ");
        message2 = new SpannableString(glumac.getBiografija());
        spannableStyle();
        biografija.setText(message1);
        biografija.append(message2);

        TextView datum = findViewById(R.id.detail_datum_rodjenja);
        message1 = new SpannableString("Datum Rodjenja: ");
        message2 = new SpannableString(glumac.getDatumRodjenja());
        spannableStyle();
        datum.setText(message1);
        datum.append(message2);

        RatingBar ratingBar = findViewById(R.id.detail_rating_bar);
        ratingBar.setRating(glumac.getOcenaGlumca());

        listViewDetail = findViewById(R.id.list_view_DETAIL);
        try {
            filmoviForeignCollection = getDatabaseHelper().getGlumac().queryForId(idPosition).getFilmovi();
            filmoviList = new ArrayList<>(filmoviForeignCollection);
            filmoviAdapter = new FilmoviAdapter(this, filmoviList);
            listViewDetail.setAdapter(filmoviAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Brisanje glumca i njegovih filmova
     */
    private void izbrisiGlumca() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.izbrisi_glumca);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            glumac = getDatabaseHelper().getGlumac().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView text = dialog.findViewById(R.id.izbrisi_glumca_text);
        message1 = new SpannableString("Da li ste sigurni da zelite da izbrisete Glumca pod nazivom: ");
        message2 = new SpannableString(glumac.getNaziv());
        spannableStyle();
        text.setText(message1);
        text.append(message2);

        confirm = dialog.findViewById(R.id.izbrisi_glumca_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                try {

                    filmoviForeignCollection = getDatabaseHelper().getGlumac().queryForId(idPosition).getFilmovi();
                    filmoviList = new ArrayList<>(filmoviForeignCollection);

                    getDatabaseHelper().getFilmovi().delete(filmoviList);
                    getDatabaseHelper().getGlumac().delete(glumac);
                    onBackPressed();

                    message1 = new SpannableString("Uspesno izbrisan Glumac sa nazivom:  ");
                    message2 = new SpannableString(glumac.getNaziv());
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        message3 = new SpannableString("Uspesno obrisan Glumac: " + glumac.getNaziv());
                        message3.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        showstatusBar2(message3);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        cancel = dialog.findViewById(R.id.izbrisi_glumca_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void dodajFilm() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dodaj_film);
        dialog.show();


        final EditText editNaziv = dialog.findViewById(R.id.dodaj_film_naziv);
        final EditText editZanr = dialog.findViewById(R.id.dodaj_film_zanr);
        final EditText editDatum = dialog.findViewById(R.id.dodaj_film_godina_izlaska);

        confirm = dialog.findViewById(R.id.dodaj_film_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNaziv.getText().toString().isEmpty()) {
                    editNaziv.setError("Polje Naziv Filma ne sme biti prazno");
                    return;
                }
                if (editZanr.getText().toString().isEmpty()) {
                    editZanr.setError("Polje Zanr ne sme biti prazno");
                    return;
                }
                if (editDatum.getText().toString().isEmpty() || editDatum.getText().toString().length() > 10 || !isValidDate(editDatum.getText().toString())) {
                    editDatum.setError("Format Datuma: dd-MM-yyyy sa - !");
                    return;
                }

                String naziv = editNaziv.getText().toString();
                String zanr = editZanr.getText().toString();
                String datum = editDatum.getText().toString();

                try {
                    intentPosition = getIntent();
                    idPosition = intentPosition.getExtras().getInt("id");
                    glumac = getDatabaseHelper().getGlumac().queryForId(idPosition);

                    filmovi = new Filmovi();
                    filmovi.setImeFilma(naziv);
                    filmovi.setZanr(zanr);
                    filmovi.setGodinaIzlaska(datum);
                    filmovi.setGlumac(glumac);

                    getDatabaseHelper().getFilmovi().create(filmovi);
                    osveziFilmove();
                    dialog.dismiss();

                    message1 = new SpannableString("Uspesno kreiran Film sa nazivom: ");
                    message2 = new SpannableString(filmovi.getImeFilma());
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        message3 = new SpannableString("Uspesno kreiran film sa nazivom: " + filmovi.getImeFilma());
                        message3.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        showstatusBar2(message3);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        cancel = dialog.findViewById(R.id.dodaj_film_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void osveziFilmove() {
        listViewDetail = findViewById(R.id.list_view_DETAIL);
        if (listViewDetail != null) {
            filmoviAdapter = (FilmoviAdapter) listViewDetail.getAdapter();
            if (filmoviAdapter != null) {
                intentPosition = getIntent();
                idPosition = intentPosition.getExtras().getInt("id");
                try {
                    filmoviForeignCollection = getDatabaseHelper().getGlumac().queryForId(idPosition).getFilmovi();
                    filmoviList = new ArrayList<>(filmoviForeignCollection);
                    filmoviAdapter.refreshList(filmoviList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void izmeniGlumca() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.izmeni_glumca);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        final EditText editNaziv = dialog.findViewById(R.id.izmeni_glumca_naziv);
        final EditText editBiografija = dialog.findViewById(R.id.izmeni_glumca_biografija);
        final EditText editOcena = dialog.findViewById(R.id.izmeni_glumca_nova_ocena);
        final EditText editDatum = dialog.findViewById(R.id.izmeni_glumca_nov_datumRodj);

        /** Provera da li je unos od 0 do 10 jer imamo 10 zvezdica za rating bar ! */
        InputFilter limitFilter = new InputOcena(0, 10);
        editOcena.setFilters(new InputFilter[]{limitFilter});

        confirm = dialog.findViewById(R.id.izmeni_glumca_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNaziv.getText().toString().isEmpty()) {
                    editNaziv.setError("Polje Naziv ne sme biti prazno !");
                    return;
                }
                if (editBiografija.getText().toString().isEmpty()) {
                    editBiografija.setError("Polje Biografija ne sme biti prazno !");
                    return;
                }
                if (editOcena.getText().toString().isEmpty()) {
                    editOcena.setError("Ocena od 0 do 10 !");
                    return;
                }
                if (editDatum.getText().toString().isEmpty() || editDatum.getText().toString().length() > 10
                        || editDatum.getText().toString().length() < 10
                        || !isValidDate(editDatum.getText().toString())) {
                    editDatum.setError("Date Format: dd-MM-yyyy with - !");
                    return;
                }

                String naziv = editNaziv.getText().toString();
                String biografija = editNaziv.getText().toString();
                float ocena = Float.parseFloat(editOcena.getText().toString());
                String datumRodj = editDatum.getText().toString();

                try {
                    glumac = getDatabaseHelper().getGlumac().queryForId(idPosition);

                    glumac.setNaziv(naziv);
                    glumac.setBiografija(biografija);
                    glumac.setOcenaGlumca(ocena);
                    glumac.setDatumRodjenja(datumRodj);

                    getDatabaseHelper().getGlumac().update(glumac);
                    dialog.dismiss();
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition(0,0);

                    message1 = new SpannableString("Uspesna izmena | Novo ime Glumca: ");
                    message2 = new SpannableString(glumac.getNaziv());
                    spannableStyle();
                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        message3 = new SpannableString("Uspena izmena | Novo ime Glumca: " + glumac.getNaziv());
                        message3.setSpan(new ForegroundColorSpan(Color.RED),0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        showstatusBar2(message3);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        cancel = dialog.findViewById(R.id.izmeni_glumca_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void consultPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
        showMessage = sharedPreferences.getBoolean(TOAST_MESSAGE, true);
        showNotify = sharedPreferences.getBoolean(NOTIFY_MESSAGE, false);
    }

    /** KORISTI OVU METODU NA TESTU !*/
    private void showStatusBarMessage(Spannable string) {

        message1 = new SpannableString("Uspesno!");
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailActivity.this);
        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentTitle(message1);
        builder.setContentText(string);

        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onResume() {
        osveziFilmove();
        consultPreferences();
        super.onResume();
    }

    /**
     * Navigaciona Fioka
     */
    private void navigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar_DETAIL);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_list);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        drawerItems.add(new NavigationItems("Glumci", "Prikazuje listu Glumaca", R.drawable.ic_show_all));
        drawerItems.add(new NavigationItems("Podesavanja", "Otvara Podesavanja Aplikacije", R.drawable.ic_settings));
        drawerItems.add(new NavigationItems("Informacije", "Informacije o Aplikaciji", R.drawable.ic_about_app));

        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        drawerListView = findViewById(R.id.nav_list_DETAIL);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DetailActivity.DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.drawer_layout_DETAIL);
        drawerPane = findViewById(R.id.drawer_pane_DETAIL);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerClosed(drawerView);
            }
        };

    }

    /**
     * OnItemClick iz NavigacioneFioke.
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                onBackPressed();
                overridePendingTransition(0, 0);
            } else if (position == 1) {
                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                AlertDialog aboutDialog = new AboutDialog(DetailActivity.this).prepareDialog();
                aboutDialog.show();
            }

            drawerLayout.closeDrawer(drawerPane);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_detail_delete:
                izbrisiGlumca();
                break;
            case R.id.menu_detail_add_movie:
                dodajFilm();
                break;
            case R.id.menu_detail_update:
                izmeniGlumca();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Proveravamo datum
     */
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }


    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showstatusBar2(Spannable string) {


        NotificationChannel notificationChannel = new NotificationChannel("NOTIFY_ID", "ReserveNotify", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(Color.GREEN);

        message1 = new SpannableString("Uspesno! ");
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailActivity.this);
        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentTitle(message1);
        builder.setContentText(string);
        builder.setChannelId("NOTIFY_ID");

        nm.notify(1, builder.build());
    }

}
