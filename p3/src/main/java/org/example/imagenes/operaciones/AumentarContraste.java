package org.example.imagenes.operaciones;

import org.example.imagenes.Imagen;

// Esta clase llamada AumentarContraste extiende (hereda de) la clase abstracta ComandoImagen.
// Esto significa que AumentarContraste es un tipo de ComandoImagen y debe implementar
// los métodos abstractos definidos en ComandoImagen o en su interfaz IComando.
public class AumentarContraste extends ComandoImagen {

    // Constructor de la clase AumentarContraste.
    // Este constructor recibe un objeto de tipo Imagen como parámetro y lo asigna
    // a la variable protegida "imagen" que se hereda de la clase ComandoImagen.
    public AumentarContraste(Imagen img) {
        // Aquí se asigna la imagen proporcionada al atributo "imagen" de la clase.
        // Esto permite que el objeto AumentarContraste trabaje con esta imagen.
        imagen = img;
    }

    // Este es el método que implementa la funcionalidad principal de la clase.
    // Sobrescribe (override) el método abstracto "ejecutar" definido en la clase base ComandoImagen.
    @Override
    public void ejecutar() {
        // Este primer bucle recorre cada columna de la imagen.
        // "imagen.getAncho()" devuelve el número total de columnas (ancho de la imagen).
        for (int i = 0; i < imagen.getAncho(); i++) {
            // Este segundo bucle recorre cada fila de la imagen.
            // "imagen.getAlto()" devuelve el número total de filas (alto de la imagen).
            for (int j = 0; j < imagen.getAlto(); j++) {
                // Se obtiene el color del píxel en la posición (i, j).
                // "imagen.get(i, j)" devuelve un entero que representa el color del píxel.
                int color = imagen.get(i, j);

                // Aquí se extrae el componente rojo (R) del color.
                // "(color >> 16) & 0xFF" desplaza los bits 16 posiciones a la derecha
                // para obtener los 8 bits más significativos (el rojo) y luego aplica
                // una máscara con 0xFF para asegurarse de que solo queden esos 8 bits.
                // Si el valor del rojo es mayor a 128, se aumenta en 30 (hasta un máximo de 255).
                // Si es menor o igual a 128, se reduce en 30 (hasta un mínimo de 0).
                int r = ((color >> 16) & 0xFF) > 128
                        ? Math.min(255, ((color >> 16) & 0xFF) + 30)
                        : Math.max(0, ((color >> 16) & 0xFF) - 30);

                // Se hace lo mismo para el componente verde (G).
                // "(color >> 8) & 0xFF" desplaza los bits 8 posiciones a la derecha
                // para obtener los 8 bits del componente verde.
                int g = ((color >> 8) & 0xFF) > 128
                        ? Math.min(255, ((color >> 8) & 0xFF) + 30)
                        : Math.max(0, ((color >> 8) & 0xFF) - 30);

                // Finalmente, se hace lo mismo para el componente azul (B).
                // "color & 0xFF" aplica una máscara para obtener los 8 bits menos significativos,
                // que representan el componente azul.
                int b = (color & 0xFF) > 128
                        ? Math.min(255, (color & 0xFF) + 30)
                        : Math.max(0, (color & 0xFF) - 30);

                // Se reconstruye el color con los nuevos valores de rojo, verde y azul.
                // "(r << 16)" desplaza el valor del rojo 16 bits a la izquierda,
                // "(g << 8)" desplaza el valor del verde 8 bits a la izquierda,
                // y "b" se queda en los 8 bits menos significativos.
                // Luego, se combinan los tres valores con el operador OR (|).
                imagen.set(i, j, (r << 16) | (g << 8) | b);
            }
        }

        // Este método notifica que la imagen ha cambiado.
        // Esto puede ser útil si hay otras partes del programa que necesitan
        // actualizarse o reaccionar a los cambios en la imagen.
        imagen.notificarCambios();
    }
}
