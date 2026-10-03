package ru.academits.phonebookspringboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {
    private Integer id;

    private String surname;

    private String name;

    private String phone;
}