package ru.academits.phonebookspringboot.repository;

import org.springframework.stereotype.Repository;
import ru.academits.phonebookspringboot.data.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
            return contacts.stream()
                    .filter(contact -> getFullContactString(contact).contains(term.trim().toUpperCase()))
                    .map(Contact::new)
                    .toList();
        }
    }

    @Override
    public void create(Contact contact) {
        synchronized (contacts) {
            int id = currentContactId.getAndIncrement();
            contacts.add(new Contact(id, contact.getSurname(), contact.getName(), contact.getPhone()));
        }
    }

    @Override
    public void update(Contact contact) {
        synchronized (contacts) {
            Contact repositoryContact = contacts.stream()
                    .filter(c -> c.getId() == contact.getId())
                    .findFirst()
                    .orElse(null);

            if (repositoryContact == null) {
                throw new IllegalArgumentException("Контакт " + contact.getId() + " не найден");
            }

            repositoryContact.setSurname(contact.getSurname());
            repositoryContact.setName(contact.getName());
            repositoryContact.setPhone(contact.getPhone());
        }
    }

    @Override
    public void delete(int contactId) {
        synchronized (contacts) {
            boolean removed = contacts.removeIf(contact -> contact.getId() == contactId);

            if (!removed) {
                throw new IllegalArgumentException("Контакт " + contactId + " не найден");
            }
        }
    }

    @Override
    public void delete(List<Integer> contactIds) {
        synchronized (contacts) {
            boolean removed = contacts.removeIf(contact -> contactIds.contains(contact.getId()));

            if (!removed) {
                throw new IllegalArgumentException("Контакты не найдены");
            }
        }
    }

    @Override
    public boolean isPhoneExists(String phone, int contactId) {
        String upperCasePhone = phone.toUpperCase();

        synchronized (contacts) {
            return contacts.stream()
                    .anyMatch(contact ->
                            contact.getId() != contactId && contact.getPhone().toUpperCase().equals(upperCasePhone));
        }
    }

    private String getFullContactString(Contact contact) {
        return (contact.getSurname() + " " + contact.getName() + " " + contact.getPhone()).toUpperCase();
    }
}