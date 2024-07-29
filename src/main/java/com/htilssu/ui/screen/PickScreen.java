package com.htilssu.ui.screen;

import com.htilssu.BattleShip;
import com.htilssu.event.game.GameAction;
import com.htilssu.manager.DifficultyManager;
import com.htilssu.manager.GameManager;
import com.htilssu.manager.ScreenManager;
import com.htilssu.multiplayer.Host;
import com.htilssu.util.AssetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static com.htilssu.manager.GameManager.gamePlayer;

public class PickScreen extends JPanel {

    private static final int IMAGE_GAP = 30; // Khoảng cách cố định giữa các hình ảnh
    private BattleShip window;
    private BufferedImage normalImage, hardImage, backgroundImage;
    private boolean isNormalImageHovered = false;
    private boolean isHardImageHovered = false;
    private int normalImageX, normalImageY, newNormalImageWidth, newImageHeight;
    private int hardImageX, hardImageY, newHardImageWidth;
    private int pickMode = GameManager.SINGLE_PLAYER;

    public PickScreen(BattleShip battleShip) {
        window = battleShip;
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        normalImage = AssetUtils.loadImage("/images/Normal.png");
        hardImage = AssetUtils.loadImage("/images/Hard.png");
        loadBackgroundImage();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                handleMouseMove(e.getX(), e.getY());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isNormalImageHovered = false;
                isHardImageHovered = false;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e.getX(), e.getY());
            }
        });
    }

    private void loadBackgroundImage() {
        backgroundImage = AssetUtils.loadImage("/images/ground.png"); // Tải hình nền
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        int difficulty = getImage(mouseX, mouseY);
        if (difficulty != -1) {
            DifficultyManager.difficulty = difficulty;
            if (pickMode == GameManager.SINGLE_PLAYER) {
                transitionToGameScreen();
            }
            else {
                Host host = window.getHost();
                if (host.isConnected()) {
                    int opponentTurn = window.getGameManager().turn == 0 ? 1 : 0;
                    host.send(GameAction.JOIN, gamePlayer.getId(), gamePlayer.getName(),
                            DifficultyManager.difficulty, opponentTurn
                    );

                    window.getGameManager().createNewGamePlay();


                    window.changeScreen(ScreenManager.PLAY_SCREEN);
                }
            }
        }
    }

    private void handleMouseMove(int mouseX, int mouseY) {
        boolean previouslyHovered = isNormalImageHovered || isHardImageHovered;

        isNormalImageHovered = isInsideImage(mouseX,
                mouseY,
                normalImageX,
                normalImageY,
                newNormalImageWidth,
                newImageHeight
        );
        isHardImageHovered = isInsideImage(mouseX,
                mouseY,
                hardImageX,
                hardImageY,
                newHardImageWidth,
                newImageHeight
        );

        if (previouslyHovered != (isNormalImageHovered || isHardImageHovered)) {
            repaint();
        }
    }

    private int getImage(int mouseX, int mouseY) {
        isNormalImageHovered = isInsideImage(mouseX,
                mouseY,
                normalImageX,
                normalImageY,
                newNormalImageWidth,
                newImageHeight
        );
        isHardImageHovered = isInsideImage(mouseX,
                mouseY,
                hardImageX,
                hardImageY,
                newHardImageWidth,
                newImageHeight
        );

        if (isNormalImageHovered) return DifficultyManager.NORMAL;
        if (isHardImageHovered) return DifficultyManager.HARD;
        return -1;
    }

    private void transitionToGameScreen() {
        window.changeScreen(ScreenManager.START2_PLAYER_SCREEN);
    }

    private boolean isInsideImage(int mouseX,
            int mouseY,
            int imageX,
            int imageY,
            int imageWidth,
            int imageHeight) {// Kiểm tra xem chuột có nằm trong phạm vi ảnh kooong
        return mouseX >= imageX && mouseX <= imageX + imageWidth && mouseY >= imageY && mouseY <= imageY + imageHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (normalImage != null && hardImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int normalImageOriginalWidth = normalImage.getWidth();
            int normalImageOriginalHeight = normalImage.getHeight();
            int hardImageOriginalWidth = hardImage.getWidth();
            int hardImageOriginalHeight = hardImage.getHeight();

            // Tính toán tỉ lệ khung hình
            float normalAspectRatio = (float) normalImageOriginalWidth / normalImageOriginalHeight;
            float hardAspectRatio = (float) hardImageOriginalWidth / hardImageOriginalHeight;

            // Xác định chiều cao tối đa cho hình ảnh để duy trì tỉ lệ khung hình
            newImageHeight = panelHeight / 2;  // Một nửa chiều cao của panel
            newNormalImageWidth = (int) (newImageHeight * normalAspectRatio);
            newHardImageWidth = (int) (newImageHeight * hardAspectRatio);

            int totalWidth = newNormalImageWidth + IMAGE_GAP + newHardImageWidth;

            // Đảm bảo hình ảnh vừa với chiều rộng của panel
            if (totalWidth > panelWidth) {
                float scaleFactor =
                        (float) (panelWidth - IMAGE_GAP) / (newNormalImageWidth + newHardImageWidth);
                newNormalImageWidth *= (int) scaleFactor;
                newHardImageWidth *= (int) scaleFactor;
                newImageHeight *= (int) scaleFactor;
            }

            // Tính toán vị trí để căn giữa hình ảnh với khoảng cách
            normalImageX = (panelWidth - totalWidth) / 2;
            normalImageY = (panelHeight - newImageHeight) / 2;
            hardImageX = normalImageX + newNormalImageWidth + IMAGE_GAP;
            hardImageY = (panelHeight - newImageHeight) / 2;

            // Vẽ hình ảnh với kích thước và khoảng cách mới
            if (isNormalImageHovered) {
                drawResizedImage(g,
                        normalImage,
                        normalImageX,
                        normalImageY,
                        newNormalImageWidth,
                        newImageHeight
                );
            }
            else {
                g.drawImage(normalImage,
                        normalImageX,
                        normalImageY,
                        newNormalImageWidth,
                        newImageHeight,
                        this
                );
            }

            if (isHardImageHovered) {
                drawResizedImage(g,
                        hardImage,
                        hardImageX,
                        hardImageY,
                        newHardImageWidth,
                        newImageHeight
                );
            }
            else {
                g.drawImage(hardImage,
                        hardImageX,
                        hardImageY,
                        newHardImageWidth,
                        newImageHeight,
                        this
                );
            }
        }
    }

    private void drawResizedImage(Graphics g,
            BufferedImage image,
            int x,
            int y,
            int width,
            int height) {
        int shrinkAmount = 10; // Kích thước thu nhỏ
        int newWidth = width - shrinkAmount; // Chiều rộng mới khi hover
        int newHeight = height - shrinkAmount; // Chiều cao mới khi hover
        int xOffset = (width - newWidth) / 2; // Tọa độ x khi hover
        int yOffset = (height - newHeight) / 2; // Tọa độ y khi hover
        g.drawImage(image, x + xOffset, y + yOffset, newWidth, newHeight, this);
    }

    public void setGameMode(int pickMode) {
        this.pickMode = pickMode;
    }
}
