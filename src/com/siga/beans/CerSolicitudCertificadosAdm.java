package com.siga.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.redabogacia.sigaservices.app.util.PropertyReader;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsExceptions;
import com.atos.utils.ClsLogging;
import com.atos.utils.ComodinBusquedas;
import com.atos.utils.GstDate;
import com.atos.utils.Row;
import com.atos.utils.RowsContainer;
import com.atos.utils.UsrBean;
import com.siga.Utilidades.FirmaPdfHelper;
import com.siga.Utilidades.GestorContadores;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.certificados.form.SIGASolicitudesCertificadosForm;
import com.siga.general.SIGAException;

public class CerSolicitudCertificadosAdm extends MasterBeanAdministrador
{
	public static final int C_ESTADO_CER_INICIAL							=1;
	public static final int C_ESTADO_CER_PEND								=2;
	public static final int C_ESTADO_CER_GENERADO							=3;
	public static final int C_ESTADO_CER_FIRMADO							=4;
	public static final int C_ESTADO_CER_ERRORGENERANDO						=5;
	
	
	//ACCIONES
	public static final String A_ABROBAR_GENERAR = "1";               //Acción de aprobar y generar 16/10/2015
	public static final String A_FINALIZAR = "2";               //Acción de finalizar 26/10/2015
	public static final String A_FACTURAR = "3";               //Acción de facturar 28/10/2015
	
	
	public static final String IDCGAE = "2000";
	
	
	public CerSolicitudCertificadosAdm(UsrBean usuario)
	{
	    super(CerSolicitudCertificadosBean.T_NOMBRETABLA, usuario);
	}

	protected String[] getCamposBean()
	{
		String[] campos = {CerSolicitudCertificadosBean.C_IDINSTITUCION,
		        		   CerSolicitudCertificadosBean.C_IDSOLICITUD,
		        		   CerSolicitudCertificadosBean.C_DESCRIPCION,
		        		   CerSolicitudCertificadosBean.C_FECHASOLICITUD,
		        		   CerSolicitudCertificadosBean.C_FECHAESTADO,
		        		   CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO,
		        		   CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL,
		        		   CerSolicitudCertificadosBean.C_IDPERSONA_DES,
		        		   CerSolicitudCertificadosBean.C_IDPERSONA_DIR,
		        		   CerSolicitudCertificadosBean.C_IDDIRECCION_DIR,
		        		   CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO,
		        		   CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION,
		        		   CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO,
		        		   CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO,
		        		   CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO,
		        		   CerSolicitudCertificadosBean.C_IDTIPOENVIOS,
		        		   CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN,
		        		   CerSolicitudCertificadosBean.C_IDINSTITUCIONCOLEGIACION,
		        		   CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO,
		        		   CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO,
		        		   CerSolicitudCertificadosBean.C_CONTADOR_CER,
		        		   CerSolicitudCertificadosBean.C_SUFIJO_CER,
		        		   CerSolicitudCertificadosBean.C_PREFIJO_CER,
		        		   CerSolicitudCertificadosBean.C_FECHADESCARGA,
		        		   CerSolicitudCertificadosBean.C_FECHACOBRO,
		        		   CerSolicitudCertificadosBean.C_FECHAENVIO,
		        		   CerSolicitudCertificadosBean.C_COMENTARIO,
		        		   CerSolicitudCertificadosBean.C_FECHAENTREGAINFO,
		        		   CerSolicitudCertificadosBean.C_FECHAMODIFICACION,
		        		   CerSolicitudCertificadosBean.C_IDMETODOSOLICITUD,
		        		   CerSolicitudCertificadosBean.C_IDMOTIVOSOLICITUD,
		        		   CerSolicitudCertificadosBean.C_IDMOTIVOANULACION,
		        		   CerSolicitudCertificadosBean.C_USUMODIFICACION,
		        		   CerSolicitudCertificadosBean.C_CBO_CODIGO,
		        		   CerSolicitudCertificadosBean.C_ACEPTACESIONMUTUALIDAD,
		        		   CerSolicitudCertificadosBean.C_CODIGO_SUCURSAL,
		        		   CerSolicitudCertificadosBean.C_USUCREACION,
		        		   CerSolicitudCertificadosBean.C_FECHACREACION};

		return campos;
	}
	
	protected String[] getCamposInsertBean()
	{
		String[] campos = {CerSolicitudCertificadosBean.C_IDINSTITUCION,
		        		   CerSolicitudCertificadosBean.C_IDSOLICITUD,
		        		   CerSolicitudCertificadosBean.C_DESCRIPCION,
		        		   CerSolicitudCertificadosBean.C_FECHASOLICITUD,
		        		   CerSolicitudCertificadosBean.C_FECHAESTADO,
		        		   CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO,
		        		   CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL,
		        		   CerSolicitudCertificadosBean.C_IDPERSONA_DES,
		        		   CerSolicitudCertificadosBean.C_IDPERSONA_DIR,
		        		   CerSolicitudCertificadosBean.C_IDDIRECCION_DIR,
		        		   CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO,
		        		   CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION,
		        		   CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO,
		        		   CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO,
		        		   CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO,
		        		   CerSolicitudCertificadosBean.C_IDTIPOENVIOS,
		        		   CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN,
		        		   CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO,
		        		   CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO,
		        		   CerSolicitudCertificadosBean.C_CONTADOR_CER,
		        		   CerSolicitudCertificadosBean.C_SUFIJO_CER,
		        		   CerSolicitudCertificadosBean.C_PREFIJO_CER,
		        		   CerSolicitudCertificadosBean.C_FECHADESCARGA,
		        		   CerSolicitudCertificadosBean.C_FECHACOBRO,
		        		   CerSolicitudCertificadosBean.C_FECHAENVIO,
		        		   CerSolicitudCertificadosBean.C_COMENTARIO,
		        		   CerSolicitudCertificadosBean.C_FECHAENTREGAINFO,
		        		   CerSolicitudCertificadosBean.C_FECHAMODIFICACION,
		        		   CerSolicitudCertificadosBean.C_IDMETODOSOLICITUD,
		        		   CerSolicitudCertificadosBean.C_IDMOTIVOSOLICITUD,
		        		   CerSolicitudCertificadosBean.C_IDMOTIVOANULACION,
		        		   CerSolicitudCertificadosBean.C_USUMODIFICACION,
		        		   CerSolicitudCertificadosBean.C_FECHACREACION,
		        		   CerSolicitudCertificadosBean.C_USUCREACION,
		        		   CerSolicitudCertificadosBean.C_CBO_CODIGO,
		        		   CerSolicitudCertificadosBean.C_ACEPTACESIONMUTUALIDAD,
		        		   CerSolicitudCertificadosBean.C_CODIGO_SUCURSAL};

		return campos;
	}

	protected String[] getClavesBean()
	{
		String[] claves = {CerSolicitudCertificadosBean.C_IDINSTITUCION, CerSolicitudCertificadosBean.C_IDSOLICITUD};

		return claves;
	}

	protected MasterBean hashTableToBean(Hashtable hash) throws ClsExceptions
	{
	    CerSolicitudCertificadosBean bean = null;

		try
		{
			bean = new CerSolicitudCertificadosBean();

			bean.setIdInstitucion(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDINSTITUCION));
			bean.setIdSolicitud(UtilidadesHash.getLong(hash, CerSolicitudCertificadosBean.C_IDSOLICITUD));
			bean.setDescripcion(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_DESCRIPCION));
			bean.setFechaSolicitud(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHASOLICITUD));
			bean.setFechaEstado(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHAESTADO));
			bean.setIdEstadoSolicitudCertificado(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO));
			bean.setIdInstitucion_Sol(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL));
			bean.setIdPersona_Des(UtilidadesHash.getLong(hash, CerSolicitudCertificadosBean.C_IDPERSONA_DES));
			bean.setIdPersona_Dir(UtilidadesHash.getLong(hash, CerSolicitudCertificadosBean.C_IDPERSONA_DIR));
			bean.setIdDireccion_Dir(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDDIRECCION_DIR));
			bean.setPpn_IdTipoProducto(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO));
			bean.setPpn_IdProductoInstitucion(UtilidadesHash.getLong(hash, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION));
			bean.setPpn_IdProducto(UtilidadesHash.getLong(hash, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO));
			bean.setFechaEmisionCertificado(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO));
			bean.setIdEstadoCertificado(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO));
			bean.setIdTipoEnvios(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDTIPOENVIOS));
			bean.setIdInstitucionColegiacion(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDINSTITUCIONCOLEGIACION));
			bean.setIdInstitucionOrigen(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN));
			bean.setIdInstitucionDestino(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO));
			bean.setIdPeticionProducto(UtilidadesHash.getLong(hash, CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO));

			bean.setContadorCer(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_CONTADOR_CER));
			bean.setPrefijoCer(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_PREFIJO_CER));
			bean.setSufijoCer(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_SUFIJO_CER));

			bean.setFechaEntregaInfo(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHAENTREGAINFO));
			bean.setFechaDescarga(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHADESCARGA));
			bean.setFechaCobro(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHACOBRO));
			bean.setFechaEnvio(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHAENVIO));
			bean.setComentario(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_COMENTARIO));
			bean.setMetodoSolicitud(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_IDMETODOSOLICITUD));
			bean.setIdMotivoSolicitud(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDMOTIVOSOLICITUD));
			bean.setIdMotivoAnulacion(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_IDMOTIVOANULACION));
			bean.setFechaMod(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHAMODIFICACION));
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_USUMODIFICACION));
			bean.setFechaCreacion(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_FECHACREACION));
			bean.setUsuCreacion(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_USUCREACION));
			bean.setCbo_codigo(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_CBO_CODIGO));
			bean.setCodigo_sucursal(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_CODIGO_SUCURSAL));
			bean.setAceptaCesionMutualidad(UtilidadesHash.getString(hash, CerSolicitudCertificadosBean.C_ACEPTACESIONMUTUALIDAD));
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

			CerSolicitudCertificadosBean b = (CerSolicitudCertificadosBean) bean;

			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDINSTITUCION, b.getIdInstitucion());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDSOLICITUD, b.getIdSolicitud());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_DESCRIPCION, b.getDescripcion());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHASOLICITUD, b.getFechaSolicitud());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHAESTADO, b.getFechaEstado());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO, b.getIdEstadoSolicitudCertificado());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL, b.getIdInstitucion_Sol());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDPERSONA_DES, b.getIdPersona_Des());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDPERSONA_DIR, b.getIdPersona_Dir());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDDIRECCION_DIR, b.getIdDireccion_Dir());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO, b.getPpn_IdTipoProducto());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION, b.getPpn_IdProductoInstitucion());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO, b.getPpn_IdProducto());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO, b.getFechaEmisionCertificado());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO, b.getIdEstadoCertificado());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDTIPOENVIOS, b.getIdTipoEnvios());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN, b.getIdInstitucionOrigen());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDINSTITUCIONCOLEGIACION, b.getIdInstitucionColegiacion());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO, b.getIdInstitucionDestino());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO, b.getIdPeticionProducto());

			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_CONTADOR_CER, b.getContadorCer());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_SUFIJO_CER, b.getSufijoCer());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_PREFIJO_CER, b.getPrefijoCer());

			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHAENTREGAINFO, b.getFechaEntregaInfo());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHADESCARGA, b.getFechaDescarga());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHACOBRO, b.getFechaCobro());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHAENVIO, b.getFechaEnvio());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_COMENTARIO, b.getComentario());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDMETODOSOLICITUD, b.getMetodoSolicitud());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDMOTIVOSOLICITUD, b.getIdMotivoSolicitud());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_IDMOTIVOANULACION, b.getIdMotivoAnulacion());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHAMODIFICACION, b.getFechaMod());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_USUMODIFICACION, b.getUsuMod());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_FECHACREACION, b.getFechaCreacion());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_USUCREACION, b.getUsuCreacion());		
			
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_CBO_CODIGO, b.getCbo_codigo());
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_CODIGO_SUCURSAL, b.getCodigo_sucursal());				
			UtilidadesHash.set(htData, CerSolicitudCertificadosBean.C_ACEPTACESIONMUTUALIDAD, b.getAceptaCesionMutualidad());				
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

    public void guardarCertificado(CerSolicitudCertificadosBean solicitud, File fPDF) throws SIGAException
    {
        DataInputStream inData = null;
        DataOutputStream outData = null;

        try
        {
        	final String idSolicitud= String.valueOf(solicitud.getIdSolicitud());
        	
            String sRutaBD = getRutaCertificadoDirectorioBD(solicitud.getIdInstitucion());

            if (sRutaBD.equals(""))
            {
                throw new SIGAException("messages.general.error.ficheroNoExiste");
            }

            String sRuta = getRutaCertificadoDirectorio(solicitud, sRutaBD);

            File fDirectorio = new File(sRuta);

            fDirectorio.mkdirs();

            //Antes de crear el fichero borramos el que exista (casos en los que se guarda un .pdf y ya exista un .zip)
            String[] myFiles = fDirectorio.list(new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    return fileName.startsWith(idSolicitud);
                }
            });
            if(myFiles != null){
            	File f;
            	for(int i = 0; i < myFiles.length; i++){
            		
            		f = new File(getRutaCertificadoDirectorio(solicitud, sRutaBD)+ File.separator +myFiles[i]); 
            		f.delete();
            	}
            }
            
            sRuta = getRutaCertificadoFichero(solicitud, sRutaBD);

            File fAux = new File(sRuta);

            FileInputStream inFile = new FileInputStream(fPDF);
            inData = new DataInputStream(inFile);

            FileOutputStream outFile = new FileOutputStream(fAux);
            outData = new DataOutputStream(outFile);

            byte[] cadena = new byte[1024];

            while((inData.read(cadena, 0, 1024))!=-1)
            {
                outData.write(cadena, 0, cadena.length);
            }
            
            inFile.close();
            outFile.close();
        }
        catch(SIGAException e)
        {
        	throw e;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            throw new SIGAException("messages.certificados.error.guardandocertificado");
        }

        finally
        {
            if (inData!=null)
            {
                try
                {
                    inData.close();
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            if (outData!=null)
            {
                try
                {
                    outData.close();
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
   /**
     * Obtiene la ruta del fichero de Log
     * @param solicitud
     * @throws SIGAException
     */

 
    public String obtenerRutaLogError(CerSolicitudCertificadosBean solicitud) throws SIGAException
    {
        String sRutaBD = getRutaCertificadoDirectorioBD(solicitud.getIdInstitucion());
        String sRuta = getRutaCertificadoFicheroLogError(solicitud, sRutaBD);
        
        return sRuta;
    }

    /**
     * Guarda varios certificados. Este método se usa cuando la plantilla tiene a su vez plantillas asociadas.
     * @param solicitud
     * @param fPDF
     * @param i
     * @throws SIGAException
     */
    public void guardarVariosCertificado(CerSolicitudCertificadosBean solicitud, File fPDF,int i) throws SIGAException
    {
        DataInputStream inData = null;
        DataOutputStream outData = null;

        try
        {
        	final String idSolicitud= String.valueOf(solicitud.getIdSolicitud());
        	
            String sRutaBD = getRutaCertificadoDirectorioBD(solicitud.getIdInstitucion());

            if (sRutaBD.equals(""))
            {
                throw new SIGAException("messages.general.error.ficheroNoExiste");
            }

            String sRuta = getRutaCertificadoDirectorio(solicitud, sRutaBD);

            File fDirectorio = new File(sRuta);

            fDirectorio.mkdirs();
            
            sRuta= getRutaCertificadoDirectorio(solicitud, sRutaBD) + File.separator + solicitud.getIdSolicitud() +"-"+i+".pdf";

            File fAux = new File(sRuta);

            FileInputStream inFile = new FileInputStream(fPDF);
            inData = new DataInputStream(inFile);

            FileOutputStream outFile = new FileOutputStream(fAux);
            outData = new DataOutputStream(outFile);

            byte[] cadena = new byte[1024];

            while((inData.read(cadena, 0, 1024))!=-1)
            {
                outData.write(cadena, 0, cadena.length);
            }
            
            inFile.close();
            outFile.close();
        }
        catch(SIGAException e)
        {
        	throw e;
        }
        catch(Exception e)
        {
        	e.printStackTrace();
            throw new SIGAException("messages.certificados.error.guardandocertificado");
        }

        finally
        {
            if (inData!=null)
            {
                try
                {
                    inData.close();
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            if (outData!=null)
            {
                try
                {
                    outData.close();
                }

                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    public File recuperarCertificado(CerSolicitudCertificadosBean solicitud) throws SIGAException
    {
        try
        {
            String sRutaBD = getRutaCertificadoDirectorioBD(solicitud.getIdInstitucion());

            if (sRutaBD.equals(""))
            {
                throw new SIGAException("messages.general.error.ficheroNoExiste");
            }

            return new File(getRutaCertificadoFichero(solicitud, sRutaBD));
        }

        catch(Exception e)
        {
            throw new SIGAException("messages.certificados.error.obtenercertificado");
        }
    }
     /**
     * Recupera varios certificados de una solicitud
     * @param solicitud
     * @param fPDF
     * @param i
     * @throws SIGAException
     */
    public ArrayList<File> recuperarVariosCertificado(CerSolicitudCertificadosBean solicitud) throws SIGAException
    {
    	final String idSolicitud= String.valueOf(solicitud.getIdSolicitud());
    	ArrayList<File> listaFicherosPDF = new ArrayList<File>();
        try
        {
            String sRutaBD = getRutaCertificadoDirectorioBD(solicitud.getIdInstitucion());

            if (sRutaBD.equals(""))
            {
                throw new SIGAException("messages.general.error.ficheroNoExiste");
            }
            
            File dir = new File (getRutaCertificadoDirectorio(solicitud, sRutaBD));
           
         // create new filename filter
            String[] myFiles = dir.list(new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    return fileName.startsWith(idSolicitud);
                }
            });
            if(myFiles != null){
            	File f;
            	for(int i = 0; i < myFiles.length; i++){
            		//Almacenamos sólo lo que no sean de log
            		if(myFiles[i].indexOf("LogError") == -1) {
	            		f = new File(getRutaCertificadoDirectorio(solicitud, sRutaBD)+ File.separator +myFiles[i]); 
	            		listaFicherosPDF.add(f);
            		}
            	}
            }
            
            return listaFicherosPDF;
        }

        catch(Exception e)
        {
            throw new SIGAException("messages.certificados.error.obtenercertificado");
        }
    }

    public String getRutaCertificadoDirectorioBD(Integer idInstitucion)
    {
        String sRuta="";

        try
        {
            GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);

            sRuta = admParametros.getValor(""+idInstitucion, "CER" ,"PATH_CERTIFICADOS", "");

            if (sRuta==null)
            {
                sRuta="";
            }
        }

        catch(Exception e)
        {
            sRuta="";
        }

        return sRuta;
    }

    public String getRutaCertificadoDirectorio(CerSolicitudCertificadosBean solicitud, String sRutaBD)
    {
        String anhoSolicitud=solicitud.getFechaSolicitud().substring(0, solicitud.getFechaSolicitud().indexOf("/"));
        String mesSolicitud=solicitud.getFechaSolicitud().substring(solicitud.getFechaSolicitud().indexOf("/")+1, solicitud.getFechaSolicitud().lastIndexOf("/"));

        return sRutaBD + File.separator + solicitud.getIdInstitucion() + File.separator + anhoSolicitud + File.separator + mesSolicitud;
    }
    public String getRutaCertificadoFichero(CerSolicitudCertificadosBean solicitud, String sRutaBD)
    {
        return getRutaCertificadoDirectorio(solicitud, sRutaBD) + File.separator + solicitud.getIdSolicitud() + ".pdf";
    }
    
    public String getRutaCertificadoFicheroLogError(CerSolicitudCertificadosBean solicitud, String sRutaBD)
    {
        return getRutaCertificadoDirectorio(solicitud, sRutaBD);
    }

	public String getNombreFicheroSalida(CerSolicitudCertificadosBean solicitudCertificadoBean) throws ClsExceptions, SIGAException {

		return getNombreVariosFicheroSalida(solicitudCertificadoBean,null);

	}
	
	public String getNombreVariosFicheroSalida(CerSolicitudCertificadosBean solicitudCertificadoBean, String idSolicitud) throws ClsExceptions, SIGAException {
		PysProductosInstitucionAdm admProd = new PysProductosInstitucionAdm(this.usrbean);
		Hashtable<String, Object> productoInstitucionPKHashtable = new Hashtable<String, Object>();
		productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDINSTITUCION, solicitudCertificadoBean.getIdInstitucion());
		productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDTIPOPRODUCTO, solicitudCertificadoBean.getPpn_IdTipoProducto());
		productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTO, solicitudCertificadoBean.getPpn_IdProducto());
		productoInstitucionPKHashtable.put(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION, solicitudCertificadoBean.getPpn_IdProductoInstitucion());

		Vector productoInstitucionPKVector = admProd.selectByPK(productoInstitucionPKHashtable);
		if (productoInstitucionPKVector == null || productoInstitucionPKVector.size() < 1) {
			throw new SIGAException("No se ha encontrado el producto");
		}
		PysProductosInstitucionBean beanProd = (PysProductosInstitucionBean) productoInstitucionPKVector.get(0);
		GestorContadores gc = new GestorContadores(this.usrbean);
		Hashtable<String, Object> contadorTablaHash = gc.getContador(solicitudCertificadoBean.getIdInstitucion(), beanProd.getIdContador());
		Integer longitud = new Integer((contadorTablaHash.get("LONGITUDCONTADOR").toString()));

		CenPersonaAdm personaAdm = new CenPersonaAdm(this.usrbean);
		// Obtenemos el nombre de la persona
		String nombreColegiado = personaAdm.obtenerNombreApellidos(String.valueOf(solicitudCertificadoBean.getIdPersona_Des()));

		// Si es distinto de null y de vacio eliminamos los caracteres
		// problemáticos
		if (nombreColegiado != null && !"".equalsIgnoreCase(nombreColegiado)) {
			nombreColegiado = UtilidadesString.eliminarAcentosYCaracteresEspeciales(nombreColegiado);
			solicitudCertificadoBean.setNombrePersona_Des(nombreColegiado);
		} else {
			solicitudCertificadoBean.setNombrePersona_Des("");
		}
		StringBuilder nombreFicheroSalida = new StringBuilder();
		nombreFicheroSalida.append(solicitudCertificadoBean.getNombrePersona_Des());
		nombreFicheroSalida.append("-");
		nombreFicheroSalida.append(solicitudCertificadoBean.getPrefijoCer().replaceAll("/", ""));
		if(solicitudCertificadoBean.getPrefijoCer() != null && !"".equalsIgnoreCase(solicitudCertificadoBean.getPrefijoCer())){
			nombreFicheroSalida.append("_");
		}	
		Integer contadorSugerido = new Integer(solicitudCertificadoBean.getContadorCer());
		String contadorFinalSugerido = UtilidadesString.formatea(contadorSugerido, longitud, true);
		nombreFicheroSalida.append(contadorFinalSugerido);
		if(solicitudCertificadoBean.getSufijoCer() !=null && !"".equalsIgnoreCase(solicitudCertificadoBean.getSufijoCer())){
			nombreFicheroSalida.append("_");
			nombreFicheroSalida.append(solicitudCertificadoBean.getSufijoCer().replaceAll("/", ""));
		}
		nombreFicheroSalida.append("-");
		
		if(idSolicitud != null && !"".equalsIgnoreCase(idSolicitud)){
			//Recortamos el id de solicitud para obtener el id de la plantilla y con ese id obtener el nombre de la plantilla. Ejemplo de idSolicitud: 435678-1
			String guionIdSolicitud = idSolicitud;
			String[] arrayGuion = guionIdSolicitud.split("-");
			if(arrayGuion.length == 1){
				 nombreFicheroSalida.append(arrayGuion[0]);
			}else{
				String puntoIdPlantilla = arrayGuion[1];
				String[] arrayPunto = puntoIdPlantilla.split("\\.");
				
				 CerPlantillasAdm admPlantilla = new CerPlantillasAdm (this.usrbean);
				 Hashtable aux = new Hashtable();
				 aux.put("IDPLANTILLA",arrayPunto[0]);
				 aux.put("IDINSTITUCION",String.valueOf(solicitudCertificadoBean.getIdInstitucion()));
				 aux.put("IDTIPOPRODUCTO",String.valueOf(solicitudCertificadoBean.getPpn_IdTipoProducto())); 
				 aux.put("IDPRODUCTO", String.valueOf(solicitudCertificadoBean.getPpn_IdProducto()));
				 aux.put("IDPRODUCTOINSTITUCION",String.valueOf(solicitudCertificadoBean.getPpn_IdProductoInstitucion()));
				
				 
				 Vector vector = admPlantilla.selectByPK(aux);
				
				 CerPlantillasBean htDatos = (CerPlantillasBean)vector.elementAt(0);
				
				 nombreFicheroSalida.append(arrayGuion[0]);
				 nombreFicheroSalida.append("-");
				 nombreFicheroSalida.append(UtilidadesString.eliminarAcentosYCaracteresEspeciales(htDatos.getDescripcion()));
				 nombreFicheroSalida.append(".pdf");
			}
			
		}else{
			nombreFicheroSalida.append(solicitudCertificadoBean.getIdSolicitud());
			nombreFicheroSalida.append(".pdf");

		}
		

		return nombreFicheroSalida.toString();

	}
	

	/**
	 * 
	 * @param form
	 * @param idInstitucion
	 * @return
	 * @throws ClsExceptions
	 */
	public PaginadorBind buscarSolicitudes(SIGASolicitudesCertificadosForm form, String idInstitucion) throws ClsExceptions {
		try {
			StringBuilder sql = new StringBuilder();
			StringBuilder sqlAux;
			sql.append("SELECT ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_FECHASOLICITUD);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_FECHAESTADO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_FECHACOBRO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_PREFIJO_CER);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_CONTADOR_CER);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_SUFIJO_CER);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDSOLICITUD);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDPERSONA_DES);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO);
			sql.append(", ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDMETODOSOLICITUD);
			sql.append(", ");
			sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(PysProductosInstitucionBean.C_TIPOCERTIFICADO);
			sql.append(" AS TIPOCERTIFICADO2, ");					
			sql.append("TRIM(TRAILING ' ' FROM "+PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(PysProductosInstitucionBean.C_DESCRIPCION+")"); //Se quita el espacio final que hay en algunas descripciones del certificado.
			sql.append(" AS TIPOCERTIFICADO, ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_APELLIDOS1);
			sql.append(" || ' ' || ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_APELLIDOS2);
			sql.append(" || ', ' || ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_NOMBRE);
			sql.append(" AS CLIENTE, ");
			
			sql.append(" (Select INSO.");
			sql.append(CenInstitucionBean.C_ABREVIATURA);
			sql.append("    From ");
			sql.append(CenInstitucionBean.T_NOMBRETABLA);
			sql.append(" INSO Where ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN);
			sql.append(" = INSO.");
			sql.append(CenInstitucionBean.C_IDINSTITUCION);
			sql.append(" ) AS INSTITUCIONORIGEN, ");
			
			sql.append(" (Select INSD.");
			sql.append(CenInstitucionBean.C_ABREVIATURA);
			sql.append("    From ");
			sql.append(CenInstitucionBean.T_NOMBRETABLA);
			sql.append(" INSD Where ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO);
			sql.append(" = INSD.");
			sql.append(CenInstitucionBean.C_IDINSTITUCION);
			sql.append(" ) AS INSTITUCIONDESTINO, ");
			
			sql.append(" (Select ");
			sql.append(CerEstadoSoliCertifiBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerEstadoSoliCertifiBean.C_DESCRIPCION);
			sql.append("    From ");
			sql.append(CerEstadoSoliCertifiBean.T_NOMBRETABLA);
			sql.append("   Where ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO);
			sql.append(" = ");
			sql.append(CerEstadoSoliCertifiBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO);
			sql.append(" ) AS ESTADOSOLICITUD, ");
			
			sql.append(" (Select ");
			sql.append(CerEstadoCertificadoBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerEstadoCertificadoBean.C_DESCRIPCION);
			sql.append("    From ");
			sql.append(CerEstadoCertificadoBean.T_NOMBRETABLA);
			sql.append("   Where ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO);
			sql.append(" = ");
			sql.append(CerEstadoCertificadoBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO);
			sql.append(" ) AS ESTADOCERTIFICADO ");
			
			
			sql.append("  FROM ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(", "); 
			sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(", "); 
			sql.append(CenPersonaBean.T_NOMBRETABLA);

			String numeroColegiado = form.getBusquedaNumCol();
			if (numeroColegiado != null && !numeroColegiado.equals("")) {
				sql.append(", ");
				sql.append(CenColegiadoBean.T_NOMBRETABLA);
			}

			
			sql.append(" WHERE ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(idInstitucion); 
			sql.append(" AND ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDPERSONA_DES);
			sql.append(" = ");
			sql.append(CenPersonaBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CenPersonaBean.C_IDPERSONA);
			sql.append(" AND ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
			sql.append(" = ");
			sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(PysProductosInstitucionBean.C_IDINSTITUCION);
			sql.append(" AND ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
			sql.append(" = ");
			sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(PysProductosInstitucionBean.C_IDPRODUCTO);
			sql.append(" AND ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
			sql.append(" = ");
			sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);
			sql.append(" AND ");
			sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
			sql.append(" = ");
			sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
			sql.append(".");
			sql.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);

			
			String fDesde = form.getFechaDesde(); 
			String fHasta = form.getFechaHasta(); 
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				fDesde = UtilidadesFecha.getFechaApruebaDeFormato(fDesde);
				fHasta = UtilidadesFecha.getFechaApruebaDeFormato(fHasta);
				sql.append(" AND ");
				sql.append(GstDate.dateBetweenDesdeAndHasta(CerSolicitudCertificadosBean.C_FECHAESTADO, fDesde, fHasta));
			}	
			
			fDesde = form.getFechaEmisionDesde(); 
			fHasta = form.getFechaEmisionHasta(); 
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				fDesde = UtilidadesFecha.getFechaApruebaDeFormato(fDesde);
				fHasta = UtilidadesFecha.getFechaApruebaDeFormato(fHasta);
				sql.append(" AND ");
				sql.append(GstDate.dateBetweenDesdeAndHasta(CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO, fDesde, fHasta));
			}	
			
			fDesde = form.getFechaSolicitudDesde(); 
			fHasta = form.getFechaSolicitudHasta();
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				fDesde = UtilidadesFecha.getFechaApruebaDeFormato(fDesde);
				fHasta = UtilidadesFecha.getFechaApruebaDeFormato(fHasta);
				sql.append(" AND ");
				sql.append(GstDate.dateBetweenDesdeAndHasta(CerSolicitudCertificadosBean.C_FECHASOLICITUD, fDesde, fHasta));
			}				

			String estadoSolicitud = form.getBusquedaEstado();
			if (estadoSolicitud!=null && !estadoSolicitud.equals("")) {
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO);
				sql.append(" in ( ");
				sql.append(estadoSolicitud.trim());
				sql.append(" ) ");
			} 
			
			String metodoSolicitud = form.getBusquedaMetodoSolicitud();
			if (metodoSolicitud!=null && !metodoSolicitud.equals("")) {
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_IDMETODOSOLICITUD);
				sql.append(" = ");
				sql.append(metodoSolicitud.trim());
				
			} 

			String tipoCertificado = form.getBusquedaTipoCertificado();
			if (tipoCertificado != null && !tipoCertificado.equals("")) {
				String idTipoProducto = "";
				String idProducto = "";
				String idProductoInstitucion = "";
				StringTokenizer st = new StringTokenizer(tipoCertificado, ",");
				if (st.hasMoreElements()) {
					idTipoProducto = (String) st.nextElement();
					idProducto = (String) st.nextElement();
					idProductoInstitucion = (String) st.nextElement();
				}
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
				sql.append(" = ");
				sql.append(idTipoProducto);
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
				sql.append(" = ");
				sql.append(idProducto);
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
				sql.append(" = ");
				sql.append(idProductoInstitucion);
			}

			if (numeroColegiado!=null && !numeroColegiado.equals("")) {
				sql.append(" AND ");
				sqlAux = new StringBuilder();
				sqlAux.append(CenColegiadoBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CenColegiadoBean.C_NCOLEGIADO);
				sql.append(ComodinBusquedas.tratarNumeroColegiado(numeroColegiado.trim(), sqlAux.toString()));
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
				sql.append(" = ");
				sql.append(CenColegiadoBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenColegiadoBean.C_IDINSTITUCION); 
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_IDPERSONA_DES);
				sql.append(" = ");
				sql.append(CenColegiadoBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CenColegiadoBean.C_IDPERSONA);
			}
			
			// Consulta sobre el campo NIF/CIF, si el usuario mete comodines la búsqueda es como se hacía siempre, en el caso de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte la consulta adecuada.
			String sCifNif = form.getBusquedaNIF();
			if (sCifNif!=null && !sCifNif.trim().equals("")) {
				sql.append(" AND ");
				sqlAux = new StringBuilder();
				sqlAux.append(CenPersonaBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CenPersonaBean.C_NIFCIF);
				if (ComodinBusquedas.hasComodin(sCifNif)){										
					sql.append(ComodinBusquedas.prepararSentenciaCompleta(sCifNif.trim(), sqlAux.toString()));
				} else {
					sql.append(ComodinBusquedas.prepararSentenciaNIF(sCifNif, sqlAux.toString().toUpperCase()));
				}
			}			
			
			String nombre = form.getBusquedaNombre();
			if (nombre!=null && !nombre.trim().equals("")) {
				sql.append(" AND ");
				sqlAux = new StringBuilder();
				sqlAux.append(CenPersonaBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CenPersonaBean.C_NOMBRE);
				sql.append(ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(), sqlAux.toString()));
			}
			
			String apellido1 = form.getBusquedaApellidos();
			if (apellido1!=null && !apellido1.trim().equals("")) {
				sql.append(" AND ");
				sqlAux = new StringBuilder();
				sqlAux.append(CenPersonaBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CenPersonaBean.C_APELLIDOS1);
				sqlAux.append(" || ' ' || ");
				sqlAux.append(CenPersonaBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CenPersonaBean.C_APELLIDOS2);
				sql.append(ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(), sqlAux.toString()));
			}
			
			String idInstitucionOrigen = form.getBusquedaIdInstitucionOrigen();
			if (idInstitucionOrigen!=null && !idInstitucionOrigen.equals("")) {
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN);
				sql.append(" = ");
				sql.append(idInstitucionOrigen);
			}

			String idInstitucionDestino = form.getBusquedaIdInstitucionDestino();
			if (idInstitucionDestino!=null && !idInstitucionDestino.equals("")) {
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCIONCOLEGIACION);
				sql.append(" = ");
				sql.append(idInstitucionDestino);
			}
			
			String idSolicitud = form.getBusquedaIdSolicitud();
			if (idSolicitud!=null && !idSolicitud.equals("")) {
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_IDSOLICITUD);
				sql.append(" = ");
				sql.append(idSolicitud.trim());
			}

			String numeroCertificado = form.getBuscarNumCertificadoCompra();
			if (numeroCertificado!=null && !numeroCertificado.equals("")) {
				sql.append(" AND ( ");
				sqlAux = new StringBuilder();
				sqlAux.append(" NVL(");
				sqlAux.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CerSolicitudCertificadosBean.C_PREFIJO_CER);
				sqlAux.append(", '') || NVL(");
				sqlAux.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CerSolicitudCertificadosBean.C_CONTADOR_CER);
				sqlAux.append(", '') || NVL(");
				sqlAux.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CerSolicitudCertificadosBean.C_SUFIJO_CER);
				sqlAux.append(", '') ");
				sql.append(ComodinBusquedas.prepararSentenciaCompleta(numeroCertificado.trim(), sqlAux.toString()));
				
				sql.append(" OR ");
				sqlAux = new StringBuilder();
				sqlAux.append(" NVL(");
				sqlAux.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CerSolicitudCertificadosBean.C_PREFIJO_CER);
				sqlAux.append(",'') || NVL(LPAD(");
				sqlAux.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CerSolicitudCertificadosBean.C_CONTADOR_CER);
				sqlAux.append(", NVL((SELECT ");
				sqlAux.append(AdmContadorBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(AdmContadorBean.C_LONGITUDCONTADOR);
				sqlAux.append(" FROM ");
				sqlAux.append(AdmContadorBean.T_NOMBRETABLA); 
				sqlAux.append(" WHERE ");
				sqlAux.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(PysProductosInstitucionBean.C_IDCONTADOR);
				sqlAux.append(" = ");
				sqlAux.append(AdmContadorBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(AdmContadorBean.C_IDCONTADOR);
				sqlAux.append(" AND ");
				sqlAux.append(PysProductosInstitucionBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(PysProductosInstitucionBean.C_IDINSTITUCION);
				sqlAux.append(" = ");
				sqlAux.append(AdmContadorBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(AdmContadorBean.C_IDINSTITUCION); 
				sqlAux.append("), 1), '0'), '') || NVL(");
				sqlAux.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sqlAux.append(".");
				sqlAux.append(CerSolicitudCertificadosBean.C_SUFIJO_CER);
				sqlAux.append(", '')");
				sql.append(ComodinBusquedas.prepararSentenciaCompleta(numeroCertificado.trim(), sqlAux.toString()));
				sql.append(" ) ");
			}
			
			String cobrado = form.getCobrado();
			if (cobrado != null && !cobrado.equals("")) {
				if (cobrado.equals("1")) {
					sql.append(" AND ");
					sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CerSolicitudCertificadosBean.C_FECHACOBRO);
					sql.append(" IS NOT NULL ");
				} else if (cobrado.equals("0")) {
					sql.append(" AND ");
					sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CerSolicitudCertificadosBean.C_FECHACOBRO);
					sql.append(" IS NULL ");
				}
			}
			
			String descargado = form.getDescargado();
			if (descargado != null && !descargado.equals("")) {
				if (descargado.equals("1")) {
					sql.append(" AND ");
					sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CerSolicitudCertificadosBean.C_FECHADESCARGA);
					sql.append(" IS NOT NULL ");
				} else if (descargado.equals("0")) {
					sql.append(" AND ");
					sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
					sql.append(".");
					sql.append(CerSolicitudCertificadosBean.C_FECHADESCARGA);
					sql.append(" IS NULL ");
				}
			}	
			
			String enviado = form.getEnviado();
			if(enviado != null && enviado.equals("1")){
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_FECHAENVIO);
				sql.append(" IS NOT NULL");
				
			}else if (enviado.equals("0")) {
				sql.append(" AND ");
				sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
				sql.append(".");
				sql.append(CerSolicitudCertificadosBean.C_FECHAENVIO);
				sql.append(" IS NULL");
			}

			sql.append(" ORDER BY ");
			sql.append(CerSolicitudCertificadosBean.C_IDSOLICITUD);
			sql.append(" DESC ");

			PaginadorBind paginador = new PaginadorBind(sql.toString(), new Hashtable());

			if (paginador.getNumeroTotalRegistros() == 0) {
				paginador = null;
			}

			return paginador;
		}

		catch (Exception e) {
			throw new ClsExceptions(e, "Error obteniendo clientes colegiados");
		}

	} // buscarSolicitudes()

    /**
     * Función que inserta solicitudes de certificado 
     * @param idPersona
     * @param idInstOrigen
     * @param idInstDestino
     * @param descripcion
     * @param user
     * @throws SIGAException
     * @throws ClsExceptions
     */
	public CerSolicitudCertificadosBean insertarSolicitudCertificado(
			String idPersona, String idInstOrigen, String idInstDestino,
			String descripcion, String fechaSolicitud, String metodoSolicitud,
			UsrBean user) throws SIGAException, ClsExceptions {

		// RGG 28/05/2007 Obtencion de parametro para ver si se envian
		// comunicados
		GenParametrosAdm admParam = new GenParametrosAdm(this.usrbean);
		String sCom = admParam.getValor(user.getLocation(), "CER", "GENERAR_COMUNICADOS", "1");
		boolean comunicado = (sCom.equals("1")) ? true : false;

		CerSolicitudCertificadosBean solicConsejoBean = null;
		String idInstitucion = user.getLocation();
		CenInstitucionAdm instAdm = new CenInstitucionAdm(this.usrbean);
		String idInstPadre = String.valueOf(instAdm.getInstitucionPadre(Integer.valueOf(idInstOrigen)));
		boolean instCorrecta = idInstitucion.equals(CerSolicitudCertificadosAdm.IDCGAE) || idInstitucion.equals(idInstOrigen)
				|| idInstitucion.equals(idInstDestino) || idInstitucion.equals(idInstPadre);
		if (instCorrecta) {
			PysProductosInstitucionAdm pysAdm = new PysProductosInstitucionAdm(this.usrbean);
			Vector diligenciasConsejo, comunicacionesColegio;
			PysProductosInstitucionBean pysDilBean = null, pysComBean = null;

			try {

				// RGG 06/05/2009 Ahora en lugar de crearse la diligencia para
				// CGAE ç
				// se crea para el consjeo que lo está haciendo sea CGAE o
				// autonómico.

				// Petición para el CGAE
				try {
					diligenciasConsejo = pysAdm.getDiligenciasConsejo(idInstitucion);
				} catch (ClsExceptions e) {
					throw e;
				}
				if (diligenciasConsejo.size() > 0) {
					pysDilBean = (PysProductosInstitucionBean) diligenciasConsejo.firstElement();
				} else {
					throw new SIGAException("messages.certificados.error.nodiligenciacgae");
				}

				solicConsejoBean = new CerSolicitudCertificadosBean();
				solicConsejoBean.setIdSolicitud(this.getNewIdSolicitudCertificado(idInstitucion));

				solicConsejoBean.setIdInstitucion(Integer.valueOf(idInstitucion));
				solicConsejoBean.setPpn_IdTipoProducto(pysDilBean.getIdTipoProducto());
				solicConsejoBean.setPpn_IdProductoInstitucion(pysDilBean.getIdProductoInstitucion());
				solicConsejoBean.setPpn_IdProducto(pysDilBean.getIdProducto());

				solicConsejoBean.setIdPersona_Des(Long.valueOf(idPersona));
				solicConsejoBean.setIdInstitucionOrigen(Integer.valueOf(idInstOrigen));
				solicConsejoBean.setIdInstitucionDestino(Integer.valueOf(idInstDestino));
				solicConsejoBean.setIdEstadoSolicitudCertificado(Integer.valueOf(CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND));
				solicConsejoBean.setDescripcion(descripcion);
				if (fechaSolicitud.equalsIgnoreCase("")) {
					solicConsejoBean.setFechaSolicitud("SYSDATE");
				} else {
					solicConsejoBean.setFechaSolicitud(GstDate.getApplicationFormatDate("", GstDate.anyadeHora(fechaSolicitud)));
				}
				if (!metodoSolicitud.equalsIgnoreCase("")) {
					solicConsejoBean.setMetodoSolicitud(metodoSolicitud);
				}
				solicConsejoBean.setFechaEstado("SYSDATE");
				solicConsejoBean.setIdInstitucion_Sol(Integer.valueOf(idInstitucion));
				solicConsejoBean.setIdEstadoCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.C_ESTADO_CER_INICIAL));

				solicConsejoBean.setFechaCreacion("SYSDATE");
				solicConsejoBean.setUsuCreacion(new Integer(user.getUserName()));		
				
				// jbd // Por defecto se acepta las condiciones de la mutualidad para las nuevas incorporaciones
				if(isCertNuevaIncorporacion(solicConsejoBean.getIdInstitucion().toString(), 
						solicConsejoBean.getPpn_IdProducto().toString(), 
						solicConsejoBean.getPpn_IdTipoProducto().toString(), 
						solicConsejoBean.getPpn_IdProductoInstitucion().toString())){
					solicConsejoBean.setAceptaCesionMutualidad("1");
				}

				
				this.insertCertificado(this.beanToHashTable(solicConsejoBean));

				if (comunicado) {

					// Petición para el colegio
					if (instAdm.tieneSIGA(Integer.valueOf(idInstOrigen))) {
						try {
							comunicacionesColegio = pysAdm.getComunicacionesInst(Integer.valueOf(idInstOrigen));
						} catch (ClsExceptions e) {
							throw e;
						}
						if (comunicacionesColegio.size() > 0) {
							pysComBean = (PysProductosInstitucionBean) comunicacionesColegio.firstElement();
						} else {
							throw new SIGAException("messages.certificados.error.nocomunicacion");
						}
						CerSolicitudCertificadosBean solicColegioBean = new CerSolicitudCertificadosBean();
						solicColegioBean.setIdSolicitud(this.getNewIdSolicitudCertificado(idInstOrigen));

						solicColegioBean.setIdInstitucion(Integer.valueOf(idInstOrigen));
						solicColegioBean.setPpn_IdTipoProducto(pysComBean.getIdTipoProducto());
						solicColegioBean.setPpn_IdProductoInstitucion(pysComBean.getIdProductoInstitucion());
						solicColegioBean.setPpn_IdProducto(pysComBean.getIdProducto());

						solicColegioBean.setIdPersona_Des(Long.valueOf(idPersona));
						solicColegioBean.setIdInstitucionOrigen(Integer.valueOf(idInstOrigen));
						solicColegioBean.setIdInstitucionDestino(Integer.valueOf(idInstDestino));
						solicColegioBean.setIdEstadoSolicitudCertificado(Integer.valueOf(CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND));
						solicColegioBean.setDescripcion(descripcion);
						solicColegioBean.setFechaSolicitud("SYSDATE");
						solicColegioBean.setFechaEstado("SYSDATE");
						solicColegioBean.setIdInstitucion_Sol(Integer.valueOf(idInstitucion));
						solicColegioBean.setIdEstadoCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.C_ESTADO_CER_INICIAL));
						solicColegioBean.setFechaCreacion("SYSDATE");
						solicColegioBean.setUsuCreacion(new Integer(user.getUserName()));	
						this.insertCertificado(this.beanToHashTable(solicColegioBean));
					}

					// Si el colegio origen es distinto del actual...
					if (!idInstitucion.equals(idInstOrigen) && !idInstitucion.equals(CerSolicitudCertificadosAdm.IDCGAE)) {
						CerSolicitudCertificadosBean solicBean = new CerSolicitudCertificadosBean();
						solicBean.setIdSolicitud(this.getNewIdSolicitudCertificado(idInstitucion));

						solicBean.setIdInstitucion(Integer.valueOf(idInstitucion));
						solicBean.setIdPersona_Des(Long.valueOf(idPersona));
						solicBean.setIdInstitucionOrigen(Integer.valueOf(idInstOrigen));
						solicBean.setIdInstitucionDestino(Integer.valueOf(idInstDestino));
						solicBean.setDescripcion(descripcion);
						String estado = ""+ (instAdm.tieneSIGA(Integer.valueOf(idInstOrigen)) ? CerEstadoSoliCertifiAdm.C_ESTADO_SOL_FINALIZADO
								: CerEstadoSoliCertifiAdm.C_ESTADO_SOL_PEND);
						solicBean.setIdEstadoSolicitudCertificado(Integer.valueOf(estado));
						solicBean.setFechaSolicitud("SYSDATE");
						solicBean.setFechaEstado("SYSDATE");
						solicBean.setIdInstitucion_Sol(Integer.valueOf(idInstitucion));
						solicBean.setIdEstadoCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.C_ESTADO_CER_INICIAL));
						solicBean.setFechaCreacion("SYSDATE");
						solicBean.setUsuCreacion(new Integer(user.getUserName()));	
						this.insertCertificado(this.beanToHashTable(solicBean));
					}
				}

			} catch (Exception e1) {

				if (!e1.getClass().getName().equals(
						"com.siga.general.SIGAException")) {
					SIGAException se = new SIGAException(
							"messages.general.error");
					String params[] = { "modulo.certificados" };
					se.setParams(params);
					throw se;
				} else {
					SIGAException se = (SIGAException) e1;
					throw se;
				}
			}
		} else {

			SIGAException exc = new SIGAException(
					"messages.certificados.error.institucionnoadecuada");
			throw exc;
		}
		return solicConsejoBean;
		
	} //insertarSolicitudCertificado()
    
	/** Funcion insert (Hashtable hash)
	 *  @param hasTable con las parejas campo-valor para realizar un where en el insert 
	 *  @return true si todo va bien OK, false si hay algun error 
	 * */
	public boolean insertCertificado(Hashtable hash) throws ClsExceptions{
		try
		{
			// Establecemos las datos de insercion
			UtilidadesHash.set(hash, MasterBean.C_USUMODIFICACION, this.usuModificacion); 
			UtilidadesHash.set(hash, MasterBean.C_FECHAMODIFICACION, "sysdate");

			Row row = new Row();
			row.load(hash);

			String [] campos = this.getCamposInsertBean();
			
			if (row.add(this.nombreTabla, campos) == 1) {
				return true;
			}
		}
		catch (Exception e) {
			throw new ClsExceptions(e,  e.getMessage());
		}
		return false;
	}
	
    public Long getNewIdSolicitudCertificado(String idInstitucion) throws ClsExceptions{
    	
    	String sql;
		RowsContainer rc = null;
		Long id=null;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql ="SELECT SEQ_SOLICITUDCERTIFICADOS.NEXTVAL FROM DUAL";
		
		try {		
			if (rc.findForUpdate(sql)) {						
				Row fila = (Row) rc.get(0);
				id = Long.valueOf((String)fila.getRow().get("NEXTVAL"));														
			}
		}	
 
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en BBDD");		
		}		
		return id;
  
    }
    
 	public boolean firmarPDF(String idSolicitud, String idInstitucion,Integer i)
 	{
        FileInputStream fisID = null;
        FileOutputStream fos = null;
        try
 		{
	    	GregorianCalendar gcFecha = new GregorianCalendar();
	        
            GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);
            String sPathCertificados = admParametros.getValor(idInstitucion, "CER", "PATH_CERTIFICADOS", "");
            CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.usrbean);
            Hashtable htSolicitud = new Hashtable();
            htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
            htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
            Vector vSolicitud = admSolicitud.selectByPK(htSolicitud);
            CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vSolicitud.elementAt(0);
            String sNombreFicheroEntrada = getRutaCertificadoDirectorio(beanSolicitud, sPathCertificados);
           
            if(i == -1){
            	//Fichero principal (se realiza como siempre se había hecho)
            	 sNombreFicheroEntrada = getRutaCertificadoFichero(beanSolicitud, sPathCertificados);
            }else{
            	//Ficheros subplantillas
            	 sNombreFicheroEntrada =sNombreFicheroEntrada + File.separator + idSolicitud +"-"+i+".pdf";
            }
	        boolean isFirmadoOk = FirmaPdfHelper.firmarPDF(new Short(idInstitucion), sNombreFicheroEntrada);
	        ClsLogging.writeFileLog("PDF FIRMADO:: " + isFirmadoOk, 10);
	        
	        
 			return isFirmadoOk;
 		}
 		
 		catch(Exception e)
 		{
 			ClsLogging.writeFileLog("***************** ERROR DE FIRMA DIGITAL EN DOCUMENTO *************************", 3);
 			ClsLogging.writeFileLog("Error al FIRMAR el PDF de la Solicitud: " + idSolicitud + " IdInstitucion: "+idInstitucion, 3);
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
//	        e.printStackTrace();
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
 			return false;
 		}
 		finally {
 			try {
 				if(fos!=null)fos.close();
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			try {
 				if(fisID!=null)	fisID.close();
 			} catch (Exception e) {
 				e.printStackTrace();
 			}

			
 		}
 	}
 	
    public boolean tieneContador(String idInstitucion, String  idSolicitud) throws ClsExceptions{
        boolean salida = true; 
        Hashtable sol = new Hashtable();
        sol.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
        sol.put(CerSolicitudCertificadosBean.C_IDSOLICITUD,idSolicitud);
        Vector v = this.selectByPK(sol);
        if (v!=null && v.size()>0) {
        	CerSolicitudCertificadosBean bb= (CerSolicitudCertificadosBean) v.get(0);
        	if (bb.getContadorCer()==null || bb.getContadorCer().trim().equals("")) {
        		salida=false;
        	}
        }
        return salida;        
    }
	
     	
 	
    public boolean esConsejo(String idInstitucion) throws ClsExceptions{
        boolean salida = false; 
        
        if (idInstitucion.equals("2000") || idInstitucion.substring(1,1).equals("3")) {
        	salida=true;
        }
        
        return salida;        
    }

    public boolean existePersonaCertificado(String idInstitucion, String idSolicitud) throws ClsExceptions {
    	boolean salida=false;
    	try {
    		String where = " WHERE "+CerSolicitudCertificadosBean.C_IDINSTITUCION + "=" + idInstitucion + " AND "+CerSolicitudCertificadosBean.C_IDSOLICITUD+ "=" + idSolicitud;
    		Vector v=this.select(where);
    		CerSolicitudCertificadosBean beanCer = null;
    		if (v!=null && v.size()>0) {
    			beanCer = (CerSolicitudCertificadosBean) v.get(0);
    		}
    		if (beanCer!=null) {
    			CenClienteAdm admCli = new CenClienteAdm(this.usrbean);
        		String where2 = " WHERE "+CenClienteBean.C_IDINSTITUCION + "=" + beanCer.getIdInstitucionOrigen() + " AND "+CenClienteBean.C_IDPERSONA+ "=" + beanCer.getIdPersona_Des();
        		Vector v2=admCli.select(where2);
        		if (v2!=null && v2.size()>0) {
        			salida = true;
        		} else {
        			salida = false;
        		}
    		}
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al comprobar existencia de persona.");
    	}
		return salida;
    }
    
   
    public CerSolicitudCertificadosTextoBean getSolicitudCertificadosTexto(String idInstitucion, String idSolicitud) throws ClsExceptions {
    	String salida="";
		RowsContainer rows=new RowsContainer();
		CerSolicitudCertificadosTextoBean beanSolicitudCertTexto = null;
		try {
    		CerSolicitudCertificadosTextoAdm admTx= new CerSolicitudCertificadosTextoAdm(this.usrbean);
    		Hashtable ht = new Hashtable();
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDINSTITUCION,idInstitucion);
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDSOLICITUD,idSolicitud);
    		Vector v = admTx.select(ht);
    		
    		if (v!=null && v.size()>0) {
    			//Como se filtra por PK saldra un unico registro
    			beanSolicitudCertTexto = (CerSolicitudCertificadosTextoBean) v.get(0); 
    			//salida = b.getTexto();

    			// CAMBIO PARA OBTENER UN BLOB
    			salida = rows.getClob(CerSolicitudCertificadosTextoBean.T_NOMBRETABLA,CerSolicitudCertificadosTextoBean.C_TEXTO," SELECT "+CerSolicitudCertificadosTextoBean.C_TEXTO+" FROM " + CerSolicitudCertificadosTextoBean.T_NOMBRETABLA + " WHERE "+CerSolicitudCertificadosTextoBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+CerSolicitudCertificadosTextoBean.C_IDSOLICITUD+"="+idSolicitud+" AND "+CerSolicitudCertificadosTextoBean.C_IDTEXTO+"="+beanSolicitudCertTexto.getIdTexto());
    			beanSolicitudCertTexto.setTexto(salida);
    		}
			
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener el Bean de Texto del certificado.");
    	}
		return beanSolicitudCertTexto;
    }
    
    
    
    public String obtenerTextoSancionesParaCertificado(String idInstitucion, String idPersona) throws ClsExceptions {
    	String salida="";
    	try {
    		CenSancionAdm admSan= new CenSancionAdm(this.usrbean);
    		Hashtable ht = new Hashtable();
    		ht.put(CenSancionBean.C_IDINSTITUCION,idInstitucion);
    		ht.put(CenSancionBean.C_IDPERSONA,idPersona);
    		Vector v = admSan.select(ht);
    		for (int i=0;i<v.size();i++) {
    			CenSancionBean b = (CenSancionBean) v.get(i);
        		/*
    			CenTipoSancionAdm admTSan= new CenTipoSancionAdm(this.usuModificacion);
        		Hashtable ht2 = new Hashtable();
        		ht2.put(CenTipoSancionBean.C_IDTIPOSANCION,b.getIdTipoSancion());
        		Vector v2 = admTSan.selectByPK(ht2);
        		for (int ii=0;ii<v2.size();ii++) {
        			CenTipoSancionBean b2 = (CenTipoSancionBean) v2.get(ii);
        			salida += b2.getDescripcion().toUpperCase() + "\\n";
        		}
        		*/
    			

    			if (!b.getTexto().trim().equals("")) {
    				salida += b.getTexto();
					salida += "\n\r\n\r";
    			}
    			
    			//salida += b.getObservaciones() + "\\n";
    		}
    		salida = UtilidadesString.escape(salida);
			
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener Sanciones Certificado.");
    	}
		return salida;
    }

    public String getTextoHistoricoEstados(String idInstitucion, String idPersona, UsrBean user, boolean isIncluirLiteratura) throws ClsExceptions{
	    StringBuffer sql = new StringBuffer();
	    sql.append(" SELECT ");
	    //Si quieren la fecha en numericos
	    if(isIncluirLiteratura){
	    	sql.append("PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(d.");
		    sql.append(CenDatosColegialesEstadoBean.C_FECHAESTADO);
		    sql.append(",'dma',");
		    sql.append(user.getLanguageInstitucion());
		    sql.append(") FECHA, ");
	    }
	    //Si queiere la letra en alfabetico
	    else{
	    	sql.append(" TO_CHAR (d.");
		    sql.append( CenDatosColegialesEstadoBean.C_FECHAESTADO); 
		    sql.append( ",'dd-mm-yyyy') FECHA, ");
	    	
	    	
	    	
	    }
	    sql.append(UtilidadesMultidioma.getCampoMultidiomaSimple("e."+ CenEstadoColegialBean.C_DESCRIPCION, user.getLanguage())); 
		sql.append(" DESCRIPCION "); 
	    sql.append(" FROM " ); 
	    sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA); 
	    sql.append( " d, ");
	    sql.append(CenEstadoColegialBean.T_NOMBRETABLA); 
	    sql.append( " e " );
	    sql.append(" WHERE "); 
	    sql.append( CenDatosColegialesEstadoBean.C_IDPERSONA); 
	    sql.append( " = "); 
	    sql.append( idPersona);
	    sql.append(" AND "); 
    	sql.append( CenDatosColegialesEstadoBean.C_IDINSTITUCION ); 
    	sql.append( " =  "); 
    	sql.append( idInstitucion);
	    sql.append(" AND d."); 
    	sql.append( CenDatosColegialesEstadoBean.C_IDESTADO); 
    	sql.append( " = e."); 
    	sql.append( CenEstadoColegialBean.C_IDESTADO);
	    sql.append(" ORDER BY d."); 
	    sql.append( CenDatosColegialesEstadoBean.C_FECHAESTADO); 
	    sql.append( " ASC ");
	    //  NO SE METE LA CONDICION "AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE"
	    //  PARA QUE SALGAN TODOS, INCLUSO LOS FUTUROS.
                            
	    Vector v;
	    String sTexto = null;
		try {
			v = this.selectGenerico(sql.toString());
			sTexto = getTextoHistoricoEstados(v,usrbean,isIncluirLiteratura); 
			
		} catch (SIGAException e) {
			throw new ClsExceptions(e,"Error al obtener Historico.");
		}
	    
	    return sTexto;
	}
    private String getTextoHistoricoEstados(Vector vHistoricoEstados,UsrBean usrBean, boolean isIncluirLiteratura){
    	int numEstados = vHistoricoEstados.size();
    	StringBuffer sbTexto = new StringBuffer(); 
    	for (int i = 0; i < numEstados; i++) {
    		Hashtable h = (Hashtable) vHistoricoEstados.get(i);
    		String fecha = (String) h.get("FECHA");
    		String estado = (String)h.get("DESCRIPCION");
    		String observaciones = (String)h.get("OBSERVACIONES");
    		if(isIncluirLiteratura){
    			if(i==0){
    				sbTexto.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificado.texto.historico.comienzo1").trim());
    				sbTexto.append(" ");
    				sbTexto.append(fecha);
    				sbTexto.append(" ");
    				sbTexto.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificado.texto.historico.comienzo2").trim());
    				sbTexto.append(" ");
    				sbTexto.append(estado.trim().toLowerCase());
    				if(observaciones!=null && !observaciones.equalsIgnoreCase("")){
    					sbTexto.append(" (");
    					sbTexto.append(observaciones);
    					sbTexto.append(")");


    				}
    			}
    			//Si es par 
    			else if(i%2==0){
    				sbTexto.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificado.texto.historico.enlace1").trim());
    				sbTexto.append(" ");
    				sbTexto.append(estado.trim().toLowerCase());
    				if(observaciones!=null && !observaciones.equalsIgnoreCase("")){
    					sbTexto.append(" (");
    					sbTexto.append(observaciones);
    					sbTexto.append(")");


    				}

    				sbTexto.append(" ");
    				sbTexto.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificado.texto.historico.enlace2").trim());
    				sbTexto.append(" ");
    				sbTexto.append(fecha);
    				//Si es impar	
    			}else{
    				sbTexto.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificado.texto.historico.enlace3").trim());
    				sbTexto.append(" ");
    				sbTexto.append(fecha);
    				sbTexto.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificado.texto.historico.enlace4").trim());
    				sbTexto.append(" ");
    				sbTexto.append(estado.trim().toLowerCase());
    				if(observaciones!=null && !observaciones.equalsIgnoreCase("")){
    					sbTexto.append(" (");
    					sbTexto.append(observaciones);
    					sbTexto.append(")");


    				}



    			}



    			
    		}else{
    			sbTexto.append(fecha);
    			sbTexto.append(" ");
    			sbTexto.append(UtilidadesString.getMensajeIdioma(usrBean, "certificados.solicitudes.literal.copiarHistorico.pasaA").trim());
    			sbTexto.append(" ");
    			sbTexto.append(estado.trim().toLowerCase());
    			if(observaciones!=null && !observaciones.equalsIgnoreCase("")){
    				sbTexto.append(" (");
    				sbTexto.append(observaciones);
    				sbTexto.append(")");


    			}
    			sbTexto.append("\n\r");
    			
    			






    		}
    	}
    	if(isIncluirLiteratura){
    		if(numEstados>0)
				sbTexto.append(UtilidadesString.getMensajeIdioma(usrbean.getLanguage(),"certificado.texto.historico.final"));
    	}
    	return UtilidadesString.escape(sbTexto.toString());

    }
    
    public String getTextoHistoricoEstadosConObservaciones(String idInstitucion, String idPersona, UsrBean user, boolean isIncluirLiteratura) throws ClsExceptions 
	{
    	
    	
    		
    		StringBuffer sql = new StringBuffer();
    	    sql.append(" SELECT ");
    	    //Si quieren la fecha en numericos
    	    if(isIncluirLiteratura){
    	    	sql.append("PKG_SIGA_FECHA_EN_LETRA.F_SIGA_FECHACOMPLETAENLETRA(d.");
    		    sql.append(CenDatosColegialesEstadoBean.C_FECHAESTADO);
    		    sql.append(",'dma',");
    		    sql.append(user.getLanguageInstitucion());
    		    sql.append(") FECHA, ");
    	    }
    	    //Si queiere la letra en alfabetico
    	    else{
    	    	
    		    sql.append(" TO_CHAR (d.");
    		    sql.append( CenDatosColegialesEstadoBean.C_FECHAESTADO); 
    		    sql.append( ",'dd-mm-yyyy') FECHA, ");
    	    	
    	    	
    	    }
    	    sql.append(UtilidadesMultidioma.getCampoMultidiomaSimple("e."+ CenEstadoColegialBean.C_DESCRIPCION, user.getLanguage())); 
    		sql.append(" DESCRIPCION "); 
    		sql.append(" ,d.");
    		sql.append(CenDatosColegialesEstadoBean.C_OBSERVACIONES);
    		sql.append(" OBSERVACIONES");
    	    sql.append(" FROM " ); 
    	    sql.append(CenDatosColegialesEstadoBean.T_NOMBRETABLA); 
    	    sql.append( " d, ");
    	    sql.append(CenEstadoColegialBean.T_NOMBRETABLA); 
    	    sql.append( " e " );
    	    sql.append(" WHERE "); 
    	    sql.append( CenDatosColegialesEstadoBean.C_IDPERSONA); 
    	    sql.append( " = "); 
    	    sql.append( idPersona);
    	    sql.append(" AND "); 
        	sql.append( CenDatosColegialesEstadoBean.C_IDINSTITUCION ); 
        	sql.append( " =  "); 
        	sql.append( idInstitucion);
    	    sql.append(" AND d."); 
        	sql.append( CenDatosColegialesEstadoBean.C_IDESTADO); 
        	sql.append( " = e."); 
        	sql.append( CenEstadoColegialBean.C_IDESTADO);
    	    sql.append(" ORDER BY d."); 
    	    sql.append( CenDatosColegialesEstadoBean.C_FECHAESTADO); 
    	    sql.append( " ASC ");
    		
    			// NO SE METE LA CONDICION DE QUE  "AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE"
    			// PARA QUE SALGAN TODOS, INCLUSO LOS FUTUROS.
    		
    		Vector v = null;
    		String sTexto = null;
    		
    		try {
    			v = this.selectGenerico(sql.toString());
    			sTexto = getTextoHistoricoEstados(v,usrbean,isIncluirLiteratura);
    			
    		
    	    
    		
    	} 
    	catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener Historico.");
    	}
		return sTexto;
    }
    public void actualizaTextoSanciones(String idInstitucion, String idSolicitud, String texto) throws ClsExceptions {
    	try {
    		CerSolicitudCertificadosTextoAdm admSan= new CerSolicitudCertificadosTextoAdm(this.usrbean);
    		Hashtable ht = new Hashtable();
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDINSTITUCION,idInstitucion);
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDSOLICITUD,idSolicitud);
    		Vector v = admSan.select(ht);
    		if (v!=null && v.size()>0) {
    			CerSolicitudCertificadosTextoBean b = (CerSolicitudCertificadosTextoBean) v.get(0);
    			// PARA INSERTAR UN VARCHAR
    			/*
    			b.setTexto(texto);
    			if (!admSan.updateDirect(b)) {
    				throw new ClsExceptions("Error al actualizar texto sanciones en certificado: "+admSan.getError());
    			}
    			*/
    			// CAMBIO PARA INSERTAR UN BLOB
    			RowsContainer rows=new RowsContainer();
    			
    			if ((texto != null) && texto.trim().equals("")) {
    				admSan.delete(b);
    			}
    			else {
        			rows.updateClob(CerSolicitudCertificadosTextoBean.T_NOMBRETABLA," WHERE "+CerSolicitudCertificadosTextoBean.C_IDINSTITUCION+"="+b.getIdInstitucion()+" AND "+CerSolicitudCertificadosTextoBean.C_IDSOLICITUD+"="+b.getIdSolicitud()+" AND "+CerSolicitudCertificadosTextoBean.C_IDTEXTO+"="+b.getIdTexto(),CerSolicitudCertificadosTextoBean.C_TEXTO,texto);
    			}
    		} 
    		else {
    			// no existen
    			CerSolicitudCertificadosTextoBean b = new CerSolicitudCertificadosTextoBean();
    			b.setIdInstitucion(new Integer(idInstitucion));
    			b.setIdSolicitud(new Long(idSolicitud));
    			String id = admSan.getNuevoID(idInstitucion,idSolicitud);
    			b.setIdTexto(new Integer(id));
    			// PARA INSERTAR UN VARCHAR
    			//b.setTexto(texto);
    			if (!admSan.insert(b)) {
    				throw new ClsExceptions("Error al insertar texto sanciones en certificado: "+admSan.getError());
    			}
    			// CAMBIO PARA INSERTAR UN BLOB
    			RowsContainer rows=new RowsContainer();
    			rows.updateClob(CerSolicitudCertificadosTextoBean.T_NOMBRETABLA," WHERE "+CerSolicitudCertificadosTextoBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+CerSolicitudCertificadosTextoBean.C_IDSOLICITUD+"="+idSolicitud+" AND "+CerSolicitudCertificadosTextoBean.C_IDTEXTO+"="+id,CerSolicitudCertificadosTextoBean.C_TEXTO,texto);
    		}
		
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al actualizar sanciones certificado.");
    	}
    }
    public void actualizaTextoCertificados(String idInstitucion, String idSolicitud, CerSolicitudCertificadosTextoBean beanSolicitudCertificadosTexto) throws ClsExceptions {
    	try {
    		CerSolicitudCertificadosTextoAdm admSan= new CerSolicitudCertificadosTextoAdm(this.usrbean);
    		Hashtable ht = new Hashtable();
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDINSTITUCION,idInstitucion);
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDSOLICITUD,idSolicitud);
    		Vector v = admSan.select(ht);
    		if (v!=null && v.size()>0) {
    			CerSolicitudCertificadosTextoBean b = (CerSolicitudCertificadosTextoBean) v.get(0);
    			// PARA INSERTAR UN VARCHAR
    			//Creamos un clone(Podiamos haber hecho el metodo en la clase) para modificar y que no meta el texto.
    			CerSolicitudCertificadosTextoBean auxBeanSolicitudCertificadosTexto = new CerSolicitudCertificadosTextoBean();
    			auxBeanSolicitudCertificadosTexto.setIdInstitucion(b.getIdInstitucion());
    			auxBeanSolicitudCertificadosTexto.setIdSolicitud(b.getIdSolicitud());
    			auxBeanSolicitudCertificadosTexto.setIdTexto(b.getIdTexto());
    			auxBeanSolicitudCertificadosTexto.setIncluirDeudas(beanSolicitudCertificadosTexto.getIncluirDeudas());
    			auxBeanSolicitudCertificadosTexto.setIncluirSanciones(beanSolicitudCertificadosTexto.getIncluirSanciones());
    			auxBeanSolicitudCertificadosTexto.setFechaMod("sysdate");
    			
    			if (!admSan.updateDirect(auxBeanSolicitudCertificadosTexto)) {
    				throw new ClsExceptions("Error al actualizar texto sanciones en certificado: "+admSan.getError());
    			}
    			
    			// CAMBIO PARA INSERTAR UN BLOB
    			RowsContainer rows=new RowsContainer();
    			
    			if ((beanSolicitudCertificadosTexto.getTexto() != null) && beanSolicitudCertificadosTexto.getTexto().trim().equals("")) {
    				admSan.delete(b);
    			}
    			else {
        			rows.updateClob(CerSolicitudCertificadosTextoBean.T_NOMBRETABLA," WHERE "+CerSolicitudCertificadosTextoBean.C_IDINSTITUCION+"="+b.getIdInstitucion()+" AND "+CerSolicitudCertificadosTextoBean.C_IDSOLICITUD+"="+b.getIdSolicitud()+" AND "+CerSolicitudCertificadosTextoBean.C_IDTEXTO+"="+b.getIdTexto(),CerSolicitudCertificadosTextoBean.C_TEXTO,beanSolicitudCertificadosTexto.getTexto());
    			}
    		} 
    		else {
    			// no existen
    			//CerSolicitudCertificadosTextoBean b = new CerSolicitudCertificadosTextoBean();
    			beanSolicitudCertificadosTexto.setIdInstitucion(new Integer(idInstitucion));
    			beanSolicitudCertificadosTexto.setIdSolicitud(new Long(idSolicitud));
    			String id = admSan.getNuevoID(idInstitucion,idSolicitud);
    			beanSolicitudCertificadosTexto.setIdTexto(new Integer(id));
    			// PARA INSERTAR UN VARCHAR
    			//b.setTexto(texto);
    			if (!admSan.insert(beanSolicitudCertificadosTexto)) {
    				throw new ClsExceptions("Error al insertar texto sanciones en certificado: "+admSan.getError());
    			}
    			// CAMBIO PARA INSERTAR UN BLOB
    			RowsContainer rows=new RowsContainer();
    			rows.updateClob(CerSolicitudCertificadosTextoBean.T_NOMBRETABLA," WHERE "+CerSolicitudCertificadosTextoBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+CerSolicitudCertificadosTextoBean.C_IDSOLICITUD+"="+idSolicitud+" AND "+CerSolicitudCertificadosTextoBean.C_IDTEXTO+"="+id,CerSolicitudCertificadosTextoBean.C_TEXTO,beanSolicitudCertificadosTexto.getTexto());
    		}
		
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al actualizar sanciones certificado.");
    	}
    }
    
        
    public String obtenerIdSolicitudDesdeIdCompra(String idSol, String idProducto, String idProductoInstitucion, String idTipoProducto,String idInstitucion) throws ClsExceptions {
    	String salida="";
    	try {
    		Hashtable ht = new Hashtable();
    		ht.put(CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO,idSol);
    		ht.put(CerSolicitudCertificadosBean.C_IDINSTITUCION,idInstitucion);
    		ht.put(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO,idProducto);
    		ht.put(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION,idProductoInstitucion);
    		ht.put(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO,idTipoProducto);
    		
    		Vector v = this.select(ht);
    		if (v!=null && v.size()>0) {
    			CerSolicitudCertificadosBean b = (CerSolicitudCertificadosBean) v.get(0);
    			salida=b.getIdSolicitud().toString();
    		}
			
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener id SOlicitud desde la compra.");
    	}
		return salida;
    }
    
    /**
     * Obtiene datos de la factura del certificado
     * @param hDatos
     * @return
     * @throws ClsExceptions
     */
	private Hashtable<String, Object> getDatosFacturaAsociada (Hashtable<String, Object> hDatos) throws ClsExceptions {
	    try {
		    String sql = "SELECT " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_NUMEROFACTURA + ", " +
		    					FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA +
					        " FROM " + FacFacturaBean.T_NOMBRETABLA + ", " +
					        	PysCompraBean.T_NOMBRETABLA +
					        " WHERE " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION + " = " + UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDINSTITUCION) +
								" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDTIPOPRODUCTO + " = " + UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO) +
								" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTO + " = " + UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO) +
								" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPRODUCTOINSTITUCION + " = " + UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION) +
								" AND " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDPETICION + " = " + UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO) +
					        	" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDINSTITUCION + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDINSTITUCION +
								" AND " + FacFacturaBean.T_NOMBRETABLA + "." + FacFacturaBean.C_IDFACTURA + " = " + PysCompraBean.T_NOMBRETABLA + "." + PysCompraBean.C_IDFACTURA;

		    Hashtable<String, Object> resultado = this.selectGenericoHash(sql);
		    if (resultado!=null)  {
		    	hDatos.put(FacFacturaBean.C_NUMEROFACTURA, UtilidadesHash.getString(resultado, FacFacturaBean.C_NUMEROFACTURA));
		    	hDatos.put(FacFacturaBean.C_IDFACTURA, UtilidadesHash.getString(resultado, FacFacturaBean.C_IDFACTURA));
		    } else  {
		    	hDatos.put(FacFacturaBean.C_NUMEROFACTURA, "");
		    	hDatos.put(FacFacturaBean.C_IDFACTURA, "");
		    }
		    
		} catch (Exception e) {
	        throw new ClsExceptions(e, "Error al obtener el estado de las facturas.");
	    }
	    
		return hDatos;
	}   
	
	public int getNumeroCertificados(String idInstitucion, String idPersona) throws ClsExceptions{
    	
    	StringBuffer sql = new StringBuffer();
		RowsContainer rc = null;
		int id=0;
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql.append("select count(*) CERTIFICADOS from " + CerSolicitudCertificadosBean.T_NOMBRETABLA);
		sql.append(" where " + CerSolicitudCertificadosBean.C_IDINSTITUCION + " = " + idInstitucion);
		sql.append(" and " + CerSolicitudCertificadosBean.C_IDPERSONA_DES + " = " + idPersona);
		
		
		try {		
			if (rc.find(sql.toString())) {						
				Row fila = (Row) rc.get(0);
				id = Integer.valueOf((String)fila.getRow().get("CERTIFICADOS"));														
			}
		}	
 
		catch (ClsExceptions e) {		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en BBDD");		
		}		
		return id;
  
    }
	
	public boolean tieneCertificadosPersonaInstitucion(String idInstitucion, String idPersona, String idTipoProducto, String idProducto, String idProductoInstitucion) throws ClsExceptions {
		boolean correcto = false;
		StringBuffer sql = new StringBuffer();
		RowsContainer rc = new RowsContainer(); 
				
		sql.append("select * from " + CerSolicitudCertificadosBean.T_NOMBRETABLA);
		sql.append(" where " + CerSolicitudCertificadosBean.C_IDINSTITUCION + " = " + idInstitucion);
		sql.append(" and " + CerSolicitudCertificadosBean.C_IDPERSONA_DES + " = " + idPersona);	
		sql.append(" and " + CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO + " = " + idTipoProducto);	
		sql.append(" and " + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO + " = " + idProducto);	
		sql.append(" and " + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION + " = " + idProductoInstitucion);	
	
		try {				
			if (rc.find(sql.toString())) {						
				if(rc != null){
					correcto = true;
				}
			}

		} catch (Exception e) {
			throw new ClsExceptions(e,"Error al buscar los certificado de una persona en una institución. ");
		}

		return correcto;

	}		
	
	/**
	 * Este metodo obtiene los certificados que tiene una persona en una institucion que no sean el pasado como parametro y que esten activos, 
	 * es decir por ejemplo no devuelve los anulados
	 * 
	 * @param idInstitucion
	 * @param idPersona
	 * @param idSolicitud
	 * @return
	 * @throws ClsExceptions
	 */
	public Vector getCertificadosActivosPersonaInstitucionOrdenados(String idInstitucion, String idPersona, String idSolicitud) throws ClsExceptions {
		StringBuffer sql = new StringBuffer();
		sql.append("select Cer.* from ");
		sql.append(CerSolicitudCertificadosBean.T_NOMBRETABLA);
		sql.append(" Cer, ");
		sql.append(PysProductosInstitucionBean.T_NOMBRETABLA);
		sql.append(" Pro");
		sql.append(" where Cer.");
		sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
		sql.append(" = ");
		sql.append(idInstitucion);
		sql.append("   and Cer.");
		sql.append(CerSolicitudCertificadosBean.C_IDPERSONA_DES);
		sql.append(" = ");
		sql.append(idPersona);	
		sql.append("   And Cer.");
		sql.append(CerSolicitudCertificadosBean.C_IDSOLICITUD);
		sql.append(" <> ");
		sql.append(idSolicitud);
		sql.append("   and Cer.");
		sql.append(CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO);
		sql.append(" in ( ");
		sql.append(CerEstadoSoliCertifiAdm.K_ESTADOS_SOL_DESDEAPROBADO);	
		sql.append(" ) ");
		
		sql.append("   and Cer.");
		sql.append(CerSolicitudCertificadosBean.C_IDINSTITUCION);
		sql.append(" = Pro.");
		sql.append(PysProductosInstitucionBean.C_IDINSTITUCION);	
		sql.append("   and Cer.");
		sql.append(CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
		sql.append(" = Pro.");
		sql.append(PysProductosInstitucionBean.C_IDTIPOPRODUCTO);	
		sql.append("   and Cer.");
		sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
		sql.append(" = Pro.");
		sql.append(PysProductosInstitucionBean.C_IDPRODUCTO);	
		sql.append("   and Cer.");
		sql.append(CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
		sql.append(" = Pro.");
		sql.append(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION);
		sql.append(" order by Pro.");
		sql.append(PysProductosInstitucionBean.C_ORDEN);
		
		Vector registros;
		try {
			registros = this.selectGenerico(sql.toString());
		} catch (SIGAException e) {
			throw new ClsExceptions(e,"Error al buscar los certificado de una persona en una institución. ");
		}
		return registros;
	}

	
	public String getContador(String idInstitucion) throws ClsExceptions
	{    	
    	String sql;
		RowsContainer rc = null;		
		String idcontador="SSPP";
		
		try { rc = new RowsContainer(); }
		catch(Exception e) { e.printStackTrace(); }
		
		sql ="SELECT LONGITUDCONTADOR FROM ADM_CONTADOR WHERE IDCONTADOR = '"+idcontador+"' AND IDINSTITUCION = "+idInstitucion;
		
		try 
		{		
			if (rc.findForUpdate(sql)) 
			{						
				Row fila = (Row) rc.get(0);
				idcontador = String.valueOf((String)fila.getRow().get("LONGITUDCONTADOR"));														
			}
		}	 
		catch (ClsExceptions e) 
		{		
			throw new ClsExceptions (e, "Error al ejecutar el 'getNuevoId' en BBDD");		
		}		
		return idcontador;  
    }
	
	
	public boolean isCertNuevaIncorporacion(String idInstitucion, String idProducto, String idTipoProducto, String idProductoInstitucion){
		boolean isCNI=false;
		Properties props = PropertyReader.getProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		String cniProducto = props.getProperty(SIGAConstants.CNI_PRODUCTO);
		String cniTipoProducto = props.getProperty(SIGAConstants.CNI_TIPOPRODUCTO);
		String cniCambioAbogado = props.getProperty(SIGAConstants.CNI_CAMBIOABOGADO);
		String cniNoEjerciente = props.getProperty(SIGAConstants.CNI_NOEJERCIENTE);
		String cniNuevaIncorporacion = props.getProperty(SIGAConstants.CNI_NUEVAINCORPORACION);
		
		if(idInstitucion.equalsIgnoreCase(String.valueOf(ClsConstants.INSTITUCION_CGAE))
			&&	idProducto.equalsIgnoreCase(cniProducto)
			&& idTipoProducto.equalsIgnoreCase(cniTipoProducto)
			&& (idProductoInstitucion.equalsIgnoreCase(cniCambioAbogado)
				|| idProductoInstitucion.equalsIgnoreCase(cniNoEjerciente)
				|| idProductoInstitucion.equalsIgnoreCase(cniNuevaIncorporacion))){
			isCNI=true;
		}
		return isCNI;
	}
	
	/**
	 * Obtiene los datos extras de la pagina de gestion de solicitudes
	 * @param vDatos
	 * @return 0:SinIcono; 1:Descarga; 2:FacturacionRapida
	 * @throws ClsExceptions
	 */
	public Vector<Hashtable<String,Object>> obtenerDatosGestionCertificados (Vector vDatos) throws ClsExceptions {
		Vector<Hashtable<String,Object>> vResultado = new Vector<Hashtable<String,Object>>();
		try {
			for (int i=0; i<vDatos.size(); i++) {
				Row rDatos = (Row)vDatos.get(i);
				Hashtable<String,Object> hDatos = rDatos.getRow();
				
				String sIdInstitucion = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDINSTITUCION);
				String sIdPersona = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDPERSONA_DES);
				String sIdPeticion = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO);
				String sIdTipoProducto = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO);
				String sIdProducto = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO);
				String sIdProductoInstitucion = UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION);
				
				String letrado = CenClienteAdm.getEsLetrado(sIdPersona, sIdInstitucion);
				hDatos.put("LETRADO", letrado);
				
				String cliente = CenClienteAdm.getEsCliente(sIdPersona, sIdInstitucion);
				hDatos.put("ESCLIENTE", cliente);
				
				String estadoSolicitud = UtilidadesHash.getString(hDatos, "ESTADOSOLICITUD");
				estadoSolicitud = UtilidadesMultidioma.getDatoMaestroIdioma(estadoSolicitud, this.usrbean);
				hDatos.put("DESCRIPCION_ESTADOSOLICITUD", estadoSolicitud);
				
				String estadoCertificado = UtilidadesHash.getString(hDatos, "ESTADOCERTIFICADO");
				estadoCertificado = UtilidadesMultidioma.getDatoMaestroIdioma(estadoCertificado, this.usrbean);
				hDatos.put("DESCRIPCION_ESTADOCERTIFICADO", estadoCertificado);
				
				String rutaFicheroLogError = "FICHERO_LOG_ERROR";
				String sRutaBD = getRutaCertificadoDirectorioBD(Integer.parseInt(sIdInstitucion));
				
				String anhoSolicitud=UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_FECHASOLICITUD).substring(0, UtilidadesHash.getString(hDatos, 
						CerSolicitudCertificadosBean.C_FECHASOLICITUD).indexOf("/"));
		        String mesSolicitud=UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_FECHASOLICITUD).substring(UtilidadesHash.getString(hDatos, 
		        		CerSolicitudCertificadosBean.C_FECHASOLICITUD).indexOf("/")+1, UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_FECHASOLICITUD).lastIndexOf("/"));

		        String rutaFinal= sRutaBD + File.separator + sIdInstitucion + File.separator + anhoSolicitud + File.separator + 
		        		mesSolicitud+File.separator+UtilidadesHash.getString(hDatos, CerSolicitudCertificadosBean.C_IDSOLICITUD);
				
		        hDatos.put(rutaFicheroLogError, rutaFinal);
				
				
				
				String sCampo = "TIPO_ICONO";  // 0:SinIcono; 1:Descarga; 2:FacturacionRapida
				if (sIdPeticion==null || sIdPeticion.equals("")) {
					hDatos.put(sCampo, "0"); // Por defecto => aparece sin icono (0)
				} else {
					hDatos = this.getDatosFacturaAsociada(hDatos);
					/* --------------- 1. Obtiene datos para icono - Comprueba si tiene factura -------------------------------- */
					boolean bBuscarIcono = true;
					
					String idFactura = UtilidadesHash.getString(hDatos, FacFacturaBean.C_IDFACTURA);
					if (idFactura!=null && !idFactura.equals("")) { // Contiene factura
						hDatos.put(sCampo, "1"); // Si tiene factura => aparece icono para descarga (1)
						bBuscarIcono = false;
					}
					
					if (bBuscarIcono) {
						/* --------------- 2. Obtiene datos para icono - Comprueba si tiene productos facturables -------------------------------- */					
						String sql = " SELECT COUNT(1) AS " + sCampo +   
								" FROM " + PysProductosSolicitadosBean.T_NOMBRETABLA +
								" WHERE " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDINSTITUCION + " = " + sIdInstitucion +								
									" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDTIPOPRODUCTO + " = " + sIdTipoProducto +
									" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTO + " = " + sIdProducto +
									" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPRODUCTOINSTITUCION + " = " + sIdProductoInstitucion +
									" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_IDPETICION + " = " + sIdPeticion +
									" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_ACEPTADO + " NOT IN ('B', 'D') " +
									" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_NOFACTURABLE + " = 0 ";
						
						Hashtable<String,Object> hResultado = this.selectGenericoHash(sql);
						if (hResultado!=null) {
							String sValorCampo = UtilidadesHash.getString(hResultado, sCampo);
							if (sValorCampo!=null && !sValorCampo.equals("0")) {
								hDatos.put(sCampo, "2"); // Si tiene productos facturables => aparece icono para facturacion rapida (2)
								bBuscarIcono = false;
							}
						}
						
						if (bBuscarIcono) {
							hDatos.put(sCampo, "0"); // Por defecto => aparece sin icono (0)
						}
					}
				}
				
				vResultado.add(hDatos);	
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Erro al obtener los datos en obtenerDatosGestionSolicitud");
		}
		
		return vResultado;
	}	
	
	public boolean actualizarFechaDescargaCertificados(List<CerSolicitudCertificadosBean> cerSolicitudCertificadosBeans){
		boolean exito  = Boolean.TRUE;
		String sql = "";
		String sqlAux = "";
		Row row = new Row();
		
		
		if(cerSolicitudCertificadosBeans.size()>0){
			Iterator<CerSolicitudCertificadosBean> iteratorSolicitudes = cerSolicitudCertificadosBeans.iterator();
			sql =  "update CER_SOLICITUDCERTIFICADOS "+
				"set "+
				" FECHAMODIFICACION = SYSDATE, "+
			    "  USUMODIFICACION = "+cerSolicitudCertificadosBeans.get(0).getUsuMod() +", "+
			    "  FECHADESCARGA = SYSDATE "+
			    "  WHERE (IDINSTITUCION, IDSOLICITUD) IN (";
			    while (iteratorSolicitudes.hasNext()) {
					CerSolicitudCertificadosBean solicitudes = (CerSolicitudCertificadosBean) iteratorSolicitudes.next();
				
					String cadena = solicitudes.getIdInstitucion()+","+solicitudes.getIdSolicitud();
			        int resultado = sqlAux.indexOf(cadena);   
			        if(resultado == -1) {
			        	sqlAux+="("+solicitudes.getIdInstitucion()+","+solicitudes.getIdSolicitud()+"),";	
			        }
			    }
			    sql+= sqlAux; 
			    if(sql!= null){
			    	sql = sql.substring(0, sql.length() - 1);
			    	sql+=")";
					try {
						 row.updateSQL(sql.toString());
					} catch (ClsExceptions e) {
						exito =Boolean.FALSE;
						return exito;
					}
			    }
		
		}
	
		return exito;
	}
}