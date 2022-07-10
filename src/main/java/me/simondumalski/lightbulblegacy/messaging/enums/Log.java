package me.simondumalski.lightbulblegacy.messaging.enums;

import java.util.logging.Level;

public enum Log {

    FILE_SAVED("Saved %args0%!", Level.INFO),
    FILE_LOADED("Loaded %args0%!", Level.INFO),

    INVALID_INTERVAL("Automatic data save interval is invalid! Defaulting to 300 seconds...", Level.WARNING),

    MISSING_DEPENDENCY("Missing plugin dependency %args0%! Some plugin functionality will be disabled.", Level.WARNING),
    MISSING_DEPENDENCY_SEVERE("Missing plugin dependency %args0%! Disabling plugin...", Level.SEVERE);


    private final String message;
    private final Level level;

    Log(String message, Level level) {
        this.message = message;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public Level getLevel() {
        return level;
    }

}
