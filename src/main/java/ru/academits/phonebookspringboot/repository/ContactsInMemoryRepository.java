package ru.academits.phonebookspringboot.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.academits.phonebookspringboot.data.BaseResponse;
import ru.academits.phonebookspringboot.data.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Repository
public class ContactsInMemoryRepository implements ContactsRepository {
    private final List<Contact> contacts = new ArrayList<>();
    private final AtomicInteger currentContactId = new AtomicInteger(1);

    @Override
    public List<Contact> getAll() {
        synchronized (contacts) {
            return contacts.stream()
                    .map(Contact::new)
                    .toList();
        }
    }

    @Override
    public List<Contact> search(String term) {
        synchronized (contacts) {
            String upperCaseTerm = term.trim().toUpperCase();

            return contacts.stream()
                    .filter(contact ->
                            contact.getSurname().toUpperCase().contains(upperCaseTerm)
                                    || contact.getName().toUpperCase().contains(upperCaseTerm)
                                    || contact.getPhone().toUpperCase().contains(upperCaseTerm))
                    .map(Contact::new)
                    .toList();
        }
    }

    @Override
    public BaseResponse create(Contact contact) {
        synchronized (contacts) {
            int id = currentContactId.getAndIncrement();
            contacts.add(new Contact(id, contact.getSurname(), contact.getName(), contact.getPhone()));

            log.info("Contact created: {}", contact);
            return BaseResponse.success();
        }
    }

    @Override
    public BaseResponse update(Contact contact, int contactId) {
        synchronized (contacts) {
            Contact repositoryContact = contacts.stream()
                    .filter(c -> c.getId() == contactId)
                    .findFirst()
                    .orElse(null);

            if (repositoryContact == null) {
                log.warn("Contact not found, contactId={}", contactId);
                return BaseResponse.error("Контакт не найден");
            }

            repositoryContact.setSurname(contact.getSurname());
            repositoryContact.setName(contact.getName());
            repositoryContact.setPhone(contact.getPhone());

            log.info("Contact updated: {}", contact);
            return BaseResponse.success();
        }
    }

    @Override
    public BaseResponse delete(int contactId) {
        synchronized (contacts) {
            boolean removed = contacts.removeIf(contact -> contact.getId() == contactId);

            if (!removed) {
                log.warn("Contact not found, id={}", contactId);
                return BaseResponse.error("Контакт не найден");
            }

            log.info("Contact deleted, id={}", contactId);
            return BaseResponse.success();
        }
    }

    @Override
    public BaseResponse delete(List<Integer> contactIds) {
        synchronized (contacts) {
            boolean removed = contacts.removeIf(contact -> contactIds.contains(contact.getId()));

            if (!removed) {
                log.warn("Contacts not found, ids={}", contactIds);
                return BaseResponse.error("Контакты не найдены");
            }

            log.info("Contacts deleted, ids={}", contactIds);
            return BaseResponse.success();
        }
    }

    @Override
    public boolean isPhoneExists(String phone) {
        synchronized (contacts) {
            return contacts.stream()
                    .anyMatch(contact -> contact.getPhone().equalsIgnoreCase(phone));
        }
    }

    @Override
    public boolean isPhoneExists(String phone, int excludedId) {
        synchronized (contacts) {
            return contacts.stream()
                    .anyMatch(contact ->
                            contact.getId() != excludedId && contact.getPhone().equalsIgnoreCase(phone));
        }
    }
}