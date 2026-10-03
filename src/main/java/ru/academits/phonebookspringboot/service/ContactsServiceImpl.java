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

    public List<Contact> get(String term) {
        if (term == null || term.isBlank()) {
            List<Contact> contacts = contactsRepository.getAll();
            log.debug("Loaded {} contact(s)", contacts.size());
            return contacts;
        }

        List<Contact> contacts = contactsRepository.search(term.trim());
        log.debug("Found {} contact(s)", contacts.size());
        return contacts;
    }

    @Override
    public void create(Contact contact) {
        String validationMessage = validateAndNormalize(contact);

        if (validationMessage != null) {
            log.warn("Validation failed on create: {}", validationMessage);
            throw new IllegalArgumentException(validationMessage);
        }

        if (isPhoneExists(contact.getPhone(), 0)) {
            log.warn("Attempt to create contact with existing phone: {}", contact.getPhone());
            throw new IllegalArgumentException("Уже есть другой контакт с таким номером");
        }

        contactsRepository.create(contact);
        log.info("Contact created: {}", contact);
    }

    @Override
    public void update(int id, Contact contact) {
        String validationMessage = validateAndNormalize(contact);

        if (validationMessage != null) {
            log.warn("Validation failed on update, id={}: {}", id, validationMessage);
            throw new IllegalArgumentException(validationMessage);
        }

        if (isPhoneExists(contact.getPhone(), id)) {
            log.warn("Attempt to update contactDto id={} to existing phone: {}", id, contact.getPhone());
            throw new IllegalArgumentException("Уже есть другой контакт с таким номером");
        }

        contact.setId(id);

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

    private boolean isPhoneExists(String phone, int contactId) {
        boolean exists = contactsRepository.isPhoneExists(phone, contactId);
        log.debug("Phone check, phone={}, contactId={}, exists={}", phone, contactId, exists);
        return exists;
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
