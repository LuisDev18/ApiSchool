package com.utp.pe.ApiSchool.repository;

import com.utp.pe.ApiSchool.entity.Profesor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor,Integer> {

    //Usando Derived Query method
    List<Profesor> findByCursoContaining(String nombre, Pageable page);

}
