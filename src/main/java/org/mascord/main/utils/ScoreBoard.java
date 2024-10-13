package org.mascord.main.utils;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mascord.main.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoard implements Listener {
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private final String[] animatedTitles = {
            "§e피", "§e피로", "§e피로 서", "§e피로 서버",
            "§e피로 서버", "§e피로 서버", "§e피로 서버",
            "§f피§e로 서버", "§e피§f로 §e서버",
            "§e피로§f 서§e버", "§e피로 서§f버", "§e피로 서버", "§e피로 서버", "§e피로 서버"
    };

    public ScoreBoard(Main plugin) {
        // 생성자
        Bukkit.getPluginManager().registerEvents(this, plugin);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (FastBoard board : boards.values()) {
                updateBoard(board,
                        "",
                        "§e소지금: 我 170,282,000원",
                        "치즈: 上 153,920개",
                        "루비: 響 3개",
                        "",
                        "총 접속자: 安 " + Bukkit.getOnlinePlayers().size() + " 명",
                        "",
                        "현재 서버: 升 야생 채널#1"
                );
            }
        }, 0L, 10L);

        // 타이틀 애니메이션을 위한 스케줄러
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (FastBoard board : boards.values()) {
                animateBoardTitle(board);
            }
        }, 0L, 12L); // 8틱마다 타이틀이 바뀌도록 설정
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FastBoard board = new FastBoard(player);

        boards.put(player.getUniqueId(), board);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        FastBoard board = boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    // 일반적인 메서드로 변경
    public void updateBoard(FastBoard board, String... lines) {
        for (int a = 0; a < lines.length; ++a) {
            lines[a] = ChatColor.translateAlternateColorCodes('&', lines[a]);
        }

        board.updateLines(lines);
    }

    // 타이틀 애니메이션 메서드
    private int titleIndex = 0;

    public void animateBoardTitle(FastBoard board) {
        String title = animatedTitles[titleIndex];
        board.updateTitle(title);

        // 타이틀 배열의 끝까지 도달하면 다시 처음으로
        titleIndex = (titleIndex + 1) % animatedTitles.length;
    }
}
