package org.example.servidor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.imagenes.Imagen;
import org.example.imagenes.gui.ServidorWebGUI;
import org.example.imagenes.operaciones.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorWebImagenes extends Thread {
    private int puerto;
    private volatile boolean activo = true;
    private final Logger logger = LogManager.getRootLogger();
    private ServerSocket servidor;
    private final ServidorWebGUI gui;

  // Constructor de la clase ServidorWebImagenes
    // Este método especial se llama automáticamente cuando se crea un nuevo objeto de esta clase.
    // Su propósito es inicializar los valores de las variables de instancia.
    public ServidorWebImagenes(int puerto, ServidorWebGUI gui) {
        // `this.puerto` se refiere a la variable de instancia `puerto` de la clase.
        // Se le asigna el valor del parámetro `puerto` que se pasa al constructor.
        this.puerto = puerto;

        // `this.gui` se refiere a la variable de instancia `gui` de la clase.
        // Se le asigna el valor del parámetro `gui` que se pasa al constructor.
        // Esto permite que el servidor interactúe con la interfaz gráfica (GUI).
        this.gui = gui;
    }

    // Método público llamado `detener`.
    // Este método se utiliza para detener el servidor de manera controlada.
    // Cambia el estado del servidor a inactivo y cierra el socket del servidor.
    public void detener() {
        // Cambia la variable `activo` a `false`.
        // Esto indica que el servidor debe detenerse.
        activo = false;

        // Bloque `try-catch` para manejar posibles errores al cerrar el servidor.
        try {
            // Verifica si el objeto `servidor` no es nulo (es decir, que ya fue inicializado)
            // y si el socket del servidor no está cerrado.
            if (servidor != null && !servidor.isClosed()) {
                // Llama al método `close()` del objeto `servidor`.
                // Esto cierra el socket del servidor y libera el puerto que estaba usando.
                servidor.close();
            }
        } catch (IOException e) {
            // Si ocurre un error al intentar cerrar el servidor, este bloque lo maneja.
            // `IOException` es una excepción que ocurre cuando hay problemas de entrada/salida.
            // Por ejemplo, si el socket ya está cerrado o hay un problema de red.

            // Llama al método `log` de la interfaz gráfica (GUI) para mostrar un mensaje de error.
            // `e.getMessage()` obtiene el mensaje de error asociado con la excepción.
            gui.log("Error cerrando el servidor: " + e.getMessage());
        }
    }
    // Método principal que se ejecuta cuando se inicia el hilo del servidor.
    public void run() {
        // Bloque try-catch para manejar excepciones al iniciar el servidor.
        try {
            // Se crea un objeto ServerSocket que escucha conexiones en el puerto especificado.
            // ServerSocket es una clase que permite que el servidor acepte conexiones de clientes.
            servidor = new ServerSocket(puerto);

            // Se registra un mensaje en la interfaz gráfica (GUI) indicando que el servidor se inició correctamente.
            gui.log("Servidor iniciado en puerto " + puerto);

            // Se registra el mismo mensaje en el sistema de logs para propósitos de depuración o auditoría.
            logger.info("Servidor iniciado en puerto " + puerto);

            // Bucle principal del servidor. Este bucle se ejecuta mientras la variable `activo` sea verdadera.
            // `activo` es una variable que permite detener el servidor de forma controlada.
            while (activo) {
                try {
                    // El método `accept()` espera hasta que un cliente se conecte al servidor.
                    // Cuando un cliente se conecta, se devuelve un objeto Socket que representa la conexión.
                    Socket clt = servidor.accept();

                    // Se registra un mensaje en la GUI indicando que un cliente se ha conectado.
                    gui.log("Cliente conectado");

                    // Se registra el mismo mensaje en los logs.
                    logger.info("Cliente conectado");

                    // Se crea un nuevo hilo para manejar al cliente conectado.
                    // Esto permite que el servidor siga aceptando nuevas conexiones mientras procesa al cliente actual.
                    new Thread(() -> manejarCliente(clt)).start();
                } catch (IOException e) {
                    // Si ocurre un error al aceptar una conexión, se maneja aquí.
                    // Por ejemplo, si el servidor está activo pero ocurre un problema de red.
                    if (activo) {
                        // Se registra el error en la GUI para que el usuario lo vea.
                        gui.log("Error aceptando cliente: " + e.getMessage());
                    }
                    // También se registra el error en los logs para propósitos de depuración.
                    logger.error("Error aceptando cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // Si ocurre un error al iniciar el servidor (por ejemplo, el puerto ya está en uso), se maneja aquí.
            // Se registra el error en la GUI.
            gui.log("No se pudo iniciar el servidor: " + e.getMessage());

            // También se registra el error en los logs.
            logger.error("No se pudo iniciar el servidor: " + e.getMessage());
        } finally {
            // El bloque `finally` se ejecuta siempre, ya sea que ocurra o no una excepción.
            // Aquí se registra un mensaje indicando que el servidor se ha detenido.
            gui.log("Servidor detenido.");
            logger.info("Servidor detenido.");
        }
    }

//manejar cliente se encagar de leer los datos del cliente verificar que su solicitud sea valida y enviarlos si se encontro atraves del servidor
    private void manejarCliente(Socket clt) {
        // Inicia un bloque try-with-resources para manejar automáticamente el cierre de recursos.
        try (
                // Crea un BufferedReader para leer datos del cliente a través del socket.
                BufferedReader input = new BufferedReader(new InputStreamReader(clt.getInputStream()));
                // Crea un OutputStream para enviar datos al cliente a través del socket.
                OutputStream output = clt.getOutputStream()
        ) {
            // Crea un StringBuilder para almacenar la solicitud HTTP recibida del cliente.
            StringBuilder entrada = new StringBuilder();
            // Lee la primera línea de la solicitud HTTP y la agrega al StringBuilder.
            entrada.append(input.readLine());
            // Mientras haya más datos disponibles en el flujo de entrada, sigue leyendo línea por línea.
            while (input.ready()) {
                entrada.append(input.readLine());
            }
            // Registra la solicitud recibida en la interfaz gráfica (GUI) para que el usuario la vea.
            gui.log("Solicitud: " + entrada);
            // Registra la solicitud en el sistema de logs para propósitos de depuración o auditoría.
            logger.info("Solicitud: " + entrada);
            // Llama al método buscarYEnviarImagen para procesar la solicitud y enviar una respuesta al cliente.
            buscarYEnviarImagen(output, entrada.toString());
        } catch (IOException e) {
            // Si ocurre un error de entrada/salida, registra el error en la GUI.
            gui.log("Error manejando cliente: " + e.getMessage());
            // También registra el error en el sistema de logs.
            logger.error("Error manejando cliente: " + e.getMessage());
        } finally {
            // En el bloque finally, intenta cerrar el socket del cliente para liberar recursos.
            try {
                clt.close();
            } catch (IOException ignored) {
                // Si ocurre un error al cerrar el socket, se ignora porque no es crítico.
            }
        }
    }

    // Método que busca y envía una imagen al cliente según la solicitud HTTP recibida.
    private void buscarYEnviarImagen(OutputStream output, String ordenHttp) throws IOException {
        // Define la ruta base donde se encuentran los archivos públicos del servidor.
        // En este caso, es una carpeta en el disco duro del sistema.
        String pathBase = "D:\\itzan copia\\programacion 3\\p3\\public\\";

        // Crea un patrón de expresión regular para identificar solicitudes HTTP específicas.
        // Este patrón busca solicitudes que sigan el formato:
        // GET /__op__/imagenes/{operacion}/img/{nombreArchivo.jpg} HTTP
        Matcher matcher = Pattern.compile("^GET /__op__/imagenes/(\\w+)/img/([\\w\\d_\\-.]+\\.(?:jpg|png|jpeg)) HTTP").matcher(ordenHttp);
        // Verifica si la solicitud coincide con el patrón definido.
        if (matcher.find()) {
            // Extrae la operación solicitada (por ejemplo, "convertir_gris") y la convierte a minúsculas.
            String operacion = matcher.group(1).toLowerCase();

            // Extrae el nombre del archivo solicitado (por ejemplo, "imagen.jpg").
            String nombreArchivo = matcher.group(2);

            // Construye la ruta completa del archivo solicitado combinando la ruta base y el nombre del archivo.
            Path path = Path.of(pathBase + "/imagenes/" + nombreArchivo);

            // Verifica si el archivo solicitado existe en el sistema de archivos.
            if (!Files.exists(path)) {
                // Si el archivo no existe, envía una respuesta HTTP 404 al cliente.
                enviar404(output);
                return; // Termina la ejecución del método.
            }

            // Carga la imagen original desde el archivo en el sistema de archivos.
            BufferedImage imgOriginal = ImageIO.read(path.toFile());

            // Crea un objeto de tipo Imagen, que es una clase personalizada para manejar imágenes.
            // Se inicializa con el ancho y alto de la imagen original.
            Imagen imagen = new Imagen(imgOriginal.getWidth(), imgOriginal.getHeight());

            // Establece la imagen cargada en el objeto Imagen.
            imagen.setImage(imgOriginal);

            // Obtiene un comando específico para realizar la operación solicitada sobre la imagen.
            ComandoImagen comando = obtenerComando(operacion, imagen);

            // Si no se encuentra un comando válido para la operación solicitada:
            if (comando == null) {
                // Registra un mensaje de error en la interfaz gráfica y en los logs.
                gui.log("Operación no válida: " + operacion);
                logger.error("Operación no válida: " + operacion);

                // Envía una respuesta HTTP 404 al cliente.
                enviar404(output);
                return; // Termina la ejecución del método.
            }

            // Ejecuta el comando para procesar la imagen según la operación solicitada.
            comando.ejecutar();

            // Convierte la imagen procesada en un arreglo de bytes para enviarla al cliente.
            byte[] datos = imagen.getBytes();

            // Escribe la cabecera HTTP para indicar que la respuesta es una imagen PNG.
            // También incluye la longitud del contenido (en bytes).
            output.write(("HTTP/1.1 200 OK\r\nContent-Type: image/png\r\nContent-Length: " + datos.length + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));

            // Escribe los datos de la imagen procesada en el flujo de salida para enviarlos al cliente.
            output.write(datos);
        } else {
            // Si la solicitud no coincide con el patrón de imágenes, se asume que es un archivo estático.

            // Extrae el archivo solicitado de la solicitud HTTP.
            String archivoSolicitado = ordenHttp.split(" ")[1];

            // Construye la ruta completa del archivo solicitado.
            Path path = Path.of(pathBase + archivoSolicitado);

            // Verifica si el archivo solicitado existe en el sistema de archivos.
            if (!Files.exists(path)) {
                // Si el archivo no existe, envía una respuesta HTTP 404 al cliente.
                enviar404(output);
                return; // Termina la ejecución del método.
            }

            // Obtiene la extensión del archivo solicitado (por ejemplo, "html", "css", "jpg").
            String extension = getExtension(path);

            // Determina el tipo de contenido (MIME type) del archivo según su extensión.
            String contentType = obtenerTipoContenido(extension);

            // Lee el contenido del archivo solicitado en un arreglo de bytes.
            byte[] datos = Files.readAllBytes(path);

            // Escribe la cabecera HTTP para indicar que la respuesta contiene el archivo solicitado.
            // Incluye el tipo de contenido y la longitud del contenido.
            output.write(("HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\nContent-Length: " + datos.length + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));

            // Escribe los datos del archivo en el flujo de salida para enviarlos al cliente.
            output.write(datos);
        }
    }


    // Método privado llamado `getExtension`.
    // Este método se utiliza para obtener la extensión de un archivo a partir de su nombre.
    // Por ejemplo, si el archivo se llama "imagen.jpg", este método devolverá "jpg".
    private String getExtension(Path path) {
        // `path.getFileName()` obtiene el nombre del archivo desde el objeto `Path`.
        // `toString()` convierte ese nombre en una cadena de texto (String).
        String fileName = path.getFileName().toString();

        // `lastIndexOf('.')` busca la última posición del carácter '.' en el nombre del archivo.
        // Esto es útil porque las extensiones de archivo suelen estar después del último punto.
        int dotIndex = fileName.lastIndexOf('.');

        // Si `dotIndex` es diferente de -1, significa que se encontró un punto en el nombre del archivo.
        // En ese caso, se devuelve la parte del nombre que está después del punto (la extensión).
        // Si no se encuentra un punto, se devuelve una cadena vacía ("").
        return (dotIndex != -1) ? fileName.substring(dotIndex + 1) : "";
    }

    // Método privado llamado `obtenerTipoContenido`.
    // Este método se utiliza para determinar el tipo de contenido (MIME type) de un archivo
    // según su extensión. Los tipos MIME son estándares que indican el tipo de datos que
    // contiene un archivo, como "text/html" para archivos HTML o "image/png" para imágenes PNG.
    private String obtenerTipoContenido(String extension) {
        // `switch` es una estructura de control que permite ejecutar diferentes bloques de código
        // según el valor de la variable `extension`. Es más limpio que usar múltiples `if-else`.
        switch (extension.toLowerCase()) { // `toLowerCase()` convierte la extensión a minúsculas.
            case "html": // Si la extensión es "html", devuelve "text/html".
                return "text/html";
            case "css": // Si la extensión es "css", devuelve "text/css".
                return "text/css";
            case "js": // Si la extensión es "js", devuelve "application/javascript".
                return "application/javascript";
            case "png": // Si la extensión es "png", devuelve "image/png".
                return "image/png";
            case "jpg": // Si la extensión es "jpg" o "jpeg", devuelve "image/jpeg".+
                return "image/jpg";
            case "jpeg":
                return "image/jpeg";
            case "json": // Si la extensión es "json", devuelve "application/json".
                return "application/json";
            default: // Si la extensión no coincide con ninguno de los casos anteriores,
                     // devuelve "application/octet-stream", que es un tipo genérico para archivos binarios.
                return "application/octet-stream";
        }
    }

    // Método privado llamado `obtenerComando`.
// Este método se utiliza para obtener un objeto de tipo `ComandoImagen` que representa
// una operación específica que se realizará sobre una imagen.
// Recibe dos parámetros:
// - `operacion`: un String que indica el nombre de la operación solicitada (por ejemplo, "convertir_gris").
// - `imagen`: un objeto de tipo `Imagen` que contiene la imagen sobre la que se aplicará la operación.
private ComandoImagen obtenerComando(String operacion, Imagen imagen) {
    // La palabra clave `switch` se utiliza para comparar el valor de la variable `operacion`
    // con diferentes casos posibles. Es una alternativa más limpia y legible que usar múltiples
    // sentencias `if-else` cuando se tienen muchas opciones.
    return switch (operacion) {
        // Caso 1: Si `operacion` es igual a "convertir_gris", se crea un nuevo objeto
        // de la clase `CnvertirAGris` (nota: parece haber un error tipográfico en el nombre de la clase)
        // y se le pasa la imagen como parámetro.
        case "convertir_gris" -> new CnvertirAGris(imagen);

        // Caso 2: Si `operacion` es igual a "espejoh", se crea un nuevo objeto
        // de la clase `EspejoHorizontal` y se le pasa la imagen como parámetro.
        case "espejoh" -> new EspejoHorizontal(imagen);

        // Caso 3: Si `operacion` es igual a "espejov", se crea un nuevo objeto
        // de la clase `EspejoVertical` y se le pasa la imagen como parámetro.
        case "espejov" -> new EspejoVertical(imagen);

        // Caso 4: Si `operacion` es igual a "suavizar", se crea un nuevo objeto
        // de la clase `SuavizarImagen` y se le pasa la imagen como parámetro.
        case "suavizar" -> new SuavizarImagen(imagen);

        // Caso 5: Si `operacion` es igual a "contraste", se crea un nuevo objeto
        // de la clase `AumentarContraste` y se le pasa la imagen como parámetro.
        case "contraste" -> new AumentarContraste(imagen);

        // Caso 6: Si `operacion` es igual a "ruido", se crea un nuevo objeto
        // de la clase `RuidoSalPimienta` y se le pasa la imagen como parámetro.
        case "ruido" -> new RuidoSalPimienta(imagen);

        // Caso por defecto: Si `operacion` no coincide con ninguno de los casos anteriores,
        // se devuelve `null`. Esto indica que no se encontró un comando válido para la operación solicitada.
        default -> null;
    };
}

// Método privado llamado `enviar404`.
// Este método se utiliza para enviar una respuesta HTTP 404 al cliente cuando el recurso solicitado
// no se encuentra disponible en el servidor.
// Recibe un parámetro:
// - `output`: un objeto de tipo `OutputStream` que representa el flujo de salida hacia el cliente.
private void enviar404(OutputStream output) throws IOException {
    // Se define un mensaje HTTP que indica que el recurso no fue encontrado.
    // Este mensaje incluye:
    // - El código de estado HTTP 404 (Not Found).
    // - El tipo de contenido de la respuesta (texto plano).
    // - Un mensaje adicional que dice "Recurso no encontrado".
    String mensaje = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\nRecurso no encontrado";

    // Se escribe el mensaje en el flujo de salida para enviarlo al cliente.
    // El método `getBytes(StandardCharsets.UTF_8)` convierte el mensaje en un arreglo de bytes
    // utilizando la codificación UTF-8, que es un estándar para representar texto.
    output.write(mensaje.getBytes(StandardCharsets.UTF_8));
 }
}