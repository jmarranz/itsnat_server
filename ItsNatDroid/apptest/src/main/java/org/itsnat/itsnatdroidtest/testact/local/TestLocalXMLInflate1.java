package org.itsnat.itsnatdroidtest.testact.local;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterViewAnimator;
import android.widget.AdapterViewFlipper;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.itsnat.droid.InflatedLayout;
import org.itsnat.droid.impl.util.ValueUtil;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.util.TestUtil;

import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEquals;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertEqualsStrokeWidth;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertFalse;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertNotNull;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertPositive;
import static org.itsnat.itsnatdroidtest.testact.util.Assert.assertTrue;


/**
 * Created by jmarranz on 19/06/14.
 */
public class TestLocalXMLInflate1
{
    public static void test(ScrollView compRoot,ScrollView parsedRoot,InflatedLayout layout)
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

        // Testing misc attribs
        {
            // Test findViewByXMLId


            RelativeLayout compRelLayout = (RelativeLayout) comp.getChildAt(childCount);
            RelativeLayout parsedRelLayout = (RelativeLayout) parsed.getChildAt(childCount);
            {

                TextView compTextView1 = (TextView) compRelLayout.getChildAt(0);
                TextView parsedTextView1 = (TextView) parsedRelLayout.getChildAt(0);

                assertEquals(compTextView1.getId(),R.id.textViewTest1);
                assertEquals(compTextView1.getId(), parsedTextView1.getId());
                if (compTextView1 != compRelLayout.findViewById(R.id.textViewTest1)) throw new RuntimeException("FAIL");
                if (parsedTextView1 != layout.findViewByXMLId("textViewTest1")) throw new RuntimeException("FAIL");

                assertEquals(compTextView1.getText(), parsedTextView1.getText());

                assertEquals(compTextView1.getTextSize(),ValueUtil.dpToPixelInt(15, res));
                assertEquals(compTextView1.getTextSize(), parsedTextView1.getTextSize());

                assertEquals(compTextView1.getPaddingLeft(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView1.getPaddingLeft(),parsedTextView1.getPaddingLeft());

                assertEquals(compTextView1.getPaddingRight(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView1.getPaddingRight(),parsedTextView1.getPaddingRight());

                assertEquals(compTextView1.getPaddingTop(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView1.getPaddingTop(),parsedTextView1.getPaddingTop());

                assertEquals(compTextView1.getPaddingBottom(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView1.getPaddingBottom(),parsedTextView1.getPaddingBottom());

                assertEquals(compTextView1.getTextColors().getDefaultColor(),0xff0000ff);
                assertEquals(compTextView1.getTextColors(), parsedTextView1.getTextColors());

                assertEquals(((ColorDrawable)compTextView1.getBackground()).getColor(), res.getColor(res.getIdentifier("@android:color/holo_green_light",null,null)));
                assertEquals(compTextView1.getBackground(), parsedTextView1.getBackground());

                TextView compTextView2 = (TextView) compRelLayout.getChildAt(1);
                TextView parsedTextView2 = (TextView) parsedRelLayout.getChildAt(1);
                assertEquals(compTextView2.getId(), parsedTextView2.getId());
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                assertEquals(compTextView2.getBackground(), parsedTextView2.getBackground());
                // Test atributo style
                // No tenemos una forma de testear "textAppearanceMedium" de forma directa, una forma es testear una de las propiedades que impone, ej el tamaño del texto
                assertEquals(compTextView2.getTextSize(), parsedTextView2.getTextSize());
            }
        }

        childCount++;

        // Testing custom class
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
                assertTrue(compTextView.isDuplicateParentStateEnabled());
                assertEquals(compTextView.isDuplicateParentStateEnabled(), parsedTextView.isDuplicateParentStateEnabled());


                ScrollView compScrollView = (ScrollView) compLinLayout.getChildAt(1);
                ScrollView parsedScrollView = (ScrollView) parsedLinLayout.getChildAt(1);
                assertTrue(compScrollView.isScrollbarFadingEnabled()); // Correspondiente a requiresFadingEdge
                assertEquals(compScrollView.isScrollbarFadingEnabled(), parsedScrollView.isScrollbarFadingEnabled());
                // Test android:fadingEdgeLength
                assertEquals(compScrollView.getVerticalFadingEdgeLength(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compScrollView.getVerticalFadingEdgeLength(), parsedScrollView.getVerticalFadingEdgeLength());
                assertEquals(compScrollView.getHorizontalFadingEdgeLength(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compScrollView.getHorizontalFadingEdgeLength(), parsedScrollView.getHorizontalFadingEdgeLength());

                // Test android:scrollbarAlwaysDrawHorizontalTrack
                assertTrue((Boolean)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mAlwaysDrawHorizontalTrack"}));
                assertEquals((Boolean)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mAlwaysDrawHorizontalTrack"}),
                             (Boolean)TestUtil.getField(parsedScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mAlwaysDrawHorizontalTrack"})
                        );

                // Test android:scrollbarAlwaysDrawVerticalTrack
                assertTrue((Boolean)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mAlwaysDrawVerticalTrack"}));
                assertEquals((Boolean)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mAlwaysDrawVerticalTrack"}),
                        (Boolean)TestUtil.getField(parsedScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mAlwaysDrawVerticalTrack"})
                );

                // Test android:scrollbarThumbHorizontal
                assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, new Class[]{View.class, TestUtil.resolveClass("android.view.View$ScrollabilityCache"), TestUtil.resolveClass("android.widget.ScrollBarDrawable")}, new String[]{"mScrollCache", "scrollBar", "mHorizontalThumb"}),
                                        ValueUtil.dpToPixelInt(0.9f, res));
                assertEquals((GradientDrawable)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mHorizontalThumb"}),
                             (GradientDrawable)TestUtil.getField(parsedScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mHorizontalThumb"})
                );

                // Test android:scrollbarThumbVertical
                assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, new Class[]{View.class, TestUtil.resolveClass("android.view.View$ScrollabilityCache"), TestUtil.resolveClass("android.widget.ScrollBarDrawable")}, new String[]{"mScrollCache", "scrollBar", "mVerticalThumb"}), ValueUtil.dpToPixelInt(0.9f, res));
                assertEquals((GradientDrawable)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mVerticalThumb"}),
                             (GradientDrawable)TestUtil.getField(parsedScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mVerticalThumb"})
                );

                // Test android:scrollbarTrackHorizontal
                assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, new Class[]{View.class, TestUtil.resolveClass("android.view.View$ScrollabilityCache"), TestUtil.resolveClass("android.widget.ScrollBarDrawable")}, new String[]{"mScrollCache", "scrollBar", "mHorizontalTrack"}),
                                        ValueUtil.dpToPixelInt(0.9f,res));
                assertEquals((GradientDrawable)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mHorizontalTrack"}),
                        (GradientDrawable)TestUtil.getField(parsedScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mHorizontalTrack"})
                );

                // Test android:scrollbarTrackVertical
                assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compScrollView, new Class[]{View.class, TestUtil.resolveClass("android.view.View$ScrollabilityCache"), TestUtil.resolveClass("android.widget.ScrollBarDrawable")}, new String[]{"mScrollCache", "scrollBar", "mVerticalTrack"}),
                                        ValueUtil.dpToPixelInt(0.9f,res));
                assertEquals((GradientDrawable)TestUtil.getField(compScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mVerticalTrack"}),
                        (GradientDrawable)TestUtil.getField(parsedScrollView, new Class[]{View.class,TestUtil.resolveClass("android.view.View$ScrollabilityCache"),TestUtil.resolveClass("android.widget.ScrollBarDrawable")},
                                new String[]{"mScrollCache", "scrollBar","mVerticalTrack"})
                );



                TextView compTextView2 = (TextView) compLinLayout.getChildAt(2);
                TextView parsedTextView2 = (TextView) parsedLinLayout.getChildAt(2);
                assertEquals(compTextView2.getText(), parsedTextView2.getText());
                // Test android:filterTouchesWhenObscured
                assertTrue(compTextView2.getFilterTouchesWhenObscured());
                // En el emulador 4.0.4 el setFilterTouchesWhenObscured() parece como si hiciera un NOT al parámetro, sin embargo en el Nexus 4 perfecto
                // por ello mostramos un alertDialog no lanzamos una excepción
                if (compTextView2.getFilterTouchesWhenObscured() != parsedTextView2.getFilterTouchesWhenObscured())
                    TestUtil.alertDialog(compTextView2.getContext(),"Test failed in filterTouchesWhenObscured, don't worry it seems an Android emulator bug (running on 4.0.4 emulator?)");
                assertTrue(compTextView2.isFocusable());
                assertEquals(compTextView2.isFocusable(), parsedTextView2.isFocusable());
                assertTrue(compTextView2.isFocusableInTouchMode());
                assertEquals(compTextView2.isFocusableInTouchMode(), parsedTextView2.isFocusableInTouchMode());
                assertFalse(compTextView2.isHapticFeedbackEnabled());
                assertEquals(compTextView2.isHapticFeedbackEnabled(), parsedTextView2.isHapticFeedbackEnabled());
                assertPositive(compTextView2.getId());
                assertEquals(compTextView2.getId(), parsedTextView2.getId());
                // No puedo testear android:isScrollContainer porque  isScrollContainer() se define en un Level superior
                //assertTrue( (((int)((Integer)getField(compTextView2,View.class,"mPrivateFlags"))) & 0x00100000) != 0); // PFLAG_SCROLL_CONTAINER_ADDED 0x00100000

                assertTrue(compTextView2.getKeepScreenOn());
                assertEquals(compTextView2.getKeepScreenOn(), parsedTextView2.getKeepScreenOn());
                assertEquals(compTextView2.getLayerType(), View.LAYER_TYPE_HARDWARE);
                assertEquals(compTextView2.getLayerType(), parsedTextView2.getLayerType());
                assertTrue(compTextView2.isLongClickable());
                assertEquals(compTextView2.isLongClickable(), parsedTextView2.isLongClickable());
                assertEquals((Integer)TestUtil.getField(compTextView2, View.class, "mMinHeight"),ValueUtil.dpToPixelInt(30, res));
                assertEquals((Integer) TestUtil.getField(compTextView2, View.class, "mMinHeight"), (Integer)TestUtil.getField(parsedTextView2, View.class, "mMinHeight"));
                assertEquals((Integer)TestUtil.getField(compTextView2, View.class, "mMinWidth"),ValueUtil.dpToPixelInt(30, res));
                assertEquals((Integer)TestUtil.getField(compTextView2, View.class, "mMinWidth"),(Integer)TestUtil.getField(parsedTextView2, View.class, "mMinWidth") );
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
                assertEquals(compTextView2.getPaddingLeft(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView2.getPaddingLeft(),parsedTextView2.getPaddingLeft());
                assertEquals(compTextView2.getPaddingRight(),ValueUtil.dpToPixelInt(11, res));
                assertEquals(compTextView2.getPaddingRight(),parsedTextView2.getPaddingRight());
                assertEquals(compTextView2.getPaddingTop(),ValueUtil.dpToPixelInt(12, res));
                assertEquals(compTextView2.getPaddingTop(),parsedTextView2.getPaddingTop());
                assertEquals(compTextView2.getPaddingBottom(),ValueUtil.dpToPixelInt(13, res));
                assertEquals(compTextView2.getPaddingBottom(),parsedTextView2.getPaddingBottom());
                assertEquals(compTextView2.getRotation(),10.5f);
                assertEquals(compTextView2.getRotation(),parsedTextView2.getRotation());
                assertEquals(compTextView2.getRotationX(),45.5f);
                assertEquals(compTextView2.getRotationX(),parsedTextView2.getRotationX());
                assertEquals(compTextView2.getRotationY(),10.5f);
                assertEquals(compTextView2.getRotationY(),parsedTextView2.getRotationY());
                assertFalse(compTextView2.isSaveEnabled());
                assertEquals(compTextView2.isSaveEnabled(),parsedTextView2.isSaveEnabled());
                assertEquals(compTextView2.getScaleX(),1.2f);
                assertEquals(compTextView2.getScaleX(),parsedTextView2.getScaleX());
                assertEquals(compTextView2.getScaleY(),1.2f);
                assertEquals(compTextView2.getScaleY(),parsedTextView2.getScaleY());
                // No testeamos android:scrollX y android:scrollY (con getScrollX() y getScrollY()) porque después de definirse correctamente
                // algo hace poner a cero los valores, quizás al insertar la View
                assertPositive(compTextView2.getScrollBarStyle());
                assertEquals(compTextView2.getScrollBarStyle(),parsedTextView2.getScrollBarStyle());
                assertFalse(compTextView2.isSoundEffectsEnabled());
                assertEquals(compTextView2.isSoundEffectsEnabled(),parsedTextView2.isSoundEffectsEnabled());
                assertEquals((String)compTextView2.getTag(),"theTag");
                assertEquals((String)compTextView2.getTag(),(String)parsedTextView2.getTag());
                assertEquals(compTextView2.getPivotX(),ValueUtil.dpToPixelInt(70, res));
                assertEquals(compTextView2.getPivotX(),parsedTextView2.getPivotX());
                assertEquals(compTextView2.getPivotY(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView2.getPivotY(),parsedTextView2.getPivotY());
                assertEquals(compTextView2.getTranslationX(),ValueUtil.dpToPixelInt(10, res));
                assertEquals(compTextView2.getTranslationX(),parsedTextView2.getTranslationX());
                assertEquals(compTextView2.getTranslationY(),ValueUtil.dpToPixelInt(10, res));
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
            // Test de android:animateLayoutChanges
            // Si animateLayoutChanges="false" getLayoutTransition() devuelve null por lo que el chequear a null es suficiente test
            assertNotNull(compLinLayout.getLayoutTransition());
            assertNotNull(parsedLinLayout.getLayoutTransition());

            assertFalse(compLinLayout.isAnimationCacheEnabled());
            assertEquals(compLinLayout.isAnimationCacheEnabled(), parsedLinLayout.isAnimationCacheEnabled());
            // Tests de android:clipChildren (el método get es Level 18)
            assertFalse( ((int)(Integer)TestUtil.getField(compLinLayout, ViewGroup.class, "mGroupFlags") & 0x1) == 0x1 ); // FLAG_CLIP_CHILDREN = 0x1
            assertEquals( ((int)(Integer)TestUtil.getField(compLinLayout, ViewGroup.class, "mGroupFlags") & 0x1) == 0x1, ((int)(Integer)TestUtil.getField(parsedLinLayout, ViewGroup.class, "mGroupFlags") & 0x1) == 0x1 );
            // Tests de android:clipToPadding
            assertFalse(((int) (Integer)TestUtil.getField(compLinLayout, ViewGroup.class, "mGroupFlags") & 0x2) == 0x2); // FLAG_CLIP_TO_PADDING = 0x2
            assertEquals(((int) (Integer)TestUtil.getField(compLinLayout, ViewGroup.class, "mGroupFlags") & 0x2) == 0x2, ((int) (Integer)TestUtil.getField(parsedLinLayout, ViewGroup.class, "mGroupFlags") & 0x2) == 0x2);
            assertEquals(compLinLayout.getDescendantFocusability(), ViewGroup.FOCUS_AFTER_DESCENDANTS);
            assertEquals(compLinLayout.getDescendantFocusability(), parsedLinLayout.getDescendantFocusability());
            assertTrue((compLinLayout.getLayoutAnimation().getDelay() - 1.0f*10/100) < 0.00001); // Testeamos el delay porque testear la igualdad del LayoutAnimationController es un rollo
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

                assertEquals(a_params.topMargin,ValueUtil.dpToPixelInt(15, res));
                assertEquals(a_params.topMargin,b_params.topMargin);
                assertEquals(a_params.leftMargin,ValueUtil.dpToPixelInt(10, res));
                assertEquals(a_params.leftMargin,b_params.leftMargin);
                assertEquals(a_params.bottomMargin,ValueUtil.dpToPixelInt(5, res));
                assertEquals(a_params.bottomMargin,b_params.bottomMargin);
                assertEquals(a_params.rightMargin,ValueUtil.dpToPixelInt(1, res));
                assertEquals(a_params.rightMargin,b_params.rightMargin);
            }
        }


        childCount++;

        // Test GridLayout Attribs Horizontal
        {
            final GridLayout compLayout = (GridLayout) comp.getChildAt(childCount);
            final GridLayout parsedLayout = (GridLayout) parsed.getChildAt(childCount);
            assertEquals(compLayout.getAlignmentMode(),GridLayout.ALIGN_BOUNDS);
            assertEquals(compLayout.getColumnCount(),3);
            assertEquals(compLayout.getColumnCount(), parsedLayout.getColumnCount());
            assertFalse(compLayout.isColumnOrderPreserved());
            assertEquals(compLayout.isColumnOrderPreserved(), parsedLayout.isColumnOrderPreserved());
            assertEquals(compLayout.getOrientation(),GridLayout.HORIZONTAL);
            assertEquals(compLayout.getOrientation(),parsedLayout.getOrientation());
            assertEquals(compLayout.getRowCount(), 3);
            assertEquals(compLayout.getRowCount(),parsedLayout.getRowCount());
            assertFalse(compLayout.isRowOrderPreserved());
            assertEquals(compLayout.isRowOrderPreserved(), parsedLayout.isRowOrderPreserved());
            assertTrue(compLayout.getUseDefaultMargins());
            assertEquals(compLayout.getUseDefaultMargins(), parsedLayout.getUseDefaultMargins());

            {
                parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
                {
                    @Override
                    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                    {
                    for(i = 0; i < 5; i++)
                    {
                        TextView compTextView = (TextView) compLayout.getChildAt(i);
                        TextView parsedTextView = (TextView) parsedLayout.getChildAt(i);
                        // Testeamos via Spec los atributos: android:layout_column, android:layout_columnSpan y android:layout_gravity
                        GridLayout.LayoutParams compParams = (GridLayout.LayoutParams)compTextView.getLayoutParams();
                        GridLayout.LayoutParams parsedParams = (GridLayout.LayoutParams)parsedTextView.getLayoutParams();
                        assertTrue(compParams.columnSpec.equals(parsedParams.columnSpec));
                        assertTrue(compParams.rowSpec.equals(parsedParams.rowSpec));
                    }
                    }
                });

            }
        }

        childCount++;

        // Test GridLayout Attribs Vertical
        {
            final GridLayout compLayout = (GridLayout) comp.getChildAt(childCount);
            final GridLayout parsedLayout = (GridLayout) parsed.getChildAt(childCount);
            assertEquals(compLayout.getAlignmentMode(),GridLayout.ALIGN_BOUNDS);
            assertEquals(compLayout.getColumnCount(),3);
            assertEquals(compLayout.getColumnCount(), parsedLayout.getColumnCount());
            assertFalse(compLayout.isColumnOrderPreserved());
            assertEquals(compLayout.isColumnOrderPreserved(), parsedLayout.isColumnOrderPreserved());
            assertEquals(compLayout.getOrientation(),GridLayout.VERTICAL);
            assertEquals(compLayout.getOrientation(),parsedLayout.getOrientation());
            assertEquals(compLayout.getRowCount(), 3);
            assertEquals(compLayout.getRowCount(),parsedLayout.getRowCount());
            assertFalse(compLayout.isRowOrderPreserved());
            assertEquals(compLayout.isRowOrderPreserved(), parsedLayout.isRowOrderPreserved());
            assertTrue(compLayout.getUseDefaultMargins());
            assertEquals(compLayout.getUseDefaultMargins(), parsedLayout.getUseDefaultMargins());

            {
                parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
                {
                    @Override
                    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                    {
                    for (i = 0; i < 5; i++)
                    {
                        TextView compTextView = (TextView) compLayout.getChildAt(i);
                        TextView parsedTextView = (TextView) parsedLayout.getChildAt(i);
                        // Testeamos via Specs los atributos: android:layout_row, android:layout_rowSpan y android:layout_gravity
                        GridLayout.LayoutParams compParams = (GridLayout.LayoutParams) compTextView.getLayoutParams();
                        GridLayout.LayoutParams parsedParams = (GridLayout.LayoutParams) parsedTextView.getLayoutParams();
                        assertTrue(compParams.columnSpec.equals(parsedParams.columnSpec));
                        assertTrue(compParams.rowSpec.equals(parsedParams.rowSpec));
                    }
                    }
                });
            }

        }



        childCount++;

        // Test FrameLayout Attribs
        {
            FrameLayout compFrameLayout = (FrameLayout) comp.getChildAt(childCount);
            FrameLayout parsedFrameLayout = (FrameLayout) parsed.getChildAt(childCount);

            assertEquals(((ColorDrawable)compFrameLayout.getForeground()).getColor(), 0x55ddffdd);
            assertEquals(compFrameLayout.getForeground(), parsedFrameLayout.getForeground());
            // Test android:foregroundGravity (getForegroundGravity() es Level 16):
            assertEquals((Integer)TestUtil.getField(compFrameLayout, "mForegroundGravity"),Gravity.TOP | Gravity.LEFT );
            assertEquals((Integer)TestUtil.getField(compFrameLayout, "mForegroundGravity"), (Integer)TestUtil.getField(parsedFrameLayout, "mForegroundGravity"));
            assertTrue(compFrameLayout.getMeasureAllChildren());
            assertEquals(compFrameLayout.getMeasureAllChildren(), parsedFrameLayout.getMeasureAllChildren());
        }

        childCount++;

        // Test LinearLayout Attribs
        {
            final LinearLayout compLinLayout = (LinearLayout) comp.getChildAt(childCount);
            final LinearLayout parsedLinLayout = (LinearLayout) parsed.getChildAt(childCount);

            assertFalse(compLinLayout.isBaselineAligned());
            assertEquals(compLinLayout.isBaselineAligned(), parsedLinLayout.isBaselineAligned());
            assertEquals(compLinLayout.getBaselineAlignedChildIndex(), 1);
            assertEquals(compLinLayout.getBaselineAlignedChildIndex(), parsedLinLayout.getBaselineAlignedChildIndex());
            // Tests android:divider (getDividerDrawable() es Level 16):
            assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compLinLayout, "mDivider"), ValueUtil.dpToPixelInt(0.9f, res));
            assertEquals((GradientDrawable) TestUtil.getField(compLinLayout, "mDivider"), (GradientDrawable) TestUtil.getField(parsedLinLayout, "mDivider"));

            assertEquals(compLinLayout.getShowDividers(), 3);
            assertEquals(compLinLayout.getShowDividers(),parsedLinLayout.getShowDividers());
            assertEquals(compLinLayout.getDividerPadding(),ValueUtil.dpToPixelInt(10, res));
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
            assertEquals((Integer)TestUtil.getField(compLinLayout, "mGravity"), Gravity.TOP |Gravity.RIGHT);
            assertEquals((Integer)TestUtil.getField(compLinLayout, "mGravity"),(Integer)TestUtil.getField(parsedLinLayout, "mGravity"));
            {
                TextView compTextView = (TextView) compLinLayout.getChildAt(0);
                TextView parsedTextView = (TextView) parsedLinLayout.getChildAt(0);
                assertEquals(compTextView.getText(), parsedTextView.getText());
                assertEquals(compTextView.getBackground(), parsedTextView.getBackground());
            }
        }

        childCount++;

        // Testing LinearLayout.LayoutParams
        {
            LinearLayout compLayout = (LinearLayout) comp.getChildAt(childCount);
            LinearLayout parsedLayout = (LinearLayout) parsed.getChildAt(childCount);
            for(int i = 0; i < 2; i++)
            {
                TextView compTextView1 = (TextView) compLayout.getChildAt(i);
                TextView parsedTextView1 = (TextView) parsedLayout.getChildAt(i);
                assertEquals(compTextView1.getText(), parsedTextView1.getText());
                assertEquals(compTextView1.getBackground(), compTextView1.getBackground());

                LinearLayout.LayoutParams compParams = (LinearLayout.LayoutParams)compTextView1.getLayoutParams();
                LinearLayout.LayoutParams parsedParams = (LinearLayout.LayoutParams)parsedTextView1.getLayoutParams();

                if (i == 0) assertEquals(compParams.gravity,Gravity.TOP|Gravity.LEFT);
                else assertEquals(compParams.gravity,Gravity.BOTTOM|Gravity.RIGHT);
                assertEquals(compParams.gravity,parsedParams.gravity);

                if (i == 0) assertEquals(compParams.weight,70);
                else assertEquals(compParams.weight,30);
                assertEquals(compParams.weight,parsedParams.weight);
            }
        }

        childCount++;

        // Test RelativeLayout (gravity)
        {
            final RelativeLayout compLayout = (RelativeLayout) comp.getChildAt(childCount);
            final RelativeLayout parsedLayout = (RelativeLayout) parsed.getChildAt(childCount);
            // Tests android:gravity (getGravity() es Level 16):
            assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"), Gravity.BOTTOM|Gravity.RIGHT);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"),(Integer)TestUtil.getField(parsedLayout, "mGravity"));
                }
            });

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
            // Tests android:ignoreGravity que es un id (no hay get):
            assertPositive((Integer)TestUtil.getField(compLayout, "mIgnoreGravity"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mIgnoreGravity"), (Integer)TestUtil.getField(parsedLayout, "mIgnoreGravity"));

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
                assertEquals(compTextRules.length, parsedTextRules.length); // Por si acaso pero son todas las posibles rules
                for(int j = 0; j < compTextRules.length; j++)
                {
                    if (i == 0)
                    {
                        if (j == RelativeLayout.ALIGN_PARENT_BOTTOM)
                            assertEquals(compTextRules[j],RelativeLayout.TRUE);
                    }
                    else if (i == 1)
                    {
                        if (j == RelativeLayout.RIGHT_OF)
                            assertPositive(compTextRules[j]); // Es un id
                    }

                    assertEquals(compTextRules[j],parsedTextRules[j]);
                }

                if (i == 0)
                    assertEquals(compTextParams.alignWithParent,parsedTextParams.alignWithParent);
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
            // android:listSelector
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

            // Tests android:columnWidth (getColumnWidth es Level 16):
            // En teoría existe el atributo mColumnWidth pero el valor final puede no coincidir con el columnWidth especificado
            // porque es corregido dinámicamente
            assertEquals((Integer)TestUtil.getField(compLayout, "mRequestedColumnWidth"),ValueUtil.dpToPixelInt(30, res));
            assertEquals((Integer)TestUtil.getField(compLayout, "mRequestedColumnWidth"),(Integer)TestUtil.getField(parsedLayout, "mRequestedColumnWidth"));

            // Tests android:gravity (getGravity es Level 16)
            assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"), Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"),(Integer)TestUtil.getField(parsedLayout, "mGravity"));
                }
            });

            // Tests android:horizontalSpacing (getHorizontalSpacing es Level 16):
            assertEquals((Integer)TestUtil.getField(compLayout, "mHorizontalSpacing"),ValueUtil.dpToPixelInt(5, res));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mHorizontalSpacing"),(Integer)TestUtil.getField(parsedLayout, "mHorizontalSpacing"));
                }
            });
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

            // Tests android:verticalSpacing (getVerticalSpacing es Level 16):
            assertEquals((Integer)TestUtil.getField(compLayout, "mVerticalSpacing"),ValueUtil.dpToPixelInt(5, res));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mVerticalSpacing"),(Integer)TestUtil.getField(parsedLayout, "mVerticalSpacing"));
                }
            });
        }

        childCount++;

        // Test ListView
        {
            final ListView compLayout = (ListView) comp.getChildAt(childCount);
            final ListView parsedLayout = (ListView) parsed.getChildAt(childCount);

            // Test android:divider
            // Test visual: líneas rojas separadoras de items
            assertEqualsStrokeWidth((GradientDrawable) compLayout.getDivider(), ValueUtil.dpToPixelInt(0.9f, res));
            assertEquals((GradientDrawable) compLayout.getDivider(),(GradientDrawable) parsedLayout.getDivider());

            assertEquals(compLayout.getDividerHeight(),ValueUtil.dpToPixelInt(2, res));
            assertEquals(compLayout.getDividerHeight(),parsedLayout.getDividerHeight());
            // Test android:footerDividersEnabled (areFooterDividersEnabled es Level 19)
            assertFalse((Boolean)TestUtil.getField(compLayout, "mFooterDividersEnabled"));
            assertEquals((Boolean)TestUtil.getField(compLayout, "mFooterDividersEnabled"), (Boolean)TestUtil.getField(parsedLayout, "mFooterDividersEnabled"));
            // Test android:headerDividersEnabled (areHeaderDividersEnabled es Level 19)
            assertFalse((Boolean)TestUtil.getField(compLayout, "mHeaderDividersEnabled"));
            assertEquals((Boolean)TestUtil.getField(compLayout, "mHeaderDividersEnabled"),(Boolean)TestUtil.getField(parsedLayout, "mHeaderDividersEnabled"));

        }

        childCount++;

        // Test ExpandableListView
        {
            final ExpandableListView compLayout = (ExpandableListView) comp.getChildAt(childCount);
            final ExpandableListView parsedLayout = (ExpandableListView) parsed.getChildAt(childCount);

            // Test android:childDivider, no hay método get
            // Test visual: líneas rojas separadoras de items
            assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compLayout, "mChildDivider"), ValueUtil.dpToPixelInt(0.9f, res));
            assertEquals((GradientDrawable) TestUtil.getField(compLayout, "mChildDivider"), (GradientDrawable) TestUtil.getField(parsedLayout, "mChildDivider"));

            // Test android:childIndicator, no hay método get, si no se define devuelve null
            assertEqualsStrokeWidth((GradientDrawable) TestUtil.getField(compLayout, "mChildIndicator"), ValueUtil.dpToPixelInt(2f, res));
            assertEquals((GradientDrawable) TestUtil.getField(compLayout, "mChildIndicator"), (GradientDrawable) TestUtil.getField(parsedLayout, "mChildIndicator"));

            // Test android:childIndicatorLeft, no hay método get
            // No podemos testearlo porque por ej en 4.4.1 tras definir mChildIndicatorLeft y mChildIndicatorright
            // se llama a un método que los cambia de nuevo de una forma inexplicable, en 4.0.3 no hay tal llamada
            //assertPositive((Integer)TestUtil.getField(compLayout,"mChildIndicatorLeft"));
            //assertEquals((Integer)getField(compLayout,"mChildIndicatorLeft"),(Integer)getField(parsedLayout,"mChildIndicatorLeft"));
            // No testeamos android:childIndicatorRight pues tenemos idéntico problema que childIndicatorLeft

            // Test android:groupIndicator, no hay método get
            // No se como testear la igualdad de dos StateListDrawable
            assertNotNull((StateListDrawable)TestUtil.getField(compLayout, "mGroupIndicator"));
            assertNotNull((StateListDrawable)TestUtil.getField(parsedLayout, "mGroupIndicator"));

            // No testeamos android:indicatorLeft ni indicatorRight porque les pasa igual que a childIndicatorLeft
            //assertPositive((Integer)getField(compLayout,"mIndicatorLeft"));
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
            assertEquals((Integer)TestUtil.getField(compLayout, "mAnimationDuration"), 100);
            assertEquals((Integer)TestUtil.getField(compLayout, "mAnimationDuration"),(Integer)TestUtil.getField(parsedLayout, "mAnimationDuration"));
            assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"), Gravity.CENTER_VERTICAL);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"),(Integer)TestUtil.getField(parsedLayout, "mGravity"));
                }
            });
            assertEquals((Integer)TestUtil.getField(compLayout, "mSpacing"),ValueUtil.dpToPixelInt(50, res));
            assertEquals((Integer)TestUtil.getField(compLayout, "mSpacing"),(Integer)TestUtil.getField(parsedLayout, "mSpacing"));
            assertEquals((Float)TestUtil.getField(compLayout, "mUnselectedAlpha"), 0.6f);
            assertEquals((Float)TestUtil.getField(compLayout, "mUnselectedAlpha"),(Float)TestUtil.getField(parsedLayout, "mUnselectedAlpha"));
        }

        childCount++;

        // Test Spinner (dropdown)
        {
            final Spinner compLayout = (Spinner) comp.getChildAt(childCount);
            final Spinner parsedLayout = (Spinner) parsed.getChildAt(childCount);

            // Tests android:dropDownHorizontalOffset
            int compDropDownHorizontalOffset =   (Integer)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownHorizontalOffset"});
            int parsedDropDownHorizontalOffset = (Integer)TestUtil.getField(parsedLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownHorizontalOffset"});
            assertPositive(compDropDownHorizontalOffset);
            if (compDropDownHorizontalOffset != parsedDropDownHorizontalOffset)
                TestUtil.alertDialog(compLayout.getContext(),"Test failed in dropDownHorizontalOffset (" + compDropDownHorizontalOffset + " - " + parsedDropDownHorizontalOffset + "), don't worry it seems dropDownHorizontalOffset is reset after showing items, anyway this value seems not to have visual influence in this example");

            // Tests android:dropDownVerticalOffset
            assertPositive((Integer)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownVerticalOffset"}));
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownVerticalOffset"}), (Integer)TestUtil.getField(parsedLayout, new Class[]{Spinner.class, ListPopupWindow.class}, new String[]{"mPopup", "mDropDownVerticalOffset"}));
                }
            });

            // Tests android:dropDownWidth ( getDropDownWidth() es Level 16)
            assertEquals((Integer)TestUtil.getField(compLayout, "mDropDownWidth"),ValueUtil.dpToPixelInt(200, res));
            assertEquals((Integer)TestUtil.getField(compLayout, "mDropDownWidth"),(Integer)TestUtil.getField(parsedLayout, "mDropDownWidth"));

            // Tests android:gravity (no get en Level 15)
            assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"), Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            parsedLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
            {
                @Override
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8)
                {
                    // No se consolida hasta que se hace el Layout
                    assertEquals((Integer)TestUtil.getField(compLayout, "mGravity"),(Integer)TestUtil.getField(parsedLayout, "mGravity"));
                }
            });

            // Tests android:popupBackground
            assertEquals(((ColorDrawable)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class, PopupWindow.class}, new String[]{"mPopup", "mPopup", "mBackground"})).getColor(), 0xffeeee55);
            assertEquals((ColorDrawable)TestUtil.getField(compLayout, new Class[]{Spinner.class, ListPopupWindow.class, PopupWindow.class}, new String[]{"mPopup", "mPopup", "mBackground"}),(ColorDrawable)TestUtil.getField(parsedLayout, new Class[]{Spinner.class, ListPopupWindow.class, PopupWindow.class}, new String[]{"mPopup", "mPopup", "mBackground"}));
        }

        childCount++;

        // Test Spinner (dialog)
        {
            final Spinner compLayout = (Spinner) comp.getChildAt(childCount);
            final Spinner parsedLayout = (Spinner) parsed.getChildAt(childCount);

            assertEquals(compLayout.getPrompt(), "Sport List");
            assertEquals(compLayout.getPrompt(), parsedLayout.getPrompt());
        }

        childCount++;

        // Test AdapterViewAnimator y AdapterViewFlipper
        {
            final AdapterViewFlipper compLayout = (AdapterViewFlipper) comp.getChildAt(childCount);
            final AdapterViewFlipper parsedLayout = (AdapterViewFlipper) parsed.getChildAt(childCount);

            // AdapterViewAnimator

            assertTrue((Boolean)TestUtil.getField(compLayout,AdapterViewAnimator.class,"mAnimateFirstTime"));
            assertEquals((Boolean) TestUtil.getField(compLayout,AdapterViewAnimator.class,"mAnimateFirstTime"), (Boolean) TestUtil.getField(parsedLayout,AdapterViewAnimator.class,"mAnimateFirstTime"));

            assertNotNull(compLayout.getInAnimation());
            assertEquals(compLayout.getInAnimation(),parsedLayout.getInAnimation());

            assertTrue((Boolean)TestUtil.getField(compLayout,AdapterViewAnimator.class,"mLoopViews"));
            assertEquals((Boolean)TestUtil.getField(compLayout,AdapterViewAnimator.class,"mLoopViews"),(Boolean)TestUtil.getField(parsedLayout,AdapterViewAnimator.class,"mLoopViews"));

            assertNotNull(compLayout.getOutAnimation());
            assertEquals(compLayout.getOutAnimation(),parsedLayout.getOutAnimation());

            // AdapterViewFlipper
            assertTrue(compLayout.isAutoStart());
            assertEquals(compLayout.isAutoStart(),parsedLayout.isAutoStart());

            // android:flipInterval  (getFlipInterval es Level 16)
            assertEquals((Integer)TestUtil.getField(compLayout,"mFlipInterval"),2000);
            assertEquals((Integer)TestUtil.getField(compLayout,"mFlipInterval"),(Integer)TestUtil.getField(parsedLayout,"mFlipInterval"));
        }

        childCount++;

        // Test AnalogClock
        {
            final AnalogClock compLayout = (AnalogClock) comp.getChildAt(childCount);
            final AnalogClock parsedLayout = (AnalogClock) parsed.getChildAt(childCount);

            // android:dial
            assertNotNull((Drawable)TestUtil.getField(compLayout, "mDial"));
            assertEquals((Drawable)TestUtil.getField(compLayout,"mDial"),(Drawable)TestUtil.getField(parsedLayout,"mDial"));

            // android:hand_hour
            assertNotNull((Drawable) TestUtil.getField(compLayout, "mHourHand"));
            assertEquals((Drawable)TestUtil.getField(compLayout,"mHourHand"),(Drawable)TestUtil.getField(parsedLayout,"mHourHand"));

            // android:hand_minute
            assertNotNull((Drawable)TestUtil.getField(compLayout,"mMinuteHand"));
            assertEquals((Drawable)TestUtil.getField(compLayout,"mMinuteHand"),(Drawable)TestUtil.getField(parsedLayout,"mMinuteHand"));

        }


//         System.out.println("\n\n\nDEFAULT VALUE: " + compLayout.getColumnCount() + " " + parsedLayout.getColumnCount());
        //System.out.println("\n\n\n");
    }


}
