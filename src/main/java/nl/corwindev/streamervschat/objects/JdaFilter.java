package nl.corwindev.streamervschat.objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import nl.corwindev.streamervschat.main;


public class JdaFilter implements Filter {

    public Result check(String loggerName, Level level, String message, Throwable throwable) {
        if (loggerName.startsWith("net.dv8tion")) {

            switch (level.name()) {
                case "INFO":
                    main.plugin.getLogger().info("[JDA] " + message);
                    break;
                case "WARN":
                    if (message.contains("Encountered 429")) {
                        main.plugin.getLogger().info(message);
                        break;
                    }

                    main.plugin.getLogger().warning("[JDA] " + message);
                    break;
                case "ERROR":
                    if (message.contains("Requester timed out while executing a request")) {
                        main.plugin.getLogger().log(java.util.logging.Level.WARNING,"[JDA] " + message + ". This is either a issue on Discord's end (https://discordstatus.com) or with your server's connection");

                        main.plugin.getLogger().log(java.util.logging.Level.WARNING, ExceptionUtils.getStackTrace(throwable));
                        break;
                    }

                    if (throwable != null) {
                        main.plugin.getLogger().info("[JDA] " + message + "\n" + ExceptionUtils.getStackTrace(throwable));
                    } else {
                        main.plugin.getLogger().info("[JDA] " + message);
                    }
                    break;
                default:
                    main.plugin.getLogger().info("[JDA] " + message);
            }
        }else if(loggerName.startsWith("com.github.twitch4j") || loggerName.startsWith("com.github.philippheuer.events4j")){
            switch (level.name()) {
                case "INFO":
                    main.plugin.getLogger().info("[TWITCH] " + message);
                    break;
                case "WARN":
                    main.plugin.getLogger().warning("[TWITCH] " + message);
                    break;
                case "ERROR":
                    if (throwable != null) {
                        main.plugin.getLogger().info("[TWITCH] " + message + "\n" + ExceptionUtils.getStackTrace(throwable));
                    } else {
                        main.plugin.getLogger().info("[TWITCH] " + message);
                    }
                    break;
                default:
                    main.plugin.getLogger().info("[TWITCH] " + message);
            }
        }else{
            return Result.NEUTRAL;
        }
        return Result.DENY;
    }

    @Override
    public Result filter(LogEvent logEvent) {
        return check(
                logEvent.getLoggerName(),
                logEvent.getLevel(),
                logEvent.getMessage()
                        .getFormattedMessage(),
                logEvent.getThrown());
    }
    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object... parameters) {
        return check(
                logger.getName(),
                level,
                message,
                null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return null;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object message, Throwable throwable) {
        return check(
                logger.getName(),
                level,
                message.toString(),
                throwable);
    }
    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return check(
                logger.getName(),
                level,
                message.getFormattedMessage(),
                throwable);
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void initialize() {

    }

    public void start() {}
    public void stop() {}
    public boolean isStarted() {
        return true;
    }
    public boolean isStopped() {
        return false;
    }

    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }
    @Override
    public Result getOnMatch() {
        return Result.NEUTRAL;
    }
}