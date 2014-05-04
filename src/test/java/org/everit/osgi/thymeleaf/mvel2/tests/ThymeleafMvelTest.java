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
package org.everit.osgi.thymeleaf.mvel2.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.everit.osgi.thymeleaf.mvel2.expression.Mvel2VariableExpressionEvaluator;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafMvelTest {

    @Test
    public void testSimpleTemplate() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
        StandardDialect standardDialect = new StandardDialect();
        standardDialect.setVariableExpressionEvaluator(new Mvel2VariableExpressionEvaluator());

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("now", new Date());
        vars.put("sentence", "Hello world");
        vars.put("someValue", "Some value");
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO("John", 30));
        users.add(new UserDTO("Garfield", 5));
        vars.put("users", users);

        IContext context = new Context(Locale.ENGLISH, vars);
        templateEngine.setDialect(standardDialect);

        String processedTemplate = templateEngine.process("META-INF/test/test1.html", context);
        System.out.println(processedTemplate);
    }
}
