package codeminimus.jrom.annotation;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AnnotationHelperTest {
    @Test
    public void testFindFieldWithAnnotation() throws Exception {
        Field fieldWithAnnotation = AnnotationHelper.HELPER.findFieldWithAnnotation(AnnotationTester.class, Key.class);

        assertThat(fieldWithAnnotation.getName(), is("stringField"));
    }

    @KeyValueModel
    public class AnnotationTester {
        @SuppressWarnings("UnusedDeclaration")
        @Key
        private String stringField;
    }
}
