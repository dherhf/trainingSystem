
import java.util.ArrayList;
import java.util.List;

public class TrainingSystem {
    private List<Program> programs; // 系统中的所有 Program

    // 构造方法
    public TrainingSystem() {
        this.programs = new ArrayList<>();
    }

    // Getter 和 Setter 方法
    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    // 添加 Program
    public void addProgram(Program program) {
        this.programs.add(program);
    }


    // 静态嵌套类 Program
    public static class Program {
        private int programId;
        private String programName;
        private List<Student> students;


        // 构造方法
        public Program(int programId,String programName) {
            this.programId = programId;
            this.programName = programName;
            this.students = new ArrayList<>();
        }

        // Getter 和 Setter 方法
        public int getProgramId() {
            return programId;
        }

        public void setProgramId(int programId) {
            this.programId = programId;
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public List<Student> getStudent() {
            return students;
        }

        public void setStudent(List<Student> student) {
            this.students = student;
        }

        // 添加 Student
        public void addStudent(Student student) {
            this.students.add(student);
        }
        // 静态嵌套类 Student
        public static class Student {
            private int studentId;//学生ID
            private String studentName;//学生名字
            private String registrationDate;//入学日期
            private int tuition;//学费
            private int grades;//成绩

            // 构造方法
            public Student(int studentId, String studentName, String registrationDate, int tuition, int grades) {
                this.studentId = studentId;
                this.studentName = studentName;
                this.registrationDate = registrationDate;
                this.tuition = tuition;
                this.grades = grades;
            }

            // Getter 和 Setter 方法
            public int getStudentId() {
                return studentId;
            }

            public void setStudentId(int studentId) {
                this.studentId = studentId;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public String getRegistrationDate() {
                return registrationDate;
            }

            public void setRegistrationDate(String registrationDate) {
                this.registrationDate = registrationDate;
            }

            public int getTuition() {
                return tuition;
            }

            public void setTuition(int tuition) {
                this.tuition = tuition;
            }

            public int getGrades() {
                return grades;
            }

            public void setGrades(int grades) {
                this.grades = grades;
            }
        }
    }


}
