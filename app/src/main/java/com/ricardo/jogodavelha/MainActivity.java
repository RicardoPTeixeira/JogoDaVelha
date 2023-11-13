package com.ricardo.jogodavelha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ricardo.jogodavelha.R;

import java.util.Arrays;
import java.util.Objects;
public class MainActivity extends AppCompatActivity {

    // Atributo: referência para o nosso banco de dados!
    // Esta referência "aponta" para o nó RAIZ da árvore!
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();

    private final String [] table = {"0", "0", "0", "0", "0", "0", "0", "0", "0"};
    private final ImageView [] images = new ImageView[9];

    private final TextView[] texts = new TextView[9];
    private String turn = "X";
    private int playerWins = 0;
    private int computerWins = 0;

    public boolean isGameOver = false;

    private int NumeroDeJogos = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeButtons();
        intializeViews();
        onClickViews();
        intializeTexts();

    }

    private void intializeViews(){
        images[0] = findViewById(R.id.img0);
        images[1] = findViewById(R.id.img1);
        images[2] = findViewById(R.id.img2);
        images[3] = findViewById(R.id.img3);
        images[4] = findViewById(R.id.img4);
        images[5] = findViewById(R.id.img5);
        images[6] = findViewById(R.id.img6);
        images[7] = findViewById(R.id.img7);
        images[8] = findViewById(R.id.img8);

    }

    private void intializeTexts(){
        texts[0] = findViewById(R.id.txt0);
        texts[1] = findViewById(R.id.txt1);
        texts[2] = findViewById(R.id.txt2);
        texts[3] = findViewById(R.id.txt3);
        texts[4] = findViewById(R.id.txt4);
        texts[5] = findViewById(R.id.txt5);
        texts[6] = findViewById(R.id.txt6);
        texts[7] = findViewById(R.id.txt7);
        texts[8] = findViewById(R.id.txt8);
    }

    private void initializeButtons(){
        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetGame());
    }

    private void onClickViews(){
        for (int x = 0; x < 9 ; x++){
            images[x].setOnClickListener(v -> {
                String id = v.getResources().getResourceName(v.getId());
                String newId = id.substring(id.length() - 1);
                int newIdInt = Integer.parseInt(newId);
                play(newIdInt);
            });
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void changeTurn(){
        if (turn.equals("X")){
            turn = "O";
            computerPlay();
        }else{
            turn = "X";
        }
    }

    private void play(int x){

        if (isGameOver){
            showToast("O jogo acabou");
            isGameOver = false;
            resetGame();
        }
        else {
            if (!Objects.equals(table[x], "0")) {
                showToast("Espaço já foi jogado");
                return;
            }
            if(turn == "X"){
                table[x] = "1";
            }else{
                table[x] = "2";
            }

            texts[x].setText(turn);
            if (checkWinner()) {
                isGameOver = true;
                showToast("O jogador " + turn + " venceu");

                if (turn.equals("X")) {
                    playerWins++;
                } else {
                    computerWins++;
                }
                updateScore();
                return;
            }
            checkDraw();
            changeTurn();
        }
    }

    private boolean checkWinner(){
        if (table[0].equals(table[1]) && table[1].equals(table[2]) && !table[0].equals("0")){
            return true;
        }
        if (table[3].equals(table[4]) && table[4].equals(table[5]) && !table[3].equals("0")){
            return true;
        }
        if (table[6].equals(table[7]) && table[7].equals(table[8]) && !table[6].equals("0")){
            return true;
        }
        if (table[0].equals(table[3]) && table[3].equals(table[6]) && !table[0].equals("0")){
            return true;
        }
        if (table[1].equals(table[4]) && table[4].equals(table[7]) && !table[1].equals("0")){
            return true;
        }
        if (table[2].equals(table[5]) && table[5].equals(table[8]) && !table[2].equals("0")){
            return true;
        }
        if (table[0].equals(table[4]) && table[4].equals(table[8]) && !table[0].equals("0")){
            return true;
        }
        return table[2].equals(table[4]) && table[4].equals(table[6]) && !table[2].equals("0");
    }

    private void checkDraw(){
        for (int x = 0; x < 9 ; x++){
            if (table[x].equals("0")){
                return;
            }
        }
        showToast("Deu velha");
        resetGame();
    }

    private void resetGame(){
        DatabaseReference tabelaDejogos = BD.child("tabelaDejogos");
        tabelaDejogos.child(String.valueOf(NumeroDeJogos)).setValue(Arrays.toString(table));
        for (int x = 0; x < 9 ; x++){
            table[x] = "0";
            texts[x].setText("");
        }
        NumeroDeJogos++;
    }

    private void updateScore(){
        TextView txtPlayerWins = findViewById(R.id.txtPlayer);
        TextView txtComputerWins = findViewById(R.id.txtComputer);
        txtPlayerWins.setText(String.valueOf(playerWins));
        txtComputerWins.setText(String.valueOf(computerWins));
    }

    private void computerPlay() {
        while (true) {
            int x = (int) (Math.random() * 9);
            if (table[x].equals("0")) {
                play(x);
                break;
            }
        }
    }
}