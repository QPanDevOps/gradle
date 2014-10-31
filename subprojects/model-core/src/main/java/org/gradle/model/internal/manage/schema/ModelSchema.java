/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.model.internal.manage.schema;

import com.google.common.collect.ImmutableSortedMap;
import net.jcip.annotations.ThreadSafe;
import org.gradle.api.Transformer;
import org.gradle.model.internal.core.ModelType;

import java.util.Collections;

@ThreadSafe
public class ModelSchema<T> {

    private final ModelType<T> type;
    private final ImmutableSortedMap<String, ModelProperty<?>> properties;
    private final Transformer<T, ModelSchema<T>> instantiator;

    public ModelSchema(ModelType<T> type, Transformer<T, ModelSchema<T>> elementInstantiator) {
        this(type, Collections.<ModelProperty<?>>emptyList(), elementInstantiator);
    }

    public ModelSchema(ModelType<T> type, Iterable<ModelProperty<?>> properties, Transformer<T, ModelSchema<T>> instantiator) {
        this.type = type;
        this.instantiator = instantiator;

        ImmutableSortedMap.Builder<String, ModelProperty<?>> builder = ImmutableSortedMap.naturalOrder();
        for (ModelProperty<?> property : properties) {
            builder.put(property.getName(), property);
        }
        this.properties = builder.build();
    }

    public ModelType<T> getType() {
        return type;
    }

    public ImmutableSortedMap<String, ModelProperty<?>> getProperties() {
        return properties;
    }

    public T createInstance() {
        return instantiator.transform(this);
    }
}
