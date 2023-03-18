package com.example.chess_01;

import java.util.*;

public class Moves {
    static String tablero[][]={
            {"r","k","b","q","a","b","k","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {"R","K","B","Q","A","B","K","R"}};
    static int posicionReyC, posicionReyL;
    static int profundidadMaxima=2;

    public static String alphaBeta(int profundidad, int beta, int alpha, String movida, int jugador) {
        String list=movidasPosibles();
        if (profundidad==0 || list.length()==0) {return movida+(Rating.rating(profundidad)*(jugador*2-1));}
        jugador=1-jugador;
        for (int i=0;i<list.length();i+=5){
            realizarMovida(list.substring(i,i+5));
            vueltaTablero();
            String returnString=alphaBeta(profundidad-1, beta, alpha, list.substring(i,i+5), jugador);
            int value=Integer.valueOf(returnString.substring(5));
            vueltaTablero();
            deshacerMovida(list.substring(i,i+5));
            if (jugador==0) {
                if (value<=beta) {beta=value; if (profundidad==profundidadMaxima) {movida=returnString.substring(0,5);}}
            } else {
                if (value>alpha) {alpha=value; if (profundidad==profundidadMaxima) {movida=returnString.substring(0,5);}}
            }
            if (alpha>=beta) {
                if (jugador==0) {return movida+beta;} else {return movida+alpha;}
            }
        }
        if (jugador==0) {return movida+beta;} else {return movida+alpha;}
    }
    public static void vueltaTablero() {
        String temp;
        for (int i=0;i<32;i++) {
            int r=i/8, c=i%8;
            if (Character.isUpperCase(tablero[r][c].charAt(0))) {
                temp=tablero[r][c].toLowerCase();
            } else {
                temp=tablero[r][c].toUpperCase();
            }
            if (Character.isUpperCase(tablero[7-r][7-c].charAt(0))) {
                tablero[r][c]=tablero[7-r][7-c].toLowerCase();
            } else {
                tablero[r][c]=tablero[7-r][7-c].toUpperCase();
            }
            tablero[7-r][7-c]=temp;
        }
        int reyTemp=posicionReyC;
        posicionReyC=63-posicionReyL;
        posicionReyL=63-reyTemp;
    }
    public static void realizarMovida(String movida) {
        if (movida.charAt(4)!='P') {
            if(!((movida.charAt(0) == movida.charAt(2)) && (movida.charAt(1) == movida.charAt(3)))){
                tablero[Character.getNumericValue(movida.charAt(2))][Character.getNumericValue(movida.charAt(3))]=tablero[Character.getNumericValue(movida.charAt(0))][Character.getNumericValue(movida.charAt(1))];
                tablero[Character.getNumericValue(movida.charAt(0))][Character.getNumericValue(movida.charAt(1))]=" ";
                if ("A".equals(tablero[Character.getNumericValue(movida.charAt(2))][Character.getNumericValue(movida.charAt(3))])) {
                    posicionReyC=8*Character.getNumericValue(movida.charAt(2))+Character.getNumericValue(movida.charAt(3));
                }
            }

        } else {
            tablero[1][Character.getNumericValue(movida.charAt(0))]=" ";
            tablero[0][Character.getNumericValue(movida.charAt(1))]=String.valueOf(movida.charAt(3));
        }
    }
    public static void deshacerMovida(String movida) {
        if (movida.charAt(4)!='P') {
            tablero[Character.getNumericValue(movida.charAt(0))][Character.getNumericValue(movida.charAt(1))]=tablero[Character.getNumericValue(movida.charAt(2))][Character.getNumericValue(movida.charAt(3))];
            tablero[Character.getNumericValue(movida.charAt(2))][Character.getNumericValue(movida.charAt(3))]=String.valueOf(movida.charAt(4));
            if ("A".equals(tablero[Character.getNumericValue(movida.charAt(0))][Character.getNumericValue(movida.charAt(1))])) {
                posicionReyC=8*Character.getNumericValue(movida.charAt(0))+Character.getNumericValue(movida.charAt(1));
            }
        } else {
            tablero[1][Character.getNumericValue(movida.charAt(0))]="P";
            tablero[0][Character.getNumericValue(movida.charAt(1))]=String.valueOf(movida.charAt(2));
        }
    }
    public static String movidasPosibles() {
        String list="";
        for (int i=0; i<64; i++) {
            if (tablero[i/8][i%8].equals("P")){
                list+=movidasPeon(i);
            }
            if (tablero[i/8][i%8].equals("R")){
                list+=movidasTorre(i);
            }
            if (tablero[i/8][i%8].equals("K")){
                list+=movidasCaballo(i);
            }
            if (tablero[i/8][i%8].equals("B")){
                list+=movidasAlfil(i);
            }
            if (tablero[i/8][i%8].equals("Q")){
                list+=movidasReina(i);
            }
            if (tablero[i/8][i%8].equals("A")){
                list+=movidasRey(i);
            }

        }
        return list;
    }
    public static String movidasPeon(int i) {
        String list="", anterior;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {
            try {//para capturar
                if (Character.isLowerCase(tablero[r-1][c+j].charAt(0)) && i>=16) {
                    anterior=tablero[r-1][c+j];
                    tablero[r][c]=" ";
                    tablero[r-1][c+j]="P";
                    if (reySeguro()) {
                        list=list+r+c+(r-1)+(c+j)+anterior;
                    }
                    tablero[r][c]="P";
                    tablero[r-1][c+j]=anterior;
                }
            } catch (Exception e) {}
            try {//para promover con captura
                if (Character.isLowerCase(tablero[r-1][c+j].charAt(0)) && i<16) {
                    String[] temp={"Q","R","B","K"};
                    for (int k=0; k<4; k++) {
                        anterior=tablero[r-1][c+j];
                        tablero[r][c]=" ";
                        tablero[r-1][c+j]=temp[k];
                        if (reySeguro()) {
                            //column1,column2,captured-piece,new-piece,P
                            list=list+c+(c+j)+anterior+temp[k]+"P";
                        }
                        tablero[r][c]="P";
                        tablero[r-1][c+j]=anterior;
                    }
                }
            } catch (Exception e) {}
        }
        try {//moverse 1 cuadro
            if (" ".equals(tablero[r-1][c]) && i>=16) {
                anterior=tablero[r-1][c];
                tablero[r][c]=" ";
                tablero[r-1][c]="P";
                if (reySeguro()) {
                    list=list+r+c+(r-1)+c+anterior;
                }
                tablero[r][c]="P";
                tablero[r-1][c]=anterior;
            }
        } catch (Exception e) {}
        try {//promocion sin captura
            if (" ".equals(tablero[r-1][c]) && i<16) {
                String[] temp={"Q","R","B","K"};
                for (int k=0; k<4; k++) {
                    anterior=tablero[r-1][c];
                    tablero[r][c]=" ";
                    tablero[r-1][c]=temp[k];
                    if (reySeguro()) {
                        list=list+c+c+anterior+temp[k]+"P";
                    }
                    tablero[r][c]="P";
                    tablero[r-1][c]=anterior;
                }
            }
        } catch (Exception e) {}
        try {//moverse 2 cuadros
            if (" ".equals(tablero[r-1][c]) && " ".equals(tablero[r-2][c]) && i>=48) {
                anterior=tablero[r-2][c];
                tablero[r][c]=" ";
                tablero[r-2][c]="P";
                if (reySeguro()) {
                    list=list+r+c+(r-2)+c+anterior;
                }
                tablero[r][c]="P";
                tablero[r-2][c]=anterior;
            }
        } catch (Exception e) {}
        return list;
    }
    public static String movidasTorre(int i) {
        String list="", anterior;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j+=2) {
            try {
                while(" ".equals(tablero[r][c+temp*j]))
                {
                    anterior=tablero[r][c+temp*j];
                    tablero[r][c]=" ";
                    tablero[r][c+temp*j]="R";
                    if (reySeguro()) {
                        list=list+r+c+r+(c+temp*j)+anterior;
                    }
                    tablero[r][c]="R";
                    tablero[r][c+temp*j]=anterior;
                    temp++;
                }
                if (Character.isLowerCase(tablero[r][c+temp*j].charAt(0))) {
                    anterior=tablero[r][c+temp*j];
                    tablero[r][c]=" ";
                    tablero[r][c+temp*j]="R";
                    if (reySeguro()) {
                        list=list+r+c+r+(c+temp*j)+anterior;
                    }
                    tablero[r][c]="R";
                    tablero[r][c+temp*j]=anterior;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while(" ".equals(tablero[r+temp*j][c]))
                {
                    anterior=tablero[r+temp*j][c];
                    tablero[r][c]=" ";
                    tablero[r+temp*j][c]="R";
                    if (reySeguro()) {
                        list=list+r+c+(r+temp*j)+c+anterior;
                    }
                    tablero[r][c]="R";
                    tablero[r+temp*j][c]=anterior;
                    temp++;
                }
                if (Character.isLowerCase(tablero[r+temp*j][c].charAt(0))) {
                    anterior=tablero[r+temp*j][c];
                    tablero[r][c]=" ";
                    tablero[r+temp*j][c]="R";
                    if (reySeguro()) {
                        list=list+r+c+(r+temp*j)+c+anterior;
                    }
                    tablero[r][c]="R";
                    tablero[r+temp*j][c]=anterior;
                }
            } catch (Exception e) {}
            temp=1;
        }
        return list;
    }
    public static String movidasCaballo(int i) {
        String list="", anterior;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {
            for (int k=-1; k<=1; k+=2) {
                try {
                    if (Character.isLowerCase(tablero[r+j][c+k*2].charAt(0)) || " ".equals(tablero[r+j][c+k*2])) {
                        anterior=tablero[r+j][c+k*2];
                        tablero[r][c]=" ";
                        tablero[r+j][c+k*2]="K";
                        if (reySeguro()) {
                            list=list+r+c+(r+j)+(c+k*2)+anterior;
                        }
                        tablero[r][c]="K";
                        tablero[r+j][c+k*2]=anterior;
                    }
                } catch (Exception e) {}
                try {
                    if (Character.isLowerCase(tablero[r+j*2][c+k].charAt(0)) || " ".equals(tablero[r+j*2][c+k])) {
                        anterior=tablero[r+j*2][c+k];
                        tablero[r][c]=" ";
                        tablero[r+j*2][c+k]="K";
                        if (reySeguro()) {
                            list=list+r+c+(r+j*2)+(c+k)+anterior;
                        }
                        tablero[r][c]="K";
                        tablero[r+j*2][c+k]=anterior;
                    }
                } catch (Exception e) {}
            }
        }
        return list;
    }
    public static String movidasAlfil(int i) {
        String list="", anterior;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j+=2) {
            for (int k=-1; k<=1; k+=2) {
                try {
                    while(" ".equals(tablero[r+temp*j][c+temp*k]))
                    {
                        anterior=tablero[r+temp*j][c+temp*k];
                        tablero[r][c]=" ";
                        tablero[r+temp*j][c+temp*k]="B";
                        if (reySeguro()) {
                            list=list+r+c+(r+temp*j)+(c+temp*k)+anterior;
                        }
                        tablero[r][c]="B";
                        tablero[r+temp*j][c+temp*k]=anterior;
                        temp++;
                    }
                    if (Character.isLowerCase(tablero[r+temp*j][c+temp*k].charAt(0))) {
                        anterior=tablero[r+temp*j][c+temp*k];
                        tablero[r][c]=" ";
                        tablero[r+temp*j][c+temp*k]="B";
                        if (reySeguro()) {
                            list=list+r+c+(r+temp*j)+(c+temp*k)+anterior;
                        }
                        tablero[r][c]="B";
                        tablero[r+temp*j][c+temp*k]=anterior;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        return list;
    }
    public static String movidasReina(int i) {
        String list="", anterior;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j++) {
            for (int k=-1; k<=1; k++) {
                if (j!=0 || k!=0) {
                    try {
                        while(" ".equals(tablero[r+temp*j][c+temp*k]))
                        {
                            anterior=tablero[r+temp*j][c+temp*k];
                            tablero[r][c]=" ";
                            tablero[r+temp*j][c+temp*k]="Q";
                            if (reySeguro()) {
                                list=list+r+c+(r+temp*j)+(c+temp*k)+anterior;
                            }
                            tablero[r][c]="Q";
                            tablero[r+temp*j][c+temp*k]=anterior;
                            temp++;
                        }
                        if (Character.isLowerCase(tablero[r+temp*j][c+temp*k].charAt(0))) {
                            anterior=tablero[r+temp*j][c+temp*k];
                            tablero[r][c]=" ";
                            tablero[r+temp*j][c+temp*k]="Q";
                            if (reySeguro()) {
                                list=list+r+c+(r+temp*j)+(c+temp*k)+anterior;
                            }
                            tablero[r][c]="Q";
                            tablero[r+temp*j][c+temp*k]=anterior;
                        }
                    } catch (Exception e) {}
                    temp=1;
                }
            }
        }
        return list;
    }
    public static String movidasRey(int i) {
        String list="", anterior;
        int r=i/8, c=i%8;
        for (int j=0; j<9; j++) {
            if (j!=4) {
                try {
                    if (Character.isLowerCase(tablero[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(tablero[r-1+j/3][c-1+j%3])) {
                        anterior=tablero[r-1+j/3][c-1+j%3];
                        tablero[r][c]=" ";
                        tablero[r-1+j/3][c-1+j%3]="A";
                        int kingTemp=posicionReyC;
                        posicionReyC=i+(j/3)*8+j%3-9;
                        if (reySeguro()) {
                            list=list+r+c+(r-1+j/3)+(c-1+j%3)+anterior;
                        }
                        tablero[r][c]="A";
                        tablero[r-1+j/3][c-1+j%3]=anterior;
                        posicionReyC=kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        return list;
    }

    public static boolean reySeguro() {
        //alfil/reina
        int temp=1;
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    while(" ".equals(tablero[posicionReyC/8+temp*i][posicionReyC%8+temp*j])) {temp++;}
                    if ("b".equals(tablero[posicionReyC/8+temp*i][posicionReyC%8+temp*j]) ||
                            "q".equals(tablero[posicionReyC/8+temp*i][posicionReyC%8+temp*j])) {
                        return false;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        //torre/reina
        for (int i=-1; i<=1; i+=2) {
            try {
                while(" ".equals(tablero[posicionReyC/8][posicionReyC%8+temp*i])) {temp++;}
                if ("r".equals(tablero[posicionReyC/8][posicionReyC%8+temp*i]) ||
                        "q".equals(tablero[posicionReyC/8][posicionReyC%8+temp*i])) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while(" ".equals(tablero[posicionReyC/8+temp*i][posicionReyC%8])) {temp++;}
                if ("r".equals(tablero[posicionReyC/8+temp*i][posicionReyC%8]) ||
                        "q".equals(tablero[posicionReyC/8+temp*i][posicionReyC%8])) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
        }
        //caballo
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    if ("k".equals(tablero[posicionReyC/8+i][posicionReyC%8+j*2])) {
                        return false;
                    }
                } catch (Exception e) {}
                try {
                    if ("k".equals(tablero[posicionReyC/8+i*2][posicionReyC%8+j])) {
                        return false;
                    }
                } catch (Exception e) {}
            }
        }
        //peon
        if (posicionReyC>=16) {
            try {
                if ("p".equals(tablero[posicionReyC/80-1][posicionReyC%8-1])) {
                    return false;
                }
            } catch (Exception e) {}
            try {
                if ("p".equals(tablero[posicionReyC/80-1][posicionReyC%8+1])) {
                    return false;
                }
            } catch (Exception e) {}
            //rey
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        try {
                            if ("a".equals(tablero[posicionReyC/8+i][posicionReyC%8+j])) {
                                return false;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return true;
    }

}