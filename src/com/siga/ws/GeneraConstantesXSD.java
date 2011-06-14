package com.siga.ws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class GeneraConstantesXSD {
	
	public static void main (String[] args) throws Exception {
		

//		String url = "jdbc:oracle:thin:pcajg/pcajg@127.0.0.1:1521:XE";
		String url = "jdbc:oracle:thin:uscgae2/uscgae2@192.168.11.55:1521:SIGADES";
//		String url = "jdbc:oracle:thin:pcajg/pcajg@192.168.11.55:1521:SIGADES";
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection(url);

		//PCAJG
//		String[] vistas = new String[]{"V_PCAJG_EJG", "V_PCAJG_ABOGADOSDESIGNADOS", "V_PCAJG_CONTRARIOS", "V_PCAJG_M_DOCUMENTACIONEXP"
//				, "V_PCAJG_FAMILIARES", "V_PCAJG_MARCASEXPEDIENTES", "V_PCAJG_DELITOS"};
		
		//PAMPLONA 2055
		//String[] vistas = new String[]{"v_ws_2055_archivo","v_ws_2055_ejg","v_ws_2055_persona"};
		
		//SANTIAGO 2064
//		String[] vistas = new String[]{"V_WS_2064_EJG", "V_WS_2064_PERSONA", "V_WS_2064_CONTRARIOS", "V_WS_2064_DOCUMENTO"};
		String[] vistas = new String[]{"V_WS_JE_2064", "V_WS_JE_2064_ASIS", "V_WS_JE_2064_DESIGNA"};
		
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
			
		rs.close();
		ps.close();
	}

}
