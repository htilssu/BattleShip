//package com.htilssu.event.game;
//
//import com.htilssu.ui.component.GameButton;
//import com.htilssu.ui.screen.Start2Player;
//import com.htilssu.util.AssetUtils;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.concurrent.ExecutionException;
//
//public class GameLoadingScreen {
//    private JProgressBar progressBar;
//    private JLabel loadingLabel;
//    private JLabel loadingImageLabel;
//    private ImageIcon[] images;
//
//    public GameLoadingScreen() {
//
//    }
//    private void startLoading () {
//        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
//            @Override
//            protected Void doInBackground() throws Exception {
//                // Giả lập tải tài nguyên game
//                for (int i = 0; i <= 100; i++) {
//                    Thread.sleep(25); // Giả lập thời gian tải tài nguyên
//                    publish(i); // Cập nhật tiến trình
//                }
//                return null;
//            }
//
//            @Override
//            protected void process(java.util.List<Integer> chunks) {
//                // Cập nhật giao diện với tiến trình hiện tại
//                int progress = chunks.get(chunks.size() - 1);
//                progressBar.setValue(progress);
//                // Cập nhật hình ảnh loading
//                int imageIndex = (progress / 25) % images.length;
//                loadingImageLabel.setIcon(images[imageIndex]);
//            }
//
//            @Override
//            protected void done() {
//                // Khi việc tải tài nguyên hoàn thành
//                try {
//                    get();
//                    progressBar.setVisible(false);
//                    loadingLabel.setVisible(false);
//                    loadingImageLabel.setVisible(false);
//                    SwingUtilities.getWindowAncestor(Start2Player.this).dispose();
//                    SetNew();
//                    player1Turn();
//                    player2turn();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        worker.execute();
//    }
//    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
//        Image img = icon.getImage();
//        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        return new ImageIcon(resizedImage);
//    }
//
//    private void loadImageScreenStart() {
//        // Tải hình ảnh
//        images = new ImageIcon[] {
//                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading1.png"), 200, 40),
//                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading2.png"), 200, 40),
//                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading3.png"), 200, 40),
//                resizeImageIcon(new ImageIcon("src/main/resources/images/Loading4.png"), 200, 40)
//        };
//    }
//}
