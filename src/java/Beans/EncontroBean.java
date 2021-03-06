/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import DAO.EncontroDAO;
import Modelo.Encontro;
import Modelo.Animal;
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
import javax.faces.bean.SessionScoped;
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
@SessionScoped
public class EncontroBean {

    private Encontro encontro;
    private Animal animal;
    private List<Encontro> encontros = new ArrayList();
    private Usuario user = (Usuario) getSession().getAttribute("usuarioLogado");

    public EncontroBean() {
        if (user != null) {
            MeusEncontros();
        }
    }

    public String Adotar(Animal a) {
        animal = a;
        encontro.setAdotante(user); //pega o usuario da sessao

        return "cadastrarEncontro.jsf";
    }

    public String Salvar() {
        System.out.println("Bean: Encontro Salvar " + encontro.getIdEncontro());
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://127.0.0.1:8080/HelpPet/rest/encontro");
        Gson gson = new Gson();

        if (encontro.getIdEncontro() == 0) {
            encontro.setAdotante(user);
            String json = gson.toJson(encontro);
            caminho.request().post(Entity.json(json));
        } else {
            if (user.getIdUsuario() == encontro.getAdotante().getIdUsuario()) {
                encontro.setEditado(false);
            } else {
                encontro.setEditado(true);
            }

            String json = gson.toJson(encontro);
            caminho.request().put(Entity.json(json));
        }

        return "meusEncontros.jsf";
    }

    public String getConfirmar(Encontro e) {
        System.out.println("Bean encontro confirmar");
        encontro = e;
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://127.0.0.1:8080/HelpPet/rest/encontro");
        Gson gson = new Gson();

        System.out.println("testando Confirmar.. " + encontro.getIdEncontro());
        System.out.println(e.getStatusEncontro());
        encontro.setStatusEncontro(true);
        String json = gson.toJson(encontro);
        caminho.request().put(Entity.json(json));

        return "meusEncontros.jsf";
    }

    public void MeusEncontros() {
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://localhost:8080/HelpPet/rest/encontro/" + user.getIdUsuario());
        String json = caminho.request().get(String.class);

        Gson gson = new Gson();
        Encontro[] vetor = gson.fromJson(json, Encontro[].class);
        encontros = Arrays.asList(vetor);
    }

    public void Listar() {
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://localhost:8080/HelpPet/rest/encontro");
        String json = caminho.request().get(String.class);

        Gson gson = new Gson();
        Encontro[] vetor = gson.fromJson(json, Encontro[].class);
        encontros = Arrays.asList(vetor);
    }

    public String VerEncontro(Encontro e) {
        encontro = e;
        return "encontro.jsf";
    }

    public String ExcluirEncontro(Encontro e) {
        System.out.println("Bean Anuncio Excluir " + e.getIdEncontro());
        
        Client cliente = ClientBuilder.newClient();
        WebTarget caminho = cliente.target("http://127.0.0.1:8080/HelpPet/rest/encontro/" + e.getIdEncontro());
        caminho.request().delete();
        
        return "meusEncontros.jsf";
    }

    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public HttpSession getSession() {
        return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }

    public Encontro getEncontro() {
        return encontro;
    }

    public void setEncontro(Encontro encontro) {
        this.encontro = encontro;
    }

    public List<Encontro> getEncontros() {
        MeusEncontros();
        return encontros;
    }

    public void setEncontros(List<Encontro> encontros) {
        this.encontros = encontros;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

}
