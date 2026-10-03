package ru.academits.phonebookspringboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PhoneBookController {
    private final ContactsService contactsService;
    private final ContactToContactDtoConverter contactToContactDtoConverter;
    private final ContactDtoToContactConverter contactDtoToContactConverter;

    @GetMapping
    public List<ContactDto> getContacts(@RequestParam(required = false) String term) {
        return contactToContactDtoConverter.convert(contactsService.get(term));
    }

    @PostMapping
    public BaseResponse createContact(@RequestBody ContactDto contactDto) {
        try {
            contactsService.create(contactDtoToContactConverter.convert(contactDto));
        } catch (IllegalArgumentException e) {
            log.warn("Create failed, id={}: {}", contactDto.getId(), e.getMessage());
            return BaseResponse.error(e.getMessage());
        }

        return BaseResponse.success();
    }

    @PutMapping("/{id}")
    public BaseResponse updateContact(@PathVariable int id, @RequestBody ContactDto contactDto) {
        try {
            contactsService.update(id, contactDtoToContactConverter.convert(contactDto));
        } catch (IllegalArgumentException e) {
            log.warn("Update failed, id={}: {}", id, e.getMessage());
            return BaseResponse.error(e.getMessage());
        }

        return BaseResponse.success();
    }

    @DeleteMapping("/{id}")
    public BaseResponse deleteContact(@PathVariable int id) {
        try {
            contactsService.delete(id);
        } catch (IllegalArgumentException e) {
            log.warn("Delete failed, id={}: {}", id, e.getMessage());
            return BaseResponse.error(e.getMessage());
        }

        return BaseResponse.success();
    }

    @DeleteMapping
    public BaseResponse deleteContacts(@RequestBody List<Integer> contactIds) {
        try {
            contactsService.delete(contactIds);
        } catch (IllegalArgumentException e) {
            log.warn("Bulk delete failed, ids={}: {}", contactIds, e.getMessage());
            return BaseResponse.error(e.getMessage());
        }

        return BaseResponse.success();
    }
}