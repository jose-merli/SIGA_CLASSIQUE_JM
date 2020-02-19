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
	 * Obtiene el bean de ejecucion para crear a partir de una consulta y un mensaje
	 * 
	 * @param consultaBean
	 * @param mensaje
	 * @return
	 * @throws ClsExceptions
	 */
	protected ConEjecucionBean getBeanFromConsulta(ConConsultaBean consultaBean, String mensaje) throws ClsExceptions {
		
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
			bean.setMensaje(mensaje);
			bean.setSentencia(consultaBean.getSentencia());
		
		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions (e, "Error al construir el bean a partir del bean de la Consulta");
		}

		return bean;

	}
	
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
	
	public void registrarInicioEjecucion(ConConsultaBean consultaBean, String mensaje) throws ClsExceptions, SIGAException {
		// guardando en BD
		ConEjecucionBean ejecucionBean = getBeanFromConsulta(consultaBean, mensaje);
		this.insert(ejecucionBean);
		
		// guardando para registro de fin posterior
		Hashtable htEjecucion = new Hashtable();
		htEjecucion.put(ConEjecucionBean.C_IDEJECUCION, ejecucionBean.getIdEjecucion());
		Vector v = this.selectByPK(htEjecucion);
		if (v!= null && v.size()!=0) {
			ejecucionBean_iniciada = (ConEjecucionBean) v.elementAt(0);
			fechaEjecucion_iniciada = new Date();
		}
	}

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
