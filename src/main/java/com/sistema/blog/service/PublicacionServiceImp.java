package com.sistema.blog.service;

import com.sistema.blog.dto.PublicacionDTO;
import com.sistema.blog.dto.PublicacionRespuesta;
import com.sistema.blog.entity.Publicacion;
import com.sistema.blog.excepctions.ResourceNotFoundException;
import com.sistema.blog.repository.PublicacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicacionServiceImp implements PublicacionService{

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PublicacionRespuesta obtenerPublicaciones(int numeroPagina, int tamanoPagina, String orderPor, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderPor).ascending() : Sort.by(orderPor).descending();
        Pageable pageable = PageRequest.of(numeroPagina, tamanoPagina, sort);
        Page<Publicacion> publicaciones = publicacionRepository.findAll(pageable);

        List<Publicacion> listaPublicaciones = publicaciones.getContent();
        List<PublicacionDTO> contenido = listaPublicaciones.stream().map(p -> mapearDTO(p)).collect(Collectors.toList());

        PublicacionRespuesta publicacionRespuesta = new PublicacionRespuesta();
        publicacionRespuesta.setContenido(contenido);
        publicacionRespuesta.setNumeroPagina(publicaciones.getNumber());
        publicacionRespuesta.setTamanoPagina(publicaciones.getSize());
        publicacionRespuesta.setTotalElementos(publicaciones.getTotalElements());
        publicacionRespuesta.setTotalPaginas(publicaciones.getTotalPages());
        publicacionRespuesta.setUltima(publicaciones.isLast());

        return publicacionRespuesta;
    }

    @Override
    public PublicacionDTO obtenerPublicacionPorId(Long id) {
        Publicacion publicacion = publicacionRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Publicacion", "Id", id));
        return mapearDTO(publicacion);
    }

    @Override
    public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO) {
        //Convertimos de DTO a Entidad
        Publicacion publicacion = mapearEntidad(publicacionDTO);
        Publicacion nuevaPublicacion = publicacionRepository.save( publicacion);
        PublicacionDTO publicacionRespuesta = mapearDTO(nuevaPublicacion);
        return publicacionRespuesta;
    }



    @Override
    public PublicacionDTO actualizarPublicacion(PublicacionDTO publicacionDTO, long id) {
        Publicacion publicacion = publicacionRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Publicacion", "Id", id));
        publicacion.setTitulo(publicacionDTO.getTitulo());
        publicacion.setDescripcion(publicacionDTO.getDescripcion());
        publicacion.setContenido(publicacionDTO.getContenido());

        Publicacion publicacionActualizada = publicacionRepository.save(publicacion);
        return mapearDTO(publicacionActualizada);
    }

    @Override
    public void eliminarPublicacion(long id) {
        Publicacion publicacion = publicacionRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Publicacion", "Id", id));
        publicacionRepository.delete(publicacion);
    }

    //Convertir Entidad a DTO
    private PublicacionDTO mapearDTO(Publicacion publicacion){
        PublicacionDTO publicacionDTO = modelMapper.map(publicacion, PublicacionDTO.class);
        return publicacionDTO;
    }

    //Convertir DTO a Entidad
    private Publicacion mapearEntidad(PublicacionDTO publicacionDTO){
        Publicacion publicacion = modelMapper.map(publicacionDTO, Publicacion.class);
        return publicacion;
    }
}
