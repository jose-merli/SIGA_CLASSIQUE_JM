
package com.siga.administracion.action;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.siga.administracion.form.UploadForm;

/**
 * This class takes the UploadForm and retrieves the text value
 * and file attributes and puts them in the request for the display.jsp
 * page to display them
 *
 * @author Mike Schachter
 * @version $Revision: 1.2 $ $Date: 2012-06-18 09:36:03 $
 */


public class UploadAction extends Action
{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws Exception {

        if (form instanceof UploadForm) {

            //this line is here for when the input page is upload-utf8.jsp,
            //it sets the correct character encoding for the response
            String encoding = request.getCharacterEncoding();
            if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8")))
            {
                response.setContentType("text/html; charset=utf-8");
            }

            UploadForm theForm = (UploadForm) form;


            //retrieve the query string value
            String queryValue = theForm.getQueryParam();

            //retrieve the file representation
            FormFile file = theForm.getTheFile();

            //retrieve the file name
            String fileName= file.getFileName();

            //retrieve the content type
            String contentType = file.getContentType();

            //retrieve the file size
            String size = (file.getFileSize() + " bytes");

            String data = null;
			InputStream stream =null;
			ByteArrayOutputStream baos = null;
			OutputStream bos = null;
            try {
                //retrieve the file data
                
                stream = file.getInputStream();
                //write the file to the file specified
                bos = new FileOutputStream(theForm.getFilePath()+fileName);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                data = "The file has been written to \"" + theForm.getFilePath() + "\"";
            }
            
            catch (FileNotFoundException fnfe) {
                return null;
            }
            catch (IOException ioe) {
                return null;
            }
            finally
            {
//				close the stream
				stream.close();
				baos.close();
                bos.close();
            }

            //place the data into the request for retrieval from display.jsp
            request.setAttribute("queryValue", queryValue);
            request.setAttribute("fileName", fileName);
            request.setAttribute("contentType", contentType);
            request.setAttribute("size", size);
            request.setAttribute("data", data);

            //destroy the temporary file created
            file.destroy();

            //return a forward to display.jsp
            return mapping.findForward("display");
        }

        //this shouldn't happen in this example
        return null;
    }
}