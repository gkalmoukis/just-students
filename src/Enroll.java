public class Enroll {
    private int student,lesson;
    private double grade;

    public Enroll() { }

    public Enroll(int student, int lesson, double grade) {
        this.student = student;
        this.lesson = lesson;
        this.grade = grade;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Enroll{" +
                "student=" + student +
                ", lesson=" + lesson +
                ", grade=" + grade +
                '}';
    }

}
