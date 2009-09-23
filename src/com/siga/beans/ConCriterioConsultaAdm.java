/*
 * Created on Apr 11, 2005
 * @author emilio.grau
 *
 */
package com.siga.beans;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.administracion.SIGAConstants;

/**
 * Administrador del Bean de la tabla ConCriterioConsulta
 */
public class ConCriterioConsultaAdm extends MasterBeanAdministrador {
	
	//	Constantes	
	static public final String CONS_CRITERIOS="%%CRITERIOS%%";

	public ConCriterioConsultaAdm(UsrBean usuario)
	{
	    super(ConCriterioConsultaBean.T_NOMBRETABLA, usuario);
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getCamposBean()
	 */
	protected String[] getCamposBean() {
		
		String[] campos = {ConCriterioConsultaBean.C_IDINSTITUCION,
				ConCriterioConsultaBean.C_ORDEN,
				ConCriterioConsultaBean.C_OPERADOR,
				ConCriterioConsultaBean.C_SEPARADORINICIO,
				ConCriterioConsultaBean.C_VALOR,
				ConCriterioConsultaBean.C_VALORDESC,
				ConCriterioConsultaBean.C_SEPARADORFIN,
				ConCriterioConsultaBean.C_IDCONSULTA,
				ConCriterioConsultaBean.C_IDOPERACION,
				ConCriterioConsultaBean.C_IDCAMPO,
				ConCriterioConsultaBean.C_ABRIRPAR,
				ConCriterioConsultaBean.C_CERRARPAR,
				ConCriterioConsultaBean.C_FECHAMODIFICACION,
				ConCriterioConsultaBean.C_USUMODIFICACION};
				
			return campos;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getClavesBean()
	 */
	protected String[] getClavesBean() {
		
		String[] claves = {ConCriterioConsultaBean.C_IDINSTITUCION,
				ConCriterioConsultaBean.C_IDCONSULTA,
				ConCriterioConsultaBean.C_ORDEN};

		return claves;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#hashTableToBean(java.util.Hashtable)
	 */
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		ConCriterioConsultaBean bean = null;

		try
		{
			bean = new ConCriterioConsultaBean();
						
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConCriterioConsultaBean.C_IDINSTITUCION));
			bean.setOrden(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_ORDEN));
			bean.setOperador(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_OPERADOR));
			bean.setSeparadorInicio(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_SEPARADORINICIO));
			bean.setValor(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_VALOR));
			bean.setValorDesc(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_VALORDESC));
			bean.setSeparadorFin(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_SEPARADORFIN));
			bean.setIdOperacion(UtilidadesHash.getInteger(hash, ConCriterioConsultaBean.C_IDOPERACION));
			bean.setAbrirPar(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_ABRIRPAR));
			bean.setCerrarPar(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_CERRARPAR));
			bean.setFechaModificacion(UtilidadesHash.getString(hash, ConCriterioConsultaBean.C_FECHAMODIFICACION));			
			bean.setUsuModificacion(UtilidadesHash.getInteger(hash, ConCriterioConsultaBean.C_USUMODIFICACION));
		
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#beanToHashTable(com.siga.beans.MasterBean)
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ConCriterioConsultaBean b = (ConCriterioConsultaBean) bean;

			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_ORDEN, b.getOrden());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_OPERADOR, b.getOperador());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_SEPARADORINICIO, b.getSeparadorInicio());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_VALOR, b.getValor());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_VALORDESC, b.getValorDesc());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_SEPARADORFIN, b.getSeparadorFin());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_IDOPERACION, b.getIdOperacion());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_ABRIRPAR, b.getAbrirPar());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_CERRARPAR, b.getCerrarPar());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_IDCAMPO, b.getIdCampo());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_FECHAMODIFICACION, b.getFechaModificacion());
			UtilidadesHash.set(htData, ConCriterioConsultaBean.C_USUMODIFICACION, b.getUsuModificacion());
			
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/** Funcion getCriteriosConsulta (String idInstitucion, String idConsulta)
	 * Devuelve los criterios de la consulta mas las descripciones del campo y del tipocampo
	 * @param idInstitucion
	 * @param idConsulta
	 * @return Vector con resultados
	 * */
	public Vector getCriteriosConsulta(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
				
		//Acceso a BBDD
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT CC.*, C."+ConCampoConsultaBean.C_NOMBREENCONSULTA;
	        sql += ", T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
	        sql += " AS DESCRIPCIONTIPOCAMPO, "+UtilidadesMultidioma.getCampoMultidiomaSimple("O."+ConOperacionConsultaBean.C_DESCRIPCION,this.usrbean.getLanguage());
	        sql += " AS DESCRIPCIONOPERACION, O."+ConOperacionConsultaBean.C_IDOPERACION;
	        sql += " FROM "+ConCriterioConsultaBean.T_NOMBRETABLA+" CC, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T, "+ConOperacionConsultaBean.T_NOMBRETABLA+" O";	        
			sql += " WHERE ";
			sql += ConCriterioConsultaBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCriterioConsultaBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CC."+ConCriterioConsultaBean.C_IDCAMPO+" AND ";
			sql += "CC."+ConCriterioConsultaBean.C_IDOPERACION+"=O."+ConOperacionConsultaBean.C_IDOPERACION;
			sql += " ORDER BY "+ConCriterioConsultaBean.C_ORDEN;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);										
					datos.add(fila);					
				}
			}
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return datos;
	}
	
	/** Funcion getWhere (String idInstitucion, String idConsulta)
	 * Devuelve un string con la parte de la query correspondiente a los criterios de filtrado
	 * @param idInstitucion
	 * @param idConsulta
	 * @return String sin la palabra 'WHERE'
	 * */
	public String getWhere(String idInstitucion, String idConsulta) throws ClsExceptions 
	{		
		//Acceso a BBDD
		String resultado = "";
		RowsContainer rc = null;
		Vector datos = new Vector();
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT C."+ConCampoConsultaBean.C_NOMBREENCONSULTA+", C."+ConCampoConsultaBean.C_TIPOCAMPO;
	        // Modificacion MAV tratar nombres reales en lugar consulta (ej 'residente' -> situacionResidente) 7/7/05
	        sql += ", C."+ConCampoConsultaBean.C_NOMBREREAL;
	        // Fin modificacion
	        sql += ", C."+ConCampoConsultaBean.C_FORMATO+", T."+ConTipoCampoConsultaBean.C_DESCRIPCION;
			sql += ", OC."+ConOperacionConsultaBean.C_SIMBOLO+", CC."+ConCriterioConsultaBean.C_VALOR+", CC."+ConCriterioConsultaBean.C_OPERADOR;
			sql += ", CC."+ConCriterioConsultaBean.C_SEPARADORINICIO+", CC."+ConCriterioConsultaBean.C_SEPARADORFIN;
			sql += ", CC."+ConCriterioConsultaBean.C_ABRIRPAR+", CC."+ConCriterioConsultaBean.C_CERRARPAR;
			sql += " FROM "+ConCriterioConsultaBean.T_NOMBRETABLA+" CC, "+ConCampoConsultaBean.T_NOMBRETABLA+" C, "+ConTipoCampoConsultaBean.T_NOMBRETABLA+" T, "+ConOperacionConsultaBean.T_NOMBRETABLA+" OC";
			sql += " WHERE ";
	        sql += ConCriterioConsultaBean.C_IDINSTITUCION+" = "+idInstitucion+" AND ";	
			sql += ConCriterioConsultaBean.C_IDCONSULTA+" = "+idConsulta+" AND ";	
			sql += "C."+ConCampoConsultaBean.C_IDTIPOCAMPO+"=T."+ConTipoCampoConsultaBean.C_IDTIPOCAMPO+" AND ";
			sql += "C."+ConCampoConsultaBean.C_IDCAMPO+"=CC."+ConCriterioConsultaBean.C_IDCAMPO+" AND ";
			sql += "CC."+ConCriterioConsultaBean.C_IDOPERACION+"=OC."+ConOperacionConsultaBean.C_IDOPERACION;
			sql += " ORDER BY "+ConCriterioConsultaBean.C_ORDEN;
			
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					if (fila.getString(ConCriterioConsultaBean.C_SEPARADORINICIO)!=null){
						resultado += fila.getString(ConCriterioConsultaBean.C_SEPARADORINICIO);
					}
					
			        // Modificacion MAV tratar nombres reales en lugar consulta (ej 'residente' -> situacionResidente) 7/7/05
					//resultado += fila.getString(ConTipoCampoConsultaBean.C_DESCRIPCION)+"."+fila.getString(ConCampoConsultaBean.C_NOMBREENCONSULTA);

					// RGG Error INC_3138 Para las comparaciones a iguales de fechas hace falta quitarle la hora.
					if(fila.getString(ConCampoConsultaBean.C_TIPOCAMPO).equals(SIGAConstants.TYPE_DATE) && fila.getString(ConOperacionConsultaBean.C_SIMBOLO).trim().equals("=")) {
						// hay que hacer un to_char
						resultado += "TO_CHAR ("+fila.getString(ConCampoConsultaBean.C_NOMBREREAL)+", 'YYYY/MM/DD')";
					} else {
						// proceso normal
						resultado += fila.getString(ConCampoConsultaBean.C_NOMBREREAL);
					}

					
			        // Fin modificacion					
					resultado += " "+fila.getString(ConOperacionConsultaBean.C_SIMBOLO)+" ";
					
					// RGG 20-06-2006 se controla que el operador es "esta vacio"
					if (!fila.getString(ConOperacionConsultaBean.C_SIMBOLO).trim().equals(ConOperacionConsultaBean.IS_NULL)) {
						
						if (fila.getString(ConCampoConsultaBean.C_FORMATO).equals(ConCampoConsultaAdm.CONS_FORMATO)){
							if (fila.getString(ConCampoConsultaBean.C_TIPOCAMPO).equals(SIGAConstants.TYPE_DATE)||fila.getString(ConCampoConsultaBean.C_TIPOCAMPO).equals(SIGAConstants.TYPE_ALPHANUMERIC)){
								resultado += "'"+fila.getString(ConCriterioConsultaBean.C_VALOR)+"'";	
							}else{
								resultado += fila.getString(ConCriterioConsultaBean.C_VALOR);
							}
						}else{
							String formato = fila.getString(ConCampoConsultaBean.C_FORMATO);
							if (formato==null || formato.equals("")){
								formato=ConCampoConsultaAdm.CONS_FORMATO;
							}
							final Pattern pattern = Pattern.compile(ConCampoConsultaAdm.CONS_FORMATO);
					        final Matcher matcher = pattern.matcher( formato );
					        
					        if (fila.getString(ConCampoConsultaBean.C_TIPOCAMPO).equals(SIGAConstants.TYPE_ALPHANUMERIC)){
					        	resultado += matcher.replaceAll("'"+fila.getString(ConCriterioConsultaBean.C_VALOR)+"'");
					        	
							}else if(fila.getString(ConCampoConsultaBean.C_TIPOCAMPO).equals(SIGAConstants.TYPE_DATE)) {
								// RGG Error INC_3138 Para las comparaciones a iguales de fechas hace falta quitarle la hora.
								if (fila.getString(ConOperacionConsultaBean.C_SIMBOLO).trim().equals("=")) {
									// hay que hacer un to_char
									resultado += "TO_CHAR ("+matcher.replaceAll(GstDate.getApplicationFormatDate("",fila.getString(ConCriterioConsultaBean.C_VALOR)))+", 'YYYY/MM/DD')";
								} else {
									// proceso normal
									resultado += matcher.replaceAll(GstDate.getApplicationFormatDate("",fila.getString(ConCriterioConsultaBean.C_VALOR)));
								}
							}else{
								resultado += matcher.replaceAll(fila.getString(ConCriterioConsultaBean.C_VALOR));
							}
						}
					}
					
				
					if (fila.getString(ConCriterioConsultaBean.C_SEPARADORFIN)!=null){
						resultado += fila.getString(ConCriterioConsultaBean.C_SEPARADORFIN);
					}
					
					if (fila.getString(ConCriterioConsultaBean.C_OPERADOR)!=null && !fila.getString(ConCriterioConsultaBean.C_OPERADOR).equals("")){
						resultado += " "+fila.getString(ConCriterioConsultaBean.C_OPERADOR)+" ";
					}else if (i<rc.size()-1){
						resultado += " "+ConConsultaAdm.CONS_Y+" ";
					}
				}
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return resultado;
	}
	
	
	/**
	 * Devuelve en un Vector de Hashtables, registros de la BD que son resultado de ejecutar la select.  
	 * @param String select: consulta SQL del SELECT almacenada en un String.
	 * @return Vector con Hashtables. Cada Hashtable es una fila del resultado del select en la base de datos.
	 * @throws ClsExceptions
	 */
	public Vector selectGenerico(String select) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.query(select)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Excepcion en FcsFactEjgAdm.selectGenerico(). Consulta SQL:"+select);
		}
		return datos;	
	}
	
	/** Funcion getWhere (String[] criterios)
	 * Devuelve un string con la parte de la query correspondiente a los criterios de filtrado a partir de<br>
	 * los criterios en forma de array de strings.
	 * @param String[] criterios 
	 * @return String sin la palabra 'WHERE'
	 * */
	public String getWhere(String[] criterios) throws ClsExceptions 
	{		
		//Acceso a BBDD
		String resultado = "";
		RowsContainer rc = null;
		Vector datos = new Vector();
		String idCampo="";
		String tipoCampo="";
		String idOperacion="";
		String valor="";
		String separadorInicio="";
		String separadorFin="";
		String operador="";
		String nombre="";
		String simbolo="";
		String formato="";
		String sql="";
		
		
		try { 
			for(int i=0;i<criterios.length;i++){
				if (criterios[i]==null ||criterios[i].equals("null")){
					break;
				}else{													
					String aTrocear = criterios[i];
					boolean hayParentesisIni = aTrocear.startsWith("#")?false:true;					
					boolean acabaEnY = aTrocear.endsWith(".Y.")?true:false;
					boolean acabaEnO = aTrocear.endsWith(".O.")?true:false;
					boolean acabaEnNo = aTrocear.endsWith(".NO.")?true:false;
					
					if (acabaEnY){
						aTrocear = aTrocear.substring(0,aTrocear.length()-3);
						operador=ConConsultaAdm.CONS_Y;
					}else if (acabaEnO){
						aTrocear = aTrocear.substring(0,aTrocear.length()-3);
						operador=ConConsultaAdm.CONS_O;
					}else if (acabaEnNo){
						aTrocear = aTrocear.substring(0,aTrocear.length()-4);
						operador=ConConsultaAdm.CONS_NO;
					}else{
						operador="";
					}
					
					boolean hayParentesisFin = aTrocear.endsWith("#")?false:true;
					
					StringTokenizer st = new StringTokenizer(aTrocear,"#");					
					if (hayParentesisIni){
						separadorInicio=st.nextToken();
						st.nextToken(); //tipocampo
						idCampo=st.nextToken();
						idOperacion=st.nextToken();
						try {
							valor=st.nextToken();
						} catch (java.util.NoSuchElementException ne) {
							// pongo un 1 porque no se puede insertar nulo??
							valor=" ";
						}
					}else{
						separadorInicio="";
						st.nextToken(); //tipocampo
						idCampo=st.nextToken();
						idOperacion=st.nextToken();
						try {
							valor=st.nextToken();
						} catch (java.util.NoSuchElementException ne) {
							// pongo un 1 porque no se puede insertar nulo??
							valor=" ";
						}

					}
					if (hayParentesisFin){
						separadorFin=st.nextToken();
					}else{
						separadorFin="";
					}
					
					rc = new RowsContainer(); 						
					
			        sql = "SELECT "+ConCampoConsultaBean.C_NOMBREREAL+", "+ConCampoConsultaBean.C_FORMATO+", "+ConCampoConsultaBean.C_TIPOCAMPO;
			        sql += " FROM "+ConCampoConsultaBean.T_NOMBRETABLA;	        
					sql += " WHERE ";
					sql += ConCampoConsultaBean.C_IDCAMPO+" = "+idCampo;
				
					if(rc.query(sql)){;
						Row fila = (Row) rc.get(0);						
						nombre= fila.getString(ConCampoConsultaBean.C_NOMBREREAL);
						formato=fila.getString(ConCampoConsultaBean.C_FORMATO);
						tipoCampo=fila.getString(ConCampoConsultaBean.C_TIPOCAMPO);
					}
					
					rc = new RowsContainer(); 		
					sql = "SELECT "+ConOperacionConsultaBean.C_SIMBOLO;
			        sql += " FROM "+ConOperacionConsultaBean.T_NOMBRETABLA;	        
					sql += " WHERE ";
					sql += ConOperacionConsultaBean.C_IDOPERACION+" = "+idOperacion;
					
					if(rc.query(sql)){;
						Row fila = (Row) rc.get(0);						
						simbolo= fila.getString(ConOperacionConsultaBean.C_SIMBOLO);
					}
					
					//Construyo el Where
					resultado += separadorInicio;
					
					// RGG Error INC_3138 Para las comparaciones a iguales de fechas hace falta quitarle la hora.
					if(tipoCampo.equals(SIGAConstants.TYPE_DATE) && simbolo.equals("=")) {
						// hay que hacer un to_char
						resultado += "TO_CHAR ("+nombre+", 'YYYY/MM/DD')";
					} else {
						// proceso normal
						resultado += nombre;
					}
					resultado += " "+simbolo+" ";
					
					// RGG Error INC_3099 Como la condición incluía ya el valor (is null) tenemos que evitar este paso.
					if (!simbolo.trim().equalsIgnoreCase(ConOperacionConsultaBean.IS_NULL)) {
						
						if (formato.equals(ConCampoConsultaAdm.CONS_FORMATO)){
							if (tipoCampo.equals(SIGAConstants.TYPE_DATE)||tipoCampo.equals(SIGAConstants.TYPE_ALPHANUMERIC)){
								resultado += "'"+valor+"'";	
							}else{
								resultado += valor;
							}
						}else{
							final Pattern pattern = Pattern.compile(ConCampoConsultaAdm.CONS_FORMATO);
					        final Matcher matcher = pattern.matcher( formato );
					        
					        if (tipoCampo.equals(SIGAConstants.TYPE_ALPHANUMERIC)){
					        	resultado += matcher.replaceAll("'"+valor+"'");
							}else if(tipoCampo.equals(SIGAConstants.TYPE_DATE)) {
								if (simbolo.equals("=")) {
									// hay que hacer un to_char
									resultado += "TO_CHAR ("+matcher.replaceAll(GstDate.getApplicationFormatDate("",valor))+", 'YYYY/MM/DD')";
								} else {
									// proceso normal
									resultado += matcher.replaceAll(GstDate.getApplicationFormatDate("",valor));
								}
								
							}else{
								resultado += matcher.replaceAll(valor);
							}
						}

					}

					resultado += separadorFin;
					if (operador.equals("")){
						resultado += " "+ConConsultaAdm.CONS_Y+" ";
					}else{
						resultado += " "+operador+" ";
					}
				}
			}
			
			if (!resultado.equals("")){
				resultado = resultado.substring(0,resultado.length()-5);
			}
			
		}catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return resultado;
	}
	
	public String sustituirParametrosColegiado(String select) {
		select = select.replaceAll("@FECHA@","SYSDATE");
		if ( select.indexOf(CenClienteBean.T_NOMBRETABLA)!=-1) {
			// CONTIENE LA TABLA CEN_COLEGIADO. 
			select = select.replaceAll("@IDPERSONA@",CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDPERSONA);
			select = select.replaceAll("@IDINSTITUCION@",CenClienteBean.T_NOMBRETABLA+"."+CenClienteBean.C_IDINSTITUCION);
		}else if (select.indexOf(CenColegiadoBean.T_NOMBRETABLA)!=-1 ) {
			// CONTIENE LA TABLA CEN_COLEGIADO. 
			select = select.replaceAll("@IDPERSONA@",CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDPERSONA);
			select = select.replaceAll("@IDINSTITUCION@",CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_IDINSTITUCION);
		}
		
		return select;
	}
	
	
}
