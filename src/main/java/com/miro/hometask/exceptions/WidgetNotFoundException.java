package com.miro.hometask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WidgetNotFoundException extends RuntimeException{

    public WidgetNotFoundException(String message) {
        super(String.format("Widget with id is %s not found ", message));
    }
}
