/*
 * Implementa las operaciones sobre la base de datos, es decir: select, insert, update... a la tabla SCS_UNIDADFAMILIAR
 * 
 * Creado: julio.vicente 04/02/2005
 *  
 */

package com.siga.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.redabogacia.sigaservices.app.AppConstants.EEJG_ESTADO;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesString;
import com.siga.beans.eejg.ScsEejgPeticionesAdm;
import com.siga.beans.eejg.ScsEejgPeticionesBean;
import com.siga.general.SIGAException;
import com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm;
			 
public class ScsUnidadFamiliarEJGAdm extends MasterBeanAdministrador {
	
	
	/**
	 * Constructor de la clase. 
	 * 
	 * @param usuario Usuario "logado" en la aplicaci�n. De tipo "Integer".  
	 */
	public ScsUnidadFamiliarEJGAdm (UsrBean usuario) {
		super( ScsUnidadFamiliarEJGBean.T_NOMBRETABLA, usuario);
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

	
	/** Funcion select (String where). Devuele todos los campos de los registros que cumplan los criterios.
	 * @param criteros para filtrar el select, campo where 
	 *  @return vector con los registros encontrados. El objeto es de tipo administrador del bean 
	 * */
	public Vector selectAll(String where) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "SELECT * FROM " + ScsUnidadFamiliarEJGBean.T_NOMBRETABLA + " " + where ;
			
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
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
	 * Efect�a un SELECT en la tabla SCS_SOJ con la clave principal que se desee buscar.  
	 * 
	 * @param hash Hashtable con los campos de b�squeda. De tipo "Hashtable". 
	 * @return Vector con los resultados del SELECT
	 */	
	public Vector selectPorClave(Hashtable hash) throws ClsExceptions {
		
		Vector datos = new Vector(); 
		
		try { 
			String where = " WHERE " + ScsUnidadFamiliarEJGBean.C_IDINSTITUCION + " = " + hash.get(ScsSOJBean.C_IDINSTITUCION) + " AND " + ScsUnidadFamiliarEJGBean.C_IDPERSONA + " = " + hash.get(ScsUnidadFamiliarEJGBean.C_IDPERSONA) + 
							" AND " + ScsUnidadFamiliarEJGBean.C_ANIO + " = " + hash.get(ScsUnidadFamiliarEJGBean.C_ANIO) + " AND " + ScsUnidadFamiliarEJGBean.C_NUMERO + " = " + hash.get(ScsUnidadFamiliarEJGBean.C_NUMERO) + " AND " + ScsUnidadFamiliarEJGBean.C_IDTIPOEJG + " = " + hash.get(ScsUnidadFamiliarEJGBean.C_IDTIPOEJG);
			datos = this.selectAll(where);
		} 
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN BUSCAR POR CLAVE");
		}
		return datos;	
	}	
	
	
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos. La preparaci�n consiste en calcular el
	 * identificador del tel�fono que se va a insertar.
	 * 
	 * @param entrada Hashtable con los campos a insertar. De tipo "Hashtable". 
	 * @return Hashtable con los campos adaptados.
	 */
	public Hashtable prepararInsert (Hashtable entrada)throws ClsExceptions 
	{
		RowsContainer rc = null;		
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsUnidadFamiliarEJGBean.C_IDPERSONA + ") + 1) AS IDPERSONA FROM " + nombreTabla;
			sql += " WHERE " + ScsUnidadFamiliarEJGBean.C_IDINSTITUCION + " = " + entrada.get(ScsUnidadFamiliarEJGBean.C_IDINSTITUCION); 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDPERSONA").equals("")) {
					entrada.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,"1");
				}
				else entrada.put(ScsUnidadFamiliarEJGBean.C_IDPERSONA,(String)prueba.get("IDPERSONA"));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCI�N. C�LCULO DE IDPERSONA");
		};		
		
		return entrada;
	}
	
	public ScsUnidadFamiliarEJGBean prepararInsert (ScsUnidadFamiliarEJGBean entrada)throws ClsExceptions 
	{
		RowsContainer rc = null;		
		
		try { 
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsUnidadFamiliarEJGBean.C_IDPERSONA + ") + 1) AS IDPERSONA FROM " + nombreTabla;
			sql += " WHERE " + ScsUnidadFamiliarEJGBean.C_IDINSTITUCION + " = " + entrada.getIdInstitucion(); 
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("IDPERSONA").equals("")) {
					entrada.setIdPersona(new Integer(1));
				}
				else entrada.setIdPersona(new Integer((String)prueba.get("IDPERSONA")));				
			}						
		}	
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e){
			 throw new ClsExceptions(e,"EXCEPCION EN PREPARAR INSERCI�N. C�LCULO DE IDPERSONA");
		};		
		
		return entrada;
	}
	
	/** Funcion getCamposBean ()
	 *  @return conjunto de datos con los nombres de todos los campos del bean
	 * 
	 */
	public String[] getCamposBean() {
		String[] campos = {	ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,			ScsUnidadFamiliarEJGBean.C_ANIO,		
							ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,				ScsUnidadFamiliarEJGBean.C_NUMERO,
							ScsUnidadFamiliarEJGBean.C_OBSERVACIONES,			ScsUnidadFamiliarEJGBean.C_ENCALIDADDE,			
							ScsUnidadFamiliarEJGBean.C_SOLICITANTE,				ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES,
							ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES,			ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES,
							ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES,	ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES,
							ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES,	ScsUnidadFamiliarEJGBean.C_OTROSBIENES,
							ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,		ScsUnidadFamiliarEJGBean.C_IDPERSONA,
							ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB, 		    ScsUnidadFamiliarEJGBean.C_IDPARENTESCO,
							ScsUnidadFamiliarEJGBean.C_TIPOINGRESO,				ScsUnidadFamiliarEJGBean.C_INCAPACITADO,
							ScsUnidadFamiliarEJGBean.C_CIRCUNSTANCIAS_EXCEPCIONALES
						};
		return campos;
	}
	
	/** Funcion getClavesBean ()
	 *  @return conjunto de datos con los nombres de todos los campos que forman la claves del bean
	 * 
	 */
	public String[] getClavesBean() {
		String[] campos = {	ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,	ScsUnidadFamiliarEJGBean.C_ANIO, ScsUnidadFamiliarEJGBean.C_NUMERO, ScsUnidadFamiliarEJGBean.C_IDTIPOEJG, ScsUnidadFamiliarEJGBean.C_IDPERSONA};
		return campos;
	}

	/** Funcion hashTableToBean (Hashtable hash)
	 *  @param hash Hashtable para crear el bean
	 *  @return bean con la informaci�n de la hashtable
	 * 
	 */
	public MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		ScsUnidadFamiliarEJGBean bean = null;
		try{
			bean = new ScsUnidadFamiliarEJGBean();
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_IDINSTITUCION));
			bean.setAnio(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_ANIO));
			bean.setNumero(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_NUMERO));
			bean.setIdTipoEJG(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_IDTIPOEJG));
			bean.setIdPersona(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_IDPERSONA));
			bean.setObservaciones(UtilidadesHash.getString(hash,ScsUnidadFamiliarEJGBean.C_OBSERVACIONES));
			bean.setEnCalidadDe(UtilidadesHash.getString(hash,ScsUnidadFamiliarEJGBean.C_ENCALIDADDE));
			bean.setSolicitante(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_SOLICITANTE));
			bean.setDescripcionIngresosAnuales(UtilidadesHash.getString(hash,ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES));
			bean.setIngresosAnuales(UtilidadesHash.getDouble(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES));			
			bean.setBienesInmuebles(UtilidadesHash.getString(hash,ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES));
			bean.setImoporteBienesInmuebles(UtilidadesHash.getDouble(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES));			
			bean.setBienesMuebles(UtilidadesHash.getString(hash,ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES));
			bean.setImoporteBienesMuebles(UtilidadesHash.getDouble(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES));			
			bean.setOtrosBienes(UtilidadesHash.getString(hash,ScsUnidadFamiliarEJGBean.C_OTROSBIENES));
			bean.setImporteOtrosBienes(UtilidadesHash.getDouble(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES));
			bean.setTipoGrupoLab(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB));
			bean.setIdParentesco(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_IDPARENTESCO));
			bean.setTipoIngreso(UtilidadesHash.getInteger(hash,ScsUnidadFamiliarEJGBean.C_TIPOINGRESO));
			bean.setIncapacitado(UtilidadesHash.getInteger(hash, ScsUnidadFamiliarEJGBean.C_INCAPACITADO));
			bean.setCircunstanciasExcepcionales(UtilidadesHash.getInteger(hash, ScsUnidadFamiliarEJGBean.C_CIRCUNSTANCIAS_EXCEPCIONALES));
		}
		catch(Exception e){
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		return bean;
	}

	/** Funcion beanToHashTable (MasterBean bean)
	 *  @param bean para crear el hashtable asociado
	 *  @return hashtable con la informaci�n del bean
	 * 
	 */
	public Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		Hashtable hash = new Hashtable();
		
		try{
			ScsUnidadFamiliarEJGBean miBean = (ScsUnidadFamiliarEJGBean) bean;			
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,miBean.getIdInstitucion());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_ANIO,miBean.getAnio());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_NUMERO,miBean.getNumero());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,miBean.getIdTipoEJG());				
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IDPERSONA,miBean.getIdPersona());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_OBSERVACIONES,miBean.getObservaciones());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_ENCALIDADDE,miBean.getEnCalidadDe());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_SOLICITANTE,miBean.getSolicitante());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES,miBean.getDescripcionIngresosAnuales());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES,miBean.getIngresosAnuales());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES,miBean.getBienesInmuebles());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES,miBean.getImoporteBienesInmuebles());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES,miBean.getBienesMuebles());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES,miBean.getImoporteBienesMuebles());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_OTROSBIENES,miBean.getOtrosBienes());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,miBean.getImporteOtrosBienes());	
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB,miBean.getTipoGrupoLab());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_IDPARENTESCO,miBean.getIdParentesco());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_TIPOINGRESO,miBean.getTipoIngreso());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_INCAPACITADO,miBean.getIncapacitado());
			UtilidadesHash.set(hash,ScsUnidadFamiliarEJGBean.C_CIRCUNSTANCIAS_EXCEPCIONALES,miBean.getCircunstanciasExcepcionales());
		}
		catch (Exception e){
			hash = null;
			throw new ClsExceptions (e, "Error al construir el hashTable a partir del bean");			
		}
		return hash;
	}

	/** Funcion getOrdenCampos ()
	 *  @return String[] conjunto de valores con los campos por los que se deber� ordenar la select
	 *  que se ejecute sobre esta tabla
	 */
	protected String[] getOrdenCampos() {
		String[] orden = {ScsUnidadFamiliarEJGBean.C_IDINSTITUCION , ScsUnidadFamiliarEJGBean.C_IDPERSONA};
		return orden;
	}
	
	/** Funcion getCamposActualizablesBean ()
	 *  @return conjunto de datos con los nombres de los campos actualizables
	 * */
	protected String[] getCamposActualizablesBean ()
	{
		String[] campos = {	ScsUnidadFamiliarEJGBean.C_IDINSTITUCION,			ScsUnidadFamiliarEJGBean.C_ANIO,		
				ScsUnidadFamiliarEJGBean.C_IDTIPOEJG,				ScsUnidadFamiliarEJGBean.C_NUMERO,
				ScsUnidadFamiliarEJGBean.C_OBSERVACIONES,			ScsUnidadFamiliarEJGBean.C_ENCALIDADDE,			
				ScsUnidadFamiliarEJGBean.C_SOLICITANTE,				ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES,
				ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES,			ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES,
				ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES,	ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES,
				ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES,	ScsUnidadFamiliarEJGBean.C_OTROSBIENES,
				ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES,		ScsUnidadFamiliarEJGBean.C_IDPERSONA,
				ScsUnidadFamiliarEJGBean.C_TIPOGRUPOLAB, 		    ScsUnidadFamiliarEJGBean.C_IDPARENTESCO,
				ScsUnidadFamiliarEJGBean.C_USUMODIFICACION, 		    ScsUnidadFamiliarEJGBean.C_FECHAMODIFICACION,
				ScsUnidadFamiliarEJGBean.C_TIPOINGRESO,				ScsUnidadFamiliarEJGBean.C_INCAPACITADO,
				ScsUnidadFamiliarEJGBean.C_CIRCUNSTANCIAS_EXCEPCIONALES
			};
		return campos;
	}
	
	public Vector getListadoCartaEJG(String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions{
		String formatoUF="999,999,990.00";
		String consultaUF = 
			"select "+
			"f."+ScsUnidadFamiliarEJGBean.C_ENCALIDADDE+","+
			"p."+ScsPersonaJGBean.C_NIF+","+
			"p."+ScsPersonaJGBean.C_NOMBRE+",p."+ScsPersonaJGBean.C_APELLIDO1+",p."+ScsPersonaJGBean.C_APELLIDO2+","+
			"nvl(to_char(f."+ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES+",'"+formatoUF+"'),'0') IMPORTEINGRESOSANUALES,"+
			"f."+ScsUnidadFamiliarEJGBean.C_DESCRIPCIONINGRESOSANUALES+","+
			"nvl(to_char(f."+ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES  +",'"+formatoUF+"'),'0') IMPORTEBIENESMUEBLES,"+
			"f."+ScsUnidadFamiliarEJGBean.C_BIENESMUEBLES+","+
			"nvl(to_char(f."+ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES+",'"+formatoUF+"'),'0') IMPORTEBIENESINMUEBLES,"+
			"f."+ScsUnidadFamiliarEJGBean.C_BIENESINMUEBLES+","+
			"nvl(to_char(f."+ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES    +",'"+formatoUF+"'),'0') IMPORTEOTROSBIENES,"+
			"f."+ScsUnidadFamiliarEJGBean.C_OTROSBIENES+
			"  from "+ScsUnidadFamiliarEJGBean.T_NOMBRETABLA+" f, "+ScsPersonaJGBean.T_NOMBRETABLA+" p " + 
			" where f."+ScsUnidadFamiliarEJGBean.C_IDINSTITUCION+" = " + institucion+
			"   and f."+ScsUnidadFamiliarEJGBean.C_IDTIPOEJG+" = " + tipoEJG + 
			"   and f."+ScsUnidadFamiliarEJGBean.C_ANIO+" = "+ anio +
			"   and f."+ScsUnidadFamiliarEJGBean.C_NUMERO+" = " + numero +
			"   and f."+ScsUnidadFamiliarEJGBean.C_IDPERSONA+" = p."+ScsPersonaJGBean.C_IDPERSONA+
			"   and f."+ScsUnidadFamiliarEJGBean.C_IDINSTITUCION+" = p."+ScsPersonaJGBean.C_IDINSTITUCION;

		return this.selectGenerico(consultaUF);

	}
	
	public Vector getTotalesCartaEJG(String institucion, String tipoEJG, String anio, String numero) throws ClsExceptions{
		String formatoTUF="999,999,999,990.00";
		String consultaTUF =
			"select "+
			"nvl(to_char(sum("+ScsUnidadFamiliarEJGBean.C_IMPORTEINGRESOSANUALES+"),'"+formatoTUF+"'),'0') SUMAINGRESOS,"+
			"nvl(to_char(sum("+ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESINMUEBLES+"),'"+formatoTUF+"'),'0') SUMAINMUEBLES,"+
			"nvl(to_char(sum("+ScsUnidadFamiliarEJGBean.C_IMPORTEBIENESMUEBLES+")  ,'"+formatoTUF+"'),'0') SUMAMUEBLES,"+
			"nvl(to_char(sum("+ScsUnidadFamiliarEJGBean.C_IMPORTEOTROSBIENES+")    ,'"+formatoTUF+"'),'0') SUMAOTROS"+
			"  from "+ScsUnidadFamiliarEJGBean.T_NOMBRETABLA+
			" where "+ScsUnidadFamiliarEJGBean.C_IDINSTITUCION+" = " + institucion+
			"   and "+ScsUnidadFamiliarEJGBean.C_IDTIPOEJG+" = " + tipoEJG + 
			"   and "+ScsUnidadFamiliarEJGBean.C_ANIO+" = "+ anio +
			"   and "+ScsUnidadFamiliarEJGBean.C_NUMERO+" = " + numero ;
		 
		return this.selectGenerico(consultaTUF);
	}
	
	public DefinirUnidadFamiliarEJGForm getUnidadFamiliar(DefinirUnidadFamiliarEJGForm unidadFamiliarForm,String longitudNumEjg,UsrBean usrBean)throws ClsExceptions{
		
		
		ScsEJGBean ejg = unidadFamiliarForm.getEjg();
		Hashtable<Integer, Object> htCodigos = new Hashtable<Integer, Object>();
		int contador = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT familia.*, nvl(trim(persona.NIF),'') NIF, persona.NOMBRE, persona.APELLIDO1, persona.APELLIDO2,persona.idtipoidentificacion,persona.asistidoautorizaeejg, ");
		sql.append(" (select F_SIGA_GETRECURSO(parentesco.descripcion,:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),usrBean.getLanguage());
		sql.append(") ");
		sql.append(" from scs_parentesco parentesco where parentesco.idinstitucion=:");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),ejg.getIdInstitucion());
		sql.append(" and parentesco.idparentesco=familia.idparentesco) PARENTESCO  ");
		sql.append(" ,decode(ejg.idpersonajg,familia.IDPERSONA,1,0) orden ");
		sql.append(" ,eejg.idpeticion , eejg.estado ,eejg.idxml ,eejg.idpeticion, eejg.csv ");
		sql.append(" ,eejg.idioma ,eejg.fechaconsulta,TO_CHAR(eejg.fechapeticion,'DD/MM/YYYY') FECHAPETICION ");
		sql.append(" ,USU.DESCRIPCION DESCRIPCIONUSUARIO,USU.NIF NIFUSUARIO ");
		sql.append(",LPAD( " + ScsEJGBean.C_NUMEJG + ", "+longitudNumEjg+",0)" + ScsEJGBean.C_NUMEJG) ;
		
		
		sql.append(" FROM SCS_UNIDADFAMILIAREJG familia, SCS_PERSONAJG persona,scs_ejg ejg, scs_eejg_peticiones eejg,ADM_USUARIOS USU ");
		sql.append(" WHERE familia.IDINSTITUCION = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),ejg.getIdInstitucion());
		sql.append(" AND familia.IDTIPOEJG = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),ejg.getIdTipoEJG());
		sql.append(" AND familia.ANIO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),ejg.getAnio());
		sql.append(" AND familia.NUMERO = :");
		contador ++;
		sql.append(contador);
		htCodigos.put(new Integer(contador),ejg.getNumero());
		sql.append("  AND familia.IDPERSONA = persona.IDPERSONA AND persona.IDINSTITUCION = familia.IDINSTITUCION");
	    sql.append(" and ejg.idinstitucion = familia.IDINSTITUCION ");
	    sql.append(" and ejg.idtipoejg = familia.idtipoejg ");
	    sql.append(" and ejg.anio = familia.ANIO ");
	    sql.append(" and ejg.numero = familia.numero ");
	    sql.append(" and familia.IDINSTITUCION =  eejg.idinstitucion (+) ");
	    sql.append(" and familia.idtipoejg = eejg.idtipoejg (+) ");
	    sql.append(" and familia.ANIO = eejg.anio (+) ");
	    sql.append(" and familia.numero = eejg.numero (+) ");
	    sql.append(" and familia.idpersona = eejg.idpersona (+) ");
	    sql.append(" AND eejg.Idusuariopeticion = USU.IDUSUARIO(+) ");
	    sql.append(" AND eejg.IDINSTITUCION = USU.IDINSTITUCION(+) ");
	    
	    if(unidadFamiliarForm.getPeticionEejg()!=null && unidadFamiliarForm.getPeticionEejg().getEstado()!=null){
	    	if(unidadFamiliarForm.getPeticionEejg().getEstado() == EEJG_ESTADO.FINALIZADO.getId()){
		    	sql.append(" and (eejg.estado =:");
		    	contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),unidadFamiliarForm.getPeticionEejg().getEstado());
				/*sql.append(" or eejg.estado =:");
		    	contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador), EEJG_ESTADO.PENDIENTE_INFO.getId());*/
				sql.append(")");
	    	}else{
	    		sql.append(" and eejg.estado =:");
		    	contador ++;
				sql.append(contador);
				htCodigos.put(new Integer(contador),unidadFamiliarForm.getPeticionEejg().getEstado());
	    		
	    	}
	    }
	    
	    sql.append(" order by familia.solicitante desc, orden desc,persona.NOMBRE,eejg.idpeticion desc ");
		
		String literalSolicitante = UtilidadesString.getMensajeIdioma(usrBean,"gratuita.busquedaEJG.literal.solicitante");
		
		unidadFamiliarForm.setEjg(ejg);
		List<DefinirUnidadFamiliarEJGForm> alUnidadFamiliar = null;
		List<String> alPeticiones = null;
		List miembros = new ArrayList();
		try {
			RowsContainer rc = new RowsContainer(); 
												
            if (rc.findBind(sql.toString(),htCodigos)) {
            	alUnidadFamiliar = new ArrayList<DefinirUnidadFamiliarEJGForm>();
            	alPeticiones = new ArrayList<String>();
            	ScsUnidadFamiliarEJGBean unidadFamiliar = null;
            	ScsPersonaJGBean personaJG = null;
            	ScsParentescoBean parentesco = null;
            	
            	
            	
            	ScsPersonaJGAdm admPersonaJG =  new ScsPersonaJGAdm(usrBean);
            	ScsEejgPeticionesAdm admEejgPeticion =  new ScsEejgPeticionesAdm(usrBean);
            	double importeMuebles = 0;
				double importeInmuebles = 0;
				double importeOtrosBienes = 0;
				double importeIngresosAnuales = 0;
				Integer idSolicitante = ejg.getIdPersonaJG();
    			for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
            		Hashtable<String, Object> htFila=fila.getRow();
            		ejg.setNumEJG((String)htFila.get(ScsEJGBean.C_NUMEJG));
            		unidadFamiliar = (ScsUnidadFamiliarEJGBean)this.hashTableToBean(htFila);
            		personaJG = (ScsPersonaJGBean) admPersonaJG.hashTableToBean(htFila);
            		
            		unidadFamiliar.setPersonaJG(personaJG);
            		parentesco = new ScsParentescoBean();
            		ScsEejgPeticionesBean peticionEejg = null;
            		if(unidadFamiliar.getSolicitante()!=null &&unidadFamiliar.getSolicitante().toString().equals(ClsConstants.DB_TRUE))
            			parentesco.setDescripcion(literalSolicitante);
            		else 
            			parentesco.setDescripcion(UtilidadesHash.getString(htFila, "PARENTESCO"));
            		unidadFamiliar.setParentesco(parentesco);
            		if(htFila.get(ScsEejgPeticionesBean.C_IDPETICION)!=null && !htFila.get(ScsEejgPeticionesBean.C_IDPETICION).equals("")){
	            		peticionEejg = (ScsEejgPeticionesBean)admEejgPeticion.hashTableToBean(htFila);
	            		AdmUsuariosBean usuarioPeticion = new AdmUsuariosBean();
	            		usuarioPeticion.setDescripcion(UtilidadesHash.getString(htFila,"DESCRIPCIONUSUARIO"));
	            		usuarioPeticion.setNIF(UtilidadesHash.getString(htFila,"NIFUSUARIO"));
	            		peticionEejg.setUsuarioPeticion(usuarioPeticion);
	            		unidadFamiliar.setPeticionEejg(peticionEejg);
            		}
            		if(unidadFamiliar.getImoporteBienesMuebles()!=null&&!miembros.contains(personaJG.getIdPersona().toString()))
						importeMuebles += unidadFamiliar.getImoporteBienesMuebles().doubleValue();
					if(unidadFamiliar.getImoporteBienesInmuebles()!=null&&!miembros.contains(personaJG.getIdPersona().toString()))
						importeInmuebles += unidadFamiliar.getImoporteBienesInmuebles().doubleValue();
					if(unidadFamiliar.getImporteOtrosBienes()!=null&&!miembros.contains(personaJG.getIdPersona().toString()))
						importeOtrosBienes += unidadFamiliar.getImporteOtrosBienes().doubleValue();
					if(unidadFamiliar.getIngresosAnuales()!=null&&!miembros.contains(personaJG.getIdPersona().toString()))
						importeIngresosAnuales += unidadFamiliar.getIngresosAnuales().doubleValue();
					if(idSolicitante!=null && idSolicitante.compareTo(unidadFamiliar.getPersonaJG().getIdPersona())==0)
						unidadFamiliarForm.setPersonaJG(unidadFamiliar.getPersonaJG());
					DefinirUnidadFamiliarEJGForm unidad = unidadFamiliar.getUnidadFamiliarEjgForm();
					unidad.setEjg(ejg);
					unidad.setPermisoEejg(unidadFamiliarForm.getPermisoEejg());
					unidad.setEsComision(unidadFamiliarForm.getEsComision());
					if(unidadFamiliarForm.getModo()!=null)
						unidad.setModo(unidadFamiliarForm.getModo());
					StringBuffer peticion = new StringBuffer();
					
					peticion.append(unidadFamiliar.getIdInstitucion());
					peticion.append("||");
					peticion.append(unidadFamiliar.getAnio());
					peticion.append("||");
					peticion.append(unidadFamiliar.getNumero());
					peticion.append("||");
					peticion.append(unidadFamiliar.getIdTipoEJG());
					peticion.append("||");
					peticion.append(unidadFamiliar.getPersonaJG().getIdPersona());
					peticion.append("||");
					if(!alPeticiones.contains(peticion.toString())){
						alPeticiones.add(peticion.toString());
						alUnidadFamiliar.add(unidad);
					}
					if(!miembros.contains(personaJG.getIdPersona().toString()))
						miembros.add(personaJG.getIdPersona().toString());
					
            	}
    			
    			unidadFamiliarForm.setImporteIngresosAnuales(UtilidadesString.formatoImporte(importeIngresosAnuales));
    			unidadFamiliarForm.setImporteBienesMuebles(UtilidadesString.formatoImporte(importeMuebles));
				unidadFamiliarForm.setImporteBienesInmuebles(UtilidadesString.formatoImporte(importeInmuebles));
				unidadFamiliarForm.setImporteOtrosBienes(UtilidadesString.formatoImporte(importeOtrosBienes));
				
    			unidadFamiliarForm.setUnidadFamiliar(alUnidadFamiliar);
            } 
       } catch (Exception e) {
       		throw new ClsExceptions (e, "Error al ejecutar consulta de getUnidadFamiliar");
       }
       return unidadFamiliarForm;

	}
	
	/* Metodo que nos devuelve todos los datos relativos a la unidad familiar
	 * Se ha creado para recuperar los datos postales en caso de ser destinatario de un EJG
	 */
	public Vector getDatosInteresadoEjg(String idInstitucion, String tipoEjg,
			String anio, String numero,String idioma, String idPersonaJG) throws Exception  
	{
		try {
			Hashtable htCodigos = new Hashtable();
			int contador = 0;
			StringBuffer sql = new StringBuffer();
			sql.append("select per.idpersona            as dest_idpersona, ");
			sql.append("       per.fechanacimiento      as dest_fechanacimiento, ");
			//sql.append("       per.fechamodificacion    as dest_fechamodificacion, ");
			//sql.append("       per.usumodificacion      as dest_usumodificacion, ");
			//sql.append("       per.idinstitucion        as dest_idinstitucion, ");
			//sql.append("       per.idpais               as dest_idpais, ");
			sql.append("       per.nif                  as dest_nif, ");
			sql.append("       per.nombre||' '||per.apellido1||' '||per.apellido2 as dest_nombrecompleto, ");
			sql.append("       per.nombre               as dest_nombre, ");
			sql.append("       per.apellido1            as dest_apellido1, ");
			sql.append("       per.apellido2            as dest_apellido2, ");
			sql.append("       per.direccion            as dest_direccion, ");
			sql.append("       per.codigopostal         as dest_codigopostal, ");
			//sql.append("       per.idprofesion          as dest_idprofesion, ");
			sql.append("       per.regimen_conyugal     as dest_regimen_conyugal, ");
			//sql.append("       per.idprovincia          as dest_idprovincia, ");
			//sql.append("       per.idpoblacion          as dest_idpoblacion, ");
			//sql.append("       per.idestadocivil        as dest_idestadocivil, ");
			sql.append("       per.tipopersonajg        as dest_tipopersonajg, ");
			//sql.append("       per.idtipoidentificacion as dest_idtipoidentificacion, ");
			sql.append("       per.observaciones        as dest_observaciones, ");
			//sql.append("       per.idrepresentantejg    as dest_idrepresentantejg, ");
			//sql.append("       per.idtipoencalidad      as dest_idtipoencalidad, ");
			sql.append("       per.sexo                 as dest_sexo, ");
			//sql.append("       per.idlenguaje           as dest_idlenguaje, ");
			sql.append("       per.numerohijos          as dest_numerohijos, ");
			sql.append("       per.fax                  as dest_fax, ");
			sql.append("       per.correoelectronico    as dest_correoelectronico, ");
			sql.append("       f_siga_getrecurso( pais.nombre, "+ idioma +") as dest_nacionalidad, ");
			sql.append("       f_siga_getrecurso( prov.nombre, "+ idioma +") as dest_provincia, ");
			sql.append("       f_siga_getrecurso( pobl.nombre, "+ idioma +") as dest_poblacion, ");
			sql.append("       tel.numerotelefono as dest_telefono, ");
			sql.append("       f_siga_getrecurso( par.descripcion, "+ idioma +") as dest_parentesco ");
			sql.append("  from scs_personajg per, scs_unidadfamiliarejg uf, cen_pais pais, cen_poblaciones pobl, ");
			sql.append("      cen_provincias prov, scs_telefonospersona tel, scs_parentesco par ");
			contador ++;
			htCodigos.put(new Integer(contador),idPersonaJG);
			sql.append(" where per.idpersona =:1 ");
			contador ++;
			htCodigos.put(new Integer(contador),idInstitucion);
			sql.append("   and uf.idinstitucion =:2 ");
			contador ++;
			htCodigos.put(new Integer(contador),tipoEjg);
			sql.append("   and uf.idtipoejg =:3 ");
			contador ++;
			htCodigos.put(new Integer(contador),anio);
			sql.append("   and uf.anio =:4 ");
			contador ++;
			htCodigos.put(new Integer(contador),numero);
			sql.append("   and uf.numero =:5 ");
			sql.append("   and uf.idpersona = per.idpersona ");
			sql.append("   and uf.idinstitucion = per.idinstitucion ");
			sql.append("   and pais.idpais(+) = per.idpais ");
			sql.append("   and prov.idprovincia(+) = per.idprovincia ");
			sql.append("   and pobl.idprovincia(+) = per.idprovincia ");
			sql.append("   and pobl.idpoblacion(+) = per.idpoblacion ");
			sql.append("   and tel.idpersona(+) = per.idpersona ");
			sql.append("   and tel.idinstitucion(+) = per.idinstitucion ");
			sql.append("   and par.idinstitucion(+) = uf.idinstitucion ");
			sql.append("   and par.idparentesco(+) = uf.idparentesco ");
           
			
			HelperInformesAdm helperInformes = new HelperInformesAdm();	
			return helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos);
			
		} catch (ClsExceptions e) {
			throw new SIGAException("No se ha podido generar la plantilla por no existir solicitante");
		
		} catch (Exception e) {
			throw new Exception ("Error ScsEJGAdm.getInteresadosEjgSalida.");
		}
	}
	public Vector getSolicitantesEjg(Integer idInstitucion, Integer idTipoEJG, Integer anio, Integer numero) throws ClsExceptions,SIGAException {
		Vector datos=new Vector();
		try {
			
			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT IDPERSONA FROM SCS_UNIDADFAMILIAREJG "); 
			sql.append(" WHERE ANIO =  ");
			sql.append(anio);
			sql.append(" AND IDINSTITUCION =  ");
			sql.append(idInstitucion);
			sql.append("  AND NUMERO = "); 
			sql.append(numero);
			sql.append(" AND IDTIPOEJG = "); 
			sql.append(idTipoEJG);
			sql.append(" AND SOLICITANTE = 1 ");
			
			
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
			throw new ClsExceptions (e, "Error al obtener la informacion sobre getSolicitantesEjg");
		}
		return datos;                        
	}
	

}