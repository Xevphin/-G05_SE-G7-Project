/**
 * Creator: Wan Adam
 * Tester:
 * Description: Interface defining the methods for a learning module, 
 * including creating pages, displaying the screen, navigating through slides, and getting the total number of pages.
 */

public interface ILearning {
    void createPage(String title, String content, int index);
    void showScreen();
    void nextslide();
    void prevslide();
    int getTotalPages();
}