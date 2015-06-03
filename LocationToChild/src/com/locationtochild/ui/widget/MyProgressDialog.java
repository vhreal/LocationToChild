package com.locationtochild.ui.widget;

import com.locationtochild.ui.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProgressDialog extends Dialog{
	
	@SuppressWarnings("unused")
	private Context context = null;
	private static MyProgressDialog myProgressDialog = null;
	
	
	public MyProgressDialog(Context context) {
		super(context);
		this.context = context;
		
	}
	
	public MyProgressDialog(Context context, int theme) {  
        super(context, theme);  
    }
	
	public static MyProgressDialog createDialog(Context context){  
        myProgressDialog = new MyProgressDialog(context,R.style.myProgressDialog);  
        myProgressDialog.setContentView(R.layout.loading);  
        myProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;  
        myProgressDialog.setCanceledOnTouchOutside(false);//点击屏幕后不消失  
        return myProgressDialog;  
    } 
	
	public void onWindowFocusChanged(boolean hasFocus){  
        
        if (myProgressDialog == null){  
            return;  
        }  
          
        ImageView imageView = (ImageView) myProgressDialog.findViewById(R.id.loading_icon);  
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();  
        animationDrawable.start();  
    }
	
	public MyProgressDialog setTitile(String strTitle){  
        return myProgressDialog;  
    }
	
	public MyProgressDialog setMessage(String strMessage){  
        TextView tvMsg = (TextView)myProgressDialog.findViewById(R.id.loading_text);  
          
        if (tvMsg != null){  
            tvMsg.setText(strMessage);  
        }  
          
        return myProgressDialog;  
    }  
 
}
