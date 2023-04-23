package com.utp.pe.ApiSchool.soap;

import com.utp.pe.ApiSchool.entity.Alumno;
import com.utp.pe.ApiSchool.service.AlumnoService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pe.edu.utp.alumnows.AddAlumnoRequest;
import pe.edu.utp.alumnows.*;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class AlumnoEndPoint {
    @Autowired
    AlumnoService alumnoService;

    @PayloadRoot(namespace = "http://utp.edu.pe/alumnows", localPart = "AddAlumnoRequest")
    @ResponsePayload
    public AddAlumnoResponse create (@Valid @RequestPayload AddAlumnoRequest request) {
        ServiceStatus serviceStatus=new ServiceStatus();
        AddAlumnoResponse response=new AddAlumnoResponse();

        Alumno alumno=new Alumno();

        alumno.setNombre(request.getNombre());
        alumno.setApellidoPaterno(request.getApellidoPaterno());
        alumno.setApellidoMaterno(request.getApellidoMaterno());
        alumno.setDni(request.getDni());
        alumno.setCelular(request.getCelular());
        alumno.setEmail(request.getEmail());
        alumno.setGenero(request.getGenero());
        //alumno.setFechaNac(request.getFechaNac());
        alumno.setPadreApoderado(request.getPadreApoderado());
        alumno.setDireccion(request.getDireccion());
        alumno= alumnoService.save(alumno);

        if(alumno!=null){
            AlumnoDetalle alumnoDetalle=new AlumnoDetalle();
            BeanUtils.copyProperties(alumno,alumnoDetalle);
            response.setAlumnoDetalle(alumnoDetalle);
            serviceStatus.setStatusCode("201 - SUCCESS");
            serviceStatus.setMessage("Content Added Succesfully");
            response.setServiceStatus(serviceStatus);
        }else{
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Content Already Available");
            response.setServiceStatus(serviceStatus);
        }
        return response;
    }

    @PayloadRoot(namespace = "http://utp.edu.pe/alumnows", localPart = "GetAllAlumnoRequest")
    @ResponsePayload
    public GetAllAlumnoResponse findAll (@RequestPayload GetAllAlumnoRequest request) {
        GetAllAlumnoResponse response = new GetAllAlumnoResponse();

        Pageable page = PageRequest.of(request.getOffset(), request.getLimit());

        List<Alumno> alumnos;
        if(request.getTexto()==null) {
            alumnos = alumnoService.findAll(page);
        }else {
            alumnos=alumnoService.findByNombre(request.getTexto(), page);
        }

        List<AlumnoDetalle> alumnoResponse=new ArrayList<>();
        for (int i = 0; i < alumnos.size(); i++) {
            AlumnoDetalle ob = new AlumnoDetalle();
            BeanUtils.copyProperties(alumnos.get(i), ob);
            alumnoResponse.add(ob);
        }
        response.getAlumnoDetalle().addAll(alumnoResponse);
        return response;
    }

    @PayloadRoot(namespace = "http://utp.edu.pe/alumnows", localPart = "GetAlumnoRequest")
    @ResponsePayload
    public GetAlumnoResponse findById (@Valid @RequestPayload GetAlumnoRequest request) {
        GetAlumnoResponse response = new GetAlumnoResponse();
        Alumno entity=alumnoService.findById(request.getId());
        AlumnoDetalle alumn=new AlumnoDetalle();
        alumn.setId(entity.getId());
        alumn.setNombre(entity.getNombre());
        alumn.setApellidoPaterno(entity.getApellidoPaterno());
        alumn.setApellidoMaterno(entity.getApellidoMaterno());
        alumn.setDni(entity.getDni());
        alumn.setCelular(entity.getCelular());
        alumn.setEmail(entity.getEmail());
        alumn.setGenero(entity.getGenero());
        //alumn.setFechaNac(entity.getFechaNac());
        alumn.setPadreApoderado(entity.getPadreApoderado());
        alumn.setDireccion(entity.getDireccion());
        response.setAlumnoDetalle(alumn);
        return response;
    }


    @PayloadRoot(namespace = "http://utp.edu.pe/alumnows", localPart = "UpdateAlumnoRequest")
    @ResponsePayload
    public UpdateAlumnoResponse updateAlumno(@Valid @RequestPayload UpdateAlumnoRequest request){

        ServiceStatus serviceStatus = new ServiceStatus();
        UpdateAlumnoResponse response= new UpdateAlumnoResponse();

        Alumno entity = alumnoService.findById(request.getAlumnoDetalle().getId());
        entity.setNombre(request.getAlumnoDetalle().getNombre());
        entity.setApellidoPaterno(request.getAlumnoDetalle().getApellidoPaterno());
        entity.setApellidoMaterno(request.getAlumnoDetalle().getApellidoMaterno());
        entity.setDni(request.getAlumnoDetalle().getDni());
        entity.setCelular(request.getAlumnoDetalle().getCelular());
        entity.setEmail(request.getAlumnoDetalle().getEmail());
        entity.setGenero(request.getAlumnoDetalle().getGenero());
        //entity.setFechaNac(request.getAlumnoDetalle().getFechaNac());
        entity.setPadreApoderado(request.getAlumnoDetalle().getPadreApoderado());
        entity.setDireccion(request.getAlumnoDetalle().getDireccion());
        entity=alumnoService.updateAlumno(entity);

        if(entity!=null){
            AlumnoDetalle alumnoDetalle=new AlumnoDetalle();
            BeanUtils.copyProperties(entity, alumnoDetalle);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Updated Successfully");
            response.setServiceStatus(serviceStatus);
        }else{
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Content Not Updated");
            response.setServiceStatus(serviceStatus);
        }
        return response;
    }

    @PayloadRoot(namespace = "http://utp.edu.pe/alumnows", localPart = "DeleteAlumnoRequest")
    @ResponsePayload
    public DeleteAlumnoResponse create (@RequestPayload DeleteAlumnoRequest request) {
        ServiceStatus serviceStatus=new ServiceStatus();
        DeleteAlumnoResponse response = new DeleteAlumnoResponse();
        boolean resp=alumnoService.delete(request.getId());
        if(resp) {
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Deleted Successfully");
            response.setServiceStatus(serviceStatus);
        }else {
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Content Not Deleted");
            response.setServiceStatus(serviceStatus);
        }
        return response;
    }

}

