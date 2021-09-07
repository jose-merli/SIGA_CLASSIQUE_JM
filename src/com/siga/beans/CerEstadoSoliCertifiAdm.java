package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;

import com.atos.utils.ClsExceptions;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesBDAdm;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;


public class CerEstadoSoliCertifiAdm extends MasterBeanAdministrador
{
	public static final int C_ESTADO_SOL_PEND								=1;
	public static final int C_ESTADO_SOL_APROBADO							=2;
	public static final int C_ESTADO_SOL_APROBANDO							=7;
	public static final int C_ESTADO_SOL_FINALIZANDO						=8;
	public static final int C_ESTADO_SOL_FACTURANDO							=9;
	public static final int C_ESTADO_SOL_GENERANDO_APROBADO					=11;
	public static final int C_ESTADO_SOL_GENERANDO_PENDIENTE_DE_FACTURAR	=12;
	public static final int C_ESTADO_SOL_GENERANDO_FINALIZADO				=13;
	public static final int C_ESTADO_SOL_ENVIOP								=3;
	public static final int C_ESTADO_SOL_FINALIZADO							=4;
	public static final int C_ESTADO_SOL_DENEGADO							=5;
	public static final int C_ESTADO_SOL_ANULADO							=6;
	public static final int C_ESTADO_SOL_PEND_FACTURAR						=10;
	
	public static final String K_ESTADOS_SOL_ACTIVO							= 	C_ESTADO_SOL_PEND + "," + 
																				C_ESTADO_SOL_APROBADO + "," + 
																				C_ESTADO_SOL_FINALIZADO + "," + 
																				C_ESTADO_SOL_PEND_FACTURAR;
	public static final String K_ESTADOS_SOL_DESDEAPROBADO 					= 	C_ESTADO_SOL_APROBADO + "," + 
																				C_ESTADO_SOL_FINALIZADO + "," + 
																				C_ESTADO_SOL_PEND_FACTURAR + "," + 
																				C_ESTADO_SOL_FINALIZANDO + "," + 
																				C_ESTADO_SOL_FACTURANDO + "," + 
																				C_ESTADO_SOL_GENERANDO_APROBADO + "," + 
																				C_ESTADO_SOL_GENERANDO_PENDIENTE_DE_FACTURAR + "," + 
																				C_ESTADO_SOL_GENERANDO_FINALIZADO;
	public static final String K_ESTADOS_SOL_ACCIONMASIVA 					= 	C_ESTADO_SOL_APROBANDO + "," + 
																				C_ESTADO_SOL_FINALIZANDO + "," + 
																				C_ESTADO_SOL_FACTURANDO + "," + 
																				C_ESTADO_SOL_GENERANDO_APROBADO + "," + 
																				C_ESTADO_SOL_GENERANDO_PENDIENTE_DE_FACTURAR + "," + 
																				C_ESTADO_SOL_GENERANDO_FINALIZADO;
	public static final String K_ESTADOS_SOL_INACTIVO 						= 	C_ESTADO_SOL_DENEGADO + "," + 
																				C_ESTADO_SOL_ANULADO;
	
	public CerEstadoSoliCertifiAdm(UsrBean usuario)
	{
	    super(CerEstadoSoliCertifiBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO,
		        		   CerEstadoSoliCertifiBean.C_DESCRIPCION};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO};

		return claves;
	}
	
	protected String[] getOrdenDescripcion()
	{
		String[] claves = {CerEstadoSoliCertifiBean.C_DESCRIPCION};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerEstadoSoliCertifiBean bean = null;

		try
		{
			bean = new CerEstadoSoliCertifiBean();

			bean.setIdEstadoSolicitudCertificado(UtilidadesHash.getInteger(hash, CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO));
			bean.setListaIdEstadoSolicitudCertificado(UtilidadesHash.getString(hash, CerEstadoSoliCertifiBean.C_LISTAIDESTADOSOLICITUDCERTIFICADO));
			bean.setDescripcion(UtilidadesHash.getString(hash, CerEstadoSoliCertifiBean.C_DESCRIPCION));
		}

		catch (Exception e)
		{
			bean = null;

			throw new ClsExceptions (e, "Error al construir el bean a partir del Hashtable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try
		{
			htData = new Hashtable();

			CerEstadoSoliCertifiBean b = (CerEstadoSoliCertifiBean) bean;

			UtilidadesHash.set(htData, CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO, b.getIdEstadoSolicitudCertificado());
			UtilidadesHash.set(htData, CerEstadoSoliCertifiBean.C_LISTAIDESTADOSOLICITUDCERTIFICADO, b.getListaIdEstadoSolicitudCertificado());
			UtilidadesHash.set(htData, CerEstadoSoliCertifiBean.C_DESCRIPCION, b.getDescripcion());
		}

		catch (Exception e)
		{
			htData = null;

			throw new ClsExceptions (e, "Error al crear el Hashtable a partir del bean");
		}

		return htData;
	}

    protected String[] getOrdenCampos()
    {
        return null;
    }
    
	public Vector select() throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" Select To_Char(Estsol.");
		sql.append(                 CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("               ) ");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , To_Char(Estsol.");
		sql.append(                 CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("               ) ");
		sql.append(         CerEstadoSoliCertifiBean.C_LISTAIDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , f_Siga_Getrecurso(Estsol.");
		sql.append(                           CerEstadoSoliCertifiBean.C_DESCRIPCION);
		sql.append("                          , ");
		sql.append(                           this.usrbean.getLanguage());
		sql.append("                         ) ");
		sql.append(         CerEstadoSoliCertifiBean.C_DESCRIPCION);
		sql.append("   From ");
		sql.append(         CerEstadoSoliCertifiBean.T_NOMBRETABLA);
		sql.append("        Estsol");
		sql.append("  Where Estsol.");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("        In (");
		sql.append(             CerEstadoSoliCertifiAdm.K_ESTADOS_SOL_ACTIVO);
		sql.append("           )");
		sql.append(" Union All");
		sql.append(" Select Null ");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , To_Char(LISTAGG(Estsol.");
		sql.append(                           CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("               , ',') within group (order by rownum)) ");
		sql.append(         CerEstadoSoliCertifiBean.C_LISTAIDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , f_siga_getrecurso_etiqueta('certificados.solicitudes.literal.estado.activo', ");
		sql.append(                                    this.usrbean.getLanguage());
		sql.append("                                  ) ");
		sql.append(         CerEstadoSoliCertifiBean.C_DESCRIPCION);
		sql.append("   From ");
		sql.append(         CerEstadoSoliCertifiBean.T_NOMBRETABLA);
		sql.append("        Estsol");
		sql.append("  Where Estsol.");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("        In (");
		sql.append(             CerEstadoSoliCertifiAdm.K_ESTADOS_SOL_ACTIVO);
		sql.append("           )");
		sql.append(" Union All");
		sql.append(" Select Null ");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , To_Char(LISTAGG(Estsol.");
		sql.append(                           CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("               , ',') within group (order by rownum)) ");
		sql.append(         CerEstadoSoliCertifiBean.C_LISTAIDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , f_siga_getrecurso_etiqueta('certificados.solicitudes.literal.estado.automatico', ");
		sql.append(                                    this.usrbean.getLanguage());
		sql.append("                                  ) ");
		sql.append(         CerEstadoSoliCertifiBean.C_DESCRIPCION);
		sql.append("   From ");
		sql.append(         CerEstadoSoliCertifiBean.T_NOMBRETABLA);
		sql.append("        Estsol");
		sql.append("  Where Estsol.");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("        In (");
		sql.append(             CerEstadoSoliCertifiAdm.K_ESTADOS_SOL_ACCIONMASIVA);
		sql.append("           )");
		sql.append(" Union All");
		sql.append(" Select Null ");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , To_Char(LISTAGG(Estsol.");
		sql.append(                           CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("               , ',') within group (order by rownum)) ");
		sql.append(         CerEstadoSoliCertifiBean.C_LISTAIDESTADOSOLICITUDCERTIFICADO);
		sql.append("      , '> ' || ");
		sql.append("        To_Char(LISTAGG(f_Siga_Getrecurso(Estsol.");
		sql.append(                                             CerEstadoSoliCertifiBean.C_DESCRIPCION);
		sql.append("                                            , ");
		sql.append(                                             this.usrbean.getLanguage());
		sql.append("               ), ',') within group (order by rownum)) ");
		sql.append(         CerEstadoSoliCertifiBean.C_DESCRIPCION);
		sql.append("   From ");
		sql.append(         CerEstadoSoliCertifiBean.T_NOMBRETABLA);
		sql.append("        Estsol");
		sql.append("  Where Estsol.");
		sql.append(         CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append("        In (");
		sql.append(             CerEstadoSoliCertifiAdm.K_ESTADOS_SOL_INACTIVO);
		sql.append("           )");

		try { 
			RowsContainer rc = new RowsContainer(); 
			if (rc.query(sql.toString())) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}

	public Vector selectGenericaBind(String where, Hashtable data) throws ClsExceptions 
	{
		Vector datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer(); 
			String sql = " SELECT " + UtilidadesMultidioma.getCampoMultidioma(CerEstadoSoliCertifiBean.C_DESCRIPCION, this.usrbean.getLanguage()) + ", " +
			  CerEstadoSoliCertifiBean.C_FECHAMODIFICACION + ", " +
			  CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO + ", " +
			  CerEstadoSoliCertifiBean.C_USUMODIFICACION + 						 			
			  " FROM " + CerEstadoSoliCertifiBean.T_NOMBRETABLA + " ";
			sql += " " + where;
			sql += this.getOrdenCampos()!=null ? UtilidadesBDAdm.sqlOrderBy(this.getOrdenCampos()) : UtilidadesBDAdm.sqlOrderBy(this.getClavesBean());
			if (rc.queryBind(sql,data)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					MasterBean registro = (MasterBean) this.hashTableToBeanInicial(fila.getRow()); 
					if (registro != null) 
						datos.add(registro);
				}
			}
		} 
		catch (Exception e) { 	
			throw new ClsExceptions (e, "Error al ejecutar el \"select\" en B.D."); 
		}
		return datos;
	}  
	
	public String getNombreEstadoSolicitudCert(String idEstado) throws ClsExceptions {
		String nombreEstado = "";

		// Acceso a BBDD
		RowsContainer rc = null;
		try {
			rc = new RowsContainer();
			String sql = " SELECT  " + UtilidadesMultidioma.getCampoMultidioma(CerEstadoSoliCertifiBean.C_DESCRIPCION, this.usrbean.getLanguage()) + " FROM  " + CerEstadoSoliCertifiBean.T_NOMBRETABLA + " ";
			sql += " WHERE " + CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO + " = " + idEstado;
			if (rc.query(sql)) {
				for (int i = 0; i < rc.size(); i++) {
					Row fila = (Row) rc.get(i);
					CerEstadoSoliCertifiBean registro = (CerEstadoSoliCertifiBean) this.hashTableToBeanInicial(fila.getRow());
					if (registro != null)
						nombreEstado = registro.getDescripcion();
				}
			}
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el \"select\" en B.D.");
		}
		return nombreEstado;
	}
}