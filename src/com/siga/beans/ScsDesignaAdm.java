
package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;


import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.MasterForm;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.MaestroDesignasForm;

/**
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_DESIGNA
 * 
 * @author ruben.fernandez
 * @since 1/11/2004 
 * @version 26/01/2006 (david.sanchezp) nuevos campos.
 */
public class ScsDesignaAdm extends MasterBeanAdministrador {


	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicación. De tipo "Integer".  
	 */
	public ScsDesignaAdm (UsrBean usuario) {
		super( ScsDesignaBean.T_NOMBRETABLA, usuario);
	}

	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	protected String[] getCamposBean() {
		String[] campos = {	ScsDesignaBean.C_IDINSTITUCION,			ScsDesignaBean.C_IDTURNO,
							ScsDesignaBean.C_ANIO,					ScsDesignaBean.C_NUMERO,
							ScsDesignaBean.C_FECHAENTRADA,			ScsDesignaBean.C_FECHAFIN,
							ScsDesignaBean.C_RESUMENASUNTO,			ScsDesignaBean.C_DELITOS,				
							ScsDesignaBean.C_FECHAMODIFICACION,		ScsDesignaBean.C_USUMODIFICACION,
							ScsDesignaBean.C_PROCURADOR,			ScsDesignaBean.C_IDTIPODESIGNACOLEGIO,
							ScsDesignaBean.C_ESTADO,				ScsDesignaBean.C_OBSERVACIONES,
							ScsDesignaBean.C_IDPROCURADOR,			ScsDesignaBean.C_IDINSTITUCIONPROCURADOR,
							ScsDesignaBean.C_IDJUZGADO,				ScsDesignaBean.C_IDINSTITUCIONJUZGADO,
							ScsDesignaBean.C_FECHA_ANULACION,       ScsDesignaBean.C_DEFENSAJURIDICA,
						    ScsDesignaBean.C_FECHAJUICIO,			ScsDesignaBean.C_IDPRETENSION,
							ScsDesignaBean.C_CODIGO, 				ScsDesignaBean.C_NUMPROCEDIMIENTO,
							ScsDesignaBean.C_IDPROCEDIMIENTO,       ScsDesignaBean.C_FECHAESTADO,
							ScsDesignaBean.C_SUFIJO,				ScsDesignaBean.C_FECHAOFICIOJUZGADO,
							ScsDesignaBean.C_FECHARECEPCIONCOLEGIO};
		return campos;
	}
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	protected String[] getClavesBean() {
		String[] campos = {	ScsDesignaBean.C_IDINSTITUCION,			ScsDesignaBean.C_IDTURNO,
							ScsDesignaBean.C_ANIO,					ScsDesignaBean.C_NUMERO};
		return campos;
	}

	
	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la información de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsDesignaBean bean = null;
		try{
			bean = new ScsDesignaBean();
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_ANIO));
			bean.setDefensaJuridica(UtilidadesHash.getString(hash,ScsDesignaBean.C_DEFENSAJURIDICA));;
			bean.setDelitos(UtilidadesHash.getString(hash,ScsDesignaBean.C_DELITOS));
			bean.setEstado(UtilidadesHash.getString(hash,ScsDesignaBean.C_ESTADO));
			bean.setFechaEntrada(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHAENTRADA));
			bean.setFechaFin(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHAFIN));
			bean.setFechaMod(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHAMODIFICACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_IDINSTITUCION));
			bean.setIdTipoDesignaColegio(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_IDTIPODESIGNACOLEGIO));
			bean.setIdTurno(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_IDTURNO));
			bean.setNumero(UtilidadesHash.getLong(hash,ScsDesignaBean.C_NUMERO));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsDesignaBean.C_OBSERVACIONES));
			bean.setProcurador(UtilidadesHash.getString(hash,ScsDesignaBean.C_PROCURADOR));
			bean.setResumenAsunto(UtilidadesHash.getString(hash,ScsDesignaBean.C_RESUMENASUNTO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_USUMODIFICACION));			
			bean.setIdProcurador(UtilidadesHash.getLong(hash,ScsDesignaBean.C_IDPROCURADOR));
			bean.setIdInstitucionProcurador(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_IDINSTITUCIONPROCURADOR));
			bean.setIdJuzgado(UtilidadesHash.getLong(hash,ScsDesignaBean.C_IDJUZGADO));
			bean.setIdInstitucionJuzgado(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_IDINSTITUCIONJUZGADO));
			bean.setFechaAnulacion(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHA_ANULACION));
			bean.setCodigo(UtilidadesHash.getString(hash,ScsDesignaBean.C_CODIGO));
			bean.setNumProcedimiento(UtilidadesHash.getString(hash,ScsDesignaBean.C_NUMPROCEDIMIENTO));
			bean.setFechaJuicio(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHAJUICIO));
			bean.setProcedimiento(UtilidadesHash.getString(hash,ScsDesignaBean.C_IDPROCEDIMIENTO));
			bean.setFechaEstado(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHAESTADO));
			bean.setIdPretension(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_IDPRETENSION));
			bean.setFechaRecepcionColegio(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHARECEPCIONCOLEGIO));
			bean.setFechaOficioJuzgado(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHAOFICIOJUZGADO));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	public Hashtable obtenerLetradoDesigna(String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions{
		RowsContainer rc = new RowsContainer();
		Hashtable hashletrado=new Hashtable();
		try{
			String consulta="select p." + CenPersonaBean.C_IDPERSONA + " idpersona,"+
									" f_siga_calculoncolegiado(des." + ScsDesignasLetradoBean.C_IDINSTITUCION + ", des." + ScsDesignasLetradoBean.C_IDPERSONA +") as ncolegiado,"+
									" p." + CenPersonaBean.C_NOMBRE + " || ' ' || p." + CenPersonaBean.C_APELLIDOS1 + " || ' ' ||  p." + CenPersonaBean.C_APELLIDOS2 + " as nombre"+
							" from " + ScsDesignasLetradoBean.T_NOMBRETABLA + " des, " + CenPersonaBean.T_NOMBRETABLA + " p"+
							" where  p." + CenPersonaBean.C_IDPERSONA + " = des." + ScsDesignasLetradoBean.C_IDPERSONA +
								" and des." + ScsDesignasLetradoBean.C_IDPERSONA + " = F_SIGA_GETIDLETRADO_DESIGNA(des.idInstitucion,des.idTurno,des.anio,des.NUMERO)" +
							
								" and  des." + ScsDesignasLetradoBean.C_IDINSTITUCION + " = " + idInstitucion+
								" and des." + ScsDesignasLetradoBean.C_IDTURNO + " = " + idTurno +
								" and des." + ScsDesignasLetradoBean.C_ANIO + " = " + anio +
								" and des." + ScsDesignasLetradoBean.C_NUMERO + " = "+  numero +
								" and rownum = 1";
								//" and trunc( des."+  ScsDesignasLetradoBean.C_FECHADESIGNA +") <= trunc(sysdate)"+ 
								// Se comenta esta condición porque el sistema permite crear designas posteriores al sysdate 
								// y con esta condicion la consulta nunca devuelve valor
								//" and ( des." +  ScsDesignasLetradoBean.C_FECHARENUNCIA + " is  null or trunc( des."+ ScsDesignasLetradoBean.C_FECHARENUNCIA +") < trunc(sysdate))";
								/*
								" and des."+ScsDesignasLetradoBean.C_FECHADESIGNA+" =(select max(d."+ScsDesignasLetradoBean.C_FECHADESIGNA+")"+
		                                                                             " from  "+ScsDesignasLetradoBean.T_NOMBRETABLA+" d "+
		                                                                             " where d."+ScsDesignasLetradoBean.C_IDINSTITUCION + " = " + idInstitucion+
		                                             								 " and d." + ScsDesignasLetradoBean.C_IDTURNO + " = " + idTurno +
																					 " and d." + ScsDesignasLetradoBean.C_ANIO + " = " + anio +
																					 " and d." + ScsDesignasLetradoBean.C_NUMERO + " = "+  numero + ")" +
		                                                                             " and trunc(d."+ScsDesignasLetradoBean.C_FECHADESIGNA+") <=trunc(sysdate))"+
		                        " and rownum=1";              */     
								
			
			
			if (rc.find(consulta)) {
				
				if (rc.size() == 1) {
	               Row fila = (Row) rc.get(0);
	               hashletrado=fila.getRow();	                  
				}
				else {
					hashletrado = null;
				}
//	            for (int i = 0; i < rc.size(); i++){
//	               Row fila = (Row) rc.get(i);
//	               hashletrado=fila.getRow();	                  
//	              // datos.add(resultado);
//	            }
	         } 
	    }
	    catch (Exception e) {
	    	throw new ClsExceptions (e, "Error al obtener la informacion sobre el letrado de la designa");
	    }
		
		return hashletrado;
	}
	
	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la información del bean
	 * 
	 */
	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = null;
		try{
			hash = new Hashtable();
			ScsDesignaBean b = (ScsDesignaBean) bean;
			UtilidadesHash.set(hash, ScsDesignaBean.C_ANIO,b.getAnio());
			UtilidadesHash.set(hash, ScsDesignaBean.C_DEFENSAJURIDICA,b.getDefensaJuridica());
			UtilidadesHash.set(hash, ScsDesignaBean.C_DELITOS,b.getDelitos());
			UtilidadesHash.set(hash, ScsDesignaBean.C_ESTADO,b.getEstado());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHAENTRADA,b.getFechaEntrada());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHAFIN,b.getFechaFin());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHAMODIFICACION,b.getFechaMod());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDINSTITUCION,b.getIdInstitucion());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDTIPODESIGNACOLEGIO,b.getIdTipoDesignaColegio());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDTURNO,b.getIdTurno());
			UtilidadesHash.set(hash, ScsDesignaBean.C_NUMERO,b.getNumero());
			UtilidadesHash.set(hash, ScsDesignaBean.C_OBSERVACIONES,b.getObservaciones());
			UtilidadesHash.set(hash, ScsDesignaBean.C_PROCURADOR,b.getProcurador());
			UtilidadesHash.set(hash, ScsDesignaBean.C_RESUMENASUNTO,b.getResumenAsunto());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDPROCURADOR,b.getIdProcurador());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDINSTITUCIONPROCURADOR,b.getIdInstitucionProcurador());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDJUZGADO,b.getIdJuzgado());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDINSTITUCIONJUZGADO,b.getIdInstitucionJuzgado());
			UtilidadesHash.set(hash, ScsDesignaBean.C_USUMODIFICACION,b.getUsuMod());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHA_ANULACION,b.getFechaAnulacion());
			UtilidadesHash.set(hash, ScsDesignaBean.C_CODIGO,b.getCodigo());
			UtilidadesHash.set(hash, ScsDesignaBean.C_NUMPROCEDIMIENTO,b.getNumProcedimiento());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHAJUICIO,b.getFechaJuicio());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDPROCEDIMIENTO,b.getProcedimiento());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHAESTADO,b.getFechaEstado());
			UtilidadesHash.set(hash, ScsDesignaBean.C_IDPRETENSION,b.getIdPretension());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHAOFICIOJUZGADO,b.getFechaOficioJuzgado());
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHARECEPCIONCOLEGIO,b.getFechaRecepcionColegio());
			return hash;
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deberá ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		return null;
	}
	
	
	/** Funcion ejecutaSelect(String select)
	 *	@param select sentencia "select" sql valida, sin terminar en ";"
	 *  @return Vector todos los registros que se seleccionen 
	 *  en BBDD debido a la ejecucion de la sentencia select
	 *
	 */
	public Vector ejecutaSelect(String select) throws ClsExceptions 
	{
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
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparación consiste en calcular el
	 * identificador del turno que se va a insertar. 
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		String values;	
		RowsContainer rc = null;
		RowsContainer rc1 = null;
		int contador = 0;
		String codigo="";
		
		try { rc = new RowsContainer();
		      rc1 = new RowsContainer();
		}
		catch(Exception e) { e.printStackTrace(); }
		
		String sql ="SELECT (MAX(NUMERO) + 1) AS NUMERO FROM " + nombreTabla + 
					" WHERE IDINSTITUCION =" + entrada.get("IDINSTITUCION") +
					" AND ANIO =" + ((((String)entrada.get("FECHAENTRADAINICIO")).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1)).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1)) +
					" AND IDTURNO =" + entrada.get("IDTURNO");	
		
		//PDM INC-4774
		
		String sqlNumeroDesig ="SELECT (MAX(to_number(CODIGO)) + 1) AS NUMERODESIG FROM " + nombreTabla + 
		" WHERE IDINSTITUCION =" + entrada.get("IDINSTITUCION") +
		" AND ANIO =" + ((((String)entrada.get("FECHAENTRADAINICIO")).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1)).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1));
		 //Obtenemos el max(codigo)+1 por institucion y anio 
		
		GenParametrosAdm paramAdm = new GenParametrosAdm (this.usrbean);
		String longitudDesigna = paramAdm.getValor (this.usrbean.getLocation (), "SCS", "LONGITUD_CODDESIGNA", "");
			
		try {		
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("NUMERO").equals("")) {
					entrada.put(ScsDesignaBean.C_NUMERO,"1");
				}
				else entrada.put(ScsDesignaBean.C_NUMERO,(String)prueba.get("NUMERO"));								
			}
			
			if (rc1.query(sqlNumeroDesig)) {
				Row fila1 = (Row) rc1.get(0);
				Hashtable prueba1 = fila1.getRow();			
				if (prueba1.get("NUMERODESIG").equals("")) {
					codigo="1";
					codigo=UtilidadesString.formatea(codigo,Integer.parseInt(longitudDesigna),true);
					entrada.put(ScsDesignaBean.C_CODIGO,codigo);
				}
				else{
					codigo=(String)prueba1.get("NUMERODESIG");
					codigo=UtilidadesString.formatea(codigo,Integer.parseInt(longitudDesigna),true);
					entrada.put(ScsDesignaBean.C_CODIGO,codigo);								
				}
			}
			
			//Ponemos el resto de campos para inssertar la nueva Designa
			entrada.put(ScsDesignaBean.C_ANIO,((((String)entrada.get("FECHAENTRADAINICIO")).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1)).substring(((String)entrada.get("FECHAENTRADAINICIO")).indexOf("/")+1)));
			entrada.put(ScsDesignaBean.C_ESTADO,"V"); // Estado Activo (V)
			entrada.put(ScsDesignaBean.C_FECHAENTRADA,GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAENTRADAINICIO")));
			//entrada.put(ScsDesignaBean.C_CODIGO,(String)entrada.get("CODIGO"));
			
			
			if (entrada.get("JUZGADO")!=null && !(entrada.get("JUZGADO")).equals("")) {
				String juzgado = (String)entrada.get("JUZGADO");
				String idJuzgado = juzgado.substring(0, juzgado.indexOf(","));
				String idInstitucionJuzgado = juzgado.substring(juzgado.indexOf(",")+1);
				entrada.put(ScsDesignaBean.C_IDJUZGADO, idJuzgado);			
				entrada.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, idInstitucionJuzgado);				
			}
//			if(entrada.get("DILIGENCIA")!=null&& !(entrada.get("DILIGENCIA")).equals("")) {
//				String diligencia=(String)entrada.get("DILIGENCIA");
//				entrada.put(ScsDesignaBean.c_, diligencia);
//			}
//			if(entrada.get("COMISARIA")!=null&& !(entrada.get("COMISARIA")).equals("")) {
//				String comisaria=(String)entrada.get("COMISARIA");
//				entrada.put(ScsDesignaBean.C_IDJUZGADO, comisaria);
//			}
			if(entrada.get("PROCEDIMIENTO")!=null&& !(entrada.get("PROCEDIMIENTO")).equals("")) {
				String procedimiento=(String)entrada.get("PROCEDIMIENTO");
				entrada.put(ScsDesignaBean.C_NUMPROCEDIMIENTO, procedimiento);
			}
//			if(entrada.get("JUZGADO")!=null&& !(entrada.get("JUZGADO")).equals("")) {
//				String juzgado=(String)entrada.get("JUZGADO");
//				entrada.put(ScsDesignaBean.C_IDJUZGADO, juzgado);
//			}
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'prepararInsert' en B.D.");		
		}
		return entrada;
	}
		
	/** 
	 * Recoge informacion sobre las actuaciones relacionadas con una designa 
	 * @param  institucion - identificador de la institucion	 	  
	 * @param  turno - identificador del turno
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getActuacionesDesigna (String institucion, String turno, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer(); 
	            	            
	            String sql ="SELECT " +
			    			ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_IDINSTITUCION + "," +
			    			ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_IDTURNO + "," +
			    			ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_ANIO + "," +
			    			ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_NUMERO + "," +
			    			ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_NUMEROASUNTO +
							" FROM " + ScsActuacionDesignaBean.T_NOMBRETABLA +
							" INNER JOIN " + ScsDesignaBean.T_NOMBRETABLA +
								" ON " + ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_IDINSTITUCION + "=" + ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_IDINSTITUCION +
								 		 " AND " +
										 ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_IDTURNO + "=" + ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_IDTURNO +
										 " AND " +
										 ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_ANIO + "=" + ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_ANIO +
										 " AND " +
										 ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_NUMERO + "=" + ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_NUMERO +
							" WHERE " +			 
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + turno +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + numero;
	            
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre las actuaciones de una designa.");
	       }
	       return datos;                        
	    }
		
	/** 
	 * Recoge informacion la informacion necesaria para rellenar la carta a los interesados de designa EJG<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  turno - identificador de turno
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @param  actuacion - identificador de la actuacion
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosEJGRelativosDesigna (String institucion, String turno, String epoca, String numero, String actuacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql ="SELECT " +
							"(" + ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_NUMERO + " || '/' || " +
							ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_ANIO + ") AS NUMERO," +
			    			ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_LUGAR + " AS JUZGADO," +
			    			ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_DESCRIPCION + " AS TURNO," +
			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
			    			ScsProcedimientosBean.T_NOMBRETABLA + "." + ScsProcedimientosBean.C_NOMBRE + " AS PROCEDIMIENTO," +
			    			ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_DELITOS + " AS DELITO," +
							" DECODE(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_SEXO + ", 'M', 'FEMENINO', 'MASCULINO') AS SEXO_LETRADO," +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ") AS DATOS_LETRADO," +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " AS IDPERSONA," +
							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ' ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_LETRADO," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + " AS TELEFONO_LETRADO" +
							" FROM " + ScsDesignaBean.T_NOMBRETABLA + "," + CenClienteBean.T_NOMBRETABLA + "," + 
									   CenPersonaBean.T_NOMBRETABLA + "," + ScsActuacionDesignaBean.T_NOMBRETABLA + "," + 
									   CenColegiadoBean.T_NOMBRETABLA + "," + ScsProcedimientosBean.T_NOMBRETABLA + "," + 
									   ScsTurnoBean.T_NOMBRETABLA + 
									   " LEFT JOIN " + ScsSubzonaBean.T_NOMBRETABLA + " ON " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDINSTITUCION +
													   " AND " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDZONA +
													   " AND " +
													   ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDSUBZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDSUBZONA +
													   " LEFT JOIN " + CenPartidoJudicialBean.T_NOMBRETABLA + " ON " +
																	   ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDPARTIDO + "=" + CenPartidoJudicialBean.T_NOMBRETABLA +"."+ CenPartidoJudicialBean.C_IDPARTIDO + "," +
									   ScsDesignasLetradoBean.T_NOMBRETABLA +
									   " LEFT JOIN " + CenDireccionesBean.T_NOMBRETABLA + " ON " +
													   CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
													   " AND " +
													   CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +
													   " LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA + " ON " +
																	   CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION +
							" WHERE " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_ANIO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_NUMERO +
							" AND " +
							ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_FECHARENUNCIA + " IS NULL" +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +							
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_ANIO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_NUMERO +
							" AND " +
							ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_NUMEROASUNTO + "=" + actuacion +
							" AND " +
							ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDINSTITUCION + "=" + ScsProcedimientosBean.T_NOMBRETABLA +"."+ ScsProcedimientosBean.C_IDINSTITUCION +
							" AND " +
							ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDPROCEDIMIENTO + "=" + ScsProcedimientosBean.T_NOMBRETABLA +"."+ ScsProcedimientosBean.C_IDPROCEDIMIENTO + 
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + turno +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + numero;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una designa EJG.");
	       }
	       return datos;                        
	    }
	
	/**
	 * 
	 * @param  institucion - identificador de la institucion
	 * @param  turno - identificador de turno
	 * @param  anio - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return numero de expediente del EJG
	 * @throws ClsExceptions
	 */	
	public String getNumeroEJGDesigna(Integer institucion, Integer anio, Integer numero, Integer turno) throws ClsExceptions {
		String ejg=null;
		String sql = 
			"select numero||' / '||anio from scs_ejg "+
			"where idinstitucion="+institucion+
			"  and designa_anio="+anio+
			"  and designa_numero="+numero;
			//"  and designa_idturno="+turno;
		
		RowsContainer rc = new RowsContainer();
		if(rc.find(sql)){
			ejg=((Row)rc.get(0)).getString("NUMERO");
		}
		
		return ejg;
	}
	
	/** 
	 * Recoge la informacion necesaria para rellenar la carta a los interesados de oficio<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  turno - identificador de turno
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @param  actuacion - identificador de la actuacion
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosCartaDesigna (String institucion, String turno, String epoca, String numero, String actuacion) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql ="SELECT " +
							"(" + ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_NUMERO + " || '/' || " +
							ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_ANIO + ") AS NUMERO," +
			    			ScsActuacionDesignaBean.T_NOMBRETABLA + "." + ScsActuacionDesignaBean.C_LUGAR + " AS JUZGADO," +
			    			ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_DESCRIPCION + " AS TURNO," +
			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
			    			ScsProcedimientosBean.T_NOMBRETABLA + "." + ScsProcedimientosBean.C_NOMBRE + " AS PROCEDIMIENTO," +
			    			ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_DELITOS + " AS DELITO," +
							" DECODE(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_SEXO + ", 'M', 'FEMENINO', 'MASCULINO') AS SEXO_LETRADO," +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ") AS DATOS_LETRADO," +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " AS IDPERSONA," +
							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ' ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_LETRADO," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + " AS TELEFONO_LETRADO" +
							" FROM " + ScsDesignaBean.T_NOMBRETABLA + "," + ScsTurnoBean.T_NOMBRETABLA + "," +
							           CenPartidoJudicialBean.T_NOMBRETABLA + "," + CenClienteBean.T_NOMBRETABLA + "," + 
									   CenPersonaBean.T_NOMBRETABLA + "," + CenDireccionesBean.T_NOMBRETABLA + "," +
									   CenPoblacionesBean.T_NOMBRETABLA + "," + ScsSubzonaBean.T_NOMBRETABLA + "," + 
									   ScsActuacionDesignaBean.T_NOMBRETABLA + "," + CenColegiadoBean.T_NOMBRETABLA + "," + 
									   ScsDesignasLetradoBean.T_NOMBRETABLA + "," + ScsProcedimientosBean.T_NOMBRETABLA +
							" WHERE " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_ANIO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_NUMERO +
							" AND " +
							ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_FECHARENUNCIA + " IS NULL" +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +							
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDINSTITUCION +
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDZONA +
							" AND " +
							ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDSUBZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDSUBZONA +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_ANIO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_NUMERO +
							" AND " +
							ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_NUMEROASUNTO + "=" + actuacion +
							" AND " +
							ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDINSTITUCION + "=" + ScsProcedimientosBean.T_NOMBRETABLA +"."+ ScsProcedimientosBean.C_IDINSTITUCION +
							" AND " +
							ScsActuacionDesignaBean.T_NOMBRETABLA +"."+ ScsActuacionDesignaBean.C_IDPROCEDIMIENTO + "=" + ScsProcedimientosBean.T_NOMBRETABLA +"."+ ScsProcedimientosBean.C_IDPROCEDIMIENTO + 
							" AND " +
							ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDPARTIDO + "=" + CenPartidoJudicialBean.T_NOMBRETABLA +"."+ CenPartidoJudicialBean.C_IDPARTIDO +
							" AND " +
							CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "(+)=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "(+)=" + ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "(+)=" + ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +
							" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + turno +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + numero;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una designa EJG.");
	       }
	       return datos;                        
	    }
	
	/** 
	 * Recoge la informacion necesaria para rellenar la carta a los interesados de oficio<br/> 
	 * @param  institucion - identificador de la institucion
	 * @param  turno - identificador de turno
	 * @param  epoca - anho del expediente	 	  
	 * @param  numero - numero de expediente
	 * @return  Vector - Filas seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */
	public Vector getDatosCartaDesigna (String institucion, String turno, String epoca, String numero) throws ClsExceptions,SIGAException {
		   Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
	            	            
	            String sql ="SELECT " +
							"(" + ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_NUMERO + " || '/' || " +
							ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_ANIO + ") AS NUMERO," +
			    			ScsTurnoBean.T_NOMBRETABLA + "." + ScsTurnoBean.C_DESCRIPCION + " AS TURNO," +
			    			ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_FECHAENTRADA + " AS FECHA_DESIGNA," +
			    			ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_RESUMENASUNTO + " AS RESUMEN_ASUNTO," +
			    			CenPartidoJudicialBean.T_NOMBRETABLA + "." + CenPartidoJudicialBean.C_NOMBRE + " AS PARTIDO_JUDICIAL," +
			    			ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_DELITOS + " AS DELITO," +
			    			ScsDesignaBean.T_NOMBRETABLA + "." + ScsDesignaBean.C_OBSERVACIONES + " AS OBSERVACIONES," +
							" DECODE(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_SEXO + ", 'M', 'FEMENINO', 'MASCULINO') AS SEXO_LETRADO," +
							"(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + ") AS DATOS_LETRADO," +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA + " AS IDPERSONA," +
							"(" + CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_DOMICILIO + " || ' ' || " +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_CODIGOPOSTAL + " || ' ' || " +
							CenPoblacionesBean.T_NOMBRETABLA + "." + CenPoblacionesBean.C_NOMBRE + ") AS DIRECCION_LETRADO," +
							CenDireccionesBean.T_NOMBRETABLA + "." + CenDireccionesBean.C_TELEFONO1 + " AS TELEFONO_LETRADO" +
							" FROM " + ScsDesignaBean.T_NOMBRETABLA + "," + ScsTurnoBean.T_NOMBRETABLA + 
									   " LEFT JOIN " + ScsSubzonaBean.T_NOMBRETABLA + 
									   		" ON " + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDINSTITUCION +
												     " AND " +
													 ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDZONA +
													 " AND " +
													 ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDSUBZONA + "=" + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDSUBZONA +
													 " LEFT JOIN " + CenPartidoJudicialBean.T_NOMBRETABLA + 
												   		  " ON " + ScsSubzonaBean.T_NOMBRETABLA +"."+ ScsSubzonaBean.C_IDPARTIDO + "=" + CenPartidoJudicialBean.T_NOMBRETABLA +"."+ CenPartidoJudicialBean.C_IDPARTIDO + "," +
							           CenClienteBean.T_NOMBRETABLA + "," + CenPersonaBean.T_NOMBRETABLA + "," + 
									   CenDireccionesBean.T_NOMBRETABLA + 
									   " LEFT JOIN " + CenPoblacionesBean.T_NOMBRETABLA + 
								   			" ON " + CenPoblacionesBean.T_NOMBRETABLA +"."+ CenPoblacionesBean.C_IDPOBLACION + "=" + CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPOBLACION + "," +  
									   CenColegiadoBean.T_NOMBRETABLA + "," + ScsDesignasLetradoBean.T_NOMBRETABLA +
							" WHERE " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsTurnoBean.T_NOMBRETABLA +"."+ ScsTurnoBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDTURNO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_ANIO +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_NUMERO +
							" AND " +
							ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_FECHARENUNCIA + " IS NULL" +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDINSTITUCION +
							" AND " +
							CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenClienteBean.T_NOMBRETABLA +"."+ CenClienteBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND " +
							CenPersonaBean.T_NOMBRETABLA +"."+ CenPersonaBean.C_IDPERSONA + "=" + ScsDesignasLetradoBean.T_NOMBRETABLA +"."+ ScsDesignasLetradoBean.C_IDPERSONA +							
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDINSTITUCION + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION +
							" AND " +
							CenDireccionesBean.T_NOMBRETABLA +"."+ CenDireccionesBean.C_IDPERSONA + "=" + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDPERSONA +
							" AND "+CenDireccionesBean.T_NOMBRETABLA+"."+CenDireccionesBean.C_FECHABAJA+" is null "+
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDINSTITUCION + "=" + institucion +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_IDTURNO + "=" + turno +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_ANIO + "=" + epoca +
							" AND " +
							ScsDesignaBean.T_NOMBRETABLA +"."+ ScsDesignaBean.C_NUMERO + "=" + numero;
														
	            if (rc.find(sql)) {
	               for (int i = 0; i < rc.size(); i++){
	                  Row fila = (Row) rc.get(i);
	                  Hashtable resultado=fila.getRow();	                  
	                  datos.add(resultado);
	               }
	            } 
	       }
	       catch (Exception e) {
	       	throw new ClsExceptions (e, "Error al obtener la informacion sobre una designa EJG.");
	       }
	       return datos;                        
	    }	
	
	/** Funcion selectGenerico (String consulta). Ejecuta la consulta que se le pasa en un string 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectGenerico(String consulta) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();			

			if (rc.query(consulta)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					Hashtable registro = (Hashtable) fila.getRow(); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		return datos;
	}
	public Hashtable getTituloPantallaDesigna (String idInstitucion, String anio, String numero, String idTurno) 
	{
		try {
			String sql = 	"select " + ScsPersonaJGBean.C_NOMBRE + "," + 
			                            ScsPersonaJGBean.C_APELLIDO1 + ", " + 
										ScsPersonaJGBean.C_APELLIDO2 + ", " + 
										"a."+ScsDesignaBean.C_ANIO + ", " +
										"a."+ScsDesignaBean.C_CODIGO+", "+
										"a."+ScsDesignaBean.C_SUFIJO+																				
							" from " + ScsPersonaJGBean.T_NOMBRETABLA + " p, " + ScsDesignaBean.T_NOMBRETABLA + " a, "  + ScsDefendidosDesignaBean.T_NOMBRETABLA + " d " + 
							" where a." + ScsDesignaBean.C_IDINSTITUCION + " = " + idInstitucion +  
							  " and a." + ScsDesignaBean.C_ANIO + " = " + anio +
							  " and a." + ScsDesignaBean.C_NUMERO  + " = " + numero +
							  " and a." + ScsDesignaBean.C_IDTURNO  + " = " + idTurno +
							  " and d." + ScsDefendidosDesignaBean.C_IDPERSONA + " = p." + ScsPersonaJGBean.C_IDPERSONA+"(+)" +
							  " and d." + ScsDefendidosDesignaBean.C_IDINSTITUCION+ " = p." + ScsPersonaJGBean.C_IDINSTITUCION+"(+)"+
							  " and d." + ScsDefendidosDesignaBean.C_IDINSTITUCION + "(+) = a." + ScsDesignaBean.C_IDINSTITUCION +
							  " and d." + ScsDefendidosDesignaBean.C_IDTURNO + "(+) = a." + ScsDesignaBean.C_IDTURNO +
							  " and d." + ScsDefendidosDesignaBean.C_ANIO + "(+) = a." + ScsDesignaBean.C_ANIO +
							  " and d." + ScsDefendidosDesignaBean.C_NUMERO + "(+) = a." + ScsDesignaBean.C_NUMERO +
							  " and rownum=1";//Si hubiera más de un interesado sólo cogemos el primero
	
			Vector v = this.selectGenerico(sql);
			if (v!=null && v.size()>0) {
				return (Hashtable) v.get(0);
			}
		} 
		catch (ClsExceptions e) {
//			e.printStackTrace();
		}
		return new Hashtable();
	}
	
	public Vector getRelacionadoCon (String institucion, String anio, String numero, String idTipo) throws ClsExceptions,SIGAException 
	{
		try {
	            	            
	       	String sql = " SELECT * FROM ( " +
							" SELECT TRIM('ASISTENCIA') SJCS, " + 
							         ScsAsistenciasBean.C_IDINSTITUCION + " IDINSTITUCION, " + 
									 ScsAsistenciasBean.C_ANIO + " ANIO, " + 
									 ScsAsistenciasBean.C_NUMERO + " NUMERO, " +
									 ScsAsistenciasBean.C_IDPERSONACOLEGIADO + " IDLETRADO, " + 
									 "TO_CHAR("+ScsAsistenciasBean.C_IDTURNO + ") IDTURNO, " +
									 "TO_CHAR (DESIGNA_TURNO) IDTURNODESIGNA, "+
									 "TO_CHAR("+ScsAsistenciasBean.C_IDTIPOASISTENCIA + ") IDTIPO, " +
									 "TO_CHAR("+ScsAsistenciasBean.C_NUMERO + ") CODIGO, " +
									 
									 "(SELECT " + ScsTurnoBean.C_ABREVIATURA + " FROM " + ScsTurnoBean.T_NOMBRETABLA + 
									 " WHERE " + ScsTurnoBean.C_IDTURNO + " = " + ScsAsistenciasBean.T_NOMBRETABLA + " ." + ScsAsistenciasBean.C_IDTURNO + 
									 " AND " + ScsTurnoBean.C_IDINSTITUCION + " = " + ScsAsistenciasBean.T_NOMBRETABLA + " ." + ScsAsistenciasBean.C_IDINSTITUCION + ") DES_TURNO, " +

									 "(SELECT f_siga_getRecurso(descripcion, " + this.usrbean.getLanguage() + ") FROM scs_tipoasistencia " +  
									 " WHERE scs_tipoasistencia.idtipoasistencia = " + ScsAsistenciasBean.T_NOMBRETABLA + "." + ScsAsistenciasBean.C_IDTIPOASISTENCIA + " ) DES_TIPO " +

							  " FROM " + ScsAsistenciasBean.T_NOMBRETABLA +
							 " WHERE " + ScsAsistenciasBean.C_DESIGNA_ANIO + " = " + anio + 
							   " AND " + ScsAsistenciasBean.C_DESIGNA_NUMERO + " = " + numero +
							   " AND " + ScsAsistenciasBean.C_DESIGNA_TURNO + " = " + idTipo +
							   " AND " + ScsAsistenciasBean.C_IDINSTITUCION + " = " + institucion +
							
							" UNION " +
							
							" SELECT TRIM('EJG') SJCS, " + 
									 "e."+ScsEJGBean.C_IDINSTITUCION + " IDINSTITUCION, " +
									 "e."+ScsEJGBean.C_ANIO + " ANIO, " + 
									 "e."+ScsEJGBean.C_NUMERO + " NUMERO, " +
									 "e."+ScsEJGBean.C_IDPERSONA + " IDLETRADO, " +
									 "TO_CHAR(e."+ScsEJGBean.C_GUARDIATURNO_IDTURNO + ") IDTURNO, " +
									 "TO_CHAR(ed." + ScsEJGDESIGNABean.C_IDTURNO + ") IDTURNODESIGNA, "+
									 "TO_CHAR(e."+ScsEJGBean.C_IDTIPOEJG + ") IDTIPO, " +
									 "e."+ScsEJGBean.C_NUMEJG +  " CODIGO, " +

									 "(SELECT " + ScsTurnoBean.C_ABREVIATURA + " FROM " + ScsTurnoBean.T_NOMBRETABLA + 
									 " WHERE " + ScsTurnoBean.C_IDTURNO + " = e." + ScsEJGBean.C_GUARDIATURNO_IDTURNO + 
									 " AND " + ScsTurnoBean.C_IDINSTITUCION + " = e." + ScsEJGBean.C_IDINSTITUCION + ") DES_TURNO, " +

									 "(SELECT f_siga_getRecurso(descripcion, " + this.usrbean.getLanguage() + ") FROM scs_tipoejg " +  
									 " WHERE scs_tipoejg.idtipoejg = e." + ScsEJGBean.C_IDTIPOEJG + " ) DES_TIPO " +
									 
							  " FROM " + ScsEJGBean.T_NOMBRETABLA +" e"+
							  " ," + ScsEJGDESIGNABean.T_NOMBRETABLA +" ed"+
							 " WHERE ed." + ScsEJGDESIGNABean.C_ANIODESIGNA + " = " + anio +
							   " AND ed." + ScsEJGDESIGNABean.C_NUMERODESIGNA + " = " + numero +
							   " AND ed." + ScsEJGDESIGNABean.C_IDTURNO + " = " + idTipo +
							   " AND ed." + ScsEJGBean.C_IDINSTITUCION + " = " + institucion +
							   " AND ed." + ScsEJGBean.C_IDINSTITUCION + " = e."+ ScsEJGDESIGNABean.C_IDINSTITUCION +
							   " AND ed." + ScsEJGDESIGNABean.C_ANIOEJG + " = e."+ ScsEJGBean.C_ANIO +
							   " AND ed." + ScsEJGDESIGNABean.C_NUMEROEJG + " = e."+ ScsEJGBean.C_NUMERO +
							   " AND ed." + ScsEJGDESIGNABean.C_IDTIPOEJG + " = e."+ ScsEJGBean.C_IDTIPOEJG +
							   " AND ed." + ScsEJGDESIGNABean.C_IDINSTITUCION + " = e."+ ScsEJGBean.C_IDINSTITUCION +
							
							" ) " +
						 " ORDER BY SJCS, IDINSTITUCION, ANIO, CODIGO ";

	       	return this.selectGenerico(sql);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de un ejg.");
		}
	}
	
	
	/**
	 * Obtiene la descripcion del nombre del turno de la designa
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getNombreTurnoDes (String idInstitucion, String idTurno) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        if (idInstitucion!=null && !idInstitucion.equals("") && idTurno!=null && !idTurno.equals("")) {
	            
		        codigos.put(new Integer(1),idInstitucion);
		        codigos.put(new Integer(2),idTurno);
				String select =	"SELECT F_SIGA_GETNOMBRETURNO(:1, :2) as NOMBRE_TURNO FROM DUAL"; 
	
				RowsContainer rc = new RowsContainer(); 
				if (rc.queryBind(select, codigos)) {
					if (rc.size() != 1) return null;
					Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
					String num = UtilidadesHash.getString(aux, "NOMBRE_TURNO");
					return num;
				}
	        }

		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el nombre del turno.");
	   			}
	   		}	
	    }
		return "";
	}
	
	/**
	 * Obtiene si la designa tiene actuaciones sin validar
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getActDesig_NoValidar (String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        codigos.put(new Integer(2),idTurno);
	        codigos.put(new Integer(3),anio);
	        codigos.put(new Integer(4),numero);
			String select =	"SELECT F_SIGA_ACTUACIONESDESIG(:1, :2, :3, :4) as ACT_NOVALIDAR FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "ACT_NOVALIDAR");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el si existen actuaciones no validadas.");
	   			}
	   		}	
	    }
		return null;
	}
	
	/**
	 * Obtiene el defendido de la designa
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getDefendidosDesigna (String idInstitucion, String idTurno, String anio, String numero, String idTipo) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        codigos.put(new Integer(2),anio);
	        codigos.put(new Integer(3),idTurno);
	        codigos.put(new Integer(4),numero);
	        codigos.put(new Integer(5),idTipo);
			String select =	"SELECT f_siga_getdefendidosdesigna(:1, :2, :3, :4, :5) as DEFEND_DESIGNA FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "DEFEND_DESIGNA");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener Defendidos de Designa.");
	   			}
	   		}	
	    }
		return null;
	}
	
	/**
	 * Obtiene el letrado designado
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getLetradoDesig (String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        
	        codigos.put(new Integer(2),idTurno);
	        codigos.put(new Integer(3),anio);
	        codigos.put(new Integer(4),numero);
	       
			String select =	"SELECT F_SIGA_GETNOMAPELETRA_DESIGNA(:1, :2, :3, :4) as LETRADO_DESIG FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "LETRADO_DESIG");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el letrado designado.");
	   			}
	   		}	
	    }
		return null;
	}
	
	
	/**
	 * Obtiene el letrado designado
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getApeNomDesig (String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    String apellidoNombre = "";
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        
	        codigos.put(new Integer(2),idTurno);
	        codigos.put(new Integer(3),anio);
	        codigos.put(new Integer(4),numero);
	       
			String select =	"SELECT F_SIGA_GETAPELETRADO_DESIGNA(:1, :2, :3, :4) as APELETRADO FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) {
					apellidoNombre+="";
				}else{
					Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
					apellidoNombre = UtilidadesHash.getString(aux, "APELETRADO") + ", ";
				}
			}
			
			select =	"SELECT F_SIGA_GETNOMLETRADO_DESIGNA(:1, :2, :3, :4) as NOMBRE FROM DUAL";
			rc = new RowsContainer();
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) {
					apellidoNombre+="";
				}else{
					Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
					apellidoNombre += UtilidadesHash.getString(aux, "NOMBRE");
				}
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el letrado designado.");
	   			}
	   		}	
	    }
		return apellidoNombre;
	}

	
	/**
	 * Obtiene el id letrado de la designacion
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getIDLetradoDesig (String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        codigos.put(new Integer(2),idTurno);
	        codigos.put(new Integer(3),anio);
	        codigos.put(new Integer(4),numero);
	       
			String select =	"SELECT F_SIGA_GETIDLETRADO_DESIGNA(:1, :2, :3, :4) as IDLETRADO_DESIG FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "IDLETRADO_DESIG");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el id del letrado designado.");
	   			}
	   		}	
	    }
		return null;
	}
	
	/**
	 * Obtiene el id letrado de la designacion
	 * @param idInstitucion
	 * @param idFactura
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getNColegiadoDesig (String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        codigos.put(new Integer(2),idInstitucion.toString());
	        codigos.put(new Integer(3),idTurno);
	        codigos.put(new Integer(4),anio);
	        codigos.put(new Integer(5),numero);
	       
			String select =	"SELECT F_SIGA_CALCULONCOLEGIADO(:1, F_SIGA_GETIDLETRADO_DESIGNA(:2,:3,:4,:5)) as NCOLEG_DESIG FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "NCOLEG_DESIG");
				return num;
			}
		}
	    catch (Exception e) {
	   		if (e instanceof SIGAException){
	   			throw (SIGAException)e;
	   		}
	   		else {
	   			if (e instanceof ClsExceptions){
	   				throw (ClsExceptions)e;
	   			}
	   			else {
	   				throw new ClsExceptions(e, "Error al obtener el id del letrado designado.");
	   			}
	   		}	
	    }
		return null;
	}
	
	public PaginadorBind getBusquedaDesigna(String idInstitucion, Hashtable miHash) throws ClsExceptions , SIGAException{
		
		String consulta = "";
		Hashtable codigosBind = new Hashtable();
		int contador=0;
	  	
	  	// Acceso a BBDD
		int totalRegistros=0;
		
		
		
		try {
			//String bBusqueda = formulario.getChkBusqueda();
			consulta=" select des.estado estado, des.anio anio, des.numero numero, des.fechaentrada fechaentrada,des.idturno idturno, des.codigo codigo, des.sufijo sufijo,des.idinstitucion idinstitucion ";
			
			consulta+=" from scs_designa des";
			contador++;
			codigosBind.put(new Integer(contador),idInstitucion);
			consulta+=" where des.idinstitucion =:"+contador;
			
			 if ((UtilidadesHash.getString(miHash,"IDTURNO") != null)&&(UtilidadesHash.getString(miHash,"IDTURNO") != "-1")&&!UtilidadesHash.getString(miHash,"IDTURNO").equals("")){
			 	contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"IDTURNO"));
			 	consulta+=" and des.idTurno = :"+contador;
			 }
			 	
			 	

			if(UtilidadesHash.getString(miHash,"NCOLEGIADO")!=null && !((String)UtilidadesHash.getString(miHash,"NCOLEGIADO")).equals("") ){
				contador++;
			    codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"NCOLEGIADO"));
			    consulta += " and F_SIGA_GETIDLETRADO_DESIGNA(des.idinstitucion,des.idturno,des.anio,des.numero) = :"+contador;
			}
			if (UtilidadesHash.getString(miHash,"ANIO") != null && !UtilidadesHash.getString(miHash,"ANIO").equalsIgnoreCase("")) {
				
			    if (UtilidadesHash.getString(miHash,"ANIO").indexOf('*') >= 0){
			    	
				    contador++;
				    consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"ANIO").trim(),"des.anio",contador,codigosBind );
				    
			    }    
			    else{
			    	contador++;
				    codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"ANIO").trim());
			    	consulta += " AND des.anio = :" + contador;
			    }	
			}
			/*if (UtilidadesHash.getString(miHash,"SUFIJO") != null && !UtilidadesHash.getString(miHash,"SUFIJO").equalsIgnoreCase("")) {
				
				
			    if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash,"SUFIJO")))	{
			    	contador++;
			    	consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(UtilidadesHash.getString(miHash,"SUFIJO"),"des.sufijo",contador,codigosBind ); 
			    	
			    }else {
			    	contador++;
				    codigosBind.put(new Integer(contador),UtilidadesHash.getString(miHash,"SUFIJO").trim());
				    consulta += " AND ltrim(des.sufijo,'0') = ltrim(:" + contador+",'0')" ; 
			    	
			    }
			}*/
			if (UtilidadesHash.getString(miHash,"CODIGO") != null && !UtilidadesHash.getString(miHash,"CODIGO").equalsIgnoreCase("")) {
				
				
			    if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash,"CODIGO")))	{
			    	contador++;
			    	consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(UtilidadesHash.getString(miHash,"CODIGO"),"des.codigo",contador,codigosBind ); 
			    	
			    }else {
			    	contador++;
				    codigosBind.put(new Integer(contador),UtilidadesHash.getString(miHash,"CODIGO").trim());
			    	// Si es numerico
			    	/*if (UtilidadesHash.getInteger(miHash,"CODIGO") != null) {
			    		consulta += " AND des.codigo =:" + contador;//El código para la busqueda se trata como un numerico para que si no meten comodines(*) 
			                                                                                      // haga la busqueda exacta y si intentan buscar el numero 3 devuelve el 3,03,003, etc...
			    	}
			    	else { // no es numerico*/
			    		consulta += " AND ltrim(des.codigo,'0') = ltrim(:" + contador+",'0')" ; 
			    	//}
			    }
			}
			if (UtilidadesHash.getString(miHash,"JUZGADO") != null && !UtilidadesHash.getString(miHash,"JUZGADO").equalsIgnoreCase("")) {
				contador++;
				String a[]=((String)UtilidadesHash.getString(miHash,"JUZGADO")).split(",");
				codigosBind.put(new Integer(contador),a[0].trim());
				consulta += " AND des.idjuzgado = :" + contador ;
			}
			if (UtilidadesHash.getString(miHash,"ASUNTO") != null && !UtilidadesHash.getString(miHash,"ASUNTO").equalsIgnoreCase("")) {
				contador++;
				codigosBind.put(new Integer(contador),UtilidadesHash.getString(miHash,"ASUNTO").trim());
				consulta += " AND des.resumenasunto = :" + contador ;
			}
			if (UtilidadesHash.getString(miHash,"ESTADO") != null && !UtilidadesHash.getString(miHash,"ESTADO").equalsIgnoreCase("")&& !UtilidadesHash.getString(miHash,"ESTADO").equalsIgnoreCase("N")) {
				contador++;
				codigosBind.put(new Integer(contador),UtilidadesHash.getString(miHash,"ESTADO").trim());
				consulta += " AND des.estado = :" + contador ;
			}
			if (UtilidadesHash.getString(miHash,"PROCEDIMIENTO") != null && !UtilidadesHash.getString(miHash,"PROCEDIMIENTO").equalsIgnoreCase("")) {
				contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"PROCEDIMIENTO").trim());
				consulta += " AND des.numprocedimiento = :" + contador;
			}
			String actuacionesPendientes = UtilidadesHash.getString(miHash,"ACTUACIONES_PENDIENTES") ;
			if (actuacionesPendientes!= null && !actuacionesPendientes.equalsIgnoreCase("")&& (actuacionesPendientes.equalsIgnoreCase("NO")||actuacionesPendientes.equalsIgnoreCase("SI")||actuacionesPendientes.equalsIgnoreCase("SINACTUACIONES"))) {
				if(actuacionesPendientes.equalsIgnoreCase("SINACTUACIONES")){
					actuacionesPendientes="";
					// contador ++;
				    // codigos.put(new Integer(contador),actuacionesPendientes.trim());
					consulta += " and upper(F_SIGA_ACTUACIONESDESIG(des.idinstitucion,des.idturno,des.anio,des.numero)) is null";
				}else{
				contador++;
				    codigosBind.put(new Integer(contador),actuacionesPendientes.trim());
				    consulta += " and upper(F_SIGA_ACTUACIONESDESIG(des.idinstitucion,des.idturno,des.anio,des.numero))=upper(:" + contador + ")";
			}
			}
			
			
			
			
			
			if (UtilidadesHash.getString(miHash,"CALIDAD") != null && !UtilidadesHash.getString(miHash,"CALIDAD").equalsIgnoreCase("")) {
				contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"CALIDAD").trim());
				consulta += " and (select count(1)"+
				"    from SCS_DEFENDIDOSDESIGNA def"+
				" where"+
				" def.ANIO = des.anio"+
				" and def.NUMERO = des.numero"+
				" and def.IDINSTITUCION = des.idinstitucion"+
				" and def.IDTURNO = des.idturno"+
				" and def.calidad = :" + contador+ ") > 0";
				
			}
			
//			aux += (((UtilidadesHash.getString(datos,"ANIO") == null)||(UtilidadesHash.getString(datos,"ANIO").equalsIgnoreCase("")))?"":" AND "+ComodinBusquedas.prepararSentenciaCompleta((String)UtilidadesHash.getString(datos,"ANIO"),"des.anio" ));
//			aux += (((UtilidadesHash.getString(datos,"NUMERO") == null)||(UtilidadesHash.getString(datos,"NUMERO").equalsIgnoreCase("")))?"":" and "+ComodinBusquedas.prepararSentenciaCompleta((String)UtilidadesHash.getString(datos,"NUMERO"),"des.numero" ));
			//aux += (((UtilidadesHash.getString(datos,"FECHAENTRADAINICIO") == null)||(UtilidadesHash.getString(datos,"FECHAENTRADAINICIO").equalsIgnoreCase("")))?"":" and des.fechaentrada > to_date('"+((String)UtilidadesHash.getString(datos,"FECHAENTRADAINICIO"))+"','DD/MM/YYYY')");
			//aux += (((UtilidadesHash.getString(datos,"FECHAENTRADAFIN") == null)||(UtilidadesHash.getString(datos,"FECHAENTRADAFIN").equalsIgnoreCase("")))?"":" and des.fechaentrada < to_date('"+((String)UtilidadesHash.getString(datos,"FECHAENTRADAFIN"))+"','DD/MM/YYYY')");
			if ((miHash.containsKey("FECHAENTRADAINICIO") && !UtilidadesHash.getString(miHash,"FECHAENTRADAINICIO").equalsIgnoreCase(""))
				||
				(miHash.containsKey("FECHAENTRADAFIN")&& !UtilidadesHash.getString(miHash,"FECHAENTRADAFIN").equalsIgnoreCase(""))
				){
				
				Vector vCondicion=GstDate.dateBetweenDesdeAndHastaBind("des.fechaentrada",
	                    GstDate.getApplicationFormatDate("",(String)UtilidadesHash.getString(miHash,"FECHAENTRADAINICIO").trim()),
						  GstDate.getApplicationFormatDate("",(String)UtilidadesHash.getString(miHash,"FECHAENTRADAFIN").trim()),
						  contador,
						  codigosBind);
				
				contador=new Integer(vCondicion.get(0).toString()).intValue();
				
				consulta +=" and " + vCondicion.get(1);
			}
			if((UtilidadesHash.getString(miHash,"IDTIPODESIGNACOLEGIO") != null)&&(!UtilidadesHash.getString(miHash,"IDTIPODESIGNACOLEGIO").equalsIgnoreCase(""))){
				contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"IDTIPODESIGNACOLEGIO").trim());
				consulta+=" and des.IDTIPODESIGNACOLEGIO =:"+contador;
			}
			
			boolean isFiltrado = false;
			String subConsulta1="";
			subConsulta1+=" AND (select count(1) from V_SIGA_DEFENDIDOS_DESIGNA VDEF";
			subConsulta1+=" where VDEF.idInstitucion = des.idinstitucion";
			subConsulta1+=" and VDEF.anio = des.anio";
			subConsulta1+=" and VDEF.numero = des.numero";
			subConsulta1+=" and VDEF.IDTURNO = des.idturno";
			if(UtilidadesHash.getString(miHash,"NIF") != null && !UtilidadesHash.getString(miHash,"NIF").equalsIgnoreCase("")){
				
				isFiltrado = true;
				subConsulta1+=" and ";
				if (ComodinBusquedas.hasComodin(miHash.get("NIF").toString())){	
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"NIF").trim(),"VDEF.NIF",contador,codigosBind);
				}else{
					contador++;
					subConsulta1 +=ComodinBusquedas.prepararSentenciaNIFBind(miHash.get("NIF").toString(),"VDEF.NIF",contador, codigosBind);
				}
				
				//
				
			}
			if(UtilidadesHash.getString(miHash,"NOMBRE") != null && !UtilidadesHash.getString(miHash,"NOMBRE").equalsIgnoreCase("")){
				isFiltrado = true;
				subConsulta1+=" and ";
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"NOMBRE").trim(),"VDEF.NOMBRE",contador, codigosBind);
			
				
				
			}
			if(UtilidadesHash.getString(miHash,"APELLIDO1") != null && !UtilidadesHash.getString(miHash,"APELLIDO1").equalsIgnoreCase("")){
				isFiltrado = true;
				subConsulta1+=" and ";
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"APELLIDO1").trim(),"VDEF.APELLIDO1",contador,codigosBind);
			

				
			}
			if(UtilidadesHash.getString(miHash,"APELLIDO2") != null && !UtilidadesHash.getString(miHash,"APELLIDO2").equalsIgnoreCase("")){
				
				isFiltrado = true;
				subConsulta1+=" and ";
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"APELLIDO2").trim(),"VDEF.APELLIDO2",contador,codigosBind);
			
				
			}
			
			subConsulta1+=" )>0 ";
			if (isFiltrado){
				consulta+=subConsulta1;
			}
			
			
			boolean tiene_juzg=UtilidadesHash.getString(miHash,"JUZGADOACTU") != null && !UtilidadesHash.getString(miHash,"JUZGADOACTU").equalsIgnoreCase("");
			boolean tiene_asunto=UtilidadesHash.getString(miHash,"ASUNTOACTUACION") != null && !UtilidadesHash.getString(miHash,"ASUNTOACTUACION").equalsIgnoreCase("");
			boolean tiene_acreditacion=UtilidadesHash.getString(miHash,"ACREDITACION") != null && !UtilidadesHash.getString(miHash,"ACREDITACION").equalsIgnoreCase("");
			boolean tiene_modulo=UtilidadesHash.getString(miHash,"MODULO") != null && !UtilidadesHash.getString(miHash,"MODULO").equalsIgnoreCase("");
			String subConsulta2="";
			if (tiene_juzg||tiene_asunto||tiene_acreditacion||tiene_modulo){
				subConsulta2+=	" and (des.idinstitucion, des.idturno, des.anio, des.numero) in"+
									" (select act.idinstitucion, act.idturno, act.anio, act.numero"+
									" from scs_actuaciondesigna act"+
									" where des.idinstitucion = act.idinstitucion"+
									" and des.idturno = act.idturno"+
									" and des.anio = act.anio"+
									" and des.numero = act.numero ";
				if (tiene_juzg) {
					String a[]=((String)UtilidadesHash.getString(miHash,"JUZGADOACTU")).split(",");
					contador++;
					codigosBind.put(new Integer(contador),a[0].trim());
					subConsulta2 += " AND act.idjuzgado = :" + contador;
				}
				if (tiene_asunto) {
					contador++;
					codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"ASUNTOACTUACION").trim());
					subConsulta2 += " AND act.numeroasunto = :" + contador;
				}
				if (tiene_acreditacion) {
		        	contador++;
					codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"ACREDITACION").trim());
					subConsulta2 += " AND act.idacreditacion = :" + contador;
				}
				if (tiene_modulo) {
					contador++;
					codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"MODULO").trim());
					subConsulta2 += " AND act.idprocedimiento = :" + contador;
				}
				subConsulta2+=")";
			}
			
			
			
			
			if (!subConsulta2.equals("")){
				consulta+=subConsulta2;
			}
			
			consulta+=" order by des.idturno, des.anio desc, des.numero desc";
      // No utilizamos la clase Paginador para la busqueda de letrados porque al filtrar por residencia la consulta no devolvia bien los 
      //  datos que eran de tipo varchar (devolvía n veces el mismo resultado), utilizamos el paginador PaginadorCaseSensitive
       // y hacemos a parte el tratamiento de mayusculas y signos de acentuación
       PaginadorBind paginador = new PaginadorBind(consulta,codigosBind);
        totalRegistros = paginador.getNumeroTotalRegistros();
 		
 		if (totalRegistros==0){					
 			paginador =null;
 		}
      
		
       
		
		return paginador;
		} 
		catch (Exception e) { 	
			throw new ClsExceptions(e,"Error obteniendo clientes colegiados"); 
		}
	}
	
	
	
	
	
	public Vector getProcuradorSalidaOficio (String idInstitucion, String numero, String turno, String anio) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), turno);
			h.put(new Integer(3), anio);
			h.put(new Integer(4), numero);

			String sql = "SELECT "+

			" PROC.NOMBRE || ' ' || PROC.APELLIDOS1 || ' ' || PROC.APELLIDOS2 AS PROCURADOR,"+
			" PROC.DOMICILIO as DOMICILIO_PROCURADOR,"+
			" PROC.IDPROVINCIA ID_PROVINCIA_PROCURADOR, PROC.IDPOBLACION AS ID_POBLACION_PROCURADOR,"+
			//-- as POBLACION_PROCURADOR
			//-- as PROVINCIA_PROCURADOR
			" PROC.CODIGOPOSTAL as CP_PROCURADOR,"+
			" PROC.TELEFONO1 as TELEFONO1_PROCURADOR,"+
			" TO_CHAR(DESPROC.FECHADESIGNA,'dd-mm-yyyy') as FECHADESIGNA_PROCURADOR"+
			" FROM SCS_DESIGNAPROCURADOR DESPROC, SCS_PROCURADOR PROC"+
			" WHERE DESPROC.IDINSTITUCION_PROC = PROC.IDINSTITUCION"+
			" AND DESPROC.IDPROCURADOR = PROC.IDPROCURADOR"+
			" AND TRUNC(DESPROC.FECHADESIGNA) <= SYSDATE"+
			" AND (DESPROC.FECHARENUNCIA IS NULL OR"+
			" TRUNC(DESPROC.FECHARENUNCIA) > SYSDATE)"+
			" AND DESPROC.IDINSTITUCION= :1   AND  DESPROC.IDTURNO= :2   AND DESPROC.ANIO= :3 " +
			"  AND  DESPROC.NUMERO= :4 ";
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de procuradores.");
		}
	}
	

	public Vector getLetradoSalidaOficio(String idPersona) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idPersona);
			String sql = "SELECT "+
			" P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' ||P.APELLIDOS2 AS NOMBRE_LETRADO,"+
			" P.NIFCIF AS NIF_LETRADO,"+
			//" P.SEXO AS SEXO_LETRADO,"+
			" DECODE(P.SEXO, null, null,'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_LETRADO_SINTRADUCIR"+
			" FROM CEN_PERSONA P"+
			" WHERE P.IDPERSONA= :1";
				
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getLetrado");
		}
	}
	public Vector getDireccionLetradoSalidaOficio(String idPersona,String idInstitucion) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			String sql = "SELECT "+
			" DIR.Domicilio DOMICILIO_LETRADO,dir.codigopostal CP_LETRADO ,dir.poblacionextranjera POBLACION_LETRADO,dir.idpoblacion ID_POBLACION_LETRADO, "+
			" dir.idprovincia ID_PROVINCIA_LETRADO,dir.telefono1 TELEFONODESPACHO_LET,dir.fax1 FAX_LETRADO,dir.correoelectronico EMAIL_LETRADO "+
			" from CEN_DIRECCIONES DIR, CEN_DIRECCION_TIPODIRECCION TIP " +
			" where dir.idinstitucion = tip.idinstitucion " +
			" and dir.idpersona = tip.idpersona  " +
			" and dir.iddireccion = tip.iddireccion " +
			" and tip.idtipodireccion = 2 " +
			" and dir.fechabaja is null "+
			" and dir.idinstitucion = :1 "+
			" and dir.idpersona = :2 "+
			" and rownum = 1 ";

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetrado");
		}
	}
	public Vector getDireccionPersonalLetradoSalidaOficio(String idPersona,String idInstitucion) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			String sql = "SELECT "+
			" dir.telefono1 TELEFONO1_LETRADO,dir.telefono2 TELEFONO2_LETRADO,dir.movil MOVIL_LETRADO "+

			" from CEN_DIRECCIONES DIR, CEN_DIRECCION_TIPODIRECCION TIP " +
			" where dir.idinstitucion = tip.idinstitucion " +
			" and dir.idpersona = tip.idpersona  " +
			" and dir.iddireccion = tip.iddireccion " +
			" and tip.idtipodireccion = 6 " +
			" and dir.fechabaja is null "+
			" and dir.idinstitucion = :1 "+
			" and dir.idpersona = :2 "+
			" and rownum = 1 ";

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las getDireccionPersonalLetrado");
		}
	}
	
	public Vector getActuacionDesignaSalidaOficio (String idInstitucion, String numero, String turno, String anio) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), turno);
			h.put(new Integer(3), anio);
			h.put(new Integer(4), numero);

			String sql = "SELECT "+

			" (DECODE(ACT.ANIO, NULL, null,ACT.ANIO || '/' || ACT.NUMERO || '/' || ACT.NUMEROASUNTO)) as NACTUACION,"+
			" PRO.NOMBRE AS PROCEDIMIENTO, "+
			//-- SI ES NULO HAY QUE COGER EL DE LA DESIGNACIÓN"+
			" TO_CHAR(ACT.FECHA, 'dd-mm-yyyy') AS FECHA_ACTUACION,"+
			" DECODE(ACT.IDPRISION, NULL, NULL,(SELECT P.NOMBRE FROM SCS_PRISION P"+
			" WHERE P.IDINSTITUCION = ACT.IDINSTITUCION_PRIS"+
			" AND P.IDPRISION = ACT.IDPRISION)) AS LUGAR " +
			//-- SI ES NULO SE BUSCA EL NOMBRE DEL JUZGADO DE LA DESIGNACION"+
			" FROM SCS_ACTUACIONDESIGNA ACT, SCS_PROCEDIMIENTOS PRO"+
			" WHERE ACT.IDINSTITUCION_PROC = PRO.IDINSTITUCION(+)"+
			" AND ACT.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO(+)"+
			" AND ACT.IDINSTITUCION = :1"+
			" AND ACT.IDTURNO = :2"+
			" AND ACT.ANIO = :3"+
			" AND ACT.NUMERO = :4"+
			" AND ROWNUM <2";
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de procuradores.");
		}
	}
	
	
	public Vector getDatosSalidaOficio (String idInstitucion, String idturno, String anio, String numero, String codigoDesigna, boolean isSolicitantes, String idPersonaJG) throws ClsExceptions  
	{	 
		Vector vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();	
		try {
			vSalida = new Vector();	
			Vector vDesigna = getDesignaSalidaOficio(idInstitucion,idturno, anio, numero, codigoDesigna); 
			//vSalida.addAll(vDesigna);
			//String juzgado = ;
			//String procedimiento = ;
			
			for (int j = 0; j < vDesigna.size(); j++) {
				Hashtable registro = (Hashtable) vDesigna.get(j);
				String numeroDesigna = (String)registro.get("NUMERO");
				String anioDesigna = (String)registro.get("ANIO");
				String idTurno  = (String)registro.get("IDTURNO");
				String idPersona  = (String)registro.get("IDPERSONA");
				if(numeroDesigna==null||numeroDesigna.trim().equals(""))
					throw new ClsExceptions("Excepcion Controlada. Seguramente no tenga letrado");
				
				//Metemos la descripcion del turno
				helperInformes.completarHashSalida(registro,helperInformes.getTurnoSalida(idInstitucion,idTurno));
				
				String idTipoDesigna  = (String)registro.get("IDTIPODESIGNACOLEGIO");
				//Metemos la descripcion del tipo de designa
				if(idTipoDesigna!=null && !idTipoDesigna.trim().equals(""))
					helperInformes.completarHashSalida(registro,helperInformes.getTipoDesignaColegio(idInstitucion,idTipoDesigna));
				else
					registro.put("DESC_TIPODESIGNA", " ");
				

				
				//metemos el numero de colegiado
				if(registro.containsKey("IDPERSONA") && registro.get("IDPERSONA")!=null && !((String)registro.get("IDPERSONA")).trim().equals("")){
					Hashtable htCodigo2 = new Hashtable();
					htCodigo2.put(new Integer(1), idInstitucion);
					htCodigo2.put(new Integer(2), (String)registro.get("IDPERSONA"));
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo2, "f_siga_calculoncolegiado", "NCOLEGIADO_LETRADO"));
				} else {
					registro.put("NCOLEGIADO_LETRADO", " ");
				}
				Hashtable htCodigo = new Hashtable();
				htCodigo.put(new Integer(1), idTurno);
				htCodigo.put(new Integer(2), idInstitucion);
				//metemos el nombre de partdo
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_NOMBRE_PARTIDO", "NOMBRE_PARTIDO"));
				htCodigo.put(new Integer(1), idInstitucion);
				htCodigo.put(new Integer(2), numeroDesigna);
				htCodigo.put(new Integer(3), idTurno);
				htCodigo.put(new Integer(4), anioDesigna);
				
				htCodigo.put(new Integer(5), "1");
				//metemos los delitos
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETDELITOS_DESIGNA", "DELITOS"));
				htCodigo.remove(new Integer(5));
				
				
				htCodigo.put(new Integer(2), idTurno);
				htCodigo.put(new Integer(3), anioDesigna);
				htCodigo.put(new Integer(4), numeroDesigna);
				
				//metemos los procuradores
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETPROCURADORCONT_DESIG", "PROCURADOR_CONTRARIOS"));

				//metemos los contrarios
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETCONTRARIOS_DESIGNA", "CONTRARIOS"));
				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETABOGADOCONTRARIO_DES", "ABOGADOS_CONTRARIOS"));
				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETREPRESENTANTE_DES", "REPRESENTANTES_CONTRARIOS"));
				
				
				
				
				htCodigo.put(new Integer(2), anioDesigna);
				htCodigo.put(new Integer(3), idTurno);
				htCodigo.put(new Integer(4), numeroDesigna);

				
				//metemos la lista actuaciones 
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETACTUACIONESDESIGNA", "LISTA_ACTUACIONES_DESIGNA"));
				//metemos la primera actuacion
				
				
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETFIRSTASISDESIGNA", "FECHA_ASISTENCIA"));
				
				
				//metemos los interesados de la designa
				htCodigo.put(new Integer(5), "0");
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETINTERESADOSDESIGNA", "LISTA_INTERESADOS_DESIGNA"));

				if(idPersona==null ||idPersona.trim().equalsIgnoreCase("")){
				    idPersona="-33"; // forzamos que no encuentre datos, en lugar de dar error
				}
				helperInformes.completarHashSalida(registro,getLetradoSalidaOficio(idPersona));
				String sexoLetrado  = (String)registro.get("SEXO_LETRADO_SINTRADUCIR");
				sexoLetrado = UtilidadesString.getMensajeIdioma(usrbean, sexoLetrado);
				registro.put("SEXO_LETRADO", sexoLetrado);
				helperInformes.completarHashSalida(registro,getDireccionLetradoSalidaOficio(idPersona,idInstitucion));
				helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalidaOficio(idPersona,idInstitucion));
				String pobLetrado = (String)registro.get("POBLACION_LETRADO");
				if(pobLetrado==null ||pobLetrado.trim().equalsIgnoreCase("")){
					String idPobLetrado = (String)registro.get("ID_POBLACION_LETRADO");
					helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetrado,"POBLACION_LETRADO"));
					String idProvLetrado = (String)registro.get("ID_PROVINCIA_LETRADO");
					if(idProvLetrado!=null && !idProvLetrado.equalsIgnoreCase(""))
						helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetrado,"PROVINCIA_LETRADO"));
					else
						UtilidadesHash.set(registro, "PROVINCIA_LETRADO", "");	
					
				}else{
					UtilidadesHash.set(registro, "PROVINCIA_LETRADO", "");
					
					
				}
				
				helperInformes.completarHashSalida(registro,getActuacionDesignaSalidaOficio(idInstitucion,numeroDesigna,idTurno,anioDesigna));
				
				String idJuzgado = (String)registro.get("IDJUZGADO");
				String idInstJuzgado = (String)registro.get("IDINSTITUCION_JUZG");
				
				if(idJuzgado==null ||idJuzgado.trim().equalsIgnoreCase("")){
				    idJuzgado="-33"; // forzamos que no encuentre datos, en lugar de dar error
				}
				helperInformes.completarHashSalida(registro,helperInformes.getJuzgadoSalida(idInstJuzgado,idJuzgado,""));
				
				String lugar = (String)registro.get("LUGAR");
				if(lugar==null || lugar.trim().equalsIgnoreCase("")){
					if(registro.get("JUZGADO")!=null && !((String)registro.get("JUZGADO")).trim().equals(""))
						registro.put("LUGAR", registro.get("JUZGADO"));
					else
						registro.put("LUGAR", "");
					
				}
				String procedimiento = (String)registro.get("PROCEDIMIENTO");

				if(procedimiento==null || procedimiento.trim().equalsIgnoreCase("")){
					String idProcedimiento = (String)registro.get("IDPROCEDIMIENTO");
					if(idProcedimiento==null || idProcedimiento.trim().equalsIgnoreCase("")){
					    idProcedimiento="-33"; // forzamos que no encuentre datos, en lugar de dar error
					}
					helperInformes.completarHashSalida(registro,helperInformes.getProcedimientoSalida(idInstitucion,idProcedimiento,""));
				}
				helperInformes.completarHashSalida(registro,getProcuradorSalidaOficio(idInstitucion,numeroDesigna,idTurno,anioDesigna));
				
				// RGG más descripciones 
				if(registro.containsKey("ID_POBLACION_PROCURADOR") && registro.get("ID_POBLACION_PROCURADOR")!=null && !((String)registro.get("ID_POBLACION_PROCURADOR")).trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida((String)registro.get("ID_POBLACION_PROCURADOR"), "POBLACION_PROCURADOR"));
				} else {
					registro.put("POBLACION_PROCURADOR", " ");
				}
				if(registro.containsKey("ID_PROVINCIA_PROCURADOR") && registro.get("ID_PROVINCIA_PROCURADOR")!=null && !((String)registro.get("ID_PROVINCIA_PROCURADOR")).trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida((String)registro.get("ID_PROVINCIA_PROCURADOR"), "PROVINCIA_PROCURADOR"));
				} else {
					registro.put("PROVINCIA_PROCURADOR", " ");
				}
				if(registro.containsKey("ID_POBLACION_JUZGADO") && registro.get("ID_POBLACION_JUZGADO")!=null && !((String)registro.get("ID_POBLACION_JUZGADO")).trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida((String)registro.get("ID_POBLACION_JUZGADO"), "POBLACION_JUZGADO"));
				} else {
					registro.put("POBLACION_JUZGADO", " ");
				}
				if(registro.containsKey("ID_PROVINCIA_JUZGADO") && registro.get("ID_PROVINCIA_JUZGADO")!=null && !((String)registro.get("ID_PROVINCIA_JUZGADO")).trim().equals("")){
					helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida((String)registro.get("ID_PROVINCIA_JUZGADO"), "PROVINCIA_JUZGADO"));
				} else {
					registro.put("PROVINCIA_JUZGADO", " ");
				}
				//Sacamos el tipo ejg colegio (@cat)
				helperInformes.completarHashSalida(registro, getTipoEJGColegioDesigna(idTurno, numeroDesigna, anioDesigna, idInstitucion, usrbean.getLanguage().toString()));
				
				//Sacamos los datos de la ultima Actuacion
				helperInformes.completarHashSalida(registro,getUltimaActuacionDesignaSalida(idInstitucion,numeroDesigna,idTurno,anioDesigna));
				if(registro.containsKey("NUMASUNTO_UA") && registro.get("NUMASUNTO_UA")!=null && !((String)registro.get("NUMASUNTO_UA")).trim().equals("")){
					String idProcedimientoUA =(String)registro.get("IDPROCEDIMIENTO_UA");
					helperInformes.completarHashSalida(registro,helperInformes.getProcedimientoSalida(idInstitucion, idProcedimientoUA,"UA"));
					
					String idJuzgadoUA =(String)registro.get("IDJUZGADO_UA");
					helperInformes.completarHashSalida(registro,helperInformes.getJuzgadoSalida(idInstitucion, idJuzgadoUA,"UA"));
					
					if(registro.containsKey("ID_POBLACION_JUZGADO_UA") && registro.get("ID_POBLACION_JUZGADO_UA")!=null && !((String)registro.get("ID_POBLACION_JUZGADO_UA")).trim().equals("")){
						helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida((String)registro.get("ID_POBLACION_JUZGADO_UA"), "POBLACION_JUZGADO_UA"));
					} else {
						registro.put("POBLACION_JUZGADO_UA", " ");
					}
				}else{
					
					registro.put("IDJUZGADO_UA", " ");
					registro.put("JUZGADO_UA", " ");
					registro.put("DIR_JUZGADO_UA", " ");
					registro.put("CP_JUZGADO_UA", " ");
					registro.put("ID_PROVINCIA_JUZGADO_UA", " ");
					registro.put("ID_POBLACION_JUZGADO_UA", " ");
					registro.put("POBLACION_JUZGADO_UA", " ");
					registro.put("IDPROCEDIMIENTO_UA", " ");
					registro.put("PROCEDIMIENTO_UA", " ");
					
					


					
					

					
				}
				
				
				
				
				if(isSolicitantes){
					Vector vDefendidos = getDefendidosDesignaSalidaOficio(idInstitucion,numeroDesigna,idTurno,anioDesigna,idPersonaJG);
					if(vDefendidos!=null && vDefendidos.size()>0){
						for (int k = 0; k < vDefendidos.size(); k++) {
							Hashtable clone = (Hashtable) registro.clone();
							Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);
							clone.putAll(registroDefendido);
							vSalida.add(clone);
						}
					}else{
						vSalida.add(registro);
						
					}	
				
				}else{
					
					vSalida.add(registro);
				}
				
			}
			

		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosSalidaOficio");
		}
		return vSalida;
		
		
		
	}
	
	
	public Vector getDesignaSalidaOficio (String idInstitucion, String idturno, String anio, String numero, String codigoDesigna) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idInstitucion);
			h.put(new Integer(3), idInstitucion);
			//h.put(new Integer(4), codigoDesigna);
			h.put(new Integer(4), idturno);
			h.put(new Integer(5), anio);
			h.put(new Integer(6), numero);

			String sql = "SELECT "+

			" DES.IDINSTITUCION, DES.IDTURNO,  DES.ANIO, DES.NUMERO,"+
			" LET.IDPERSONA, DES.IDINSTITUCION_JUZG, DES.IDJUZGADO,"+
			" DES.OBSERVACIONES AS OBSERVACIONES,"+
			" DES.NUMPROCEDIMIENTO AS AUTOS,"+
			" TO_CHAR(DES.FECHAJUICIO, 'dd/MM/yyyy') AS FECHA_JUICIO,"+
			" TO_CHAR(DES.FECHAJUICIO, 'HH24:MI') AS HORA_JUICIO,"+
			" DES.ANIO AS ANIO_DESIGNA,"+
			" DES.CODIGO AS CODIGO,"+
			" DES.RESUMENASUNTO AS ASUNTO,"+
			" DES.IDPROCEDIMIENTO  AS IDPROCEDIMIENTO,"+
			" DES.ANIO || '/' || DES.CODIGO AS NOFICIO,"+
			" TO_CHAR(DES.FECHAENTRADA, 'dd-mm-yyyy') AS FECHA_DESIGNA,"+
			" TO_CHAR(SYSDATE, 'dd') AS DIA_ACTUAL,"+
			" TO_CHAR(SYSDATE, 'MONTH', 'NLS_DATE_LANGUAGE = SPANISH') AS MES_ACTUAL,"+
			" TO_CHAR(SYSDATE, 'yyyy') AS ANIO_ACTUAL"+
			
			" ,DES.idtipodesignacolegio AS IDTIPODESIGNACOLEGIO, "+
			" TO_CHAR(DES.FECHARECEPCIONCOLEGIO, 'dd/MM/yyyy') AS FECHA_RECEPCION_COLEGIO,"+
			" TO_CHAR(DES.FECHAOFICIOJUZGADO, 'dd/MM/yyyy') AS FECHA_OFICIO_JUZGADO "+
		    
		    //-- campos calculados en el recorrido
		    
		    //--DES.TURNO_DESCRIPCION AS DESCRIPCION_TURNO,
		    //--DES.ABREV_TURNO AS ABREV_TURNO,       
		    //--F_SIGA_NOMBRE_PARTIDO(DES.IDTURNO, DES.IDINSTITUCION) as NOMBRE_PARTIDO,
		    /*F_SIGA_GETDELITOS_DESIGNA(DES.IDINSTITUCION,
		                                       DES.NUMERO,
		                                       DES.IDTURNO,
		                                       DES.ANIO,
		                                       1) AS DELITOS,*/
		    /*F_SIGA_GETCONTRARIOS_DESIGNA(DES.IDINSTITUCION,
		                                          DES.IDTURNO,
		                                          DES.ANIO,
		                                          DES.NUMERO) AS CONTRARIOS,
		      F_SIGA_GETPROCURADORCONT_DESIG(DES.IDINSTITUCION,
		                                            DES.IDTURNO,
		                                            DES.ANIO,
		                                            DES.NUMERO) AS PROCURADOR_CONTRARIOS,*/  
		    /*F_SIGA_GETINTERESADOSDESIGNA(DES.IDINSTITUCION,
		                                          DES.ANIO,
		                                          DES.IDTURNO,
		                                          DES.NUMERO,
		                                          0) LISTA_INTERESADOS_DESIGNA,
		             F_SIGA_GETACTUACIONESDESIGNA(DES.IDINSTITUCION,
		                                          DES.ANIO,
		                                          DES.IDTURNO,
		                                          DES.NUMERO) LISTA_ACTUACIONES_DESIGNA,*/
		     /* F_SIGA_GETFIRSTASISDESIGNA(DES.IDINSTITUCION,
		                                        DES.ANIO,
		                                        DES.IDTURNO,
		                                        DES.NUMERO) AS FECHA_ASISTENCIA*/                                                                                     

			" FROM SCS_DESIGNA DES, SCS_DESIGNASLETRADO LET"+
			" WHERE :1 = LET.IDINSTITUCION(+)"+
			" AND DES.IDTURNO = LET.IDTURNO(+)"+
			" AND DES.ANIO = LET.ANIO(+)"+
			" AND DES.NUMERO = LET.NUMERO(+)"+
			" AND (LET.FECHADESIGNA IS NULL OR"+
			" let.idpersona = F_SIGA_GETIDLETRADO_DESIGNA(:2,des.idTurno,des.anio,des.NUMERO))" +
			/*" LET.FECHADESIGNA = (SELECT MAX(LET2.FECHADESIGNA)"+
			" FROM SCS_DESIGNASLETRADO LET2"+
			" WHERE :2 = LET2.IDINSTITUCION"+
			" AND LET.IDTURNO = LET2.IDTURNO"+
			" AND LET.ANIO = LET2.ANIO"+
			" AND LET.NUMERO = LET2.NUMERO"+
			" AND TRUNC(LET2.FECHADESIGNA) <= TRUNC(SYSDATE)))"+*/
			" AND des.IDINSTITUCION = :3"+
			" AND des.IDTURNO = :4" +
			" AND des.ANIO = :5" +
			" AND des.NUMERO = :6" ;
			

			//" AND des.CODIGO = :4";
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
			//return this.selectGenericoBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de procuradores.");
		}
	}
	
	public Vector getDefendidosDesignaSalidaOficio (String idInstitucion, String numero, 
			String idTurno, String anio, String idPersonaJG) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), anio);
			h.put(new Integer(3), idTurno);
			h.put(new Integer(4), numero);
			h.put(new Integer(5), idInstitucion);
			h.put(new Integer(6), anio);
			h.put(new Integer(7), idTurno);
			h.put(new Integer(8), numero);
			h.put(new Integer(9), this.usrbean.getLanguage());
			h.put(new Integer(10), this.usrbean.getLanguage());
			h.put(new Integer(11), idInstitucion);
			h.put(new Integer(12), anio);
			h.put(new Integer(13), idTurno);
			h.put(new Integer(14), numero);
			if (idPersonaJG!=null && !idPersonaJG.trim().equals("")) {
				h.put(new Integer(15), idPersonaJG);
			}
			String sql = "SELECT INTERESADO.IDINSTITUCION,"+
				" INTERESADO.IDTURNO,   INTERESADO.ANIO,   INTERESADO.NUMERO,"+
				" DECODE((select count(EJGDES1.idinstitucion) from SCS_EJGDESIGNA EJGDES1"+
				" where EJGDES1.IDINSTITUCION = :1"+
				" and EJGDES1.ANIODESIGNA = :2"+
				" and EJGDES1.IDTURNO = :3"+
				" and EJGDES1.NUMERODESIGNA = :4),1,"+
				" (select EJGDES.ANIOEJG || '/' || ejg.NUMEJG"+
				" from scs_ejg ejg, scs_ejgdesigna ejgdes"+
				" where ejg.anio = ejgdes.anioejg"+
				" and ejg.numero = ejgdes.numeroejg"+
				" and ejg.idinstitucion = ejgdes.idinstitucion"+
				" and ejg.idtipoejg = ejgdes.idtipoejg"+
				" AND ejgdes.IDINSTITUCION = :5"+
				" and ejgdes.ANIODESIGNA = :6"+
				" and ejgdes.IDTURNO = :7"+
				" and ejgdes.NUMERODESIGNA = :8),"+
				" DECODE(INTERESADO.ANIOEJG, NULL,'0'," +
				"INTERESADO.ANIOEJG || '/' || INTERESADO.NUMEJG)) AS NUMERO_EJG,"+
				" INTERESADO.NOMBRE || ' ' || INTERESADO.APELLIDO1 || ' ' ||"+
				" INTERESADO.APELLIDO2 AS NOMBRE_DEFENDIDO,"+
				" INTERESADO.NIF AS NIF_DEFENDIDO,"+
				
				" INTERESADO.DIRECCION AS DOMICILIO_DEFENDIDO,"+
				" INTERESADO.CODIGOPOSTAL AS CP_DEFENDIDO,"+
				" INTERESADO.NOMBRE_POB AS POBLACION_DEFENDIDO,"+
				" INTERESADO.NOMBRE_PROV AS PROVINCIA_DEFENDIDO,"+
				" INTERESADO.TELEFONO AS TELEFONO1_DEFENDIDO,"+
				" f_siga_getrecurso_etiqueta(DECODE(INTERESADO.SEXO,  null,  null,  'M','gratuita.personaEJG.sexo.mujer',"+
				" 'gratuita.personaEJG.sexo.hombre'),:9) AS SEXO_DEFENDIDO,"+
				" INTERESADO.IDLENGUAJE AS IDLENGUAJE_DEFENDIDO," +
				" f_siga_getrecurso_etiqueta(DECODE(INTERESADO.CALIDAD,"+
				" null,   '', 'D', 'gratuita.personaJG.calidad.literal.demandante',"+
				" 'gratuita.personaJG.calidad.literal.demandado'),:10)"+
				"  AS CALIDAD_DEFENDIDO,"+
				" INTERESADO.OBSERVACIONES AS OBS_INTERESADO,"+
				" INTERESADO.OBSERVACIONES AS OBS_DEFENDIDO,"+
				" F_SIGA_GETCODIDIOMA(INTERESADO.IDLENGUAJE) AS CODIGOLENGUAJE,"+
				" INTERESADO.FECHARESOLUCIONCAJG AS FECHARESOLUCIONCAJG"+

				"   FROM V_SIGA_INTERESADOS_DESIGNA    INTERESADO"+
				" WHERE INTERESADO.IDINSTITUCION = :11"+
				" and INTERESADO.ANIO = :12"+
				" and INTERESADO.IDTURNO = :13"+
				" and INTERESADO.NUMERO = :14";
				if (idPersonaJG!=null && !idPersonaJG.trim().equals("")) {
					sql+= " and INTERESADO.IDPERSONAJG = :15";
				}
				HelperInformesAdm helperInformes = new HelperInformesAdm();	
				return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDefendidosDesignaSalidaOficio.");
		}
	}
	
	
	public void anularDesigna(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{
		// TODO Auto-generated method stub
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		HttpSession ses = request.getSession();
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.usrbean);
		ScsActuacionDesignaAdm actuacionAdm = new ScsActuacionDesignaAdm(this.usrbean);
		ScsDesignaBean designaBean = new ScsDesignaBean();
		ScsActuacionDesignaBean actuacionDesignaBean = new ScsActuacionDesignaBean();
		Hashtable datosEntrada;
		
		try {			
			datosEntrada = (Hashtable)miform.getDatos();
			// RECOGEMOS LOS CAMPOS CLAVES PARA LA ACTUALIZACION DE LA TABLA SCS_DESIGNA
			String anio=(String)datosEntrada.get("ANIO");
			String numero = (String)datosEntrada.get("NUMERO");
			String idTurno = (String)datosEntrada.get("IDTURNO");
			String fechaAnulacion = "SYSDATE";
			String idInstitucion = usr.getLocation();	
			String Codigo= (String)datosEntrada.get("CODIGO");	
			String Sufijo= (String)datosEntrada.get("SUFIJO");	
			String contador="";
			 if (Sufijo!=null && !Sufijo.equals("")){ 
					 contador=Codigo+"-"+Sufijo;
			 }else{
				 	 contador=Codigo;
			 }
						
			
			// Obtenemos las actuaciones para esa designa. Con cada una comprobaremos si no esta facturada ni
			// anulada. Si es asi la anularemos			
			String whereActuacion = " where "   +ScsActuacionDesignaBean.C_ANIO+"="+anio+
									"   and "   +ScsActuacionDesignaBean.C_IDINSTITUCION+"="+idInstitucion+
			 						"   and "   +ScsActuacionDesignaBean.C_NUMERO+"="+numero+
			 						"   and "   +ScsDesignaBean.C_IDTURNO+"="+(String)datosEntrada.get("IDTURNO")+" ";
			
			Vector actuaciones = actuacionAdm.select(whereActuacion);
			for(int i=0;i<actuaciones.size();i++)
			{
				actuacionDesignaBean = (ScsActuacionDesignaBean)actuaciones.elementAt(i);
				if((actuacionDesignaBean.getFacturado() == null || actuacionDesignaBean.getFacturado().trim().equals(""))&& actuacionDesignaBean.getAnulacion().intValue() !=new Integer(ClsConstants.DB_TRUE).intValue())
				{
					actuacionDesignaBean.setAnulacion(new Integer(1));
					String anulacion[] = new String[1];
					anulacion[0]=ScsActuacionDesignaBean.C_ANULACION;
					if(!actuacionAdm.updateDirect(actuacionDesignaBean,actuacionAdm.getClavesBean(),anulacion))
						throw new ClsExceptions(actuacionAdm.getError());
					
				}
			}
			
			// Comprobamos que se quiera compensar o no por la anulacion de la designacion
			String compensar = request.getParameter("compensar");
			if (compensar.equalsIgnoreCase("1")){ // En caso de que SI se quiera compensar al letrado
				ScsSaltosCompensacionesAdm  compensacionAdm  = new ScsSaltosCompensacionesAdm(this.usrbean);
				ScsSaltosCompensacionesBean compensacionBean = new ScsSaltosCompensacionesBean();
				
				// Obetenemos el letrado asociado a la designa
				String idLetrado = getIDLetradoDesig(idInstitucion, idTurno, anio, numero);
				
				// Asignamos los datos al bean de la compensacion
				compensacionBean.setIdInstitucion(Integer.valueOf(idInstitucion));
				compensacionBean.setIdTurno(Integer.valueOf(idTurno));
				compensacionBean.setIdSaltosTurno(Long.valueOf(compensacionAdm.getNuevoIdSaltosTurno(idInstitucion, idTurno)));
				compensacionBean.setFecha("SYSDATE");
				compensacionBean.setIdPersona(new Long(idLetrado));
				String mensaje=UtilidadesString.getMensajeIdioma((String)usr.getLanguage(),"gratuita.compensacion.motivo");
				 mensaje=mensaje+".\n"+ UtilidadesString.getMensajeIdioma(usr,
					"gratuita.literal.numeroDesignacion")+": "+ contador; 
				//compensacionBean.setMotivos(UtilidadesString.getMensajeIdioma((String)usr.getLanguage(),"gratuita.compensacion.motivo"));
				 compensacionBean.setMotivos(mensaje);
				compensacionBean.setSaltoCompensacion("C");
				compensacionBean.setUsuMod(this.usuModificacion);
				compensacionBean.setFechaMod("SYSDATE");
				
				// Insertamos la nueva compensacion en la base de datos
				if (!compensacionAdm.insert(compensacionBean)){
					throw new ClsExceptions(compensacionAdm.getError());
				}
				
			}
		} catch(ClsExceptions e){
			throw e; 
		} catch(Exception e){
			throw new ClsExceptions(e,e.toString()); 
		}		
	}

	public void desAnularDesigna(ActionMapping mapping, MasterForm formulario, HttpServletRequest request, HttpServletResponse response) throws ClsExceptions, SIGAException{
		// TODO Auto-generated method stub
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		HttpSession ses = request.getSession();
		MaestroDesignasForm miform = (MaestroDesignasForm)formulario;
		ScsDesignaAdm designaAdm = new ScsDesignaAdm (this.usrbean);
		ScsActuacionDesignaAdm actuacionAdm = new ScsActuacionDesignaAdm(this.usrbean);
		ScsDesignaBean designaBean = new ScsDesignaBean();
		ScsActuacionDesignaBean actuacionDesignaBean = new ScsActuacionDesignaBean();
		Hashtable datosEntrada;
		
		try {			
			datosEntrada = (Hashtable)miform.getDatos();
			// RECOGEMOS LOS CAMPOS CLAVES PARA LA ACTUALIZACION DE LA TABLA SCS_DESIGNA
			String anio=(String)datosEntrada.get("ANIO");
			String numero = (String)datosEntrada.get("NUMERO");
			String idTurno = (String)datosEntrada.get("IDTURNO");
			String fechaAnulacion = "SYSDATE";
			String idInstitucion = usr.getLocation();	
				
			
			// Obtenemos las actuaciones para esa designa. Con cada una comprobaremos si no esta facturada ni
			// anulada. Si es asi la anularemos			
			String whereActuacion = " where "   +ScsActuacionDesignaBean.C_ANIO+"="+anio+
									"   and "   +ScsActuacionDesignaBean.C_IDINSTITUCION+"="+idInstitucion+
			 						"   and "   +ScsActuacionDesignaBean.C_NUMERO+"="+numero+
			 						"   and "   +ScsDesignaBean.C_IDTURNO+"="+(String)datosEntrada.get("IDTURNO")+" ";
			
			Vector actuaciones = actuacionAdm.select(whereActuacion);
			for(int i=0;i<actuaciones.size();i++)
			{
				actuacionDesignaBean = (ScsActuacionDesignaBean)actuaciones.elementAt(i);
				if((actuacionDesignaBean.getFacturado() == null || actuacionDesignaBean.getFacturado().trim().equals(""))&& actuacionDesignaBean.getAnulacion().intValue() !=new Integer(ClsConstants.DB_FALSE).intValue())
				{
					actuacionDesignaBean.setAnulacion(new Integer(0));
					String anulacion[] = new String[1];
					anulacion[0]=ScsActuacionDesignaBean.C_ANULACION;
					if(!actuacionAdm.updateDirect(actuacionDesignaBean,actuacionAdm.getClavesBean(),anulacion))
						throw new ClsExceptions(actuacionAdm.getError());
					
				}
			}
		} catch(ClsExceptions e){
			throw e; 
		} catch(Exception e){
			throw new ClsExceptions(e,e.toString()); 
		}		
	}
	/**
	 * getUltimaActuacionDesignaSalida: Metodo que obtiene los campos necesarios para
	 * la consulta de oficio de la ultima actuacion metida para la designa
	 * @param idInstitucion
	 * @param numero
	 * @param turno
	 * @param anio
	 * @return
	 * @throws ClsExceptions
	 */

	public Vector getUltimaActuacionDesignaSalida (String idInstitucion, String numero, String turno, String anio) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), turno);
			h.put(new Integer(3), anio);
			h.put(new Integer(4), numero);
			String sql = "select * from (select Scs_actuaciondesigna.numeroasunto NUMASUNTO_UA, " +
					" Scs_actuaciondesigna.idjuzgado IDJUZGADO_UA, " +
					" to_char(Scs_actuaciondesigna.fecha,'dd-mm-yyyy') FECHA_UA, " +
					" Scs_actuaciondesigna.idprocedimiento IDPROCEDIMIENTO_UA, " +
					" Scs_actuaciondesigna.observaciones OBSERVACIONES_UA, " +
					" to_char(Scs_actuaciondesigna.Fechajustificacion,'dd-mm-yyyy') FECHA_JUSTIF_UA, " +
					" Scs_actuaciondesigna.observacionesjustificacion OBSERVACIONES_JUSTIF_UA " +
					" from scs_actuaciondesigna " +
					" WHERE scs_actuaciondesigna.IDINSTITUCION = :1 " +
					" AND scs_actuaciondesigna.idturno = :2 " +
					" AND scs_actuaciondesigna.anio = :3 " +
					" AND scs_actuaciondesigna.numero = :4 " +
					" ORDER BY FECHA desc) " +
					" WHERE ROWNUM=1";
			
				
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener getUltimaActuacionDesignaSalida.");
		}
	}
	
	/**
	 * @param institucion
	 * @param tipoEJG
	 * @param anio
	 * @param numero
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public String getFechaEntrada (String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions,SIGAException {
		String fechaApertura = "";
		try {
            RowsContainer rc = new RowsContainer(); 

		    Hashtable codigos = new Hashtable();
		    int contador=0;
		    
            String sql ="SELECT des." + ScsDesignaBean.C_FECHAENTRADA +
                      " FROM SCS_DESIGNA des ";
      	            contador++;
    				codigos.put(new Integer(contador),idInstitucion);
    	            sql += " WHERE des.IDINSTITUCION =:"+contador;
    	            contador++;
    				codigos.put(new Integer(contador),idTurno);
    	            sql += "  and des.IDTURNO =:" +contador;
    	            contador++;
    				codigos.put(new Integer(contador),anio);
    	            sql += "  and des.ANIO =:" + contador;
    	            contador++;
    				codigos.put(new Integer(contador),numero);
    	            sql += "  and des.NUMERO =:" + contador ;
													
            if (rc.findBind(sql,codigos)) {
               for (int i = 0; i < rc.size(); i++){
                  Row fila = (Row) rc.get(i);
                  Hashtable resultado=fila.getRow();	                  
                  fechaApertura = (String)resultado.get(ScsDesignaBean.C_FECHAENTRADA);
               }
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta.");
       }
		return fechaApertura;
	}
	
	
	/*Devuelve la clave de la asistencia en caso de que este creado o relacionado con una asistencia*/
	public Hashtable procedeDeAsistencia(String idturno,String numero, String anio){
		
		//ScsDefinirSOJAdm admBean =  new ScsDefinirSOJAdm(this.getUserBean(request));
		
		Vector v=new Vector();
		Hashtable h=new Hashtable();
		
		String sql="select a.anio as ASIANIO, a.numero as ASINUMERO" +
				"  from scs_asistencia a" +
				" where a.designa_anio = "+anio+
				"   and a.designa_numero = "+ numero +
				"   and a.designa_turno ="+ idturno +
				"   and a.idinstitucion = "+ this.usrbean.getLocation();

		try {
			v=super.selectGenerico(sql);
			if(v.size()>0)
				h=(Hashtable)v.get(0);
		} catch (ClsExceptions e) {
			e.printStackTrace();
		} 
		catch (SIGAException e1) {
			e1.printStackTrace();
		}
		
		return h;
	}
	
	public Vector getTipoEJGColegioDesigna(String idturno, String numero, String anio, String idinstitucion, String idioma) throws ClsExceptions{
		Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
		        StringBuffer sql = new StringBuffer();
		        
		        sql.append("select F_SIGA_GETRECURSO(tec.descripcion, "+idioma+") as TIPO_EJG_COLEGIO");
		        sql.append(" from scs_designa des, scs_ejg ejg, scs_ejgdesigna edes, scs_tipoejgcolegio tec ");
		        sql.append(" where des.idinstitucion = ejg.idinstitucion ");
		        sql.append(" and des.idinstitucion=edes.idinstitucion ");
		        sql.append(" and des.idinstitucion = " + idinstitucion);
		        sql.append(" and des.anio = " + anio);
		        sql.append(" and des.idturno = " + idturno);
		        sql.append(" and des.numero = " + numero);
		        sql.append(" and des.numero = edes.numerodesigna ");
		        sql.append(" and des.idturno = edes.idturno ");
		        sql.append(" and ejg.numero = edes.numeroejg ");
		        sql.append(" and ejg.idtipoejg = edes.idtipoejg ");
		        sql.append(" and tec.idinstitucion = ejg.idinstitucion ");
		        sql.append(" and tec.idtipoejgcolegio=ejg.idtipoejgcolegio");
		        if (rc.find(sql.toString())) {
		               for (int i = 0; i < rc.size(); i++){
		                  Row fila = (Row) rc.get(i);
		                  Hashtable resultado=fila.getRow();	                  
		                  datos.add(resultado);
		               }
		            } 
		       }
		       catch (Exception e) {
		       		throw new ClsExceptions (e, "Error al obtener la informacion sobre el tipo ejg colegio de una designa.");
		       }
		       return datos;      
	}
	
	
	  public void compensacionDesigna(HttpServletRequest request, String anio,String codigo, String numero, String idTurno, String idinstitucion) throws ClsExceptions, SIGAException{
		// TODO Auto-generated method stub
		UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
		HttpSession ses = request.getSession();   
		
		try {	
		
			// Comprobamos que se quiera compensar o no por la anulacion de la designacion
			  String compensar = request.getParameter("compensar");			
				ScsSaltosCompensacionesAdm  compensacionAdm  = new ScsSaltosCompensacionesAdm(this.usrbean);
				ScsSaltosCompensacionesBean compensacionBean = new ScsSaltosCompensacionesBean();				
				// Obetenemos el letrado asociado a la designa
				String idLetrado = getIDLetradoDesig(idinstitucion, idTurno, anio, numero);				
				// Asignamos los datos al bean de la compensacion
				compensacionBean.setIdInstitucion(Integer.valueOf(idinstitucion));
				compensacionBean.setIdTurno(Integer.valueOf(idTurno));
				compensacionBean.setIdSaltosTurno(Long.valueOf(compensacionAdm.getNuevoIdSaltosTurno(idinstitucion, idTurno)));
				compensacionBean.setFecha("SYSDATE");
				compensacionBean.setIdPersona(new Long(idLetrado));
				String mensaje=UtilidadesString.getMensajeIdioma((String)usr.getLanguage(),"gratuita.compensacion.motivoDesignacion");
				mensaje=mensaje+".\n"+ UtilidadesString.getMensajeIdioma(usr,
					"gratuita.literal.numeroDesignacion")+": "+ codigo;
				compensacionBean.setMotivos(mensaje);
				//compensacionBean.setMotivos(UtilidadesString.getMensajeIdioma((String)usr.getLanguage(),"gratuita.compensacion.motivoDesignacion"));
				compensacionBean.setSaltoCompensacion("C");
				compensacionBean.setUsuMod(this.usuModificacion);
				compensacionBean.setFechaMod("SYSDATE");				
				// Insertamos la nueva compensacion en la base de datos
				if (!compensacionAdm.insert(compensacionBean)){
					throw new ClsExceptions(compensacionAdm.getError());
				}				
			
		} catch(ClsExceptions e){
			throw e; 
		} catch(Exception e){
			throw new ClsExceptions(e,e.toString()); 
		}		
		
	  }
	
}