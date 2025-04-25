package org.example.imagenes.operaciones;

import org.example.imagenes.Imagen;

public class SuavizarImagen extends ComandoImagen {
    public SuavizarImagen(Imagen imagen) {
        this.imagen = imagen;
    }
    @Override
    public void ejecutar() {
        int ancho = imagen.getAncho();
        int alto = imagen.getAlto();
        int[][] copia = new int[ancho][alto];

        for (int i = 1; i < ancho - 1; i++) {
            for (int j = 1; j < alto - 1; j++) {
                int r = 0, g = 0, b = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int color = imagen.get(i + dx, j + dy);
                        r += (color >> 16) & 0xFF;
                        g += (color >> 8) & 0xFF;
                        b += color & 0xFF;
                    }
                }
                r /= 9; g /= 9; b /= 9;
                copia[i][j] = (r << 16) | (g << 8) | b;
            }
        }

        for (int i = 1; i < ancho - 1; i++) {
            for (int j = 1; j < alto - 1; j++) {
                imagen.set(i, j, copia[i][j]);
            }
        }
        imagen.notificarCambios();
    }
}
