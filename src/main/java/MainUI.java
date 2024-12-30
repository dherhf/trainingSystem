import javax.swing.*; // 引入 Swing 库用于创建用户界面
import java.awt.*; // 引入 AWT 库用于界面布局和组件
import java.awt.event.*; // 引入事件监听器相关的库
import java.util.List; // 引入 List 用于处理集合数据

public class MainUI {

    private static final Font TAB_FONT = new Font("微软雅黑", Font.BOLD, 18); // 定义选项卡字体样式

    public static void showMainUI() {
        JFrame mainFrame = new JFrame("主界面"); // 创建主界面窗口
        mainFrame.setSize(1024, 768); // 设置窗口大小
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置关闭操作
        mainFrame.setLayout(new BorderLayout()); // 设置布局为 BorderLayout

        JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT); // 创建选项卡组件
        pane.setFont(TAB_FONT); // 设置选项卡字体
        pane.setBounds(0, 0, 500, 300); // 设置选项卡大小和位置

        TrainingSystem trainingSystem = JsonReaderGson.getTrainingSystem(); // 获取训练系统数据

        // 初始化选项卡
        initializeTabs(pane, trainingSystem);

        mainFrame.add(pane); // 将选项卡添加到主窗口
        mainFrame.setLocationRelativeTo(null); // 设置窗口居中显示
        mainFrame.setVisible(true); // 显示窗口
    }

    private static void initializeTabs(JTabbedPane pane, TrainingSystem trainingSystem) {
        List<TrainingSystem.Program> programs = trainingSystem.getPrograms(); // 获取所有训练项目
        for (TrainingSystem.Program program : programs) {
            addTab(pane, program.getProgramName()); // 为每个训练项目添加选项卡
        }
        addAddTabButton(pane); // 添加“新增”选项卡按钮
    }

    private static JPanel addTab(JTabbedPane tabbedPane, String title) {
        JPanel titlePanel = createTabTitlePanel(tabbedPane, title); // 创建选项卡标题面板
        JPanel contentPanel = StudentGridUI.showStudentGridUI(tabbedPane.getTabCount()); // 获取选项卡内容面板

        tabbedPane.addTab(title, contentPanel); // 添加新的选项卡和内容面板
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, titlePanel); // 设置自定义标题

        return titlePanel; // 返回标题面板
    }

    private static JPanel createTabTitlePanel(JTabbedPane tabbedPane, String title) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // 创建标题面板并设置布局
        titlePanel.setOpaque(false); // 设置标题面板背景透明

        JLabel titleLabel = new JLabel(title); // 创建标题标签
        titleLabel.setFont(TAB_FONT); // 设置标签字体
        titlePanel.add(titleLabel); // 将标签添加到标题面板

        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tabbedPane.indexOfTabComponent(titlePanel); // 获取当前选项卡索引
                if (index != -1) {
                    if (e.getClickCount() == 1) {
                        tabbedPane.setSelectedIndex(index); // 单击选择选项卡
                    } else if (e.getClickCount() == 2) {
                        enterEditMode(tabbedPane, titlePanel, titleLabel, index); // 双击进入编辑模式
                    }
                }
            }
        });

        return titlePanel; // 返回标题面板
    }

    private static void addAddTabButton(JTabbedPane tabbedPane) {
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 创建“新增”选项卡面板
        addPanel.setOpaque(false); // 设置面板背景透明

        JLabel addLabel = new JLabel("＋"); // 创建“新增”标签
        addLabel.setFont(TAB_FONT); // 设置标签字体
        addLabel.setToolTipText("添加新项目"); // 设置提示文字

        addLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleAddNewTab(tabbedPane); // 双击添加新选项卡
                }
            }
        });

        addPanel.add(addLabel); // 将“新增”标签添加到面板
        tabbedPane.addTab("", null); // 添加一个空选项卡
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, addPanel); // 设置面板为标题
    }

    private static void handleAddNewTab(JTabbedPane tabbedPane) {
        String defaultTitle = "新项目"; // 定义默认标题
        tabbedPane.removeTabAt(tabbedPane.getTabCount() - 1); // 移除“新增”选项卡

        TrainingSystem trainingSystem = JsonReaderGson.getTrainingSystem(); // 获取训练系统数据
        trainingSystem.addProgram(new TrainingSystem.Program(tabbedPane.getTabCount() + 1, "")); // 添加新项目到训练系统

        JPanel newTitlePanel = addTab(tabbedPane, defaultTitle); // 创建新选项卡
        JLabel newTitleLabel = (JLabel) newTitlePanel.getComponent(0); // 获取新选项卡标题标签
        enterEditMode(tabbedPane, newTitlePanel, newTitleLabel, tabbedPane.getTabCount() - 1); // 进入编辑模式
        addAddTabButton(tabbedPane); // 添加“新增”按钮
    }

    private static void enterEditMode(JTabbedPane tabbedPane, JPanel titlePanel, JLabel titleLabel, int selectedIndex) {
        JTextField textField = new JTextField(titleLabel.getText()); // 创建文本框用于编辑标题
        textField.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 设置文本框字体
        Dimension labelSize = titleLabel.getPreferredSize(); // 获取标签尺寸
        textField.setPreferredSize(new Dimension(labelSize.width + 5, labelSize.height)); // 设置文本框尺寸

        titlePanel.remove(titleLabel); // 移除原有标签
        titlePanel.add(textField, 0); // 添加文本框
        titlePanel.revalidate(); // 刷新面板布局
        titlePanel.repaint(); // 重绘面板

        textField.addActionListener(e -> handleEditConfirm(tabbedPane, titlePanel, titleLabel, textField, selectedIndex)); // 添加确认编辑事件

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!isFocusInsideTab(tabbedPane)) {
                    cancelEdit(titlePanel, titleLabel, textField); // 失去焦点取消编辑
                }
            }
        });

        textField.requestFocus(); // 聚焦到文本框
        textField.selectAll(); // 全选文本框内容
    }

    private static void handleEditConfirm(JTabbedPane tabbedPane, JPanel titlePanel, JLabel titleLabel, JTextField textField, int selectedIndex) {
        String newTitle = textField.getText().trim(); // 获取文本框输入的标题
        if (!newTitle.isEmpty()) {
            updateProgramName(selectedIndex, newTitle); // 更新训练系统中的项目名称
            updateTabTitle(tabbedPane, titlePanel, titleLabel, newTitle); // 更新选项卡标题
        } else {
            cancelEdit(titlePanel, titleLabel, textField); // 输入为空时取消编辑
        }
    }

    private static void updateProgramName(int index, String newName) {
        TrainingSystem trainingSystem = JsonReaderGson.getTrainingSystem(); // 获取训练系统数据
        trainingSystem.getPrograms().get(index).setProgramName(newName); // 更新指定项目的名称
        JsonReaderGson.setTrainingSystem(trainingSystem); // 保存更新后的训练系统数据
    }

    private static void updateTabTitle(JTabbedPane tabbedPane, JPanel titlePanel, JLabel titleLabel, String newTitle) {
        titleLabel.setText(newTitle); // 更新标题标签文字
        titlePanel.remove(0); // 移除旧组件
        titlePanel.add(titleLabel, 0); // 添加新标签
        titlePanel.revalidate(); // 刷新面板布局
        titlePanel.repaint(); // 重绘面板

        int index = tabbedPane.indexOfTabComponent(titlePanel); // 获取选项卡索引
        if (index != -1) {
            tabbedPane.setTitleAt(index, newTitle); // 更新选项卡标题
        }
    }

    private static void cancelEdit(JPanel titlePanel, JLabel titleLabel, JTextField textField) {
        titlePanel.remove(textField); // 移除文本框
        titlePanel.add(titleLabel, 0); // 还原标签
        titlePanel.revalidate(); // 刷新面板布局
        titlePanel.repaint(); // 重绘面板
    }

    private static boolean isFocusInsideTab(JTabbedPane tabbedPane) {
        Component focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner(); // 获取当前焦点组件
        return focusedComponent != null && SwingUtilities.isDescendingFrom(focusedComponent, tabbedPane); // 判断焦点是否在选项卡内
    }
}