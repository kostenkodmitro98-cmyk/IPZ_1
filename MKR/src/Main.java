// Базовий інтерфейс компонента
interface Message {
    String getContent();
}

// Конкретний компонент
class BasicMessage implements Message {
    private String text;

    public BasicMessage(String text) {
        this.text = text;
    }

    @Override
    public String getContent() {
        return text;
    }
}

//Декоратор
abstract class MessageDecorator implements Message {
    protected Message decoratedMessage;

    public MessageDecorator(Message message) {
        this.decoratedMessage = message;
    }

    @Override
    public String getContent() {
        return decoratedMessage.getContent();
    }
}

// Декоратор для шифрування
class EncryptedMessage extends MessageDecorator {
    public EncryptedMessage(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        return encrypt(super.getContent());
    }

    private String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char)(((c - base + 3) % 26) + base);
            }
            result.append(c);
        }
        return result.toString();
    }
}

// Декоратор для стиснення тексту
class CompressedMessage extends MessageDecorator {
    public CompressedMessage(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        return compress(super.getContent());
    }

    private String compress(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }
}

// Декоратор для додавання дати та часу
class TimestampMessage extends MessageDecorator {
    public TimestampMessage(Message message) {
        super(message);
    }

    @Override
    public String getContent() {
        String timestamp = java.time.LocalDateTime.now().toString();
        return super.getContent() + " [Time: " + timestamp + "]";
    }
}

// Декоратор для додавання ПІБ автора
class AuthorMessage extends MessageDecorator {
    private String author;

    public AuthorMessage(Message message, String author) {
        super(message);
        this.author = author;
    }

    @Override
    public String getContent() {
        return super.getContent() + " [Author: " + author + "]";
    }
}

// Головний клас
public class Main {
    public static void main(String[] args) {
        // Початкове повідомлення
        Message message = new BasicMessage("Hello   World! My name is Dmytro.");
        System.out.println("Оригінальне повідомлення: " + message.getContent());

        // Застосовуємо стиснення
        message = new CompressedMessage(message);
        System.out.println("Після стиснення: " + message.getContent());

        // Додаємо час
        message = new TimestampMessage(message);
        System.out.println("Після додавання часу: " + message.getContent());

        // Додаємо автора
        message = new AuthorMessage(message, "Kostenko Dmytro");
        System.out.println("Після додавання автора: " + message.getContent());

        // Шифруємо повідомлення
        message = new EncryptedMessage(message);
        System.out.println("Після шифрування: " + message.getContent());

        System.out.println("\n--- Інший порядок обробки ---");

        // Інший порядок: спочатку шифрування, потім автор
        Message message2 = new BasicMessage("Secret information");
        System.out.println("Оригінал: " + message2.getContent());

        message2 = new EncryptedMessage(message2);
        System.out.println("Шифрування: " + message2.getContent());

        message2 = new AuthorMessage(message2, "Kostenko Dmytro");
        System.out.println("Додаємо автора: " + message2.getContent());

        System.out.println("\n--- Всі обробки разом ---");
        Message message3 = new BasicMessage("Final   example  text");
        message3 = new CompressedMessage(message3);
        message3 = new TimestampMessage(message3);
        message3 = new AuthorMessage(message3, "Kostenko Dmytro");
        message3 = new EncryptedMessage(message3);
        System.out.println("Результат: " + message3.getContent());
    }
}