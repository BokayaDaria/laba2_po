import java.util.ArrayList;
import java.util.List;

public class Course {
    private String title;
    private int id;
    private List<Student> students;
    private Teacher teacher;

    public Course(String title, int id) {
        this.title = title;
        this.id = id;
        this.students = new ArrayList<>(); // Инициализация!
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
            student.addCourse(this); // Двусторонняя связь!
        }
    }

    public void removeStudent(Student student) {
        if (student != null && students.remove(student)) {
            student.removeCourse(this);
        }
    }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}