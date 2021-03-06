/*
 * see license.txt 
 */
package seventh.client.inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Keys;

/**
 * @author Tony
 *
 */
public class InputMap extends Inputs {

    public static interface Action {
        public void action();
    }
    
    private Map<String, Action> actions;
    private IntMap<String> keymap;
    private IntMap<String> buttonmap;
    private IntMap<String> controllermap;
    
    private ControllerInput controllerInput;
    private String[] povButtons;
    private String[] scroller;
    /**
     * 
     */
    public InputMap(ControllerInput controllerInput) {
        this.controllerInput = controllerInput;
        
        this.actions = new HashMap<String, InputMap.Action>();
        this.keymap = new IntMap<String>();
        this.buttonmap = new IntMap<String>();
        this.controllermap = new IntMap<String>();
        this.povButtons = new String[PovDirection.values().length];
        
        this.scroller = new String[2];
    }
    
    /* (non-Javadoc)
     * @see seventh.client.Inputs#scrolled(int)
     */
    @Override
    public boolean scrolled(int notches) {    
        if(notches < 0) {
        	scrollerAction(0);
        }
        else {
        	scrollerAction(1);
        }
        return true;
    }
    
   
    public void scrollerAction(int i){
        if(this.scroller[i] != null) {
            Action action = this.actions.get(scroller[i]);
            IsActionNull(action);
        }
    }
    
    public void IsActionNull(Action action){
    	if(action != null)
    		action.action();
    }
    

    public void addAction(String name, Action action) {
        this.actions.put(name, action);
    }
    public void removeAction(String name) {
        this.actions.remove(name);
    }
    
    public void clearActions() {
        this.actions.clear();
    }
    
    public void bindKey(int key, String name) {
        this.keymap.put(key, name);
    }
    
    public void removeKeyBind(int key) {
        this.keymap.remove(key);
    }
    
    public void bindButton(int button, String name) {
        this.buttonmap.put(button, name);
    }
    
    public void removeButtonBind(int button) {
        this.buttonmap.remove(button);
    }
    
    public void bindControllerButton(int button, String name) {
        this.controllermap.put(button, name);
    }
    
    public void removeControllerBind(int button) {
        this.controllermap.remove(button);
    }
    
    public void bindPovButton(PovDirection pov, String name) {
        this.povButtons[pov.ordinal()] = name;
    }
    
    public void removePovBind(PovDirection pov) {
        this.povButtons[pov.ordinal()] = null;
    }
    
    public void KeyDownAction(Keys keys){
        int key = keys.next();
        if(isKeyDown(keys.next())) {
            Action action = this.actions.get(this.keymap.get(key));
            IsActionNull(action);
        }
    }
    
    public void IsButtonDownAction(Keys keys){
        int button = keys.next();
        if(isButtonDown(keys.next())) {
            Action action = this.actions.get(this.buttonmap.get(button));
            IsActionNull(action);
        }
    }
    
    public void controlButtonDownAction(Keys keys){
        int button = keys.next();
        if(controllerInput.isButtonDown(keys.next())) {
            Action action = this.actions.get(this.controllermap.get(button));
            IsActionNull(action);
        }
    }
    
    /**
     * Polls all input devices to see if a game action should take place
     */
    public void pollInput() {
        Keys keys = this.keymap.keys();
        while(keys.hasNext) {
        	KeyDownAction(keys);
        }
        
        keys = this.buttonmap.keys();
        while(keys.hasNext) {
        	IsButtonDownAction(keys);
        }
        
        if(this.controllerInput.isConnected()) {
            keys = this.controllermap.keys();
            while(keys.hasNext) {
            		controlButtonDownAction(keys);
            }
            
            PovDirection[] dirs = PovDirection.values();
            isPovDirectionDownAction(dirs);
        }
    }
    
    public void isPovDirectionDownAction(PovDirection[] dirs){
        for(int i = 0; i < dirs.length; i++) {
            if (controllerInput.isPovDirectionDown(dirs[i]) ) {
                String name = povButtons[i];
                if(name != null) {
                    Action action = this.actions.get(name);
                    if(action!=null) {
                        action.action();
                        break;
                    }
                }                    
            }
        }
    }
    
}
