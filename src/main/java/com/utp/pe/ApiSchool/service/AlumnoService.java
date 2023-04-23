package com.utp.pe.ApiSchool.service;

import com.utp.pe.ApiSchool.entity.Alumno;
import com.utp.pe.ApiSchool.entity.Alumno;
import com.utp.pe.ApiSchool.repository.AlumnoRepository;
import com.utp.pe.ApiSchool.repository.AlumnoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AlumnoService {

    @Autowired
    AlumnoRepository alumnoRepository;
    
    @Transactional
    public Alumno save(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }
    
    @Transactional(readOnly=true)
    public List<Alumno> findAll(Pageable page) {
        try {
            return alumnoRepository.findAll(page).toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly=true)
    public List<Alumno> findByNombre(String nombre, Pageable page) {
        try {
            return alumnoRepository.findByNombreContaining(nombre, page);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly=true)
    public Alumno findById(int id) {
        try {
            Alumno registro = alumnoRepository.findById(id).orElseThrow();
            return registro;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    @Transactional
    public Alumno updateAlumno(Alumno alumno){
        try{
            Alumno alum= alumnoRepository.findById(alumno.getId()).orElseThrow();
            if(!Objects.isNull(alumno)){
                BeanUtils.copyProperties(alumno, alum);
            }
            
            alum.setNombre(alumno.getNombre());
            alum.setApellidoPaterno(alumno.getApellidoPaterno());
            alum.setApellidoMaterno(alumno.getApellidoMaterno());
            alum.setDni(alumno.getDni());
            alum.setCelular(alumno.getCelular());
            alum.setEmail(alumno.getEmail());
            alum.setGenero(alumno.getGenero());
            //alum.setFechaNac(alumno.getFechaNac());
            alum.setPadreApoderado(alumno.getPadreApoderado());
            alum.setDireccion(alumno.getDireccion());
            alumnoRepository.save(alum);
            return alum;
        }catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Boolean delete(int id){
        try {
            Alumno alumno=alumnoRepository.findById(id).orElseThrow();
            alumnoRepository.delete(alumno);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }

}
