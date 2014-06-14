package com.tanozaslan.kaleBarcodeScanner;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class KaleBarceodeScannerActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private Button btnPreview,btnSend;
	private Button btnPopUpPreviewDismiss;

	private EditText etBayiiKodu,etSiparisZaman,etTeslimZaman,etEmail,etNotes,etOdemeSekli,etNakliyeSekli;
	
	private String siparisEmail="tanozaslan@gmail.com";
	private String selectedEditText;
	private StringBuilder selectedDate;
	
	private siparis girilenSiparis; //ANA SIPARIS OBJESI
	
	private boolean allFilled=true;
	private PopupWindow pwPreview;
	
	private int checkedNakliyeRadioButtonId=0;
	private int checkedOdemeRadioButtonId=0;
	
	private int screenHeight,screenWidth;
	
	private SharedPreferences prefs;
	
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	
	ArrayList <urun> urunArrayList = new ArrayList<urun>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        prefs = getSharedPreferences("preference_list", 0);
        
        initializeUI();
        
        urunArrayList = urunEkle.urunArray; 
    }

	private void initializeUI() {
		
		girilenSiparis=new siparis();
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;
		
		Log.d(String.valueOf(screenHeight),String.valueOf(screenWidth));

		
		//Resources r = getResources();
    	//float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bannerWidth, r.getDisplayMetrics());
		//new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,getWindowManager().getDefaultDisplay().getHeight()-(int)px*2);
		
		final Calendar c = Calendar.getInstance();
	    mYear = c.get(Calendar.YEAR);
	    mMonth = c.get(Calendar.MONTH);
	    mDay = c.get(Calendar.DAY_OF_MONTH);
		
		//btnScan=(Button) findViewById(R.id.btnScan);
		//btnScan.setOnClickListener(this);
	    
	    
	    etOdemeSekli = (EditText)findViewById(R.id.etOdemeSekli);
	    etOdemeSekli.setOnClickListener(this);
	    
	    etNakliyeSekli = (EditText)findViewById(R.id.etNakliyeSekli);
	    etNakliyeSekli.setOnClickListener(this);
	    
		btnPreview=(Button) findViewById(R.id.btnPreview);
		btnPreview.setOnClickListener(this);
		
		btnSend=(Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(this);
		

		
		etBayiiKodu=(EditText) findViewById(R.id.etBayiiKodu);
		prefs = getSharedPreferences("preference_list", 0);
        //SharedPreferences.Editor prefsEditor = prefs.edit();
        
        if(!prefs.getString("bayiiKodu", "noBayiiKodu").equals("noBayiiKodu")){
        	etBayiiKodu.setText(prefs.getString("bayiiKodu", "noBayiiKodu"));
        }
				
		etEmail=(EditText) findViewById(R.id.etEmail);
		etEmail.setText(siparisEmail);
		girilenSiparis.setEMail(siparisEmail);
				
		etNotes=(EditText) findViewById(R.id.etNotes);
		
		//SIPARIS ZAMAN
		etSiparisZaman=(EditText) findViewById(R.id.etSiparisZaman);
		etSiparisZaman.setText(new StringBuilder()
	                // Month is 0 based so add 1
	        		.append(mDay).append("-")
	                .append(mMonth + 1).append("-")                
	                .append(mYear).append(" "));
		girilenSiparis.setSiparisZamani(etSiparisZaman.getText().toString());
		
		etSiparisZaman.setOnTouchListener(new OnTouchListener() {
	        public boolean onTouch(View v, MotionEvent event) {
	            // TODO Auto-generated method stub
	        	selectedEditText="siparisZaman";
	            showDialog(DATE_DIALOG_ID);
	            return false;
	        }
	    });
		
		//TESLIM ZAMAN
		etTeslimZaman=(EditText) findViewById(R.id.etTeslimZaman);
		etTeslimZaman.setOnTouchListener(new OnTouchListener() {
	        public boolean onTouch(View v, MotionEvent event) {
	            // TODO Auto-generated method stub
	        	selectedEditText="teslimZaman";
	            showDialog(DATE_DIALOG_ID);		            
	            return false;
	            
	        }
	    });
	
		
	}
 
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		/*case R.id.btnScan:
			runScan();
			break;*/
		case R.id.btnPreview:
			setSiparisDetails();
			inflatePopUpPreview();
			if(!allFilled)showErrorMessage();
			break;
		case R.id.btnSend:
			setSiparisDetails();
			Log.d("allFilled",String.valueOf(allFilled));
			//if(allFilled)sendEmail();
			if(allFilled)sendBackgroundEmail();
			else showErrorMessage(); 
			break;
		case R.id.btnPopUpDismiss:
			pwPreview.dismiss();
			break;
		case R.id.etOdemeSekli:
			inflatePopUpOdemeSekli();
			break;		
		case R.id.etNakliyeSekli:
			inflatePopUpNakliyeSekli();
			break;
		}
	}

	private void updateDateDisplay() {
		selectedDate=new StringBuilder()
        // Month is 0 based so add 1
		.append(mDay).append("-")
        .append(mMonth + 1).append("-")                
        .append(mYear).append(" ");
		
		if(selectedEditText.equals("siparisZaman")){
			etSiparisZaman.setText(selectedDate);
			//girilenSiparis.setSiparisZamani(String.valueOf(selectedDate));
		}else if(selectedEditText.equals("teslimZaman")){
			etTeslimZaman.setText(selectedDate);
			//girilenSiparis.setTeslimZamani(String.valueOf(selectedDate));
		}
		
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	   switch (id) {
	   case DATE_DIALOG_ID:
	      return new DatePickerDialog(this,
	                mDateSetListener,
	                mYear, mMonth, mDay);
	   }
	   return null;
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
		    new DatePickerDialog.OnDateSetListener() {
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            mYear = year;
		            mMonth = monthOfYear;
		            mDay = dayOfMonth;
		            updateDateDisplay();
		        }
		    };
		    	    		
    private void setSiparisDetails(){
    	
    	//URUNLER
    	girilenSiparis.setUrunArrayList(urunArrayList);
    	
    	//BAYII KODU
    	if(etBayiiKodu.getText().toString().matches("")){ 
    		etBayiiKodu.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
    		etBayiiKodu.invalidate();
    		allFilled=allFilled&&false;
    	}
    	else{
    		girilenSiparis.setBayiiKodu(String.valueOf(etBayiiKodu.getText()));
    		etBayiiKodu.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
    		etBayiiKodu.invalidate();
    		allFilled=allFilled&&true;
    		
    		prefs = getSharedPreferences("preference_list", 0);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            if(!prefs.getString("bayiiKodu", "noBayiiKodu").equals(girilenSiparis.getBayiiKodu())||prefs.getString("bayiiKodu", "noBayiiKodu").equals("noBayiiKodu")){
            	prefsEditor.putString("bayiiKodu", girilenSiparis.getBayiiKodu());
            	prefsEditor.commit();
            }
    	}
    	
    	//ODEME SEKLİ
    	if(etOdemeSekli.getText().toString().matches("")){ 
    		etOdemeSekli.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
    		etOdemeSekli.invalidate();
    		allFilled=allFilled&&false;
    	}
    	else{
    		girilenSiparis.setOdemeSekli(String.valueOf(etOdemeSekli.getText()));
    		etOdemeSekli.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
    		etOdemeSekli.invalidate();
    		allFilled=allFilled&&true;
    	}
    	
    	//NAKLIYE SEKLI
    	if(etNakliyeSekli.getText().toString().matches("")){ 
    		etNakliyeSekli.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
    		etNakliyeSekli.invalidate();
    		allFilled=allFilled&&false;
    	}
    	else{
    		girilenSiparis.setNakliyeSekli(String.valueOf(etNakliyeSekli.getText()));
    		etNakliyeSekli.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
    		etNakliyeSekli.invalidate();
    		allFilled=allFilled&&true;
    	}
    	   	
    	//SIPARIS ZAMAN
    	if(etSiparisZaman.getText().toString().matches("")){ 
    		etSiparisZaman.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
    		etSiparisZaman.invalidate();
    		allFilled=allFilled&&false;
    	}
    	else{
    		girilenSiparis.setSiparisZamani(String.valueOf(etSiparisZaman.getText()));
    		etSiparisZaman.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
    		etSiparisZaman.invalidate();
    		allFilled=allFilled&&true;
    	}
    	  	
    	//TESLIM ZAMANI
    	if(etTeslimZaman.getText().toString().matches("")){ 
    		etTeslimZaman.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
    		etTeslimZaman.invalidate();
    		allFilled=allFilled&&false;
    	}
    	else{
    		girilenSiparis.setTeslimZamani(String.valueOf(etTeslimZaman.getText()));
    		etTeslimZaman.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
    		etTeslimZaman.invalidate();
    		allFilled=allFilled&&true;
    	}
    	
    	//EMail
    	if(etEmail.getText().toString().matches("")){ 
    		etEmail.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
    		etEmail.invalidate();
    		allFilled=allFilled&&false;
    	}
    	else{
    		girilenSiparis.setEMail(String.valueOf(etEmail.getText()));
    		etEmail.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
    		etEmail.invalidate();
    		allFilled=allFilled&&true;
    	}
    	
    	
    	girilenSiparis.setNotlar(etNotes.getText().toString());
    }
		
    private void showErrorMessage(){
    	Toast toast = Toast.makeText(this, getString(R.string.fieldEmptyError), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		allFilled=true;
    }
    
	private void sendEmail(){

		/*Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, etEmail.getText().toString());
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, girilenSiparis.getBayiiKodu());
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, girilenSiparis.getUrunKodu());
		startActivity(emailIntent);*/
		//String urunString="";
		
		
		String[] recipients = new String[]{girilenSiparis.getEMail()};
		String subject = girilenSiparis.getBayiiKodu()+" Siparis";
		
		String bodyLine1=girilenSiparis.getBayiiKodu() + " nolu bayiinin, " + girilenSiparis.getSiparisZamani()+" tarihli sipariş detaylari:";
		
		
		String terminTarihi="Termin Tarihi : "+girilenSiparis.getTeslimZamani();
		String odemeSekli="Ödeme Şekli : "+girilenSiparis.getOdemeSekli();
		String nakliyeSekli="Nakliye Şekli : "+girilenSiparis.getNakliyeSekli();
		String notlar= girilenSiparis.getNotlar();
		String urunler="\tÜrünler:\n"+"\n"+getUrunler();
		
		String eMailBody=bodyLine1+"\n\n"+terminTarihi+"\n"+odemeSekli+"\n"+nakliyeSekli+"\n\n"+urunler+"\n\n"+notlar;
		
		Log.d("subject",girilenSiparis.getBayiiKodu()+" bayinin"+girilenSiparis.getSiparisZamani()+" tarihli siparisi");
		Log.d("body",girilenSiparis.getBayiiKodu() + " nolu bayiinin, " + girilenSiparis.getSiparisZamani()+" tarihli sipariş detaylari:");
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, eMailBody);
		//emailIntent.setType("text/plain");
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));

	}
	
    
	private void sendBackgroundEmail(){
		Mail m = new Mail("tanozaslan@gmail.com", "hakan2492"); 
		
		
		/*-----------------*/
		
		String[] recipients = new String[]{girilenSiparis.getEMail()};
		String subject = girilenSiparis.getBayiiKodu()+" Siparis";
		
		String bodyLine1=girilenSiparis.getBayiiKodu() + " nolu bayiinin, " + girilenSiparis.getSiparisZamani()+" tarihli sipariş detaylari:";
		
		
		String terminTarihi="Termin Tarihi : "+girilenSiparis.getTeslimZamani();
		String odemeSekli="Ödeme Şekli : "+girilenSiparis.getOdemeSekli();
		String nakliyeSekli="Nakliye Şekli : "+girilenSiparis.getNakliyeSekli();
		String notlar= girilenSiparis.getNotlar();
		String urunler="\tÜrünler:\n"+"\n"+getUrunler();
		
		String eMailBody=bodyLine1+"\n\n"+terminTarihi+"\n"+odemeSekli+"\n"+nakliyeSekli+"\n\n"+urunler+"\n\n"+notlar;
		
		/*-----------------*/
		
		 
	    String[] toArr = {"tanozaslan@gmail.com", "tan@codigomobile.com"};
	      
	    m.setTo(recipients); 
	    m.setFrom("wooo@wooo.com"); 
	    m.setSubject(subject); 
	    m.setBody(eMailBody); 
	 
	    try { 
	      //m.addAttachment("/sdcard/filelocation"); 
	 
	     if(m.send()) { 
	        Toast.makeText(KaleBarceodeScannerActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
	      } else { 
	        Toast.makeText(KaleBarceodeScannerActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
	      } 
	     } catch(Exception e) { 
	       //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
	       Log.e("MailApp", "Could not send email", e); 
	     } 
	}

	
	private void inflatePopUpPreview(){
		LayoutInflater inflater = (LayoutInflater)
		this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pwPreview = new PopupWindow(inflater.inflate(R.layout.previewpopup, null, false),470,500,true);		    
		pwPreview.showAtLocation(this.findViewById(R.id.llMain), Gravity.CENTER, 0, 0);
		View myPoppyView = pwPreview.getContentView();
		btnPopUpPreviewDismiss=(Button)myPoppyView.findViewById(R.id.btnPopUpDismiss);
		btnPopUpPreviewDismiss.setOnClickListener(this);
		
		TextView tv1PopUp=new TextView(this);
		tv1PopUp = (TextView)myPoppyView.findViewById(R.id.tv1PopUp);
		tv1PopUp.setText(girilenSiparis.getBayiiKodu() + " nolu bayiinin, " + girilenSiparis.getSiparisZamani()+" tarihli sipariş detaylari:");
			
		TextView tvUrunSiparisZamanPopUp=new TextView(this);
		tvUrunSiparisZamanPopUp=(TextView)myPoppyView.findViewById(R.id.tvUrunSiparisZamanPopUp);
		tvUrunSiparisZamanPopUp.setText("Termin Tarihi :"+girilenSiparis.getTeslimZamani());
		
		TextView tvUrunOdemeSekliPopUp=new TextView(this);
		tvUrunOdemeSekliPopUp=(TextView)myPoppyView.findViewById(R.id.tvUrunOdemeSekliPopUp);
		tvUrunOdemeSekliPopUp.setText("Ödeme Şekli :"+girilenSiparis.getOdemeSekli());
		
		TextView tvUrunNakliyeSekliPopUp=new TextView(this);
		tvUrunNakliyeSekliPopUp=(TextView)myPoppyView.findViewById(R.id.tvUrunNakliyeSekliPopUp);
		tvUrunNakliyeSekliPopUp.setText("Nakliye Şekli :"+girilenSiparis.getNakliyeSekli());
		
		TextView tvUrunKodIsimPopUp=new TextView(this);
		tvUrunKodIsimPopUp=(TextView)myPoppyView.findViewById(R.id.tvUrunlerPopUp);
		tvUrunKodIsimPopUp.setText(getUrunler());
		
		TextView tvNotlarPopUp=new TextView(this);
		tvNotlarPopUp=(TextView)myPoppyView.findViewById(R.id.tvNotlarPopUp);
		tvNotlarPopUp.setText(girilenSiparis.getNotlar());
		
	}
            
    private void inflatePopUpOdemeSekli(){
    	Dialog dialog = new Dialog(this);

    	dialog.setContentView(R.layout.odemeseklipopup);
    	
    	WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    	lp.dimAmount=0.0f;
    	dialog.getWindow().setAttributes(lp);
    	dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    	   
    	RadioGroup rgOdemeSekli;

    	RadioButton rbPesin, rbTaksit;
    	
    	LayoutInflater inflater = (LayoutInflater)
    	this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	final PopupWindow popOdemeSekli = new PopupWindow(inflater.inflate(R.layout.odemeseklipopup, null, false),400,300,true);		
    	popOdemeSekli.showAtLocation(this.findViewById(R.id.llMain), Gravity.CENTER, 0, 0);
    	
    	
    	View myPoppyView = popOdemeSekli.getContentView();
    	
    	
    	
    	
    	rgOdemeSekli=(RadioGroup)myPoppyView.findViewById(R.id.rgOdemeSekli);

    	
    	OnClickListener radio_listener = new OnClickListener() {
    	    public void onClick(View v) {
    	        // Perform action on clicks
    	        RadioButton rb = (RadioButton) v;    	        
    	        etOdemeSekli.setText(rb.getText().toString());
    	        checkedOdemeRadioButtonId=rb.getId();
    	        
    	        girilenSiparis.setOdemeSekli(rb.getText().toString());//SETTING the ODEME SEKLI
    	        
    	        popOdemeSekli.dismiss();
    	    }
    	};
		rbPesin=(RadioButton)myPoppyView.findViewById(R.id.rdPesin);
    	rbPesin.setOnClickListener(radio_listener);
    	
    	rbTaksit=(RadioButton)myPoppyView.findViewById(R.id.rdTaksit);
    	rbTaksit.setOnClickListener(radio_listener);
    	
    	if(checkedOdemeRadioButtonId!=0)
    		rgOdemeSekli.check(checkedOdemeRadioButtonId);
    	
    }
    
    private void inflatePopUpNakliyeSekli(){
    	
    	RadioGroup rgNakliyeSekli;
    	RadioButton rbKamyon,rbTren,rbGemi,rbKargo,rbKendiAraci;
    	
    	LayoutInflater inflater = (LayoutInflater)
    	this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	final PopupWindow popNakliyeSekli = new PopupWindow(inflater.inflate(R.layout.nakliyeseklipopup, null, false),400,600,true);		    
    	popNakliyeSekli.showAtLocation(this.findViewById(R.id.llMain), Gravity.CENTER, 0, 0);
    	View myPoppyView = popNakliyeSekli.getContentView();
    	
    	rgNakliyeSekli=(RadioGroup)myPoppyView.findViewById(R.id.rgNakliyeSekli);
    	
    	
    	OnClickListener radio_listener = new OnClickListener() {
    	    public void onClick(View v) {
    	        // Perform action on clicks
    	        RadioButton rb = (RadioButton) v;    	        
    	        etNakliyeSekli.setText(rb.getText().toString());
    	        checkedNakliyeRadioButtonId=rb.getId();
    	        
    	        girilenSiparis.setNakliyeSekli(rb.getText().toString());//SETTING the NAKLIYE SEKLI
    	        
    	        popNakliyeSekli.dismiss();
    	    }
    	};
    	rbKamyon=(RadioButton)myPoppyView.findViewById(R.id.rdKamyon);
    	rbKamyon.setOnClickListener(radio_listener);
    	
    	rbTren=(RadioButton)myPoppyView.findViewById(R.id.rdTren);
    	rbTren.setOnClickListener(radio_listener);
    	
    	rbGemi=(RadioButton)myPoppyView.findViewById(R.id.rdGemi);
    	rbGemi.setOnClickListener(radio_listener);
    	
    	rbKargo=(RadioButton)myPoppyView.findViewById(R.id.rdKargo);
    	rbKargo.setOnClickListener(radio_listener);
    	
    	rbKendiAraci=(RadioButton)myPoppyView.findViewById(R.id.rdKendiAraci);
    	rbKendiAraci.setOnClickListener(radio_listener);   	
    	
    	if(checkedNakliyeRadioButtonId!=0)
    		rgNakliyeSekli.check(checkedNakliyeRadioButtonId);
    }
       
    private String getUrunler(){
    	String urunString="";
    	for(int urunIndex=0;urunIndex<urunArrayList.size(); urunIndex++){
			//urunString=urunString+"\t"+urunArrayList.get(urunIndex).getKod()+" - "+urunArrayList.get(urunIndex).getIsim()+"\t"+String.valueOf(urunArrayList.get(urunIndex).getSiparisMiktar())+urunArrayList.get(urunIndex).getSiparisBirim()+"\n";		
			urunString=urunString+urunArrayList.get(urunIndex).getKod()+" - "+String.valueOf(urunArrayList.get(urunIndex).getSiparisMiktar())+urunArrayList.get(urunIndex).getSiparisBirim()+"\n"+urunArrayList.get(urunIndex).getIsim()+"\n"+"-----"+"\n";
    	}
    	
    	return urunString;
    }
  	 
}

	