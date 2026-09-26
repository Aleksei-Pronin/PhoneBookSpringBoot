package ru.academits.phonebookspringboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.academits.phonebookspringboot.data.Contact;
import ru.academits.phonebookspringboot.repository.ContactsRepository;

import java.util.List;

@Service
@Slf4j
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository contactsRepository;

    public ContactsServiceImpl(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    @Override
    public List<Contact> getAll() {
        List<Contact> contacts = contactsRepository.getAll();
        log.debug("Loaded {} contact(s)", contacts.size());
        return contacts;
    }

    @Override
    public List<Contact> search(String term) {
        List<Contact> contacts = contactsRepository.search(term);
        log.info("Found {} contact(s)", contacts.size());
        return contacts;
    }

    @Override
    public void create(Contact contact) {
        contactsRepository.create(contact);
        log.info("Contact created: {}", contact);
    }

    @Override
    public void update(Contact contact) {
        contactsRepository.update(contact);
        log.info("Contact updated: {}", contact);
    }

    @Override
    public void delete(int contactId) {
        contactsRepository.delete(contactId);
        log.info("Contact deleted, id={}", contactId);
    }

    @Override
    public void delete(List<Integer> contactIds) {
        contactsRepository.delete(contactIds);
        log.info("Contacts deleted, ids={}", contactIds);
    }

    @Override
    public boolean isPhoneExists(String phone, int contactId) {
        boolean exists = contactsRepository.isPhoneExists(phone, contactId);
        log.debug("Phone check, phone={}, contactId={}, exists={}", phone, contactId, exists);
        return exists;
    }
}
