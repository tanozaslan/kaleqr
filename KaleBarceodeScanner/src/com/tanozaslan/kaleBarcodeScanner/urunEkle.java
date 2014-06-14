package com.tanozaslan.kaleBarcodeScanner;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class urunEkle extends ListActivity implements OnClickListener{
	
	static ArrayList <urun> urunArray = new ArrayList<urun>();
		
	public int numQue[] = {-1,-1,-1,-1,-1};
	private int digit=0;
	private int numBuff;

	private Button btnQrTara,btnSiparisEkran;
	private EditText etPopUpUrunMiktar, etPopUpUrunBirim;
	
	boolean urunListeEditFlag=false;
	boolean editCase=false;
	int urunArrayPosition;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.urunekle);
		initializeUI();
		
		Log.d("11111","1111");
		if(!urunArray.isEmpty()) showUrunList();
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener(){
	        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int row, long arg3) {
	        	runLongClick(row);
	        	return true;
	           }});
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("KaleQR uygulamasından çıkmak istediğinize emin misiniz?")
    	       .setCancelable(false)
    	       .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                urunEkle.this.finish();
    	           }
    	       })
    	       .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	private void showUrunList() {
        setListAdapter(new urunListeCustomBaseAdapter(this, urunArray));		
	}

	private void initializeUI() {
		
		
		btnQrTara = (Button) findViewById(R.id.btnScan);
		btnQrTara.setOnClickListener(this);
		
		btnSiparisEkran=(Button) findViewById(R.id.btnSiparisEkran);
		btnSiparisEkran.setOnClickListener(this);		
				
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		editCase=true;
		
		super.onListItemClick(l, v, position, id);
				
		urunListeEditFlag=true;
		urunArrayPosition=position;
		
		inflatePopUpSiparis(urunArray.get(position).getKod(), urunArray.get(position).getIsim());
		
		etPopUpUrunMiktar.setText(String.valueOf(urunArray.get(position).getSiparisMiktar()));
		etPopUpUrunBirim.setText(urunArray.get(position).getSiparisBirim());
				
	}
	
	protected void runLongClick(int row){
		Log.d("UZUN",String.valueOf(row));
		urunArray.remove(row);
		showUrunList();
	}

	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btnScan:
			runScan();
			break;
		case R.id.btnSiparisEkran:			
			goToSiparisEkran();			
			break;
		}
		
	}
	
	private void goToSiparisEkran() {		
		if(urunArray.isEmpty()){
			Toast toast = Toast.makeText(this, getString(R.string.urunArrayEmptyError), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			Intent intentSiparisEkran = new Intent(urunEkle.this, KaleBarceodeScannerActivity.class);
			startActivity(intentSiparisEkran);
        }	
	}

	private void runScan(){
		
		urunListeEditFlag=false;
		editCase=false;
		
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String [] urunKodIsim;	
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         Log.d("BARCODE",contents);
		         
		         urunKodIsim=parseQrContent(contents);
		         
		         Log.d("CONTENT",contents);
		                  
		         inflatePopUpSiparis(urunKodIsim[0],urunKodIsim[1]);
		         
		         //girilenSiparis.setUrunKodu(contents);
		         // Handle successful scan
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
		      }
		   }
		}
	
	private String[] parseQrContent(String content){
		String [] urunKodIsim = new String [2];
		String finishString="&/&";
		String codeQue="code";
		String nameQue="name";
		
		Log.d("CONTENT 1",content);
		//content=content.replace(" ","");
		Log.d("CONTENT 2",content);
		
		//Code Parsing
		if(content.indexOf("code")==-1){  
			urunKodIsim[0]="no code";		
		}else{			
			int finishIndex=content.indexOf(finishString,content.indexOf(codeQue));		
			urunKodIsim[0]=content.substring(content.indexOf(codeQue)+codeQue.length(),finishIndex);		
			Log.d("Code",urunKodIsim[0]);
		}
		
		//Name Parsing		
		if(content.indexOf("name")==-1){  
			urunKodIsim[1]="no name";		
		}else{			
			int finishIndex=content.indexOf(finishString,content.indexOf(nameQue));		
			urunKodIsim[1]=content.substring(content.indexOf(nameQue)+nameQue.length(),finishIndex);		
			Log.d("Name",urunKodIsim[1]);
		}
	
		return urunKodIsim;
		
	}
	
	private void inflatePopUpSiparis(final String urunKod, final String urunIsim){
		
		final LinearLayout llFooter =(LinearLayout) findViewById(R.id.footer);
 		final RelativeLayout.LayoutParams llListParams = (RelativeLayout.LayoutParams)llFooter.getLayoutParams();
 		llFooter.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,1));
 	
		//llListParams.height=0;
		
		
		clearSiparisKeyboardCache();
		
		TextView tvUrunKod,tvUrunIsim;
		
		Button btnPopUpIptal,btnPopUpEkle;
		Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btnDEL;
		
		final RadioButton rbM2, rbAdet, rbKutu, rbPalet;

    	LayoutInflater inflater = (LayoutInflater)
    	this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	final PopupWindow pwSiparis = new PopupWindow(inflater.inflate(R.layout.siparismiktarpopup, null, false),480,650,true);		    
    	pwSiparis.showAtLocation(this.findViewById(R.id.rlMainUrunEkle), Gravity.CENTER, 0, 0);
    	//pwSiparis.setFocusable(true);
    	View myPopUpSiparisView = pwSiparis.getContentView();
    	
    	//POPULATE URUN BILGI
    	tvUrunKod = (TextView)myPopUpSiparisView.findViewById(R.id.tvPopUpUrunKod);
    	tvUrunIsim = (TextView)myPopUpSiparisView.findViewById(R.id.tvPopUpUrunIsim);
    	tvUrunKod.setText(urunKod);
    	tvUrunIsim.setText(urunIsim);
    	
    	//RADIO BUTTONS   	
    	etPopUpUrunBirim=(EditText)myPopUpSiparisView.findViewById(R.id.etPopUpUrunBirim);
    	
    	rbM2=(RadioButton)myPopUpSiparisView.findViewById(R.id.rbm2);
    	rbM2.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	etPopUpUrunBirim.setText(rbM2.getText().toString());
          }
    	});
    	
    	rbAdet=(RadioButton)myPopUpSiparisView.findViewById(R.id.rbadet);
    	rbAdet.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	etPopUpUrunBirim.setText(rbAdet.getText().toString());
          }
    	});
    	
    	rbKutu=(RadioButton)myPopUpSiparisView.findViewById(R.id.rbkutu);
    	rbKutu.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	etPopUpUrunBirim.setText(rbKutu.getText().toString());
          }
    	});
    	
    	rbPalet=(RadioButton)myPopUpSiparisView.findViewById(R.id.rbpalet);
    	rbPalet.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	etPopUpUrunBirim.setText(rbPalet.getText().toString());
          }
    	});
    	
    	//KLAVYE  	
    	etPopUpUrunMiktar=(EditText)myPopUpSiparisView.findViewById(R.id.etPopUpUrunMiktar);
    	
    	btn1=(Button) myPopUpSiparisView.findViewById(R.id.btn1);
		btn1.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(1);
          }
    	});
		
		btn2=(Button) myPopUpSiparisView.findViewById(R.id.btn2);
		btn2.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(2);
          }
    	});
		
		btn3=(Button) myPopUpSiparisView.findViewById(R.id.btn3);
		btn3.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(3);
           }
    	});
		
		btn4=(Button) myPopUpSiparisView.findViewById(R.id.btn4);
		btn4.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(4);
          }
    	});
		
		btn5=(Button) myPopUpSiparisView.findViewById(R.id.btn5);
		btn5.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(5);
          }
    	});
		
		btn6=(Button) myPopUpSiparisView.findViewById(R.id.btn6);
		btn6.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(6);
          }
    	});
		
		btn7=(Button) myPopUpSiparisView.findViewById(R.id.btn7);
		btn7.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(7);
          }
    	});
		
		btn8=(Button) myPopUpSiparisView.findViewById(R.id.btn8);
		btn8.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(8);
          }
    	});
		
		btn9=(Button) myPopUpSiparisView.findViewById(R.id.btn9);
		btn9.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(9);
          }
    	});
		
		btn0=(Button) myPopUpSiparisView.findViewById(R.id.btn0);
		btn0.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	insertQueElement(0);
          }
    	});
				
		btnDEL=(Button) myPopUpSiparisView.findViewById(R.id.btnDEL);
		btnDEL.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	deleteQueElement();
          }
    	});   	    		
    	
    	btnPopUpIptal=(Button)myPopUpSiparisView.findViewById(R.id.btnPopUpIptal);
    	btnPopUpIptal.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	pwSiparis.dismiss();
    			llFooter.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
    			llListParams.addRule(RelativeLayout.ALIGN_BOTTOM);
    			llFooter.setLayoutParams(llListParams);
          }
    	});
    	
    	btnPopUpEkle=(Button)myPopUpSiparisView.findViewById(R.id.btnPopUpEkle);
    	if(editCase) btnPopUpEkle.setText("GÜNCELLE");
    	else btnPopUpEkle.setText("EKLE");
    	btnPopUpEkle.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) {
            	
            	if(etPopUpUrunMiktar.getText().toString().matches("")||etPopUpUrunBirim.getText().toString().matches("")){
	            	if(etPopUpUrunMiktar.getText().toString().matches("")){
	            		etPopUpUrunMiktar.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
	            		etPopUpUrunMiktar.invalidate();
	            	}
	            	else{
	            		etPopUpUrunMiktar.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
	            		etPopUpUrunMiktar.invalidate();
	            	}
	            	if(etPopUpUrunBirim.getText().toString().matches("")){
	            		etPopUpUrunBirim.getBackground().setColorFilter(Color.rgb(255, 133, 145),Mode.MULTIPLY);
	            		etPopUpUrunBirim.invalidate();
	            	}
	            	else{
	            		etPopUpUrunBirim.getBackground().setColorFilter(Color.WHITE,Mode.MULTIPLY);
	            		etPopUpUrunBirim.invalidate();
	            	}
	            	
	            	showErrorMessage();
            	}
            	else{
            		if(urunListeEditFlag){
            			urunArray.set(urunArrayPosition, new urun(urunIsim,urunKod,Integer.parseInt(etPopUpUrunMiktar.getText().toString()),etPopUpUrunBirim.getText().toString()));
            			pwSiparis.dismiss();  
            			showUrunList(); 
            			llFooter.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            			llListParams.addRule(RelativeLayout.ALIGN_BOTTOM);
            			llFooter.setLayoutParams(llListParams);
            		}
            		else{
            			urunArray.add(new urun(urunIsim,urunKod,Integer.parseInt(etPopUpUrunMiktar.getText().toString()),etPopUpUrunBirim.getText().toString()));        		
            			pwSiparis.dismiss(); 
            			showUrunList(); 
            			llFooter.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            			llListParams.addRule(RelativeLayout.ALIGN_BOTTOM);
            			llFooter.setLayoutParams(llListParams);

            		}
            	}
          }
    	});
    	
    	
    }
	
	private void insertQueElement(int inputNum) {
		// TODO Auto-generated method stub
		if (!(digit==0 && inputNum==0)){
			if (digit<numQue.length){
				numQue[digit]=inputNum;
				digit++;
				Log.d("digit",String.valueOf(digit));
				showNumber();			
			}
		}
	}
	
	private void showNumber(){
		numBuff=0;
		if(digit!=numQue.length||digit!=0){
			for(int k=0; k<digit;k++){			
				numBuff=(int) (numBuff+numQue[k]*(java.lang.Math.pow(10,(digit-(k+1)))));
			}
		}
		etPopUpUrunMiktar.setText(String.valueOf(numBuff));
	}
	
	private void deleteQueElement(){	
		if (digit==0){
			numQue[digit]=-1;			
			showNumber();
			}else{
			digit--;
			numQue[digit]=-1;			
			showNumber();
		}
		Log.d("digit",String.valueOf(digit));
	}
	
	private void clearSiparisKeyboardCache(){
		digit=0;
		for(int i=0; i<numQue.length; i++)
			numQue[i] = -1;
	}	

	private void showErrorMessage(){
    	Toast toast = Toast.makeText(this, getString(R.string.fieldEmptyError), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
    }
}
