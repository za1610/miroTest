package com.miro.hometask.service;


import com.miro.hometask.exceptions.WidgetNotFoundException;
import com.miro.hometask.models.Widget;
import com.miro.hometask.repository.WidgetRepository;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WidgetService {

    private static Integer maxZIndexUsed = Integer.MIN_VALUE;


    @Autowired
    WidgetRepository widgetRepository;

    public Long create(Widget widget) {
        //might also add checks for overflow, but skipped for test task
        if(widget.getZ() == null){
            widget.setZ(maxZIndexUsed+1);
            maxZIndexUsed++;
        }
        else{
            checkAndUpdateZIndexes(widget.getZ());
        }

        widget.setLastModificationDate(new Date());
        Widget newWidget = widgetRepository.save(widget);

        //updating maxZ being used every time on Zindex save operation(skipped for update)
        maxZIndexUsed = maxZIndexUsed < widget.getZ() ? widget.getZ() : maxZIndexUsed;

        return newWidget.getId();
    }

    public Widget getWidget(Long id) {
            Optional<Widget> w = widgetRepository.findById(id);
            if(!w.isPresent()){
                throw new WidgetNotFoundException(id.toString());
            }
            return w.get();

    }

    public Widget updateWidget(Long id, Widget widget) {
        Widget currW = getWidget(id);
        if(currW == null){
            throw new WidgetNotFoundException(id.toString());
        }
        currW.setHeight(widget.getHeight());
        currW.setWidth(widget.getWidth());
        currW.setY(widget.getY());
        currW.setX(widget.getX());
        //might be necessary to add check if the z index is already in use
        // if yes throw exception, or move forward other widgets
        //kept it simple within the scope of the test task
        currW.setZ(widget.getZ());
        currW.setLastModificationDate(new Date());

        widgetRepository.save(currW);

        return currW;

    }


    public void remove(Long id) {
        Optional<Widget> w = widgetRepository.findById(id);
        if(!w.isPresent()){
            throw new WidgetNotFoundException(id.toString());
        }
        widgetRepository.deleteById(id);
    }

    public List<Widget> retrieveAll() {
        return widgetRepository.findAllByOrderByZAsc();

    }


    private void checkAndUpdateZIndexes(Integer z){
        //check if specified z index is used and increment others if yes
        boolean increment = false;
        for(Widget w : this.retrieveAll()){
            if(w.getZ() == z){
                increment=true;
            }

            if(increment) {
                w.setZ(w.getZ()+1);
                w.setLastModificationDate(new Date());
                widgetRepository.save(w);
                maxZIndexUsed = maxZIndexUsed < w.getZ() ? w.getZ() : maxZIndexUsed;
            }
        }

    }

}
