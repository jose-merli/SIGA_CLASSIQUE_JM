package com.siga.administracion.form;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
//import org.apache.struts.action.ActionForm;
import com.siga.general.MasterForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;

import com.siga.administracion.SIGAGestorInterfaz;
/**
 * @author tomas.narros
 *
 */
public class SIGAGestorInterfazForm extends MasterForm {
	Hashtable data;
	private String logoImg = null;
	private String logoImgPath = null;
	private String idColor = null;
	private String idTipoLetra = null;	
	private String modo = "";
	
	public static final String ERROR_PROPERTY_MAX_LENGTH_EXCEEDED = "org.apache.struts.webapp.upload.MaxLengthExceeded";

	/**
     * The value of the embedded query string parameter
     */
    protected String queryParam;

    /**
     * Whether or not to write to a file
     */
    protected boolean writeFile;

    /**
     * The file that the user has uploaded
     */
    protected FormFile theFile;


    public SIGAGestorInterfazForm(){
		data=new Hashtable();
	}
	
	
    /**
     * Retrieve the value of the query string parameter
     */
    public String getQueryParam() {
        return queryParam;
    }

    /**
     * Set the value of the query string parameter
     */
    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    /**
     * Retrieve a representation of the file the user has uploaded
     */
    public FormFile getTheFile() {
        return theFile;
    }

    /**
     * Set a representation of the file the user has uploaded
     */
    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
    }

    /**
     * Set whether or not to write to a file
     */
    public void setWriteFile(boolean writeFile) {
        this.writeFile = writeFile;
    }

    /**
     * Get whether or not to write to a file
     */
    public boolean getWriteFile() {
        return writeFile;
    }

	/**
	 * @return Nombre del archivo del icono
	 */
	public String getLogoImg() {
		return (String)data.get(SIGAGestorInterfaz.C_LOGO);
	}
	
	/**
	 * @return Nombre del archivo del icono
	 */
	public String getLogoImgPath() {
		return (String)data.get(SIGAGestorInterfaz.C_LOGO);
	}

	/**
	 * @return
	 */
	public String getIdColor() {
		return (String)data.get(SIGAGestorInterfaz.C_COLOR);
	}

	/**
	 * @return
	 */
	public String getIdTipoLetra() {
		return (String)data.get(SIGAGestorInterfaz.C_TIPOLETRA);
	}

	/**
	 * @param string
	 */
	public void setLogoImg(FormFile theFile) {
		data.put(SIGAGestorInterfaz.C_LOGO, theFile);
	}

	/**
	 * @param string
	 */
	public void setLogoImgPath(String fich) {
		data.put(SIGAGestorInterfaz.C_LOGO, fich);
	}

	/**
	 * @param string
	 */
	public void setIdColor(String string) {
		data.put(SIGAGestorInterfaz.C_COLOR, string);
	}

	/**
	 * @param string
	 */
	public void setIdTipoLetra(String string) {
		data.put(SIGAGestorInterfaz.C_TIPOLETRA, string);
	}
	
	/**
	 * @param hashtable Objeto Hash conteniendo los datos y usando como clave el nombre de los campos en BBDD
	 */
	public void setData(Hashtable hashtable) {
		data = hashtable;
	}
	

	/**
	 * @return Objeto Hash conteniendo los datos y usando como clave el nombre de los campos en BBDD
	 */
	public Hashtable getData() {
		return data;
	}

	/**
	 * Inicializa el objecto Hash que contiene los datos. 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request){
		writeFile = false;
		if(data!=null) data.clear();
		else data=new Hashtable();
	}
	
	/**
     * Check to make sure the client hasn't exceeded the maximum allowed upload size inside of this
     * validate method.
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = null;
        //has the maximum length been exceeded?
        Boolean maxLengthExceeded = (Boolean)
                request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
        if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue()))
        {
            errors = new ActionErrors();
            errors.add(ERROR_PROPERTY_MAX_LENGTH_EXCEEDED, new ActionError("maxLengthExceeded"));
        }
        return errors;

    }

}
