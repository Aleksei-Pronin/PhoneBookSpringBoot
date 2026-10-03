package ru.academits.phonebookspringboot.converter;

import org.springframework.stereotype.Service;
import ru.academits.phonebookspringboot.data.Contact;
import ru.academits.phonebookspringboot.dto.ContactDto;

@Service
public class ContactDtoToContactConverter implements Converter<ContactDto, Contact> {
    @Override
    public Contact convert(ContactDto contactDto) {
        Contact contact = new Contact();

        contact.setSurname(contactDto.getSurname());
        contact.setName(contactDto.getName());
        contact.setPhone(contactDto.getPhone());

        return contact;
    }
}
