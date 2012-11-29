/*
* Author: Jose M. Arranz Santamaria
* (C) Innowhere Software Services S.L.
*/

function pkg_itsnat_opera_8_mobile(pkg)
{
    pkg.Opera8MobileHTMLDocument = Opera8MobileHTMLDocument;

function Opera8MobileHTMLDocument()
{
    this.W3CHTMLDocument = itsnat.W3CHTMLDocument;
    this.W3CHTMLDocument();

    this.getLenChildNodes = getLenChildNodes;
    function getLenChildNodes(node)
    {
        if (!node.hasChildNodes()) return 0; // Para evitar lo mas posible acceder a length (fallo aleatorio)
        return node.childNodes.length;
    }
}

} // pkg_itsnat_opera_8_mobile

function itsnat_init_opera_8_mobile(win)
{
    var itsnat = itsnat_init_w3c(win);
    pkg_itsnat_opera_8_mobile(itsnat);
    return itsnat;
}
