import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class StudentGridUI {

    public static JPanel showStudentGridUI(int programID) {

        // 初始化学生数据
        TrainingSystem trainingSystem = JsonReaderGson.getTrainingSystem();
        TrainingSystem.Program program = trainingSystem.getPrograms().get(programID);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // 搜索框
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("微软雅黑", Font.BOLD, 18));
        JButton searchButton = new JButton("搜索");
        searchButton.setPreferredSize(new Dimension(75, 30));
        searchButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        searchButton.setBackground(Color.BLUE);
        searchButton.setForeground(Color.WHITE);

        // 搜索框高度固定
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // 获取表格及模型
        DefaultTableModel tableModel = createTableModel(program.getStudent());
        JTable studentTable = createStudentTable(tableModel, program);

        //
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(studentTable.getModel());
        studentTable.setRowSorter(sorter);

        // 同步表格修改至原始数据
        addTableModelListener(tableModel, program);

        // 搜索功能：按钮点击和回车触发
        Runnable searchAction = () -> {
            String keyword = searchField.getText().trim();
            List<TrainingSystem.Program.Student> filteredStudents = program.getStudent().stream()
                    .filter(student -> student.getStudentName().contains(keyword) ||
                            String.valueOf(student.getStudentId()).contains(keyword))
                    .collect(Collectors.toList());
            updateTableModel(tableModel, filteredStudents);
        };

        searchButton.addActionListener(e -> searchAction.run());
        searchField.addActionListener(e -> searchAction.run()); // 回车触发搜索

        JPanel tablePanel = getJPanel(program, tableModel, studentTable);

        mainPanel.add(tablePanel, BorderLayout.CENTER);


        return mainPanel;
    }

    private static JPanel getJPanel(TrainingSystem.Program program, DefaultTableModel tableModel, JTable studentTable) {
        // 添加学生按钮
        JButton addButton = new JButton("添加学生");
        addButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        addButton.setBackground(Color.WHITE);
        addButton.setForeground(Color.BLACK);
        addButton.addActionListener(e -> {
            // 创建新学生对象并添加到数据列表
            TrainingSystem.Program.Student newStudent = new TrainingSystem.Program.Student(
                    program.getProgramId() * 100 + program.getStudent().size() + 1, // 自动生成ID
                    "新学生",
                    "2024-01-01",
                    0,
                    0
            );
            program.addStudent(newStudent);

            // 添加到表格模型
            tableModel.addRow(new Object[]{
                    newStudent.getStudentId(),
                    newStudent.getStudentName(),
                    newStudent.getRegistrationDate(),
                    newStudent.getTuition(),
                    newStudent.getGrades(),
                    "删除"
            });
        });

        // 创建底部面板
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(addButton, BorderLayout.CENTER);


        // 将表格和按钮加入到面板中
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(studentTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private static DefaultTableModel createTableModel(List<TrainingSystem.Program.Student> students) {
        String[] columnNames = {"ID", "姓名", "报名日期", "缴费金额", "成绩", " "};
        Object[][] data = students.stream()
                .map(student -> new Object[]{
                        student.getStudentId(),
                        student.getStudentName(),
                        student.getRegistrationDate(),
                        student.getTuition(),
                        student.getGrades(),
                        "删除"
                }).toArray(Object[][]::new);
        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // 禁止编辑 ID 列
            }

        };
    }

    private static JTable createStudentTable(DefaultTableModel tableModel, TrainingSystem.Program program) {
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("微软雅黑", Font.BOLD, 18));

        // 设置表头字体
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 18));

        // 自定义渲染器为按钮
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        // 自定义编辑器为按钮并添加点击事件
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), program));
        // 自定义单元格编辑器，确保输入框字体一致
        table.setDefaultEditor(Object.class, new CustomCellEditor());

        table.getColumnModel().getColumn(5).setPreferredWidth(75); // 最后一列宽度缩小
        table.getColumnModel().getColumn(5).setMaxWidth(75); // 限制最大宽度
        table.getColumnModel().getColumn(5).setMinWidth(75); // 限制最小宽度
        return table;
    }

    private static void addTableModelListener(DefaultTableModel tableModel, TrainingSystem.Program program) {
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column >= 0 && row >= 0) {
                Object newValue = tableModel.getValueAt(row, column);
                TrainingSystem.Program.Student student = program.getStudent().get(row);

                // 更新 program 的学生数据
                switch (column) {
                    case 1: // 姓名
                        student.setStudentName(newValue.toString());
                        break;
                    case 2: // 报名日期
                        student.setRegistrationDate(newValue.toString());
                        break;
                    case 3: // 缴费金额
                        student.setTuition(Integer.parseInt(newValue.toString()));
                        break;
                    case 4: // 成绩
                        student.setGrades(Integer.parseInt(newValue.toString()));
                        break;
                }
            }
            //保存数据
            JsonReaderGson.setTrainingSystem(JsonReaderGson.getTrainingSystem());
        });
    }

    private static void updateTableModel(DefaultTableModel tableModel, List<TrainingSystem.Program.Student> students) {
        tableModel.setRowCount(0); // 清空表格数据
        for (TrainingSystem.Program.Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getStudentId(),
                    student.getStudentName(),
                    student.getRegistrationDate(),
                    student.getTuition(),
                    student.getGrades(),
                    "删除"
            });
        }
    }

    // 自定义单元格编辑器，设置字体
    private static class CustomCellEditor extends DefaultCellEditor {

        public CustomCellEditor() {
            super(new JTextField()); // 使用 JTextField 作为编辑组件
            JTextField textField = (JTextField) getComponent();
            textField.setFont(new Font("微软雅黑", Font.BOLD, 18)); // 设置字体
        }
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("微软雅黑", Font.BOLD, 18));// 设置按钮字体
            setBackground(new Color(220, 53, 69));
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }


    static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private boolean isPushed;
        private final TrainingSystem.Program program;

        public ButtonEditor(JCheckBox checkBox, TrainingSystem.Program program) {
            super(checkBox);
            this.program = program;
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("微软雅黑", Font.BOLD, 18)); // 设置按钮字体
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null ? "" : value.toString());
            button.setText(label);
            isPushed = true;

            // 移除现有的监听器
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }
            // 添加新的事件监听器
            button.addActionListener(event -> {
                int studentId = (int) table.getModel().getValueAt(row, 0); // 获取学生ID

                program.getStudent().removeIf(student -> student.getStudentId() == studentId);// 从学生列表中删除该学生

                // 从表格中删除该行
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                tableModel.removeRow(row);
            });

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

    }
}