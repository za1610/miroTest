package com.miro.hometask;


import com.miro.hometask.exceptions.WidgetNotFoundException;
import com.miro.hometask.models.Widget;
import com.miro.hometask.service.WidgetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WidgetServiceUnitTests {

    @Autowired
    private WidgetService widgetService;


    @Test
    public void testCreate() {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        Long id = widgetService.create(w);
        assertTrue(id > 0);
    }

    @Test
    public void testCreateWithNullZ() {

        Widget wPrevMax = new Widget(5, 5, 100, 1.0, 2.0);
        widgetService.create(wPrevMax);

        Widget w = new Widget(5, 5, null, 1.0, 2.0);
        Long id = widgetService.create(w);

        assertTrue(widgetService.getWidget(id).getZ() == 101);
    }

    @Test
    public void testCreateWithShift() {
        Widget w1 = new Widget(5, 5, 5, 1.0, 2.0);
        Long id1 = widgetService.create(w1);

        Widget w2 = new Widget(5, 5, 5, 1.0, 2.0);
        Long id2 = widgetService.create(w2);

        widgetService.getWidget(id1);
        assertTrue(widgetService.getWidget(id1).getZ() == 6);
    }

    @Test
    public void testGetExistingWidget() {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        Long id = widgetService.create(w);

        Widget widgetRetrieved = widgetService.getWidget(id);
        assertTrue(Long.compare(widgetRetrieved.getId(),id) == 0);

    }

    @Test(expected = WidgetNotFoundException.class)
    public void testGetNonExistingWidget() {
        Widget widgetRetrieved = widgetService.getWidget(Long.valueOf(123));
    }




    @Test
    public void testUpdate() {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        Long id = widgetService.create(w);

        //updated X index to 7
        Widget w2 = new Widget(7, 5, 5, 1.0, 2.0);
        widgetService.updateWidget(id, w2);

        assertTrue(widgetService.getWidget(id).getX() == 7);

    }

    @Test(expected = WidgetNotFoundException.class)
    public void testRemoveNonExisting() {
        widgetService.remove(Long.valueOf(123));
    }

    @Test(expected = WidgetNotFoundException.class)
    public void testRemoveExisting() {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        Long id = widgetService.create(w);
        Widget retrievedWidget = widgetService.getWidget(id);
        assertNotNull(retrievedWidget);

        widgetService.remove(id);

        widgetService.getWidget(id);

    }



}
