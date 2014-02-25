package com.siga.beans;


import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;


/**
 * Implementa las operaciones de base de datos sobre la tabla SCS_ESTADOEJG
 * @since 03-02-2005
 * @version adrianag - 11-09-2008
 */
public class ScsEstadoEJGAdm extends MasterBeanAdministrador
{
	//////////////////// CONSTRUCTOR ////////////////////
	public ScsEstadoEJGAdm (UsrBean usuario)
	{
		super (ScsEstadoEJGBean.T_NOMBRETABLA, usuario);
	}
	
	
	//////////////////// METODOS DE ADMINISTRADOR ////////////////////
	protected String[] getCamposBean ()
	{
		String[] campos =
		{
				ScsEstadoEJGBean.C_IDINSTITUCION,
				ScsEstadoEJGBean.C_IDTIPOEJG,
				ScsEstadoEJGBean.C_ANIO,
				ScsEstadoEJGBean.C_NUMERO,
				ScsEstadoEJGBean.C_IDESTADOPOREJG,
				ScsEstadoEJGBean.C_IDESTADOEJG,
				ScsEstadoEJGBean.C_FECHAINICIO,
				ScsEstadoEJGBean.C_FECHAMODIFICACION,
				ScsEstadoEJGBean.C_USUMODIFICACION,
				ScsEstadoEJGBean.C_OBSERVACIONES,
				ScsEstadoEJGBean.C_AUTOMATICO,
				ScsEstadoEJGBean.C_PROPIETARIOCOMISION
		};
		
		return campos;
	} //getCamposBean ()
	
	protected String[] getClavesBean ()
	{
		String[] campos =
		{
				ScsEstadoEJGBean.C_IDINSTITUCION,
				ScsEstadoEJGBean.C_IDTIPOEJG,
				ScsEstadoEJGBean.C_ANIO,
				ScsEstadoEJGBean.C_NUMERO,
				ScsEstadoEJGBean.C_IDESTADOPOREJG
		};
		
		return campos;
	} //getClavesBean ()
	
	public MasterBean hashTableToBean (Hashtable hash)
			throws ClsExceptions
	{
		ScsEstadoEJGBean bean = null;
		
		try {
			bean = new ScsEstadoEJGBean ();
			
			bean.setIdInstitucion (UtilidadesHash.getInteger (hash, ScsEstadoEJGBean.C_IDINSTITUCION));			
			bean.setIdTipoEJG (UtilidadesHash.getInteger (hash, ScsEstadoEJGBean.C_IDTIPOEJG));			
			bean.setAnio (UtilidadesHash.getInteger (hash, ScsEstadoEJGBean.C_ANIO));
			bean.setNumero (UtilidadesHash.getInteger (hash, ScsEstadoEJGBean.C_NUMERO));
			bean.setIdEstadoPorEJG (UtilidadesHash.getInteger (hash, ScsEstadoEJGBean.C_IDESTADOPOREJG));
			bean.setIdEstadoEJG (UtilidadesHash.getInteger (hash, ScsEstadoEJGBean.C_IDESTADOEJG));
			bean.setFechaInicio (UtilidadesHash.getString (hash, ScsEstadoEJGBean.C_FECHAINICIO));
			bean.setFechaMod (UtilidadesHash.getString (hash, ScsEstadoEJGBean.C_FECHAMODIFICACION));
			bean.setObservaciones(UtilidadesHash.getString(hash, ScsEstadoEJGBean.C_OBSERVACIONES));
			bean.setAutomatico(UtilidadesHash.getString(hash, ScsEstadoEJGBean.C_AUTOMATICO));
			bean.setPropietarioComisino(UtilidadesHash.getString(hash, ScsEstadoEJGBean.C_PROPIETARIOCOMISION));
		}
		catch (Exception e) {
			 throw new ClsExceptions (e,
					 "EXCEPCION EN TRANSFORMAR HASHTABLE A BEAN");
		}
		
		return bean;
	} //hashTableToBean ()
	
	public Hashtable beanToHashTable (MasterBean bean)
			throws ClsExceptions
	{
		Hashtable htData = null;
		
		try {
			htData = new Hashtable ();
			ScsEstadoEJGBean b = (ScsEstadoEJGBean) bean;
			
			htData.put (ScsEstadoEJGBean.C_IDINSTITUCION,		b.getIdInstitucion ());
			htData.put (ScsEstadoEJGBean.C_IDTIPOEJG,			b.getIdTipoEJG ());
			htData.put (ScsEstadoEJGBean.C_ANIO,				b.getAnio ());			
			htData.put (ScsEstadoEJGBean.C_NUMERO,				b.getNumero ());
			htData.put (ScsEstadoEJGBean.C_IDESTADOPOREJG,		b.getIdEstadoPorEJG ());
			htData.put (ScsEstadoEJGBean.C_IDESTADOEJG,			b.getIdEstadoEJG ());
			htData.put (ScsEstadoEJGBean.C_FECHAINICIO,			b.getFechaInicio ());
			htData.put (ScsEstadoEJGBean.C_FECHAMODIFICACION,	b.getFechaMod ());
			htData.put (ScsEstadoEJGBean.C_USUMODIFICACION,		b.getUsuMod ());
			htData.put (ScsEstadoEJGBean.C_OBSERVACIONES,		b.getObservaciones ());
			htData.put (ScsEstadoEJGBean.C_AUTOMATICO,		    b.getAutomatico());
			htData.put (ScsEstadoEJGBean.C_PROPIETARIOCOMISION,	b.getPropietarioComision());
		}
		catch (Exception e){
			 throw new ClsExceptions (e,
					 "EXCEPCION EN TRANSFORMAR EL BEAN A HASHTABLE");
		}
		
		return htData;
	} //beanToHashTable ()

	/**
	 * Obtiene el tipo de ordenación con el que se desea obtener la seleccion 
	 * 
	 * @return Vector de Strings con los campos
	 * con los que se desea realizar la ordenaciOn
	 */
	protected String[] getOrdenCampos ()
	{
		String[] campos =
		{
				ScsEstadoEJGBean.C_FECHAINICIO ,
				ScsEstadoEJGBean.C_IDESTADOPOREJG
		};
		
		return campos;
	} //getOrdenCampos ()
	
	
	//////////////////// METODOS DE BD ////////////////////
	/**
	 * Prepara los datos, para posteriormente insertarlos en la base de datos.
	 * Esta preparacion consiste en transformar el formato de la fecha
	 * 
	 * @param entrada Hashtable con los campos a insertar
	 * @return Hashtable con los campos adaptados
	 */
	public Hashtable prepararInsert (Hashtable entrada)
			throws ClsExceptions 
	{
		RowsContainer rc = null;
		String estadoPorEJG="";
		try {
			//Ponemos la fecha en el formato de la base de datos
			entrada.put (ScsEstadoEJGBean.C_FECHAINICIO,GstDate.getApplicationFormatDate ("",entrada.get (ScsEstadoEJGBean.C_FECHAINICIO).toString ()));
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="SELECT (MAX("+ ScsEstadoEJGBean.C_IDESTADOPOREJG + ") + 1) AS ESTADOPOREJG FROM " + nombreTabla+
			            " WHERE "+ScsEstadoEJGBean.C_IDINSTITUCION+"="+entrada.get("IDINSTITUCION")+
						"  AND "+ScsEstadoEJGBean.C_ANIO+"="+entrada.get("ANIO")+
						"  AND " +ScsEstadoEJGBean.C_NUMERO+"="+entrada.get("NUMERO")+
						"  AND " +ScsEstadoEJGBean.C_IDTIPOEJG+"="+entrada.get("IDTIPOEJG");
						
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				Hashtable prueba = fila.getRow();			
				if (prueba.get("ESTADOPOREJG").equals("")) {
					estadoPorEJG = "1";
				}
				else estadoPorEJG = prueba.get("ESTADOPOREJG").toString();	
				entrada.put(ScsEstadoEJGBean.C_IDESTADOPOREJG,estadoPorEJG);
			}		
			
			
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "EXCEPCION EN PREPARAR INSERCIÓN.");
		}
		
		return entrada;
	} //prepararInsert ()
	public Hashtable getEstadoAnterior (Hashtable entrada)throws ClsExceptions {
		RowsContainer rc = null;
		int estadoPorEJG=1;
		try {
			
			//Ponemos la fecha en el formato de la base de datos
			rc = new RowsContainer();		
			// Se prepara la sentencia SQL para hacer el select
			String sql ="select idestadoejg, estadoejg.idestadoporejg"+
						" from Scs_Estadoejg estadoejg"+
						" where idinstitucion = "+entrada.get(CajgEJGRemesaBean.C_IDINSTITUCION)+""+
					   " and numero = "+entrada.get(CajgEJGRemesaBean.C_NUMERO)+""+
					   " and anio = "+entrada.get(CajgEJGRemesaBean.C_ANIO)+""+
					   " and idtipoejg = "+entrada.get(CajgEJGRemesaBean.C_IDTIPOEJG)+""+  
					   " and ( estadoejg.idestadoporejg)="+ 
					                               "(SELECT max(ultimoestado.idestadoporejg)"+
					                                  " from SCS_ESTADOEJG ultimoestado"+
					                                 " where ultimoestado.IDINSTITUCION ="+
					                                       " estadoejg.IDINSTITUCION"+
					                                   " and ultimoestado.IDTIPOEJG ="+
					                                      " estadoejg.IDTIPOEJG"+
					                                   " and ultimoestado.ANIO = estadoejg.ANIO"+
					                                   " and ultimoestado.NUMERO = estadoejg.NUMERO)";
						
			if (rc.query(sql)) {
				Row fila = (Row) rc.get(0);
				return fila.getRow();			
//				estadoPorEJG = Integer.parseInt(prueba.get(ScsEstadoEJGBean.C_IDESTADOEJG).toString());	
				
			}		
			
			
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "EXCEPCION EN OBTENER ULTIMO ESTADO.");
		}

		return null;
	} 
	
	public Hashtable prepararUpdate (Hashtable entrada)	throws ClsExceptions{
		RowsContainer rc = null;
		String estadoPorEJG="";
		try {
	//	Ponemos la fecha en el formato de la base de datos
			entrada.put (ScsEstadoEJGBean.C_FECHAINICIO,
					GstDate.getApplicationFormatDate ("",
							entrada.get (ScsEstadoEJGBean.C_FECHAINICIO).toString ()));
			
	
	
	
		}
		catch (ClsExceptions e) {
			throw e;			
		}
		catch (Exception e) {
			throw new ClsExceptions(e, "EXCEPCION EN PREPARAR INSERCIÓN.");
		}

		return entrada;
	} //prepararInsert ()
	
	/** 
	 * Llama a la funcion update () generica
	 * que actualiza en BD a partir del Hash dado
	 * 
	 * @param hasTable con parejas campo-valor para realizar where en update 
	 * @return true -> OK false -> Error 
	 */
	public boolean update (Hashtable hash)
			throws ClsExceptions
	{
		try {
			return this.update (hash, null);
		}
		catch (Exception e)	{
			throw new ClsExceptions (e,
					"Error al ejecutar el 'update' en B.D. " + e.getMessage());
		}
	} //update ()
	
	/** 
	 * Ejecuta la consulta de seleccion 
	 * 
	 * @param String consulta: la consulta que hay que ejecutar
	 * @return Vector con los registros encontrados 
	 * */
	public Vector selectGenerico (String consulta)
			throws ClsExceptions 
	{
		Vector datos = new Vector ();
		
		//Acceso a BBDD
		RowsContainer rc = null;
		try {
			rc = new RowsContainer ();			
			
			if (rc.query (consulta)) {
				for (int i=0; i<rc.size (); i++) {
					Row fila = (Row) rc.get (i);
					Hashtable registro = (Hashtable) fila.getRow ();
					if (registro != null)
						datos.add (registro);
				}
			}
		}
		catch (Exception e) { 	
			throw new ClsExceptions (e,
					"Error al ejecutar el 'select' en B.D."); 
		}
		
		return datos;
	} //selectGenerico ()
	
	public ScsMaestroEstadosEJGBean getEstadoEjg (Short idEstadoJG,String codIdioma)
			throws ClsExceptions 
	{		
		ScsMaestroEstadosEJGBean maestroEstadosEJGBean = null;
		
		//Acceso a BBDD
		RowsContainer rc = null;
		try {
			rc = new RowsContainer ();			
			StringBuffer s = new StringBuffer();
			s.append(" SELECT IDESTADOEJG, F_SIGA_GETRECURSO(DESCRIPCION, ");
			s.append(codIdioma);
			s.append(" ) DESCRIPCION, FECHAMODIFICACION, USUMODIFICACION, CODIGOEXT, BLOQUEADO, ");
			s.append(" ORDEN, VISIBLECOMISION, CODIGOEJIS FROM SCS_MAESTROESTADOSEJG WHERE IDESTADOEJG = ");
			s.append(idEstadoJG);
			if (rc.query (s.toString())) {
				for (int i=0; i<rc.size (); i++) {
					Row fila = (Row) rc.get (i);
					Hashtable registro = (Hashtable) fila.getRow ();
					
					if (registro != null){
						maestroEstadosEJGBean = new ScsMaestroEstadosEJGBean();
						maestroEstadosEJGBean.setBloqueado(UtilidadesHash.getString(registro, ScsMaestroEstadosEJGBean.C_BLOQUEADO));
						maestroEstadosEJGBean.setCodigoejis(UtilidadesHash.getString(registro, ScsMaestroEstadosEJGBean.C_CODIGOEJIS));
						maestroEstadosEJGBean.setCodigoExt(UtilidadesHash.getString(registro, ScsMaestroEstadosEJGBean.C_CODIGOEXT));
						maestroEstadosEJGBean.setDescripcion(UtilidadesHash.getString(registro, ScsMaestroEstadosEJGBean.C_DESCRIPCION));
						maestroEstadosEJGBean.setIdEstadoEJG(UtilidadesHash.getInteger(registro, ScsMaestroEstadosEJGBean.C_IDESTADOEJG));
						maestroEstadosEJGBean.setOrden(UtilidadesHash.getShort(registro, ScsMaestroEstadosEJGBean.C_ORDEN));
						maestroEstadosEJGBean.setVisiblecomision(UtilidadesHash.getString(registro, ScsMaestroEstadosEJGBean.C_VISIBLECOMISION));
					}
				}
			}
		}
		catch (Exception e) { 	
			throw new ClsExceptions (e,
					"Error al ejecutar el 'select' en B.D."); 
		}
		
		return maestroEstadosEJGBean;
	} //selectGenerico ()
	
	
	public Vector getEstadosEjg(ScsEJGBean ejg) throws ClsExceptions, SIGAException{
		
		Vector v = new Vector();
		Hashtable claves = new Hashtable();
		
		claves.put (new Integer (1), ejg.getIdInstitucion());
		claves.put (new Integer (2), ejg.getIdTipoEJG());
		claves.put (new Integer (3), ejg.getAnio());
		claves.put (new Integer (4), ejg.getNumero());
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT estado.*, estadoejg.visiblecomision ");
		query.append("  FROM SCS_ESTADOEJG estado, SCS_MAESTROESTADOSEJG estadoejg ");
		query.append(" WHERE estado.IDESTADOEJG = estadoejg.IDESTADOEJG ");
		query.append("   AND estado.IDINSTITUCION =:1");
		query.append("   AND estado.IDTIPOEJG =:2");
		query.append("   AND estado.ANIO =:3");
		query.append("   AND estado.NUMERO =:4");
		query.append(" ORDER BY ESTADO.FECHAINICIO asc, ESTADO.IDESTADOPOREJG asc");
		v = this.selectGenericoBind(query.toString(), claves);
		
		return v;
	}
	
}
