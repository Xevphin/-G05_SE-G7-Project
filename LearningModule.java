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
        titleLabel.setBounds(20, 15, 320, 35);
        page.add(titleLabel);

        // 2. The Poster (Image)
        // We look for an image named "poster0.png", "poster1.png", etc., based on the page index.
        int textY = 370;       // Default text starting position
        int textHeight = 170;  // Default text height

        try {
            String imagePath = "poster" + index + ".jpeg"; // Ensure this matches your file type
            ImageIcon originalIcon = new ImageIcon(imagePath);
            
            if (originalIcon.getIconWidth() > 0) {
                int origW = originalIcon.getIconWidth();
                int origH = originalIcon.getIconHeight();
                
                // Calculate new height while maintaining the original aspect ratio!
                int targetW = 320;
                int targetH = (origH * targetW) / origW;

                // Safety cap: If they upload a super tall portrait image, restrict it to 200px 
                // so it doesn't push the text completely off the bottom of the screen.
                if (targetH > 380) {
                    targetH = 380;
                    targetW = (origW * targetH) / origH; 
                }

                Image scaledImg = originalIcon.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
                
                // Center the image nicely in the middle of the screen
                int xOffset = (360 - targetW) / 2;
                
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
                imageLabel.setBounds(xOffset, 58, targetW, targetH);
                page.add(imageLabel);

                // Dynamically push the text down based on how tall the image actually is
                textY = 58 + targetH + 12; 
                textHeight = 545 - textY; // Fill the remaining space above the buttons
            } else {
                JLabel placeholder = new JLabel("[ Poster Image Space ]", SwingConstants.CENTER);
                placeholder.setBounds(25, 58, 310, 200);
                placeholder.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                page.add(placeholder);
            }
        } catch (Exception e) {
            System.out.println("Image missing for page " + index);
        }

        // 3. The Text Area 
        JTextArea textArea = new JTextArea(content);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFocusable(false); // Pro-tip: This hides that weird blinking text cursor!
        textArea.setBackground(new Color(245, 248, 252));
        textArea.setFont(new Font("Arial", Font.PLAIN, 13));
        textArea.setBounds(25, textY, 310, Math.max(textHeight, 40)); // Uses dynamic positioning
        page.add(textArea);

        // 4. Navigation Buttons
        JButton prevBtn = new JButton("Previous");
        prevBtn.setBounds(20, 555, 100, 40);
        prevBtn.addActionListener(e -> prevSlide());
        page.add(prevBtn);

        JButton nextBtn = new JButton(index == TOTAL_PAGES - 1 ? "Finish" : "Next");
        nextBtn.setBounds(230, 555, 100, 40);
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