package com.utp.pe.ApiSchool.repository;

import java.util.List;

import com.utp.pe.ApiSchool.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer>{
	//List<Asistencia> findByIdEstudianteContaining(int idEstudiante);
	List<Asistencia> findByIdEstudiante(int idEstudiante);
	
	
	
}
