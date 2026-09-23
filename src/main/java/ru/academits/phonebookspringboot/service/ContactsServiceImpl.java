package ru.academits.phonebookspringboot.service;

import org.springframework.stereotype.Service;
import ru.academits.phonebookspringboot.data.Contact;
import ru.academits.phonebookspringboot.repository.ContactsRepository;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository contactsRepository;

    public ContactsServiceImpl(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    @Override
    public List<Contact> getAll(String term) {
        return contactsRepository.getAll(term);
    }

    @Override
    public void create(Contact contact) {
        contactsRepository.create(contact);
    }

    @Override
    public void update(Contact contact) {
        contactsRepository.update(contact);
    }

    @Override
    public void delete(int contactId) {
        contactsRepository.delete(contactId);
    }

    @Override
    public void delete(List<Integer> contactIds) {
        contactsRepository.delete(contactIds);
    }

    @Override
    public boolean isPhoneExists(String phone, int contactId) {
        return contactsRepository.isPhoneExists(phone, contactId);
    }
}
