package com.miro.hometask.controller;

import com.miro.hometask.exceptions.WidgetNotFoundException;
import com.miro.hometask.models.Widget;
import com.miro.hometask.service.WidgetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;


@RestController
@RequestMapping("/widgets")
class WidgetController {

    @Autowired
    WidgetService widgetService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createWidget(@RequestBody Widget widget){
        //adding some checks
        if(widget.getHeight() == null || widget.getWidth() == null ||
                widget.getX() == null || widget.getY() == null ){

            //attributes must be specified before creation of widget
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(widgetService.create(widget).toString());
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Widget> getWidget(@PathVariable String id) {
        try{
            Long.valueOf(id);
        }
        catch (NumberFormatException e){
            return ResponseEntity.badRequest().build();
        }
        Widget w =  widgetService.getWidget(Long.valueOf(id));
        return new ResponseEntity<Widget>(w, HttpStatus.OK);
    }


    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Widget> updateWidget(@PathVariable String id, @RequestBody Widget widget) {
        try{
            Long.valueOf(id);
        }
        catch (NumberFormatException e){
            return ResponseEntity.badRequest().build();
        }
        if(widget.getHeight() == null || widget.getWidth() == null ||
                widget.getX() == null || widget.getY() == null ||
                widget.getZ() == null ){

                //deleting of widget attributes is not allowed
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(widgetService.updateWidget(Long.valueOf(id), widget));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Widget> removeWidget(@PathVariable String id) {
        try{
            Long.valueOf(id);
        }
        catch (NumberFormatException e){
            return ResponseEntity.badRequest().build();
        }

        widgetService.remove(Long.valueOf(id));
        return ResponseEntity.noContent().build();
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Widget>> getAllWidgets(){
        return  ResponseEntity.ok(widgetService.retrieveAll());
    }


    /*@ExceptionHandler({ WidgetNotFoundException.class})
    public void handleWidgetNotFoundException(HttpServletResponse response) throws IOException {

       response.sendError(HttpStatus.NOT_FOUND.value());
        //return ResponseEntity.notFound().build();
    }*/


    @ExceptionHandler({WidgetNotFoundException.class, HttpMessageNotWritableException.class})
    public ResponseEntity<String> customHandleNotFound(Exception ex, WebRequest request) {

      //  CustomErrorResponse errors = new CustomErrorResponse();
       // errors.setTimestamp(LocalDateTime.now());
       // errors.setError(ex.getMessage());
       // errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);

    }

   /* @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

    class CustomErrorResponse{
        private int status;
        private String error;
    }*/

}
