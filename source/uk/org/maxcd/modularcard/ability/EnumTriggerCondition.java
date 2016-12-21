package uk.org.maxcd.modularcard.ability;

import java.util.ArrayList;

import uk.org.maxcd.modularcard.*;

public enum EnumTriggerCondition
{
    ENTER_PLAY(1, "On Enter:", true),
    LEAVE_PLAY(0.8, "On Leave:", false),
    TURN_START(2, "On Turn Start:", true),
    TURN_END(2.5, "On Turn End:", false);

    public final double triggerValue;
    public final String triggerText;
    public final boolean synchronous;

    EnumTriggerCondition(double triggerValue, String triggerText, boolean synchronous)
    {
        this.triggerValue = triggerValue;
        this.triggerText = triggerText;
        this.synchronous = synchronous;
    }
}