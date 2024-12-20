package Core;

public class StatusEffect {
    private String type;
    private long endTime;

    public StatusEffect(String type, long duration) {
        this.type = type;
        this.endTime = System.currentTimeMillis() + duration;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.endTime;
    }

    public String getType() {
        return type;
    }
}
