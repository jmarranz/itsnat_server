/*
 * This file is not part of the ItsNat framework.
 *
 * Original source code use and closed source derivatives are authorized
 * to third parties with no restriction or fee.
 * The original source code is owned by the author.
 *
 * This program is distributed AS IS in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * (C) Innowhere Software a service of Jose Maria Arranz Santamaria, Spanish citizen.
 */

package org.itsnat.feashow;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;

/**
 *
 * @author jmarranz
 */
public class SkyFireAdaptor
{
    public static boolean isSkyFire(ItsNatServletRequest request)
    {
        return BrowserUtil.isSkyFire(request);
    }

    public static void fix(FeatureShowcaseDocument featShowDoc)
    {
        /* SkyFire v1.0 fools with the CSS property "cursor" set to
         * "pointer" in some circumstances, for instance we cannot
         * click inside a <ul> element (clicks are ignored or target is ever UL).
         */
        ItsNatHTMLDocument itsNatDoc = featShowDoc.getItsNatHTMLDocument();

        String code =
            "var stSheets = document.styleSheets;" +
            "for(var i = 0; i < stSheets.length; i++)" +
            "{" +
            "  var rules = stSheets[i].cssRules;" +
            "  for(var j = 0; j < rules.length; j++)" +
            "  {" +
            "    var rule = rules[j];" +
            "    if ((rule.type == CSSRule.STYLE_RULE) && (rule.selectorText == \".mainMenuNodeList\"))" +
            "      rule.style.cursor = \"auto\";" +
            "  }" +
            "}\n";

        itsNatDoc.addCodeToSend(code);
    }

}
