package com.utp.pe.ApiSchool.soap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.utp.pe.ApiSchool.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.utp.pe.ApiSchool.entity.Rol;
import com.utp.pe.ApiSchool.entity.Usuario;
import com.utp.pe.ApiSchool.service.UsuarioService;

import jakarta.validation.Valid;
import pe.edu.utp.schoolusuariows.*;

@Endpoint
public class UsuarioEndPoint {
	@Autowired
    UsuarioService usuarioService;

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolusuariows", localPart = "GetAllUsuarioRequest")
    @ResponsePayload
	public GetAllUsuarioResponse findByEmailUsuario (@RequestPayload GetAllUsuarioRequest request) {
    	GetAllUsuarioResponse response = new GetAllUsuarioResponse();
    	
		List<Usuario> usuarios;		
		usuarios=usuarioService.findUsuariosByEmail(request.getTexto());		
		
		List<UsuarioDetalle> usuarioResponse=new ArrayList<>();
		for (int i = 0; i < usuarios.size(); i++) {
		     UsuarioDetalle ob = new UsuarioDetalle();
		     		     
		     ob.setIdUsuario(usuarios.get(i).getIdUsuario());
		     ob.setEmail(usuarios.get(i).getEmail());
		     ob.setRol(usuarios.get(i).getRol().toString());
		     
		     usuarioResponse.add(ob);    
		}
		response.getUsuarioDetalle().addAll(usuarioResponse);		
		return response;
	}
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolusuariows", localPart = "LoginUsuarioRequest")
    @ResponsePayload
	public LoginUsuarioResponse loginUsuario (@RequestPayload LoginUsuarioRequest request) {
    	
    	LoginUsuarioResponse response = new LoginUsuarioResponse();
    	
    	ServiceStatus serviceStatus=new ServiceStatus();
    	
    	try {
    	
    		UsuarioDetalleToken usuarioT= new UsuarioDetalleToken();
        	UsuarioDetalle usuarioD=new UsuarioDetalle();
        	
        	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));   	
        	var usuario=usuarioService.findUsuarioByEmail(request.getEmail());    	
        	var jwtToken=jwtService.generateToken(usuario);
    		
        	usuarioD.setIdUsuario(usuario.getIdUsuario());
        	usuarioD.setEmail(usuario.getEmail());
        	usuarioD.setRol(usuario.getRol().toString());
        	
        	usuarioT.setUsuarioDetalle(usuarioD);
        	
    		response.setUsuarioDetalleToken(usuarioT);		
    		response.getUsuarioDetalleToken().setToken(jwtToken);
    		
            serviceStatus.setStatusCode("201 - SUCCESS");
            serviceStatus.setMessage("User logged succesfully");
            response.setServiceStatus(serviceStatus);
            
    		return response;
    		
		} catch (Exception e) {
			
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Something went wrong. "+ e.getMessage());
            response.setServiceStatus(serviceStatus);
			return response;
		}	
	}
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolusuariows", localPart = "DeleteUsuarioRequest")
	@ResponsePayload
	public DeleteUsuarioResponse delete (@RequestPayload DeleteUsuarioRequest request) {
		ServiceStatus serviceStatus=new ServiceStatus();
		DeleteUsuarioResponse response = new DeleteUsuarioResponse();
		boolean resp=usuarioService.deleteUsuario(request.getIdUsuario());
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
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolusuariows", localPart = "AddUsuarioRequest")
    @ResponsePayload
    public AddUsuarioResponse createUsuario (@Valid @RequestPayload AddUsuarioRequest request) throws ParseException {
        ServiceStatus serviceStatus=new ServiceStatus();

        AddUsuarioResponse response=new AddUsuarioResponse();
        String passEncode=encoder.encode(request.getPassword());
        
        Usuario usuario=new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passEncode);
        usuario.setActivo(true);
        usuario.setRol(Rol.valueOf(request.getRol()));        

       usuario= usuarioService.saveUsuario(usuario);

        if(usuario!=null){
            UsuarioDetalle usuarioDetalle=new UsuarioDetalle();            
            
            usuarioDetalle.setIdUsuario(usuario.getIdUsuario());
            usuarioDetalle.setEmail(usuario.getEmail());
            usuarioDetalle.setRol(usuario.getRol().toString());            
            
            response.setUsuarioDetalle(usuarioDetalle);
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
    
    @PayloadRoot(namespace = "http://utp.edu.pe/schoolusuariows", localPart = "UpdateUsuarioRequest")    						  
    @ResponsePayload
    public UpdateUsuarioResponse updateUsuario(@Valid @RequestPayload UpdateUsuarioRequest request) throws ParseException{

        ServiceStatus serviceStatus = new ServiceStatus();
        UpdateUsuarioResponse response= new UpdateUsuarioResponse();  
	    
        Usuario entity = usuarioService.GetUsuario(request.getIdUsuario());
        String passEncode=encoder.encode(request.getPassword());
        
        entity.setEmail(request.getEmail());        
        entity.setPassword(passEncode);
        entity.setRol(Rol.valueOf(request.getRol()));        
        
        entity=usuarioService.updateUsuario(entity);

        if(entity!=null){
            UsuarioDetalle usuarioDetalle=new UsuarioDetalle();            
            
            usuarioDetalle.setIdUsuario(entity.getIdUsuario());
            usuarioDetalle.setEmail(entity.getEmail());
            usuarioDetalle.setRol(entity.getRol().toString());                        
            response.setUsuarioDetalle(usuarioDetalle);   
                        
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
