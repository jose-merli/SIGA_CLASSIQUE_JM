/**
 * 
 */
package com.siga.ws.i2055;

import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;

import com.atos.utils.ClsLogging;

/**
 * @author angelcpe
 *
 */
public class LogSIGAAsignaHandler extends BasicHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.axis.Handler#invoke(org.apache.axis.MessageContext)
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		Message msg = messageContext.getCurrentMessage();
		ClsLogging.writeFileLog(msg.getSOAPPartAsString(), 3);
	}

}
