package com.utp.pe.ApiSchool.soap;

import com.utp.pe.ApiSchool.entity.Profesor;
import com.utp.pe.ApiSchool.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import pe.edu.utp.schoolws.*;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class ProfesorEndpoint {

    @Autowired
    ProfesorService profesorService;

    @PayloadRoot(namespace = "http://utp.edu.pe/schoolws", localPart = "AddProfesorRequest")
    @ResponsePayload
    public AddProfesorResponse create (@Valid @RequestPayload AddProfesorRequest request) {
        ServiceStatus serviceStatus=new ServiceStatus();

        AddProfesorResponse response=new AddProfesorResponse();

        Profesor profesor=new Profesor();
        profesor.setNombre(request.getNombre());
        profesor.setApellidoMaterno(request.getApellidoMaterno());
        profesor.setApellidoPaterno(request.getApellidoPaterno());
        profesor.setCelular(request.getCelular());
        profesor.setEmail(request.getEmail());
        profesor.setDni(request.getDni());
        profesor.setCurso(request.getCurso());


       profesor= profesorService.save(profesor);

        if(profesor!=null){
            ProfesorDetalle profesorDetalle=new ProfesorDetalle();
            BeanUtils.copyProperties(profesor,profesorDetalle);
            response.setProfesorDetalle(profesorDetalle);
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

    @PayloadRoot(namespace = "http://utp.edu.pe/schoolws", localPart = "GetAllProfesorRequest")
    @ResponsePayload
    public GetAllProfesorResponse findAll (@RequestPayload GetAllProfesorRequest request) {
        GetAllProfesorResponse response = new GetAllProfesorResponse();

        Pageable page = PageRequest.of(request.getOffset(), request.getLimit());

        List<Profesor> profesores;
        if(request.getTexto()==null) {
            profesores = profesorService.findAll(page);
        }else {
            profesores=profesorService.findByNombreCurso(request.getTexto(), page);
        }

        List<ProfesorDetalle> profesorResponse=new ArrayList<>();
        for (int i = 0; i < profesores.size(); i++) {
            ProfesorDetalle ob = new ProfesorDetalle();
            BeanUtils.copyProperties(profesores.get(i), ob);
            profesorResponse.add(ob);
        }
        response.getProfesorDetalle().addAll(profesorResponse);
        return response;
    }

    @PayloadRoot(namespace = "http://utp.edu.pe/schoolws", localPart = "GetProfesorRequest")
    @ResponsePayload
    public GetProfesorResponse findById (@Valid @RequestPayload GetProfesorRequest request) {
        GetProfesorResponse response = new GetProfesorResponse();
        Profesor entity=profesorService.findById(request.getId());
        ProfesorDetalle prosor=new ProfesorDetalle();
        prosor.setId(entity.getId());
        prosor.setNombre(entity.getNombre());
        prosor.setApellidoPaterno(entity.getApellidoPaterno());
        prosor.setApellidoMaterno(entity.getApellidoMaterno());
        prosor.setDni(entity.getDni());
        prosor.setCelular(entity.getCelular());
        prosor.setCurso(entity.getCurso());
        response.setProfesorDetalle(prosor);
        return response;
    }


    @PayloadRoot(namespace = "http://utp.edu.pe/schoolws", localPart = "UpdateProfesorRequest")
    @ResponsePayload
    public UpdateProfesorResponse updateProfesor(@Valid @RequestPayload UpdateProfesorRequest request){

        ServiceStatus serviceStatus = new ServiceStatus();
        UpdateProfesorResponse response= new UpdateProfesorResponse();

        Profesor entity = profesorService.findById(request.getProfesorDetalle().getId());
        entity.setCelular(request.getProfesorDetalle().getCelular());
        entity.setEmail(request.getProfesorDetalle().getEmail());
        entity.setCurso(request.getProfesorDetalle().getCurso());
        entity=profesorService.updateProfesor(entity);

        if(entity!=null){
            ProfesorDetalle profesorDetalle=new ProfesorDetalle();
            BeanUtils.copyProperties(entity, profesorDetalle);
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

    @PayloadRoot(namespace = "http://utp.edu.pe/schoolws", localPart = "DeleteProfesorRequest")
	@ResponsePayload
	public DeleteProfesorResponse create (@RequestPayload DeleteProfesorRequest request) {
		ServiceStatus serviceStatus=new ServiceStatus();
		DeleteProfesorResponse response = new DeleteProfesorResponse();
		boolean resp=profesorService.delete(request.getId());
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
