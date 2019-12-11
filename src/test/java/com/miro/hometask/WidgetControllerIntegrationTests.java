package com.miro.hometask;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.miro.hometask.models.Widget;
import com.miro.hometask.service.WidgetService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WidgetControllerIntegrationTests {


    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    private RestTemplate patchRestTemplate;


    HttpHeaders headers = new HttpHeaders();

    private List<Widget> widgets;
    @Autowired
    private WidgetService widgetService;


    @Before
    public void setup() throws Exception {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        widgetService.create(w);

        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    }



    @Test
    public void testCreateWidget() throws Exception {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        HttpEntity<Widget> entity = new HttpEntity<Widget>(w, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                getURL("/widgets"), HttpMethod.POST, entity, String.class);
        assertTrue(response.getStatusCodeValue() == 200);
    }


    @Test
    public void testGetExistingWidget() throws Exception {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        Long id = widgetService.create(w);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<Widget> response = restTemplate.exchange(
                getURL("/widgets/" + id), HttpMethod.GET, entity, Widget.class);
        assertEquals(id.toString(), response.getBody().getId().toString());
    }



    @Test
    public void testUpdateWidget() throws Exception {
        Widget w = new Widget(5, 5, 5, 1.0, 2.0);
        Long id = widgetService.create(w);

        //z val changed to 10
        Widget wToUpdate = new Widget(5, 5, 10, 1.0, 2.0);
        HttpEntity<Widget> entity = new HttpEntity<Widget>(wToUpdate, headers);
        ResponseEntity<Widget> response = patchRestTemplate.exchange(
                getURL("/widgets/" + id), HttpMethod.PATCH, entity, Widget.class);
        assertEquals("10", response.getBody().getZ().toString());
    }

    @Test
    public void testRemoveExistingWidget() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<Widget> response = restTemplate.exchange(
                getURL("/widgets/1"), HttpMethod.DELETE, entity, Widget.class);
        assertEquals(response.getStatusCodeValue(), 204);
    }

    @Test
    public void testRemoveNonExistingWidget() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity response = restTemplate.exchange(
                getURL("/widgets/111"), HttpMethod.DELETE, entity, String.class);
        assertEquals(response.getStatusCodeValue(), 404);
    }

    @Test
    public void testGetAllWidgetsMoreThanZero() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<List<Widget>> response = restTemplate.exchange(
                getURL("/widgets/"), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Widget>>() {
                });
        assertTrue(response.getBody().size() > 0);
     }



    private String getURL(String uri) {
        return "http://localhost:" + port + uri;
    }

}
