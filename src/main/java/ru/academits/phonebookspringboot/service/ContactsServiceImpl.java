package ru.academits.phonebookspringboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.academits.phonebookspringboot.data.BaseResponse;
import ru.academits.phonebookspringboot.data.Contact;
import ru.academits.phonebookspringboot.repository.ContactsRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactsServiceImpl implements ContactsService {
    private final ContactsRepository contactsRepository;

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
    public BaseResponse create(Contact contact) {
        BaseResponse validationResult = validateAndNormalize(contact, 0);
        return !validationResult.isSuccess() ? validationResult : contactsRepository.create(contact);
    }

    @Override
    public BaseResponse update(Contact contact, int contactId) {
        BaseResponse validationResult = validateAndNormalize(contact, contactId);
        return !validationResult.isSuccess() ? validationResult : contactsRepository.update(contact, contactId);
    }

    @Override
    public BaseResponse delete(int contactId) {
        return contactsRepository.delete(contactId);
    }

    @Override
    public BaseResponse delete(List<Integer> contactIds) {
        return contactsRepository.delete(contactIds);
    }

    private BaseResponse validateAndNormalize(Contact contact, int contactId) {
        String surname = normalize(contact.getSurname());
        String name = normalize(contact.getName());
        String phone = normalize(contact.getPhone());

        if (surname.isEmpty()) {
            log.warn("Surname is empty");
            return BaseResponse.error("Необходимо заполнить фамилию");
        }

        if (name.isEmpty()) {
            log.warn("Name is empty");
            return BaseResponse.error("Необходимо заполнить имя");
        }

        if (phone.isEmpty()) {
            log.warn("Phone is empty");
            return BaseResponse.error("Необходимо заполнить номер телефона");
        }

        if (contactsRepository.isPhoneExists(phone, contactId)) {
            log.warn("Attempt to use existing phone: {}", phone);
            return BaseResponse.error("Уже есть другой контакт с таким номером");
        }

        contact.setSurname(surname);
        contact.setName(name);
        contact.setPhone(phone);

        return BaseResponse.success();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}