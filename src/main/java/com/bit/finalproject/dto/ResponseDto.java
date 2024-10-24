package com.bit.finalproject.dto;

<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;
=======
import lombok.*;

>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70
import org.springframework.data.domain.Page;


import java.util.List;

@Getter
@Setter
public class ResponseDto<T> {
    private T item;
    private List<T> items;
    private Page<T> pageItems;
    private int statusCode;
    private String statusMessage;
}
