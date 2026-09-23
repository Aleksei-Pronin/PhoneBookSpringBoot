package ru.academits.phonebookspringboot.controller;

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
import ru.academits.phonebookspringboot.data.ResponseDto;
import ru.academits.phonebookspringboot.service.ContactsService;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class PhoneBookController {
    private final ContactsService contactsService;

    public PhoneBookController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping
    public List<Contact> getContacts(@RequestParam(required = false) String term) {
        return contactsService.getAll(term);
    }

    @PostMapping
    public ResponseDto createContact(@RequestBody Contact contact) {
        String validationMessage = validateAndNormalize(contact);

        if (validationMessage != null) {
            return new ResponseDto(false, validationMessage);
        }

        if (contactsService.isPhoneExists(contact.getPhone(), 0)) {
            return new ResponseDto(false, "Уже есть другой контакт с таким номером");
        }

        contactsService.create(contact);

        return new ResponseDto(true, null);
    }

    @PutMapping("/{id}")
    public ResponseDto updateContact(@PathVariable int id, @RequestBody Contact contact) {
        String validationMessage = validateAndNormalize(contact);

        if (validationMessage != null) {
            return new ResponseDto(false, validationMessage);
        }

        if (contactsService.isPhoneExists(contact.getPhone(), id)) {
            return new ResponseDto(false, "Уже есть другой контакт с таким номером");
        }

        contact.setId(id);

        try {
            contactsService.update(contact);
        } catch (IllegalArgumentException e) {
            return new ResponseDto(false, e.getMessage());
        }

        return new ResponseDto(true, null);
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteContact(@PathVariable int id) {
        try {
            contactsService.delete(id);
        } catch (IllegalArgumentException e) {
            return new ResponseDto(false, e.getMessage());
        }

        return new ResponseDto(true, null);
    }

    @DeleteMapping
    public ResponseDto deleteContacts(@RequestBody List<Integer> contactIds) {
        try {
            contactsService.delete(contactIds);
        } catch (IllegalArgumentException e) {
            return new ResponseDto(false, e.getMessage());
        }

        return new ResponseDto(true, null);
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