package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageProcessingForm extends JFrame {
    private BufferedImage img;
    private JLabel originalImageLabel;
    private JLabel[] processedImageLabels;
    private JButton loadButton;
    private JButton processButton;

    public ImageProcessingForm() {
        super("Image Processor");

        originalImageLabel = new JLabel();
        originalImageLabel.setHorizontalAlignment(JLabel.CENTER);
        processedImageLabels = new JLabel[4];
        for (int i = 0; i < processedImageLabels.length; i++) {
            processedImageLabels[i] = new JLabel();
            processedImageLabels[i].setHorizontalAlignment(JLabel.CENTER);
        }

        loadButton = new JButton("Load Image");
        processButton = new JButton("Apply Filters");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImage();
            }
        });

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);  // Padding

        // Original image label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        add(originalImageLabel, gbc);

        // Load button
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        add(loadButton, gbc);

        // Process button
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(processButton, gbc);

        // Processed image labels
        for (int i = 0; i < processedImageLabels.length; i++) {
            gbc.gridx = 2 + (i % 2);
            gbc.gridy = i / 2;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.5;
            gbc.weighty = 0.5;
            add(processedImageLabels[i], gbc);
        }

        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                img = ImageIO.read(fileChooser.getSelectedFile());
                ImageIcon imageIcon = new ImageIcon(img);
                Image scaledImage = imageIcon.getImage().getScaledInstance(400, -1, Image.SCALE_SMOOTH);
                originalImageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void applyFilters() {
        if (img == null) return;

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(() -> processedImageLabels[0].setIcon(new ImageIcon(scaleImage(applyThreshold(img)))));
        executor.submit(() -> processedImageLabels[1].setIcon(new ImageIcon(scaleImage(applyNegative(img)))));
        executor.submit(() -> processedImageLabels[2].setIcon(new ImageIcon(scaleImage(applyBrightness(img, 50)))));
        executor.submit(() -> processedImageLabels[3].setIcon(new ImageIcon(scaleImage(applyEdgeDetection(img)))));
        executor.shutdown();
    }

    private BufferedImage applyThreshold(BufferedImage src) {
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                Color color = new Color(src.getRGB(x, y));
                int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                if (brightness > 128) {
                    result.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    result.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return result;
    }

    private BufferedImage applyNegative(BufferedImage src) {
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                Color color = new Color(src.getRGB(x, y));
                Color negative = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                result.setRGB(x, y, negative.getRGB());
            }
        }
        return result;
    }

    private BufferedImage applyBrightness(BufferedImage src, int amount) {
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                Color color = new Color(src.getRGB(x, y));
                int red = Math.min(255, color.getRed() + amount);
                int green = Math.min(255, color.getGreen() + amount);
                int blue = Math.min(255, color.getBlue() + amount);
                Color brightened = new Color(red, green, blue);
                result.setRGB(x, y, brightened.getRGB());
            }
        }
        return result;
    }

    private BufferedImage applyEdgeDetection(BufferedImage src) {
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        int[][] kernel = {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
        int offset = kernel.length / 2;

        for (int y = offset; y < src.getHeight() - offset; y++) {
            for (int x = offset; x < src.getWidth() - offset; x++) {
                int edgeColor = applyKernel(src, kernel, x, y);
                result.setRGB(x, y, edgeColor);
            }
        }
        return result;
    }

    private int applyKernel(BufferedImage src, int[][] kernel, int x, int y) {
        int red = 0, green = 0, blue = 0;
        int offset = kernel.length / 2;

        for (int ky = -offset; ky <= offset; ky++) {
            for (int kx = -offset; kx <= offset; kx++) {
                Color color = new Color(src.getRGB(x + kx, y + ky));
                red += color.getRed() * kernel[ky + offset][kx + offset];
                green += color.getGreen() * kernel[ky + offset][kx + offset];
                blue += color.getBlue() * kernel[ky + offset][kx + offset];
            }
        }
        red = Math.min(Math.max(red, 0), 255);
        green = Math.min(Math.max(green, 0), 255);
        blue = Math.min(Math.max(blue, 0), 255);

        return new Color(red, green, blue).getRGB();
    }

    private Image scaleImage(BufferedImage src) {
        return src.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImageProcessingForm();
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
    }
}
