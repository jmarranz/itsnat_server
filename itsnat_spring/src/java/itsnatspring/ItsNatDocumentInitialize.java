package itsnatspring;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;

public interface ItsNatDocumentInitialize
{
    public void load(ItsNatServletRequest request, ItsNatServletResponse response);
}
