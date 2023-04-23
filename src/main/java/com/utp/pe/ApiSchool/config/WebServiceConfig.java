package com.utp.pe.ApiSchool.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }
    
    //@Primary
    @Bean
    public XsdSchema articulosSchema() {
        return new SimpleXsdSchema(new ClassPathResource("profesor-detalle.xsd"));
    }
    //ws/profesor.wsdl
    @Bean(name = "profesor")
    public DefaultWsdl11Definition defaultWsdl11Definition2(XsdSchema articulosSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ProfesorPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://utp.edu.pe/schoolws");
        wsdl11Definition.setSchema(articulosSchema);
        return wsdl11Definition;
    }
    
    //@Primary
    @Bean
    public XsdSchema asistenciaSchema() {
        return new SimpleXsdSchema(new ClassPathResource("asistencia-detalle.xsd"));
    }
    //ws/asistencia.wsdl
    
    @Bean(name = "asistencia")
    public DefaultWsdl11Definition defaultWsdl11Definition3(XsdSchema asistenciaSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("AsistenciaPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://utp.edu.pe/schoolasistenciaws");
        wsdl11Definition.setSchema(asistenciaSchema);
        return wsdl11Definition;
    }
}
