/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jdave.wicket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.hamcrest.Matcher;

/**
 * @author Joni Freeman
 */
public class Selector {
    public <T> T first(MarkupContainer root, Class<T> componentType, Matcher<?> matcher) {
        List<T> firstMatch = select(root, componentType, matcher, IVisitor.STOP_TRAVERSAL);
        return firstMatch.isEmpty() ? null : firstMatch.get(0);
    }

    public <T> Collection<T> all(WebMarkupContainer root, Class<T> componentType, final Matcher<?> matcher) {
        return select(root, componentType, matcher, IVisitor.CONTINUE_TRAVERSAL);
    }

    private <T> List<T> select(MarkupContainer root, Class<T> componentType, final Matcher<?> matcher, final Object actionOnMatch) {
        final List<T> matches = new ArrayList<T>();
        root.visitChildren(componentType, new IVisitor() {
            @SuppressWarnings("unchecked")
            public Object component(Component component) {
                if (matcher.matches(component.getModelObject())) {
                    matches.add((T) component);
                    return actionOnMatch;
                }
                return IVisitor.CONTINUE_TRAVERSAL;
            }
        });
        return matches;
    }
}
