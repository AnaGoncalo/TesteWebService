/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Modelo.Evento;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ana Gonçalo
 */
public class EventoDAO {
    
    public static String CadastrarEvento(Evento evento) throws SQLException
    {
        System.out.println("evento dao");
        Connection conn = Banco.getConexao();
        PreparedStatement pstmt = null;
        String comandoSql = "INSERT INTO Evento(nomeEvento, dataEvento, horarioEvento, descricaoEvento, fotoEvento, idUsuario,"
                + " idLocalizacao) values(?, ?, ?, ?, ?, ?, ?)";
        try
        {
            pstmt = conn.prepareStatement(comandoSql);
            pstmt.setString(1, evento.getNomeEvento());
            pstmt.setDate(2, (Date) evento.getDataEvento());
            pstmt.setString(3, evento.getHorarioEvento());
            pstmt.setString(4, evento.getDescricaoEvento());
            pstmt.setString(5, evento.getFotoEvento());
            pstmt.setInt(6, evento.getIdUsuario());
            pstmt.setInt(7, evento.getIdLocalizacao());
           
            pstmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            Banco.closeConexao(conn, null, pstmt, null);
        }
        return "OK!";
    }
    
    public static List<Evento> ListarEventos() throws SQLException
    {
        Connection conn = Banco.getConexao();
        ResultSet rs = null;
        Statement stmt= null;
        List<Evento> lista = new ArrayList();
        String sql= "SELECT * FROM Evento";
        try
        {
            stmt = conn.createStatement();
            rs= stmt.executeQuery(sql);
            while(rs.next())
            { 
                Evento a = new Evento(rs.getInt("idEvento"), rs.getString("nomeEvento"), rs.getDate("dataEvento"), 
                        rs.getString("horarioEvento"), rs.getString("descricaoEvento"), rs.getString("fotoEvento"),
                        rs.getInt("idUsuario"), rs.getInt("idLocalizacao"));
                lista.add(a);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EventoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            Banco.closeConexao(conn, rs, null, stmt);
        } 
        return lista;
    }
}