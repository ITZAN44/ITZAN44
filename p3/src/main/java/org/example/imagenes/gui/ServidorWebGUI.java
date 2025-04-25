package org.example.imagenes.gui;

import org.example.servidor.ServidorWebImagenes;

import javax.swing.*;
import java.awt.*;

public class ServidorWebGUI extends JFrame {
    private JButton btnIniciar;
    private JButton btnDetener;
    private JTextArea logArea;
    private ServidorWebImagenes servidor;

   // Constructor de la clase `ServidorWebGUI`.
    // Un constructor es un método especial que se llama automáticamente cuando se crea un objeto de esta clase.
    // Su propósito es inicializar el objeto, configurando sus propiedades o ejecutando código necesario al inicio.
    public ServidorWebGUI() {
        // Llama al método `setTitle` para establecer el título de la ventana de la interfaz gráfica.
        // Este título aparecerá en la barra superior de la ventana.
        setTitle("Servidor Web de Imágenes");

        // Llama al método `setSize` para definir el tamaño de la ventana.
        // En este caso, la ventana tendrá un ancho de 600 píxeles y una altura de 400 píxeles.
        setSize(600, 400);

        // Llama al método `setDefaultCloseOperation` para especificar qué debe ocurrir
        // cuando el usuario cierra la ventana. `EXIT_ON_CLOSE` indica que el programa
        // debe terminar completamente cuando se cierre la ventana.
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Llama al método `setLocationRelativeTo` con el valor `null`.
        // Esto centra la ventana en la pantalla cuando se abre, en lugar de aparecer en la esquina superior izquierda.
        setLocationRelativeTo(null);

        // Llama al método `initUI`, que es un método privado definido más adelante en esta clase.
        // Este método se encarga de inicializar y configurar los elementos de la interfaz gráfica (botones, paneles, etc.).
        initUI();
    }

    // Método privado llamado `initUI`.
    // Este método se encarga de inicializar y configurar la interfaz gráfica de usuario (GUI) de la aplicación.
    // Es privado porque solo se utiliza dentro de esta clase y no necesita ser accesible desde otras partes del programa.
    private void initUI() {
        // Crea un nuevo panel llamado `panel` con un diseño de tipo `BorderLayout`.
        // `BorderLayout` es un tipo de diseño que organiza los componentes en cinco áreas: norte, sur, este, oeste y centro.
        JPanel panel = new JPanel(new BorderLayout());

        // Crea otro panel llamado `top` con el diseño predeterminado (que es `FlowLayout`).
        // Este panel se usará para colocar los botones en la parte superior de la ventana.
        JPanel top = new JPanel();

        // Crea un botón llamado `btnIniciar` con el texto "Iniciar Servidor".
        // Este botón permitirá al usuario iniciar el servidor cuando haga clic en él.
        btnIniciar = new JButton("Iniciar Servidor");

        // Crea otro botón llamado `btnDetener` con el texto "Detener Servidor".
        // Este botón permitirá al usuario detener el servidor, pero inicialmente estará deshabilitado.
        btnDetener = new JButton("Detener Servidor");

        // Deshabilita el botón `btnDetener` al inicio porque el servidor aún no está en ejecución.
        // Esto evita que el usuario intente detener un servidor que no ha sido iniciado.
        btnDetener.setEnabled(false);

        // Agrega el botón `btnIniciar` al panel `top`.
        // Esto coloca el botón en el panel superior de la ventana.
        top.add(btnIniciar);

        // Agrega el botón `btnDetener` al panel `top`.
        // Ahora ambos botones estarán juntos en el panel superior.
        top.add(btnDetener);

        // Crea un área de texto llamada `logArea` donde se mostrarán mensajes de registro (log).
        // Por ejemplo, se puede usar para mostrar mensajes como "Servidor iniciado" o "Servidor detenido".
        logArea = new JTextArea();

        // Hace que el área de texto `logArea` no sea editable.
        // Esto significa que el usuario no podrá escribir directamente en esta área, solo se usará para mostrar mensajes.
        logArea.setEditable(false);

        // Crea un componente de desplazamiento llamado `scroll` y le pasa `logArea` como contenido.
        // Esto permite que el área de texto tenga barras de desplazamiento si el contenido es más grande que el área visible.
        JScrollPane scroll = new JScrollPane(logArea);

        // Agrega el panel `top` (que contiene los botones) a la parte superior del panel principal `panel`.
        // Esto se hace usando `BorderLayout.NORTH`, que coloca el contenido en la parte superior.
        panel.add(top, BorderLayout.NORTH);

        // Agrega el componente de desplazamiento `scroll` (que contiene el área de texto) al centro del panel principal `panel`.
        // Esto se hace usando `BorderLayout.CENTER`, que ocupa el espacio central disponible.
        panel.add(scroll, BorderLayout.CENTER);

        // Agrega el panel principal `panel` a la ventana principal de la aplicación.
        // Esto hace que todos los componentes configurados anteriormente se muestren en la ventana.
        add(panel);

        // Agrega un "escuchador de eventos" (ActionListener) al botón `btnIniciar`.
        // Este escuchador ejecutará el método `iniciarServidor` cuando el usuario haga clic en el botón.
        btnIniciar.addActionListener(e -> iniciarServidor());

        // Agrega un "escuchador de eventos" (ActionListener) al botón `btnDetener`.
        // Este escuchador ejecutará el método `detenerServidor` cuando el usuario haga clic en el botón.
        btnDetener.addActionListener(e -> detenerServidor());
    }

   // Método privado llamado `iniciarServidor`.
    // Este método se utiliza para iniciar el servidor web de imágenes.
    // Es privado porque solo se puede llamar desde dentro de esta clase.
    // No devuelve ningún valor porque su tipo de retorno es `void`.
    private void iniciarServidor() {
        btnIniciar.setEnabled(false);
        btnDetener.setEnabled(true);

        // Línea 1: Se crea un nuevo objeto de la clase `ServidorWebImagenes`.
        // `ServidorWebImagenes` es una clase que representa el servidor web que manejará las imágenes.
        // El constructor de esta clase recibe dos parámetros:
        // - `8050`: Este es el número de puerto en el que el servidor escuchará las solicitudes.
        //   Un puerto es como una "puerta" en la computadora que permite la comunicación con otros programas.
        // - `this`: Esto se refiere a la instancia actual de la clase `ServidorWebGUI`.
        //   Se pasa como referencia para que el servidor pueda interactuar con la interfaz gráfica (por ejemplo, para registrar mensajes en el área de texto).
        servidor = new ServidorWebImagenes(8050, this);

        // Línea 2: Se llama al método `start` del objeto `servidor`.
        // Este método inicia el servidor en un nuevo hilo de ejecución.
        // Un hilo (o "thread") es como un "camino" separado en el que el programa puede ejecutar tareas.
        // Esto permite que el servidor funcione en segundo plano mientras la interfaz gráfica sigue respondiendo al usuario.
        servidor.start();
    }
        // Línea 3: Se deshabilita el botón `btnIniciar`.
        // Esto se hace llamando al método `setEnabled` con el valor `false`.

    // Método privado llamado `detenerServidor`.
    // Este método se utiliza para detener el servidor web que está en ejecución.
    // Es privado porque solo se puede llamar desde dentro de esta clase.
    // No devuelve ningún valor porque su tipo de retorno es `void`.
    // Esto significa que su propósito es realizar una acción, no calcular o devolver un resultado.
    private void detenerServidor() {
        // Línea 1: Llama al método `detener` del objeto `servidor`.
        // `servidor` es una instancia de la clase `ServidorWebImagenes`, que representa el servidor web.
        // El método `detener` probablemente contiene la lógica para detener el servidor,
        // como cerrar conexiones, liberar recursos, etc.
        servidor.detener();

        // Línea 2: Habilita el botón `btnIniciar` llamando al método `setEnabled` con el valor `true`.
        // Esto permite que el usuario pueda hacer clic en el botón "Iniciar Servidor" nuevamente.
        // Se hace porque, una vez que el servidor se detiene, tiene sentido permitir que se pueda reiniciar.
        btnIniciar.setEnabled(true);

        // Línea 3: Deshabilita el botón `btnDetener` llamando al método `setEnabled` con el valor `false`.
        // Esto evita que el usuario intente detener un servidor que ya no está en ejecución.
        // Es una buena práctica deshabilitar botones que no tienen sentido en un estado específico de la aplicación.
        btnDetener.setEnabled(false);

        // Línea 4: Llama al método `log` para registrar un mensaje en el área de texto `logArea`.
        // El mensaje "Servidor detenido." se agrega al registro para informar al usuario
        // que el servidor se ha detenido correctamente.
        // Este método utiliza `SwingUtilities.invokeLater` internamente para asegurarse
        // de que las actualizaciones de la interfaz gráfica se realicen en el hilo correcto.
        log("Servidor detenido.");
    }

   // Método público llamado `log`.
    // Este método se utiliza para agregar mensajes de texto al área de texto `logArea`.
    // Es público porque puede ser llamado desde otras clases o partes del programa.
    // Recibe un parámetro:
    // - `mensaje`: una cadena de texto (String) que contiene el mensaje que se desea mostrar en el área de texto.
    public void log(String mensaje) {

        // `SwingUtilities.invokeLater` es un método que asegura que el código dentro de su bloque
        // se ejecute en el hilo de la interfaz gráfica (Event Dispatch Thread o EDT).
        // Esto es importante porque todas las actualizaciones de la interfaz gráfica en Swing
        // deben realizarse en este hilo para evitar errores o comportamientos inesperados.
        SwingUtilities.invokeLater(() ->

            // `logArea.append` agrega el texto del mensaje al final del área de texto `logArea`.
            // `mensaje + "\n"` concatena el mensaje con un salto de línea ("\n") para que cada mensaje
            // aparezca en una nueva línea dentro del área de texto.
            logArea.append(mensaje + "\n")
        );
    }


}
