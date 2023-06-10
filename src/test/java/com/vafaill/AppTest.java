package com.vafaill;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class AppTest {
    @Test
    void createButton_ValidText_ReturnsButtonWithText() {
        String buttonText = "Test Button";
        JButton button = App.createButton(buttonText);
        Assertions.assertEquals(buttonText, button.getText());
    }

    @Test
    void createButton_EmptyText_ReturnsButtonWithEmptyText() {
        String buttonText = "";
        JButton button = App.createButton(buttonText);
        Assertions.assertEquals(buttonText, button.getText());
    }

    @Test
    void createButton_NullText_ReturnsButtonWithNullText() {
        String buttonText = null;
        JButton button = App.createButton(buttonText);
        Assertions.assertNull(button.getText());
    }

    @Test
    void createButton_UpdatesBackgroundOnMouseEnter() {
        JButton button = App.createButton("Test");
        Color defaultBackground = button.getBackground();
        button.doClick(); // Simulate a mouse click
        button.getMouseListeners()[0].mouseEntered(null); // Trigger mouse enter event
        Assertions.assertNotEquals(defaultBackground, button.getBackground());
    }

    @Test
    void createButton_UpdatesBackgroundOnMouseExit() {
        JButton button = App.createButton("Test");
        Color defaultBackground = button.getBackground();
        button.doClick(); // Simulate a mouse click
        button.getMouseListeners()[0].mouseExited(null); // Trigger mouse exit event
        Assertions.assertEquals(defaultBackground, button.getBackground());
    }

    @Test
    void createLoginContentPanel_ValidUsernameAndPassword_ReturnsPanelWithFields() {
        JPanel panel = App.createLoginContentPanel();
        JTextField usernameField = (JTextField) panel.getComponent(1);
        JPasswordField passwordField = (JPasswordField) panel.getComponent(3);
        Assertions.assertNotNull(usernameField);
        Assertions.assertNotNull(passwordField);
    }

    @Test
    void createGameContentPanel_ValidTitle_ReturnsPanelWithTitle() {
        JPanel panel = App.createGameContentPanel();
        JLabel titleLabel = (JLabel) panel.getComponent(0);
        Assertions.assertEquals("Çarpım Tablosu Alıştırması", titleLabel.getText());
    }

    @Test
    void createGameContentPanel_StartButtonActionListener_StartsTimer() {
        JPanel panel = App.createGameContentPanel();
        JPanel gamePanel = (JPanel) panel.getComponent(1);
        JButton startButton = (JButton) gamePanel.getComponent(1);

        long startTime = System.currentTimeMillis();
        startButton.doClick();

        Component[] components = gamePanel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel timerLabel = (JLabel) component;
                String timerText = timerLabel.getText();
                Assertions.assertTrue(timerText.matches("Geçen Süre: \\d+ saniye"));
                break;
            }
        }

        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        Assertions.assertTrue(elapsedTime > 0);
    }
}