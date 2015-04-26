package cz.bachelor.inspection;

import cz.bachelor.metamodel.Rule;
import cz.bachelor.metamodel.condition.Condition;
import cz.bachelor.metamodel.condition.Eval;
import cz.bachelor.metamodel.condition.Group;
import cz.bachelor.metamodel.condition.Pattern;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.drools.compiler.rule.builder.MVELConstraintBuilder;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.rule.EvalCondition;
import org.drools.core.rule.Function;
import org.drools.core.rule.GroupElement;
import org.drools.core.rule.RuleConditionElement;
import org.drools.core.rule.constraint.MvelConstraint;
import org.drools.core.spi.Constraint;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.KiePackage;
import org.kie.api.runtime.KieContainer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Rule inspectRule(RuleImpl droolsRule) {
        Rule rule = new Rule();
        rule.setName(droolsRule.getName());
        rule.setPckg(droolsRule.getPackageName());
        // set globals and functions
//        KieServices.Factory.get().getKieClasspathContainer().getKieBase("userbase");
        try {
            KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();
            Field kBases = kieContainer.getClass().getDeclaredField("kBases");
            kBases.setAccessible(true);
            for (Object o : ((Map) kBases.get(kieContainer)).values()) {
                KnowledgeBaseImpl base = (KnowledgeBaseImpl) o;
                InternalKnowledgePackage knowledgePackage = base.getPackage(droolsRule.getPackageName());
                if (knowledgePackage != null) {
                    for (Map.Entry<String, String> entry : knowledgePackage.getGlobals().entrySet()) {
                        rule.getGlobals().put(entry.getKey(), entry.getValue());
                    }
                    for (Function function : knowledgePackage.getFunctions().values()) {
                        // toDo: test functionality
                        rule.getFunctions().add(function.getName());
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (RuleConditionElement conditionElement : droolsRule.getLhs().getChildren()) {
            //toDo: create a factory for this
            Condition condition;
            if (conditionElement instanceof org.drools.core.rule.Pattern) {
                condition = new Pattern();
                for (Constraint constraint : ((org.drools.core.rule.Pattern) conditionElement).getConstraints()) {
                    if (constraint instanceof MvelConstraint) {
                        ((Pattern) condition).getConstraints().add(((MvelConstraint) constraint).getExpression());
                    } else {
                        throw new IllegalArgumentException("Constraint type not supported: " +
                                constraint.getClass().getName());
                    }
                }
            } else if (conditionElement instanceof GroupElement) {
                condition = new Group();
            } else if (conditionElement instanceof EvalCondition) {
                condition = new Eval();
                ((Eval) condition).setConstraint(((EvalCondition) conditionElement).getEvalExpression().toString());
            } else {
                throw new IllegalArgumentException("Condition class not supported: " +
                        conditionElement.getClass().getName());
            }
            rule.getConditions().add(condition);
            //toDo: declarations - in rules, conditions, or both?
        }
        return rule;
    }

    /**
     * Inspects all rules in given {@link KiePackage} and returns a set of {@link Rule}s created from Drools
     * {@link org.kie.api.definition.rule.Rule}s in the package (one package == single *.drl file)
     *
     * @param kiePackage a package to inspect rules from
     * @return set of created {@link Rule}s
     */
    public Set<Rule> inspectPackage(KiePackage kiePackage) {
        Set<Rule> rules = new HashSet<>();
        if (kiePackage.getRules().size() > 0) {
            for (org.kie.api.definition.rule.Rule rule : kiePackage.getRules()) {
                rules.add(inspectRule((RuleImpl) rule));
            }
        }
        return rules;
    }

    /**
     * Inspects packages in given {@link KieBase} and returns a map, where key is package name and
     * value is a set of {@link Rule}s from that package.
     *
     * @param kieBase
     * @return
     */
    public Map<String, Set<Rule>> inspectBase(KieBase kieBase) {
        Map<String, Set<Rule>> rulesMap = new HashMap<>();
        if (kieBase.getKiePackages().size() > 0) {
            for (KiePackage kiePackage : kieBase.getKiePackages()) {
                Set<Rule> rules = inspectPackage(kiePackage);
                if (rules.size() > 0) {
                    rulesMap.put(kiePackage.getName(), rules);
                }
            }
        }
        return rulesMap;
    }
}
