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

    @Test
    public void findModelName() {
        assertThat(AnnotationHelper.HELPER.findModelName(AnnotationTester.class), is("annotationTester"));
    }

    @Test
    public void findModelName_NameAttribute() {
        assertThat(AnnotationHelper.HELPER.findModelName(NameAnnotationTester.class), is("happy"));
    }

    @KeyValueModel
    public class AnnotationTester {
        @SuppressWarnings("UnusedDeclaration")
        @Key
        private String stringField;
    }

    @KeyValueModel(name = "happy")
    public class NameAnnotationTester {
    }
}
