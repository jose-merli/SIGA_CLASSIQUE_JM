package com.siga.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



public class GeneraConstantesXSD {
	
	public static void main (String[] args) throws Exception {
		
		String url = "jdbc:oracle:thin:@192.168.11.55:1521:SIGADES";
		String user = "uscgae2";
		String pass = "uscgae2";
		
		Properties props = new Properties();
		props.put("user", user);
		props.put("password", pass);
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(url, props);

		//PCAJG
		String[] vistas = new String[]{"V_PCAJG_EJG", "V_PCAJG_ABOGADOSDESIGNADOS", "V_PCAJG_CONTRARIOS", "V_PCAJG_M_DOCUMENTACIONEXP"
				, "V_PCAJG_FAMILIARES", "V_PCAJG_MARCASEXPEDIENTES", "V_PCAJG_DELITOS"};
		
		//PAMPLONA 2055
		//String[] vistas = new String[]{"v_ws_2055_archivo","v_ws_2055_ejg","v_ws_2055_persona"};
		
		List arrayCampos = new ArrayList(); 
		
		System.out.println("/**");
		System.out.println(" * NO MODIFICAR. Clase generada con GeneraConstantesXSD");
		System.out.println(" */");
		
//		System.out.println("package com.siga.gratuita.util;");
		System.out.println("");
		System.out.println("/**");
		System.out.println(" * @author angelcpe");
		System.out.println(" *");
		System.out.println(" */");
		System.out.println("public interface PCAJGConstantes {");
		
		
		for (int i = 0; i < vistas.length; i++) {
			extraerCampos(conn, vistas[i], arrayCampos);
		}
		
		System.out.println("}");
		
		conn.close();
		
	}

	private static void extraerCampos(Connection conn, String vista, List arrayCampos) throws Exception {
		String sql = "SELECT * FROM " + vista;		

		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = null;
		
		try {
			rs = ps.executeQuery();
		} catch (SQLException e){
			System.err.println(sql);
			throw e;
		}
		
		
		if (rs.next()) {
			System.out.println("");			
			System.out.println("     /***** CAMPOS DE LA VISTA " + vista + " ****/");
			System.out.println("");
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				String column = rs.getMetaData().getColumnName(i);
				if (!arrayCampos.contains(column)) {
					arrayCampos.add(column);
					System.out.println("	public final String " + column + " = \"" + column + "\";");	
				} else {
//					System.err.println("YA EXISTE EL CAMPO " + column);
				}
				
			}
			System.out.println("");
		} else {
			throw new IllegalArgumentException("La vista " + vista + " no tiene datos y no se pueden extraer sus columnas.");
		}	
		rs.close();
		ps.close();
	}

}
