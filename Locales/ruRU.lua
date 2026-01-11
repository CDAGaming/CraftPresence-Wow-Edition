--[[
MIT License

Copyright (c) 2018 - 2026 CDAGaming (cstack2011@yahoo.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
--]]
-- Перевод ZamestoTV
-- Lua APIs
local self = CraftPresence
local L = self.libraries.AceLocale:NewLocale(self.internals.name, "ruRU")
if not L then return end

-- Type Identifier Data
L["TYPE_UNKNOWN"] = "Неизвестно"
L["TYPE_NONE"] = "Нет"
L["TYPE_ADDED"] = "Добавлено"
L["TYPE_MODIFY"] = "Изменено"
L["STATUS_TRUE"] = "активно"
L["STATUS_FALSE"] = "неактивно"

-- Formatting Data
L["FORMAT_LEVEL"] = "Уровень %s"
L["FORMAT_SETTING"] = "%s (Должно быть %s)"
L["FORMAT_COMMENT"] = self:SetFormat("%s|r\n\n*По умолчанию:|r %s", self.colors.GREEN)
L["FORMAT_USER_PREFIX"] = "(%s) "

-- Primary Logging Data
L["LOG_DEBUG"] = self:SetFormat("*[Отладка]|r %s", self.colors.GREY)
L["LOG_VERBOSE"] = self:SetFormat("*[Подробно]|r %s", self.colors.GREY)
L["LOG_ERROR"] = self:SetFormat("*[Ошибка]|r %s", self.colors.RED)
L["LOG_WARNING"] = self:SetFormat("*[Предупреждение]|r %s", self.colors.GOLD)
L["LOG_INFO"] = "[Инфо] %s"

-- Config Category Data
L["CATEGORY_TITLE_GENERAL"] = "Общие"
L["CATEGORY_COMMENT_GENERAL"] = "Общие настройки для отображения информации."

L["CATEGORY_TITLE_PRESENCE"] = "Присутствие"
L["CATEGORY_TITLE_PRESENCE_EXTENDED"] = "Поля богатого присутствия"
L["CATEGORY_COMMENT_PRESENCE"] = "Настройки для персонализации общих полей отображения богатого присутствия."
L["CATEGORY_COMMENT_PRESENCE_INFO"] = self:SetFormat([[Найдено %s полей богатого присутствия!

*Примечание:|r См. вкладку ^Кнопки|r для дополнительных настроек.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_BUTTONS"] = "Кнопки"
L["CATEGORY_TITLE_BUTTONS_EXTENDED"] = "Пользовательские кнопки"
L["CATEGORY_COMMENT_BUTTONS"] = "Настройки для персонализации дополнительных данных кнопок."
L["CATEGORY_COMMENT_BUTTONS_INFO"] = "Найдено %s пользовательских кнопок!"

L["CATEGORY_TITLE_LABELS"] = "Метки"
L["CATEGORY_TITLE_LABELS_EXTENDED"] = "Пользовательские метки"
L["CATEGORY_COMMENT_LABELS"] = "Настройки для персонализации состояний юнитов (например, В бою, Отсутствует и т.д.)."
L["CATEGORY_COMMENT_LABELS_INFO"] = self:SetFormat([[Найдено %s меток!

*Примечание:|r Используйте ^/cp labels|r для дополнительной информации.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_PLACEHOLDERS"] = "Заполнитель"
L["CATEGORY_TITLE_PLACEHOLDERS_EXTENDED"] = "Пользовательские заполнители"
L["CATEGORY_COMMENT_PLACEHOLDERS"] = "Настройки для персонализации данных заполнителей."
L["CATEGORY_COMMENT_PLACEHOLDERS_INFO"] = self:SetFormat([[Найдено %s заполнителей!

*Примечание:|r Используйте ^/cp placeholders|r для дополнительной информации.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_EVENTS"] = "События"
L["CATEGORY_TITLE_EVENTS_EXTENDED"] = "Доступные события"
L["CATEGORY_COMMENT_EVENTS"] = "Настройки для персонализации событий, которые вызывают обновления богатого присутствия."
L["CATEGORY_COMMENT_EVENTS_INFO"] = self:SetFormat([[Найдено %s событий!

*Примечание:|r Используйте ^/cp events|r для дополнительной информации.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_METRICS"] = "Метрики"
L["CATEGORY_TITLE_METRICS_EXTENDED"] = "Доступные сервисы метрик"
L["CATEGORY_COMMENT_METRICS"] = "Настройки для персонализации сбора данных сторонних метрик."
L["CATEGORY_COMMENT_METRICS_INFO"] = self:SetFormat([[Найдено %s сервисов метрик!

*Примечание:|r Для полного применения некоторых настроек может потребоваться ^перезагрузка|r.]],
        self.colors.GOLD, self.colors.GREEN
)

L["CATEGORY_TITLE_EXTRA"] = "Дополнительно"
L["CATEGORY_COMMENT_EXTRA"] = "Дополнительные параметры настройки отображения информации аддона."

L["CATEGORY_TITLE_ABOUT"] = "О программе"
L["CATEGORY_COMMENT_ABOUT"] = "Просмотр информации об этом аддоне."

-- Config Variable Data
L["TITLE_CLIENT_ID"] = "Идентификатор клиента"
L["COMMENT_CLIENT_ID"] = "Идентификатор клиента, используемый для получения ресурсов, ключей иконок и заголовков."
L["USAGE_CLIENT_ID"] = "<18-значный (или выше) числовой идентификатор здесь>"
L["DEFAULT_CLIENT_ID"] = "805124430774272000"
L["ERROR_CLIENT_ID"] = self:SetFormat("Проверка корректности не пройдена для *Идентификатора клиента|r. Введите ^18-значное (или выше)|r числовое значение.",
        self.colors.GREEN, self.colors.GREY
)

L["TITLE_DEBUG_MODE"] = "Режим отладки"
L["COMMENT_DEBUG_MODE"] = "Включает отображение подробных и более описательных логов."

L["TITLE_VERBOSE_MODE"] = "Подробный режим"
L["COMMENT_VERBOSE_MODE"] = "Включает отображение детализированных логов отладки."

L["TITLE_SHOW_MINIMAP_ICON"] = "Показывать иконку на миникарте"
L["COMMENT_SHOW_MINIMAP_ICON"] = "Включает отображение иконки на миникарте, используемой для доступа к настройкам."

L["TITLE_SHOW_COMPARTMENT_ENTRY"] = "Показывать запись в отсеке аддонов"
L["COMMENT_SHOW_COMPARTMENT_ENTRY"] = "Включает отображение записи в отсеке аддонов, используемой для доступа к настройкам."

L["TITLE_QUEUED_PIPELINE"] = "Очередь обработки"
L["COMMENT_QUEUED_PIPELINE"] = "Переключает, будет ли задержка обратного вызова работать в стиле пропуска или очереди."

L["TITLE_SHOW_WELCOME_MESSAGE"] = "Показывать приветственное сообщение"
L["COMMENT_SHOW_WELCOME_MESSAGE"] = "Включает отображение логов инициализации аддона."

L["TITLE_ENFORCE_INTERFACE"] = "Принудительный интерфейс Retail"
L["COMMENT_ENFORCE_INTERFACE"] = self:SetFormat([[Включает принудительное использование настроек интерфейса Retail.

*Примечания:|r
  - Эта настройка предназначена для отображения интерфейса Retail на некоторых устаревших клиентах,
таких как оригинальные Classic, TBC и Wrath, часто используемые для частных серверов.
  - Из-за множества устаревших аддонов, используемых в этих клиентах, и поскольку эта настройка
включает ^изменение размера|r фреймов и использование ^ранних|r API, могут возникнуть проблемы.
  - Эта опция отключена в неподходящих клиентах, так как в этих версиях она не действует.

*Предупреждение:|r После изменения этой настройки требуется ^перезагрузка|r для применения изменений.]],
        self.colors.GOLD, self.colors.RED
)

L["TITLE_OPTIONAL_MIGRATIONS"] = "Дополнительные миграции"
L["COMMENT_OPTIONAL_MIGRATIONS"] = self:SetFormat([[Включает выполнение дополнительных миграций конфигурации.

*Предупреждение:|r Эти миграции могут ^сбросить|r некоторые значения конфигурации.]],
        self.colors.GOLD, self.colors.RED
)

L["TITLE_CALLBACK_DELAY"] = "Задержка обратного вызова"
L["COMMENT_CALLBACK_DELAY"] = "Задержка (в секундах) после срабатывания события перед запуском неф强制更新 RPC."
L["MINIMUM_CALLBACK_DELAY"] = 0
L["MAXIMUM_CALLBACK_DELAY"] = 5
L["DEFAULT_CALLBACK_DELAY"] = 1

L["TITLE_FRAME_CLEAR_DELAY"] = "Задержка очистки фрейма"
L["COMMENT_FRAME_CLEAR_DELAY"] = "Задержка (в секундах) после событий, не связанных с отладкой, перед очисткой нарисованных фреймов."
L["MINIMUM_FRAME_CLEAR_DELAY"] = 3
L["MAXIMUM_FRAME_CLEAR_DELAY"] = 15
L["DEFAULT_FRAME_CLEAR_DELAY"] = 5

L["TITLE_FRAME_WIDTH"] = "Ширина фрейма"
L["COMMENT_FRAME_WIDTH"] = self:SetFormat([[Ширина в пикселях, на которую должен быть отрисован каждый фрейм события.

*Примечание:|r Это значение должно соответствовать свойству ^pixel_width|r в скрипте Python.]],
        self.colors.GOLD, self.colors.GREEN
)
L["MINIMUM_FRAME_WIDTH"] = 3
L["MAXIMUM_FRAME_WIDTH"] = 15
L["DEFAULT_FRAME_WIDTH"] = 6

L["TITLE_FRAME_HEIGHT"] = "Высота фрейма"
L["COMMENT_FRAME_HEIGHT"] = self:SetFormat([[Высота в пикселях, на которую должен быть отрисован каждый фрейм события.

*Примечание:|r Это значение должно соответствовать свойству ^pixel_height|r в скрипте Python.]],
        self.colors.GOLD, self.colors.GREEN
)
L["MINIMUM_FRAME_HEIGHT"] = 3
L["MAXIMUM_FRAME_HEIGHT"] = 15
L["DEFAULT_FRAME_HEIGHT"] = 6

L["TITLE_FRAME_ANCHOR"] = "Точка привязки фрейма"
L["COMMENT_FRAME_ANCHOR"] = self:SetFormat([[Относительная точка привязки, с которой начинается отрисовка пикселей каждого фрейма события.

*Примечание:|r Это значение требует настройки свойства ^orientation|r в скрипте Python.]],
        self.colors.GOLD, self.colors.GREEN
)
L["DEFAULT_FRAME_ANCHOR"] = 1

L["TITLE_VERTICAL_FRAMES"] = "Использовать вертикальные фреймы"
L["COMMENT_VERTICAL_FRAMES"] = self:SetFormat([[Определяет, должны ли фреймы генерироваться в вертикальной ориентации.

*Примечание:|r Это значение требует настройки свойства ^orientation|r в скрипте Python.]],
        self.colors.GOLD, self.colors.GREEN
)

L["TITLE_FRAME_START_X"] = "Начальная позиция фрейма по X"
L["COMMENT_FRAME_START_X"] = self:SetFormat([[Начальная позиция по оси X для начала отрисовки фреймов.

*Примечание:|r Это значение требует настройки свойства ^orientation|r в скрипте Python.]],
        self.colors.GOLD, self.colors.GREEN
)
L["DEFAULT_FRAME_START_X"] = 0

L["TITLE_FRAME_START_Y"] = "Начальная позиция фрейма по Y"
L["COMMENT_FRAME_START_Y"] = self:SetFormat([[Начальная позиция по оси Y для начала отрисовки фреймов.

*Примечание:|r Это значение требует настройки свойства ^orientation|r в скрипте Python.]],
        self.colors.GOLD, self.colors.GREEN
)
L["DEFAULT_FRAME_START_Y"] = 0

L["TITLE_RELOAD_UI"] = "Перезагрузить интерфейс"
L["COMMENT_RELOAD_UI"] = "Перезагружает пользовательский интерфейс."

L["TITLE_STATE"] = "Состояние игры"
L["COMMENT_STATE"] = "Данные, которые будут интерпретированы для области состояния игры в богатом присутствии."
L["DEFAULT_STATE_MESSAGE"] = self:SetFormat("*scenario**dungeon**raid**battleground**arena**default*", self.internals.defaults.globalKey)

L["TITLE_DETAILS"] = "Детали"
L["COMMENT_DETAILS"] = "Данные, которые будут интерпретированы для области деталей в богатом присутствии."
L["DEFAULT_DETAILS_MESSAGE"] = self:SetFormat("*player_info*", self.internals.defaults.innerKey)

L["TITLE_LARGEIMAGE"] = "Большое изображение"
L["COMMENT_LARGEIMAGE"] = "Данные, которые будут интерпретированы для области большого изображения в богатом присутствии."
L["DEFAULT_LARGE_IMAGE_KEY"] = "wow_icon"
L["DEFAULT_LARGE_IMAGE_MESSAGE"] = self:SetFormat("*realm_info*", self.internals.defaults.innerKey)

L["TITLE_SMALLIMAGE"] = "Малое изображение"
L["COMMENT_SMALLIMAGE"] = "Данные, которые будут интерпретированы для области малого изображения в богатом присутствии."
L["DEFAULT_SMALL_IMAGE_KEY"] = self:SetFormat("*player_alliance*", self.internals.defaults.innerKey)
L["DEFAULT_SMALL_IMAGE_MESSAGE"] = self:SetFormat("*player_alliance*", self.internals.defaults.innerKey)

L["TITLE_PRIMARYBUTTON"] = "Основная кнопка"
L["COMMENT_PRIMARYBUTTON"] = "Данные, которые будут интерпретированы для области основной кнопки в богатом присутствии."

L["TITLE_SECONDARYBUTTON"] = "Вторичная кнопка"
L["COMMENT_SECONDARYBUTTON"] = "Данные, которые будут интерпретированы для области вторичной кнопки в богатом присутствии."

L["TITLE_TOGGLE_ENABLED"] = "Включено"
L["COMMENT_TOGGLE_ENABLED"] = "Определяет, должны ли эти данные использоваться."
L["DEFAULT_TOGGLE_ENABLED"] = "true"

L["TITLE_INPUT_MINIMUMTOC"] = "Минимальная версия TOC"
L["COMMENT_INPUT_MINIMUMTOC"] = "Минимальная версия TOC для регистрации и использования этих данных."
L["USAGE_INPUT_MINIMUMTOC"] = "<5-значный номер TOC или версия игры (x.x.x) здесь>"
L["DEFAULT_INPUT_MINIMUMTOC"] = "Текущая версия => " .. self:GetBuildInfo("extended_version")

L["TITLE_INPUT_MAXIMUMTOC"] = "Максимальная версия TOC"
L["COMMENT_INPUT_MAXIMUMTOC"] = "Максимальная версия TOC для регистрации и использования этих данных."
L["USAGE_INPUT_MAXIMUMTOC"] = "<5-значный номер TOC или версия игры (x.x.x) здесь>"
L["DEFAULT_INPUT_MAXIMUMTOC"] = "Текущая версия => " .. self:GetBuildInfo("extended_version")

L["TITLE_TOGGLE_ALLOWREBASEDAPI"] = "Разрешить ребазированный API"
L["COMMENT_TOGGLE_ALLOWREBASEDAPI"] = "Определяет, использовать ли эти данные с версиями клиента с ребазированным API."
L["DEFAULT_TOGGLE_ALLOWREBASEDAPI"] = "false"

L["TITLE_INPUT_PROCESSCALLBACK"] = "Обратный вызов обработки"
L["COMMENT_INPUT_PROCESSCALLBACK"] = "Функция, которая, если есть, вызывается перед срабатыванием события в аддоне."
L["USAGE_INPUT_PROCESSCALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_PROCESSTYPE"] = "Тип обработки"
L["COMMENT_INPUT_PROCESSTYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова обработки|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_PROCESSTYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_ACTIVECALLBACK"] = "Обратный вызов активности"
L["COMMENT_INPUT_ACTIVECALLBACK"] = "Функция, которая, если есть, возвращается, когда это состояние активно."
L["USAGE_INPUT_ACTIVECALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_ACTIVETYPE"] = "Тип активности"
L["COMMENT_INPUT_ACTIVETYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова активности|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_ACTIVETYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_INACTIVECALLBACK"] = "Обратный вызов неактивности"
L["COMMENT_INPUT_INACTIVECALLBACK"] = "Функция, которая, если есть, возвращается, когда это состояние неактивно."
L["USAGE_INPUT_INACTIVECALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_INACTIVETYPE"] = "Тип неактивности"
L["COMMENT_INPUT_INACTIVETYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова неактивности|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_INACTIVETYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_KEYCALLBACK"] = "Обратный вызов ключа"
L["COMMENT_INPUT_KEYCALLBACK"] = "Функция, которая, если есть, прикрепляется как ключ для этого поля."
L["USAGE_INPUT_KEYCALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_KEYTYPE"] = "Тип ключа"
L["COMMENT_INPUT_KEYTYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова ключа|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_KEYTYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_KEYFORMATCALLBACK"] = "Обратный вызов формата ключа"
L["COMMENT_INPUT_KEYFORMATCALLBACK"] = self:SetFormat("Функция, которая, если есть, используется для форматирования *Обратного вызова ключа|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_KEYFORMATCALLBACK"] = "<Функция или допустимый строковый тип для GetCaseData здесь>"

L["TITLE_INPUT_KEYFORMATTYPE"] = "Тип формата ключа"
L["COMMENT_INPUT_KEYFORMATTYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова формата ключа|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_KEYFORMATTYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_MESSAGECALLBACK"] = "Обратный вызов сообщения"
L["COMMENT_INPUT_MESSAGECALLBACK"] = "Функция, которая, если есть, прикрепляется как сообщение для этого поля."
L["USAGE_INPUT_MESSAGECALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_MESSAGETYPE"] = "Тип сообщения"
L["COMMENT_INPUT_MESSAGETYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова сообщения|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_MESSAGETYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_MESSAGEFORMATCALLBACK"] = "Обратный вызов формата сообщения"
L["COMMENT_INPUT_MESSAGEFORMATCALLBACK"] = self:SetFormat("Функция, которая, если есть, используется для форматирования *Обратного вызова сообщения|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_MESSAGEFORMATCALLBACK"] = "<Функция или допустимый строковый тип для GetCaseData здесь>"

L["TITLE_INPUT_MESSAGEFORMATTYPE"] = "Тип формата сообщения"
L["COMMENT_INPUT_MESSAGEFORMATTYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова формата сообщения|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_MESSAGEFORMATTYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_LABELCALLBACK"] = "Обратный вызов метки"
L["COMMENT_INPUT_LABELCALLBACK"] = "Функция, которая, если есть, прикрепляется как метка для состояния."
L["USAGE_INPUT_LABELCALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_LABELTYPE"] = "Тип метки"
L["COMMENT_INPUT_LABELTYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова метки|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_LABELTYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_URLCALLBACK"] = "Обратный вызов URL"
L["COMMENT_INPUT_URLCALLBACK"] = "Функция, которая, если есть, прикрепляется как URL для состояния."
L["USAGE_INPUT_URLCALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_URLTYPE"] = "Тип URL"
L["COMMENT_INPUT_URLTYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова URL|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_URLTYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_EVENTCALLBACK"] = "Обратный вызов события"
L["COMMENT_INPUT_EVENTCALLBACK"] = "Функция, которая, если есть, срабатывает при вызове игрового события."
L["USAGE_INPUT_EVENTCALLBACK"] = "<Функция или строковая ссылка на функцию здесь>"

L["TITLE_INPUT_REGISTERCALLBACK"] = "Обратный вызов регистрации"
L["COMMENT_INPUT_REGISTERCALLBACK"] = "Функция, которая, если есть и возвращает true, позволяет зарегистрировать данные."
L["USAGE_INPUT_REGISTERCALLBACK"] = "<Булева функция здесь>"

L["TITLE_INPUT_UNREGISTERCALLBACK"] = "Обратный вызов снятия регистрации"
L["COMMENT_INPUT_UNREGISTERCALLBACK"] = "Функция, которая, если есть, выполняется при выгрузке данных."
L["USAGE_INPUT_UNREGISTERCALLBACK"] = "<Допустимая функция здесь>"

L["TITLE_INPUT_STATECALLBACK"] = "Обратный вызов состояния"
L["COMMENT_INPUT_STATECALLBACK"] = "Функция, которая, если есть, определяет, считается ли состояние активным."
L["USAGE_INPUT_STATECALLBACK"] = "<Допустимая функция здесь>"

L["TITLE_INPUT_TAGCALLBACK"] = "Обратный вызов тега"
L["COMMENT_INPUT_TAGCALLBACK"] = "Функция, которая, если есть, позволяет данным иметь условные выражения."
L["USAGE_INPUT_TAGCALLBACK"] = "<Строка или строковая функция здесь>"

L["TITLE_INPUT_TAGTYPE"] = "Тип тега"
L["COMMENT_INPUT_TAGTYPE"] = self:SetFormat("Тип переменной, который должен интерпретироваться для *Обратного вызова тега|r.",
        self.colors.GREEN
)
L["USAGE_INPUT_TAGTYPE"] = "<Название типа переменной здесь, может быть function|string>"

L["TITLE_INPUT_PREFIX"] = "Префикс"
L["COMMENT_INPUT_PREFIX"] = "Префикс, используемый для этих данных."
L["USAGE_INPUT_PREFIX"] = "<Ваше сообщение здесь>"

L["TITLE_INPUT_SUFFIX"] = "Суффикс"
L["COMMENT_INPUT_SUFFIX"] = "Суффикс, используемый для этих данных."
L["USAGE_INPUT_SUFFIX"] = "<Ваше сообщение здесь>"

-- Global Placeholder Defaults
L["DEFAULT_DUNGEON_MESSAGE"] = self:SetFormat("*zone_name* - В подземелье *difficulty_info* *lockout_encounters*", self.internals.defaults.innerKey)
L["DEFAULT_RAID_MESSAGE"] = self:SetFormat("*zone_name* - В рейде *difficulty_info* *lockout_encounters*", self.internals.defaults.innerKey)
L["DEFAULT_SCENARIO_MESSAGE"] = self:SetFormat("*zone_name* - В сценарии *difficulty_info* *lockout_encounters*", self.internals.defaults.innerKey)
L["DEFAULT_BATTLEGROUND_MESSAGE"] = self:SetFormat("*zone_name* - На поле боя", self.internals.defaults.innerKey)
L["DEFAULT_ARENA_MESSAGE"] = self:SetFormat("*zone_name* - На арене", self.internals.defaults.innerKey)
L["DEFAULT_FALLBACK_MESSAGE"] = self:SetFormat("*zone_info*", self.internals.defaults.innerKey)

-- Global Label Defaults
L["DEFAULT_LABEL_AWAY"] = "Отсутствует"
L["DEFAULT_LABEL_BUSY"] = "Занят"
L["DEFAULT_LABEL_DEAD"] = "Мёртв"
L["DEFAULT_LABEL_GHOST"] = "Призрак"
L["DEFAULT_LABEL_COMBAT"] = "В бою"

-- Logging Data
L["VERBOSE_LAST_ENCODED"] = "Последняя отправленная активность => %s"
L["DEBUG_SEND_ACTIVITY"] = "Отправка активности => %s"
L["DEBUG_MAX_BYTES"] = "Максимальный объём байтов, который может быть сохранён: %s"
L["DEBUG_VALUE_CHANGED"] = self:SetFormat("*%s|r изменено с ^%s|r на ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_EVENT_SKIPPED"] = self:SetFormat("Событие пропущено:\n Название: *%s|r\n Данные: ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_EVENT_PROCESSING"] = self:SetFormat("Обработка события:\n Название: *%s|r\n Данные: ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_PLACEHOLDER_PROCESSING"] = self:SetFormat("Обработка заполнителя:\n Название: *%s|r\n Данные: ^%s|r", self.colors.GREEN, self.colors.GREY)
L["INFO_RESET_CONFIG"] = "Сброс данных конфигурации..."
L["INFO_RESET_CONFIG_SINGLE"] = "Сброс данных конфигурации с запросом => %s"
L["INFO_OUTDATED_CONFIG"] = self:SetFormat("Обнаружена устаревшая конфигурация!\n Миграция с версии *v%s|r на *v%s|r...", self.colors.GREEN)
L["INFO_OPTIONAL_MIGRATION_DATA_ONE"] = self:SetFormat("Дополнительные миграции применимы для версии *v%s|r на *v%s|r!",
        self.colors.GREEN
)
L["INFO_OPTIONAL_MIGRATION_DATA_TWO"] = self:SetFormat("Включите *%s|r и выполните ^/cp config migrate|r для их применения.",
        self.colors.GREEN, self.colors.GREY
)
L["ERROR_MESSAGE_OVERFLOW"] = "Сообщение RPC не может быть обработано из-за превышения максимального объёма байтов (%s/%s)"
L["ERROR_COMMAND_CONFIG"] = "Отсутствует необходимая опция конфигурации для выполнения этой команды (Включите %s)"
L["ERROR_COMMAND_UNKNOWN"] = "Неизвестная команда! (Ввод: %s)"
L["WARNING_BUILD_UNSUPPORTED"] = self:SetFormat([[Вы используете *неподдерживаемую|r сборку CraftPresence!

Обнаружена версия: ^%s|r
Примечание: Это сообщение можно игнорировать, если это исходная сборка.]],
        self.colors.RED, self.colors.LIGHT_BLUE
)
L["WARNING_EVENT_RENDERING_ONE"] = "Некоторые игровые настройки могут мешать генерации событий богатого присутствия"
L["WARNING_EVENT_RENDERING_TWO"] = "Проверьте и настройте следующие параметры: %s"
L["ADDON_LOAD_INFO"] = self:SetFormat("^%s|r Загружен.\n Используйте */cp|r или */craftpresence|r для команд.", self.colors.GREEN, self.colors.LIGHT_BLUE)
L["ADDON_CLOSE"] = "Завершение работы богатого присутствия Discord..."
L["ADDON_BUILD_INFO"] = "Информация о сборке: %s"

-- Command: /cp placeholders
L["PLACEHOLDERS_NOTE_ONE"] = self:SetFormat("ПРИМЕЧАНИЕ: Ключи, заключённые в *^|r, являются глобальными (могут содержать внутренние ключи),",
        self.colors.GREEN, self.internals.defaults.globalKey
)
L["PLACEHOLDERS_NOTE_TWO"] = self:SetFormat("в то время как ключи, заключённые в *^|r, являются внутренними (не могут содержать другие ключи)",
        self.colors.GREY, self.internals.defaults.innerKey
)

-- Dynamic Data - Access
L["DATA_QUERY"] = self:SetFormat("Поиск %s, содержащих *%s|r...", self.colors.GREY)
L["DATA_FOUND_INTRO"] = self:SetFormat("Доступные %s (*<ключ>|r => ^<значение>|r):", self.colors.GREEN, self.colors.GREY)
L["DATA_FOUND_NONE"] = self:SetFormat("*Не найдено %s в указанных параметрах|r", self.colors.RED)
L["DATA_FOUND_DATA"] = self:SetFormat("*%s|r => ^%s|r", self.colors.GREEN, self.colors.GREY)

-- Dynamic Data - Creation
L["COMMAND_CREATE_SUCCESS"] = self:SetFormat("%s пользовательский тег *%s|r для ^%s|r с данными: ^%s|r",
        self.colors.GREEN, self.colors.GREY
)
L["COMMAND_CREATE_MODIFY"] = "Указанные аргументы заменят другие данные, используйте команду create:modify"

-- Dynamic Data - Removal
L["COMMAND_REMOVE_SUCCESS"] = self:SetFormat("Удалён ключ в ^%s|r => *%s|r",
        self.colors.GREEN, self.colors.GREY
)
L["COMMAND_REMOVE_NO_MATCH"] = "Не найдено совпадений для указанных аргументов"

-- Command: /cp integration
L["INTEGRATION_QUERY"] = self:SetFormat("Включение интеграций для *%s|r...", self.colors.GREY)
L["INTEGRATION_NOT_FOUND"] = self:SetFormat("*Не найдено интеграций для включения в указанных параметрах|r", self.colors.RED)
L["INTEGRATION_ALREADY_USED"] = self:SetFormat("*Указанная интеграция уже используется|r", self.colors.RED)

-- Command: /cp clear|clean
L["COMMAND_CLEAR_SUCCESS"] = "Очистка активных данных фрейма..."

-- Command: /cp reset
L["COMMAND_RESET_NOT_FOUND"] = "Данные конфигурации, соответствующие запросу, не найдены => %s"

-- Integration: Event Modification
L["COMMAND_EVENT_SUCCESS"] = self:SetFormat("Успешно выполнена операция ^%s|r на *%s|r с привязкой *%s|r",
        self.colors.GREEN, self.colors.GREY
)
L["COMMAND_EVENT_NO_TRIGGER"] = self:SetFormat("Невозможно выполнить операцию ^%s|r на *%s|r (Недопустимый триггер)",
        self.colors.GREEN, self.colors.GREY
)

-- Config Error Standards
L["ERROR_RANGE_DEFAULT"] = self:SetFormat("Проверка корректности не пройдена для *%s|r. Введите число между ^%s|r и ^%s|r.",
        self.colors.GREEN, self.colors.GREY
)
L["ERROR_STRING_DEFAULT"] = self:SetFormat("Проверка корректности не пройдена для *%s|r. Введите допустимую строку.",
        self.colors.GREEN, self.colors.GREY
)

-- Config Warning Standards
L["WARNING_VALUE_UNSAFE"] = self:SetFormat("Выбранное значение для *%s|r может вызвать некорректное поведение в некоторых случаях.",
        self.colors.GREEN
)

-- Function Error Standards
L["ERROR_FUNCTION_DISABLED"] = "Эта функция (%s) отключена в данной версии клиента, попробуйте другие методы..."
L["ERROR_FUNCTION_DEPRECATED"] = self:SetFormat("Используемая вами функция помечена как устаревшая, с информацией: *%s|r",
        self.colors.GREY
)
L["ERROR_FUNCTION"] = self:SetFormat("Используемая вами функция вызвала ошибку, с информацией: *%s|r",
        self.colors.GREY
)
L["TITLE_ATTEMPTED_FUNCTION"] = "Попытка выполнения функции"
L["TITLE_REPLACEMENT_FUNCTION"] = "Функция-замена"
L["TITLE_REMOVAL_VERSION"] = "Версия удаления"
L["TITLE_FUNCTION_MESSAGE"] = "Сообщение"
L["ERROR_FUNCTION_REPLACE"] = self:SetFormat("Для исправления используйте новую функцию или выполните */cp reset|r, если не уверены",
        self.colors.GREY
)

-- General Command Data
L["USAGE_CMD_INTRO"] = "Использование команд =>"
L["USAGE_CMD_HELP"] = self:SetFormat("  */cp|r ^help|r или */cp|r ^?|r  -  Отображает это справочное меню.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CONFIG"] = self:SetFormat("  */cp|r ^config [migrate,standalone]|r  -  Отображает/Мигрирует *ConfigUI|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CLEAN"] = self:SetFormat("  */cp|r ^[clean,clear] [reset]|r  -  Сбрасывает фреймы аддона.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_UPDATE"] = self:SetFormat("  */cp|r ^update [debug]|r  -  Принудительное или отладочное обновление богатого присутствия.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_MINIMAP"] = self:SetFormat("  */cp|r ^minimap|r  -  Переключает кнопку на миникарте.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_COMPARTMENT"] = self:SetFormat("  */cp|r ^compartment|r  -  Переключает запись в отсеке аддонов.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_STATUS"] = self:SetFormat("  */cp|r ^status|r  -  Просмотр последнего отправленного события богатого присутствия.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_RESET"] = self:SetFormat("  */cp|r ^reset [grp,key]|r  -  Сброс опций в *ConfigUI|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_SET"] = self:SetFormat("  */cp|r ^set [grp,key]|r  -  Установка опций в *ConfigUI|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_INTEGRATION"] = self:SetFormat("  */cp|r ^integration [query]|r  -  Включение интеграций.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_PLACEHOLDERS"] = self:SetFormat("  */cp|r ^placeholders [create,remove,list][query]|r  -  Доступ к заполнителям.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_EVENTS"] = self:SetFormat("  */cp|r ^events [create,remove,list] [query]|r  -  Доступ к событиям.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_LABELS"] = self:SetFormat("  */cp|r ^labels [create,remove,list][query]|r  -  Доступ к меткам игрока.",
        self.colors.GREEN, self.colors.GREY
)

L["USAGE_CMD_CREATE_PLACEHOLDERS"] = self:SetFormat("  *Запрос:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CREATE_EVENTS"] = self:SetFormat("  *Запрос:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_CREATE_LABELS"] = self:SetFormat("  *Запрос:|r %s ^[name,minVersion,maxVersion,allowRebasedApi]|r.",
        self.colors.GREEN, self.colors.GREY
)

L["USAGE_CMD_REMOVE_PLACEHOLDERS"] = self:SetFormat("  *Запрос:|r %s ^<name>|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_REMOVE_EVENTS"] = self:SetFormat("  *Запрос:|r %s ^<name>|r.",
        self.colors.GREEN, self.colors.GREY
)
L["USAGE_CMD_REMOVE_LABELS"] = self:SetFormat("  *Запрос:|r %s ^<name>|r.",
        self.colors.GREEN, self.colors.GREY
)

L["USAGE_CMD_NOTE"] = self:SetFormat([[Примечания:
  - Все команды должны начинаться с */%s|r или */%s|r.
  - Необязательные аргументы в командах обозначены как ^[syntax]|r.]],
        self.colors.GREEN, self.colors.GREY
)

-- Frame Text Data
L["ADDON_HEADER_VERSION"] = self:SetFormat("%s *%s|r", self.colors.LIGHT_BLUE)
L["ADDON_HEADER_CREDITS"] = "Благодарности"

L["ADDON_HEADER_ADVANCED_FRAME"] = "Расширенные настройки фрейма"
L["ADDON_SUMMARY_ADVANCED_FRAME"] = self:SetFormat([[Эти опции предназначены только для *Расширенного использования|r.

*Примечание:|r Эти настройки требуют ^перезагрузки клиента|r и настройки свойства ^orientation|r в скрипте Python.]],
        self.colors.GOLD, self.colors.GREEN
)

L["ADDON_SUMMARY"] = "CraftPresence позволяет настраивать отображение вашего игрового процесса в Discord с помощью богатого присутствия."
L["ADDON_DESCRIPTION"] = self:SetFormat([[Создано *CDAGaming|r (https://gitlab.com/CDAGaming)

Благодарность *AipNooBest|r и *wowdim|r на Github за оригинальный базовый проект, который сделал это возможным.

Особая благодарность *the-emerald/python-discord-rpc|r и *AipNooBest/wow-discord-rpc|r]],
        self.colors.GREEN
)

L["ADDON_TOOLTIP_THREE"] = "Нажмите для доступа к данным конфигурации."
L["ADDON_TOOLTIP_FIVE"] = self:SetFormat("Переключите кнопку на миникарте с помощью */cp minimap|r", self.colors.LIGHT_BLUE)
