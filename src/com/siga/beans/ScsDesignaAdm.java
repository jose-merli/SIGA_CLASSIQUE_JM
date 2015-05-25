
package com.siga.beans;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.struts.action.ActionMapping;
import org.codehaus.jackson.map.ObjectMapper;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesBDAdm;
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
	 * @param usuario Usuario "logado" en la aplicaci�n. De tipo "Integer".  
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
							ScsDesignaBean.C_FECHARECEPCIONCOLEGIO, ScsDesignaBean.C_ART27,
							ScsDesignaBean.C_FECHAALTA, 			ScsDesignaBean.C_NIG,
							ScsDesignaBean.C_ANIOPROCEDIMIENTO};
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
	 *  @return bean con la informaci�n de la hashtable
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
			bean.setFechaAlta(UtilidadesHash.getString(hash,ScsDesignaBean.C_FECHAALTA));
			bean.setArt27(UtilidadesHash.getString(hash,ScsDesignaBean.C_ART27));
			bean.setNIG(UtilidadesHash.getString(hash,ScsDesignaBean.C_NIG));
			bean.setAnioProcedimiento(UtilidadesHash.getInteger(hash,ScsDesignaBean.C_ANIOPROCEDIMIENTO));
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
									" des." + ScsDesignasLetradoBean.C_IDINSTITUCIONORIGEN +", "+
									" f_siga_calculoncolegiado(des." + ScsDesignasLetradoBean.C_IDINSTITUCIONORIGEN + ", des." + ScsDesignasLetradoBean.C_IDPERSONA +") as ncolegiadoorigen,"+
									" f_siga_calculoncolegiado(des." + ScsDesignasLetradoBean.C_IDINSTITUCION + ", des." + ScsDesignasLetradoBean.C_IDPERSONA +") as ncolegiado,"+
									" f_siga_gettipocliente(des." + ScsDesignasLetradoBean.C_IDPERSONA + ", des." + ScsDesignasLetradoBean.C_IDINSTITUCION +",sysdate) as datoscolegiales,"+
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
								// Se comenta esta condici�n porque el sistema permite crear designas posteriores al sysdate 
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
	 *  @return hashtable con la informaci�n del bean
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
			UtilidadesHash.set(hash, ScsDesignaBean.C_FECHAALTA,b.getFechaAlta());
			UtilidadesHash.set(hash, ScsDesignaBean.C_ART27,b.getArt27());
			UtilidadesHash.set(hash, ScsDesignaBean.C_NIG,b.getNIG());
			UtilidadesHash.set(hash, ScsDesignaBean.C_ANIOPROCEDIMIENTO ,b.getAnioProcedimiento());
			return hash;
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deber� ordenar la select
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
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparaci�n consiste en calcular el
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
			
			UtilidadesHash.set(entrada, ScsDesignaBean.C_FECHAESTADO,UtilidadesBDAdm.getFechaBD(""));
			
			String sJuzgado=((String)entrada.get("JUZGADO"));
			if (sJuzgado!=null && !sJuzgado.equals("")) {
				String sIdJuzgado = "";
				String sIdInstitucionJuzgado = "";
				if (sJuzgado.startsWith("{")){
					// ES UN JSON
					HashMap<String, String> hmIdJuzgadoObtenido = new ObjectMapper().readValue(sJuzgado, HashMap.class);
					sIdJuzgado = hmIdJuzgadoObtenido.get("idjuzgado");
					if(sIdJuzgado == null)
						sIdJuzgado = hmIdJuzgadoObtenido.get("idjuzgadosel");
					sIdInstitucionJuzgado = hmIdJuzgadoObtenido.get("idinstitucion");
				} else if (!sJuzgado.equals("")){
					String[] juzgado =sJuzgado.split(",");
					sIdJuzgado = juzgado[0];
					sIdInstitucionJuzgado = juzgado[1];
				}
				if (sIdJuzgado!=null && !sIdJuzgado.equals("")){
					entrada.put(ScsDesignaBean.C_IDJUZGADO, sIdJuzgado);
					entrada.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, sIdInstitucionJuzgado);
				} else {
					entrada.put(ScsDesignaBean.C_IDJUZGADO, "");
					entrada.put(ScsDesignaBean.C_IDINSTITUCIONJUZGADO, "");
				}	
				entrada.remove("JUZGADO");
			} 
			String idPretension =  ((String)entrada.get("IDPRETENSION"));
			if (idPretension!=null && !idPretension.equals("")) {
				entrada.put(ScsDesignaBean.C_IDPRETENSION, idPretension);
			}
			
			String idModulo =  ((String)entrada.get("IDPROCEDIMIENTO"));
			if (idModulo!=null && !idModulo.equals("")) {
				String sIdModulo = "";
				if (idModulo.startsWith("{")){
					// ES UN JSON
					HashMap<String, String> hmIdJuzgadoObtenido = new ObjectMapper().readValue(idModulo, HashMap.class);
					sIdModulo = hmIdJuzgadoObtenido.get("idprocedimiento");
				} else{
					sIdModulo = idModulo;
				}
				entrada.put(ScsDesignaBean.C_IDPROCEDIMIENTO, sIdModulo);

			}			
			
			
			
			if(entrada.get("PROCEDIMIENTO")!=null&& !(entrada.get("PROCEDIMIENTO")).equals("")) {
				String procedimiento=(String)entrada.get("PROCEDIMIENTO");
				entrada.put(ScsDesignaBean.C_NUMPROCEDIMIENTO, procedimiento);
			}
		}	
		catch (Exception e) {		
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
							  " and rownum=1";//Si hubiera m�s de un interesado s�lo cogemos el primero
	
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
	
	public Vector getRelacionadoCon (String institucion, String anio, String numero, String idTipo,String longitudNumEjg ) throws ClsExceptions,SIGAException 
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
									 "TO_CHAR(e."+ScsEJGBean.C_IDTIPOEJG + ") IDTIPO, " ;
	       							if(longitudNumEjg!=null)
	       								sql += "LPAD( e." + ScsEJGBean.C_NUMEJG + ", "+longitudNumEjg+",0) CODIGO ,";
	       							else
	       								sql+="e."+ScsEJGBean.C_NUMEJG +  " CODIGO, " ;
									 
									 

									sql+= "(SELECT " + ScsTurnoBean.C_ABREVIATURA + " FROM " + ScsTurnoBean.T_NOMBRETABLA + 
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
						 " ORDER BY SJCS, IDINSTITUCION, ANIO desc, CODIGO desc";

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
	public String getIDProcuradorDesig (String idInstitucion, String idTurno, String anio, String numero) throws ClsExceptions,SIGAException {
	    Hashtable codigos = new Hashtable();
	    try {
	        codigos.put(new Integer(1),idInstitucion.toString());
	        codigos.put(new Integer(2),idTurno);
	        codigos.put(new Integer(3),anio);
	        codigos.put(new Integer(4),numero);
	        codigos.put(new Integer(5),5);
	        
 
	        
	       
			String select =	"SELECT F_SIGA_GETPROCURADOR_DESIGNA(:1, :2, :3, :4,:5) as IDPROCURADOR_DESIG FROM DUAL"; 

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select, codigos)) {
				if (rc.size() != 1) return null;
				Hashtable aux = (Hashtable)((Row) rc.get(0)).getRow();
				String num = UtilidadesHash.getString(aux, "IDPROCURADOR_DESIG");
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
	   				throw new ClsExceptions(e, "Error al obtener el id del PROCURADOR designado.");
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
		boolean esComision=(miHash.containsKey("ESCOMISION") && UtilidadesString.stringToBoolean(miHash.get("ESCOMISION").toString()));
		Short[] idInstitucionesComision = usrbean.getInstitucionesComision();
		
		//aalg. INC_06694_SIGA. Se modifica la query para hacerla m�s eficiente
		try {
			consulta=" select distinct des.estado estado, des.anio anio, des.numero numero, des.fechaentrada fechaentrada,des.idturno idturno, des.codigo||'' codigo, des.sufijo sufijo,des.idinstitucion idinstitucion ";
			if(idInstitucionesComision!=null && idInstitucionesComision.length>0){
				consulta +=  ",(SELECT INT.ABREVIATURA FROM CEN_INSTITUCION INT WHERE INT.IDINSTITUCION = des.IDINSTITUCION) INST_ABREV ";
			}
			consulta+=" from scs_designa des ";
			
			if(UtilidadesHash.getString(miHash,"NCOLEGIADO")!=null && !((String)UtilidadesHash.getString(miHash,"NCOLEGIADO")).equals("") ){
				consulta += ", SCS_DESIGNASLETRADO l ";
			}
			
			if (UtilidadesHash.getString(miHash,"CALIDAD") != null && !UtilidadesHash.getString(miHash,"CALIDAD").equalsIgnoreCase("")) {
				consulta += ", SCS_DEFENDIDOSDESIGNA def ";
			}
			
			boolean tiene_juzg=UtilidadesHash.getString(miHash,"JUZGADOACTU") != null && !UtilidadesHash.getString(miHash,"JUZGADOACTU").equalsIgnoreCase("");
			boolean tiene_asunto=UtilidadesHash.getString(miHash,"ASUNTOACTUACION") != null && !UtilidadesHash.getString(miHash,"ASUNTOACTUACION").equalsIgnoreCase("");
			boolean tiene_acreditacion=UtilidadesHash.getString(miHash,"ACREDITACION") != null && !UtilidadesHash.getString(miHash,"ACREDITACION").equalsIgnoreCase("");
			boolean tiene_modulo=UtilidadesHash.getString(miHash,"MODULO") != null && !UtilidadesHash.getString(miHash,"MODULO").equalsIgnoreCase("");
			
			if (tiene_juzg||tiene_asunto||tiene_acreditacion||tiene_modulo){
				consulta+=	", scs_actuaciondesigna act ";
			}
			
			boolean tiene_interesado=false;
			if((UtilidadesHash.getString(miHash,"NIF") != null && !UtilidadesHash.getString(miHash,"NIF").equalsIgnoreCase(""))
					|| (UtilidadesHash.getString(miHash,"NOMBRE") != null && !UtilidadesHash.getString(miHash,"NOMBRE").equalsIgnoreCase(""))
					|| (UtilidadesHash.getString(miHash,"APELLIDO1") != null && !UtilidadesHash.getString(miHash,"APELLIDO1").equalsIgnoreCase(""))
					|| (UtilidadesHash.getString(miHash,"APELLIDO2") != null && !UtilidadesHash.getString(miHash,"APELLIDO2").equalsIgnoreCase(""))){
				tiene_interesado = true;
			}
			
			if (tiene_interesado){
				consulta += ", SCS_DEFENDIDOSDESIGNA DED, SCS_PERSONAJG PER ";
			}
			
			if(esComision){
				if(miHash.get("IDINSTITUCION")!=null && !miHash.get("IDINSTITUCION").toString().equals("")){
					contador++;
					codigosBind.put(new Integer(contador),miHash.get("IDINSTITUCION").toString());
					consulta += " where des.IDINSTITUCION = :"+ contador;
				}else if(idInstitucionesComision!=null && idInstitucionesComision.length>0){
					consulta += " where des.IDINSTITUCION IN (";
					for (int i = 0; i < idInstitucionesComision.length; i++) {
						contador++;
						codigosBind.put(new Integer(contador),idInstitucionesComision[i]);
						consulta += "  :"+ contador;
						consulta += ",";
					}
					consulta = consulta.substring(0,consulta.length()-1);
					consulta += ")";
				}else{
					contador++;
					codigosBind.put(new Integer(contador),idInstitucion);
					consulta+=" where des.idinstitucion =:"+contador;
				}
			}else{
				
				contador++;
				codigosBind.put(new Integer(contador),idInstitucion);
				consulta+=" where des.idinstitucion =:"+contador;
			}
			
			if(UtilidadesHash.getString(miHash,"NCOLEGIADO")!=null && !((String)UtilidadesHash.getString(miHash,"NCOLEGIADO")).equals("") ){
				consulta += " and l.idinstitucion =des.idinstitucion ";
			    consulta += " and l.idturno =des.idturno ";
			    consulta += " and l.anio =des.anio "; 
			    consulta += " and l.numero =des.numero ";
			    consulta += " and (l.Fechadesigna is null or";
			    consulta += " l.Fechadesigna = (SELECT MAX(LET2.Fechadesigna) FROM SCS_DESIGNASLETRADO LET2";
			    consulta += " WHERE l.IDINSTITUCION = LET2.IDINSTITUCION AND l.IDTURNO = LET2.IDTURNO";
			    consulta += " AND l.ANIO = LET2.ANIO AND l.NUMERO = LET2.NUMERO";
			    consulta += " AND TRUNC(LET2.Fechadesigna) <= TRUNC(SYSDATE)))";
			    
			    contador++;
			    codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"NCOLEGIADO"));
			    consulta += " and l.idPersona = :" + contador + " ";
			}
				
			if ((UtilidadesHash.getString(miHash,"IDTURNO") != null)&&(UtilidadesHash.getString(miHash,"IDTURNO") != "-1")&&!UtilidadesHash.getString(miHash,"IDTURNO").equals("")){
			 	contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"IDTURNO"));
			 	consulta+=" and des.idTurno = :"+contador;
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

			if (UtilidadesHash.getString(miHash,"CODIGO") != null && !UtilidadesHash.getString(miHash,"CODIGO").equalsIgnoreCase("")) {
				
				
			    if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash,"CODIGO")))	{
			    	contador++;
			    	consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(UtilidadesHash.getString(miHash,"CODIGO"),"des.codigo",contador,codigosBind ); 
			    	
			    }else {
			    	contador++;
				    codigosBind.put(new Integer(contador),UtilidadesHash.getString(miHash,"CODIGO").trim());
			    	consulta += " AND ltrim(des.codigo,'0') = ltrim(:" + contador+",'0')" ; 
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
			if (UtilidadesHash.getString(miHash,"NIG2") != null && !UtilidadesHash.getString(miHash,"NIG2").equalsIgnoreCase("")) {
				contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"NIG2").trim());
				consulta += " AND des.nig = :" + contador;
			}			
			String actuacionesPendientes = UtilidadesHash.getString(miHash,"ACTUACIONES_PENDIENTES") ;
			if (actuacionesPendientes!= null && !actuacionesPendientes.equalsIgnoreCase("")&& (actuacionesPendientes.equalsIgnoreCase("NO")||actuacionesPendientes.equalsIgnoreCase("SI")||actuacionesPendientes.equalsIgnoreCase("SINACTUACIONES"))) {
				if(actuacionesPendientes.equalsIgnoreCase("SINACTUACIONES")){
					actuacionesPendientes="";
					consulta += " and upper(F_SIGA_ACTUACIONESDESIG(des.idinstitucion,des.idturno,des.anio,des.numero)) is null";
				}else{
				contador++;
				    codigosBind.put(new Integer(contador),actuacionesPendientes.trim());
				    consulta += " and upper(F_SIGA_ACTUACIONESDESIG(des.idinstitucion,des.idturno,des.anio,des.numero))=upper(:" + contador + ")";
				}
			}
			
			//Mostrar ART 27
			String mostarArt27 = UtilidadesHash.getString(miHash,"MOSTRAR_ART27") ;
			if (mostarArt27!= null && !mostarArt27.equalsIgnoreCase("") && !mostarArt27.equalsIgnoreCase("T")) {
				if(mostarArt27.equalsIgnoreCase("S")){
					consulta += " AND des.art27 = 1";
				}else if(mostarArt27.equalsIgnoreCase("N")){
				    consulta += " AND des.art27 = 0";
				}
			}
			
			if (UtilidadesHash.getString(miHash,"CALIDAD") != null && !UtilidadesHash.getString(miHash,"CALIDAD").equalsIgnoreCase("")) {
				contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"CALIDAD").trim());
				consulta += " and def.ANIO = des.anio"+
				" and def.NUMERO = des.numero"+
				" and def.IDINSTITUCION = des.idinstitucion"+
				" and def.IDTURNO = des.idturno"+
				" and def.idtipoencalidad= :" + contador+ " ";
			}
			
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
			
			if (tiene_interesado){
				consulta+=" AND DED.IDINSTITUCION = PER.IDINSTITUCION";
				consulta+="    AND DED.IDPERSONA = PER.IDPERSONA";
				consulta+="    AND DED.IDINSTITUCION = des.idInstitucion";
				consulta+="    AND DED.ANIO = des.ANIO";
				consulta+="    AND DED.IDTURNO = des.idTURNO";
				consulta+="    AND DED.NUMERO = des.NUMERO";

				if(UtilidadesHash.getString(miHash,"NIF") != null && !UtilidadesHash.getString(miHash,"NIF").equalsIgnoreCase("")){
					consulta+=" and ";
					if (ComodinBusquedas.hasComodin(miHash.get("NIF").toString())){	
					contador++;
					consulta+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"NIF").trim(),"PER.NIF",contador,codigosBind);
					}else{
						contador++;
						consulta +=ComodinBusquedas.prepararSentenciaNIFBind(miHash.get("NIF").toString(),"PER.NIF",contador, codigosBind);
					}
				}
				if(UtilidadesHash.getString(miHash,"NOMBRE") != null && !UtilidadesHash.getString(miHash,"NOMBRE").equalsIgnoreCase("")){
					consulta+=" and ";
					contador++;
					consulta+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"NOMBRE").trim(),"PER.NOMBRE",contador, codigosBind);
				}
				if(UtilidadesHash.getString(miHash,"APELLIDO1") != null && !UtilidadesHash.getString(miHash,"APELLIDO1").equalsIgnoreCase("")){
					consulta+=" and ";
					contador++;
					consulta+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"APELLIDO1").trim(),"PER.APELLIDO1",contador,codigosBind);
				}
				if(UtilidadesHash.getString(miHash,"APELLIDO2") != null && !UtilidadesHash.getString(miHash,"APELLIDO2").equalsIgnoreCase("")){
					consulta+=" and ";
					contador++;
					consulta+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"APELLIDO2").trim(),"PER.APELLIDO2",contador,codigosBind);
				}
			}
			
			if (tiene_juzg||tiene_asunto||tiene_acreditacion||tiene_modulo){
				consulta+=	" and des.idinstitucion = act.idinstitucion"+
									" and des.idturno = act.idturno"+
									" and des.anio = act.anio"+
									" and des.numero = act.numero ";
				if (tiene_juzg) {
					String a[]=((String)UtilidadesHash.getString(miHash,"JUZGADOACTU")).split(",");
					contador++;
					codigosBind.put(new Integer(contador),a[0].trim());
					consulta += " AND act.idjuzgado = :" + contador;
				}
				if (tiene_asunto) {
					contador++;
					codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"ASUNTOACTUACION").trim());
					consulta += " AND act.numeroasunto = :" + contador;
				}
				if (tiene_acreditacion) {
		        	contador++;
					codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"ACREDITACION").trim());
					consulta += " AND act.idacreditacion = :" + contador;
				}
				if (tiene_modulo) {
					contador++;
					codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"MODULO").trim());
					consulta += " AND act.idprocedimiento = :" + contador;
				}
			}

			// jbd // inc7744 // Cambiamos el order by porque parece que afecta a la query cuando se busca por colegiado
			// consulta+=" order by des.idturno, des.anio desc, des.numero desc";
			consulta+=" order by des.anio desc, codigo desc";
      // No utilizamos la clase Paginador para la busqueda de letrados porque al filtrar por residencia la consulta no devolvia bien los 
      //  datos que eran de tipo varchar (devolv�a n veces el mismo resultado), utilizamos el paginador PaginadorCaseSensitive
       // y hacemos a parte el tratamiento de mayusculas y signos de acentuaci�n
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
	
	
	public Vector getBusquedaDesignaRelacionada(String idInstitucion, Hashtable miHash) throws ClsExceptions , SIGAException{
		
		String consulta = "";
		Hashtable codigosBind = new Hashtable();
		int contador=0;
	  	
	  	// Acceso a BBDD
		int totalRegistros=0;
		
		
		
		try {
			//String bBusqueda = formulario.getChkBusqueda();
			consulta=" select des.estado estado, des.anio anio, des.numero numero, des.fechaentrada fechaentrada,des.idturno idturno, des.codigo||'' codigo, des.sufijo sufijo,des.idinstitucion idinstitucion ";
			
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
			    
			    consulta += " and :"+contador+"= (select max(L.IDPERSONA) from SCS_DESIGNASLETRADO L where l.idinstitucion =des.idinstitucion ";
			    consulta += " and l.idturno =des.idturno ";
			    consulta += " and l.anio =des.anio "; 
			    consulta += " and l.numero =des.numero ";
			    consulta += " and (L.Fechadesigna is null or";
			    consulta += " L.Fechadesigna = (SELECT MAX(LET2.Fechadesigna) FROM SCS_DESIGNASLETRADO LET2";
			    consulta += " WHERE L.IDINSTITUCION = LET2.IDINSTITUCION AND L.IDTURNO = LET2.IDTURNO";
			    consulta += " AND L.ANIO = LET2.ANIO AND L.NUMERO = LET2.NUMERO";
			    consulta += " AND TRUNC(LET2.Fechadesigna) <= TRUNC(SYSDATE))))";
			    
			    //consulta += " and F_SIGA_GETIDLETRADO_DESIGNA(des.idinstitucion,des.idturno,des.anio,des.numero) = :"+contador;
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

			if (UtilidadesHash.getString(miHash,"CODIGO") != null && !UtilidadesHash.getString(miHash,"CODIGO").equalsIgnoreCase("")) {
				
				
			    if (ComodinBusquedas.hasComodin(UtilidadesHash.getString(miHash,"CODIGO")))	{
			    	contador++;
			    	consulta += " AND " + ComodinBusquedas.prepararSentenciaCompletaBind(UtilidadesHash.getString(miHash,"CODIGO"),"des.codigo",contador,codigosBind ); 
			    	
			    }else {
			    	contador++;
				    codigosBind.put(new Integer(contador),UtilidadesHash.getString(miHash,"CODIGO").trim());
			    	consulta += " AND ltrim(des.codigo,'0') = ltrim(:" + contador+",'0')" ; 
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
			if (UtilidadesHash.getString(miHash,"NIG2") != null && !UtilidadesHash.getString(miHash,"NIG2").equalsIgnoreCase("")) {
				contador++;
				codigosBind.put(new Integer(contador),(String)UtilidadesHash.getString(miHash,"NIG2").trim());
				consulta += " AND des.nig = :" + contador;
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
			
			//Mostrar ART 27
			String mostarArt27 = UtilidadesHash.getString(miHash,"MOSTRAR_ART27") ;
			if (mostarArt27!= null && !mostarArt27.equalsIgnoreCase("") && !mostarArt27.equalsIgnoreCase("T")) {
				if(mostarArt27.equalsIgnoreCase("S")){
					consulta += " AND des.art27 = 1";
				}else if(mostarArt27.equalsIgnoreCase("N")){
				    consulta += " AND des.art27 = 0";
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
				" and def.idtipoencalidad= :" + contador+ ") > 0";
				
			}
			
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
			// jbd // Cambiamos la consulta de la vista por una mas ligera con la tabla de defendidos
			/*subConsulta1+=" AND (select count(1) from V_SIGA_DEFENDIDOS_DESIGNA VDEF";
			subConsulta1+=" where VDEF.idInstitucion = des.idinstitucion";
			subConsulta1+=" and VDEF.anio = des.anio";
			subConsulta1+=" and VDEF.numero = des.numero";
			subConsulta1+=" and VDEF.IDTURNO = des.idturno";*/
			subConsulta1+=" AND (SELECT count(1)";
			subConsulta1+="   FROM SCS_DEFENDIDOSDESIGNA DEF, SCS_PERSONAJG PER";
			subConsulta1+="  WHERE DEF.IDINSTITUCION = PER.IDINSTITUCION";
			subConsulta1+="    AND DEF.IDPERSONA = PER.IDPERSONA";
			subConsulta1+="    AND DEF.IDINSTITUCION = des.idInstitucion";
			subConsulta1+="    AND DEF.ANIO = des.ANIO";
			subConsulta1+="    AND DEF.IDTURNO = des.idTURNO";
			subConsulta1+="    AND DEF.NUMERO = des.NUMERO";
			if(UtilidadesHash.getString(miHash,"NIF") != null && !UtilidadesHash.getString(miHash,"NIF").equalsIgnoreCase("")){
				isFiltrado = true;
				subConsulta1+=" and ";
				if (ComodinBusquedas.hasComodin(miHash.get("NIF").toString())){	
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"NIF").trim(),"PER.NIF",contador,codigosBind);
				}else{
					contador++;
					subConsulta1 +=ComodinBusquedas.prepararSentenciaNIFBind(miHash.get("NIF").toString(),"PER.NIF",contador, codigosBind);
				}
			}
			if(UtilidadesHash.getString(miHash,"NOMBRE") != null && !UtilidadesHash.getString(miHash,"NOMBRE").equalsIgnoreCase("")){
				isFiltrado = true;
				subConsulta1+=" and ";
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"NOMBRE").trim(),"PER.NOMBRE",contador, codigosBind);
			}
			if(UtilidadesHash.getString(miHash,"APELLIDO1") != null && !UtilidadesHash.getString(miHash,"APELLIDO1").equalsIgnoreCase("")){
				isFiltrado = true;
				subConsulta1+=" and ";
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"APELLIDO1").trim(),"PER.APELLIDO1",contador,codigosBind);
			}
			if(UtilidadesHash.getString(miHash,"APELLIDO2") != null && !UtilidadesHash.getString(miHash,"APELLIDO2").equalsIgnoreCase("")){
				isFiltrado = true;
				subConsulta1+=" and ";
				contador++;
				subConsulta1+=ComodinBusquedas.prepararSentenciaCompletaBind((String)UtilidadesHash.getString(miHash,"APELLIDO2").trim(),"PER.APELLIDO2",contador,codigosBind);
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
			// jbd // inc7744 // Cambiamos el order by porque parece que afecta a la query cuando se busca por colegiado
			// consulta+=" order by des.idturno, des.anio desc, des.numero desc";
			consulta+=" order by des.anio desc, des.codigo desc";

			return this.selectGenericoNLSBind(consulta, codigosBind);	
      
		} catch (Exception e) { 	
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
			" TO_CHAR(DESPROC.FECHADESIGNA,'dd-mm-yyyy') as FECHADESIGNA_PROCURADOR,"+
			" PROC.IDPROCURADOR AS IDPROCURADORDESIGNA"+
			" ,CP.NOMBRE AS PROCURADOR_COL_NOMBRE, "+
			" CP.DIRECCION AS PROCURADOR_COL_DIRECCION, "+
			" (SELECT F_SIGA_GETRECURSO(TIPOVIA.DESCRIPCION, 1) "+
			" FROM CEN_TIPOVIA TIPOVIA "+
			" WHERE TIPOVIA.IDTIPOVIA = CP.IDTIPOVIA "+
			" AND TIPOVIA.IDINSTITUCION = 2000) AS PROCURADOR_COL_TIPOVIA, "+
			" CP.NUMERODIR AS PROCURADOR_COL_NUMERODIR, "+
			" CP.ESCALERADIR AS PROCURADOR_COL_ESCALERADIR, "+
			" CP.PISODIR AS PROCURADOR_COL_PISODIR, "+
			" CP.PUERTADIR AS PROCURADOR_COL_PUERTADIR, "+
			" CP.CODIGOPOSTAL AS PROCURADOR_COL_CODPOSTAL, "+
			" (SELECT POBLACION.NOMBRE "+
			" FROM CEN_POBLACIONES POBLACION, CEN_PROVINCIAS PROVINCIA "+
			" WHERE POBLACION.IDPROVINCIA = PROVINCIA.IDPROVINCIA "+
			" AND CP.IDPROVINCIA = POBLACION.IDPROVINCIA "+
			" AND CP.IDPOBLACION = POBLACION.IDPOBLACION) AS PROCURADOR_COL_POBLACION, "+
			" (SELECT PROVINCIA.NOMBRE "+
			" FROM CEN_PROVINCIAS PROVINCIA "+
			" WHERE PROVINCIA.IDPROVINCIA = CP.IDPROVINCIA) AS PROCURADOR_COL_PROVINCIA "+
			
			
			" FROM SCS_DESIGNAPROCURADOR DESPROC, SCS_PROCURADOR PROC"+
			" , CEN_COLEGIOPROCURADOR CP "+
			" WHERE " +
			"  PROC.IDCOLPROCURADOR = CP.IDCOLPROCURADOR(+) "+
			" AND DESPROC.IDINSTITUCION_PROC = PROC.IDINSTITUCION"+
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
	

	public Vector getLetradoSalidaOficio(Hashtable htCodigo) throws ClsExceptions 	{
		
		try {
			 String sql= " Select TO_CHAR(Dp.Fechadesigna,'dd/mm/yyyy') as FECHALETRADO_ACTUAL, "+           
			 			  " P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' ||P.APELLIDOS2 AS NOMBRE_LETRADO, "+
			 			  " P.NIFCIF AS NIF_LETRADO, DECODE(P.SEXO, null, null,'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_LETRADO_SINTRADUCIR "+
			 			  " , DECODE(P.SEXO,'H','o','a') AS O_A_LETRADO "+
			 			  " , DECODE(P.SEXO,'H','el','la') AS EL_LA_LETRADO "+
			 			  " From Scs_Designasletrado Dp, Cen_Persona p, Cen_Colegiado c "+
			 			  " Where Dp.Idinstitucion= :1 "+
			 			  " And Dp.Idinstitucion = c.Idinstitucion "+
			 			  " And Dp.Idpersona = c.Idpersona "+
			 			  " And Dp.Idpersona = p.Idpersona " +
			 			  " And Dp.Idturno = :2 "+
			 			  " And Dp.Anio= :3 "+
			 			  " And Dp.Numero =:4 "+			 			  
			 			  " and Dp.Idpersona =:5"+
			 			  " and Dp.Fecharenuncia is null";		
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, htCodigo);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getLetrado");
		}
	}
	
	public Vector getLetradoSalidaOficioArt27(Hashtable htCodigo) throws ClsExceptions 	{
		
		try {
			 String sql= " Select TO_CHAR(Dp.Fechadesigna,'dd/mm/yyyy') as FECHALETRADO_ACTUAL, "+           
			 			  " P.NOMBRE || ' ' || P.APELLIDOS1 || ' ' ||P.APELLIDOS2 AS NOMBRE_LETRADO, "+
			 			  " P.NIFCIF AS NIF_LETRADO, DECODE(P.SEXO, null, null,'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_LETRADO_SINTRADUCIR "+
			 			  " , DECODE(P.SEXO,'H','o','a') AS O_A_LETRADO "+
			 			  " , DECODE(P.SEXO,'H','el','la') AS EL_LA_LETRADO "+
			 			  " From Scs_Designasletrado Dp, Cen_Persona p, Cen_Cliente c "+
			 			  " Where Dp.Idinstitucion= :1 "+
			 			  " And Dp.Idinstitucion = c.Idinstitucion "+
			 			  " And Dp.Idpersona = c.Idpersona "+
			 			  " And Dp.Idpersona = p.Idpersona " +
			 			  " And Dp.Idturno = :2 "+
			 			  " And Dp.Anio= :3 "+
			 			  " And Dp.Numero =:4 "+			 			  
			 			  " and Dp.Idpersona =:5"+
			 			  " and Dp.Fecharenuncia is null";		
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, htCodigo);
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
			String sql = "SELECT * FROM (SELECT "+
					" DIR.domicilio DOMICILIO_DESPACHO_LETRADO, "+
					" DIR.codigopostal CP_DESPACHO_LETRADO, "+
					" DIR.idpoblacion IDPOBLACION_DESPACHO_LETRADO, "+
					" DIR.poblacionextranjera POBLACION_DESPACHO_LETRADO, "+					
					" DIR.idprovincia IDPROVINCIA_DESPACHO_LETRADO, "+
					" DIR.telefono1 TELEFONO1_DESPACHO_LETRADO, "+
					" DIR.telefono2 TELEFONO2_DESPACHO_LETRADO, "+
					" DIR.fax1 FAX1_DESPACHO_LETRADO, "+
					" DIR.fax2 FAX2_DESPACHO_LETRADO, "+
					" DIR.correoelectronico EMAIL_DESPACHO_LETRADO, "+
					" DIR.movil MOVIL_DESPACHO_LETRADO"+
				" FROM CEN_DIRECCIONES DIR, "+
					" CEN_DIRECCION_TIPODIRECCION TIP " +
				" WHERE dir.idinstitucion = tip.idinstitucion " +
					" and dir.idpersona = tip.idpersona  " +
					" and dir.iddireccion = tip.iddireccion " +
					" and tip.idtipodireccion = 2 " +
					" and dir.fechabaja is null "+
					" and dir.idinstitucion = :1 "+
					" and dir.idpersona = :2 "+
				" ORDER BY dir.fechamodificacion DESC) "+
				" WHERE ROWNUM = 1 "; 

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetrado");
		}
	}
	
	public Vector getDireccionLetradoSalidaOficioPreferente(String idPersona,String idInstitucion) throws ClsExceptions {
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			String sql = "SELECT * FROM (SELECT "+
					" DIR.domicilio DOMICILIO_DESPACHO_LETRADO, "+
					" DIR.codigopostal CP_DESPACHO_LETRADO, "+
					" DIR.idpoblacion IDPOBLACION_DESPACHO_LETRADO, "+
					" DIR.poblacionextranjera POBLACION_DESPACHO_LETRADO, "+					
					" DIR.idprovincia IDPROVINCIA_DESPACHO_LETRADO, "+
					" DIR.telefono1 TELEFONO1_DESPACHO_LETRADO, "+
					" DIR.telefono2 TELEFONO2_DESPACHO_LETRADO, "+
					" DIR.fax1 FAX1_DESPACHO_LETRADO, "+
					" DIR.fax2 FAX2_DESPACHO_LETRADO, "+
					" DIR.correoelectronico EMAIL_DESPACHO_LETRADO, "+
					" DIR.movil MOVIL_DESPACHO_LETRADO"+
				" FROM CEN_DIRECCIONES DIR, "+
					" CEN_DIRECCION_TIPODIRECCION TIP " +
				" WHERE dir.idinstitucion = tip.idinstitucion " +
					" and dir.idpersona = tip.idpersona  " +
					" and dir.iddireccion = tip.iddireccion " +
					" and dir.preferente like '%C%' "+
					" and tip.idtipodireccion = 2 " +
					" and dir.fechabaja is null "+
					" and dir.idinstitucion = :1 "+
					" and dir.idpersona = :2 "+
				" ORDER BY dir.fechamodificacion DESC) "+
				" WHERE ROWNUM = 1 ";

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDireccionLetradoSalidaOficioPreferente");
		}
	}
	
	
	public Vector getDireccionPersonalLetradoSalidaOficio(String idPersona,String idInstitucion) throws ClsExceptions {
		try {
			Hashtable h = new Hashtable();
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idPersona);
			String sql = "SELECT * FROM (SELECT "+
				" DIR.domicilio DOMICILIO_GUARDIA_LETRADO, "+
				" DIR.codigopostal CP_GUARDIA_LETRADO, "+
				" DIR.idpoblacion IDPOBLACION_GUARDIA_LETRADO, "+
				" DIR.poblacionextranjera POBLACION_GUARDIA_LETRADO, "+					
				" DIR.idprovincia IDPROVINCIA_GUARDIA_LETRADO, "+
				" DIR.telefono1 TELEFONO1_GUARDIA_LETRADO, "+
				" DIR.telefono2 TELEFONO2_GUARDIA_LETRADO, "+
				" DIR.fax1 FAX1_GUARDIA_LETRADO, "+
				" DIR.fax2 FAX2_GUARDIA_LETRADO, "+
				" DIR.correoelectronico EMAIL_GUARDIA_LETRADO, "+
				" DIR.movil MOVIL_GUARDIA_LETRADO"+
			" FROM CEN_DIRECCIONES DIR, "+
				" CEN_DIRECCION_TIPODIRECCION TIP " +
			" WHERE dir.idinstitucion = tip.idinstitucion " +
				" and dir.idpersona = tip.idpersona  " +
				" and dir.iddireccion = tip.iddireccion " +
				" and tip.idtipodireccion = 6 " +
				" and dir.fechabaja is null "+
				" and dir.idinstitucion = :1 "+
				" and dir.idpersona = :2 "+
			" ORDER BY dir.fechamodificacion DESC) "+
			" WHERE ROWNUM = 1 ";

			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las getDireccionPersonalLetradoSalidaOficio");
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

			" (DECODE(ACT.ANIO, NULL, null,ACT.ANIO || '/' || DES.CODIGO || '/' || ACT.NUMEROASUNTO)) as NACTUACION,"+
			" PRO.NOMBRE AS PROCEDIMIENTO, "+
			//-- SI ES NULO HAY QUE COGER EL DE LA DESIGNACI�N"+
			" TO_CHAR(ACT.FECHA, 'dd-mm-yyyy') AS FECHA_ACTUACION,"+
			" DECODE(ACT.IDPRISION, NULL, NULL,(SELECT P.NOMBRE FROM SCS_PRISION P"+
			" WHERE P.IDINSTITUCION = ACT.IDINSTITUCION_PRIS"+
			" AND P.IDPRISION = ACT.IDPRISION)) AS LUGAR " +
			//-- SI ES NULO SE BUSCA EL NOMBRE DEL JUZGADO DE LA DESIGNACION"+
			" FROM SCS_ACTUACIONDESIGNA ACT, SCS_PROCEDIMIENTOS PRO, SCS_DESIGNA DES"+
			" WHERE ACT.IDINSTITUCION_PROC = PRO.IDINSTITUCION(+)"+
			" AND ACT.IDPROCEDIMIENTO = PRO.IDPROCEDIMIENTO(+)"+
			" AND ACT.IDINSTITUCION = DES.IDINSTITUCION"+
			" AND ACT.IDTURNO = DES.IDTURNO"+
			" AND ACT.ANIO = DES.ANIO"+
			" AND ACT.NUMERO = DES.NUMERO"+
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
	
	
	public Vector getEstadoCivilDesignaDefendido (String idInstitucion, String turno,String anio,String numero,String idPersonaJG,String idioma) throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable(); 
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), turno);
			h.put(new Integer(3), anio);
			h.put(new Integer(4), numero);
			h.put(new Integer(5), idPersonaJG);

			String sql = "Select " +
			"		F_SIGA_GETRECURSO(estcivil.descripcion, "+idioma+") estadocivil_defendido,estcivil.descripcion " +
			" from cen_estadocivil estcivil,"+
		       "scs_defendidosdesigna defen,"+
		       "scs_personajg persJg,"+
		       "Scs_Designa Des "+ 
		       
		       " where des.anio = defen.anio(+) " +
			   " AND des.numero = defen.numero(+) " +
			   " AND des.idinstitucion = defen.idinstitucion " +
			   " AND des.idturno = defen.idturno(+) " +
			   " and persJg.idpersona = defen.idpersona " +
			   " AND estcivil.idestadocivil = persJg.idestadocivil " +
		       
		       
				"   And Des.Idinstitucion = :1 " +
				"   And Des.Idturno = :2 " +
				"   And Des.Anio = :3 " +
				"   And Des.Numero = :4"+
				"   and persJg.idpersona =:5 " ;
				
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre las relaciones de procuradores.");
		}
	}
	
	
			
	public Vector getDatosSalidaOficio (String idInstitucion, String idturno, String anio, String numero, String codigoDesigna, boolean isSolicitantes, boolean isContrarios, String idPersonaJG, String idioma,String idiomaInforme, String tipoDestinatarioInforme, Boolean agregarEtiqEJG, String longitudNumEjg) throws ClsExceptions  
	{	 
	Vector vSalida = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();	
		try {
			vSalida = new Vector();	
			Vector vDesigna = getDesignaSalidaOficio(idInstitucion,idturno, anio, numero, codigoDesigna, idioma); 
			
			Hashtable htCodigo = new Hashtable();
			for (int j = 0; j < vDesigna.size(); j++) {
				Hashtable registro = (Hashtable) vDesigna.get(j);
				
				String numeroDesigna = (String)registro.get("NUMERO");
				String anioDesigna = (String)registro.get("ANIO");
				String idTurno  = (String)registro.get("IDTURNO");
				String idPersona  = (String)registro.get("IDPERSONA");
//				String idiomaletrado = (String)registro.get("IDIOMA_LETRADO");			
				String idInstitucionOrigen = (String)registro.get("IDINSTITUCIONORIGEN");
								
				Vector vDefendidos = getDefendidosDesignaSalidaOficio(idInstitucion,numeroDesigna,idTurno,anioDesigna,idPersonaJG, idPersona,longitudNumEjg);
				// ENTRA EN ESTE CODIGO SI ES UN INFORME QUE DESDOBLA POR SOLICITANTE
				if(isSolicitantes){											
					if(vDefendidos!=null && vDefendidos.size()>0){
						for (int k = 0; k < vDefendidos.size(); k++) {
							Hashtable clone = (Hashtable) registro.clone();							
							Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);								
							String turnodefendido=(String)registroDefendido.get("IDTURNO");							
							if (turnodefendido!=null && !turnodefendido.trim().equals("")){
								registro.putAll(registroDefendido);
							}
//							String numEjgDefendido = "";
//							if(idPersonaJG!=null &&  registroDefendido.get("IDPERSONAINTERESADO").equals(idPersonaJG) && registroDefendido.get("NUMERO_EJG")!=null)
//								numEjgDefendido = (String) registroDefendido.get("NUMERO_EJG");
							if(registroDefendido!=null && registroDefendido.get("NUMERO_EJG")!=null)
								clone.put("NUMERO_EJG_DEFENDIDO", (String) registroDefendido.get("NUMERO_EJG"));
							registroDefendido  = getregistrodatosDesigna(registro, idInstitucion,idioma,tipoDestinatarioInforme,agregarEtiqEJG, isSolicitantes, isContrarios,longitudNumEjg);
							/**Para saaber en que idioma se tiene que imprimer la carta de oficio**/
							registroDefendido.put("CODIGOLENGUAJE", idiomaInforme);							
							
							htCodigo = new Hashtable();
							String sexoDefendico="";
							if((String)registroDefendido.get("SEXO_DEFENDIDO")!=null && !((String)registroDefendido.get("SEXO_DEFENDIDO")).trim().equals("")){
								sexoDefendico=	(String)registroDefendido.get("SEXO_DEFENDIDO");
								htCodigo.put(new Integer(1), sexoDefendico);
								htCodigo.put(new Integer(2), idioma);
								helperInformes.completarHashSalida(registroDefendido,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETRECURSO_ETIQUETA", "SEXO_DEFENDIDO"));
							}else{
								registroDefendido.put("SEXO_DEFENDIDO", "");
							}
							htCodigo = new Hashtable();
							String calidadDefendido="";
							if((String)registroDefendido.get("CALIDAD_DEFENDIDO")!=null && !((String)registroDefendido.get("CALIDAD_DEFENDIDO")).trim().equals("")){
								calidadDefendido=	(String)registroDefendido.get("CALIDAD_DEFENDIDO");
								htCodigo.put(new Integer(1), calidadDefendido);
								htCodigo.put(new Integer(2), idioma);
								helperInformes.completarHashSalida(registroDefendido,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETRECURSO", "CALIDAD_DEFENDIDO"));
							}else{
								registroDefendido.put("CALIDAD_DEFENDIDO", "");
							}			
							
							String nombrePais="";
							if((String)registroDefendido.get("NOMBRE_PAIS")!=null && !((String)registroDefendido.get("NOMBRE_PAIS")).trim().equals("")){
								nombrePais=	(String)registroDefendido.get("NOMBRE_PAIS");
								htCodigo.put(new Integer(1), nombrePais);
								htCodigo.put(new Integer(2), idioma);
								helperInformes.completarHashSalida(registroDefendido,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETRECURSO", "NOMBRE_PAIS"));
							}else{
								registroDefendido.put("NOMBRE_PAIS", "");
							}
							
							Vector estadodCivilDefendidoVector = getEstadoCivilDesignaDefendido(idInstitucion,idturno, anio, numero,(String)registroDefendido.get("IDPERSONAINTERESADO"),idioma);
							String estadoCivilDefendido = "";
							if(estadodCivilDefendidoVector.size()>0){
								estadoCivilDefendido = (String) ((Hashtable)estadodCivilDefendidoVector.get(0)).get("ESTADOCIVIL_DEFENDIDO");
							}
							registroDefendido.put("ESTADOCIVIL_DEFENDIDO", estadoCivilDefendido);
							
							//representante del defendido
							String IdpersonaRepresentante=(String)registro.get("IDREPRESENTANTEJG");
							
							if((IdpersonaRepresentante!=null)&&(!IdpersonaRepresentante.isEmpty())){
								ScsPersonaJGAdm scsPersonaJGAdm=new ScsPersonaJGAdm(usrbean);
								Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG(IdpersonaRepresentante,idInstitucion);
								registroDefendido.put("NOMBRE_REPR_DEF", (String) representanteLegalDefendido.get("NOMBRE_PJG"));
								registroDefendido.put("NOMBRE_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("NOMBRE_PJG")).toUpperCase());
								registroDefendido.put("APELLIDO1_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO1_PJG"));
								registroDefendido.put("APELLIDO1_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO1_PJG")).toUpperCase());
								registroDefendido.put("APELLIDO2_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO2_PJG"));
								registroDefendido.put("APELLIDO2_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO2_PJG")).toUpperCase());
								String sexoRepr_def = UtilidadesString.getMensajeIdioma(usrbean, (String) representanteLegalDefendido.get("SEXO_PJG"));		
								registroDefendido.put("SEXO_REPR_DEF",sexoRepr_def);
								registroDefendido.put("O_A_REPR_DEF", (String) representanteLegalDefendido.get("O_A_PJG"));
								registroDefendido.put("EL_LA_REPR_DEF", (String) representanteLegalDefendido.get("EL_LA_PJG"));
								registroDefendido.put("NIFCIF_REPR_DEF", (String) representanteLegalDefendido.get("NIF_PJG"));
								registroDefendido.put("IDPERSONA_REPR_DEF", (String) representanteLegalDefendido.get("IDPERSONA_PJG"));
								registroDefendido.put("IDDIRECCION_REPR_DEF", (String) representanteLegalDefendido.get("IDDIRECCION_PJG"));
								registroDefendido.put("DOMICILIO_REPR_DEF", (String) representanteLegalDefendido.get("DOMICILIO_PJG"));
								registroDefendido.put("CODIGOPOSTAL_REPR_DEF", (String) representanteLegalDefendido.get("CP_PJG"));
								registroDefendido.put("TELEFONO1_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO1_PJG"));
								registroDefendido.put("TELEFONO2_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO2_PJG"));
								registroDefendido.put("MOVIL_REPR_DEF", (String) representanteLegalDefendido.get("MOVIL_PJG"));
								registroDefendido.put("LISTA_TELEFONOS_REPR_DEF", (String) representanteLegalDefendido.get("LISTA_TELEFONOS_REPR"));
								registroDefendido.put("FAX1_REPR_DEF", (String) representanteLegalDefendido.get("FAX_PJG"));
								registroDefendido.put("CORREOELECTRONICO_REPR_DEF", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
								registroDefendido.put("NOMBRE_POBLACION_REPR_DEF", (String) representanteLegalDefendido.get("POBLACION_PJG"));
								registroDefendido.put("NOMBRE_PROVINCIA_REPR_DEF", (String) representanteLegalDefendido.get("PROVINCIA_PJG"));
							
							}else{
								
								
								registroDefendido.put("NOMBRE_REPR_DEF", "");
								registroDefendido.put("NOMBRE_REPR_DEF_MAYUS", "");
								registroDefendido.put("APELLIDO1_REPR_DEF", "");
								registroDefendido.put("APELLIDO1_REPR_DEF_MAYUS","");
								registroDefendido.put("APELLIDO2_REPR_DEF", "");
								registroDefendido.put("APELLIDO2_REPR_DEF_MAYUS", "");
								registroDefendido.put("SEXO_REPR_DEF", "");
								registroDefendido.put("O_A_REPR_DEF", "");
								registroDefendido.put("EL_LA_REPR_DEF", "");
								registroDefendido.put("NIFCIF_REPR_DEF", "");
								registroDefendido.put("IDPERSONA_REPR_DEF", "");
								registroDefendido.put("IDDIRECCION_REPR_DEF", "");
								registroDefendido.put("DOMICILIO_REPR_DEF", "");
								registroDefendido.put("CODIGOPOSTAL_REPR_DEF","");
								registroDefendido.put("TELEFONO1_REPR_DEF", "");
								registroDefendido.put("TELEFONO2_REPR_DEF", "");
								registroDefendido.put("MOVIL_REPR_DEF", "");
								registroDefendido.put("LISTA_TELEFONOS_REPR_DEF", "");
								registroDefendido.put("FAX1_REPR_DEF", "");
								registroDefendido.put("CORREOELECTRONICO_REPR_DEF", "");
								registroDefendido.put("NOMBRE_POBLACION_REPR_DEF","");
								registroDefendido.put("NOMBRE_PROVINCIA_REPR_DEF", "");
						
							}
							clone.putAll(registroDefendido);
							
							
//							clone.put("NUMERO_EJG_DEFENDIDO", numEjgDefendido);
							
							vSalida.add(clone);
						}  // END FOR
						
					// SI ENTRA AL ELSE NO TIENE DEFENDIDOS 
					}else{						
						
						registro.putAll(getregistrodatosDesigna(registro, idInstitucion, idioma,tipoDestinatarioInforme,agregarEtiqEJG, isSolicitantes, isContrarios,longitudNumEjg));
						
						
						// Control de datos para el informe
						if((String)registro.get("FECHARESOLUCIONCAJG")!=null && !((String)registro.get("FECHARESOLUCIONCAJG")).trim().equals(""))
							registro.put("FECHARESOLUCIONCAJG", "");
						
						if((String)registro.get("FECHARESOLUCIONCAJGLETRA")!=null && !((String)registro.get("FECHARESOLUCIONCAJGLETRA")).trim().equals(""))
							registro.put("FECHARESOLUCIONCAJGLETRA", "");
						
						if((String)registro.get("NUMERO_EJG")!=null && !((String)registro.get("NUMERO_EJG")).trim().equals(""))
							registro.put("NUMERO_EJG", "");
						
						if((String)registro.get("FECHAAPERTURA_EJG")!=null && !((String)registro.get("FECHAAPERTURA_EJG")).trim().equals(""))
							registro.put("FECHAAPERTURA_EJG", "");
						
						if((String)registro.get("FECHAAPERTURA_EJG_LETRA")!=null && !((String)registro.get("FECHAAPERTURA_EJG_LETRA")).trim().equals(""))
							registro.put("FECHAAPERTURA_EJG_LETRA", "");
						
						if((String)registro.get("PARRAFO_LETRADO_PROCURADOR")!=null && !((String)registro.get("PARRAFO_LETRADO_PROCURADOR")).trim().equals(""))
							registro.put("PARRAFO_LETRADO_PROCURADOR", "");
						
						// Como no tiene defendidos, no muestra las siguientes etiquetas
						registro.put("CODIGOLENGUAJE", idiomaInforme);	
						registro.put("LISTA_TELEFONOS_INTERESADO", "");						
						registro.put("NIF_DEFENDIDO", "");
						registro.put("NOMBRE_DEFENDIDO", "");
						registro.put("DOMICILIO_DEFENDIDO", "");
						registro.put("CP_DEFENDIDO", "");
						registro.put("POBLACION_DEFENDIDO", "");
						registro.put("PROVINCIA_DEFENDIDO", "");
						registro.put("TELEFONO1_DEFENDIDO", "");
						registro.put("SEXO_DEFENDIDO", "");
						registro.put("IDLENGUAJE_DEFENDIDO", "");
						registro.put("CALIDAD_DEFENDIDO", "");
						registro.put("OBS_DEFENDIDO", "");		
						registro.put("O_A_DEFENDIDO", "");
						registro.put("EL_LA_DEFENDIDO", "");
						registro.put("ESTADOCIVIL_DEFENDIDO", "");
						registro.put("NOMBRE_PAIS", "");
						registro.put("IDPERSONAINTERESADO", "");
						registro.put("NUMERO_EJG_DEFENDIDO", "");
						//representante del defendido
						registro.put("NOMBRE_REPR_DEF", "");
						registro.put("NOMBRE_REPR_DEF_MAYUS", "");
						registro.put("APELLIDO1_REPR_DEF", "");
						registro.put("APELLIDO1_REPR_DEF_MAYUS","");
						registro.put("APELLIDO2_REPR_DEF", "");
						registro.put("APELLIDO2_REPR_DEF_MAYUS", "");
						registro.put("SEXO_REPR_DEF", "");
						registro.put("O_A_REPR_DEF", "");
						registro.put("EL_LA_REPR_DEF", "");
						registro.put("NIFCIF_REPR_DEF", "");
						registro.put("IDPERSONA_REPR_DEF", "");
						registro.put("IDDIRECCION_REPR_DEF", "");
						registro.put("DOMICILIO_REPR_DEF", "");
						registro.put("CODIGOPOSTAL_REPR_DEF","");
						registro.put("TELEFONO1_REPR_DEF", "");
						registro.put("TELEFONO2_REPR_DEF", "");
						registro.put("MOVIL_REPR_DEF", "");
						registro.put("LISTA_TELEFONOS_REPR_DEF", "");
						registro.put("FAX1_REPR_DEF", "");
						registro.put("CORREOELECTRONICO_REPR_DEF", "");
						registro.put("NOMBRE_POBLACION_REPR_DEF","");
						registro.put("NOMBRE_PROVINCIA_REPR_DEF", "");
						
						
						
						vSalida.add(registro);
					}	
				
				// ENTRA EN ESTE CODIGO SI ES UN INFORME QUE NO DESDOBLA POR SOLICITANTE
				}else{	
						
					registro.putAll(getregistrodatosDesigna(registro, idInstitucion,idioma,tipoDestinatarioInforme,agregarEtiqEJG,  isSolicitantes, isContrarios,longitudNumEjg));				
										
					
					if(vDefendidos!=null && vDefendidos.size()>0){
						for (int k = 0; k < vDefendidos.size(); k++) {
							Hashtable registroDefendido = (Hashtable) vDefendidos.get(k);								
							if(registroDefendido!=null && registroDefendido.get("NUMERO_EJG")!=null)
								registroDefendido.put("NUMERO_EJG_DEFENDIDO", (String) registroDefendido.get("NUMERO_EJG"));
														
							/****
							  	Se modifica para que salgan los informes de los interesados en el idioma que tenga dicho interesado, 
								si no tiene idioma se imprime en el idioma del usuario. y los delitos tambi�n salen en el idioma del interesado.								
							***/							
							
							/**Depende del idioma del defendido se tiene que imprimir la carta del defendido, si no tiene idioma se imprime en el idioma de la instituci�n.**/
								
							/**Llama a la funcion para recuperar los valores con el idioma que le pasamos.**/
							/**Para saaber en que idioma se tiene que imprimer la carta de oficio**/
							registroDefendido.put("CODIGOLENGUAJE", idiomaInforme);								
							htCodigo = new Hashtable();
							String sexoDefendico="";
							if((String)registroDefendido.get("SEXO_DEFENDIDO")!=null && !((String)registroDefendido.get("SEXO_DEFENDIDO")).trim().equals("")){
								sexoDefendico=	(String)registroDefendido.get("SEXO_DEFENDIDO");
								htCodigo.put(new Integer(1), sexoDefendico);
								htCodigo.put(new Integer(2), idioma);
								helperInformes.completarHashSalida(registroDefendido,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETRECURSO_ETIQUETA", "SEXO_DEFENDIDO"));
							}else{
								registroDefendido.put("SEXO_DEFENDIDO", "");
							}
							htCodigo = new Hashtable();
							String calidadDefendido="";
							if((String)registroDefendido.get("CALIDAD_DEFENDIDO")!=null && !((String)registroDefendido.get("CALIDAD_DEFENDIDO")).trim().equals("")){
								calidadDefendido=	(String)registroDefendido.get("CALIDAD_DEFENDIDO");
								htCodigo.put(new Integer(1), calidadDefendido);
								htCodigo.put(new Integer(2), idioma);
								helperInformes.completarHashSalida(registroDefendido,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETRECURSO", "CALIDAD_DEFENDIDO"));
							}else{
								registroDefendido.put("CALIDAD_DEFENDIDO", "");
							}			
							
							String nombrePais="";
							if((String)registroDefendido.get("NOMBRE_PAIS")!=null && !((String)registroDefendido.get("NOMBRE_PAIS")).trim().equals("")){
								nombrePais=	(String)registroDefendido.get("NOMBRE_PAIS");
								htCodigo.put(new Integer(1), nombrePais);
								htCodigo.put(new Integer(2), idioma);
								helperInformes.completarHashSalida(registroDefendido,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETRECURSO", "NOMBRE_PAIS"));
							}else{
								registroDefendido.put("NOMBRE_PAIS", "");
							}
							
							Vector estadodCivilDefendidoVector = getEstadoCivilDesignaDefendido(idInstitucion,idturno, anio, numero,(String)registroDefendido.get("IDPERSONAINTERESADO"),idioma);
							String estadoCivilDefendido = "";
							if(estadodCivilDefendidoVector.size()>0){
								estadoCivilDefendido = (String) ((Hashtable)estadodCivilDefendidoVector.get(0)).get("ESTADOCIVIL_DEFENDIDO");
							}
							registroDefendido.put("ESTADOCIVIL_DEFENDIDO", estadoCivilDefendido);
							
							//representante del defendido
							String IdpersonaRepresentante=(String)registro.get("IDREPRESENTANTEJG");
							
							if((IdpersonaRepresentante!=null)&&(!IdpersonaRepresentante.isEmpty())){
								ScsPersonaJGAdm scsPersonaJGAdm=new ScsPersonaJGAdm(usrbean);
								Hashtable representanteLegalDefendido = scsPersonaJGAdm.getDatosPersonaJG(IdpersonaRepresentante,idInstitucion);
								registroDefendido.put("NOMBRE_REPR_DEF", (String) representanteLegalDefendido.get("NOMBRE_PJG"));
								registroDefendido.put("NOMBRE_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("NOMBRE_PJG")).toUpperCase());
								registroDefendido.put("APELLIDO1_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO1_PJG"));
								registroDefendido.put("APELLIDO1_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO1_PJG")).toUpperCase());
								registroDefendido.put("APELLIDO2_REPR_DEF", (String) representanteLegalDefendido.get("APELLIDO2_PJG"));
								registroDefendido.put("APELLIDO2_REPR_DEF_MAYUS", ((String) representanteLegalDefendido.get("APELLIDO2_PJG")).toUpperCase());
								String sexoRepr_def = UtilidadesString.getMensajeIdioma(usrbean, (String) representanteLegalDefendido.get("SEXO_PJG"));	
								registroDefendido.put("SEXO_REPR_DEF", sexoRepr_def);
								registroDefendido.put("O_A_REPR_DEF", (String) representanteLegalDefendido.get("O_A_PJG"));
								registroDefendido.put("EL_LA_REPR_DEF", (String) representanteLegalDefendido.get("EL_LA_PJG"));
								registroDefendido.put("NIFCIF_REPR_DEF", (String) representanteLegalDefendido.get("NIF_PJG"));
								registroDefendido.put("IDPERSONA_REPR_DEF", (String) representanteLegalDefendido.get("IDPERSONA_PJG"));
								registroDefendido.put("IDDIRECCION_REPR_DEF", (String) representanteLegalDefendido.get("IDDIRECCION_PJG"));
								registroDefendido.put("DOMICILIO_REPR_DEF", (String) representanteLegalDefendido.get("DOMICILIO_PJG"));
								registroDefendido.put("CODIGOPOSTAL_REPR_DEF", (String) representanteLegalDefendido.get("CP_PJG"));
								registroDefendido.put("TELEFONO1_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO1_PJG"));
								registroDefendido.put("TELEFONO2_REPR_DEF", (String) representanteLegalDefendido.get("TELEFONO2_PJG"));
								registroDefendido.put("MOVIL_REPR_DEF", (String) representanteLegalDefendido.get("MOVIL_PJG"));
								registroDefendido.put("LISTA_TELEFONOS_REPR_DEF", (String) representanteLegalDefendido.get("LISTA_TELEFONOS_REPR"));
								registroDefendido.put("FAX1_REPR_DEF", (String) representanteLegalDefendido.get("FAX_PJG"));
								registroDefendido.put("CORREOELECTRONICO_REPR_DEF", (String) representanteLegalDefendido.get("CORREOELECTRONICO_PJG"));
								registroDefendido.put("NOMBRE_POBLACION_REPR_DEF", (String) representanteLegalDefendido.get("POBLACION_PJG"));
								registroDefendido.put("NOMBRE_PROVINCIA_REPR_DEF", (String) representanteLegalDefendido.get("PROVINCIA_PJG"));
							
							}else{
								
								
								registroDefendido.put("NOMBRE_REPR_DEF", "");
								registroDefendido.put("NOMBRE_REPR_DEF_MAYUS", "");
								registroDefendido.put("APELLIDO1_REPR_DEF", "");
								registroDefendido.put("APELLIDO1_REPR_DEF_MAYUS","");
								registroDefendido.put("APELLIDO2_REPR_DEF", "");
								registroDefendido.put("APELLIDO2_REPR_DEF_MAYUS", "");
								registroDefendido.put("SEXO_REPR_DEF", "");
								registroDefendido.put("O_A_REPR_DEF", "");
								registroDefendido.put("EL_LA_REPR_DEF", "");
								registroDefendido.put("NIFCIF_REPR_DEF", "");
								registroDefendido.put("IDPERSONA_REPR_DEF", "");
								registroDefendido.put("IDDIRECCION_REPR_DEF", "");
								registroDefendido.put("DOMICILIO_REPR_DEF", "");
								registroDefendido.put("CODIGOPOSTAL_REPR_DEF","");
								registroDefendido.put("TELEFONO1_REPR_DEF", "");
								registroDefendido.put("TELEFONO2_REPR_DEF", "");
								registroDefendido.put("MOVIL_REPR_DEF", "");
								registroDefendido.put("LISTA_TELEFONOS_REPR_DEF", "");
								registroDefendido.put("FAX1_REPR_DEF", "");
								registroDefendido.put("CORREOELECTRONICO_REPR_DEF", "");
								registroDefendido.put("NOMBRE_POBLACION_REPR_DEF","");
								registroDefendido.put("NOMBRE_PROVINCIA_REPR_DEF", "");
						
							}

							if(k==0){
								registro.putAll(registroDefendido);
							}
						} // END FOR
						registro.put("defendido", vDefendidos);						
					}else{
						registro.put("CODIGOLENGUAJE", idiomaInforme);
						registro.put("LISTA_TELEFONOS_INTERESADO", "");
						registro.put("NIF_DEFENDIDO", "");
						registro.put("NOMBRE_DEFENDIDO", "");
						registro.put("DOMICILIO_DEFENDIDO", "");
						registro.put("CP_DEFENDIDO", "");
						registro.put("POBLACION_DEFENDIDO", "");
						registro.put("PROVINCIA_DEFENDIDO", "");
						registro.put("TELEFONO1_DEFENDIDO", "");
						registro.put("SEXO_DEFENDIDO", "");
						registro.put("IDLENGUAJE_DEFENDIDO", "");
						registro.put("CALIDAD_DEFENDIDO", "");
						registro.put("OBS_DEFENDIDO", "");		
						registro.put("O_A_DEFENDIDO", "");
						registro.put("EL_LA_DEFENDIDO", "");
						registro.put("ESTADOCIVIL_DEFENDIDO", "");
						registro.put("NOMBRE_PAIS", "");
						registro.put("IDPERSONAINTERESADO", "");
						
						
						//representante del defendido
						registro.put("NOMBRE_REPR_DEF", "");
						registro.put("NOMBRE_REPR_DEF_MAYUS", "");
						registro.put("APELLIDO1_REPR_DEF", "");
						registro.put("APELLIDO1_REPR_DEF_MAYUS","");
						registro.put("APELLIDO2_REPR_DEF", "");
						registro.put("APELLIDO2_REPR_DEF_MAYUS", "");
						registro.put("SEXO_REPR_DEF", "");
						registro.put("O_A_REPR_DEF", "");
						registro.put("EL_LA_REPR_DEF", "");
						registro.put("NIFCIF_REPR_DEF", "");
						registro.put("IDPERSONA_REPR_DEF", "");
						registro.put("IDDIRECCION_REPR_DEF", "");
						registro.put("DOMICILIO_REPR_DEF", "");
						registro.put("CODIGOPOSTAL_REPR_DEF","");
						registro.put("TELEFONO1_REPR_DEF", "");
						registro.put("TELEFONO2_REPR_DEF", "");
						registro.put("MOVIL_REPR_DEF", "");
						registro.put("LISTA_TELEFONOS_REPR_DEF", "");
						registro.put("FAX1_REPR_DEF", "");
						registro.put("CORREOELECTRONICO_REPR_DEF", "");
						registro.put("NOMBRE_POBLACION_REPR_DEF","");
						registro.put("NOMBRE_PROVINCIA_REPR_DEF", "");
						
						
						//representante del defendido
						registro.put("NOMBRE_REPR_DEF", "");
						registro.put("NOMBRE_REPR_DEF_MAYUS", "");
						registro.put("APELLIDO1_REPR_DEF", "");
						registro.put("APELLIDO1_REPR_DEF_MAYUS","");
						registro.put("APELLIDO2_REPR_DEF", "");
						registro.put("APELLIDO2_REPR_DEF_MAYUS", "");
						registro.put("SEXO_REPR_DEF", "");
						registro.put("O_A_REPR_DEF", "");
						registro.put("EL_LA_REPR_DEF", "");
						registro.put("NIFCIF_REPR_DEF", "");
						registro.put("IDPERSONA_REPR_DEF", "");
						registro.put("IDDIRECCION_REPR_DEF", "");
						registro.put("DOMICILIO_REPR_DEF", "");
						registro.put("CODIGOPOSTAL_REPR_DEF","");
						registro.put("TELEFONO1_REPR_DEF", "");
						registro.put("TELEFONO2_REPR_DEF", "");
						registro.put("MOVIL_REPR_DEF", "");
						registro.put("LISTA_TELEFONOS_REPR_DEF", "");
						registro.put("FAX1_REPR_DEF", "");
						registro.put("CORREOELECTRONICO_REPR_DEF", "");
						registro.put("NOMBRE_POBLACION_REPR_DEF","");
						registro.put("NOMBRE_PROVINCIA_REPR_DEF", "");
						
						
					}
					
					// Control de datos para el informe
					if(registro.get("FECHARESOLUCIONCAJG")==null)
//					if((String)registro.get("FECHARESOLUCIONCAJG")!=null && !((String)registro.get("FECHARESOLUCIONCAJG")).trim().equals(""))
						registro.put("FECHARESOLUCIONCAJG", "");
					
//					if((String)registro.get("FECHARESOLUCIONCAJGLETRA")!=null && !((String)registro.get("FECHARESOLUCIONCAJGLETRA")).trim().equals(""))
					if(registro.get("FECHARESOLUCIONCAJGLETRA")==null)
						registro.put("FECHARESOLUCIONCAJGLETRA", "");
					
//					if((String)registro.get("NUMERO_EJG")!=null && !((String)registro.get("NUMERO_EJG")).trim().equals(""))
					if(registro.get("NUMERO_EJG")==null)
						registro.put("NUMERO_EJG", "");
					
//					if((String)registro.get("FECHAAPERTURA_EJG")!=null && !((String)registro.get("FECHAAPERTURA_EJG")).trim().equals(""))
					if(registro.get("FECHAAPERTURA_EJG")==null)
						registro.put("FECHAAPERTURA_EJG", "");
					
//					if((String)registro.get("FECHAAPERTURA_EJG_LETRA")!=null && !((String)registro.get("FECHAAPERTURA_EJG_LETRA")).trim().equals(""))
					if(registro.get("FECHAAPERTURA_EJG_LETRA")==null)
						registro.put("FECHAAPERTURA_EJG_LETRA", "");
					
//					if((String)registro.get("PARRAFO_LETRADO_PROCURADOR")!=null && !((String)registro.get("PARRAFO_LETRADO_PROCURADOR")).trim().equals(""))
					if(registro.get("PARRAFO_LETRADO_PROCURADOR")==null)
						registro.put("PARRAFO_LETRADO_PROCURADOR", "");					
					
					// Como desdobla, no muestra las siguientes etiquetas individualmente

					
					vSalida.add(registro);
				}		
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getDatosSalidaOficio");
		}
		return vSalida;
	}	
	
	public Vector getDesignaSalidaOficio (String idInstitucion,
										  String idturno,
										  String anio,
										  String numero,
										  String codigoDesigna,
										  String idioma)
		throws ClsExceptions  
	{
		try {
			Hashtable h = new Hashtable();
			
			h.put(new Integer(1), idInstitucion);
			h.put(new Integer(2), idturno);
			h.put(new Integer(3), anio);
			h.put(new Integer(4), numero);
			String sql = 
				"Select Des.Idinstitucion, " +
				"       Des.Idturno, " +
				"       Des.Anio, " +
				"       Des.Numero, " +
				"       Let.Idpersona, " +
				"       Des.Idinstitucion_Juzg, " +
				"       Des.Idjuzgado, " +
				"       Des.Observaciones As Observaciones, " +
				"       Des.DELITOS As COMENTARIO_DELITOS, " +
				"       Decode(Des.anioprocedimiento, null, Des.Numprocedimiento, (Des.Numprocedimiento || '/' || Des.anioprocedimiento))  As Autos, " +
				"       Des.Numprocedimiento As NUMEROPROCEDIMIENTO, " +
				"       Des.anioprocedimiento As ANIOPROCEDIMIENTO, " +
				"       Decode(Des.anioprocedimiento, null, Des.Numprocedimiento, (Des.Numprocedimiento || '/' || Des.anioprocedimiento))  As NUMEROANIOPROCEDIMIENTO, " +				
				"       To_Char(Des.Fechajuicio, 'dd/MM/yyyy') As Fecha_Juicio, " +
				"       To_Char(Des.Fechajuicio, 'HH24:MI') As Hora_Juicio, " +
				"       Des.Anio As Anio_Designa, " +
				"       Des.Codigo As Codigo, " +
				"       Des.Resumenasunto As Asunto, " +
				"       Des.Idprocedimiento As Idprocedimiento, " +
				"       Des.NIG, " +
				"       Des.Anio || '/' || Des.Codigo As Noficio, " +
				"       To_Char(Des.Fechaentrada, 'dd-mm-yyyy') As Fecha_Designa, " +
				"		To_Char(Des.Fechafin, 'dd-mm-yyyy') AS FECHA_CIERRE, "+	
				" 		PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(Des.Fechaentrada, 'M', "+idioma+") FECHADESIGNAENLETRA, " +
				" 		PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(Des.Fechafin, 'M', "+idioma+") FECHA_CIERRE_LETRA, " +				
				"       To_Char(Sysdate, 'dd') As Dia_Actual, " +
				" 		To_Char(SYSDATE, 'dd/mm/yyyy') AS MESACTUAL, " +			
				"       To_Char(Sysdate, 'yyyy') As Anio_Actual, " +
				"       Des.Idtipodesignacolegio As Idtipodesignacolegio, " +
				"       To_Char(Des.Fecharecepcioncolegio, 'dd/MM/yyyy') As Fecha_Recepcion_Colegio, " +
				"       To_Char(Des.Fechaoficiojuzgado, 'dd/MM/yyyy') As Fecha_Oficio_Juzgado, " +
				"        " +
				"       p.Nombre || ' ' || p.Apellidos1 || " +
				"       Decode(p.Apellidos2, Null, '', ' ' || p.Apellidos2) || ' ' || " +
				"       Decode(Let.IdinstitucionOrigen, Null, f_Siga_Calculoncolegiado(Let.Idinstitucion, Let.Idpersona),  f_Siga_Calculoncolegiado(Let.IdinstitucionOrigen, Let.Idpersona)) Letrado_Actual, " +
				"       f_Siga_Calculoncolegiado(Let.IdinstitucionOrigen, Let.Idpersona) Letrado_Actual_Origen, " +
				//"       f_Siga_Calculoncolegiado(Let.Idinstitucion, Let.Idpersona) Letrado_Actual, " +
				"       Let.IdinstitucionOrigen, " +
				"       (SELECT cli.Idlenguaje FROM Cen_Cliente cli Where cli.Idinstitucion = Let.Idinstitucion And cli.Idpersona = Let.Idpersona) as Idioma_Letrado,  " +
				"       Pant.Nombre || ' ' || Pant.Apellidos1 || " +
				"       Decode(Pant.Apellidos2, Null, '', ' ' || Pant.Apellidos2) || ' ' || " +
				"       f_Siga_Calculoncolegiado(Letant.Idinstitucion, Letant.Idpersona) Ultimo_Letrado_Sustituido, " +
				"        " +
				"       Letant.Fecharenuncia Fecharenuncia, " +
				"       To_Char(Letant.Fecharenuncia,'dd/mm/yyyy') AS FECHARENUNCIAENLETRA, "		+		
				"       Letant.Fecharenunciasolicita Fecha_Solicitudrenuncia, " +
				"       To_Char(Letant.Fecharenunciasolicita,'dd/mm/yyyy') AS FECHASOLICITUDRENUNCIA, " +				
				" 		Des.Idpretension AS Idpretension, " + 
				"		Letant.OBSERVACIONES AS OBSERVACIONES_ULTIMOLETRADO, " +
				"		Let.OBSERVACIONES AS OBSERVACIONES_CAMBIOLETRADO "+
				"  From Scs_Designa         Des, " +
				"       Scs_Designasletrado Let, " +
				"       Cen_Persona         p, " +
				"       Scs_Designasletrado Letant, " +
				"       Cen_Persona         Pant " +
				" Where Des.Idinstitucion = Let.Idinstitucion(+) " +
				"   And Des.Idturno = Let.Idturno(+) " +
				"   And Des.Anio = Let.Anio(+) " +
				"   And Des.Numero = Let.Numero(+) " +
				"   And Let.Idpersona = p.Idpersona(+) " +
				"   And Let.Fechadesigna(+) = " +
				"       f_Siga_Getfecha_Letradodesigna(Des.Idinstitucion, " +
				"                                      Des.Idturno, " +
				"                                      Des.Anio, " +
				"                                      Des.Numero) " +
				"   And Des.Idinstitucion = Letant.Idinstitucion(+) " +
				"   And Des.Idturno = Letant.Idturno(+) " +
				"   And Des.Anio = Letant.Anio(+) " +
				"   And Des.Numero = Letant.Numero(+) " +
				"   And Letant.Idpersona = Pant.Idpersona(+) " +
				"   And Letant.Fechadesigna(+) = " +
				"       f_Siga_Getfecha_Let_Antdesigna(Des.Idinstitucion, " +
				"                                      Des.Idturno, " +
				"                                      Des.Anio, " +
				"                                      Des.Numero) " +
				"   " +
				"   And Des.Idinstitucion = :1 " +
				"   And Des.Idturno = :2 " +
				"   And Des.Anio = :3 " +
				"   And Des.Numero = :4";
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql, h);
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre el letrado designado.");
		}
	} //getDesignaSalidaOficio()
	
	public Vector getDefendidosDesignaSalidaOficio(String idInstitucion, String numero, String idTurno, String anio, String idPersonaJG,String idPersona, String longitudNumEjg)
			throws ClsExceptions {
		try {

			Vector defendidos = getVectorDefendidosDesigna(idInstitucion, numero, idTurno, anio, idPersonaJG, idPersona,longitudNumEjg);
//			Vector datos = getDatosEJGDefendidoDesigna(idInstitucion, numero, idTurno, anio);
			if (defendidos != null && defendidos.size() > 0) {
//				
				return defendidos;

			} else {
				Vector solicitantes = getSolicitantesEJGDesigna(idInstitucion, numero, idTurno, anio, idPersonaJG,longitudNumEjg);
				return solicitantes;
			}
				
			
			
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDefendidosDesignaSalidaOficio.");
		}
	}
	
	public Vector getDefendidosDesignaInforme(String idInstitucion, String anio, String idTurno, String numero,String idPersonaJG, String longitudNumEjg)
			throws ClsExceptions {
		try {
			ScsDefendidosDesignaAdm defendidosDesignaAdm = new ScsDefendidosDesignaAdm(usrbean);
			Vector defendidos = defendidosDesignaAdm.getDefendidosDesigna(Integer.valueOf(idInstitucion),Integer.valueOf(anio), Integer.valueOf(numero), Integer.valueOf(idTurno) ,idPersonaJG);

			if (defendidos != null && defendidos.size() > 0) {
				return defendidos;

			} else {
				Vector solicitantes = getSolicitantesEJGDesigna(idInstitucion, numero, idTurno, anio,idPersonaJG,longitudNumEjg);
				return solicitantes;
			}
				
			
			
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getDefendidosDesigna.");
		}
	}
	
	
	public Vector getVectorDefendidosDesigna(String idInstitucion, String numero, String idTurno, String anio, String idPersonaJG, String idPersona, Object longitudNumEjg){
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), idTurno);
		h.put(new Integer(3), anio);
		h.put(new Integer(4), numero);
		h.put(new Integer(5), idInstitucion);
		h.put(new Integer(6), idTurno);
		h.put(new Integer(7), anio);
		h.put(new Integer(8), numero);

		StringBuffer sql = new StringBuffer("SELECT DEF.IDINSTITUCION, ");
		Vector defendidos = null;
		sql.append(" DEF.IDTURNO, ");
		sql.append(" DEF.ANIO, ");
		sql.append(" DEF.NUMERO, ");
		sql.append(" PERJG.IDPERSONA IDPERSONAINTERESADO, ");
		sql.append(" PERJG.NOMBRE || ' ' || PERJG.APELLIDO1 || ' ' ||  PERJG.APELLIDO2 AS NOMBRE_DEFENDIDO, ");	
		sql.append(" DECODE(PERJG.DIRECCION, NULL, NULL, ((SELECT (UPPER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+this.usrbean.getLanguage()+"), 1, 1))) || (LOWER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+this.usrbean.getLanguage()+"), 2))) FROM CEN_TIPOVIA TV WHERE TV.IDTIPOVIA = PERJG.IDTIPOVIA AND TV.IDINSTITUCION = PERJG.IDINSTITUCION) || ' ' || PERJG.DIRECCION || ' ' || PERJG.NUMERODIR || ' ' || PERJG.ESCALERADIR || ' ' || PERJG.PISODIR || ' ' || PERJG.PUERTADIR)) AS DOMICILIO_DEFENDIDO, ");
		sql.append(" PERJG.CODIGOPOSTAL AS CP_DEFENDIDO, ");
		sql.append(" POB.NOMBRE AS POBLACION_DEFENDIDO, ");
		sql.append(" PROV.NOMBRE AS PROVINCIA_DEFENDIDO, ");
		sql.append(" PAIS.NOMBRE AS NOMBRE_PAIS, ");
		sql.append(" perjg.OBSERVACIONES AS OBS_DEFENDIDO, ");
		sql.append(" (SELECT TEL2.NUMEROTELEFONO ");
		sql.append(" FROM SCS_TELEFONOSPERSONA TEL2 ");
		sql.append(" WHERE TEL2.IDINSTITUCION = PERJG.IDINSTITUCION ");
		sql.append(" AND TEL2.IDPERSONA = PERJG.IDPERSONA ");
		sql.append(" AND ROWNUM < 2) AS TELEFONO1_DEFENDIDO, ");
		sql.append("(SELECT WMSYS.WM_CONCAT(LTEL.NOMBRETELEFONO||':'||LTEL.NUMEROTELEFONO) from SCS_TELEFONOSPERSONA LTEL WHERE LTEL.IDINSTITUCION = PERJG.IDINSTITUCION AND LTEL.IDPERSONA = PERJG.IDPERSONA) AS LISTA_TELEFONOS_INTERESADO,");
		sql.append(" PERJG.NIF AS NIF_DEFENDIDO, ");
		sql.append(" DECODE(PERJG.SEXO,  null,  null,  'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_DEFENDIDO, ");
		sql.append(" DECODE(PERJG.SEXO, 'H','o','a') AS O_A_DEFENDIDO, ");
		sql.append(" DECODE(PERJG.SEXO, 'H','el','la') AS EL_LA_DEFENDIDO, ");
		sql.append(" PERJG.IDLENGUAJE AS IDLENGUAJE_DEFENDIDO, ");
		sql.append(" (SELECT ejg.anio  ");
		sql.append(" FROM SCS_EJG               ejg,  ");
		sql.append(" Scs_Ejgdesigna        ejgdes,  ");
		sql.append(" scs_unidadfamiliarejg ufa  ");
		sql.append(" WHERE def.IDINSTITUCION  = ejgdes.IDINSTITUCION  ");
		sql.append(" AND def.idturno = ejgdes.idturno  ");
		sql.append(" AND def.anio = ejgdes.aniodesigna  ");
		sql.append(" AND def.Numero = ejgdes.Numerodesigna  ");			
		sql.append(" AND ejgdes.IDINSTITUCION = ejg.IDINSTITUCION  ");
		sql.append(" AND ejgdes.Idtipoejg = ejg.IDTIPOEJG  ");
		sql.append(" AND ejgdes.anioejg = ejg.anio  ");
		sql.append(" AND ejgdes.numeroejg = ejg.numero  ");		
		sql.append(" AND ejg.IDINSTITUCION = ufa.IDINSTITUCION  ");
		sql.append(" AND ejg.Idtipoejg = ufa.IDTIPOEJG  ");
		sql.append(" AND ejg.anio = ufa.anio  ");
		sql.append(" AND ejg.numero = ufa.numero  ");		
		sql.append(" AND def.IDINSTITUCION = ufa.IDINSTITUCION  ");
		sql.append(" AND def.idpersona = ufa.idpersona  ");
		sql.append(" and rownum = 1)  ");
		sql.append(" AS ANIO_EJG,  ");
		sql.append(" (SELECT (ejg.ANIO || '/' || LPAD(ejg.NUMEJG,");
		sql.append(longitudNumEjg);
		sql.append(",0)   ");
		sql.append(")   ");
		sql.append(" FROM SCS_EJG               ejg,  ");
		sql.append(" Scs_Ejgdesigna        ejgdes,  ");
		sql.append(" scs_unidadfamiliarejg ufa  ");
		sql.append(" WHERE def.IDINSTITUCION  = ejgdes.IDINSTITUCION  ");
		sql.append(" AND def.idturno = ejgdes.idturno  ");
		sql.append(" AND def.anio = ejgdes.aniodesigna  ");
		sql.append(" AND def.Numero = ejgdes.Numerodesigna  ");			
		sql.append(" AND ejgdes.IDINSTITUCION = ejg.IDINSTITUCION  ");
		sql.append(" AND ejgdes.Idtipoejg = ejg.IDTIPOEJG  ");
		sql.append(" AND ejgdes.anioejg = ejg.anio  ");
		sql.append(" AND ejgdes.numeroejg = ejg.numero  ");		
		sql.append(" AND ejg.IDINSTITUCION = ufa.IDINSTITUCION  ");
		sql.append(" AND ejg.Idtipoejg = ufa.IDTIPOEJG  ");
		sql.append(" AND ejg.anio = ufa.anio  ");
		sql.append(" AND ejg.numero = ufa.numero  ");		
		sql.append(" AND def.IDINSTITUCION = ufa.IDINSTITUCION  ");
		sql.append(" AND def.idpersona = ufa.idpersona  ");
		sql.append(" and rownum = 1)  ");
		sql.append(" AS NUMERO_EJG, ");		
		sql.append(" (SELECT to_char(ejg.FECHARESOLUCIONCAJG, 'dd/mm/yyyy')  ");
		sql.append(" FROM SCS_EJG               ejg,  ");
		sql.append(" Scs_Ejgdesigna        ejgdes,  ");
		sql.append(" scs_unidadfamiliarejg ufa  ");
		sql.append(" WHERE def.IDINSTITUCION  = ejgdes.IDINSTITUCION  ");
		sql.append(" AND def.idturno = ejgdes.idturno  ");
		sql.append(" AND def.anio = ejgdes.aniodesigna  ");
		sql.append(" AND def.Numero = ejgdes.Numerodesigna  ");		
		sql.append(" AND ejgdes.IDINSTITUCION = ejg.IDINSTITUCION  ");
		sql.append(" AND ejgdes.Idtipoejg = ejg.IDTIPOEJG  ");
		sql.append(" AND ejgdes.anioejg = ejg.anio  ");
		sql.append(" AND ejgdes.numeroejg = ejg.numero  ");		
		sql.append(" AND ejg.IDINSTITUCION = ufa.IDINSTITUCION  ");
		sql.append(" AND ejg.Idtipoejg = ufa.IDTIPOEJG  ");
		sql.append(" AND ejg.anio = ufa.anio  ");
		sql.append(" AND ejg.numero = ufa.numero  ");		
		sql.append(" AND def.IDINSTITUCION = ufa.IDINSTITUCION  ");
		sql.append(" AND def.idpersona = ufa.idpersona  ");
		sql.append(" and rownum = 1)  ");
		sql.append(" AS FECHARESOLUCIONCAJG, ");		
		sql.append(" (SELECT COUNT(1) ");
		sql.append(" FROM scs_ejgdesigna ejgdes ");
		sql.append(" WHERE ejgdes.idinstitucion  = :1  ");
		sql.append(" AND ejgdes.IDTURNO = :2 ");
		sql.append(" AND ejgdes.Aniodesigna = :3 ");
		sql.append(" AND ejgdes.Numerodesigna = :4 ) COUNT_EJG, ");

		sql.append(" CAL.DESCRIPCION AS CALIDAD_DEFENDIDO, ");
		sql.append(" CAL.IDTIPOENCALIDAD, ");
		sql.append(" PERJG.IDREPRESENTANTEJG IDREPRESENTANTEJG ");
		sql.append(" FROM SCS_DEFENDIDOSDESIGNA DEF, ");
		sql.append(" SCS_PERSONAJG         PERJG, ");
		sql.append(" scs_tipoencalidad     CAL, ");
		sql.append(" CEN_POBLACIONES       POB, ");
		sql.append(" CEN_PROVINCIAS        PROV, ");
		sql.append(" CEN_PAIS              PAIS ");
		sql.append(" WHERE PERJG.IDINSTITUCION = DEF.IDINSTITUCION ");
		sql.append(" AND PERJG.IDPERSONA = DEF.IDPERSONA ");
		sql.append(" AND cal.idtipoencalidad(+) = def.idtipoencalidad ");
		sql.append(" AND cal.Idinstitucion(+) = def.Idinstitucion ");
		sql.append(" AND PERJG.IDPOBLACION = POB.IDPOBLACION(+) ");
		sql.append(" AND PERJG.IDPROVINCIA = PROV.IDPROVINCIA(+) ");
		sql.append(" AND PERJG.IDPAIS = PAIS.IDPAIS(+) ");

		sql.append(" AND DEF.IDINSTITUCION = :5 ");
		sql.append(" AND DEF.IDTURNO = :6 ");
		sql.append(" AND DEF.ANIO = :7 ");
		sql.append(" AND DEF.NUMERO = :8  ");
		if (idPersonaJG != null && !idPersonaJG.trim().equals("")) {
			h.put(new Integer(9), idPersonaJG);
			sql.append(" AND PERJG.IDPERSONA = :9  ");
		}
		sql.append(" ORDER BY PERJG.IDPERSONA");
		

		try {
			defendidos = this.ejecutaSelectBind(sql.toString(), h);
		} catch (ClsExceptions e) {
			e.printStackTrace();
		}

		return defendidos;
	}
	
	public Vector getSolicitantesEJGDesigna(String idInstitucion, String numero, String idTurno, String anio, String idPersonaJG, String longitudNumEjg) {

		Vector solicitantes = null;
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), idTurno);
		h.put(new Integer(3), anio);
		h.put(new Integer(4), numero);

		StringBuffer sql = new StringBuffer("SELECT DES.IDINSTITUCION, ");
		sql.append(" des.IDTURNO, ");
		sql.append(" des.Aniodesigna, ");
		sql.append(" des.Numerodesigna, ");
		sql.append(" PERJG.IDPERSONA IDPERSONAINTERESADO, ");
		sql.append(" PERJG.NOMBRE || ' ' || PERJG.APELLIDO1 || ' ' ||  PERJG.APELLIDO2 AS NOMBRE_DEFENDIDO, ");	
		sql.append(" DECODE(PERJG.DIRECCION, NULL, NULL, ((SELECT (UPPER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+this.usrbean.getLanguage()+"), 1, 1))) || (LOWER(SUBSTR(F_SIGA_GETRECURSO(TV.DESCRIPCION,"+this.usrbean.getLanguage()+"), 2))) FROM CEN_TIPOVIA TV WHERE TV.IDTIPOVIA = PERJG.IDTIPOVIA AND TV.IDINSTITUCION = PERJG.IDINSTITUCION) || ' ' || PERJG.DIRECCION || ' ' || PERJG.NUMERODIR || ' ' || PERJG.ESCALERADIR || ' ' || PERJG.PISODIR || ' ' || PERJG.PUERTADIR)) AS DOMICILIO_DEFENDIDO, ");
		sql.append(" PERJG.CODIGOPOSTAL AS CP_DEFENDIDO, ");
		sql.append(" POB.NOMBRE AS POBLACION_DEFENDIDO, ");
		sql.append(" PROV.NOMBRE AS PROVINCIA_DEFENDIDO, ");
		sql.append(" PAIS.NOMBRE AS NOMBRE_PAIS, ");
		sql.append(" perjg.OBSERVACIONES AS OBS_DEFENDIDO, ");
		sql.append(" (SELECT TEL2.NUMEROTELEFONO ");
		sql.append(" FROM SCS_TELEFONOSPERSONA TEL2 ");
		sql.append(" WHERE TEL2.IDINSTITUCION = PERJG.IDINSTITUCION ");
		sql.append(" AND TEL2.IDPERSONA = PERJG.IDPERSONA ");
		sql.append(" AND ROWNUM < 2) AS TELEFONO1_DEFENDIDO, ");
		sql.append("(SELECT WMSYS.WM_CONCAT(LTEL.NOMBRETELEFONO||':'||LTEL.NUMEROTELEFONO) from SCS_TELEFONOSPERSONA LTEL WHERE LTEL.IDINSTITUCION = PERJG.IDINSTITUCION AND LTEL.IDPERSONA = PERJG.IDPERSONA) AS LISTA_TELEFONOS_INTERESADO,");
		sql.append(" PERJG.NIF AS NIF_DEFENDIDO, ");
		sql.append(" DECODE(PERJG.SEXO,  null,  null,  'M','gratuita.personaEJG.sexo.mujer','gratuita.personaEJG.sexo.hombre') AS SEXO_DEFENDIDO, ");
		sql.append(" DECODE(PERJG.SEXO, 'H','o','a') AS O_A_DEFENDIDO, ");
		sql.append(" DECODE(PERJG.SEXO, 'H','el','la') AS EL_LA_DEFENDIDO, ");
		sql.append(" PERJG.IDLENGUAJE AS IDLENGUAJE_DEFENDIDO, ");		
		sql.append(" ejg.anio ANIO_EJG,  ");
		
		
		sql.append(" ejg.ANIO || '/' || lpad(ejg.NUMEJG,"+longitudNumEjg+",0) AS NUMERO_EJG, ");
		sql.append(" to_char(ejg.FECHARESOLUCIONCAJG, 'dd/mm/yyyy') AS FECHARESOLUCIONCAJG, ");
		sql.append(" CAL.DESCRIPCION AS CALIDAD_DEFENDIDO, ");
		sql.append(" CAL.IDTIPOENCALIDAD, ");
		sql.append(" PERJG.IDREPRESENTANTEJG IDREPRESENTANTEJG ");

		sql.append(" FROM SCS_EJG          ejg, ");
		sql.append(" Scs_Ejgdesigna        des, ");
		sql.append(" Scs_Unidadfamiliarejg UFA, ");
		sql.append(" SCS_PERSONAJG         PERJG, ");
		sql.append(" scs_tipoencalidad     CAL, ");
		sql.append(" CEN_POBLACIONES       POB, ");
		sql.append(" CEN_PROVINCIAS        PROV, ");
		sql.append(" CEN_PAIS              PAIS ");
		sql.append(" WHERE cal.idtipoencalidad(+) = ejg.idtipoencalidad ");
		sql.append(" AND cal.Idinstitucion(+) = ejg.Idinstitucion ");

		sql.append(" AND UFA.IDINSTITUCION = PERJG.IDINSTITUCION ");
		sql.append(" AND UFA.SOLICITANTE = 1 ");
		sql.append(" AND UFA.IDPERSONA = PERJG.IDPERSONA ");
		sql.append(" AND UFA.IDINSTITUCION = ejg.IDINSTITUCION ");
		sql.append(" AND UFA.IDTIPOEJG = ejg.IDTIPOEJG ");
		sql.append(" AND UFA.ANIO = ejg.ANIO ");
		sql.append(" AND UFA.NUMERO = ejg.NUMERO ");

		sql.append(" AND des.IDINSTITUCION = ejg.IDINSTITUCION ");
		sql.append(" AND des.Idtipoejg = ejg.IDTIPOEJG ");
		sql.append(" AND des.Anioejg = ejg.ANIO ");
		sql.append(" AND des.Numeroejg = ejg.NUMERO ");

		sql.append(" AND PERJG.IDPOBLACION = POB.IDPOBLACION(+) ");
		sql.append(" AND PERJG.IDPROVINCIA = PROV.IDPROVINCIA(+) ");
		sql.append(" AND PERJG.IDPAIS = PAIS.IDPAIS(+) ");

		sql.append(" AND DES.IDINSTITUCION = :1 ");
		sql.append(" AND DES.IDTURNO = :2 ");
		sql.append(" AND DES.ANIODESIGNA = :3 ");
		sql.append(" AND DES.NUMERODESIGNA = :4  ");
		if (idPersonaJG != null && !idPersonaJG.trim().equals("")) {
			h.put(new Integer(5), idPersonaJG);
			sql.append(" AND PERJG.IDPERSONA = :5  ");
		}

		try {
			solicitantes = this.ejecutaSelectBind(sql.toString(), h);
		} catch (ClsExceptions e) {
			e.printStackTrace();
		}

		return solicitantes;
	}
	
	
	
	
	
	
	
	public Vector getDatosEJGDefendidoDesigna(String idInstitucion, String numero, String idTurno, String anio, String longitudNumEjg) throws ClsExceptions {

		Vector datos = null;
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), idTurno);
		h.put(new Integer(3), anio);
		h.put(new Integer(4), numero);

		StringBuffer sql = new StringBuffer("SELECT ejg.ANIO ANIO_EJG, (ejg.ANIO || '/' || lpad(ejg.NUMEJG,"+longitudNumEjg+",0)) AS NUMERO_EJG ");
		sql.append(" FROM SCS_EJG ejg, Scs_Ejgdesigna des ");
		sql.append(" WHERE des.IDINSTITUCION = ejg.IDINSTITUCION ");
		sql.append(" AND des.Idtipoejg = ejg.IDTIPOEJG ");
		sql.append(" AND des.Anioejg = ejg.ANIO ");
		sql.append(" AND des.Numeroejg = ejg.NUMERO ");

		sql.append(" AND DES.IDINSTITUCION = :1 ");
		sql.append(" AND DES.IDTURNO = :2 ");
		sql.append(" AND DES.ANIODESIGNA = :3 ");
		sql.append(" AND DES.NUMERODESIGNA = :4  ");

		try {
			datos = this.ejecutaSelectBind(sql.toString(), h);
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e,e.toString()); 
		}

		return datos;
	}
	
	public Vector getDatosEJG(String idInstitucion, String numero, String idTurno, String anio, String longitudNumEjg) throws ClsExceptions {

		Vector datos = null;
		Hashtable h = new Hashtable();
		h.put(new Integer(1), idInstitucion);
		h.put(new Integer(2), idTurno);
		h.put(new Integer(3), anio);
		h.put(new Integer(4), numero);

		StringBuffer sql = new StringBuffer("SELECT ejg.ANIO ANIO_EJG, ejg.numero AS NUMERO_EJG, ");
		sql.append(" ejg.idtipoejg AS TIPO_EJG, lpad(ejg.numejg,"+longitudNumEjg+",0) AS NUM_EJG ");
		sql.append(" FROM SCS_EJG ejg, Scs_Ejgdesigna des ");
		sql.append(" WHERE des.IDINSTITUCION = ejg.IDINSTITUCION ");
		sql.append(" AND des.Idtipoejg = ejg.IDTIPOEJG ");
		sql.append(" AND des.Anioejg = ejg.ANIO ");
		sql.append(" AND des.Numeroejg = ejg.NUMERO ");

		sql.append(" AND DES.IDINSTITUCION = :1 ");
		sql.append(" AND DES.IDTURNO = :2 ");
		sql.append(" AND DES.ANIODESIGNA = :3 ");
		sql.append(" AND DES.NUMERODESIGNA = :4  ");

		try {
			datos = this.ejecutaSelectBind(sql.toString(), h);
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e,e.toString()); 
		}

		return datos;
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
					"gratuita.literal.numeroDesignacion")+": "+ anio+"/"+ contador; 
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
					" Scs_actuaciondesigna.observacionesjustificacion OBSERVACIONES_JUSTIF_UA, " +
					" Scs_actuaciondesigna.NUMEROPROCEDIMIENTO NUMEROPROCEDIMIENTO_UA " +
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
		        sql.append(" and des.anio = edes.aniodesigna ");
		        sql.append(" and des.idturno = edes.idturno ");
		        sql.append(" and ejg.numero = edes.numeroejg ");
		        sql.append(" and ejg.idtipoejg = edes.idtipoejg ");		       
		        sql.append(" and ejg.anio=edes.anioejg ");
		        sql.append(" and ejg.idinstitucion=edes.idinstitucion ");
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
					"gratuita.literal.numeroDesignacion")+": "+  anio+"/"+codigo;
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
	  
	  
	  public Vector getejgsdesigna(String idturno, String  numero, String anio, String idinstitucion, String longitudNumEjg) throws ClsExceptions{
		
	  		Vector datos=new Vector();
	       try {
	            RowsContainer rc = new RowsContainer();
	            String sql = " SELECT TABLA.*, " +
	            		" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(TABLA.FECHARESOLUCIONCAJG, 'DMA', "+this.usrbean.getLanguage()+") AS FECHARESOLUCIONCAJGLETRA, " +
	            		" PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(TABLA.FECHAAPERTURA_EJG, 'DMA', "+this.usrbean.getLanguage()+") AS FECHAAPERTURA_EJG_LETRA " +
        			" FROM ( " +
		            	" SELECT  EJG.aniocajg || '/' || EJG.numero_cajg as ANIONUMEROCAJG, " + 
		        			" EJG.numero_cajg  || '/' || EJG.aniocajg as NUMEROANIOCAJG, " + 
			            	" EJG.aniocajg as ANIOCAJG, " + 
		        			" EJG.numero_cajg as NUMEROCAJG, " + 
			            	" EJG.idpersona as IDPERSONA, " +
		        			" TO_CHAR(EJG.FECHAAPERTURA, 'dd/mm/yyyy') AS FECHAAPERTURA_EJG, " +
		        			" EJG.anio || '/' || lpad(EJG.numejg,"+longitudNumEjg+",0) ANIONUMEROEJG, " +
		        			" EJG.anio || '/' || lpad(EJG.numejg,"+longitudNumEjg+",0) NUMERO_EJG, " +
			            	" lpad(EJG.numejg,"+longitudNumEjg+",0)  || '/' || EJG.anio NUMEROANIOEJG, " +
		        			" PROCU.NOMBRE || ' ' || PROCU.APELLIDOS1 || ' ' || PROCU.APELLIDOS2 AS PROCURADOR_EJG, " +
			            	" EJG.anio ANIOEJG, " + 
		        			" EJG.numero NUMEROEJG, " + 
			            	" lpad(EJG.numejg,"+longitudNumEjg+",0)  NUMEJG, " + 
		        			" EJG.idtipoejg IDTIPOEJG, " +
		        			" NVL2(EJG.IDACTA, ( " +
								" SELECT TO_CHAR(ACTA.FECHARESOLUCION, 'dd/mm/yyyy') " +
								" FROM SCS_ACTACOMISION ACTA " +
								" WHERE ACTA.IDACTA = EJG.IDACTA " +
								" AND ACTA.IDINSTITUCION =  EJG.IDINSTITUCION " +
								" AND ACTA.ANIOACTA =  EJG.ANIOACTA " +        
								" ),  TO_CHAR(EJG.FECHARESOLUCIONCAJG, 'dd/mm/yyyy')) AS FECHARESOLUCIONCAJG " +
						" FROM SCS_EJG EJG, " +
							" SCS_DESIGNA DES, " +
							" SCS_TURNO TUR, " +
							" SCS_TIPOEJG TIP, " +	
					        " SCS_TIPODICTAMENEJG TDIC, " +
							" SCS_EJGDESIGNA EJGDES, " +
					        " SCS_PROCURADOR PROCU " +
				        " WHERE EJGDES.Aniodesigna = DES.anio " +
					       	" AND EJGDES.Numerodesigna = DES.numero" +
					       	" AND EJGDES.Idturno = DES.idturno " +		        
					        " AND EJGDES.Idinstitucion = DES.idinstitucion " +
					        " AND EJGDES.Idinstitucion = EJG.idinstitucion " +
					        " AND EJGDES.Anioejg = EJG.anio " +
					        " AND EJGDES.Numeroejg = EJG.numero " +
			                " AND EJGDES.Idtipoejg = EJG.idtipoejg " +
			                " AND TIP.idtipoejg = EJG.idtipoejg " +
					        " AND TDIC.idtipodictamenejg(+) = EJG.idtipodictamenejg " +
			                " AND TDIC.idtipodictamenejg(+) = EJG.idinstitucion" +
			                " AND TUR.idinstitucion(+) = EJG.idinstitucion " +
			                " AND TUR.idturno(+) = EJG.guardiaturno_idturno " +
			                " AND PROCU.idprocurador(+)= EJG.idprocurador " +
			                " AND PROCU.idinstitucion(+) = " + idinstitucion +
			                " AND DES.anio = " + anio +
			                " AND DES.numero = " + numero +
			                " AND DES.idturno = " + idturno +
			                " AND DES.idinstitucion = " + idinstitucion +
                 	" ) TABLA ";
	            
               if (rc.find(sql)) {
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
	  
	  /**
	   * nombre: getregistrodatosDesigna
	   * valores: se le pasa los valores de el registro, institucion y el idioma que queremos que nos aparezca.
	   * Devuelve: nos devuelve un hastable de todos los datos.
	   * **/
	  public Hashtable getregistrodatosDesigna(Hashtable registro,String idInstitucion, String idioma, String tipoDestinatarioInforme, boolean agregarEtiqEJG, boolean isSolicitantes, boolean isContrarios,String longitudNunmEjg) throws ClsExceptions {
//		  Hashtable vsalida=new Hashtable();	
		  HelperInformesAdm helperInformes = new HelperInformesAdm();
		  String numeroDesigna = (String)registro.get("NUMERO");
		  String anioDesigna = (String)registro.get("ANIO");
		  String idTurno  = (String)registro.get("IDTURNO");
		  String idPersona  = (String)registro.get("IDPERSONA");
		  String institucionOrigen  = (String)registro.get("IDINSTITUCIONORIGEN");
		  
		  String idTipoDesigna="";
		  Hashtable htCodigo = new Hashtable();
		  
		  try{			  
			if(numeroDesigna==null||numeroDesigna.trim().equals(""))
				throw new ClsExceptions("Excepcion Controlada. Seguramente no tenga letrado");				
			  
		  	//Metemos la descripcion del turno
			helperInformes.completarHashSalida(registro,helperInformes.getTurnoSalida(idInstitucion,idTurno));
			
			idTipoDesigna  = (String)registro.get("IDTIPODESIGNACOLEGIO");
			//Metemos la descripcion del tipo de designa
			if(idTipoDesigna!=null && !idTipoDesigna.trim().equals(""))
				helperInformes.completarHashSalida(registro,helperInformes.getTipoDesignaColegio(idInstitucion,idTipoDesigna,idioma));
			else
				registro.put("DESC_TIPODESIGNA", " ");							
			  
			//metemos el numero de colegiado
			if(registro.containsKey("IDPERSONA") && registro.get("IDPERSONA")!=null && !((String)registro.get("IDPERSONA")).trim().equals("")){
				Hashtable htCodigo2 = new Hashtable();
				htCodigo2.put(new Integer(1), idInstitucion);
				htCodigo2.put(new Integer(2), (String)registro.get("IDPERSONA"));
				if(institucionOrigen!=null && !institucionOrigen.trim().equals("")){
					htCodigo2.put(new Integer(1), institucionOrigen);
					htCodigo2.put(new Integer(2), (String)registro.get("IDPERSONA"));
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo2, "f_siga_calculoncolegiado", "NCOLEGIADO_LETRADO"));
				} else {
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo2, "f_siga_calculoncolegiado", "NCOLEGIADO_LETRADO"));
				}	
			} else {
				registro.put("NCOLEGIADO_LETRADO", " ");
			}
				
			htCodigo = new Hashtable();
			htCodigo.put(new Integer(1), idTurno);
			htCodigo.put(new Integer(2), idInstitucion);
			//metemos el nombre de partdo
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_NOMBRE_PARTIDO", "NOMBRE_PARTIDO"));
			htCodigo.put(new Integer(1), idInstitucion);				
			htCodigo.put(new Integer(2), idTurno);
			htCodigo.put(new Integer(3), anioDesigna);
			htCodigo.put(new Integer(4), numeroDesigna);
			
			//metemos los procuradores
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETPROCURADORCONT_DESIG", "PROCURADOR_CONTRARIOS"));

			//metemos los contrarios
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETCONTRARIOS_DESIGNA", "CONTRARIOS"));
			
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETABOGADOCONTRARIO_DES", "ABOGADOS_CONTRARIOS"));
			
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETREPRESENTANTE_DES", "REPRESENTANTES_CONTRARIOS"));
			
			/**
			 * Se Saca el nombre del tramitador de la EJG relacionada con la designa, si hay mas de una EJG relacionado se saca
			 * un listado de los tramitadores de cada EJG relacionada con la designa y entre parentesis el anio/numero cajg
			 * **/
			Vector ejgsdesingna=getejgsdesigna(idTurno, numeroDesigna, anioDesigna, idInstitucion,longitudNunmEjg);				
			ScsEJGAdm ejgadm = new ScsEJGAdm(this.usrbean);
			int tamanio= ejgsdesingna.size();
			String Listadotramitador="";				
			String Listadoanionumerocajg="";
			String Listadonumeroaniocajg="";								
			String listadoAnioNumeroEjg="";
			String listadoNumeroAnioEjg="";
			
			String sListadoFechaResolucionCajg="";
			String sListadoFechaResolucionCajgLetra="";
			String sListadoFechaAperturaEjg="";
			String sListadoFechaAperturaEjgLetra="";
				
			//Guarda el nombre del procurador del EJG
			String procuradorEjg="";											
											
			Vector Vtramitador=null;
			
			if(ejgsdesingna.size()>0)
			{	
				for (int i = 0; i < ejgsdesingna.size(); i++) {					
					Hashtable registroejg = (Hashtable) ejgsdesingna.get(i);				
				    String idLetradoEjg  = (String)registroejg.get("IDPERSONA");
				    String aniocajg = (String)registroejg.get("ANIOCAJG");
				    String numerocajg = (String)registroejg.get("NUMEROCAJG");	
				    String numeroaniocajg = (String)registroejg.get("NUMEROANIOCAJG"); 
				    String anionumerocajg = (String)registroejg.get("ANIONUMEROCAJG");
				    String numeroAnioEjg = (String)registroejg.get("NUMEROANIOEJG"); 
				    String anioNumeroEjg = (String)registroejg.get("ANIONUMEROEJG");
				    String numeroEJG = (String)registroejg.get("NUMERO_EJG");
				    String sFechaResolucionCajg = (String)registroejg.get("FECHARESOLUCIONCAJG");
				    String sFechaResolucionCajgLetra = (String)registroejg.get("FECHARESOLUCIONCAJGLETRA");
				    String sFechaAperturaEJG = (String)registroejg.get("FECHAAPERTURA_EJG");
				    String sFechaAperturaEJGLetra = (String)registroejg.get("FECHAAPERTURA_EJG_LETRA");
				    
				    if(procuradorEjg.equals(""))
				    	procuradorEjg = (String)registroejg.get("PROCURADOR_EJG");
				    else
				    	procuradorEjg = procuradorEjg +","+(String)registroejg.get("PROCURADOR_EJG");
				    
				    if (!numeroaniocajg.equals("/") && (!anionumerocajg.equals("/"))){
				    	Listadoanionumerocajg+=","+anionumerocajg;
				    	Listadonumeroaniocajg+=","+numeroaniocajg;
				    }
				    
				    if (!numeroAnioEjg.equals("/") && (!anioNumeroEjg.equals("/"))){
				    	listadoAnioNumeroEjg+=","+anioNumeroEjg;
				    	listadoNumeroAnioEjg+=","+numeroAnioEjg;
				    }
				    
					if(idLetradoEjg!=null && !idLetradoEjg.trim().equalsIgnoreCase("")){
						Vtramitador=ejgadm.getColegiadoSalida(idInstitucion,idLetradoEjg,"TRAMITADOR_EGJ");							
						for (int l = 0; l < Vtramitador.size(); l++) {							  
							Hashtable registrotramitador = (Hashtable) Vtramitador.get(l);
							String nombretramitador = (String)registrotramitador.get("NOMBRE_TRAMITADOR_EGJ");									
							if(nombretramitador!=null && !nombretramitador.trim().equalsIgnoreCase("")){										
								if (((aniocajg==null || aniocajg.equals(""))&&(numerocajg==null || numerocajg.equals("")))||(tamanio==1)){
									Listadotramitador+=","+nombretramitador;
								} else {	
									Listadotramitador+=","+nombretramitador+"("+aniocajg+"/"+numerocajg+")";
								}
							}
						}							
				    }
					
					if (sFechaResolucionCajg != null) {
						if (sListadoFechaResolucionCajg.equals("")) {
							sListadoFechaResolucionCajg = sFechaResolucionCajg;
							sListadoFechaResolucionCajgLetra = sFechaResolucionCajgLetra;
						} else {
							sListadoFechaResolucionCajg += "," + sFechaResolucionCajg;
							sListadoFechaResolucionCajgLetra += "," + sFechaResolucionCajgLetra;
						}						
					}
					
					if (sFechaAperturaEJG != null) {
						if (sListadoFechaAperturaEjg.equals("")) {
							sListadoFechaAperturaEjg = sFechaAperturaEJG;
							sListadoFechaAperturaEjgLetra = sFechaAperturaEJGLetra;
						} else {
							sListadoFechaAperturaEjg += "," + sFechaAperturaEJG;
							sListadoFechaAperturaEjgLetra += "," + sFechaAperturaEJGLetra;
						}						
					}
					if (numeroEJG!=null && !numeroEJG.equals("")){
						UtilidadesHash.set(registro, "NUMERO_EJG", numeroEJG);
						
						//Ini Mod MJM se incluyen las etiquetas del informe de EJG
                        //Se agregan las etiquetas del informe de EJG al informe de designas
                        //Si se est� invocando al m�todo desde EJG o desde designas env�o telem�tico
                        //no hay que sacar estas etiquetas
                        if(agregarEtiqEJG==true)
                        {
                            boolean agregarEtiqDesigna=false;
                            ScsEJGAdm scsEJGAdm = new ScsEJGAdm(this.usrbean);

                            Vector datosEjgVector=scsEJGAdm.getDatosInformeEjg(idInstitucion, (String)registroejg.get("IDTIPOEJG"), (String)registroejg.get("ANIOEJG"),(String)registroejg.get("NUMEROEJG"), isSolicitantes,isContrarios, null,null,true,tipoDestinatarioInforme, agregarEtiqDesigna,longitudNunmEjg);     

                            Hashtable datosEjgRel = new Hashtable();
                            boolean contieneContrarios=false;
                            
                            if(datosEjgVector.size()>0){
                               
                                for (int dv = 0; dv < datosEjgVector.size(); dv++) {
                               
                                    Hashtable datosEjgRelAux = (Hashtable) datosEjgVector.get(dv);
                                   
                                    Enumeration keysEjgRel = datosEjgRelAux.keys();
                                   
                                    while (keysEjgRel.hasMoreElements())
                                    {
                                        String keyEjgRel =  (String) keysEjgRel.nextElement();

                                        String claveNew = "EJGRELDESIGNA_"+ keyEjgRel;
                                       
                                        if(datosEjgRelAux.get(keyEjgRel) instanceof String){
                                       
                                            String valor = (String)datosEjgRelAux.get(keyEjgRel);
                                       
                                            datosEjgRel.put(claveNew, valor);
           
                                       
                                        //Si es un area del informe
                                        }else if(datosEjgRelAux.get(keyEjgRel) instanceof Vector){
                                       
                                           if(keyEjgRel.contains("contrarios")){
                                        	 claveNew = "EJGRELDESIGNA_Acontrarios";
                                        	 contieneContrarios=true;
                                           }	 
                                            
                                           
                                            Vector areasRenombrada = new Vector();
                                            Vector areaVector = (Vector) datosEjgRelAux.get(keyEjgRel);
                                       
                                           
                                            if(areaVector.size()>0){
                                               
                                                for (int av = 0; av < areaVector.size(); av++){
                                                   
                                                    Hashtable area = (Hashtable) areaVector.get(av);
                                                    Enumeration e2 = area.keys();
                                                    Hashtable areaHashtableRenombrado = new Hashtable();
                                                    while (e2.hasMoreElements()){
                                               
                                                        Object element2 =  e2.nextElement();
                                                        String claveNewArea = claveNew+"_"+element2;
                                                       
                                                        String valorArea = (String)area.get((String) element2);
                                                        areaHashtableRenombrado.put(claveNewArea,valorArea);
                                                       
                                                    }   
                                                    areasRenombrada.add(areaHashtableRenombrado);
                                                   
                                                }
                                               
                                               
                                            }
                                            datosEjgRel.put(claveNew, areasRenombrada);
           
                                        }   
                                   
                                    }
                                   
                                }
                            }
                            
                            //Sino contiene contrarios pasamos las etiquetas vac�as
                            if(contieneContrarios==false)
                            {
                            	Vector contrariosEjgRenom = new Vector();
                            	 Hashtable datosregionCon = new Hashtable();
                            	 
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_ABOGADO_CONTRARIO", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_CP_PJG", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_DOMICILIO_PJG", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_NIF_PJG", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_NOMBRE_PJG", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_O_A_PJG", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_POBLACION_PJG", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_PROCURADOR_CONTRARIO", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_PROVINCIA_PJG", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_REPRESENTANTE_CONTRARIO", "");
                            	datosregionCon.put( "EJGRELDESIGNA_Acontrarios_TELEFONO1_PJG", "");
                            	
                            	contrariosEjgRenom.add(datosregionCon);
						    	datosEjgRel.put("EJGRELDESIGNA_Acontrarios", contrariosEjgRenom);
                            }
                            
                            //�rea de unidad familiar 
                            Vector regionUF = scsEJGAdm.getDatosRegionUF(idInstitucion,(String)registroejg.get("IDTIPOEJG"), (String)registroejg.get("ANIOEJG"),(String)registroejg.get("NUMEROEJG"),idioma,longitudNunmEjg);
						   
                            Hashtable datosregionUF = new Hashtable();
						     Vector regionUFRenom = new Vector();
                            if(regionUF.size()>0){
						    	
						    	for (int rv = 0; rv < regionUF.size(); rv++) {
                               
                                    Hashtable datosRegionUFAux = (Hashtable) regionUF.get(rv);
                                   
                                    Enumeration keysRegionUF = datosRegionUFAux.keys();
                                   
                                    while (keysRegionUF.hasMoreElements())
                                    {
                                        String keyRegionUF =  (String) keysRegionUF.nextElement();
                                       
                                        String claveUF = "EJGRELDESIGNA_unidadfamiliar_"+ keyRegionUF;

                                        String valorUF = (String)datosRegionUFAux.get(keyRegionUF);
                                       
                                        datosregionUF.put(claveUF, valorUF);
                                    }
                                    
						    	}
						    	
						    	regionUFRenom.add(datosregionUF);
						    	
						    	if(regionUFRenom!=null)
						    		datosEjgRel.put("EJGRELDESIGNA_unidadfamiliar", regionUFRenom);
						    }
 
                            //�rea de c�nyuge
                            
                            Vector regionConyuge = scsEJGAdm.getDatosRegionConyuge(idInstitucion,(String)registroejg.get("IDTIPOEJG"), (String)registroejg.get("ANIOEJG"),(String)registroejg.get("NUMEROEJG"),idioma,longitudNunmEjg);
                            
                            Hashtable datosregionConyuge = new Hashtable();
                            Vector regionConyugeRenom = new Vector();
                            
						    if(regionConyuge.size()>0){
						    	
                               for (int rc = 0; rc < regionConyuge.size(); rc++) {
                            	   
                                    Hashtable datosRegionConyugeAux = (Hashtable) regionConyuge.get(rc);
                                   
                                    Enumeration keysRegionConyuge = datosRegionConyugeAux.keys();
                                   
                                    while (keysRegionConyuge.hasMoreElements())
                                    {
                                        String keyRegionConyuge =  (String) keysRegionConyuge.nextElement();
                                       
                                        String claveConyuge = "EJGRELDESIGNA_conyuge_"+ keyRegionConyuge;

                                        String valorConyuge = (String)datosRegionConyugeAux.get(keyRegionConyuge);
                                       
                                        datosregionConyuge.put(claveConyuge, valorConyuge);
                                    }
                                    
						    	}
                            
                            regionConyugeRenom.add(datosregionConyuge);
						    
						    if(regionConyugeRenom!=null)
                            	datosEjgRel.put("EJGRELDESIGNA_conyuge", regionConyugeRenom);
                               
						    }

						    if(datosEjgRel!=null)
                                registro.putAll(datosEjgRel);
						    
                           
                        }//Fin agregar etiquetas EJG
                        //Fin Mod MJM se incluyen las etiquetas del informe de EJG
						
						
					} else {
						UtilidadesHash.set(registro, "NUMERO_EJG", "");
					}

				}//FIN FOR
			}else{
				UtilidadesHash.set(registro, "NUMERO_EJG", "");
			}	
				
			UtilidadesHash.set(registro, "FECHARESOLUCIONCAJG", sListadoFechaResolucionCajg);
			UtilidadesHash.set(registro, "FECHARESOLUCIONCAJGLETRA", sListadoFechaResolucionCajgLetra);
			UtilidadesHash.set(registro, "FECHAAPERTURA_EJG", sListadoFechaAperturaEjg);
			UtilidadesHash.set(registro, "FECHAAPERTURA_EJG_LETRA", sListadoFechaAperturaEjgLetra);				
			
			if (procuradorEjg!=null && !procuradorEjg.equals("")){
				UtilidadesHash.set(registro, "PROCURADOR_EJG", procuradorEjg);
			} else {
				UtilidadesHash.set(registro, "PROCURADOR_EJG", "");
			}
			
			
				
			if (Listadotramitador!=null && !Listadotramitador.equals("")){
				Listadotramitador =Listadotramitador.substring(1);					
				UtilidadesHash.set(registro, "NOMBRE_TRAMITADOR_EJG", Listadotramitador);
			} else {
				UtilidadesHash.set(registro, "NOMBRE_TRAMITADOR_EJG", "");
			}
				
			if (Listadoanionumerocajg!=null && !Listadoanionumerocajg.equals("")){
				Listadoanionumerocajg=Listadoanionumerocajg.substring(1);
				UtilidadesHash.set(registro, "LISTAANIONUMEROCAJ", Listadoanionumerocajg);					
			} else {
				UtilidadesHash.set(registro, "LISTAANIONUMEROCAJ", "");
			}
			
			if (Listadonumeroaniocajg!=null && !Listadonumeroaniocajg.equals("")){
				Listadonumeroaniocajg=Listadonumeroaniocajg.substring(1);
				UtilidadesHash.set(registro, "LISTANUMEROANIOCAJ", Listadonumeroaniocajg);					
			} else {
				UtilidadesHash.set(registro, "LISTANUMEROANIOCAJ", "");
			}
			
			//LISTADO ANIO NUMERO EJG				
			if (listadoAnioNumeroEjg!=null && !listadoAnioNumeroEjg.equals("")){
				listadoAnioNumeroEjg=listadoAnioNumeroEjg.substring(1);
				UtilidadesHash.set(registro, "LISTAANIONUMEROEJGS", listadoAnioNumeroEjg);					
			} else {
				UtilidadesHash.set(registro, "LISTAANIONUMEROEJGS", "");
			}
			
			if (listadoNumeroAnioEjg!=null && !listadoNumeroAnioEjg.equals("")){
				listadoNumeroAnioEjg=listadoNumeroAnioEjg.substring(1);
				UtilidadesHash.set(registro, "LISTANUMEROANIOEJGS", listadoNumeroAnioEjg);					
			} else {
				UtilidadesHash.set(registro, "LISTANUMEROANIOEJGS", "");
			}				
				
			htCodigo.put(new Integer(2), anioDesigna);
			htCodigo.put(new Integer(3), idTurno);
			htCodigo.put(new Integer(4), numeroDesigna);			
			
			//metemos la lista actuaciones 
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETACTUACIONESDESIGNA", "LISTA_ACTUACIONES_DESIGNA"));
			//metemos la primera actuacion				
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETFIRSTASISDESIGNA", "FECHA_ASISTENCIA"));
			
			htCodigo.put(new Integer(5), "0");
			helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETINTERESADOSDESIGNA", "LISTA_INTERESADOS_DESIGNA"));

			if(idPersona==null ||idPersona.trim().equalsIgnoreCase("")){
			    idPersona="-33"; // forzamos que no encuentre datos, en lugar de dar error
			}
			htCodigo = new Hashtable();
			htCodigo.put(new Integer(1), idInstitucion);				
			htCodigo.put(new Integer(2), idTurno);
			htCodigo.put(new Integer(3), anioDesigna);
			htCodigo.put(new Integer(4), numeroDesigna);
			htCodigo.put(new Integer(5), idPersona);
			
			//Si no se encuentra info en cen_colegiado (Art.27) se busca en cen_nocolegiado
			//if(registro.get("NCOLEGIADO_LETRADO") != null && !((String)registro.get("NCOLEGIADO_LETRADO")).equals("")){
			if(institucionOrigen!=null && !institucionOrigen.trim().equals("")){
				helperInformes.completarHashSalida(registro,getLetradoSalidaOficioArt27(htCodigo));
			} else {
				helperInformes.completarHashSalida(registro,getLetradoSalidaOficio(htCodigo));
			}
			String sexoLetrado  = (String)registro.get("SEXO_LETRADO_SINTRADUCIR");				
			if (sexoLetrado!=null && !sexoLetrado.trim().equals("")){
					htCodigo = new Hashtable();
					htCodigo.put(new Integer(1), sexoLetrado);
					htCodigo.put(new Integer(2), idioma);				
					helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(
					htCodigo, "F_SIGA_GETRECURSO_ETIQUETA", "SEXO_LETRADO"));
			} else {
						registro.put("SEXO_LETRADO", "");
			}
			
			String observacionesCambioLetrado  = (String)registro.get("OBSERVACIONES_CAMBIOLETRADO");
			if (observacionesCambioLetrado!=null && !observacionesCambioLetrado.trim().equals("")){		
				registro.put("OBSERVACIONES_CAMBIOLETRADO", observacionesCambioLetrado);
			} else {
				registro.put("OBSERVACIONES_CAMBIOLETRADO", "");
			}					
			
			/**Fecha en Letras de la fecha del letrado actual, para que aparezcan en el idioma que se le pasa**/							
			String fechaLetradoActual  = (String)registro.get("FECHALETRADO_ACTUAL");
			if (fechaLetradoActual!=null && !fechaLetradoActual.trim().equals("")){		
				htCodigo = new Hashtable();						
				htCodigo.put(new Integer(1), fechaLetradoActual);
				htCodigo.put(new Integer(2), "m");
				htCodigo.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHALETRADOACTUAL_LETRA"));
			} else {
				registro.put("FECHALETRADOACTUAL_LETRA", "");
			}
		
		
			Vector vPreferente = getDireccionLetradoSalidaOficioPreferente(idPersona,idInstitucion);		
			String dir = (String)((Hashtable)vPreferente.get(0)).get("DOMICILIO_DESPACHO_LETRADO");
			if (dir != null && !dir.trim().equals("")){
				helperInformes.completarHashSalida(registro,vPreferente);
			} else {
				helperInformes.completarHashSalida(registro, getDireccionLetradoSalidaOficio(idPersona, idInstitucion));
			}
			
			String pobLetrado = (String)registro.get("POBLACION_DESPACHO_LETRADO");
			if(pobLetrado==null ||pobLetrado.trim().equalsIgnoreCase("")){
				String idPobLetrado = (String)registro.get("IDPOBLACION_DESPACHO_LETRADO");
				helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetrado,"POBLACION_DESPACHO_LETRADO"));
				String idProvLetrado = (String)registro.get("IDPROVINCIA_DESPACHO_LETRADO");
				if(idProvLetrado!=null && !idProvLetrado.equalsIgnoreCase(""))
					helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetrado,"PROVINCIA_DESPACHO_LETRADO"));
				else
					UtilidadesHash.set(registro, "PROVINCIA_DESPACHO_LETRADO", "");	
				
			} else {
				UtilidadesHash.set(registro, "PROVINCIA_DESPACHO_LETRADO", "");									
			}
				
			if (registro.get("DOMICILIO_DESPACHO_LETRADO")!=null) {
				registro.put("DOMICILIO_LETRADO", registro.get("DOMICILIO_DESPACHO_LETRADO"));
			}
			if (registro.get("CP_DESPACHO_LETRADO")!=null) {
				registro.put("CP_LETRADO", registro.get("CP_DESPACHO_LETRADO"));
			}
			if (registro.get("IDPOBLACION_DESPACHO_LETRADO")!=null) {
				registro.put("ID_POBLACION_LETRADO", registro.get("IDPOBLACION_DESPACHO_LETRADO"));
			}
			if (registro.get("POBLACION_DESPACHO_LETRADO")!=null) {
				registro.put("POBLACION_LETRADO", registro.get("POBLACION_DESPACHO_LETRADO"));
			}
			if (registro.get("IDPROVINCIA_DESPACHO_LETRADO")!=null) {
				registro.put("ID_PROVINCIA_LETRADO", registro.get("IDPROVINCIA_DESPACHO_LETRADO"));
			}
			if (registro.get("PROVINCIA_DESPACHO_LETRADO")!=null) {
				registro.put("PROVINCIA_LETRADO", registro.get("PROVINCIA_DESPACHO_LETRADO"));
			}
			if (registro.get("TELEFONO1_DESPACHO_LETRADO")!=null) {
				registro.put("TELEFONODESPACHO_LET", registro.get("TELEFONO1_DESPACHO_LETRADO"));
			}
			if (registro.get("FAX1_DESPACHO_LETRADO")!=null) {
				registro.put("FAX_LETRADO", registro.get("FAX1_DESPACHO_LETRADO"));
			}
			if (registro.get("EMAIL_DESPACHO_LETRADO")!=null) {
				registro.put("EMAIL_LETRADO", registro.get("EMAIL_DESPACHO_LETRADO"));
			}
			if (registro.get("MOVIL_DESPACHO_LETRADO")!=null) {
				registro.put("MOVILDESPACHO_LET", registro.get("MOVIL_DESPACHO_LETRADO"));
			}				
				
			helperInformes.completarHashSalida(registro,getDireccionPersonalLetradoSalidaOficio(idPersona,idInstitucion));
			pobLetrado = (String)registro.get("POBLACION_GUARDIA_LETRADO");
			if(pobLetrado==null ||pobLetrado.trim().equalsIgnoreCase("")){
				String idPobLetrado = (String)registro.get("IDPOBLACION_GUARDIA_LETRADO");
				helperInformes.completarHashSalida(registro,helperInformes.getNombrePoblacionSalida(idPobLetrado,"POBLACION_GUARDIA_LETRADO"));
				String idProvLetrado = (String)registro.get("IDPROVINCIA_GUARDIA_LETRADO");
				if(idProvLetrado!=null && !idProvLetrado.equalsIgnoreCase(""))
					helperInformes.completarHashSalida(registro,helperInformes.getNombreProvinciaSalida(idProvLetrado,"PROVINCIA_GUARDIA_LETRADO"));
				else
					UtilidadesHash.set(registro, "PROVINCIA_GUARDIA_LETRADO", "");	
				
			} else {
				UtilidadesHash.set(registro, "PROVINCIA_GUARDIA_LETRADO", "");									
			}				
			
			if (registro.get("TELEFONO1_GUARDIA_LETRADO")!=null) {
				registro.put("TELEFONO1_LETRADO", registro.get("TELEFONO1_GUARDIA_LETRADO"));
			}
			if (registro.get("TELEFONO2_GUARDIA_LETRADO")!=null) {
				registro.put("TELEFONO2_LETRADO", registro.get("TELEFONO2_GUARDIA_LETRADO"));
			}
			if (registro.get("MOVIL_GUARDIA_LETRADO")!=null) {
				registro.put("MOVIL_LETRADO", registro.get("MOVIL_GUARDIA_LETRADO"));
			}
			
			String parrafoLetrado = "";
			if (registro.containsKey("NOMBRE_LETRADO")  && registro.get("NOMBRE_LETRADO")!=null && !((String)registro.get("NOMBRE_LETRADO")).trim().equals("") ) {
				parrafoLetrado = UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.letrado") + " " + (String)registro.get("NOMBRE_LETRADO");
				
				if (registro.containsKey("TELEFONO1_GUARDIA_LETRADO")  && registro.get("TELEFONO1_GUARDIA_LETRADO")!=null && !((String)registro.get("TELEFONO1_GUARDIA_LETRADO")).trim().equals("") ) {
					parrafoLetrado += " " + UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.telefono")  + " " + (String)registro.get("TELEFONO1_GUARDIA_LETRADO");
				}
			}	
			//registro.put("PARRAFO_LETRADO", parrafoLetrado);
			
			helperInformes.completarHashSalida(registro,getActuacionDesignaSalidaOficio(idInstitucion,numeroDesigna,idTurno,anioDesigna));
			
			String idJuzgado = (String)registro.get("IDJUZGADO");				
			String idInstJuzgado = (String)registro.get("IDINSTITUCION_JUZG");				
			if(idJuzgado==null ||idJuzgado.trim().equalsIgnoreCase("")){
			    idJuzgado="-33"; // forzamos que no encuentre datos, en lugar de dar error
			    idInstJuzgado = "0";
			}
			helperInformes.completarHashSalida(registro,helperInformes.getJuzgadoSalida(idInstJuzgado,idJuzgado,""));		
		
			String Idpretension  = (String)registro.get("IDPRETENSION");
			ScsEJGAdm ejgdm = new ScsEJGAdm(this.usrbean);				
			if(Idpretension!=null && !Idpretension.trim().equalsIgnoreCase("")){			
				Vector vpretenciones=ejgdm.getPretension(Idpretension, idInstitucion,idioma);
				for (int s = 0; s < vpretenciones.size(); s++) {
					Hashtable registropretenciones = (Hashtable) vpretenciones.get(s);
					String procedimiento = (String)registropretenciones.get("PRETENSION");
					if(procedimiento!=null && !procedimiento.trim().equalsIgnoreCase(""))
						registro.put("PROCEDIMIENTO_DESIGNA", procedimiento);
					else
						UtilidadesHash.set(registro, "PROCEDIMIENTO_DESIGNA", "");
				}							
						
			} else	
				UtilidadesHash.set(registro, "PROCEDIMIENTO_DESIGNA", "");
			
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
			
			String parrafoProcurador = "";
			if (registro.containsKey("PROCURADOR")  && registro.get("PROCURADOR")!=null && !((String)registro.get("PROCURADOR")).trim().equals("") ) {
				parrafoProcurador = UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.procurador") + " " + (String)registro.get("PROCURADOR");
				
				if (registro.containsKey("TELEFONO1_PROCURADOR")  && registro.get("TELEFONO1_PROCURADOR")!=null && !((String)registro.get("TELEFONO1_PROCURADOR")).trim().equals("") ) {
					parrafoProcurador +=  " " + UtilidadesString.getMensajeIdioma(idioma, "informes.cartaOficio.parrafo.telefono")  + " " + (String)registro.get("TELEFONO1_PROCURADOR");
				}
			}				
			//registro.put("PARRAFO_PROCURADOR", parrafoProcurador);								
			registro.put("PARRAFO_LETRADO_PROCURADOR", parrafoLetrado+"\r"+parrafoProcurador);
			
			// RGG m�s descripciones 
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
			Vector vtipoejgcolegiodesigna=getTipoEJGColegioDesigna(idTurno, numeroDesigna, anioDesigna, idInstitucion, idioma);				
			for (int s = 0; s < vtipoejgcolegiodesigna.size(); s++) {
				Hashtable registropretenciones = (Hashtable) vtipoejgcolegiodesigna.get(s);
				String tipoejgDesigna = (String)registropretenciones.get("TIPO_EJG_COLEGIO");
				if(tipoejgDesigna!=null && !tipoejgDesigna.trim().equalsIgnoreCase("")){
					registro.put("TIPO_EJG_COLEGIO", tipoejgDesigna);
				} else {
					registro.put("TIPO_EJG_COLEGIO", " ");
				}
			}
			if (vtipoejgcolegiodesigna.size()==0){
				registro.put("TIPO_EJG_COLEGIO", " ");
			}
					
			String observacionesUltimoLetrado  = (String)registro.get("OBSERVACIONES_ULTIMOLETRADO");
			if (observacionesUltimoLetrado!=null && !observacionesUltimoLetrado.trim().equals("")){		
				registro.put("OBSERVACIONES_ULTIMOLETRADO", observacionesUltimoLetrado);
			} else {
				registro.put("OBSERVACIONES_ULTIMOLETRADO", "");
			}						
						
			/**Fechas en Letras para que aparezcan en el idioma que se le pasa**/							
			String fechaRenuncia  = (String)registro.get("FECHARENUNCIAENLETRA");
			if (fechaRenuncia!=null && !fechaRenuncia.trim().equals("")){		
				htCodigo = new Hashtable();						
				htCodigo.put(new Integer(1), fechaRenuncia);
				htCodigo.put(new Integer(2), "m");
				htCodigo.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHARENUNCIA_ENLETRA"));
			} else {
				registro.put("FECHARENUNCIA_ENLETRA", "");
			}
			
			String fechaSolicitudRenunciaLetra  = (String)registro.get("FECHASOLICITUDRENUNCIA");
			if (fechaSolicitudRenunciaLetra!=null && !fechaSolicitudRenunciaLetra.trim().equals("")){		
				htCodigo = new Hashtable();						
				htCodigo.put(new Integer(1), fechaSolicitudRenunciaLetra);
				htCodigo.put(new Integer(2), "m");
				htCodigo.put(new Integer(3), idioma);								
				helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA", "FECHA_SOLICITUDRENUNCIA_LETRA"));
			} else {
				registro.put("FECHA_SOLICITUDRENUNCIA_LETRA", "");
			}		
					
						
			/**se muestra el mes actual en letra**/
			htCodigo = new Hashtable();
		    htCodigo.put(new Integer(1), (String)registro.get("MESACTUAL"));
			htCodigo.put(new Integer(2), "m");
			htCodigo.put(new Integer(3), idioma);
	     	helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHAENLETRA", "MES_ACTUAL"));
	     	registro.put("MES_ACTUAL", registro.get("MES_ACTUAL").toString().toUpperCase());
	     	registro.put("MES_ACTUAL_MINUS", registro.get("MES_ACTUAL").toString().toLowerCase());
	     		
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
				registro.put("CATEGORIA_UA", " ");
				registro.put("IDJURISDICCION_UA", " ");
				registro.put("IDINSTITUCION_JUZG_UA", " ");
				registro.put("COMPLEMENTO_UA", " ");	
				registro.put("NUMEROPROCEDIMIENTO_UA", " ");				
			}

	     	htCodigo = new Hashtable();
			htCodigo.put(new Integer(1), idInstitucion);
			htCodigo.put(new Integer(2), numeroDesigna);
			htCodigo.put(new Integer(3), idTurno);
			htCodigo.put(new Integer(4), anioDesigna);
            htCodigo.put(new Integer(5),idioma);				                								
            helperInformes.completarHashSalida(registro,helperInformes.ejecutaFuncionSalida(htCodigo, "F_SIGA_GETDELITOS_DESIGNA", "DELITOS"));
			htCodigo.remove(new Integer(5));
															
			if(Idpretension!=null && !Idpretension.trim().equalsIgnoreCase("")){			
				Vector vpretenciones=ejgdm.getPretension(Idpretension, idInstitucion,idioma);
				for (int s = 0; s < vpretenciones.size(); s++) {
					Hashtable registropretenciones = (Hashtable) vpretenciones.get(s);
					String procedimiento1 = (String)registropretenciones.get("PRETENSION");
					if(procedimiento1!=null && !procedimiento1.trim().equalsIgnoreCase(""))
						registro.put("PROCEDIMIENTO_DESIGNA", procedimiento1);
					else
						registro.put("PROCEDIMIENTO_DESIGNA", "");
				}							
			} else	
				registro.put("PROCEDIMIENTO_DESIGNA", "");	
							
			helperInformes.completarHashSalida(registro,helperInformes.getTurnoSalida(idInstitucion,idTurno));
								
			//Metemos la descripcion del tipo de designa
			Vector vtipoDesignaColegio=null;
			if(idTipoDesigna!=null && !idTipoDesigna.trim().equals("")){
				vtipoDesignaColegio=helperInformes.getTipoDesignaColegio(idInstitucion,idTipoDesigna,idioma);
				for (int s = 0; s < vtipoDesignaColegio.size(); s++) {
					Hashtable tipodesignacolegio = (Hashtable) vtipoDesignaColegio.get(s);
					String tipodesigna = (String)tipodesignacolegio.get("DESC_TIPODESIGNA");
					if (!tipodesigna.equals("")&& tipodesigna!=null){
						registro.put("DESC_TIPODESIGNA", tipodesigna);
					} else {
						registro.put("DESC_TIPODESIGNA", " ");
					}
				}
			} else {
				registro.put("DESC_TIPODESIGNA", " ");
			}
							
	       /**Sacamos la etiqueta TIPO_EJG_COLEGIO en el idioma del interesado si este no tiene idioma se saca en el idioma de la institucion**/
			Vector vtipoejgcolegiodesigna1=getTipoEJGColegioDesigna(idTurno, numeroDesigna, anioDesigna, idInstitucion, idioma);				
			for (int s = 0; s < vtipoejgcolegiodesigna1.size(); s++) {
				Hashtable registropretenciones = (Hashtable) vtipoejgcolegiodesigna1.get(s);
				String tipoejgDesigna = (String)registropretenciones.get("TIPO_EJG_COLEGIO");
				if(tipoejgDesigna!=null && !tipoejgDesigna.trim().equalsIgnoreCase("")){
					registro.put("TIPO_EJG_COLEGIO", tipoejgDesigna);
				} else {
					registro.put("TIPO_EJG_COLEGIO", " ");
				}
			}						
			
			if (vtipoejgcolegiodesigna1.size()==0){
				registro.put("TIPO_EJG_COLEGIO", " ");
			}
			
		  }catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion en getregistrodatosEjg");
		  }
			
		  return registro;
	  }
	  
	  public Vector ejecutaSelectBind(String select, Hashtable codigos) throws ClsExceptions {
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
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
	  public Vector getPersonasDesignadasEjg(Integer idInstitucion, Integer idTipoEJG, Integer anio, Integer numero) throws ClsExceptions,SIGAException {
			Vector datos=new Vector();
			try {
				
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT DISTINCT F_SIGA_GETIDLETRADO_DESIGNA(EJGD.IDINSTITUCION, EJGD.IDTURNO, EJGD.ANIODESIGNA, EJGD.NUMERODESIGNA) AS IDPERSONA ");
				sql.append("FROM SCS_EJGDESIGNA EJGD ");
				sql.append("WHERE EJGD.IDINSTITUCION = ");
				sql.append(idInstitucion);
				sql.append("AND EJGD.IDTIPOEJG = ");
				sql.append(idTipoEJG);
				sql.append("AND EJGD.ANIOEJG =  ");
				sql.append(anio);
				sql.append("AND EJGD.NUMEROEJG =  ");
				sql.append(numero);


				
				
				
				RowsContainer rc = new RowsContainer(); 
				if (rc.find(sql.toString())) {
					for (int i = 0; i < rc.size(); i++){
						Row fila = (Row) rc.get(i);
						Hashtable resultado=fila.getRow();	                  
						datos.add(resultado);
					}
				} 
			}
			catch (Exception e) {
				throw new ClsExceptions (e, "Error al obtener la informacion sobre getPersonasDesignadasEjg");
			}
			return datos;                        
		}
	
	public Long getIdJuzgadoDesigna(String institucion, String anio, String numero, String turno) throws ClsExceptions,SIGAException {
		Long idJuzgado = null;
		try {
			
			StringBuffer sql = new StringBuffer();
			sql.append(" select des.idJuzgado  ");
			sql.append(" from SCS_DESIGNA des  ");
 
			sql.append(" WHERE des.idinstitucion = ");
			sql.append(institucion);
			sql.append(" AND des.ANIO = ");
			sql.append(anio);
			sql.append(" and des.NUMERO =  ");
			sql.append(numero);
			
			sql.append(" and des.IDTURNO = ");
			sql.append(turno);
			RowsContainer rc = new RowsContainer(); 
			if (rc.find(sql.toString())) {
				Row fila = (Row) rc.get(0);
				if(fila.getString("IDJUZGADO") != null && !fila.getString("IDJUZGADO").equals("")){
					idJuzgado = Long.parseLong((String)fila.getString("IDJUZGADO"));
				}
			} 
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al obtener la informacion sobre el idjuzgado de una designa.");
		}
		return idJuzgado;                        
	}
	
	public Vector getDatosProcuradoresEJGRelacionados(String idInstitucion, String numero, String idTurno, String anio, String longitudNumEjg) throws ClsExceptions {
		Vector datos = null;

		//SELECT
		StringBuffer sql = new StringBuffer("SELECT ejg.idprocurador, ejg.idinstitucion_proc, ");
		sql.append("  ejg.idtipoejg, ejg.idinstitucion, ejg.numero, lpad(EJG.numejg,"+longitudNumEjg+",0) numejg, ejg.anio      ");
		
		//FROM 
		sql.append(" FROM scs_ejg             ejg,   ");
		sql.append("      scs_designa         des,   ");
		sql.append("      scs_ejgdesigna      ejgDes ");
		
		//WHERE
		sql.append(" WHERE ejgDes.Aniodesigna 	= des.anio ");
		sql.append("   AND ejgDes.Numerodesigna	= des.numero ");
		sql.append("   AND ejgDes.Idturno 		= des.idturno ");
		sql.append("   AND ejgDes.Idinstitucion = des.idinstitucion ");
		sql.append("   AND ejgDes.Idinstitucion = ejg.idinstitucion ");
		sql.append("   AND ejgDes.Anioejg 		= ejg.anio ");
		sql.append("   AND ejgDes.Numeroejg 	= ejg.numero ");
		sql.append("   AND ejgDes.Idtipoejg 	= ejg.idtipoejg ");
		
		//FILTROS
		sql.append("   AND des.anio 		 = " + anio);
		sql.append("   AND des.numero 		 = " + numero);
		sql.append("   AND des.idturno 		 = " + idTurno);
		sql.append("   AND des.idinstitucion = " + idInstitucion);
		
		try {
			datos = this.ejecutaSelect(sql.toString());
		
		} catch (ClsExceptions e) {
			throw new ClsExceptions(e,e.toString()); 
		}

		return datos;
	}
	
	public void updateDirectHistorico(Long idPersona,Hashtable designaHashtable, String[] claves, String [] clavesCampos,Hashtable  designaOriginalHashtable,List<String> ocultarClaveList,Hashtable<String, String> cambiarNombreSalidaHashtable) throws ClsExceptions {
		UserTransaction tx = usrbean.getTransaction();
		try {
			
			tx.begin();
			
			updateDirect(designaHashtable, claves, clavesCampos);	
			
			CenHistoricoAdm cenHistoricoAdm = new CenHistoricoAdm(usrbean);
			Hashtable historicoHashtable = new Hashtable();
			StringBuffer motivo = new StringBuffer();
			motivo.append(UtilidadesString.getMensajeIdioma(this.usrbean, "gratuita.busquedaDesignas.literal.designa"));
			motivo.append(" ");
			motivo.append(UtilidadesHash.getString(designaOriginalHashtable, ScsDesignaBean.C_ANIO));
			motivo.append("/");
			motivo.append(UtilidadesHash.getString(designaOriginalHashtable, ScsDesignaBean.C_CODIGO));
			historicoHashtable.put(CenHistoricoBean.C_MOTIVO, motivo.toString());
			CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
			 
			boolean isInsertado = admHis.auditoriaColegiados(idPersona,motivo.toString(), ClsConstants.TIPO_CAMBIO_HISTORICO_DESIGNACIONMODIFICACION,designaHashtable, 
					designaOriginalHashtable, clavesCampos,ocultarClaveList,cambiarNombreSalidaHashtable, CenHistoricoAdm.ACCION_UPDATE, usrbean.getLanguage(), false); 
			tx.commit();	
			
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw new ClsExceptions(e,"Error al actualizacion la designa por parte de los colegiados ");
		}
	
	}
	
}