package ru.elsu.oio;

public final class Url {
    public static final String INDEX_PAGE               = "/";
    public static final String ROBOTS_TXT               = "/robots.txt";
    public static final String SITEMAP_XML              = "/sitemap.xml";

    public static final String LOGIN_PAGE               = "/login";
    public static final String LOGOUT_PAGE              = "/logout";

    // Ошибки
    public static final String UNAUTHORIZED_PAGE        = "/error-401";
    public static final String FORBIDDEN_PAGE           = "/error-403";
    public static final String NOT_FOUND_PAGE           = "/error-404";
    public static final String INTERNAL_ERROR_PAGE      = "/error-500";

    // Сотрудники
    public static final String PERSONS                  = "/persons";
    public static final String PERSON                   = "/persons/{id}";

    // API
    public static final String API                      = "/api";

    // Фото сотрудника (API)
    public static final String PERSON_PHOTO             = "/getphoto/{id}";

    // DOC
    public static final String DOC                      = "/doc";

    // Табель
    public static final String TABEL_PAGE               = "/tabel";
    public static final String TABEL                    = "/tabel/{year}/{month}";
    public static final String TABEL_SPECIAL_DAYS       = "/tabel/spdays/{year}/{month}";




    // Настройки пользователя
    public static final String USERSETTINGS_PAGE        = "/usersettings";
    public static final String USERPROFILE_PAGE         = "/userprofile";

    // Справочники




    private Url(){}

}