package me.simondumalski.lightbulblegacy.messaging.enums;

public enum Message {

    PREFIX("messages.prefix"),

    UNKNOWN_COMMAND("messages.errors.unknown-command"),
    INSUFFICIENT_PERMISSIONS("messages.errors.insufficient-permissions"),
    INVALID_PLAYER("messages.errors.invalid-player"),

    ADMIN_HELP("messages.admin.help"),
    RELOAD("messages.admin.reload"),

    COMPENSATE_HELP("messages.compensate.help"),
    ALREADY_COMPENSATED("messages.compensate.already-compensated"),
    COMPENSATION_SENT("messages.compensate.sent"),
    COMPENSATION_RECEIVED("messages.compensate.received"),

    COMBAT_HELP("messages.combat.help"),
    IN_COOLDOWN("messages.combat.in-cooldown"),
    IN_COMBAT("messages.combat.in-combat"),
    ENTERING_COMBAT("messages.combat.entering-combat"),
    LEAVING_COMBAT("messages.combat.leaving-combat"),
    PVP_ENABLED("messages.combat.pvp-enabled"),
    PVP_DISABLED("messages.combat.pvp-disabled"),
    ATTACKER_PVP_DISABLED("messages.combat.attacker-pvp-disabled"),
    DEFENDER_PVP_DISABLED("messages.combat.defender-pvp-disabled");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
