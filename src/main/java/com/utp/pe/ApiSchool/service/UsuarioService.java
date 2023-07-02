package com.utp.pe.ApiSchool.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utp.pe.ApiSchool.entity.Usuario;
import com.utp.pe.ApiSchool.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Transactional(readOnly=true)
	public List<Usuario> findUsuariosByEmail(String email) {
		try {
			return usuarioRepository.findByEmailContaining(email);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

    @Transactional(readOnly=true)
	public Usuario findUsuarioByEmail(String email) {
		try {
			Usuario registro =usuarioRepository.findByEmail(email).orElseThrow(); 
			return registro;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
    
    @Transactional(readOnly=true)
    public Usuario GetUsuario(int IdUsuario) {
        try {
            Usuario registro = usuarioRepository.findById(IdUsuario).orElseThrow();
            return registro;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    
	@Transactional
	public boolean deleteUsuario(int idUsuario) {
		try {
			Usuario usuario=usuarioRepository.findById(idUsuario).orElseThrow();
			usuarioRepository.delete(usuario);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	
	@Transactional
	public Usuario saveUsuario(Usuario usuario) {
		try {
			Usuario nuevo=usuarioRepository.save(usuario);
			return nuevo;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
    @Transactional
    public Usuario updateUsuario(Usuario usuario){
       try{
           Usuario usuarioU= usuarioRepository.findById(usuario.getIdUsuario()).orElseThrow();
           if(!Objects.isNull(usuarioU)){
               BeanUtils.copyProperties(usuario, usuarioU);
           }
           usuarioU.setEmail(usuario.getEmail());
           usuarioU.setPassword(usuario.getPassword());
           usuarioU.setActivo(usuario.isActivo());
           usuarioU.setRol(usuario.getRol());
           usuarioRepository.save(usuarioU);
           return usuarioU;
       }catch(Exception e) {
           log.error(e.getMessage());
           return null;
       }
    }	    
}
