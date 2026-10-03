package ru.academits.phonebookspringboot.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.academits.phonebookspringboot.data.Contact;
import ru.academits.phonebookspringboot.service.ContactsService;

import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class Scheduler {
    private final ContactsService contactsService;
    private final Random random = new Random();

    public Scheduler(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @Scheduled(fixedRate = 10000)
    public void deleteRandomContact() {
        List<Contact> contacts = contactsService.get("");

        if (contacts.isEmpty()) {
            return;
        }

        Contact contact = contacts.get(random.nextInt(contacts.size()));
        contactsService.delete(contact.getId());

        log.info("Random contact deleted: {}", contact);
    }
}