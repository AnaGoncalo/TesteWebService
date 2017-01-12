/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import DAO.ExperienciaDAO;
import Modelo.Experiencia;
import Modelo.Usuario;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author Ana Gonçalo
 */
@ManagedBean
@RequestScoped
public class ExperienciaBean {
    private Experiencia experiencia = new Experiencia();
    private List<Experiencia> experiencias = new ArrayList();
    private Usuario user = (Usuario) getSession().getAttribute("usuarioLogado");
    
    public ExperienciaBean() {
        Listar();
    }
    
    public void Listar(){
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://localhost:8080/TesteWS/rest/experiencia");
        String json = caminho.request().get(String.class);
        
        Gson gson = new Gson();
        Experiencia[] vetor = gson.fromJson(json, Experiencia[].class);
        experiencias = Arrays.asList(vetor);
    }
    
    public void MinhasExperiencias(){
        try {
            experiencias = ExperienciaDAO.ListarPorUsuario(user.getIdUsuario());
        } catch (SQLException ex) {
            Logger.getLogger(ExperienciaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Salvar(){
        Client cliente = ClientBuilder.newClient();
        
        WebTarget caminho = cliente.target("http://127.0.0.1:8080/TesteWS/rest/experiencia");
        
        Gson gson = new Gson();
        
        String json = gson.toJson(experiencia);
        
        caminho.request().post(Entity.json(json));
        
        Listar();
    }
    
    public FacesContext getFacesContext(){
        return FacesContext.getCurrentInstance();
    }
  
    public HttpSession getSession(){
        return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }

    public Experiencia getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Experiencia experiencia) {
        this.experiencia = experiencia;
    }

    public List<Experiencia> getExperiencias() {
        return experiencias;
    }

    public void setExperiencias(List<Experiencia> experiencias) {
        this.experiencias = experiencias;
    }
    
    
}
