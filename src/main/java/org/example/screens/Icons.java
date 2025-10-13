package org.example.screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Centralized icon/emoji holder and loader utilities.
 * Use the emoji constants for simple labels, or getIcon()/getScaledIcon() to load images from
 * classpath, file system, or HTTP URLs (with timeouts and a default user-agent).
 */
public final class Icons {
    private Icons() {}

    // Common emojis used across the app
    public static final String SHOP = "🛍️";
    public static final String CART = "🛒";
    public static final String HISTORY = "📋";
    public static final String ACCOUNT = "👤";
    public static final String LIGHT = "☀️";
    public static final String DARK = "🌙";
    public static final String WARNING = "⚠️";
    public static final String CHECK = "✅";
    public static final String ERROR = "❌";

    // Keys for common image icons (PNG/SVG URLs)
    public enum Key {
        SHOP,
        CART_ADD,
        HISTORY,
        ACCOUNT,
        LIGHT,
        DARK,
        ALERT,
        CHECK,
        WARNING
    }

    // Map a key to a URL (you can replace these with your provided image URLs)
    private static final Map<Key, String> iconUrlByKey = new ConcurrentHashMap<>();

    // Simple in-memory cache for scaled icons
    private static final Map<String, ImageIcon> cache = new ConcurrentHashMap<>();

    public static ImageIcon getIcon(String pathOrUrl) {
        return getScaledIcon(pathOrUrl, -1, -1);
    }

    public static ImageIcon getScaledIcon(String pathOrUrl, int width, int height) {
        if (pathOrUrl == null || pathOrUrl.trim().isEmpty()) return null;
        final String key = pathOrUrl + "|" + width + "x" + height;
        ImageIcon cached = cache.get(key);
        if (cached != null) return cached;
        try {
            BufferedImage img = loadBuffered(pathOrUrl.trim());
            if (img == null) return null;
            Image image = img;
            if (width > 0 && height > 0) {
                image = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
            ImageIcon icon = new ImageIcon(image);
            cache.put(key, icon);
            return icon;
        } catch (Exception ignored) {
            return null;
        }
    }

    // --- Registry helpers to work with Keys ---
    public static void register(Key key, String url) {
        if (key != null && url != null && !url.isBlank()) {
            iconUrlByKey.put(key, url.trim());
        }
    }

    // Register default classpath PNGs under src/main/resources/icons/
    public static void registerDefaults() {
        register(Key.SHOP, "icons/shop.png");
        register(Key.CART_ADD, "icons/cart_add.png");
        register(Key.HISTORY, "icons/history.png");
        register(Key.ACCOUNT, "icons/account.png");
        register(Key.LIGHT, "icons/light.png");
        register(Key.DARK, "icons/dark.png");
        register(Key.ALERT, "icons/alert.png");
        register(Key.CHECK, "icons/check.png");
        register(Key.WARNING, "icons/warning.png");
    }

    public static ImageIcon getIcon(Key key) {
        return getIcon(key, -1, -1);
    }

    public static ImageIcon getIcon(Key key, int width, int height) {
        String url = iconUrlByKey.get(key);
        return (url != null) ? getScaledIcon(url, width, height) : null;
    }

    private static BufferedImage loadBuffered(String path) {
        try {
            // Try as URL
            try {
                URL url = new URL(path);
                var conn = url.openConnection();
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(6000);
                conn.setRequestProperty("User-Agent", "JavaMarket/1.0");
                try (InputStream in = conn.getInputStream()) {
                    return ImageIO.read(in);
                }
            } catch (Exception ignoreUrl) {
                // ignore
            }

            // Try as classpath resource
            try (InputStream in = Icons.class.getClassLoader().getResourceAsStream(path)) {
                if (in != null) return ImageIO.read(in);
            }

            // Try as local file
            File f = new File(path);
            if (f.exists()) return ImageIO.read(f);
        } catch (Exception ignored) {
        }
        return null;
    }
}


