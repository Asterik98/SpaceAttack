package com.example.spaceattack;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class OnePlayerAddData extends AppCompatActivity {
    EditText name;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oneadddata_activity);
        name=findViewById(R.id.name);
    }
    public void playClick(View v){
        if(name.getText().length()!=0) {
            username=String.valueOf(name.getText());
            Intent intent = new Intent(this, play1.class);
            intent.putExtra(play2.EXTRA_PLAYER, username);
            startActivity(intent);
        }else if(name.getText().length()==0){
            name.setError("Isi nama terlebih dahulu");
        }
    }


}
