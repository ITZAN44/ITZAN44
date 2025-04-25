package org.example.imagenes.operaciones;

import org.example.imagenes.Imagen;

public class EspejoHorizontal extends ComandoImagen {
    public EspejoHorizontal(Imagen imagen) {
        this.imagen = imagen;
    }
    @Override
    public void ejecutar() {

        for (int j = 0; j < imagen.getAlto(); j++) {
            for (int i = 0; i < imagen.getAncho() / 2; i++) {
                int opuesto = imagen.getAncho() - i - 1;
                int tmp = imagen.get(i, j);
                imagen.set(i, j, imagen.get(opuesto, j));
                imagen.set(opuesto, j, tmp);
            }
        }
        imagen.notificarCambios();
    }
}
