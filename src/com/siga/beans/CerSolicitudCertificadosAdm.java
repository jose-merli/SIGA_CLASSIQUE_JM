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
import java.util.StringTokenizer;
import java.util.Vector;

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
import com.siga.Utilidades.UtilidadesHash;
import com.siga.Utilidades.UtilidadesMultidioma;
import com.siga.Utilidades.UtilidadesString;
import com.siga.general.SIGAException;

public class CerSolicitudCertificadosAdm extends MasterBeanAdministrador
{
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
		        		   CerSolicitudCertificadosBean.C_USUMODIFICACION};

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
		        		   CerSolicitudCertificadosBean.C_USUCREACION};

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
			bean.setUsuMod(UtilidadesHash.getInteger(hash, CerSolicitudCertificadosBean.C_USUCREACION));
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

	public PaginadorBind buscarSolicitudes(String idInstitucion, String fechaDesde, String fechaHasta, String fechaEmisionDesde, 
			String fechaEmisionHasta, String estadoSolicitud, String tipoCertificado, String numeroColegiado, String CIFNIF,
			String nombre, String apellido1, String idInstitucionOrigen, String idInstitucionDestino, String idSolicitud,
			String numeroCertificado) throws ClsExceptions, SIGAException {

		try {
			Hashtable codigos = new Hashtable();
			int contador = 0;
			int totalRegistros = 0;

			String sSQL = " SELECT     " + "SOL." + CerSolicitudCertificadosBean.C_FECHASOLICITUD + " AS FECHA, "
					+ "SOL." + CerSolicitudCertificadosBean.C_FECHAESTADO + " AS FECHAESTADO, "
					+ "SOL." + CerSolicitudCertificadosBean.C_FECHADESCARGA + " AS FECHADESCARGA, "
					+ "SOL." + CerSolicitudCertificadosBean.C_FECHACOBRO + " AS FECHACOBRO, "
					+ "SOL." + CerSolicitudCertificadosBean.C_FECHAENVIO + " AS FECHAENVIO, "
					+ "SOL." + CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO + " AS FECHAEMISION, "
					+ "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCION + ", "
					+ "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO + ", "
					+ "SOL." + CerSolicitudCertificadosBean.C_IDSOLICITUD + ", "
					+ "SOL." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO + ", "
					+ "SOL." + CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO + ", "
					+ "SOL." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION + ", "
					+ "PR." + PysProductosInstitucionBean.C_TIPOCERTIFICADO + " AS TIPOCERTIFICADO2, "
					+ "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN + ", "
					+ "PR." + PysProductosInstitucionBean.C_DESCRIPCION + " AS TIPOCERTIFICADO, "
					+ "PER." + CenPersonaBean.C_APELLIDOS1 + "||' '||PER." + CenPersonaBean.C_APELLIDOS2 + "||', '||PER." + CenPersonaBean.C_NOMBRE + " AS CLIENTE, "
					+ "SOL." + CerSolicitudCertificadosBean.C_IDPERSONA_DES + " AS IDPERSONA, "
					+ "INSO." + CenInstitucionBean.C_ABREVIATURA + " AS INSTITUCIONORIGEN, "
					+ "INSD." + CenInstitucionBean.C_ABREVIATURA + " AS INSTITUCIONDESTINO, "
					+ "SOL." + CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO + ", " 
					+ "ES." + CerEstadoSoliCertifiBean.C_DESCRIPCION + " AS ESTADOSOLICITUD, " 
					+ "SOL." + CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO + ", "
					+ "EC." + CerEstadoCertificadoBean.C_DESCRIPCION + " AS ESTADOCERTIFICADO, " 
					+ "SOL." + CerSolicitudCertificadosBean.C_IDPETICIONPRODUCTO + " AS IDPETICION" + ", "
					+ "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCION_SOL + " AS IDINSTITUCION_SOL ";

			sSQL += " FROM " + CerSolicitudCertificadosBean.T_NOMBRETABLA + " SOL, " 
					+ PysProductosInstitucionBean.T_NOMBRETABLA + " PR, " 
					+ CenPersonaBean.T_NOMBRETABLA + " PER, "
					+ CenInstitucionBean.T_NOMBRETABLA + " INSO, "
					+ CenInstitucionBean.T_NOMBRETABLA + " INSD, "
					+ CerEstadoCertificadoBean.T_NOMBRETABLA + " EC, "
					+ CerEstadoSoliCertifiBean.T_NOMBRETABLA + " ES ";

			if (numeroColegiado != null && !numeroColegiado.equals("")) {
				sSQL += ", " + CenColegiadoBean.T_NOMBRETABLA + " COL ";
			}

			sSQL += " WHERE ";
			contador++;
			codigos.put(new Integer(contador), idInstitucion);
			sSQL += "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCION + "=:" + contador
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_IDPERSONA_DES + "=PER." + CenPersonaBean.C_IDPERSONA
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCION + "=PR." + PysProductosInstitucionBean.C_IDINSTITUCION
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO + "=PR." + PysProductosInstitucionBean.C_IDPRODUCTO
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO + "=PR." + PysProductosInstitucionBean.C_IDTIPOPRODUCTO
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION + "=PR." + PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN + "=INSO." + CenInstitucionBean.C_IDINSTITUCION + "(+)"
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO + "=INSD." + CenInstitucionBean.C_IDINSTITUCION + "(+)"
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO + "=ES." + CerEstadoSoliCertifiBean.C_IDESTADOSOLICITUDCERTIFICADO
					+ " AND " + "SOL." + CerSolicitudCertificadosBean.C_IDESTADOCERTIFICADO + "=EC." + CerEstadoCertificadoBean.C_IDESTADOCERTIFICADO;

			if (fechaDesde != null && !fechaDesde.equals("")
					|| fechaHasta != null && !fechaHasta.equals("")) {
				// -------------- dateBetweenDesdeAndHastaBind
				Vector v = GstDate.dateBetweenDesdeAndHastaBind(
						CerSolicitudCertificadosBean.C_FECHAESTADO,
						fechaDesde != null && !fechaDesde.equals("") ? GstDate.getApplicationFormatDate("", fechaDesde) : null, 
						fechaHasta != null && !fechaHasta.equals("") ? GstDate.getApplicationFormatDate("", fechaHasta) : null, contador, codigos);
				Integer in = (Integer) v.get(0);
				String st = (String) v.get(1);
				contador = in.intValue();
				// --------------
				sSQL += " AND " + st;
			}
			if (fechaEmisionDesde != null && !fechaEmisionDesde.equals("")
					|| fechaEmisionHasta != null && !fechaEmisionHasta.equals("")) {
				// -------------- dateBetweenDesdeAndHastaBind
				Vector v = GstDate.dateBetweenDesdeAndHastaBind(
						CerSolicitudCertificadosBean.C_FECHAEMISIONCERTIFICADO,
						fechaEmisionDesde != null && !fechaEmisionDesde.equals("") ? GstDate.getApplicationFormatDate("", fechaEmisionDesde) : null, 
						fechaEmisionHasta != null && !fechaEmisionHasta.equals("") ? GstDate.getApplicationFormatDate("", fechaEmisionHasta) : null, contador, codigos);
				Integer in = (Integer) v.get(0);
				String st = (String) v.get(1);
				contador = in.intValue();
				// --------------
				sSQL += " AND " + st;
			}
			if ((estadoSolicitud != null && !estadoSolicitud.equals("") && !estadoSolicitud
					.equals("99"))) {
				contador++;
				codigos.put(new Integer(contador), estadoSolicitud);
				sSQL += " AND SOL." + CerSolicitudCertificadosBean.C_IDESTADOSOLICITUDCERTIFICADO + "=:" + contador;
			}
			if ((estadoSolicitud != null && !estadoSolicitud.equals("") && estadoSolicitud .equals("99"))) {
				sSQL += " AND SOL." + CerSolicitudCertificadosBean.C_FECHAENVIO + " IS NULL";
			}

			// RGG CAMBIO PAR BUSCAR POR ID PRODUCTO
			if (tipoCertificado != null && !tipoCertificado.equals("")) {

				String idTipoProducto = "";
				String idProducto = "";
				String idProductoInstitucion = "";
				String codigo = tipoCertificado;
				StringTokenizer st = new StringTokenizer(codigo, ",");
				if (st.hasMoreElements()) {
					idTipoProducto = (String) st.nextElement();
					idProducto = (String) st.nextElement();
					idProductoInstitucion = (String) st.nextElement();
				}
				contador++;
				codigos.put(new Integer(contador), idTipoProducto);
				sSQL += " AND (SOL."
						+ CerSolicitudCertificadosBean.C_PPN_IDTIPOPRODUCTO
						+ "=:" + contador;
				contador++;
				codigos.put(new Integer(contador), idProducto);
				sSQL += " AND SOL."
						+ CerSolicitudCertificadosBean.C_PPN_IDPRODUCTO + "=:"
						+ contador;
				contador++;
				codigos.put(new Integer(contador), idProductoInstitucion);
				sSQL += " AND SOL."
						+ CerSolicitudCertificadosBean.C_PPN_IDPRODUCTOINSTITUCION
						+ "=:" + contador + ") ";
			}

			if (numeroColegiado != null && !numeroColegiado.equals("")) {
				sSQL += " AND "
						+ ComodinBusquedas.tratarNumeroColegiado(
								numeroColegiado, "COL."
										+ CenColegiadoBean.C_NCOLEGIADO)
						+ " AND SOL."
						+ CerSolicitudCertificadosBean.C_IDINSTITUCION
						+ "=COL." + CenColegiadoBean.C_IDINSTITUCION
						+ " AND SOL."
						+ CerSolicitudCertificadosBean.C_IDPERSONA_DES
						+ "=COL." + CenColegiadoBean.C_IDPERSONA;
			}

			// Consulta sobre el campo NIF/CIF, si el usuario mete comodines la
			// búsqueda es como se hacía siempre, en el caso
			// de no meter comodines se ha creado un nuevo metodo
			// ComodinBusquedas.preparaCadenaNIFSinComodin para que monte
			// la consulta adecuada.
			if (CIFNIF != null && !CIFNIF.equals("")) {
				if (ComodinBusquedas.hasComodin(CIFNIF)) {
					contador++;
					sSQL += " AND "
							+ ComodinBusquedas.prepararSentenciaCompletaBind(
									CIFNIF.trim(), "PER."
											+ CenPersonaBean.C_NIFCIF,
									contador, codigos);
				} else {
					contador++;
					sSQL += " AND "
							+ ComodinBusquedas.prepararSentenciaNIFBind(CIFNIF,
									"PER." + CenPersonaBean.C_NIFCIF, contador,
									codigos);
				}
			}

			if (nombre != null && !nombre.equals("")) {
				contador++;
				sSQL += " AND "
						+ ComodinBusquedas.prepararSentenciaCompletaBind(nombre
								.trim(), "PER." + CenPersonaBean.C_NOMBRE,
								contador, codigos);
			}

			// cambio en criterios de busqueda (Apellidos1 representa a los dos
			// apellidos)
			if (apellido1 != null && !apellido1.equals("")) {
				contador++;
				sSQL += " AND EXISTS (SELECT 'X' FROM cen_persona per1 where  per.idpersona = per1.idpersona AND ( ";
				sSQL += ComodinBusquedas.prepararSentenciaCompletaBind(
						apellido1.trim(),
						"per1." + CenPersonaBean.C_APELLIDOS1, contador,
						codigos);
				contador++;
				sSQL += " OR "
						+ ComodinBusquedas.prepararSentenciaCompletaBind(
								apellido1.trim(), "per1."
										+ CenPersonaBean.C_APELLIDOS2,
								contador, codigos);
				contador++;
				sSQL += " OR "
						+ ComodinBusquedas.prepararSentenciaCompletaBind(
								apellido1.replaceAll(" ", ""), "replace(per1."
										+ CenPersonaBean.C_APELLIDOS1
										+ ",' ','')||replace(per1."
										+ CenPersonaBean.C_APELLIDOS2
										+ ",' ','')", contador, codigos);
				sSQL += "))";

			}

			if (idSolicitud != null && !idSolicitud.equals("")) {
				contador++;
				codigos.put(new Integer(contador), idSolicitud);
				sSQL += " AND SOL."
						+ CerSolicitudCertificadosBean.C_IDSOLICITUD + "=:"
						+ contador;

			}

			if (idInstitucionOrigen != null && !idInstitucionOrigen.equals("")) {
				contador++;
				codigos.put(new Integer(contador), idInstitucionOrigen);
				sSQL += " AND SOL."
						+ CerSolicitudCertificadosBean.C_IDINSTITUCIONORIGEN
						+ "=:" + contador;

			}

			if (idInstitucionDestino != null
					&& !idInstitucionDestino.equals("")) {
				contador++;
				codigos.put(new Integer(contador), idInstitucionDestino);
				sSQL += " AND SOL."
						+ CerSolicitudCertificadosBean.C_IDINSTITUCIONDESTINO
						+ "=:" + contador;
			}

			// El contador busca concatenando los tres campos del contador, sin
			// formatear.x
			if (numeroCertificado != null && !numeroCertificado.equals("")) {
				contador++;
				sSQL += " AND ( ("
						+ ComodinBusquedas
								.prepararSentenciaCompletaBind(
										numeroCertificado.trim(),
										"(NVL(SOL."
												+ CerSolicitudCertificadosBean.C_PREFIJO_CER
												+ ",'') || NVL(SOL."
												+ CerSolicitudCertificadosBean.C_CONTADOR_CER
												+ ",'') || NVL(SOL."
												+ CerSolicitudCertificadosBean.C_SUFIJO_CER
												+ ",''))", contador, codigos);
				sSQL += ") ";
				contador++;
				sSQL += " OR ("
						+ ComodinBusquedas
								.prepararSentenciaCompletaBind(
										numeroCertificado.trim(),
										"(NVL(SOL."
												+ CerSolicitudCertificadosBean.C_PREFIJO_CER
												+ ",'') || NVL(LPAD (SOL."
												+ CerSolicitudCertificadosBean.C_CONTADOR_CER
												+ ",(select nvl("
												+ AdmContadorBean.C_LONGITUDCONTADOR
												+ ", 1) FROM "
												+ AdmContadorBean.T_NOMBRETABLA
												+ " WHERE PR."
												+ PysProductosInstitucionBean.C_IDCONTADOR
												+ " = "
												+ AdmContadorBean.C_IDCONTADOR
												+ " AND PR."
												+ PysProductosInstitucionBean.C_IDINSTITUCION
												+ " = "
												+ AdmContadorBean.C_IDINSTITUCION
												+ ") , '0'),'') || NVL(SOL."
												+ CerSolicitudCertificadosBean.C_SUFIJO_CER
												+ ",''))", contador, codigos);
				sSQL += ") " + " )";
			}

			// RGG cambio de orden
			sSQL += " ORDER BY "
					+ CerSolicitudCertificadosBean.C_IDSOLICITUD + " DESC ";

			PaginadorBind paginador = new PaginadorBind(sSQL, codigos);
			totalRegistros = paginador.getNumeroTotalRegistros();

			if (totalRegistros == 0) {
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
		int contador = 0;
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
        RowsContainer rows=new RowsContainer();
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
        RowsContainer rows=new RowsContainer();
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
        		CenClienteBean beanCli = null;
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
    
    public String getTextoCertificado(String idInstitucion, String idSolicitud) throws ClsExceptions {
    	String salida="";
		RowsContainer rows=new RowsContainer();
    	try {
    		CerSolicitudCertificadosTextoAdm admTx= new CerSolicitudCertificadosTextoAdm(this.usrbean);
    		Hashtable ht = new Hashtable();
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDINSTITUCION,idInstitucion);
    		ht.put(CerSolicitudCertificadosTextoBean.C_IDSOLICITUD,idSolicitud);
    		Vector v = admTx.select(ht);
    		if (v!=null && v.size()>0) {
    			CerSolicitudCertificadosTextoBean b = (CerSolicitudCertificadosTextoBean) v.get(0); 
    			//salida = b.getTexto();

    			// CAMBIO PARA OBTENER UN BLOB
    			salida = rows.getClob(CerSolicitudCertificadosTextoBean.T_NOMBRETABLA,CerSolicitudCertificadosTextoBean.C_TEXTO," SELECT "+CerSolicitudCertificadosTextoBean.C_TEXTO+" FROM " + CerSolicitudCertificadosTextoBean.T_NOMBRETABLA + " WHERE "+CerSolicitudCertificadosTextoBean.C_IDINSTITUCION+"="+idInstitucion+" AND "+CerSolicitudCertificadosTextoBean.C_IDSOLICITUD+"="+idSolicitud+" AND "+CerSolicitudCertificadosTextoBean.C_IDTEXTO+"="+b.getIdTexto());
    		}
			
    	} catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener el Texto del certificado.");
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
    
    public PysCompraBean obtenerCompra(String idInstitucion, String idSolicitudCertificado) throws ClsExceptions 
	{
        PysCompraBean salida = null;
    	try {
    		String sql = " select com.idinstitucion IDINSTITUCION, com.idpeticion IDPETICION, com.idtipoproducto IDTIPOPRODUCTO, com.idproducto IDPRODUCTO, com.idproductoinstitucion IDPRODUCTOINSTITUCION " +
    		    	" from cer_solicitudcertificados cer, pys_peticioncomprasuscripcion pet, pys_compra com " +
		    	" where pet.idinstitucion=com.idinstitucion " +
		    	" and pet.idpeticion = com.idpeticion " +
		    	" and com.idtipoproducto = cer.ppn_idtipoproducto " +
		    	" and com.idproducto = cer.ppn_idproducto " +
		    	" and com.idproductoinstitucion = cer.ppn_idproductoinstitucion " +
		    	" and pet.idinstitucion=cer.idinstitucion " +
		    	" and pet.idpeticion=cer.idpeticionproducto " +
		    	" and cer.idinstitucion=" + idInstitucion + 
		    	" and cer.idsolicitud=" + idSolicitudCertificado;
		    	
    		Vector v = this.selectGenerico(sql);
    		if (v.size()>0) {
    			Hashtable h = (Hashtable) v.get(0);
    			Hashtable ht = new Hashtable();
        		ht.put(PysCompraBean.C_IDINSTITUCION,UtilidadesHash.getString(h, "IDINSTITUCION"));
        		ht.put(PysCompraBean.C_IDPETICION,UtilidadesHash.getString(h, "IDPETICION"));
        		ht.put(PysCompraBean.C_IDPRODUCTO,UtilidadesHash.getString(h, "IDPRODUCTO"));
        		ht.put(PysCompraBean.C_IDPRODUCTOINSTITUCION,UtilidadesHash.getString(h, "IDPRODUCTOINSTITUCION"));
        		ht.put(PysCompraBean.C_IDTIPOPRODUCTO,UtilidadesHash.getString(h, "IDTIPOPRODUCTO"));
        		PysCompraAdm admCom = new PysCompraAdm(this.usrbean);
        		Vector  v2 = admCom.selectByPK(ht);
        		if (v2!=null && v2.size()>0) {
        		    salida = (PysCompraBean)v2.get(0);
        		}
    		}

    	} 
    	catch (Exception e) {
    		throw new ClsExceptions(e,"Error al obtener Historico.");
    	}

    	if (salida==null) {
	        throw new ClsExceptions("No se ha encontrado la compra relacionada con la solicitud de certificado");
	    }
		
    	return salida;
    }
    
	public static Hashtable getDatosFacturaAsociada (String idInstitucion, String idTipoProducto, String idProducto, String idProductoInstitucion, String idPeticionProducto) throws ClsExceptions {
		Hashtable salida = new Hashtable();
	    try {
		    Hashtable codigos = new Hashtable();
		    codigos.put(new Integer(1),idInstitucion);
		    codigos.put(new Integer(2),idTipoProducto);
		    codigos.put(new Integer(3),idProducto);
		    codigos.put(new Integer(4),idProductoInstitucion);
		    codigos.put(new Integer(5),idPeticionProducto);
		    String select =	"select SOL.IDINSTITUCION, SOL.ppn_idtipoproducto, SOL.ppn_idproducto, SOL.ppn_idproductoinstitucion,  SOL.idpeticionproducto, " +
		        		" (select com.idfactura " +
				        " from pys_peticioncomprasuscripcion pet, pys_compra com " +
				        " where pet.idinstitucion = com.idinstitucion " +
				        " and pet.idpeticion = com.idpeticion " +
				        " and com.idtipoproducto = SOL.ppn_idtipoproducto " +
				        " and com.idproducto = SOL.ppn_idproducto " +
				        " and com.idproductoinstitucion = SOL.ppn_idproductoinstitucion " +
				        " and pet.idinstitucion = SOL.idinstitucion " +
				        " and pet.idpeticion = SOL.idpeticionproducto) as IDFACTURACOMPRA, " +
				        " (select fa.NUMEROFACTURA " +
				        " from fac_facturacionprogramada f, fac_factura fa " +
				        " where f.idinstitucion = fa.idinstitucion " +
				        " and f.idseriefacturacion = fa.idseriefacturacion " +
				        " and f.idprogramacion = fa.idprogramacion " +
				        " and fa.idinstitucion = SOL.IDINSTITUCION " +
				        " and fa.idfactura = " +
				        " (select com.idfactura " +
				        "                   from pys_peticioncomprasuscripcion pet, pys_compra com " +
				        " where pet.idinstitucion = com.idinstitucion " +
				        " and pet.idpeticion = com.idpeticion " +
				        "  and com.idtipoproducto = SOL.ppn_idtipoproducto " +
				        "  and com.idproducto = SOL.ppn_idproducto " +
				        "  and com.idproductoinstitucion = " +
				        "      SOL.ppn_idproductoinstitucion " +
				        "  and pet.idinstitucion = SOL.idinstitucion " +
				        "  and pet.idpeticion = SOL.idpeticionproducto)) NUMEROFACTURA, " +
				        " (select f.fechaconfirmacion " +
				        " from fac_facturacionprogramada f, fac_factura fa " +
				        " where f.idinstitucion = fa.idinstitucion " +
				        " and f.idseriefacturacion = fa.idseriefacturacion " +
				        " and f.idprogramacion = fa.idprogramacion " +
				        " and fa.idinstitucion = SOL.IDINSTITUCION " +
				        " and fa.idfactura = " +
				        " (select com.idfactura " +
				        " from pys_peticioncomprasuscripcion pet, pys_compra com " +
				        " where pet.idinstitucion = com.idinstitucion " +
				        "  and pet.idpeticion = com.idpeticion " +
				        "  and com.idtipoproducto = SOL.ppn_idtipoproducto " +
				        "  and com.idproducto = SOL.ppn_idproducto " +
				        "  and com.idproductoinstitucion = " +
				        "      SOL.ppn_idproductoinstitucion " +
				        "  and pet.idinstitucion = SOL.idinstitucion " +
				        "  and pet.idpeticion = SOL.idpeticionproducto)) FECHA_CONFIRMACION, " +
				        " (select f.idestadopdf " +
				        " from fac_facturacionprogramada f, fac_factura fa " +
				        " where f.idinstitucion = fa.idinstitucion " +
				        " and f.idseriefacturacion = fa.idseriefacturacion " +
				        " and f.idprogramacion = fa.idprogramacion " +
				        " and fa.idinstitucion = SOL.IDINSTITUCION " +
				        " and fa.idfactura = " +
				        " (select com.idfactura " +
				        " from pys_peticioncomprasuscripcion pet, pys_compra com " +
				        " where pet.idinstitucion = com.idinstitucion " +
				        "  and pet.idpeticion = com.idpeticion " +
				        "  and com.idtipoproducto = SOL.ppn_idtipoproducto " +
				        "  and com.idproducto = SOL.ppn_idproducto " +
				        "  and com.idproductoinstitucion = " +
				        "      SOL.ppn_idproductoinstitucion " +
				        "  and pet.idinstitucion = SOL.idinstitucion " +
				        "  and pet.idpeticion = SOL.idpeticionproducto)) ESTADO_PDF " +
				        " from cer_solicitudcertificados SOL " +   
				        " where SOL.IDINSTITUCION=:1 " +
				        " AND   SOL.ppn_idtipoproducto=:2 " +
				        " AND   SOL.ppn_idproducto=:3 " +
				        " AND   SOL.ppn_idproductoinstitucion=:4 " +
				        " AND   SOL.idpeticionproducto=:5 ";

			RowsContainer rc = new RowsContainer(); 
			if (rc.queryBind(select,codigos)) {
				if (rc.size()>0) {
				    salida = (Hashtable)((Row) rc.get(0)).getRow();
				}
			}
		}
	    catch (Exception e) {
	        throw new ClsExceptions(e, "Error al obtener el estado de las facturas.");
	    }
		return salida;
	}   
	
	public int getNumeroCertificados(String idInstitucion, String idPersona) throws ClsExceptions{
    	
    	StringBuffer sql = new StringBuffer();
		RowsContainer rc = null;
		int contador = 0;
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
}