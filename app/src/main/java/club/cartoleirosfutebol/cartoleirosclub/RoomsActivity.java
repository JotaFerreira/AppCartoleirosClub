package club.cartoleirosfutebol.cartoleirosclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class RoomsActivity extends AppCompatActivity {

    private ImageView imgAtacantes;
    private ImageView imgMeias;
    private ImageView imgLaterais;
    private ImageView imgZagueiros;
    private ImageView imgGoleiros;
    private ImageView imgTecnicos;
    private ImageView imgCartoleiros;

    public static final String _URLMAIN = "http://cartoleirosfutebol.club/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cartoleiros Messenger");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgAtacantes = (ImageView) findViewById(R.id.atacantesImageView);
        imgMeias = (ImageView) findViewById(R.id.meiasImageView);
        imgZagueiros = (ImageView) findViewById(R.id.zagueirosImageView);
        imgLaterais = (ImageView) findViewById(R.id.lateraisImageView);
        imgGoleiros = (ImageView) findViewById(R.id.goleirosImageView);
        imgTecnicos = (ImageView) findViewById(R.id.tecnicosImageView);
        imgCartoleiros = (ImageView) findViewById(R.id.cartoleirosImageView);

        imgAtacantes.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivityByRoom("_atacantes");
            }
        });

        imgMeias.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivityByRoom("_meias");
            }
        });

        imgZagueiros.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivityByRoom("_zagueiros");
            }
        });

        imgLaterais.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivityByRoom("_laterais");
            }
        });

        imgGoleiros.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivityByRoom("_goleiros");
            }
        });

        imgTecnicos.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivityByRoom("_tecnicos");
            }
        });

        imgCartoleiros.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivityByRoom("");
            }
        });

        loadRoomImages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startActivityByRoom(String room){
        Intent msnIntent = new Intent(RoomsActivity.this, MessengerActivity.class);
        msnIntent.putExtra("room","messages" + room);
        startActivity(msnIntent);
    }

    private void loadRoomImages(){

        String imgUrlAtacantes = _URLMAIN + "/img/atacantes.jpg";
        String imgUrlMeias = _URLMAIN + "/img/meias.jpg";
        String imgUrlLaterais = _URLMAIN + "/img/laterais.jpg";
        String imgUrlZagueiros = _URLMAIN + "/img/zagueiros.jpg";
        String imgUrlGoleiros = _URLMAIN + "/img/goleiros.jpg";
        String imgUrlTecnicos = _URLMAIN + "/img/tecnicos.jpg";
        String imgUrlCartoleiros = _URLMAIN + "/img/logo_banner.png";

        Glide.with(this).load(imgUrlAtacantes).into(imgAtacantes);
        Glide.with(this).load(imgUrlMeias).into(imgMeias);
        Glide.with(this).load(imgUrlLaterais).into(imgLaterais);
        Glide.with(this).load(imgUrlZagueiros).into(imgZagueiros);
        Glide.with(this).load(imgUrlGoleiros).into(imgGoleiros);
        Glide.with(this).load(imgUrlTecnicos).into(imgTecnicos);
        Glide.with(this).load(imgUrlCartoleiros).into(imgCartoleiros);

    }

}
