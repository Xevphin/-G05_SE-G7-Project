/**
 * Creator: Wan Adam 103014
 * Tester: Wan Adam 103014
 * Description: Interface defining the methods for a learning module, 
 * including creating pages, displaying the screen, navigating through slides, and getting the total number of pages.
 */

public interface ILearning {
    void createPage(String title, String content, int index);
    void showScreen();
    void nextSlide();
    void prevSlide();
    int getTotalPages();
}