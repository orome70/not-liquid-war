package org.orome.liquidwar;

import org.orome.liquidwar.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/*
import android.widget.CheckBox;
import android.widget.EditText;
*/
import android.widget.Toast;

public class MainApp extends Activity {
	Testeur tt;
	TesteurOptim tt2;
	private void update(TextView txt, double val){
		String texte = "Le temps de la simulation : "+val;
		txt.setText(texte);
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	tt = new Testeur(5,5);
    	tt2 = new TesteurOptim(5,5);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView textRes = (TextView)findViewById(R.id.textView2);
        final Button lanceur = (Button) findViewById(R.id.button1);
        final Button lanceur2 = (Button) findViewById(R.id.button2);

        lanceur.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View v){
                // Performs action on clicks
				double res = tt.simulation(10);
                Toast.makeText(MainApp.this, "Simulation effectuée", Toast.LENGTH_SHORT).show();
                update(textRes,res);
            }
        });
        lanceur2.setOnClickListener(new OnClickListener() {
			@Override
            public void onClick(View v){
                // Performs action on clicks
				double res = tt2.simulation(10);
                Toast.makeText(MainApp.this, "Simulation optimisée effectuée", Toast.LENGTH_SHORT).show();
                update(textRes,res);
            }
        });
        
    }
}