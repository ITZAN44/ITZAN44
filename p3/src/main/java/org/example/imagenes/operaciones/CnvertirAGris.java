// Línea 1: Declaración del paquete al que pertenece esta clase.
// Un paquete es como una carpeta que organiza las clases relacionadas.
// En este caso, la clase pertenece al paquete "org.example.imagenes.operaciones".
package org.example.imagenes.operaciones;

// Línea 4: Importación de la clase "Imagen".
// Esto permite usar la clase "Imagen" en este archivo.
// La clase "Imagen" probablemente contiene métodos y atributos para manipular imágenes.
import org.example.imagenes.Imagen;

// Línea 6: Declaración de la clase "CnvertirAGris".
// Esta clase extiende (hereda de) otra clase llamada "ComandoImagen".
// Esto significa que "CnvertirAGris" es un tipo de "ComandoImagen" y puede usar o sobrescribir
// los métodos y atributos definidos en "ComandoImagen".
public class CnvertirAGris extends ComandoImagen {

    // Línea 8: Constructor de la clase "CnvertirAGris".
    // Un constructor es un método especial que se llama cuando se crea un objeto de esta clase.
    // Este constructor recibe un parámetro "img" de tipo "Imagen".
    public CnvertirAGris(Imagen img) {
        // Línea 9: Asignación del parámetro "img" al atributo "imagen".
        // "imagen" es un atributo heredado de la clase "ComandoImagen".
        // Esto permite que la clase "CnvertirAGris" trabaje con la imagen proporcionada.
        imagen = img;
    }

    // Línea 12: Sobrescritura del método "ejecutar".
    // Este método es obligatorio porque "ComandoImagen" probablemente lo define como abstracto.
    // Aquí se implementa la funcionalidad específica para convertir una imagen a escala de grises.
    @Override
    public void ejecutar() {
        // Línea 14: Bucle externo que recorre las columnas de la imagen.
        // "imagen.getAncho()" devuelve el número total de columnas (ancho de la imagen).
        for (int i = 0; i < imagen.getAncho(); i++) {
            // Línea 15: Bucle interno que recorre las filas de la imagen.
            // "imagen.getAlto()" devuelve el número total de filas (alto de la imagen).
            for (int j = 0; j < imagen.getAlto(); j++) {
                // Línea 19: Obtención del componente rojo (R) del color del píxel en la posición (i, j).
                // "imagen.get(i, j)" devuelve un entero que representa el color del píxel.
                // El color está codificado en formato RGB (rojo, verde, azul).
                // "(0x00FF0000 & imagen.get(i, j))" aplica una máscara para extraer los bits del rojo.
                // ">> 16" desplaza esos bits 16 posiciones a la derecha para obtener el valor del rojo.
                int r = (0x00FF0000 & imagen.get(i, j)) >> 16;

                // Línea 20: Obtención del componente verde (G) del color del píxel.
                // "(0x0000FF00 & imagen.get(i, j))" aplica una máscara para extraer los bits del verde.
                // ">> 8" desplaza esos bits 8 posiciones a la derecha para obtener el valor del verde.
                int g = (0x0000FF00 & imagen.get(i, j)) >> 8;

                // Línea 21: Obtención del componente azul (B) del color del píxel.
                // "(0x000000FF & imagen.get(i, j))" aplica una máscara para extraer los bits del azul.
                // No es necesario desplazar los bits porque ya están en las posiciones menos significativas.
                int b = (0x000000FF & imagen.get(i, j));

                // Línea 23: Cálculo del promedio de los componentes rojo, verde y azul.
                // Esto convierte el color a un tono de gris.
                // La fórmula "(r + g + b) / 3" calcula el promedio de los tres componentes.
                int x = (r + g + b) / 3;

                // Línea 24: Reconstrucción del color en escala de grises.
                // "x" se usa para los tres componentes (rojo, verde y azul) porque en un color gris
                // todos los componentes tienen el mismo valor.
                // "(x | (x << 8) | (x << 16))" combina los valores de "x" para formar un color RGB.
                // "(x << 16)" coloca "x" en los bits del rojo.
                // "(x << 8)" coloca "x" en los bits del verde.
                // "x" se queda en los bits del azul.
                imagen.set(i, j, x | (x << 8) | (x << 16));
            }
        }

        // Línea 27: Notificación de que la imagen ha cambiado.
        // Esto puede ser útil si otras partes del programa necesitan actualizarse
        // o reaccionar a los cambios en la imagen.
        imagen.notificarCambios();
    }
}
