/***********************************************************************************/
/* Se define la geometría y se almacenan en los buffers de memoria y se renderiza. */
/***********************************************************************************/
class Cono {

    /* segmentosH = slices o longitud, segmentosV = stacks o latitud  */
    constructor(gl, radioArriba, radioAbajo, alto, cantidadDeSectores, arriba, abajo) {

        var i, j, kv, kc, ki, angulo, x, y;

        var cantidadDeVertices = 4 * cantidadDeSectores + 6;
        var cantidadDeTriangulos = 2 * cantidadDeSectores;

        if (arriba) {
            cantidadDeTriangulos += cantidadDeSectores;
        }

        if (abajo) {
            cantidadDeTriangulos += cantidadDeSectores;
        }
        this.cantidadDeIndices = cantidadDeTriangulos * 3;

        /* Las coordenadas cartesianas (x, y, z) */
        var vertices = new Float32Array(cantidadDeVertices * 3);

        /* Los colores x c/vértice (r,g,b,a) */
        var colores = new Float32Array(cantidadDeVertices * 4);

        /* Indices */
        var indices = new Uint16Array(this.cantidadDeIndices);

        /* Se leen los vertices */
        kv = 0;
        kc = 0;
        var anguloDelSector = 2 * Math.PI / cantidadDeSectores; // 1 vuelta (en radianes)/cantidad de sectores

        // Circulo de arriba
        for (i = 0; i <= cantidadDeSectores; i++) {
            angulo = i * anguloDelSector;

            x = Math.cos(angulo);
            y = Math.sin(angulo);

            vertices[kv++] = radioArriba * x;
            vertices[kv++] = radioArriba * y;
            vertices[kv++] = alto / 2;

            colores[kc++] = 1;
            colores[kc++] = 0;
            colores[kc++] = 0;
            colores[kc++] = 1;
        }

        // Circulo de abajo
        for (i = 0; i <= cantidadDeSectores; i++) {
            angulo = i * anguloDelSector;

            x = Math.cos(angulo);
            y = Math.sin(angulo);

            vertices[kv++] = radioAbajo * x;
            vertices[kv++] = radioAbajo * y;
            vertices[kv++] = -alto / 2;

            colores[kc++] = 1;
            colores[kc++] = 0;
            colores[kc++] = 0;
            colores[kc++] = 1;
        }

        /* Se leen los indices */

        /**
         *  ki1 ------- ki1+1
         *     |     / | 
         *     |   /   |
         *     | /     |
         *  ki2 ------- ki2+1
         *  ki1,ki2,ki1+1, ki1+1,ki2,ki2+1 =>  2 triangulo
         */
        ki = 0
        let ki1 = 0;
        let ki2 = cantidadDeSectores + 1;
        for (j = 0; j < cantidadDeSectores; j++) {
            // ki2 => ki1+1 => ki1 
            indices[ki++] = ki2;
            indices[ki++] = ki1 + 1;
            indices[ki++] = ki1;

            // ki2 => ki2+1 => ki1+1 
            indices[ki++] = ki2;
            indices[ki++] = ki2 + 1;
            indices[ki++] = ki1 + 1;
            ki1++;
            ki2++;
        }

        if (arriba) {
            let p, p1, nv;
            p = kv / 3;
            vertices[kv++] = 0;
            vertices[kv++] = 0;
            vertices[kv++] = alto / 2;

            colores[kc++] = 0;
            colores[kc++] = 1;
            colores[kc++] = 0;
            colores[kc++] = 1;

            p1 = kv / 3;

            // Circulo de arriba
            for (i = 0; i <= cantidadDeSectores; i++) {
                nv = i * 3;
                vertices[kv++] = vertices[nv];
                vertices[kv++] = vertices[nv + 1];
                vertices[kv++] = vertices[nv + 2];

                colores[kc++] = 0;
                colores[kc++] = 1;
                colores[kc++] = 0;
                colores[kc++] = 1;
            }

            /**
             *             1
             *           / | 
             *         /   |
             *       /     |
             *    p ------- 0
             *    p,0, 0,1, 1,p  =>  6  indices
             */
            for (i = 0; i < cantidadDeSectores; i++) {
                indices[ki++] = p;     // p
                indices[ki++] = p1;    // 0
                indices[ki++] = p1 + 1;    // 1
                p1++;
            }
        }

        if (abajo) {
            let p, p1, nv;
            p = kv / 3;
            vertices[kv++] = 0;
            vertices[kv++] = 0;
            vertices[kv++] = -alto / 2;

            colores[kc++] = 0;
            colores[kc++] = 1;
            colores[kc++] = 0;
            colores[kc++] = 1;

            p1 = kv / 3;
            j = cantidadDeSectores + 1;

            // Circulo de abajo
            for (i = 0; i <= cantidadDeSectores; i++) {
                nv = j * 3;
                vertices[kv++] = vertices[nv];
                vertices[kv++] = vertices[nv + 1];
                vertices[kv++] = vertices[nv + 2];

                colores[kc++] = 0;
                colores[kc++] = 1;
                colores[kc++] = 0;
                colores[kc++] = 1;
                j++;
            }

            /**
             *             ki2+1
             *           / | 
             *         /   |
             *       /     |
             *    p ------- ki2
             *    p,ki2, ki2,ki2+1, ki2+1,p  =>  6  indices
             */
            for (i = 0; i < cantidadDeSectores; i++) {
                indices[ki++] = p;     // p
                indices[ki++] = p1;    // ki2
                indices[ki++] = p1 + 1;  // ki2 + 1
                p1++;
            }
        }
        /* Se crea el objeto del arreglo de vértices (VAO) */
        this.cilindroVAO = gl.createVertexArray();

        /* Se activa el objeto */
        gl.bindVertexArray(this.cilindroVAO);


        /* Se genera un nombre (código) para el buffer */
        var verticeBuffer = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, verticeBuffer);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los vértices (indice = 0) */
        gl.enableVertexAttribArray(0);

        /* Se especifica el arreglo de vértices */
        gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var codigoColores = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoColores);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colores), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los colores (indice = 1) */
        gl.enableVertexAttribArray(1);

        /* Se especifica el arreglo de colores */
        gl.vertexAttribPointer(1, 4, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var indiceBuffer = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, indiceBuffer);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);


        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

    }

    dibuja(gl) {

        /* Se activa el objeto del arreglo de vértices */
        gl.bindVertexArray(this.cilindroVAO);

        /* Renderiza las primitivas desde los datos de los arreglos (vértices,
         * normales e indices) */
        gl.drawElements(gl.TRIANGLES, this.cantidadDeIndices, gl.UNSIGNED_SHORT, 0);

        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

    }
}

class Cubo {
    constructor(gl) {

        /**
         *       3 --------- 2
         *       /|        /|   
         *      / |       / |
         *    7 --------- 6 |
         *     |  |      |  |
         *     | 0 ------|-- 1 
         *     | /       | /
         *     |/        |/
         *    4 --------- 5  
         */

        /* Las coordenadas cartesianas (x, y, z) */
        var vertices = [
            // Frente
            -1, -1, 1, // 4   0
            1, -1, 1, // 5   1
            1, 1, 1, // 6   2
            -1, 1, 1, // 7   3
            // Atrás
            -1, 1, -1, // 3   4
            1, 1, -1, // 2   5
            1, -1, -1, // 1   6
            -1, -1, -1, // 0   7
            // Izquierda
            -1, -1, -1, // 0   8
            -1, -1, 1, // 4   9
            -1, 1, 1, // 7  10 
            -1, 1, -1, // 3  11
            // Derecha
            1, -1, 1, // 5  12 
            1, -1, -1, // 1  13
            1, 1, -1, // 2  14
            1, 1, 1, // 6  15
            // Abajo
            -1, -1, -1, // 0  16
            1, -1, -1, // 1  17
            1, -1, 1, // 5  18
            -1, -1, 1, // 4  19
            // Arriba
            -1, 1, 1, // 7  20
            1, 1, 1, // 6  21
            1, 1, -1, // 2  22
            -1, 1, -1  // 3  23
        ];

        /* Los colores x c/vértice (r,g,b,a) */
        var colores = [
            // Frente - lila
            1, 0, 1, 1, // 4   0
            1, 0, 1, 1, // 5   1
            1, 0, 1, 1, // 6   2
            1, 0, 1, 1, // 7   3	
            // Atrás - amarillo
            1, 1, 0, 1, // 3   4	
            1, 1, 0, 1, // 2   5
            1, 1, 0, 1, // 1   6	
            1, 1, 0, 1, // 0   7	
            // Izquierda - celeste
            0, 1, 1, 1, // 0   8
            0, 1, 1, 1, // 4   9
            0, 1, 1, 1, // 7  10
            0, 1, 1, 1, // 3  11
            // Derecha - rojo
            1, 0, 0, 1, // 5  12
            1, 0, 0, 1, // 1  13
            1, 0, 0, 1, // 2  14
            1, 0, 0, 1, // 6  15
            // Abajo - azul
            0, 0, 1, 1, // 0  16
            0, 0, 1, 1, // 1  17
            0, 0, 1, 1, // 5  18
            0, 0, 1, 1, // 4  19
            // Arriba - verde
            0, 1, 0, 1, // 7  20
            0, 1, 0, 1, // 6  21
            0, 1, 0, 1, // 2  22
            0, 1, 0, 1  // 3  23
        ];

        /* Indices */
        var indices = [
            0, 1, 2, 0, 2, 3, // Frente
            4, 5, 6, 4, 6, 7, // Atrás
            8, 9, 10, 8, 10, 11, // Izquierda 
            12, 13, 14, 12, 14, 15, // Derecha
            16, 17, 18, 16, 18, 19, // Abajo
            20, 21, 22, 20, 22, 23  // Arriba
        ];

        /* Se crea el objeto del arreglo de vértices (VAO) */
        this.cuboVAO = gl.createVertexArray();

        /* Se activa el objeto */
        gl.bindVertexArray(this.cuboVAO);


        /* Se genera un nombre (código) para el buffer */
        var codigoVertices = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoVertices);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los vértices (indice = 0) */
        gl.enableVertexAttribArray(0);

        /* Se especifica el arreglo de vértices */
        gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var codigoColores = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoColores);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colores), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los colores (indice = 1) */
        gl.enableVertexAttribArray(1);

        /* Se especifica el arreglo de colores */
        gl.vertexAttribPointer(1, 4, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var codigoDeIndices = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, codigoDeIndices);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);


        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

    }

    dibuja(gl) {

        /* Se activa el objeto del arreglo de vértices */
        gl.bindVertexArray(this.cuboVAO);

        /* Renderiza las primitivas desde los datos de los arreglos (vértices,
         * colores e indices) */
        gl.drawElements(gl.TRIANGLES, 36, gl.UNSIGNED_SHORT, 0);

        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

    }
}


class CuboMalla {
    constructor(gl) {

        /* Las coordenadas cartesianas (x, y) */
        var vertices = [
            -1, -1, -1, // 0
            1, -1, -1, // 1
            1, 1, -1, // 2
            -1, 1, -1, // 3
            -1, -1, 1, // 4
            1, -1, 1, // 5
            1, 1, 1, // 6
            -1, 1, 1, // 7
        ];

        /* Indices */
        var indices = [
            4, 5, 5, 6, 6, 7, 7, 4, // Frente
            3, 2, 2, 1, 1, 0, 0, 3, // Atrás
            0, 4, 4, 7, 7, 3, 3, 0, // Izquierda
            5, 1, 1, 2, 2, 6, 6, 5, // Derecha
            0, 1, 1, 5, 5, 4, 4, 0, // Abajo
            7, 6, 6, 2, 2, 3, 3, 7, // Arriba
        ];

        /* Se crea el objeto del arreglo de vértices (VAO) */
        this.cuboVAO = gl.createVertexArray();

        /* Se activa el objeto */
        gl.bindVertexArray(this.cuboVAO);


        /* Se genera un nombre (código) para el buffer */
        var codigoVertices = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoVertices);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los vértices (indice = 0) */
        gl.enableVertexAttribArray(0);

        /* Se especifica el arreglo de vértices */
        gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var codigoDeIndices = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, codigoDeIndices);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);


        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

    }

    dibuja(gl) {

        /* Se activa el objeto del arreglo de vértices */
        gl.bindVertexArray(this.cuboVAO);

        /* Renderiza las primitivas desde los datos de los arreglos (vértices,
         * colores e indices) */
        gl.drawElements(gl.LINES, 48, gl.UNSIGNED_SHORT, 0);

        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

    }
}

class PiramideMalla {
    constructor(gl) {

        /* Las coordenadas cartesianas (x, y, z) */
        var vertices = [
            // Abajo
            -1, -1, -1, // 0  
            1, -1, -1, // 1  
            1, -1, 1, // 2  
            -1, -1, 1, // 3  
            // Frente
            0, 1, 0  // 4
        ];

        /* Indices */
        var indices = [

            0, 1, 1, 2, 2, 3, 3, 0,

            0, 4, 4, 1,
            1, 4, 4, 2,
            2, 4, 4, 3,
            3, 4, 4, 0,

        ];

        this.PiramideVAO = gl.createVertexArray();
        gl.bindVertexArray(this.PiramideVAO);
        var codigoVertices = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoVertices);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
        gl.enableVertexAttribArray(0);
        gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 0, 0);
        var codigoDeIndices = gl.createBuffer();
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, codigoDeIndices);
        gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);
        gl.bindVertexArray(null);
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

    }

    dibuja(gl) {

        gl.bindVertexArray(this.PiramideVAO);
        gl.drawElements(gl.LINES, 24, gl.UNSIGNED_SHORT, 0);
        gl.bindVertexArray(null);

    }
}

class OctahedroMalla {
    constructor(gl) {
        var vertices = [
            -2, 0, 0,
            0, 1, 0,
            2, 0, 0,
            0, -1, 0,
            0, 0, -2,
            0, 0, 2,
        ];


        var indices = [
            0, 1, 4, 0,
            3, 0, 4, 3,
            1, 2, 4, 1,
            2, 3, 4, 2,
            0, 1, 5, 0,
            3, 0, 5, 3,
            1, 2, 5, 1,
            2, 3, 5, 2,
        ];

        /* Se crea el objeto del arreglo de vértices (VAO) */
        this.octahedroVAO = gl.createVertexArray();

        /* Se activa el objeto */
        gl.bindVertexArray(this.octahedroVAO);


        /* Se genera un nombre (código) para el buffer */
        var codigoVertices = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoVertices);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los vértices (indice = 0) */
        gl.enableVertexAttribArray(0);

        /* Se especifica el arreglo de vértices */
        gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var codigoDeIndices = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, codigoDeIndices);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);


        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

    }

    dibuja(gl) {

        /* Se activa el objeto del arreglo de vértices */
        gl.bindVertexArray(this.octahedroVAO);

        /* Renderiza las primitivas desde los datos de los arreglos (vértices,
         * colores e indices) */
        gl.drawElements(gl.LINES, 32, gl.UNSIGNED_SHORT, 0);

        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

    }
}

class EsferaMalla {

    /* segmentosH = slices o longitud, segmentosV = stacks o latitud  */
    constructor(gl, radio, segmentosH, segmentosV) {

        let cantidadDeVertices = (segmentosH + 1) * (segmentosV + 1);
        this.cantidadDeIndices = segmentosH * segmentosV * 6 * 2; // 6 vert (c/cuadrado)

        let i, j, k, x, y, z, theta_, phi_, k1, k2;

        /* Las coordenadas cartesianas (x, y) */
        let vertices = new Float32Array(cantidadDeVertices * 3);

        /* Indices */
        let indices = new Uint16Array(this.cantidadDeIndices);

        /* Considere a las Coordenadas Esféricas para los siguientes cálculos */

        /* Se leen los vertices y las normales */
        k = 0;
        let theta = 2 * Math.PI / segmentosH; // 1 vuelta 360/segmentosH
        let phi = Math.PI / segmentosV;       // 1/2 vuelta 180/segmentosV

        // latitud
        for (i = 0; i <= segmentosV; i++) {
            phi_ = i * phi - Math.PI / 2; // -90..90 grados

            // longitud
            for (j = 0; j <= segmentosH; j++) {
                theta_ = j * theta; // 0..180 grados
                x = radio * Math.cos(theta_) * Math.cos(phi_);
                y = radio * Math.sin(theta_) * Math.cos(phi_);
                z = radio * Math.sin(phi_);

                //console.log(" theta: " + toDegrees(theta_) + " phi: " + toDegrees(phi_));

                vertices[k++] = x;
                vertices[k++] = y;
                vertices[k++] = z;
            }
        }

        /* Se leen los indices */

        /**
         *    k2 ------- k2+1
         *     |      /  | 
         *     |    /    |
         *     | /       |
         *    k1 ------- k1+1  
         *    k1---k2+1---k2   k1---k1+1---k2+1
         */
        k = 0;
        for (i = 0; i < segmentosV; i++) {
            k1 = i * (segmentosH + 1);      // inicio del actual segmentoV
            k2 = k1 + segmentosH + 1;       // inicio del siguiente segmentoV
            for (j = 0; j < segmentosH; j++) {
                indices[k++] = k1 + j;        // k1---k2+1---k2
                indices[k++] = k2 + j + 1;
                indices[k++] = k2 + j + 1;
                indices[k++] = k2 + j;
                indices[k++] = k2 + j;
                indices[k++] = k1 + j;

                indices[k++] = k1 + j;        // k1---k1+1---k2+1
                indices[k++] = k1 + j + 1;
                indices[k++] = k1 + j + 1;
                indices[k++] = k2 + j + 1;
                indices[k++] = k2 + j + 1;
                indices[k++] = k1 + j;
            }
        }

        /*
        console.log(vertices.length);
        for (i = 0; i < vertices.length; i+=3) {
          console.log(i + " : " + vertices[i] + "  " + vertices[i+1] + "  " + vertices[i+2])
        }
        console.log(indices.length);
        for (i = 0; i < indices.length; i+=6) {
          console.log(i + " : " + indices[i] + "  " + indices[i+1] + "  " + indices[i+2] + "  " + indices[i+3] + "  " + indices[i+4] + "  " + indices[i+5])
        }
        */

        /* Se crea el objeto del arreglo de vértices (VAO) */
        this.esferaVAO = gl.createVertexArray();

        /* Se activa el objeto */
        gl.bindVertexArray(this.esferaVAO);


        /* Se genera un nombre (código) para el buffer */
        var verticeBuffer = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, verticeBuffer);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los vértices (indice = 0) */
        gl.enableVertexAttribArray(0);

        /* Se especifica el arreglo de vértices */
        gl.vertexAttribPointer(0, 3, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var indiceBuffer = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, indiceBuffer);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices), gl.STATIC_DRAW);


        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

    }

    dibuja(gl) {

        /* Se activa el objeto del arreglo de vértices */
        gl.bindVertexArray(this.esferaVAO);

        /* Renderiza las primitivas desde los datos de los arreglos (vértices,
         * normales e indices) */
        gl.drawElements(gl.LINES, this.cantidadDeIndices, gl.UNSIGNED_SHORT, 0);

        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

    }
}

