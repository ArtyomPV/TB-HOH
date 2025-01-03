есть телеграм бот на  java, у него есть инлайн клавиатура, например четыре кнопки, и также есть обработчик который обрабатывает callback от инлайн клавиатуры, как написать код используя слоистую архитектруру


Создание Telegram бота на Java с использованием слоистой архитектуры подразумевает разделение вашего приложения на несколько слоев, таких как слой обработки, слой сервиса и слой представления. Я покажу вам, как можно организовать такой код, используя библиотеку [TelegramBots](https://github.com/rubenlagus/TelegramBots) для работы с Telegram Bot API.

### Структура Проекта

1. **Controller (Слой контроллера)**: Обрабатывает входящие сообщения и вызовы от Telegram.
2. **Service (Слой сервиса)**: Логика обработки команд и вызовов.
3. **Model (Модель)**: Сущности, которые вы используете в приложении.

### 1. Добавление Зависимостей

Если вы используете Maven, добавьте зависимость `telegrambots-spring-boot-starter` в ваш `pom.xml`:

```xml
<dependency>
    <groupId>org.telegram</groupId>
    <artifactId>telegrambots-spring-boot-starter</artifactId>
    <version>5.5.0</version>
</dependency>
```

### 2. Создание Моделей

Создайте модели для ваших данных, если они необходимы. Например:

```java
package ru.gb.telegrambot.model;

public class Button {
    private String text;
    private String callbackData;
    
    // Getter и Setter
}
```

### 3. Создание Сервиса

Создайте сервис, который будет содержать логику обработки команд и callback-ов от кнопок:

```java
package ru.gb.telegrambot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class BotService {
    
    public SendMessage handleCallback(String callbackData) {
        SendMessage response = new SendMessage();
        switch (callbackData) {
            case "button1":
                response.setText("Вы нажали кнопку 1");
                break;
            case "button2":
                response.setText("Вы нажали кнопку 2");
                break;
            case "button3":
                response.setText("Вы нажали кнопку 3");
                break;
            case "button4":
                response.setText("Вы нажали кнопку 4");
                break;
            default:
                response.setText("Неизвестная команда");
        }
        return response;
    }
}
```

### 4. Создание Контроллера Бота

Создайте контроллер, который будет обрабатывать входящие сообщения и связанные с ними события:

```java
package ru.gb.telegrambot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import ru.gb.telegrambot.service.BotService;

@Component
public class TelegramBotController extends TelegramLongPollingBot {

    @Autowired
    private BotService botService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        }
    }

    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        SendMessage response = botService.handleCallback(callbackData);
        response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        try {
            execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Update update) {
        // Логика обработки текстовых сообщений, если необходимо
    }

    @Override
    public String getBotUsername() {
        return "YOUR_BOT_USERNAME"; // Укажите здесь имя вашего бота
    }

    @Override
    public String getBotToken() {
        return "YOUR_BOT_TOKEN"; // Укажите здесь токен вашего бота
    }
}
```

### 5. Создание Инлайн Клавиатуры

Вы можете создать инлайн клавиатуру в методе обработки сообщений:

```java
private void handleMessage(Update update) {
    SendMessage message = new SendMessage();
    message.setChatId(update.getMessage().getChatId());
    message.setText("Выберите кнопку:");

    // Создание инлайн клавиатуры
    InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    
    List<InlineKeyboardButton> row1 = Arrays.asList(
        new InlineKeyboardButton().setText("Кнопка 1").setCallbackData("button1"),
        new InlineKeyboardButton().setText("Кнопка 2").setCallbackData("button2")
    );

    List<InlineKeyboardButton> row2 = Arrays.asList(
        new InlineKeyboardButton().setText("Кнопка 3").setCallbackData("button3"),
        new InlineKeyboardButton().setText("Кнопка 4").setCallbackData("button4")
    );

    rows.add(row1);
    rows.add(row2);
    
    keyboardMarkup.setKeyboard(rows);
    message.setReplyMarkup(keyboardMarkup);

    try {
        execute(message);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### Заключение

Таким образом, у вас будет организованный проект с использованием слоистой архитектуры для Telegram бота на Java. Если у вас есть вопросы или нужна помощь с конкретными частями, не стесняйтесь спрашивать!


в классе BotService нужно использовать паттерн Стратегия вместо switch

Для реализации паттерна "Стратегия" в вашем `BotService`, мы можем создать несколько классов, реализующих общий интерфейс для обработки различных команд. Это позволит избежать использования конструкции `switch`, сделав код более чистым и управляемым.

### 1. Определение Интерфейса Стратегии

Сначала создайте интерфейс `CallbackHandler`, который определяет метод для обработки callback-ов:

```java
package ru.gb.telegrambot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CallbackHandler {
    SendMessage handle(String callbackData);
}
```

### 2. Создание Реализаций Стратегий

Теперь создайте конкретные классы, реализующие интерфейс `CallbackHandler`.

```java
package ru.gb.telegrambot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class Button1Handler implements CallbackHandler {
    @Override
    public SendMessage handle(String callbackData) {
        SendMessage response = new SendMessage();
        response.setText("Вы нажали кнопку 1");
        return response;
    }
}

@Component
public class Button2Handler implements CallbackHandler {
    @Override
    public SendMessage handle(String callbackData) {
        SendMessage response = new SendMessage();
        response.setText("Вы нажали кнопку 2");
        return response;
    }
}

@Component
public class Button3Handler implements CallbackHandler {
    @Override
    public SendMessage handle(String callbackData) {
        SendMessage response = new SendMessage();
        response.setText("Вы нажали кнопку 3");
        return response;
    }
}

@Component
public class Button4Handler implements CallbackHandler {
    @Override
    public SendMessage handle(String callbackData) {
        SendMessage response = new SendMessage();
        response.setText("Вы нажали кнопку 4");
        return response;
    }
}
```

### 3. Модификация `BotService`

Теперь измените `BotService`, чтобы он использовал список стратегий для обработки `callbackData`:

```java
package ru.gb.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

@Service
public class BotService {

    private final Map<String, CallbackHandler> handlers = new HashMap<>();

    @Autowired
    public BotService(Button1Handler button1Handler, 
                      Button2Handler button2Handler,
                      Button3Handler button3Handler,
                      Button4Handler button4Handler) {

        handlers.put("button1", button1Handler);
        handlers.put("button2", button2Handler);
        handlers.put("button3", button3Handler);
        handlers.put("button4", button4Handler);
    }

    public SendMessage handleCallback(String callbackData) {
        CallbackHandler handler = handlers.get(callbackData);
        
        if (handler != null) {
            return handler.handle(callbackData);
        } else {
            SendMessage response = new SendMessage();
            response.setText("Неизвестная команда");
            return response;
        }
    }
}
```

### 4. Заключение

С помощью вышеописанных изменений реализован паттерн "Стратегия" в обработке команд вашего Telegram бота. Теперь вместо конструкции `switch`, каждый обработчик реализует интерфейс `CallbackHandler`, что позволяет легко добавлять новые обработчики без изменения существующего кода.

Если у вас возникнут дополнительные вопросы или потребуется помощь с другими аспектами, дайте знать!


## К данному телеграм боту нужно добавить модуль для сохранения в базу данных пользователей которые общались с ботом, использовать postgresql и сохранять id пользователя, дату и время общения

Для сохранения информации о пользователях, взаимодействующих с вашим Telegram-ботом, в базу данных PostgreSQL, вам необходимо:

1. **Добавить зависимости для работы с базой данных**.
2. **Создать модель базы данных**.
3. **Создать репозиторий для доступа к данным**.
4. **Интегрировать сохранение данных в логику вашего бота**.

### Шаг 1: Добавление зависимостей

Если вы используете **Maven**, добавьте следующее в ваш `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Если используете **Gradle**, добавьте следующее в ваш `build.gradle`:

```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
runtimeOnly 'org.postgresql:postgresql'
```

### Шаг 2: Конфигурация подключения к базе данных

Добавьте настройки подключения к базе данных в ваш `application.properties` или `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Шаг 3: Создание модели пользователя

Создайте класс модели для сохранения информации о пользователе бота:

```java
package ru.gb.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_id", unique = true, nullable = false)
    private Long telegramId;

    @Column(name = "interaction_time", nullable = false)
    private LocalDateTime interactionTime;

    // Constructors, Getters and Setters

    public User() {}

    public User(Long telegramId, LocalDateTime interactionTime) {
        this.telegramId = telegramId;
        this.interactionTime = interactionTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public LocalDateTime getInteractionTime() {
        return interactionTime;
    }

    public void setInteractionTime(LocalDateTime interactionTime) {
        this.interactionTime = interactionTime;
    }
}
```

### Шаг 4: Создание репозитория

Создайте интерфейс репозитория для доступа к данным:

```java
package ru.gb.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.telegrambot.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
```

### Шаг 5: Сохранение пользователей в базе данных

Теперь обновите ваш `BotService` или контроллер бота, чтобы сохранять информацию о пользователе, когда он взаимодействует с ботом.

```java
package ru.gb.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.gb.telegrambot.model.User;
import ru.gb.telegrambot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class BotService {
    
    private final UserRepository userRepository;
    private final Map<String, CallbackHandler> handlers = new HashMap<>();

    @Autowired
    public BotService(UserRepository userRepository, 
                      Button1Handler button1Handler, 
                      Button2Handler button2Handler,
                      Button3Handler button3Handler,
                      Button4Handler button4Handler) {
        this.userRepository = userRepository;
        
        handlers.put("button1", button1Handler);
        handlers.put("button2", button2Handler);
        handlers.put("button3", button3Handler);
        handlers.put("button4", button4Handler);
    }

    public SendMessage handleCallback(String callbackData, Long telegramId) {
        // Сохранение пользователя в базе данных
        User user = new User(telegramId, LocalDateTime.now());
        userRepository.save(user);
        
        CallbackHandler handler = handlers.get(callbackData);
        
        if (handler != null) {
            return handler.handle(callbackData);
        } else {
            SendMessage response = new SendMessage();
            response.setText("Неизвестная команда");
            return response;
        }
    }
}
```

### Шаг 6: Изменение обработки Callback в контроллере

Теперь необходимо изменить обработку callback в вашем контроллере, чтобы передавать `telegramId` в `BotService`.

```java
private void handleCallbackQuery(Update update) {
    String callbackData = update.getCallbackQuery().getData();
    Long telegramId = update.getCallbackQuery().getFrom().getId(); // Получаем ID пользователя Telegram
    SendMessage response = botService.handleCallback(callbackData, telegramId);
    response.setChatId(update.getCallbackQuery().getMessage().getChatId());
    try {
        execute(response);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### Заключение

Теперь ваш бот сохраняет информацию о пользователях, взаимодействующих с ним, в базе данных PostgreSQL. Это реализовано с помощью слоистой архитектуры, что делает ваш код модульным и легким для расширения.

Если у вас есть дополнительные вопросы или нужны уточнения, дайте знать!

## Нужно модернизировать код:
1. На стартовом экране вывести текс с приветствием и с инлайн клавиатурой с тремя кнопками: Строительство, Инженерные сети, Автоматика
2. При нажатии кнопки Строительство вывести текст: "Здесь вы можете ознакомиться с завершенными объектами, прочитать интересные статьи и если остались вопросы свяжитесь с нами" под текстом должны быть четыре кнопки : Проекты, Интересные статьи, Связаться с нами и кнопка назад которая возвращает в предыдущий экран.
3. При нажатии на кнопку Проекты вывести новый экран с текстом заголовком , кртинкой и описанием к картинке    

Давайте обновим предложение и предоставим более полное и структурированное решение, чтобы создать Telegram-бота, который выводит приветствие с инлайн-клавиатурой и обрабатывает различные команды. Я также включу комментарии для лучшего понимания.

### Шаг 1: Создание структуры бота

Вот общая структура и классы, которые нам понадобятся.

1. `TelegramBotController` — основная логика обработки сообщений.
2. `BotService` — сервис для обработки команд и управления состоянием.
3. Классы для кнопок и отправки сообщений.

### Код для `TelegramBotController`

```java
package ru.gb.telegrambot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.gb.telegrambot.service.BotService;

@Component
public class TelegramBotController extends TelegramLongPollingBot {
    
    @Autowired
    private BotService botService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        }
    }

    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        Long telegramId = update.getCallbackQuery().getFrom().getId();
        SendMessage response = botService.handleCallback(callbackData, telegramId);
        response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        try {
            execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Update update) {
        // Обработка команды /start для приветственного сообщения
        if (update.getMessage().getText().equals("/start")) {
            sendWelcomeMessage(update.getMessage().getChatId());
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Добро пожаловать! Пожалуйста, выберите один из вариантов:");

        // Создание инлайн клавиатуры
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = Arrays.asList(
            new InlineKeyboardButton().setText("Строительство").setCallbackData("construction"),
            new InlineKeyboardButton().setText("Инженерные сети").setCallbackData("engineering_networks"),
            new InlineKeyboardButton().setText("Автоматика").setCallbackData("automation")
        );

        rows.add(row);
        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "YOUR_BOT_USERNAME"; // Укажите имя вашего бота
    }

    @Override
    public String getBotToken() {
        return "YOUR_BOT_TOKEN"; // Укажите токен вашего бота
    }
}
```

### Код для `BotService`

Теперь обновим `BotService`, чтобы добавить логику обработки кнопок, а также кнопок "Проекты", "Интересные статьи", "Связаться с нами" и "Назад".

```java
package ru.gb.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.gb.telegrambot.model.User;
import ru.gb.telegrambot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BotService {
    
    private final UserRepository userRepository;

    @Autowired
    public BotService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SendMessage handleCallback(String callbackData, Long telegramId) {
        SendMessage message = new SendMessage();
        message.setChatId(telegramId.toString());  // Указываем чат ID

        // Сохраняем информацию о пользователе
        User user = new User(telegramId, LocalDateTime.now());
        userRepository.save(user);

        switch (callbackData) {
            case "construction":
                message.setText("Здесь вы можете ознакомиться с завершенными объектами, прочитать интересные статьи и если остались вопросы свяжитесь с нами.");
                addConstructionOptions(message);
                break;
            case "projects":
                sendProjectInfo(message.getChatId());
                break;
            case "interesting_articles":
                message.setText("Здесь вы можете прочитать интересные статьи.");
                // Добавьте логику или вызов метода для интересных статей
                addConstructionOptions(message); // Возвращение к кнопкам
                break;
            case "contact_us":
                message.setText("Свяжитесь с нами по контактным данным...");
                addConstructionOptions(message); // Возвращение к кнопкам
                break;
            case "back":
                message.setText("Добро пожаловать! Пожалуйста, выберите один из вариантов:");
                addMainOptions(message);
                break;
            default:
                message.setText("Неизвестная команда");
        }
        return message;
    }

    private void addConstructionOptions(SendMessage message) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = Arrays.asList(
            new InlineKeyboardButton().setText("Проекты").setCallbackData("projects"),
            new InlineKeyboardButton().setText("Интересные статьи").setCallbackData("interesting_articles"),
            new InlineKeyboardButton().setText("Связаться с нами").setCallbackData("contact_us"),
            new InlineKeyboardButton().setText("Назад").setCallbackData("back")
        );

        rows.add(row);
        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);
    }

    private void addMainOptions(SendMessage message) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = Arrays.asList(
            new InlineKeyboardButton().setText("Строительство").setCallbackData("construction"),
            new InlineKeyboardButton().setText("Инженерные сети").setCallbackData("engineering_networks"),
            new InlineKeyboardButton().setText("Автоматика").setCallbackData("automation")
        );

        rows.add(row);
        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);
    }

    private void sendProjectInfo(String chatId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto("https://example.com/your-project-image.jpg"); // Укажите URL изображения
        sendPhoto.setCaption("Заголовок проекта\nОписание: Здесь вы можете получить информацию о проекте...");

        try {
            execute(sendPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### Шаг 3: Обработка проекта и изображения

В методе `sendProjectInfo` отправляется изображение с заголовком и описанием. Вам нужно будет заменить `https://example.com/your-project-image.jpg` на реальный URL изображения вашего проекта.

### Заключение

Теперь ваш бот будет:

1. Выводить приветствие с инлайн-клавиатурой.
2. При нажатии на кнопку "Строительство" отображать текст с предложением и кнопками для других действий.
3. Отправлять изображение и описание проекта, когда пользователь нажимает кнопку "Проекты".

Если у вас есть дополнительные вопросы или изменения, дайте знать!

## нужно использовать стратегию Command и Strategy

Для реализации функциональности, которая позволяет пользователям просматривать список проектов с кнопками "Следующий", "Предыдущий" и "Назад", нам нужно сделать следующее:

1. Создать класс `Project`, который будет содержать название, описание и фото.
2. Создать класс `ProjectCommand`, который будет управлять выводом информации о проектах.
3. Обновить `CommandManager` и соответствующий класс для обработки состояния просмотра проектов.

### Шаг 1: Создание класса `Project`

Создадим класс `Project`, который будет представлять проект.

```java
package ru.gb.telegrambot.model;

public class Project {
    private String title;
    private String imageUrl;
    private String description;

    public Project(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
```

### Шаг 2: Реализация `ProjectCommand`

Теперь создадим класс `ProjectCommand`, который будет обрабатывать отображение проектов.

```java
package ru.gb.telegrambot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectCommand implements Command {
    private List<Project> projects;
    private int currentIndex;

    public ProjectCommand(List<Project> projects) {
        this.projects = projects;
        this.currentIndex = 0; // Начнем с первого проекта
    }

    @Override
    public SendMessage execute(Long chatId) {
        return showProject(chatId);
    }

    private SendMessage showProject(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());

        if (currentIndex < 0 || currentIndex >= projects.size()) {
            sendMessage.setText("Проект не найден.");
            return sendMessage;
        }

        Project project = projects.get(currentIndex);
        sendMessage.setText("**" + project.getTitle() + "**\n\n" + project.getDescription());

        // Создание инлайн кнопок для навигации
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Кнопки для навигации
        List<InlineKeyboardButton> row = Arrays.asList(
            new InlineKeyboardButton().setText("Предыдущий").setCallbackData("previous"),
            new InlineKeyboardButton().setText("Следующий").setCallbackData("next"),
            new InlineKeyboardButton().setText("Назад").setCallbackData("back")
        );

        rows.add(row);
        keyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(keyboardMarkup);

        return sendMessage;
    }

    public SendPhoto showProjectImage(Long chatId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId.toString());
        Project project = projects.get(currentIndex);
        sendPhoto.setPhoto(project.getImageUrl());
        sendPhoto.setCaption(project.getDescription());

        return sendPhoto;
    }

    public void nextProject() {
        if (currentIndex < projects.size() - 1) {
            currentIndex++;
        }
    }

    public void previousProject() {
        if (currentIndex > 0) {
            currentIndex--;
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
```

### Шаг 3: Обновление `CommandManager`

Теперь обновим `CommandManager`, чтобы включить `ProjectCommand`.

```java
package ru.gb.telegrambot.service;

import org.springframework.stereotype.Service;
import ru.gb.telegrambot.command.Command;
import ru.gb.telegrambot.command.ConstructionCommand;
import ru.gb.telegrambot.command.ProjectCommand;
import ru.gb.telegrambot.model.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandManager {
    private final Map<String, Command> commandMap = new HashMap<>();
    private ProjectCommand projectCommand;

    public CommandManager() {
        // Создаем список проектов
        List<Project> projects = new ArrayList<>();
        projects.add(new Project("Проект 1", "https://example.com/image1.jpg", "Описание проекта 1"));
        projects.add(new Project("Проект 2", "https://example.com/image2.jpg", "Описание проекта 2"));
        projects.add(new Project("Проект 3", "https://example.com/image3.jpg", "Описание проекта 3"));
        projects.add(new Project("Проект 4", "https://example.com/image4.jpg", "Описание проекта 4"));

        projectCommand = new ProjectCommand(projects);

        // Регистрируем команды
        commandMap.put("/start", new StartCommand());
        commandMap.put("construction", projectCommand);
        // Другие команды
    }

    public Command getCommand(String commandName, Long chatId) {
        switch (commandName) {
            case "projects":
                return projectCommand;
            case "next":
                projectCommand.nextProject();
                return projectCommand;
            case "previous":
                projectCommand.previousProject();
                return projectCommand;
            case "back":
                return new ConstructionCommand();
            default:
                return commandMap.get(commandName);
        }
    }
}
```

### Шаг 4: Обновление `TelegramBotController`

Теперь обновим `TelegramBotController`, чтобы включить добавление метода, который будет обрабатывать новые команды.

```java
@Override
public void onUpdateReceived(Update update) {
    if (update.hasCallbackQuery()) {
        handleCallbackQuery(update);
    } else if (update.hasMessage() && update.getMessage().hasText()) {
        handleMessage(update);
    }
}

private void handleCallbackQuery(Update update) {
    String callbackData = update.getCallbackQuery().getData();
    Long telegramId = update.getCallbackQuery().getFrom().getId();
    
    Command command = commandManager.getCommand(callbackData, telegramId);
    if (command != null) {
        if (command instanceof ProjectCommand) {
            // Отправляем изображение проекта
            SendPhoto photoMessage = ((ProjectCommand) command).showProjectImage(telegramId);
            try {
                execute(photoMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            SendMessage response = command.execute(telegramId);
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            try {
                execute(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

### Заключение

Теперь ваш бот будет:

1. Показывать название, описание и фото проекта при нажатии на кнопку "Проекты".
2. Обеспечивать навигацию между проектами с помощью кнопок "Следующий", "Предыдущий" и "Назад".

Не забудьте заменить URL изображений на реальные. Если у вас есть другие вопросы или требуется дополнительная функциональность, дайте знать!
