package ru.academits.phonebookspringboot.converter;

import org.springframework.stereotype.Service;
import ru.academits.phonebookspringboot.data.Contact;
import ru.academits.phonebookspringboot.dto.ContactDto;

@Service
public class ContactToContactDtoConverter implements Converter<Contact, ContactDto> {
    @Override
    public ContactDto convert(Contact contact) {
        ContactDto contactDto = new ContactDto();

        contactDto.setId(contact.getId());
        contactDto.setSurname(contact.getSurname());
        contactDto.setName(contact.getName());
        contactDto.setPhone(contact.getPhone());

        return contactDto;
    }
}