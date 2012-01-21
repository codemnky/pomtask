package codeminimus.jrom;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@SuppressWarnings("UnusedDeclaration")
public class StringJedisConnection {
    private final JedisConnection connection;

    public StringJedisConnection(JedisConnection connection) {
        this.connection = connection;
    }

    public void close() throws DataAccessException {
        connection.close();
    }

    public Jedis getNativeConnection() {
        return connection.getNativeConnection();
    }

    public boolean isClosed() {
        return connection.isClosed();
    }

    public boolean isQueueing() {
        return connection.isQueueing();
    }

    public boolean isPipelined() {
        return connection.isPipelined();
    }

    public void openPipeline() {
        connection.openPipeline();
    }

    public List<Object> closePipeline() {
        return connection.closePipeline();
    }

    public List<byte[]> sort(String key, SortParameters params) {
        return connection.sort(key.getBytes(), params);
    }

    public Long sort(String key, SortParameters params, byte[] sortKey) {
        return connection.sort(key.getBytes(), params, sortKey);
    }

    public Long dbSize() {
        return connection.dbSize();
    }

    public void flushDb() {
        connection.flushDb();
    }

    public void flushAll() {
        connection.flushAll();
    }

    public void bgSave() {
        connection.bgSave();
    }

    public void bgWriteAof() {
        connection.bgWriteAof();
    }

    public void save() {
        connection.save();
    }

    public List<String> getConfig(String param) {
        return connection.getConfig(param);
    }

    public Properties info() {
        return connection.info();
    }

    public Long lastSave() {
        return connection.lastSave();
    }

    public void setConfig(String param, String value) {
        connection.setConfig(param, value);
    }

    public void resetConfigStats() {
        connection.resetConfigStats();
    }

    public void shutdown() {
        connection.shutdown();
    }

    public byte[] echo(byte[] message) {
        return connection.echo(message);
    }

    public String ping() {
        return connection.ping();
    }

    public Long del(byte[]... keys) {
        return connection.del(keys);
    }

    public void discard() {
        connection.discard();
    }

    public List<Object> exec() {
        return connection.exec();
    }

    public Boolean exists(String key) {
        return connection.exists(key.getBytes());
    }

    public Boolean expire(String key, long seconds) {
        return connection.expire(key.getBytes(), seconds);
    }

    public Boolean expireAt(String key, long unixTime) {
        return connection.expireAt(key.getBytes(), unixTime);
    }

    public Set<byte[]> keys(byte[] pattern) {
        return connection.keys(pattern);
    }

    public void multi() {
        connection.multi();
    }

    public Boolean persist(String key) {
        return connection.persist(key.getBytes());
    }

    public Boolean move(String key, int dbIndex) {
        return connection.move(key.getBytes(), dbIndex);
    }

    public byte[] randomKey() {
        return connection.randomKey();
    }

    public void rename(byte[] oldName, byte[] newName) {
        connection.rename(oldName, newName);
    }

    public Boolean renameNX(byte[] oldName, byte[] newName) {
        return connection.renameNX(oldName, newName);
    }

    public void select(int dbIndex) {
        connection.select(dbIndex);
    }

    public Long ttl(String key) {
        return connection.ttl(key.getBytes());
    }

    public DataType type(String key) {
        return connection.type(key.getBytes());
    }

    public void unwatch() {
        connection.unwatch();
    }

    public void watch(byte[]... keys) {
        connection.watch(keys);
    }

    public byte[] get(String key) {
        return connection.get(key.getBytes());
    }

    public void set(String key, byte[] value) {
        connection.set(key.getBytes(), value);
    }

    public byte[] getSet(String key, byte[] value) {
        return connection.getSet(key.getBytes(), value);
    }

    public Long append(String key, byte[] value) {
        return connection.append(key.getBytes(), value);
    }

    public List<byte[]> mGet(byte[]... keys) {
        return connection.mGet(keys);
    }

    public void mSet(Map<byte[], byte[]> tuples) {
        connection.mSet(tuples);
    }

    public void mSetNX(Map<byte[], byte[]> tuples) {
        connection.mSetNX(tuples);
    }

    public void setEx(String key, long time, byte[] value) {
        connection.setEx(key.getBytes(), time, value);
    }

    public Boolean setNX(String key, byte[] value) {
        return connection.setNX(key.getBytes(), value);
    }

    public byte[] getRange(String key, long start, long end) {
        return connection.getRange(key.getBytes(), start, end);
    }

    public Long decr(String key) {
        return connection.decr(key.getBytes());
    }

    public Long decrBy(String key, long value) {
        return connection.decrBy(key.getBytes(), value);
    }

    public Long incr(String key) {
        return connection.incr(key.getBytes());
    }

    public Long incrBy(String key, long value) {
        return connection.incrBy(key.getBytes(), value);
    }

    public Boolean getBit(String key, long offset) {
        return connection.getBit(key.getBytes(), offset);
    }

    public void setBit(String key, long offset, boolean value) {
        connection.setBit(key.getBytes(), offset, value);
    }

    public void setRange(String key, byte[] value, long start) {
        connection.setRange(key.getBytes(), value, start);
    }

    public Long strLen(String key) {
        return connection.strLen(key.getBytes());
    }

    public Long lPush(String key, byte[] value) {
        return connection.lPush(key.getBytes(), value);
    }

    public Long rPush(String key, byte[] value) {
        return connection.rPush(key.getBytes(), value);
    }

    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        return connection.bLPop(timeout, keys);
    }

    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        return connection.bRPop(timeout, keys);
    }

    public byte[] lIndex(String key, long index) {
        return connection.lIndex(key.getBytes(), index);
    }

    public Long lInsert(String key, RedisListCommands.Position where, byte[] pivot, byte[] value) {
        return connection.lInsert(key.getBytes(), where, pivot, value);
    }

    public Long lLen(String key) {
        return connection.lLen(key.getBytes());
    }

    public byte[] lPop(String key) {
        return connection.lPop(key.getBytes());
    }

    public List<byte[]> lRange(String key, long start, long end) {
        return connection.lRange(key.getBytes(), start, end);
    }

    public Long lRem(String key, long count, byte[] value) {
        return connection.lRem(key.getBytes(), count, value);
    }

    public void lSet(String key, long index, byte[] value) {
        connection.lSet(key.getBytes(), index, value);
    }

    public void lTrim(String key, long start, long end) {
        connection.lTrim(key.getBytes(), start, end);
    }

    public byte[] rPop(String key) {
        return connection.rPop(key.getBytes());
    }

    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        return connection.rPopLPush(srcKey, dstKey);
    }

    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        return connection.bRPopLPush(timeout, srcKey, dstKey);
    }

    public Long lPushX(String key, byte[] value) {
        return connection.lPushX(key.getBytes(), value);
    }

    public Long rPushX(String key, byte[] value) {
        return connection.rPushX(key.getBytes(), value);
    }

    public Boolean sAdd(String key, byte[] value) {
        return connection.sAdd(key.getBytes(), value);
    }

    public Long sCard(String key) {
        return connection.sCard(key.getBytes());
    }

    public Set<byte[]> sDiff(byte[]... keys) {
        return connection.sDiff(keys);
    }

    public void sDiffStore(byte[] destKey, byte[]... keys) {
        connection.sDiffStore(destKey, keys);
    }

    public Set<byte[]> sInter(byte[]... keys) {
        return connection.sInter(keys);
    }

    public void sInterStore(byte[] destKey, byte[]... keys) {
        connection.sInterStore(destKey, keys);
    }

    public Boolean sIsMember(String key, byte[] value) {
        return connection.sIsMember(key.getBytes(), value);
    }

    public Set<byte[]> sMembers(String key) {
        return connection.sMembers(key.getBytes());
    }

    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        return connection.sMove(srcKey, destKey, value);
    }

    public byte[] sPop(String key) {
        return connection.sPop(key.getBytes());
    }

    public byte[] sRandMember(String key) {
        return connection.sRandMember(key.getBytes());
    }

    public Boolean sRem(String key, byte[] value) {
        return connection.sRem(key.getBytes(), value);
    }

    public Set<byte[]> sUnion(byte[]... keys) {
        return connection.sUnion(keys);
    }

    public void sUnionStore(byte[] destKey, byte[]... keys) {
        connection.sUnionStore(destKey, keys);
    }

    public Boolean zAdd(String key, double score, byte[] value) {
        return connection.zAdd(key.getBytes(), score, value);
    }

    public Long zCard(String key) {
        return connection.zCard(key.getBytes());
    }

    public Long zCount(String key, double min, double max) {
        return connection.zCount(key.getBytes(), min, max);
    }

    public Double zIncrBy(String key, double increment, byte[] value) {
        return connection.zIncrBy(key.getBytes(), increment, value);
    }

    public Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        return connection.zInterStore(destKey, aggregate, weights, sets);
    }

    public Long zInterStore(byte[] destKey, byte[]... sets) {
        return connection.zInterStore(destKey, sets);
    }

    public Set<byte[]> zRange(String key, long start, long end) {
        return connection.zRange(key.getBytes(), start, end);
    }

    public Set<RedisZSetCommands.Tuple> zRangeWithScores(String key, long start, long end) {
        return connection.zRangeWithScores(key.getBytes(), start, end);
    }

    public Set<byte[]> zRangeByScore(String key, double min, double max) {
        return connection.zRangeByScore(key.getBytes(), min, max);
    }

    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(String key, double min, double max) {
        return connection.zRangeByScoreWithScores(key.getBytes(), min, max);
    }

    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(String key, long start, long end) {
        return connection.zRevRangeWithScores(key.getBytes(), start, end);
    }

    public Set<byte[]> zRangeByScore(String key, double min, double max, long offset, long count) {
        return connection.zRangeByScore(key.getBytes(), min, max, offset, count);
    }

    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return connection.zRangeByScoreWithScores(key.getBytes(), min, max, offset, count);
    }

    public Set<byte[]> zRevRangeByScore(String key, double min, double max, long offset, long count) {
        return connection.zRevRangeByScore(key.getBytes(), min, max, offset, count);
    }

    public Set<byte[]> zRevRangeByScore(String key, double min, double max) {
        return connection.zRevRangeByScore(key.getBytes(), min, max);
    }

    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return connection.zRevRangeByScoreWithScores(key.getBytes(), min, max, offset, count);
    }

    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(String key, double min, double max) {
        return connection.zRevRangeByScoreWithScores(key.getBytes(), min, max);
    }

    public Long zRank(String key, byte[] value) {
        return connection.zRank(key.getBytes(), value);
    }

    public Boolean zRem(String key, byte[] value) {
        return connection.zRem(key.getBytes(), value);
    }

    public Long zRemRange(String key, long start, long end) {
        return connection.zRemRange(key.getBytes(), start, end);
    }

    public Long zRemRangeByScore(String key, double min, double max) {
        return connection.zRemRangeByScore(key.getBytes(), min, max);
    }

    public Set<byte[]> zRevRange(String key, long start, long end) {
        return connection.zRevRange(key.getBytes(), start, end);
    }

    public Long zRevRank(String key, byte[] value) {
        return connection.zRevRank(key.getBytes(), value);
    }

    public Double zScore(String key, byte[] value) {
        return connection.zScore(key.getBytes(), value);
    }

    public Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        return connection.zUnionStore(destKey, aggregate, weights, sets);
    }

    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        return connection.zUnionStore(destKey, sets);
    }

    public Boolean hSet(String key, String field, String value) {
        return connection.hSet(key.getBytes(), field.getBytes(), value.getBytes());
    }

    public Boolean hSetNX(String key, byte[] field, byte[] value) {
        return connection.hSetNX(key.getBytes(), field, value);
    }

    public Boolean hDel(String key, String field) {
        return connection.hDel(key.getBytes(), field.getBytes());
    }

    public Boolean hExists(String key, String field) {
        return connection.hExists(key.getBytes(), field.getBytes());
    }

    public String hGet(String key, String field) {
        return new String(connection.hGet(key.getBytes(), field.getBytes()));
    }

    public Map<byte[], byte[]> hGetAll(String key) {
        return connection.hGetAll(key.getBytes());
    }

    public Long hIncrBy(String key, byte[] field, long delta) {
        return connection.hIncrBy(key.getBytes(), field, delta);
    }

    public Set<byte[]> hKeys(String key) {
        return connection.hKeys(key.getBytes());
    }

    public Long hLen(String key) {
        return connection.hLen(key.getBytes());
    }

    public List<byte[]> hMGet(String key, byte[]... fields) {
        return connection.hMGet(key.getBytes(), fields);
    }

    public void hMSet(String key, Map<byte[], byte[]> tuple) {
        connection.hMSet(key.getBytes(), tuple);
    }

    public List<byte[]> hVals(String key) {
        return connection.hVals(key.getBytes());
    }

    public Long publish(byte[] channel, byte[] message) {
        return connection.publish(channel, message);
    }

    public Subscription getSubscription() {
        return connection.getSubscription();
    }

    public boolean isSubscribed() {
        return connection.isSubscribed();
    }

    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        connection.pSubscribe(listener, patterns);
    }

    public void subscribe(MessageListener listener, byte[]... channels) {
        connection.subscribe(listener, channels);
    }
}
