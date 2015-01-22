package cn.rfidcn.activitylog.state.hbase;

import java.util.Map;

import org.apache.log4j.Logger;

import storm.trident.state.State;
import storm.trident.state.StateFactory;
import storm.trident.state.StateType;
import storm.trident.state.map.CachedMap;
import storm.trident.state.map.MapState;
import storm.trident.state.map.NonTransactionalMap;
import storm.trident.state.map.OpaqueMap;
import storm.trident.state.map.SnapshottableMap;
import storm.trident.state.map.TransactionalMap;
import backtype.storm.task.IMetricsContext;
import backtype.storm.tuple.Values;

/**
 * Factory for creating {@link HBaseAggregateState} objects for Trident
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class HBaseAggregateFactory implements StateFactory {
  private static final Logger LOG = Logger.getLogger(HBaseAggregateFactory.class);
  private StateType type;
  private HbaseTridentConfig config;

  /**
   * @param config The {@link HbaseTridentConfig}
   * @param type The {@link StateType}
   */
  public HBaseAggregateFactory(final HbaseTridentConfig config, final StateType type) {
    this.config = config;
    this.type = type;

    if (config.getStateSerializer() == null) {
      config.setStateSerializer(HbaseTridentConfig.DEFAULT_SERIALZERS.get(type));
      if (config.getStateSerializer() == null) {
        throw new RuntimeException("Unable to find serializer for state type: " + type);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("Setting default serializer: " + config.getStateSerializer().getClass().getName());
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public State makeState(Map conf, IMetricsContext metrics, int partitionIndex, int numPartitions) {
    HBaseAggregateState state = new HBaseAggregateState(config);
    CachedMap c = new CachedMap(state, config.getStateCacheSize());

    MapState ms;
    if (type == StateType.NON_TRANSACTIONAL) {
      ms = NonTransactionalMap.build(c);
    } else if (type == StateType.OPAQUE) {
      ms = OpaqueMap.build(c);
    } else if (type == StateType.TRANSACTIONAL) {
      ms = TransactionalMap.build(c);
    } else {
      throw new RuntimeException("Unknown state type: " + type);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Creating new HBaseState: " + type);
    }

    return new SnapshottableMap(ms, new Values("$GLOBAL$"));
  }
}
