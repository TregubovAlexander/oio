package ru.elsu.oio.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.elsu.oio.Url;
import ru.elsu.oio.controller.exeption.ImageUploadException;
import ru.elsu.oio.dto.ApiError;
import ru.elsu.oio.controller.exeption.IncorrectUserDataException;
import ru.elsu.oio.dao.ChildrenDao;
import ru.elsu.oio.dao.SprDolDao;
import ru.elsu.oio.dto.*;
import ru.elsu.oio.entity.*;
import ru.elsu.oio.services.PersonService;
import ru.elsu.oio.utils.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class PersonRestController {

    @Autowired
    private PersonService personService;

    @Autowired
    private ChildrenDao childrenDao;

    @Autowired
    private SprDolDao sprDolDao;

    // Папка с файлами (задана в параметрах)
    @Value("${files.path}")
    private String filesPath;

    //region === Список всех сотрудников отдела =================================================================================
    @GetMapping(Url.PERSONS_API)
    public List<PersonDto> getAllPersons() {
        List<Person> personList = personService.getAllInitialized();
        List<PersonDto> personDtoList = new ArrayList<>();
        for (Person p : personList) {
            personDtoList.add(new PersonDto(p));
        }
        return personDtoList;
    }
    //endregion


    //region === Инормация о конкретном сотруднике с идентификатором id =========================================================
    @GetMapping(Url.PERSON_API)
    public ResponseEntity<Object> getPersonById(@PathVariable Long id) {

        // Получаем сотрудника по id
        Person person = personService.getById(id);
        if (person == null) {

            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Сотрудник не найден", "Сотрудник с ID= " + id + " не найден");
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        return new ResponseEntity<Object>(new PersonDto(person), HttpStatus.OK);

    }
    //endregion


    //region === Проверка и коррекция присланного объекта personDto (ФИО, ДР, пол) ==============================================

    /**
     * Проверяем присланные данные на корректность, учитывая обязательные поля.
     * Исправляем формат данных.
     *
     * @param personDto данные присылаются в формате JSON
     * @param required  true - учитываем обязательные для заполения поля, требуем их наличие
     *                  false - не учитываем обязательные поля
     * @throws IncorrectUserDataException
     */
    private void dtoCheckAndCorrect(PersonDto personDto, boolean required) throws IncorrectUserDataException {
        String s = null;
        //region ФИО
        s = personDto.getSurname();
        if (s != null) {
            if (Util.surnameIsValid(s)) {
                personDto.setSurname(Util.surnameCase(s));
            } else {
                throw new IncorrectUserDataException("Фамилия указана неверно: разрешены только русские буквы и один дефис или пробел в качестве разделителя, длина ограничена 30 символами");
            }
        } else if (required) throw new IncorrectUserDataException("Фамилия обязательна к заполнению");
        s = personDto.getName();
        if (s != null) {
            if (Util.nameIsValid(s)) {
                personDto.setName(Util.nameCase(s));
            } else {
                throw new IncorrectUserDataException("Имя указано неверно: разрешены только русские буквы, длина ограничена 30 символами");
            }
        } else if (required) throw new IncorrectUserDataException("Имя обязательно к заполнению");
        s = personDto.getPatronymic();
        if (s != null) {
            if (Util.nameIsValid(s)) {
                personDto.setPatronymic(Util.nameCase(s));
            } else {
                throw new IncorrectUserDataException("Отчество указано неверно: разрешены только русские буквы, длина ограничена 30 символами");
            }
        } else if (required) throw new IncorrectUserDataException("Отчество обязательно к заполнению");
        //endregion
        //region ДР
        s = personDto.getDr();
        if (s != null) {
            try {
                Date d = Util.strToDate(s);
                personDto.setDr(Util.dateToStr(d));
            } catch (RuntimeException ex) {
                throw new IncorrectUserDataException("Дата рождения указана некорректно");
            }
        } else if (required) throw new IncorrectUserDataException("Дата рождения обязательна к заполнению");
        //endregion
        //region Пол
        s = personDto.getGender();
        if (s != null) {
            String c = Character.toString(s.trim().toLowerCase().charAt(0));
            if (new String("fm").contains(c)){
                personDto.setGender(c);
            } else {
                throw new IncorrectUserDataException("Пол указан неверно. Допустимые символы m и f");
            }
        } else if (required) throw new IncorrectUserDataException("Пол обязателен к заполнению");
        //endregion
    }

    /**
     * Проверяем присланные данные на корректность. Нет обязательных полей.
     * Исправляем формат данных.
     */
    private void dtoCheckAndCorrect(PersonDto personDto) throws IncorrectUserDataException {
        dtoCheckAndCorrect(personDto, false);
    }
    //endregion


    //region === Добавление сотрудника ==========================================================================================
    @PostMapping(Url.PERSONS_API)
    public ResponseEntity<?> createPerson(@RequestBody PersonDto personDto) {
        ApiError apiError = null;
        Person person = null;
        try {
            dtoCheckAndCorrect(personDto, true);  // Проверяем корректность переданных данных

            // Проверяем, что такого сотрудника еще нет в БД (два одинаковых сотрудника запрещены)
            if (personService.personExists(personDto)) {
                throw new IncorrectUserDataException("Сотрудник уже есть в базе данных. Два раза добавить одного и того же человека нельзя!");
            }

            person = personService.createPerson(personDto);
        } catch (RuntimeException e){
            String err = e.getMessage();
            apiError = new ApiError(HttpStatus.BAD_REQUEST, "Сохранение не удалось", err);
            return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        //region Если все нормально - Возвращаем HttpStatus.CREATED и передаем URI вновь созданного ресурса через HTTP-заголовок Location
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPersonUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(person.getId()).toUri();
        responseHeaders.setLocation(newPersonUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
        //endregion
    }
    //endregion


    //region === Обновление (сохранение) сотрудника с идентификатором id ========================================================
    @PutMapping(Url.PERSON_API)
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody PersonDto personDto) {
        ApiError apiError = null;

        //region Получаем сотрудника с указанным ID
        Person person = personService.getById(id);
        if (person == null) {
            // Возвращаем ошибку, если ID указан не верно
            apiError = new ApiError(HttpStatus.NOT_FOUND, "Сохранение не удалось", "Сотрудник с ID=" + id + " не найден в базе данных");
            return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
        }//endregion

        // Обновляем
        try {
            setPerson(personDto, person);
            personService.save(person);
        } catch (RuntimeException ex){
            String err = ex.getMessage();
            apiError = new ApiError(HttpStatus.BAD_REQUEST, "Сохранение не удалось", err);
            return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }



    // --- Заполнение полей person из полученного JSON personDto -------------------------------------------------------
    private void setPerson(PersonDto dto, Person person) throws IncorrectUserDataException{
        String s = null;
        Boolean b = null;
        Long id = null;
        SprDol dol = null;
        Date d1,d2;
        Float f;

        //TODO: для должностей и детей сделать возможность частичного заполнения данных
        //TODO: Реализовать проверку адреса по КЛАДР. Некорректный адрес не добавлять! Пока сохраняется все, что передали

        dtoCheckAndCorrect(dto); // Проверяем и обрабатываем поля ФИО, ДР, пол
        s = dto.getSurname();
        if (s != null) person.setSurname(s);
        s = dto.getName();
        if (s != null) person.setName(s);
        s = dto.getPatronymic();
        if (s != null) person.setPatronymic(s);
        s = dto.getDr();
        if (s != null) person.setDr(Util.strToDate(s));
        s = dto.getGender();
        if (s != null) person.setGender(s);

        //region Адрес
        Boolean newAddress = false;
        AddressDto addressDto = dto.getAddress();
        if (addressDto != null) {
            // Начинаем править только если хотя бы одно поле заполнено
            Boolean someFieldsNotNull = false;
            for (Field field : addressDto.getClass().getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    if (field.get(addressDto) != null) {
                        someFieldsNotNull = true;
                        break;
                    }
                } catch (IllegalAccessException e) {}
            }
            if (someFieldsNotNull) {
                Address address = person.getAddress();
                if (address == null) {
                    address = new Address();
                    address.setPerson(person);
                    newAddress = true;
                }
                //region streetId
                s = addressDto.getStreetId();
                if (s != null) {
                    s = s.replaceAll("[^0-9]", ""); // удалится все кроме цифр
                    if (!s.isEmpty()) {
                        address.setStreetId(s);
                    } else {
                        throw new IncorrectUserDataException("Поле address.streetId обязательно к заполнению");
                    }
                } else {
                    if (newAddress) throw new IncorrectUserDataException("Поле address.Id обязательно к заполнению");
                }//endregion
                //region zip
                s = addressDto.getZip();
                if (s != null) {
                    s = s.replaceAll("[^0-9]", "");
                    if (s.isEmpty()) s = null;
                    address.setZip(s);
                }//endregion
                // region region
                s = address.getRegion();
                if (s != null) {
                    s = s.trim();
                    if (s.isEmpty()) s = null;
                    address.setRegion(s);
                }//endregion
                // region district
                s = addressDto.getDistrict();
                if (s != null) {
                    s = s.trim();
                    if (s.isEmpty()) s = null;
                    address.setDistrict(s);
                }//endregion
                // region city
                s = addressDto.getCity();
                if (s != null) {
                    s = s.trim();
                    if (!s.isEmpty()) {
                        address.setCity(s);
                    } else {
                        throw new IncorrectUserDataException("Поле address.city обязательно к заполнению");
                    }
                } else {
                    if (newAddress) throw new IncorrectUserDataException("Поле address.city обязательно к заполнению");
                }//endregion
                // region street
                s = addressDto.getStreet();
                if (s != null) {
                    s = s.trim();
                    if (s.isEmpty()) s = null;
                    address.setStreet(s);
                }//endregion
                // region building
                s = addressDto.getBuilding();
                if (s != null) {
                    s = s.trim();
                    if (!s.isEmpty()) {
                        address.setBuilding(s);
                    } else {
                        throw new IncorrectUserDataException("Поле address.building обязательно к заполнению");
                    }
                } else {
                    if (newAddress) throw new IncorrectUserDataException("Поле address.building обязательно к заполнению");
                }//endregion
                // region kvartira
                s = addressDto.getKvartira();
                if (s != null) {
                    s = s.trim();
                    if (s.isEmpty()) s = null;
                    address.setKvartira(s);
                }//endregion
                // region text
                s = addressDto.getText();
                if (s != null) {
                    s = s.trim();
                    if (!s.isEmpty()) {
                        address.setText(s);
                    } else {
                        throw new IncorrectUserDataException("Поле address.text обязательно к заполнению");
                    }
                } else {
                    if (newAddress) throw new IncorrectUserDataException("Поле address.text обязательно к заполнению");
                }//endregion
                person.setAddress(address);
            }
        }//endregion
        //region Паспорт
        Boolean newPasport = false;
        PasportDto pasportDto = dto.getPasport();
        if (pasportDto != null) {
            // Начинаем править только если хотя бы одно поле заполнено
            if (pasportDto.getSer() != null || pasportDto.getNum() != null || pasportDto.getDat() != null || pasportDto.getOrg() != null){
                Pasport pasport = person.getPasport();
                if (pasport == null) {
                    pasport = new Pasport();
                    pasport.setPerson(person);
                    newPasport = true;
                }
                //region серия
                s = pasportDto.getSer();
                if (s != null) {
                    s = s.replaceAll("[^0-9]", ""); // удалится все кроме цифр
                    if (s.length() == 4) {
                        pasport.setSer(s);
                    } else {
                        throw new IncorrectUserDataException("Серия паспорта задана неверно");
                    }
                } else {
                    if (newPasport) throw new IncorrectUserDataException("Все реквизиты паспорта обязательны к заполнению");
                }//endregion
                //region номер
                s = pasportDto.getNum();
                if (s != null) {
                    s = s.replaceAll("[^0-9]", ""); // удалится все кроме цифр
                    if (s.length() == 6) {
                        pasport.setNum(s);
                    } else {
                        throw new IncorrectUserDataException("Номер паспорта задан неверно");
                    }
                } else {
                    if (newPasport) throw new IncorrectUserDataException("Все реквизиты паспорта обязательны к заполнению");
                }//endregion
                //region дата выдачи
                s = pasportDto.getDat();
                if (s != null) {
                    try {
                        pasport.setDat(Util.strToDate(s));
                    } catch (RuntimeException ex) {
                        throw new IncorrectUserDataException("Дата выдачи паспорта указана неверно");
                    }
                } else {
                    if (newPasport) throw new IncorrectUserDataException("Все реквизиты паспорта обязательны к заполнению");
                }//endregion
                //region кем выдан
                s = pasportDto.getOrg();
                if (s != null) {
                    s = s.trim();
                    if (!s.isEmpty()) {
                        pasport.setOrg(s);
                    } else {
                        throw new IncorrectUserDataException("Не указано кем выдан паспорт");
                    }
                } else {
                    if (newPasport) throw new IncorrectUserDataException("Все реквизиты паспорта обязательны к заполнению");
                }//endregion
                person.setPasport(pasport);
            }
        }//endregion
        //region Телефоны
        s = dto.getHomePhone();
        if (s != null) {
            s = s.replaceAll("[^0-9]", ""); // удалится все кроме цифр
            if (s.length() <= 10) {
                person.setHomePhone(Util.phoneNumberFormat(s));
            } else {
                throw new IncorrectUserDataException("Телефонный номер ограничен 10 цифрами");
            }
        }
        s = dto.getWorkPhone();
        if (s != null) {
            s = s.replaceAll("[^0-9]", "");
            if (s.length() <= 10) {
                person.setWorkPhone(Util.phoneNumberFormat(s));
            } else {
                throw new IncorrectUserDataException("Телефонный номер ограничен 10 цифрами");
            }
        }
        s = dto.getMobilePhone();
        if (s != null) {
            s = s.replaceAll("[^0-9]", "");
            if (s.length() >= 11) {
                s = s.substring(1);
            }
            if (s.length() == 10 ) {
                person.setMobilePhone(Util.mobilePhoneNumberFormat(s));
            } else {
                throw new IncorrectUserDataException("Номер мобильного телфона указан неверно");
            }
        }
        //endregion
        //region E-mail
        s = dto.getEmail();
        if (s != null) {
            s = s.trim();
            if (s != "") {
                if (Util.emailIsValid(s)) {
                    person.setEmail(s);
                } else {
                    throw new IncorrectUserDataException("E-mail указан неверно");
                }
            } else {
                person.setEmail(null);
            }
        }//endregion
        //region Должности
        List<PostDto> postDtoList = dto.getPosts();
        List<Post> postList = person.getPostList();
        Post post;

        if (postDtoList != null) {
            // Перебираем должности
            for (PostDto postDto : postDtoList) {
                id = postDto.getId();
                post = null; // обнуляем должность на каждой итерации

                //region Получаем обрабатываемую должность (id > 0 - редактируем, id < 0 - добавляем)
                if (id > 0) {
                    // Находим должность в списке по ID
                    for (int i = 0; i < postList.size(); i++) {
                        if (postList.get(i).getId().equals((Long)id)) {
                            post = postList.get(i);
                            break;
                        }
                    }
                } else {
                    // Создаем новую должность
                    post = new Post();
                    post.setPerson(person);
                    post.setDateEnd(null);
                    post.setActive(true);
                }
                //endregion

                //region Заполняем должность новыми данными
                if (post != null) {
                    //region Должность
                    dol = sprDolDao.getById(postDto.getDolId());
                    if (dol != null) {
                        post.setDol(dol);
                    } else {
                        throw new IncorrectUserDataException("Должность отсутствует в справочнике");
                    }//endregion
                    //region Дата начала
                    s = postDto.getDateBegin();
                    if ((s != null) && (s.trim() != "")) {
                        try {
                            post.setDateBegin(Util.strToDate(s));
                        } catch (RuntimeException ex) {
                            throw new IncorrectUserDataException("Дата начала должности указана некорректно");
                        }
                    } else {
                        throw new IncorrectUserDataException("Дата начала должности - обязательный параметр");
                    }//endregion
                    //region Дата окончания
                    if (id > 0) {
                        s = postDto.getDateEnd();
                        if (s != null) {
                            s = s.trim();
                            if (s != "") {
                                try {
                                    d1 = post.getDateBegin();
                                    d2 = Util.strToDate(s);
                                    if (d2.after(d1)) {
                                        post.setDateEnd(d2);
                                    } else {
                                        throw new IncorrectUserDataException("Дата окончания должности должна быть позже даты начала");
                                    }
                                } catch (RuntimeException ex) {
                                    throw new IncorrectUserDataException("Дата окончания должности указана некорректно");
                                }
                            }
                        }
                    }
                    //endregion
                    //region Ставка
                    f = postDto.getStavka();
                    if (f != null) {
                        if (f == 0.25 || f == 0.5 || f == 0.75 || f == 1 || f == 1.25 || f == 1.5) {
                            post.setStavka(f);
                        } else {
                            throw new IncorrectUserDataException("Ставка указана неверно");
                        }
                    }//endregion
                    //region Активность
                    if (id > 0) {
                        b = postDto.getActive();
                        if (b == null) b = true;
                        if (!b && (post.getDateEnd() == null)) {
                            throw new IncorrectUserDataException("Для не активной должности обязательно должна быть указана дата окончания");
                        }
                        post.setActive(b);
                    }
                    //endregion
                } else {
                    throw new IncorrectUserDataException("У сотрудника нет должности с таким идентификатором");
                }
                //endregion

                // Добавляем должность post в список postList
                if (id < 0) postList.add(post);
            }
        }
        //endregion Должности
        //region Дата принятия
        s = dto.getDatPrin();
        if (s != null) {
            s = s.trim();
            if (s != "") {
                try {
                    person.setDatPrin(Util.strToDate(s));
                } catch (RuntimeException ex) {
                    throw new IncorrectUserDataException("Дата принятия на работу указана некорректно");
                }
            } else {
                person.setDatPrin(null);
            }
        }//endregion
        //region Табельный номер
        s = dto.getTabNo();
        if (s != null) {
            s = s.replaceAll("[^0-9]", ""); // удалится все кроме цифр
            person.setTabNo(s);
        }//endregion
        //region Состоит в браке
        b = dto.getSemPol();
        if (b != null) {
            person.setSemPol(b);
        }//endregion
        //region Дети
        List<ChildrenDto> childrenDtoList = dto.getChildrens();
        List<Children> childrenList = person.getChildrenList();
        Children children;
        String chSurname, chName, chPatronymic, chGender, chBirthSertificate;
        Date chDr;

        if (childrenDtoList != null) {
            // Перебираем детей из DTO
            for (ChildrenDto childrenDto : childrenDtoList) {
                id = childrenDto.getId();
                children = null;

                //region Получаем обрабатываемого ребенка
                if (id > 0) {
                    // Находим ребенка в списке по ID
                    for (int i = 0; i < childrenList.size(); i++) {
                        if (childrenList.get(i).getId().equals((Long)id)) {
                            children = childrenList.get(i);
                            break;
                        }
                    }
                } else {
                    // Создаем нового ребенка
                    children = new Children();
                }
                //endregion

                //region Получаем данные из DTO
                chSurname = ""; chName = ""; chPatronymic = ""; chDr = null; chGender = ""; chBirthSertificate = "";

                s = childrenDto.getSurname();
                if (s != null) {
                    if (s.trim().toLowerCase().equals("delete")){
                        chSurname = "delete";
                    } else {
                        if (Util.surnameIsValid(s)) {
                            chSurname = Util.surnameCase(s);
                        } else {
                            throw new IncorrectUserDataException("Фамилия ребенка указана неверно: разрешены только русские буквы и один дефис или пробел в качестве разделителя, длина ограничена 30 символами");
                        }
                    }
                }
                s = childrenDto.getName();
                if (s != null) {
                    if (Util.nameIsValid(s)) {
                        chName = Util.nameCase(s);
                    } else {
                        throw new IncorrectUserDataException("Имя ребенка указано неверно: разрешены только русские буквы, длина ограничена 30 символами");
                    }
                }
                s = childrenDto.getPatronymic();
                if (s != null) {
                    if (Util.nameIsValid(s)) {
                        chPatronymic = Util.nameCase(s);
                    } else {
                        throw new IncorrectUserDataException("Отчество ребенка указано неверно: разрешены только русские буквы, длина ограничена 30 символами");
                    }
                }
                s = childrenDto.getDr();
                try {
                    chDr = Util.strToDate(s);
                } catch (RuntimeException ex) {
                    throw new IncorrectUserDataException("Дата рождения ребенка указана некорректно");
                }

                s = childrenDto.getGender();
                if (s != null) {
                    String c = Character.toString(s.trim().toLowerCase().charAt(0));
                    if (new String("fm").contains(c)){
                        chGender = c;
                    } else {
                        throw new IncorrectUserDataException("Пол ребенка указан неверно. Допустимые символы m и f");
                    }
                } else {
                    throw new IncorrectUserDataException("Пол ребенка обязателен к заполнению");
                }

                s = childrenDto.getBirthSertificate();
                if (s != null) {
                    s = s.trim();
                    if (Util.birthSertificateIsValid(s)) {
                        chBirthSertificate = s.toUpperCase();
                    } else {
                        throw new IncorrectUserDataException("Свидетельство о рождении введено неверно. Формат: римское число дефис две русские буквы пробел шесть цифр");
                    }
                } else {
                    chBirthSertificate = null;
                }
                //endregion

                //region Если ребенок у сотрудника найден, или создан новый (пустой), то работаем с ним
                if (children != null) {

                    if (id > 0 && (chSurname.equals("delete"))) {
                        //region Удаление ребенка

                        childrenList.remove(children);  // Удаляем ребенка из списка детей сотрудника (разрываем связь)
                        Children ch = childrenDao.getById(id); // Получаем ребенка из DAO для дальнейшего удаления

                        //region Удаляем сотрудника из списка детей ребенка
                        List<Person> pl = ch.getPersonList();
                        for (int i = 0; i < pl.size(); i++) {
                            Person p = pl.get(i);
                            if (person.getId() == p.getId()) {
                                pl.remove(i);
                                break;
                            }
                        }
                        //endregion

                        if (pl.size() == 0) {
                            childrenDao.delete(ch); // Удаляем самого ребенка, если у него не осталось родителей
                        } else {
                            childrenDao.save(ch);
                        }


                        //endregion
                    } else {
                        //region Заполняем ребенка (редактирование/добавление)
                        children.setSurname(chSurname);
                        children.setName(chName);
                        children.setPatronymic(chPatronymic);
                        children.setDr(chDr);
                        children.setGender(chGender);
                        if (chBirthSertificate != null) children.setBirthSertificate(chBirthSertificate);
                        //endregion
                    }

                    if (id < 0) {
                        //region Добавление ребенка

                        // Проверяем, а может ребенок уже добавлен другому сотруднику, тогда просто добавляем его и этому
                        Children chFind = childrenDao.get(chSurname, chName, chDr, chGender);
                        if (chFind != null) {
                            // Уже есть такой ребенок
                            // Проверяем что данный сотрудник не его родитель
                            Boolean personHasThisChildren = false;
                            for (Children ch : childrenList) {
                                if (ch.getId() == chFind.getId()) {
                                    personHasThisChildren = true;
                                    break;
                                }
                            }
                            if (!personHasThisChildren) {
                                chFind.setPatronymic(chPatronymic);
                                if (chBirthSertificate != null && chBirthSertificate != "") {
                                    chFind.setBirthSertificate(chBirthSertificate);
                                }
                                childrenList.add(chFind);
                            } else {
                                throw new IncorrectUserDataException("Нельзя добавить ребенка дважды одному и тому же сотруднику ");
                            }
                        } else {
                            childrenList.add(children);
                        }
                        //endregion
                    }

                } else {
                    throw new IncorrectUserDataException("У сотрудника нет ребенка с таким идентификатором");
                }
                //endregion
            }
        }
        //endregion
        //region Дополнительные сведения
        s = dto.getDopsved();
        if (s != null) {
            person.setDopsved(s);
        }//endregion
    }

    //endregion


    //region === Удаление сотрудника ============================================================================================
    /**
     * Удаление сотрудника. Сотрудник не удаляется из БД, а помечается как уволенный (uvolen=1).
     *
     * @param id Идентификатор сотрудника
     * @return Если все нормально, возвращаем HttpStatus.OK, иначе возвращаем ошибку в ApiError
     */
    @DeleteMapping(Url.PERSON_API)
    public ResponseEntity<?> deletePerson(@PathVariable Long id, @RequestParam(value = "date", required = true) String date) {
        Date datUvol = null;

        //region Проверяем корректность даты увольнения
        try {
            datUvol = Util.strToDate(date);
        } catch (RuntimeException ex) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST,
                    "Некорректный параметр",
                    "Дата увольнения не указана или указана некорректно"
            );
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }//endregion

        //region Получаем сотрудника по id
        Person person = personService.getById(id);
        if (person == null) {
            ApiError apiError = new ApiError(
                    HttpStatus.NOT_FOUND,
                    "ID сотрудника указан неверно",
                    "Сотрудник с ID= " + id + " не найден"
            );
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }//endregion

        //region Проверяем, что дата увольнения не меньше даты приема + 1 день
        Date datPrin = person.getDatPrin();
        if (datPrin != null && !datPrin.before(datUvol)) {
            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST,
                    "Некорректный параметр",
                    "Дата увольнения должна быть больше даты принятия (" + Util.dateToStr(datPrin) + ")"
            );
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }
        //endregion

        //region Увольняем сотрудника
        person.setUvolen(true);
        person.setDatUvol(datUvol);
        personService.save(person);
        //endregion

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion


    //region === Получение/Сохранение фотографии ================================================================================

    // Получение фотографии из папки и выдача в виде байтового массива
    @GetMapping(Url.PERSON_PHOTO_API)
    @ResponseBody
    public byte[] getPhoto(@PathVariable long id) {
        Path path = Paths.get(filesPath + "/photo/" + id + ".jpg" );
        byte[] data = new byte[0];
        try {                                   // TODO: Подумать над обработчиком
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Добавление фотографии
    @PostMapping(Url.PERSON_API + "/uploadphoto")
    @ResponseBody
    public FileUploadResult uploadPhoto(@PathVariable Long id, @RequestParam(value="personPhoto", required=false) MultipartFile image) {
        String path = filesPath + "/photo/" + id + ".jpg";

        //String ppp = context.getRealPath("/");        //  C:\\projects\\oio\\target\\oio
        //String ppp = context.getContextPath();        //  /oio

        FileUploadResult fur = new FileUploadResult();          // Вспомогательный объект с информацией о результате загрузки файла
        try {
            if(!image.isEmpty()) {
                // Проверить изображение
                if(!image.getContentType().equals("image/jpeg")) {
                    throw new ImageUploadException("Поддерживаются только изображения формата JPEG");
                }
                saveImage(path, image);      // Сохранить файл
            }
        } catch (ImageUploadException e) {
            fur.set("ERR", e.getMessage(), "");
            return fur;
        }
        fur.set("OK", "Изображение успешно сохранено на стороне сервера!", path);
        return fur;
    }

    // Сохранение фотографии на стороне сервера
    private void saveImage(String filename, MultipartFile image) throws ImageUploadException {
        try {
            File file = new File(filename);
            FileUtils.writeByteArrayToFile(file, image.getBytes());
        } catch (IOException e) {
            throw new ImageUploadException("Не удается сохранить изображение", e);
        }
    }

    //endregion

}
