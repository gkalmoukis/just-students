class Student{
    private int id;
    private String name;
    private int semester;

    public Student() { }

    public Student(int id, String name, int semester){
        this.id = id;
        this.name = name;
        this.semester = semester;
    }

    public int getRegistryId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSemester() {
        return semester;
    }

    public void setRegistryId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", semester=" + semester +
                '}';
    }
}