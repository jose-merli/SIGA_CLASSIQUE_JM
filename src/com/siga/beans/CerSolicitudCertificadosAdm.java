package com.siga.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.GregorianCalendar;
import java.util.Hashtable;
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
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.siga.Utilidades.PaginadorBind;
import com.siga.Utilidades.UtilidadesFecha;
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.administracion.SIGAConstants;
import com.siga.certificados.form.SIGASolicitudesCertificadosForm;
import com.siga.general.SIGAException;

public class CerSolicitudCertificadosAdm extends MasterBeanAdministrador {
	public static final String K_ESTADO_SOL_PEND="1";
	public static final String K_ESTADO_SOL_APROBADO="2";
	public static final String K_ESTADO_SOL_ENVIOP="3";
	public static final String K_ESTADO_SOL_FINALIZADO="4";
	public static final String K_ESTADO_SOL_DENEGADO="5";
	public static final String K_ESTADO_SOL_ANULADO="6";
	public static final String K_ESTADO_CER_INICIAL="1";
	public static final String K_ESTADO_CER_PEND="2";
	public static final String K_ESTADO_CER_GENERADO="3";
	public static final String K_ESTADO_CER_FIRMADO="4";
	public static final String K_ESTADO_CER_ERRORGENERANDO="5";
	
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
		        		   CerSolicitudCertificadosBean.C_USUMODIFICACION,
		        		   CerSolicitudCertificadosBean.C_CBO_CODIGO,
		        		   CerSolicitudCertificadosBean.C_ACEPTACESIONMUTUALIDAD,
		        		   CerSolicitudCertificadosBean.C_CODIGO_SUCURSAL};

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
            String sRutaBD = getRutaCertificadoDirectorioBD(solicitud);

            if (sRutaBD.equals(""))
            {
                throw new SIGAException("messages.general.error.ficheroNoExiste");
            }

            String sRuta = getRutaCertificadoDirectorio(solicitud, sRutaBD);

            File fDirectorio = new File(sRuta);

            fDirectorio.mkdirs();

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

    public File recuperarCertificado(CerSolicitudCertificadosBean solicitud) throws SIGAException
    {
        try
        {
            String sRutaBD = getRutaCertificadoDirectorioBD(solicitud);

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

    public void borrarCertificado(CerSolicitudCertificadosBean solicitud) throws SIGAException
    {
        try
        {
            String sRutaBD = getRutaCertificadoDirectorioBD(solicitud);

            if (sRutaBD.equals(""))
            {
                throw new SIGAException("messages.general.error.ficheroNoExiste");
            }

            File fCertificado = new File(getRutaCertificadoFichero(solicitud, sRutaBD));

            fCertificado.delete();
        }

        catch(Exception e)
        {
            throw new SIGAException("messages.certificados.error.borrarcertificado");
        }
    }

    public String getRutaCertificadoDirectorioBD(CerSolicitudCertificadosBean solicitud)
    {
        String sRuta="";

        try
        {
            GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);

            sRuta = admParametros.getValor(""+solicitud.getIdInstitucion(), "CER" ,"PATH_CERTIFICADOS", "");

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

    /**
     * 
     * @param form
     * @param idInstitucion
     * @return
     * @throws ClsExceptions
     */
	public PaginadorBind buscarSolicitudes(SIGASolicitudesCertificadosForm form, String idInstitucion) throws ClsExceptions {
		try {
			String sql = "SELECT " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_FECHASOLICITUD + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_FECHAESTADO + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_FECHACOBRO + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCION + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDSOLICITUD + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDPERSONA_DES + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO + ", " +
							CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO + ", " +
							PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_TIPOCERTIFICADO + " AS TIPOCERTIFICADO2, " +					
							PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_DESCRIPCION + " AS TIPOCERTIFICADO, " +
							CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1 + " || ' ' || " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS2 + " || ', ' || " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE + " AS CLIENTE, " +					
							"INSO." + CenInstitucionBean.C_ABREVIATURA + " AS INSTITUCIONORIGEN, " +
							"INSD." + CenInstitucionBean.C_ABREVIATURA + " AS INSTITUCIONDESTINO, " +					 
							CerEstadoSoliCertifiBean.T_NOMBRETABLA + "." + CerEstadoSoliCertifiBean.C_DESCRIPCION + " AS ESTADOSOLICITUD, " + 					
							CerEstadoCertificadoBean.T_NOMBRETABLA + "." + CerEstadoCertificadoBean.C_DESCRIPCION + " AS ESTADOCERTIFICADO " +
						" FROM " + CerSolicitudCertificadosBean.T_NOMBRETABLA + ", " + 
							PysProductosInstitucionBean.T_NOMBRETABLA + ", " + 
							CenPersonaBean.T_NOMBRETABLA + ", " +
							CenInstitucionBean.T_NOMBRETABLA + " INSO, " +
							CenInstitucionBean.T_NOMBRETABLA + " INSD, " +
							CerEstadoSoliCertifiBean.T_NOMBRETABLA + ", " +
							CerEstadoCertificadoBean.T_NOMBRETABLA;

			String numeroColegiado = form.getNumeroCertificado();
			if (numeroColegiado != null && !numeroColegiado.equals("")) {
				sql += ", " + CenColegiadoBean.T_NOMBRETABLA;
			}

			sql += " WHERE " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCION + " = " + idInstitucion +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDPERSONA_DES + " = " + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_IDPERSONA +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCION + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTO +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION + " = " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN + " = INSO." + CenInstitucionBean.C_IDINSTITUCION + "(+)" +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO + " = INSD." + CenInstitucionBean.C_IDINSTITUCION + "(+)" +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO + " = " + CerEstadoSoliCertifiBean.T_NOMBRETABLA + "." + CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO +
					" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO + " = " + CerEstadoCertificadoBean.T_NOMBRETABLA + "." + CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO;
			
			String fDesde = form.getFechaDesde(); 
			String fHasta = form.getFechaHasta(); 
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				fDesde = UtilidadesFecha.getFechaApruebaDeFormato(fDesde);
				fHasta = UtilidadesFecha.getFechaApruebaDeFormato(fHasta);
				sql += " AND " + GstDate.dateBetweenDesdeAndHasta(CerSolicitudCertificadosBean.C_FECHAESTADO, fDesde, fHasta);
			}	
			
			fDesde = form.getFechaEmisionDesde(); 
			fHasta = form.getFechaEmisionHasta(); 
			if ((fDesde!=null && !fDesde.trim().equals("")) || (fHasta!=null && !fHasta.trim().equals(""))) {
				fDesde = UtilidadesFecha.getFechaApruebaDeFormato(fDesde);
				fHasta = UtilidadesFecha.getFechaApruebaDeFormato(fHasta);
				sql += " AND " + GstDate.dateBetweenDesdeAndHasta(CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO, fDesde, fHasta);
			}	

			String estadoSolicitud = form.getEstado();
			if (estadoSolicitud!=null && !estadoSolicitud.equals("") && !estadoSolicitud.equals("99")) {
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO + " = " + estadoSolicitud;
				
			} else if (estadoSolicitud!=null && estadoSolicitud.equals("99")) {
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_FECHAENVIO + " IS NULL";
			}

			String tipoCertificado = form.getTipoCertificado();
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
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO + " = " + idTipoProducto;
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO + " = " + idProducto;
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION + " = " + idProductoInstitucion;
			}

			if (numeroColegiado!=null && !numeroColegiado.equals("")) {
				sql += " AND " + ComodinBusquedas.tratarNumeroColegiado(numeroColegiado, CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_NCOLEGIADO) +
						" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCION + " = " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDINSTITUCION + 
						" AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDPERSONA_DES + " = " + CenColegiadoBean.T_NOMBRETABLA + "." + CenColegiadoBean.C_IDPERSONA;
			}
			
			// Consulta sobre el campo NIF/CIF, si el usuario mete comodines la b�squeda es como se hac�a siempre, en el caso de no meter comodines se ha creado un nuevo metodo ComodinBusquedas.preparaCadenaNIFSinComodin para que monte la consulta adecuada.
			String sCifNif = form.getCIFNIF();
			if (sCifNif!=null && !sCifNif.trim().equals("")) {
				if (ComodinBusquedas.hasComodin(sCifNif)){	
					sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(sCifNif.trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF);
				} else {
					sql += " AND " + ComodinBusquedas.prepararSentenciaNIF(sCifNif, " UPPER(" + CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NIFCIF + ") ");
				}
			}			
			
			String nombre = form.getNombre();
			if (nombre!=null && !nombre.trim().equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(nombre.trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_NOMBRE);
			}
			
			String apellido1 = form.getApellido1();
			if (apellido1!=null && !apellido1.trim().equals("")) {
				sql += " AND " + ComodinBusquedas.prepararSentenciaCompleta(apellido1.trim(), CenPersonaBean.T_NOMBRETABLA + "." + CenPersonaBean.C_APELLIDOS1);
			}
			
			String idInstitucionOrigen = form.getIdInstitucionOrigen();
			if (idInstitucionOrigen!=null && !idInstitucionOrigen.equals("")) {
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN + " = " + idInstitucionOrigen;
			}

			String idInstitucionDestino = form.getIdInstitucionDestino();
			if (idInstitucionDestino!=null && !idInstitucionDestino.equals("")) {
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO + " = " + idInstitucionDestino;
			}
			
			String idSolicitud = form.getBuscarIdSolicitudCertif();
			if (idSolicitud!=null && !idSolicitud.equals("")) {
				sql += " AND " + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_IDSOLICITUD + " = " + idSolicitud;
			}

			String numeroCertificado = form.getBuscarNumCertificadoCompra();
			if (numeroCertificado!=null && !numeroCertificado.equals("")) {
				sql += " AND ( " +
								ComodinBusquedas.prepararSentenciaCompleta(numeroCertificado.trim(), 
									" NVL(" + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PREFIJO_CER + ", '') || " +
									" NVL(" + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_CONTADOR_CER + ", '') || " +
									" NVL(" + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_SUFIJO_CER + ", '') ") +
							" OR " +
								ComodinBusquedas.prepararSentenciaCompleta(numeroCertificado.trim(),
									" NVL(" + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_PREFIJO_CER + ",'') || " +
									" NVL(LPAD(" + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_CONTADOR_CER + ", (" +
											" SELECT NVL(" + AdmContadorBean.T_NOMBRETABLA + "." + AdmContadorBean.C_LONGITUDCONTADOR + ", 1) " +
											" FROM " + AdmContadorBean.T_NOMBRETABLA + 
											" WHERE " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDCONTADOR + " = " + AdmContadorBean.T_NOMBRETABLA + "." + AdmContadorBean.C_IDCONTADOR +
													" AND " + PysProductosInstitucionBean.T_NOMBRETABLA + "." + PysProductosInstitucionBean.C_IDINSTITUCION + " = " + AdmContadorBean.T_NOMBRETABLA + "." + AdmContadorBean.C_IDINSTITUCION + 
										"), '0'), '') || " +
									" NVL(" + CerSolicitudCertificadosBean.T_NOMBRETABLA + "." + CerSolicitudCertificadosBean.C_SUFIJO_CER + ", '')") +
							" ) ";
			}

			sql += " ORDER BY " + CerSolicitudCertificadosBean.C_IDSOLICITUD + " DESC ";

			PaginadorBind paginador = new PaginadorBind(sql, new Hashtable());

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
     * Funci�n que inserta solicitudes de certificado 
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
				// CGAE �
				// se crea para el consjeo que lo est� haciendo sea CGAE o
				// auton�mico.

				// Petici�n para el CGAE
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
				solicConsejoBean.setIdEstadoSolicitudCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND));
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
				solicConsejoBean.setIdEstadoCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.K_ESTADO_CER_INICIAL));

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

					// Petici�n para el colegio
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
						solicColegioBean.setIdEstadoSolicitudCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND));
						solicColegioBean.setDescripcion(descripcion);
						solicColegioBean.setFechaSolicitud("SYSDATE");
						solicColegioBean.setFechaEstado("SYSDATE");
						solicColegioBean.setIdInstitucion_Sol(Integer.valueOf(idInstitucion));
						solicColegioBean.setIdEstadoCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.K_ESTADO_CER_INICIAL));
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
						String estado = instAdm.tieneSIGA(Integer.valueOf(idInstOrigen)) ? CerSolicitudCertificadosAdm.K_ESTADO_SOL_FINALIZADO
								: CerSolicitudCertificadosAdm.K_ESTADO_SOL_PEND;
						solicBean.setIdEstadoSolicitudCertificado(Integer.valueOf(estado));
						solicBean.setFechaSolicitud("SYSDATE");
						solicBean.setFechaEstado("SYSDATE");
						solicBean.setIdInstitucion_Sol(Integer.valueOf(idInstitucion));
						solicBean.setIdEstadoCertificado(Integer.valueOf(CerSolicitudCertificadosAdm.K_ESTADO_CER_INICIAL));
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
 
 	public boolean firmarPDF(String idSolicitud, String idInstitucion)
 	{
        FileInputStream fisID = null;
        FileOutputStream fos = null;
        try
 		{
	    	GregorianCalendar gcFecha = new GregorianCalendar();
	        
            GenParametrosAdm admParametros = new GenParametrosAdm(this.usrbean);

            String sPathCertificados = admParametros.getValor(idInstitucion, "CER", "PATH_CERTIFICADOS", "");
            String sPathCertificadosDigitales = admParametros.getValor(idInstitucion, "CER", "PATH_CERTIFICADOS_DIGITALES", "");
            String sNombreCertificadosDigitales = admParametros.getValor(idInstitucion, "CER", "NOMBRE_CERTIFICADOS_DIGITALES", "");
            String sClave = admParametros.getValor(idInstitucion, "CER", "CLAVE_CERTIFICADOS_DIGITALES", "");
            boolean tieneParametro = admParametros.tieneParametro(idInstitucion, "CER", "CLAVE_CERTIFICADOS_DIGITALES");
            String sIDDigital =""; 
            if (tieneParametro) {
                sIDDigital = sPathCertificadosDigitales + File.separator + idInstitucion + File.separator + sNombreCertificadosDigitales;
            } else {
                sIDDigital = sPathCertificadosDigitales + File.separator + sNombreCertificadosDigitales;
            }

            CerSolicitudCertificadosAdm admSolicitud = new CerSolicitudCertificadosAdm(this.usrbean);
            
            Hashtable htSolicitud = new Hashtable();
            htSolicitud.put(CerSolicitudCertificadosBean.C_IDINSTITUCION, idInstitucion);
            htSolicitud.put(CerSolicitudCertificadosBean.C_IDSOLICITUD, idSolicitud);
            
            Vector vSolicitud = admSolicitud.selectByPK(htSolicitud);
            
            CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean)vSolicitud.elementAt(0);
            
            String sNombreFicheroEntrada = getRutaCertificadoDirectorio(beanSolicitud, sPathCertificados);
            sNombreFicheroEntrada = getRutaCertificadoFichero(beanSolicitud, sPathCertificados);
            
            String sNombreFicheroSalida = sNombreFicheroEntrada + ".tmp";

            File fOut = new File(sNombreFicheroSalida);
            File fIn = new File(sNombreFicheroEntrada);

	        fisID = new FileInputStream(sIDDigital);
	        KeyStore ks = KeyStore.getInstance("PKCS12");
	        ks.load(fisID, sClave.toCharArray());

	        fisID.close();

	        
	        String sAlias = (String)ks.aliases().nextElement();
	
	        PrivateKey pKey = (PrivateKey)ks.getKey(sAlias, sClave.toCharArray());
	        
	        Certificate[] aCertificados = ks.getCertificateChain(sAlias);
	        
	        PdfReader reader = new PdfReader(sNombreFicheroEntrada);
	        
	        fos = new FileOutputStream(sNombreFicheroSalida);
	        
	        PdfStamper stamper = PdfStamper.createSignature(reader, fos, '\0');
	        PdfSignatureAppearance psa = stamper.getSignatureAppearance();
	        
	        psa.setCrypto(pKey, aCertificados, null, PdfSignatureAppearance.WINCER_SIGNED);
	        
	        psa.setSignDate(gcFecha);

	        stamper.close();
	        fos.close();
	        
	        fIn.delete();
	        
	        fOut.renameTo(fIn);

 			return true;
 		}
 		
 		catch(Exception e)
 		{
 			ClsLogging.writeFileLog("***************** ERROR DE FIRMA DIGITAL EN DOCUMENTO *************************", 3);
 			ClsLogging.writeFileLog("Error al FIRMAR el PDF de la Solicitud: " + idSolicitud + " IdInstitucion: "+idInstitucion, 3);
 			ClsLogging.writeFileLog("*******************************************************************************", 3);
	        e.printStackTrace();
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
/*
    public String obtenerTextoHistoricoParaCertificado(String idInstitucion, String idPersona, UsrBean user) throws ClsExceptions 
	{
    	String salida = "";
    	try {
    		String sql = " SELECT TO_CHAR (d." + CenDatosColegialesEstadoBean.C_FECHAESTADO + ",'dd-mm-yyyy') FECHA, " + UtilidadesMultidioma.getCampoMultidiomaSimple("e." + CenEstadoColegialBean.C_DESCRIPCION, user.getLanguage()) + " DESCRIPCION " + 
    				       " FROM " + CenDatosColegialesEstadoBean.T_NOMBRETABLA + " d, " + CenEstadoColegialBean.T_NOMBRETABLA + " e " +
    				      " WHERE " + CenDatosColegialesEstadoBean.C_IDPERSONA + " = " + idPersona +
    				        " AND " + CenDatosColegialesEstadoBean.C_IDINSTITUCION + " = " + idInstitucion +
    				        " AND d." + CenDatosColegialesEstadoBean.C_IDESTADO + " = e." + CenEstadoColegialBean.C_IDESTADO +
						  " ORDER BY d." + CenDatosColegialesEstadoBean.C_FECHAESTADO + " ASC ";
    			// NO SE METE LA CONDICION DE QUE  "AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <= SYSDATE"
    			// PARA QUE SALGAN TODOS, INCLUSO LOS FUTUROS.
    		
    		Vector v = this.selectGenerico(sql);
    		for (int i = 0; i < v.size(); i++) {
    			Hashtable h = (Hashtable) v.get(i);
    			
    			salida += UtilidadesHash.getString(h, "FECHA") + " " + 
				          UtilidadesString.getMensajeIdioma(user, "certificados.solicitudes.literal.copiarHistorico.pasaA") + " " + 
						  UtilidadesHash.getString(h, "DESCRIPCION") + "\n\r"; 
    		}
    		
    		if (salida.length() > 1) salida = salida.substring(0, salida.length() - 1);
    		
    		salida = UtilidadesString.escape(salida);
    	} 
    	catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener Historico.");
    	}
		return salida;
    }*/
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
	//  NO SE METE LA CONDICION DE QUE "AND CEN_DATOSCOLEGIALESESTADO.FECHAESTADO <=
	//  SYSDATE"
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
			throw new ClsExceptions(e,"Error al buscar los certificado de una persona en una instituci�n. ");
		}

		return correcto;

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
				
				hDatos = this.getDatosFacturaAsociada(hDatos);
				
				String letrado = CenClienteAdm.getEsLetrado(sIdPersona, sIdInstitucion);
				hDatos.put("LETRADO", letrado);
				
				String cliente = CenClienteAdm.getEsCliente(sIdPersona, sIdInstitucion);
				hDatos.put("CLIENTE", cliente);
				
				String estadoSolicitud = UtilidadesHash.getString(hDatos, "ESTADOSOLICITUD");
				estadoSolicitud = UtilidadesMultidioma.getDatoMaestroIdioma(estadoSolicitud, this.usrbean);
				hDatos.put("DESCRIPCION_ESTADOSOLICITUD", estadoSolicitud);
				
				String estadoCertificado = UtilidadesHash.getString(hDatos, "ESTADOCERTIFICADO");
				estadoCertificado = UtilidadesMultidioma.getDatoMaestroIdioma(estadoCertificado, this.usrbean);
				hDatos.put("DESCRIPCION_ESTADOCERTIFICADO", estadoCertificado);
				
				/* --------------- 1. Obtiene datos para icono - Comprueba si tiene factura -------------------------------- */
				boolean bBuscarIcono = true;
				String sCampo = "TIPO_ICONO";  // 0:SinIcono; 1:Descarga; 2:FacturacionRapida
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
								" AND " + PysProductosSolicitadosBean.T_NOMBRETABLA + "." + PysProductosSolicitadosBean.C_ACEPTADO + " NOT IN ('B', 'D', 'P') " +
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
				
				vResultado.add(hDatos);	
			}
			
		} catch (Exception e) {
			throw new ClsExceptions (e, "Erro al obtener los datos en obtenerDatosGestionSolicitud");
		}
		
		return vResultado;
	}		
}