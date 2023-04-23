package com.utp.pe.ApiSchool.repository;

import com.utp.pe.ApiSchool.entity.Alumno;
import com.utp.pe.ApiSchool.entity.Profesor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    List<Alumno> findByNombreContaining(String nombre, Pageable page);

}
