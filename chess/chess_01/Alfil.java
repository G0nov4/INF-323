package com.example.chess_01;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Alfil {
    private float vertices[] = new float [] {
            -0.5f,+0.25f,+0f,+0.75f,+0.5f,+0.25f,
            -0.5f,+0.25f,+0.5f,+0.25f,+0f,-0.5f,
            +0f,-0.5f,+0.5f,-0.75f,-0.5f,-0.75f
    };

    private FloatBuffer bufVertices;

    public Alfil(){
        ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices.length * 4);
        bufByte.order(ByteOrder.nativeOrder()); // Utiliza el orden del byte nativo
        bufVertices = bufByte.asFloatBuffer(); // Convierte de byte a float
        bufVertices.put(vertices);
        bufVertices.rewind(); // puntero al principio del buffer
    }
    private float posx = 0;
    private float posy = 0;
    private float col1 = 0;
    private float col2 = 0;
    private float col3 = 0;

    public float getPosx(){
        return posx;
    };
    public float getPosy(){
        return posy;
    };
    public float getCol1(){
        return col1;
    }
    public float getCol2(){
        return col2;
    }
    public float getCol3(){
        return col3;
    }
    public void setPosx(float x){
        posx = x;
    };
    public void setPosy(float y){
        posy = y;
    };

    public void dibuja(GL10 gl, float col1, float col2, float col3) {
        /* Se habilita el acceso al arreglo de vértices */
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        /* Se especifica los datos del arreglo de vértices */
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bufVertices);

        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        //Dibujando alfiles
        gl.glColor4f(col1, col2, col3, 0);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 9);

        /* Se deshabilita el acceso al arreglo de vértices */
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
