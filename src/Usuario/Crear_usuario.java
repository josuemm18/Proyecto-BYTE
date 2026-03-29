package Usuario;

import Login.Login;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.util.HashMap;

public class Crear_usuario extends JFrame {

    private JTextField txtNombre, txtApellido, txtUsuario;
    private JPasswordField txtContrasena, txtConfirmar;

    private final Color COLOR_PRIMARIO = Color.decode("#2699FB");
    private final Color COLOR_FONDO = new Color(245, 245, 245);

    private static HashMap<String, String> usuarios = new HashMap<>();
    private static final String RUTA = "lib/usuarios.csv";

    public Crear_usuario() {
        setSize(350, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarUsuarios();
        initComponents();
        setVisible(true);
    }

    private void initComponents() {

        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_FONDO);

        // 🔹 LOGO
        ImageIcon icono = new ImageIcon("lib/Imagenes/LogoUsuarios.png");
        Image imagen = icono.getImage().getScaledInstance(180, 80, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(imagen));
        lblLogo.setBounds(85, 30, 180, 80);
        panel.add(lblLogo);

        // 🔹 Nombre
        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(50, 120, 250, 15);
        lblNombre.setForeground(Color.GRAY);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtNombre.setOpaque(false);
        panel.add(new PanelRedondeado(txtNombre, COLOR_PRIMARIO)).setBounds(50, 140, 250, 45);

        // 🔹 Apellidos
        JLabel lblApellido = new JLabel("Apellidos");
        lblApellido.setBounds(50, 200, 250, 15);
        lblApellido.setForeground(Color.GRAY);
        panel.add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtApellido.setOpaque(false);
        panel.add(new PanelRedondeado(txtApellido, COLOR_PRIMARIO)).setBounds(50, 220, 250, 45);

        // 🔹 Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setBounds(50, 280, 250, 15);
        lblUsuario.setForeground(Color.GRAY);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtUsuario.setOpaque(false);
        panel.add(new PanelRedondeado(txtUsuario, COLOR_PRIMARIO)).setBounds(50, 300, 250, 45);

        // 🔹 Contraseña
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setBounds(50, 360, 250, 15);
        lblPass.setForeground(Color.GRAY);
        panel.add(lblPass);

        txtContrasena = new JPasswordField();
        txtContrasena.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtContrasena.setOpaque(false);
        panel.add(new PanelRedondeado(txtContrasena, COLOR_PRIMARIO)).setBounds(50, 380, 250, 45);

        // 🔹 Confirmar contraseña
        JLabel lblConfirmar = new JLabel("Confirmar contraseña");
        lblConfirmar.setBounds(50, 440, 250, 15);
        lblConfirmar.setForeground(Color.GRAY);
        panel.add(lblConfirmar);

        txtConfirmar = new JPasswordField();
        txtConfirmar.setBorder(new EmptyBorder(10, 15, 10, 15));
        txtConfirmar.setOpaque(false);
        panel.add(new PanelRedondeado(txtConfirmar, COLOR_PRIMARIO)).setBounds(50, 460, 250, 45);

        // 🔹 Botón Crear
        JButton btnCrear = new JButton("Crear cuenta");
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(new BotonRedondeado(btnCrear, COLOR_PRIMARIO)).setBounds(50, 520, 250, 50);

        btnCrear.addActionListener(e -> validar());

        // 🔹 Botón Volver
        JLabel lblVolver = new JLabel("Volver");
        lblVolver.setBounds(240, 590, 80, 20);
        lblVolver.setForeground(COLOR_PRIMARIO);
        lblVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblVolver.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblVolver);

        lblVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new Login();
                dispose();
            }

            public void mouseEntered(java.awt.event.MouseEvent e) {
                lblVolver.setText("<html><u>Volver</u></html>");
                lblVolver.setForeground(COLOR_PRIMARIO.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                lblVolver.setText("Volver");
                lblVolver.setForeground(COLOR_PRIMARIO);
            }
        });

        add(panel);
    }

    private void validar() {

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());

        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || pass.isEmpty() || confirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return;
        }

        if (usuario.length() < 8) {
            JOptionPane.showMessageDialog(this, "El usuario debe tener mínimo 8 caracteres");
            return;
        }

        if (usuarios.containsKey(usuario)) {
            JOptionPane.showMessageDialog(this, "El usuario ya está en uso");
            return;
        }

        if (pass.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener mínimo 6 caracteres");
            return;
        }

        if (!pass.matches(".*[A-Z].*")) {
            JOptionPane.showMessageDialog(this, "Debe tener al menos una mayúscula");
            return;
        }

        if (!pass.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
            return;
        }

        guardarUsuario(nombre, apellido, usuario, pass);
        usuarios.put(usuario, pass);

JOptionPane.showMessageDialog(this, "Usuario creado correctamente");

// Abrir Login automáticamente
new Login();

// Cerrar ventana actual
dispose();
    }

    private void guardarUsuario(String n, String a, String u, String p) {
        try {
            File archivo = new File(RUTA);
            archivo.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write(n + "," + a + "," + u + "," + p);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar");
        }
    }

    private void cargarUsuarios() {
        try {
            File archivo = new File(RUTA);
            if (!archivo.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(",");
                if (d.length >= 4) usuarios.put(d[2], d[3]);
            }

            br.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer archivo");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Crear_usuario::new);
    }
}

/* 🔹 Panel redondeado */
class PanelRedondeado extends JPanel {
    private final Color color;

    public PanelRedondeado(JComponent comp, Color color) {
        this.color = color;
        setLayout(new BorderLayout());
        setOpaque(false);
        comp.setOpaque(false);
        add(comp);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
        g2.setColor(color);
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);
    }
}

/* 🔹 Botón redondeado */
class BotonRedondeado extends JPanel {
    private final JButton btn;
    private Color color;
    private final Color base;

    public BotonRedondeado(JButton btn, Color base) {
        this.btn = btn;
        this.base = base;
        this.color = base;

        setLayout(new BorderLayout());
        setOpaque(false);

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(Color.WHITE);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                color = base.darker();
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                color = base;
                repaint();
            }
        });

        add(btn);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
    }
}