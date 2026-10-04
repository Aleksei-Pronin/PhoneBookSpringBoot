package ru.academits.phonebookspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.academits.phonebookspringboot.converter.ContactDtoToContactConverter;
import ru.academits.phonebookspringboot.converter.ContactToContactDtoConverter;
import ru.academits.phonebookspringboot.data.BaseResponse;
import ru.academits.phonebookspringboot.dto.ContactDto;
import ru.academits.phonebookspringboot.service.ContactsService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactsService contactsService;
    private final ContactToContactDtoConverter contactToContactDtoConverter;
    private final ContactDtoToContactConverter contactDtoToContactConverter;

    @GetMapping
    public List<ContactDto> getContacts(@RequestParam(required = false) String term) {
        return contactToContactDtoConverter.convert(contactsService.get(term));
    }

    @PostMapping
    public BaseResponse createContact(@RequestBody ContactDto contact) {
        return contactsService.create(contactDtoToContactConverter.convert(contact));
    }

    @PutMapping("/{id}")
    public BaseResponse updateContact(@RequestBody ContactDto contact, @PathVariable int id) {
        return contactsService.update(contactDtoToContactConverter.convert(contact), id);
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteContact(@PathVariable int id) {
        return contactsService.delete(id);
    }

    @DeleteMapping
    public BaseResponse deleteContacts(@RequestBody List<Integer> contactIds) {
        return contactsService.delete(contactIds);
    }
}