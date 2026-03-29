package Login;

import Usuario.Crear_usuario;
import Inicio.Inicio;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.*;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    private final Color COLOR_PRIMARIO = Color.decode("#2699FB");
    private final Color COLOR_FONDO = new Color(245, 245, 245);

    private static final String RUTA = "lib/usuarios.csv";

    public Login() {
        setTitle("");
        setSize(350, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setIconImage(new ImageIcon(new byte[0]).getImage());

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_FONDO);

        // 🔹 LOGO
        ImageIcon icono = new ImageIcon("lib/Imagenes/Logo.jpg");
        Image imagen = icono.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imagen));
        lblLogo.setBounds(105, 30, 140, 140);
        panel.add(lblLogo);

        // 🔹 Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setBounds(50, 200, 250, 15);
        lblUsuario.setForeground(Color.GRAY);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtUsuario.setOpaque(false);
        panel.add(new PanelRedondeado(txtUsuario, COLOR_PRIMARIO)).setBounds(50, 220, 250, 45);

        // 🔹 Contraseña
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setBounds(50, 280, 250, 15);
        lblContrasena.setForeground(Color.GRAY);
        panel.add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtContrasena.setOpaque(false);
        panel.add(new PanelRedondeado(txtContrasena, COLOR_PRIMARIO)).setBounds(50, 300, 250, 45);

        // 🔹 BOTÓN LOGIN
        JButton btnIngresar = new JButton("Iniciar sesión");
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(new BotonRedondeado(btnIngresar, COLOR_PRIMARIO)).setBounds(50, 370, 250, 50);

        btnIngresar.addActionListener(e -> validarLogin());

        // 🔹 Crear usuario
        JLabel lblCrearUsuario = new JLabel("Crear usuario");
        lblCrearUsuario.setBounds(50, 435, 250, 20);
        lblCrearUsuario.setForeground(COLOR_PRIMARIO);
        lblCrearUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(lblCrearUsuario);

        lblCrearUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new Crear_usuario();
                dispose();
            }

            public void mouseEntered(java.awt.event.MouseEvent e) {
                lblCrearUsuario.setText("<html><u>Crear usuario</u></html>");
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                lblCrearUsuario.setText("Crear usuario");
            }
        });

        add(panel);
    }

    private void validarLogin() {

        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña");
            return;
        }

        try {
            File archivo = new File(RUTA);

            if (!archivo.exists()) {
                JOptionPane.showMessageDialog(this, "No hay usuarios registrados");
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length >= 4) {
                    String nombre = datos[0];
                    String apellido = datos[1];
                    String user = datos[2];
                    String pass = datos[3];

                    if (user.equals(usuario)) {

                        if (pass.equals(contrasena)) {

                            JOptionPane.showMessageDialog(this, "Bienvenido " + nombre + " " + apellido);

                            // 🔥 Enviar nombre completo al Inicio
                            new Inicio(nombre + " " + apellido);

                            dispose();
                            br.close();
                            return;

                        } else {
                            JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
                            br.close();
                            return;
                        }
                    }
                }
            }

            br.close();
            JOptionPane.showMessageDialog(this, "Usuario no encontrado");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer usuarios");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}

/* 🔹 Panel redondeado */
class PanelRedondeado extends JPanel {

    private final Color colorBorde;

    public PanelRedondeado(JComponent comp, Color colorBorde) {
        this.colorBorde = colorBorde;
        setLayout(new BorderLayout());
        setOpaque(false);
        comp.setOpaque(false);
        add(comp);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
        g2.setColor(colorBorde);
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 50, 50);
    }
}

/* 🔹 Botón redondeado */
class BotonRedondeado extends JPanel {

    private final JButton boton;
    private Color colorActual;
    private final Color colorNormal;
    private final Color colorHover;

    public BotonRedondeado(JButton boton, Color base) {
        this.boton = boton;
        this.colorNormal = base;
        this.colorHover = base.darker();
        this.colorActual = colorNormal;

        setLayout(new BorderLayout());
        setOpaque(false);

        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                colorActual = colorHover;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                colorActual = colorNormal;
                repaint();
            }
        });

        add(boton);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(colorActual);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);
    }
}