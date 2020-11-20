import javax.management.JMException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class StudentFrame extends JFrame {

    public static void main(String[] args) {
        ArrayList<Student> mstudents = new ArrayList<Student>();
        ArrayList<Lesson> mlessons = new ArrayList<Lesson>();
        ArrayList<Enroll> menrolls = new ArrayList<Enroll>();

        JFrame frame = new JFrame("Student Frame");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        JLabel outputLabel = new JLabel();
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
               try {
                   BufferedWriter StudentsWriter;
                   StudentsWriter = Files.newBufferedWriter(Paths.get("src/students.txt"));
                   for ( Student s : mstudents) {
                       StudentsWriter.write(s.toCsvLine());
                       StudentsWriter.newLine();
                   }
                   StudentsWriter.close();

                   BufferedWriter LessonsWriter;
                   LessonsWriter = Files.newBufferedWriter(Paths.get("src/lessons.txt"));
                   for ( Lesson l : mlessons) {
                       LessonsWriter.write(l.toCsvLine());
                       LessonsWriter.newLine();
                   }
                   LessonsWriter.close();

                   BufferedWriter EnrollsWriter;
                   EnrollsWriter = Files.newBufferedWriter(Paths.get("src/enrolls.txt"));
                   for ( Enroll e : menrolls) {
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
                String studentDisplay;

                JTextField registryIdField = new JTextField(10);

                JPanel newShowStudentPanel = new JPanel();

                newShowStudentPanel.add(new JLabel("STUDENT"));
                newShowStudentPanel.add(registryIdField);

                int result = JOptionPane.showConfirmDialog(null, newShowStudentPanel, "ENROLL LESSON", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int student = Integer.parseInt(registryIdField.getText());
                    for (Student s : mstudents) {
                        if(s.getRegistryId() == student){
                            studentDisplay = s.toString();
                            double gradeSum = 0.0;
                            int enrolledLessonCount = 0;
                            for (Enroll enroll : menrolls) {
                                if(enroll.getStudent() == student){
                                    enrolledLessonCount ++;
                                    gradeSum += enroll.getGrade();
                                }
                            }
                            studentDisplay += "<br>" + "AVG= " +gradeSum / enrolledLessonCount;
                            outputLabel.setText("<html>"+studentDisplay+"</html>");
                        }
                    }


                }
            }
        });
        studentMenu.add(menuItemShowStudent);

        JMenuItem menuItemDeleteStudent = new JMenuItem("DELETE STUDENT");
        menuItemDeleteStudent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "";
                JTextField registryIdField = new JTextField(10);

                JPanel newDeleteStudentPanel = new JPanel();

                newDeleteStudentPanel.add(new JLabel("STUDENT"));
                newDeleteStudentPanel.add(registryIdField);

                int result = JOptionPane.showConfirmDialog(null, newDeleteStudentPanel, "DELETE STUDENT", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    int student = Integer.parseInt(registryIdField.getText());
                    int studentIteratorIndex = 0;
                    int studentToRemove = -1;
                    ArrayList enrollsToRemove = new ArrayList();

                    for (Student s : mstudents) {
                        if (s.getRegistryId() == student) {
                            studentToRemove = studentIteratorIndex;
                            for (Iterator<Enroll> iterator = menrolls.iterator(); iterator.hasNext();) {
                                Enroll enrollToRemove = iterator.next();
                                if(enrollToRemove.getStudent() == student) {
                                    iterator.remove();
                                }
                            }
                            message = "Student " + student + " deleted";
                            outputLabel.setText("<html>" + message + "</html>");
                        }
                        studentIteratorIndex++;
                    }
                    if(studentToRemove != -1){
                        mstudents.remove(studentToRemove);
                    }

                }
            }
        });
        studentMenu.add(menuItemDeleteStudent);

        JMenuItem menuItemNewLesson = new JMenuItem("NEW LESSON");
        menuItemNewLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                String lessonDisplay = "";

                JTextField idField = new JTextField(10);

                JPanel newShowLessonPanel = new JPanel();

                newShowLessonPanel.add(new JLabel("LESSON"));
                newShowLessonPanel.add(idField);

                int result = JOptionPane.showConfirmDialog(null, newShowLessonPanel, "SHOW LESSON", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    int lesson = Integer.parseInt(idField.getText());
                    for (Lesson l : mlessons) {
                        if (l.getId() == lesson) {
                            lessonDisplay = l.toString();
                            for (Enroll enroll : menrolls) {
                                if(enroll.getLesson() == lesson){
                                    int studentRegistryId = enroll.getStudent();
                                    for (Student s : mstudents) {
                                        if (s.getRegistryId() == studentRegistryId){
                                            lessonDisplay += "<br>"+s.getRegistryId()+
                                                    ", "+s.getName()+
                                                    ", "+s.getSemester()+
                                                    ", "+enroll.getGrade();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    outputLabel.setText("<html>"+lessonDisplay+"</html>");
                }
            }
        });
        lessonMenu.add(menuItemShowEnrolls);

        JMenuItem menuItemSelectSemester = new JMenuItem("SELECT SEMESTER");
        menuItemSelectSemester.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String semesterDisplay = "";

                JTextField semesterField = new JTextField(10);

                JPanel newShowSemesterPanel = new JPanel();

                newShowSemesterPanel.add(new JLabel("SEMESTER"));
                newShowSemesterPanel.add(semesterField);

                int result = JOptionPane.showConfirmDialog(null, newShowSemesterPanel, "SHOW SEMESTER", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int semester = Integer.parseInt(semesterField.getText());
                    for (Lesson l : mlessons) {
                        if(l.getSemester() == semester){
                            semesterDisplay += "<br>"+l.toString();
                        }
                    }

                }
                outputLabel.setText("<html>"+semesterDisplay+"</html>");
            }
        });
        lessonMenu.add(menuItemSelectSemester);

        JMenuItem menuItemDeleteLesson = new JMenuItem("DELETE LESSON");
        menuItemDeleteLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "";
                JTextField idField = new JTextField(10);

                JPanel newDeleteStudentPanel = new JPanel();

                newDeleteStudentPanel.add(new JLabel("LESSON"));
                newDeleteStudentPanel.add(idField);

                int result = JOptionPane.showConfirmDialog(null, newDeleteStudentPanel, "DELETE LESSON", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int lesson = Integer.parseInt(idField.getText());
                    int lessonIteratorIndex = 0;
                    int lessonToRemove = -1;

                    for (Lesson s : mlessons) {
                        if (s.getId() == lesson) {
                            lessonToRemove = lessonIteratorIndex;
                            for (Iterator<Enroll> iterator = menrolls.iterator(); iterator.hasNext(); ) {
                                Enroll enrollToRemove = iterator.next();
                                if (enrollToRemove.getLesson() == lesson) {
                                    iterator.remove();
                                }
                            }
                            message = "Lesson " + lesson + " deleted";
                            outputLabel.setText("<html>" + message + "</html>");
                        }
                        lessonIteratorIndex++;
                    }
                    if (lessonToRemove != -1) {
                        mlessons.remove(lessonToRemove);
                    }
                }
            }
        });
        lessonMenu.add(menuItemDeleteLesson);

        frame.getContentPane().add(BorderLayout.NORTH, menuBar);

        outputLabel.setText("");
        JPanel mainFramePanel = new JPanel();
        mainFramePanel.add(outputLabel);
        frame.add(mainFramePanel);

        frame.setVisible(true);
    }
}