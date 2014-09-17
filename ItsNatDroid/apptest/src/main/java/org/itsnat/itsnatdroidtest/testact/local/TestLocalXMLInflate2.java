package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEquals;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestLocalXMLInflate2
{
    public static void test(ScrollView compRoot,ScrollView parsedRoot)
    {
        Resources res = compRoot.getContext().getResources();

        // comp = "Layout compiled"
        // parsed = "Layout dynamically parsed"
        // No podemos testear layout_width/height en el ScrollView root porque un View está desconectado y al desconectar el width y el height se ponen a 0
        // assertEquals(comp.getWidth(),parsed.getWidth());
        // assertEquals(comp.getHeight(),parsed.getHeight());

        LinearLayout comp = (LinearLayout)compRoot.getChildAt(0);
        LinearLayout parsed = (LinearLayout)parsedRoot.getChildAt(0);

        assertEquals(comp.getOrientation(),parsed.getOrientation());

        int childCount = 1;

        // buttonReload
        {
            Button compButton = (Button) comp.getChildAt(childCount);
            Button parsedButton = (Button) parsed.getChildAt(childCount);
        }



        childCount++;

        // Test ImageView
        {
            final ImageView compLayout = (ImageView) comp.getChildAt(childCount);
            final ImageView parsedLayout = (ImageView) parsed.getChildAt(childCount);
        }



//         System.out.println("\n\n\nDEFAULT VALUE: " + compLayout.getColumnCount() + " " + parsedLayout.getColumnCount());
        //System.out.println("\n\n\n");
    }


}
