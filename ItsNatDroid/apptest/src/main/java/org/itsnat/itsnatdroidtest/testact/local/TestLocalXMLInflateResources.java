package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEquals;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertNotNull;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestLocalXMLInflateResources
{
    public static void test(ScrollView compRoot, ScrollView parsedRoot)
    {
        final Resources res = compRoot.getContext().getResources();

        // comp = "Layout compiled"
        // parsed = "Layout dynamically parsed"
        // No podemos testear layout_width/height en el ScrollView root porque un View está desconectado y al desconectar el width y el height se ponen a 0
        // assertEquals(comp.getWidth(),parsed.getWidth());
        // assertEquals(comp.getHeight(),parsed.getHeight());

        LinearLayout comp = (LinearLayout) compRoot.getChildAt(0);
        LinearLayout parsed = (LinearLayout) parsedRoot.getChildAt(0);

        assertEquals(comp.getOrientation(), parsed.getOrientation());

        int childCount = 0;

        // buttonBack
        {
            Button compButton = (Button) comp.getChildAt(childCount);
            Button parsedButton = (Button) parsed.getChildAt(childCount);
            assertEquals(compButton.getId(), parsedButton.getId());
            assertEquals(compButton.getText(), parsedButton.getText());
        }

        childCount++;

        // buttonReload
        {
            Button compButton = (Button) comp.getChildAt(childCount);
            Button parsedButton = (Button) parsed.getChildAt(childCount);
            assertEquals(compButton.getId(), parsedButton.getId());
            assertEquals(compButton.getText(), parsedButton.getText());
        }

        childCount++;

        // Test BitmapDrawable attribs
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(), "BitmapDrawable (image in center)");
            assertEquals(compLayout.getText(), parsedLayout.getText());

            assertNotNull((BitmapDrawable) compLayout.getBackground());
            assertEquals((BitmapDrawable)compLayout.getBackground(), (BitmapDrawable)parsedLayout.getBackground());

        }

        childCount++;

        // Test BitmapDrawable 2 attribs
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(), "BitmapDrawable 2 (image repeated)");
            assertEquals(compLayout.getText(), parsedLayout.getText());

            assertNotNull((BitmapDrawable) compLayout.getBackground());
            assertEquals((BitmapDrawable)compLayout.getBackground(), (BitmapDrawable)parsedLayout.getBackground());
        }

        childCount++;

        // Test ClipDrawable attribs
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(), "ClipDrawable (middle img)");
            assertEquals(compLayout.getText(), parsedLayout.getText());

            assertNotNull((ClipDrawable) compLayout.getBackground());
            assertEquals((ClipDrawable)compLayout.getBackground(), (ClipDrawable)parsedLayout.getBackground());
        }

        childCount++;

        // Test ClipDrawable 2 attribs
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(), "ClipDrawable 2 (half img)");
            assertEquals(compLayout.getText(), parsedLayout.getText());

            assertNotNull((ClipDrawable) compLayout.getBackground());
            assertEquals((ClipDrawable)compLayout.getBackground(), (ClipDrawable)parsedLayout.getBackground());
        }




        childCount++;

        // Test NinePatchDrawable attribs
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(), "NinePatchDrawable (border must be green)");
            assertEquals(compLayout.getText(), parsedLayout.getText());

            assertNotNull((NinePatchDrawable) compLayout.getBackground());
            assertEquals((NinePatchDrawable)compLayout.getBackground(), (NinePatchDrawable)parsedLayout.getBackground());

        }

        childCount++;

        // Test LayerDrawable attribs
        {
            final TextView compLayout = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getText(), "LayerDrawable (2 green rects and centered img)");
            assertEquals(compLayout.getText(), parsedLayout.getText());

            assertNotNull((LayerDrawable) compLayout.getBackground());
            assertEquals((LayerDrawable)compLayout.getBackground(), (LayerDrawable)parsedLayout.getBackground());
        }


//         System.out.println("\n\n\nDEFAULT VALUE: " + compLayout.getColumnCount() + " " + parsedLayout.getColumnCount());
        //System.out.println("\n\n\n");

    }
}