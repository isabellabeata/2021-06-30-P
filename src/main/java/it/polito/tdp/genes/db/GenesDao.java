package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Classification;
import it.polito.tdp.genes.model.Genes;



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
	
	public List<String> getVertici(){
		String sql="SELECT DISTINCT(c.Localization) AS loc "
				+ "FROM classification c "
				+ "ORDER BY c.Localization ASC ";
		
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String s= res.getString("loc");
				result.add(s);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
		
	}

	public List<Classification> getClassifications (Map<String, Classification> idMap){
		String sql="SELECT DISTINCT c.GeneID AS geneid, c.Localization AS loc "
				+ "FROM classification c";
		
		List<Classification> result = new ArrayList<Classification>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Classification c= new Classification(res.getString("geneid"), res.getString("localization"));
				idMap.put(c.getGeneId(), c);
				result.add(c);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public Arco getArco(String inter1, String inter2) {
		String sql="SELECT c1.Localization, c2.Localization, i.`Type` AS tipo1, COUNT(*) AS peso "
				+ "FROM classification c1, interactions i, classification c2 "
				+ "WHERE c1.Localization=? AND  c2.Localization=? "
				+ "	AND (c1.GeneID=i.GeneID1 AND c2.GeneID =i.GeneID2) "
				+ "	GROUP BY i.`Type` UNION( "
				+ "SELECT c1.Localization, c2.Localization, i.`Type` AS tipo2,  COUNT(*) AS peso "
				+ "FROM classification c1, interactions i, classification c2 "
				+ "WHERE c1.Localization=? AND  c2.Localization=? "
				+ "	AND (c1.GeneID=i.GeneID2 AND c2.GeneID =i.GeneID1) "
				+ "	GROUP BY i.`Type`) ";
		
		Arco a= null;
		List<String> listt= new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, inter1);
			st.setString(2, inter2);
			st.setString(3, inter1);
			st.setString(4, inter2);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				a= new Arco(inter1, inter2, 0);
				if(res.getString("tipo1")!=null && !(listt.contains(res.getString("tipo1")))) {
					listt.add(res.getString("tipo1"));
				}
					
			}
			if(a!=null) {
					a.setPeso(listt.size());
			}
			res.close();
			st.close();
			conn.close();
			return a;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
			
	}

}
