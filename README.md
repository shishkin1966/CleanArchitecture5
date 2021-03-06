**Clean Architecture version 5**
 
Пример реализации Clean Architecture

При построении приложения использованы архитектурные шаблоны [VIPER](https://www.raywenderlich.com/5192-android-viper-tutorial) и [Service Locator](https://docs.microsoft.com/en-us/previous-versions/msp-n-p/ff648968(v=pandp.10)). [Схема приложения](Схема.png).
Архитектура реализует следующую цепочку событий:

 источник(view -> презентер) -> запрос -> провайдер данных -> исполнитель (специалист) -> сообщение с результатом -> мессенджер <-> получатель (презентер)

В основе архитектуры лежит модель общественного разделения труда и парадигма функционального программирования. Все объекты делятся на генераторы сервиса и пользователей(потребителей) сервисов. Выделены следующие единицы:
- специалист(Specialist) - объект, который предоставляет сервис любым пользователям(объектам) без их учета
- объекты-подписчики (SpecialistSubscriber), которые регистрируются в объединениях для получения/предоставления сервиса
- малые объединения подписчиков(SmallUnion) - объект группы подписчиков(SpecialistSubscriber) для получения/предоставления сервиса
- объединения подписчиков(Union) - объект группы подписчиков(SpecialistSubscriber) для получения/предоставления расширенной функциональности

Регистрацию подписчиков и их объединений, а также специалистов и их взаимное связывание, отмену регистрации осуществляет сервис локатор (Service Locator).

Все объекты учитываемые сервис локатором реализуют интерфейсы Validated и Subscriber. Интерфейс Subscriber, содержит методы:

- String getName() - получить имя подписчика.
- String getPasport() - получить паспорт подписчика

Интерфейс Validated определяет следующие методы:

- Result<Boolean> validateExt() - проверить состояние объекта и получить описание ошибки
- boolean validate() - проверить состояние объекта

Специалист(Specialist) - объект, предоставляющий какую-либо функциональность любым объектам.  
Состав интерфейса Specialist:
- boolean isPersistent() – флаг, определяющий тип специалиста - постоянный/выгружаемый - т.е определяет разрешение операции отмены регистрации
- void onUnRegister() – событие, которое предваряет операцию отмену регистрации 
	специалиста. Используется для внутренних операций в специалисте, необходимых перед отменой 
	регистрации
- void onRegister() – событие, которое завершает операцию регистрации специалиста
- void stop() - остановить работу специалиста

Объединение(SmallUnion или Union) подписчиков (классов, реализующие интерфейс SpecialistSubscriber) учитывает подписчиков только одного типа, и могут быть зависимыми от других специалистов. Интерфейс SpecialistSubscriber, являющийся наследником интерфейса Subscriber и имеет методы:
- List<String> getSpecialistSubscription() – получить список подписки со специалистами (список имен специалистов или объединений, на которые хочет подписаться объект)
	
По времени жизни специалисты делятся на:
- постоянные (или синглтоны). Время жизни - время жизни приложения. Не могут быть выгружены
- нормальные. Могут быть загружены и выгружены
- короткоживущие. Самовыгружающиеся специалисты по какому-либо признаку(например по бездействию или отсутствию подписчиков)

Наследником интерфейса Specialist является интерфейс SmallUnion, имеющий методы:
- Secretary createSecretary() - Получить секретаря (объект учитывающий подписчиков)
- boolean checkSubscriber(T subscriber) - проверить подписчика (и в частности его паспорт). Подписчики не прошедшие проверку не входят в список подписчиков объединения
- void register(T subscriber) – зарегистрировать подписчика
- void unregister(T subscriber) – отписать подписчика
- void unregister(String name) - отписать подписчика по его имени
- List<T> getSubscribers() - получить список всех подписчиков
- List<T> getValidatedSubscribers - получить список валидных подписчиков
- List<T> getReadySubscribers() - получить список готовых подписчиков. Т.е. подписчиков, которые поддерживают состояния(Stateable) и находятся в соостянии отличном от CREATE и DESTROY
- boolean hasSubscribers() - проверить наличие подписчиков
- boolean hasSubscriber(String name) - проверить наличие подписчика
- T getSubscriber(String name) – получить подписчика по его имени.
- Result<Boolean> validate(String name) - проверить подписчика
- void onRegisterFirstSubscriber() - событие - появился первый подписчик
- void onUnRegisterLastSubscriber() - событие - отписан последний подписчик
- void onAddSubscriber(T subscriber) - событие - добавлен подписчик

Наследником интерфейса SmallUnion является Union. Состав методов:
- T getCurrentSubscriber() - получить текущего подписчика объединения. Если подписчики реализуют интерфейс Stateable 
	(имеющий состояние), то выбирается объект, находящий в состоянии STATE_RESUME 
	(текущий на экране). Если нет - выбирается первый по списку.
- void setCurrentSubscriber(T subscriber) – установить текущего подписчика у объединения

Как было указано, управлением специалистов и объединений занимается администратор (Service Locator), 
реализующий интерфейс ServiceLocator:
- <C> C get(String specialistName) – получить специалиста/объединение по его имени
- boolean register(Specialist module) - зарегистрировать специалиста/объединение. Используется только 
	для Singleton объектов
- boolean register(String name) – зарегистрировать специалиста/объединение, предварительно создав 
	его. Предпочительный метод.
- boolean unregister(String specialistName) - отменить регистрацию специалиста/объединения
- boolean register(SpecialistSubscriber subscriber) – зарегистрировать подписчика объединений
- boolean unregister(SpecialistSubscriber subscriber) – отменить регистрацию подписчика
- boolean setCurrentSubscriber(SpecialistSubscriber subscriber) – сделать подписчика текущим. Администратор сам 
	сделает текущим данного подписчика у всех объединений, на которые подписан объект
- void onStart - событие - старт Service Locator
- void onStop - событие - остановка Service Locator

Регистрация специалистов(объединений) проводиться по имени класса. При этом ключом является только его 
последняя часть(без package name), что дает возможность гибко использовать различных специалистов с одним именем, 
но описанные в разных package. Исключение составляют постоянные специалисты(объединения), которые
нельзя повторно зарегистрировать. Также невозможно отменить регистрацию у объединений, которые имеют подписчиков. При регистрации специалиста проверяется соответствие интерфейсов, если специалист с таким же именем уже загружен.

Привязка и отвязка подписчиков (классов с интерфейсом SpecialistSubscriber) к специалистам осуществляется администратором. Если специалист не загружен производится автоматическая создание необходимых специалистов(объединений)(кроме Singleton классов). Singleton классы должны быть созданы и зарегистрированы у администратора заранее. Отписка подписчиков проводится сразу от всех подписанных им специалистов. При удалении подписчиков проверяется их значение на совпадение. Если совпадают имена подписчиков, но не равны значения - операция игнорируется.

В составе данной реализации описаны следующие специалисты/объединения:
- ApplicationSpecialistImpl(Singleton) - служит для получения контекста приложения. Является слушателем событий: переход приложения в фон, восстановление приложения из фона, гашение экрана, включение экрана. 
- ErrorSpecialistImpl(Singleton) - специалист для регистрации ошибок. Предоставляет функционал регистрации ошибок в ходе работы приложения
- CrashSpecialist - специалист, получающий управление при незапланированных прерываниях приложения
- MessagerUnionImpl - объединение для учета почтовых клиентов или месседжер. Обеспечивает хранение и гарантированную доставку почтовых сообщений. Почтовые сообщения хранятся в объединении и подписчики читают их при переходе в состояние STATE_RESUME и при подключению к месседжеру. Если получатель сообщения находится в этом же состоянии(STATE_RESUME), то сообщения доставляются сразу же. Возможен контроль сообщений на наличие дубликатов, при этом будут удалятся все предыдущие копии данного сообщения. Поддерживаются списки рассылки. Почтовое объединение хранит почтовые сообщения независимо от цикла жизни подписчиков(почта подписчика сохраняется при уничтожении подписчика и доступна при его повторном создании). Возможно добавление сообщений только активным получателям.
- ViewUnionImpl - объединение, отвечающее за View объекты и в частности это Activities. Предоставляет сервис Activity для пользователей приложения
- PresenterUnionImpl - объединение, регистрирующее презентеры и обеспечивающий доступ к ним. Презентеры могут быть локальными,   т.е. регистрируются только локально или глобальные, ссылку на которые можно получить через данное объединение. Презентеры реализуют интерфейс Stateable и имеют жизненный цикл View, к которому привязаны.
- UseCasesSpecialistImpl - специалист, обрабатывающий бизнес и прочую логику приложения
- ObservableUnionImpl - объединение, отвечающее за учет Observable объектов. Поддерживаются ContentObserver объекты, BroadcastReceiver и слушатели БД. Каждый Observable объект могут иметь нескольких слушателей. Старт подписки на событие проводиться при появлении первого слушателя Observable объекта, а отписка - при разрегистрации последнего слушателя. Db Observable объекты регистрируют изменения в не зависимости от наличия слушателей.
- RequestSpecialistImpl - специалист, осуществляющий выполнение различных запросов(классов с интерфейсом Request), как в параллельном, так и последовательном режиме, а также await задач. Запросы реализуют интерфейс Runnable.
- CacheSpecialistImpl - специалист, реализующий кэш в памяти
- SecureStorageSpecialistImpl - специалист, позволяющий шифрование и дешифрацию, а также хранение на диске зашифрованных данных
- NotificationSpecialistImpl - специалист, предназначенный для вывода сообщений в зону уведомлений 
- LocationUnionImpl - объединение, предоставляющее сервис геолокации. Поддерживается остановка сервиса при гашении экрана и ухода приложения в фон. 
- PreferencesSpecialistImpl - специалист Preferences приложения
- NetProviderImpl - реализация провайдера для выборки данных из сети
- DbProviderImpl - реализация провайдера для выборки данных из БД SQLite. Допускается соединение сразу с несколькими БД.
- JobSpecialistImpl - специалист для запуска заданий по расписанию
- MediaSpecialistImpl - специалист для проигрывания media файлов
- ScannerUnionImpl - специалист по сканированию баркодов
- IdleSpecialistImpl - специалист, отвечающий за бездействие пользователя
- CalculationUnionImpl - пример объединения для долгих расчетов, результаты которого слушают несколько объектов. Сам расчет зависит от множества входящих параметров.
- SerializableDiskStorageSpecialistImpl - специалист, реализующий кэш на диске для Serializable объектов
- PaginatorUnionImpl - объединения для учета объектов постраничной выборки данных. В отличие от PagedList от Google Paginator поддерживает повторную выборку данных, стратегию размеров выборки данных (размер первоначальной выборки и последующих).

А также Observable объекты:
- DbObservable - слушает изменения в БД
- NetworkBroadcastReceiverObservable - слушает наличие/отсутствие сети и ее качество
- ScreenBroadcastReceiverObservable - слушает включение/гашение экрана
- AbsContentObservable - слушает изменения в ContentProvider

Подробнее остановимся на DbObservable объекте. Объект DbObservable предназначен для регистрации изменений
в объектах БД и оповещении об этом своих слушателей. Для этого DbObservable регистрирует объекты, реализующие интерфейс DbObservableSubscriber (наследник от ObservableSubscriber),
который содержит метод:
- List<String> getListenObjects() - получить список наименований слушаемых объектов (и в частности таблиц БД)

Каждый запрос, изменяющий объекты БД, может оповещать об этом DbObservable, вызывая метод:
- onChange(String object)

Получая вызов данного метода, DbObservable объект осуществляет оповещение зарегистренных к данному моменту слушателей.


Все указанные специалисты и объединения приведены только для примера. В качестве примера взяты только 4 реальных экрана - просмотр и создание счетов, курсы криптовалют, просмотр карт. Образец охватывает максимально широкий спектр задач и ситуаций. Реализованы следующие возможности:
- отслеживание гашения/включения экрана
- отслеживание перехода приложения в фон и выход из фона
- отслеживание наличия и отсутствия сети (с контролем кол-ва параллельных запросов в зависимости от качества сети)
- отслеживание изменений в таблицах БД
- отслеживание бездействия пользователя
- резервное копирование и восстановление настроек приложения и БД на внешнем устройстве
- использованы запросы к БД, к нескольким сетевым провайдерам, постраничный запрос и постраничный запрос от Google, и долгое вычисление с многими изменяющимися параметрами
- вывод уведомлений
- использование геолокации и определение адреса места
- использование сканера баркодов
- разная верстка для различных устройств (для 10 дюймовых планшетов в ориентации горизонтальная бокового меню нет)

В реальных условиях возможно проектирование своих специалистов и объединений, реализующий необходимый только вам функционал.

Что не добавлено? Не добавлен специалист рабочих столов(он разработан, но не включен в данный проект). Примерами могут служить настройка
приложения 1С на рабочие места или настольные банковские клиенты. Специалист рабочих столов - это
более широкое понятие, чем смена theme в приложении и включает в себя как смену цветовой гаммы, так и смену верстки экранов приложения.

