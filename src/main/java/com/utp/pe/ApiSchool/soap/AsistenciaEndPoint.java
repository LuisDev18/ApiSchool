package com.utp.pe.ApiSchool.soap;

import com.utp.pe.ApiSchool.entity.Asistencia;
import com.utp.pe.ApiSchool.entity.Profesor;
import com.utp.pe.ApiSchool.service.AsistenciaService;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import pe.edu.utp.schoolasistenciaws.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Endpoint
public class AsistenciaEndPoint {
    @Autowired
    AsistenciaService asistenciaService;

    @PayloadRoot(namespace = "http://utp.edu.pe/schoolasistenciaws", localPart = "GetAsistenciaEstudianteRequest")
    @ResponsePayload
	public GetAsistenciaEstudianteResponse findByEstudiante (@RequestPayload GetAsistenciaEstudianteRequest request) {
    	GetAsistenciaEstudianteResponse response = new GetAsistenciaEstudianteResponse();
    	
		List<Asistencia> asistencia;		
		asistencia=asistenciaService.findByEstudiante(request.getIdEstudiante());		
		
		List<AsistenciaDetalle> asistenciaResponse=new ArrayList<>();
		for (int i = 0; i < asistencia.size(); i++) {
		     AsistenciaDetalle ob = new AsistenciaDetalle();		     
		     
		     Calendar calendar = Calendar.getInstance();
		     calendar.setTime(asistencia.get(i).getFechaAsistencia());
		     calendar.add(Calendar.HOUR_OF_DAY, 5);  
		     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		     String strDate = dateFormat.format(calendar.getTime());  
		     		     
		     ob.setFechaAsistencia(strDate);
		     
		     BeanUtils.copyProperties(asistencia.get(i), ob);
		     asistenciaResponse.add(ob);    
		}
		response.getAsistenciaDetalle().addAll(asistenciaResponse);		
		return response;
	}
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolasistenciaws", localPart = "DeleteAsistenciaRequest")
	@ResponsePayload
	public DeleteAsistenciaResponse delete (@RequestPayload DeleteAsistenciaRequest request) {
		ServiceStatus serviceStatus=new ServiceStatus();
		DeleteAsistenciaResponse response = new DeleteAsistenciaResponse();
		boolean resp=asistenciaService.delete(request.getIdAsistencia());
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
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolasistenciaws", localPart = "AddAsistenciaRequest")
    @ResponsePayload
    public AddAsistenciaResponse create (@Valid @RequestPayload AddAsistenciaRequest request) throws ParseException {
        ServiceStatus serviceStatus=new ServiceStatus();

        AddAsistenciaResponse response=new AddAsistenciaResponse();
 
	    String sDate1=request.getFechaAsistencia();  
	    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date1);
	    calendar.add(Calendar.HOUR_OF_DAY, -5);  
		
		
        Asistencia asistencia=new Asistencia();
        asistencia.setIdClase(request.getIdClase());
        asistencia.setIdEstudiante(request.getIdEstudiante());
        asistencia.setFechaAsistencia(calendar.getTime());
        asistencia.setObservacion(request.getObservacion());
        asistencia.setAuxiliar(request.getAuxiliar());        

       asistencia= asistenciaService.save(asistencia);

        if(asistencia!=null){
            AsistenciaDetalle asistenciaDetalle=new AsistenciaDetalle();
            
            
            BeanUtils.copyProperties(asistencia,asistenciaDetalle);
            
            asistenciaDetalle.setFechaAsistencia(sDate1);
            
            
            response.setAsistenciaDetalle(asistenciaDetalle);
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
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolasistenciaws", localPart = "UpdateAsistenciaRequest")
    @ResponsePayload
    public UpdateAsistenciaResponse updateAsistencia(@Valid @RequestPayload UpdateAsistenciaRequest request) throws ParseException{

        ServiceStatus serviceStatus = new ServiceStatus();
        UpdateAsistenciaResponse response= new UpdateAsistenciaResponse();
        
	    String sDate1=request.getFechaAsistencia();  
	    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date1);
	    calendar.add(Calendar.HOUR_OF_DAY, -5);  
	    
        Asistencia entity = asistenciaService.findByIdAsistencia(request.getIdAsistencia());
        entity.setIdClase(request.getIdClase());
        entity.setIdEstudiante(request.getIdEstudiante());
        entity.setFechaAsistencia(calendar.getTime());
        entity.setObservacion(request.getObservacion());
        entity.setAuxiliar(request.getAuxiliar());
        
        entity=asistenciaService.updateAsistencia(entity);

        if(entity!=null){
            AsistenciaDetalle asistenciaDetalle=new AsistenciaDetalle();            
            BeanUtils.copyProperties(entity, asistenciaDetalle);            
            asistenciaDetalle.setFechaAsistencia(sDate1);            
            response.setAsistenciaDetalle(asistenciaDetalle);   
                        
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
   
}
