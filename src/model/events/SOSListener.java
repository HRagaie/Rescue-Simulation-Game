package model.events;

import simulation.Rescuable;

public interface SOSListener {
public void receiveSOSCall(Rescuable r); //reports citizen or building affected by disaster
}
