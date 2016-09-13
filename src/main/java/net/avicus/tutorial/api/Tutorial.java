package net.avicus.tutorial.api;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Tutorial {
    private final Optional<String> id;
    private final String name;
    private final List<? extends TutorialStep> steps;

    public Tutorial(Optional<String> id, String name, List<? extends TutorialStep> steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public Tutorial(Optional<String> id, String name, SimpleTutorialStep... steps) {
        this(id, name, Arrays.asList(steps));
    }

    public ActiveTutorial create(Player player, ActionResult commandResult, ActionResult chatResult) {
        return new ActiveTutorial(this, player, commandResult, chatResult);
    }

    /**
     * The id of this tutorial, which is optional.
     * @return
     */
    public Optional<String> getId() {
        return this.id;
    }

    /**
     * The name of this tutorial.
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Check if this tutorial contains a certain step.
     * @param index
     * @return
     */
    public boolean hasStep(int index) {
        return index >= 0 && index < getSteps().size();
    }

    /**
     * Get the collection of steps that this tutorial contains.
     * @return
     */
    public List<? extends TutorialStep> getSteps() {
        return this.steps;
    }
}
