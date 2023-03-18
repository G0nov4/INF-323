package com.example.chess_01;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//import com.example.interaccion.SonidoSoundPool;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;
import android.widget.Toast;

public class Renderiza extends GLSurfaceView implements Renderer {

    private Tablero tablero_gui;
    private Context contexto;
    public static Moves m;
    private Peon peon;
    private Torre torre;
    private Caballo caballo;
    private Alfil alfil;
    private Reina reina;
    private Rey rey;
    public float posx;
    public float posy;
    public float oldx;
    public float oldy;
    public float newx;
    public float newy;
    private int alto;
    private int ancho;
    private String movidaUsuario = "";
    private boolean sw = true;
    private String posibles;
    private String [][]matrix_gui;

    public Renderiza(Context contexto){
        super(contexto);
        this.contexto = contexto;

        /* Inicia el renderizado */
        this.setRenderer(this);

        /* La ventana solicita recibir una entrada */
        this.requestFocus();

        /* Establece que la ventana detectará el modo táctil */
        this.setFocusableInTouchMode(true);

        /* Se renderizará al inicio o cuando se llame a requestRender() */
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {

        tablero_gui = new Tablero();
        peon = new Peon();
        torre = new Torre();
        caballo = new Caballo();
        alfil = new Alfil();
        reina = new Reina();
        rey = new Rey();
        //posibles = maux.movidasPosibles();

        while (!"A".equals(m.tablero[m.posicionReyC/8][m.posicionReyC%8])){m.posicionReyC++;}//get King's location
        while (!"a".equals(m.tablero[m.posicionReyL/8][m.posicionReyL%8])){m.posicionReyL++;}//get king's location
        /* Color de fondo */
        gl.glClearColor(0, 1, 1, 0);


        //m.makeMove("7655 ");
        //m.undoMove("7655 ");

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        /* Inicializa el buffer de color */
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glPushMatrix();
        tablero_gui.dibuja(gl);
        gl.glPopMatrix();

        gl.glTranslatef(1, 1, 0);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j<8; j++){
                gl.glPushMatrix();
                gl.glTranslatef(j*2, -i*2, 0);
                if(m.tablero[i][j].equals("P")){
                    peon.dibuja(gl, 1, 1, 1);
                }
                if(m.tablero[i][j].equals("p")){
                    peon.dibuja(gl, 0, 0, 0);
                }
                if(m.tablero[i][j].equals("R")){
                    torre.dibuja(gl, 1, 1, 1);
                }
                if(m.tablero[i][j].equals("r")){
                    torre.dibuja(gl, 0, 0, 0);
                }
                if(m.tablero[i][j].equals("K")){
                    caballo.dibuja(gl, 1, 1, 1);
                }
                if(m.tablero[i][j].equals("k")){
                    caballo.dibuja(gl, 0, 0, 0);
                }
                if(m.tablero[i][j].equals("B")){
                    alfil.dibuja(gl, 1, 1, 1);
                }
                if(m.tablero[i][j].equals("b")){
                    alfil.dibuja(gl, 0, 0, 0);
                }
                if(m.tablero[i][j].equals("Q")){
                    reina.dibuja(gl, 1, 1, 1);
                }
                if(m.tablero[i][j].equals("q")){
                    reina.dibuja(gl, 0, 0, 0);
                }
                if(m.tablero[i][j].equals("A")){
                    rey.dibuja(gl, 1, 1, 1);
                }
                if(m.tablero[i][j].equals("a")){
                    rey.dibuja(gl, 0, 0, 0);
                }
                gl.glPopMatrix();
            }
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        ancho = width;
        alto = height;
        /* Ventana de despliegue */
        gl.glViewport(0, 0, width, height);
        /* Matriz de Proyección */
        gl.glMatrixMode(GL10.GL_PROJECTION);
        /* Inicializa la Matriz de Proyección */
        gl.glLoadIdentity();
        /* Proyección paralela */
        //GLU.gluOrtho2D(gl, -10, 10, -15, 15);
        //GLU.gluOrtho2D(gl, -6.6f, 22.6f, -0.25f, 16.25f);
        GLU.gluOrtho2D(gl, 0f, 16f, -21f, 9f);
        /* Matriz del Modelo-Vista */
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        /* Inicializa la Matriz del Modelo-Vista */
        gl.glLoadIdentity();
    }

    public boolean onTouchEvent(MotionEvent e) {

        /* Obtiene la coordenada de la pantalla */
        float posx = e.getX();
        float posy = e.getY();

        /* Se considera cuando se levanta el dedo de la pantalla. */
        if (e.getAction() == MotionEvent.ACTION_UP) {

            /* En coordenadas del OpenGL */
            if(puntoEstaDentroDelRectangulo(((posx / (float) ancho) * 16f) , ((1 - posy / (float) alto) * 24f) - 4f, 0, 0,16, 16)){
                if(sw){
                    oldx = ((posx / (float) ancho) * 16f) - 0f;
                    oldy = ((posy / (float) alto) * 30f) - 7f;
                    sw = false;
                }else{
                    newx = ((posx / (float) ancho) * 16f) - 0f;
                    newy = ((posy / (float) alto) * 30f) - 7f;
                    if((int)(oldy/2) == 1 && m.tablero[(int)(oldy/2)][(int)(oldx/2)].equals("P") && (int)(newy/2) == 0){
                        //promocion
                        movidaUsuario = "" + (int)(oldx/2) +(int)(newx/2)+m.tablero[(int)(newy/2)][(int)(newx/2)]+"QP";
                    }else{
                        movidaUsuario = "" + (int)(oldy/2) + (int)(oldx/2) + (int)(newy/2) + (int)(newx/2) + m.tablero[(int)(newy/2)][(int)(newx/2)];
                    }
                    String posibles = m.movidasPosibles();
                    if(posibles.replaceAll(movidaUsuario, " ").length() < posibles.length()){
                        //si es valido

                        m.realizarMovida(movidaUsuario);
                        requestRender();
                        double timetorender = System.currentTimeMillis();
                        double espera = 450;
                        while(System.currentTimeMillis() - espera < timetorender){
                        }
                        m.vueltaTablero();

                        String movidaComputador = m.alphaBeta(m.profundidadMaxima, 1000000, -1000000, "", 0);

                        m.realizarMovida(movidaComputador);

                        m.vueltaTablero();


                    }else{
                        System.out.println(m.movidasPosibles());
                        System.out.println(movidaUsuario);
                        System.out.println("no valido");
                        requestRender();
                    }
                    sw = true;

                }
            }

            requestRender(); // Llama por defecto

        }
        return true;
    }
    private boolean puntoEstaDentroDelRectangulo(float posx, float posy, int x,
                                                 int y, int ancho, int alto) {
        return (x < posx && posx < x + ancho && y < posy && posy < y + alto);
    }
}