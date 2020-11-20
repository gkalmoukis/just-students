public class Lesson {
    private int id;
    private String title;
    private int semester;

    public Lesson() { }

    public Lesson(int id, String title, int semester){
        this.id = id;
        this.title = title;
        this.semester = semester;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", semester=" + semester +
                '}';
    }

    public String toCsvLine() {
        return id+","+title+","+semester;
    }
}
