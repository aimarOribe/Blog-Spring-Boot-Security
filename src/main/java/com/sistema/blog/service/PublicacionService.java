package com.sistema.blog.service;

import com.sistema.blog.dto.PublicacionDTO;
import com.sistema.blog.dto.PublicacionRespuesta;

public interface PublicacionService {
    public PublicacionRespuesta obtenerPublicaciones(int numeroPagina, int tamanoPagina, String orderPor, String sortDir);
    public PublicacionDTO obtenerPublicacionPorId(Long id);
    public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO);
    public PublicacionDTO actualizarPublicacion(PublicacionDTO publicacionDTO, long id);
    public void eliminarPublicacion(long id);
}
