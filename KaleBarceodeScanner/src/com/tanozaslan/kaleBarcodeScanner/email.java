package com.tanozaslan.kaleBarcodeScanner;

public class email {
	private int bayiiKodu;
	private int urunKodu;
	private String urunIsmi;
	private int urunMiktari;
	private int siparisZamani;
	private int teslimZamani;
	private String notlar;
	
	email(int bayiiKoduField, int urunKoduField, String urunIsmiField, int urunMiktariField, int siparisZamaniField, int teslimZamaniField, String notlarField){
		setBayiiKodu(bayiiKoduField);
		setUrunKodu(urunKoduField);
		setUrunIsmi(urunIsmiField);
		setUrunMiktari(urunMiktariField);
		setSiparisZamini(siparisZamaniField);
		setTeslimZamani(teslimZamaniField);
		setNotlar(notlarField);
	}

	
	/*SET METHODS*/
	public void setNotlar(String notlarField) {
		// TODO Auto-generated method stub
		notlar=notlarField;
	}

	public void setTeslimZamani(int teslimZamaniField) {
		// TODO Auto-generated method stub
		teslimZamani=teslimZamaniField;
	}

	public void setSiparisZamini(int siparisZamaniField) {
		// TODO Auto-generated method stub
		siparisZamani=siparisZamaniField;
	}

	public void setUrunMiktari(int urunMiktariField) {
		// TODO Auto-generated method stub
		urunMiktari=urunMiktariField;
	}

	public void setUrunIsmi(String urunIsmiField) {
		// TODO Auto-generated method stub
		urunIsmi=urunIsmiField;
	}

	public void setUrunKodu(int urunKoduField) {
		// TODO Auto-generated method stub
		urunKodu=urunKoduField;
	}

	public void setBayiiKodu(int bayiiKoduField) {
		// TODO Auto-generated method stub
		bayiiKodu=bayiiKoduField;
	}
	

	/*Get METHODS*/
	public int getBayiiKodu(){
		return bayiiKodu;
	}
	
	public int getUrunKodu(){
		return urunKodu;
	}
	
	public String getUrunIsmi(){
		return urunIsmi;
	}
	
	public int getUrunMiktari(){
		return urunMiktari;
	}
	
	public int getSiparisZamani(){
		return siparisZamani;
	}
	
	public int getTeslimZamani(){
		return teslimZamani;
	}
	
	public String getNotlar(){
		return notlar;
	}

}
