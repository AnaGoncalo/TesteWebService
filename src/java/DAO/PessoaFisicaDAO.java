/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Modelo.PessoaFisica;
import Modelo.PessoaJuridica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ana Gonçalo
 */
public class PessoaFisicaDAO {
    
    public static List<PessoaFisica> listarHelpers() throws SQLException
    {
        Connection conn = Banco.getConexao();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PessoaFisica> lista = new ArrayList();
        String comandoSql= "SELECT * FROM PessoaFisica pf inner join usuario on usuario.idUsuario = pf.idHelper where Usuario.idPermissao = 1";
        try
        {
            pstmt = conn.prepareStatement(comandoSql);
            
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                PessoaFisica pf = new PessoaFisica(rs.getInt("idHelper"), rs.getString("cpf"), rs.getInt("idUsuario"), rs.getString("nomeUsuario"), 
                                        rs.getString("email"), rs.getString("senha"), rs.getDate("dataNascimento"), rs.getString("foto"), rs.getInt("idPermissao"));    
                lista.add(pf);
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PessoaFisicaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            Banco.closeConexao(conn, rs, pstmt, null);
        } 
        return lista;
    }
    
    
}