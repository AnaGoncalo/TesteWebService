/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Modelo.Encontro;
import java.sql.Connection;
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
public class EncontroDAO {
    
    public static String CadastrarEncontro(Encontro encontro) throws SQLException
    {
        System.out.println("encontro dao");
        Connection conn = Banco.getConexao();
        PreparedStatement pstmt = null;
        String comandoSql = "INSERT INTO Encontro(dataEncontro, horarioEncontro, statusEncontro, editado, idAnimal, idUsuario, "
                + "localizacao) values(?, ?, ?, ?, ?, ?, ?)";
        try
        {
            pstmt = conn.prepareStatement(comandoSql);
            pstmt.setString(1, encontro.getDataEncontro());
            pstmt.setString(2, encontro.getHorarioEncontro());
            pstmt.setBoolean(3, encontro.isStatusEncontro());
            pstmt.setBoolean(4, encontro.isEditado());
            pstmt.setInt(5, encontro.getIdAnimal());
            pstmt.setInt(6, encontro.getIdUsuario());
            pstmt.setString(7, encontro.getLocalizacao());
           
            pstmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(EncontroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            Banco.closeConexao(conn, null, pstmt, null);
        }
        return "OK!";
    }
    
    public static String EditarEncontro(Encontro encontro) throws SQLException
    {
        System.out.println("encontro dao editar");
        Connection conn = Banco.getConexao();
        PreparedStatement pstmt = null;
        String comandoSql = "UPDATE Encontro SET dataEncontro = ?, horarioEncontro = ?, statusEncontro = ?, editado = ?, "
                + "idAnimal = ?, idUsuario = ?, localizacao = ? WHERE idEncontro = ?";
        try
        {
            pstmt = conn.prepareStatement(comandoSql);
            pstmt.setString(1, encontro.getDataEncontro());
            pstmt.setString(2, encontro.getHorarioEncontro());
            pstmt.setBoolean(3, encontro.isStatusEncontro());
            pstmt.setBoolean(4, encontro.isEditado());
            pstmt.setInt(5, encontro.getIdAnimal());
            pstmt.setInt(6, encontro.getIdUsuario());
            pstmt.setString(7, encontro.getLocalizacao());
            pstmt.setInt(8, encontro.getIdEncontro());
           
            pstmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(EncontroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            Banco.closeConexao(conn, null, pstmt, null);
        }
        return "OK!";
    }
    
    public static String ExcluirEncontro(Encontro encontro) throws SQLException
    {
        System.out.println("encontro dao excluir");
        Connection conn = Banco.getConexao();
        PreparedStatement pstmt = null;
        String comandoSql = "DELETE FROM Encontro WHERE idEncontro = ?";
        try
        {
            pstmt = conn.prepareStatement(comandoSql);
            pstmt.setInt(1, encontro.getIdEncontro());
           
            pstmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(EncontroDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally
        {
            Banco.closeConexao(conn, null, pstmt, null);
        }
        return "OK!";
    }
    
    public static List<Encontro> ListarEncontros() throws SQLException
    {
        Connection conn = Banco.getConexao();
        ResultSet rs = null;
        Statement stmt= null;
        List<Encontro> lista = new ArrayList();
        String sql= "SELECT * FROM Encontro";
        try
        {
            stmt = conn.createStatement();
            rs= stmt.executeQuery(sql);
            while(rs.next())
            { 
                Encontro a = new Encontro(rs.getInt("idEncontro"), rs.getString("dataEncontro"), rs.getString("horarioEncontro"),
                        rs.getBoolean("statusEncontro"), rs.getBoolean("editado"), rs.getInt("idAnimal"), rs.getInt("idUsuario"),
                        rs.getString("localizacao"));
                lista.add(a);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EncontroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            Banco.closeConexao(conn, rs, null, stmt);
        } 
        return lista;
    }
    
    public static List<Encontro> ListarPorUsuario(int idUsuario) throws SQLException
    {
        Connection conn = Banco.getConexao();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Encontro> lista = new ArrayList();
        String sql= "SELECT * FROM Encontro INNER JOIN Animal ON Encontro.idAnimal = Animal.idAnimal "
                + "WHERE Encontro.idUsuario = ? OR Animal.idUsuario = ?";
        try
        {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idUsuario);
            
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                Encontro a = new Encontro(rs.getInt("idEncontro"), rs.getString("dataEncontro"), rs.getString("horarioEncontro"),
                        rs.getBoolean("statusEncontro"), rs.getBoolean("editado"), rs.getInt("idAnimal"), rs.getInt("idUsuario"),
                        rs.getString("localizacao"));
                lista.add(a);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(EncontroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            Banco.closeConexao(conn, rs, pstmt, null);
        } 
        return lista;
    }
    
}
