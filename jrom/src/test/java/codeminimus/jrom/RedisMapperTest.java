package codeminimus.jrom;

import codeminimus.jrom.annotation.Key;
import codeminimus.jrom.annotation.KeyValueModel;
import codeminimus.jrom.annotation.Sequence;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.Jedis;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RedisMapperTest {
    public static final int TEST_DB_INDEX = 11;
    private RedisMapper mapper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void buildMapper() {
        JedisConnection connection = new JedisConnection(new Jedis("localhost"));
        connection.select(TEST_DB_INDEX);
        mapper = new RedisMapper(connection);
    }

    @Test
    public void crud() {
        BasicModel basicModel = new BasicModel();
        basicModel.byteField = 4;
        basicModel.byteObjectField = 5;
        basicModel.shortField = 1004;
        basicModel.shortObjectField = 1005;
        basicModel.charField = 't';
        basicModel.charObjectField = 'u';
        basicModel.intField = 1000004;
        basicModel.intObjectfield = 1000005;
        basicModel.longField = 1000000000004L;
        basicModel.longObjectField = 1000000000005L;
        basicModel.floatField = 1.4F;
        basicModel.floatObjectfield = 1.5F;
        basicModel.doubleField = 1000000000.4D;
        basicModel.doubleObjectField = 1000000000.5D;
        basicModel.stringField = "happy";
        basicModel.date = new Date();

        BasicModel save = mapper.save(basicModel);

        assertThat(save, is(basicModel));
        assertThat(save.key, notNullValue());

        BasicModel read = mapper.read(save.key, BasicModel.class);

        assertThat(read, is(save));
        assertThat(read.key, is(save.key));

        read.doubleField = 15.55D;
        read.stringField = "updated";

        BasicModel update = mapper.update(read);

        assertThat(update, is(read));
        assertThat(update.key, is(read.key));
    }

    @KeyValueModel
    public static class BasicModel {
        @Key
        @Sequence
        private Long key;
        private long longField;
        private Long longObjectField;
        private int intField;
        private Integer intObjectfield;
        private short shortField;
        private Short shortObjectField;
        private byte byteField;
        private Byte byteObjectField;
        private char charField;
        private Character charObjectField;
        private double doubleField;
        private Double doubleObjectField;
        private float floatField;
        private Float floatObjectfield;
        private String stringField;
        private Date date;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BasicModel that = (BasicModel) o;

            if (byteField != that.byteField) return false;
            if (charField != that.charField) return false;
            if (Double.compare(that.doubleField, doubleField) != 0) return false;
            if (Float.compare(that.floatField, floatField) != 0) return false;
            if (intField != that.intField) return false;
            if (longField != that.longField) return false;
            if (shortField != that.shortField) return false;
            if (byteObjectField != null ? !byteObjectField.equals(that.byteObjectField) : that.byteObjectField != null)
                return false;
            if (charObjectField != null ? !charObjectField.equals(that.charObjectField) : that.charObjectField != null)
                return false;
            if (date != null ? !date.equals(that.date) : that.date != null) return false;
            if (doubleObjectField != null ? !doubleObjectField.equals(that.doubleObjectField) : that.doubleObjectField != null)
                return false;
            if (floatObjectfield != null ? !floatObjectfield.equals(that.floatObjectfield) : that.floatObjectfield != null)
                return false;
            if (intObjectfield != null ? !intObjectfield.equals(that.intObjectfield) : that.intObjectfield != null)
                return false;
            if (longObjectField != null ? !longObjectField.equals(that.longObjectField) : that.longObjectField != null)
                return false;
            if (shortObjectField != null ? !shortObjectField.equals(that.shortObjectField) : that.shortObjectField != null)
                return false;
            return !(stringField != null ? !stringField.equals(that.stringField) : that.stringField != null);

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = (int) (longField ^ (longField >>> 32));
            result = 31 * result + (longObjectField != null ? longObjectField.hashCode() : 0);
            result = 31 * result + intField;
            result = 31 * result + (intObjectfield != null ? intObjectfield.hashCode() : 0);
            result = 31 * result + (int) shortField;
            result = 31 * result + (shortObjectField != null ? shortObjectField.hashCode() : 0);
            result = 31 * result + (int) byteField;
            result = 31 * result + (byteObjectField != null ? byteObjectField.hashCode() : 0);
            result = 31 * result + (int) charField;
            result = 31 * result + (charObjectField != null ? charObjectField.hashCode() : 0);
            temp = doubleField != +0.0d ? Double.doubleToLongBits(doubleField) : 0L;
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (doubleObjectField != null ? doubleObjectField.hashCode() : 0);
            result = 31 * result + (floatField != +0.0f ? Float.floatToIntBits(floatField) : 0);
            result = 31 * result + (floatObjectfield != null ? floatObjectfield.hashCode() : 0);
            result = 31 * result + (stringField != null ? stringField.hashCode() : 0);
            result = 31 * result + (date != null ? date.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "BasicModel{" +
                    "key=" + key +
                    ", longField=" + longField +
                    ", longObjectField=" + longObjectField +
                    ", intField=" + intField +
                    ", intObjectfield=" + intObjectfield +
                    ", shortField=" + shortField +
                    ", shortObjectField=" + shortObjectField +
                    ", byteField=" + byteField +
                    ", byteObjectField=" + byteObjectField +
                    ", charField=" + charField +
                    ", charObjectField=" + charObjectField +
                    ", doubleField=" + doubleField +
                    ", doubleObjectField=" + doubleObjectField +
                    ", floatField=" + floatField +
                    ", floatObjectfield=" + floatObjectfield +
                    ", stringField='" + stringField + '\'' +
                    ", date=" + date +
                    '}';
        }
    }
}
