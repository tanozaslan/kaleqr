package com.tanozaslan.kaleBarcodeScanner;

import java.util.ArrayList;

public class siparis {
	private ArrayList <urun> urunArrayList = new ArrayList<urun>();
	
	private String bayiiKodu;
	private String odemeSekli;
	private String nakliyeSekli;
	
	private String siparisZamani;
	private String teslimZamani;
	private String eMail;
	private String notlar;
	
	
	
	public siparis(){
		
	}
	
	public siparis(@SuppressWarnings("rawtypes") ArrayList urunArrayListField,String bayiiKoduField, String odemeSekliField, String nakliyeSekliField, String siparisZamaniField, String teslimZamaniField, String notlarField, String eMailField){
		setUrunArrayList(urunArrayListField);
		setBayiiKodu(bayiiKoduField);
		
		setOdemeSekli(odemeSekliField);
		setNakliyeSekli(nakliyeSekliField);		
		
		setSiparisZamani(siparisZamaniField);
		setTeslimZamani(teslimZamaniField);
		setNotlar(notlarField);
		setEMail(eMailField);
		
	}

	
	/*SET METHODS*/
	@SuppressWarnings("unchecked")
	public void setUrunArrayList(@SuppressWarnings("rawtypes") ArrayList urunArrayListField){
		urunArrayList=urunArrayListField;
	}
	
	public void setNotlar(String notlarField) {
		// TODO Auto-generated method stub
		notlar=notlarField;
	}

	public void setTeslimZamani(String teslimZamaniField) {
		// TODO Auto-generated method stub
		teslimZamani=teslimZamaniField;
	}

	public void setSiparisZamani(String siparisZamaniField) {
		// TODO Auto-generated method stub
		siparisZamani=siparisZamaniField;
	}


	public void setOdemeSekli(String odemeSekliField) {
		// TODO Auto-generated method stub
		odemeSekli=odemeSekliField;
	}

	public void setNakliyeSekli(String nakliyeSekliField) {
		// TODO Auto-generated method stub
		nakliyeSekli=nakliyeSekliField;
	}

	public void setBayiiKodu(String bayiiKoduField) {
		// TODO Auto-generated method stub
		bayiiKodu=bayiiKoduField;
	}
	
	public void setEMail(String eMailField){
		eMail=eMailField;
	}

	/*Get METHODS*/
	@SuppressWarnings("rawtypes")
	public ArrayList getUrunArrayList(){
		return urunArrayList;
	}
	
	public String getBayiiKodu(){
		return bayiiKodu;
	}
	
	public String getOdemeSekli(){
		return odemeSekli;
	}
	
	public String getNakliyeSekli(){
		return nakliyeSekli;
	}	
	
	public String getSiparisZamani(){
		return siparisZamani;
	}
	
	public String getTeslimZamani(){
		return teslimZamani;
	}
	
	public String getNotlar(){
		return notlar;
	}
	
	public String getEMail(){
		return eMail;
	}

}
