package com.siga.gui.processTree;

import java.util.*;
import java.awt.datatransfer.*;

public class SIGAUGTransferableNode implements Transferable
{
	public static final DataFlavor NODE_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Node");
    public static final DataFlavor SOURCE_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, "Source");
    private Object node;
    private Object source;
    private DataFlavor[] flavors = { NODE_FLAVOR };

    public SIGAUGTransferableNode(Object nd, Object _source)
    {
		node = nd;
		source = _source;
    }

    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
    {
    	if (flavor == NODE_FLAVOR)
    	{
        	return node;
       	}
       	
       	else if (flavor == SOURCE_FLAVOR)
       	{
        	return source;
       	}
       	
       	else
       	{
        	throw new UnsupportedFlavorException(flavor);
        }
	}

    public DataFlavor[] getTransferDataFlavors()
    {
    	return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
    	return Arrays.asList(flavors).contains(flavor);
    }
}