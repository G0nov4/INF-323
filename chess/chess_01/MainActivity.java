package com.example.chess_01;

import java.util.Arrays;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

public class MainActivity extends Activity {
    GLSurfaceView superficie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//	 	Movidas m = new Movidas();
//	 	System.out.println(m.posibleMoves());
//
//	 	//m.makeMove("7655 ");
//        for (int i=0;i<8;i++) {
//            System.out.println(Arrays.toString(m.chessBoard[i]));
//        }
//
//        while (!"A".equals(m.chessBoard[m.kingPositionC/8][m.kingPositionC%8])){m.kingPositionC++;}//get King's location
//        while (!"a".equals(m.chessBoard[m.kingPositionL/8][m.kingPositionL%8])){m.kingPositionL++;}//get king's location
//        /*JFrame f=new JFrame("Chess Tutorial");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        UserInterface ui=new UserInterface();
//        f.add(ui);
//        f.setSize(500, 500);
//        f.setVisible(true);*/
//        System.out.println(m.posibleMoves());
//        //System.out.println(m.alphaBeta(m.globalDepth, 1000000, -1000000, "", 0));
//        m.makeMove(m.alphaBeta(m.globalDepth, 1000000, -1000000, "", 0));
//        //m.makeMove("7655 ");
//        //m.undoMove("7655 ");
//        for (int i=0;i<8;i++) {
//            System.out.println(Arrays.toString(m.chessBoard[i]));
//        }
//
//
        /* Ventana sin título */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        /* Establece las banderas de la ventana de esta Actividad */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* Se crea el objeto Renderiza */
        superficie = new Renderiza(this);

        /*
         * Activity <- GLSurfaceView  : Coloca la Vista de la Superficie del
         * OpenGL como un Contexto de ésta Actividad.
         */
        setContentView(superficie);
        // setContentView(R.layout.activity_main);
    }
}