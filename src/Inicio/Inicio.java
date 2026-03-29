package Inicio;

import Login.Login;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Inicio extends JFrame {

    private JPanel panelMaestro;
    private JPanel panelDetalle;
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_PRIMARIO = Color.decode("#2699FB");
    private final Color COLOR_SECUNDARIO = Color.decode("#0A2459");
    private final Color COLOR_TEXTO = new Color(50, 50, 50);

    public Inicio(String nombreUsuario) {
        setTitle("Inicio");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);

        // 🔹 Barra de herramientas
        JToolBar barraHerramientas = new JToolBar();
        barraHerramientas.setFloatable(false);
        barraHerramientas.setBackground(COLOR_FONDO);

        JButton botonCerrarSesion = new JButton("Cerrar sesión");
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.setBackground(COLOR_PRIMARIO);
        botonCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCerrarSesion.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        botonCerrarSesion.addActionListener(e -> {
            new Login();
            dispose();
        });

        barraHerramientas.add(Box.createHorizontalGlue()); // empuja el botón a la derecha
        barraHerramientas.add(botonCerrarSesion);

        // 🔹 Panel maestro (lista de donuts)
        panelMaestro = new JPanel();
        panelMaestro.setLayout(new BoxLayout(panelMaestro, BoxLayout.Y_AXIS));
        panelMaestro.setBackground(COLOR_FONDO);

        JScrollPane scrollMaestro = new JScrollPane(panelMaestro);
        scrollMaestro.setBorder(BorderFactory.createEmptyBorder());

        // 🔹 Panel detalle (información del donut seleccionado)
        panelDetalle = new JPanel();
        panelDetalle.setBackground(Color.WHITE);
        panelDetalle.setLayout(new BorderLayout());
        JLabel etiquetaDetalle = new JLabel("Seleccione un donut para ver detalles");
        etiquetaDetalle.setHorizontalAlignment(SwingConstants.CENTER);
        etiquetaDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panelDetalle.add(etiquetaDetalle, BorderLayout.CENTER);

        // 🔹 JSplitPane para maestro-detalle
        JSplitPane separador = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollMaestro, panelDetalle);
        separador.setDividerLocation(250);
        separador.setDividerSize(5);

        // 🔹 Layout principal
        setLayout(new BorderLayout());
        add(barraHerramientas, BorderLayout.NORTH);
        add(separador, BorderLayout.CENTER);

        setVisible(true);

        // 🔹 Consumir API de donuts de forma asíncrona
        consumirAPI();
    }

    private void consumirAPI() {
        new Thread(() -> {
            try {
                URL url = new URL("https://private-1ca53c-training45.apiary-mock.com/donuts");
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");

                BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder respuesta = new StringBuilder();
                String linea;
                while ((linea = lector.readLine()) != null) {
                    respuesta.append(linea);
                }
                lector.close();

                JSONArray jsonArray = new JSONArray(respuesta.toString());

                SwingUtilities.invokeLater(() -> {
                    panelMaestro.removeAll();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objeto = jsonArray.getJSONObject(i);
                        String nombre = objeto.optString("name", "Sin nombre");
                        double precio = objeto.optDouble("ppu", 0.0);

                        // 🔹 Crear tarjeta del donut
                        JPanel tarjeta = new JPanel();
                        tarjeta.setLayout(new BorderLayout());
                        tarjeta.setBackground(Color.WHITE);
                        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                                new EmptyBorder(10, 10, 10, 10)
                        ));

                        JLabel etiquetaNombre = new JLabel("Dona " + nombre);
                        etiquetaNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
                        tarjeta.add(etiquetaNombre, BorderLayout.CENTER);

                        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

                        // 🔹 Evento click para mostrar detalle del donut
                        tarjeta.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                mostrarDetalle(nombre, precio);
                            }
                        });

                        panelMaestro.add(tarjeta);
                        panelMaestro.add(Box.createRigidArea(new Dimension(0, 5))); // espacio entre tarjetas
                    }

                    panelMaestro.revalidate();
                    panelMaestro.repaint();
                });

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    panelMaestro.removeAll();
                    panelMaestro.add(new JLabel("Error al consumir la API"));
                    panelMaestro.revalidate();
                    panelMaestro.repaint();
                });
            }
        }).start();
    }

    private void mostrarDetalle(String nombre, double precio) {
        panelDetalle.removeAll();

        JPanel detalle = new JPanel();
        detalle.setLayout(new BoxLayout(detalle, BoxLayout.Y_AXIS));
        detalle.setBorder(new EmptyBorder(20, 20, 20, 20));
        detalle.setBackground(Color.WHITE);

        JLabel etiquetaNombre = new JLabel("Nombre: " + nombre);
        etiquetaNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        etiquetaNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiquetaPrecio = new JLabel("Precio: $" + precio);
        etiquetaPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        etiquetaPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        detalle.add(etiquetaNombre);
        detalle.add(Box.createRigidArea(new Dimension(0, 10)));
        detalle.add(etiquetaPrecio);

        panelDetalle.add(detalle, BorderLayout.NORTH);
        panelDetalle.revalidate();
        panelDetalle.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Inicio("Josué"));
    }
}