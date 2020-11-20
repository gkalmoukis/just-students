import javax.management.JMException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class App extends JFrame {

    public static void main(String[] args) {
        ArrayList<Student> mstudents = new ArrayList<Student>();
        ArrayList<Lesson> mlessons = new ArrayList<Lesson>();
        ArrayList<Enroll> menrolls = new ArrayList<Enroll>();

        JFrame frame = new JFrame("Student Frame");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("FILE");
        JMenu studentMenu = new JMenu("STUDENT");
        JMenu lessonMenu = new JMenu("LESSON");

        menuBar.add(fileMenu);
        menuBar.add(studentMenu);
        menuBar.add(lessonMenu);

        JMenuItem menuItemLoad = new JMenuItem("LOAD");
        menuItemLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.out.print("Load data action");
                String studentsFile = "src/students.txt";
                String lessonsFile = "src/lessons.txt";
                String enrollsFile = "src/enrolls.txt";

                BufferedReader br = null;
                String line = "";
                String splitBy = ",";

                try {
                    br = new BufferedReader(new FileReader(studentsFile));

                    while ((line = br.readLine()) != null) {
                        String[] student = line.split(splitBy);

                        int id = Integer.parseInt(student[0]);
                        String name = student[1];
                        int semester = Integer.parseInt(student[2]);

                        mstudents.add(new Student(id, name, semester));
                    }

                    br = new BufferedReader(new FileReader(lessonsFile));
                    while ((line = br.readLine()) != null) {
                        String[] lesson = line.split(splitBy);
                        int id = Integer.parseInt(lesson[0]);
                        String title = lesson[1];
                        int semester = Integer.parseInt(lesson[2]);

                        mlessons.add(new Lesson(id, title, semester));
                    }

                    br = new BufferedReader(new FileReader(enrollsFile));
                    while ((line = br.readLine()) != null) {
                        String[] enroll = line.split(splitBy);

                        int student = Integer.parseInt(enroll[0]);
                        int lesson = Integer.parseInt(enroll[1]);
                        double grade = Double.parseDouble(enroll[2]);

                        menrolls.add(new Enroll(student, lesson, grade));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            for (Lesson l : mlessons)
                            {
                                System.out.println(l.toString());
                            }
                            for (Student s : mstudents)
                            {
                                System.out.println(s.toString());
                            }
                            for (Enroll e : menrolls)
                            {
                                System.out.println(e.toString());
                            }
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        fileMenu.add(menuItemLoad);

        JMenuItem menuItemSave = new JMenuItem("SAVE");
        menuItemSave.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ev) {
               System.out.print("Save data");
               try {
                   BufferedWriter StudentsWriter;
                   StudentsWriter = Files.newBufferedWriter(Paths.get("src/students.txt"));
                   for ( Student s : mstudents) {
                       System.out.print(s.toCsvLine());
                       StudentsWriter.write(s.toCsvLine());
                       StudentsWriter.newLine();
                   }
                   StudentsWriter.close();

                   BufferedWriter LessonsWriter;
                   LessonsWriter = Files.newBufferedWriter(Paths.get("src/lessons.txt"));
                   for ( Lesson l : mlessons) {
                       System.out.print(l.toCsvLine());
                       LessonsWriter.write(l.toCsvLine());
                       LessonsWriter.newLine();
                   }
                   LessonsWriter.close();

                   BufferedWriter EnrollsWriter;
                   EnrollsWriter = Files.newBufferedWriter(Paths.get("src/enrolls.txt"));
                   for ( Enroll e : menrolls) {
                       System.out.print(e.toCsvLine());
                       EnrollsWriter.write(e.toCsvLine());
                       EnrollsWriter.newLine();
                   }
                   EnrollsWriter.close();

               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           }
        });
        fileMenu.add(menuItemSave);

        JMenuItem menuItemExit = new JMenuItem("EXIT");
        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });
        fileMenu.add(menuItemExit);

        JMenuItem menuItemNewStudent = new JMenuItem("NEW STUDENT");
        menuItemNewStudent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("New student");
                JTextField idField = new JTextField(10);
                JTextField nameField = new JTextField(10);
                JTextField semesterField = new JTextField(10);

                JPanel newStudentPanel = new JPanel();

                newStudentPanel.add(new JLabel("REGISTRY ID"));
                newStudentPanel.add(idField);

                newStudentPanel.add(new JLabel("NAME"));
                newStudentPanel.add(nameField);

                newStudentPanel.add(new JLabel("SEMESTER"));
                newStudentPanel.add(semesterField);

                int result = JOptionPane.showConfirmDialog(null, newStudentPanel,
                        "Add New Student", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int id = Integer.parseInt(idField.getText());
                    String name =  nameField.getText();
                    int semester = Integer.parseInt(semesterField.getText());

                    Student newStudent = new Student(id,name,semester);
                    mstudents.add(newStudent);
                }
            }
        });
        studentMenu.add(menuItemNewStudent);

        JMenuItem menuItemEnrollLesson = new JMenuItem("ENROLL LESSON");
        menuItemEnrollLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("Enroll lesson action");
                JComboBox studentComboBox = new JComboBox();
                JComboBox lessonComboBox = new JComboBox();
                JTextField gradeField = new JTextField(10);

                for (Student s : mstudents) {
                    studentComboBox.addItem(s.getName());
                }
                for (Lesson l : mlessons) {
                    lessonComboBox.addItem(l.getTitle());
                }

                JPanel newEnrollPanel = new JPanel();

                newEnrollPanel.add(new JLabel("STUDENT"));
                newEnrollPanel.add(studentComboBox);

                newEnrollPanel.add(new JLabel("LESSON"));
                newEnrollPanel.add(lessonComboBox);

                newEnrollPanel.add(new JLabel("GRADE"));
                newEnrollPanel.add(gradeField);

                int result = JOptionPane.showConfirmDialog(null, newEnrollPanel, "ENROLL LESSON", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int student = mstudents.get(studentComboBox.getSelectedIndex()).getRegistryId();
                    int lesson = mlessons.get(lessonComboBox.getSelectedIndex()).getId();
                    double grade = Double.parseDouble(gradeField.getText());

                    Enroll newEnroll = new Enroll(student,lesson,grade);
                    menrolls.add(newEnroll);
                }
            }
        });
        studentMenu.add(menuItemEnrollLesson);

        JMenuItem menuItemShowStudent = new JMenuItem("SHOW STUDENT");
        menuItemShowStudent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("Show Student action");
            }
        });
        studentMenu.add(menuItemShowStudent);

        JMenuItem menuItemDeleteStudent = new JMenuItem("DELETE STUDENT");
        menuItemDeleteStudent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("Delete Student action");

            }
        });
        studentMenu.add(menuItemDeleteStudent);

        JMenuItem menuItemNewLesson = new JMenuItem("NEW LESSON");
        menuItemNewLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("New lesson action");
                JTextField idField = new JTextField(10);
                JTextField titleField = new JTextField(10);
                JTextField semesterField = new JTextField(10);

                JPanel newLessonPanel = new JPanel();

                newLessonPanel.add(new JLabel("ID"));
                newLessonPanel.add(idField);

                newLessonPanel.add(new JLabel("TITLE"));
                newLessonPanel.add(titleField);

                newLessonPanel.add(new JLabel("SEMESTER"));
                newLessonPanel.add(semesterField);

                int result = JOptionPane.showConfirmDialog(null, newLessonPanel,
                        "Add New Student", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int id = Integer.parseInt(idField.getText());
                    String title =  titleField.getText();
                    int semester = Integer.parseInt(semesterField.getText());

                    Lesson newLesson = new Lesson(id,title,semester);
                    mlessons.add(newLesson);
                }
            }
        });
        lessonMenu.add(menuItemNewLesson);

        JMenuItem menuItemShowEnrolls = new JMenuItem("SHOW ENROLLS");
        menuItemShowEnrolls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("show enrolls action");

            }
        });
        lessonMenu.add(menuItemShowEnrolls);

        JMenuItem menuItemSelectSemester = new JMenuItem("SELECT SEMESTER");
        menuItemSelectSemester.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("show enrolls action");

            }
        });
        lessonMenu.add(menuItemSelectSemester);

        JMenuItem menuItemDeleteLesson = new JMenuItem("DELETE LESSON");
        menuItemDeleteLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.print("delete lesson action");

            }
        });
        lessonMenu.add(menuItemDeleteLesson);

        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.setVisible(true);
    }
}