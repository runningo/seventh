/*
 * see license.txt 
 */
package seventh.game.events;

import leola.frontend.listener.Event;
import seventh.game.entities.Flag;

/**
 * @author Tony
 *
 */
public class FlagStolenEvent extends Event {

    private Flag flag;
    private int playerId;
    
    /**
     * @param source
     */
    public FlagStolenEvent(Object source, Flag flag, int playerId) {
        super(source);
        this.flag = flag;
        this.playerId = playerId;
    }
    
    /**
     * @return the flag
     */
    public Flag getFlag() {
        return flag;
    }
    
    /**
     * @return the playerId
     */
    public int getPlayerId() {
        return playerId;
    }

}
