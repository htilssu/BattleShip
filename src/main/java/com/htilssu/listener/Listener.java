package com.htilssu.listener;

import com.htilssu.annotation.EventHandler;

/**
 * Interface đánh dấu một class là một listener, khi 1 listener implement interface này thì khi định nghĩa các hàm xử
 * lý bắt buộc phải có annotation {@link EventHandler}
 * <p>
 * Ví dụ:
 * <pre>
 * {@code
 * public class PlayerListener implements Listener {
 *
 * @EventHandler
 * public void onPlayerAttack(PlayerAttackEvent e) {
 * // Xử lý sự kiện bắn của người chơi
 * }}}
 * </pre>
 * <p>
 * Trong đó {@code onPlayerAttack} là hàm xử lý
 * sự kiện bắn của người chơi, {@code PlayerAttackEvent}
 * là sự kiện bắn được truyền vào
 *
 * @see EventHandler
 */
public interface Listener {
}
