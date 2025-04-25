package org.example.imagenes.operaciones;

import org.example.imagenes.Imagen;

public class EspejoVertical extends ComandoImagen {
    public EspejoVertical(Imagen imagen) {
        this.imagen = imagen;
    }
    @Override
    public void ejecutar() {
        int ancho = imagen.getAncho();
        int alto = imagen.getAlto();
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto / 2; j++) {
                int opuesto = alto - j - 1;
                int tmp = imagen.get(i, j);
                imagen.set(i, j, imagen.get(i, opuesto));
                imagen.set(i, opuesto, tmp);
            }
        }
        imagen.notificarCambios();
    }
}
