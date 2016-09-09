package net.avicus.tutorial.api;

import com.google.common.base.Preconditions;
import net.avicus.tutorial.plugin.TutorialPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * A tutorial that a player is viewing.
 */
public class ActiveTutorial {
    private final Tutorial tutorial;
    private final Player player;
    private final ActionResult commandResult;
    private final ActionResult chatResult;
    private final ActiveTutorialListener listener;

    private PlayerState savedState;
    private int currentStep;

    public ActiveTutorial(Tutorial tutorial, Player player, ActionResult commandResult, ActionResult chatResult) {
        this.tutorial = tutorial;
        this.player = player;
        this.commandResult = commandResult;
        this.chatResult = chatResult;
        this.listener = new ActiveTutorialListener(this);
    }

    /**
     * Starts the tutorial.
     */
    public void start() {
        // Save current state
        this.savedState = PlayerState.of(this.player);
        Bukkit.getServer().getPluginManager().registerEvents(this.listener, TutorialPlugin.getInstance());
        setStep(0);
    }

    /**
     * Stops the tutorial.
     * @param restoreState True to restore the state of which the player began the tutorial.
     */
    public void stop(boolean restoreState) {
        Preconditions.checkArgument(isStarted(), "Tutorial not started.");

        // Hide the last state
        hideState(this.currentStep);

        // Apply original state
        if (restoreState) {
            this.savedState.apply(this.player);
        }
        this.savedState = null;

        HandlerList.unregisterAll(this.listener);
        this.currentStep = 0;
    }

    /**
     * Check if the tutorial has started.
     * @return
     */
    public boolean isStarted() {
        return this.savedState != null;
    }

    /**
     * Set the tutorial step.
     * @param index
     */
    public void setStep(int index) {
        Preconditions.checkArgument(isStarted(), "Tutorial not started.");
        Preconditions.checkArgument(this.tutorial.hasStep(index), "Step does not exist.", index);

        this.currentStep = index;

        // Hide previous step, for all but first step.
        if (this.currentStep > 0)
        hideState(this.currentStep - 1);

        // Show the current step.
        getCurrentStep().show(this.player);
    }

    private void hideState(int state) {
        this.tutorial.getSteps().get(state).hide(this.player);
    }

    /**
     * Get the current tutorial step.
     * @return
     */
    public TutorialStep getCurrentStep() {
        return this.tutorial.getSteps().get(this.currentStep);
    }

    /**
     * Get the chat setting.
     * @return
     */
    public ActionResult getCommandResult() {
        return this.commandResult;
    }

    /**
     * Get the chat setting.
     * @return
     */
    public ActionResult getChatResult() {
        return this.chatResult;
    }

    /**
     * Get the player.
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }
}
