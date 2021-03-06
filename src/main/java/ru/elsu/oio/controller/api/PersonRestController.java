package ru.elsu.oio.controller.api;

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
import ru.elsu.oio.controller.exeption.ResourceNotFoundException;
import ru.elsu.oio.dto.ErrorDetail;
import ru.elsu.oio.controller.exeption.IncorrectUserDataException;
import ru.elsu.oio.dao.ChildrenDao;
import ru.elsu.oio.dto.*;
import ru.elsu.oio.dto.AddressDto;
import ru.elsu.oio.dto.ChildrenDto;
import ru.elsu.oio.dto.PasportDto;
import ru.elsu.oio.entity.*;
import ru.elsu.oio.services.PersonService;
import ru.elsu.oio.services.SprService;
import ru.elsu.oio.utils.Util;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(Url.API)
public class PersonRestController {

    @Autowired
    private PersonService personService;

    @Autowired
    private ChildrenDao childrenDao;

    @Autowired
    private SprService sprService;

    // Папка с файлами (задана в параметрах)
    @Value("${files.path}")
    private String filesPath;

    //region === Список всех сотрудников отдела =================================================================================
    @GetMapping(Url.PERSONS)
    public List<PersonDto> getAllPersons() {
        List<Person> personList = personService.getAllInitialized();
        List<PersonDto> personDtoList = new ArrayList<>();
        for (Person p : personList) {
            personDtoList.add(p.toDto());
        }
        return personDtoList;
    }
    //endregion


    //region === GET - Инормация о конкретном сотруднике с идентификатором id ===================================================
    @GetMapping(Url.PERSON)
    public ResponseEntity<Object> getPersonById(@PathVariable Long id) {

        // Получаем сотрудника по id
        Person person = personService.getById(id);
        if (person == null) {
            throw new ResourceNotFoundException("Сотрудник с ID " + id + " не найден");
        }

        return new ResponseEntity<Object>(person.toDto(), HttpStatus.OK);

    }
    //endregion


    //region === Коррекция присланного объекта personDto (ФИО, ДР, пол) =========================================================
    /**
     * Исправляем формат данных
     *
     * @param personDto данные присылаются в формате JSON
     */
    private void correctDto(PersonDto personDto) {
        personDto.setSurname(Util.surnameCase(personDto.getSurname()));         // Ф
        personDto.setName(Util.nameCase(personDto.getName()));                  // И
        personDto.setPatronymic(Util.nameCase(personDto.getPatronymic()));      // О
        personDto.setGender(personDto.getGender().toLowerCase());               // Пол
    }
    //endregion


    //region === POST - Добавление сотрудника ===================================================================================
    @PostMapping(Url.PERSONS)
    public ResponseEntity<?> createPerson(@Valid @RequestBody PersonDto personDto) {
        ErrorDetail errorDetail = null;
        Person person = null;
        try {
            // Корректируем переданные данные
            correctDto(personDto);

            // Проверяем, что такого сотрудника еще нет в БД (два одинаковых сотрудника запрещены)
            if (personService.personExists(personDto)) {
                throw new IncorrectUserDataException("Сотрудник уже есть в базе данных. Два раза добавить одного и того же человека нельзя!");
            }

            person = personService.createPerson(personDto);
        } catch (RuntimeException e) {
            errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Сохранение не удалось", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        // Если все нормально - Возвращаем HttpStatus.CREATED и передаем URI вновь созданного ресурса через HTTP-заголовок Location
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPersonUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(person.getId()).toUri();
        responseHeaders.setLocation(newPersonUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    //endregion


    //region === PUT - Обновление (сохранение) сотрудника с идентификатором id ==================================================
    @PutMapping(Url.PERSON)
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @Valid @RequestBody PersonDto personDto) {
        ErrorDetail errorDetail = null;

        //region Получаем сотрудника с указанным ID
        Person person = personService.getById(id);
        if (person == null) {
            // Возвращаем ошибку, если ID указан не верно
            errorDetail = new ErrorDetail(
                    HttpStatus.NOT_FOUND,
                    "Сохранение не удалось",
                    "Сотрудник с ID=" + id + " не найден в базе данных"
            );
            return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
        }//endregion

        // Обновляем
        try {
            setPerson(personDto, person);
            personService.save(person);
        } catch (RuntimeException ex){
            String err = ex.getMessage();
            errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Сохранение не удалось", err);
            return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
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

        //TODO: Реализовать проверку адреса по КЛАДР. Некорректный адрес не добавлять! Пока сохраняется все, что передали

        //region ФИО, ДР, пол
        correctDto(dto);
        person.setSurname(dto.getSurname());
        person.setName(dto.getName());
        person.setPatronymic(dto.getPatronymic());
        person.setDr(Util.strToDate(dto.getDr()));
        person.setGender(dto.getGender());
        //endregion
        //region Адрес
        AddressDto addressDto = dto.getAddress();
        if (addressDto != null) {
            Address address = person.getAddress();
            if (address == null) {
                address = new Address();
                address.setPerson(person);
            }
            // streetId
            address.setStreetId(addressDto.getStreetId());
            // zip
            s = addressDto.getZip();
            if (s != null) {
                address.setZip(getTrimOrNull(s));
            }
            // region
            s = addressDto.getRegion();
            if (s != null) {
                address.setRegion(getTrimOrNull(s));
            }
            // district
            s = addressDto.getDistrict();
            if (s != null) {
                address.setDistrict(getTrimOrNull(s));
            }
            // city
            address.setCity(addressDto.getCity().trim());
            // street
            s = addressDto.getStreet();
            if (s != null) {
                address.setStreet(getTrimOrNull(s));
            }
            // building
            address.setBuilding(addressDto.getBuilding().trim());
            // kvartira
            s = addressDto.getKvartira();
            if (s != null) {
                address.setKvartira(getTrimOrNull(s));
            }
            // text
            address.setText(addressDto.getText().trim());

            person.setAddress(address);
        }//endregion
        //region Паспорт
        PasportDto pasportDto = dto.getPasport();
        if (pasportDto != null) {
            Pasport pasport = person.getPasport();
            if (pasport == null) {
                pasport = new Pasport();
                pasport.setPerson(person);
            }

            pasport.setSer(pasportDto.getSer());
            pasport.setNum(pasportDto.getNum());
            pasport.setDat(Util.strToDate(pasportDto.getDat()));
            pasport.setOrg(pasportDto.getOrg().trim());

            person.setPasport(pasport);
        }//endregion
        //region Телефоны E-mail
        s = dto.getHomePhone();
        if (s != null) person.setHomePhone(Util.phoneNumberFormat(s));
        s = dto.getWorkPhone();
        if (s != null) person.setWorkPhone(Util.phoneNumberFormat(s));
        s = dto.getMobilePhone();
        if (s != null) person.setMobilePhone(Util.mobilePhoneNumberFormat(s));
        // E-mail
        s = dto.getEmail();
        if (s != null) person.setEmail(s.trim());
        //endregion
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
                    dol = sprService.getDolById(postDto.getDolId());
                    if (dol != null) {
                        post.setDol(dol);
                    } else {
                        throw new IncorrectUserDataException("Должность отсутствует в справочнике");
                    }//endregion
                    // Дата начала
                    post.setDateBegin(Util.strToDate(postDto.getDateBegin()));
                    //region Дата окончания
                    if (id > 0) {
                        s = postDto.getDateEnd();
                        if (s != null) {

                            s = s.trim();
                            if (s != "") {
                                d1 = post.getDateBegin();
                                d2 = Util.strToDate(s);
                                if (d2.after(d1)) {
                                    post.setDateEnd(d2);
                                } else {
                                    throw new IncorrectUserDataException("Дата окончания должности должна быть позже даты начала");
                                }
                            } else {
                                post.setDateEnd(null);
                            }
                        }
                    }
                    //endregion
                    // Ставка
                    f = postDto.getStavka();
                    if (f != null) post.setStavka(f);
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
            if (!s.isEmpty()) {
                person.setDatPrin(Util.strToDate(s));
            } else {
                person.setDatPrin(null);
            }
        }//endregion
        //region Табельный номер
        s = dto.getTabNo();
        if (s != null) person.setTabNo(s.trim());
        //endregion
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
                if (s.trim().toLowerCase().equals("delete")){
                    chSurname = "delete";
                } else {
                    chSurname = Util.surnameCase(s);
                }
                chName = Util.nameCase(childrenDto.getName());
                chPatronymic = Util.nameCase(childrenDto.getPatronymic());
                chDr = Util.strToDate(childrenDto.getDr());
                chGender = childrenDto.getGender().toLowerCase();
                s = childrenDto.getBirthSertificate();
                if (s != null) {
                    chBirthSertificate = s.trim().toUpperCase();
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
                        if (chBirthSertificate != null && !chBirthSertificate.isEmpty()) {
                            children.setBirthSertificate(chBirthSertificate);
                        } else {
                            children.setBirthSertificate(null);
                        }
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
        }
        //endregion
    }

    //endregion


    //region === DELETE - Удаление сотрудника ===================================================================================
    /**
     * Удаление сотрудника. Сотрудник не удаляется из БД, а помечается как уволенный (uvolen=1).
     *
     * @param id Идентификатор сотрудника
     * @return Если все нормально, возвращаем HttpStatus.OK, иначе возвращаем ошибку в ApiError
     */
    @DeleteMapping(Url.PERSON)
    public ResponseEntity<?> deletePerson(@PathVariable Long id, @RequestParam(value = "date", required = true) String date) {
        Date datUvol = null;

        //region Проверяем корректность даты увольнения
        try {
            datUvol = Util.strToDate(date);
        } catch (RuntimeException ex) {
            ErrorDetail errorDetail = new ErrorDetail(
                    HttpStatus.BAD_REQUEST,
                    "Некорректный параметр",
                    "Дата увольнения не указана или указана некорректно"
            );
            return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
        }//endregion

        //region Получаем сотрудника с указанным ID
        Person person = personService.getById(id);
        if (person == null) {
            ErrorDetail errorDetail = new ErrorDetail(
                    HttpStatus.NOT_FOUND,
                    "ID сотрудника указан неверно",
                    "Сотрудник с ID= " + id + " не найден в базе данных"
            );
            return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
        }//endregion

        //region Проверяем, что дата увольнения не меньше даты приема + 1 день
        Date datPrin = person.getDatPrin();
        if (datPrin != null && !datPrin.before(datUvol)) {
            ErrorDetail errorDetail = new ErrorDetail(
                    HttpStatus.BAD_REQUEST,
                    "Некорректный параметр",
                    "Дата увольнения должна быть больше даты принятия (" + Util.dateToStr(datPrin) + ")"
            );
            return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
        }
        //endregion

        //region Увольняем сотрудника
        person.setUvolen(true);
        person.setDatUvol(datUvol);
        for (Post post : person.getPostList()) {
            if (post.getActive()) {
                post.setActive(false);
                post.setDateEnd(datUvol);
            }
        }

        personService.save(person);
        //endregion

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion


    //region === Получение/Сохранение фотографии ================================================================================

    // Получение фотографии из папки и выдача в виде байтового массива
    @GetMapping(Url.PERSON_PHOTO)
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
    @PostMapping(Url.PERSON + "/uploadphoto")
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




    //region === ВСПОМОГАТЕЛЬНЫЕ ПРИВАТНЫЕ МЕТОДЫ ===============================================================================


    private String getTrimOrNull(String s) {
        if (s != null) {
            s = s.trim();
            if (s.isEmpty()) s = null;
        }
        return s;
    }




    //endregion

}
