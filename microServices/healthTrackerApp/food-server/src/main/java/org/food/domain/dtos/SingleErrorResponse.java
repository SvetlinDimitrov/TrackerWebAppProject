package org.food.domain.dtos;

import lombok.Getter;

@Getter
public class SingleErrorResponse{
    private String message;

    public SingleErrorResponse(String message){
        this.message = message;
    }
}
