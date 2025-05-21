package com.library;

import com.library.entity.Issue;
import com.library.entity.User;
import com.library.entity.Book;
import com.library.service.AuthService;
import com.library.service.BookService;
import com.library.service.IssueService;
import com.library.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final AuthService authService = new AuthService();
    private static final BookService bookService = new BookService();
    private static final IssueService issueService = new IssueService();
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в библиотеку!");
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.print(">> ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            User currentUser = null;

            switch (choice) {
                case 1:
                    System.out.println("Введите имя:");
                    String firstName = scanner.nextLine();
                    System.out.println("Введите фамилию:");
                    String lastName = scanner.nextLine();
                    System.out.println("Введите отчество:");
                    String middleName = scanner.nextLine();
                    System.out.println("Введите никнейм:");
                    String nickname = scanner.nextLine();

                    currentUser = authService.register(firstName, lastName, middleName, nickname);
                    if (currentUser.getId() != null) {
                        System.out.println("Регистрация прошла успешно!");
                    } else {
                        System.out.println("Пользователь не зарегистрирован");
                    }

                    break;
                case 2:
                    System.out.println("Введите никнейм для входа:");
                    String _nickname = scanner.nextLine();
                    try {
                        currentUser = authService.authenticate(_nickname);
                        System.out.println("Вход выполнен успешно!");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    break;
                default:
                    System.out.println("Неверный ввод");
                    break;
            }

            // Логика в зависимости от роли
            if (currentUser.getId() != null) {
                if (currentUser.getRole() == User.Role.ADMIN) {
                    adminMenu(scanner);
                } else {
                    userMenu(scanner, currentUser);
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Ожидалось число...");
        }
    }

    private static void adminMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Меню администратора:");
            System.out.println("1. Добавить книгу");
            System.out.println("2. Удалить книгу");
            System.out.println("3. Просмотреть все книги");
            System.out.println("4. Просмотреть пользователей");
            System.out.println("5. Изменить книгу");
            System.out.println("0. Выйти");
            System.out.print(">> ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 5:
                    try {
                        List<Book> books = bookService.listBooks();
                        books.forEach(book -> System.out.println(book.toString()));

                        System.out.print("Введите ID книги для изменения: ");
                        Long updateId = Long.parseLong(scanner.nextLine());

                        Optional<Book> optionalBook = books.stream()
                                .filter(b -> b.getId().equals(updateId))
                                .findFirst();

                        if (optionalBook.isEmpty()) {
                            System.out.println("Книга с таким ID не найдена.");
                            break;
                        }

                        //if (optionalBook.get().isReserved()) {
                        //    System.out.println("Книга находится на руках и не может быть изменена.");
                        //    break;
                        //}

                        //List<Issue> issues = issueService.getAllByBookId(updateId);
                        //for (Issue issue : issues) {
                        //    issueService.deleteIssue(issue.getId());
                        //}

                        System.out.println("Введите название книги:");
                        String title = scanner.nextLine();
                        System.out.println("Введите автора книги:");
                        String author = scanner.nextLine();

                        bookService.updateBookById(updateId, author, title);

                        System.out.println("Книга успешно изменена");

                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат ID.");
                    } catch (Exception e) {
                        System.out.println("Ошибка при изменении книги: " + e.getMessage());
                    }

                    break;
                case 1:
                    System.out.println("Введите название книги:");
                    String title = scanner.nextLine();
                    System.out.println("Введите автора книги:");
                    String author = scanner.nextLine();
                    Book newBook = new Book(title, author, false);
                    bookService.addBook(newBook);
                    System.out.println("Книга добавлена!");
                    break;
                case 2:
                    try {
                        List<Book> books = bookService.listBooks();
                        books.forEach(book -> System.out.println(book.toString()));

                        System.out.print("Введите ID книги для удаления: ");
                        Long deleteId = Long.parseLong(scanner.nextLine());

                        Optional<Book> optionalBook = books.stream()
                                .filter(b -> b.getId().equals(deleteId))
                                .findFirst();

                        if (optionalBook.isEmpty()) {
                            System.out.println("Книга с таким ID не найдена.");
                            break;
                        }

                        if (optionalBook.get().isReserved()) {
                            System.out.println("Книга находится на руках и не может быть удалена.");
                            break;
                        }

                        List<Issue> issues = issueService.getAllByBookId(deleteId);
                        for (Issue issue : issues) {
                            issueService.deleteIssue(issue.getId());
                        }

                        bookService.deleteBook(deleteId);


                        System.out.println("Книга успешно удалена.");
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный формат ID.");
                    } catch (Exception e) {
                        System.out.println("Ошибка при удалении книги: " + e.getMessage());
                    }
                    break;
                case 3:
                    bookService.listBooks().forEach(book -> System.out.println(book.toString()));
                    break;
                case 4:
                    userService.listUsers().forEach(user -> System.out.println("Имя пользователя: " + user.getNickname()));
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    break;
            }
        }

    }

    private static void userMenu(Scanner scanner, User currentUser) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Меню пользователя:");
            System.out.println("1. Просмотреть книги");
            System.out.println("2. Взять книгу");
            System.out.println("3. Вернуть книгу");
            System.out.println("4. Изменить данные");
            System.out.println("5. Удалить аккаунт");
            System.out.println("0. Выйти");
            System.out.print(">> ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    bookService.listBooks().forEach(book -> System.out.println(book.toString()));
                    break;
                case 2:
                    // Получаем список книг, которые еще не зарезервированы
                    List<Book> availableBooks = bookService.getAllBookNotReserved();

                    if (availableBooks.isEmpty()) {
                        System.out.println("Нет доступных книг для бронирования.");
                        break;
                    }

                    // Показываем доступные книги
                    System.out.println("Доступные книги для бронирования:");
                    availableBooks.forEach(System.out::println);

                    System.out.print("Введите ID книги, которую хотите взять: ");
                    while (true) {
                        try {
                            Long bookId = Long.parseLong(scanner.nextLine());

                            // Проверяем, есть ли такая книга в списке
                            Optional<Book> optionalBook = availableBooks.stream()
                                    .filter(book -> book.getId().equals(bookId))
                                    .findFirst();

                            if (optionalBook.isEmpty()) {
                                System.out.print("Книга с таким ID недоступна. Повторите ввод: ");
                                continue;
                            }

                            Book selectedBook = optionalBook.get();

                            // Создаем запись о бронировании (issue)
                            Issue issue = new Issue();
                            issue.setBook(selectedBook);
                            issue.setUser(currentUser);
                            issue.setIssueDate(LocalDate.now());
                            issue.setReturnDate(null); // Книга пока не возвращена

                            // Сохраняем запись и обновляем статус книги
                            issueService.issueBook(issue);
                            selectedBook.setReserved(true);
                            bookService.updateBook(selectedBook);

                            System.out.println("Книга успешно забронирована!");
                            break;

                        } catch (NumberFormatException e) {
                            System.out.print("Неверный формат ID. Введите число: ");
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка: " + e.getMessage());
                            break;
                        }
                    }
                    break;
                case 3:
                    // Получаем активные брони текущего пользователя
                    List<Issue> activeIssues = issueService.getActiveIssuesByUser(currentUser.getId());

                    if (activeIssues.isEmpty()) {
                        System.out.println("У вас нет книг, которые нужно вернуть.");
                        break;
                    }

                    System.out.println("Ваши книги, находящиеся на руках:");
                    for (Issue issue : activeIssues) {
                        System.out.println("ID: " + issue.getBook().getId() + " | Название: " + issue.getBook().getTitle());
                    }

                    System.out.print("Введите ID книги, которую хотите вернуть: ");
                    while (true) {
                        try {
                            Long returnBookId = Long.parseLong(scanner.nextLine());

                            Optional<Issue> optionalIssue = activeIssues.stream()
                                    .filter(issue -> issue.getBook().getId().equals(returnBookId))
                                    .findFirst();

                            if (optionalIssue.isEmpty()) {
                                System.out.print("Книга с таким ID не найдена среди ваших бронирований. Повторите ввод: ");
                                continue;
                            }

                            Issue issueToClose = optionalIssue.get();
                            Book bookToReturn = issueToClose.getBook();

                            // Обновляем флаг книги и дату возврата
                            bookToReturn.setReserved(false);
                            issueToClose.setReturnDate(LocalDate.now());

                            bookService.updateBook(bookToReturn);
                            issueService.issueBook(issueToClose); // перезаписываем issue c returnDate

                            System.out.println("Книга успешно возвращена!");
                            break;

                        } catch (NumberFormatException e) {
                            System.out.print("Неверный формат ID. Введите число: ");
                        } catch (Exception e) {
                            System.out.println("Ошибка при возврате книги: " + e.getMessage());
                            break;
                        }
                    }
                    break;
                case 4:
                    System.out.print("Введите новое имя: ");
                    currentUser.setFirstName(scanner.nextLine());

                    System.out.print("Введите новую фамилию: ");
                    currentUser.setLastName(scanner.nextLine());

                    System.out.print("Введите новое отчество: ");
                    currentUser.setMiddleName(scanner.nextLine());

                    System.out.print("Введите новый ник: ");
                    currentUser.setNickname(scanner.nextLine());

                    userService.updateUser(currentUser);
                    break;
                case 5:

                    issueService.deleteIssuesByUserId(currentUser.getId());

                    userService.deleteUser(currentUser.getId());

                    System.out.println("Аккаунт и связанные брони успешно удалены.");
                    exit = true;
                    break;
                case 0:
                    exit = true;
                default:
                    break;
            }
        }

    }
}
