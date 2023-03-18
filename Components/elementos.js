
class Circulo {
    constructor(gl, radio) {

        /* Las coordenadas cartesianas (x, y) */
        var vertices = [];

        /* Los colores x c/vértice (r,g,b,a) */
        var colores = [];

        /* Lee los vértices (x,y) y colores (r,g,b,a) */
        for (var i = 0; i < 360; i++) {
            vertices.push(radio * Math.cos(i * Math.PI / 180));
            vertices.push(radio * Math.sin(i * Math.PI / 180));

            colores.push(1);
            colores.push(0);
            colores.push(0);
            colores.push(1);
        }

        this.circuloVAO = gl.createVertexArray();
        gl.bindVertexArray(this.circuloVAO);

        var codigoVertices = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoVertices);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
        gl.enableVertexAttribArray(0);
        gl.vertexAttribPointer(0, 2, gl.FLOAT, false, 0, 0);


        var codigoColores = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoColores);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colores), gl.STATIC_DRAW);
        gl.enableVertexAttribArray(1);
        gl.vertexAttribPointer(1, 4, gl.FLOAT, false, 0, 0);

        gl.bindVertexArray(null);
        gl.bindBuffer(gl.ARRAY_BUFFER, null);

    }

    dibuja(gl, llenado) {
        identidad(MatrizModelo);
        traslacion(MatrizModelo, 0, 0, 0);
        gl.uniformMatrix4fv(uMatrizModelo, false, MatrizModelo);
        gl.bindVertexArray(this.circuloVAO);
        gl.drawArrays((llenado) ? gl.TRIANGLE_FAN : gl.LINE_LOOP, 0, 360);
        gl.bindVertexArray(null);
    }
}

class Rectangulo {
    constructor(gl) {

        /***************************************************************************/
        /* Paso 3: Se define la geometría y se almacenan en los buffers de memoria.*/
        /***************************************************************************/

        /**
         *       2
         *       /\
         *      /  \
         *     /    \
         *    /      \
         *   /________\
         *  0          1  
         */

        /* Las coordenadas cartesianas (x, y) */
        var vertices = [
            -1, -1, // 0
            1, -1, // 1
            1, 1, // 2
            -1, 1, // 3

            -1, -1, // 0
            1, -1, // 1
            1, 1, // 2
            -1, 1, // 3

            -1, -1, // 0
            1, -1, // 1
            1, 1, // 2
            -1, 1, // 3
        ];

        /* Los colores x c/vértice (r,g,b,a) */
        var colores = [
            1, 0, 0, 1, // 0
            1, 1, 0, 1, // 1
            1, 1, 0, 1, // 2
            1, 0, 0, 1, // 3

            0, 0, 1, 1, // 0
            0, 0, 1, 1, // 1
            0, 0, 1, 1, // 2
            0, 0, 1, 1, // 3

            1, 1, 0, 1, // 0
            1, 1, 0, 1, // 1
            1, 1, 0, 1, // 2
            1, 1, 0, 1, // 3
        ];

        /* Se crea el objeto del arreglo de vértices (VAO) */
        this.rectanguloVAO = gl.createVertexArray();

        /* Se activa el objeto */
        gl.bindVertexArray(this.rectanguloVAO);


        /* Se genera un nombre (código) para el buffer */
        var codigoVertices = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoVertices);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los vértices (indice = 0) */
        gl.enableVertexAttribArray(0);

        /* Se especifica los atributos del arreglo de vértices */
        gl.vertexAttribPointer(0, 2, gl.FLOAT, false, 0, 0);


        /* Se genera un nombre (código) para el buffer */
        var codigoColores = gl.createBuffer();

        /* Se asigna un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, codigoColores);

        /* Se transfiere los datos desde la memoria nativa al buffer de la GPU */
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colores), gl.STATIC_DRAW);

        /* Se habilita el arreglo de los colores (indice = 1) */
        gl.enableVertexAttribArray(1);

        /* Se especifica los atributos del arreglo de colores */
        gl.vertexAttribPointer(1, 4, gl.FLOAT, false, 0, 0);


        /* Se desactiva el objeto del arreglo de vértices */
        gl.bindVertexArray(null);

        /* Se deja de asignar un nombre (código) al buffer */
        gl.bindBuffer(gl.ARRAY_BUFFER, null);
    }


    dibuja(gl) {
        identidad(MatrizModelo);
        traslacion(MatrizModelo, 0, 0, 0)
        gl.uniformMatrix4fv(uMatrizModelo, false, MatrizModelo);
        gl.bindVertexArray(this.rectanguloVAO);
        gl.drawArrays(gl.TRIANGLE_FAN, 0, 4);
        gl.bindVertexArray(null);
    }
}


export default
    {
        Circulo,
        Rectangulo
    }