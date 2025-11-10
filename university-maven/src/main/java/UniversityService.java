import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class UniversityService {
    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();

    public UniversityService() {
        loadData();
    }

    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
        }
    }

    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
        }
    }

    public List<Student> findStudentsByCourse(Course course) {
        List<Student> result = new ArrayList<>();
        if (course != null && course.getStudents() != null) {
            result.addAll(course.getStudents()); // Прямая связь!
        }
        return result;
    }

    public List<Course> filterCoursesByStudentCount(int minStudents) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (c != null && c.getStudents() != null && c.getStudents().size() >= minStudents) {
                result.add(c);
            }
        }
        result.sort((c1, c2) -> Integer.compare(c1.getStudents().size(), c2.getStudents().size()));
        return result;
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses); // Защита от модификации
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void saveData() {
        try (PrintWriter writer = new PrintWriter("data.txt")) {
            writer.println("# Курсы и студенты");
            for (Course c : courses) {
                writer.println("COURSE," + c.getId() + "," + c.getTitle());
                for (Student s : c.getStudents()) {
                    writer.println("STUDENT," + s.getName() + "," + s.getId() + "," + c.getId());
                }
            }
            // Студенты без курсов
            for (Student s : students) {
                if (s.getCourses().isEmpty()) {
                    writer.println("STUDENT," + s.getName() + "," + s.getId() + ",0");
                }
            }
            System.out.println("Данные сохранены в data.txt");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File("data.txt");
        if (!file.exists()) {
            Course math = new Course("Math", 1);
            addCourse(math);
            saveData();
            return;
        }

        List<Course> tempCourses = new ArrayList<>();
        List<Student> tempStudents = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split(",", 4);
                if (parts.length < 2) continue;

                if (parts[0].equals("COURSE")) {
                    int id = Integer.parseInt(parts[1]);
                    String title = parts[2];
                    Course course = new Course(title, id);
                    tempCourses.add(course);
                } else if (parts[0].equals("STUDENT") && parts.length >= 4) {
                    String name = parts[1];
                    int id = Integer.parseInt(parts[2]);
                    int courseId = Integer.parseInt(parts[3]);

                    Student student = new Student(name, id);
                    tempStudents.add(student);

                    if (courseId != 0) {
                        Course course = tempCourses.stream()
                                .filter(c -> c.getId() == courseId)
                                .findFirst().orElse(null);
                        if (course != null) {
                            course.addStudent(student); // Двусторонняя связь!
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }

        courses.addAll(tempCourses);
        students.addAll(tempStudents);
    }
}