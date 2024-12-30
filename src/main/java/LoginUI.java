import javax.swing.*; // 导入Swing框架，用于创建用户界面
import java.awt.*; // 导入AWT框架，用于布局管理和图形处理

public class LoginUI { // 定义主类
    public static void main(String[] args) { // 主方法
        new LoginUI().showLoginUI(); // 创建LoginUI实例并显示登录界面
    }

    public void showLoginUI() { // 显示登录界面的方法
        JFrame loginFrame = new JFrame("登录界面"); // 创建主窗口
        loginFrame.setSize(525, 300); // 设置窗口大小
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置关闭操作
        loginFrame.setLayout(new BorderLayout()); // 设置布局为BorderLayout

        JLabel titleLabel = new JLabel("欢迎登录", JLabel.CENTER); // 创建标题标签并居中
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28)); // 设置字体
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // 设置边距
        loginFrame.add(titleLabel, BorderLayout.NORTH); // 将标题添加到窗口顶部

        JPanel panel = new JPanel(new GridBagLayout()); // 创建主面板并使用GridBagLayout布局
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // 设置面板边距
        GridBagConstraints gbc = new GridBagConstraints(); // 创建布局约束对象
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件间距
        gbc.fill = GridBagConstraints.HORIZONTAL; // 设置填充方式为水平填充

        JLabel userLabel = new JLabel("用户名:"); // 创建用户名标签
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18)); // 设置字体
        gbc.gridx = 0; // 设置组件位置列索引为0
        gbc.gridy = 0; // 设置组件位置行索引为0
        gbc.weightx = 0; // 设置水平权重为0，不伸展
        panel.add(userLabel, gbc); // 添加用户名标签到面板

        JTextField userText = new JTextField(); // 创建用户名输入框
        userText.setFont(new Font("微软雅黑", Font.PLAIN, 18)); // 设置字体
        gbc.gridx = 1; // 设置组件位置列索引为1
        gbc.weightx = 1; // 设置水平权重为1，允许伸展
        panel.add(userText, gbc); // 添加用户名输入框到面板

        JLabel passwordLabel = new JLabel("密码:"); // 创建密码标签
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18)); // 设置字体
        gbc.gridx = 0; // 设置组件位置列索引为0
        gbc.gridy = 1; // 设置组件位置行索引为1
        gbc.weightx = 0; // 设置水平权重为0，不伸展
        panel.add(passwordLabel, gbc); // 添加密码标签到面板

        JPasswordField passwordText = new JPasswordField(); // 创建密码输入框
        passwordText.setFont(new Font("微软雅黑", Font.PLAIN, 18)); // 设置字体
        gbc.gridx = 1; // 设置组件位置列索引为1
        gbc.weightx = 1; // 设置水平权重为1，允许伸展
        panel.add(passwordText, gbc); // 添加密码输入框到面板

        loginFrame.add(panel, BorderLayout.CENTER); // 将主面板添加到窗口中央

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15)); // 创建按钮面板并设置流式布局
        JButton loginButton = new JButton("登录"); // 创建登录按钮
        JButton cancelButton = new JButton("取消"); // 创建取消按钮

        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 18)); // 设置登录按钮字体
        cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 18)); // 设置取消按钮字体

        loginButton.setPreferredSize(new Dimension(120, 40)); // 设置登录按钮大小
        cancelButton.setPreferredSize(new Dimension(120, 40)); // 设置取消按钮大小

        loginButton.setBackground(new Color(0, 123, 255)); // 设置登录按钮背景颜色
        loginButton.setForeground(Color.WHITE); // 设置登录按钮前景颜色
        cancelButton.setBackground(new Color(220, 53, 69)); // 设置取消按钮背景颜色
        cancelButton.setForeground(Color.WHITE); // 设置取消按钮前景颜色

        loginButton.setFocusPainted(false); // 设置登录按钮不绘制焦点
        cancelButton.setFocusPainted(false); // 设置取消按钮不绘制焦点

        loginButton.addActionListener(e -> { // 添加登录按钮点击事件
            String username = userText.getText().trim(); // 获取用户名输入
            String password = new String(passwordText.getPassword()); // 获取密码输入
            if (username.isEmpty()) { // 检查用户名是否为空
                showErrorTooltip(userText, "请填写用户名！"); // 显示错误提示
            } else if (password.isEmpty()) { // 检查密码是否为空
                showErrorTooltip(passwordText, "请填写密码！"); // 显示错误提示
            } else if ("admin".equals(username) && "123456".equals(password)) { // 检查用户名和密码是否匹配
                loginFrame.dispose(); // 关闭登录窗口
                MainUI.showMainUI(); // 显示主界面
            } else { // 用户名或密码错误
                showErrorTooltip(userText, "用户名或密码错误！"); // 显示错误提示
            }
        });

        cancelButton.addActionListener(e -> { // 添加取消按钮点击事件
            userText.setText(""); // 清空用户名输入框
            passwordText.setText(""); // 清空密码输入框
        });

        buttonPanel.add(loginButton); // 添加登录按钮到按钮面板
        buttonPanel.add(cancelButton); // 添加取消按钮到按钮面板
        loginFrame.add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板添加到窗口底部

        loginFrame.setLocationRelativeTo(null); // 设置窗口居中显示
        loginFrame.setVisible(true); // 设置窗口可见
    }

    private void showErrorTooltip(JComponent component, String message) { // 显示错误提示的方法
        JToolTip toolTip = new JToolTip(); // 创建提示工具
        toolTip.setFont(new Font("微软雅黑", Font.BOLD, 18)); // 设置提示字体
        toolTip.setTipText(message); // 设置提示信息

        Point location = component.getLocationOnScreen(); // 获取组件屏幕位置
        int x = location.x + component.getWidth() / 3; // 计算提示框的X坐标
        int y = location.y + component.getHeight() + 5; // 计算提示框的Y坐标

        Popup popup = PopupFactory.getSharedInstance().getPopup(component, toolTip, x, y); // 创建弹出框
        popup.show(); // 显示弹出框

        Timer timer = new Timer(2000, e -> popup.hide()); // 创建定时器，用于自动关闭弹出框
        timer.setRepeats(false); // 设置定时器只执行一次
        timer.start(); // 启动定时器
    }
}