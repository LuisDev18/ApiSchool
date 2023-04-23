package com.utp.pe.ApiSchool.service;

import com.utp.pe.ApiSchool.entity.Asistencia;
import com.utp.pe.ApiSchool.entity.Profesor;
import com.utp.pe.ApiSchool.repository.AsistenciaRepository;


import lombok.extern.slf4j.Slf4j;

import com.utp.pe.ApiSchool.entity.Asistencia;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AsistenciaService {

    @Autowired
    AsistenciaRepository asistenciaRepository;
    
	@Transactional(readOnly=true)
	public List<Asistencia> findByEstudiante(int idEstudiante) {
		try {
			//return asistenciaRepository.findByIdEstudiante(idAlumno);
			return asistenciaRepository.findByIdEstudiante(idEstudiante);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

    @Transactional(readOnly=true)
    public Asistencia findByIdAsistencia(int idAsistencia) {
        try {
            Asistencia registro = asistenciaRepository.findById(idAsistencia).orElseThrow();
            return registro;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    
	@Transactional
	public boolean delete(int idAsistencia) {
		try {
			Asistencia asistencia=asistenciaRepository.findById(idAsistencia).orElseThrow();
			asistenciaRepository.delete(asistencia);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	
	@Transactional
	public Asistencia save(Asistencia asistencia) {
		try {
			Asistencia nuevo=asistenciaRepository.save(asistencia);
			return nuevo;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
    @Transactional
    public Asistencia updateAsistencia(Asistencia asistencia){
       try{
           Asistencia asistenciaU= asistenciaRepository.findById(asistencia.getIdAsistencia()).orElseThrow();
           if(!Objects.isNull(asistenciaU)){
               BeanUtils.copyProperties(asistencia, asistenciaU);
           }
           asistenciaU.setIdClase(asistencia.getIdClase());
           asistenciaU.setIdEstudiante(asistencia.getIdEstudiante());
           asistenciaU.setFechaAsistencia(asistencia.getFechaAsistencia());
           asistenciaU.setObservacion(asistencia.getObservacion());
           asistenciaU.setAuxiliar(asistencia.getAuxiliar());
           asistenciaRepository.save(asistenciaU);
           return asistenciaU;
       }catch(Exception e) {
           log.error(e.getMessage());
           return null;
       }
    }	
}
