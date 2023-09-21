package com.siga.beans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.transaction.UserTransaction;

import org.redabogacia.sigaservices.app.AppConstants;
import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.SIGALogging;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.general.EjecucionPLs;



/**
 * CenColaCambioLetradoAdm
 * 
 * @author Desconocido
 * @since 2007-03-19
 * @version 2008-05-30 adrianag: Limpieza general
 */
public class CenColaCambioLetradoAdm extends MasterBeanAdministrador
{
	//////////////////// CONSTRUCTORES ////////////////////
    public CenColaCambioLetradoAdm (UsrBean usu) {
		super (CenColaCambioLetradoBean.T_NOMBRETABLA, usu);
	}
	
    
    
	//////////////////// METODOS COMUNES DE ADMINISTRADOR ////////////////////
    protected String[] getCamposBean()
    {
		String [] campos = 
		{
				CenColaCambioLetradoBean.C_FECHACAMBIO,
				CenColaCambioLetradoBean.C_FECHAMODIFICACION, 
				CenColaCambioLetradoBean.C_IDCAMBIO,
				CenColaCambioLetradoBean.C_IDDIRECCION,
				CenColaCambioLetradoBean.C_IDINSTITUCION,
				CenColaCambioLetradoBean.C_IDPERSONA,
				CenColaCambioLetradoBean.C_IDTIPOCAMBIO,
				CenColaCambioLetradoBean.C_USUMODIFICACION
		};
		return campos;
    } //getCamposBean()
	
    protected String[] getClavesBean()
    {
		String [] campos = 
		{
				CenColaCambioLetradoBean.C_IDCAMBIO, 
    			CenColaCambioLetradoBean.C_IDINSTITUCION, 	
    			CenColaCambioLetradoBean.C_IDPERSONA
    	};
		return campos;
    } //getClavesBean()
	
    protected String[] getOrdenCampos()
    {
		String [] campos = 
		{
				CenColaCambioLetradoBean.C_FECHACAMBIO,
		        CenColaCambioLetradoBean.C_IDCAMBIO, 
				CenColaCambioLetradoBean.C_IDINSTITUCION, 	
				CenColaCambioLetradoBean.C_IDPERSONA
		};
        return campos;
    } //getOrdenCampos()

    protected MasterBean hashTableToBean(Hashtable hash)
    		throws ClsExceptions 
    {
		CenColaCambioLetradoBean bean = null;
		
		try {
			bean = new CenColaCambioLetradoBean();
			bean.setFechaCambio(UtilidadesHash.getString(hash, CenColaCambioLetradoBean.C_FECHACAMBIO));
			bean.setFechaMod(UtilidadesHash.getString(hash, CenColaCambioLetradoBean.C_FECHAMODIFICACION));
			bean.setIdCambio(UtilidadesHash.getLong(hash, CenColaCambioLetradoBean.C_IDCAMBIO));
			bean.setIdDireccion(UtilidadesHash.getLong(hash, CenColaCambioLetradoBean.C_IDDIRECCION));
			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CenColaCambioLetradoBean.C_IDINSTITUCION));
			bean.setIdPersona(UtilidadesHash.getLong(hash, CenColaCambioLetradoBean.C_IDPERSONA));
			bean.setIdTipoCambio(UtilidadesHash.getInteger(hash, CenColaCambioLetradoBean.C_IDTIPOCAMBIO));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CenColaCambioLetradoBean.C_USUMODIFICACION));
		}
		catch (Exception e) {
			ClsLogging.writeFileLog("ERROR - CenColaCambioLetradoAdm.hashTableToBean(): " + e.getMessage(), 0);
			bean = null;	
			throw new ClsExceptions (e, "Error al construir el bean a partir del hashTable");
		}
		
		return bean;    
	} //hashTableToBean()
	
    protected Hashtable beanToHashTable(MasterBean bean)
    		throws ClsExceptions 
    {
		Hashtable htData = null;
		try {
			htData = new Hashtable();
			CenColaCambioLetradoBean b = (CenColaCambioLetradoBean) bean;
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_FECHACAMBIO, 			b.getFechaCambio());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_FECHAMODIFICACION, 	b.getFechaMod());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDCAMBIO, 			b.getIdCambio());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDDIRECCION, 			b.getIdDireccion());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDINSTITUCION, 		b.getIdInstitucion());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDPERSONA, 			b.getIdPersona());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_IDTIPOCAMBIO, 		b.getIdTipoCambio());
			UtilidadesHash.set(htData, CenColaCambioLetradoBean.C_USUMODIFICACION, 		b.getUsuMod());
		}
		catch (Exception e) {
			ClsLogging.writeFileLog("ERROR - CenColaCambioLetradoAdm.beanToHashTable(): " + e.getMessage(), 0);
			htData = null;
			throw new ClsExceptions (e, "Error al crear el hashTable a partir del bean");
		}
		return htData;	
    } //beanToHashTable()
	
    
    
	//////////////////// OTROS METODOS ////////////////////
    public boolean insertarCambioEnCola (int idTipoCambio,
    									 Integer idInstitucion,
    									 Long idPersona,
    									 Long idDireccion) 
    {
        try
        {
			CenColaCambioLetradoBean bean = new CenColaCambioLetradoBean ();
			bean.setFechaCambio ("SYSDATE");
			bean.setIdCambio (this.getNuevoId (idInstitucion, idPersona));
			if (idDireccion != null) bean.setIdDireccion (idDireccion);
			bean.setIdInstitucion (idInstitucion);
			bean.setIdPersona (idPersona);
			bean.setIdTipoCambio (new Integer (idTipoCambio));
			return this.insert (bean);
        }
        catch (Exception e) {
        	ClsLogging.writeFileLog("ERROR - CenColaCambioLetradoAdm.insertarCambioEnCola(): " + e.getMessage(), 0);
            e.printStackTrace ();
            this.setError (e.getMessage () + " (" + 
            		CenColaCambioLetradoBean.T_NOMBRETABLA + ")");
            return false;
        }
    } //insertarCambioEnCola()
	
	public Long getNuevoId(Integer idInstitucion,
						   Long idPersona)
			throws ClsExceptions 
	{
		try {
			String select = 
				"select max(" +CenColaCambioLetradoBean.C_IDCAMBIO+") + 1 AS ID " +
				"  from "+CenColaCambioLetradoBean.T_NOMBRETABLA+"" + 
				" where "+CenColaCambioLetradoBean.C_IDINSTITUCION+" = " + idInstitucion + 
				"   and "+CenColaCambioLetradoBean.C_IDPERSONA+" = " + idPersona;					  			
			
			RowsContainer rc = new RowsContainer();
			if (rc.findForUpdate(select)) {
				Hashtable prueba = ((Row) rc.get(0)).getRow();
				Long id = UtilidadesHash.getLong(prueba, "ID");
				if (id == null) 
					return new Long(1);
				else 
				    return id;								
			}
			throw new ClsExceptions ("Error al obtener el id del cambio en B.D."); 
		}
		catch (Exception e) {
			ClsLogging.writeFileLog("ERROR - CenColaCambioLetradoAdm.getNuevoId(): " + e.getMessage(), 0);
			throw new ClsExceptions (e, "Error al obtener el id del cambio en B.D."); 
		}
	} //getNuevoId()
	
	/**
	 * Revisa si hay algun registro en la cola de cambios de letrado, lo trata y lo elimina de la cola.
	 * Se consulta la cola despues de cada tratamiento, para evitar asi historico masivo de registros.
	 */
	public void chequearCola()
	{
		// Controles
		CenPersonaAdm personaAdm2 = new CenPersonaAdm(this.usrbean);
		UserTransaction tx;
		
		try {
			// obteniendo ruta de log de cola
			ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
			String pathFicheroLog = rp.returnProperty("LogAdmin.archivo");
			String nombreFichero = rp.returnProperty("LogAdmin.archivo.gestionColas");
			SIGALogging log = new SIGALogging(pathFicheroLog+File.separator+nombreFichero);

			tx = this.usrbean.getTransaction();

			Vector vCola = this.selectFirst();
			while (vCola.size() > 0) {

				CenColaCambioLetradoBean bean = (CenColaCambioLetradoBean) vCola.get(0);
				vCola.remove(0);
				String nombreCliente = personaAdm2.obtenerNombreApellidos("" + bean.getIdPersona());

				try {

					tx.begin();

					String rc[] = EjecucionPLs.ejecutarPL_ActualizarDatosLetrado(bean.getIdInstitucion(), bean.getIdPersona(), bean.getIdTipoCambio(), bean.getIdDireccion(), this.usuModificacion);

					if (!this.delete(bean)) { //si falla el borrado del registro en BD
						log.writeLogGestorColaSincronizarDatos(SIGALogging.ERROR, bean.getIdInstitucion(), bean.getIdPersona(), nombreCliente, "Error borrando elemento de la cola");
					} else if ((new Integer(rc[0])).intValue() < 0) { //si falla la ejecucion del paquete de actualizacion
						log.writeLogGestorColaSincronizarDatos(SIGALogging.ERROR, bean.getIdInstitucion(), bean.getIdPersona(), nombreCliente, rc[1]);
					} else { //si todo va bien
						log.writeLogGestorColaSincronizarDatos(SIGALogging.INFO, bean.getIdInstitucion(), bean.getIdPersona(), nombreCliente, rc[1]);
					}
					
					tx.commit();

				} catch (Exception e) {
					log.write("ERROR - CenColaCambioLetradoAdm.chequearCola(): " + e.getMessage());
					tx.rollback();
				} finally {
					vCola = this.selectFirst();
				}
			}
			
			log.write("Actualizardatosletrado: antes de terminar, ejecutamos un parche para revisar direcciones de Traspaso que no se han copiado");
			StringBuilder sqlOOJJ = new StringBuilder();
			sqlOOJJ.append("Insert Into Cen_Colacambioletrado");
			sqlOOJJ.append("  (Idpersona, Idinstitucion, Idcambio, Fechacambio, Idtipocambio, Iddireccion, Fechamodificacion, Usumodificacion)");
			sqlOOJJ.append("  (Select Col.Idpersona, Col.Idinstitucion, nvl((Select max(col2.Idcambio) From Cen_Colacambioletrado col2), 0)+rownum, Sysdate, 30, Dir.Iddireccion, Sysdate, -7");
			sqlOOJJ.append("     From Cen_Colegiado Col, Cen_Direcciones Dir, Cen_Direccion_Tipodireccion Tip");
			sqlOOJJ.append("    Where Col.Situacionejercicio = '1'");
			sqlOOJJ.append("      And Dir.Idinstitucion = Tip.Idinstitucion");
			sqlOOJJ.append("      And Dir.Idpersona = Tip.Idpersona");
			sqlOOJJ.append("      And Dir.Iddireccion = Tip.Iddireccion");
			sqlOOJJ.append("      And Tip.Idtipodireccion = ");
			sqlOOJJ.append(AppConstants.TIPO_TRASPASO_ORGANOS_JUDICIALES);
			sqlOOJJ.append("      And Dir.Idinstitucion = Col.Idinstitucion");
			sqlOOJJ.append("      And Dir.Idpersona = Col.Idpersona");
			sqlOOJJ.append("      And Dir.Fechabaja Is Null");
			sqlOOJJ.append("      /* que no exista direccion copiada en el Consejo */");
			sqlOOJJ.append("      And Not Exists (Select 1");
			sqlOOJJ.append("             From Cen_Direcciones Dircon, Cen_Direccion_Tipodireccion Tipcon");
			sqlOOJJ.append("            Where Dircon.Idinstitucion = Tipcon.Idinstitucion");
			sqlOOJJ.append("              And Dircon.Idpersona = Tipcon.Idpersona");
			sqlOOJJ.append("              And Dircon.Iddireccion = Tipcon.Iddireccion");
			sqlOOJJ.append("              And Tipcon.Idtipodireccion = ");
			sqlOOJJ.append(AppConstants.TIPO_TRASPASO_ORGANOS_JUDICIALES);
			sqlOOJJ.append("              And Dircon.Idinstitucion = ");
			sqlOOJJ.append(AppConstants.IDINSTITUCION_2000);
			sqlOOJJ.append("              And Dircon.Idpersona = Col.Idpersona");
			sqlOOJJ.append("              And Dircon.Fechabaja Is Null");
			sqlOOJJ.append("              /* si ya se ha copiado la direccion al Consejo, en este sera posterior a la del colegio */");
			sqlOOJJ.append("              And dircon.Fechamodificacion >= dir.Fechamodificacion)");
			sqlOOJJ.append("      And Exists (Select 1");
			sqlOOJJ.append("             From Cen_Datoscolegialesestado Est2");
			sqlOOJJ.append("            Where Est2.Idinstitucion = Col.Idinstitucion");
			sqlOOJJ.append("              And Est2.Idpersona = Col.Idpersona");
			sqlOOJJ.append("              And Trunc(Est2.Fechaestado) <= Sysdate)");
			sqlOOJJ.append("      And Not Exists (Select 1");
			sqlOOJJ.append("             From Cen_Colacambioletrado Col2");
			sqlOOJJ.append("            Where Col.Idinstitucion = Col2.Idinstitucion");
			sqlOOJJ.append("              And Col.Idpersona = Col2.Idpersona))");
			this.insertSQL(sqlOOJJ.toString());
			
			log.write("Actualizardatosletrado: antes de terminar, ejecutamos un parche para revisar direcciones de CorreoWeb que no se han copiado");
			StringBuilder sqlCens = new StringBuilder();
			sqlCens.append("Insert Into Cen_Colacambioletrado");
			sqlCens.append("  (Idpersona, Idinstitucion, Idcambio, Fechacambio, Idtipocambio, Iddireccion, Fechamodificacion, Usumodificacion)");
			sqlCens.append("  (Select Col.Idpersona, Col.Idinstitucion, nvl((Select max(col2.Idcambio) From Cen_Colacambioletrado col2), 0)+rownum, Sysdate, 30, Dir.Iddireccion, Sysdate, -7");
			sqlCens.append("     From Cen_Colegiado Col, Cen_Direcciones Dir, Cen_Direccion_Tipodireccion Tip");
			sqlCens.append("    Where Col.Situacionejercicio = '1'");
			sqlCens.append("      And Dir.Idinstitucion = Tip.Idinstitucion");
			sqlCens.append("      And Dir.Idpersona = Tip.Idpersona");
			sqlCens.append("      And Dir.Iddireccion = Tip.Iddireccion");
			sqlCens.append("      And Tip.Idtipodireccion = ");
			sqlCens.append(AppConstants.TIPO_DIRECCION_CENSOWEB);
			sqlCens.append("      And Dir.Idinstitucion = Col.Idinstitucion");
			sqlCens.append("      And Dir.Idpersona = Col.Idpersona");
			sqlCens.append("      And Dir.Fechabaja Is Null");
			sqlCens.append("      /* que no exista direccion copiada en el Consejo */");
			sqlCens.append("      And Not Exists (Select 1");
			sqlCens.append("             From Cen_Direcciones Dircon, Cen_Direccion_Tipodireccion Tipcon");
			sqlCens.append("            Where Dircon.Idinstitucion = Tipcon.Idinstitucion");
			sqlCens.append("              And Dircon.Idpersona = Tipcon.Idpersona");
			sqlCens.append("              And Dircon.Iddireccion = Tipcon.Iddireccion");
			sqlCens.append("              And Tipcon.Idtipodireccion = ");
			sqlCens.append(AppConstants.TIPO_DIRECCION_CENSOWEB);
			sqlCens.append("              And Dircon.Idinstitucion = ");
			sqlCens.append(AppConstants.IDINSTITUCION_2000);
			sqlCens.append("              And Dircon.Idpersona = Col.Idpersona");
			sqlCens.append("              And Dircon.Fechabaja Is Null");
			sqlCens.append("              /* si ya se ha copiado la direccion al Consejo, en este sera posterior a la del colegio */");
			sqlCens.append("              And dircon.Fechamodificacion >= dir.Fechamodificacion)");
			sqlCens.append("      And Exists (Select 1");
			sqlCens.append("             From Cen_Datoscolegialesestado Est2");
			sqlCens.append("            Where Est2.Idinstitucion = Col.Idinstitucion");
			sqlCens.append("              And Est2.Idpersona = Col.Idpersona");
			sqlCens.append("              And Trunc(Est2.Fechaestado) <= Sysdate)");
			sqlCens.append("      And Not Exists (Select 1");
			sqlCens.append("             From Cen_Colacambioletrado Col2");
			sqlCens.append("            Where Col.Idinstitucion = Col2.Idinstitucion");
			sqlCens.append("              And Col.Idpersona = Col2.Idpersona))");

			try {
				tx.begin();
				this.insertSQL(sqlOOJJ.toString());
				this.insertSQL(sqlCens.toString());
			} catch (Exception e) {
				log.write("ERROR - CenColaCambioLetradoAdm.chequearCola() > arreglo de direcciones perdidas: " + e.getMessage());
				tx.rollback();
			}
		} catch (Exception e) {
			Date dat = Calendar.getInstance().getTime();
			SimpleDateFormat sdfLong = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			String fecha = sdfLong.format(dat);
			ClsLogging.writeFileLog("ERROR: " + fecha + ": Error al configurar el tratamiento de la cola de cambio de letrado", 0);
		}
	} //chequearCola()
}
