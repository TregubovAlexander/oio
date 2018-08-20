/*
 Сюда вынес общие функции, использующиеся в различных сценариях
 */
(function($) {

    $.DATA_NAME = 'oioObject';

    // Отправка JSON-объекта на сервер методом POST
    $.postJSON = function (url, data, success, error) {
        var deferred = $.ajax({
            url: url,
            data: JSON.stringify(data),
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: success,
            error: error
        });
        return deferred.promise();
    }


    // Добавление к AJAX-запросу служебной информации, если включена защита CSRF
    // type - необязательный  - по-умолчанию 'GET'
    // $widget - необязательный параметр - элемент виджета, в котором нужно включить крутилку
    // data - JSON с данными для POST и PUT запросов
    $.myAjax = function (url, type, $widget, data) {
        // Необходимо для того, чтобы Spring Sequrity разрешал Post Ajax запросы , если включена защита CSRF
        var csrfToken = $('meta[name="_csrf"]').attr('content');
        var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

        type = type || 'GET';
        if ($widget) $widget.widget_box('reload'); // Запускаем крутилку

        var settings = {
            type: type,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }

        if ((type == 'PUT') || (type == 'POST')) {
            settings.contentType = 'application/json; charset=utf-8';
            settings.dataType = 'json';
            settings.data = data;
        }

        var deferred = $.ajax(url, settings);
        return deferred.promise();
    }


    // Показ сообщения пользователю об ошибке операции
    $.showErrorMessage = function (result, title) {
        // Произошла ошибка (result.readyState = 4)
        var errmsg = '', timer = 10000;

        // Формируем сообщение об ошибке
        // HttpStatus 500 - Internal Server Error
        if (result.status >= 500) {
            result.message = "Внутренняя ошибка сервера";
        } else {
            // HttpStatus >= 300 - наш JSON, если есть, то находится в поле responseJSON, а иначе смотрим сообщение в поле statusText
            if (result.responseJSON) {

                result = result.responseJSON;

                if (result.errors && result.errors.length) {
                    errmsg = '<div class="hr hr-double hr-dotted hr18"></div><ul class="list-unstyled spaced">';
                    for (var i in result.errors) {
                        errmsg += '<li><i class="ace-icon fa fa-angle-double-right red"></i><span class="smaller-70">' + result.errors[i] + '</span></li>';
                        timer += 1000;
                    }
                    errmsg += '</ul>';
                }
            } else {
                result.message = result.statusText;
            }
        }

        swal({
            title: title,
            html: result.message + errmsg,
            type: 'error',
            timer: timer,
            background: '#f2dede',
            allowOutsideClick: true,
            buttonsStyling: false,
            confirmButtonText: 'Ok',
            confirmButtonClass: 'btn btn-lg btn-danger'
        });
    }


    // Показ сообщения пользователю об успехе или ошибке операции
    $.showUserMessage = function (result, titleOk, textOk, titleErr, func) {
        if (result == '' || result.status == 200 || result.status == 201) {
            swal({
                title: titleOk,
                text: textOk,
                type: 'success',
                timer: 5000,
                background: '#dff0d8',
                allowOutsideClick: false,
                buttonsStyling: false,
                confirmButtonText: 'Ok',
                confirmButtonClass: 'btn btn-lg btn-success'
            }).then(function () {
                func()
            }, function (dismiss) {
                func()
            });
        } else {
            // Произошла ошибка (result.readyState = 4)
            $.showErrorMessage(result, titleErr);
        }
    }


    // Форматирование даты к виду yyyy-mm-dd
    $.formatDate = function (date) {

        var d = date.getDate();
        if (d < 10) d = '0' + d;

        var m = date.getMonth() + 1;
        if (m < 10) m = '0' + m;

        var y = date.getFullYear();

        return y + '-' + m + '-' + d;
    }

    // Переформатирование текстовой строки с датой из формата yyyy-mm-dd в формат dd.mm.yyyy
    $.reformatDate = function (s) {
        return s.split('-').reverse().join('.');
    }


    // Работа с таблицами
    {
        // Заполнение всей таблицы
        $.tableCreate = function($tableBody, $templateRow, data, tableFillRow) {
            $tableBody.empty();
            for (var i in data) {
                $.tableAddRow($tableBody, $templateRow, data[i], tableFillRow);
            }
        }

        // Добавление строки в таблицу
        $.tableAddRow = function($tableBody, $templateRow, data, tableFillRow) {
            var $row = $.tableCreateRow($templateRow, data, tableFillRow);
            $tableBody.append($row);
        }

        // Создание и заполнение одной строки таблицы
        $.tableCreateRow = function ($templateRow, data, tableFillRow) {
            $el = $templateRow.clone();
            tableFillRow($el, data);
            return $el;
        }

        // Изменение данных в строке таблицы
        $.tableUpdateRow = function($row, data, tableFillRow) {
            tableFillRow($row, data);
        }

        // Удаление строки таблицы
        $.tableDeleteRow = function (e, url, $widget) {
            e.preventDefault();
            var $tr = $(e.target).parents('tr')
            var id = $tr.data($.DATA_NAME).id;
//console.log($tr.data($.DATA_NAME));
            swal({
                title: "Внимание!",
                text: "Вы действительно хотите удалить запись?",
                type: "warning",
                timer: 100000,
                background:"#fcf8e3",
                allowOutsideClick: false,
                buttonsStyling: false,
                confirmButtonText: "Удалить",
                confirmButtonClass: "btn btn-lg btn-warning",
                showCancelButton: true,
                focusCancel: true,
                cancelButtonText: "Отменить",
                cancelButtonClass: "btn btn-lg btn-cancel"
            }).then(function(){
                $.myAjax(url + id, 'DELETE', $widget)
                    .done(function (result) {
                        $tr.remove();
                    })
                    .fail(function (result) {
                        $.showErrorMessage(result, 'Ошибка удаления!');
                    })
                    .always(function (result) {
                        $widget.trigger('reloaded.ace.widget'); // Отключаем крутилку
                    });
            }).catch(swal.noop);
        }

        // Клик по иконке редактирования строки таблицы открывает модальное окно
        $.tableEditRow = function(e, $modal) {
            e.preventDefault();
            $modal.data($.DATA_NAME, $(e.target).parents('tr').data($.DATA_NAME));
            $modal.modal();
        }
    }


    // Получение списка объектов Id + Name по заданному URL
    $.getIdNameList = function(url) {
        return $.myAjax(url);
    }


    // Заполнение пунктов выпадающего списка (парами id + name)
    $.setSelectOptions = function($select, data, id) {
        id = id || 0;
        var options = '';
        $.each(data, function() {
            options += '<option ';
            if (this.id === id) options += ' selected ';
            options += ' value="' + this.id + '">' + this.name + '</option>';
        });
        $select.html(options);
    }


})(jQuery);
