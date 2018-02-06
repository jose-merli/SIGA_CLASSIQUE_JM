package com.siga.beans;

import java.util.Hashtable;
import java.util.Vector;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.SIGAException;

public class CerIncompatibilidadesAdm extends MasterBeanAdministrador
{
	public CerIncompatibilidadesAdm(UsrBean usuario)
	{
		super(CerIncompatibilidadesBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = 
			{ 
				CerIncompatibilidadesBean.C_IDINSTITUCION, 
				CerIncompatibilidadesBean.C_IDTIPOPRODUCTO, 
				CerIncompatibilidadesBean.C_IDPRODUCTO,
				CerIncompatibilidadesBean.C_IDPRODUCTOINSTITUCION, 
				CerIncompatibilidadesBean.C_IDTIPOPROD_INCOMPATIBLE,
				CerIncompatibilidadesBean.C_IDPROD_INCOMPATIBLE, 
				CerIncompatibilidadesBean.C_IDPRODINST_INCOMPATIBLE, 
				CerIncompatibilidadesBean.C_MOTIVO,
				CerIncompatibilidadesBean.C_FECHAMODIFICACION, 
				CerIncompatibilidadesBean.C_USUMODIFICACION 
			};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = 
			{ 
				CerIncompatibilidadesBean.C_IDINSTITUCION, 
				CerIncompatibilidadesBean.C_IDTIPOPRODUCTO, 
				CerIncompatibilidadesBean.C_IDPRODUCTO,
				CerIncompatibilidadesBean.C_IDPRODUCTOINSTITUCION, 
				CerIncompatibilidadesBean.C_IDTIPOPROD_INCOMPATIBLE,
				CerIncompatibilidadesBean.C_IDPROD_INCOMPATIBLE, 
				CerIncompatibilidadesBean.C_IDPRODINST_INCOMPATIBLE
			};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
		CerIncompatibilidadesBean bean = null;

		try {
			bean = new CerIncompatibilidadesBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CerIncompatibilidadesBean.C_IDINSTITUCION));
			bean.setIdTipoProducto(UtilidadesHash.getInteger(hash, CerIncompatibilidadesBean.C_IDTIPOPRODUCTO));
			bean.setIdProducto(UtilidadesHash.getInteger(hash, CerIncompatibilidadesBean.C_IDPRODUCTO));
			bean.setIdProductoInstitucion(UtilidadesHash.getInteger(hash, CerIncompatibilidadesBean.C_IDPRODUCTOINSTITUCION));
			bean.setIdTipoProd_Incompatible(UtilidadesHash.getInteger(hash, CerIncompatibilidadesBean.C_IDTIPOPROD_INCOMPATIBLE));
			bean.setIdProd_Incompatible(UtilidadesHash.getInteger(hash, CerIncompatibilidadesBean.C_IDPROD_INCOMPATIBLE));
			bean.setIdProdInst_Incompatible(UtilidadesHash.getInteger(hash, CerIncompatibilidadesBean.C_IDPRODINST_INCOMPATIBLE));
			bean.setMotivo(UtilidadesHash.getString(hash, CerIncompatibilidadesBean.C_MOTIVO));

		} catch (Exception e) {
			bean = null;
			throw new ClsExceptions(e, "Error al construir el bean a partir del hashTable");
		}

		return bean;
	}

	protected Hashtable beanToHashTable(MasterBean bean) throws ClsExceptions
	{
		Hashtable htData = null;

		try {
			htData = new Hashtable();

			CerIncompatibilidadesBean b = (CerIncompatibilidadesBean) bean;

			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_IDTIPOPRODUCTO, b.getIdTipoProducto());
			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_IDPRODUCTO, b.getIdProducto());
			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_IDPRODUCTOINSTITUCION, b.getIdProductoInstitucion());
			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_IDTIPOPROD_INCOMPATIBLE, b.getIdTipoProd_Incompatible());
			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_IDPROD_INCOMPATIBLE, b.getIdProd_Incompatible());
			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_IDPRODINST_INCOMPATIBLE, b.getIdProdInst_Incompatible());
			UtilidadesHash.set(htData, CerIncompatibilidadesBean.C_MOTIVO, b.getMotivo());
		
		} catch (Exception e) {
			htData = null;
			throw new ClsExceptions(e, "Error al crear el hashTable a partir del bean");
		}

		return htData;
	}

	protected String[] getOrdenCampos()
	{
		return null;
	}

	/**
	 * Obtiene todos los tipos de certificados que son incompatibles con el certificado pasado como parametro
	 * 
	 * @param idInstitucion
	 * @param idTipoProducto
	 * @param idProducto
	 * @param idProductoInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector obtenerListaIncompatibilidades(String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion) throws ClsExceptions
	{
		Vector<Hashtable<String, String>> datos = new Vector<Hashtable<String, String>>();
		
		RowsContainer rc = null;
		
		try
		{
			rc = new RowsContainer();
			
			StringBuilder sql = new StringBuilder();
			sql.append("Select Pro.");
			sql.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
			sql.append(", Pro.");
			sql.append(PysProductosInstitucionBean.C_IDPRODUCTO);
			sql.append(", Pro.");
			sql.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
			sql.append(", Pro.");
			sql.append(PysProductosInstitucionBean.C_DESCRIPCION);
			sql.append(", Inc.");
			sql.append(CerIncompatibilidadesBean.C_MOTIVO);
			sql.append("  From ");
			sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(" Pro, ");
			sql.append(CerIncompatibilidadesBean.T_NOMBRETABLA);
			sql.append(" Inc ");
			sql.append(" Where Pro.");
			sql.append(PysProductosInstitucionBean.C_IDINSTITUCION);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDINSTITUCION);
			sql.append("   And Pro.");
			sql.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDTIPOPROD_INCOMPATIBLE);
			sql.append("   And Pro.");
			sql.append(PysProductosInstitucionBean.C_IDPRODUCTO);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPROD_INCOMPATIBLE);
			sql.append("   And Pro.");
			sql.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPRODINST_INCOMPATIBLE);
			
			sql.append("   And ");
			sql.append(idInstitucion);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDINSTITUCION);
			sql.append("   And ");
			sql.append(idTipoProducto);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDTIPOPRODUCTO);
			sql.append("   And ");
			sql.append(idProducto);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPRODUCTO);
			sql.append("   And ");
			sql.append(idProductoInstitucion);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPRODUCTOINSTITUCION);

			if (rc.query(sql.toString())) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
				    Hashtable<String, String> ht = new Hashtable<String, String>();
				    
				    ht.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, fila.getString(PysProductosInstitucionBean.C_IDTIPOPRODUCTO));
				    ht.put(PysProductosInstitucionBean.C_IDPRODUCTO, fila.getString(PysProductosInstitucionBean.C_IDPRODUCTO));
				    ht.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, fila.getString(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION));
				    ht.put(PysProductosInstitucionBean.C_DESCRIPCION, fila.getString(PysProductosInstitucionBean.C_DESCRIPCION));
				    ht.put(CerIncompatibilidadesBean.C_MOTIVO, fila.getString(CerIncompatibilidadesBean.C_MOTIVO));

				    datos.add(ht);
				}
			}
		}
		
		catch(Exception e)
		{
			throw new ClsExceptions (e, "Error al ejecutar el 'select' en B.D."); 
		}
		
		return datos;
	}

	/**
	 * Dada una solicitud de certificado (idinstitucion, idsolicitud), busca si existen certificados de tipo incompatible para la persona del mismo.
	 * Las solicitudes tienen que estar como minimo aprobadas para que 'existan'.
	 * @param idInstitucion
	 * @param idSolicitud
	 * @return
	 * @throws ClsExceptions
	 */
	public boolean esIncompatible(Integer idInstitucion, Long idSolicitud) throws ClsExceptions
	{
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("Select * ");
			sql.append("  From ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(" Solnuevo, ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(" Otrosol, ");
			sql.append(CerIncompatibilidadesBean.T_NOMBRETABLA);
			sql.append(" Inc ");

			sql.append(" Where Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append("     = ");
			sql.append(idInstitucion);
			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_IDSOLICITUD);
			sql.append("     = ");
			sql.append(idSolicitud);
			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append("     = Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_IDPERSONA_DES);
			sql.append("     = Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_IDPERSONA_DES);
			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_IDSOLICITUD);
			sql.append("     <> Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_IDSOLICITUD);

			sql.append("   And Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDINSTITUCION);
			sql.append("   And Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDTIPOPROD_INCOMPATIBLE);
			sql.append("   And Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPROD_INCOMPATIBLE);
			sql.append("   And Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPRODINST_INCOMPATIBLE);
			sql.append("   and Otrosol.");
			sql.append(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO);
			sql.append(" in ( ");
			sql.append(CerEstadoSoliCertifiAdm.K_ESTADOS_SOL_DESDEAPROBADO);	
			sql.append(" ) ");

			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDINSTITUCION);
			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDTIPOPRODUCTO);
			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPRODUCTO);
			sql.append("   And Solnuevo.");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
			sql.append("     = Inc.");
			sql.append(CerIncompatibilidadesBean.C_IDPRODUCTOINSTITUCION);

			RowsContainer rc = new RowsContainer();
			if (rc.query(sql.toString())) {
				return (rc.size() > 0);
			} else {
				return false;
			}
			
		} catch (Exception e) {
			throw new ClsExceptions(e, "Error al ejecutar el 'select' en B.D.");
		}
	}

}