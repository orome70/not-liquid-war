package org.orome.liquidwar;

/*
This set of programs is an attempt to produce an Android clone of Liquid War 6.
Copyright (C)  2005, 2006, 2007, 2008, 2009, 2010, 2011  Christian Mauduit <ufoot@ufoot.org>
Liquid War 6 homepage : http://www.gnu.org/software/liquidwar6/

The present project is currently *not* connected to the original author of Liquid War.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Author: Orome orome@orome.org

*/


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