import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class UI extends JFrame {

    private static JsonObject objectMain;
    private static String status = "";

    private JLabel day, day1, day2, day3, day4, day5, day6;
    private JLabel label, label_1, label_2, label_3, label_4, label_5, label_6;
    private JMenu reload;

    static UI frame = null;

    private static String cityPinyin = "";
    private static String IP = "";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                frame = new UI();
                System.out.println("实例化UI成功！");
                // The following code ensure the window shown on the center of screen.
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation((dim.width - frame.getWidth()) / 2, (dim.height - frame.getHeight()) / 2);
                frame.setVisible(true);// 展示UI
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (getIpAddress()) {// 获取公网IP
                if (getAddressByIP(IP)) {// 查询地址
                    weather();// 查询天气数据

                    frame.setTitle(updateTitle());// 改变标题

                    load();// 绘制UI
                    System.out.println("结束！");
                } else {
                    frame.setTitle("失败：无法获取城市信息");// 改变标题
                }
            } else {
                frame.setTitle("失败：无法获取本地IP");// 改变标题
            }
        });
    }

    private static String updateTitle() {
        JsonObject basicObj = objectMain.get("basic").getAsJsonObject();// 解析数据
        String city = basicObj.get("city").getAsString();
        String update = basicObj.get("update").getAsJsonObject().get("loc").getAsString();
        String lat = basicObj.get("lat").getAsString();
        String lon = basicObj.get("lon").getAsString();

        return "IP：" + IP + " / 城市：" + city + " / 上次更新：" + update + " / 经度：" + lat + " / 纬度：" + lon;
    }

    public static boolean getIpAddress() {
        try {
            URL url = new URL("https://sapi.k780.com/?app=ip.local&format=json");
            System.out.println("公网IP查询接口:" + url);
            try {
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "GB2312");
                BufferedReader bReader = new BufferedReader(isr);

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bReader.readLine()) != null) {
                    builder.append(line);
                }
                bReader.close();
                isr.close();
                is.close();

                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(builder.toString());
                if (object.get("success").getAsString().equals("1")) {
                    IP = object.get("result").getAsJsonObject().get("ip").getAsString();
                    System.out.println(IP);

                    return true;
                } else {
                    System.out.println("获取IP失败");

                    return false;
                }
            } catch (IOException e1) {
                e1.printStackTrace();

                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

            return false;
        }
    }

    public static void load() {
        for (int i = 0; i < 7; i++) {
            JsonObject dailyForecastObj = objectMain.get("daily_forecast").getAsJsonArray().get(i).getAsJsonObject();

            String code_d = dailyForecastObj.get("cond").getAsJsonObject().get("code_d").getAsString();
            String txt_d = dailyForecastObj.get("cond").getAsJsonObject().get("txt_d").getAsString();

            String imgUrl = "<html><img src='https://www.heweather.com/files/images/cond_icon/" + code_d + ".png' /><html>";

            String tmpL, tmpH;
            tmpL = dailyForecastObj.get("tmp").getAsJsonObject().get("min").getAsString() + "~";
            tmpH = dailyForecastObj.get("tmp").getAsJsonObject().get("max").getAsString() + "℃";

            if (i == 0) {
                frame.day.setText(imgUrl);
                frame.label.setText(txt_d + " / " + tmpL + tmpH);
            } else if (i == 1) {
                frame.day1.setText(imgUrl);
                frame.label_1.setText(txt_d + " / " + tmpL + tmpH);
            } else if (i == 2) {
                frame.day2.setText(imgUrl);
                frame.label_2.setText(txt_d + " / " + tmpL + tmpH);
            } else if (i == 3) {
                frame.day3.setText(imgUrl);
                frame.label_3.setText(txt_d + " / " + tmpL + tmpH);
            } else if (i == 4) {
                frame.day4.setText(imgUrl);
                frame.label_4.setText(txt_d + " / " + tmpL + tmpH);
            } else if (i == 5) {
                frame.day5.setText(imgUrl);
                frame.label_5.setText(txt_d + " / " + tmpL + tmpH);
            } else if (i == 6) {
                frame.day6.setText(imgUrl);
                frame.label_6.setText(txt_d + " / " + tmpL + tmpH);
            }
        }
    }

    public static boolean getAddressByIP(String strIP) {
        try {
            URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + strIP);
            System.out.println("城市查询接口:" + url);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bReader = new BufferedReader(isr);

            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bReader.readLine()) != null) {
                builder.append(line);
            }
            bReader.close();
            isr.close();
            is.close();

            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(builder.toString());
            if (object.get("code").getAsString().equals("0")) {
                String city = object.get("data").getAsJsonObject().get("city").getAsString();

                cityPinyin = ChineseToEnglish.getPingYin(city);
                System.out.println("城市：" + cityPinyin);

                return true;
            } else {
                System.out.println("获取城市失败！");

                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    public static void weather() {
        try {
            URL url = new URL("https://free-api.heweather.com/v5/forecast?city=" + cityPinyin
                    + "&key=e45fd4f711884af694433fa04227f533");
            System.out.println("天气接口:" + url);
            try {
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader bReader = new BufferedReader(isr);

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bReader.readLine()) != null) {
                    builder.append(line);
                }
                bReader.close();
                isr.close();
                is.close();

                JsonParser parser = new JsonParser();
                JsonObject object = (JsonObject) parser.parse(builder.toString());

                objectMain = object.get("HeWeather5").getAsJsonArray().get(0).getAsJsonObject();

                status = objectMain.get("status").getAsString();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public UI() {
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(UI.class.getResource("/com/sun/java/swing/plaf/motif/icons/DesktopIcon.gif")));
        setFont(new Font("微软雅黑 Light", Font.ITALIC, 14));
        setTitle("\u5929\u6C14\u9884\u62A5\uFF08\u6B63\u5728\u52A0\u8F7D\u6570\u636E...\uFF09");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 195);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("\u9009\u9879");
        menu.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
        menuBar.add(menu);

        JMenuItem about = new JMenuItem("\u5173\u4E8E");
        about.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
        menu.add(about);
        about.addActionListener(e -> JOptionPane.showMessageDialog(UI.this,
                "<html>本程序调用【和风天气API】提供7天内天气预报功能。<br><br>代码地址：https://github.com/lifankohome/weather<html>",
                "关于此程序：", JOptionPane.PLAIN_MESSAGE));

        JMenuItem exit = new JMenuItem("\u9000\u51FA");
        exit.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
        menu.add(exit);
        exit.addActionListener(e -> System.exit(0));

        reload = new JMenu("\u5237\u65B0");
        reload.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread(() -> {
                    status = "reload";
                    reload.setText("刷新中");

                    if (getIpAddress()) {// 获取公网IP
                        if (getAddressByIP(IP)) {// 查询地址
                            weather();
                            while (status.equals("reload")) {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            frame.setTitle(updateTitle());// 改变标题
                            load();
                            reload.setText("已刷新");
                        }
                    } else {
                        reload.setText("刷新失败");
                        frame.setTitle("失败：无法获取本地IP");
                    }
                }).start();
            }
        });
        reload.setFont(new Font("微软雅黑 Light", Font.PLAIN, 12));
        menuBar.add(reload);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel = new JPanel();
        contentPane.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        day = new JLabel("New label");
        panel.add(day);
        day.setHorizontalAlignment(SwingConstants.CENTER);

        day.setText("<html><img src='http://files.heweather.com/cond_icon/999.png' /><html>");

        label = new JLabel("- - -");
        label.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.SOUTH);

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        day1 = new JLabel("<html><img src='http://files.heweather.com/cond_icon/999.png' /><html>");
        day1.setIcon(null);
        panel_1.add(day1);

        label_1 = new JLabel("- - -");
        label_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        panel_1.add(label_1, BorderLayout.SOUTH);

        JPanel panel_2 = new JPanel();
        contentPane.add(panel_2);
        panel_2.setLayout(new BorderLayout(0, 0));

        day2 = new JLabel("<html><img src='http://files.heweather.com/cond_icon/999.png' /><html>");
        panel_2.add(day2);

        label_2 = new JLabel("- - -");
        label_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        panel_2.add(label_2, BorderLayout.SOUTH);

        JPanel panel_3 = new JPanel();
        contentPane.add(panel_3);
        panel_3.setLayout(new BorderLayout(0, 0));

        day3 = new JLabel("<html><img src='http://files.heweather.com/cond_icon/999.png' /><html>");
        panel_3.add(day3);

        label_3 = new JLabel("- - -");
        label_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        panel_3.add(label_3, BorderLayout.SOUTH);

        JPanel panel_4 = new JPanel();
        contentPane.add(panel_4);
        panel_4.setLayout(new BorderLayout(0, 0));

        day4 = new JLabel("<html><img src='http://files.heweather.com/cond_icon/999.png' /><html>");
        panel_4.add(day4);

        label_4 = new JLabel("- - -");
        label_4.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        panel_4.add(label_4, BorderLayout.SOUTH);

        JPanel panel_5 = new JPanel();
        contentPane.add(panel_5);
        panel_5.setLayout(new BorderLayout(0, 0));

        day5 = new JLabel("<html><img src='http://files.heweather.com/cond_icon/999.png' /><html>");
        panel_5.add(day5);

        label_5 = new JLabel("- - -");
        label_5.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        panel_5.add(label_5, BorderLayout.SOUTH);

        JPanel panel_6 = new JPanel();
        contentPane.add(panel_6);
        panel_6.setLayout(new BorderLayout(0, 0));

        day6 = new JLabel("<html><img src='http://files.heweather.com/cond_icon/999.png' /><html>");
        panel_6.add(day6);

        label_6 = new JLabel("- - -");
        label_6.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        panel_6.add(label_6, BorderLayout.SOUTH);
    }
}
