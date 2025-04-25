package org.example.imagenes.operaciones;// Línea 1: Declaración del paquete al que pertenece esta clase.
// Un paquete es como una carpeta que organiza las clases relacionadas.
// En este caso, la clase pertenece al paquete "org.example.imagenes.operaciones".
// Línea 3: Importación de la clase "Imagen".
// Esto permite usar la clase "Imagen" en este archivo.
// La clase "Imagen" probablemente contiene métodos y atributos para manipular imágenes.
import org.example.imagenes.Imagen;

// Línea 5: Importación de la clase "Random".
// "Random" es una clase de Java que se usa para generar números aleatorios.
// En este caso, se usará para decidir aleatoriamente si un píxel será blanco o negro.
import java.util.Random;

// Línea 7: Declaración de la clase "RuidoSalPimienta".
// Una clase es una plantilla que define cómo se comporta un objeto.
// Esta clase extiende (hereda de) otra clase llamada "ComandoImagen".
// Esto significa que "RuidoSalPimienta" es un tipo de "ComandoImagen" y puede usar o sobrescribir
// los métodos y atributos definidos en "ComandoImagen".
public class RuidoSalPimienta extends ComandoImagen {

    // Línea 8: Declaración de un atributo privado llamado "probabilidad".
    // Este atributo almacena la probabilidad de que un píxel sea modificado.
    // Es privado, lo que significa que solo puede ser accedido desde dentro de esta clase.
    // El valor inicial es 0.05, lo que representa un 5% de probabilidad.
    private double probabilidad = 0.05; // 5% de los píxeles

    // Línea 9: Constructor de la clase "RuidoSalPimienta".
    // Un constructor es un método especial que se llama cuando se crea un objeto de esta clase.
    // Este constructor recibe un parámetro "imagen" de tipo "Imagen".
    public RuidoSalPimienta(Imagen imagen) {
        // Línea 10: Asignación del parámetro "imagen" al atributo "imagen".
        // "imagen" es un atributo heredado de la clase "ComandoImagen".
        // Esto permite que la clase "RuidoSalPimienta" trabaje con la imagen proporcionada.
        this.imagen = imagen;
    }

    // Línea 13: Sobrescritura del método "ejecutar".
    // Este método es obligatorio porque "ComandoImagen" probablemente lo define como abstracto.
    // Aquí se implementa la funcionalidad específica para agregar ruido de sal y pimienta a la imagen.
    @Override
    public void ejecutar() {
        // Línea 15: Creación de un objeto de la clase "Random".
        // Este objeto se usará para generar números aleatorios.
        Random rand = new Random();

        // Línea 16: Bucle externo que recorre las columnas de la imagen.
        // "imagen.getAncho()" devuelve el número total de columnas (ancho de la imagen).
        for (int i = 0; i < imagen.getAncho(); i++) {
            // Línea 17: Bucle interno que recorre las filas de la imagen.
            // "imagen.getAlto()" devuelve el número total de filas (alto de la imagen).
            for (int j = 0; j < imagen.getAlto(); j++) {
                // Línea 18: Generación de un número aleatorio entre 0 y 1.
                // Si el número es menor que "probabilidad" (5%), se modifica el píxel.
                if (rand.nextDouble() < probabilidad) {
                    // Línea 19: Generación de un valor booleano aleatorio.
                    // "rand.nextBoolean()" devuelve "true" o "false" al azar.
                    // Si es "true", el píxel será blanco (sal). Si es "false", será negro (pimienta).
                    boolean sal = rand.nextBoolean();

                    // Línea 20: Modificación del color del píxel en la posición (i, j).
                    // "imagen.set(i, j, color)" cambia el color del píxel en la posición indicada.
                    // Si "sal" es "true", el color será blanco (0xFFFFFF).
                    // Si "sal" es "false", el color será negro (0x000000).
                    imagen.set(i, j, sal ? 0xFFFFFF : 0x000000); // blanco o negro
                }
            }
        }

        // Línea 24: Notificación de que la imagen ha cambiado.
        // Esto puede ser útil si otras partes del programa necesitan actualizarse
        // o reaccionar a los cambios en la imagen.
        imagen.notificarCambios();
    }
}


