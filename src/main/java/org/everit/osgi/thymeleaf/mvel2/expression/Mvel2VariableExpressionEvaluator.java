/**
 * This file is part of Everit - Thymeleaf MVEL2 Extension.
 *
 * Everit - Thymeleaf MVEL2 Extension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Thymeleaf MVEL2 Extension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Thymeleaf MVEL2 Extension.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.osgi.thymeleaf.mvel2.expression;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.ICache;
import org.thymeleaf.cache.ICacheManager;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IContextVariableRestriction;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.context.VariablesMap;
import org.thymeleaf.expression.ExpressionEvaluatorObjects;
import org.thymeleaf.standard.expression.IStandardConversionService;
import org.thymeleaf.standard.expression.IStandardVariableExpressionEvaluator;
import org.thymeleaf.standard.expression.StandardExpressionExecutionContext;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.expression.StandardVariableRestrictions;

public class Mvel2VariableExpressionEvaluator implements IStandardVariableExpressionEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(Mvel2VariableExpressionEvaluator.class);

    private static final String MVEL2_CACHE_PREFIX = "{mvel2}";

    /*
     * Meant to be overwritten
     */
    protected Map<String, Object> computeAdditionalContextVariables(
            final IProcessingContext processingContext) {
        return Collections.emptyMap();
    }

    @Override
    public Object evaluate(Configuration configuration, IProcessingContext processingContext, String expression,
            StandardExpressionExecutionContext expContext, boolean useSelectionAsRoot) {

        if (logger.isTraceEnabled()) {
            logger.trace("[THYMELEAF][{}] MVEL2 expression: evaluating expression \"{}\" on target",
                    TemplateEngine.threadIndex(), expression);
        }

        Object compiledExpression = null;
        ICache<String, Object> cache = null;

        if (configuration != null) {
            final ICacheManager cacheManager = configuration.getCacheManager();
            if (cacheManager != null) {
                cache = cacheManager.getExpressionCache();
                if (cache != null) {
                    compiledExpression = cache.get(MVEL2_CACHE_PREFIX + expression);
                }
            }
        }

        if (compiledExpression == null) {
            compiledExpression = MVEL.compileExpression(expression);
            if (cache != null && null != compiledExpression) {
                cache.put(MVEL2_CACHE_PREFIX + expression, compiledExpression);
            }
        }

        final Map<String, Object> contextVariables = processingContext.getExpressionObjects();

        final Map<String, Object> additionalContextVariables = computeAdditionalContextVariables(processingContext);
        if (additionalContextVariables != null) {
            contextVariables.putAll(additionalContextVariables);
        }

        final Object evaluationRoot =
                (useSelectionAsRoot ?
                        processingContext.getExpressionSelectionEvaluationRoot() :
                        processingContext.getExpressionEvaluationRoot());

        setVariableRestrictions(expContext, evaluationRoot, contextVariables);

        final Object result = MVEL.executeExpression(compiledExpression, evaluationRoot, contextVariables);

        if (!expContext.getPerformTypeConversion()) {
            return result;
        }

        final IStandardConversionService conversionService =
                StandardExpressions.getConversionService(configuration);

        return conversionService.convert(configuration, processingContext, result, String.class);
    }

    protected void setVariableRestrictions(final StandardExpressionExecutionContext expContext,
            final Object evaluationRoot, final Map<String, Object> contextVariables) {

        final List<IContextVariableRestriction> restrictions =
                (expContext.getForbidRequestParameters() ?
                        StandardVariableRestrictions.REQUEST_PARAMETERS_FORBIDDEN : null);

        final Object context = contextVariables.get(ExpressionEvaluatorObjects.CONTEXT_VARIABLE_NAME);
        if (context != null && context instanceof IContext) {
            final VariablesMap<?, ?> variablesMap = ((IContext) context).getVariables();
            variablesMap.setRestrictions(restrictions);
        }
        if (evaluationRoot != null && evaluationRoot instanceof VariablesMap<?, ?>) {
            ((VariablesMap<?, ?>) evaluationRoot).setRestrictions(restrictions);
        }
    }

}
