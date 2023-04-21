package com.utp.pe.ApiSchool.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message="El elemento nombre no puede ser vacio")
    private String nombre;

    @NotBlank(message="El elemento apellido paterno no puede ser vacio")
    private String apellidoPaterno;

    @NotBlank(message="El elemento apellido materno no puede ser vacio")
    private String apellidoMaterno;

    @Pattern(regexp="(^$|[0-9]{8})",message = "El DNI debe ser de 8 digits")
    @NotBlank(message="El elemento dni no puede ser vacio")
    private String dni;

    @Pattern(regexp="(^$|[0-9]{9})",message = "El celular debe ser de 9 digits")
    private String celular;

    @Email(message = "Ingresar un correo electronico valido" )
    private String email;

    @NotBlank(message="El elemento curso no puede ser vacio")
    private String curso;

}
