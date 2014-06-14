package com.tanozaslan.kaleBarcodeScanner;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class urunListeCustomBaseAdapter extends BaseAdapter {
	 private static ArrayList<urun> urunArrayList;
	 
	 private LayoutInflater mInflater;

	 public urunListeCustomBaseAdapter(Context context, ArrayList<urun> urunArray) {
		 urunArrayList = urunArray;
		 mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
	  return urunArrayList.size();
	 }

	 public Object getItem(int position) {
	  return urunArrayList.get(position);
	 }

	 public long getItemId(int position) {
	  return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
	  ViewHolder holder;
	  if (convertView == null) {
	   convertView = mInflater.inflate(R.layout.urunliste, null);
	   holder = new ViewHolder();
	   holder.txtKod = (TextView) convertView.findViewById(R.id.twListeUrunKod);
	   holder.txtIsim=(TextView) convertView.findViewById(R.id.twListeUrunIsim);
	   holder.txtMiktar = (TextView) convertView.findViewById(R.id.twListeUrunMiktar);
	   holder.txtBirim = (TextView) convertView.findViewById(R.id.twListeUrunBirim);

	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	  
	  holder.txtKod.setText(urunArrayList.get(position).getKod());
	  holder.txtIsim.setText(urunArrayList.get(position).getIsim());
	  holder.txtMiktar.setText(String.valueOf(urunArrayList.get(position).getSiparisMiktar()));
	  holder.txtBirim.setText(urunArrayList.get(position).getSiparisBirim());

	  return convertView;
	 }

	 static class ViewHolder {
	  TextView txtKod;
	  TextView txtIsim;
	  TextView txtMiktar;
	  TextView txtBirim;
	 }
	}