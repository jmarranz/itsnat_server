package org.itsnat.itsnatdroidtest.testact.local;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.itsnatdroidtest.testact.util.TestUtil;

import java.lang.reflect.Field;

import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEquals;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertFalse;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertNotNull;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertPositive;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertTrue;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestLocalXMLInflate
{
    public static void test(ScrollView compRoot,ScrollView parsedRoot)
    {
        // comp = "Layout compiled"
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
            assertEquals(compButton.getId(), parsedButton.getId());
            assertEquals(compButton.getText(), parsedButton.getText());
        }

        childCount++;

        // Testing misc attribs
        {
            RelativeLayout compRelLayout = (RelativeLayout) comp.getChildAt(childCount);
            RelativeLayout parsedRelLayout = (RelativeLayout) parsed.getChildAt(childCount);
            {

                TextView compTextView1 = (TextView) compRelLayout.getChildAt(0);
                TextView parsedTextView1 = (TextView) parsedRelLayout.getChildAt(0);
                assertEquals(compTextView1.getId(), parsedTextView1.getId());
                assertEquals(compTextView1.getText(), parsedTextView1.getText());
                assertEquals(compTextView1.getTextSize(), parsedTextView1.getTextSize());
                assertPositive(compTextView1.getPaddingLeft());
                assertEquals(compTextView1.getPaddingLeft(),parsedTextView1.getPaddingLeft());
                assertPositive(compTextView1.getPaddingRight());
                assertEquals(compTextView1.getPaddingRight(),parsedTextView1.getPaddingRight());
                assertPositive(compTextView1.getPaddingTop());
                assertEquals(compTextView1.getPaddingTop(),parsedTextView1.getPaddingTop());
                assertPositive(compTextView1.getPaddingBottom());
                assertEquals(compTextView1.getPaddingBottom(),parsedTextView1.getPaddingBottom());
                assertEquals(compTextView1.getTextColors(), parsedTextView1.getTextColors());
                assertEquals(compTextView1.getBackground(), parsedTextView1.getBackground());

                TextView compTextView2 = (TextView) compRelLayout.getChildAt(1);
                TextView parsedTextView2 = (TextView) parsedRelLayout.getChildAt(1);
                assertEquals(compTextView2.getId(), parsedTextView2.getId());
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertEquals(compTextView2.getBackground(), parsedTextView2.getBackground());
                // No tenemos una forma de testear "textAppearanceMedium" de forma directa, una forma es testear una de las propiedades que impone, ej el tamaño del texto
                assertEquals(compTextView2.getTextSize(), parsedTextView2.getTextSize());
            }
        }

        childCount++;

        // Testing custom currentTarget class
        {
            TextView compCustomTextView = (TextView) comp.getChildAt(childCount);
            TextView parsedCustomTextView = (TextView) parsed.getChildAt(childCount);
            assertEquals(compCustomTextView.getText(), parsedCustomTextView.getText());
            assertEquals(compCustomTextView.getBackground(), parsedCustomTextView.getBackground());
        }


        childCount++;

        // Test View Attribs
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView.getAlpha(),0.7f);
                assertEquals(compTextView.getAlpha(), parsedTextView.getAlpha());
                assertEquals(((ColorDrawable)compTextView.getBackground()).getColor(), 0xffdddddd);
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
                assertFalse(compTextView.isClickable());
                assertEquals(compTextView.isClickable(), parsedTextView.isClickable());
                assertEquals(compTextView.getContentDescription(), "For Testing View Attribs");
                assertEquals(compTextView.getContentDescription(), parsedTextView.getContentDescription());
                assertEquals(compTextView.getDrawingCacheQuality(), View.DRAWING_CACHE_QUALITY_HIGH);
                assertEquals(compTextView.getDrawingCacheQuality(), parsedTextView.getDrawingCacheQuality());

                ScrollView compScrollView = (ScrollView) compLinLayout.getChildAt(1);
                ScrollView parsedScrollView = (ScrollView) parsedLinLayout.getChildAt(1);
                assertTrue(compScrollView.isScrollbarFadingEnabled()); // Correspondiente a requiresFadingEdge
                assertEquals(compScrollView.isScrollbarFadingEnabled(), parsedScrollView.isScrollbarFadingEnabled());
                assertPositive(compScrollView.getVerticalFadingEdgeLength());
                assertEquals(compScrollView.getVerticalFadingEdgeLength(), parsedScrollView.getVerticalFadingEdgeLength());
                assertPositive(compScrollView.getHorizontalFadingEdgeLength());
                assertEquals(compScrollView.getHorizontalFadingEdgeLength(), parsedScrollView.getHorizontalFadingEdgeLength());


                TextView compTextView2 = (TextView) compLinLayout.getChildAt(2);
                TextView parsedTextView2 = (TextView) parsedLinLayout.getChildAt(2);
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertTrue(compTextView2.getFilterTouchesWhenObscured());
                // En el emulador 4.0.4 el setFilterTouchesWhenObscured() parece como si hiciera un NOT al parámetro, sin embargo en el Nexus 4 perfecto
                // por ello mostramos un alertDialog no lanzamos una excepción
                if (compTextView2.getFilterTouchesWhenObscured() != parsedTextView2.getFilterTouchesWhenObscured())
                    TestUtil.alertDialog(compTextView2.getContext(),"Test fail in filterTouchesWhenObscured, don't worry it seems an Android emulator bug (running on 4.0.3 emulator?)");
                assertTrue(compTextView2.isFocusable());
                assertEquals(compTextView2.isFocusable(), parsedTextView2.isFocusable());
                assertTrue(compTextView2.isFocusableInTouchMode());
                assertEquals(compTextView2.isFocusableInTouchMode(), parsedTextView2.isFocusableInTouchMode());
                assertFalse(compTextView2.isHapticFeedbackEnabled());
                assertEquals(compTextView2.isHapticFeedbackEnabled(), parsedTextView2.isHapticFeedbackEnabled());
                assertPositive(compTextView2.getId());
                assertEquals(compTextView2.getId(), parsedTextView2.getId());
                // No puedo testear android:isScrollContainer porque  isScrollContainer() se define en un Level superior
                assertTrue(compTextView2.getKeepScreenOn());
                assertEquals(compTextView2.getKeepScreenOn(), parsedTextView2.getKeepScreenOn());
                assertEquals(compTextView2.getLayerType(), View.LAYER_TYPE_HARDWARE);
                assertEquals(compTextView2.getLayerType(), parsedTextView2.getLayerType());
                assertTrue(compTextView2.isLongClickable());
                assertEquals(compTextView2.isLongClickable(), parsedTextView2.isLongClickable());
                // No puedo testear android:minHeight porque  getMinimumHeight() se define en un Level superior
                // No puedo testear android:minWidth porque  getMinimumWidth() se define en un Level superior
                assertPositive(compTextView2.getNextFocusDownId());
                assertEquals(compTextView2.getNextFocusDownId(), parsedTextView2.getNextFocusDownId());
                assertPositive(compTextView2.getNextFocusForwardId());
                assertEquals(compTextView2.getNextFocusForwardId(), parsedTextView2.getNextFocusForwardId());
                assertPositive(compTextView2.getNextFocusLeftId());
                assertEquals(compTextView2.getNextFocusLeftId(), parsedTextView2.getNextFocusLeftId());
                assertPositive(compTextView2.getNextFocusRightId());
                assertEquals(compTextView2.getNextFocusRightId(), parsedTextView2.getNextFocusRightId());
                assertPositive(compTextView2.getNextFocusUpId());
                assertEquals(compTextView2.getNextFocusUpId(), parsedTextView2.getNextFocusUpId());
                // No puedo testear android:onClick porque no hay get nativo asociado
                assertPositive(compTextView2.getPaddingLeft());
                assertEquals(compTextView2.getPaddingLeft(),parsedTextView2.getPaddingLeft());
                assertPositive(compTextView2.getPaddingRight());
                assertEquals(compTextView2.getPaddingRight(),parsedTextView2.getPaddingRight());
                assertPositive(compTextView2.getPaddingTop());
                assertEquals(compTextView2.getPaddingTop(),parsedTextView2.getPaddingTop());
                assertPositive(compTextView2.getPaddingBottom());
                assertEquals(compTextView2.getPaddingBottom(),parsedTextView2.getPaddingBottom());
                assertPositive(compTextView2.getRotation());
                assertEquals(compTextView2.getRotation(),parsedTextView2.getRotation());
                assertPositive(compTextView2.getRotationX());
                assertEquals(compTextView2.getRotationX(),parsedTextView2.getRotationX());
                assertPositive(compTextView2.getRotationY());
                assertEquals(compTextView2.getRotationY(),parsedTextView2.getRotationY());
                assertFalse(compTextView2.isSaveEnabled());
                assertEquals(compTextView2.isSaveEnabled(),parsedTextView2.isSaveEnabled());
                assertPositive(compTextView2.getScaleX());
                assertEquals(compTextView2.getScaleX(),parsedTextView2.getScaleX());
                assertPositive(compTextView2.getScaleY());
                assertEquals(compTextView2.getScaleY(),parsedTextView2.getScaleY());
                // No testeamos android:scrollX y android:scrollY (con getScrollX() y getScrollY()) porque después de definirse correctamente
                // algo hace poner a cero los valores, quizás al insertar la View
                assertPositive(compTextView2.getScrollBarStyle());
                assertEquals(compTextView2.getScrollBarStyle(),parsedTextView2.getScrollBarStyle());
                assertFalse(compTextView2.isSoundEffectsEnabled());
                assertEquals(compTextView2.isSoundEffectsEnabled(),parsedTextView2.isSoundEffectsEnabled());
                assertEquals((String)compTextView2.getTag(),"theTag");
                assertEquals((String)compTextView2.getTag(),(String)parsedTextView2.getTag());
                assertPositive(compTextView2.getPivotX());
                assertEquals(compTextView2.getPivotX(),parsedTextView2.getPivotX());
                assertPositive(compTextView2.getPivotY());
                assertEquals(compTextView2.getPivotY(),parsedTextView2.getPivotY());
                assertPositive(compTextView2.getTranslationX());
                assertEquals(compTextView2.getTranslationX(),parsedTextView2.getTranslationX());
                assertPositive(compTextView2.getTranslationY());
                assertEquals(compTextView2.getTranslationY(),parsedTextView2.getTranslationY());
            }

        }

        childCount++;

        // Test ViewGroup Attribs
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            assertTrue(compLinLayout.addStatesFromChildren());
            assertEquals(compLinLayout.addStatesFromChildren(), parsedLinLayout.addStatesFromChildren());
            assertFalse(compLinLayout.isAlwaysDrawnWithCacheEnabled());
            assertEquals(compLinLayout.isAlwaysDrawnWithCacheEnabled(), parsedLinLayout.isAlwaysDrawnWithCacheEnabled());
            assertNotNull(compLinLayout.getLayoutTransition()); // Test de android:animateLayoutChanges
            assertNotNull(parsedLinLayout.getLayoutTransition()); // "
            assertFalse(compLinLayout.isAnimationCacheEnabled());
            assertEquals(compLinLayout.isAnimationCacheEnabled(), parsedLinLayout.isAnimationCacheEnabled());
            // No podemos testear android:clipChildren porque no tenemos el método get es Level 18
            // No podemos testear android:clipToPadding porque no tenemos un método get
            assertEquals(compLinLayout.getDescendantFocusability(),ViewGroup.FOCUS_AFTER_DESCENDANTS);
            assertEquals(compLinLayout.getDescendantFocusability(), parsedLinLayout.getDescendantFocusability());
            assertPositive(compLinLayout.getLayoutAnimation().getDelay()); // Testeamos el delay porque testear la igualdad del LayoutAnimationController es un rollo
            assertEquals(compLinLayout.getLayoutAnimation().getDelay(), parsedLinLayout.getLayoutAnimation().getDelay());
            assertEquals(compLinLayout.getPersistentDrawingCache(),parsedLinLayout.getPersistentDrawingCache());
            assertTrue(compLinLayout.isMotionEventSplittingEnabled());
            assertEquals(compLinLayout.isMotionEventSplittingEnabled(),parsedLinLayout.isMotionEventSplittingEnabled());
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
            }
        }

        childCount++;

        // Test ViewGroup.MarginLayoutParams
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            for(int i = 0; i < 2; i++)
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());

                ViewGroup.MarginLayoutParams a_params = (ViewGroup.MarginLayoutParams)compTextView.getLayoutParams();
                ViewGroup.MarginLayoutParams b_params = (ViewGroup.MarginLayoutParams)parsedTextView.getLayoutParams();

                assertEquals(a_params.topMargin,b_params.topMargin);
                assertEquals(a_params.leftMargin,b_params.leftMargin);
                assertEquals(a_params.bottomMargin,b_params.bottomMargin);
                assertEquals(a_params.rightMargin,b_params.rightMargin);
            }
        }


        childCount++;

        // Test GridLayout Attribs Horizontal
        {
            GridLayout compGridLayout = (GridLayout) comp.getChildAt(childCount);
            GridLayout parsedGridLayout = (GridLayout) parsed.getChildAt(childCount);
            assertEquals(compGridLayout.getAlignmentMode(),GridLayout.ALIGN_BOUNDS);
            assertEquals(compGridLayout.getColumnCount(),3);
            assertEquals(compGridLayout.getColumnCount(),parsedGridLayout.getColumnCount());
            assertFalse(compGridLayout.isColumnOrderPreserved());
            assertEquals(compGridLayout.isColumnOrderPreserved(),parsedGridLayout.isColumnOrderPreserved());
            assertEquals(compGridLayout.getOrientation(),GridLayout.HORIZONTAL);
            assertEquals(compGridLayout.getOrientation(),parsedGridLayout.getOrientation());
            assertEquals(compGridLayout.getRowCount(),3);
            assertEquals(compGridLayout.getRowCount(),parsedGridLayout.getRowCount());
            assertFalse(compGridLayout.isRowOrderPreserved());
            assertEquals(compGridLayout.isRowOrderPreserved(),parsedGridLayout.isRowOrderPreserved());
            assertTrue(compGridLayout.getUseDefaultMargins());
            assertEquals(compGridLayout.getUseDefaultMargins(),parsedGridLayout.getUseDefaultMargins());

            {
                for(int i = 0; i < 5; i++)
                {
                    TextView compTextView = (TextView) compGridLayout.getChildAt(i);
                    TextView parsedTextView = (TextView) parsedGridLayout.getChildAt(i);
                    // Testeamos via Spec los atributos: android:layout_column, android:layout_columnSpan y android:layout_gravity
                    GridLayout.LayoutParams compParams = (GridLayout.LayoutParams)compTextView.getLayoutParams();
                    GridLayout.LayoutParams parsedParams = (GridLayout.LayoutParams)parsedTextView.getLayoutParams();
                    compParams.columnSpec.equals(parsedParams.columnSpec);
                    compParams.rowSpec.equals(parsedParams.rowSpec);
                }
            }
        }

        childCount++;

        // Test GridLayout Attribs Vertical
        {
            GridLayout compGridLayout = (GridLayout) comp.getChildAt(childCount);
            GridLayout parsedGridLayout = (GridLayout) parsed.getChildAt(childCount);
            assertEquals(compGridLayout.getAlignmentMode(),GridLayout.ALIGN_BOUNDS);
            assertEquals(compGridLayout.getColumnCount(),3);
            assertEquals(compGridLayout.getColumnCount(),parsedGridLayout.getColumnCount());
            assertFalse(compGridLayout.isColumnOrderPreserved());
            assertEquals(compGridLayout.isColumnOrderPreserved(),parsedGridLayout.isColumnOrderPreserved());
            assertEquals(compGridLayout.getOrientation(),GridLayout.VERTICAL);
            assertEquals(compGridLayout.getOrientation(),parsedGridLayout.getOrientation());
            assertEquals(compGridLayout.getRowCount(),3);
            assertEquals(compGridLayout.getRowCount(),parsedGridLayout.getRowCount());
            assertFalse(compGridLayout.isRowOrderPreserved());
            assertEquals(compGridLayout.isRowOrderPreserved(),parsedGridLayout.isRowOrderPreserved());
            assertTrue(compGridLayout.getUseDefaultMargins());
            assertEquals(compGridLayout.getUseDefaultMargins(),parsedGridLayout.getUseDefaultMargins());

            {
                for(int i = 0; i < 5; i++)
                {
                    TextView compTextView = (TextView) compGridLayout.getChildAt(i);
                    TextView parsedTextView = (TextView) parsedGridLayout.getChildAt(i);
                    // Testeamos via Specs los atributos: android:layout_row, android:layout_rowSpan y android:layout_gravity
                    GridLayout.LayoutParams compParams = (GridLayout.LayoutParams)compTextView.getLayoutParams();
                    GridLayout.LayoutParams parsedParams = (GridLayout.LayoutParams)parsedTextView.getLayoutParams();
                    compParams.columnSpec.equals(parsedParams.columnSpec);
                    compParams.rowSpec.equals(parsedParams.rowSpec);
                }
            }

        }



        childCount++;

        // Test FrameLayout Attribs
        {
            FrameLayout compFrameLayout = (FrameLayout) comp.getChildAt(childCount);
            FrameLayout parsedFrameLayout = (FrameLayout) parsed.getChildAt(childCount);

            assertEquals(((ColorDrawable)compFrameLayout.getForeground()).getColor(), 0x55ddffdd);
            assertEquals(compFrameLayout.getForeground(), parsedFrameLayout.getForeground());
            // No podemos testear android:foregroundGravity porque getForegroundGravity() es Level 16
            assertTrue(compFrameLayout.getMeasureAllChildren());
            assertEquals(compFrameLayout.getMeasureAllChildren(), parsedFrameLayout.getMeasureAllChildren());
        }

        childCount++;

        // Test LinearLayout Attribs
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);

            assertFalse(compLinLayout.isBaselineAligned());
            assertEquals(compLinLayout.isBaselineAligned(), parsedLinLayout.isBaselineAligned());
            assertEquals(compLinLayout.getBaselineAlignedChildIndex(), 1);
            assertEquals(compLinLayout.getBaselineAlignedChildIndex(), parsedLinLayout.getBaselineAlignedChildIndex());
            // No podemos testear android:divider porque getDividerDrawable() es Level 16
            assertEquals(compLinLayout.getShowDividers(), 3);
            assertEquals(compLinLayout.getShowDividers(),parsedLinLayout.getShowDividers());
            assertPositive(compLinLayout.getDividerPadding());
            assertEquals(compLinLayout.getDividerPadding(),parsedLinLayout.getDividerPadding());

            assertTrue(compLinLayout.isMeasureWithLargestChildEnabled());
            assertEquals(compLinLayout.isMeasureWithLargestChildEnabled(),parsedLinLayout.isMeasureWithLargestChildEnabled());
            assertEquals(compLinLayout.getWeightSum(),1.0f);
            assertEquals(compLinLayout.getWeightSum(),parsedLinLayout.getWeightSum());
        }

        childCount++;

        // Test LinearLayout gravity
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
                assertEquals(compTextView.getGravity(), parsedTextView.getGravity());
            }
        }

        childCount++;

        // Testing LinearLayout.LayoutParams
        {
            LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);
            for(int i = 0; i < 2; i++)
            {
                TextView compTextView1 = (TextView) compLinLayout.getChildAt(i);
                TextView parsedTextView1 = (TextView) parsedLinLayout.getChildAt(i);
                assertEquals(compTextView1.getText(), parsedTextView1.getText());
                assertEquals(compTextView1.getBackground(), compTextView1.getBackground());

                LinearLayout.LayoutParams compParams = (LinearLayout.LayoutParams)compTextView1.getLayoutParams();
                LinearLayout.LayoutParams parsedParams = (LinearLayout.LayoutParams)parsedTextView1.getLayoutParams();

                assertEquals(compParams.gravity,parsedParams.gravity);
                assertEquals(compParams.weight,parsedParams.weight);
            }
        }

        childCount++;

        // Test RelativeLayout (gravity)
        {
            RelativeLayout compLayout = (RelativeLayout) comp.getChildAt(childCount);
            RelativeLayout parsedLayout = (RelativeLayout) parsed.getChildAt(childCount);
            // No puedo testear android:gravity porque getGravity() es Level 16

            {
                TextView compTextView = (TextView) compLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
            }
        }

        childCount++;

        // Test RelativeLayout (ignoreGravity)
        {
            RelativeLayout compLayout = (RelativeLayout) comp.getChildAt(childCount);
            RelativeLayout parsedLayout = (RelativeLayout) parsed.getChildAt(childCount);
            // No puedo testear android:gravity porque getGravity() es Level 16

            {
                TextView compTextView = (TextView) compLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
            }
        }

        childCount++;

        // Test RelativeLayout.LayoutParams
        {
            RelativeLayout compLayout = (RelativeLayout) comp.getChildAt(childCount);
            RelativeLayout parsedLayout = (RelativeLayout) parsed.getChildAt(childCount);

            for(int i = 0; i < 2; i++)
            {
                TextView compTextView = (TextView) compLayout.getChildAt(i);
                TextView parsedTextView = (TextView) parsedLayout.getChildAt(i);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());

                RelativeLayout.LayoutParams compTextParams = (RelativeLayout.LayoutParams)compTextView.getLayoutParams();
                RelativeLayout.LayoutParams parsedTextParams = (RelativeLayout.LayoutParams)parsedTextView.getLayoutParams();
                int[] compTextRules = compTextParams.getRules();
                int[] parsedTextRules = parsedTextParams.getRules();
                assertEquals(compTextRules.length, parsedTextRules.length);
                for(int j = 0; j < compTextRules.length; j++)
                {
                    assertEquals(compTextRules[j],parsedTextRules[j]);
                }
            }
        }

        childCount++;

        // Test AbsListView
        {
            ListView compLayout = (ListView) comp.getChildAt(childCount);
            ListView parsedLayout = (ListView) parsed.getChildAt(childCount);

            assertEquals(compLayout.getCacheColorHint(),0xffff0000);
            assertEquals(compLayout.getCacheColorHint(), parsedLayout.getCacheColorHint());
            assertEquals(compLayout.getChoiceMode(), AbsListView.CHOICE_MODE_MULTIPLE);
            assertEquals(compLayout.getChoiceMode(), parsedLayout.getChoiceMode());
            // No podemos testear android:drawSelectorOnTop porque no hay un isDrawSelectorOnTop
            assertFalse(compLayout.isFastScrollEnabled()); // Preferiría testear el true pero no se porqué razón se ignora el true
            assertEquals(compLayout.isFastScrollEnabled(), parsedLayout.isFastScrollEnabled());
            assertEquals(((ColorDrawable)compLayout.getSelector()).getColor(), 0x6600ff00);
            assertEquals(compLayout.getSelector(), parsedLayout.getSelector());
            assertFalse(compLayout.isScrollingCacheEnabled());
            assertEquals(compLayout.isScrollingCacheEnabled(), parsedLayout.isScrollingCacheEnabled());
            assertFalse(compLayout.isSmoothScrollbarEnabled());
            assertEquals(compLayout.isSmoothScrollbarEnabled(), parsedLayout.isSmoothScrollbarEnabled());
            assertTrue(compLayout.isStackFromBottom());
            assertEquals(compLayout.isStackFromBottom(), parsedLayout.isStackFromBottom());
            assertTrue(compLayout.isTextFilterEnabled());
            assertEquals(compLayout.isTextFilterEnabled(), parsedLayout.isTextFilterEnabled());
            assertEquals(compLayout.getTranscriptMode(), AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            assertEquals(compLayout.getTranscriptMode(), parsedLayout.getTranscriptMode());

         }

        childCount++;

        // Test GridView
        {
            final GridView compLayout = (GridView) comp.getChildAt(childCount);
            final GridView parsedLayout = (GridView) parsed.getChildAt(childCount);

            // No podemos testear android:columnWidth, getColumnWidth es Level 16
            // No podemos testear android:gravity, getGravity es Level 16
            // No podemos testear android:horizontalSpacing, getHorizontalSpacing es Level 16
            assertEquals(compLayout.getNumColumns(), 3);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // Usamos un OnLayoutChangeListener porque el setNumColumns define un atributo "request" que no es el atributo
                    // donde va el numColumns definitivo el cual se calcula al hacer el layout
                    assertEquals(compLayout.getNumColumns(),parsedLayout.getNumColumns());
                }
            });
            assertEquals(compLayout.getStretchMode(),GridView.STRETCH_COLUMN_WIDTH); // Es el modo por defecto pero los demás modos en nuestro test se ven muy mal
            assertEquals(compLayout.getStretchMode(), parsedLayout.getStretchMode());
            // No podemos testear android:verticalSpacing, getVerticalSpacing es Level 16
        }

        childCount++;

        // Test ListView
        {
            final ListView compLayout = (ListView) comp.getChildAt(childCount);
            final ListView parsedLayout = (ListView) parsed.getChildAt(childCount);

            // No se como testear la igualdad de dos GradientDrawable, si no se define por defecto devuelve un NinePatchDrawable
            // Test visual: líneas rojas separadoras de items
            assertNotNull(((GradientDrawable) compLayout.getDivider()));
            assertNotNull(((GradientDrawable) parsedLayout.getDivider()));
            assertPositive(compLayout.getDividerHeight());
            assertEquals(compLayout.getDividerHeight(),parsedLayout.getDividerHeight());
            // No se puede testear android:footerDividersEnabled y android:headerDividersEnabled porque no hay métodos get (areFooterDividersEnabled/areHeaderDividersEnabled son Level 19)

        }

        childCount++;

        // Test ExpandableListView
        {
            final ExpandableListView compLayout = (ExpandableListView) comp.getChildAt(childCount);
            final ExpandableListView parsedLayout = (ExpandableListView) parsed.getChildAt(childCount);

            // No puedo testear android:childDivider por que no hay método get
            // Test visual: líneas rojas separadoras de items
            // Lo mismo ocurre con los demás atributos de esta clase
            // android:childIndicator, android:childIndicatorLeft,android:childIndicatorRight,groupIndicator
            // android:indicatorLeft, android:indicatorRight
        }

        childCount++;

        // Space for page scrolling between lists
        {
            final TextView compTextView = (TextView) comp.getChildAt(childCount);
            final TextView parsedLayout = (TextView) parsed.getChildAt(childCount);
        }

        childCount++;

        // Test AbsSpinner (entries sólo) y Gallery
        {
            final Gallery compLayout = (Gallery) comp.getChildAt(childCount);
            final Gallery parsedLayout = (Gallery) parsed.getChildAt(childCount);
            assertEquals((Integer) getField(compLayout, "mAnimationDuration"), 100);
            assertEquals((Integer)getField(compLayout,"mAnimationDuration"),(Integer)getField(parsedLayout,"mAnimationDuration"));
            assertEquals((Integer)getField(compLayout,"mGravity"), Gravity.CENTER_VERTICAL);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)getField(compLayout,"mGravity"),(Integer)getField(parsedLayout,"mGravity"));
                }
            });
            assertPositive((Integer) getField(compLayout, "mSpacing"));
            assertEquals((Integer)getField(compLayout,"mSpacing"),(Integer)getField(parsedLayout,"mSpacing"));
            assertEquals((Float) getField(compLayout, "mUnselectedAlpha"), 0.6f);
            assertEquals((Float)getField(compLayout,"mUnselectedAlpha"),(Float)getField(parsedLayout,"mUnselectedAlpha"));
        }

        childCount++;

        // Test Spinner
        {
            final Spinner compLayout = (Spinner) comp.getChildAt(childCount);
            final Spinner parsedLayout = (Spinner) parsed.getChildAt(childCount);
        }


//         System.out.println("\n\n\nDEFAULT VALUE: " + compLayout.getColumnCount() + " " + parsedLayout.getColumnCount());
        //System.out.println("\n\n\n");
    }

    protected static Object getField(View view,String fieldName)
    {
        try
        {
            Field field = view.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(view);
        }
        catch (NoSuchFieldException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
    }
}
