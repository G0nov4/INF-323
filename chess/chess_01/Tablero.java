package com.example.chess_01;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Tablero {
    public char piezas[][] = new char[8][8];
    private boolean ocupado[][] = new boolean[8][8];
    private String color[][] = new String[8][8];

    private float vertices_cuadro[] = new float[]{
            0f, 0f, 2f, 0f, 2f, 2f,
            0f, 0f, 2f, 2f, 0f, 2f,
    };

    private FloatBuffer bufVertices;

    public Tablero(){
        String []aux0 = new String []{"rkbqabkr", "pppppppp", "nnnnnnnn", "nnnnnnnn", "nnnnnnnn",
                "nnnnnnnn", "pppppppp", "RKBQABKR"};
        for(int i = 0; i < 8; i++){
            String current = aux0[i];
            for(int j = 0; j < 8; j++){
                piezas[i][j] = current.charAt(j);
                if(current.charAt(j) == 'n')
                    ocupado[i][j] = false;
                else
                    ocupado[i][j] = true;
            }
        }

        ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices_cuadro.length * 4);
        bufByte.order(ByteOrder.nativeOrder()); // Utiliza el orden del byte nativo
        bufVertices = bufByte.asFloatBuffer(); // Convierte de byte a float
        bufVertices.put(vertices_cuadro);
        bufVertices.rewind(); // puntero al principio del buffer
    }

    public void mostrar(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                System.out.println(piezas[i][j]);
            }
        }
    }

    public void dibuja(GL10 gl) {
        int n = 0;
        int m = 0;
        int cont = 0;
        boolean aux = true;
        boolean sw = true;
        /* Se habilita el acceso al arreglo de vértices */
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        /* Se especifica los datos del arreglo de vértices */
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bufVertices);
        for(int i = 0; i<8; i++){
            for(int j= 0; j<8; j++){
                gl.glTranslatef(m, 0, 0);
                if(sw){
                    gl.glColor4f(218/255f, 176/255f, 127/255f, 0);
                }else{
                    gl.glColor4f(139/255f, 95/255f, 55/255f, 0);
                }
                sw = !sw;
                gl.glDrawArrays(GL10.GL_TRIANGLES, cont, 6);
                //cont = cont + 3;
                m = 2;
            }
            sw = !sw;
            gl.glTranslatef(-16, -2, 0);
            //m = -16;
        }

        /* Se deshabilita el acceso al arreglo de vértices */
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

}
