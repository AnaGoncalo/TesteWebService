/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import DAO.AnimalDAO;
import Modelo.Animal;
import Modelo.Encontro;
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
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ana Gonçalo
 */
@ManagedBean
@RequestScoped
public class AnimalBean {
    private Animal animal = new Animal();
    private Usuario dono = null;
    private Usuario user = (Usuario) getSession().getAttribute("usuarioLogado");
  
    private List<Animal> animais = new ArrayList();
    private List<Animal> animaisTop = new ArrayList();
    private Encontro encontro = new Encontro();
    
    public AnimalBean() throws SQLException {
        Listar();
        animaisTop = animais.subList(1, 5);
    }
    
    public void Salvar(){
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://127.0.0.1:8080/TesteWS/rest/animal");
        Gson gson = new Gson();
        
        if(animal.getIdAnimal() != 0)
        {
            animal.setIdUsuario(user.getIdUsuario());
            String json = gson.toJson(animal);
            caminho.request().post(Entity.json(json));
        }
        else
        {
            String json = gson.toJson(animal);
            caminho.request().put(Entity.json(json));
        }
        
        Listar();
    }
    
    public void Listar(){
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://localhost:8080/TesteWS/rest/animal");
        String json = caminho.request().get(String.class);
        
        Gson gson = new Gson();
        Animal[] vetor = gson.fromJson(json, Animal[].class);
        animais = Arrays.asList(vetor);
    }
    
    public void MeusAnimais(){
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://localhost:8080/TesteWS/rest/animal/" + user.getIdUsuario());
        String json = caminho.request().get(String.class);
        Gson gson = new Gson();
        Animal[] vetor = gson.fromJson(json, Animal[].class);
        animais = Arrays.asList(vetor);
    }
    
    public String SalvarEncontro(){
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://127.0.0.1:8080/TesteWS/rest/encontro");
        Gson gson = new Gson();
       
        
        if(encontro.getIdEncontro() == 0)
        {
            encontro.setIdUsuario(user.getIdUsuario());
            encontro.setIdAnimal(animal.getIdAnimal());
            String json = gson.toJson(encontro);
            caminho.request().post(Entity.json(json));
        }
        else
        {
            String json = gson.toJson(encontro);
            caminho.request().put(Entity.json(json));
        }
        return "index.jsf";
    }
    
    
    
    public String VerAnimal(Animal a){
        animal = a;
        
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://localhost:8080/TesteWS/rest/usuario/" + animal.getIdUsuario());
        String json = caminho.request().get(String.class);
        Gson gson = new Gson();
        dono = gson.fromJson(json, Usuario.class);
        
        return "animal.jsf";
    }
    
    public String Adotar(Animal a){
        animal = a;
        //encontro = new Encontro();
        encontro.setIdUsuario(user.getIdUsuario()); //pega o usuario da sessao
        encontro.setIdAnimal(animal.getIdAnimal());
        
        return "cadastrarEncontro.jsf";
    }
    
    public FacesContext getFacesContext(){
        return FacesContext.getCurrentInstance();
    }
  
    public HttpSession getSession(){
        return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }
    
    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public List<Animal> getAnimais() {
        return animais;
    }

    public void setAnimais(List<Animal> animais) {
        this.animais = animais;
    }

    public Encontro getEncontro() {
        return encontro;
    }

    public void setEncontro(Encontro encontro) {
        this.encontro = encontro;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public List<Animal> getAnimaisTop() {
        return animaisTop;
    }

    public void setAnimaisTop(List<Animal> animaisTop) {
        this.animaisTop = animaisTop;
    }
    
    
}
