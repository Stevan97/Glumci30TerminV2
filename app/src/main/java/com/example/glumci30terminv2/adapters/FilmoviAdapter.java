package com.example.glumci30terminv2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.glumci30terminv2.R;
import com.example.glumci30terminv2.activities.DetailActivity;
import com.example.glumci30terminv2.db.model.Filmovi;

import java.util.List;

public class FilmoviAdapter extends BaseAdapter {

    private Context context;
    private List<Filmovi> filmoviList;

    public FilmoviAdapter(Context context, List<Filmovi> filmoviList) {
        this.context = context;
        this.filmoviList = filmoviList;
    }

    private Spannable message1 = null;
    private Spannable message2 = null;

    @Override
    public int getCount() {
        return filmoviList.size();
    }

    @Override
    public Object getItem(int position) {
        return filmoviList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.filmovi_adapter, null);

        TextView naziv = convertView.findViewById(R.id.filmovi_adapter_naziv);
        message1 = new SpannableString("Naziv Filma: ");
        message2 = new SpannableString(filmoviList.get(position).getImeFilma());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView zanr = convertView.findViewById(R.id.filmovi_adapter_zanr);
        message1 = new SpannableString("Zanr Filma: ");
        message2 = new SpannableString(filmoviList.get(position).getZanr());
        spannableStyle();
        zanr.setText(message1);
        zanr.append(message2);

        TextView godina = convertView.findViewById(R.id.filmovi_adapter_datum_izlaska);
        message1 = new SpannableString("Datum Izlaska Filma: ");
        message2 = new SpannableString(filmoviList.get(position).getGodinaIzlaska());
        spannableStyle();
        godina.setText(message1);
        godina.append(message2);


        return convertView;
    }

    public void refreshList(List<Filmovi> filmovis) {
        this.filmoviList.clear();
        this.filmoviList.addAll(filmovis);
        notifyDataSetChanged();
    }

    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
