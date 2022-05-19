package com.dc.view;

import com.dc.aes.AESUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESView extends JDialog {
    private JPanel contentPane;
    private JButton 密钥管理Button;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JTextField textField1;
    private JTextField textField2;
    private JButton 加密文件Button;
    private JTextArea textArea2;
    private JButton 加密数据Button;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JTextField textField3;
    private JTextField textField4;
    private JTextArea 传输数据textArea;
    private JButton 解密数据Button;
    private JButton 解密文件Button;
    private JButton 接收Button;
    private JButton 发送Button;
    private JTextField 对方IP;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JTextField textField5;
    private JTextArea textArea5;
    private JLabel 控制台IP;
    private JButton 接收文件Button;
    private JTextField textField6;
    private JButton 发送文件Button1;
    private boolean receiveState = false;

    private static String ip;
    private static String message;

    private static RecieveThread rt;
    ;

    public AESView() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(密钥管理Button);

        InetAddress host = null;
        try {
            host = InetAddress.getLocalHost();
            String hostAddress = host.getHostAddress();
            ip = hostAddress;
            控制台IP.setText("本机IP:" + hostAddress);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        密钥管理Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        加密数据Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    //                位数
                    int selectedItem = Integer.parseInt((String) comboBox3.getSelectedItem());
                    //                填充
                    Object selectedItem1 = comboBox2.getSelectedItem();
                    //                模式
                    Object selectedItem2 = comboBox1.getSelectedItem();
                    //                合成
                    String type = "AES/" + selectedItem2 + "/" + selectedItem1;
                    //                    偏移量
                    byte[] bytes = "1234567812345678".getBytes("UTF-8");
                    //              密钥
                    String text = textField5.getText();

                    //              明文
                    String text1 = textArea1.getText();
                    if (selectedItem2.equals("ECB") || selectedItem2.equals("CFB")) {
                        bytes = null;
                    }
                    long startTime = System.nanoTime();
                    byte[] bytes1 = AESUtil.encryptOrdecrypt(true, text1.getBytes("UTF-8"), text, bytes, selectedItem, type);
                    long endTime = System.nanoTime(); //获取结束时间
                    String s = Base64.getEncoder().encodeToString(bytes1);
                    //                设置密文
                    textArea2.setText(s);
                    textArea5.setText(String.valueOf((endTime - startTime)));
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    JOptionPane.showMessageDialog(null, "此模式不能选择NOthing填充", "填充错误", JOptionPane.ERROR_MESSAGE);

                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    badPaddingException.printStackTrace();
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                }


            }
        });
        发送Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //                位数
                    int selectedItem = Integer.parseInt((String) comboBox3.getSelectedItem());
                    //                填充
                    Object selectedItem1 = comboBox2.getSelectedItem();
                    //                模式
                    Object selectedItem2 = comboBox1.getSelectedItem();
                    //                合成
                    String type = "AES/" + selectedItem2 + "/" + selectedItem1;
                    //                    偏移量
                    byte[] bytes = "1234567812345678".getBytes("UTF-8");
                    //              密钥
                    String text = textField5.getText();
                    //              明文
                    String text1 = 传输数据textArea.getText();
                    if (selectedItem2.equals("ECB") || selectedItem2.equals("CFB")) {
                        bytes = null;
                    }
                    long startTime = System.nanoTime();
                    byte[] bytes1 = AESUtil.encryptOrdecrypt(true, text1.getBytes("UTF-8"), text, bytes, selectedItem, type);
                    long endTime = System.nanoTime(); //获取结束时间
                    String s = Base64.getEncoder().encodeToString(bytes1);
                    //向局域网发送密文
                    if (!对方IP.getText().equals("")) {
                        ip = 对方IP.getText();
                    }
                    message = s;
                    SendThread st = new SendThread();
                    st.start();
                    textArea5.setText(String.valueOf((endTime - startTime)));
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    JOptionPane.showMessageDialog(null, "此模式不能选择NOthing填充", "填充错误", JOptionPane.ERROR_MESSAGE);
                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    badPaddingException.printStackTrace();
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                }
            }
        });
        接收Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                接收Button.setText("正在监听");
                接收Button.setEnabled(false);
                发送Button.setEnabled(false);
                rt = new RecieveThread();
                rt.start();
            }
        });
        加密文件Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileInputStream fileInputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    //                位数
                    int selectedItem = Integer.parseInt((String) comboBox3.getSelectedItem());

                    //                填充
                    Object selectedItem1 = comboBox2.getSelectedItem();
                    //                模式
                    Object selectedItem2 = comboBox1.getSelectedItem();
                    //                合成
                    String type = "AES/" + selectedItem2 + "/" + selectedItem1;
                    //                    偏移量
                    byte[] bytes = "1234567812345678".getBytes("UTF-8");
                    if (selectedItem2.equals("ECB") || selectedItem2.equals("CFB")) {
                        bytes = null;
                    }
                    //              密钥
                    String text = textField5.getText();

                    fileInputStream = new FileInputStream(new File(textField1.getText()));
                    fileOutputStream = new FileOutputStream(new File(textField2.getText()));
                    byte[] bytes1 = new byte[1024];

                    int read = 0;
                    String context = "";
                    long startTime = System.nanoTime();
                    while ((read = fileInputStream.read(bytes1)) > 0) {
                        String s = new String(bytes1, 0, read);
                        context = context + s;
                    }
                    byte[] bytes2 = AESUtil.encryptOrdecrypt(true, context.getBytes(), text, bytes, selectedItem, type);
                    byte[] encode = Base64.getEncoder().encode(bytes2);
                    fileOutputStream.write(encode);
                    long endTime = System.nanoTime(); //获取结束时间
                    textArea5.setText(String.valueOf((endTime - startTime)));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    JOptionPane.showMessageDialog(null, "此模式不能选择NOthing填充", "填充错误", JOptionPane.ERROR_MESSAGE);

                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    badPaddingException.printStackTrace();
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                } finally {
                    try {
                        fileInputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }


            }
        });
        解密数据Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //                位数
                    int selectedItem = Integer.parseInt((String) comboBox3.getSelectedItem());
                    //                填充
                    Object selectedItem1 = comboBox2.getSelectedItem();
                    //                模式
                    Object selectedItem2 = comboBox1.getSelectedItem();
                    //                合成
                    String type = "AES/" + selectedItem2 + "/" + selectedItem1;
                    //                    偏移量
                    byte[] bytes = "1234567812345678".getBytes("UTF-8");
                    //              密钥
                    String text = textField5.getText();

                    //              明文
                    byte[] text1 = Base64.getDecoder().decode(textArea3.getText());
                    if (selectedItem2.equals("ECB") || selectedItem2.equals("CFB")) {
                        bytes = null;
                    }
                    long startTime = System.nanoTime();

                    byte[] bytes1 = AESUtil.encryptOrdecrypt(false, text1, text, bytes, selectedItem, type);
                    long endTime = System.nanoTime(); //获取结束时间
                    String s = new String(bytes1);
                    textArea4.setText(s);
                    textArea5.setText(String.valueOf((endTime - startTime)));
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    illegalBlockSizeException.printStackTrace();
                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    JOptionPane.showMessageDialog(null, "密钥可能错误", "密钥错误", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                }

            }
        });
        密钥管理Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordEn dialog = new PasswordEn();
                dialog.pack();
                dialog.setVisible(true);

            }
        });
        解密文件Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                FileInputStream fileInputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    //                位数
                    int selectedItem = Integer.parseInt((String) comboBox3.getSelectedItem());

                    //                填充
                    Object selectedItem1 = comboBox2.getSelectedItem();
                    //                模式
                    Object selectedItem2 = comboBox1.getSelectedItem();
                    //                合成
                    String type = "AES/" + selectedItem2 + "/" + selectedItem1;
                    //                    偏移量
                    byte[] bytes = "1234567812345678".getBytes("UTF-8");
                    if (selectedItem2.equals("ECB") || selectedItem2.equals("CFB")) {
                        bytes = null;
                    }
                    //              密钥
                    String text = textField5.getText();

                    fileInputStream = new FileInputStream(new File(textField3.getText()));
                    fileOutputStream = new FileOutputStream(new File(textField4.getText()));
                    byte[] bytes1 = new byte[1024];
                    int read = 0;
                    String context = "";
                    long startTime = System.nanoTime();
                    while ((read = fileInputStream.read(bytes1)) > 0) {

                        String s = new String(bytes1, 0, read);
                        context = context + s;
                    }
                    byte[] decode = Base64.getDecoder().decode(context);
                    byte[] bytes2 = AESUtil.encryptOrdecrypt(false, decode, text, bytes, selectedItem, type);
                    fileOutputStream.write(bytes2);
                    long endTime = System.nanoTime(); //获取结束时间
                    textArea5.setText(String.valueOf((endTime - startTime)));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    JOptionPane.showMessageDialog(null, "此模式不能选择NOthing填充", "填充错误", JOptionPane.ERROR_MESSAGE);
                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    badPaddingException.printStackTrace();
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                } finally {
                    try {
                        fileInputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

            }
        });
    }

    class SendThread extends Thread {
        public void run() {
            try {
                Socket socket = null;
                try {
                    socket = new Socket(ip, 10010);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "对方IP未开启监听", "传输错误", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(e);
                }
                // 向服务端程序发送数据
                OutputStream ops = null;
                try {
                    ops = socket.getOutputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                OutputStreamWriter opsw = new OutputStreamWriter(ops);
                BufferedWriter bw = new BufferedWriter(opsw);
                try {

                    bw.write(message + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    bw.flush();
                    System.out.println(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class RecieveThread extends Thread {
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(10010);
                传输数据textArea.setText("监听端口等待数据......");
                Socket s = ss.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                //读取客户端发送来的消息
                String mess = br.readLine();
                System.out.println(mess);
                try {
                    //                位数
                    int selectedItem = Integer.parseInt((String) comboBox3.getSelectedItem());
                    //                填充
                    Object selectedItem1 = comboBox2.getSelectedItem();
                    //                模式
                    Object selectedItem2 = comboBox1.getSelectedItem();
                    //                合成
                    String type = "AES/" + selectedItem2 + "/" + selectedItem1;
                    //                    偏移量
                    byte[] bytes = "1234567812345678".getBytes("UTF-8");
                    //              密钥
                    String text = textField5.getText();
                    if (selectedItem2.equals("ECB") || selectedItem2.equals("CFB")) {
                        bytes = null;
                    }
                    byte[] text1 = Base64.getDecoder().decode(mess);
                    byte[] bytes1 = AESUtil.encryptOrdecrypt(false, text1, text, bytes, selectedItem, type);
                    String str = new String(bytes1);
                    传输数据textArea.setText(str);

                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    illegalBlockSizeException.printStackTrace();
                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    传输数据textArea.setText("密钥可能错误");
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                }
                br.close();
                s.close();
                ss.close();
                接收Button.setText("从网络解密接收");
                接收Button.setEnabled(true);
                发送Button.setEnabled(true);
            } catch (IOException e) {
                传输数据textArea.setText("监听进程出错");
                throw new RuntimeException(e);
            }
        }
    }

    private void onOK() {
        System.out.println("暂无功能");
        dispose();
    }

    private void onCancel() {
        System.out.println("退出");
        dispose();
    }

    public static void main(String[] args) {
        AESView dialog = new AESView();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
