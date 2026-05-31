/**
 * Creator: Wan Adam
 * Tester:
 * Description: Displays educational content about SDG 4 using CardLayout
 */
import java.awt.*;
import javax.swing.*;

public class LearningModule implements ILearning {

    private JFrame learningFrame;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private int currentPage = 0;
    private Home parentHome;
    private final int TOTAL_PAGES = 10; // Total number of content pages

    public LearningModule() {
        learningFrame = new JFrame("SDG 4 - Learning Module");
        learningFrame.setSize(360, 640);
        learningFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        learningFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        for (int i = 0; i < TOTAL_PAGES; i++) {
            createPage("SDG 4 Topic " + (i + 1), "Educational content for page " + (i + 1) +
            " goes here. Quality education is the foundation to improving peopls's lives and sustainable development.", i);
        }

        learningFrame.add(contentPanel);
        showScreen();
    }

    public void setParent(Home home) {
        this.parentHome = home;
    }

   @Override
   public void createPage(String title, String content, int index) {
        JPanel page = new JPanel(null);
        page.setBackground(new Color(245, 248, 252));

        // 1. The Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 120, 90));
        titleLabel.setBounds(20, 20, 300, 30);
        page.add(titleLabel);

        // 2. The Poster (Image)
        // We look for an image named "poster0.png", "poster1.png", etc., based on the page index.
        try {
            String imagePath = "poster" + index + ".png"; 
            ImageIcon originalIcon = new ImageIcon(imagePath);
            
            // Check if the image actually exists before trying to draw it
            if (originalIcon.getIconWidth() > 0) {
                // Scale the image to fit the screen nicely
                Image scaledImg = originalIcon.getImage().getScaledInstance(280, 160, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
                imageLabel.setBounds(35, 60, 280, 160);
                page.add(imageLabel);
            } else {
                // If no image is found, show a placeholder box
                JLabel placeholder = new JLabel("[ Poster Image Space ]", SwingConstants.CENTER);
                placeholder.setBounds(35, 60, 280, 160);
                placeholder.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                page.add(placeholder);
            }
        } catch (Exception e) {
            System.out.println("Image missing for page " + index);
        }

        // 3. The Text Area (Pushed down to y=230 to make room for the poster)
        JTextArea textArea = new JTextArea(content);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setBackground(new Color(245, 248, 252));
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBounds(35, 230, 280, 250);
        page.add(textArea);

        // 4. Navigation Buttons
        JButton prevBtn = new JButton("Previous");
        prevBtn.setBounds(20, 500, 100, 40);
        prevBtn.addActionListener(e -> prevSlide());
        page.add(prevBtn);

        JButton nextBtn = new JButton(index == TOTAL_PAGES - 1 ? "Finish" : "Next");
        nextBtn.setBounds(220, 500, 100, 40);
        nextBtn.addActionListener(e -> nextSlide());
        page.add(nextBtn);

        contentPanel.add(page, "Page" + index);
    }

    @Override
    public void showScreen() {
        learningFrame.setVisible(true);
    }

    @Override
    public void nextSlide() {
        if (currentPage < TOTAL_PAGES - 1) {
            currentPage++;
            cardLayout.show(contentPanel, "Page" + currentPage);
        } else {
            learningFrame.dispose();
            if (parentHome != null) parentHome.showHomePanel();
        }
    }

    @Override
    public void prevSlide() {
        if (currentPage > 0) {
            currentPage--;
            cardLayout.show(contentPanel, "Page" + currentPage);
        }
    }

    @Override
    public int getTotalPages() {
        return TOTAL_PAGES;
    }
}