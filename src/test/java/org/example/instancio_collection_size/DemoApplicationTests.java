package org.example.instancio_collection_size;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.collection;
import static org.instancio.Select.types;
import static org.instancio.settings.Keys.*;

class DemoApplicationTests {

    static final class SampleClass {
        @NotEmpty
        private Collection<String> notEmptyCollection;

        @Size(min = 1)
        private Collection<String> collectionWithMinSize;

        private Collection<String> collection;

        public Collection<String> getNotEmptyCollection() {
            return notEmptyCollection;
        }

        public Collection<String> getCollectionWithMinSize() {
            return collectionWithMinSize;
        }

        public Collection<String> getCollection() {
            return collection;
        }
    }

    @Test
    void collectionSizeValidationEnabledTest() {
        assertThat(Instancio.of(SampleClass.class)
                .withSetting(COLLECTION_MIN_SIZE, 1)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .withSetting(BEAN_VALIDATION_ENABLED, true)
                .create())
                .extracting(SampleClass::getNotEmptyCollection, SampleClass::getCollectionWithMinSize, SampleClass::getCollection)
                .allSatisfy(collection -> assertThat(collection)
                        .asInstanceOf(collection(String.class))
                        .hasSize(1));
    }

    @Test
    void collectionSizeValidationDisabledTest() {
        assertThat(Instancio.of(SampleClass.class)
                .withSetting(COLLECTION_MIN_SIZE, 1)
                .withSetting(COLLECTION_MAX_SIZE, 1)
                .withSetting(BEAN_VALIDATION_ENABLED, false)
                .create())
                .extracting(SampleClass::getNotEmptyCollection, SampleClass::getCollectionWithMinSize, SampleClass::getCollection)
                .allSatisfy(collection -> assertThat(collection)
                        .asInstanceOf(collection(String.class))
                        .hasSize(1));
    }

    @Test
    void collectionSizeInGeneratorValidationEnabledTest() {
        assertThat(Instancio.of(SampleClass.class)
                .withSetting(BEAN_VALIDATION_ENABLED, true)
                .generate(types().of(Collection.class), gen -> gen.collection().size(1))
                .create())
                .extracting(SampleClass::getNotEmptyCollection, SampleClass::getCollectionWithMinSize, SampleClass::getCollection)
                .allSatisfy(collection -> assertThat(collection)
                        .asInstanceOf(collection(String.class))
                        .hasSize(1));
    }

    @Test
    void collectionSizeInGeneratorValidationDisabledTest() {
        assertThat(Instancio.of(SampleClass.class)
                .withSetting(BEAN_VALIDATION_ENABLED, false)
                .generate(types().of(Collection.class), gen -> gen.collection().size(1))
                .create())
                .extracting(SampleClass::getNotEmptyCollection, SampleClass::getCollectionWithMinSize, SampleClass::getCollection)
                .allSatisfy(collection -> assertThat(collection)
                        .asInstanceOf(collection(String.class))
                        .hasSize(1));
    }
}
