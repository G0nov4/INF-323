package com.example.chess_01;

public class Rating {
    static int tableroPeones[][]={
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}};

    static int tableroTorres[][]={
            { 0,  0,  0,  0,  0,  0,  0,  0},
            { 5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            { 0,  0,  0,  5,  5,  0,  0,  0}};

    static int tableroCaballos[][]={
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}};

    static int tableroAlfiles[][]={
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}};

    static int tableroReinas[][]={
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}};

    static int tableroRey[][]={
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            { 20, 20,  0,  0,  0,  0, 20, 20},
            { 20, 30, 10,  0,  0, 10, 30, 20}};

    static int tableroReyFin[][]={
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}};

    public static int rating(int profundidad) {
        int counter=0, material=rateMaterial();
        counter+=material;
        counter+=ratingPosicion(material);
        Moves.vueltaTablero();
        material=rateMaterial();
        counter-=material;
        counter-=ratingPosicion(material);
        Moves.vueltaTablero();
        return -(counter+profundidad*50);
    }

    public static int rateMaterial() {
        int counter=0, bishopCounter=0;
        for (int i=0;i<64;i++) {
            if (Moves.tablero[i/8][i%8].equals("P")) {
                counter+=100;
            }
            if (Moves.tablero[i/8][i%8].equals("R")) {
                counter+=500;
            }
            if (Moves.tablero[i/8][i%8].equals("K")) {
                counter+=300;
            }
            if (Moves.tablero[i/8][i%8].equals("B")) {
                counter+=1;
            }
            if (Moves.tablero[i/8][i%8].equals("Q")) {
                counter+=900;
            }

        }
        if (bishopCounter>=2) {
            counter+=300*bishopCounter;
        } else {
            if (bishopCounter==1) {counter+=250;}
        }
        return counter;
    }

    public static int ratingPosicion(int material) {
        int counter=0;
        for (int i=0;i<64;i++) {
            if (Moves.tablero[i/8][i%8].equals("P")) {
                counter+=tableroPeones[i/8][i%8];
            }
            if (Moves.tablero[i/8][i%8].equals("R")) {
                counter+=tableroTorres[i/8][i%8];
            }
            if (Moves.tablero[i/8][i%8].equals("K")) {
                counter+=tableroCaballos[i/8][i%8];
            }
            if (Moves.tablero[i/8][i%8].equals("B")) {
                counter+=tableroAlfiles[i/8][i%8];
            }
            if (Moves.tablero[i/8][i%8].equals("Q")) {
                counter+=tableroReinas[i/8][i%8];
            }
            if (Moves.tablero[i/8][i%8].equals("A")) {
                if (material>=1750) {
                    counter+=tableroRey[i/8][i%8];
                }
                else{
                    counter+=tableroReyFin[i/8][i%8];
                }
            }

        }
        return counter;
    }
}
