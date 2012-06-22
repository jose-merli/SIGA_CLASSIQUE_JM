package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;


/**
 * @author daniel.campos - 15-10-2004
 * @version daniel.campos - 15-12-2004
 * @version daniel.campos - 28-12-2004 - Incorpora metodo estadosColegiales
 * @version miguel.villegas - 12-01-2005 - Incorpora metodo para adecuar 
 *   fechas a la hora de insertar en tablas
 * @version RGG - cambio visibilidad public class CenColegiadoAdm 
 *   extends MasterBeanAdministrador {
 * @version adrian.ayala - 04-06-2008 - creado existeColegiado ()
 */
public class CenColegiadoAdm extends MasterBeanAdmVisible
{
	public CenColegiadoAdm (UsrBean usu) {
		super (CenColegiadoBean.T_NOMBRETABLA, usu);
	}
	
	/**
	 * @param tabla
	 * @param usuario
	 * @param userbean
	 * @param idInsitucionClientes
	 * @param idPersonaCliente
	 */
	public CenColegiadoAdm(Integer usuario, UsrBean usrbean,int idInstitucionCliente, long idPersonaCliente) {
		super( CenColegiadoBean.T_NOMBRETABLA, usuario, usrbean, idInstitucionCliente,idPersonaCliente);
	}	

	
	protected String[] getCamposBean() {
		String [] campos = {CenColegiadoBean.C_COMUNITARIO, 		CenColegiadoBean.C_FECHADEONTOLOGIA,
							CenColegiadoBean.C_FECHAINCORPORACION, 	CenColegiadoBean.C_FECHAJURA,
							CenColegiadoBean.C_FECHAMODIFICACION,	CenColegiadoBean.C_FECHAMOVIMIENTO,
							CenColegiadoBean.C_FECHAPRESENTACION, 	CenColegiadoBean.C_FECHATITULACION, 
							CenColegiadoBean.C_IDINSTITUCION, 		CenColegiadoBean.C_IDPERSONA, 
							CenColegiadoBean.C_IDTIPOSSEGURO,		CenColegiadoBean.C_INDTITULACION,
							CenColegiadoBean.C_JUBILACIONCUOTA,		CenColegiadoBean.C_NCOLEGIADO,
							CenColegiadoBean.C_NCOMUNITARIO, 		CenColegiadoBean.C_OTROSCOLEGIOS,
							CenColegiadoBean.C_SITUACIONEJERCICIO, 	CenColegiadoBean.C_SITUACIONEMPRESA,
							CenColegiadoBean.C_SITUACIONRESIDENTE,	CenColegiadoBean.C_USUMODIFICACION,
							CenColegiadoBean.C_CUENTACONTABLESJCS,  CenColegiadoBean.C_IDENTIFICADORDS,
							CenColegiadoBean.C_NMUTUALISTA};
		return campos;
	}

	protected String[] getClavesBean() {
		String [] claves = {CenColegiadoBean.C_IDINSTITUCION, 		CenColegiadoBean.C_IDPERSONA};
		return claves;
	}
	
	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {

		CenColegiadoBean bean = null;
		
		try {
			bean = new CenColegiadoBean();
			bean.setComunitario	(UtilidadesHash.getString(hash, CenColegiadoBean.C_COMUNITARIO));
			bean.setFechaDeontologia(UtilidadesHash.getString(hash, CenColegiadoBean.C_FECHADEONTOLOGIA));
			bean.setFechaIncorporacion(UtilidadesHash.getString(hash, CenColegiadoBean.C_FECHAINCORPORACION));
			bean.setFechaJura(UtilidadesHash.getString(hash,CenColegiadoBean.C_FECHAJURA));
			bean.setFechaMod(UtilidadesHash.getString(hash,CenColegiadoBean.C_FECHAMODIFICACION));
			bean.setFechaMovimiento(UtilidadesHash.getString(hash,CenColegiadoBean.C_FECHAMOVIMIENTO));
			bean.setFechaPresentacion(UtilidadesHash.getString(hash,CenColegiadoBean.C_FECHAPRESENTACION));
			bean.setFechaTitulacion(UtilidadesHash.getString(hash,CenColegiadoBean.C_FECHATITULACION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash,CenColegiadoBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash,CenColegiadoBean.C_IDPERSONA));
			bean.setIdTipoSeguro(UtilidadesHash.getInteger(hash,CenColegiadoBean.C_IDTIPOSSEGURO));
			bean.setIndTitulacion(UtilidadesHash.getString(hash,CenColegiadoBean.C_INDTITULACION));
			bean.setJubilacionCuota(UtilidadesHash.getString(hash,CenColegiadoBean.C_JUBILACIONCUOTA));
			bean.setNColegiado(UtilidadesHash.getString(hash,CenColegiadoBean.C_NCOLEGIADO));
			bean.setNComunitario(UtilidadesHash.getString(hash,CenColegiadoBean.C_NCOMUNITARIO));
			bean.setOtrosColegios(UtilidadesHash.getString(hash,CenColegiadoBean.C_OTROSCOLEGIOS));
			bean.setSituacionEjercicio(UtilidadesHash.getString(hash,CenColegiadoBean.C_SITUACIONEJERCICIO));
			bean.setSituacionEmpresa(UtilidadesHash.getString(hash,CenColegiadoBean.C_SITUACIONEMPRESA));
			bean.setSituacionResidente(UtilidadesHash.getString(hash,CenColegiadoBean.C_SITUACIONRESIDENTE));
			bean.setUsuMod(UtilidadesHash.getInteger(hash,CenColegiadoBean.C_USUMODIFICACION));
			bean.setCuentaContableSJCS(UtilidadesHash.getString(hash,CenColegiadoBean.C_CUENTACONTABLESJCS));
			bean.setIdentificadorDS(UtilidadesHash.getString(hash,CenColegiadoBean.C_IDENTIFICADORDS));
			bean.setNMutualista(UtilidadesHash.getString(hash,CenColegiadoBean.C_NMUTUALISTA));
		}
		catch (Exception e) { 
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenColegiadoBean b = (CenColegiadoBean) bean;
			UtilidadesHash.set(htData, CenColegiadoBean.C_COMUNITARIO, 			b.getComunitario());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHADEONTOLOGIA, 	b.getFechaDeontologia());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAINCORPORACION, 	b.getFechaIncorporacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAJURA, 			b.getFechaJura());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAMOVIMIENTO, 		b.getFechaMovimiento());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAPRESENTACION, 	b.getFechaPresentacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHATITULACION, 		b.getFechaTitulacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_IDPERSONA, 			b.getIdPersona());
			UtilidadesHash.set(htData, CenColegiadoBean.C_IDTIPOSSEGURO, 		b.getIdTipoSeguro());
			UtilidadesHash.set(htData, CenColegiadoBean.C_INDTITULACION, 		b.getIndTitulacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_JUBILACIONCUOTA, 		b.getJubilacionCuota());
			UtilidadesHash.set(htData, CenColegiadoBean.C_NCOLEGIADO, 			b.getNColegiado());
			UtilidadesHash.set(htData, CenColegiadoBean.C_NCOMUNITARIO, 		b.getNComunitario());
			UtilidadesHash.set(htData, CenColegiadoBean.C_OTROSCOLEGIOS, 		b.getOtrosColegios());
			UtilidadesHash.set(htData, CenColegiadoBean.C_SITUACIONEJERCICIO, 	b.getSituacionEjercicio());
			UtilidadesHash.set(htData, CenColegiadoBean.C_SITUACIONEMPRESA, 	b.getSituacionEmpresa());
			UtilidadesHash.set(htData, CenColegiadoBean.C_SITUACIONRESIDENTE, 	b.getSituacionResidente());
			UtilidadesHash.set(htData, CenColegiadoBean.C_USUMODIFICACION, 		b.getUsuMod());
			UtilidadesHash.set(htData, CenColegiadoBean.C_CUENTACONTABLESJCS, 	b.getCuentaContableSJCS());
			UtilidadesHash.set(htData, CenColegiadoBean.C_IDENTIFICADORDS,      b.getIdentificadorDS());
			UtilidadesHash.set(htData, CenColegiadoBean.C_NMUTUALISTA,      	b.getNMutualista());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}

	/* (non-Javadoc)
	 * @see com.siga.beans.MasterBeanAdministrador#getOrdenCampos()
	 */
	protected String[] getOrdenCampos() {
		return this.getClavesBean();
	}
	
	/** 
	 * Obtiene los datos colegiales de una persona dependiendo de la institucion
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @return  CenColegiadoBean con los datos colegiales  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public CenColegiadoBean getDatosColegiales (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException{
		try {
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash, CenColegiadoBean.C_IDPERSONA, idPersona);
			UtilidadesHash.set(hash, CenColegiadoBean.C_IDINSTITUCION, idInstitucion);
			Vector v = this.select(hash);
			if ((v != null) && (v.size() == 1)) {
				return (CenColegiadoBean) v.get(0);
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
		return null;
	}

	/** 
	 * Obtiene el estado colegial y los datos asociados de una persona dependiendo de la institucion
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @return  HashTable con los datos  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public Hashtable getEstadoColegial (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException{
		
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = "";
			
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),idPersona.toString());
		    codigos.put(new Integer(2),idInstitucion.toString());
			
			sql = " SELECT " + CenDatosColegialesEstadoBean.C_FECHAESTADO	+ ", " +
							   CenDatosColegialesEstadoBean.T_NOMBRETABLA	+ "." + CenDatosColegialesEstadoBean.C_IDESTADO		+ " AS " + CenEstadoColegialBean.C_IDESTADO + ", " +
							   CenDatosColegialesEstadoBean.C_IDINSTITUCION	+ ", " +
							   CenDatosColegialesEstadoBean.C_IDPERSONA		+ ", " +
							   CenDatosColegialesEstadoBean.C_OBSERVACIONES + ", " +
							   UtilidadesMultidioma.getCampoMultidioma(CenEstadoColegialBean.C_DESCRIPCION, this.usrbean.getLanguage()) +  
				  " FROM " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + ", " + CenEstadoColegialBean.T_NOMBRETABLA +
				  " WHERE " + CenDatosColegialesEstadoBean.C_IDPERSONA + " = :1 AND " + 
				  			  CenDatosColegialesEstadoBean.C_IDINSTITUCION + " = :2 AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE AND " +
							  CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDESTADO + " = " + CenEstadoColegialBean.T_NOMBRETABLA + "." + CenEstadoColegialBean.C_IDESTADO +
				  " ORDER BY " + CenDatosColegialesEstadoBean.C_FECHAESTADO	+ " DESC ";
			
			// RGG cambio visibilidad
			rc = this.findBind(sql,codigos);
			if (rc!=null) {
				if (rc.size() >= 1) {
					Row fila = (Row) rc.get(0);
					return (Hashtable)fila.getRow();
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recueperar los datos");
		}
		return null;
	}

	/** 
	 * Recoge informacion sobre los estados colegiales de un determinado cliente colegiado <br/>
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @return  Vector - Filas de la tabla seleccionadas  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public Vector getEstadosColegiales (Long idPersona, Integer idInstitucion, String idioma) throws ClsExceptions, SIGAException{ 
		
		RowsContainer rc = null;
		Vector datos=new Vector();
		
		try { 
			rc = new RowsContainer(); 
			Hashtable codigos = new Hashtable();
			codigos.put(new Integer(1),idioma);
			codigos.put(new Integer(2),idPersona.toString());
		    codigos.put(new Integer(3),idInstitucion.toString());
			String sql = "";
			sql = " SELECT " + CenDatosColegialesEstadoBean.C_FECHAESTADO	+ ", " +
							   CenDatosColegialesEstadoBean.T_NOMBRETABLA	+ "." + CenDatosColegialesEstadoBean.C_IDESTADO		+ " AS " + CenEstadoColegialBean.C_IDESTADO + ", " +
							   CenDatosColegialesEstadoBean.C_IDINSTITUCION	+ ", " +
							   CenDatosColegialesEstadoBean.C_IDPERSONA		+ ", " +
							   CenDatosColegialesEstadoBean.C_OBSERVACIONES + ", " +
							   "F_SIGA_GETRECURSO (" + CenEstadoColegialBean.C_DESCRIPCION + ",:1) " + CenEstadoColegialBean.C_DESCRIPCION  + 
				  " FROM " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + ", " + CenEstadoColegialBean.T_NOMBRETABLA +
				  " WHERE " + CenDatosColegialesEstadoBean.C_IDPERSONA + " = :2 AND " + 
				  			  CenDatosColegialesEstadoBean.C_IDINSTITUCION + " = :3 AND " +
							  CenDatosColegialesEstadoBean.T_NOMBRETABLA + "." + CenDatosColegialesEstadoBean.C_IDESTADO + " = " + CenEstadoColegialBean.T_NOMBRETABLA + "." + CenEstadoColegialBean.C_IDESTADO +
				  " ORDER BY " + CenDatosColegialesEstadoBean.C_FECHAESTADO	+ " DESC ";
			
            // RGG cambio visibilidad
			rc = this.findBind(sql,codigos);
			if (rc!=null) {
            	for (int i = 0; i < rc.size(); i++){
            		Row fila = (Row) rc.get(i);
	                datos.add(fila);
	            }
	        }			
		} 
		
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
		return datos;
	}
	
	/** 
	 * Adecua los formatos de las fechas para la insercion en BBDD. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable prepararFormatosFechas (Hashtable entrada)throws ClsExceptions, SIGAException 
	{
		String fecha;
		
		try {		
			entrada.put("FECHAPRESENTACION",GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAPRESENTACION")));			
			entrada.put("FECHAINCORPORACION",GstDate.getApplicationFormatDate("",(String)entrada.get("FECHAINCORPORACION")));
			fecha=(String)entrada.get("FECHAJURA");
			if ((fecha!=null)&&(!fecha.equalsIgnoreCase(""))){
				entrada.put("FECHAJURA",GstDate.getApplicationFormatDate("",fecha));	
			}
			fecha=(String)entrada.get("FECHATITULACION");			
			if ((fecha!=null)&&(!fecha.equalsIgnoreCase(""))){
				entrada.put("FECHATITULACION",GstDate.getApplicationFormatDate("",fecha));	
			}
			fecha=(String)entrada.get("FECHADEONTOLOGIA");			
			if ((fecha!=null)&&(!fecha.equalsIgnoreCase(""))){
				entrada.put("FECHADEONTOLOGIA",GstDate.getApplicationFormatDate("",fecha));	
			}						
		}	
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al adecuar los formatos delas fechas.");		
		}
		
		return entrada;
	}
	
	/** 
	 * Prepara los campos de check para insertar en tabla. <br/>
	 * @param  entrada - tabla hash con los valores del formulario 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable controlFormatosCheck (Hashtable entrada) 
	{
						
		if (!entrada.containsKey(CenColegiadoBean.C_COMUNITARIO)) {
			entrada.put(CenColegiadoBean.C_COMUNITARIO,"0");
		}
			
		if (!entrada.containsKey(CenColegiadoBean.C_INDTITULACION)) {
			entrada.put(CenColegiadoBean.C_INDTITULACION,"0");
		}							

		if (!entrada.containsKey(CenColegiadoBean.C_OTROSCOLEGIOS)) {
			entrada.put(CenColegiadoBean.C_OTROSCOLEGIOS,"0");
		}
			
		if (!entrada.containsKey(CenColegiadoBean.C_JUBILACIONCUOTA)) {
			entrada.put(CenColegiadoBean.C_JUBILACIONCUOTA,"0");
		}		
		
		if (!entrada.containsKey(CenColegiadoBean.C_SITUACIONRESIDENTE)) {
			entrada.put(CenColegiadoBean.C_SITUACIONRESIDENTE,"0");
		}
			
		if (!entrada.containsKey(CenColegiadoBean.C_SITUACIONEJERCICIO)) {
			entrada.put(CenColegiadoBean.C_SITUACIONEJERCICIO,"0");
		}							

		if (!entrada.containsKey(CenColegiadoBean.C_SITUACIONEMPRESA)) {
			entrada.put(CenColegiadoBean.C_SITUACIONEMPRESA,"0");
		}		
		
		return entrada;
	}	
		
	/** 
	 * Pasa de bean a hashtable sin tener en cuenta fecha modificacion y usuario modificacion. <br/>
	 * @param  entrada - bean origen 
	 * @return  Hashtable - Tabla ya preparada  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */	
	public Hashtable prepararHashtable(CenColegiadoBean bean) throws ClsExceptions, SIGAException {
		
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			UtilidadesHash.set(htData, CenColegiadoBean.C_COMUNITARIO,bean.getComunitario());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHADEONTOLOGIA,bean.getFechaDeontologia());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAINCORPORACION,bean.getFechaIncorporacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAJURA,bean.getFechaJura());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHAPRESENTACION,bean.getFechaPresentacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_FECHATITULACION,bean.getFechaTitulacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_IDINSTITUCION,bean.getIdInstitucion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_IDPERSONA,bean.getIdPersona());
			if (bean.getIdTipoSeguro()!=null) 
				UtilidadesHash.set(htData, CenColegiadoBean.C_IDTIPOSSEGURO,bean.getIdTipoSeguro());
			else
				UtilidadesHash.set(htData, CenColegiadoBean.C_IDTIPOSSEGURO,"");
			UtilidadesHash.set(htData, CenColegiadoBean.C_INDTITULACION,bean.getIndTitulacion());
			UtilidadesHash.set(htData, CenColegiadoBean.C_JUBILACIONCUOTA,bean.getJubilacionCuota());
			UtilidadesHash.set(htData, CenColegiadoBean.C_NCOLEGIADO,bean.getNColegiado());
			UtilidadesHash.set(htData, CenColegiadoBean.C_NCOMUNITARIO,bean.getNComunitario());
			UtilidadesHash.set(htData, CenColegiadoBean.C_OTROSCOLEGIOS,bean.getOtrosColegios());
			UtilidadesHash.set(htData, CenColegiadoBean.C_SITUACIONEJERCICIO,bean.getSituacionEjercicio());
			UtilidadesHash.set(htData, CenColegiadoBean.C_SITUACIONEMPRESA,bean.getSituacionEmpresa());
			UtilidadesHash.set(htData, CenColegiadoBean.C_SITUACIONRESIDENTE,bean.getSituacionResidente());
			UtilidadesHash.set(htData, CenColegiadoBean.C_CUENTACONTABLESJCS,bean.getCuentaContableSJCS());
			UtilidadesHash.set(htData, CenColegiadoBean.C_NMUTUALISTA,bean.getNMutualista());
		}
		catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
	}	
	
	/**
	 * Modifica los datos colegiales y rellena la tabla de historicos (CEN_HISTORICO)	 
	 * @param  modificacion - datos modificados 
	 * @param  original - datos originales
	 * @param  entHistorico - entrada historico
	 * @return  Boolean - Resultado de la operacion  
	 * @exception  ClsExceptions  En cualquier caso de error 
	 */
	public boolean modificacionConHistorico (Hashtable modificacion, Hashtable original, Hashtable entHistorico, String idioma) throws ClsExceptions, SIGAException 
	{

		boolean resultado=false;
		
		try {
			String comunitarioOriginal = (String) original.get(CenColegiadoBean.C_COMUNITARIO);
			String comunitario = (String) modificacion.get(CenColegiadoBean.C_COMUNITARIO);
			
			//LMS 11/08/2006
			//No hace falta calcular de nuevo el NCOLEGIADO, pues ya le tiene copiado de NCOMUNITARIO.
//			//Si era comunitario y quito el check del mismo calculo el nuevo ncolegiado:
//			//En este caso el letrado no podra volver a ser espanhol.
//			if (comunitarioOriginal.equals(ClsConstants.DB_TRUE) &&
//					comunitario.equals(ClsConstants.DB_FALSE)) {
//						String nColegiadoNuevo = this.getNColegiado(new Integer((String)original.get(CenColegiadoBean.C_IDINSTITUCION)));
//						modificacion.put(CenColegiadoBean.C_NCOLEGIADO,nColegiadoNuevo);
//				}  
				
			// RGG cambio para poder modificar el ncomunitario en la solicitud de incorporacion
			if (comunitario.equals(ClsConstants.DB_TRUE)) {
				modificacion.put(CenColegiadoBean.C_NCOMUNITARIO,(String)modificacion.get(CenColegiadoBean.C_NCOLEGIADO));
				modificacion.put(CenColegiadoBean.C_NCOLEGIADO,"");
			}  

			// RGG 29/12/2006 cambio para poder mantener separado ncomunitario/ncolegiado. El campo ncomunitario se mantiene y se
			// genera un nuevo codigo para ncolegiado
			if (comunitarioOriginal.equals(ClsConstants.DB_TRUE) &&	comunitario.equals(ClsConstants.DB_FALSE)) {

			    //	String nColegiadoNuevo = this.getNColegiado(new Integer((String)original.get(CenColegiadoBean.C_IDINSTITUCION)));

		        GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.usrbean);
		        String sContadorUnico = parametrosAdm.getValor((String)original.get(CenColegiadoBean.C_IDINSTITUCION), ClsConstants.MODULO_CENSO, "CONTADOR_UNICO_NCOLEGIADO_NCOMUNIT", "1"); 
		        if (sContadorUnico == null || sContadorUnico.equals("")) {
		            sContadorUnico = new String (ClsConstants.DB_TRUE);
		        }
		        
		        // Solo solicitamos un nuevo id si usa contardores distintos para ncolegiado y ncomunitario
		        if (!sContadorUnico.equals(ClsConstants.DB_TRUE)) {
					String nColegiadoNuevo = this.getNColegiado_NComunitario(new Integer((String)original.get(CenColegiadoBean.C_IDINSTITUCION)), ClsConstants.TIPO_COLEGIACION_ESPANHOL);
					modificacion.put(CenColegiadoBean.C_NCOLEGIADO,nColegiadoNuevo);
		        }
			}  
		
			//Se comprueba q el nº de clegiado no existe para ese colegio
			
			if(comunitarioOriginal.equals(ClsConstants.DB_TRUE))
			{
				String nComunitarioOri = (String) original.get(CenColegiadoBean.C_NCOMUNITARIO);
				String nComunitarioModi = (String) modificacion.get(CenColegiadoBean.C_NCOMUNITARIO);
				
				if(nComunitarioOri==null)
					nComunitarioOri ="";
				
				if(nComunitarioModi != null && !nComunitarioModi.equals(nComunitarioOri))
				{
					if (this.existeColegiado 
						(new Integer ((String)original.get(CenColegiadoBean.C_IDINSTITUCION)),(String)modificacion.get(CenColegiadoBean.C_NCOLEGIADO),(String)modificacion.get(CenColegiadoBean.C_NCOMUNITARIO)) != null)
							throw new SIGAException ("error.message.NumColegiadoRepetido");
				}	
			}
			else
			{
			
				String nColegiadoOri = (String) original.get(CenColegiadoBean.C_NCOLEGIADO);
				String nColegiadoModi = (String) modificacion.get(CenColegiadoBean.C_NCOLEGIADO);
			
				if(nColegiadoOri==null)
					nColegiadoOri ="";
			
				if(nColegiadoModi != null && !nColegiadoModi.equals(nColegiadoOri))
				{
					if (this.existeColegiado 
							(new Integer ((String)original.get(CenColegiadoBean.C_IDINSTITUCION)),(String)modificacion.get(CenColegiadoBean.C_NCOLEGIADO),(String)modificacion.get(CenColegiadoBean.C_NCOMUNITARIO)) != null)
							throw new SIGAException ("error.message.NumColegiadoRepetido");
				}
			}
			if (this.update(modificacion,original)) {
				// Obtengo del formulario datos que me interesaran para la insercion
				CenColegiadoBean beanColegiado = new CenColegiadoBean ();
				beanColegiado = (CenColegiadoBean) this.hashTableToBean(modificacion);
				beanColegiado.setOriginalHash(original);
				CenHistoricoAdm admHis = new CenHistoricoAdm (this.usrbean);
				if (admHis.insertCompleto(entHistorico, beanColegiado, CenHistoricoAdm.ACCION_UPDATE, idioma)) {
					resultado=true;
				}	
			}					
		}
		catch (SIGAException ex) {
			throw ex;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al insertar datos en B.D.");
		}
		return resultado;
	}	
	
	
	
	/**
	 * Busca el numero de colegiado (o numero comunitario si procede). 
	 * Devuelve null si el usuario no es colegiado o se ha producido una excepcion al buscarlo.
	 *  
	 * @param user UsrBean
	 * @return
	 */
	public  String getIdentificadorColegiado(UsrBean user){
		String consulta=null, numcolegiado=null;
		CenColegiadoBean bean = null;

		try {
			if (user.isLetrado()){
			    Hashtable codigos = new Hashtable();
				codigos.put(new Integer(1),user.getLocation());
			    codigos.put(new Integer(2),new Long(user.getIdPersona()).toString());
				consulta = " WHERE "+CenColegiadoBean.C_IDINSTITUCION+"=:1"+
						   " AND "+CenColegiadoBean.C_IDPERSONA+"=:2";
				
				bean = (CenColegiadoBean)this.selectBind(consulta,codigos).get(0);
				numcolegiado = this.getIdentificadorColegiado(bean);
			} else
				numcolegiado = null;
		} catch (Exception e){
			numcolegiado = null;
		}
		return numcolegiado;
	}
	
	/**
	 * Obtiene el numero identificador del colegiado: si es comunitario el nº comunitario, sino el nº colegiado 	 
	 * @param  CenColegiadoBean con los datos del colegiado 
	 * @return  String el numero de identificador  
	 */
	public String getIdentificadorColegiado (CenColegiadoBean bean) {
		
		if (bean == null) 
			return "";
		
		String rc = "";
		if (bean.getComunitario().equalsIgnoreCase(ClsConstants.DB_TRUE)) 
			rc = bean.getNComunitario();
		else 
			rc = bean.getNColegiado();
		
		// RGG 13-03-2006 cambio para que nunca devuelva un blanco y produzca un filtro vacio en las busquedas.
		if (rc == null) 
			return "-1";
		if (rc.trim().equals("")) 
			return "-1";
		return rc;
	}

	/**
	 * Obtiene el numero identificador de la persona a partir del numero de colegiado. 	 
	 * @param   String numero de colegiado 
	 * @param   String identificador de la institucion 
	 * @return  String el numero de identificador de la persona 
	 */
	public String getIdPersona (String nColegiado, String idInstitucion) {
		
		String consulta = null;
		CenColegiadoBean bean = null;
		String idPersona = null;
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
	    codigos.put(new Integer(2),nColegiado);
		
		if (nColegiado == null || idInstitucion == null) 
			return "";
		
		try {
			
				consulta = " WHERE "+CenColegiadoBean.C_IDINSTITUCION+"=:1 "+
						   " AND decode (" + CenColegiadoBean.C_COMUNITARIO + ", '1'," + CenColegiadoBean.C_NCOMUNITARIO + "," + CenColegiadoBean.C_NCOLEGIADO + ") = :2";  			
				bean = (CenColegiadoBean)this.selectBind(consulta,codigos).get(0);
				idPersona = bean.getIdPersona().toString(); 
		
			
		} catch (Exception e){
			idPersona = null;
		}
		return idPersona;
		
	}

	
	/**
	 * Realiza el alta de un colegiado y de sus datosColegialesEstado 
	 * @param beanCol CenColegiadoBean con los datos del colegiado
	 * @param idEstado int con el id del estado colegial
	 * @return boolean con el resultado
	 * @throws SIGAException con la excepcion de aplicación
	 */
	public boolean insertConEstado (CenColegiadoBean beanCol, int idEstado, String fechaEstado) throws SIGAException 
	{
		try {
			CenDatosColegialesEstadoAdm admDCE = new CenDatosColegialesEstadoAdm(this.usrbean);
			CenDatosColegialesEstadoBean beanDCE = new CenDatosColegialesEstadoBean(); 
			if (!this.insert(beanCol)) {
				throw new SIGAException(this.getError());
			} else {
				// inserto los datoColegialesEstado
				beanDCE.setIdPersona(beanCol.getIdPersona());
				beanDCE.setIdInstitucion(beanCol.getIdInstitucion());
				beanDCE.setIdEstado(new Integer(idEstado));
				// jbd 08-02-2010 Añadimos una fecha manualmente, si no se pone nada se usa sysdate
				if(fechaEstado.equalsIgnoreCase(""))
					beanDCE.setFechaEstado("SYSDATE");
				else
					beanDCE.setFechaEstado(fechaEstado);
				if (!admDCE.insert(beanDCE)) {
					throw new ClsExceptions(admDCE.getError());
				}
			}
			return true;
		}
		catch (SIGAException e) {
			throw e;
		}
		catch (Exception e) {
			throw new SIGAException ("Error al insertar datos en B.D.",e);
		}
	}

	/** 
	 * Obtiene un numero de colegiado o comunitario nuevo para una institucion. Tiene encuenta si el contador es comun a colegiado y comunitario o distintos 
	 * @param  idInstitucion - identificador de la institucion	
	 * @param  tipo - tipo de colegiacion	
	 * @return  String  con el uevo identificador
	 * @exception  SIGAExceptions  Error de aplicacion 
	 */	
	public String getNColegiado_NComunitario (Integer idInstitucion, int tipo) throws ClsExceptions, SIGAException
	{
		try {
		    RowsContainer rc = new RowsContainer(); 
	
	        GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.usrbean);
	        String sContadorUnico = parametrosAdm.getValor("" + idInstitucion, ClsConstants.MODULO_CENSO, "CONTADOR_UNICO_NCOLEGIADO_NCOMUNIT", "1"); 
	        if (sContadorUnico == null || sContadorUnico.equals("")) {
	            sContadorUnico = new String (ClsConstants.DB_TRUE);
	        }
	        
	        // unico contador para ncolegiado y ncomunitario
	        if (sContadorUnico.equals(ClsConstants.DB_TRUE)) {
	            Hashtable codigos = new Hashtable();
				codigos.put(new Integer(1),idInstitucion.toString());
			    String sql = " SELECT DECODE (COUNT(1), 0, 1, MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO   + ",'0'))) + 1) AS MAXIMO_NCOLEGIADO, " +
	            	  		        " DECODE (COUNT(1), 0, 1, MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOMUNITARIO + ",'0'))) + 1) AS MAXIMO_NCOMUNITARIO " +
		  			           " FROM " + CenColegiadoBean.T_NOMBRETABLA + 
		  			          " WHERE " + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=:1";		
	            
				rc = this.findBind(sql,codigos);
	            if (rc != null) {
					if (rc.size()>0)	{
						Row fila = (Row) rc.get(0);
						Hashtable prueba = fila.getRow();
		            	Integer nColegiado   = new Integer((String)prueba.get("MAXIMO_NCOLEGIADO"));
		            	Integer nComunitario = new Integer((String)prueba.get("MAXIMO_NCOMUNITARIO"));
		            	if (nColegiado.intValue() > nComunitario.intValue()) 
		            	    return "" + nColegiado.intValue();
		            	else 
		            	    return "" + nComunitario.intValue();
					}
				}
	        }
	        
	        // Contadores distintos para ncolegiado y ncomunitario
	        else {
	            String sql = "";
	
	            Hashtable codigos = new Hashtable();
				codigos.put(new Integer(1),idInstitucion.toString());
			    sql = " SELECT DECODE (COUNT(1), 0, 1, MAX(TO_NUMBER(NVL(" + CenColegiadoBean.T_NOMBRETABLA + "." + (tipo == ClsConstants.TIPO_COLEGIACION_COMUNITARIO ? CenColegiadoBean.C_NCOMUNITARIO : CenColegiadoBean.C_NCOLEGIADO) + ",'0'))) + 1) AS MAXIMO " +
	            	    " FROM " + CenColegiadoBean.T_NOMBRETABLA + 
		               " WHERE " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + " =:1 ";						 										
	            
				rc = this.findBind(sql,codigos);
	            if (rc!=null) {
					if (rc.size()>0)	{
						Row fila = (Row) rc.get(0);
						Hashtable prueba = fila.getRow();
		            	return (String)prueba.get("MAXIMO");
					}
				}
	        }
	        return null;	        
		}
		catch(Exception e) {
			throw new ClsExceptions (e, "Error al obtener un nuevo numero de colegiado / comunitario.");
		}
	}
	
	
//	/** 
//	 * Obtiene un numero de colegiado nuevo para una institucion
//	 * @param  idInstitucion - identificador de la institucion	
//	 * @return  String  con el uevo identificador
//	 * @exception  SIGAExceptions  Error de aplicacion 
//	 */		
//	public String getNColegiado (Integer idInstitucion) throws ClsExceptions, SIGAException{
//		String salida = null;
//		try{
//            RowsContainer rc = new RowsContainer(); 
//            /* CAMBIO DE UNICIDAD NCOLEGIADO/NCOMUNITARIO
//            String sql ="SELECT DECODE((MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+","+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+")))+1),NULL,1,(MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+","+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+")))+1)) AS MAXIMO FROM " + 
//			CenColegiadoBean.T_NOMBRETABLA + 
//			" WHERE " + 
//			CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion.toString();						 										
//			*/
//            
//
//	        String sql = " SELECT DECODE (COUNT(1), 0, 1, MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+",'0'))) + 1) AS MAXIMO " +
//	  			    " FROM " + CenColegiadoBean.T_NOMBRETABLA + 
//	  			   " WHERE " + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion.toString();						 										
//	
//            // RGG cambio visibilidad
//			rc = this.find(sql);
//            if (rc!=null) {
//				if (rc.size()>0)	{
//					Row fila = (Row) rc.get(0);
//					Hashtable prueba = fila.getRow();
//	            	salida = (String)prueba.get("MAXIMO");
//				}
//			}
//            return salida;
//		}
//		catch(Exception e) {
//			throw new ClsExceptions (e, "Error al obtener un nuevo numero de colegiado.");
//		}
//	}
//
//	/** 
//	 * Obtiene un numero de comunitario nuevo para una institucion
//	 * @param  idInstitucion - identificador de la institucion	
//	 * @return  String  con el uevo identificador
//	 * @exception  SIGAExceptions  Error de aplicacion 
//	 */		
//	public String getNComunitario (Integer idInstitucion) throws ClsExceptions, SIGAException{
//		String salida = null;
//		try{
//            RowsContainer rc = new RowsContainer(); 
//            /* se cambia la unicidad de ncolegiado/ncomunitario
//            String sql ="SELECT DECODE((MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+","+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+")))+1),NULL,1,(MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOLEGIADO+","+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+")))+1)) AS MAXIMO FROM " + 
//			CenColegiadoBean.T_NOMBRETABLA + 
//			" WHERE " + 
//			CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion.toString();						 										
//			*/
//            String sql = " SELECT DECODE (COUNT(1), 0, 1, MAX(TO_NUMBER(NVL("+CenColegiadoBean.T_NOMBRETABLA+"."+CenColegiadoBean.C_NCOMUNITARIO+",'0'))) + 1) AS MAXIMO " +
//            			   " FROM " + CenColegiadoBean.T_NOMBRETABLA + 
//            			  " WHERE " + CenColegiadoBean.T_NOMBRETABLA +"."+ CenColegiadoBean.C_IDINSTITUCION + "=" + idInstitucion.toString();						 										
//			
//            // RGG cambio visibilidad
//            rc = this.find(sql);
//            if (rc!=null) {
//				if (rc.size()>0)	{
//					Row fila = (Row) rc.get(0);
//					Hashtable prueba = fila.getRow();
//	            	salida = (String)prueba.get("MAXIMO");
//				}
//			}
//            return salida;
//		}
//		catch(Exception e) {
//			throw new ClsExceptions (e, "Error al obtener un nuevo numero de comunitario.");
//		}
//	}

	
	/** 
	 * Comprueba si existe el numero de colegiado o comunitario en la tabla de colegiados para una misma institucion
	 * @param  numero: Número de colegiado 
	 * @param  tipo de colegiacion
	 * @param  idInstitucion 
	 * @return  boolean con el resultado  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public boolean existeNColegiado_NComunitario_X_Institucion (String numero, int tipo, String idInstitucion) throws ClsExceptions, SIGAException
	{
	    try {
	        GenParametrosAdm parametrosAdm = new GenParametrosAdm(this.usrbean);
	        String sContadorUnico = parametrosAdm.getValor(idInstitucion, ClsConstants.MODULO_CENSO, "CONTADOR_UNICO_NCOLEGIADO_NCOMUNIT", "1"); 
	        if (sContadorUnico == null || sContadorUnico.equals("")) {
	            sContadorUnico = new String (ClsConstants.DB_TRUE);
	        }
	        
	        // hay un unico contador para ncolegiado y ncomunitario
	        if (sContadorUnico.equals(ClsConstants.DB_TRUE)) {
	            Hashtable codigos = new Hashtable();
	            codigos.put(new Integer(1),idInstitucion);
	            codigos.put(new Integer(2),numero);
	            codigos.put(new Integer(3),numero);
			    String where = " WHERE " + CenColegiadoBean.C_IDINSTITUCION + " =:1  AND (" + CenColegiadoBean.C_NCOLEGIADO + " = :2 OR " + CenColegiadoBean.C_NCOMUNITARIO + " = :3)";
				Vector v = this.selectBind(where,codigos);
				if ((v != null) && (v.size()>0))
					return true;
				else 
					return false;
	        }
	        // hay dos contadores distintos para ncolegiado y ncomunitario
	        else {
	            Hashtable codigos = new Hashtable();
	            codigos.put(new Integer(1),idInstitucion);
	            codigos.put(new Integer(2),numero);
	            
	            String where = " WHERE " + CenColegiadoBean.C_IDINSTITUCION + " = :1 AND " + (tipo == ClsConstants.TIPO_COLEGIACION_COMUNITARIO ? CenColegiadoBean.C_NCOMUNITARIO : CenColegiadoBean.C_NCOLEGIADO) + " = :2 ";
				Vector v = this.selectBind(where,codigos);
				if ((v != null) && (v.size()>0))
					return true;
	        	else
					return false;
	        }
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
	}

//	/** 
//	 * Comprueba si existe el numero  de colegiado o el número de comunitario 
//	 * en la tabla de colegiados (Solo uno de los dos valores debe tener valor)
//	 * @param  nColegiado Número de colegiado 
//	 * @return  boolean con el resultado  
//	 * @exception  ClsExceptions  En cualquier caso de error
//	 */		
//	public boolean existeNColegiado(String nColegiado) throws ClsExceptions, SIGAException{
//		try {
//			String valor=nColegiado;
//			Vector v = this.select(" WHERE "+CenColegiadoBean.C_NCOLEGIADO+"='"+valor+"' OR "+CenColegiadoBean.C_NCOMUNITARIO+"='"+valor+"'");
//			if ((v != null) && (v.size()>0)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Error al recuperar los datos");
//		}
//	}

//	/** 
//	 * Comprueba si existe el numero  de colegiado
//	 * en la tabla de colegiados 
//	 * para una misma institucion
//	 * @param  nColegiado Número de colegiado 
//	 * @param  idInstitucion 
//	 * @return  boolean con el resultado  
//	 * @exception  ClsExceptions  En cualquier caso de error
//	 */		
//	public boolean existeNColegiadoInstitucion(String nColegiado,String idInstitucion) throws ClsExceptions, SIGAException{
//		try {
//			String valor=nColegiado;
//			//Vector v = this.select(" WHERE ("+CenColegiadoBean.C_NCOLEGIADO+"='"+valor+"' OR "+CenColegiadoBean.C_NCOMUNITARIO+"='"+valor+"') AND ("+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+")");
//			Vector v = this.select(" WHERE "+CenColegiadoBean.C_NCOLEGIADO+"='"+valor+"' AND ("+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+")");
//			if ((v != null) && (v.size()>0)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Error al recuperar los datos");
//		}
//	}
//
//	/** 
//	 * Comprueba si existe el numero  de comunitario 
//	 * en la tabla de colegiados 
//	 * para una misma institucion
//	 * @param  nColegiado Número de colegiado 
//	 * @param  idInstitucion 
//	 * @return  boolean con el resultado  
//	 * @exception  ClsExceptions  En cualquier caso de error
//	 */		
//	public boolean existeNComunitarioInstitucion(String nColegiado,String idInstitucion) throws ClsExceptions, SIGAException{
//		try {
//			String valor=nColegiado;
//			//Vector v = this.select(" WHERE ("+CenColegiadoBean.C_NCOLEGIADO+"='"+valor+"' OR "+CenColegiadoBean.C_NCOMUNITARIO+"='"+valor+"') AND ("+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+")");
//			Vector v = this.select(" WHERE "+CenColegiadoBean.C_NCOMUNITARIO+"='"+valor+"' AND ("+CenColegiadoBean.C_IDINSTITUCION+"="+idInstitucion+")");
//			if ((v != null) && (v.size()>0)) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//		catch (Exception e) {
//			throw new ClsExceptions (e, "Error al recuperar los datos");
//		}
//	}

	/** 
	 * Obtiene el estado colegial y los datos asociados de una persona dependiendo de la institucion
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @return  booelan si reside en algun colegio  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public boolean getResidenciaColegio (String idPersona, String idInstitucion) throws ClsExceptions, SIGAException{
		
		RowsContainer rc = null;
		boolean resultado = false;
		String colegios = "";
		
		try { 
			rc = new RowsContainer(); 
			String sql = "";
			Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),idPersona);
            codigos.put(new Integer(2),idInstitucion);
            sql = " SELECT COUNT(" + CenColegiadoBean.C_IDPERSONA	+ ") as CUENTA" +
				  " FROM " + CenColegiadoBean.T_NOMBRETABLA +
				  " WHERE " + CenColegiadoBean.C_IDPERSONA + " = :1 AND " + 
				  			  CenColegiadoBean.C_IDINSTITUCION + " = :2 AND " +
				  			  CenColegiadoBean.C_SITUACIONRESIDENTE + " = '" + ClsConstants.DB_TRUE + "'";
			
			if (rc.findBind(sql,codigos)) {
				if (rc.size() >= 1) {
					Row fila = (Row) rc.get(0);					
					Hashtable prueba = fila.getRow();
	            	colegios = (String)prueba.get("CUENTA");
	            	if (colegios.equalsIgnoreCase("0")){
	            		resultado=false;
	            	}
	            	else{
	            		resultado=true;
	            	}
				}
			}
		} 
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al recueperar los datos");
		}
		return resultado;
	}
	
	/**
	 * actualiza el campo otros colegios en todos los colegios
	 * a los que pertenece
	 * @param idPersona Long con el id de persona
	 * @param idInstitucion Integer con el id de institucion
	 */
	public void actualizaColegiadoOtraInstitucion (Long idPersona, Integer idInstitucion) throws ClsExceptions, SIGAException 
	{
		try
		{
		    Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),idPersona.toString());
            codigos.put(new Integer(2),idInstitucion.toString());
            Vector v = this.selectBind(" WHERE IDPERSONA=:1 AND IDINSTITUCION <> :2",codigos);
			if (v!=null && v.size()>0) {
				for (int i=0;i<v.size();i++) {
					//Bean Original:
					CenColegiadoBean colBean = (CenColegiadoBean) v.get(i);

					//Nueva Hashtable:
					Hashtable nuevaHash = new Hashtable();
					nuevaHash.put(CenColegiadoBean.C_IDINSTITUCION,colBean.getIdInstitucion());
					nuevaHash.put(CenColegiadoBean.C_IDPERSONA,colBean.getIdPersona());
					nuevaHash.put(CenColegiadoBean.C_OTROSCOLEGIOS,ClsConstants.DB_TRUE);
					
					String claves[] = {CenColegiadoBean.C_IDINSTITUCION,CenColegiadoBean.C_IDPERSONA};
					String campos[] = {CenColegiadoBean.C_OTROSCOLEGIOS};
					
					//UPDATE:
					if (!this.updateDirect(nuevaHash, claves, campos)) {						
						throw new SIGAException(this.getError());
					}
				}
			}
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	
	/**
	 * Comprueba si existe un colegiado
	 * 
	 * @param idPersona Long
	 * @param idInstitucion Integer
	 * @return CenColegiadoBean o null
	 */
	public CenColegiadoBean existeColegiado (Integer idInstitucion,String numeroColegiado,String numeroComunitario)
			throws ClsExceptions, SIGAException
	{
		try
		{
			CenColegiadoBean salida = null;
			Hashtable codigos = new Hashtable();
			Vector v = new Vector();
			if(numeroComunitario!=null && !numeroComunitario.equals(""))
	        {
				codigos.put(new Integer(1),idInstitucion.toString());
				//codigos.put(new Integer(2),numeroColegiado);
				//codigos.put(new Integer(2),numeroComunitario);
				//codigos.put(new Integer(4),numeroColegiado);
				//codigos.put(new Integer(5),numeroComunitario);
           
            	//v = this.selectBind(" WHERE IDINSTITUCION = :1 AND  (NCOLEGIADO = :2 OR NCOMUNITARIO = :3 OR NCOLEGIADO = :5 OR NCOMUNITARIO = :4)" ,codigos);
				v = this.selectBind(" WHERE IDINSTITUCION = :1 AND '"+numeroComunitario+"' IN (NCOLEGIADO,NCOMUNITARIO)" ,codigos);
            }
            else
            {
            	codigos.put(new Integer(1),idInstitucion.toString());
				//codigos.put(new Integer(2),numeroColegiado);
				//codigos.put(new Integer(3),numeroColegiado);				            	
				v = this.selectBind(" WHERE IDINSTITUCION = :1 AND '"+numeroColegiado+"' IN (NCOLEGIADO,NCOMUNITARIO)" ,codigos);
            	//v = this.selectBind(" WHERE IDINSTITUCION = :1 AND  (NCOLEGIADO = :2 OR NCOMUNITARIO = :3)" ,codigos);
            }
            /*
            Vector v;
            if (comunitario.equals("0")){
            	v = this.selectBind(" WHERE NCOLEGIADO = :1 AND IDINSTITUCION = :2" ,codigos);
            }else{
            	v = this.selectBind(" WHERE NCOMUNITARIO = :1 AND IDINSTITUCION = :2" ,codigos);
            }
            */
			if (v != null && v.size () > 0)
				salida = (CenColegiadoBean) v.get(0);
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	} //existeColegiado ()
	
	/**
	 * Comprueba si existe un colegiado
	 * 
	 * @param idPersona Long
	 * @param idInstitucion Integer
	 * @return CenColegiadoBean o null
	 */
	public CenColegiadoBean existeColegiado (Long idPersona, 
											 Integer idInstitucion)
			throws ClsExceptions, SIGAException
	{
		try
		{
			CenColegiadoBean salida = null;
			Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),idPersona.toString());
            codigos.put(new Integer(2),idInstitucion.toString());
            Vector v = this.selectBind(" WHERE IDPERSONA = :1 AND IDINSTITUCION = :2" ,codigos);
			if (v != null && v.size () > 0)
				salida = (CenColegiadoBean) v.get(0);
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	} //existeColegiado ()	
	
	/**
	 * Comprueba si existe un colegiado para una institución concreta
	 * @param nColegiado String
	 * @param idPersona Long
	 * @param idInstitucion Integer
	 * @return CenColegiadoBean o null
	 */
	public boolean existeNColegiado (String nColegiado, 
									 int idInstitucion,
									 int idTipoCol)
			throws ClsExceptions, SIGAException
	{
		try
		{
			CenColegiadoBean salida = null;
			Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),nColegiado);
            codigos.put(new Integer(2),nColegiado);
            codigos.put(new Integer(3),idInstitucion);
            
            Vector v;
            //if (idTipoCol == ClsConstants.TIPO_COLEGIACION_ESPANHOL){
            	v = this.selectBind(" WHERE (NCOLEGIADO = :1 OR NCOMUNITARIO = :2) AND IDINSTITUCION = :3" ,codigos);
           // }else{
            	//v = this.selectBind(" WHERE NCOMUNITARIO = :1 AND IDINSTITUCION = :2" ,codigos);
            //}
            // En caso de que v tenga un elemento o mas
            //significa que ya existe un letrado con ese nº de colegiado para esa institución.
			if (v != null && v.size () > 0)
				return true;
			else
				return false;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	}
	
	/**
	 * Comprueba si existe un colegiado a traves de idpersona para una
	 * institucion diferente a la pasada como parametro
	 * 
	 * @param idPersona Long
	 * @param idInstitucion Integer
	 * @return CenColegiadoBean o null
	 */
	public CenColegiadoBean existeColegiadoOtraInstitucion (Long idPersona, 
															Integer idInstitucion)
			throws ClsExceptions, SIGAException
	{
		try
		{
			CenColegiadoBean salida = null;
			Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),idPersona.toString());
            codigos.put(new Integer(2),idInstitucion.toString());
            Vector v = this.selectBind(" WHERE IDPERSONA = :1 AND IDINSTITUCION <> :2" ,codigos);
			if (v != null && v.size () > 0)
				salida = (CenColegiadoBean) v.get(0);
			return salida;
		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error al consultar datos en B.D.");
		}
	} //existeColegiadoOtraInstitucion ()
	
	/**
	 * @param idInstitucion Integer con el id de institucion
	 * @param idPersona Long con el id de persona
	 * @param lenguaje
	 * @return sexo, numero colegiado, nombre, direccion y telefonos
	 * @throws ClsExceptions
	 */
	public Hashtable obtenerDatosColegiado(String idInstitucion, String idPersona, String lenguaje)throws ClsExceptions{
		Hashtable ht=null;
		String lg=lenguaje.toUpperCase();
		String hombre=UtilidadesString.getMensajeIdioma(lg,"censo.sexo.hombre");
		String mujer=UtilidadesString.getMensajeIdioma(lg,"censo.sexo.mujer");
		
		Hashtable codigos = new Hashtable();
		codigos.put(new Integer(1),idInstitucion);
		codigos.put(new Integer(2),idInstitucion);
		codigos.put(new Integer(3),idInstitucion);
        codigos.put(new Integer(4),idPersona);
        String consultaLetrado=
			"select "+
			"decode(p."+CenPersonaBean.C_SEXO+",'H','"+hombre+"','M','"+mujer+"','') SEXO_LETRADO,"+
			"F_SIGA_CALCULONCOLEGIADO(c."+CenColegiadoBean.C_IDINSTITUCION+",c."+CenColegiadoBean.C_IDPERSONA+") NCOLEGIADO_LETRADO,"+
			"p."+CenPersonaBean.C_APELLIDOS1+"||' '||p."+CenPersonaBean.C_APELLIDOS2+"||', '|| p."+CenPersonaBean.C_NOMBRE+" NOMBRE_LETRADO,"+
			"p."+CenPersonaBean.C_APELLIDOS1+ " APELLIDO1_LETRADO,"+
			"p."+CenPersonaBean.C_APELLIDOS2+ " APELLIDO2_LETRADO,"+
			"p."+CenPersonaBean.C_NOMBRE+ " N_LETRADO,"+
			" F_SIGA_GETRECURSO(tra."+CenTratamientoBean.C_DESCRIPCION +", 1) TRATAMIENTO, "+
			"tra."+CenTratamientoBean.C_IDTRATAMIENTO +" IDTRATAMIENTO,"+
			"d2.*,"+
			"d6."+CenDireccionesBean.C_TELEFONO1+" TELEFONO1_LETRADO,"+
			"d6."+CenDireccionesBean.C_TELEFONO2+" TELEFONO2_LETRADO,"+
			"d6."+CenDireccionesBean.C_MOVIL+" MOVIL_LETRADO,"+
			"d6."+CenDireccionesBean.C_FAX1+" FAX1"+
			" from "+
			CenPersonaBean.T_NOMBRETABLA+" p,"+
			CenColegiadoBean.T_NOMBRETABLA+" c,"+
			CenClienteBean.T_NOMBRETABLA+" cl,"+
			CenTratamientoBean.T_NOMBRETABLA+" tra,"+		
			"(select d."+CenDireccionesBean.C_IDPERSONA+", d."+CenDireccionesBean.C_DOMICILIO+"||' '||d."+CenDireccionesBean.C_CODIGOPOSTAL+"||' '||pb."+CenPoblacionesBean.C_NOMBRE+" DIRECCION_LETRADO,"+
			"d.DOMICILIO DIRECCION_LETR," +
			"d.CODIGOPOSTAL CP_LETR," +
			"pb.NOMBRE POBLACION_LETR," +
			"d.poblacionextranjera POBLACION_EXTRANJERA,"+
			"(select pr.nombre from cen_provincias pr where pr.idprovincia=d.idprovincia) PROVINCIA_LETR"+
			"   from "+CenDireccionesBean.T_NOMBRETABLA+" d,"+CenPoblacionesBean.T_NOMBRETABLA+" pb,"+CenDireccionTipoDireccionBean.T_NOMBRETABLA+" td "+
			"  where d."+CenDireccionesBean.C_IDINSTITUCION+"=:1"+
			"    and d."+CenDireccionesBean.C_IDINSTITUCION+"= td."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+
			"    and d."+CenDireccionesBean.C_IDDIRECCION+"= td."+CenDireccionTipoDireccionBean.C_IDDIRECCION+
			"    and d."+CenDireccionesBean.C_IDPERSONA+"= td."+CenDireccionTipoDireccionBean.C_IDPERSONA+
			"    AND d."+CenDireccionesBean.C_FECHABAJA+" is null "+
			"    and td."+CenTipoDireccionBean.C_IDTIPODIRECCION+"=2 "+
			
			"    and d."+CenDireccionesBean.C_IDPOBLACION+"=pb."+CenPoblacionesBean.C_IDPOBLACION+"(+)"+
			"  order by d."+CenDireccionesBean.C_FECHAMODIFICACION+" desc "+
			")    d2,"+
			"(select d."+CenDireccionesBean.C_IDPERSONA+",d."+CenDireccionesBean.C_TELEFONO1+",d."+CenDireccionesBean.C_TELEFONO2+",d."+CenDireccionesBean.C_MOVIL+",d."+CenDireccionesBean.C_FAX1+
			"   from "+CenDireccionesBean.T_NOMBRETABLA+" d,"+CenDireccionTipoDireccionBean.T_NOMBRETABLA+" td "+
			"  where d."+CenDireccionesBean.C_IDINSTITUCION+"=:2"+
			"    and d."+CenDireccionesBean.C_IDINSTITUCION+"= td."+CenDireccionTipoDireccionBean.C_IDINSTITUCION+
			"    and d."+CenDireccionesBean.C_IDDIRECCION+"= td."+CenDireccionTipoDireccionBean.C_IDDIRECCION+
			"    and d."+CenDireccionesBean.C_IDPERSONA+"= td."+CenDireccionTipoDireccionBean.C_IDPERSONA+
			"    AND d."+CenDireccionesBean.C_FECHABAJA+" is null "+
			"    and td."+CenTipoDireccionBean.C_IDTIPODIRECCION+"=6 "+
			"  order by d."+CenDireccionesBean.C_FECHAMODIFICACION+" desc "+
			")    d6 "+
			"where c."+CenColegiadoBean.C_IDINSTITUCION+"=:3"+
			"  and c."+CenColegiadoBean.C_IDINSTITUCION+"=cl."+CenClienteBean.C_IDINSTITUCION+
			"  and p."+CenPersonaBean.C_IDPERSONA+" = :4"+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=c."+CenColegiadoBean.C_IDPERSONA+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=cl."+CenClienteBean.C_IDPERSONA+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=d2."+CenDireccionesBean.C_IDPERSONA+"(+)"+
			"  and p."+CenPersonaBean.C_IDPERSONA+"=d6."+CenDireccionesBean.C_IDPERSONA+"(+)"+
			"  and cl."+ CenClienteBean.C_IDTRATAMIENTO+"= tra."+CenTratamientoBean.C_IDTRATAMIENTO+
			"  and rownum <2";
		
		RowsContainer rc = this.findBind(consultaLetrado,codigos);
		if(rc!=null && rc.size()==1){
			ht=((Row)rc.get(0)).getRow();
		}
		return ht;
		
	}
	public Vector getInformeColegiado (String idInstitucion, String idPersona,String idioma,boolean isInforme)throws ClsExceptions {
			Vector vInforme = null;
			HelperInformesAdm helperInformes = new HelperInformesAdm();
			CenEstadoColegialAdm admEstadoCol = null;
			CenDireccionesAdm admDirecciones = null;
			CenDatosCVAdm admDatosCV = null;
			CenTiposCVAdm admTiposCV = null;
			CenTiposCVSubtipo1Adm admSubtipo1CV = null;
			CenTiposCVSubtipo2Adm admSubtipo2CV = null;
			CenCuentasBancariasAdm admCuentasBancarias = new CenCuentasBancariasAdm(usrbean);
			
			try {
				
				vInforme = getDatosInformeColegiado(idInstitucion, idPersona, idioma, isInforme); 

				Hashtable registro = null;
				if(isInforme){
					registro = (Hashtable) vInforme.get(0);	
				}else{
					registro = ((Row) vInforme.get(0)).getRow();
				}
				

				//Aniadimos los datos del colegiado del ejg
				admEstadoCol = new CenEstadoColegialAdm(usrbean);
				//Añadimos el estado Colegial
				helperInformes.completarHashSalida(registro,admEstadoCol.getEstadoColegial(idInstitucion, idPersona, idioma));
				//Añadimos los campos de la direccion preferente para el tipo de envio
				//AQUI TENGO DUDAS, NO SE SI ES ESTO LO QUE PIDEN. EN TAL CASO ME TENDRE QUE TRAER EL IDTIPOENVIO.
				//AHORA SE LO METO A PELO
				admDirecciones = new CenDireccionesAdm(usrbean);
				helperInformes.completarHashSalida(registro,admDirecciones.getDireccionPreferente(idInstitucion, idPersona, "1"));
				
				helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteAbono(idInstitucion, idPersona));
				helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteCargo(idInstitucion, idPersona));
				helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteSJCS(idInstitucion, idPersona));
								
			}catch (Exception e) {
				throw new ClsExceptions (e, "Error ScsEJGAdm.getInformeColegiado.");
			}
		return vInforme;
						
	}
	
	private Vector getDatosInformeColegiado (String idInstitucion, String idPersona,String idioma,boolean isInforme
	) throws ClsExceptions  
	{
		Vector datos = null;
		try {
			Hashtable htCodigos = new Hashtable();
			
			int keyContador = 0;


			StringBuffer sql = new StringBuffer();
			sql.append(" ");
			
			sql.append(" SELECT PER.NOMBRE, PER.APELLIDOS1, PER.APELLIDOS2, ");
			sql.append(" PER.NIFCIF, PER.IDTIPOIDENTIFICACION, TO_CHAR(PER.FECHANACIMIENTO, 'dd-mm-yyyy') FECHANACIMIENTO, ");
			sql.append(" PER.IDESTADOCIVIL,PER.NATURALDE, PER.FALLECIDO, PER.SEXO,"); 
			sql.append(" DECODE(PER.SEXO,'H','o','a') as O_A,");
			sql.append(" DECODE(PER.SEXO,'H','el','la') as EL_LA,");
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(EC.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_ESTADOCIVIL, ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);

			sql.append(" F_SIGA_GETRECURSO(TI.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_TIPOIDENTIFICACION, ");
			
			sql.append(" TO_CHAR(CLI.FECHAALTA, 'dd-mm-yyyy') FECHAALTA, CLI.CARACTER,      CLI.PUBLICIDAD, ");
			sql.append(" CLI.GUIAJUDICIAL, CLI.ABONOSBANCO, CLI.CARGOSBANCO, ");
			sql.append(" CLI.COMISIONES, CLI.IDTRATAMIENTO, CLI.IDLENGUAJE, CLI.FOTOGRAFIA, ");
			sql.append(" CLI.ASIENTOCONTABLE, TO_CHAR(CLI.FECHACARGA, 'dd-mm-yyyy') FECHACARGA, CLI.LETRADO, TO_CHAR(CLI.FECHAACTUALIZACION, 'dd-mm-yyyy') FECHAACTUALIZACION, ");
			sql.append(" TO_CHAR(CLI.FECHAEXPORTCENSO, 'dd-mm-yyyy') FECHAEXPORTCENSO, CLI.NOENVIARREVISTA, CLI.NOAPARECERREDABOGACIA, ");

			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(TRA.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_TRATAMIENTO, ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);

			sql.append(" F_SIGA_GETRECURSO(LEN.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_LENGUAJE, ");
			
			sql.append(" TO_CHAR(COL.FECHAPRESENTACION, 'dd-mm-yyyy') FECHAPRESENTACION,  TO_CHAR(COL.FECHAINCORPORACION, 'dd-mm-yyyy') FECHAINCORPORACION, ");
			sql.append(" COL.INDTITULACION, COL.JUBILACIONCUOTA, COL.SITUACIONEJERCICIO, ");
			sql.append(" COL.SITUACIONRESIDENTE, COL.SITUACIONEMPRESA,  ");
			sql.append(" COL.COMUNITARIO, COL.NCOLEGIADO, ");
			sql.append(" TO_CHAR(COL.FECHAJURA, 'dd-mm-yyyy') FECHAJURA, COL.NCOMUNITARIO, TO_CHAR(COL.FECHATITULACION, 'dd-mm-yyyy') FECHATITULACION, ");
			sql.append(" COL.OTROSCOLEGIOS, TO_CHAR(COL.FECHADEONTOLOGIA, 'dd-mm-yyyy') FECHADEONTOLOGIA, TO_CHAR(COL.FECHAMOVIMIENTO, 'dd-mm-yyyy') FECHAMOVIMIENTO, ");
			sql.append(" COL.IDTIPOSSEGURO, COL.CUENTACONTABLESJCS, ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(SEG.NOMBRE, :");
			sql.append(keyContador);
			sql.append(") DESC_TIPOSEGURO ");
			
			
		//	sql.append(" F_SIGA_GETTIPOCLIENTE(COL.IDPERSONA, COL.IDINSTITUCION, SYSDATE) ESTADO_COLEGIAL, ");
		//	sql.append(" F_SIGA_GETDIRECCION(COL.IDINSTITUCION, COL.IDPERSONA, 2) DIRECION_PREFERENTE ");

			sql.append(" FROM CEN_COLEGIADO          COL, ");
			sql.append(" CEN_CLIENTE            CLI, ");
			sql.append(" CEN_PERSONA            PER, ");
			sql.append(" CEN_ESTADOCIVIL        EC, ");
			sql.append(" CEN_TIPOIDENTIFICACION TI, ");
			sql.append(" CEN_TRATAMIENTO        TRA, ");
			sql.append(" ADM_LENGUAJES          LEN, ");
			sql.append(" CEN_TIPOSSEGURO        SEG ");
			sql.append(" WHERE COL.IDINSTITUCION = CLI.IDINSTITUCION ");
			sql.append(" AND COL.IDPERSONA = CLI.IDPERSONA ");
			sql.append(" AND COL.IDPERSONA = PER.IDPERSONA ");
			sql.append(" AND CLI.IDPERSONA = PER.IDPERSONA ");
			sql.append(" AND PER.IDTIPOIDENTIFICACION = TI.IDTIPOIDENTIFICACION ");
			sql.append(" AND PER.IDESTADOCIVIL = EC.IDESTADOCIVIL(+) ");
			sql.append(" AND CLI.IDTRATAMIENTO = TRA.IDTRATAMIENTO ");
			sql.append(" AND CLI.IDLENGUAJE = LEN.IDLENGUAJE ");
			sql.append(" AND COL.IDTIPOSSEGURO = SEG.IDTIPOSSEGURO(+) ");
			
			 
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" AND COL.IDPERSONA = :");
			sql.append(keyContador);

			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND COL.IDINSTITUCION = :");
			sql.append(keyContador);


			if(isInforme){
				HelperInformesAdm helperInformes = new HelperInformesAdm();	
				datos = helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos); 
				
			}else{
				RowsContainer rc = new RowsContainer();
				
				if (rc.queryBind(sql.toString(), htCodigos)) {
					datos = rc.getAll();
					
				}
			}

				
			

		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getInformeColegiado.");
		}
		return datos;
	}
	private Vector getDatosInformeLetrado (String idInstitucion, String idPersona, String idioma,boolean isInforme
	) throws ClsExceptions  
	{
		Vector datos = null;
		try {
			Hashtable htCodigos = new Hashtable();
			int keyContador = 0;


			StringBuffer sql = new StringBuffer();
			sql.append(" SELECT PER.NOMBRE, PER.APELLIDOS1, PER.APELLIDOS2, ");
			sql.append(" PER.NIFCIF, PER.IDTIPOIDENTIFICACION, TO_CHAR(PER.FECHANACIMIENTO, 'dd-mm-yyyy') FECHANACIMIENTO, ");
			sql.append(" PER.IDESTADOCIVIL,PER.NATURALDE, PER.FALLECIDO, PER.SEXO, ");  

			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(EC.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_ESTADOCIVIL, ");
			
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(TI.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_TIPOIDENTIFICACION, ");
			

			sql.append(" TO_CHAR(CLI.FECHAALTA, 'dd-mm-yyyy') FECHAALTA, CLI.CARACTER,      CLI.PUBLICIDAD, ");
			sql.append(" CLI.GUIAJUDICIAL, CLI.ABONOSBANCO, CLI.CARGOSBANCO, ");
			sql.append(" CLI.COMISIONES, CLI.IDTRATAMIENTO, CLI.IDLENGUAJE, CLI.FOTOGRAFIA, ");
			sql.append(" CLI.ASIENTOCONTABLE, TO_CHAR(CLI.FECHACARGA, 'dd-mm-yyyy') FECHACARGA, CLI.LETRADO, TO_CHAR(CLI.FECHAACTUALIZACION, 'dd-mm-yyyy') FECHAACTUALIZACION, ");
			sql.append(" TO_CHAR(CLI.FECHAEXPORTCENSO, 'dd-mm-yyyy') FECHAEXPORTCENSO, CLI.NOENVIARREVISTA, CLI.NOAPARECERREDABOGACIA, ");

			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			sql.append(" F_SIGA_GETRECURSO(TRA.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_TRATAMIENTO, ");
			
			keyContador++;
			htCodigos.put(new Integer(keyContador), idioma);
			
			sql.append(" F_SIGA_GETRECURSO(LEN.DESCRIPCION, :");
			sql.append(keyContador);
			sql.append(") DESC_LENGUAJE ");
			
		//	sql.append(" F_SIGA_GETTIPOCLIENTE(COL.IDPERSONA, COL.IDINSTITUCION, SYSDATE) ESTADO_COLEGIAL, ");
		//	sql.append(" F_SIGA_GETDIRECCION(COL.IDINSTITUCION, COL.IDPERSONA, 2) DIRECION_PREFERENTE ");

			sql.append(" FROM CEN_CLIENTE            CLI, ");
			sql.append(" CEN_PERSONA            PER, ");
			sql.append(" CEN_ESTADOCIVIL        EC, ");
			sql.append(" CEN_TIPOIDENTIFICACION TI, ");
			sql.append(" CEN_TRATAMIENTO        TRA, ");
			sql.append(" ADM_LENGUAJES          LEN ");
			
			sql.append(" WHERE CLI.IDPERSONA = PER.IDPERSONA ");
			sql.append(" AND PER.IDTIPOIDENTIFICACION = TI.IDTIPOIDENTIFICACION ");
			sql.append(" AND PER.IDESTADOCIVIL = EC.IDESTADOCIVIL(+) ");
			sql.append(" AND CLI.IDTRATAMIENTO = TRA.IDTRATAMIENTO ");
			sql.append(" AND CLI.IDLENGUAJE = LEN.IDLENGUAJE ");
		
			
			 
			keyContador++;
			htCodigos.put(new Integer(keyContador), idPersona);
			sql.append(" AND CLI.IDPERSONA = :");
			sql.append(keyContador);

			keyContador++;
			htCodigos.put(new Integer(keyContador), idInstitucion);
			sql.append(" AND CLI.IDINSTITUCION = :");
			sql.append(keyContador);


			if(isInforme){
				HelperInformesAdm helperInformes = new HelperInformesAdm();	
				datos = helperInformes.ejecutaConsultaBind(sql.toString(), htCodigos); 
				
			}else{
				RowsContainer rc = new RowsContainer();
				
				if (rc.queryBind(sql.toString(), htCodigos)) {
					datos = rc.getAll();
					
				}
			}

		}
		catch (Exception e) {
			throw new ClsExceptions (e, "Error CenColegiadoAdm.getDatosInformeLetrado.");
		}
		return datos;
	}
	public Vector getInformeLetrado (String idInstitucion, String idPersona,String idioma,boolean isInforme)throws ClsExceptions {
		Vector vInforme = null;
		HelperInformesAdm helperInformes = new HelperInformesAdm();
		CenEstadoColegialAdm admEstadoCol = null;
		CenDireccionesAdm admDirecciones = null;
		CenCuentasBancariasAdm admCuentasBancarias = new CenCuentasBancariasAdm(usrbean);
		try {
			
			vInforme = getDatosInformeLetrado(idInstitucion, idPersona, idioma, isInforme);
			Hashtable registro = null;
			if(isInforme){
				registro = (Hashtable) vInforme.get(0);	
			}else{
				registro = ((Row) vInforme.get(0)).getRow();
			}
			

			//Aniadimos los datos del colegiado del ejg
			admEstadoCol = new CenEstadoColegialAdm(usrbean);
			//Añadimos el estado Colegial
			helperInformes.completarHashSalida(registro,admEstadoCol.getEstadoColegial(idInstitucion, idPersona, idioma));
			//Añadimos los campos de la direccion preferente para el tipo de envio
			//AQUI TENGO DUDAS, NO SE SI ES ESTO LO QUE PIDEN. EN TAL CASO ME TENDRE QUE TRAER EL IDTIPOENVIO.
			//AHORA SE LO METO A PELO
			admDirecciones = new CenDireccionesAdm(usrbean);
			helperInformes.completarHashSalida(registro,admDirecciones.getDireccionPreferente(idInstitucion, idPersona, "1"));
			
			helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteAbono(idInstitucion, idPersona));
			helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteCargo(idInstitucion, idPersona));
			helperInformes.completarHashSalida(registro,admCuentasBancarias.getCuentaCorrienteSJCS(idInstitucion, idPersona));
			
		}catch (Exception e) {
			throw new ClsExceptions (e, "Error ScsEJGAdm.getInformeLetrado.");
		}
	return vInforme;
					
	}
	
	/** 
	 * Obtiene los datos colegiales de una persona dependiendo de la institucion
	 * @param  idPersona - identificador de la persona
	 * @param  idInstitucion - identificador de la institucion	
	 * @return  CenColegiadoBean con los datos colegiales  
	 * @exception  ClsExceptions  En cualquier caso de error
	 */		
	public Vector getColegiaciones (String idPersona) throws ClsExceptions, SIGAException{
		Vector colegiaciones = new Vector();
		try {
			Hashtable hash = new Hashtable();
			UtilidadesHash.set(hash, CenColegiadoBean.C_IDPERSONA, idPersona);
			Vector v = this.select(hash);
			if ((v != null) && (v.size()>0)) {
				for (int i = 0; i < v.size(); i++) {
					colegiaciones.add(((CenColegiadoBean)v.get(i)).getIdInstitucion());
				}
			}
		}catch (Exception e) {
			throw new ClsExceptions (e, "Error al recuperar los datos");
		}
		return colegiaciones;
	}
	
	public int getNumeroColegiaciones (Long idPersona)throws ClsExceptions{
		RowsContainer rc = null;
		int resultado = 0;
		
		try { 
			rc = new RowsContainer(); 
			String sql = "";
			Hashtable codigos = new Hashtable();
            codigos.put(new Integer(1),idPersona);
            sql = " SELECT COUNT(C." + CenColegiadoBean.C_IDPERSONA	+ ") as COLEGIACIONES" +
				  " FROM " + CenColegiadoBean.T_NOMBRETABLA + " C , " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + " DC " +
				  " WHERE C." + CenColegiadoBean.C_IDPERSONA + " = :1 " +
            	  " AND C.IDINSTITUCION = DC.IDINSTITUCION "+
            	  " AND C.IDPERSONA = DC.IDPERSONA "+
				  " AND dc.fechaestado = "+
            	  " 	(select max(DC2.fechaestado) "+
				  "       from CEN_DATOSCOLEGIALESESTADO DC2 "+
				  "       where DC2.idinstitucion = c.idinstitucion "+
				  "          and DC2.idpersona = c.idpersona "+
				  "          and trunc(DC2.fechaestado) < trunc(sysdate))";
            	  			
			if (rc.findBind(sql,codigos)) {
				if (rc.size() >= 1) {
					Row fila = (Row) rc.get(0);					
					Hashtable prueba = fila.getRow();
	            	resultado = Integer.parseInt((String)prueba.get("COLEGIACIONES"));
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions (e, "Error al recueperar los datos");
		}
		
		return resultado;
	}
}
