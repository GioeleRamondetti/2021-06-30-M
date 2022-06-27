package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Integer> getAllchromo(){
		String sql = "SELECT distinct Chromosome FROM Genes WHERE Chromosome<>0 ";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			List<Integer> result=new ArrayList<>();
			while (res.next()) {

				result.add(res.getInt("Chromosome"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	public List<Double> getAllpesi(int i,int y){
		String sql = "SELECT DISTINCT g1.GeneID,g2.GeneID,i.Expression_Corr FROM interactions i,genes g1,genes g2 "
				+ "WHERE g1.GeneID=i.GeneID1 AND g2.GeneID=i.GeneID2 and g1.Chromosome=? AND g2.Chromosome=?";
		List<Double> result = new ArrayList<Double>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, i);
			st.setInt(2, y);
			ResultSet res = st.executeQuery();
			
				while (res.next()) {
					result.add(res.getDouble("i.Expression_Corr"));
				}
			
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
}
