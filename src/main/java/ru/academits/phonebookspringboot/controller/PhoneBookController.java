package ru.academits.phonebookspringboot.controller;

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
import ru.academits.phonebookspringboot.data.Contact;
import ru.academits.phonebookspringboot.data.BaseResponse;
import ru.academits.phonebookspringboot.service.ContactsService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@Slf4j
public class PhoneBookController {
    private final ContactsService contactsService;

    public PhoneBookController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping
    public List<Contact> getContacts(@RequestParam(required = false) String term) {
        if (term == null || term.isBlank()) {
            return contactsService.getAll();
        }

        return contactsService.search(term);
    }

    @PostMapping
    public BaseResponse createContact(@RequestBody Contact contact) {
        String validationMessage = validateAndNormalize(contact);

        if (validationMessage != null) {
            log.warn("Validation failed on create: {}", validationMessage);
            return BaseResponse.error(validationMessage);
        }

        if (contactsService.isPhoneExists(contact.getPhone(), 0)) {
            log.warn("Attempt to create contact with existing phone: {}", contact.getPhone());
            return BaseResponse.error("Уже есть другой контакт с таким номером");
        }

        contactsService.create(contact);

        return BaseResponse.success();
    }

    @PutMapping("/{id}")
    public BaseResponse updateContact(@PathVariable int id, @RequestBody Contact contact) {
        String validationMessage = validateAndNormalize(contact);

        if (validationMessage != null) {
            log.warn("Validation failed on update, id={}: {}", id, validationMessage);
            return BaseResponse.error(validationMessage);
        }

        if (contactsService.isPhoneExists(contact.getPhone(), id)) {
            log.warn("Attempt to update contact id={} to existing phone: {}", id, contact.getPhone());
            return BaseResponse.error("Уже есть другой контакт с таким номером");
        }

        contact.setId(id);

        try {
            contactsService.update(contact);
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

    private String validateAndNormalize(Contact contact) {
        String surname = normalize(contact.getSurname());
        String name = normalize(contact.getName());
        String phone = normalize(contact.getPhone());

        if (surname.isEmpty()) {
            return "Необходимо заполнить фамилию";
        }

        if (name.isEmpty()) {
            return "Необходимо заполнить имя";
        }

        if (phone.isEmpty()) {
            return "Необходимо заполнить номер телефона";
        }

        contact.setSurname(surname);
        contact.setName(name);
        contact.setPhone(phone);

        return null;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}