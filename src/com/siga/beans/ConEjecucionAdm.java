package com.siga.beans;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

/**
 * Administrador del Bean de Estadistica de Ejecucion de Consultas 
 */
public class ConEjecucionAdm extends MasterBeanAdministrador {
	
	// variable para identificar que ejecucion terminar
	private ConEjecucionBean ejecucionBean_iniciada = null;
	private Date fechaEjecucion_iniciada = null;
	
	public ConEjecucionAdm(UsrBean usuario)	{
	    super(ConEjecucionBean.T_NOMBRETABLA, usuario);
	}
	
	protected String[] getCamposBean() {
		
		String[] campos = {ConEjecucionBean.C_IDEJECUCION,
				ConEjecucionBean.C_IDINSTITUCION,
				ConEjecucionBean.C_IDUSUARIO,
				ConEjecucionBean.C_IDCONSULTA,
				ConEjecucionBean.C_IDINSTITUCION_CONSULTA,
				ConEjecucionBean.C_FECHAEJECUCION,
				ConEjecucionBean.C_TIEMPOEJECUCION,
				ConEjecucionBean.C_MENSAJE,
				ConEjecucionBean.C_SENTENCIA};
		
		return campos;
	}	

	protected String[] getClavesBean() {
		String[] claves = {ConEjecucionBean.C_IDEJECUCION};
		return claves;
	}

	protected String[] getOrdenCampos() {
		return null;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions {
		
		ConEjecucionBean bean = null;

		try
		{
			bean = new ConEjecucionBean();
						
			bean.setIdEjecucion(UtilidadesHash.getLong(hash, ConEjecucionBean.C_IDEJECUCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, ConEjecucionBean.C_IDINSTITUCION));
			bean.setIdUsuario(UtilidadesHash.getInteger(hash, ConEjecucionBean.C_IDUSUARIO));
			bean.setIdConsulta(UtilidadesHash.getLong(hash, ConEjecucionBean.C_IDCONSULTA));
			bean.setIdInstitucion_Consulta(UtilidadesHash.getInteger(hash, ConEjecucionBean.C_IDINSTITUCION_CONSULTA));
			bean.setFechaEjecucion(UtilidadesHash.getString(hash, ConEjecucionBean.C_FECHAEJECUCION));			
			bean.setTiempoEjecucion(UtilidadesHash.getLong(hash, ConEjecucionBean.C_TIEMPOEJECUCION));
			bean.setMensaje(UtilidadesHash.getString(hash, ConEjecucionBean.C_MENSAJE));
			bean.setSentencia(UtilidadesHash.getString(hash, ConEjecucionBean.C_SENTENCIA));
		
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions {
		
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			ConEjecucionBean b = (ConEjecucionBean) bean;

			UtilidadesHash.set(htData, ConEjecucionBean.C_IDEJECUCION, b.getIdEjecucion());
			UtilidadesHash.set(htData, ConEjecucionBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, ConEjecucionBean.C_IDUSUARIO, b.getIdUsuario());
			UtilidadesHash.set(htData, ConEjecucionBean.C_IDCONSULTA, b.getIdConsulta());
			UtilidadesHash.set(htData, ConEjecucionBean.C_IDINSTITUCION_CONSULTA, b.getIdInstitucion_Consulta());
			UtilidadesHash.set(htData, ConEjecucionBean.C_FECHAEJECUCION, b.getFechaEjecucion());
			UtilidadesHash.set(htData, ConEjecucionBean.C_TIEMPOEJECUCION, b.getTiempoEjecucion());
			UtilidadesHash.set(htData, ConEjecucionBean.C_MENSAJE, b.getMensaje());
			UtilidadesHash.set(htData, ConEjecucionBean.C_SENTENCIA, b.getSentencia());
			
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}
	
	/**
	 * Obtiene el bean de ejecucion para crear a partir de una consulta y una sentencia.
	 * Si la sentencia es nula, obtiene la sentencia de la consulta original.
	 * 
	 * @param consultaBean
	 * @param sentencia
	 * @return
	 * @throws ClsExceptions
	 */
	protected ConEjecucionBean getBeanFromConsulta(ConConsultaBean consultaBean, String sentencia) throws ClsExceptions {
		
		ConEjecucionBean bean = null;

		try
		{
			bean = new ConEjecucionBean();
						
			bean.setIdEjecucion(getNewIdEjecucion());
			bean.setIdInstitucion(Integer.valueOf(this.usrbean.getLocation()));
			bean.setIdUsuario(Integer.valueOf(this.usrbean.getUserName()));
			bean.setIdConsulta(consultaBean.getIdConsulta());
			bean.setIdInstitucion_Consulta(consultaBean.getIdInstitucion());
			bean.setFechaEjecucion("sysdate");
			if (sentencia != null && ! "".equalsIgnoreCase(sentencia)) {
				bean.setSentencia(sentencia);
			} else {
				bean.setSentencia(consultaBean.getSentencia());
			}
		
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del bean de la Consulta");
		}

		return bean;

	}
	
	/**
	 * Obtiene el siguiente valor para la secuencia de la PK de esta tabla
	 * 
	 * @return
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	private Long getNewIdEjecucion() throws ClsExceptions, SIGAException 
	{		
		long nuevoIdEjecucion = 1;
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 						
			
	        String sql = "SELECT SEQ_CON_EJECUCION_PK.Nextval AS NEWID FROM DUAL ";
						
			if (rc.find(sql)) {
				Hashtable htRow=((Row)rc.get(0)).getRow();
				if(!((String)htRow.get("NEWID")).equals("")) {
					nuevoIdEjecucion = Long.valueOf((String)htRow.get("NEWID"));
				}
			}
		} catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		if(nuevoIdEjecucion>=new Long("10000000000"))
			throw new SIGAException("messages.general.error.identificadorExcedido");
		return new Long (nuevoIdEjecucion);
	}
	
	/**
	 * Guarda el inicio de la ejecucion de una consulta con un mensaje. No añade la sentencia.
	 * 
	 * @param consultaBean
	 * @param mensaje
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void registrarInicioEjecucion(ConConsultaBean consultaBean, String mensaje) throws ClsExceptions, SIGAException {
		registrarInicioEjecucion(consultaBean, mensaje, null);
	}
	/**
	 * Guarda el inicio de la ejecucion de una consulta con un mensaje.
	 * Solo se puede registrar una vez. 
	 * 
	 * @param consultaBean
	 * @param mensaje
	 * @param sentencia
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void registrarInicioEjecucion(ConConsultaBean consultaBean, String mensaje, String sentencia) throws ClsExceptions, SIGAException {
		// comprobando que no hay un registro inicial
		if (ejecucionBean_iniciada != null) {
			throw new ClsExceptions ("Ya se ha iniciado ejecucion de consulta"); 
		}
		
		// guardando la hora de inicio para calcular el tiempo de ejecucion
		fechaEjecucion_iniciada = new Date();
		
		// guardando en BD
		ConEjecucionBean ejecucionBean = getBeanFromConsulta(consultaBean, sentencia);
		ejecucionBean.setMensaje(mensaje);
		this.insert(ejecucionBean);
		
		// guardando para registro de fin posterior
		Hashtable htEjecucion = new Hashtable();
		htEjecucion.put(ConEjecucionBean.C_IDEJECUCION, ejecucionBean.getIdEjecucion());
		Vector v = this.selectByPK(htEjecucion);
		if (v!= null && v.size()!=0) {
			ejecucionBean_iniciada = (ConEjecucionBean) v.elementAt(0);
		}
	}

	/**
	 * Adjunta la sentencia al registro de ejecucion insertado previamente.
	 * Si no se creo el registro de ejecucion previamente, dara error.
	 * 
	 * @param sentencia
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void registrarSentenciaEjecucion(String sentencia) throws ClsExceptions, SIGAException {
		// comprobando que hay un registro inicial
		if (ejecucionBean_iniciada == null) {
			throw new ClsExceptions ("No se ha iniciado ejecucion de consulta"); 
		}
		
		// guardando cambios en registro
		ejecucionBean_iniciada.setTiempoEjecucion(new Date().getTime() - fechaEjecucion_iniciada.getTime());
		if (sentencia != null && ! "".equalsIgnoreCase(sentencia)) {
			ejecucionBean_iniciada.setSentencia(sentencia);
		}
		this.update(ejecucionBean_iniciada);
		
		// guardando para registro de fin posterior
		Hashtable htEjecucion = new Hashtable();
		htEjecucion.put(ConEjecucionBean.C_IDEJECUCION, ejecucionBean_iniciada.getIdEjecucion());
		Vector v = this.selectByPK(htEjecucion);
		if (v!= null && v.size()!=0) {
			ejecucionBean_iniciada = (ConEjecucionBean) v.elementAt(0);
		}
	}

	/**
	 * Registra el tiempo de ejecucion junto a un mensaje en el registro de ejecucion insertado previamente.
	 * Si no se creo el registro de ejecucion previamente, dara error.
	 * Si el mensaje es nulo o vacio, no se anyade el mensaje.
	 * 
	 * @param mensaje
	 * @throws ClsExceptions
	 * @throws SIGAException
	 */
	public void registrarFinEjecucion(String mensaje) throws ClsExceptions, SIGAException {
		// comprobando que hay un registro inicial
		if (ejecucionBean_iniciada == null) {
			throw new ClsExceptions ("No se ha iniciado ejecucion de consulta"); 
		}
		
		// guardando cambios en registro
		ejecucionBean_iniciada.setTiempoEjecucion(new Date().getTime() - fechaEjecucion_iniciada.getTime());
		if (mensaje != null && ! "".equalsIgnoreCase(mensaje)) {
			ejecucionBean_iniciada.setMensaje(ejecucionBean_iniciada.getMensaje() + " - " + mensaje);
		}
		this.update(ejecucionBean_iniciada);
		
		// limpiando ejecucion
		ejecucionBean_iniciada = null;
		fechaEjecucion_iniciada = null;
	}
}
