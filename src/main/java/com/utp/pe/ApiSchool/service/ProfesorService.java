package com.utp.pe.ApiSchool.service;

import com.utp.pe.ApiSchool.entity.Profesor;
import com.utp.pe.ApiSchool.repository.ProfesorRepository;
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
public class ProfesorService {

    @Autowired
    ProfesorRepository profesorRepository;


    @Transactional
    public Profesor save(Profesor profesor) {
        return profesorRepository.save(profesor);
    }


    @Transactional(readOnly=true)
    public List<Profesor> findAll(Pageable page) {
        try {
            return profesorRepository.findAll(page).toList();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly=true)
    public List<Profesor> findByNombreCurso(String nombreCurso, Pageable page) {
        try {
            return profesorRepository.findByCursoContaining(nombreCurso,page);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly=true)
    public Profesor findById(int id) {
        try {
            Profesor registro = profesorRepository.findById(id).orElseThrow();
            return registro;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    @Transactional
    public Profesor updateProfesor(Profesor profesor){
       try{
           Profesor profe= profesorRepository.findById(profesor.getId()).orElseThrow();
           if(!Objects.isNull(profe)){
               BeanUtils.copyProperties(profesor, profe);
           }
           profe.setEmail(profesor.getEmail());
           profe.setCelular(profesor.getCelular());
           profe.setCurso(profesor.getCurso());
           profesorRepository.save(profe);
           return profe;
       }catch(Exception e) {
           log.error(e.getMessage());
           return null;
       }
    }

    public Boolean delete(int id){
        try {
            Profesor profesor=profesorRepository.findById(id).orElseThrow();
            profesorRepository.delete(profesor);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }
}
