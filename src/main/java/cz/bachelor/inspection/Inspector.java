package cz.bachelor.inspection;

import cz.bachelor.metamodel.Rule;
import cz.bachelor.metamodel.condition.Condition;
import cz.bachelor.metamodel.condition.Eval;
import cz.bachelor.metamodel.condition.Group;
import cz.bachelor.metamodel.condition.Pattern;
import org.drools.compiler.rule.builder.MVELConstraintBuilder;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.rule.EvalCondition;
import org.drools.core.rule.GroupElement;
import org.drools.core.rule.RuleConditionElement;
import org.drools.core.rule.constraint.MvelConstraint;
import org.drools.core.spi.Constraint;

/**
 * Inspects Drools rules and creates {@link cz.bachelor.metamodel.Rule} objects from them.
 */
public class Inspector {

    /**
     * Inspects a specific Drools rule and creates a {@link Rule} object from it.
     *
     * @param droolsRule
     * @return
     */
    //toDo: RuleImpl, or Rule interface?
    public Rule inspectRule(RuleImpl droolsRule) {
        Rule rule = new Rule();
        rule.setName(droolsRule.getName());
        rule.setPckg(droolsRule.getPackageName());
        //toDo: include functions and global variables
        for (RuleConditionElement conditionElement : droolsRule.getLhs().getChildren()) {
            //toDo: create a factory for this
            Condition condition;
            if (conditionElement instanceof org.drools.core.rule.Pattern) {
                condition = new Pattern();
                for (Constraint constraint : ((org.drools.core.rule.Pattern) conditionElement).getConstraints()) {
                    if (constraint instanceof MvelConstraint) {
                        ((Pattern) condition).getConstraints().add(((MvelConstraint)constraint).getExpression());
                    } else {
                        throw new IllegalArgumentException("Constraint type not supported: " +
                                constraint.getClass().getName());
                    }
                }
            } else if (conditionElement instanceof GroupElement) {
                condition = new Group();
            } else if (conditionElement instanceof EvalCondition) {
                condition = new Eval();
                ((Eval)condition).setConstraint(((EvalCondition) conditionElement).getEvalExpression().toString());
            } else {
                throw new IllegalArgumentException("Condition class not supported: " +
                        conditionElement.getClass().getName());
            }
            rule.getConditions().add(condition);
            //toDo: declarations - in rules, conditions, or both?
        }


        return rule;
    }
}
