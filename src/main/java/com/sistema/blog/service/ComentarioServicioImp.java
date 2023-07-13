package com.sistema.blog.service;

import com.sistema.blog.dto.ComentarioDTO;
import com.sistema.blog.entity.Comentario;
import com.sistema.blog.entity.Publicacion;
import com.sistema.blog.excepctions.BlogAppException;
import com.sistema.blog.excepctions.ResourceNotFoundException;
import com.sistema.blog.repository.ComentarioRepository;
import com.sistema.blog.repository.PublicacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioServicioImp implements ComentarioService{

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ComentarioDTO crearComentario(long publicacionId, ComentarioDTO comentarioDTO) {
        Comentario comentario = mapearEntidad(comentarioDTO);
        Publicacion publicacion = publicacionRepository
                .findById(publicacionId).orElseThrow(() -> new ResourceNotFoundException("Publicacion", "Id", publicacionId));
        comentario.setPublicacion(publicacion);
        Comentario nuevoComentario = comentarioRepository.save(comentario);
        return mapearDTO(nuevoComentario);
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorPublicacionId(long publicacionId) {
        List<Comentario> comentarios = comentarioRepository.findByPublicacionId(publicacionId);
        return comentarios.stream().map(c -> mapearDTO(c)).collect(Collectors.toList());
    }

    @Override
    public ComentarioDTO obtenerComentarioPorId(long publicacionId, long comentarioId) {
        Publicacion publicacion = publicacionRepository
                .findById(publicacionId).orElseThrow(() -> new ResourceNotFoundException("Publicacion", "Id", publicacionId));
        Comentario comentario = comentarioRepository
                .findById(comentarioId).orElseThrow(() -> new ResourceNotFoundException("Comentario", "Id", comentarioId));
        if(!comentario.getPublicacion().getId().equals(publicacion.getId())){
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "Comentario No Pertenece a la publicacion");
        }
        return mapearDTO(comentario);
    }

    @Override
    public ComentarioDTO actualizarComentario(Long publicacionId, Long comentarioId, ComentarioDTO comentarioDTO) {
        Publicacion publicacion = publicacionRepository
                .findById(publicacionId).orElseThrow(() -> new ResourceNotFoundException("Publicacion", "Id", publicacionId));
        Comentario comentario = comentarioRepository
                .findById(comentarioId).orElseThrow(() -> new ResourceNotFoundException("Comentario", "Id", comentarioId));
        if(!comentario.getPublicacion().getId().equals(publicacion.getId())){
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "Comentario No Pertenece a la publicacion");
        }
        comentario.setNombre(comentarioDTO.getNombre());
        comentario.setEmail(comentarioDTO.getEmail());
        comentario.setCuerpo(comentarioDTO.getCuerpo());

        Comentario comentarioActualizado = comentarioRepository.save(comentario);
        return mapearDTO(comentarioActualizado);
    }

    @Override
    public void eliminarComentario(Long publicacionId, Long comentarioId) {
        Publicacion publicacion = publicacionRepository
                .findById(publicacionId).orElseThrow(() -> new ResourceNotFoundException("Publicacion", "Id", publicacionId));
        Comentario comentario = comentarioRepository
                .findById(comentarioId).orElseThrow(() -> new ResourceNotFoundException("Comentario", "Id", comentarioId));
        if(!comentario.getPublicacion().getId().equals(publicacion.getId())){
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "Comentario No Pertenece a la publicacion");
        }
        comentarioRepository.delete(comentario);
    }

    private ComentarioDTO mapearDTO(Comentario comentario){
        ComentarioDTO comentarioDTO = modelMapper.map(comentario, ComentarioDTO.class);
        return comentarioDTO;
    }

    private Comentario mapearEntidad(ComentarioDTO comentarioDTO){
        Comentario comentario = modelMapper.map(comentarioDTO, Comentario.class);
        return comentario;
    }

}
