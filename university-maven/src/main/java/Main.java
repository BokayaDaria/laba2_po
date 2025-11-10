import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UniversityService service = new UniversityService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Учёт студентов ===");
            System.out.println("1. Поиск студентов по курсу (введите ID курса)");
            System.out.println("2. Фильтрация курсов по числу студентов (>=)");
            System.out.println("3. Добавить студента");
            System.out.println("4. Добавить курс");
            System.out.println("5. Выйти");
            System.out.print("Выберите действие (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера

            if (choice == 1) {
                System.out.print("Введите ID курса: ");
                int id = scanner.nextInt();
                Course course = service.getCourses().stream()
                        .filter(c -> c.getId() == id)
                        .findFirst()
                        .orElse(null);
                if (course != null) {
                    List<Student> found = service.findStudentsByCourse(course);
                    System.out.println("Студенты на курсе " + course.getTitle() + " (ID: " + course.getId() + "):");
                    if (found.isEmpty()) {
                        System.out.println("Нет студентов.");
                    } else {
                        for (Student s : found) {
                            System.out.println("- " + s.getName() + " (ID: " + s.getId() + ")");
                        }
                    }
                } else {
                    System.out.println("Курс с ID " + id + " не найден.");
                }
            } else if (choice == 2) {
                System.out.print("Введите минимальное число студентов: ");
                int min = scanner.nextInt();
                List<Course> filtered = service.filterCoursesByStudentCount(min);
                System.out.println("Курсы с >= " + min + " студентами (отсортировано по возрастанию числа студентов):");
                if (filtered.isEmpty()) {
                    System.out.println("Нет курсов, соответствующих условию.");
                } else {
                    for (Course c : filtered) {
                        System.out.println("- " + c.getTitle() + " (ID: " + c.getId() + ", студентов: " + c.getStudents().size() + ")");
                    }
                }
            } else if (choice == 3) {
                System.out.print("Введите имя студента: ");
                String name = scanner.nextLine().trim();
                System.out.print("Введите ID студента: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Введите ID курса для добавления (или 0, если не привязывать): ");
                int courseId = scanner.nextInt();
                Student student = new Student(name, id);
                if (courseId != 0) {
                    Course course = service.getCourses().stream()
                            .filter(c -> c.getId() == courseId)
                            .findFirst()
                            .orElse(null);
                    if (course != null) {
                        student.addCourse(course);
                        course.addStudent(student);
                        service.addStudent(student);
                        System.out.println("Студент " + name + " добавлен к курсу " + course.getTitle() + ".");
                    } else {
                        System.out.println("Курс с ID " + courseId + " не найден. Студент добавлен без курса.");
                        service.addStudent(student);
                    }
                } else {
                    service.addStudent(student);
                    System.out.println("Студент " + name + " добавлен (без курса).");
                }
                service.saveData();
            } else if (choice == 4) {
                System.out.print("Введите название курса: ");
                String title = scanner.nextLine().trim();
                System.out.print("Введите ID курса: ");
                int id = scanner.nextInt();
                if (service.getCourses().stream().anyMatch(c -> c.getId() == id)) {
                    System.out.println("Курс с ID " + id + " уже существует. Используйте другой ID.");
                } else {
                    Course course = new Course(title, id);
                    service.addCourse(course);
                    service.saveData();
                    System.out.println("Курс " + title + " добавлен.");
                }
            } else if (choice == 5) {
                service.saveData();
                System.out.println("Программа завершена.");
                break;
            } else {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        scanner.close();
    }
}