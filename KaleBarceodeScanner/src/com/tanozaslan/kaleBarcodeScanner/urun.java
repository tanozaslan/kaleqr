package com.tanozaslan.kaleBarcodeScanner;

public class urun {
	private String isim;
	private String kod;
	private int siparisMiktar;
	private String siparisBirim; 
	
	//CONSTRUCTORS
	public urun(String urunKod, int urunSiparisMiktar){
		setIsim("no name");
		setKod(urunKod);
		setSiparisMiktar(urunSiparisMiktar);
		setSiparisBirim("no value");
		
	}
	public urun(String urunIsim, String urunKod, int urunSiparisMiktar, String urunSiparisBirim){
		setIsim(urunIsim);
		setKod(urunKod);
		setSiparisMiktar(urunSiparisMiktar);
		setSiparisBirim(urunSiparisBirim);
	}

	//SETS
	public void setIsim(String urunIsim) {
		isim=urunIsim;
	}
	public void setKod(String urunKod) {
		kod=urunKod;
		
	}
	public void setSiparisMiktar(int urunSiparisMiktar) {
		siparisMiktar=urunSiparisMiktar;
	}
	public void setSiparisBirim(String urunSiparisBirim) {
		siparisBirim=urunSiparisBirim;
		
	}
		
	//GETS
	public String getIsim(){
		return isim;
	}
	public String getKod(){
		return kod;
	}	
	public int getSiparisMiktar(){
		return siparisMiktar;
	}
	public String getSiparisBirim(){
		return siparisBirim;
	}
}
